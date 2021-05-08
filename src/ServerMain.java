//File Server.java
import java.io.*;
import java.net.*;

class ServerMain
{
   static final int DEFAULT_SERVER_PORT = 25610;
   
   static final int DEFAULT_MAX_CLIENTS = 4;
   static final int DEFAULT_ACTIVE_CLIENTS = 0;
      
   static final int SOCKET_TIMEOUT = 1000;
   static final long DEFAULT_INACTIVITY_TIMEOUT = 120000;//= 120 seconds = 2 mins
   
   static private ServerSocket service;
   
   static private int MaxClients;
   //static private int ActiveClients;
   static GameWorld world;
   static LoopingThread loop;
   
   static private boolean Running;
   
   static private long inactivityTimeout;
   static private long lastActiveTime;
   
   public static void main( String args[] )
	{
      System.out.println("Race-Kart Assignment Server");

      
      //Use Defaults
      int serverPort = DEFAULT_SERVER_PORT;
      
      MaxClients = DEFAULT_MAX_CLIENTS;
      //ActiveClients = DEFAULT_ACTIVE_CLIENTS;
      
      inactivityTimeout = DEFAULT_INACTIVITY_TIMEOUT;
      lastActiveTime = System.currentTimeMillis();
      
      //Check ARGS
      
      
      //Initalize World
      world = new GameWorld();
      
      //World Thread
      loop = new LoopingThread();
      
      Thread worldThread = new Thread(loop);
      
      worldThread.start();      
      
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
         
         service.setSoTimeout(SOCKET_TIMEOUT);
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
               try
		         {
   			      server = service.accept();
               
                  ClientHandler handler = new ClientHandler(server);
            
                  Thread t = new Thread(handler);
                  t.start();
               
                  //activeClients++;
               }
               catch (SocketTimeoutException e)
               {
			         System.err.println("Socket Exception: " + e);
		         }
            }
            
            //Keep server open,
            
            //Check connections? go to close if none are open
            
            if (ClientHandler.ActiveClients() <= 0)
            {
               //Inactive for to long?
               if (System.currentTimeMillis() - lastActiveTime >= inactivityTimeout)
               {
            
                  //Stop server
                  Running = false;
                  
                  loop.StopLoop();
               }
            }
            else
            {
               //Update last active time.
               lastActiveTime = System.currentTimeMillis();
            }
            
         }
         while(Running);
			
         System.out.println("Inactive for longer than timeout period, server shutting down...");
         
         server.close();
		}  
		catch (IOException e)
		{
			System.err.println("I/O Exception: " + e);
		}
      /*catch (BindException e)
      {
         System.err.println("Bind Exception: " + e);
      }*/
	}

}