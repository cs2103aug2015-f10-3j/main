package ui.view;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public final class CustomizedDocumentFilter extends DocumentFilter {
	private JTextPane textPane = null;
	private StyledDocument styledDocument = null;
	private final StyleContext styleContext = StyleContext.getDefaultStyleContext();
	private final AttributeSet redAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.RED);
	private final AttributeSet orangeAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.ORANGE);
	private final AttributeSet blackAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
	private String[] keywords = {"deadline"};
	private Pattern pattern = null;
	// Use a regular expression to find the words you are looking for


	public CustomizedDocumentFilter(JTextPane textPane){
		this.textPane = textPane;
		styledDocument = textPane.getStyledDocument();
		pattern = buildPattern();
	}

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
	 * Runs your updates later, not during the event notification.
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
	 * Build the regular expression that looks for the whole word of each word that you wish to find.  The "\\b" is the beginning or end of a word boundary.  The "|" is a regex "or" operator.
	 * @return
	 */
	private Pattern buildPattern(){
		StringBuilder sb = new StringBuilder();
		
		for (String token : keywords) {
			sb.append("\\b"); // Start of word boundary
			sb.append(token);
			sb.append("\\b|"); // End of word boundary and an or for the next word
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1); // Remove the trailing "|"
		}

		Pattern p = Pattern.compile(sb.toString());

		return p;
	}


	private void updateTextStyles(){
		// Clear existing styles
		String text = textPane.getText().replaceAll("\r\n","\n");
		//styledDocument.setCharacterAttributes(0, text.length(), blackAttributeSet, true);

		// Look for tokens and highlight them
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			// Change the color of recognized tokens
			int start = matcher.start();
			int end = matcher.end();
			String beforeText = text.substring(0, start);
			int startOfLine = beforeText.lastIndexOf("\n");
			String afterText = text.substring(++startOfLine, text.length());
			int endOfLine = afterText.indexOf("\n");
			System.out.println(text);
			System.out.println("highlight length" + (endOfLine));
			styledDocument.setCharacterAttributes(startOfLine, endOfLine, redAttributeSet, false);
		}
	}
}
