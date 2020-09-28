package assignment1;

import java.util.Arrays;
import cnss.simulator.*;
import cnss.lib.*;

public class FilesSender extends AbstractApplicationAlgorithm {

	public static int BLOCKSIZE = 10000;            // 10000*8 = 80000 bits
	public static int TOTAL_PACKETSIZE = BLOCKSIZE+Packet.HEADERSIZE;


	int totSent = 0;
	int totalBlocks = 0;
	int filesize = 0;

	public FilesSender() {
		super(true, "files-sender");
	}

	public int initialise(int now, int node_id, Node self, String[] args) {
		super.initialise(now, node_id, self, args);
		log(0, "starting");
		if ( args.length != 1 ) {
			System.err.println("files-sender: missing argument time "+now+"\n\n");
			System.exit(-1);
		}
		totalBlocks = Integer.parseInt(args[0]);
		for ( int i = 1; i <= totalBlocks; i++ ) {
			self.send( self.createDataPacket( 1, new byte[BLOCKSIZE]));
			log(now, "sent packet of size "+TOTAL_PACKETSIZE+" n. "+i);
		}
		self.set_timeout(60000); // 60 seconds later
		return 0;	
	}

	public void on_timeout(int now) {
		self.send( self.createDataPacket( 1, new byte[TOTAL_PACKETSIZE*totalBlocks]));
		log(now, "sent packet of size: "+TOTAL_PACKETSIZE*totalBlocks+" n. "+(totalBlocks+1));
	}

	public void on_receive(int now, DataPacket p) {
		log(now, "received ack "+p+ " w/ payload "+new String(p.getPayload()));
	}

	public void showState(int now) {
		System.out.println(name + " sent " + totSent + " packets with blocks");
	}

} 