package ttt;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ttt.Game.GameState;
import ttt.Game.Seed;



public class GameBoard extends JFrame{

	public static  int ROWS;
	public static  int COLS;
	public static final int CELL_SIZE = 100; // cell width and height (square)
	public static final int CANVAS_WIDTH = CELL_SIZE * COLS;  // the drawing canvas
	public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
	public static final int GRID_WIDTH = 8;                   // Grid-line's width
	public static final int GRID_WIDHT_HALF = GRID_WIDTH / 2; // Grid-line's half-width
	// Symbols (cross/nought) are displayed inside a cell, with padding from border
	public static final int CELL_PADDING = CELL_SIZE / 6;
	public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2; // width/height
	public static final int SYMBOL_STROKE_WIDTH = 8; // pen's stroke width
	private DrawCanvas canvas; // Drawing canvas (JPanel) for the game board
	private JLabel statusBar;  // Status Bar
	private Game gg;


	public GameBoard() {

		gg = new Game();
		this.ROWS = gg.getRows();
		this.COLS = gg.getCols();
		// The canvas (JPanel) fires a MouseEvent upon mouse-click


		// Setup the status bar (JLabel) to display status message

		initGUI();


	}

	public void initGUI() {
		canvas = new DrawCanvas();  // Construct a drawing canvas (a JPanel)
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
				handleMouseClick(e);
				// Refresh the drawing canvas
				repaint();  // Call-back paintComponent().
			}
		});
		createStatusBar();

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(canvas, BorderLayout.CENTER);
		cp.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();  // pack all the components in this JFrame
		setTitle("Tic Tac Toe");
		setVisible(true);  // show this JFrame
	}

	public void createStatusBar() {
		statusBar = new JLabel("  ");
		statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
	}

	public void handleMouseClick(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		// Get the row and column clicked
		int rowSelected = mouseY / CELL_SIZE;
		int colSelected = mouseX / CELL_SIZE;
		gg.updateGame(rowSelected, colSelected);
	}

	class DrawCanvas extends JPanel {
		@Override
		public void paintComponent(Graphics g) {  // invoke via repaint()
			super.paintComponent(g);    // fill background
			setBackground(Color.WHITE); // set its background color
			// Draw the grid-lines
			drawGridLines(g);		
			drawSeeds(g);
			drawStatusBar();

		}
		private void drawGridLines(Graphics g) {
			g.setColor(Color.LIGHT_GRAY);
			for (int row = 1; row < ROWS; ++row) {
				g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDHT_HALF,
						CANVAS_WIDTH-1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
			}
			for (int col = 1; col < COLS; ++col) {
				g.fillRoundRect(CELL_SIZE * col - GRID_WIDHT_HALF, 0,
						GRID_WIDTH, CANVAS_HEIGHT-1, GRID_WIDTH, GRID_WIDTH);
			}
		}

		private void drawSeeds(Graphics g) {
			// Draw the Seeds of all the cells if they are not empty
			// Use Graphics2D which allows us to set the pen's stroke
			Graphics2D g2d = (Graphics2D)g;
			g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND));  // Graphics2D only
			for (int row = 0; row < ROWS; ++row) {
				for (int col = 0; col < COLS; ++col) {
					int x1 = col * CELL_SIZE + CELL_PADDING;
					int y1 = row * CELL_SIZE + CELL_PADDING;
					if (gg.isCellCross(row, col)) {
						g2d.setColor(Color.RED);
						int x2 = (col + 1) * CELL_SIZE - CELL_PADDING;
						int y2 = (row + 1) * CELL_SIZE - CELL_PADDING;
						g2d.drawLine(x1, y1, x2, y2);
						g2d.drawLine(x2, y1, x1, y2);
					} else if (gg.isCellNought(row, col)) {
						g2d.setColor(Color.BLUE);
						g2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
					}
				}
			}
		}

		private void drawStatusBar() {
			// Print status-bar message
			if (gg.getCurrentState() == GameState.PLAYING) {
				statusBar.setForeground(Color.BLACK);
				if (gg.getCurrentPlayer() == Seed.CROSS) {
					statusBar.setText("X's Turn");
				} else {
					statusBar.setText("O's Turn");
				}
			} else if (gg.getCurrentState() == GameState.DRAW) {
				statusBar.setForeground(Color.RED);
				statusBar.setText("It's a Draw! Click to play again.");
			} else if (gg.getCurrentState() == GameState.CROSS_WON) {
				statusBar.setForeground(Color.RED);
				statusBar.setText("'X' Won! Click to play again.");
			} else if (gg.getCurrentState() == GameState.NOUGHT_WON) {
				statusBar.setForeground(Color.RED);
				statusBar.setText("'O' Won! Click to play again.");
			}
		}

	}

	public static void main(String[] args) {
		// Run GUI codes in the Event-Dispatching thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GameBoard(); // Let the constructor do the job
			}
		});
	}
}
