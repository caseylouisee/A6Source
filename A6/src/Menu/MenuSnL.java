package Menu;
/**
 * @file MenuSnL.java 
 * @author Casey Denner - A5, Casey Denner - A6
 * @brief The menu class specific to the SnL game
 * This class is for the menu of the SnL game, covering the player
 * colour selection, the number of players and the numbers of snakes and 
 * ladders on the board. The menu also allows for the input of player names
 * @date 29/03/2015
 *  
 */


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Display.Display;
import Game.GameSnL;

/**
 * @class MenuSnL
 * @brief The menu class specific to the SnL game
 * This class is for the menu of the SnL game, covering the player
 * colour selection, the number of players and the numbers of snakes and 
 * ladders on the board. The menu also allows for the input of player names
 * 
 */
public class MenuSnL {

	/** Title of the window */
	private final String WINDOW_TITLE = "Snakes and Ladders";

	/** Array list of player number options */
	private final String[] SNL_NUMBER_PLAYERS = { " ", "2 players",
			"3 players", "4 players" };

	/** Array list of player colour options */
	private final String[] SNL_COLOR_PLAYERS = { " ", "BLUE", "RED",
			"YELLOW", "GREEN"};

	/** Width of the window */
	private final int WINDOW_WIDTH = 500;

	/** Height of the window */
	private final int WINDOW_HEIGHT = 550;

	/** Array to hold the type of players */
	private final String[] HUMAN_OR_AI = {"Human", "AI"};

	/** Maximum number of players */
	private final int MAX_NUM_PLAYERS = 4;

	/** Maximum number of snakes/ladders for comboBox */
	private final int MAX_NUM_SNL = 41;

	/** Frame for the Snakes and Ladders Menu */
	private JFrame m_frame;

	/** Array List of text fields for player's names */
	private ArrayList<JTextField> m_playersNameTextField;

	/** Array List of combo boxes for player's colours */
	private ArrayList<JComboBox<String>> m_playersColours;

	/** Array list of player name label */
	private ArrayList<JLabel> m_namePlayersLabel;

	/** Array list to hold player type */
	private ArrayList<JComboBox<String>> m_playerTypeComboBox;

	/** Button to start game */
	private JButton m_initGameButton;

	/** Combo box for players */
	private JComboBox<String> m_playersComboBox;

	/** Number of players, initialised to 0 */
	private int m_numberPlayers = 0;

	/** Combo box to select number of snakes */
	private JComboBox<Integer> m_playerSnakes;

	/** Combo box to select number of ladders */
	private JComboBox<Integer> m_playerLadders;

	/** Number of snakes initialised to 1 */
	private int m_snakesNo = 1;

	/** Number of ladders initialised to 1 */
	private int m_laddersNo = 1;

	/** Visualization check box */
	private JCheckBox m_visualizationBox;

	/** 
	 * This is the MenuSnL constructor. It sets up a new frame setting 
	 * its height and width. 
	 */
	public MenuSnL() {

		if(GameSelector.m_TRACE){
			System.out.println("MenuSnL::ConstructorCalled");
		}

		m_frame = new JFrame(WINDOW_TITLE);
		m_frame.setBounds(Display.XPOS_COL100, Display.YPOS_ROW100, 
				WINDOW_WIDTH, WINDOW_HEIGHT);
		m_frame.setLayout(null);
		m_frame.getContentPane().setBackground(Color.BLACK);
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.setResizable(false);

		addLogo();
		addLabels();
		addNumberOfPlayersComboBox();
		addColoursComboBox();
		addPlayersNamesTextField();
		addPlayerType();
		addVisualizationCheckBox();
		addGameButtons();
		addSnakesComboBox();
		addLaddersComboBox();
		m_frame.setVisible(true);
	}

