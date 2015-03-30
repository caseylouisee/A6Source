package Display;
/**
 * @file DisplaySnL.java
 * 
 * @brief Class to display the SnL game
 * 
 * @date 29/03/2015
 * 
 * @author Casey Denner
 *  
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.apache.commons.lang3.time.StopWatch;

import Game.GameSnL;
import Menu.GameSelector;
import Menu.MenuSnL;
import Player.PlayerSnL;
/**
 * @class DisplaySnL
 *
 */
public class DisplaySnL extends JPanel implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8392438217721100997L;
	/* ------ CONSTANTS ----- */
	/** Sets shape's width and height */
	public static final int SQUARE_WIDTH = 50;
	public static final int SQUARE_HEIGHT = 50;
	public static final int PIECE_DIAMETER = 20;


	/* ------ VARIABLES ------ */
	int m_dialogResponse;
	int roll = 1;

	/** Label to display who's turn it is */
	JLabel m_playerTurn;

	/** Snake head and tail buffered images */
	private BufferedImage m_snakeHead;
	private BufferedImage m_snakeTail;
	private BufferedImage ladder;

	/** Array lists to hold snakes and ladders positions */
	ArrayList<Integer> m_snakeLocations = new ArrayList<Integer>();
	ArrayList<Integer> m_ladderLocations = new ArrayList<Integer>();

	/**Array list to hold players */
	ArrayList<PlayerSnL> m_players = new ArrayList<PlayerSnL>();

	/** Stop watch variable to display timer */
	private StopWatch m_stopWatch;

	/** JPanel to display timer */
	private JPanel m_timerPanel = new JPanel();
	private JLabel m_timerLabel;

	/** Boolean to test if game is running */
	private boolean gameRunning = false;

	/** Game object set as null, when initiated set to GameSnL */
	GameSnL m_gameSnL = null;

	/** JButton to roll dice */
	final JButton m_rollDiceBtn = new JButton("Roll dice");

	/** Label for dice image */
	private JLabel m_dicePicLabel = new JLabel(new ImageIcon("null"));

	/** Button to save game */
	private JButton m_saveGameButton = new JButton();

	/** JPanel to contain player information */
	private JPanel m_playerInfoPanel = null;

	/** ArrayList to contain player information (names) */ 
	private ArrayList<JLabel> m_playerInfo = null;

	/** Arraylist to contain player positions */
	private ArrayList<JLabel> m_playerPos = null;

	/** JPanel used for winning visual feedback */
	JPanel boardOverlay = null;


	/** gets the co_ordinates on the board for a particular square
	 * @param squareNo
	 */
	public int[] getCoordinates(int squareNo) {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::getCoordinates");
		}
		int yTranslation = 9 - getYVal(squareNo);
		int xTranslation;
		if (squareNo % 20 > 10 || squareNo % 20 == 0) {
			xTranslation = 11 - getXVal(squareNo);
		} else {
			xTranslation = getXVal(squareNo);
		}
		int x = (xTranslation * 50);
		int y = (yTranslation * 50) - 0;
	
		// System.out.println("X coordinate: " + x);
		// System.out.println("Y coordinate: " + y);
	
		int[] coordinates = { x, y };
		return coordinates;
	}

	/** Gets the X Value co-ordinate for the squareNo
	 * @param squareNo
	 * @return squareNo
	 */
	public int getXVal(int squareNo) {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::getXVal");
		}
		if (squareNo % 10 == 0) {
			return 10;
		} else {
			return squareNo % 10;
		}
	}

	/** Gets the Y Value co-ordinate for the squareNo
	 * @param squareNo
	 * @return squareNo
	 */
	public int getYVal(int squareNo) {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::getYVal");
		}
		return ((squareNo - getXVal(squareNo)) / 10);
	}

	/* ------ METHODS ------ */
	/** Constructor for DisplaySnL
	 *
	 * @param gameSnL set to m_GameSnL
	 * @param snakesList set to m_snakeLocations
	 * @param laddersList set to m_laddersList
	 * @param players set to m_players
	 */
	public DisplaySnL(GameSnL gameSnL, ArrayList<Integer> snakesList,
			ArrayList<Integer> laddersList, ArrayList<PlayerSnL> players) {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::ConstructorCalled");
		}
		m_gameSnL = gameSnL;
		m_snakeLocations = snakesList;
		m_ladderLocations = laddersList;
		m_players = players;
	}

	/** Adds the player name, who's turn it is and the roll dice button to
	 *  the display 
	 *  
	 * @param frame displays the screen output
	 **/
	public void addPlayInterface(JFrame frame) {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::addPlayInterface");
		}
		m_playerTurn = new JLabel();
		m_playerTurn.setText("It is player "
				+ m_players.get(m_gameSnL.getIterator()).getPlayerName()
				+ "'s turn!");
		m_playerTurn.setBounds(Display.XPOS_COL200, 
				Display.YPOS_ROW500+Display.OFFSET2, 
				Display.COMPONENT_WIDTH200, Display.COMPONENT_HEIGHT20);

		add(m_playerTurn);
		addRollDice();
		addSaveButton();
		addPlayerInformation();

		createTimer();
		frame.add(m_timerPanel);

		gameRunning = true;	

		Thread thread = new Thread(){
			public void run(){
				while(gameRunning){
					displayTimer();
					printPosition();
				}
			}
		};

		thread.start();

	}

	/**
	 * Sets the bounds for the m_rollDiceBtn
	 */
	private void addRollDice() {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::addRollDice");
		}
		m_rollDiceBtn.setBounds(Display.XPOS_COL450-Display.OFFSET2, 
				Display.YPOS_ROW500+Display.OFFSET2, Display.COMPONENT_WIDTH100, 
				Display.COMPONENT_HEIGHT40);

		if(m_players.get(m_gameSnL.getIterator()).getPlayerName()
				.endsWith(".AI")){
			m_rollDiceBtn.setText("Start game");
			m_rollDiceBtn.doClick();	
		}

		addDiceActionListener();
	}

	/**
	 * Adds the actionListener for the m_rollDiceBtn
	 */
	private void addDiceActionListener() {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::addDiceActionListener");
		}
		m_rollDiceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(m_players.get(m_gameSnL.getIterator()).getPlayerName()
						.endsWith(".AI")){

					ActionListener listen = new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							m_rollDiceBtn.setText("Roll Dice");
							System.out.println("AI player found");
							roll = m_gameSnL.buttonPush();

							if(m_players.get(m_gameSnL.getIterator())
									.getPlayerName()
									.endsWith(".AI")){
								m_rollDiceBtn.doClick();	
							}
							m_playerTurn.setText("It is player "
									+ m_players.get(m_gameSnL.getIterator())
									.getPlayerName() + "'s turn!");
						}
					};	
					Timer timer = new Timer(1000, listen);
					timer.setRepeats(false);
					timer.restart();

				} else {
					m_rollDiceBtn.setText("Roll Dice");
					roll = m_gameSnL.buttonPush();

					m_playerTurn.setText("It is player "
							+ m_players.get(m_gameSnL.getIterator())
							.getPlayerName()
							+ "'s turn!");

					if(m_players.get(m_gameSnL.getIterator()).getPlayerName()
							.endsWith(".AI")){
						m_rollDiceBtn.doClick();	
					}
				}

				m_playerTurn.setText("It is player "
						+ m_players.get(m_gameSnL.getIterator()).getPlayerName()
						+ "'s turn!");

				m_dicePicLabel.setIcon(new ImageIcon("res/Dice"+roll+".png"));

			}
		});

		m_dicePicLabel.setIcon(new ImageIcon("res/Dice"+roll+".png"));

		m_dicePicLabel.setBounds(Display.XPOS_COL400+Display.OFFSET1,
				Display.YPOS_ROW550, Display.COMPONENT_WIDTH150,
				Display.COMPONENT_HEIGHT100);
		
		add(m_rollDiceBtn);
		add(m_dicePicLabel);

	}

	/** 
	 * Sets the playerInfoPanel's bounds and adds the player name's and 
	 * position's to it in the form of JLabels.
	 */
	private void addPlayerInformation() {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::addPlayerInformation");
		}
		m_playerInfoPanel = new JPanel();
		m_playerInfoPanel.setBounds(Display.XPOS_COL50, Display.YPOS_ROW550, 
				Display.COMPONENT_WIDTH300, Display.COMPONENT_HEIGHT80);
		m_playerInfoPanel.setLayout(null);
		m_playerInfoPanel.setBackground(Color.WHITE);

		m_playerInfo = new ArrayList<JLabel>();
		m_playerPos = new ArrayList<JLabel>();

		for(int i = 0; i<m_players.size(); i++){

			m_playerInfo.add(new JLabel(m_gameSnL.getPlayer(i)
					.getPlayerName()));
			m_playerInfo.get(i).setBounds(0, Display.OFFSET2*i, 
					Display.COMPONENT_WIDTH100, Display.COMPONENT_HEIGHT20);
			m_playerInfo.get(i).setVisible(true);

			Integer location = m_gameSnL.getPlayerLocation(i);
			m_playerPos.add(new JLabel(location.toString()));
			m_playerPos.get(i).setBounds(Display.XPOS_COL200, Display.OFFSET2*i, 
					Display.COMPONENT_WIDTH100, Display.COMPONENT_HEIGHT20);

			m_playerInfoPanel.add(m_playerInfo.get(i));
			m_playerInfoPanel.add(m_playerPos.get(i));

		}

		add(m_playerInfoPanel);

	}

	/**
	 * Prints the players position in a JLabel
	 */
	private void printPosition() {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::printPosition");
		}
		Integer location = m_gameSnL.getPlayerLocation(m_gameSnL.getIterator());
		m_playerPos.get(m_gameSnL.getIterator()).setText(location.toString());	
	}

	/**
	 * Sets the save button text and bounds and creates action listener
	 */
	private void addSaveButton() {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::addSaveButton");
		}
		m_saveGameButton.setText("Save Game");
		m_saveGameButton.setBounds(Display.XPOS_COL50, Display.YPOS_ROW650, 
				Display.COMPONENT_WIDTH100, Display.COMPONENT_HEIGHT20);
		m_saveGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Game saved");
				saveGame();
			}
		});	
		add(m_saveGameButton);


	}

	/** method to save game to the file res/saveSnL.csv */
	public void saveGame(){
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::saveGame");
		}
		try {
			FileWriter writer = new FileWriter("res/saveSnL.csv");

			/*
			 * Format: 
			 * Number of players\n
			 * playername,r,g,b,playerposition\n
			 * playername,playercolour,playerposition\n
			 * ....
			 * number of snakes,number of ladder\n
			 * position of snakes\n
			 * position of ladders
			 */
			writer.append(m_gameSnL.getNumberOfPlayers()+"\n");
			for(int i=0; i<m_gameSnL.getNumberOfPlayers();i++){
				writer.append(m_players.get(i).getPlayerName()+",");
				Integer red = m_players.get(i).getPlayerColor().getRed();
				Integer green = m_players.get(i).getPlayerColor().getGreen();
				Integer blue = m_players.get(i).getPlayerColor().getBlue();


				writer.append(red.toString()+","+ green.toString()+","
						+blue.toString()+",");

				Integer playerLocation = m_players.get(i).getPlayerLocation();
				writer.append(playerLocation.toString()+"\n");
			}


			Integer snakes = m_snakeLocations.size()/2;
			Integer ladders = m_ladderLocations.size()/2;
			writer.append(snakes.toString()+",");
			writer.append(ladders.toString()+"\n");

			Integer snakeLocation;

			snakeLocation = m_snakeLocations.get(0);
			writer.append(snakeLocation.toString());

			for(int i = 1; i< m_snakeLocations.size();i++){
				snakeLocation = m_snakeLocations.get(i);
				writer.append(","+snakeLocation.toString());
			}
			writer.append("\n");

			Integer ladderLocation;
			ladderLocation = m_ladderLocations.get(0);
			writer.append(ladderLocation.toString());


			for(int i = 1; i< m_ladderLocations.size();i++){
				ladderLocation = m_ladderLocations.get(i);
				writer.append(","+ladderLocation.toString());
			}


			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		} 

	}

	/** Calls the method to draw the board and snakes and ladders, display timer
	 * @param graphics
	 */
	public void paintComponent(Graphics graphics) {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::paintComponent");
		}
		super.paintComponent(graphics);

		this.setBackground(Color.WHITE);
		this.drawBoard(graphics);

		if (m_snakeLocations.size() > 0) {
			this.printSnakes(graphics, m_snakeLocations);
		}
		if (m_ladderLocations.size() > 0) {
			this.printLadders(graphics, m_ladderLocations);
		}

		paintPlayer(graphics);

		repaint();
	}

	/**
	 * Paints the player according to their colour and location
	 * @param graphics
	 */
	public void paintPlayer(Graphics graphics) {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::paintPlayer");
		}
		for (int i = 0; i < m_players.size(); i++) {
			this.printPlayer(graphics, m_players.get(i).getPlayerColor(),
					m_players.get(i).getPlayerLocation());
		}

		repaint();
	}

	/** Draws the board 
	 * @param graphics
	 */
	public void drawBoard(Graphics graphics) {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::drawBoard");
		}
		this.setBackground(Color.WHITE);

		graphics.setColor(Color.BLACK);
		int l = 100;
		for (int j = 0; j < 10; j++) {
			for (int i = 1; i <= 10; i++) {
				graphics.drawRect((i * SQUARE_WIDTH), (j * SQUARE_HEIGHT),
						SQUARE_WIDTH, SQUARE_HEIGHT);
				graphics.drawString(String.valueOf(l), i * SQUARE_WIDTH + 5, j
						* SQUARE_HEIGHT + 15);
				l--;
			}
			j++;
			l = l - 9;
			for (int i = 1; i <= 10; i++) {
				graphics.drawRect((i * SQUARE_WIDTH), (j * SQUARE_HEIGHT),
						SQUARE_WIDTH, SQUARE_HEIGHT);
				graphics.drawString(String.valueOf(l), i * SQUARE_WIDTH + 5, j
						* SQUARE_HEIGHT + 15);
				l++;
			}
			l = l - 11;
		}

	}


	/**
	 * Paints the player to the screen
	 */
	public void updateGraphics(){
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::updateGraphics");
		}
		paintPlayer(getGraphics());
	}

	/**
	 * Updates the graphics on the screen particularly the player location
	 * @param coordinates holds the coordinates of the location of the player
	 */
	public void updateGraphics(int[] coordinates){
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::updateGraphics");
		}
		for (int i = 0; i < m_players.size(); i++) {
			this.printPlayer(getGraphics(), m_players.get(i).getPlayerColor(),
					m_players.get(i).getPlayerLocation(),coordinates);
		}
		repaint();
	}
	
	/** Prints the ladders on the board 
	 * @param graphics
	 * @param squares
	 */
	public void printLadders(Graphics graphics, ArrayList<Integer> squares) {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::printLadders");
		}
		//Pulls image from file
		try {
			ladder = ImageIO.read(new File("res/ladder.png"));
		} catch (IOException e) {
			System.out.println("wrong path");
		}

		for (int i = 0; i < squares.size(); i++) {

			int startSquare = squares.get(i);
			i++;
			int endSquare = squares.get(i);

			// System.out.println("Start square is: " + startSquare);
			// System.out.println("End square is: " + endSquare);

			//gets coordinates for ladder square locations
			int[] startCoordinates = getCoordinates(startSquare);
			int[] endCoordinates = getCoordinates(endSquare);

			graphics.drawImage(ladder, startCoordinates[0],
					startCoordinates[1], Display.OFFSET3, Display.OFFSET3,null);
			graphics.drawImage(ladder, endCoordinates[0], endCoordinates[1],
					Display.OFFSET3, Display.OFFSET3, null);

			Graphics2D graphics2 = (Graphics2D) graphics;
			graphics2.setColor(new Color(139, 90, 43));
			graphics2.setStroke(new BasicStroke(5));
			graphics2.drawLine(startCoordinates[0] + 5,
					startCoordinates[1] + Display.OFFSET5, 
					endCoordinates[0] + 5, endCoordinates[1]);
			graphics2.drawLine(startCoordinates[0] + 35,
					startCoordinates[1] + Display.OFFSET5, 
					endCoordinates[0] + 35, endCoordinates[1]);

		}

	}

	/** Prints the snakes on the board
	 * @param graphics
	 * @param snakes
	 */
	public void printSnakes(Graphics graphics, ArrayList<Integer> snakes) {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::printSnakes");
		}
		try {
			m_snakeHead = ImageIO.read(new File("res/SnakeHead.png"));
			m_snakeTail = ImageIO.read(new File("res/SnakeTail.png"));
		} catch (IOException e) {
			System.out.println("wrong path");
		}

		for (int i = 0; i < snakes.size(); i++) {

			int startSquare = snakes.get(i);
			i++;
			int endSquare = snakes.get(i);

			int[] startCoordinates = getCoordinates(startSquare);
			int[] endCoordinates = getCoordinates(endSquare);

			graphics.drawImage(m_snakeHead, startCoordinates[0],
					startCoordinates[1], 40, 40, null);
			graphics.drawImage(m_snakeTail, endCoordinates[0],
					endCoordinates[1] + 10, 40, 40, null);

			Graphics2D graphics2 = (Graphics2D) graphics;
			graphics2.setColor(Color.GREEN);
			graphics2.setStroke(new BasicStroke(10));
			graphics2.drawLine(startCoordinates[0] + 20,
					startCoordinates[1] + 35, endCoordinates[0] + 20,
					endCoordinates[1] + 10);
		}
	}

	/** Prints the player to the board 
	 * @param graphics
	 * @param playerColor
	 * @param squareNo
	 */
	public void printPlayer(Graphics graphics, Color playerColor, int squareNo){
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::printPlayer");
		}
		graphics.setColor(playerColor);
		int[] coordinates = getCoordinates(squareNo);
		graphics.fillOval(coordinates[0] + 20, coordinates[1] + 25,
				PIECE_DIAMETER, PIECE_DIAMETER);
	}

	/** 
	 * Prints the player counter according to their colour 
	 * @param graphics
	 * @param playerColor
	 * @param squareNo
	 * @param coordinates
	 */
	public void printPlayer(Graphics graphics, Color playerColor, int squareNo,
			int[] coordinates) {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::printPlayer");
		}
		graphics.setColor(playerColor);
		graphics.fillOval(coordinates[0], coordinates[1],
				PIECE_DIAMETER, PIECE_DIAMETER);
	}

	/**
	 * Creates the stop watch timer
	 */
	public void createTimer() {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::createTimer");
		}
		if (m_stopWatch == null) {

			m_timerPanel.setBounds(Display.XPOS_COL50, 
					Display.YPOS_ROW500+Display.OFFSET2,
					Display.COMPONENT_WIDTH100, Display.COMPONENT_HEIGHT20);
			m_timerPanel.setLayout(null);
			m_timerPanel.setBackground(Color.WHITE);

			JLabel lblTimer = new JLabel("Timer");
			lblTimer.setBounds(0, 0, Display.COMPONENT_WIDTH100, 
					Display.COMPONENT_HEIGHT20);
			m_timerPanel.add(lblTimer);

			/* Creating timer */
			m_stopWatch = new StopWatch();
			m_stopWatch.start();
		} else {
			m_stopWatch.reset();
			m_stopWatch.start();
		}
		JPanel m_stopwatchPanel = (JPanel) m_timerPanel;
		m_timerLabel = (JLabel) m_stopwatchPanel.getComponent(0);
	}

	/** Displays the stopwatch timer **/
	public void displayTimer() {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::displayTimer");
		}
		m_timerLabel.setText(m_stopWatch.toString());
	}

	/** 
	 * When there is a winner a frame pops up to show winner 
	 * @Param frame the frame for the display
	 */
	public void winner(JFrame frame) {
		if(GameSelector.m_TRACE){
			System.out.println("DisplaySnL::winner");
		}
		boardOverlay = new JPanel();
		boardOverlay.setBounds(0, 0, Display.XPOS_COL500, 
				Display.XPOS_COL500);
		boardOverlay.setVisible(true);
		frame.add(boardOverlay);
		Thread flash = new Thread(){
			public void run(){
				while(true){
					boardOverlay.setOpaque(true);
					boardOverlay.setBackground(Color.WHITE);
					try {Thread.sleep(50);}
					catch (InterruptedException e) {}
					boardOverlay.setBackground(Color.RED);
					try {Thread.sleep(50);}
					catch (InterruptedException e) {}
					boardOverlay.setBackground(Color.BLUE);
					try {Thread.sleep(50);}
					catch (InterruptedException e) {}

				}
			}
		};
		flash.start();

		m_dialogResponse = JOptionPane.showConfirmDialog(frame,
				"Winner! Would you like to play again?", "Snakes and Ladders",
				JOptionPane.YES_NO_OPTION);
		if (m_dialogResponse == 0) {
			frame.dispose();
			new MenuSnL();
		}
		else if (m_dialogResponse == 1) {
			frame.dispose();
			System.exit(0);
		}
	}

	/** Overrides the run method in this runnable class to repeatedly 
	 * paintComponents as the timer needs to be continuously updated 
	 */
	@Override
	public void run() {
		while (gameRunning) {
			paintComponents(getGraphics());
		}
	}

}