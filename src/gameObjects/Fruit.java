package gameObjects;

import Geom.Point3D;

public class Fruit implements GameElement{
	private int id;
	private Point3D mikum;
	private int weight;
	public long eatTime;
	
	
	/**
	 * Create Fruit object
	 * @param _id of fruit
	 * @param _startPoint is the location of fruit
	 * @param _weight value of the fruit
	 */
	public Fruit(int _id,Point3D _startPoint,int _weight) {
		id=_id;
		mikum=_startPoint;		
		weight=_weight;
		eatTime=0;
	}
	
	/**************************getters******************************/
	
	/**
	 * @return the location Fruit
	 */
	public Point3D getPoint() {
		return mikum;
	}
	/**
	 * @return the id of Fruit
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the attribute Fruit
	 */
	@Override
	public int[] getAttribute() {
		int[] attribute={weight};
		return attribute;
	}
	
	/**
	 * @return the time that the pacman eat this fruit
	 */
	public long getEatTime() {
		return eatTime;
	}
	
	/*************************setters*********************************/
	
	
	public String toString() {
		return "f,"+id + "," + mikum.x()+","+mikum.y()+","+mikum.z() + "," + weight;
	}
	
	/**
	 * set the time that the pacman eat this fruit
	 * @param time of eating
	 */
	public void setEatTime(long time) {
		eatTime=time;
	}
	
	/**
	 * @return string data of Fruit
	 */
	public String stringToFile() {
		return "f,"+id + "," + mikum.y()+","+mikum.x()+","+mikum.z() + "," + weight;
	}

	/**
	 * This function set a new id to the fruit
	 */
	@Override
	public void setId(int x) {
		id=x;
	}

	/**
	 * This function set a new point to the fruit
	 */
	@Override
	public void setPoint(Point3D point) {
		mikum=point;
	}

	@Override
	public void setAttribute() {
	}
}
