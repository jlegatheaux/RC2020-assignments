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
 * @author You
 *
 */

public class GetFile {
	private static final int BUF_SIZE = 512;
	private static final int MAX_RETRY = 3;
	
	private static Stats stat;
	
	public static void main(String[] args) throws Exception {
		if ( args.length != 1 ) {
			System.out.println("Usage: java GetFile url_to_access");
			System.exit(0);
		}
		String url = args[0];
		URL u = new URL(url);
		// Assuming URL of the form http://server-name:port/path ....
		int port = u.getPort() == -1 ? 80 : u.getPort();
		String path = u.getPath() == "" ? "/" : u.getPath();
		
		downloadFile(u.getHost(), port, path);

	}
		
        // Implement here to download the file requested in the URL
        // and write the file in the client side.
        // In the end print te requires detatistics
    
	private static void downloadFile(String host, int port, String path) 
			throws UnknownHostException, IOException {

	    ....
	}

        // can add other private stuff as needed
}


