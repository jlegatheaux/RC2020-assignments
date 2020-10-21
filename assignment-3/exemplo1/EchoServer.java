import java.io.* ;
import java.net.* ;

public class EchoServer {

   public static final int DEFAULT_PORT = 8000 ;
	
   public static void main(String args[] ) throws Exception {

       int port=DEFAULT_PORT;
       if (args.length == 1) {
	   port=Integer.parseInt(args[0]);
       }
       
       System.out.println("Server waiting in port " + port);
       // creates a server socket to wayt for connections
       try (ServerSocket serverSocket = new ServerSocket( port )) {
	   for(;;) { 
	             Socket clientSocket = serverSocket.accept() ;
		     System.out.println("Got a client connection from "
			+ clientSocket.getInetAddress().getHostName());

		     // handling the client connection
		     new ConnectionHandler().handle( clientSocket );
		   }
       } catch (IOException x) {
	   x.printStackTrace();
       }
   }
}
