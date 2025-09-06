import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;

public class MyMouseAdapter extends MouseAdapter {
private int flaggedBombs = 0;  
public CoreMinesweeper myCoreMinesweeper;
private boolean enabled = true; 

public void mousePressed(MouseEvent e) {
		if (!enabled) {
		    return;
		}
		Component c = e.getComponent();
		while (!(c instanceof JFrame)) {
			c = c.getParent();
			if (c == null) {
				return;
			}
		}
		JFrame myFrame = (JFrame) c;
		MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
		Insets myInsets = myFrame.getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		e.translatePoint(-x1, -y1);
		int x = e.getX();
		int y = e.getY();
		myPanel.x = x;
		myPanel.y = y;
		myPanel.mouseDownGridX = myPanel.getGridX(x, y);
		myPanel.mouseDownGridY = myPanel.getGridY(x, y);
		myPanel.repaint();
		int gridX = myPanel.getGridX(x, y);
		int gridY = myPanel.getGridY(x, y);
		
		if(flaggedBombs==myCoreMinesweeper.getBombCount()) {
			String bombAround; 
			int num; 
			for(int i = 0; i<myCoreMinesweeper.getBombColumn(); i++) {
				for(int j = 0; j<myCoreMinesweeper.getBombRow(); j++) {
					bombAround = myCoreMinesweeper.checkBombsArround(i, j); 
					num = Integer.parseInt(bombAround);
					if (myPanel.colorArray[i][j].equals(Color.red)) {
						myPanel.colorArray[i][j] = Color.red;
					} else if(num>0){
						myPanel.bombNumber[i][j]=(bombAround);
						myPanel.colorArray[i][j] = Color.GRAY;
					} else {
						myPanel.colorArray[i][j] = Color.GRAY;
					}
					myPanel.repaint();
				}
			}
			enabled = false;
			myPanel.announcementForWinOrLoss(true);
		}
		switch (e.getButton()) {
		case 1:		//Left mouse button

			break;
		case 3:		//Right mouse button
			//Do nothing

			if ((gridX == -1) || (gridY == -1)) {
				//Is releasing outside
				//Do nothing
			}
			Color oldColor = myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY];

			if(oldColor.equals(Color.RED)){
				Color newColor = Color.WHITE;

				myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = newColor;
				myPanel.repaint();
			}

			else if (oldColor.equals(Color.WHITE)){
				Color newColor = Color.RED;
				
				if(myCoreMinesweeper.checkBomb(gridX, gridY)){
					flaggedBombs+=1;
				}
				myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = newColor;
				myPanel.repaint();
			}

			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
}


public void mouseReleased(MouseEvent e) {
		if (!enabled) {
		    return;
		}
		switch (e.getButton()) {
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame)c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			int gridX = myPanel.getGridX(x, y);
			int gridY = myPanel.getGridY(x, y);
			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} else {
						//Released the mouse button on the same cell where it was pressed

						//On the left column and on the top row... do nothing
						//
						//On the grid other than on the left column and on the top row:
						Color oldColor = myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY];

