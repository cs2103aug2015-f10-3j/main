//@@author A0125528E
package main.paddletask.ui.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.paddletask.common.data.ParserConstants;

public class CommandSuggestor {

	private final JTextField textField;
	private final Window container;
	private JPanel suggestionsPanel;
	private JWindow autoSuggestionPopUpWindow;
	private String typedWord;
	private final HashMap<String, ArrayList<String>> optionsMap = new HashMap<String, ArrayList<String>>();
	private int currentIndexOfSpace, suggestionWindowWidth, suggestionWindowHeight;
	private static final String KEYNAME_DOWN = "Down released";
	private static final String KEYNAME_UP = "Up released";
	private static final String KEYNAME_F2 = "F2 released";
	private static final String KEYNAME_ESC = "Esc released";
	private static final String SPACE = " ";
	private static final int COMMAND_WORD = 0;
	private static final int OPTIONS_WORD = 1;
	private static final int INVALID_WORD = -1;
	private static final String EMPTY_STRING = "";
	private static final char CHAR_SLASH = '/';
	private static final char CHAR_DASH = '-';
	private static int currentIndex = 0;

	private DocumentListener documentListener = new DocumentListener() {
		@Override
		public void insertUpdate(DocumentEvent de) {
			checkForAndShowSuggestions();
		}

		@Override
		public void removeUpdate(DocumentEvent de) {
			checkForAndShowSuggestions();
		}

		@Override
		public void changedUpdate(DocumentEvent de) {
			checkForAndShowSuggestions();
		}
	};
	private final Color suggestionsTextColor;
	private final Color suggestionFocusedColor;

	public CommandSuggestor(JTextField textField, Window mainWindow, Color popUpBackground, Color textColor, Color suggestionFocusedColor, float opacity) {
		this.textField = textField;
		this.suggestionsTextColor = textColor;
		this.container = mainWindow;
		this.suggestionFocusedColor = suggestionFocusedColor;
		this.textField.getDocument().addDocumentListener(documentListener);

		typedWord = "";
		currentIndexOfSpace = 0;
		suggestionWindowWidth = 0;
		suggestionWindowHeight = 0;

		createCommandOptionMap();

		mainWindow.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent arg0) {
				recalculatingNewPosition();
			}
			
