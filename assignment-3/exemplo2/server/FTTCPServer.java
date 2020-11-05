package server;
/**
 * FTTCPServer - File Transfer TCP Server
 * A TCP Server to receive files sent by clients.
 * Notice: this is not a concurrent server
 * Initial material for background to Assignment 3
 */

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FTTCPServer {

    static final int BLOCKSIZE = 1024; // Can change if you want
    public static final int PORT = 8000 ; // Change if you want

	/**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        // Need a socket to expect client connections in port PORT
	ServerSocket serverSocket = new ServerSocket( PORT ) ;

	for(;;) { 
	    System.out.println("Server ready at port "+PORT);
	    Socket clientSocket = serverSocket.accept();
	    // Got a client connection
	    InputStream is = clientSocket.getInputStream();
	    int n ;
	    byte[] buf = new byte[BLOCKSIZE] ;
			
	    for ( n=0; n<BLOCKSIZE; n++ ) {  
	    // Read data sent by the client
		int s = is.read();
		if ( s!=-1 ) buf[n]=(byte)s;
		else System.exit(1); // Uhm ... not expected
		if ( buf[n] == 0 ) break;
		}
	    System.out.println("Receiving: '"+new String(buf, 0, n)+"'");
            // Write received data in a file named tmp.out
	    FileOutputStream f = new FileOutputStream("tmp.out");

            long starttime=System.currentTimeMillis();
	    int count=0;
	    while( (n = is.read( buf )) > 0 ) 
		{
		    count=count+n;
		    f.write( buf, 0, n ) ;
		}
	    long endtime=System.currentTimeMillis();

	    clientSocket.close();
	    f.close();
	    // Let's compute the transfer statistc ...
       
	    long ttime= endtime-starttime;
	    
            // Let's compute the transfer statistc ...
            System.out.println("\nServer observed stats:");
            System.out.println("Nr of Bytes received (inbound): " + count + " bytes" );
            System.out.println("Nr of Bits received (inbound) : " + 8*count + " bits" );
            System.out.println("Transfer time: " + ttime + " ms" );
            System.out.println("Transfer Rate: " + 1000*(count/ttime) + " bytes/s" );
            System.out.println("           or: " + 8000*(count/ttime) + " bits/s" );
            System.out.println("           or: " +  ((8*count)/(1000*ttime)) + " Kbits/s\n\n" );


	}
    }
}
