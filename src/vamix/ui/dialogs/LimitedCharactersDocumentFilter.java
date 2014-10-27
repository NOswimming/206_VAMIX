package vamix.ui.dialogs;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *  * A document filter for limited the number of character entered in a text area.
 * Used by the AddTextDialog GUI component
 * 
 * Taken from a Stack Overflow thread and modified. The thread can be found here:
 * http://stackoverflow.com/questions/8377103/limit-of-characters-in-jtextarea-row
 * 
 * @see #AddTextDialog
 */
public class LimitedCharactersDocumentFilter extends DocumentFilter {

	private final int maxLength;

	public LimitedCharactersDocumentFilter(int maxLength) {
		this.maxLength = maxLength;
	}

	public void insertString(DocumentFilter.FilterBypass fb, int offset,
			String str, AttributeSet attr) throws BadLocationException {
		if ((fb.getDocument().getLength() + str.length()) <= this.maxLength)
			super.insertString(fb, offset, str, attr);
		else
			Toolkit.getDefaultToolkit().beep();
	}

	public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
			String str, AttributeSet attrs) throws BadLocationException {
		if ((fb.getDocument().getLength() + str.length()) <= this.maxLength)
			super.replace(fb, offset, length, str, attrs);
		else
			Toolkit.getDefaultToolkit().beep();

	}
}
