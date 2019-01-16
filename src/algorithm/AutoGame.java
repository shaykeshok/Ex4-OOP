package algorithm;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import Geom.Point3D;
import game.Game;
import game.Map;
import gameObjects.Box;
import gameObjects.Fruit;
import gameObjects.Pacman;
import graph.Graph;
import graph.Graph_Algo;
import graph.Node;

/**
 * This class calcute the best way that the pacman should go to eat the fruits
 * 
 * @author Shayke Shok and Omer Edut
 *
 */
public class AutoGame {
	private Game game;
	private Map map;
	private Point3D nearestFruit;
	private double mindistance;
	private Pacman myPacman;
	private Graph G;
	private List<Point3D> pointLst;
	private List<String> shortestPath;

	public AutoGame(Game _game) {
		game = _game;
		map = new Map();
		mindistance = Double.MAX_VALUE;
		myPacman = game.getPacman().get(game.getindexOfM());
	}

	public Point3D calcMostNearlyFruit() {
		double tempDistance;
		boolean crashing;
		for (Fruit fruit : game.getFruit()) {
			crashing = false;
			tempDistance = Double.MAX_VALUE;
			if (checkCrash(map.polar2Pixel(myPacman.getPoint()), map.polar2Pixel(fruit.getPoint())))
				tempDistance = map.polar2Pixel(myPacman.getPoint()).distance2D(map.polar2Pixel(fruit.getPoint()));
			else {
				tempDistance = getOverCrash(map.polar2Pixel(fruit.getPoint()));
				crashing = true;
			}
			if (tempDistance < mindistance) {
				mindistance = tempDistance;
				if (crashing) {
					String[] element=shortestPath.get(1).split(",");
					nearestFruit = new Point3D(Double.parseDouble(element[1]),Double.parseDouble(element[2]),Double.parseDouble(element[3]));
				}
				else
					nearestFruit = map.polar2Pixel(fruit.getPoint());
			}

		}
		return new Point3D(nearestFruit);

	}

	/**
	 * This function find all the points of the boxes of the game and put them into
	 * array
	 * 
	 * @return list of the points of all the boxes
	 */
	public void box2lst() {
		pointLst = new ArrayList<Point3D>();

		Point3D point1, point2;
		for (Box box : game.getBox()) {
			point1 = map.polar2Pixel(box.getStartPoint());
			point2 = map.polar2Pixel(box.getEndPoint());
			pointLst.add(new Point3D(point1.x(), point1.y()));
			pointLst.add(new Point3D(point1.x(), point2.y()));
			pointLst.add(new Point3D(point2.x(), point1.y()));
			pointLst.add(new Point3D(point2.x(), point2.y()));

		}

	}

	/**
	 * This function take the array of points that I create in another function and
	 * create list of lines from every box I take every box and convert it to six
	 * lines
	 * 
	 * @return list of lines of the boxes
	 */
	public List<Line2D> boxes2lines() {
		ArrayList<Line2D> linesLst = new ArrayList<Line2D>();
		Point3D point1, point2;
		for (Box box : game.getBox()) {
			point1 = map.polar2Pixel(box.getStartPoint());
			point2 = map.polar2Pixel(box.getEndPoint());

			Point3D tmp1 = new Point3D(point1.x(), point1.y());
			Point3D tmp2 = new Point3D(point1.x(), point2.y());
			Point3D tmp3 = new Point3D(point2.x(), point1.y());
			Point3D tmp4 = new Point3D(point2.x(), point2.y());

			linesLst.add(new Line2D.Double(tmp1.x(), tmp1.y(), tmp2.x(), tmp2.y()));
			linesLst.add(new Line2D.Double(tmp1.x(), tmp1.y(), tmp3.x(), tmp3.y()));
			linesLst.add(new Line2D.Double(tmp4.x(), tmp4.y(), tmp2.x(), tmp2.y()));
			linesLst.add(new Line2D.Double(tmp4.x(), tmp4.y(), tmp3.x(), tmp3.y()));
			linesLst.add(new Line2D.Double(tmp1.x(), tmp1.y(), tmp4.x(), tmp4.y()));
			linesLst.add(new Line2D.Double(tmp2.x(), tmp2.y(), tmp3.x(), tmp3.y()));
		}
		return linesLst;
	}

	/**
	 * This function check if the way from point source to point target cut one of
	 * the lines of the boxes
	 * 
	 * @param source point
	 * @param target point
	 * @return false if the way from source to target cut one of the lines of the
	 *         boxes
	 */
	public boolean checkCrash(Point3D source, Point3D target) {
		List<Line2D> linesLst = boxes2lines();
		Line2D lineSrc2Targt = new Line2D.Double(source.x(), source.y(), target.x(), target.y());

		for (Line2D line : linesLst) {
			Point3D p1 = new Point3D(line.getX1(), line.getY1());
			Point3D p2 = new Point3D(line.getX2(), line.getY2());
			if ((!source.equals(p1)) && (!source.equals(p2)) && (!target.equals(p1)) && (!target.equals(p2)))
			if (lineSrc2Targt.intersectsLine(line))
				return false;
		}
		return true;
	}

