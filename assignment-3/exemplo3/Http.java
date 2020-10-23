
/**
 * Auxiliary methods to deal with HTTP requests and replies
 * RC MIEI - FCT/UNL
 */

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;



public class Http {

	private static final int BUF_SIZE = 1024;

	/**
	 * Copies all data from an input stream to an output stream
	 */
	public static void dumpStream( InputStream in, OutputStream out)
			throws IOException {
		byte[] buf = new byte[BUF_SIZE];
		int n;
		while ((n = in.read(buf)) >= 0)
			out.write(buf, 0, n);
	}

	/**
	 * Reads one HTTP header line from an input stream
	 */
	public static String readLine( InputStream is ) throws IOException {
		StringBuffer sb = new StringBuffer() ;
		int c ;
		while( (c = is.read() ) >= 0 ) {
			if( c == '\r' ) continue ;
			if( c == '\n' ) break ;
			sb.append((char)c);
		}
		return sb.toString() ;
	} 


	/**
	 * Parses the first line of the HTTP request and returns an array
	 * of three strings: reply[0] = method, reply[1] = object and reply[2] = version
	 * Example: input "GET index.html HTTP/1.0"
	 * output reply[0] = "GET", reply[1] = "index.html" and reply[2] = "HTTP/1.0"
	 * 
	 * If the input is malformed, it returns null
	 */
	public static String[] parseHttpRequest( String request) {
		Matcher m = HTTP_REQUEST_REGEX.matcher(request);
		if (m.matches())
			return new String[] { m.group(1), m.group(2), m.group(3) };
		else
			return null;
	}
	static final Pattern HTTP_REQUEST_REGEX = 
			Pattern.compile("^(GET|POST|PUT|HEAD|OPTIONS|DELETE)\\s(\\S*)\\s(HTTP\\/\\d\\.\\d)");


	/**
	 * Parses the first line of the HTTP reply and returns an array
	 * of three strings: reply[0] = version, reply[1] = number and reply[2] = result message
	 * Example: input "HTTP/1.0 501 Not Implemented"
	 * output reply[0] = "HTTP/1.0", reply[1] = "501" and reply[2] = "Not Implemented"
	 * 
	 * If the input is malformed, it returns null
	 */
	public static String[] parseHttpReply (String reply) {
		Matcher m = HTTP_REPLY_REGEX.matcher(reply);
		if (m.matches())
			return new String[] { m.group(1), m.group(2), m.group(3) };
		else
			return null;
	}
	static final Pattern HTTP_REPLY_REGEX = 
			Pattern.compile("^(HTTP\\/\\d\\.\\d)\\s(\\d{3})(.*)");

		
	
	/**
	 * Parses an HTTP header returning an array with the name of the attribute header
	 * in position 0 and its value in position 1
	 * Example, for "Connection: Keep-alive", 
	 * returns: [0]->"Connection"; [1]->"Keep-alive" 
	 * 
	 * If the input is malformed, it returns null
	 *
	 */
	public static String[] parseHttpHeader( String header) {
		Matcher m = HTTP_HEADER_REGEX.matcher(header);
		if (m.matches())
			return new String[] { m.group(1), m.group(2).trim() };
		else
			return null;
	}
	static final Pattern HTTP_HEADER_REGEX = Pattern.compile("^(\\S+):\\s(.+)");

	
	
	/**
	 * Parses an HTTP request range value, returning an array the range values
	 * Examples: 
	 * "bytes=1000-2000" returns: { 1000, 2000 }
	 * "bytes=1000-" returns: { 1000, -1 }
	 * 
	 * If the input is malformed, it returns null
	 */
	public static long[] parseRangeValues(String value) {
		Matcher m = HTTP_HEADER_RANGE_REGEX.matcher(value);
		if (m.matches()) {
			String first = m.group(1), second = m.group(2);
			if (second == null)
				return new long[] { Long.valueOf(first), -1L };
			else
				return new long[] { Long.valueOf(first), Long.valueOf(second) };
		} else
			return null;
	}
	static final Pattern HTTP_HEADER_RANGE_REGEX = Pattern.compile("bytes=(\\d+)-(\\d+)?");

	
	/**
	 * Parses an HTTP reply range value, returned by a server 
	 * it returns an array with the range values as well as the size of the file
	 * Examples: "bytes 0-20/3000" returns { 0, 20, 3000 }
	 * 		     "bytes 10-30" returns { 10, 30, -1 }
	 * If the input is malformed, it returns null
	 */
	public static long[] parseRangeValuesSentByServer(String value) {
		Matcher m = HTTP_SERVER_HEADER_RANGE_REGEX.matcher(value);
		if (m.matches()) {
			String first = m.group(1), second = m.group(2), third = m.group(3);
			if (third == null || third.equals("*"))
				return new long[] { Long.valueOf(first), Long.valueOf(second), -1L };
			else
				return new long[] { Long.valueOf(first), Long.valueOf(second), Long.valueOf(third) };
		} else
			return null;
	}
	static final Pattern HTTP_SERVER_HEADER_RANGE_REGEX 
		= Pattern.compile("bytes\\s(\\d+)-(\\d+)/?(\\d+|\\*)?");

	


	
	
	
	
