package main.paddletask.ui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import main.paddletask.background.Reminder;

@SuppressWarnings("serial")
public class MainPanel extends JPanel{
	
	private static Container contentPane = null;
	private static MainFrame mainFrame = null;
	
	public void populateContentPane(Container contentPane, MainFrame mainFrame) {
		MainPanel.contentPane = contentPane;
		this.mainFrame = mainFrame;
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		panel.setBackground(Color.white);
		JButton commandLineButton = prepareCliButton();
		JButton graphicButton = prepareGraphicButton();
		Box box = prepareBoxComponent(commandLineButton, graphicButton);
		
		panel.add(box, BorderLayout.PAGE_END );
		contentPane.add(panel, BorderLayout.CENTER);
	}
	
	public Box prepareBoxComponent(JButton first, JButton second) {
		Box box = Box.createVerticalBox();
		box.add(first);
		box.add(second);
		box.setAlignmentX(Component.CENTER_ALIGNMENT);
		//box.setLayout(BorderLayout.CENTER);
		return box;
	}
	
	public JButton prepareCliButton(){
		URL resource = Reminder.class.getResource("/images/CLI.png");
		Image buttonIcon = Toolkit.getDefaultToolkit().getImage(resource);
		JButton button = new JButton(new ImageIcon(buttonIcon));
		button.setPreferredSize(new Dimension(buttonIcon.getHeight(null), buttonIcon.getWidth(null)));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				contentPane.removeAll();
				CommandLinePanel nextPanel = new CommandLinePanel(mainFrame);
				nextPanel.populateContentPane(contentPane);
				contentPane.validate();
				contentPane.repaint();

			}
		});
		return button;
	}
	
	public JButton prepareGraphicButton(){
		URL resource = Reminder.class.getResource("/images/GUI.png");
		Image buttonIcon = Toolkit.getDefaultToolkit().getImage(resource);
		JButton button = new JButton(new ImageIcon(buttonIcon));
		button.setPreferredSize(new Dimension(buttonIcon.getHeight(null), buttonIcon.getWidth(null)));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*contentPane.removeAll();
				CommandLinePanel nextPanel = new CommandLinePanel();
				nextPanel.populateContentPane(contentPane);
				 */
			}
		});
		return button;
	}
	
	public boolean setInputFocus() {
		return true;
	}
	
}
