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
      //No kart
      clientKart = -1;
      
      
      Karts = new RaceKart[MAXIMUM_RACEKARTS];
      controls = new byte[MAXIMUM_RACEKARTS][INPUT_KEY_MATRIX_SIZE];
      
      // for (int kart = 0; kart < MAXIMUM_RACEKARTS; kart++)
//       {
//          
//       }
      
      //Debug
      Karts[0] = new RaceKart("Red", 1, 1, 50, Math.PI*0.01);
      Karts[0].SetPosition(10, 10);
      
      Karts[1] = new RaceKart("Green", 1, 1, 50, Math.PI*0.01);
      Karts[1].SetPosition(110, 10);
      
      Karts[2] = new RaceKart("Blue", 1, 1, 50, Math.PI*0.01);
      Karts[2].SetPosition(10, 110);
      
      Karts[3] = new RaceKart("Bot", 1, 1, 50, Math.PI*0.01);
      Karts[3].SetPosition(110, 110);
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
   
   public void MoveKarts()
   {
      //For each kart in GameWorld
      for(int kart = 0; kart < MAXIMUM_RACEKARTS; kart++)
      {
         //if (kart == clientKart)
         //{
         //   controls[kart] = AssessMode.GetFrame().GetPanel().GetInputKeyMatrix();
         //}
      
         
         Karts[kart].TickForward(controls[kart]);
         
         
      }
      
   }
   
   
   public void run()
   {
      MoveKarts();
      
      
   }
   
}