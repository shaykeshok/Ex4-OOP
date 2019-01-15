package algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Robot.Play;
import game.Game;
import game.Map;
import gameObjects.Box;
import gameObjects.Fruit;
import gameObjects.Ghost;
import gameObjects.Pacman;
import graph.Graph;
import graph.Graph_Algo;
import graph.Node;
import Geom.Point3D;
/**
 * This class calcute the best way that the pacman should go to eat the fruits
 * @author Shayke Shok and Omer Edut
 *
 */
public class BuildPath {

	private List<Fruit> fruits;
//	private List<Point3D> boxPoints;
	private Graph lg,g;
	private List<ArrayList<String>> gamePath;
	private Map map;
	private Game game;
	private List<Point3D> pointsLst;

	public BuildPath(Game _game) {
		game = _game;
		gamePath = new ArrayList<ArrayList<String>>(); 
		fruits=game.getFruit();
		lg = new Graph();
		g = new Graph();
		map = new Map();
		addBoxsToGraph();
		pointsLst=box2arr();
		calcPaths();

	}

	/**
	 * This method calculate the shortest path between two fruits
	 */
	private void calcPaths() {

		for (int i = 0; i < fruits.size(); ++i) {
			List<Node> shortestPath = new ArrayList<Node>();
			for (int j = i + 1; j < fruits.size(); ++j) {
				Fruit f1 = fruits.get(i);
				Fruit f2 = fruits.get(j);
				if (checkCrash(f1.getPoint(), f2.getPoint())) {
					connectFruitToGraph(f1);
					connectFruitToGraph(f2);
				} else {
					Node a = new Node("F" + f1.getId());
					Node b = new Node("F" + f2.getId());
					if (!lg.exist(a)) {
						lg.add(a);
					}
					if (!lg.exist(b)) {
						lg.add(b);
					}
					lg.add(a);
					lg.add(b);
					lg.addEdge("F" + String.valueOf(f1.getId()), "F" + String.valueOf(f2.getId()),
							f1.getPoint().distance2D(f2.getPoint()));
				}
				Graph_Algo.dijkstra(g, "F" + f1.getId());
				Node dst = lg.getNodeByName("F" + f2.getId());
				
				shortestPath.add(dst);
			}
			Node minDst = null;
			double minDist = Double.MAX_VALUE;
			for (int k = 0; k < shortestPath.size(); ++k) {
				if (shortestPath.get(k).getDist() < minDist) {
					minDst = shortestPath.get(k);
					minDist = minDst.getDist();
				}
			}
			if (minDst != null) {
				gamePath.add(minDst.getPath());
			}
		}
	}

	/**
	 * This method check for all point in the graph which point can to connect to
	 * given fruit
	 * 
	 * @param fruit to connect
	 */
	private void connectFruitToGraph(Fruit fruit) {
		for (int i = 0; i < pointsLst.size(); ++i) {
			if (!checkCrash(pointsLst.get(i), fruit.getPoint())) {
				Node a = new Node(String.valueOf(i));
				Node b = new Node("F" + fruit.getId());
				if (!lg.exist(a)) {
					lg.add(a);
				}
				if (!lg.exist(b)) {
					lg.add(b);
				}
				lg.addEdge(String.valueOf(i), "F" + fruit.getId(), pointsLst.get(i).distance2D(fruit.getPoint()));
			}
		}
	}

	/**
	 * This method check if has box between two points
	 * 
	 * @param src point
	 * @param dst point
	 * @return true if has crash, else return false
	 */
	private boolean checkCrash(Point3D src, Point3D dst) {
		double m = (dst.y() - src.y()) / (dst.x() - src.x());
		boolean hasCrash = false;
		for (int i = 0; i < game.getBox().size() && !hasCrash; ++i) {
			Box b = game.getBox().get(i);
			double yc = m * getPointC(b).x() - m * src.x() + src.y();
			double ya = m * getPointA(b).x() - m * src.x() + src.y();
			double xa = (getPointA(b).y() - src.y()) / m + src.x();
			double xc = (getPointC(b).y() - src.y()) / m + src.x();
			if (yc == getPointC(b).y() || ya == getPointA(b).y() || xa == getPointA(b).x()
					|| xc == getPointC(b).x()) {
				hasCrash = true;
			}
		}
		return hasCrash;
	}

