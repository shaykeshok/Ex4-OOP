package gameObjects;



import Geom.Point3D;
import game.Map;



public class Box implements GameElement{
	private int id;
	private Point3D point1, point2;
	private Map map=new Map();
	private Point3D start;
	private Point3D end;

	public Box(int _id, Point3D _point1, Point3D _point2) {
		id = _id;
		point1 = _point1;
		point2 = _point2;
		start=new Point3D(point1.x()-0.000011,point1.y()-0.000011,point1.z());
		end=new Point3D(point2.x()+0.000011,point2.y()+0.000011,point2.z());
	}
	public Point3D getStart() {
		return start;
	}
	public Point3D getEnd() {
		return end;
	}
	@Override
	public int getId() {
		return 0;
	}

	@Override
	public void sedId() {
		
	}

	@Override
	public Point3D getPoint() {	
		return point1;
	}
	
	/**
	 * The func calcute the height and the width of the box
	 * @return ans the array the contains the height and the width of the box
	 */
	public double[] getHeightWidth() {	
		Point3D pix1=map.polar2Pixel(point1);
		Point3D pix2=map.polar2Pixel(point2);
		System.out.println("pix1:"+pix1);
		System.out.println("pix2:"+pix2);
		System.out.println("Math.abs(pix1.x()-pix2.x()):"+Math.abs(pix1.x()-pix2.x()));
		System.out.println("Math.abs(pix1.y()-pix2.y():"+Math.abs(pix1.y()-pix2.y()));
		double[] ans= {Math.abs(pix1.x()-pix2.x()),Math.abs(pix1.y()-pix2.y())};
		return ans;
	}
	

	@Override
	public void setPoint() {
		
	}

	@Override
	public int[] getAttribute() {
		return null;
	}

	@Override
	public void setAttribute() {
		
	}
	
	
}
