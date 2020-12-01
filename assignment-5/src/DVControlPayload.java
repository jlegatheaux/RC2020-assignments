
import java.nio.ByteBuffer;

public class DVControlPayload {

	private ByteBuffer bb;
	
	/**
	* Constructor for creating a new DVControlPayload containing a distance-vector
	* 
	* @ param tot the number of announcements contained in the DVector to send
	* @ param announcements - an array with 2 integers per announcement
	* that is, a pair (destination, metric) per announcement
	* 
	**/
	public DVControlPayload (int tot, int[] announcements) {
			bb = ByteBuffer.allocate((tot*2+1)*Integer.BYTES);
			bb.putInt(tot);
			int i = 0;
			while ( i < tot*2 ) {
				bb.putInt(announcements[i]);
				bb.putInt(announcements[i+1]);
				i += 2;
			}
	}
	
	/**
	 * Returns the vector as a byte[] that can be sent in a packet
	 * Must be called after the creation using the previous constructor
	 * 
	 * @return a byte[] containing the announcement
	 */
	public byte[] toByteArray() {
		byte[] result = new byte[bb.position()];
		System.arraycopy(bb.array(), 0, result, 0, result.length);
		return result;
	}
	
	
	/**
	 * Constructor for decoding a byte array received as a DVControlPayload
	 * 
	 **/
	public DVControlPayload(byte[] payload) {
		this.bb = ByteBuffer.wrap(payload);
	}
	
	
	/**
	 * Gets an int (4 bytes) stored in net byte order (Big Endian).
	 * Must be called after creating a DVControlPayload object using
	 * the control packet payload, that is, the previous constructor
	 * 
	 * @return int the number of announcements contained in this DVControlPayload
	 */
	public int getTotal() {
		return bb.getInt();
	}
	
	/**
	 * Gets the announcements, each is two ints (2 x 4 bytes), stored in net byte order (Big Endian)
	 * Must be called after creating a DVControlPayload object using
	 * the control packet payload and after calling getTot() on it
	 * 
	 * @return int the int[] with the announcements contained in this DVControlPayload
	 */
	public int[] getAnnouncements(int total) {
		int[] result = new int[total*2];
		int i = 0;
		while ( i < total*2 ) {
			result[i] = bb.getInt();
			result[i+1] = bb.getInt();
			i += 2;
		}
		return result;
	}
	
}


