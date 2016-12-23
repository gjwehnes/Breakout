
public class Paddle {

	//This should really be using inheritance from a common Sprite class; however, for those who havn't yet
	//covered inheritance, we keep things slightly simpler (and introduce a little bit of code duplication)
	
	private double height = 20;
	private double width = 100;
	private double currentX = 20;
	private double currentY = 20;
	private double velocityX = 200;        			//PIXELS PER SECOND
	private double velocityY = 0;          			//PIXELS PER SECOND
	private double accelerationX = 0;          		//PIXELS PER SECOND PER SECOND 
	private double accelerationY = 0;          		//PIXELS PER SECOND PER SECOND
	private int minimumX = 0;
	private int maximumX = 500;

	
	
	public Paddle(int width, int height, double currentX, double currentY, double velocityX, double velocityY,
			double accelerationX, double accelerationY, int minimumX, int maximumX) {
		super();
		this.height = height;
		this.width = width;
		this.currentX = currentX;
		this.currentY = currentY;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.accelerationX = accelerationX;
		this.accelerationY = accelerationY;
		this.maximumX = maximumX;
		this.minimumX = minimumX;
	}

	public double getLeft() {
		return currentX;
	}

	public double getTop() {
		return currentY;
	}

	public double getRight() {
		return (currentX + width);
	}

	public double getBottom() {
		return (currentY + height);
	}
	
	public double getHeight() {
		return height;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getVelocityX() {
		return velocityX;
	}

	public void updatePosition(int direction, long actual_delta_time) {
		//important that we set the velocity only when updating, as key events are not guaranteed to occur synchronously
		if (direction < 0) {		
			if (velocityX > -50) {
				velocityX -= 50;										
			}
			else {
				velocityX -= 10;										
			}
		}
		else if (direction > 0)
			if (velocityX < 50) {
				velocityX = 50;										
			}
			else {
				velocityX += 10;										
			}
		else {
			velocityX = (int)(velocityX * 0.9);			
		}

		if ((this.currentX > minimumX) && (velocityX < 0)) {
			this.currentX = this.currentX + (velocityX * actual_delta_time * 0.001);
		}
		else if ((this.currentX < (maximumX - width)) && (velocityX > 0)) {
			this.currentX = this.currentX + (velocityX * actual_delta_time * 0.001);
		}
	}
}
