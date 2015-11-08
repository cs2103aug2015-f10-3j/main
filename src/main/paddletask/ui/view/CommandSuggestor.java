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

	/*** Variables ***/
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
	private static final int NORMAL_WORD = -1;
	private static final String EMPTY_STRING = "";
	private static final char CHAR_SLASH = '/';
	private static final char CHAR_DASH = '-';
	private static int currentIndex = 0;
	private static boolean isActivated = false;

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

	/*** Constructors ***/
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

	/*** Methods ***/
	/**
	 * This method will recalculate the new position of the MainFrame and
	 * will fit Suggestion pop up window to the new location of MainFrame.
	 * 
	 */
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

	/**
	 * This method will use textField and add bindings of Keys to request for 
	 * auto suggestion pop up window.
	 * F2 will request focus into the popup window.
	 * Down Key will traverse downwards through the popup window.
	 * Up Key will traverse upwards through the popup window.
	 * Upon reaching end points, it will continue upwards or downwards upon respective direction.
	 * 
	 */
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

	/**
	 * This method will set the focus back to the textField.
	 * 
	 */
	private void setFocusToTextField() {
		container.toFront();
		container.requestFocusInWindow();
		textField.requestFocusInWindow();
	}

	/**
	 * This method will get the added suggestion labels and
	 * return an arraylist of them.
	 * 
	 * @return suggestionLabelList
	 * 				ArrayList of SuggestionLabel added
	 */
	public ArrayList<SuggestionLabel> getAddedSuggestionLabels() {
		ArrayList<SuggestionLabel> suggestionLabelList = new ArrayList<>();
		for (int i = 0; i < suggestionsPanel.getComponentCount(); i++) {
			if (suggestionsPanel.getComponent(i) instanceof SuggestionLabel) {
				SuggestionLabel suggestionLabel = (SuggestionLabel) suggestionsPanel.getComponent(i);
				suggestionLabelList.add(suggestionLabel);
			}
		}
		return suggestionLabelList;
	}

	/**
	 * This method will be executed upon instances when required to check for
	 * suggestions available or unavailable.
	 * This method will trigger the popup window or hide the popup window respectively.
	 */
	public void checkForAndShowSuggestions() {
		typedWord = getCurrentlyTypedWord();
		int wordCategory = checkWordCategory(typedWord);

		suggestionsPanel.removeAll();

		suggestionWindowWidth = 0;
		suggestionWindowHeight = 0;

		boolean isAdded = wordTyped(typedWord, wordCategory);

		if (!isAdded) {
			if (autoSuggestionPopUpWindow.isVisible()) {
				autoSuggestionPopUpWindow.setVisible(false);
			}
		} else {
			if (isActivated) {
				showPopUpWindow();
				setFocusToTextField();
			} else {
				autoSuggestionPopUpWindow.setVisible(false);
			}
		}
	}
	
	/**
	 * This method will add the given word to the suggestion label.
	 * It will call for calculation of the popup window size and add the
	 * new label into the suggestionPanel.
	 * 
	 * @param	word
	 * 				String to be added as suggestion
	 */
	protected void addWordToSuggestions(String word) {
		SuggestionLabel suggestionLabel = new SuggestionLabel(word, suggestionFocusedColor, suggestionsTextColor, this);

		calculatePopUpWindowSize(suggestionLabel);

		suggestionsPanel.add(suggestionLabel);
	}

	/**
	 * This method will extract the word that is currently
	 * being typed in the textField. It will lookout for spaces
	 * and handle accordingly.
	 * 
	 * @return wordBeingTyped
	 * 				String of the word currently typed in textField
	 */
	public String getCurrentlyTypedWord() {//get newest word after last white spaceif any or the first word if no white spaces
		String text = textField.getText();
		String wordBeingTyped = EMPTY_STRING;
		if (text.contains(SPACE)) {
			int locationOfSpace = text.lastIndexOf(SPACE);
			if (locationOfSpace >= currentIndexOfSpace) {
				currentIndexOfSpace = locationOfSpace;
				wordBeingTyped = text.substring(text.lastIndexOf(SPACE));
			}
		} else {
			wordBeingTyped = text;
		}
		return wordBeingTyped.trim();
	}

	/**
	 * This method will use the given string and check if it is a command word
	 * or options word or a normal word.
	 * 
	 * @param typedWord
	 * 				String that is currently typed
	 * 
	 * @return integer category of the word
	 * 	
	 */
	public int checkWordCategory(String typedWord){
		String text = textField.getText();
		int currentPosition = textField.getCaretPosition();
		int firstSpacePosition = text.indexOf(SPACE);
		if (firstSpacePosition > currentPosition || firstSpacePosition == NORMAL_WORD) {
			return COMMAND_WORD;
		} else if (firstSpacePosition == currentPosition) {
			return NORMAL_WORD;
		} else {
			return OPTIONS_WORD;
		}
	}

	/**
	 * This method will use the given label to calculate
	 * the required size of width and height for the popup window
	 * to accommodate the new number of label.
	 * 
	 * @param label
	 * 				JLabel to be calculated
	 * 	
	 */
	private void calculatePopUpWindowSize(JLabel label) {
		//so we can size the JWindow correctly
		if (suggestionWindowWidth < label.getPreferredSize().width) {
			suggestionWindowWidth = label.getPreferredSize().width;
		}
		suggestionWindowHeight += label.getPreferredSize().height;
	}

	/**
	 * This method will be executed when the popup window
	 * are to be displayed. The method will check for the location
	 * and set its location accordingly to maintain its jointed form with the
	 * input field. This method will also check if the autoSuggestionPopUpWindow
	 * is out of screen and not visible, if so, it will set the window to span upwards
	 * rather than the usual downwards direction.
	 * 	
	 */
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

	/**
	 * This method will return the autoSuggestion PopUp window.
	 * 
	 * @return autoSuggestionPopUpWindow
	 * 				JWindow of the popup window
	 * 	
	 */
	public JWindow getAutoSuggestionPopUpWindow() {
		return autoSuggestionPopUpWindow;
	}

	/**
	 * This method will return the window of the container.
	 * 
	 * @return container
	 * 				Window of the container
	 * 	
	 */
	public Window getContainer() {
		return container;
	}

	/**
	 * This method will return the instance of textField of 
	 * the MainPanel.
	 * 
	 * @return textField
	 * 				JTextField in MainPanel
	 * 	
	 */
	public JTextField getTextField() {
		return textField;
	}

	/**
	 * This method will use the given typedWord and category of the word
	 * to determine what dictionary of suggestions to be used. It will the
	 * traverse the arraylist and add the word to suggestion. It will also
	 * traverse and match the word, if match, it will be added to the suggestion
	 * panel to be displayed.
	 * 
	 * @param typedWord
	 * 				String of the word currently typed
	 * 		  wordCategory
	 * 				integer category of the word
	 * 
	 * @return boolean
	 * 				true: when the word typed has suggestion
	 * 				false: when the word typed has no suggestion
	 * 	
	 */
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
		
		boolean isSuggestionAdded = false;

		for (String word : dictionary) {
			boolean isFullyMatched = true;
			for (int i = 0; i < typedWord.length(); i++) {
				if (i < word.length()) {
					if (!typedWord.toLowerCase().startsWith(String.valueOf(word.toLowerCase().charAt(i)), i)) {//check for match
						isFullyMatched = false;
						break;
					}
				}
			}
			if (isFullyMatched) {
				addWordToSuggestions(word);
				isSuggestionAdded = true;
			}
		}
		return isSuggestionAdded;
	}

	/**
	 * This method will take the current caret location and obtain the current word
	 * typed, followed by the word before. It will then check if the previous word is an
	 * option. If it is, it will return true, else, false.
	 * 
	 * @param dictionary
	 * 				ArrayList of suggestions
	 * 
	 * @return boolean
	 * 				true: when previous word is an option
	 * 				false: when the previous word is not an option
	 * 	
	 */
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

	/**
	 * This method will obtain the content from the textField and
	 * extract the first word before a space to be returned.
	 * 
	 * @return String
	 * 				the first word of the textField
	 * 	
	 */
	public String getFirstWordFromTextField(){
		String[] textContents = textField.getText().trim().split(SPACE);
		return textContents[0];
	}


	/**
	 * This method will create the necessary optionsMap to contain the mapping
	 * of commands, and commands to options in order for the suggestions to
	 * be able to take the arraylist of specific commands/options from.
	 * 
	 * 	
	 */
	public void createCommandOptionMap(){
		optionsMap.put("", createCommandsList());
		optionsMap.put(ParserConstants.COMMANDS.ADD.toString(), addOptionList());
		optionsMap.put(ParserConstants.COMMANDS.EDIT.toString(), editOptionList());
		optionsMap.put(ParserConstants.COMMANDS.VIEW.toString(), viewOptionList());
		optionsMap.put(ParserConstants.COMMANDS.TAG.toString(), tagOptionList());
		optionsMap.put(ParserConstants.COMMANDS.UNTAG.toString(), tagOptionList());
	}

	/**
	 * This method will create the arraylist of commands from the Common component, ParserConstants.
	 * This will retrieve the commands value from there and return the arraylist of compiled commands.
	 * 
	 * @return words
	 * 				ArrayList of String retrieved
	 * 	
	 */
	public ArrayList<String> createCommandsList(){
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<ParserConstants.COMMANDS> commandList = new ArrayList<ParserConstants.COMMANDS>
		(Arrays.asList(ParserConstants.COMMANDS.values()));
		for (ParserConstants.COMMANDS command : commandList) {
			words.add(command.toString());
		}
		return words;
	}

	/**
	 * This method will create the arraylist of options by add command from the Common component, ParserConstants.
	 * This will retrieve the option value from there and return the arraylist of compiled options.
	 * 
	 * @return words
	 * 				ArrayList of String retrieved
	 * 	
	 */
	public ArrayList<String> addOptionList(){
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<ParserConstants.ADD_OPTIONS> commandList = new ArrayList<ParserConstants.ADD_OPTIONS>
		(Arrays.asList(ParserConstants.ADD_OPTIONS.values()));
		for (ParserConstants.ADD_OPTIONS command : commandList) {
			words.add(command.toString());
		}
		return words;
	}

	/**
	 * This method will create the arraylist of options by edit command from the Common component, ParserConstants.
	 * This will retrieve the option value from there and return the arraylist of compiled options.
	 * 
	 * @return words
	 * 				ArrayList of String retrieved
	 * 	
	 */
	public ArrayList<String> editOptionList(){
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<ParserConstants.EDIT_OPTIONS> commandList = new ArrayList<ParserConstants.EDIT_OPTIONS>
		(Arrays.asList(ParserConstants.EDIT_OPTIONS.values()));
		for (ParserConstants.EDIT_OPTIONS command : commandList) {
			words.add(command.toString());
		}
		return words;
	}

	/**
	 * This method will create the arraylist of options by view command from the Common component, ParserConstants.
	 * This will retrieve the option value from there and return the arraylist of compiled options.
	 * 
	 * @return words
	 * 				ArrayList of String retrieved
	 * 	
	 */
	public ArrayList<String> viewOptionList(){
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<ParserConstants.VIEW_OPTIONS> commandList = new ArrayList<ParserConstants.VIEW_OPTIONS>
		(Arrays.asList(ParserConstants.VIEW_OPTIONS.values()));
		for (ParserConstants.VIEW_OPTIONS command : commandList) {
			words.add(command.toString());
		}
		return words;
	}

	/**
	 * This method will create the arraylist of options by tag command from the Common component, ParserConstants.
	 * This will retrieve the option value from there and return the arraylist of compiled options.
	 * 
	 * @return words
	 * 				ArrayList of String retrieved
	 * 	
	 */
	public ArrayList<String> tagOptionList(){
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<ParserConstants.TAGUNTAG_OPTIONS> commandList = new ArrayList<ParserConstants.TAGUNTAG_OPTIONS>(Arrays.asList(ParserConstants.TAGUNTAG_OPTIONS.values()));
		for (ParserConstants.TAGUNTAG_OPTIONS command : commandList) {
			words.add(command.toString());
		}
		return words;
	}

	/**
	 * This method will retrieve the point coordinate of the location from the lower left corner of 
	 * the autoSuggestionPopUpWindow. It will be calculated with respect to the current screen and return a boolean 
	 * value if it is within the screen boundary or not. 
	 * 
	 * @return boolean
	 * 				true: when the entire autoSuggestionPopUpWindow is within the screen
	 * 				false: when the entire autoSuggestionPopUpWindow is NOT within the screen
	 * 	
	 */
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
	
	/**
	 * This method will toggle the activation boolean of the popup window.
	 * If called when isActivated is true, it will be toggled false.
	 * If called when isActivated is false, it will be toggled true.
	 * Then it will update the suggestions accordingly by calling the check of suggestions.
	 * 
	 * 	
	 */
	public void toggleActivation(){
		if (isActivated) {
			isActivated = false;
		} else {
			isActivated = true;
		}
		checkForAndShowSuggestions();
	}
}

