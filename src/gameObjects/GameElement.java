package gameObjects;

import Geom.Point3D;
/**
 * Interface of object game
 * @author Shayke Shok and Omer Edut
 *
 */
public interface GameElement {

	public int getId();
	public void setId(int x);
	public Point3D getPoint();
	public void setPoint(Point3D point);
	public int[] getAttribute();
	public void setAttribute();

}
