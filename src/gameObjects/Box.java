package gameObjects;

import Geom.Point3D;

/**
 * This class represents a box in the game that represents a obstacle
 * @author Shayke Shok and Omer Edut
 *
 */
public class Box implements GameElement{
	private int id;
	private Point3D point1, point2;
	private Point3D start,end;

	public Box(int _id, Point3D _point1, Point3D _point2) {
		id = _id;
		point1 = _point1;
		point2 = _point2;
		start=new Point3D(point1.x()-0.000011,point1.y()-0.000011,point1.z());
		end=new Point3D(point2.x()+0.000011,point2.y()+0.000011,point2.z());
	}
	/**
	 * @return start point
	 */
	public Point3D getStartPoint() {
		return start;
	}
	
	/**
	 * @return end point
	 */
	public Point3D getEndPoint() {
		return end;
	}
	/**
	 * @return the id of Box
	 */
	@Override
	public int getId() {
		return id;
	}
	/**
	 * @return one of the point of the box
	 */
	@Override
	public Point3D getPoint() {	
		return point1;
	}

	@Override
	public void setPoint(Point3D point) {
	}

	@Override
	public int[] getAttribute() {
		return null;
	}

	@Override
	public void setAttribute() {
	}
	/**
	 * This function set new id to the box
	 */
	@Override
	public void setId(int x) {
		id=x;
	}
	
	
}
