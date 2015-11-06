//@@author A0125528E
package main.paddletask.ui.view;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import main.paddletask.background.Reminder;
import main.paddletask.command.api.ClearCommand;
import main.paddletask.command.api.SearchTaskCommand;
import main.paddletask.command.api.ViewTaskCommand;
import main.paddletask.common.util.DateTimeHelper;
import main.paddletask.common.util.LoggingHandler;
import main.paddletask.task.entity.Task;
import main.paddletask.ui.controller.UIController;

/**
 * This create the GUI and show it. This is the Mainframe class
 * to hold the panel and the GUI that will be displayed.
 * If CLI mode were to be enabled, this class will be 
 * responsible for getting user input and displaying onto
 * the command line.
 */

public class MainFrame implements Observer{

	/*** Variables ***/
	private static final String TITLE = "PaddleTask";
	private static final String WELCOME_MSG_1 = "Welcome to PaddleTask.";
	private static final String WELCOME_MSG_2 = "Today is %s.";
	private static final String WELCOME_MSG_3 = "Your upcoming tasks for today:";
	private static final String FIRST_COMMAND = "view all today";
	private static final String REMINDER_MSG = "Reminder Alert!";
	private static JFrame frame;
	private static MainPanel panel;
	private static boolean isMinimized = false;
	private static Scanner sc = new Scanner(System.in);
	private static final String CLI_COMMAND = "cli";
	private static UIController uiController;
	private static final char PRIORITY_INDICATOR = '*';
	private static final char BOLD_INDICATOR = '@';
	private static boolean ui_Mode = false;
	private static MainFrame mainFrame = null;
	private static final int WELCOME_MSG_SIZE = 3;
	private static final String OPTION_MAXIMIZE = "Maximize";
	private static final String OPTION_ICONIFY = "Iconify";
	private static final String OPTION_CLOSE = "Close";
	private static final int SIZE_PROPORTION = 2;
	private static final int REMOVE_ONE = 1;
	private static final int CHARACTER_LOCATION = 0;
	private static final int OFFSET_ZERO = 0;

	/*** Constructor ***/
	public MainFrame(){
		uiController = UIController.getInstance(this);
	}

	/*** Methods ***/
	/**
	 * This method is the main method of the program.
	 * 
	 * @param  String[] argument on execution
	 */
	public static void main(String[] args) {
		LoggingHandler handler = new LoggingHandler();
		handler.setupLoggingHandler();
		initiate(args);
	}

	/**
	 * This method is to initiate the mainFrame object and determine
	 * command line mode or GUI mode to be deployed.
	 * 
	 * @param  String[] argument on execution
	 * 
	 * @return true when the method completes
	 */
	public static boolean initiate(String[] args) {
		mainFrame = new MainFrame();
		if(args.length > OFFSET_ZERO){
			String input = args[OFFSET_ZERO];
			if(input.equals(CLI_COMMAND)){
				mainFrame.cliMode();
				return true;
			}
		}
		initiateGUI();
		return true;
	}
	
	/**
	 * This method is to initiate the command line mode
	 * of PaddleTask. This will take in input from the user
	 * to be processed.
	 * 
	 */
	public void cliMode(){
		prepareWelcome();
		while(sc.hasNext()){
			String command = sc.nextLine();
			String[] output = uiController.processUserInput(command);
			if(output!= null){
				outputToCmd(output);
			}

		}
	}

	/**
	 * This method is to prepare the welcome message of 
	 * PaddleTask. It will also send a "view all today"
	 * command that will be used to display user's task today
	 * on initialization of PaddleTask.
	 * 
	 */
	public void prepareWelcome(){
		String today = DateTimeHelper.getDate(LocalDateTime.now());
		String[] outputs = new String[WELCOME_MSG_SIZE];
		int counter = OFFSET_ZERO;
		outputs[counter++] = WELCOME_MSG_1;
		outputs[counter++] = String.format(WELCOME_MSG_2, today);
		outputs[counter++] = WELCOME_MSG_3;
		String[] output = uiController.processUserInput(FIRST_COMMAND);
		outputToCmd(outputs);
		outputToCmd(output);
	}

	/**
	 * This method is process the output and
	 * display it onto the command line.
	 * 
	 * @param output 
	 * 					String array to be displayed
	 * 
	 */
	public void outputToCmd(String[] output){
		System.out.println();
		for(String s : output){
			if(s!=null){
				while(s.charAt(CHARACTER_LOCATION) == BOLD_INDICATOR || 
						s.charAt(CHARACTER_LOCATION) == PRIORITY_INDICATOR ){
					s = s.substring(REMOVE_ONE);
				}
				System.out.println(s);
			}
		}
		System.out.println();
	}

