package gui;

import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Robot.Play;
import coords.MyCoords;
import game.Game;
import game.Map;
import gameObjects.Box;
import gameObjects.Fruit;
import gameObjects.Ghost;
import gameObjects.Pacman;
import Geom.Point3D;

public class MainWindow extends JFrame implements MouseListener {
	private MenuBar menuBar;
	private Menu fileMenu, gameMenu, createGame;
	private MenuItem openItem, saveItem, clearItem, pacmanItem, playGameItem;
	private Game game;
	private Image pacman, fruit, ghost;
	public BufferedImage myImage;
	private boolean play = false, setMyPacman = false;
	private Map map;
	JPanel jPanel;
	private int x = -1, y = -1;
	private Play play1;
	private MyCoords myCoords;
	private double dir = 0;
	private String id1, id2, id3;

	public MainWindow(String _id1, String _id2, String _id3) {
		id1 = _id1;
		id2 = _id2;
		id3 = _id3;
		jPanel = new JPanel() {
			public void paint(Graphics g) {
				g.drawImage(map.getImg(), 0, 0, this);

				for (Pacman pacmanO : game.getPacman()) {
					Point3D pix = map.polar2Pixel(pacmanO.getPoint());
					g.drawImage(pacman, pix.ix(), pix.iy(), 30, 30, this);
				}
				for (Fruit fruitO : game.getFruit()) {
					Point3D pix = map.polar2Pixel(fruitO.getPoint());
					g.drawImage(fruit, pix.ix(), pix.iy(), 30, 30, this);
				}
				for (Ghost ghostO : game.getGhosts()) {
					Point3D pix = map.polar2Pixel(ghostO.getPoint());
					g.drawImage(ghost, pix.ix(), pix.iy(), 30, 30, this);
				}
				for (Box boxO : game.getBox()) {
					Point3D pix = map.polar2Pixel(boxO.getPoint());
					double[] arr = boxO.getHeightWidth();
					g.drawRect( pix.ix(), (int) pix.y(), (int) arr[0], (int) arr[1]);
				}
			}
		};
		getContentPane().add(jPanel);
		game = new Game();
		map = new Map();
		try {
			pacman = ImageIO.read(new File("icons\\pacman.png"));
			fruit = ImageIO.read(new File("icons\\pineapple.png"));
			ghost = ImageIO.read(new File("icons\\ghost.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		myCoords = new MyCoords();
		initGUI();
		this.addMouseListener(this);
		createActions();

	}

	/**
	 * Init the gui
	 */
	private void initGUI() {
		// set file menu
		menuBar = new MenuBar();
		fileMenu = new Menu("File");
		openItem = new MenuItem("Open csv file");
		saveItem = new MenuItem("Save file to csv");

		menuBar.add(fileMenu);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		this.setMenuBar(menuBar);

		// set game menu
		gameMenu = new Menu("Game options");
		createGame = new Menu("Create new game");
		clearItem = new MenuItem("Clear game");
		pacmanItem = new MenuItem("Set my pacman location");
		playGameItem = new MenuItem("start game");
		createGame.add(pacmanItem);
		gameMenu.add(createGame);
		gameMenu.add(clearItem);
		gameMenu.add(playGameItem);
		menuBar.add(gameMenu);
	}

	@Override
	public void mouseClicked(MouseEvent arg) {
		System.out.println("mouse Clicked");
		System.out.println("(" + arg.getX() + "," + arg.getY() + ")");
		x = arg.getX();
		y = arg.getY();
		Point3D point = new Point3D(map.pixel2Polar(x, y));
		if (setMyPacman) {
			setMyPacmanPos(point);
			play1.setInitLocation(point.x(), point.y());
			jPanel.repaint();

			setMyPacman = false;
			// pacmanItem.hide();
		} else {
			if (play) {
				double[] arr = myCoords.azimuth_elevation_dist(game.getPacman().get(game.getindexOfM()).getPoint(),
						point);
				dir = arr[0];
			}
		}
	}

	/**
	 * 
	 * set the new location of my pacman
	 * 
	 * @param x2 point x
	 * @param y2 point y
	 */
	private void setMyPacmanPos(Point3D point) {
		if (x != -1 && y != -1) {
			game.getPacman().get(game.getindexOfM()).setPoint(point);
		}
	}

	public void createActions() {
		pacmanItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMyPacman = true;
			}

		});
		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("open file");
				try {
					String pth = readFileDialog();
					play1 = new Play(pth);
					putid();

					getBoardData();
					jPanel.repaint();

					System.out.println();
					System.out.println("Init Player Location should be set");
				} catch (FileNotFoundException e1) {
					System.out.println("File not found");
				}
			}

		});
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("save file");
				String pth = writeFileDialog();
				/*
				 * try { game.saveGame(pth); } catch (IOException ex) {
				 * System.out.print("Error writing file  " + ex); }
				 */
			}
		});
		playGameItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("start game..");
				play1.start();
				play = true;
				x = y = -1;
				new Thread(new Runnable() {
					public void run() {
						int i = 0;
						while (play1.isRuning()) {
							i++;
							play1.rotate(dir);
							System.out.println("****** " + i + " ******");
							String info = play1.getStatistics();
							System.out.println(info);
							getBoardData();
							jPanel.repaint();

							System.out.println("end run: " + i);
						}
						play = false;
					}
				}).start();

			}
		});
		clearItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println("clear map");
				game.clear();
				x = y = -1;
				jPanel.repaint();

			}

		});
	}

	/**
	 * method the send the ids to the server
	 */
	private void putid() {
		if (id1 == null || id1.isEmpty())
			id1 = "0";
		if (id2 == null || id2.isEmpty())
			id2 = "0";
		if (id3 == null || id3.isEmpty())
			id3 = "0";
		play1.setIDs(Long.parseLong(id1), Long.parseLong(id2), Long.parseLong(id3));
	}

	/**
	 * Method that read file from the computer
	 * 
	 * @throws FileNotFoundException
	 */
	public String readFileDialog() throws FileNotFoundException {
		// try read from the file

		FileDialog fd = new FileDialog(this, "Open csv file", FileDialog.LOAD);
		fd.setFile("*.csv");
		fd.setDirectory("C:\\");
		fd.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".csv");
			}
		});
		fd.setVisible(true);
		String folder = fd.getDirectory();
		String fileName = fd.getFile();
		return folder + fileName;

	}

	/**
	 * get the game-board data
	 */
	private void getBoardData() {
		game.clear();
		ArrayList<String> board_data = play1.getBoard();
		for (int i = 0; i < board_data.size(); i++) {
			game.addString(board_data.get(i));
			System.out.println(board_data.get(i));
		}
	}

	/**
	 * Method that write file to the computer
	 * 
	 * @return path that the user want to save the file
	 */
	public String writeFileDialog() {
		// try write to the file
		FileDialog fd = new FileDialog(this, "Save the game", FileDialog.SAVE);
		fd.setFile("*.csv");
		fd.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".csv");
			}
		});
		fd.setVisible(true);
		String folder = fd.getDirectory();
		String fileName = fd.getFile();
		return folder + fileName;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}