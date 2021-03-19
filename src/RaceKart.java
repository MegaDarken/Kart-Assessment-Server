//File: RaceKart.java
import java.io.Serializable;
import java.awt.*;

public class RaceKart implements Serializable
{
   //Constants
   static final int DIRECTIONS = 16;
   static final double TAU = Math.PI*2;
   static final double PI_CARDINAL_RATIO = DIRECTIONS/TAU;//
   static final double QUARTER_PI = Math.PI*0.5;
   
   
   static final int NORTH = 0;
   static final int NORTH_NORTH_EAST = 1;
   static final int NORTH_EAST = 2;
   static final int NORTH_EAST_EAST = 3;
   static final int EAST = 4;
   static final int SOUTH_EAST_EAST = 5;
   static final int SOUTH_EAST = 6;
   static final int SOUTH_SOUTH_EAST = 7;
   static final int SOUTH = 8;
   static final int SOUTH_SOUTH_WEST = 9;
   static final int SOUTH_WEST = 10;
   static final int SOUTH_WEST_WEST = 11;
   static final int WEST = 12;
   static final int NORTH_WEST_WEST = 13;
   static final int NORTH_WEST = 14;
   static final int NORTH_NORTH_WEST = 15;

   private final String KART_LIVERY_RED = "Red";
   private final String KART_LIVERY_GREEN = "Green";
   private final String KART_LIVERY_BLUE = "Blue";
   private final String KART_LIVERY_BOT = "Bot";

   static final String DEFAULT_LIVERY = "Bot";
   static final int DEFAULT_WEIGHT = 5;
   static final double DEFAULT_ACCELERATION = 0.5;
   static final double DEFAULT_TOP_SPEED = 50;
   static final double DEFAULT_TURNING_SPEED = Math.PI*0.05;
   static final double DEFAULT_FRICTION = 0.5;

   private final int INPUT_KEY_MATRIX_SIZE = 4;

   private final int INPUT_FORWARD = 0;
   private final int INPUT_BACKWARD = 1;
   private final int INPUT_LEFT = 2;
   private final int INPUT_RIGHT = 3;
   
   private final byte INPUT_ON_VALUE = 127;
   private final byte INPUT_OFF_VALUE = 0;

   //Attributes that should stay the same
   private String livery;
   
   private int weight;
   private double acceleration;// U/T/T
   private double top_speed;// U/T
   private double turning_speed;//Rad/T
   private double friction;//U/T/T
   
   private int carLength;
   private int carWidth;
   
   //Attributes that should change
   private float xPosition;
   private float yPosition;
   
   private float xVelocity;
   private float yVelocity;
   
   private float Bearing;
   
   
   public RaceKart(String livery, int weight, double acceleration, double top_speed, double turning_speed, int carWidth, int carLength)
   {
      this.livery = livery;
      this.weight = weight;
      this.acceleration = acceleration;
      this.top_speed = top_speed;
      this.turning_speed = turning_speed;
      
      this.friction = DEFAULT_FRICTION;
      
      this.carLength = carLength;
      this.carWidth = carWidth;
      
      this.xPosition = 0;
      this.yPosition = 0;
      
      this.xVelocity = 0;
      this.yVelocity = 0;
   }
   
   public RaceKart()
   {
      this.livery = DEFAULT_LIVERY;
      this.weight = DEFAULT_WEIGHT;
      this.acceleration = DEFAULT_ACCELERATION;
      this.top_speed = DEFAULT_TOP_SPEED;
      this.turning_speed = DEFAULT_TURNING_SPEED;
      
      this.friction = DEFAULT_FRICTION;
      
      this.xPosition = 0;
      this.yPosition = 0;
      
      this.xVelocity = 0;
      this.yVelocity = 0;

   }
   
   private void TickForwardVelocity()
   {
      this.xPosition += this.xVelocity;
      this.yPosition += this.yVelocity;
   }
   
