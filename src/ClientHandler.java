//File: ClientHandler
import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;


class ClientHandler implements Runnable
{
   //Const
   private final String REQUEST_CONTROL = "control";
   private final String REQUEST_KART = "kart";
   
   private final String SPLIT_CHAR = " ";


   static private int activeClients = 0;
   
   static public int ActiveClients()
   {
      return activeClients;
   }

   
   private Scanner scanner;// = new Scanner(System.in);

   private Socket server;
   

		// Declare an input stream and String to store message from client		
	private BufferedReader inputStream;
	private String line;
   private int index;
   private String responseLine;
		
		// Declare an output stream to client		
	private DataOutputStream outputStream;
      
   private ObjectOutput outputObject = null;
   private ObjectInput inputObject = null;
   
   
   public ClientHandler(Socket server)
   {
      this.server = server;
      
      scanner = new Scanner(System.in);
      
      
		
		try
		{
			//clientSocket = new Socket(hostAddress, hostPort);
			
			outputStream = new DataOutputStream(
				server.getOutputStream()
			);
			
			inputStream = new BufferedReader(
				new InputStreamReader(
					server.getInputStream()
				)
			);
         
         outputObject = new ObjectOutputStream(
            server.getOutputStream()
         );
         
         inputObject = new ObjectInputStream(
            server.getInputStream()
         );
		} 
		catch (UnknownHostException e)
		{
			System.err.println("Unknown host: " + e);
		}
		catch (IOException e)
		{
			System.err.println("Couldn't get I/O for the connection to: " + e);
		}
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
   
    public void CloseConnections()
   {
      try
      {
         outputStream.close();
   		inputStream.close();
         
         outputObject.close();
   		inputObject.close();
   
         
   		//clientSocket.close();
      }
      catch (UnknownHostException e)
		{
			System.err.println("Trying to connect to unknown host: " + e);
		}
		catch (IOException e)
		{
			System.err.println("IOException:  " + e);
		}
   }
   
   //REQUEST HANDLING
   
   private void sendRequest()
   {
      if (
         server != null && 
         outputStream != null && 
         inputStream != null &&
         outputObject != null &&
         inputObject != null
      ) {
         try
			{
         
            
            //do 
            //{
               //System.out.print("CLIENT: ");
               //line = scanner.nextLine(); 
               //index = scanner.nextLine();

				   outputStream.writeBytes( line + SPLIT_CHAR + index + "\n" );
            
   				if((responseLine = inputStream.readLine()) != null)
   				{
   					System.out.println("SERVER: " + responseLine);
   				}
               
               if ( responseLine.equals("CLOSE") )
               {
                  //Send signal to exit
                  //break;
               }
               
               //String[] splitLine = responseLine.split(" ");
               
               //line = splitLine[0];
               
               switch(line)
               {
                  case REQUEST_CONTROL:
                     
                     //Get object
                     byte[] currentControl = (byte[]) inputObject.readObject();
                     Server.world.GetControls()[index] = currentControl;
                     break;
                  
                  case REQUEST_KART:
                     
                     //Get object
                     RaceKart currentKart = (RaceKart) inputObject.readObject();
                     Server.world.GetKarts()[index] = currentKart;
                     break;
               
               }
               
               try
               {
                  Thread.sleep(10);
               }
               catch(Exception e)
               {
                  System.out.print("Exception thrown for Thread.sleep: " + e);
               }
               
            //} while(true);
            
			}
         catch (ClassNotFoundException e)
         {
            System.err.println("Class not found: " + e);
         }
			catch (UnknownHostException e)
			{
				System.err.println("Trying to connect to unknown host: " + e);
			}
			catch (IOException e)
			{
				System.err.println("IOException:  " + e);
			}

      }
   }
   
   private void receiveRequest()
   {
      if (
         server != null && 
         outputStream != null && 
         inputStream != null &&
         outputObject != null &&
         inputObject != null
      ) {
      
      String line = "";
            
         try 
         {
            
            //Connection Loop
            //do
            //{
      			if((line = inputStream.readLine()) != null)
      			{
      				
                  outputStream.writeBytes( line + "\n" );
      			}
               
               if ( line.equals("CLOSE") )
               {
                  //Send signal to exit
                  //break
               }
               
               //Split line into parts
               String[] splitLine = line.split(SPLIT_CHAR);
               
               //Check length
               line = splitLine[0];
               int currentIndex = Integer.parseInt(splitLine[1]);
               
               switch(line)
               {
                  case REQUEST_CONTROL:
                     
                     //Get object
                     
                     // write object to stream
                     outputObject.writeObject(Server.world.GetControls()[index]);
            
                     // send it
                     outputObject.flush();
                     break;
                  
                  case REQUEST_KART:
                     
                     //Get object
                     //Server.world.GetKarts()[index];//RaceKart currentKart = (RaceKart) inputObject.readObject();
                     
                     // write object to stream
                     outputObject.writeObject(Server.world.GetKarts()[index]);
            
                     // send it
                     outputObject.flush();
                     break;
               
               }

               
               try
               {
                  //Thread.sleep(1);
               }
               catch(Exception e)
               {
                  System.out.print("Exception thrown for Thread.sleep: " + e);
               }
   
            //} while(true);
            
         }
         catch (Exception e)
         {
            System.out.print("Exception thown." + e);
         }
      }
   }
   
}