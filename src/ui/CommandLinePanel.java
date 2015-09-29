package ui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class CommandLinePanel extends Panel {
	protected static int NUM_COMPONENTS = 3;

	//protected static boolean restrictSize = true;
	//protected static boolean sizeIsRandom = false;
	private static final String STRING_EMPTY = "";

	public void populateContentPane(Container contentPane) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		JTextArea textArea = prepareJTextArea();
		JTextField inputField = prepareTextField(textArea);
		JScrollPane areaScrollPane = prepareScrollPane(textArea);

		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		Box box = Box.createVerticalBox();
		box.add(areaScrollPane);
		box.add(inputField);
		panel.add(box, BorderLayout.PAGE_END);
		contentPane.add(panel, BorderLayout.CENTER);
	}

	private JScrollPane prepareScrollPane(JTextArea textArea) {
		JScrollPane areaScrollPane = new JScrollPane(textArea);
		areaScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		return areaScrollPane;
	}

	private JTextField prepareTextField(final JTextArea textArea) {
		final JTextField inputField = new JTextField();
		inputField.setMaximumSize(new Dimension(inputField.getMaximumSize().width ,inputField.getPreferredSize().height));
		inputField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String input = inputField.getText();
				UIController uiController = new UIController();
				String[] output = uiController.processUserInput(input);
				textArea.append( input + "\n");
				for(String s : output){
					textArea.append( s + "\n");
				}
				inputField.setText(STRING_EMPTY);
			}
		});
		return inputField;
	}

	private JTextArea prepareJTextArea() {
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		return textArea;
	}

	/*public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			restrictSize = true;
		} else {
			restrictSize = false;
		}
	}*/

}
