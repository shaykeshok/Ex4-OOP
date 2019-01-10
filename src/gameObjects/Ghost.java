package gameObjects;

import Geom.Point3D;

public class Ghost implements GameElement{
	private int id,speed,radius;
	private Point3D pos;
	
	public Ghost(int _id,Point3D point,int _speed,int _radius) {
		id=_id;
		pos=point;
		speed=_speed;
		radius=_radius;
	}
	
	
	/*****************************Getters****************************/
	
	/**
	 * @return the current location of the ghost
	 */
	public synchronized Point3D getPoint() {
		return pos;
	}
	
	/**
	 * @return id of the ghost
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the attributes of the ghost like: speed and radius
	 */
	public int[] getAttribute() {
		int[] attribute= {speed,radius};
		return attribute;
	}
	
	/***************************Setters******************************/
	/**
	 * This method set the value of the ghost speed
	 * @param newSpeed the new speed of ghost
	 */
	public void setSpeed(int newSpeed) {
		speed=newSpeed;
	}
	
	/**
	 * This method set the value of the ghost radius eating
	 * @param newRadius the new radius eating of the ghost
	 */
	public void setRadius(int newRadius) {
		radius=newRadius;
	}
	
	/**
	 * This method set value of the ghost location
	 * @param point new location
	 */
	public synchronized void setPoint(Point3D point) {
		pos=point;
	}


	@Override
	public void sedId() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setPoint() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setAttribute() {
		// TODO Auto-generated method stub
		
	}
	
	
}
