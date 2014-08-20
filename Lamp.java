import java.util.Random;

import javax.swing.JOptionPane;


public class Lamp {

	private int[][] CHANGED; //represents the number of time cell[x][y] changed states
	private boolean CONFIG = false; //whether the grid is in a state of configuration or not
	private Grid GRID;
	private Color WHITE = new Color(255,255,255);
	private Color BLACK = new Color(0,0,0);
	private Color GREEN = new Color(0,150,0);
	private Color RED = new Color(255,0,0);
	private Color BLUE = new Color(0,0,255);
	private Color GOLD = new Color(255,215,0);
	private Random RAND = new Random();
	
	private void setup(){
		String rows = JOptionPane.showInputDialog("Number of Rows (at least 4)");
		int numRows = Integer.parseInt(rows);
		String cols = JOptionPane.showInputDialog("Number of Columns (at least 4)");
		int numCols = Integer.parseInt(cols);
		if(numRows <4 || numCols < 4){
			numRows = 4;
			numCols = 4;
		}
		GRID = new Grid(numRows, numCols +1, WHITE); //the last column is for the options
		GRID.setLineColor(BLACK);
		GRID.setTextColor(RED);
		for(int i=0; i <GRID.getNumRows(); i++){
			GRID.setColor(new Location(i,GRID.getNumCols()-1), GREEN);
		}
		//reset, random, start config, end config button
		GRID.setText(new Location(0,GRID.getNumCols()-1), "RESET");
		GRID.setText(new Location(1,GRID.getNumCols()-1), "RANDOM");
		GRID.setText(new Location(2,GRID.getNumCols()-1), "START CONFIG");
		GRID.setText(new Location(3,GRID.getNumCols()-1), "END CONFIG");
		//-----set up the CHANGED array
		CHANGED = new int[GRID.getNumRows()][GRID.getNumCols()-1];
		//display CHANGED(i,j) in each cell
		for(int i=0;i < CHANGED.length;i++){
			for(int j =0; j< CHANGED[0].length;j++){
				GRID.setText(new Location(i,j), CHANGED[i][j]+"");
			}
		}
	}
	/*
	 * Sets all cells to WHITE
	 */
	private void reset(){
		for(int i =0;i<GRID.getNumRows(); i++){
			for(int j=0; j<GRID.getNumCols()-1; j++){
				GRID.setColor(new Location(i,j), WHITE);
				CHANGED[i][j] = 0;
				GRID.setText(new Location(i,j), CHANGED[i][j]+"");
			}
		}
	}
	/*
	 * Randomly sets a cell to white or black
	 */
	private void random(){
		for(int i =0;i<GRID.getNumRows(); i++){
			for(int j=0; j<GRID.getNumCols()-1; j++){
				int n = RAND.nextInt(2);
				if(n ==0)
					GRID.setColor(new Location(i,j), WHITE);
				else
					GRID.setColor(new Location(i,j), BLACK);
				CHANGED[i][j] = 0;
				GRID.setText(new Location(i,j), CHANGED[i][j]+"");
			}
		}
	}
	/*
	 * Invert the cells in loc's row and col
	 */
	private void invert(Location loc){
		//invert the row
		for(int i=0; i<GRID.getNumCols()-1;i++){
			if(GRID.getColor(new Location(loc.getRow(),i)).equals(WHITE))
				GRID.setColor(new Location(loc.getRow(),i), BLACK);
			else
				GRID.setColor(new Location(loc.getRow(),i), WHITE);
			CHANGED[loc.getRow()][i]++;
			GRID.setText(new Location(loc.getRow(),i), CHANGED[loc.getRow()][i]+"");
		}
		//invert col, make sure not to invert loc (since it was inverted already)
		for(int i=0; i<GRID.getNumRows();i++){
			if(i == loc.getRow())
				continue;
			if(GRID.getColor(new Location(i,loc.getCol())).equals(WHITE))
				GRID.setColor(new Location(i,loc.getCol()), BLACK);
			else
				GRID.setColor(new Location(i, loc.getCol()), WHITE);
			CHANGED[i][loc.getCol()]++;
			GRID.setText(new Location(i,loc.getCol()), CHANGED[i][loc.getCol()]+"");
		}
	}
	/*
	 * Enters config mode
	 */
	private void configStart(){
		CONFIG = true;
		GRID.setColor(new Location(2, GRID.getNumCols()-1), GOLD);
	}
	/*
	 * Ends configuration mode
	 */
	private void configEnd(){
		CONFIG = false;
		for(int i =0; i<GRID.getNumRows();i++){
			for(int j=0; j< GRID.getNumCols()-1;j++){
				CHANGED[i][j] = 0;
				GRID.setText(new Location(i,j), CHANGED[i][j]+"");
			}
		}
		GRID.setColor(new Location(2, GRID.getNumCols()-1), GREEN);
	}
	/*
	 * Inverts the cell at location l, does not update CHANGED array
	 */
	private void invertCell(Location l){
		if(GRID.getColor(l).equals(WHITE)){
			GRID.setColor(l, BLACK);
		}else
			GRID.setColor(l, WHITE);
	}
	private void execute(){
		Location clicked;
		while(true){
			GRID.pause(100);
			clicked = GRID.checkLastLocationClicked();
			if(clicked==null)
				continue;
			//System.out.println(clicked.toString());
			if(clicked.equals(new Location(0,GRID.getNumCols()-1))){//reset
				reset();
			}else if(clicked.equals(new Location(1,GRID.getNumCols()-1))){ //random
				random();
			}else if(clicked.equals(new Location(2,GRID.getNumCols()-1))){ //start config
				if(!CONFIG){
					//if we're not already in configuration mode
					configStart();
				}
			}else if(clicked.equals(new Location(3,GRID.getNumCols()-1))){
				if(CONFIG){
					//if we're in config mode
					configEnd();
				}
			}else if(clicked.getCol() == GRID.getNumCols()-1){
				//last row not valid
				continue;
			}else{
				//clicked on a valid cell
				if(CONFIG){
					invertCell(clicked);
				}else
					invert(clicked); //invert row and column
			}
		}
	}
	public Lamp(){
		setup();
		execute();
	}
	public static void main(String[] args) {
		new Lamp();
	}
}
