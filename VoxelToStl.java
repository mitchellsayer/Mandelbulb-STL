import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class VoxelToStl {
	static int size = 256;
	static boolean[][][] voxels = MandelbulbVoxel.getArr();
	
	public static double getPos(int i) {
		return (double)i/(double)(size);
	}
	
	public static void doTris(int x0, int y0, int z0, int x1, int y1, int z1, int x2, int y2, int z2, int nx, int ny, int nz) {
		System.out.printf("facet normal %d %d %d %n outer loop %n", nx, ny, nz);
		System.out.printf("    vertex %f %f %f %n", getPos(x0), getPos(y0), getPos(z0));
		System.out.printf("    vertex %f %f %f %n", getPos(x1), getPos(y1), getPos(z1));
		System.out.printf("    vertex %f %f %f %n", getPos(x2), getPos(y2), getPos(z2));
		System.out.printf("  end loop%n  end facet %n");
	}
	
	public static void doface(int x, int y, int z, int x0, int y0, int z0, int x1, int y1, int z1, int x2, int y2, int z2, int x3, int y3, int z3, int nx, int ny, int nz) {
	  doTris(x+x0, y+y0, z+z0, x+x1, y+y1, z+z1, x+x2, y+y2, z+z2, nx,ny,nz);
	  doTris(x+x2, y+y2, z+z2, x+x3, y+y3, z+z3, x+x0, y+y0, z+z0, nx,ny,nz);
	}
	
	public static boolean getVoxel(int x, int y, int z) {
		if (x>=size || x<0) return true;
		if (y>=size || y<0) return true;
		if (z>=size || z<0) return true;
		return voxels[z][y][x];
	}
	
	public static void doVoxel(int x, int y, int z) {
		if (!getVoxel(x,y,z)) {
		    if (getVoxel(x,y,z-1))
		    	doface(x,y,z, 0,1,0, 1,1,0, 1,0,0, 0,0,0, 0,0,-1); // In the Z plane at the back  
		    if (getVoxel(x,y,z+1))
		    	doface(x,y,z, 0,0,1, 1,0,1, 1,1,1, 0,1,1, 0,0,1); // In the Z plane at the front
		    if (getVoxel(x-1,y,z))
		    	doface(x,y,z, 0,0,1, 0,1,1, 0,1,0, 0,0,0, -1,0,0); // On the left hand side
		    if (getVoxel(x+1,y,z))
		    	doface(x,y,z, 1,0,0, 1,1,0, 1,1,1, 1,0,1, 1,0,0); // On the right hand side
		    if (getVoxel(x,y-1,z))
		    	doface(x,y,z, 0,0,0, 1,0,0, 1,0,1, 0,0,1, 0,-1,0); // On the top
		    if (getVoxel(x,y+1,z))
		    	doface(x,y,z, 0,1,1, 1,1,1, 1,1,0, 0,1,0, 0,1,0); // On the bottom
		  }
	}
	
	public static void main(String[] args) {
		try {
			System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("mandelbulb.stl"))));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int x, y, z;
		
		System.out.printf("solid mandelbulb %n");
		
		for(z=0;z<size;z++) {
		    for(y=0;y<size;y++) {
		      for(x=0;x<size;x++) {
		    	  try {
		    	  	doVoxel(x,y,z);
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		    }
		}
		
		System.out.printf("endsolid mandelbulb %n");
	}
}
