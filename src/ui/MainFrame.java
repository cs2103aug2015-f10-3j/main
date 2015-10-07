package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Create the GUI and show it.  For thread safety,
 * this method should be invoked from the
 * event-dispatching thread.
 */

public class MainFrame {

	/*** Variables ***/
	private static final String TITLE = "PaddleTask";

	/*** Methods ***/
    /**
     * This method is the main method of the program.
     * 
     * @param  String[] argument on execution
     */
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

    /**
     * This method prepare the UI for display and decorations.
     * 
     * 
     */
	private static void createAndShowGUI() {
		//Create and set up the window.
		//Use SeaGlass Look and Feel to enhance display
		try {
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		} catch (Exception e) { }

		prepareFrame();
	}

    /**
     * This method prepare the Frame to be displayed.
     * It sets the variable for the frame and uses CommandLinePanel.
     * 
     */
	
	public static void prepareFrame() {
		JFrame frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		CommandLinePanel panel = new CommandLinePanel();
		panel.populateContentPane(frame.getContentPane());

		//Display the window.
		Dimension size = frame.getToolkit().getScreenSize();
		size.setSize(size.width / 2, size.height / 2);
		frame.setSize(size);
		//frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}


}
