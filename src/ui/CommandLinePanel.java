package ui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class CommandLinePanel extends Panel implements ItemListener {
	protected static int NUM_COMPONENTS = 3;
 
    protected static boolean restrictSize = true;
    protected static boolean sizeIsRandom = false;
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

	public JScrollPane prepareScrollPane(JTextArea textArea) {
		JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		return areaScrollPane;
	}

	public JTextField prepareTextField(final JTextArea textArea) {
		final JTextField inputField = new JTextField();
		inputField.setMaximumSize(new Dimension(inputField.getMaximumSize().width ,inputField.getPreferredSize().height));
        inputField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               textArea.append(inputField.getText() + "\n");
               inputField.setText(STRING_EMPTY);
            }
        });
		return inputField;
	}

	public JTextArea prepareJTextArea() {
		JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		return textArea;
	}
 
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            restrictSize = true;
        } else {
            restrictSize = false;
        }
    }

}
