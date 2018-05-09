
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * last 4 UIN: 1062
 * last date modified: 10/12/2016
 * JEightPuzzleFrame
 */
public class JEightPuzzleFrame extends JFrame implements ActionListener {
	private BufferedImage image;
	public static int height;
	public static int width;
	private JPanel centerPanel;
	private String path;
	private JComponent emptyButton = new JPanel();
	private int[][] board = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 } };
	private JButton buttons[] = new JButton[8];
	private int position[] = new int [9];

	/*
	 * Constructor with two parameters
	 */
	public JEightPuzzleFrame(String title, String path) {
		super(title);
		this.path = path;
		setGame(); //start puzzle
	}
	
	/*
	 * reads the image into a BufferedImage object (image)
	 */
	private void getImage() {
		image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.err.println("Image not found");
			System.exit(1);
		}
		width = image.getWidth() / 3;
		height = image.getHeight() / 3;
	}

	/*
	 * allocates another BufferedImage object whose size is
	 * the same as the one of the wanted icon
	 */
	private BufferedImage getIcon(int leftTopX, int leftTopY) {
		getImage(); //reads the image into a BufferedImage object (image)
		int iconWidth = image.getWidth() / 3;
		int iconLength = image.getHeight() / 3;
		BufferedImage part = new BufferedImage(iconWidth, iconLength, 
												BufferedImage.TYPE_4BYTE_ABGR);
		for (int x = 0; x < iconLength; x++) {
			for (int y = 0; y < iconWidth; y++) {
				part.setRGB(x, y, image.getRGB(x + leftTopX, y + leftTopY));
			}
		}
		return part;
	}
	
	/*
	 * setting up the initial game
	 */
	private void setGame() {
		setUp();
		int[] initialArray = { 0, 1, 4, 5, 2, 3, 6, 7 }; //1 less
		boolean [] used = new boolean[9];
		centerPanel.add(emptyButton);
		for (int i = 0; i < 8; i++) {
			position[0] = 0;
			buttons[i].addActionListener(this);
			centerPanel.add(buttons[initialArray[i]]);
			position[i + 1] = initialArray[i] + 1;
			used[initialArray[i]] = true;
			buttons[i].setVisible(true);
			//System.out.println(position[i]); //testing
			
		}
		validate();
		setSize(image.getWidth(), image.getHeight()); 
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/*
	 * Set up the JPanel, Grid, and JButton
	 */
	public void setUp(){
		centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(3, 3, 0, 0));
		add(centerPanel);
		getImage();
		int[][] coordinatePairs = { { 0, 0 }, 				// [0,0] , [0,1]
									{ width, 0 }, 			// [1,0] , [1,1]
									{ width * 2, 0 }, 		// [2,0] , [2,1]
									{ 0, width }, 			// [3,0] , [3,1]
									{ width, width }, 		// [4,0] , [4,1]
									{ width * 2, width }, 	// [5,0] , [5,1]
									{ 0, width * 2 }, 		// [6,0] , [6,1]
									{ width, width * 2 } };	// [7,0] , [7,1]
		
		for (int i = 0; i < 8; i++) {
			buttons[i] = new JButton(); //create buttons
			//setting icons of buttons based on the coordinatePairs
			buttons[i].setIcon(new ImageIcon(getIcon(coordinatePairs[i][0], 
													  coordinatePairs[i][1])));
		}
	}
	
	/*
	 * method to shuffle array
	 */
	public static void shuffle(int[] initialArray) {
		for (int i = 0; i < initialArray.length; i++) {
			int rand = i + (int) (Math.random() * (initialArray.length - i));
			int temp = initialArray[rand];
			initialArray[rand] = initialArray[i];
			initialArray[i] = temp;
		}
	}

	/*
	 * setting up game after completion
	 */
	private void newGame(){
		setUp(); 
		Random rand = new Random();
		int randomNum = rand.nextInt(8); //random number from 0-7
		int[] initialArray = { 0, 1, 4, 5, 2, 3, 6, 7 }; //1 less
		shuffle(initialArray); //shuffle initialArray  
		for (int i = 0; i < 8; i++) {
			if (i == randomNum){ //emptyButton at random location
				//System.out.println("random "+randomNum); //testing
				centerPanel.add(emptyButton);
				position[i] = 0;
				position[i+1] = initialArray[i] + 1; 
				//System.out.println(position[i]); //testing
				buttons[i].addActionListener(this);
				centerPanel.add(buttons[initialArray[i]]);
				buttons[i].setVisible(true);
			} else if ((i <= randomNum) && position[i] != 0){ //set up position 
														   	//before emptyButton
				position[i] = initialArray[i] + 1;
				//System.out.println(position[i]); //testing
				buttons[i].addActionListener(this);
				centerPanel.add(buttons[initialArray[i]]);
				buttons[i].setVisible(true);
			} else { //set up position after emptyButton
				position[i+1] = initialArray[i] + 1; 
				//System.out.println(position[i]); //testing
				buttons[i].addActionListener(this);
				centerPanel.add(buttons[initialArray[i]]);
				buttons[i].setVisible(true);
			} 
		}
		validate();
		setSize(image.getWidth(), image.getHeight()); 
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/*
	 * List of actions performed
	 */
	public void actionPerformed(ActionEvent event) {

		JButton button = (JButton) event.getSource();
		Dimension size = button.getSize();
		int emptyX = emptyButton.getX(); // x coordinate of emptybutton
		int emptyY = emptyButton.getY(); // y coordinate of emptybutton
		int buttonX = button.getX(); // x coordinate of button
		int buttonY = button.getY(); // y coordinate of button
		int positionX = buttonX / size.width; //x position of button
		int positionY = buttonY / size.height; //y position of button
		int buttonIndex = board[positionY][positionX];

		//same X coordinate and positive Y
		if (emptyX == buttonX && (emptyY - buttonY) == size.height) {
																   //slide down
			int newIndex = buttonIndex + 3;
			centerPanel.remove(buttonIndex);
			centerPanel.add(emptyButton, buttonIndex);
			centerPanel.add(button, newIndex);
			centerPanel.validate();
			int temp = position[buttonIndex];
			position[buttonIndex] = position[newIndex];
			position[newIndex] = temp;
		}

		//same X coordinate and negative Y
		if (emptyX == buttonX && (emptyY - buttonY) == -size.height) {
																	//slide up
			int newIndex = buttonIndex - 3;
			centerPanel.remove(newIndex);
			centerPanel.add(button, newIndex);
			centerPanel.add((emptyButton), buttonIndex);
			centerPanel.validate();
			int temp = position[buttonIndex];
			position[buttonIndex] = position[newIndex];
			position[newIndex] = temp;
		}
		
		//same Y coordinate and positive X
		if (emptyY == buttonY && (emptyX - buttonX) == size.width) {
																  // slide right
			int newIndex = buttonIndex + 1;
			centerPanel.remove(buttonIndex);
			centerPanel.add((emptyButton), buttonIndex);
			centerPanel.add(button, newIndex);
			centerPanel.validate();
			int temp = position[buttonIndex];
			position[buttonIndex] = position[newIndex];
			position[newIndex] = temp;
		}

		//same y coordinate and negative X
		if (emptyY == buttonY && (emptyX - buttonX) == -size.width) {
																  // slide left
			int newIndex = buttonIndex - 1;
			centerPanel.remove(buttonIndex);
			centerPanel.add((emptyButton), newIndex);
			centerPanel.add(button, newIndex);
			centerPanel.validate();
			int temp = position[buttonIndex];
			position[buttonIndex] = position[newIndex];
			position[newIndex] = temp;
		}
		
		/*
		 * testing 
		 * if (position[0] == 1 && position[1] == 2 && position[2] == 3 ){
		 *	System.out.println("first three works");
		 *	System.out.println();
		 * }
		 */
		
		if (position[0] == 1 && position[1] == 2 && position[2] == 3 && 
			position[3] == 4 && position[4] == 5 && position[5] == 6 && 
			position[6] == 7 && position[7] == 8 ) {
			JOptionPane.showMessageDialog(centerPanel, "Good Job! " 
				+ "Click ok to scramble and play again");
			centerPanel.removeAll();
			newGame(); 
		}
	}

	public static void main(String[] args) {

		new JEightPuzzleFrame("Eight Puzzle Game", "FGCU_logo.png");

	}

}