			public void componentResized(ComponentEvent e){
				recalculatingNewPosition();
			}
		});

		autoSuggestionPopUpWindow = new JWindow(mainWindow);
		autoSuggestionPopUpWindow.setOpacity(opacity);
		autoSuggestionPopUpWindow.setVisible(false);

		suggestionsPanel = new JPanel();
		suggestionsPanel.setLayout(new GridLayout(0, 1));
		suggestionsPanel.setBackground(popUpBackground);

		addKeyBindingToRequestFocusInPopUpWindow();
	}

	private void recalculatingNewPosition() {
		int windowX = 0, windowY = 0;
		windowX = textField.getLocationOnScreen().x;
		windowY = textField.getLocationOnScreen().y + textField.getHeight();
		autoSuggestionPopUpWindow.setLocation(windowX, windowY);
		if(!isLocationInScreenBounds()){
			windowY -= autoSuggestionPopUpWindow.getPreferredSize().getHeight() + textField.getHeight();
			autoSuggestionPopUpWindow.setLocation(windowX, windowY);
		}
	}

	@SuppressWarnings("serial")
	private void addKeyBindingToRequestFocusInPopUpWindow() {
		textField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0, true), KEYNAME_F2);
		textField.getActionMap().put(KEYNAME_F2, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {//focuses the first label on popwindow
				currentIndex = 0;
				for (int i = 0; i < suggestionsPanel.getComponentCount(); i++) {
					if (suggestionsPanel.getComponent(i) instanceof SuggestionLabel) {
						((SuggestionLabel) suggestionsPanel.getComponent(i)).setFocused(true);
						autoSuggestionPopUpWindow.toFront();
						autoSuggestionPopUpWindow.requestFocusInWindow();
						suggestionsPanel.requestFocusInWindow();
						suggestionsPanel.getComponent(i).requestFocusInWindow();
						break;
					}
				}
			}
		});
		suggestionsPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), KEYNAME_DOWN);
		suggestionsPanel.getActionMap().put(KEYNAME_DOWN, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent ae) {//allows scrolling of labels in pop window (I know very hacky for now :))

				ArrayList<SuggestionLabel> suggestionLabelLists = getAddedSuggestionLabels();
				int max = suggestionLabelLists.size();

				if (max > 1) {//more than 1 suggestion
					SuggestionLabel currentLabel = suggestionLabelLists.get(currentIndex);
					if (currentLabel.isFocused()) {
						currentLabel.setFocused(false);
					}
					currentIndex++;
					if (currentIndex >= max) {
						currentIndex = 0;
					}
					suggestionLabelLists.get(currentIndex).setFocused(true);
					autoSuggestionPopUpWindow.toFront();
					autoSuggestionPopUpWindow.requestFocusInWindow();
					suggestionsPanel.requestFocusInWindow();
					suggestionsPanel.getComponent(currentIndex).requestFocusInWindow();
				}
			}
		});
		
		suggestionsPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), KEYNAME_UP);
		suggestionsPanel.getActionMap().put(KEYNAME_UP, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent ae) {//allows scrolling of labels in pop window (I know very hacky for now :))

				ArrayList<SuggestionLabel> suggestionLabelLists = getAddedSuggestionLabels();
				int max = suggestionLabelLists.size();

				if (max > 1) {//more than 1 suggestion
					SuggestionLabel currentLabel = suggestionLabelLists.get(currentIndex);
					if (currentLabel.isFocused()) {
						currentLabel.setFocused(false);
					}
					currentIndex--;
					if (currentIndex < 0) {
						currentIndex = max - 1;
					}
					suggestionLabelLists.get(currentIndex).setFocused(true);
					autoSuggestionPopUpWindow.toFront();
					autoSuggestionPopUpWindow.requestFocusInWindow();
					suggestionsPanel.requestFocusInWindow();
					suggestionsPanel.getComponent(currentIndex).requestFocusInWindow();
				}
			}
		});
		suggestionsPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), KEYNAME_ESC);
		suggestionsPanel.getActionMap().put(KEYNAME_ESC, new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				autoSuggestionPopUpWindow.setVisible(false);
				setFocusToTextField();
				checkForAndShowSuggestions();
			}

		});
	}

	private void setFocusToTextField() {
		container.toFront();
		container.requestFocusInWindow();
		textField.requestFocusInWindow();
	}

	public ArrayList<SuggestionLabel> getAddedSuggestionLabels() {
		ArrayList<SuggestionLabel> sls = new ArrayList<>();
		for (int i = 0; i < suggestionsPanel.getComponentCount(); i++) {
			if (suggestionsPanel.getComponent(i) instanceof SuggestionLabel) {
				SuggestionLabel sl = (SuggestionLabel) suggestionsPanel.getComponent(i);
				sls.add(sl);
			}
		}
		return sls;
	}

	public void checkForAndShowSuggestions() {
		typedWord = getCurrentlyTypedWord();
		int wordCategory = checkWordCategory(typedWord);

		suggestionsPanel.removeAll();//remove previos words/jlabels that were added

		//used to calcualte size of JWindow as new Jlabels are added
		suggestionWindowWidth = 0;
		suggestionWindowHeight = 0;

		boolean added = wordTyped(typedWord, wordCategory);

		if (!added) {
			if (autoSuggestionPopUpWindow.isVisible()) {
				autoSuggestionPopUpWindow.setVisible(false);
			}
		} else {
			showPopUpWindow();
			setFocusToTextField();
		}
	}

	protected void addWordToSuggestions(String word) {
		SuggestionLabel suggestionLabel = new SuggestionLabel(word, suggestionFocusedColor, suggestionsTextColor, this);

		calculatePopUpWindowSize(suggestionLabel);

		suggestionsPanel.add(suggestionLabel);
	}

	public String getCurrentlyTypedWord() {//get newest word after last white spaceif any or the first word if no white spaces
		String text = textField.getText();
		String wordBeingTyped = "";
		if (text.contains(SPACE)) {
			int tmp = text.lastIndexOf(SPACE);
			if (tmp >= currentIndexOfSpace) {
				currentIndexOfSpace = tmp;
				wordBeingTyped = text.substring(text.lastIndexOf(SPACE));
			}
		} else {
			wordBeingTyped = text;
		}
		return wordBeingTyped.trim();
	}

	public int checkWordCategory(String typedWord){
		String text = textField.getText();
		int currentPosition = textField.getCaretPosition();
		int firstSpacePosition = text.indexOf(SPACE);
		if (firstSpacePosition > currentPosition || firstSpacePosition == INVALID_WORD) {
			return COMMAND_WORD;
		} else if (firstSpacePosition == currentPosition) {
			return INVALID_WORD;
		} else {
			return OPTIONS_WORD;
		}
	}

	private void calculatePopUpWindowSize(JLabel label) {
		//so we can size the JWindow correctly
		if (suggestionWindowWidth < label.getPreferredSize().width) {
			suggestionWindowWidth = label.getPreferredSize().width;
		}
		suggestionWindowHeight += label.getPreferredSize().height;
	}

	private void showPopUpWindow() {
		autoSuggestionPopUpWindow.getContentPane().add(suggestionsPanel);
		autoSuggestionPopUpWindow.setSize(suggestionWindowWidth, suggestionWindowHeight);
		autoSuggestionPopUpWindow.setVisible(true);

		int windowX = 0;
		int windowY = 0;

		windowX = textField.getLocationOnScreen().x;//container.getX() + textField.getX() + 5;
		windowY = textField.getLocationOnScreen().y + textField.getHeight();
		/*
		if (suggestionsPanel.getHeight() > autoSuggestionPopUpWindow.getMinimumSize().height) {
			windowY = container.getY() + textField.getY() + textField.getHeight() + autoSuggestionPopUpWindow.getMinimumSize().height;
		} else {
			windowY = container.getY() + textField.getY() + textField.getHeight() + autoSuggestionPopUpWindow.getHeight();
		}*/

		autoSuggestionPopUpWindow.setLocation(windowX, windowY); 
		if (!isLocationInScreenBounds()) {
			windowY -= autoSuggestionPopUpWindow.getPreferredSize().getHeight() + textField.getHeight();
			autoSuggestionPopUpWindow.setLocation(windowX, windowY);
		}
		autoSuggestionPopUpWindow.setMinimumSize(new Dimension(0, 30));
		autoSuggestionPopUpWindow.revalidate();
		autoSuggestionPopUpWindow.repaint();
	}

	public JWindow getAutoSuggestionPopUpWindow() {
		return autoSuggestionPopUpWindow;
	}

	public Window getContainer() {
		return container;
	}

	public JTextField getTextField() {
		return textField;
	}

	public boolean wordTyped(String typedWord, int wordCategory) {
		ArrayList<String> dictionary = new ArrayList<String>();
		if (wordCategory == COMMAND_WORD) {
			dictionary = optionsMap.get(EMPTY_STRING);
		} else if (wordCategory == OPTIONS_WORD) {
			String command = getFirstWordFromTextField();
			if (optionsMap.containsKey(command)) {
				dictionary = optionsMap.get(command);
			}
		}

		if (typedWord.isEmpty()) {
			for (String word : dictionary) {
				if (word.charAt(0) != CHAR_SLASH && word.charAt(0) != CHAR_DASH) {
					addWordToSuggestions(word);
				}
			}
			return true;
		}
		
		if (checkPreviousWordIsOption(dictionary)) {
			return false;
		}
		
		boolean suggestionAdded = false;

		for (String word : dictionary) {
			boolean fullymatches = true;
			for (int i = 0; i < typedWord.length(); i++) {
				if (i < word.length()) {
					if (!typedWord.toLowerCase().startsWith(String.valueOf(word.toLowerCase().charAt(i)), i)) {//check for match
						fullymatches = false;
						break;
					}
				}
			}
			if (fullymatches) {
				addWordToSuggestions(word);
				suggestionAdded = true;
			}
		}
		return suggestionAdded;
	}

	private boolean checkPreviousWordIsOption(ArrayList<String> dictionary) {
		int currentPosition = textField.getCaretPosition();
		String text = textField.getText();
		String[] textArray = text.split(SPACE);
		int currentWordLocation = -1;
		int length = 0;
		for (int i = 0; i < textArray.length ; i++) {
			length += textArray[i].length();
			if(currentPosition < length){
				currentWordLocation = i;
			}
		}
		if (currentWordLocation > 0) {
			if(dictionary.contains(textArray[currentWordLocation - 1])){
				return true;
			}
		}
		return false;
	}

	public String getFirstWordFromTextField(){
		String[] textContents = textField.getText().trim().split(SPACE);
		return textContents[0];
	}

	public void createCommandOptionMap(){
		optionsMap.put("", createCommandsList());
		optionsMap.put(ParserConstants.COMMANDS.ADD.toString(), addOptionList());
		optionsMap.put(ParserConstants.COMMANDS.EDIT.toString(), editOptionList());
		optionsMap.put(ParserConstants.COMMANDS.VIEW.toString(), viewOptionList());
		optionsMap.put(ParserConstants.COMMANDS.TAG.toString(), tagOptionList());
		optionsMap.put(ParserConstants.COMMANDS.UNTAG.toString(), tagOptionList());
	}

	public ArrayList<String> createCommandsList(){
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<ParserConstants.COMMANDS> commandList = new ArrayList<ParserConstants.COMMANDS>
		(Arrays.asList(ParserConstants.COMMANDS.values()));
		for (ParserConstants.COMMANDS command : commandList) {
			words.add(command.toString());
		}
		return words;
	}

	public ArrayList<String> addOptionList(){
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<ParserConstants.ADD_OPTIONS> commandList = new ArrayList<ParserConstants.ADD_OPTIONS>
		(Arrays.asList(ParserConstants.ADD_OPTIONS.values()));
		for (ParserConstants.ADD_OPTIONS command : commandList) {
			words.add(command.toString());
		}
		return words;
	}

	public ArrayList<String> editOptionList(){
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<ParserConstants.EDIT_OPTIONS> commandList = new ArrayList<ParserConstants.EDIT_OPTIONS>
		(Arrays.asList(ParserConstants.EDIT_OPTIONS.values()));
		for (ParserConstants.EDIT_OPTIONS command : commandList) {
			words.add(command.toString());
		}
		return words;
	}

	public ArrayList<String> viewOptionList(){
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<ParserConstants.VIEW_OPTIONS> commandList = new ArrayList<ParserConstants.VIEW_OPTIONS>
		(Arrays.asList(ParserConstants.VIEW_OPTIONS.values()));
		for (ParserConstants.VIEW_OPTIONS command : commandList) {
			words.add(command.toString());
		}
		return words;
	}

	public ArrayList<String> tagOptionList(){
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<ParserConstants.TAGUNTAG_OPTIONS> commandList = new ArrayList<ParserConstants.TAGUNTAG_OPTIONS>(Arrays.asList(ParserConstants.TAGUNTAG_OPTIONS.values()));
		for (ParserConstants.TAGUNTAG_OPTIONS command : commandList) {
			words.add(command.toString());
		}
		return words;
	}

	public boolean isLocationInScreenBounds() {
		Point location = new Point();
		location.setLocation(autoSuggestionPopUpWindow.getLocation().x, autoSuggestionPopUpWindow.getLocation().y 
				+ autoSuggestionPopUpWindow.getHeight());
		// Check if the location is in the bounds of one of the graphics devices.
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
		Rectangle graphicsConfigurationBounds = new Rectangle();

		for (int j = 0; j < graphicsDevices.length; j++) {
			GraphicsDevice graphicsDevice = graphicsDevices[j];
			graphicsConfigurationBounds.setRect(graphicsDevice.getDefaultConfiguration().getBounds());
			graphicsConfigurationBounds.setRect(graphicsConfigurationBounds.x, graphicsConfigurationBounds.y,
					graphicsConfigurationBounds.width, graphicsConfigurationBounds.height);
			if (graphicsConfigurationBounds.contains(location.x, location.y)) {
				return true;
			}
		}
		return false;

	}
}

