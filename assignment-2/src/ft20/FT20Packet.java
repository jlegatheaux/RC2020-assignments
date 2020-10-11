package ft20;

import java.nio.ByteBuffer;

public class FT20Packet {

	public static final byte UPLOAD = 1;
	public static final byte ERROR = 2;
	public static final byte DATA = 3;
	public static final byte ACK = 4;
	public static final byte FIN = 5;

	static final int MAX_FT20_PACKET_SIZE = (1 << 16); // 64KB

	private ByteBuffer bb;

	public final byte opcode;

	/**
	 * Constructor for creating a new FT20Packet with the given opcode
	 * 
	 **/
	protected FT20Packet(byte opcode) {
		this.opcode = (byte) opcode;
		bb = ByteBuffer.allocate(MAX_FT20_PACKET_SIZE);
		bb.put(opcode);
	}

	/**
	 * Constructor for decoding a byte array as a FT20Packet
	 * 
	 **/
	protected FT20Packet(byte[] payload) {
		this.bb = ByteBuffer.wrap(payload);
		this.opcode = bb.get();
	}

	public static  FT20Packet decodeFromBytes(byte[] payload) {
		// peek the opcode to decode the packet...
		int opcode = payload[0];
		
		switch (opcode) {
		case UPLOAD:
			return new FT20_UploadPacket(payload);
		case DATA:
			return new FT20_DataPacket(payload);
		case ACK:
			return new FT20_AckPacket(payload);
		case FIN:
			return new FT20_FinPacket(payload);
		case ERROR:
			return new FT20_ErrorPacket(payload);
		default:
			System.err.println("FATAL: Unknown packet opcode....");
			System.exit(-1);
		}
		return null;
	}

	public byte[] encodeToBytes() {
		byte[] res = new byte[bb.position()];
		System.arraycopy(bb.array(), 0, res, 0, res.length);
		return res;
	}

	/********************
	 * PUTS - adds info to packet current position (starts at 0)
	 **********************/

	/**
	 * Appends an int (4 bytes, in net byte order) to the FT20Packet
	 * 
	 * @param i int to append
	 * @return this packet
	 */
	FT20Packet putInt(int i) {
		bb.putInt(i);
		return this;
	}

	/**
	 * Appends a Java String to the FT20Packet [does not include any terminator!]
	 * 
	 * @param s string to append
	 * @return this packet
	 */
	FT20Packet putString(String s) {
		bb.put(s.getBytes());
		return this;
	}

	/**
	 * Appends length bytes from the byte array to the FT20Packet
	 * 
	 * @param block  byte array from were to copy
	 * @param length numb of bytes to append
	 * @return this packet
	 */
	FT20Packet putBytes(byte[] block, int length) {
		bb.put(block, 0, length);
		return this;
	}

	/********************
	 * GETS - gets info from packet current position (starts at 0)
	 **********************/

	/**
	 * Gets an int (4 bytes) stored in net byte order (Big Endian)
	 * 
	 * @return int with value
	 */
	int getInt() {
		return bb.getInt();
	}

	/**
	 * Gets a Java String (from current position until end)
	 * 
	 * @return String
	 */
	String getString() {
		byte[] b = new byte[bb.remaining()];
		bb.get(b);
		return new String(b);
	}

	/**
	 * Gets a byte array (from current position until end)
	 * 
	 * @return byte[]
	 */
	byte[] getBytes() {
		byte[] b = new byte[bb.remaining()];
		bb.get(b);
		return b;
	}
}
