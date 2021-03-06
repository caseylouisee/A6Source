/**
 * @file GameSelector.java
 * @author Casey Denner - A5, Casey Denner - A6
 * @date 29/03/2015
 * @brief Select between games
 * 
 * Creates the starting menu depending on game choice
 * Also creates game specific settings such as the design of the menu, and
 *  the ability to choose either SnL or TTT.
 */

package Menu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import Display.Display;

/**
 * @class GameSelector
 * @brief Creates the starting menu depending on game choice
 * Also creates game specific settings such as the design of the menu, and
 * the ability to choose either SnL or TTT.
 */
public class GameSelector {
	/** Title of the window */
	private final static String WINDOW_TITLE = "GAME SELECTOR";
	
	/** Width of the window */
	private final static int WINDOW_WIDTH = 730;
	
	/** Length of the window */
	private final static int WINDOW_HEIGHT = 450;
	
	/** Testing variable */
	public static Boolean m_TRACE;
	
	/** Frame being used */
	private static JFrame m_frame;

	/** 
	 * Constructor with testing boolean. If true, test statements print
	 * @param testing 
	 */
	public GameSelector(boolean testing){
		m_TRACE = testing;
		createWindow();
		createUI();
	}
	
	/** Creates and displays interface to select between games */
	private static void createWindow() {
		if(GameSelector.m_TRACE){
			System.out.println("GameSelector::createWindow called method, "
					+ "no parameters needed") ;
		}
		m_frame = new JFrame(WINDOW_TITLE);
		m_frame.setBounds(Display.XPOS_COL100, Display.YPOS_ROW100,
				WINDOW_WIDTH, WINDOW_HEIGHT);
		m_frame.getContentPane().setLayout(null);
		m_frame.setResizable(false);
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.getContentPane().setBackground(Color.BLACK);
		JLabel title = new JLabel("Choose a game to begin!",
				SwingConstants.CENTER);
		title.setForeground(Color.WHITE);
		title.setBounds(0, Display.OFFSET20, WINDOW_WIDTH, 
				Display.COMPONENT_WIDTH50);
		m_frame.getContentPane().add(title);
	}

	/** Handles what happens when a game is chosen */
	private static void createUI() {
		if(GameSelector.m_TRACE){
			System.out.println("GameSelector::createUI called method, "
					+ "no parameters needed") ;
		}
		JButton SnLButton = new JButton();
		SnLButton.setIcon(new ImageIcon("res/SNLIMG.jpg"));
		SnLButton.setBorderPainted(false);
		SnLButton.setFocusPainted(false);
		SnLButton.setContentAreaFilled(false);
		SnLButton.setBounds(Display.XPOS_COL100, Display.YPOS_ROW100, 
				Display.COMPONENT_WIDTH250, Display.COMPONENT_HEIGHT250);
		SnLButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_frame.dispose();
				new MenuSnL();
			}
		});

		JLabel snakesAndLadders = new JLabel("Snakes and Ladders");
		snakesAndLadders.setForeground(Color.WHITE);
		snakesAndLadders.setBounds(Display.XPOS_COL150, Display.YPOS_ROW350,
				WINDOW_WIDTH, Display.COMPONENT_HEIGHT50);
		snakesAndLadders.setForeground(Color.WHITE);

		JButton TTTButton = new JButton();
		TTTButton.setIcon(new ImageIcon("res/TTTIMG.jpg"));
		TTTButton.setBorderPainted(false);
		TTTButton.setFocusPainted(false);
		TTTButton.setContentAreaFilled(false);
		TTTButton.setBounds(Display.XPOS_COL400, Display.YPOS_ROW100, 
				Display.COMPONENT_WIDTH250, Display.COMPONENT_HEIGHT250);
		TTTButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_frame.dispose();
				new MenuTTT();
			}
		});

		JLabel ticTacToe = new JLabel("Dans Crazy Tic Tac Toe");
		ticTacToe.setForeground(Color.WHITE);
		ticTacToe.setBounds(Display.XPOS_COL450, Display.YPOS_ROW350, 
				WINDOW_WIDTH, Display.COMPONENT_HEIGHT50);
		ticTacToe.setForeground(Color.WHITE);
		m_frame.getContentPane().add(snakesAndLadders);
		m_frame.getContentPane().add(ticTacToe);
		m_frame.getContentPane().add(SnLButton);
		m_frame.getContentPane().add(TTTButton);
		m_frame.setVisible(true);
	}

	/**
	 * Calls methods to create and display the user interface, as well as
	 * handles what happens when a game is chosen
	 * Unit tests are done here too
	 */
	public static void main(String[] args) {
		Boolean bool;
		
		if(args[0].equalsIgnoreCase("1")){
			bool = true;
		}else{
			bool = false;
		}
		
		new GameSelector(bool);
	}

}
