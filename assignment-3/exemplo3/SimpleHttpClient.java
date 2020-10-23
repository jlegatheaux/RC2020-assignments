import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/** A really simple HTTP Client
 * 
 * @author The RC - FCT/UNL class instructors
 *
 */

public class SimpleHttpClient {
	private static final int BUF_SIZE = 512;
	private static final int MAX_RETRY = 3;
	
	private static Stats stat;
	
	public static void main(String[] args) throws Exception {
		if ( args.length != 1 ) {
			System.out.println("Usage: java SimpleHttpClient url_to_access");
			System.exit(0);
		}
		String url = args[0];
		URL u = new URL(url);
		// Assuming URL of the form http://server-name:port/path ....
		int port = u.getPort() == -1 ? 80 : u.getPort();
		String path = u.getPath() == "" ? "/" : u.getPath();
		
		downloadFile(u.getHost(), port, path);
	}

	private static void downloadFile(String host, int port, String path) 
			throws UnknownHostException, IOException {
		String filename = path.substring( path.lastIndexOf('/')+1);
		if ( filename.equals("")) filename = "index.html";

		Socket sock = new Socket( host, port );

		OutputStream out = sock.getOutputStream();
		InputStream in = sock.getInputStream();
		String request = String.format(
			"GET %s HTTP/1.0\r\n"+
			"Host: %s\r\n"+
			"User-Agent: X-RC2020 SimpleHttpClient\r\n\r\n", path, host);

		out.write(request.getBytes());

		System.out.println("\nSent Request:\n-------------\n"+request);		
		System.out.println("Got Reply:");
		System.out.println("\nReply Header:\n--------------");
		
		String answerLine = Http.readLine(in);  // first line is always present
		System.out.println(answerLine);
		String[] reply = Http.parseHttpReply(answerLine);
		long[] range = null;

		answerLine = Http.readLine(in);
		while ( !answerLine.equals("") ) {
			System.out.println(answerLine);
			String[] head = Http.parseHttpHeader(answerLine);
			answerLine = Http.readLine(in);
		}

		if ( reply[1].equals("200")) {

		System.out.println("\nReply Body:\n--------------");
                        long time0 = System.currentTimeMillis();
                        int n;
                        byte[] buffer = new byte[BUF_SIZE];
			
			while( (n = in.read(buffer)) >= 0 ) {
			     System.out.write(buffer, 0, n);
			}
		}
		else
		    System.out.println("Ooops, received status:" + reply[1]);
	}
}
		
// long time = System.currentTimeMillis()-time0;
// System.err.println("Time= "+time+"ms "+(f.length()-start)+"bytes Kbits/s= "+ (8*(f.length()-start)/(double)time));
		
