package coords;

import Geom.Point3D;

public class MyCoords {
	private final int RadiusEarth = 6371000;
	
	/**
	 * @param p: convert him from degrees to radian
	 * @return new point of radians
	 */
	private Point3D d2r(Point3D p) {
		return new Point3D(Point3D.d2r(p.x()),Point3D.d2r(p.y()),p.z());
	}
	
	/**
	 * computes the distance between 2 gps points
	 * @param gps0 is the first gps point given
	 * @param gps1 is the second gps point given
	 * @return distance between the two points
	 */
	public double distance3d(Point3D gps0, Point3D gps1) {
		if(isValid_GPS_Point(gps0)&&isValid_GPS_Point(gps1)) {
		Point3D gps0Radians = d2r(gps0);
		Point3D gps1Radians = d2r(gps1);		
		double diffX = gps1Radians.x() - gps0Radians.x();
		double diffY = gps1Radians.y() - gps0Radians.y();		
		double lat = Math.sin(diffX)*RadiusEarth;
		double lon = Math.sin(diffY)*RadiusEarth*Math.cos(gps0Radians.x());	
		return Math.sqrt(Math.pow(lat, 2) + Math.pow(lon, 2) );
	
		}
		return 0;
	}
	
	/**
	 * computes the polar representation of the 3D vector be gps0-->gps1 Note: this
	 * method should return an azimuth (aka yaw), elevation (pitch), and distance
	 */
	public double[] azimuth_elevation_dist(Point3D gps0, Point3D gps1) {
		if (isValid_GPS_Point(gps0) && isValid_GPS_Point(gps0)) {
			double[] ans = new double[3];

			// calculate azimuth
			double x0 = Point3D.d2r(gps0.x());
			double x1 = Point3D.d2r(gps1.x());
			double dY = Point3D.d2r(gps1.y() - gps0.y());

			double x = Math.sin(dY) * Math.cos(x1);
			double y = Math.cos(x0) * Math.sin(x1) - Math.sin(x0) * Math.cos(x1) * Math.cos(dY);
			double azimuth = Math.atan2(x, y);

			if (Point3D.r2d(azimuth) < 0)
				azimuth = 360 + Math.toDegrees(azimuth);
			else
				azimuth = Math.toDegrees(azimuth);

			double elevation = Point3D.r2d(Math.acos((gps1.z() - gps0.z()) / distance3d(gps0, gps1)));

			ans[0] = azimuth;
			ans[1] = elevation;
			ans[2] = distance3d(gps0, gps1);

			return ans;
		}
		return null;
	}
	
	/**
	 * return true if this point is a valid lat, lon , lat coordinate:[-180,+180],[-90,+90],[-450, +inf]
	 * @param p -point
	 * @return true if this point is a valid point
	 */
	public boolean isValid_GPS_Point(Point3D p) {
		return (p.x() >= (-180) && p.x() <= 180) && (p.y() >= (-90) && p.x() <= 90) && (p.z() >= (-450));
	}

}
