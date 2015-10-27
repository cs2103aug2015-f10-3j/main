package ui.view;
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;

import background.Reminder;
import command.api.ClearCommand;
import common.data.DoublyLinkedList;
import common.data.Node;
import common.util.DateTimeHelper;
import task.entity.Task;
import ui.controller.UIController;

@SuppressWarnings("serial")
public class CommandLinePanel extends JPanel implements Observer,KeyListener {

	/*** Variables ***/
	private static final String STRING_EMPTY = "";
	private static final String NEXT_LINE = "\n";
	private static final String WELCOME_MSG_1 = "Welcome to PaddleTask.";
	private static final String WELCOME_MSG_2 = "Today is %s.";
	private static final String WELCOME_MSG_3 = "Your upcoming tasks for today:";
	private static final String FIRST_COMMAND = "view all today";
	protected static int NUM_COMPONENTS = 3;
	protected UIController uiController = null;
	private static Font font = new Font("Courier",Font.PLAIN, 12);
	private JTextField inputField = null;
	//private JTextArea textArea = null;
	private JTextPane textPane = null;
	public static JDialog reminderDialog = null;
	private JPanel panel = null;
	private DoublyLinkedList commandList = null;
	private Box box = null;
	private Node node = null;
	private String currentCommand = null;
	private JScrollPane scrollPane = null;

	//protected static boolean restrictSize = true;
	//protected static boolean sizeIsRandom = false;