	/* -------- METHODS --------- */
	/** 
	 * Adds the labels on the frame for the title, number of players, 
	 * number of snakes, number of ladders. 
	 */
	private void addLabels() {
		if(GameSelector.m_TRACE){
			System.out.println("MenuSnL:: addLabels");
		}
		JLabel title = new JLabel("SNAKES & LADDERS", SwingConstants.CENTER);
		m_frame.add(title);

		JLabel numberPlayersLabel = new JLabel("Number players:");
		numberPlayersLabel.setBounds(Display.XPOS_COL50, 
				Display.YPOS_ROW100+Display.OFFSET20, 
				Display.COMPONENT_WIDTH150, Display.COMPONENT_HEIGHT20);
		numberPlayersLabel.setForeground(Color.WHITE);
		m_frame.add(numberPlayersLabel);

		m_namePlayersLabel = new ArrayList<JLabel>();
		for (int i = 0; i < MAX_NUM_PLAYERS; i++) {
			m_namePlayersLabel.add(new JLabel("Name " + (i + 1) + ": "));
			m_namePlayersLabel.get(i).setBounds(Display.XPOS_COL100, 
					Display.YPOS_ROW150 + Display.OFFSET40* i, 
					Display.COMPONENT_WIDTH150, 
					Display.COMPONENT_HEIGHT20);
			m_namePlayersLabel.get(i).setVisible(false);
			m_frame.add(m_namePlayersLabel.get(i));

			JLabel playerLadderslbl = new JLabel("Number of ladders: ");
			playerLadderslbl.setForeground(Color.WHITE);
			playerLadderslbl.setBounds(Display.XPOS_COL100, Display.YPOS_ROW400, 
					Display.COMPONENT_WIDTH150, Display.COMPONENT_HEIGHT20);
			m_frame.add(playerLadderslbl);

			JLabel playerSnakeslbl = new JLabel("Number of snakes: ");
			playerSnakeslbl.setForeground(Color.WHITE);
			playerSnakeslbl.setBounds(Display.XPOS_COL100, Display.YPOS_ROW350,
					Display.COMPONENT_WIDTH150, Display.COMPONENT_HEIGHT20);
			m_frame.add(playerSnakeslbl);

		}
	}