						if(oldColor.equals(Color.RED)){
							//Do nothing. It's flagged. 
						} else {
							boolean bombed = false;
							int column = myCoreMinesweeper.getBombColumn();
							int row = myCoreMinesweeper.getBombRow();
							Color newColor = Color.GRAY; 
							String num;
							int numAround; 
							
							if(myCoreMinesweeper.checkBomb(gridX, gridY)){
								newColor = Color.black;
								for(int i = 0; i<column; i++) {
									for(int j = 0; j<row; j++) {
										if(myCoreMinesweeper.checkBomb(i, j)){ 
											bombed = true;
											if (myPanel.colorArray[i][j].equals(Color.red)) {
												myPanel.colorArray[i][j] = Color.red;
											} else {
												myPanel.colorArray[i][j] = Color.black;
											}
										} else { 
											if (myPanel.colorArray[i][j].equals(Color.red)) {
												myPanel.colorArray[i][j] = Color.red;
											} 
										}
										myPanel.repaint();
									}
								}
								//enabled = false;
								myPanel.announcementForWinOrLoss(false);

							}
							
							if(bombed){
								for(int i = 0; i<column; i++) {
									for(int j = 0; j<row; j++) {
										Color currentColor = myPanel.colorArray[i][j];

										if(currentColor.equals(Color.GRAY)){
											num = myCoreMinesweeper.checkBombsArround(i, j);
											numAround = Integer.parseInt(num);
											if(numAround>0){
												myPanel.bombNumber[i][j]=(num);
											}
										}
									}
								}								
							}
							
							if(newColor.equals(Color.GRAY)){
								num = myCoreMinesweeper.checkBombsArround(gridX, gridY);
								numAround = Integer.parseInt(num);
								
								
								if(numAround>0){
									myPanel.bombNumber[gridX][gridY]=(num);
								} else if(numAround==0) {
									String[][] emptySpaces = myCoreMinesweeper.getEmptySpaces(gridX, gridY);
									String[][] secondEmpty = null;
									
									int checked = 0; 
									
									while(checked<(81*81)) {
										for (int i = 0; i < column; i++) {
											for (int j = 0; j < row; j++) {
												if(emptySpaces[i][j]!=null) {
													if(emptySpaces[i][j]=="empty") {
														secondEmpty = myCoreMinesweeper.getEmptySpaces(i, j);
														emptySpaces = append(emptySpaces, secondEmpty);
														
														checked++; 
														
														newColor = Color.GRAY;  
													} else {
														myPanel.bombNumber[i][j]=(emptySpaces[i][j]);
														newColor = Color.GRAY;
													}
													myPanel.colorArray[i][j] = newColor;
												}
											}
										}
									}
								}
								
							}							
							myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = newColor;
							myPanel.repaint();
						}

					}
				}
			}
			myPanel.repaint();
			break;
		case 3:		//Right mouse button
			//Do nothing
			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
}

public String[][] append(String[][] a, String[][] b) {
	String[][] result = new String[a.length+b.length][]; 
	System.arraycopy(a, 0, result, 0, a.length);
	System.arraycopy(b, 0, result, a.length, b.length);	
	return result;
}

public void disableMouse(MouseEvent e) {
	Component c = e.getComponent();
	while (!(c instanceof JFrame)) {
		c = c.getParent();
		if (c == null) {
			return;
		}
	}
	JFrame myFrame = (JFrame) c;
	MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
	Insets myInsets = myFrame.getInsets();
	int x1 = myInsets.left;
	int y1 = myInsets.top;
	e.translatePoint(-x1, -y1);
	int x = e.getX();
	int y = e.getY();
	myPanel.x = x;
	myPanel.y = y;
	myPanel.mouseDownGridX = myPanel.getGridX(x, y);
	myPanel.mouseDownGridY = myPanel.getGridY(x, y);
	myPanel.repaint();	

	if(flaggedBombs==myCoreMinesweeper.getBombCount()) {
		String bombAround; 
		int num; 
		for(int i = 0; i<myCoreMinesweeper.getBombColumn(); i++) {
			for(int j = 0; j<myCoreMinesweeper.getBombRow(); j++) {
				bombAround = myCoreMinesweeper.checkBombsArround(i, j); 
				num = Integer.parseInt(bombAround);
				if (myPanel.colorArray[i][j].equals(Color.red)) {
					myPanel.colorArray[i][j] = Color.red;
				} else if(num>0){
					myPanel.bombNumber[i][j]=(bombAround);
					myPanel.colorArray[i][j] = Color.GRAY;
				} else {
					myPanel.colorArray[i][j] = Color.GRAY;
				}
				myPanel.repaint();
			}
		}
	}
	
		switch (e.getButton()) {
			case 1:		//Left mouse button
			
				break;
			case 3:		//Right mouse button
				//Do nothing
				break;
			default:    //Some other button (2 = Middle mouse button, etc.)
				//Do nothing
				break;
		}
	}
}
