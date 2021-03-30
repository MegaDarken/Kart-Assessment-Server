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
   private final String CONTINUE_NOTE = "continue";
   
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
   
   private boolean connected;
   
   public ClientHandler(Socket server)
   {
      this.server = server;
      
      scanner = new Scanner(System.in);
      
      System.out.println(server.getRemoteSocketAddress());
		
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
         
         connected = true;
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
   
      System.out.println("Request: " + line);
      
      if (
         server != null && 
         outputStream != null && 
         inputStream != null &&
         outputObject != null &&
         inputObject != null
      ) {
      
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
            
            //if line is received?
            if ((line = receiveMessage()) != null)
            {
               System.out.print("Receiving: " + index);
            
               //Split line into parts
               String[] splitLine = line.split(SPLIT_CHAR);
               
               //Check length
               if(line.length() > 1)
               {
                  line = splitLine[0];
                  index = Integer.parseInt(splitLine[1]);
                  
                  AttemptSleep(10);
                  
                  receiveRequest();
               }
            }
            
            System.out.print(receiveMessage());
            
            System.out.print("Sending: " + index);
             
            sendMessage(line + SPLIT_CHAR + index);
            
            AttemptSleep(10);
            
            sendRequest();
            
            sendMessage(CONTINUE_NOTE);  
            
            //if ()
            if ( line.equals("CLOSE") )
            {
               break;
            }
            
            
            AttemptSleep(10);
            
            index++;

         } while(connected);
         
			
			// Comment out/remove the outputStream and server close statements if server should remain live
			CloseConnections();
         
         }
         catch (Exception e)
         {
            System.out.print("Exception thown." + e);
         }
      }
      else
      {
         System.out.println("Failed to run due to 'null' element");
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
               /*

				   sendMessage( line + SPLIT_CHAR + index );
            
               String responce;
            
   				if((responce = receiveMessage()) != null)
   				{
   					System.out.println("SERVER: " + line);
   				}
               
               sendMessage( line + SPLIT_CHAR + index );
   				               
               //String[] splitLine = responseLine.split(" ");
               
               //line = splitLine[0];
               */
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
                     
                  default:
                     String currentObject = (String)inputObject.readObject();
                     System.out.println("Object Defaulted: " + currentObject);
               
               }
               
               /*if((responseLine = inputStream.readLine()) != null)
   				{
   					System.out.println("SERVER: " + responseLine);
   				}
               
               if ( responseLine.equals("CLOSE") )
               {
                  //Send signal to exit
                  //break;
               }*/
               
               //AttemptSleep(10);
               
            //} while(true);
            
			}
			catch (UnknownHostException e)
			{
				System.err.println("Trying to connect to unknown host: " + e);
			}
			catch (IOException e)
			{
				System.err.println("IOException: " + e);
			}
         catch (ClassNotFoundException e)
         {
            System.err.println("ClassNotFoundException: " + e);

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
            
            
               System.out.println("(Receive:Responce2File): " + line );
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
                  
                  default:
                     sendMessage("ERROR: DEFAULT!");
               
               }

               
               //AttemptSleep(1);
            
         }
         catch (Exception e)
         {
            System.out.print("Exception thown." + e);
         }
      }
   }
   
   private void sendMessage(String message)
   {
      try
      {
         outputStream.writeBytes( message + "\n" );
      }
      catch (IOException e)
		{
			System.err.println("IOException:  " + e);
      }
   }
   
   private String receiveMessage() 
   {
      try 
      {
         return inputStream.readLine();
         
      } 
      catch (IOException e)
		{
			System.err.println("IOException:  " + e);
         connected = false;
         return null;
      }
   }
   
   private void sendKart()
   {
      try
      {
         // write object to stream
         outputObject.writeObject(ServerMain.world.GetKarts()[index]);
   
         // send it
         outputObject.flush();
      }
		catch (IOException e)
		{
			System.err.println("IOException:  " + e);
         connected = false;
		}
   }
   
   private void receiveKart()
   {
      try
      {
         //Collect kart
         RaceKart currentKart = (RaceKart) inputObject.readObject();
         
         //Place into world
         ServerMain.world.GetKarts()[index] = currentKart;
      }
      catch (ClassNotFoundException e)
      {
         System.err.println("Class not found: " + e);
      }
		catch (IOException e)
		{
			System.err.println("IOException:  " + e);
         connected = false;
		}
   }
   
   private void sendControl()
   {
      try
      {
         // write object to stream
         outputObject.writeObject(ServerMain.world.GetControls()[index]);
      
         // send it
         outputObject.flush();
      }
		catch (IOException e)
		{
			System.err.println("IOException:  " + e);
         connected = false;
		}
   }
   
   private void receiveControl()
   {
      try
      {
         //Collect control
         byte[] currentControl = (byte[]) inputObject.readObject();
         
         //Place into world
         ServerMain.world.GetControls()[index] = currentControl;
      }
      catch (ClassNotFoundException e)
      {
         System.err.println("Class not found: " + e);
      }
		catch (IOException e)
		{
			System.err.println("IOException:  " + e);
         connected = false;
		}
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