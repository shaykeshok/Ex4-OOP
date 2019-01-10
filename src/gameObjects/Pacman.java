package gameObjects;

import java.util.ArrayList;
import java.util.List;

import Geom.Point3D;

public class Pacman implements GameElement {
	private int id;
	private transient Point3D mikum;
	private int speed;
	private int radius;
	private List<Point3D> pathPacman;
	private int score;
	
	
	/**
	 * Create Packman element 
	 * @param _id of packman
	 * @param _startPoint the start location of the packman
	 * @param _speed of packman in meter per second
	 * @param _radius of the eating
	 * @param _orientation of the packman
	 */
	public Pacman(int _id,Point3D _startPoint,int _speed,int _radius) {
		id=_id;
		mikum=_startPoint;
		speed=_speed;
		radius=_radius;
		pathPacman=new ArrayList<Point3D>();
		score=0;
	}
	
	/*****************************Getters****************************/
	
	public Pacman(Pacman pacman) {
		id=pacman.getId();
		mikum=new Point3D(pacman.getPoint());
		speed=pacman.getAttribute()[0];
		radius=pacman.getAttribute()[1];
		pathPacman=new ArrayList<Point3D>();
		pathPacman.addAll(pacman.getPath());
	}

	/**
	 * @return the current location of packman
	 */
	public synchronized Point3D getPoint() {
		return mikum;
	}
	
	/**
	 * @return id of pacman
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the attributes of the pacman like: speed and radius
	 */
	@Override
	public int[] getAttribute() {
		int[] attribute= {speed,radius};
		return attribute;
	}
	
	/**
	 * @return path of packman
	 */
	public List<Point3D> getPath() {
		return pathPacman;
	}
	/**
	 * 
	 * @return the score of the pacman now
	 */
	public int getScore() {
		return score;
	}
	
	/***************************Setters******************************/
	/**
	 * This method set the value of packman speed
	 * @param newSpeed the new speed of packman
	 */
	public void setSpeed(int newSpeed) {
		speed=newSpeed;
	}
	
	/**
	 * This method set the value of packman radius eating
	 * @param newRadius the new radius eating of packman
	 */
	public void setRadius(int newRadius) {
		radius=newRadius;
	}
	
	/**
	 * This method set value of packman location
	 * @param point new location
	 */
	public synchronized void setPoint(Point3D point) {
		mikum=point;
	}
	
	/**
	 * Set the path eating of the pacman
	 * @param path
	 */
	public void setPath(List<Point3D> path) {
		pathPacman.clear();
		pathPacman.addAll(path);
	}
	
	/**
	 * Set the score of the pacman
	 * @param wight of the fruit equal to the score
	 */
	public void setScore(int i) {
		score+=i;
	}
	
	@Override
	public String toString() {
		return "p,"+id + "," + mikum.x()+","+mikum.y()+","+mikum.z() + "," + speed + "," + radius;
	}
	
	/**
	 * @return String of packman values
	 */
	public String stringToFile() {
		return "p,"+id + "," + mikum.y()+","+mikum.x()+","+mikum.z() + "," + speed + "," + radius;
	}

	/**
	 * This method add path to packman
	 */
	public void addPath(Point3D point) {		
		pathPacman.add(new Point3D(point));
	}
	
	/**
	 * This method clear the path of the pacman
	 */
	public void clearPath() {		
		pathPacman.clear();
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
