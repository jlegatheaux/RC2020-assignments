import java.io.*;
import java.net.*;

public class ConnectionHandler {
    private static final int TMP_BUF_SIZE = 1024;

    public void handle(Socket cs) throws IOException {

	InputStream is = cs.getInputStream();
	OutputStream os = cs.getOutputStream();

        // implements the data ECHO, by reading and writing 
        // while the connection is not closed
	int n ;
	byte[] buf = new byte[TMP_BUF_SIZE] ;
	while( (n = is.read(buf)) > 0 )
		os.write( buf, 0, n );
    }
}
