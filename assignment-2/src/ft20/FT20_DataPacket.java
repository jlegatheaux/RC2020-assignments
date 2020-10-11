package ft20;

public class FT20_DataPacket extends FT20Packet {
	public final int seqN, timestamp;
	public final byte[] block;

	FT20_DataPacket(byte[] payload) {
		super(payload);
		this.timestamp = super.getInt();
		this.seqN = super.getInt();
		this.block = super.getBytes();
	}

	public FT20_DataPacket(int seqN, int timestamp, byte[] data, int datalen) {
		super(DATA);
		super.putInt(timestamp).putInt(seqN).putBytes(data, datalen);
		this.timestamp = timestamp;
		this.seqN = seqN;
		this.block = data;
	}

	public FT20_DataPacket(int seqN, int timestamp, byte[] data) {
		this(seqN, timestamp, data, data.length);
	}
	
	public String toString() {
		return String.format("DATA<%d, %d, len: %d>", seqN, timestamp, block.length);
	}

}