	public double getOverCrash(Point3D fruitPoint) {

		G = new Graph();
		String source = "M," + map.polar2Pixel(myPacman.getPoint()).toString();
		String target = "F," + fruitPoint.toString();
		G.add(new Node(source)); // Node pacman

		addBoxsToGraph(); // add all the kodkods of the boxes to the graph end connect the kodkods of
							// every box
		G.add(new Node(target)); // Node fruit
		connectPoint2Boxes(source); // connect pacman to all the boxes it cannot crash
		connectPoint2Boxes(target); // connect fruit to all the boxes it cannot crash

		for (Box box : game.getBox()) {
			connectPoint2Boxes("" + box.getId() + "," + getPointA(box).toString());
			connectPoint2Boxes("" + box.getId() + "," + getPointB(box).toString());
			connectPoint2Boxes("" + box.getId() + "," + getPointC(box).toString());
			connectPoint2Boxes("" + box.getId() + "," + getPointD(box).toString());
		}

		// This is the main call for computing all the shortest path from node 0 ("a")
		Graph_Algo.dijkstra(G, source);

		Node b = G.getNodeByName(target);
		System.out.println("***** Graph Demo for OOP_Ex4 *****");
		System.out.println(b);
		System.out.println("Dist: " + b.getDist());
		shortestPath = b.getPath();
		for (int i = 0; i < shortestPath.size(); i++) {
			System.out.print("," + shortestPath.get(i));
		}
		return b.getDist();
	}

	private void connectPoint2Boxes(String s) {
		int index=s.indexOf(',');
		Point3D point = new Point3D(s.substring(index+1));
		for (Box box : game.getBox()) {
			if (checkCrash(getPointA(box), point) && (!getPointA(box).equals(point)))
				G.addEdge("" + box.getId() + "," + getPointA(box).toString(), s, getPointA(box).distance2D(point));
			if (checkCrash(getPointB(box), point) && (!getPointB(box).equals(point)))
				G.addEdge("" + box.getId() + "," + getPointB(box).toString(), s, getPointB(box).distance2D(point));
			if (checkCrash(getPointC(box), point) && (!getPointC(box).equals(point)))
				G.addEdge("" + box.getId() + "," + getPointC(box).toString(), s, getPointC(box).distance2D(point));
			if (checkCrash(getPointD(box), point) && (!getPointD(box).equals(point)))
				G.addEdge("" + box.getId() + "," + getPointD(box).toString(), s, getPointD(box).distance2D(point));
		}
	}

	/**
	 * This method add to the Graph all the points of box
	 * 
	 */
	private void addBoxsToGraph() {

		for (Box box : game.getBox()) {
			String kodkodA = "" + box.getId() + "," + getPointA(box).toString();
			G.add(new Node(kodkodA));
			String kodkodB = "" + box.getId() + "," + getPointB(box).toString();
			G.add(new Node(kodkodB));
			String kodkodC = "" + box.getId() + "," + getPointC(box).toString();
			G.add(new Node(kodkodC));
			String kodkodD = "" + box.getId() + "," + getPointD(box).toString();
			G.add(new Node(kodkodD));
		}
	}

	/**
	 * This function calc one of the kodkod of box
	 * 
	 * @param box
	 * @return kodkod a
	 */
	private Point3D getPointA(Box box) {
		Point3D point1 = map.polar2Pixel(box.getStartPoint());
		return new Point3D(point1.x(), point1.y());
	}

	/**
	 * This function calc one of the kodkod of box
	 * 
	 * @param box
	 * @return kodkod b
	 */
	private Point3D getPointB(Box box) {
		Point3D point1 = map.polar2Pixel(box.getStartPoint());
		Point3D point2 = map.polar2Pixel(box.getEndPoint());
		return new Point3D(point1.x(), point2.y());
	}

	/**
	 * This function calc one of the kodkod of box
	 * 
	 * @param box
	 * @return kodkod c
	 */
	private Point3D getPointC(Box box) {
		Point3D point1 = map.polar2Pixel(box.getStartPoint());
		Point3D point2 = map.polar2Pixel(box.getEndPoint());
		return new Point3D(point2.x(), point1.y());
	}

