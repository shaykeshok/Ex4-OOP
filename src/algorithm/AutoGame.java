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

}
