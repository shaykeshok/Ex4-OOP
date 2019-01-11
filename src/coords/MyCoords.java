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
	
	public double[] azimuth_elevation_dist2(Point3D gps0, Point3D gps1) {
		// Build a new array to contain azimuth, elevation and distance
		double [] azimuth_elevation_dist= new double[3];
		// Calculates the azimuth
		double deltaY=Math.toRadians(gps1.y()-gps0.y());
		double x1=Math.toRadians(gps1.x());
		double x0=Math.toRadians(gps0.x());
		double Y= Math.sin(deltaY) * Math.cos(x1);
		double X = Math.cos(x0)*Math.sin(x1) -Math.sin(x0)*Math.cos(x1)*Math.cos(deltaY);
		double teta = Math.atan2(Y, X);
		double azimuth=(Math.toDegrees(teta)+360)%360;
	
		// Put the Azimuth value at the first place in the array
		azimuth_elevation_dist[0]=azimuth;
	
	
		double distance=distance3d(gps0,gps1);
		double deltaz=Math.toDegrees(gps1.z()-gps0.z());
		double elevation=deltaz/distance;
		azimuth_elevation_dist[1]=elevation;
	
		// Put the Distance value at the third place in the array
		azimuth_elevation_dist[2]=distance;
		return azimuth_elevation_dist;


	}
	
	/**
	 * computes the 3D vector (in meters) between two gps like points
	 * @param gps0 is the first gps point given
	 * @param gps1 is the second gps point given
	 * @return vector of the two points that one is
	 */
	public Point3D vector3D(Point3D gps0, Point3D gps1) {
		if (isValid_GPS_Point(gps0) && isValid_GPS_Point(gps0)) {		
			double lon= Math.cos(Math.toRadians(gps0.x()));		
			double x= RadiusEarth*Math.sin(Math.toRadians((gps1.x()-gps0.x())));
			double y= lon*RadiusEarth*Math.sin(Math.toRadians((gps1.y()-gps0.y())));
			double z= gps1.z()-gps0.z();			
			return new Point3D(x,y,z);		
		}
		return null;
	}
	

    public double[] azimuth_elevation_dist3(Point3D gps0, Point3D gps1) {
        int azi = 0;
        int elv = 1;
        int dis = 2;

        Point3D aziPoint = vector3D(gps0, gps1);
        double distance = distance3d(gps0, gps1);
        double height = gps1.z() - gps0.z();
        double elevation = Math.acos(height/distance);

        double azimuth = 0;
        double alpha = (Math.atan(Math.abs(aziPoint.y())/Math.abs(aziPoint.x()))*180)/Math.PI;
        if (aziPoint.y() >= 0){
            if (aziPoint.x() >= 0){
                azimuth = alpha;
            } else {
                azimuth = 180 - alpha;
            }
        } else {
            if (aziPoint.x() >= 0) {
                azimuth = 360 - alpha;
            } else {
                azimuth = 180 + alpha;
            }
        }
        double[] aed = new double[3];
        ///////////////////////////////////////////////////
        aed[azi] = azimuth;
        aed[elv] = elevation;
        aed[dis] = distance;
        return aed;
    }

}
