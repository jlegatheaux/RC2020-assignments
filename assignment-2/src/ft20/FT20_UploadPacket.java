package ft20;

public class FT20_UploadPacket extends FT20Packet {
	public final int timestamp;
	public final String filename;

	FT20_UploadPacket(byte[] payload) {
		super(payload);
		this.timestamp = super.getInt();
		this.filename = super.getString();
	}

	public FT20_UploadPacket(String filename, int timestamp) {
		super(UPLOAD);
		this.timestamp = timestamp;
		this.filename = filename;
		super.putInt(timestamp).putString(filename);
	}
	
	public String toString() {
		return String.format("UPLOAD<%s, %d>", filename, timestamp);
	}
}
