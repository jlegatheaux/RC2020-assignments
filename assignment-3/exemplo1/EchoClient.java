import java.io.*;
import java.net.*;
import java.util.*;

public class EchoClient {

    public static void main(String[] args ) throws Exception {
		
    if( args.length != 2 ) {
      System.out.println("usage: java EchoClient <serverhost> <serverport>");
      System.exit(0) ;
    }
    
    String server = args[0] ;
    int port = Integer.parseInt(args[1]) ;
		
    Socket socket = new Socket( server, port ) ;
    OutputStream os = socket.getOutputStream();
    InputStream is = socket.getInputStream() ;

    Scanner in = new Scanner( System.in ) ;
    byte[] buf;
    int n;
    String echoRequest;
    
	do {
	    echoRequest = in.nextLine();
	    echoRequest = echoRequest + "\n";

    	    System.out.println("I will send: " +echoRequest);
            os.write( echoRequest.getBytes() );

	    String echoReply = new Scanner(is).nextLine();
            System.out.println("Reply form Server: " +echoReply);

      	   } while( !echoRequest.equals("!quit\n") ) ;

           socket.close() ;
    }
}
