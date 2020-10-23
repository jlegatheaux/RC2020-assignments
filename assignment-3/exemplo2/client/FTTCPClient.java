/**
 * FTTCPClient - File TCP Transfer Client
 * A client that can send files to a FTTCPServer
 * Initial material for background to Assignment 3
 */
package client;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FTTCPClient {

    static final int BLOCKSIZE = 1024; // you can change if you want
	

	public static void main(String[] args) throws Exception {
		if( args.length != 3 ) {
			System.out.println("usage: java FTTCPClient <host> <port> <filename>") ;
			System.exit(0) ;
		}
		String server = args[0] ;
		int port = Integer.parseInt(args[1]);
		String filename = args[2];

		System.out.println("Sending: "+filename);
		// open file
		FileInputStream f = new FileInputStream(filename);
		
		// Creates socket to connect to the server
		Socket socket = new Socket( server, port ) ;
		OutputStream os = socket.getOutputStream();

		os.write( filename.getBytes()); // to send the file name
		os.write( new byte[]{0} ); // a separator

		int n ;
		byte[] buf = new byte[BLOCKSIZE] ;
		while( (n = f.read( buf )) > 0 )   // send the file content to the server
			os.write( buf, 0, n ) ;

		socket.close();
		f.close();
		System.out.println( "Done" );
	}
}