	/**
	 * Sends an error message "501: Request Not Implemented"
	 */
	public static void sendsNotImplementedPage(OutputStream out) 
			throws IOException {
		String page = 
				"<HTML><BODY>501: Request Not Implemented</BODY></HTML>\r\n" ;
		int length = page.length();
		String output = "HTTP/1.0 501 Not Implemented\r\n";
		output += "Date: "+new Date().toString()+"\r\n";
		output += "Content-type: text/html\r\n";
		output += "Server: X-RC2018\r\n";
		output += "Content-Length: "+String.valueOf(length)+"\r\n\r\n";
		out.write(output.getBytes());
	}

	/**
	 * Sends a simple valid page with the text of the parameter simplePage
	 */
	public static void sendsSimplePage(String simplePage, OutputStream out) 
			throws IOException {
		String page = "<HTML><BODY>" + simplePage + "</BODY></HTML>\r\n";
		int length = page.length();
		String output = "HTTP/1.0 200 OK\r\n";
		output += "Date: "+new Date().toString()+"\r\n";
		output += "Content-type: text/html\r\n";
		output += "Server: X-RC2018\r\n";
		output += "Content-Length: "+String.valueOf(length)+"\r\n\r\n";
		out.write(output.getBytes());
	}


	
/*********************************************/	
/**  some simple tests
 * 	
 */
	static void unitTestsAdHoc() {

		String request1 = "%s /resource HTTP/1.0";
		String request2 = "%s /resource?key1=val1&key2=val2 HTTP/1.1";

		for (String i : new String[] { "GET", "PUT", "POST", "HEAD", "DELETE", "OPTIONS" }) {
			Assert( Result(Http::PrintArray, Http.parseHttpRequest(String.format(request1, i))) != null );
			Assert( Result(Http::PrintArray, Http.parseHttpRequest(String.format(request2, i))) != null );
		}

		String reply1 = "HTTP/1.0 200 OK";
		String reply2 = "HTTP/1.1 501 Not Implemented";

		Assert( Result(Http::PrintArray, Http.parseHttpReply(reply1)) != null);
		Assert( Result(Http::PrintArray, Http.parseHttpReply(reply2)) != null);

		String header = "Content-Length: 12345";
		String range1 = "Range: bytes=1-";
		String range2 = "Range: bytes=1-2";

		Assert( Result(Http::PrintArray, Http.parseHttpHeader(header)) != null);
		Assert( Result(Http::PrintArray, Http.parseHttpHeader(range1)) != null);
		Assert( Result(Http::PrintArray, Http.parseHttpHeader(range2)) != null);
		
		Assert( Result(Http::PrintArray, Http.parseRangeValues(Http.parseHttpHeader(range1)[1])) != null);
		Assert( Result(Http::PrintArray, Http.parseRangeValues(Http.parseHttpHeader(range2)[1])) != null);
		
		String range3 = "bytes 1-10/100";
		String range4 = "bytes 1-2";
		String range5 = "bytes 0-1000/*";
		String range6 = "bytes *";

		Assert( Result(Http::PrintArray, Http.parseRangeValuesSentByServer(range3)) != null);
		Assert( Result(Http::PrintArray, Http.parseRangeValuesSentByServer(range4)) != null);
		Assert( Result(Http::PrintArray, Http.parseRangeValuesSentByServer(range5)) != null);
		Assert( Result(Http::PrintArray, Http.parseRangeValuesSentByServer(range6)) == null);

	}

	static void Assert(boolean value) {
		if (value != true)
			throw new AssertionError();
	}

	static <T> T Result(Consumer<T> c, T value) {
		c.accept(value);
		return value;
	}
	
	static <T> void PrintArray( T[] arr ) {
		System.out.println( Arrays.asList( arr ));
	}
	
	static void PrintArray( long[] arr ) {
		System.out.println(Arrays.toString(arr));
	}
	
	public static void main(String[] args) {
		unitTestsAdHoc();
	}
}
