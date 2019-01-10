package game;

import java.util.ArrayList;
import java.util.List;

import gameObjects.Box;
import gameObjects.Fruit;
import gameObjects.GameElement;
import gameObjects.Ghost;
import gameObjects.Pacman;
import Geom.Point3D;

public class Game {
	private List<Pacman> pacmanLst;
	private List<Fruit> fruitLst;
	private List<Ghost> ghostLst;
	private List<Box> boxLst;
	private int indexOfM=0;

	/**
	 * Create Game object
	 */
	public Game() {
		pacmanLst = new ArrayList<Pacman>();
		fruitLst = new ArrayList<Fruit>();
		ghostLst = new ArrayList<Ghost>();
		boxLst = new ArrayList<Box>();
	}

	/*************************** Getters ***************************/

	/**
	 * @return the list of the packmans in the game
	 */
	public List<Pacman> getPacman() {
		return pacmanLst;
	}

	/**
	 * @return the list of the fruits in the game
	 */
	public List<Fruit> getFruit() {
		return fruitLst;
	}

	/**
	 * @return the list of the ghosts in the game
	 */
	public List<Ghost> getGhosts() {
		return ghostLst;
	}

	/**
	 * @return the list of the boxes in the game
	 */
	public List<Box> getBox() {
		return boxLst;
	}
	/**
	 * get the index of my pacman in the list of all the pacmans
	 * @return indexOfM the index of my pacman
	 */
	public int getindexOfM() {
		return indexOfM;
	}
	/*********************************Setters*****************************/
	
	/**
	 * set the index of my pacman in the list of all the pacmans
	 * @param x the index in the list
	 */
	public void setindexOfM(int x) {
		indexOfM=x;
	}
	/*************************** Methodes ************************/

	/**
	 * This method get String of element game and convert it to element game
	 * 
	 * @param s sting of element game (packman or fruit or ghost or box)
	 */
	public void addString(String s) {
		String[] element = s.split(",");
		GameElement object;
		Point3D point=new Point3D(Double.parseDouble(element[2]),Double.parseDouble(element[3]),Double.parseDouble(element[4]));
		switch (element[0]) {
		case "p": case "P":
			object=new Pacman(Integer.parseInt(element[1]),point,(int)Double.parseDouble(element[5]),(int)Double.parseDouble(element[6]));
			pacmanLst.add((Pacman) object);
			break;
		case "m":case "M":
			object=new Pacman(Integer.parseInt(element[1]),point,(int)Double.parseDouble(element[5]),(int)Double.parseDouble(element[6]));
			pacmanLst.add((Pacman) object);
			indexOfM=pacmanLst.size()-1;
			break;
		case "f":case "F":
			object=new Fruit(Integer.parseInt(element[1]),point,(int)Double.parseDouble(element[5]));
			fruitLst.add((Fruit) object);
			break;
		case "g":case "G":
			object=new Ghost(Integer.parseInt(element[1]),point,(int)Double.parseDouble(element[5]),(int)Double.parseDouble(element[6]));
			ghostLst.add((Ghost) object);
			break;
		case "b":case "B":
			object=new Box(Integer.parseInt(element[1]),point,new Point3D(Double.parseDouble(element[5]),Double.parseDouble(element[6]),Double.parseDouble(element[7])));
			boxLst.add((Box) object);
			break;
		}
		
	}
	

	/**
	 * This method clear the game lists
	 */
	public void clear() {
		fruitLst.clear();
		pacmanLst.clear();
		ghostLst.clear();
		boxLst.clear();
	}
}
