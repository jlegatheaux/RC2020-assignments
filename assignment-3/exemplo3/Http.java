import java.net.*;
import java.util.*;
import java.io.*;


/**
 * Auxiliary methods to deal with HTTP requests and replies
 */
public class Http {


	/**
	 * Copies data from an input stream to an output stream
	 */
	public static void dumpStream( InputStream in, OutputStream out)
			throws IOException {
		byte[] arr = new byte[1024];
		for( ;;) {
			int n = in.read( arr);
			if( n == -1)
				break;
			out.write( arr, 0, n);
		}
	}

	/**
	 * Reads one line from a HTTP header
	 */
	public static String readLine( InputStream is ) throws IOException {
		StringBuffer sb = new StringBuffer() ;
		int c ;
		while( (c = is.read() ) >= 0 ) {
			if( c == '\r' ) continue ;
			if( c == '\n' ) break ;
			sb.append((char)c ) ;
		}
		return sb.toString() ;
	} 


	/**
	 * Parses the first line of the HTTP request and returns an array
	 * of three strings: reply[0] = method, reply[1] = object and reply[2] = version
	 * Example: input "GET index.html HTTP/1.0"
	 * output reply[0] = "GET", reply[1] = "index.html" and reply[2] = "HTTP/1.0"
	 * 
	 * If the input is malformed, it returns something unpredictable
	 */


	public static String[] parseHttpRequest( String request) {
		String[] error = { "ERROR", "", "" };
		String[] result = { "", "", "" };
		int pos0 = request.indexOf( ' ');
		if( pos0 == -1) return error;
		result[0] = request.substring( 0, pos0).trim();
		pos0++;
		int pos1 = request.indexOf( ' ', pos0);
		if( pos1 == -1) return error;
		result[1] = request.substring( pos0, pos1).trim();
		result[2] = request.substring( pos1 + 1).trim();
		if(! result[1].startsWith("/")) return error;
		if(! result[2].startsWith("HTTP")) return error;
		return result;
	}

	
	/**
	 * Parses the first line of the HTTP reply and returns an array
	 * of three strings: reply[0] = version, reply[1] = number and reply[2] = result message
	 * Example: input "HTTP/1.0 501 Not Implemented"
	 * output reply[0] = "HTTP/1.0", reply[1] = "501" and reply[2] = "Not Implemented"
	 * 
	 * If the input is malformed, it returns something unpredictable
	 */

	public static String[] parseHttpReply (String reply) {
		String[] result = { "", "", "" };
		int pos0 = reply.indexOf(' ');
		if( pos0 == -1) return result;
		result[0] = reply.substring( 0, pos0).trim();
		pos0++;
		int pos1 = reply.indexOf(' ', pos0);
		if( pos1 == -1) return result;
		result[1] = reply.substring( pos0, pos1).trim();
		result[2] = reply.substring( pos1 + 1).trim();
		return result;
	}



	

	
	
	
	/**
	 * Parses an HTTP header returning an array with the name of the attribute header
	 * in position 0 and its value in position 1
	 * Example, for "Connection: Keep-alive", returns:
	 * [0]->"Connection"; [1]->"Keep-alive" 
	 * 
	 * If the input is malformed, it returns something unpredictable
	 *
	 */
	public static String[] parseHttpHeader( String header) {
		String[] result = { "ERROR", "" };
		int pos0 = header.indexOf( ':');
		if( pos0 == -1)
			return result;
		result[0] = header.substring( 0, pos0).trim();
		result[1] = header.substring( pos0 + 1).trim();
		return result;
	}
	
	/**
	 * Parses an HTTP range header value sent by a client, 
	 * returning an array the range values
	 * Examples: "range=1000-2000" returns { 1000, 2000 }
	 * "range=1000-" returns { 1000, -1 }
	 * If the input is malformed, it returns {-1,-1} or something unpredictable
	 */
	public static int[] parseRangeValues(String value) {
		int[] result = { -1,-1 }; // special cases
		int pos0 = value.indexOf('=');
		int pos1 = value.indexOf('-');
		if ( pos0 == -1 || pos1 == -1 ) return result;
//		System.out.println("Range value = \""+value+"\" positions: "+pos0+" "+pos1+ " string "
//				+value.substring(pos0+1, pos1)+"\n");
		result[0] = Integer.valueOf(value.substring(pos0+1, pos1));
		String upperSide = value.substring(pos1+1).trim(); 
		if (upperSide.isEmpty() ) result[1]=-1; // useless since it already is
		else result[1]=Integer.valueOf(upperSide);
//		System.out.println("range from: "+result[0]+" to "+result[1]);
		return result;
	}
	
	
	
	public static int[] parseRangeValuesSentByClient(String value) {
		return parseRangeValues(value);
	}
	
	/**
	 * Parses an HTTP range header value returned by a server 
	 * it returns an array with the range values as well as the size of the file
	 * Examples: "bytes 0-20/3000" returns { 0, 20, 3000 }
	 * If the input is malformed, it returns {-1,-1, -1} or something unpredictable
	 */
	
	public static int[] parseRangeValuesSentByServer(String value) {
		int[] result = { -1, -1, -1 }; // special cases
		int pos0 = value.indexOf(' ');
		int pos1 = value.indexOf('-');
		int pos2 = value.indexOf('/');
		if (pos0 == -1 || pos1 == -1 || pos2 == -1)
			return result;
		result[0] = Integer.valueOf(value.substring(pos0 + 1, pos1));
		String upperSide = value.substring(pos1 + 1, pos2).trim();
		String fileLength = value.substring(pos2 + 1).trim();
		if (upperSide.isEmpty())
			result[1] = -1; // useless since it already is
		else
			result[1] = Integer.valueOf(upperSide);
		if (fileLength.isEmpty())
			result[2] = -1; // useless since it already is
		else
			result[2] = Integer.valueOf(fileLength);
		return result;
	}
	
	

	/**
	 * From the contents of a form with format application/x-www-form-urlencoded
	 * it returns an object of type Properties associating each element with its value
	 */
	public static Properties parseHttpPostContents( String contents)
			throws IOException {
		Properties props = new Properties();
		Scanner scanner = new Scanner(contents).useDelimiter( "&");
		while( scanner.hasNext()) {
			Scanner inScanner = new Scanner( scanner.next()).useDelimiter( "=");
			String propName = URLDecoder.decode( inScanner.next(), "UTF-8");
			String propValue = "";
			try {
				propValue = URLDecoder.decode( inScanner.next(), "UTF-8");
			} catch( Exception e) {
				// do nothing
			}
			props.setProperty( propName, propValue);
		}
		return props;
	}

}
