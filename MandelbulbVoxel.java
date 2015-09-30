public class MandelbulbVoxel {
	private static double range = 1.2;
	static int size = 500;
	static int maxIterations = 20;
	static double mandlePower = 8.0;
	static boolean fractalPoints[][][] = new boolean[size][size][size];
	
	public static boolean[][][] getArr() {
		calculateFractal();
		return fractalPoints;
	}
	
	public static double cartesianVal(double low, double high, int size, int offset) {
		return low+((high-low)/(double)size)*(double)offset;
	}
	
	public static int checkPoint(double cx, double cy, double cz){
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		double newx, newy, newz;
		double r, theta, phi;
		int i;
		
		for (i=0; i< maxIterations && x*x + y*y + z*z <2.0; i++) {
			double rPower;
			
			r = Math.sqrt(x*x + y*y + z*z);
			theta = Math.atan2(Math.sqrt(x*x + y*y), z);
			phi = Math.atan2(y, x);
			
			rPower = Math.pow(r, mandlePower);
			newx = rPower * Math.sin(theta*mandlePower) * Math.cos(phi*mandlePower);
			newy = rPower * Math.sin(theta*mandlePower) * Math.sin(phi*mandlePower);
			newz = rPower * Math.cos(theta*mandlePower);
			
			x = newx + cx;
			y = newy + cy;
			z = newz + cz;
		}
		return i;
	}
	
	public static void calculateFractal() {
		int x,y,z;
		
		for (z=0;z<size;z++) {
			double fz = cartesianVal(-range,range,size,z);
			for (y=0;y<size;y++) {
				double fy = cartesianVal(-range,range,size,y);
				for (x=0;x<size;x++) {
					double fx= cartesianVal(-range,range,size,x);
					int val = checkPoint(fx, fy, fz);
					fractalPoints[z][y][x] = (val>=(maxIterations-1))?false:true;
				}
			}
		}
	}
}