	/*** Constructors ***/
	public CommandLinePanel(){
		uiController = UIController.getInstance(this);
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
		box = prepareBoxComponent(inputField, scrollPane);

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
		String today = DateTimeHelper.getDate(LocalDateTime.now());
		String[] outputs = new String[3];
		int counter = 0;
		outputs[counter++] = WELCOME_MSG_1;
		outputs[counter++] = String.format(WELCOME_MSG_2, today);
		outputs[counter++] = WELCOME_MSG_3;
		append(outputs);
		performCommand(FIRST_COMMAND);
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
	public Box prepareBoxComponent(JTextField inputField, JScrollPane areaScrollPane) {
		Box box = Box.createVerticalBox();
		box.add(areaScrollPane);
		box.add(inputField);
		return box;
	}

	/**
	 * This method prepares a scroll pane for the textarea to enable scrolling
	 * for display.
	 * 
	 * @param JTextArea
	 *            the input field on the panel

	 * @return JScrollPane with textarea
	 */
	private JScrollPane prepareScrollPane(JTextPane textPane) {
		final JScrollPane areaScrollPane = new JScrollPane(textPane);
		areaScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setAutoscrolls(true);
		/*areaScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

			BoundedRangeModel brm = areaScrollPane.getVerticalScrollBar().getModel();
			boolean wasAtBottom = true;

			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (!brm.getValueIsAdjusting()) {
					if (wasAtBottom)
						brm.setValue(brm.getMaximum());
				} else
					wasAtBottom = ((brm.getValue() + brm.getExtent()) == brm.getMaximum());

			}
		});*/
		return areaScrollPane;
	}

	/**
	 * This method prepares a textfield for the user to input data.
	 * This will initiate UIController.java upon execution of a input by the user.
	 * 
	 * @param JTextArea
	 *            the input field on the panel

	 * @return JTextField 
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
				append(input);
				performCommand(input);
				inputField.setText(STRING_EMPTY);
				addCommandToList(input);
				currentCommand = null;
			}
		});
		return inputField;
	}

	private void addCommandToList(String input){
		node = null;
		commandList.insertLast(input);
	}

	public void append(String s) {
		try {
			Document doc = textPane.getDocument();
			textPane.setCaretPosition(doc.getLength());
			doc.insertString(doc.getLength(), s + NEXT_LINE, null);
			/*Rectangle r = textPane.modelToView(doc.getLength());
			if (r != null) {
				scrollRectToVisible(r);
			}*/
		} catch(BadLocationException exc) {
			assert false;
			exc.printStackTrace();
		}

	}

	public void append(String[] output){
		//textPane.setEditable(true);
		try {
			Document doc = textPane.getDocument();
			textPane.setCaretPosition(doc.getLength());
			String finalOutput = "";
			for(String s : output ){
				finalOutput += s + NEXT_LINE;
			}
			finalOutput += NEXT_LINE;
			doc.insertString(doc.getLength(), finalOutput, null);
		} catch(BadLocationException exc) {
			assert false;
			exc.printStackTrace();
		}
		box.repaint();
		scrollPane.repaint();
		panel.repaint();
		//textPane.setEditable(false);
	}

	public void performCommand(String input){
		String[] output = uiController.processUserInput(input);
		append(output);
	}

	/**
	 * This method appends text into the textArea to display to the user
	 * with a input from the user.
	 * 
	 * @param JTextArea
	 *            the input field on the panel
	 *        input
	 *        	  the user input 
	 */
	public void appendTexts(final JTextArea textArea, String input) {
		String[] output = uiController.processUserInput(input);
		if (textArea.getText().length() > 0) {
			for(String s : output){
				textArea.append( s + NEXT_LINE);
			}
			textArea.append(NEXT_LINE);
		}
	}

	/**
	 * This method appends text into the textArea to display to the user
	 * with an array of output
	 * 
	 * @param JTextArea
	 *            the input field on the panel
	 *        output
	 *        	  a string array of outputs 
	 */
	public void appendTexts(final JTextArea textArea, String[] output) {
		for(String s : output){
			textArea.append( s + NEXT_LINE);
		}
	}

	/**
	 * This method prepares a text area for the output of the program to be displayed
	 * on.
	 * 
	 * @return JTextArea for display
	 */
	private JTextPane prepareJTextPane() {
		JTextPane inputTextPane = new JTextPane();
		inputTextPane.setFont(font);
		//inputTextPane.setLineWrap(true);
		inputTextPane.setEditable(false);

		/*DefaultCaret caret = (DefaultCaret)inputTextPane.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);*/
		return inputTextPane;
	}

	/*public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			restrictSize = true;
		} else {
			restrictSize = false;
		}
	}*/

	/**
	 * This method will prepare the focus of the window onto inputField.
	 * 
	 * @return boolean on success of execution
	 */
	public boolean setInputFocus() {
		return inputField.requestFocusInWindow();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof ClearCommand) {
			textPane.setText(null);
		} else if(o instanceof Reminder){
			createReminder((ArrayList<Task>)arg);
		} else {
			String msg = (String)arg;
			append(msg);
		}
	}

	public void createReminder(ArrayList<Task> taskList){
		System.out.println("Reminder alert");
		if (reminderDialog == null) {
			Window topWindow = SwingUtilities.getWindowAncestor(panel);
			reminderDialog = new JDialog(topWindow, "Modal Dialog", ModalityType.APPLICATION_MODAL);
			reminderDialog.getContentPane().add(new ReminderPanel(taskList, reminderDialog).getMainPanel());
			reminderDialog.pack();
			reminderDialog.setLocationRelativeTo(topWindow);
			reminderDialog.setVisible(true);
		} else {
			reminderDialog.getContentPane().add(new ReminderPanel(taskList, reminderDialog).getMainPanel());
			reminderDialog.pack();
			reminderDialog.setVisible(true);
		}
	}

	public static void setDialogNull(){
		reminderDialog = null;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int keyCode = e.getKeyCode();
		switch( keyCode ) { 
		case KeyEvent.VK_UP:
			// handle up 
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
			break;
		case KeyEvent.VK_DOWN:
			// handle down
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
			break;
		case KeyEvent.VK_LEFT:
			// handle left
			break;
		case KeyEvent.VK_RIGHT :
			// handle right
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}


}
