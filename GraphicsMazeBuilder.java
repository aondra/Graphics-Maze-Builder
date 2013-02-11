// Ashton Ondra 
// CSE 373 Homework 4- Maze Builder 
// 11/10/2011
// 
// This is a MazeBuilder class that uses our MyDisjSets implementation of 
// a disjoint set in order to build a randomized maze with desired dimensions.
// This maze is then graphically drawn onto the screen using JFrame.

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;


public class GraphicsMazeBuilder extends JFrame{

	protected int height;
	protected int width;
	protected ArrayList<Edge> edges;
	protected int rest = 0;
	protected node root;
	protected int var = 10; // determines the size of each edge

	public class node{

		public Point location;
		public node up;
		public node left;
		public node right;
		public node down;
		

		public node(){
			this(null, null, null, null,null);

		}

		public node(Point p){
			this(null,null,null,null,p);
		}

		public node(node up, node left, node right, node down, Point p){
			this.up = up; 
			this.left = left;
			this.right = right;
			this.down = down;
			this.location = p;
		}
	}
	// Inner Edge class
	// This class is used to define edges in the form of (x, y) (edge between location x and location y).
	public static class Edge{

		int x;
		int y;

		// Initializes a new edge between x and y
		public Edge(int x, int y){

			this.x = x;
			this.y = y;
		}

		// prints out the edge in a string of the form: "{ x, y }"
		public String toString(){
			return "{" + x + ", " + y + "}";

		}

		// Checks to determine whether two edges are equal
		// Two edges are classified as equal if they both contain the same
		// two locations, IE (x,y) would equal (y,x)
		public boolean isEqual(Edge other){
			return this.x == other.x && this.y == other.y || this.x == other.y && this.y == other.x;

		}


	}


	// Draws our maze to our java graphics panel
	public void paint(Graphics g){

		
		int starter = 50; // determines the starting location for your maze
		g.drawLine(starter, starter, starter + width * var, starter);
		g.drawLine(starter, starter + var, starter, starter + height * var);
		g.drawLine(starter, starter+ height * var, starter + width*var, starter + height*var);
		g.drawLine(starter + width*var, starter, starter+width*var, starter+ (height -1 )*var);
		g.setColor(Color.RED);
		g.drawString("Ashton Ondra", 2 * starter + width * var, starter + var);
		g.setColor(Color.black);
		int currentY = -1;


		for (int j = 0; j < height; j++) {
			currentY++;
			if (j != 0){

				for (int i = 0; i < width; i++) {
					int loc = j * width + i;

					int locX = starter + ((loc%width)) *var;
					int locY = starter + (currentY)*var;


					if (containsEdge(edges, new Edge(loc, loc - width))) {
						g.drawLine(locX, locY, locX + var, locY );

					}

					try {
						Thread.sleep(rest);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}

			for (int i = 0; i < width; i++) {
				int loc = j * width + i;

				int locX = starter + ((loc%width) +1) *var;
				int locY = starter + (currentY)*var;


				if (containsEdge(edges, new Edge(loc, loc + 1))) {

					g.drawLine(locX, locY, locX, locY + var);

				}

				try {
					Thread.sleep(rest);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		}

	}
	
	/**
	   * This method initializes our GraphicsMazeBuilder 
	   * @param height: The height of the maze we'd like to make. 
	   * @param width: The width of the maze we'd like to make.
	   * @param edges: An ArrayList containing all edges within the maze.
	   */
	public GraphicsMazeBuilder(int height, int width, ArrayList<Edge> edges){
		this.height = height;
		this.width = width;
		this.edges = edges;
		setBackground(Color.WHITE);
		setTitle("Maze");
		setSize(height*var + 100, width*var + 100);
		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}



	/**
	   * The main method draws our maze using an adaption of the Kruskal algorithm for finding Minimal Spanning Trees in graphs (Where I take the grid cells as vertices and the walls as edges).
	   * In order for our maze to be valid, we must have a height and width larger than 0, every location must be reachable by any other location, and no cycles can exist. 
	   * @param args[0]: The height of the maze we'd like to make. 
	   * @param args[1]: The width of the maze we'd like to make.
	   * @throws java.lang.NullPointerException reports any problems trying to execute process
	   */
	public static void main (String[] args){

		int height =Integer.parseInt(args[0]);
		int width = Integer.parseInt(args[1]);


		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Edge> maze = new ArrayList<Edge>();

		MyDisjSets ds = new MyDisjSets(width * height);
		for(int i = 0; i < ds.numSets(); i++){
			if (i >= width) {

				if (i % width != width - 1) {
					edges.add(new Edge(i, i + 1));
				}

				edges.add(new Edge(i,i-width));
			} else if (i < width - 1){
				edges.add(new Edge(i,i + 1));
			}
		}

		Random r = new Random();
		while(ds.numSets() > 1){
			int i = r.nextInt(edges.size());
			Edge e = edges.get(i);
			edges.remove(i);
			int x = ds.find(e.x);
			int y = ds.find(e.y);

			if(x != y){
				ds.union(x,y);
			} else {
				maze.add(e);
			}

		}
		for(int i=0; i< edges.size(); i++){
			maze.add(edges.get(i));
		}
		GraphicsMazeBuilder m = new GraphicsMazeBuilder( height, width, maze);

	}


	/**
	   * The containsEdge method checks to see whether an ArrayList of Edges contains a specific edge.
	   *
	   * @param edges: An ArrayList of Edges that we would like to check 
	   * @param edge: The specific edge we're looking for in edges.
	   * @return Boolean: true if edges actually contains edge
	   */
	private boolean containsEdge(ArrayList<Edge> edges, Edge edge){
		for(int i = 0; i < edges.size(); i++){
			if (edge.isEqual(edges.get(i))){
				return true;
			}
		}
		return false;

	}
}