   private void TickForwardAccelerate()
   {
      //System.out.println(Bearing);
   
      this.xVelocity += (this.acceleration * Math.sin(this.Bearing));
      this.yVelocity -= (this.acceleration * Math.cos(this.Bearing));
   }
   
   private void TickForwardDecelerate()
   {
      //System.out.println(Bearing);
   
      this.xVelocity -= (this.acceleration * Math.sin(this.Bearing));
      this.yVelocity += (this.acceleration * Math.cos(this.Bearing));
   }
   
   private void TickForwardTurnLeft()
   {
      this.Bearing -= this.turning_speed;
   }
   
   private void TickForwardTurnRight()
   {
      this.Bearing += this.turning_speed;
   }
   
   private void TickForwardFriction()
   {
      double xfriction = this.friction;
      double yfriction = this.friction;
   
      xfriction += (this.acceleration * (Math.abs(xVelocity)/top_speed));
      yfriction += (this.acceleration * (Math.abs(yVelocity)/top_speed));
   
      if (Math.abs(this.xVelocity) < xfriction)
      {
         xfriction = Math.abs(xVelocity);
      }
      
      if (Math.abs(this.yVelocity) < yfriction)
      {
         yfriction = Math.abs(yVelocity);
      }
   
      if (this.xVelocity < 0)
      {
         xfriction = -xfriction;
      }
      
      if (this.yVelocity < 0)
      {
         yfriction = -yfriction;
      }
      
      this.xVelocity -= (xfriction);
      this.yVelocity -= (yfriction);
   }
   
   public void TickForward(byte[] controls)
   {
      if (controls.length == INPUT_KEY_MATRIX_SIZE)
      {
         //Accelerate/Decelerate based on controls
         if (controls[INPUT_FORWARD] > INPUT_OFF_VALUE)
         {
            TickForwardAccelerate();
         }
         else if (controls[INPUT_BACKWARD] > INPUT_OFF_VALUE)
         {
            TickForwardDecelerate();
         }
         
         if (controls[INPUT_LEFT] > INPUT_OFF_VALUE)
         {
            TickForwardTurnLeft();
         }
         else if (controls[INPUT_RIGHT] > INPUT_OFF_VALUE)
         {
            TickForwardTurnRight();
         }

      }
   
         
      TickForwardVelocity();
      
      TickForwardFriction();
   }
   
   public float X()
   {
      return this.xPosition;
   }
   
   public float Y()
   {
      return this.yPosition;
   }
   
   public void SetPosition(float x, float y)
   {
      this.xPosition = x;
      this.yPosition = y;
   }

   public String Livery()
   {
      return this.livery;
   }
   
   public void Livery(String value)
   {
      this.livery = value;
   }

   protected double GetVelocity()
   {
      return Math.sqrt((this.xVelocity*this.xVelocity)+(this.yVelocity*this.yVelocity));
   }
   
   public int GetCardinalDirection()
   {
      this.Bearing = (float)((this.Bearing + TAU) % TAU);
      double cardinalValue = (this.Bearing * PI_CARDINAL_RATIO);
      //cardinalValue = (cardinalValue - 0.5);
      
      int output = (int)Math.round(cardinalValue);//(cardinalValue % DIRECTIONS);
      output = ((output + DIRECTIONS) % DIRECTIONS);
      
      
      //System.out.println(Bearing);
      //System.out.println(cardinalValue);
      //System.out.println(output);
      
      return output;
   }
   
   private Point CornerPoint(float x, float y)
   {
      double distance = Math.sqrt((x * x) + (y * y));
   
      double angle = Math.atan2(y, x);
      angle = (angle + TAU - this.Bearing) % TAU;
      
      double sineValue = Math.sin(angle);
      double cosineValue = Math.cos(angle);
      
      Point output = new Point((int)(distance * sineValue), (int)(distance * cosineValue));
      
      return output;
   }
   
