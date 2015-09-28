package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
	private static final String TITLE = "PaddleTask";
	
    private static void createAndShowGUI() {
        //Create and set up the window.
        //Use the Java look and feel.
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { }

        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        
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

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
