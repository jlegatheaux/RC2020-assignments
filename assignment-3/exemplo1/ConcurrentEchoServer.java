import java.io.*;
import java.net.*;

public class ConcurrentEchoServer {
    
    public static final int DEFAULT_PORT = 8000 ;
    public static void main( String[] args )throws Exception {

	int port=DEFAULT_PORT;
	if (args.length == 1) {
	    port=Integer.parseInt(args[0]);
	}
        System.out.println("Server waiting in port " + port);

	try (ServerSocket serverSocket = new ServerSocket( port )) {

	    Socket clientSocket;
	    ServiceHandler servthread;

	    while(true) {
		clientSocket = serverSocket.accept();
		servthread = new ServiceHandler(clientSocket);
		servthread.start();
	    }
	}
	catch (IOException x) {
	    x.printStackTrace();
	}
    }
}


