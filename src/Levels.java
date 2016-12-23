import java.awt.Color;

public class Levels {

	private static final String YEL = "YEL";
	private static final String GRE = "GRE";
	private static final String ORA = "ORA";
	private static final String MAG = "MAG";

	final static long STANDARD_LEVEL_TIME_ALLOTMENT = 60 * 1000;

	public static Color[][] getLevel(int level) {
		String[][] levelArray;
		switch (level) {
	        case 1:  return translateLevel(level1);
	        case 2:  return translateLevel(level2);
	        case 3:  return translateLevel(level3);
	        case 4:  return translateLevel(level4);
	        default: return new Color[0][0];
		}
	}

	public static long getLevelTimeAllotment(int level) {
		switch (level) {
        case 1:  return STANDARD_LEVEL_TIME_ALLOTMENT;
        default: return STANDARD_LEVEL_TIME_ALLOTMENT;
	}
	}
	
	private static Color[][] translateLevel(String[][] level) {
		Color[][] translation = new Color[level.length][level[0].length];
    	for (int row = 0; row < level.length; row++) {
    		for (int col = 0; col < level[0].length; col++) {
    			translation[row][col] = translateColorCode(level[row][col]);
    		}
    	}
		return translation;
	}
	
	private static Color translateColorCode(String colorCode) {
		switch (colorCode) {
	        case GRE:  	return Color.GREEN;
	        case YEL:  	return Color.YELLOW;
	        case ORA:  	return Color.ORANGE;
	        case MAG:  	return Color.MAGENTA;
	        default: 	return Color.BLACK;
		}
	}
	
	private static String[][] level1 = new String[][]{
		  { GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE },
		  { GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE },
		  { MAG, GRE, GRE, MAG, GRE, GRE, GRE, GRE, MAG, GRE, GRE, MAG },
		  { GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE },
		  { GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE }
		};
		
	private static String[][] level2 = new String[][]{
		  { MAG, MAG, MAG, MAG, MAG, MAG, MAG, MAG, MAG, MAG, MAG, MAG },
		  { ORA, ORA, ORA, ORA, ORA, ORA, ORA, ORA, ORA, ORA, ORA, ORA },
		  { YEL, YEL, YEL, YEL, YEL, YEL, YEL, YEL, YEL, YEL, YEL, YEL },
		  { GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE },
		  { GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE }
		};

	private static String[][] level3 = new String[][]{
		  { GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE },
		  { GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE },
		  { GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE },
		  { GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE },
		  { YEL, YEL, YEL, YEL, YEL, YEL, YEL, YEL, YEL, YEL, YEL, YEL }
		};
		
	private static String[][] level4 = new String[][]{
		  { GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE },
		  { GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE },
		  { GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE },
		  { GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE, GRE },
		  { YEL, YEL, YEL, YEL, YEL, YEL, YEL, YEL, YEL, YEL, YEL, YEL }
		};

}
