//File: LoopingThread.java
import java.lang.*;
import java.util.concurrent.*;

public class LoopingThread implements Runnable
{
   //Constant(s)
   private final int TICKS_PER_SECOND = 30;

   private boolean _looping;
   private int _sleepDuration;
   
   public LoopingThread()
   {
      _looping = true;
      _sleepDuration = Math.round(1000 / TICKS_PER_SECOND);
      
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
   

   public void run()
   {
   
      //Loopfor duration of runtime
      while(_looping)
      {
      
         //Process World
         Thread worldThread = new Thread(ServerMain.world);
         
         worldThread.start();
         
         //Sleep
         AttemptSleep(_sleepDuration);
      }
      
   
   }

}