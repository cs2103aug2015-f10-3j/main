package ui.view;

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

import background.Reminder;
import command.api.ClearCommand;
import command.api.SearchTaskCommand;
import command.api.ViewTaskCommand;
import common.util.DateTimeHelper;
import common.util.LoggingHandler;
import task.entity.Task;
import ui.controller.UIController;

/**
 * Create the GUI and show it.  For thread safety,
 * this method should be invoked from the
 * event-dispatching thread.
 */

public class MainFrame implements Observer{

	/*** Variables ***/
	private static final String TITLE = "PaddleTask";
	private static final String SEA_GLASS_LOOK_AND_FEEL = "com.seaglasslookandfeel.SeaGlassLookAndFeel";
	private static final String WELCOME_MSG_1 = "Welcome to PaddleTask.";
	private static final String WELCOME_MSG_2 = "Today is %s.";
	private static final String WELCOME_MSG_3 = "Your upcoming tasks for today:";
	private static final String FIRST_COMMAND = "view all today";
	private static JFrame frame;
	private static CommandLinePanel panel;
	private static boolean isMinimized = false;
	private static Scanner sc = new Scanner(System.in);
	private static final String CLI_COMMAND = "cli";
	private static UIController uiController;
	private static final char PRIORITY_INDICATOR = '*';
	private static final char BOLD_INDICATOR = '@';
	private static boolean ui_Mode = false;
	private static MainFrame mainFrame = null;

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
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		
		initiate(args);
	}

	public static boolean initiate(String[] args) {
		mainFrame = new MainFrame();
		if(args.length > 0){
			String input = args[0];
			if(input.equals(CLI_COMMAND)){
				//mainFrame.cliMode();
				return true;
			}
		}
		initiateGUI();
		return true;
	}
	
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

	public void prepareWelcome(){
		String today = DateTimeHelper.getDate(LocalDateTime.now());
		String[] outputs = new String[3];
		int counter = 0;
		outputs[counter++] = WELCOME_MSG_1;
		outputs[counter++] = String.format(WELCOME_MSG_2, today);
		outputs[counter++] = WELCOME_MSG_3;
		String[] output = uiController.processUserInput(FIRST_COMMAND);
		outputToCmd(outputs);
		outputToCmd(output);
	}

	public void outputToCmd(String[] output){
		for(String s : output){
			if(s!=null){
				while(s.charAt(0) == BOLD_INDICATOR || s.charAt(0) == PRIORITY_INDICATOR ){
					s = s.substring(1);
				}
				System.out.println(s);
			}
		}
	}

	/**
	 * This method is to schedule a job for the event-dispatching thread, followed
	 * by creating and showing this application's GUI.
	 * 
	 */
	public static void initiateGUI() {
		ui_Mode = true;
		LoggingHandler handler = new LoggingHandler();
		handler.setupLoggingHandler();
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
		//Create and set up the window.
		//Use SeaGlass Look and Feel to enhance display
		try {
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			//UIManager.setLookAndFeel(SEA_GLASS_LOOK_AND_FEEL);
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
		panel = new CommandLinePanel(mainFrame);
		//nextPanel.populateContentPane(contentPane);
		//panel = new MainPanel();
		panel.populateContentPane(frame.getContentPane());

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

	private static void removeDefaultButtons(Component com){
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
		URL resource = Reminder.class.getResource("/images/bulb.gif");
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

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if (o instanceof ClearCommand || o instanceof ViewTaskCommand || o instanceof SearchTaskCommand) {
			if(ui_Mode){
				CommandLinePanel.setPaneToNull();
			} else{
				System.out.println("\033[H\033[2J");
			}
		} else if(o instanceof Reminder){
			if(arg instanceof ArrayList<?>){
				if(ui_Mode){
					CommandLinePanel.createReminder((ArrayList<Task>)arg);
				} else{
					String[] output = uiController.format((ArrayList<Task>)arg);
					outputToCmd(output);
				}
			}
		} else {
			String msg = (String)arg;
			if(ui_Mode){
				CommandLinePanel.updatePrint(msg);
			} else{
				System.out.println(msg);
			}
		}
	}
}