@SuppressWarnings("serial")
class SuggestionLabel extends JLabel {

	private boolean focused = false;
	private final JWindow autoSuggestionsPopUpWindow;
	private final JTextField textField;
	private final CommandSuggestor commandSuggestor;
	private Color suggestionsTextColor, suggestionBorderColor;

	public SuggestionLabel(String string, final Color borderColor, Color suggestionsTextColor, CommandSuggestor commandSuggestor) {
		super(string);

		this.suggestionsTextColor = suggestionsTextColor;
		this.commandSuggestor = commandSuggestor;
		this.textField = commandSuggestor.getTextField();
		this.suggestionBorderColor = borderColor;
		this.autoSuggestionsPopUpWindow = commandSuggestor.getAutoSuggestionPopUpWindow();

		initComponent();
	}

	private void initComponent() {
		setFocusable(true);
		setForeground(suggestionsTextColor);
		setBorder(new LineBorder(Color.BLACK));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				super.mouseClicked(me);

				replaceWithSuggestedText();

				autoSuggestionsPopUpWindow.setVisible(false);
			}
		});

		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "Enter released");
		getActionMap().put("Enter released", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				replaceWithSuggestedText();
				autoSuggestionsPopUpWindow.setVisible(false);
			}
		});
	}

	public void setFocused(boolean focused) {
		if (focused) {
			setBorder(new LineBorder(suggestionBorderColor));
		} else {
			setBorder(new LineBorder(Color.BLACK));
		}
		repaint();
		this.focused = focused;
	}

	public boolean isFocused() {
		return focused;
	}

	private void replaceWithSuggestedText() {
		String suggestedWord = getText();
		String text = textField.getText();
		String typedWord = commandSuggestor.getCurrentlyTypedWord();
		String t = text.substring(0, text.lastIndexOf(typedWord));
		String tmp = t + text.substring(text.lastIndexOf(typedWord)).replace(typedWord, suggestedWord);
		textField.setText(tmp + " ");
	}
}
