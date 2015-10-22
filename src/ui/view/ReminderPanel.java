package ui.view;

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

import task.entity.Task;
import ui.controller.UIController;

public class ReminderPanel {
	private ArrayList<Task> taskList = null;
	private static final String TITLE = "PaddleTask Reminder";
	private static final String CLOSE_MSG = "Close";
	private static final String NEXT_LINE = "\n";
	private JPanel reminderPanel = new JPanel();
	private JTextArea textArea;
	private JDialog dialog;
	private JButton okButton;
	private static Font font = new Font("Courier",Font.PLAIN, 12);
	private Box box = null;
	
	public ReminderPanel(ArrayList<Task> taskList, JDialog dialog){
		this.taskList = taskList;
		this.dialog = dialog;
		dialog.setTitle(TITLE);
		removeDefaultButtons(dialog);
		preparePanelComponents();
		processArrayList();
	}
	
	private void processArrayList(){
		UIController uiController = UIController.getInstance(null);
		String[] output = uiController.format(taskList);
		appendTexts(textArea, output);
	}

	private void preparePanelComponents() {
		reminderPanel.setLayout(new BoxLayout(reminderPanel, BoxLayout.PAGE_AXIS));
		textArea = prepareJTextArea();
		JScrollPane scrollPane = prepareScrollPane(textArea);
		okButton = prepareButton();
		prepareBoxComponent(scrollPane);
		prepareBoxComponent(okButton);
		reminderPanel.add(box, BorderLayout.PAGE_END);

	}
	
	private JScrollPane prepareScrollPane(JTextArea textArea) {
		JScrollPane areaScrollPane = new JScrollPane(textArea);
		areaScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setAutoscrolls(true);
		return areaScrollPane;
	}
	
	private JTextArea prepareJTextArea() {
		textArea = new JTextArea();
		textArea.setFont(font);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		Dimension size = reminderPanel.getToolkit().getScreenSize();
		size.setSize(size.width / 2 , size.height / 3);
		textArea.setPreferredSize(size);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		return textArea;
	} 
	
	public JPanel getMainPanel(){
		return reminderPanel;
	}
	
	public Box prepareBoxComponent(Component comp) {
		if(box == null){
			box = Box.createVerticalBox();
		}
		box.add(comp,BorderLayout.CENTER);
		return box;
	}
	
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
	
}
