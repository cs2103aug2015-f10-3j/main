//@@author A0125528E
package main.paddletask.ui.view;
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;

import main.paddletask.common.data.DoublyLinkedList;
import main.paddletask.common.data.Node;
import main.paddletask.common.util.DateTimeHelper;
import main.paddletask.task.entity.Task;
import main.paddletask.ui.controller.UIController;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements KeyListener {

	/*** Variables ***/
	private static final String STRING_EMPTY = "";
	private static final String NEXT_LINE = "\n";
	private static final String WELCOME_MSG_1 = "Welcome to PaddleTask.";
	private static final String WELCOME_MSG_2 = "Today is %s.";
	private static final String WELCOME_MSG_3 = "Your upcoming tasks for today:";
	private static final String MODAL_OPTION = "Modal Dialog";
	private static final String FIRST_COMMAND = "view all today";
	private static final String KEYNAME_FONT_UP = "Font up";
	private static final String KEYNAME_FONT_DOWN = "Font down";
	private static final int OFFSET_ZERO = 0;
	private static final int SUBSTRING_BEGIN = 1;
	private static final char PRIORITY_INDICATOR = '*';
	private static final char BOLD_INDICATOR = '@';
	private static final int INCREASE_FONT_SIZE = 1;
	private static final int DECREASE_FONT_SIZE = -1;
	protected static int NUM_COMPONENTS = 3;
	protected UIController uiController = null;
	private static Font font = new Font("Consolas",Font.PLAIN, 15);
	private JTextField inputField = null;
	//private JTextArea textArea = null;
	private static JTextPane textPane = null;
	public static JDialog reminderDialog = null;
	private static JPanel panel = null;
	private static JPanel errorPanel = null;
	private static JLabel errorLabel = null;
	private static DoublyLinkedList commandList = null;
	private Box box = null;
	private Node node = null;
	private String currentCommand = null;
	private static JScrollPane scrollPane = null;
	private MainFrame mainFrame = null;
	private static final float OPACITY_OF_SUGGESTIONS = 0.9f;
	private CommandSuggestor commandSuggestor = null;
	private static final Color backgroundColor = new Color(200, 200, 200); //Gray color
	private static final int timeDelay = 2000;
	private static final ActionListener time = new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent evt) {
	    	errorPanel.setVisible(false);
	    	errorTimer.stop();
	    }
	};
	private static final Timer errorTimer = new Timer(timeDelay, time);

	/*** Constructors ***/
	public MainPanel(MainFrame mainFrame){
		this.mainFrame = mainFrame;
		uiController = UIController.getInstance(mainFrame);
		commandList = new DoublyLinkedList();
	}

	/*** Methods ***/
	/**
	 * This method populate a panel for the frame. 
	 * It prepares various components needed for the panel.
	 * 
	 * @param Container
	 *            Container for the pane to be created on.
	 * 
	 */
	public void populateContentPane(Container contentPane) {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createLineBorder(Color.black));

		textPane = prepareJTextPane();

		JTextField inputField = prepareTextField();
		scrollPane = prepareScrollPane(textPane);
		
		errorPanel = prepareErrorPanel();
		
		box = prepareBoxComponent(inputField, scrollPane, errorPanel);

		panel.add(box, BorderLayout.PAGE_END);
		contentPane.add(panel, BorderLayout.CENTER);
		prepareWelcome();
		this.setInputFocus();
	}

	/**
	 * This method prepares the program with a welcome message.
	 * It will also perform a command "view all today" to show today's task.
	 * 
	 */
	public void prepareWelcome(){
		String today = DateTimeHelper.getDate(DateTimeHelper.now());
		String[] outputs = new String[3];
		int counter = 0;
		outputs[counter++] = WELCOME_MSG_1;
		outputs[counter++] = String.format(WELCOME_MSG_2, today);
		outputs[counter++] = WELCOME_MSG_3;
		String[] output = uiController.processUserInput(FIRST_COMMAND);
		append(outputs);
		append(output);
	}

	/**
	 * This method prepares a box component to be created on the panel.
	 * Aligns the component together in a box layout vertically.
	 * 
	 * @param JTextField
	 *            the input field on the panel
	 *        JScrollPane
	 * 			  the scroll pane for the text area.		
	 * @return Box format with the components on the panel
	 */
	public Box prepareBoxComponent(JTextField inputField, JScrollPane areaScrollPane, JPanel errorPanel) {
		Box box = Box.createVerticalBox();
		box.add(areaScrollPane);
		box.add(errorPanel);
		box.add(inputField);
		return box;
	}

	/**
	 * This method prepares a scroll pane for the textPane to enable scrolling
	 * for display.
	 * 
	 * @param textPane
	 * 				JTextPane of the panel

	 * @return areaScrollPane 
	 * 				JScrollPane with textPane
	 */
	private JScrollPane prepareScrollPane(JTextPane textPane) {
		final JScrollPane areaScrollPane = new JScrollPane(textPane);
		areaScrollPane.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				inputField.requestFocus();
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		textPane.addMouseListener(areaScrollPane.getMouseListeners()[0]);
		areaScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setAutoscrolls(true);
		return areaScrollPane;
	}

	/**
	 * This method prepares a textfield for the user to input data.
	 * This will initiate UIController.java upon execution of a input by the user.
	 * 
	 * @return inputField - the JTextField created
	 */
	private JTextField prepareTextField() {
		inputField = new JTextField();
		inputField.setMaximumSize(new Dimension(inputField.getMaximumSize().width ,inputField.getPreferredSize().height));
		inputField.requestFocus();
		inputField.addKeyListener(this);
		inputField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = inputField.getText();
				addCommandToList(input);
				performCommand(input);
				inputField.setText(STRING_EMPTY);
				currentCommand = null;
			}
		});
		inputField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0, true), KEYNAME_FONT_UP);
		inputField.getActionMap().put(KEYNAME_FONT_UP, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {//focuses the first label on popwindow
				CustomizedDocumentFilter.changeFontSize(INCREASE_FONT_SIZE, textPane);
			}
		});
		inputField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0, true), KEYNAME_FONT_DOWN);
		inputField.getActionMap().put(KEYNAME_FONT_DOWN, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {//focuses the first label on popwindow
				CustomizedDocumentFilter.changeFontSize(DECREASE_FONT_SIZE, textPane);
			}
		});
		commandSuggestor = new CommandSuggestor(inputField, mainFrame.getFrame(),
				Color.WHITE.brighter(), Color.BLUE, Color.RED, OPACITY_OF_SUGGESTIONS);
		
		return inputField;
	}
	
	/**
	 * This method prepares a Panel for displaying of error messages to the user.
	 * 
	 * @return errorPanel - the JPanel created
	 */
	private JPanel prepareErrorPanel() {
		errorPanel = new JPanel();
		errorPanel.setMaximumSize(inputField.getMaximumSize());
		errorPanel.setVisible(false);
		errorPanel.setForeground(Color.RED);
		errorPanel.setBackground(Color.WHITE);
		errorPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
		errorPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		errorPanel.add(prepareErrorLabel());
		return errorPanel;
	}
	

	/**
	 * This method prepares a label for displaying of error messages to the user.
	 * 
	 * @return errorLabel - the JLabel created
	 */
	private JLabel prepareErrorLabel() {
		errorLabel = new JLabel();
		errorLabel.setMaximumSize(inputField.getMaximumSize());
		errorLabel.setHorizontalAlignment(SwingConstants.LEFT);
		errorLabel.setForeground(Color.RED);
		return errorLabel;
	}

	private void addCommandToList(String input){
		node = null;
		commandList.insertLast(input);
	}

	/**
	 * This method appends a string of text into the textPane to display to the user
	 * 
	 * @param s
	 *        	String s to be appended to textPane
	 *        isCommand
	 *        	boolean to check if it is command
	 */
	public static void append(String s, boolean isCommand) {
		try {
			Document doc = textPane.getDocument();
			textPane.setCaretPosition(doc.getLength());
			SimpleAttributeSet color = null;
			if(isCommand){
				color = new SimpleAttributeSet();
				color.addAttributes(CustomizedDocumentFilter.setBold());
			}
			doc.insertString(doc.getLength(), s + NEXT_LINE + NEXT_LINE, color);
		} catch(BadLocationException exc) {
			assert false;
			exc.printStackTrace();
		}

	}

	/**
	 * This method appends a string array of text into the textPane to display to the user
	 * 
	 * @param output
	 * 			String[] to be appended to textPane
	 *        	   
	 */
	public void append(String[] output){
		try {
			Document doc = textPane.getDocument();
			textPane.setCaretPosition(doc.getLength());
			String outputString = STRING_EMPTY;
			for(String s : output ){
				if(s != null){
					outputString = s + NEXT_LINE;
					SimpleAttributeSet color = new SimpleAttributeSet();
					if(outputString.charAt(OFFSET_ZERO)==BOLD_INDICATOR){
						color.addAttributes(CustomizedDocumentFilter.setBold());
						outputString = outputString.substring(SUBSTRING_BEGIN);
						color.addAttributes(CustomizedDocumentFilter.setBackgroundColorForHeader(backgroundColor));
					}
					if(outputString.charAt(OFFSET_ZERO)==PRIORITY_INDICATOR){
						color.addAttributes(CustomizedDocumentFilter.changeToOrange());
						outputString = outputString.substring(SUBSTRING_BEGIN);
					}
					if(outputString.charAt(OFFSET_ZERO)==PRIORITY_INDICATOR){
						color.addAttributes(CustomizedDocumentFilter.changeToRed());
						outputString = outputString.substring(SUBSTRING_BEGIN);
					}
					doc.insertString(doc.getLength(), outputString, color);
				}
			}
			doc.insertString(doc.getLength(), NEXT_LINE, null);
		} catch(BadLocationException exc) {
			assert false;
			exc.printStackTrace();
		}
	}

	/**
	 * This method performs the command from the
	 * given string and uses uiController to 
	 * continue the process.
	 * It will prepare to append 
	 * the outputs to be displayed.
	 * 
	 * @return input
	 * 				String input from user
	 */
	public void performCommand(String input){
		boolean isCommand = true;
		String[] output = uiController.processUserInput(input);
		if(output!= null){
			append(input, isCommand);
			append(output);
		}
	}

	/**
	 * This method prepares a text pane for the 
	 * output of the program to be displayed on.
	 * 
	 * @return inputTextPane
	 * 				JTextPane for the panel
	 */
	private JTextPane prepareJTextPane() {
		JTextPane inputTextPane = new JTextPane();
		inputTextPane.setFont(font);
		inputTextPane.setEditable(false);
		((AbstractDocument) inputTextPane.getDocument()).setDocumentFilter(new CustomizedDocumentFilter(inputTextPane));

		return inputTextPane;
	}

	/**
	 * This method will prepare the focus of the window onto inputField.
	 * 
	 * @return boolean on success of execution
	 */
	public boolean setInputFocus() {
		return inputField.requestFocusInWindow();
	}

	/**
	 * This method will set the text pane
	 * to null for clearing text.
	 * 
	 */
	public static void setPaneToNull(){
		textPane.setText(null);
	}

	/**
	 * This method will display and print out
	 * the error message.
	 * 
	 * @param msg
	 * 				String msg to be displayed
	 */
	public static void displayError(String msg){
		errorLabel.setText(msg);
		if (!errorTimer.isRunning()) {
			errorPanel.setVisible(true);
			errorTimer.start();
		}
	}
	
	/**
	 * This method will update and print out
	 * the given string.
	 * 
	 * @param msg
	 * 				String msg to be displayed
	 */
	public static void updatePrint(String msg){
		append(commandList.getLast().getData(), true);
		append(msg +NEXT_LINE, false);
	}

	/**
	 * This method prepare the reminder panel 
	 * to display the reminders of tasks.
	 * 
	 * @param taskList
	 * 				array list of tasks 
	 */
	public void createReminder(ArrayList<Task> taskList){
		//System.out.println("Reminder alert");
		if (reminderDialog == null) {
			Window topWindow = SwingUtilities.getWindowAncestor(panel);
			reminderDialog = new JDialog(topWindow, MODAL_OPTION , ModalityType.APPLICATION_MODAL);
			reminderDialog.getContentPane().add(new ReminderPanel(taskList, reminderDialog).getMainPanel());
			reminderDialog.pack();
			reminderDialog.setLocationRelativeTo(topWindow);
			reminderDialog.setLocation(reminderDialog.getLocation().x, 0);
			reminderDialog.setVisible(true);
		} else {
			reminderDialog.getContentPane().add(new ReminderPanel(taskList, reminderDialog).getMainPanel());
			reminderDialog.pack();
			reminderDialog.setVisible(true);
		}
	}

	/**
	 * This method sets the dialog to null
	 * for next reminder.
	 * 
	 */
	public static void setDialogNull(){
		reminderDialog = null;
	}
	
	public void triggerCommandSuggestor(){
		commandSuggestor.checkForAndShowSuggestions();
	}

	/**
	 * This method processes the key pressed by user
	 * and specifically looks for key up and key down
	 * to perform histories of commands.
	 * 
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch( keyCode ) { 
		case KeyEvent.VK_UP:
			// handle up 
			historyOnUp();
			break;
		case KeyEvent.VK_DOWN:
			// handle down
			historyOnDown();
			break;
		case KeyEvent.VK_LEFT:
			// handle left
			break;
		case KeyEvent.VK_RIGHT :
			// handle right
			break;
		}
	}

	/**
	 * This method keeps track of the
	 * history of the commands keyed by users and
	 * on down button. It will display the history which 
	 * is more recent. If it is most recent,
	 * it will not change anymore.
	 * 
	 */
	public void historyOnDown() {
		if(node != null){
			if(node.getNext()!=null){
				node = node.getNext();
				inputField.setText(node.getData());
			} else{
				if(currentCommand!=null){
					inputField.setText(currentCommand);
					currentCommand = null;
				}
			}
		}
	}

	/**
	 * This method keeps track of the
	 * history of the commands keyed by users and
	 * on up button. It will display the history which is 
	 * previously typed, in chronological order. If it is 
	 * the last of the commands keyed, it will not change anymore.
	 * Current command keyed will be saved on the cache and can
	 * be retrieved by down button.
	 * 
	 */
	public void historyOnUp() {
		if(node == null){
			node = commandList.getLast();
			if(currentCommand == null){
				currentCommand = inputField.getText();
			}
			if(node!=null){
				inputField.setText(node.getData());
			}

		} else{
			if(node.getPrevious()!=null){
				node = node.getPrevious();
				if(currentCommand == null){
					currentCommand = inputField.getText();
					node = commandList.getLast();
				}
				inputField.setText(node.getData());
			}
		}
	}

	//@@author generated
	@Override
	public void keyTyped(KeyEvent e) {}

	//@@author generated
	@Override
	public void keyReleased(KeyEvent e) {}
	
}
