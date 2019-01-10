package gameObjects;

import Geom.Point3D;

public interface GameElement {

	public int getId();
	public void sedId();
	public Point3D getPoint();
	public void setPoint();
	public int[] getAttribute();
	public void setAttribute();

}
