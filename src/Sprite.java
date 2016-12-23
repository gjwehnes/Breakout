import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Sprite {

	private Movement movement = new Movement();

	private double SINE_OF_SIXTY = Math.sin(Math.toRadians(60));
	private double height = 50;
	private double width = 50;
	private double currentX = 20;
	private double currentY = 20;
	private double velocityX = 200;        			//PIXELS PER SECOND
	private double velocityY = 0;          			//PIXELS PER SECOND
	private double initialVelocity = 200;
	private double accelerationX = 0;          		//PIXELS PER SECOND PER SECOND 
	private double accelerationY = 0;          		//PIXELS PER SECOND PER SECOND 
	private Image image_left;
	private Image image_right;

	public Sprite(long width, long height, double currentX, double currentY, double velocityX,
			double velocityY, double accelerationX, double accelerationY) {
		this.height = height;
		this.width = width;
		this.currentX = currentX;
		this.currentY = currentY;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.accelerationX = accelerationX;
		this.accelerationY = accelerationY;
		
		initialVelocity = getTotalVelocity();
		
		try {
			this.image_left = ImageIO.read(new File("res/happy-face.png"));
		} catch (IOException e) {
		}

		try {
			this.image_right = ImageIO.read(new File("res/happy-face-right.png"));
		} catch (IOException e) {
		}


	}
	
	public double getLeft() {
		return currentX;
	}

	public void setLeft(double currentX) {
		this.currentX = currentX;
	}

	public double getTop() {
		return currentY;
	}

	public void setTop(double currentY) {
		this.currentY = currentY;
	}

	public double getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public Image getImage() {
		if (this.velocityX < 0) {
			return image_left;
		}
		else {
			return image_right;
		}
	}

	public double getAccelerationX() {
		return accelerationX;
	}

	public void setAccelerationX(double accelerationX) {
		this.accelerationX = accelerationX;
	}

	public double getAccelerationY() {
		return accelerationY;
	}

	public void setAccelerationY(double accelerationY) {
		this.accelerationY = accelerationY;
	}
	
	public double getTotalVelocity() {
		return Math.sqrt((velocityX * velocityX) + (velocityY * velocityY));
	}

	public void update(ArrayList<Barrier> barriers, Paddle paddle, long actual_delta_time ) {
		updateVelocity(actual_delta_time);
		updatePosition(barriers, paddle, actual_delta_time);
	}

    private void updateVelocity(long actual_delta_time) {
    	this.velocityX += (accelerationX * actual_delta_time * 0.001);
    	this.velocityY += (accelerationY * actual_delta_time * 0.001);
    	//System.out.printf("currentX: %f; currentY: %f; velocityX: %f; velocityY: %f\n",sprite.currentX, sprite.currentY, sprite.velocityX, sprite.velocityY );
    }

	private void updatePosition(ArrayList<Barrier> barriers, Paddle paddle, long actual_delta_time ) {
		
		//calculate new position assuming there are no changes in direction
	    double movement_x = (this.velocityX * actual_delta_time * 0.001);
	    double movement_y = (this.velocityY * actual_delta_time * 0.001);
	    double new_x = this.currentX + movement_x;
	    double new_y = this.currentY + movement_y;

	    double this_top = Math.round(this.currentY);
	    double this_bottom = this_top + this.height;
	    double this_left = Math.round(this.currentX);
	    double this_right = this_left + this.width;
		 
	    for (Barrier barrier : barriers) {
	    	
			//colliding with top edge of barrier?
	        //?moving down (can only collide if sprite is moving down)
	        if (movement_y > 0) {
	            //?is the this to the left || right of the barrier? (can only collide if this is not the case) 
	            if (!( (this_left > barrier.getRight()) || (this_right < barrier.getLeft()))) {
	                this.calculateBounce(this_bottom, movement_y, barrier.getTop(), movement,1);
	                if (movement.didCollide) {
	                	new_y = movement.newLocaton -this.height;
	                	this.velocityY = (movement.newVelocity * 1000) / actual_delta_time;
	                	barrier.decrementHitsRemaining();
	                }
	            }
	        }

	        //colliding with bottom edge of barrier?
	        //?moving up (can only collide if sprite is moving up)
	        if (movement_y < 0) {
	            //is the this to the left || right of the barrier? (can only collide if this is not the case) 
	            if (! ((this_left > barrier.getRight()) || (this_right < barrier.getLeft()))) {
	                this.calculateBounce(this_top, movement_y, barrier.getBottom(), movement,1);
	                if (movement.didCollide) {
	                	new_y = movement.newLocaton;
	                	this.velocityY = (movement.newVelocity * 1000) / actual_delta_time;
	                	barrier.decrementHitsRemaining();
	                }
	            }
	        }

	        //colliding with left edge of barrier?
	        //?moving right (can only collide if sprite is moving to right)
	        if (movement_x > 0) {
	            //?is the this above || below the barrier? (can only collide if this is not the case) 
	            if (!( (this_top > barrier.getBottom()) || (this_bottom < barrier.getTop()))) {
	                this.calculateBounce(this_right, movement_x, barrier.getLeft(), movement,1);
	                if (movement.didCollide) {
	                	new_x = movement.newLocaton - this.width;
	                	this.velocityX = (movement.newVelocity * 1000) / actual_delta_time;
	                	barrier.decrementHitsRemaining();
	                }
	            }
	        }

	        //colliding with right edge of barrier?
	        //?moving left (can only collide if sprite is moving to left)
	        if (movement_x < 0) {
	            //?is the this above || below the barrier? (can only collide if this is not the case) 
	            if (!( (this_top > barrier.getBottom()) || (this_bottom < barrier.getTop()))) {
	                this.calculateBounce(this_left, movement_x, barrier.getRight(), movement, 1);
	                if (movement.didCollide) {
	                	this.velocityX = (movement.newVelocity * 1000) / actual_delta_time;
	                	new_x = movement.newLocaton;
	                	barrier.decrementHitsRemaining();
	                }
	            }
	        }
	    }
	    
		//colliding with top edge of paddle?
        //?moving down (can only collide if sprite is moving down)
        if (movement_y > 0) {
            //?is the this to the left || right of the barrier? (can only collide if this is not the case) 
            if (!( (this_left > paddle.getRight()) || (this_right < paddle.getLeft()))) {
                this.calculateBounce(this_bottom, movement_y, paddle.getTop(), movement,1);
                if (movement.didCollide) {
                	new_y = movement.newLocaton -this.height;
                	this.velocityY = (movement.newVelocity * 1000) / actual_delta_time;
                	//change velocity of sprite in response to velocity of paddle!
                	this.velocityX = this.velocityX + paddle.getVelocityX();
                	//is the velocityX now greater than the maximum allowed?
                	if (Math.abs(this.velocityX) > Math.abs(initialVelocity * SINE_OF_SIXTY)) {
                		this.velocityX = Math.signum(this.velocityX) * initialVelocity * SINE_OF_SIXTY;
                	}
                	this.velocityY = - Math.sqrt((initialVelocity * initialVelocity) - (velocityX * velocityX));
                	System.out.println(velocityY);
                	
                	
                }
            }
        }
	    
	    
	    this.setLeft(new_x);
	    this.setTop(new_y);
                    
	}
	    
	protected void calculateBounce(double location, double velocity, double boundary, Movement movement, double energyLoss) {

		double distanceToBoundary = 0;
			
	    if ( (velocity > 0) && (location <= boundary) && ((location + velocity) >= boundary)) {
	        distanceToBoundary = (boundary - location);
	        movement.newLocaton = boundary - (velocity - distanceToBoundary);
	        movement.newVelocity = - velocity * energyLoss;
	        movement.didCollide = true;
	    }
	    else if (velocity < 0 && (location >= boundary) && ((location + velocity) <= boundary)) {
	        distanceToBoundary = (location - boundary);
	        movement.newLocaton = boundary - (velocity + distanceToBoundary);
	        movement.newVelocity = - velocity * energyLoss;
	        movement.didCollide = true;
	    }
	    else {
	    	movement.newLocaton = location + velocity;
	    	movement.newVelocity = velocity;
	    	movement.didCollide = false;
	    }
	    
	}
	
	
}
