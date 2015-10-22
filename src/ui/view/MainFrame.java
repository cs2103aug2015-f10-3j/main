package ui.view;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import common.util.LoggingHandler;

/**
 * Create the GUI and show it.  For thread safety,
 * this method should be invoked from the
 * event-dispatching thread.
 */

public class MainFrame {

	/*** Variables ***/
	private static final String TITLE = "PaddleTask";
	private static final String LOOK_AND_FEEL = "com.seaglasslookandfeel.SeaGlassLookAndFeel";
	private static JFrame frame;
	private static MainPanel panel;
	private static boolean isMinimized = false;

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
				LoggingHandler handler = new LoggingHandler();
				handler.setupLoggingHandler();
				try {
					implementNativeKeyHook();
				} catch (Throwable e) {
					System.exit(1);
				}
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
			UIManager.setLookAndFeel(LOOK_AND_FEEL);
		} catch (Exception e) { }
		prepareFrame();
		if (isSystemTrayReady()) {
			isMinimized = false;
			//minimizeToTray();
		}
	}

	/**
	 * This method prepare the Frame to be displayed.
	 * It sets the variable for the frame and uses CommandLinePanel.
	 * 
	 */

	public static void prepareFrame() {
		frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowIconified(WindowEvent e) {
				minimizeToTray();
			}

			public void windowClosing(WindowEvent e) {
				minimizeToTray();
			}
		});
		removeDefaultButtons(frame);
		panel = new MainPanel();
		panel.populateContentPane(frame.getContentPane());
		UIManager.put("InternalFrameTitlePane.closeButtonToolTip", "Close PaddleTask");
		UIManager.put("InternalFrameTitlePane.minimizeButtonText", "Minimize");
		UIManager.put("InternalFrameTitlePane.maximizeButtonText", "Maximize");
		//Display the window.
		Dimension size = frame.getToolkit().getScreenSize();
		size.setSize(size.width / 2, size.height / 2);
		frame.setSize(size);
		//frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	/**
	 * This method removes the default buttons of minimize, maximise and close
	 * on the frame. 
	 * 
	 *  @param  Component 
	 *  			JFrame frame
	 */
	
	public static void removeDefaultButtons(Component com){
		if(com instanceof JButton){
			String name = ((JButton) com).getAccessibleContext().getAccessibleName();
			if(name.equals("Maximize")|| name.equals("Iconify")||
					name.equals("Close")){
				com.getParent().remove(com);
			}
		}
		if (com instanceof Container){
			Component[] comps = ((Container)com).getComponents();
			for(int x = 0, y = comps.length; x < y; x++){
				removeDefaultButtons(comps[x]);
			}
		}
	}

	private static void minimizeToTray() {
		int state = frame.getExtendedState(); // get current state
		state = state | Frame.ICONIFIED; // add minimized to the state
		frame.setExtendedState(state);
		frame.setVisible(false);
		isMinimized = true;
	}

	private static void restoreToDesktop() {
		int state = frame.getExtendedState(); // get current state
		state = state & ~Frame.ICONIFIED; // remove minimized to the state
		frame.setExtendedState(state);
		frame.setVisible(true);
		frame.toFront();
		frame.repaint();
		panel.setInputFocus();
		isMinimized = false;
	}

	private static boolean isSystemTrayReady() {
		if (!SystemTray.isSupported()) {
			return false;
		}
		SystemTray tray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().getImage("src/images/bulb.gif");
		TrayIcon trayIcon = new TrayIcon(image, "PaddleTask");
		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restoreToDesktop();
			}
		});
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static void implementNativeKeyHook() throws Exception {
		GlobalScreen.registerNativeHook();

		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.WARNING);

		GlobalScreen.addNativeKeyListener(new NativeKeyListener() {

			@Override
			public void nativeKeyTyped(NativeKeyEvent e) { }

			@Override
			public void nativeKeyReleased(NativeKeyEvent e) { }

			@Override
			public void nativeKeyPressed(NativeKeyEvent e) {
				if (e.getModifiers() == (NativeKeyEvent.CTRL_L_MASK)) {
					if (e.getKeyCode() == NativeKeyEvent.VC_SPACE) {
						if (isMinimized) {
							restoreToDesktop();
						} else {
							minimizeToTray();
						}
					}
				}
			}
		});
	}
}
