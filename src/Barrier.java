import java.awt.Color;

public class Barrier {
	
	private double height = 50;
	private double width = 50;
	private double currentX = 20;
	private double currentY = 20;
	private Color color = Color.WHITE;
	private boolean isPermanent = false;
	private int hitsRemaining = 1;
	private int points = 0;	
	
	public Barrier(long width, long height, double currentX, double currentY,Color color) {
		this.height = height;
		this.width = width;
		this.currentX = currentX;
		this.currentY = currentY;
		this.color = color;
		
		setValues();
	}
	
	private void decrementColor() {
		if (this.color.equals(Color.ORANGE)) {
			color = Color.YELLOW;
		}
		else if (this.color.equals(Color.YELLOW)) {
			color = Color.GREEN;
		}
	}
	
	private void setValues() {
		if (this.color.equals(Color.GREEN)) {
			points = 25;
			hitsRemaining = 1;
		}
		else if (this.color.equals(Color.YELLOW)) {
			points = 100;
			hitsRemaining = 2;
		}
		else if (this.color.equals(Color.ORANGE)) {
			points = 250;
			hitsRemaining = 3;
		}
		else if (this.color.equals(Color.MAGENTA)) {
			points = 1000;
			hitsRemaining = 1;
		}
		else {
			isPermanent = true;
			points = 100;
			hitsRemaining = 0;
		}
	}
	
	public double getLeft() {
		return (long) currentX;
	}

	public double getTop() {
		return (long) currentY;
	}

	public double getRight() {
		return (long) (currentX + width);
	}

	public double getBottom() {
		return (long) (currentY + height);
	}
	
	public double getHeight() {
		return height;
	}
	
	public double getWidth() {
		return width;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isPermanent() {
		return isPermanent;
	}

	public void setPermanent(boolean isPermanent) {
		this.isPermanent = isPermanent;
	}

	public int getHitsRemaining() {
		return hitsRemaining;
	}

	public void decrementHitsRemaining() {
		if (this.isPermanent == false) {
			this.hitsRemaining--;
			decrementColor();
		}
	}
	
	public int getPoints() {
		return points;		
	}
	
}