	/**
	 * This function find all the points of the boxes of the game and put them into
	 * array
	 * 
	 * @return array of the points of all the boxes
	 */
	public List<Point3D> box2arr() {
		pointsLst = new ArrayList<Point3D>();

		Point3D point1, point2;
		for (Box box : game.getBox()) {
			point1 = map.polar2Pixel(box.getStartPoint());
			point2 = map.polar2Pixel(box.getEndPoint());
			pointsLst.add(new Point3D(point1.x(), point1.y()));
			pointsLst.add(new Point3D(point1.x(), point2.y()));
			pointsLst.add(new Point3D(point2.x(), point1.y()));
			pointsLst.add(new Point3D(point2.x(), point2.y()));
		}
		return pointsLst;
	}

	private Point3D getPointA(Box box) {
		Point3D point1 = map.polar2Pixel(box.getStartPoint());
		return new Point3D(point1.x(), point1.y());
	}
	private Point3D getPointB(Box box) {
		Point3D point1 = map.polar2Pixel(box.getStartPoint());
		Point3D point2 = map.polar2Pixel(box.getEndPoint());
		return new Point3D(point1.x(), point2.y());
	}
	private Point3D getPointC(Box box) {
		Point3D point1 = map.polar2Pixel(box.getStartPoint());
		Point3D point2 = map.polar2Pixel(box.getEndPoint());
		return new Point3D(point2.x(), point1.y());
	}
	private Point3D getPointD(Box box) {
		Point3D point2 = map.polar2Pixel(box.getEndPoint());
		return new Point3D(point2.x(), point2.y());
	}

	
	/**
	 * This method add to the Graph all the points of box
	 * 
	 */
	private void addBoxsToGraph() {
		for (Box box : game.getBox()) {
			lg.add(new Node("1" + box.getId()));
			lg.add(new Node("2" + box.getId()));
			lg.add(new Node("3" + box.getId()));
			lg.add(new Node("4" + box.getId()));
			lg.addEdge("1" + box.getId(), "2" + box.getId(), getPointA(box).distance2D(getPointB(box)));
			lg.addEdge("2" + box.getId(), "c" + box.getId(), getPointB(box).distance2D(getPointC(box)));
			lg.addEdge("3" + box.getId(), "4" + box.getId(), getPointC(box).distance2D(getPointD(box)));
			lg.addEdge("4" + box.getId(), "1" + box.getId(), getPointD(box).distance2D(getPointA(box)));
		}
	}

	/**
	 * This method connect the pacman to the fruits
	 * 
	 * @param startPoint of the pacman
	 */
	private void firstStep(Point3D startPoint) {
		Node m = new Node("M");
		g.add(m);
		for (int i = 0; i < fruits.size(); ++i) {
			if (checkCrash(startPoint, fruits.get(i).getPoint())) {
				for (int j = 0; j < pointsLst.size(); ++j) {
					if (!checkCrash(startPoint, pointsLst.get(j))) {
						lg.addEdge("M", String.valueOf(i), startPoint.distance2D(pointsLst.get(i)));
					}
				}
			} else {
				lg.addEdge("M", "F" + fruits.get(i).getId(), startPoint.distance2D(fruits.get(i).getPoint()));
			}
		}
	}

	/**
	 * This method return the path for game
	 * 
	 * @return path for game
	 */
	public List<String> getGamePath() {
		List<String> game = new ArrayList<String>();
		for (int i = 0; i < gamePath.size(); ++i) {
			List<String> pathString = gamePath.get(i);
			for (int j = 0; j < pathString.size(); ++j) {
				game.add(pathString.get(j));
			}
		}
		return game;
	}

}