	/**
	 * This function calc one of the kodkod of box
	 * 
	 * @param box
	 * @return kodkod d
	 */
	private Point3D getPointD(Box box) {
		Point3D point2 = map.polar2Pixel(box.getEndPoint());
		return new Point3D(point2.x(), point2.y());
	}

//	/**
//	 *  this function get to points and give the best time to go from a1 to b1
//	 * according to the given Graph
//	 */
//	public double disTime(Geom.Point3D a1,Geom.Point3D b1) {
//		Point3D[] pointsArr=box2arr();
//
//		List<Line2D> linesLst=boxes2lines();
//
//		Point3D a=new Point3D(a1.x(),a1.y());
//		Point3D b=new Point3D(b1.x(),b1.y());
//		Graph G = new Graph();
//		String source = "a";
//		String target = "b";
//		G.add(new Node(source)); // Node "a" (0)
//		for(int i=0;i<points.length;i++) {
//			Node d = new Node(""+i);
//
//			G.add(d);
//		}
//		G.add(new Node(target));
//
//		String temp="";
//		String temp1="";
//		// addnig all the edge of a
//		for (int i = 0; i < points.length; i++) {
//			if(hasLine(a,points[i])) {
//				temp=""+i;
//				G.addEdge("a",temp,a.distance2D(points[i]));
//			}
//		}
//		if(hasLine(a,b)) {
//			G.addEdge("a","b",a.distance2D(b));
//		}
//
//		// addnig all the edge of b
//		for (int i = 0; i < points.length; i++) {
//			if(hasLine(b,points[i])) {
//				temp=""+i;
//				G.addEdge("b",temp,b.distance2D(points[i]));
//			}
//		}
//
//
//		// adding all the edge of the graph
//		for (int i = 0; i < points.length; i++) {
//			for (int j = 0; j < points.length; j++) {
//				if((i!=j)&&(hasLine(points[i],points[j]))) {
//					temp=""+i;
//					temp1=""+j;
//					G.addEdge(temp,temp1,points[i].distance2D(points[j]));
//				}
//			}
//		}
//		count++;
//		Graph_Algo.dijkstra(G, source);
//		Node g = G.getNodeByName(target);
//		return g.getDist();
//	}
//  
//	/**
//	 * chack what is the nearst fruit to go in a given game - use the "disTime" fun
//	 */
//	public Geom.Point3D nearFrut() {
//		
//		double besttime=10000;
//		Geom.Point3D bestPoint= new Geom.Point3D(0.0,0.0);
//		Geom.Point3D temp_point;
//		Geom.Point3D start_point=m2.PointGps2Pix(game.getPlayers().get(0).getPoint());
//		ArrayList<Fruit>f=game.getFruits();
//		if(f.size()<2) {
//			Fruit temp_Fruit=f.get(0);
//			return temp_Fruit.getPoint();			
//		}
//		Iterator<Fruit> it2 =f.iterator();
//		Fruit temp_Fruit ;
//		while(it2.hasNext()) {
//			temp_Fruit=(Fruit)it2.next();
//			temp_point=m2.PointGps2Pix(temp_Fruit.getPoint());
//			if(hasLine(new Point3D( start_point.x(),start_point.y()),new Point3D( temp_point.x(),temp_point.y()))) {
//				if(new Point3D( start_point.x(),start_point.y()).distance2D(new Point3D( temp_point.x(),temp_point.y()))<besttime) {
//					bestPoint=  temp_point;
//					besttime=new Point3D( start_point.x(),start_point.y()).distance2D(new Point3D( temp_point.x(),temp_point.y()));
//				}
//			}
//			///
//			else {
//				if(disTime(start_point,temp_point)<besttime) {
//					bestPoint=  temp_point;
//					besttime=disTime(start_point,temp_point);
//				}
//			}
//		}
//		return bestPoint;
//	}
//
//
//	
//	/**
//	 * this is the main function - calc the best fruit to go and give the rotate for this fruit.
//	 */
//	public double rotet() {
//
//		/// this is bad part of the algorithm - not generic
//		if((game.getFruits().size()<2)&&game.getPackmans().size()!=2) {
//			
//			Geom.Point3D a1;
//			Geom.Point3D start=m2.PointGps2Pix(game.getPlayers().get(0).getPoint());
//			if(start.x()>850) {
//				 a1=m2.PointPix2Gps(new Geom.Point3D(1012,434));
//			}
//			else {
//				 a1=m2.PointPix2Gps(new Geom.Point3D(873,590));	
//			}
//			double rot=360-((game.getPlayers().get(0).getPoint().north_angle(a1)+270)%360);
//			return rot;
//
//		}
//		/// this is bad part of the algorithm - not generic
//		if((game.getFruits().size()<2)&&game.getPackmans().size()==2) {
//			
//			Geom.Point3D a1;
//			Geom.Point3D start=m2.PointGps2Pix(game.getPlayers().get(0).getPoint());
//
//				 a1=m2.PointPix2Gps(new Geom.Point3D(0,0));
//
//			double rot=360-((game.getPlayers().get(0).getPoint().north_angle(a1)+270)%360);
//			return rot;
//
//		}
//		/// this is bad part of the algorithm - not generic
//		Point3D q=new Point3D(m2.PointGps2Pix(game.getPlayers().get(0).getPoint()).x(),m2.PointGps2Pix(game.getPlayers().get(0).getPoint()).y());
//		if((game.getFruits().size()==14)&&((game.getDateName().contains("6")))&&(q.distance2D(new Point3D(1270,159))<21)) {
//			double rot=360-((game.getPlayers().get(0).getPoint().north_angle(togo())+270)%360);
//			
//			return rot;
//		}
////		if((game.getDateName().contains("7"))&&(q.distance2D(new Point3D(1270,159))<21)&&(game.getFruits().size()>4)) {
////			//double rot=360-((game.getPlayers().get(0).getPoint().north_angle(togo())+270)%360);
////			double rot=180;
////			return rot;
////		}
//		
//		///////////////////////////////////////////////////////
//		
//		Geom.Point3D a1=m2.PointGps2Pix(game.getPlayers().get(0).getPoint());
//		Geom.Point3D b1= nearFrut();
//		Point3D[] points=box2arr();
//
//		ArrayList<Line2D> lines=boxes2lines();
//		Point3D a=new Point3D(a1.x(),a1.y());
//		Point3D b=new Point3D(b1.x(),b1.y());
//		Graph G = new Graph();
//		String source = "a";
//		String target = "b";
//		G.add(new Node(source)); // Node "a" (0)
//		for(int i=0;i<points.length;i++) {
//			Node d = new Node(""+i);
//			G.add(d);
//		}
//		G.add(new Node(target));
//
//		String temp="";
//		String temp1="";
//		// addnig all the edge of a
//		for (int i = 0; i < points.length; i++) {
//			if(hasLine(a,points[i])) {
//				temp=""+i;
//			
//				G.addEdge("a",temp,a.distance2D(points[i]));
//			}
//		}
//		if(hasLine(a,b)) {
//		
//			G.addEdge("a","b",a.distance2D(b));
//		}
//
//		// addnig all the edge of b
//		for (int i = 0; i < points.length; i++) {
//			if(hasLine(b,points[i])) {
//				temp=""+i;
//
//				G.addEdge("b",temp,b.distance2D(points[i]));
//			}
//		}
//
//
//		// adding all the edge of the graph
//		for (int i = 0; i < points.length; i++) {
//			for (int j = 0; j < points.length; j++) {
//				if((i!=j)&&(hasLine(points[i],points[j]))) {
//					temp=""+i;
//					temp1=""+j;
//					G.addEdge(temp,temp1,points[i].distance2D(points[j]));
//				}
//			}
//		}
//		//Graph_Algo ga=new Graph_Algo();
//		//ga.dijkstra(G, source);
//		Graph_Algo.dijkstra(G, source);
//		Node g = G.getNodeByName(target);
//		String s=g.toString();
//		int index=0;
//		if(hasLine(a,b)) {
//			double rot=360-((game.getPlayers().get(0).getPoint().north_angle(togo())+270)%360);
//			return rot;
//		}
//		else {
//			s=g.getPath().get(1);
//			index=Integer.parseInt(s);
//		}
//		Geom.Point3D p1=m2.PointPix2Gps(new Geom.Point3D( points[index].x(),points[index].y()));
//		double rot=360-((game.getPlayers().get(0).getPoint().north_angle(p1)+270)%360);
//		return rot;
//
//	}
//
//
//
//	/**
//	 * 	if there is a fruit near to the packman without any box in the midle -> goto the nearst fruit
//	 * this is a bug -go to the fruit not to the nearest point
//	 */
//	public Geom.Point3D togo(){
//		ArrayList<Fruit>f=game.getFruits();
//		Geom.Point3D p1=m2.PointGps2Pix(game.getPlayers().get(0).getPoint());
//		Point3D player=new Point3D(p1.x(),p1.y());
//		Geom.Point3D ans=f.get(0).getPoint();
//		int index=0;
//		double dis=10000;
//		Iterator<Fruit> it2 =f.iterator();
//		Fruit temp_Fruit ;
//		while(it2.hasNext()) {
//			temp_Fruit=(Fruit)it2.next();
//			Geom.Point3D temp_point=m2.PointGps2Pix(temp_Fruit.getPoint());
//
//			Point3D target=new Point3D(temp_point.x(),temp_point.y());
//
//			if(player.distance2D(target)<dis) {
//				dis=player.distance2D(target);
//				ans=temp_point;
//			}
//
//		}
//
//		return m2.PointPix2Gps(new Geom.Point3D(ans.x(),ans.y()));
//	}

}
