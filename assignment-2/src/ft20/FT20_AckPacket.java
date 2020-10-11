package ft20;

public class FT20_AckPacket extends FT20Packet {
	public final int cSeqN, sSeqN, timestamp;

	FT20_AckPacket(byte[] payload) {
		super( payload );
		this.timestamp = super.getInt();
		this.cSeqN = super.getInt();
		this.sSeqN = super.getInt();
	}

	public FT20_AckPacket(int cSeqN, int sSeqN, int timestamp) {
		super( ACK );
		super.putInt(timestamp).putInt(cSeqN).putInt(sSeqN);
		this.cSeqN = cSeqN;
		this.sSeqN = sSeqN;
		this.timestamp = timestamp;
	}
	
	
	public String toString() {
		return String.format("ACK<%d, %d, %d>", cSeqN, sSeqN, timestamp);
	}
}