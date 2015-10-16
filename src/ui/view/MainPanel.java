package ui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainPanel extends JPanel{
	
	private static Container contentPane = null;
	
	public void populateContentPane(Container contentPane) {
		this.contentPane = contentPane;
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JButton commandLineButton = prepareCliButton();
		JButton graphicButton = prepareGraphicButton();
		Box box = prepareBoxComponent(commandLineButton, graphicButton);
		
		panel.add(box, BorderLayout.CENTER);
		contentPane.add(panel, BorderLayout.CENTER);
	}
	
	public Box prepareBoxComponent(JButton first, JButton second) {
		Box box = Box.createVerticalBox();
		box.add(first);
		box.add(second);
		return box;
	}
	
	public JButton prepareCliButton(){
		BufferedImage buttonIcon = null;
		try {
			buttonIcon = ImageIO.read(new File("src/images/CLI.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JButton button = new JButton(new ImageIcon(buttonIcon));
		//button.setPreferredSize(new Dimension(buttonIcon.getHeight(), buttonIcon.getWidth()));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				contentPane.removeAll();
				CommandLinePanel nextPanel = new CommandLinePanel();
				nextPanel.populateContentPane(contentPane);
			}
		});
		return button;
	}
	
	public JButton prepareGraphicButton(){
		BufferedImage buttonIcon = null;
		try {
			buttonIcon = ImageIO.read(new File("src/images/GUI.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JButton button = new JButton(new ImageIcon(buttonIcon));
		button.setPreferredSize(new Dimension(buttonIcon.getHeight(), buttonIcon.getWidth()));
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
