//File Server.java
import java.io.*;
import java.net.*;

class Server
{
   static final int DEFAULT_SERVER_PORT = 25610;
   
   static final int DEFAULT_MAX_CLIENTS = 4;
   static final int DEFAULT_ACTIVE_CLIENTS = 0;
      
   static final int SOCKET_TIMEOUT = 1000;
   
   static private ServerSocket service;
   
   static private int MaxClients;
   //static private int ActiveClients;
   
   static private boolean Running;
   
   public static void main( String args[] )
	{
      //Use Defaults
      int serverPort = DEFAULT_SERVER_PORT;
      
      MaxClients = DEFAULT_MAX_CLIENTS;
      //ActiveClients = DEFAULT_ACTIVE_CLIENTS;
      
      //Check ARGS
      
      
      //Launch server
      LaunchServer(serverPort);
   }
   
   public static void LaunchServer(int port)
   {
		// Declare a server socket and a client socket for the server
		service = null;
		
     
		// Try to open a server socket on port 5000
		try
		{
			service = new ServerSocket(port);
		}
		catch (IOException e)
		{
			System.out.println(e);
		}  

		// Create a socket object from the ServerSocket to listen and accept
		// connections. Open input and output streams
		try
		{
         //int maxClients = 2;
         //int activeClients = 0;
         Socket server = null;
         Running = true;
         
         do
         {
            if (ClientHandler.ActiveClients() < MaxClients)
            {
			   server = service.accept();
            
            ClientHandler handler = new ClientHandler(server);
         
            Thread t = new Thread(handler);
            t.start();
            
            //activeClients++;
            }
            
            //Keep server open,
            
            //Check connections? close if none are open
            
            if (ClientHandler.ActiveClients() < 0)
            {
            
               //Stop server
               Running = false;
            }
         }
         while(Running);
			
         server.close();
		}  
		catch (IOException e)
		{
			System.out.println(e);
		}
	}

}