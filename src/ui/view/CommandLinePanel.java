package ui.view;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

import ui.controller.UIController;

@SuppressWarnings("serial")
public class CommandLinePanel extends JPanel implements Observer {
	
	 /*** Variables ***/
	protected static int NUM_COMPONENTS = 3;
	protected UIController uiController = null;
	private static final String STRING_EMPTY = "";
	private static Font font = new Font("Courier",Font.PLAIN, 12);
	private static final String NEXT_LINE = "\n";
	private JTextField inputField;
	private JTextArea inputTextArea;
	//protected static boolean restrictSize = true;
	//protected static boolean sizeIsRandom = false;
	
	/*** Constructors ***/
	public CommandLinePanel(){
		uiController = UIController.getInstance(this);
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
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JTextArea textArea = prepareJTextArea();
		JTextField inputField = prepareTextField(textArea);
		JScrollPane areaScrollPane = prepareScrollPane(textArea);
		areaScrollPane.setAutoscrolls(true);
		Box box = prepareBoxComponent(inputField, areaScrollPane);
		
		panel.add(box, BorderLayout.PAGE_END);
		contentPane.add(panel, BorderLayout.CENTER);
		this.setInputFocus();
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
	private JScrollPane prepareScrollPane(JTextArea textArea) {
		JScrollPane areaScrollPane = new JScrollPane(textArea);
		areaScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		return areaScrollPane;
	}

	private JTextField prepareTextField(final JTextArea textArea) {
		inputField = new JTextField();
		inputField.setMaximumSize(new Dimension(inputField.getMaximumSize().width ,inputField.getPreferredSize().height));
		inputField.requestFocus();
		inputField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String input = inputField.getText();
				textArea.append(input);
				textArea.append(NEXT_LINE);
				String[] output = uiController.processUserInput(input);
				if (textArea.getText().length() > 0) {
					for(String s : output){
						textArea.append( s + NEXT_LINE);
					}
					textArea.append(NEXT_LINE);
				}
				inputField.setText(STRING_EMPTY);
			}
		});
		return inputField;
	}
	
    /**
     * This method prepares a text area for the output of the program to be displayed
     * on.
     * 
     * @return JTextArea for display
     */
	private JTextArea prepareJTextArea() {
		inputTextArea = new JTextArea();
		inputTextArea.setFont(font);
		inputTextArea.setLineWrap(true);
		inputTextArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret)inputTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		return inputTextArea;
	}

	/*public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			restrictSize = true;
		} else {
			restrictSize = false;
		}
	}*/
	
	public boolean setInputFocus() {
		return inputField.requestFocusInWindow();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg == null) {
			inputTextArea.setText(null);
		} else {
			String msg = (String)arg;
			inputTextArea.append(msg);
			inputTextArea.append(NEXT_LINE);
		}
	}

}
