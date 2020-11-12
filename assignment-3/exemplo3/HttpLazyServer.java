
/** A really simple and lazy HTTP server supporting ranges
 * 
 * @author The RC - FCT/UNL class instructors
 *
 */

import java.io.*;
import java.net.*;
import java.util.Date;

public class HttpLazyServer {

	static final int PORT = 8080;
	static final int MAX_BYTES = 1000000;


	/**
	 * Sends an error message "Not Implemented"
	 */
	private static void sendsNotSupportedPage(OutputStream out) 
			throws IOException {
		String page = 
				"<HTML><BODY>Lazy server: request not supported</BODY></HTML>";
		int length = page.length();
		String header = "HTTP/1.0 501 Not Implemented\r\n";
		header += "Date: "+new Date().toString()+"\r\n";
		header += "Content-type: text/html\r\n";
		header += "Server: "+"X-Lazy-Server-RC2019"+"\r\n";
		header += "XAlmost-Accept-Ranges: bytes\r\n";
		header += "Content-Length: "+String.valueOf(length)+"\r\n\r\n";
		header += page;
		out.write(header.getBytes());
	}

	/**
	 * Sends a simple valid page with the text of the parameter simplePage
	 */
	private static void sendsSimplePage(String simplePage, OutputStream out) 
			throws IOException {
		String page = 
				"<HTML><BODY>Lazy server: "+simplePage+"</BODY></HTML>\r\n";
		int length = page.length();
		String header = "HTTP/1.0 200 OK\r\n";
		header += "Date: "+new Date().toString()+"\r\n";
		header += "Content-type: text/html\r\n";
		header += "Server: "+"X-Lazy-Server-RC2019"+"\r\n";
		header += "X-Almost-Accept-Ranges: bytes\r\n";
		header += "Content-Length: "+String.valueOf(length)+"\r\n\r\n";
		header += page;
		out.write(header.getBytes());
	}


	private static void processClientRequest(Socket s) {
		try {
			InputStream in = s.getInputStream();
			OutputStream out = s.getOutputStream();
			long[] ranges = { -1,-1 }; // useless but anyway
			String line = Http.readLine(in);
			System.out.println("\nGot: \n\n"+line);
			String[] request = Http.parseHttpRequest(line);
			// ignore, but print the header of the http message
			line = Http.readLine(in);
			while ( ! line.equals("") ) {
				System.out.println(line);
				String[] header = Http.parseHttpHeader(line);
				if ( header[0].equalsIgnoreCase("Range") )
					ranges = Http.parseRangeValues(header[1]);
				line = Http.readLine(in);
			}
			System.out.println();
			if( request[0].equalsIgnoreCase("GET") && request[1] != "") {
				sendFile(request[1], ranges, out);
			} else {
				sendsNotSupportedPage(out);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}


	/**
	 * sendFile: when available, sends the file in the URL to the client
	 * 
	 */
	private static void sendFile(String fileName, long[] ranges, OutputStream out)
			throws IOException {
		// strips the leading "/"
		String name = fileName.substring(1);
		File f = new File(name);
		System.out.println("I will try to send file: \""+name+"\"");
		if ( name == "" ) sendsSimplePage ("The empty name is not a file",out);
		else if ( !f.exists() ) sendsSimplePage ("File \""+fileName+"\" does not exist",out);
		else if ( !f.isFile() ) sendsSimplePage ("File \""+fileName+"\" is not a file",out);
		else if ( !f.canRead() ) sendsSimplePage ("File \""+fileName+"\" cannot be read",out);
		else {
			// we are going to send something
			long fileSize = f.length();
			long rest = 0;
			if (ranges[0] == -1 && ranges[1] == -1 ) { // special case: no ranges 
				ranges[1] = fileSize-1;
				ranges[0] = 0;
			}
			else if ( ranges[1] == -1 ) { // special case: up to the end 
				ranges[1] = fileSize-1;
			}
			rest = fileSize - ranges[0];     // never sends more then available
			if (rest > MAX_BYTES) rest=MAX_BYTES; // never sends more then MAX_BYTES
			long rangeSize = ranges[1]-ranges[0]+1;
			if (rest >= rangeSize) rest=rangeSize; // never sends more then demanded
			
			// rest is negative or 0 if fileSize < ranges[0] or if ranges[1] < ranges[0]
			// rest is <= still available && <= MAX_BYTES && <= demanded
			long size = rest <= 0? 0 : rest; // number of bytes to send

			RandomAccessFile file = new RandomAccessFile ( f, "r" );
			StringBuilder header = new StringBuilder("");

			if ( size == 0 && fileSize>0 ) { // || ranges[1] > fileSize-1 ) {
				header.append("HTTP/1.0 416 Range not satisfiable\r\n");
				header.append("Date: "+new Date().toString()+"\r\n");
				header.append("Server: "+"X-Lazy-Server-RC2019"+"\r\n");
				header.append("XAlmost-Accept-Ranges: bytes\r\n");
				header.append("Content-Range: bytes *-0\r\n"); //
				file.close();
				out.write(header.toString().getBytes());
				return;
			}
			// send the all file? it covers the case where a range was asked
			// but the all file is sent (bytes=0-fileSize, bytes=0-, bytes=0-something too big)
			else if ( ranges[0]==0 && size == fileSize ) {
				header.append("HTTP/1.0 200 OK\r\n");
				header.append("Date: "+new Date().toString()+"\r\n");
				header.append("Server: "+"X-Lazy-Server-RC2019"+"\r\n");
				header.append("XAlmost-Accept-Ranges: bytes\r\n");
				header.append("Content-Length: "+String.valueOf(size)+"\r\n\r\n");
			}
			else  { // there are ranges and something to send
				header.append("HTTP/1.0 206 Partial Content\r\n");
				header.append("Date: "+new Date().toString()+"\r\n");
				header.append("Server: "+"X-Lazy-Server-RC2019"+"\r\n");
				header.append("XAlmost-Accept-Ranges: bytes\r\n");
				header.append("Content-Range: bytes "+ranges[0]+"-"+(ranges[0]+size-1)+"/*\r\n"); // "/"+fileSize+
				header.append("Content-Length: "+String.valueOf(size)+"\r\n\r\n");
			}
			out.write(header.toString().getBytes());
			// size > 0 since there is something to send
			long bufferSize = (size <= 4096) ? size : 4096;
			byte[] buffer = new byte[(int) bufferSize];
			int totalSent = 0;
			file.seek(ranges[0]);
			for(;;) {
				int n = file.read(buffer,0,(int) bufferSize);
				if( n == -1) break;
				out.write(buffer, 0, n);
				totalSent += n;
				if ( size - totalSent < bufferSize ) bufferSize = size - totalSent;
				if ( bufferSize == 0 ) break;
			}
			file.close();
		}
	}


	/**
	 * MAIN - accept and handle client connections
	 */

	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket( PORT );
		for (;;) {
			try {
				System.out.println("\nHttp lazy server ready at port "+PORT+ " waiting for request ...");
				System.out.println("I only accept range requests in the form \"Range: bytes=x-y\"");
				System.out.println("I only accept to send at most "+MAX_BYTES+ " in each reply\n");
				Socket clientSock = ss.accept();
				processClientRequest( clientSock );
				clientSock.close();
			} catch (Exception e ) {
				ss.close();
				System.err.println("Http lazy server is going down :(");
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

}
