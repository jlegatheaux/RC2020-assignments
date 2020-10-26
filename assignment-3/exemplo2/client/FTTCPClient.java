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
		// Implement here the required code for the client ...
			
	}
}