@SuppressWarnings("serial")
class SuggestionLabel extends JLabel {

	/*** Variables ***/
	private boolean isFocused = false;
	private final JWindow autoSuggestionsPopUpWindow;
	private final JTextField textField;
	private final CommandSuggestor commandSuggestor;
	private Color suggestionsTextColor, suggestionBorderColor;

	/*** Constructors ***/
	public SuggestionLabel(String string, final Color borderColor, Color suggestionsTextColor, CommandSuggestor commandSuggestor) {
		super(string);

		this.suggestionsTextColor = suggestionsTextColor;
		this.commandSuggestor = commandSuggestor;
		this.textField = commandSuggestor.getTextField();
		this.suggestionBorderColor = borderColor;
		this.autoSuggestionsPopUpWindow = commandSuggestor.getAutoSuggestionPopUpWindow();

		initComponent();
	}

	/*** Methods ***/
	/**
	 * This method will initialize the component of Suggestion label and prepare
	 * the mouse event on clicked for the replacement of the suggestion to the textField.
	 * 
	 */
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

	/**
	 * This method will set the border of the label based on the boolean
	 * isFocused. It will be set to the suggestionBorderColor if true, or else, 
	 * the default black if it is false.
	 * 
	 * @param isFocused
	 * 				boolean of the label if it is focused
	 */
	public void setFocused(boolean isFocused) {
		if (isFocused) {
			setBorder(new LineBorder(suggestionBorderColor));
		} else {
			setBorder(new LineBorder(Color.BLACK));
		}
		repaint();
		this.isFocused = isFocused;
	}

	/**
	 * This method will return the boolean value of the label isFocused.
	 * 
	 * @return isFocused
	 * 				boolean of the label if it is focused
	 */
	public boolean isFocused() {
		return isFocused;
	}

	/**
	 * This method will use the suggested Word and replace the textField with the label's
	 * suggested word.
	 */
	private void replaceWithSuggestedText() {
		String suggestedWord = getText();
		String text = textField.getText();
		String typedWord = commandSuggestor.getCurrentlyTypedWord();
		String t = text.substring(0, text.lastIndexOf(typedWord));
		String tmp = t + text.substring(text.lastIndexOf(typedWord)).replace(typedWord, suggestedWord);
		textField.setText(tmp + " ");
	}
}
