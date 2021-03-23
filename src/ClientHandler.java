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
      /*
         inputStream = new BufferedReader(
				new InputStreamReader(
					server.getInputStream()
				)
			);
			
			outputStream = new DataOutputStream(
				server.getOutputStream()
			);
         
         outputObject = new ObjectOutputStream(
            server.getOutputStream()
         );
         
         inputObject = new ObjectInputStream(
            server.getInputStream()
         );
         */
         
         index = 0;
         
         //Connection Loop
         do
         {
            index = index % ServerMain.world.GetKarts().length;
         
   			/*if((line = inputStream.readLine()) != null)
   			{
   				outputStream.writeBytes( line + "\n" );
   			}*/
            
            line = REQUEST_KART;
            
            System.out.print("Receiving: " + index);
            receiveRequest();
            
            System.out.print("Sending: " + index);
            sendRequest();
            
            //if ()
            if ( line.equals("CLOSE") )
            {
               break;
            }
            
            AttemptSleep(1);
            
            index++;

         } while(true);
         
			
			// Comment out/remove the outputStream and server close statements if server should remain live
			CloseConnections();
         
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
            
   				               
               //String[] splitLine = responseLine.split(" ");
               
               //line = splitLine[0];
               
               switch(line)
               {
                  case REQUEST_CONTROL:
                     
                     //Get object
                     
                     receiveControl();
                     
                     break;
                  
                  case REQUEST_KART:
                     
                     //Get object
                     receiveKart();
                     
                     break;
               
               }
               
               if((responseLine = inputStream.readLine()) != null)
   				{
   					System.out.println("SERVER: " + responseLine);
   				}
               
               if ( responseLine.equals("CLOSE") )
               {
                  //Send signal to exit
                  //break;
               }
               
               AttemptSleep(10);
               
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
				System.err.println("IOException: " + e);
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
               
               //Respond
               
               //Split line into parts
               String[] splitLine = line.split(SPLIT_CHAR);
               
               //Check length
               line = splitLine[0];
               int currentIndex = Integer.parseInt(splitLine[1]);
               
               switch(line)
               {
                  case REQUEST_CONTROL:
                     
                     //Get object
                     
                     sendControl();
                     
                     break;
                  
                  case REQUEST_KART:
                     
                     //Get object
                     //Server.world.GetKarts()[index];//RaceKart currentKart = (RaceKart) inputObject.readObject();
                     sendKart();
                     
                     break;
               
               }

               
               AttemptSleep(1);
   
            //} while(true);
            
         }
         catch (Exception e)
         {
            System.out.print("Exception thown." + e);
         }
      }
   }
   
   private void sendMessage()
   {
   
   }
   
   private void receiveMessage()
   {
   
   }
   
   private void sendKart()
   {
      // write object to stream
      outputObject.writeObject(ServerMain.world.GetKarts()[index]);

      // send it
      outputObject.flush();
   }
   
   private void receiveKart()
   {
      //Collect kart
      RaceKart currentKart = (RaceKart) inputObject.readObject();
      
      //Place into world
      ServerMain.world.GetKarts()[index] = currentKart;
   }
   
   private void sendControl()
   {
      // write object to stream
      outputObject.writeObject(ServerMain.world.GetControls()[index]);
   
      // send it
      outputObject.flush();
   }
   
   private void receiveControl()
   {
      //Collect control
      byte[] currentControl = (byte[]) inputObject.readObject();
      
      //Place into world
      ServerMain.world.GetControls()[index] = currentControl;
   }
   
   
   private void AttemptSleep(int duration)
   {
      try
      {
         Thread.sleep(duration);
      }
      catch(Exception e)
      {
         System.out.print("Exception thrown for Thread.sleep: " + e);
      }
   }
   
}