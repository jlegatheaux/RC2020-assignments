
import java.io.*;
import java.util.*;

import cnss.simulator.*;
import ft20.*;

public class FT20Server extends FT20AbstractApplication implements FT20_PacketHandler {

	private int windowSize; // by default in blocks

	private SortedMap<Integer, byte[]> window = new TreeMap<>();

	private int nextSeqN;
	private String filename;
	private FileOutputStream fos;

	public FT20Server() {
		super(true, "   FT20-Server");
	}

	@Override
	public int initialise(int now, int nodeId, Node self, String[] args) {
		super.initialise(now, nodeId, self, args, this);
		if( args.length != 1 ) {
			System.err.println( this.getClass().getSimpleName() + " missing windowSize argument [in config file]");
			System.exit(-1);
		}
		this.windowSize = Integer.parseInt(args[0]);
		this.fos = null;
		this.nextSeqN = 0;
		return 0;
	}

	@Override
	public void on_receive_upload(int now, int client, FT20_UploadPacket upload) {
		if (nextSeqN <= 1) {
			super.sendPacket(now, client, new FT20_AckPacket(0, 0, upload.timestamp));
			nextSeqN = 1;
			window.clear();
			filename = upload.filename;
		} else
			super.sendPacket(now, client,
					new FT20_ErrorPacket("Unexpected packet type...[Already initiated a transfer...]"));
	}

	@Override
	public void on_receive_data(int now, int client, FT20_DataPacket data) {
		
		// outside the window.
		if (data.seqN < nextSeqN || data.seqN > nextSeqN + windowSize)
			super.sendPacket(now, client, new FT20_AckPacket(nextSeqN - 1, -1, data.timestamp));
		else {
			
			window.putIfAbsent(data.seqN, data.block);
			
			//try to slide window and flush to disk.
			byte[] block;
			while ((block = window.remove(nextSeqN)) != null) {
				writeBlockToFile(block);
				nextSeqN += 1;
			}
			super.sendPacket(now, client, new FT20_AckPacket(nextSeqN - 1, data.seqN, data.timestamp));
		}
	}

	@Override
	public void on_receive_fin(int now, int client, FT20_FinPacket fin) {
		if (window.isEmpty() && nextSeqN == fin.seqN)
			super.printReport( now );

		super.sendPacket(now, client, new FT20_AckPacket(fin.seqN, fin.seqN, fin.timestamp));
	}

	private void writeBlockToFile(byte[] data) {
		try {
			if (fos == null)
				fos = new FileOutputStream("copy-of-" + filename);
			fos.write(data);
		} catch (Exception x) {
			System.err.println("FATAL ERROR: " + x.getMessage());
			System.exit(-1);
		}
	}
}