   public Point FrontLeft()
   {
      float x = xPosition - carWidth;
      float y = yPosition - carLength;
      
      return CornerPoint(x, y);
   }
   
   public Point FrontRight()
   {
      float x = xPosition + carWidth;
      float y = yPosition - carLength;
      
      return CornerPoint(x, y);
   }
   
   public Point BackLeft()
   {
      float x = xPosition - carWidth;
      float y = yPosition + carLength;
      
      return CornerPoint(x, y);
   }
   
   public Point BackRight()
   {
      float x = xPosition + carWidth;
      float y = yPosition + carLength;
      
      return CornerPoint(x, y);
   }
   
   public boolean WithinBounding(int x, int y)
   {
      boolean found = true;
      
      //Trig effect
      /*
      
      //Bounding Box dimentions
      /*double xBoundingUpper = 
      (((this.xPosition + this.width) * cosineValue)
       - ((this.yPosition + this.length) * sineValue));
       
      double yBoundingUpper = 
      (((this.xPosition + this.width) * sineValue)
       + ((this.yPosition + this.length) * cosineValue));
       
       double xBoundingLower = 
      (((this.xPosition - this.width) * cosineValue)
       - ((this.yPosition - this.length) * sineValue));
       
      double yBoundingLower = 
      (((this.xPosition - this.width) * sineValue)
       + ((this.yPosition - this.length) * cosineValue)); */
       /*
       double xFrontLeft = (this.xPosition - this.width) * sineValue;
       double yFrontLeft = (this.yPosition - this.length) * cosineValue;
       
       double xBackRight = (this.xPosition + this.width) * sineValue;
       double yBackRight = (this.yPosition + this.length) * cosineValue;*/
      
      //Consider other point
      double xRelitive = x - this.xPosition;
      double yRelitive = y - this.yPosition;
      
      //Rotate to be relitive
      double distance = Math.sqrt((xRelitive * xRelitive) + (yRelitive * yRelitive));
      
      double angle = Math.atan2(yRelitive, xRelitive);
      angle = (angle + TAU - this.Bearing) % TAU;
      
      double sineValue = Math.sin(angle);
      double cosineValue = Math.cos(angle);
      
      xRelitive = distance * sineValue;
      yRelitive = distance * cosineValue;
      
      //Within bounding?
      found = (found && xRelitive < this.carWidth);
      found = (found && yRelitive < this.carLength);
      
      if (found)
      {
         found = (found && xRelitive > -this.carWidth);
         found = (found && yRelitive > -this.carLength);
      }
      
         
     
      //float lineA = 
      /*(((xRelitive - xBoundingUpper) * (yBoundingLower - yBoundingUpper))
       - ((yRelitive - yBoundingUpper) * (xBoundingLower - xBoundingUpper)));
       
       float lineB = 
      (((xRelitive - xBoundingUpper) * (yBoundingLower - yBoundingUpper))
       - ((yRelitive - yBoundingUpper) * (xBoundingLower - xBoundingUpper)));
       
      float lineC = 
      (((xRelitive - xBoundingLower) * (yBoundingUpper - yBoundingLower))
       - ((yRelitive - yBoundingLower) * (xBoundingUpper - xBoundingLower)));
      */
      
      return found;
   } 
   
   public boolean WithinBounding(Point point)
   {
      return WithinBounding(point.x, point.y);
   }
   
   
   public boolean WithinCircularBounding(RaceKart kart)
   {
      boolean found = false;
      
      double selfRadius = Math.sqrt((this.carWidth * this.carWidth) + (this.carLength * this.carLength));
      double otherRadius = Math.sqrt((kart.carWidth * kart.carWidth) + (kart.carLength * kart.carLength));
      
      float xDistance = (this.xPosition - kart.X());
      float  yDistance = (this.yPosition - kart.Y());

      double distanceBetween = Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));;
      
      found = distanceBetween <= (selfRadius + otherRadius);
      
      return found;
   }
   
}