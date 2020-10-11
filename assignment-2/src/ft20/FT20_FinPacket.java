package ft20;

public class FT20_FinPacket extends FT20Packet {
	public final int seqN, timestamp;

	FT20_FinPacket(byte[] payload) {
		super(payload);
		this.seqN = super.getInt();
		this.timestamp = super.getInt();
	}

	public FT20_FinPacket(int seqN, int timestamp) {
		super(FIN);
		super.putInt(seqN).putInt(timestamp);
		this.seqN = seqN;
		this.timestamp = timestamp;
	}
	
	
	public String toString() {
		return String.format("FIN<%d, %d>", seqN, timestamp);
	}
}