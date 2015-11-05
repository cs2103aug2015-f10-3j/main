//@@author A0125528E
package main.paddletask.ui.view;

import java.awt.BorderLayout;
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
import javax.swing.text.DefaultCaret;

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
	private JPanel reminderPanel = new JPanel();
	private JTextArea textArea;
	private JDialog dialog;
	private JButton okButton;
	private static Font font = new Font("Courier",Font.PLAIN, 12);
	private Box box = null;
	private static final int HEIGHT = 3;
	private static final int WIDTH = 2;
	
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
		appendTexts(textArea, output);
	}

	/**
	 * This method prepares the panel layout
	 * and all the necessary components needed for
	 * PaddleTask to display reminders.
	 * 
	 */
	private void preparePanelComponents() {
		reminderPanel.setLayout(new BoxLayout(reminderPanel, BoxLayout.PAGE_AXIS));
		textArea = prepareJTextArea();
		JScrollPane scrollPane = prepareScrollPane(textArea);
		okButton = prepareButton();
		prepareBoxComponent(scrollPane);
		prepareBoxComponent(okButton);
		reminderPanel.add(box, BorderLayout.PAGE_END);
	}
	
	/**
	 * This method prepares a scroll pane for the textPane to enable scrolling
	 * for display.
	 * 
	 * @param textArea
	 * 				JTextPane of the panel

	 * @return areaScrollPane 
	 * 				JScrollPane with textPane
	 */
	private JScrollPane prepareScrollPane(JTextArea textArea) {
		JScrollPane areaScrollPane = new JScrollPane(textArea);
		areaScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setAutoscrolls(true);
		return areaScrollPane;
	}
	
	/**
	 * This method prepares a text area for displaying
	 * output to the user.
	 * 
	 * @return textArea
	 * 				JTextPane of the panel
	 */
	private JTextArea prepareJTextArea() {
		textArea = new JTextArea();
		textArea.setFont(font);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		Dimension size = reminderPanel.getToolkit().getScreenSize();
		size.setSize(size.width / WIDTH , size.height / HEIGHT);
		textArea.setPreferredSize(size);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		return textArea;
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
				CommandLinePanel.setDialogNull();
			}
		});
		return button;
	}
	
	/**
	 * This method prepares a button to allow the user
	 * to close the reminder when clicked.
	 * 
	 * @param textArea
	 * 				JTextArea to be appended to.
	 * 		  output
	 * 				String array of outputs
	 */
	public void appendTexts(final JTextArea textArea, String[] output) {
		for(String s : output){
			textArea.append( s + NEXT_LINE);
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
