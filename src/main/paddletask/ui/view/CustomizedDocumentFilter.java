//@@author A0125528E
package main.paddletask.ui.view;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public final class CustomizedDocumentFilter extends DocumentFilter {
	
	/*** Variables ***/
	private JTextPane textPane = null;
	private StyledDocument styledDocument = null;
	private final static StyleContext styleContext = StyleContext.getDefaultStyleContext();
	private static final AttributeSet redAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.RED);
	//private static final AttributeSet orangeAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.ORANGE);
	private static final AttributeSet blackAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
	private static final String REGEX_TAG = "#\\w{1,}";
	private static final String REGEX_START_BOUND = "\\b";
	private static final String REGEX_END_BOUND = "\\b|";
	private static final int REMOVE_TRAILING = 1;
	private static final String CARRIAGE_RETURN = "\r\n";
	private static final String NEXT_LINE = "\n";
	private static final String KEYWORD_DEADLINE = "deadline";

	/*** Constructors ***/
	public CustomizedDocumentFilter(JTextPane textPane){
		this.textPane = textPane;
		styledDocument = textPane.getStyledDocument();
	}

	/*** Methods ***/
	@Override
	public void insertString(FilterBypass fb, int offset, String text, AttributeSet attributeSet) throws BadLocationException {
		super.insertString(fb, offset, text, attributeSet);
		handleTextChanged();
	}

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
		super.remove(fb, offset, length);
		handleTextChanged();
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attributeSet) throws BadLocationException {
		super.replace(fb, offset, length, text, attributeSet);
		handleTextChanged();
	}

	/**
	 * This method will handle the text changed on the document.
	 * It will be invoke later and will run as a separate thread.
	 * 
	 */
	private void handleTextChanged(){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateTextStyles();
			}
		});
	}

	/**
	 * This method will build the regex from the given
	 * string output.
	 * 
	 * @param keywords
	 * 				String array of keywords
	 */
	private Pattern buildPattern(String[] keywords){
		StringBuilder sb = new StringBuilder();
		
		for (String token : keywords) {
			sb.append(REGEX_START_BOUND); 
			sb.append(token);
			sb.append(REGEX_END_BOUND); 
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - REMOVE_TRAILING);
		}
		Pattern p = Pattern.compile(sb.toString());
		return p;
	}
	
	/**
	 * This method will change the attribute set to orange and bold,
	 * and return the changed set.
	 * 
	 * @return orangeAttributeSet
	 * 				edited attribute set to orange
	 */
	public static AttributeSet changeToOrange(){
		AttributeSet orangeAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.ORANGE);
		orangeAttributeSet = styleContext.addAttribute(orangeAttributeSet, StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
		return orangeAttributeSet;
	}
	
	/**
	 * This method will change the attribute set to red and bold,
	 * and return the changed set.
	 * 
	 * @return redAttributeSet
	 * 				edited attribute set to red
	 */
	public static AttributeSet changeToRed(){
		AttributeSet redAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.RED);
		redAttributeSet = styleContext.addAttribute(redAttributeSet, StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
		return redAttributeSet;
	}

	/**
	 * This method will change the attribute set to bold,
	 * and return the changed set.
	 * 
	 * @return attributes
	 * 				SimpleAttributeSet set to bold.
	 */
	public static SimpleAttributeSet setBold(){
		SimpleAttributeSet attributes = new SimpleAttributeSet(); 
		attributes.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
		return attributes;
	}
	
	/**
	 * This method updates the text styles by 
	 * calling changeFontForTags() method.
	 * 
	 */
	private void updateTextStyles(){
		changeFontForTags();
	}
	
	/**
	 * This method prepares an attribute set for blue color.
	 * It will compile a regex for tag to build and find 
	 * all the tags in the document.
	 * This method will then change the tags to blue color and
	 * bold.
	 * 
	 */
	private void changeFontForTags(){
		AttributeSet blueAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
		blueAttributeSet = styleContext.addAttribute(blueAttributeSet, StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
		Pattern pattern = Pattern.compile(REGEX_TAG);
		String text = textPane.getText().replaceAll(CARRIAGE_RETURN ,NEXT_LINE);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			// Change the color of recognized tokens
			int start = matcher.start();
			int end = matcher.end();
			styledDocument.setCharacterAttributes(start, end - start, blueAttributeSet, false);
		}
	}
	
	/**
	 * This method will compile a regex for the keywords to build and find 
	 * all the keywords in the document.
	 * This method will then change the keywords to red color.
	 * 
	 */
	private void changeFontOfLineByKeywords(){
		String[] keywords = {KEYWORD_DEADLINE};
		Pattern pattern = buildPattern(keywords);
		String text = textPane.getText().replaceAll(CARRIAGE_RETURN,NEXT_LINE);

		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			String beforeText = text.substring(0, start);
			int startOfLine = beforeText.lastIndexOf(NEXT_LINE);
			String afterText = text.substring(++startOfLine, text.length());
			int endOfLine = afterText.indexOf(NEXT_LINE);
			styledDocument.setCharacterAttributes(startOfLine, endOfLine, redAttributeSet, false);
		}
	}
}