	/** Creates a combo box to select the number of players. */
	private void addNumberOfPlayersComboBox() {
		if(GameSelector.m_TRACE){
			System.out.println("MenuSnL:: addNumberOfPLayersComboBox");
		}
		m_playersComboBox = new JComboBox<String>(SNL_NUMBER_PLAYERS);
		m_playersComboBox.setBounds(Display.XPOS_COL150, 
				Display.YPOS_ROW100 + Display.OFFSET20, Display.COMPONENT_WIDTH150, 
				Display.COMPONENT_HEIGHT20);
		m_frame.add(m_playersComboBox);
		m_playersComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					m_numberPlayers = 0;
					switch (event.getItem().toString()) {
					case "2 players":
						m_numberPlayers = 2;
						break;
					case "3 players":
						m_numberPlayers = 3;
						break;
					case "4 players":
						m_numberPlayers = 4;
						break;
					default:
					}
					for (int i = 0; i < MAX_NUM_PLAYERS; i++) {
						m_playersNameTextField.get(i).setVisible(false);
						m_namePlayersLabel.get(i).setVisible(false);
						m_playersColours.get(i).setVisible(false);
						m_playerTypeComboBox.get(i).setVisible(false);
						m_initGameButton.setVisible(false);
					}
					for (int i = 0; i < m_numberPlayers; i++) {
						m_namePlayersLabel.get(i)
						.setForeground(Color.WHITE);
						m_playersNameTextField.get(i).setVisible(true);
						m_playerTypeComboBox.get(i).setVisible(true);
						m_namePlayersLabel.get(i).setVisible(true);
						m_playersColours.get(i).setVisible(true);
						m_initGameButton.setVisible(true);
					}
				}
			}
		});
		m_frame.add(m_playersComboBox);
	}

	/** Adds the text fields to the frame to set the player's names. */
	private void addPlayersNamesTextField() {
		if(GameSelector.m_TRACE){
			System.out.println("MenuSnL:: addPlayerNamesTextField");
		}
		m_playersNameTextField = new ArrayList<JTextField>();
		for (int i = 0; i < MAX_NUM_PLAYERS; i++) {
			m_playersNameTextField.add(new JTextField("Player "+(i+1)));
			m_playersNameTextField.get(i).setVisible(false);
			m_playersNameTextField.get(i).setBounds(Display.XPOS_COL150, 
					Display.YPOS_ROW150 + Display.OFFSET40 * i,
					Display.COMPONENT_WIDTH150, Display.COMPONENT_HEIGHT20);
			m_frame.add(m_playersNameTextField.get(i));
		}
	}

	/** Adds the combo box to the screen to select the player's colours.*/
	private void addColoursComboBox() {
		if(GameSelector.m_TRACE){
			System.out.println("MenuSnL:: addColoursComboBox");
		}
		m_playersColours = new ArrayList<JComboBox<String>>();
		for (int i = 0; i < MAX_NUM_PLAYERS; i++) {
			m_playersColours.add(new JComboBox<String>(SNL_COLOR_PLAYERS));
			m_playersColours.get(i).setBounds(Display.XPOS_COL300, 
					Display.YPOS_ROW150 + Display.OFFSET40 * i, 
					Display.COMPONENT_WIDTH100, Display.COMPONENT_HEIGHT20);
			m_playersColours.get(i).setVisible(false);
			m_frame.add(m_playersColours.get(i));
			m_playersColours.get(i).addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					if (event.getStateChange() == ItemEvent.SELECTED) {
					}
				}
			});
		}
	}


	/** Adds the combo box to set the player type */
	private void addPlayerType(){
		if(GameSelector.m_TRACE){
			System.out.println("MenuSnL:: addPlayerType");
		}
		m_playerTypeComboBox = new ArrayList<JComboBox<String>>();
		for(int i = 0; i < MAX_NUM_PLAYERS; i++){
			m_playerTypeComboBox.add(new JComboBox<String>(HUMAN_OR_AI));
			m_playerTypeComboBox.get(i).setBounds(Display.XPOS_COL400,
					Display.YPOS_ROW150 + Display.OFFSET40 * i,
					Display.COMPONENT_WIDTH100, Display.COMPONENT_HEIGHT20);
			m_playerTypeComboBox.get(i).setVisible(false);
			m_frame.add(m_playerTypeComboBox.get(i));	
		}
	}

	/** Adds the combo box to the frame to select the number of ladders.*/
	private void addLaddersComboBox() {
		if(GameSelector.m_TRACE){
			System.out.println("MenuSnL:: addLaddersComboBox");
		}
		m_playerLadders = new JComboBox<Integer>();
		for(int i = 1; i<MAX_NUM_SNL; i++){
			m_playerLadders.addItem(i);
		}
		m_playerLadders.setBounds(Display.XPOS_COL250, Display.YPOS_ROW400,
				Display.COMPONENT_WIDTH75, Display.COMPONENT_HEIGHT20);
		m_playerLadders.setVisible(true);
		m_frame.getContentPane().add(m_playerLadders);
		m_playerLadders.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				String numberOfLadders = event.getItem().toString();
				m_laddersNo = Integer.parseInt(numberOfLadders);
				if (event.getStateChange() == ItemEvent.SELECTED) {
				}
			}
		});
	}

	/** Adds the combo box to the frame to select the number of ladders.*/
	private void addSnakesComboBox() {
		if(GameSelector.m_TRACE){
			System.out.println("MenuSnL:: addSnakesComboBox");
		}
		m_playerSnakes = new JComboBox<Integer>();
		for(int i = 1; i<MAX_NUM_SNL; i++){
			m_playerSnakes.addItem(i);
		}
		m_playerSnakes.setBounds(Display.XPOS_COL250, Display.YPOS_ROW350,
				Display.COMPONENT_WIDTH75, Display.COMPONENT_HEIGHT20);
		m_playerSnakes.setVisible(true);
		m_frame.getContentPane().add(m_playerSnakes);
		m_playerSnakes.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				String numberOfSnakes = event.getItem().toString();
				m_snakesNo = Integer.parseInt(numberOfSnakes);
				if (event.getStateChange() == ItemEvent.SELECTED) {
				}
			}
		});
	}

	/** Adds the visualization check box to the frame. */
	private void addVisualizationCheckBox(){
		if(GameSelector.m_TRACE){
			System.out.println("MenuSnL :: addVisualizationCheckBox");
		}
		m_visualizationBox = new JCheckBox("Visualization?");
		m_visualizationBox.setBounds(Display.XPOS_COL350, Display.YPOS_ROW350,
				Display.COMPONENT_WIDTH150, Display.COMPONENT_HEIGHT20);
		m_visualizationBox.setText("Visualization?");
		m_visualizationBox.setForeground(Color.WHITE);
		m_visualizationBox.setVisible(true);

		m_visualizationBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				Object source =  event.getItemSelectable();
				if (source == m_visualizationBox) {
					if(GameSelector.m_TRACE){
						System.out.println("MenuSnL :: addVisualizationCheckBox"
								+ " - Selected");
					}
				}
				if (event.getStateChange() == ItemEvent.DESELECTED){
					if(GameSelector.m_TRACE){
						System.out.println("MenuSnL :: addVisualizationCheckBox"
								+ " - Deselected");
					}
				}
			}
		});

		m_frame.add(m_visualizationBox);
	}

	/** Adds the snakes and ladders logo to the frame. */
	private void addLogo() {
		if(GameSelector.m_TRACE){
			System.out.println("MenuSnL:: addLogo");
		}
		JButton m_logoButton = new JButton();
		m_logoButton.setIcon(new ImageIcon("res/DCSNLLOGO.png"));
		m_logoButton.setBorderPainted(false);
		m_logoButton.setFocusPainted(false);
		m_logoButton.setContentAreaFilled(false);
		m_logoButton.setBounds(0, 0, WINDOW_WIDTH,
				Display.COMPONENT_HEIGHT100);
		m_logoButton.setVisible(true);
		m_frame.add(m_logoButton);
	}

	/** Adds the start, load and back button to the frame. */
	private void addGameButtons() {
		if(GameSelector.m_TRACE){
			System.out.println("MenuSnL:: addGameButtons");
		}
		m_initGameButton = new JButton();
		m_initGameButton.setIcon(new ImageIcon("res/STARTBTN.png"));
		m_initGameButton.setBorderPainted(false);
		m_initGameButton.setFocusPainted(false);
		m_initGameButton.setContentAreaFilled(false);
		m_initGameButton.setVisible(false);
		m_initGameButton.setBounds(Display.XPOS_COL200-Display.OFFSET20, 
				Display.YPOS_ROW400+Display.OFFSET30, Display.COMPONENT_WIDTH170, 
				Display.COMPONENT_HEIGHT85);
		m_initGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendForm();
			}
		});
		JButton m_goBackButton = new JButton();
		m_goBackButton.setIcon(new ImageIcon("res/BACKBTN.png"));
		m_goBackButton.setBorderPainted(false);
		m_goBackButton.setFocusPainted(false);
		m_goBackButton.setContentAreaFilled(false);
		m_goBackButton.setBounds(Display.OFFSET10, 
				Display.YPOS_ROW400+Display.OFFSET30, 
				Display.COMPONENT_WIDTH160, Display.COMPONENT_HEIGHT85);
		m_goBackButton.setVisible(true);
		m_goBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_frame.dispose();
				new GameSelector(GameSelector.m_TRACE);
			}
		});
		JButton m_loadGame = new JButton();
		m_loadGame.setText("Load Game");
		m_loadGame.setIcon(new ImageIcon(("res/LOADBTN.png")));
		m_loadGame.setBorderPainted(false);
		m_loadGame.setFocusPainted(false);
		m_loadGame.setContentAreaFilled(false);
		m_loadGame.setBounds(Display.XPOS_COL350+Display.OFFSET10, 
				Display.YPOS_ROW400+Display.OFFSET30, 
				Display.COMPONENT_WIDTH130, Display.COMPONENT_HEIGHT85);
		m_loadGame.setVisible(true);
		m_loadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File path = new File("savedGamesSnL/");
				File [] files = path.listFiles();
				for (int i = 0; i < files.length; i++){
					if (files[i].isFile()){ 
						System.out.println(files[i]);
					}
				}

				File fileName = (File) JOptionPane.showInputDialog(m_frame, 
						"What game do you want to load?",
						"Load Game",
						JOptionPane.QUESTION_MESSAGE, 
						null, 
						files, 
						files[0]);

				if(fileName==null){
					return;
				}else{
					loadGame(fileName.toString());
				}
			}

		});

		m_frame.add(m_loadGame);
		m_frame.add(m_initGameButton);
		m_frame.add(m_goBackButton);
	}

	/** loads the game from the file res/saveSnL */
	private void loadGame(String fileName){
		if(GameSelector.m_TRACE){
			System.out.println("MenuSnL:: loadGame");
		}

		String csvFile = fileName;
		BufferedReader br = null;
		String line="";

		ArrayList<String> m_playerNames = new ArrayList<String>();
		ArrayList<Color> m_playerColors = new ArrayList<Color>();
		ArrayList<Integer> m_playerPositions = new ArrayList<Integer>();
		ArrayList<Integer> m_snakes = new ArrayList<Integer>();
		ArrayList<Integer> m_ladders = new ArrayList<Integer>();

		try{
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine();

			if(line==null){
				JOptionPane.showMessageDialog(null, "No save game data found");
			}else{	
				String[] columns = new String[5];
				try{
					int numberOfPlayers = Integer.parseInt(line);
					if(GameSelector.m_TRACE){
						System.out.println("No. of players: "+numberOfPlayers);
					}
					line = br.readLine();
					columns = line.split(",");

					Color color;
					for(int i=0;i<numberOfPlayers;i++){
						m_playerNames.add(columns[0]);
						color = new Color(Integer.parseInt(columns[1]),
								Integer.parseInt(columns[2]),
								Integer.parseInt(columns[3]));
						m_playerColors.add(color);
						m_playerPositions.add(Integer.parseInt(columns[4]));
						for(int j=0;j<5;j++){
							if(GameSelector.m_TRACE){
								System.out.print(columns[j]+",");
							}
						}
						System.out.println();
						line = br.readLine();
						columns = line.split(",");
					}

					int numberOfSnakes = Integer.parseInt(columns[0]);
					int numberOfLadders = Integer.parseInt(columns[1]);
					if(GameSelector.m_TRACE){
						System.out.println(columns[0]+", "+columns[1]);
					}
					line = br.readLine();
					columns = line.split(",");

					for(int i=0;i<numberOfSnakes*2;i+=2){
						m_snakes.add(Integer.parseInt(columns[i]));
						m_snakes.add(Integer.parseInt(columns[i+1]));
					}

					line = br.readLine();
					columns = line.split(",");

					for(int i=0; i<numberOfLadders*2;i+=2){
						m_ladders.add(Integer.parseInt(columns[i]));
						m_ladders.add(Integer.parseInt(columns[i+1]));
					}

					br.close();
					m_frame.dispose();
					new GameSnL(m_playerNames,m_playerColors,m_playerPositions,
							m_snakes,m_ladders, false);

					if(GameSelector.m_TRACE){
						System.out.println("MenuSnL:: load() - new GameSnL");
					}

					System.out.println(m_snakes);
					System.out.println(m_ladders);
				}catch(NumberFormatException e){
					JOptionPane.showMessageDialog(null, 
							"Incorrect/corrupt save file.");
				}



			} 
		}catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "No save game data found");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.print("");
		}
	}

	/** 
	 * Sends the information of all the players and the 
	 * number of snakes and ladders to GameSnl. 
	 */
	private void sendForm() {
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> colours = new ArrayList<String>();
		ArrayList<Color> colors = new ArrayList<Color>();

		for (int i = 0; i < m_numberPlayers; i++) {

			String name;
			String pattern = "([ ]*+[0-9A-Za-z]++[ ]*+)+";
			name = m_playersNameTextField.get(i).getText().toString();
			if (name.replaceAll("\\s", "").equals("") | !name.matches(pattern)){
				if(GameSelector.m_TRACE){
					System.out.println("MenuSnL:: sendForm - name isn't valid");
				}
				JOptionPane.showMessageDialog(
						null,
						"Please enter a valid name for Player " + (i + 1) + "."
						+ " Valid names include letters or numbers",
						"Form Data", JOptionPane.ERROR_MESSAGE);
				return;
			} 

			if (m_playerTypeComboBox.get(i).getSelectedItem().equals("Human")){
				name = m_playersNameTextField.get(i).getText().toString();
				if(GameSelector.m_TRACE){
					System.out.println("MenuSnL:: sendForm -"
							+ " human player name set to " + name);
				}
				names.add(name);
			}

			else{
				name = m_playersNameTextField.get(i).getText()
						.toString()+".AI";
				if(GameSelector.m_TRACE){
					System.out.println("MenuSnL:: sendForm -"
							+ " ai player name set to " + name);
				}
				names.add(name);
			}

			String color = m_playersColours.get(i).getSelectedItem().toString();
			Color c;
			Boolean colorUsed = false;
			if (color == "GREEN") {
				c = Color.GREEN;
				if(colors.contains(c)){
					colorUsed = true;
				}else{
					colors.add(c);
				}
			} else if (color == "BLUE") {
				c = Color.BLUE;
				if(colors.contains(c)){
					colorUsed = true;
				}else{
					colors.add(c);
				}
			} else if (color == "RED") {
				c = Color.RED;
				if(colors.contains(c)){
					colorUsed = true;
				}else{
					colors.add(c);
				}
			} else if (color == "YELLOW") {
				c = Color.YELLOW;
				if(colors.contains(c)){
					colorUsed = true;
				}else{
					colors.add(c);
				}
			}

			if (color.replaceAll("\\s", "").equals("")) {
				if(GameSelector.m_TRACE){
					System.out.println("MenuSnL:: sendForm -"
							+ " colour not selected");
				}
				JOptionPane.showMessageDialog(null,
						"Please select a color for Player " + (i + 1) + ".",
						"Form Data", JOptionPane.ERROR_MESSAGE);
				return;
			} else if(colorUsed){
				if(GameSelector.m_TRACE){
					System.out.println("MenuSnL:: sendForm -"
							+ " colour already chosen");
				}
				JOptionPane.showMessageDialog(null,
						"Please select a different color for Player " + 
								(i + 1) + ".","Form Data", 
								JOptionPane.ERROR_MESSAGE);
				return;
			}else {
				if(GameSelector.m_TRACE){
					System.out.println("MenuSnL:: sendForm -"
							+ " player colours set");
				}
				colours.add(color);
			}

		}

		String message = "";
		message += "Number Players: " + m_numberPlayers + "\n";

		for (int i = 0; i < m_numberPlayers; i++)
			message += "Player " + (i + 1) + ": "
					+ m_playersNameTextField.get(i).getText() + "\n";

		for (int i = 0; i < m_numberPlayers; i++)
			message += "Color " + (i + 1) + ": "
					+ m_playersColours.get(i).getSelectedItem() + "\n";

		JOptionPane.showMessageDialog(null, message, "Form Data",
				JOptionPane.INFORMATION_MESSAGE);

		m_frame.dispose();

		if(m_visualizationBox.isSelected()){
			new GameSnL(names, colors, m_snakesNo, m_laddersNo, true);
			if(GameSelector.m_TRACE){
				System.out.println("MenuSnL::sendForm - new GameSnL "
						+ "with visualization");
			}
		}else{
			new GameSnL(names, colors, m_snakesNo, m_laddersNo,false);
			if(GameSelector.m_TRACE){
				System.out.println("MenuSnL::sendForm - new GameSnL "
						+ "no visualization");
			}
		}
	}

	/** This is the test method. 
	 * It calls the menuSnL constructor to add all the correct buttons, 
	 * all methods are voids so it's impossible to input invalid information. 
	 * All methods are called, if no load game file available it will try to 
	 * start the game with no player information throwing an error.
	 */
	public static void main(String[] args) { 
		MenuSnL test = new MenuSnL();	
		test.addLabels();
		test.addNumberOfPlayersComboBox();
		test.addPlayersNamesTextField();
		test.addColoursComboBox();
		test.addPlayerType();
		test.addLaddersComboBox();
		test.addSnakesComboBox();
		test.addLogo();
		test.addGameButtons();
		test.loadGame("savedGamesSnL/saveSnL.csv");
		test.sendForm();
	}
}