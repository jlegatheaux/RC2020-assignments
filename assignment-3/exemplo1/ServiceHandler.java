import java.io.*;
import java.net.*;

class ServiceHandler extends Thread {
    Socket connection;
	
    public ServiceHandler(Socket c) {
	super("EchoServer service thread");
	connection = c;
    }
    
    public void run()  {
         // handling the client connection                          
	try {
	    new ConnectionHandler().handle(connection);
	}
	catch (IOException x)
	    {
		x.printStackTrace();
	    }
    }
}
