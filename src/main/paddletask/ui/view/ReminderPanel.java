//@@author A0125528E
package main.paddletask.ui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;

import main.paddletask.task.entity.Task;
import main.paddletask.ui.controller.UIController;

public class ReminderPanel {
	
	/*** Variables ***/
	private ArrayList<Task> taskList = null;
	private static final String TITLE = "PaddleTask Reminder";
	private static final String CLOSE_MSG = "Close";
	private static final String NEXT_LINE = "\n";
	private static final String OPTION_MAXIMIZE = "Maximize";
	private static final String OPTION_ICONIFY = "Iconify";
	private static final String OPTION_CLOSE = "Close";
	private static final String STRING_EMPTY = "";
	private static final char PRIORITY_INDICATOR = '*';
	private static final char BOLD_INDICATOR = '@';
	private static final int OFFSET_ZERO = 0;
	private static final int SUBSTRING_BEGIN = 1;
	private JPanel reminderPanel = new JPanel();
	private JTextPane textPane;
	private JDialog dialog;
	private JButton okButton;
	private static Font font = new Font("Consolas",Font.BOLD, 15);
	private Box box = null;
	private static final int HEIGHT = 4;
	private static final int WIDTH = 2;
	private static final Color backgroundColor = new Color(180, 0, 0); //Red color
	
	/*** Constructors ***/
	public ReminderPanel(ArrayList<Task> taskList, JDialog dialog){
		this.taskList = taskList;
		this.dialog = dialog;
		dialog.setTitle(TITLE);
		removeDefaultButtons(dialog);
		preparePanelComponents();
		processArrayList();
	}
	
	/*** Methods ***/
	/**
	 * This method process the array lists given
	 * and formats it by calling uiController.
	 * Followed after will be preparing to append
	 * the text for display. 
	 * 
	 */
	private void processArrayList(){
		UIController uiController = UIController.getInstance(null);
		String[] output = uiController.format(taskList);
		append(output);
	}

	/**
	 * This method prepares the panel layout
	 * and all the necessary components needed for
	 * PaddleTask to display reminders.
	 * 
	 */
	private void preparePanelComponents() {
		reminderPanel.setLayout(new BoxLayout(reminderPanel, BoxLayout.PAGE_AXIS));
		textPane = prepareJTextPane();
		JScrollPane scrollPane = prepareScrollPane(textPane);
		okButton = prepareButton();
		prepareBoxComponent(scrollPane);
		prepareBoxComponent(okButton);
		reminderPanel.add(box, BorderLayout.PAGE_END);
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
		JScrollPane areaScrollPane = new JScrollPane(textPane);
		areaScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setAutoscrolls(true);
		return areaScrollPane;
	} 
	
	/**
	 * This method gets the reminderPanel when called.
	 * 
	 * @return textArea
	 * 				JTextPane of the panel
	 */
	public JPanel getMainPanel(){
		return reminderPanel;
	}
	
	/**
	 * This method prepares a box component to be created on the panel.
	 * Aligns the component together in a box layout vertically.
	 * 
	 * @param comp
	 * 				Component to be added into box		
	 * @return box
	 * 			Box formatting with the components on the panel
	 */
	public Box prepareBoxComponent(Component comp) {
		if(box == null){
			box = Box.createVerticalBox();
		}
		box.add(comp,BorderLayout.CENTER);
		return box;
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
        Dimension size = reminderPanel.getToolkit().getScreenSize();
        size.setSize(size.width / WIDTH , size.height / HEIGHT);
        inputTextPane.setPreferredSize(size);

		return inputTextPane;
	}
	
	/**
	 * This method prepares a button to allow the user
	 * to close the reminder when clicked.
	 * 
	 * @return textArea
	 * 				JTextPane of the panel
	 */
	public JButton prepareButton(){
		JButton button = new JButton();
		button.setText(CLOSE_MSG);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				MainPanel.setDialogNull();
			}
		});
		return button;
	}
	
	/**
	 * This method appends a string of text into the textPane to display to the user
	 * 
	 * @param s
	 *        	String s to be appended to textPane
	 */
	public void append(String s) {
		try {
			Document doc = textPane.getDocument();
			textPane.setCaretPosition(doc.getLength());
			doc.insertString(doc.getLength(), s + NEXT_LINE + NEXT_LINE, null);
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
						color.addAttributes(CustomizedDocumentFilter.changeToWhite());
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
	 * This method removes the default buttons of minimize, maximise and close
	 * on the Dialog. 
	 * 
	 *  @param  Component 
	 *  			JDialog reminderDialog
	 */
	
	public static void removeDefaultButtons(Component com){
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
	
}
