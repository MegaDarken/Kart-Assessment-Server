//File: LoopingThread.java
import java.lang.*;
import java.util.concurrent.*;

public class LoopingThread implements Runnable
{
   private boolean _looping;
   
   public LoopingThread()
   {
      _looping = true;
      
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
         
      }
      
   
   }

}