	/**
	 * This method is to schedule a job for the event-dispatching thread, followed
	 * by creating and showing this application's GUI.
	 * 
	 */
	public static void initiateGUI() {
		ui_Mode = true;
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
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
	 */
	private static void createAndShowGUI() {
		prepareFrame();
		if (isSystemTrayReady()) {
			isMinimized = false;
			//minimizeToTray();
		}
	}

	/**
	 * This method prepare the Frame to be displayed.
	 * It sets the variable for the frame and uses CommandLinePanel.
	 * The default size of PaddleTask is set
	 * to the halved of the resolution of the user's screen dynamically
	 * to suit all users' screen.
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
		panel = new MainPanel(mainFrame);
		panel.populateContentPane(frame.getContentPane());
		Dimension size = frame.getToolkit().getScreenSize();
		size.setSize(size.width / SIZE_PROPORTION, size.height / SIZE_PROPORTION);
		frame.setSize(size);
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
	private static void removeDefaultButtons(Component com){
		if(com instanceof JButton){
			String name = ((JButton) com).getAccessibleContext().getAccessibleName();
			if(name.equals(OPTION_MAXIMIZE)|| name.equals(OPTION_ICONIFY)||
					name.equals(OPTION_CLOSE)){
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

	/**
	 * This method is the update method for Observer class
	 * of MainFrame. 
	 * 
	 *  @param 	o
	 *  			Observable that called update
	 *			arg  
	 *  			argument passed with update
	 *  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if (o instanceof ClearCommand || o instanceof ViewTaskCommand || o instanceof SearchTaskCommand) {
			if(ui_Mode){
				MainPanel.setPaneToNull();
			} else{
				System.out.println();
				//System.out.println(CLEAR_SCREEN);
			}
		} else if(o instanceof Reminder){
			if(arg instanceof ArrayList<?>){
				if(ui_Mode){
					MainPanel.createReminder((ArrayList<Task>)arg);
				} else{
					String[] output = uiController.format((ArrayList<Task>)arg);
					System.out.println(REMINDER_MSG);
					outputToCmd(output);
				}
			}
		} else {
			String msg = (String)arg;
			if(ui_Mode){
				MainPanel.updatePrint(msg);
			} else{
				System.out.println(msg);
				System.out.println();
			}
		}
	}
	
	//@@author A0125473H
	/**
	 * This method is invoked to hide and minimize the
	 * application to window's system tray
	 */
	private static void minimizeToTray() {
		int state = frame.getExtendedState(); // get current state
		state = state | Frame.ICONIFIED; // add minimized to the state
		frame.setExtendedState(state);
		frame.setVisible(false); // hide the window
		isMinimized = true;
	}


	/**
	 * This method is invoked to restore and unhide the
	 * application from window's system tray
	 */
	private static void restoreToDesktop() {
		int state = frame.getExtendedState(); // get current state
		state = state & ~Frame.ICONIFIED; // remove minimized to the state
		frame.setExtendedState(state);
		frame.setVisible(true); // unhide the window
		frame.toFront();
		frame.repaint();
		panel.setInputFocus();
		isMinimized = false;
	}

	/**
	 * This method is invoked to check if the system tray
	 * is accessible by the application
	 * 
	 * @return <code>true</code> if system tray is accessible
	 *         <code>false</code> otherwise
	 */
	private static boolean isSystemTrayReady() {
		if (!SystemTray.isSupported()) {
			return false;
		}
		SystemTray tray = SystemTray.getSystemTray();
		URL resource = Reminder.class.getResource("../../resources/images/bulb.gif");
		Image image = Toolkit.getDefaultToolkit().getImage(resource);
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

	/**
	 * This method is invoked to implement a global keystroke hook
	 * onto the user's system
	 */
	private static void implementNativeKeyHook() throws Exception {
		GlobalScreen.registerNativeHook();

		//supress all warnings except level warning
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.WARNING);

		GlobalScreen.addNativeKeyListener(new NativeKeyListener() {

			@Override
			public void nativeKeyTyped(NativeKeyEvent e) { }

			@Override
			public void nativeKeyReleased(NativeKeyEvent e) { }

			// only required to hook the key press event
			// and check for a combination of LCTRL + SPACE
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
