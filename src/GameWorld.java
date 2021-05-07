//File: GameWorld.java
import java.lang.*;

public class GameWorld implements Runnable
{
   //Constants
   private final int MAXIMUM_RACEKARTS = 4;

   private final int INPUT_KEY_MATRIX_SIZE = 4;
   
   

   //Game Objects
   private RaceKart[] Karts;
   private int clientKart;
   
   private byte[][] controls;
   
   public GameWorld()
   {
      //Fetch client kart from server
      
      
      Karts = new RaceKart[MAXIMUM_RACEKARTS];
      controls = new byte[MAXIMUM_RACEKARTS][INPUT_KEY_MATRIX_SIZE];
      
      // for (int kart = 0; kart < MAXIMUM_RACEKARTS; kart++)
//       {
//          
//       }
      
      //Debug
      //Karts[0] = new RaceKart("Red", 1, 1, 50, Math.PI*0.05, 10, 20);
      //Karts[0].SetPosition(210, 210);
      
      //Karts[1] = new RaceKart("Green", 1, 1, 50, Math.PI*0.05, 10, 20);
      //Karts[1].SetPosition(110, 210);
      
      //Karts[2] = new RaceKart("Blue", 1, 1, 50, Math.PI*0.05, 10, 20);
      //Karts[2].SetPosition(210, 110);
      
      //Karts[3] = new RaceKart("Bot", 1, 1, 50, Math.PI*0.05, 10, 20);
      //Karts[3].SetPosition(110, 110);
   }
   
   public int GetClientKart()
   {
      return this.clientKart;
   }
   
   public RaceKart[] GetKarts()
   {
      return Karts;
   }
   
   public void SetClientKart(int value)
   {
      this.clientKart = value;
   }
   
   public byte[][] GetControls()
   {
      return this.controls;
   }
   
   public void MoveKarts()
   {
      //For each kart in GameWorld
      for(int kart = 0; kart < MAXIMUM_RACEKARTS; kart++)
      {
         
         if (Karts[kart] != null)
         {
         
            if (!Karts[kart].Collided())
            {
               Karts[kart].TickForward(controls[kart]);
            }
            
            
            //Get Kart Image
            
            //Get Corrisonding label
            
            // Apply Image to label
         }
      }
      
   }
   
   private void CheckCollsions()
   {
      
      for(int i = 0; i < Karts.length; i++)
      {
         if (Karts[i] != null)
         {
            for(int j = 0; j < Karts.length; j++)
            {
               if (Karts[j] != null)
               {
                  if(i != j)
                  {
                     //Are they close to each other
                     if(Karts[i].WithinCircularBounding(Karts[j]))
                     {
                        System.out.println("Close: " + i + " " + j);
                     
                        boolean isColliding = false;
                        
                        //For each corner
                        isColliding = (isColliding || Karts[i].WithinBounding(Karts[j].FrontLeft()));
                        isColliding = (isColliding || Karts[i].WithinBounding(Karts[j].FrontRight()));
                        isColliding = (isColliding || Karts[i].WithinBounding(Karts[j].BackLeft()));
                        isColliding = (isColliding || Karts[i].WithinBounding(Karts[j].BackRight()));
                        
                        //Collision is occouring?
                        if (isColliding)
                        {
                           System.out.println("Collision: " + i + " " + j);
                           
                           Karts[i].Collided(isColliding);
                        }
                     }
                  }
               }
            }
         }
      }
      
      
   }
   
   
   public void run()
   {
      MoveKarts();
      
      CheckCollsions();
   }
   
}