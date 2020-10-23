import java.net.*;
import java.io.*;

public class GetURL
{
    public static void main ( String[] args ) throws IOException 
    {
    try 
	{
	    URL url = new URL( args[0] );

	    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	    String str;
	    while ((str = in.readLine()) != null) 
		    System.out.println(str);
	    in.close();
	} 
    catch (MalformedURLException e) {} 
    catch (IOException e) {}
    }
}
