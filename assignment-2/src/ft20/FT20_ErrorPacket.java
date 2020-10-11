package ft20;

public class FT20_ErrorPacket extends FT20Packet {
	public final String error;

	FT20_ErrorPacket(byte[] payload) {
		super(payload);
		this.error = super.getString();
	}

	public FT20_ErrorPacket(String error) {
		super(ERROR);
		super.putString(error);
		this.error = error;
	}
	
	public String toString() {
		return String.format("ERROR<%s", error);
	}
}