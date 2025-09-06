import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int INNER_CELL_SIZE = 70;
	private static final int TOTAL_COLUMNS = 9;
	private static final int TOTAL_ROWS = 9;   //Last row has only one cell (FIXED)
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public String[][] bombNumber = new String [TOTAL_COLUMNS][TOTAL_ROWS];
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	
	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}

		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //Paints the grid
			for (int y = 0; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;
			}
		}
		
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   
			for (int y = 0; y < TOTAL_ROWS; y++) {
				bombNumber[x][y] = "";
			}
		}
	}
	
	//Getters for Grid and sizes.
	public int getGridSize(){
		int size = 0;		
		size = (TOTAL_COLUMNS)*(TOTAL_ROWS);		
		return size;
	}

	public int getColumnSize() {
		return TOTAL_COLUMNS; 
	}

	public int getRowsSize() {
		return TOTAL_ROWS; 
	}
	
	//Announcement for whether the player wins or loses.
	public void announcementForWinOrLoss(boolean win) {
		if(win) {
			JOptionPane.showMessageDialog(null,"You have won!!!");
		}
		else {
			JOptionPane.showMessageDialog(null,"Sorry, I'm afraid you have lost...");

		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x1, y1, width + 1, height + 1);

		//Draw the lines of the grid.
		//By default, the grid will be 10x10 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= (TOTAL_COLUMNS); x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS)));
		}

	
		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				if ((x == 0) || (y != TOTAL_ROWS)) {
					Color c = colorArray[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
					
					//Colors of the numbers after revealing.
					String s = bombNumber[x][y];
					g.setColor(Color.GREEN);
					g.drawString(s, (x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1)+12, (y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1)+18);
				}
			}
		}
	}


	// This method helps to find the adjacent boxes that don't have a mine.
	// It is partially implemented since the verify hasn't been discussed in class
	// Verify that the coordinates in the parameters are valid.
	// Also verifies if there are any mines around the x,y coordinate
//	public static void revealAdjacent(int x, int y){ //DID NOT USE THIS METHOD, CHECK CoreMinesweeper class to find the methods used instead.
//		if((x<0) || (y<0) || (x>=9) || (y>=9)){return;}
//		//Do nothing outside the grid.
//		else {
//			colorArray[x][y] = Color.GRAY;
//			revealAdjacent(x-1, y);
//			revealAdjacent(x+1, y);
//			revealAdjacent(x, y-1);
//			revealAdjacent(x, y+1);
//			revealAdjacent(x-1, y-1);
//			revealAdjacent(x+1, y+1);
//			revealAdjacent(x+1, y-1);
//			revealAdjacent(x-1, y+1);
//			
//		}
//
//		System.out.println("Test");
//
//	}



	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS) {    //The lower left extra cell
			return x;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {   //Outside the rest of the grid (CHANGE MADE)
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell 
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return y;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {   //Outside the rest of the grid (CHANGE MADE)
			return -1;
		}
		return y;
	}
}
