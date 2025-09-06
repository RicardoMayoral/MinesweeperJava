import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame myFrame = new JFrame("Minesweeper");
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.setLocation(400, 50);
		myFrame.setSize(900, 900);

		MyPanel myPanel = new MyPanel();
		myFrame.add(myPanel);

		MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
		myFrame.addMouseListener(myMouseAdapter);
		
		int bombNum = 10; 
		int columns = myPanel.getColumnSize(); 
		int rows = myPanel.getRowsSize(); 
		CoreMinesweeper mainMinesweeper= new CoreMinesweeper(columns, rows, bombNum); 		
		myMouseAdapter.myCoreMinesweeper = mainMinesweeper; 

		myFrame.setVisible(true);
	}
}
