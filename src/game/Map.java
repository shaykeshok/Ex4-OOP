package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import coords.MyCoords;
import Geom.Point3D;

public class Map {
	public String picPath;
	public Point3D rightCornerUp,leftCornerDown,rightCornerDown,leftCornerUp;
	public static final int WIDTHPIC=1433,HEIGHTPIC=642;
	private double max_x = 35.212400,max_y = 32.105740;
	private double min_x = 35.202350,min_y = 32.101900;
	private double pixel_per_radian_x,pixel_per_radian_y ;
	private double pixel_per_meter_x,pixel_per_meter_y;
	private double width_map_in_meters = 943,height_map_in_meters = 427;
	private BufferedImage mapImage;
	private int mapWidth,mapHeight;
	private MyCoords myCoords;



	/**
	 * Create Map object
	 */
	public Map() {
		try {
			mapImage = ImageIO.read(new File("images\\Ariel1.png"));
			mapWidth = mapImage.getWidth();
			mapHeight = mapImage.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
		myCoords = new MyCoords();
		pixel_per_radian_x = mapWidth/(max_x-min_x);
		pixel_per_radian_y = mapHeight/(max_y-min_y);

		pixel_per_meter_x = mapWidth/width_map_in_meters;
		pixel_per_meter_y = mapHeight/height_map_in_meters;
	}

	/**
     * Create Map object by given string path to map image
     * @param _picPath path of image
     */
	public Map(String _picPath) {
		leftCornerUp = new Point3D( 32.105394,  35.202532, 0);
		rightCornerUp = new Point3D( 32.105444,  35.212496, 0);
		leftCornerDown = new Point3D( 32.101899,  35.202447, 0);
		rightCornerDown = new Point3D( 32.101899,  35.212496, 0);
		picPath=_picPath;
	}
	
	/**
	 * Create Map object by
	 * @param _rightCornerUp limit of the map
	 * @param _leftCornerDown limit of the map
	 * @param _leftCornerUp limit of the map
	 * @param _rightCornerDown limit of the map
	 * @param _picPath path of image
	 */
	public Map(Point3D _rightCornerUp,Point3D _leftCornerDown,Point3D _leftCornerUp,Point3D _rightCornerDown,String _picPath) {
		rightCornerUp=_rightCornerUp;
		leftCornerDown=_leftCornerDown;
		leftCornerUp=_leftCornerUp;
		rightCornerDown=_rightCornerDown;
		picPath=_picPath;
	}


	/**
	 * This method convert pixels point to polar point
	 * @param x,y of pixel point to convert
	 * @return the given point in polar
	 */
	public Point3D pixel2Polar(int x,int y) {

		double xNew = ((x/pixel_per_radian_x) + min_x);
		double yNew = max_y - (y / pixel_per_radian_y);
		return new Point3D(xNew,yNew);


	}

	/**
	 * This method convert polar point to pixels point
	 * @param point to convert
	 * @return the given point in pixels
	 */
	public Point3D polar2Pixel(Point3D point) {
		double x = Math.abs(point.x() - min_x) * pixel_per_radian_x;
		double y = Math.abs(max_y - point.y()) * pixel_per_radian_y;
		return new Point3D(roundNumber(x), roundNumber(y), point.z());
	}

	/**
	 * This method rounded the pixel up or down
	 * @param n number to rounded
	 * @return number rounded
	 */
	private int roundNumber(double n){
		if ((n - (int)n) > 0.5){
			return (int)n + 1;
		}else {
			return (int)n;
		}
	}

	/**
	 * @return the map image
	 */
	public BufferedImage getImg() {
		return mapImage;
	}

	/**
	 * Get the angle between two points on screen (pixels), in degrees. <br>
	 * Note: Calculating Rescaled / non-original / non-Raw points. angle might be different with different screen sizes. unless
	 * given a Raw points as arguments.<br>
	 * the angle is in degrees and in clockwise <br>
	 * Directly Upwards = 0 degrees. <br>
	 * Directly on the Right = 90 degrees. <br>
	 * Directly Downwards = 180 degrees. <br>
	 * Directly on the Left = 270 degrees 
	 * @param p1 - point in pixels
	 * @param p2 - point in pixels
	 * @return angle in degrees
	 */
	public double getAngle(Point3D p1, Point3D p2) {
		double deltaX, deltaY, angle;
		deltaX = p2.x()-p1.x();
		deltaY = p2.y()-p1.y();

		angle = Math.toDegrees(Math.atan2(deltaX, deltaY));
		return (angle < 0)? angle+360 : angle;
	}

	/**
	 * Get the angle between two points on screen (RAW pixels), in degrees. <br>
	 * Note: calculating RAW points. <br>
	 * First the function reverting the points into a RAW-pixel points,
	 * if Raw pixel points are passed as argument, the function would still try to revert the point to RAW
	 * as it doesn't recognize which is RAW and which isn't, please provide only Non-RAW pixel coordinates.
	 * @param p1 - point in pixels
	 * @param p2 - point in pixels
	 * @return angle in degrees
	 */
	public double getAngleRaw(Point3D p1, Point3D p2) {
		double scaleFactorX=1, scaleFactorY=1;
		double deltaX, deltaY, angle;
		deltaX = (p2.x()-p1.x())/scaleFactorX;
		deltaY = (p2.y()-p1.y())/scaleFactorY;

		angle = Math.toDegrees(Math.atan2(deltaX, deltaY));
		return (angle < 0)? angle+360 : angle;
	}
	/**
	 * This method convert pixels point to polar point
	 * @param point to convert
	 * @return the given point in polar
	 */
	public Point3D pixel2Polar(Point3D point3d) {
		double x = ((point3d.x()/pixel_per_radian_x) + min_x);
		double y = max_y - (point3d.y() / pixel_per_radian_y);
		return new Point3D(x,y,point3d.z());
	}
}
