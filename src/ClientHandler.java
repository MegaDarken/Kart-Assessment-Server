//File: ClientHandler
import java.lang.*;
import java.io.*;
import java.net.*;


class ClientHandler implements Runnable
{
   static private int activeClients = 0;
   
   static public int ActiveClients()
   {
      return activeClients;
   }

   private Socket server;

		// Declare an input stream and String to store message from client		
	private BufferedReader inputStream;
	private String line;
		
		// Declare an output stream to client		
	private DataOutputStream outputStream;
      
   private ObjectInput inputObject = null;
   
   public ClientHandler(Socket server)
   {
      this.server = server;
   }
   
   public void run()
   {
      //Start Connection
      activeClients++;
   
      try
      {
         inputStream = new BufferedReader(
				new InputStreamReader(
					server.getInputStream()
				)
			);
			
			outputStream = new DataOutputStream(
				server.getOutputStream()
			);
         
         inputObject = new ObjectInputStream(
            server.getInputStream()
         );
         /*
         try 
         {
            // get object and cast it to a Kart (serializable class)
            Kart kart = (Kart) inputObject.readObject();
            
            // test out the kart
            System.out.println( "Kart name: " + kart.getName() );
         } catch (ClassNotFoundException e) 
         {
            
         }
         */
         //Connection Loop
         do
         {
   			if((line = inputStream.readLine()) != null)
   			{
   				outputStream.writeBytes( line + "\n" );
   			}
            
            if ( line.equals("CLOSE") )
            {
               break;
            }
            
            try
            {
               Thread.sleep(1);
            }
            catch(Exception e)
            {
               System.out.print("Exception thrown for Thread.sleep: " + e);
            }

         } while(true);
         
			
			// Comment out/remove the outputStream and server close statements if server should remain live
			outputStream.close();
			inputStream.close();
         
         }
         catch (Exception e)
         {
            System.out.print("Exception thown." + e);
         }
         
         //End Connection
         activeClients--;
   }
}