//@@author A0125473H
package test.cases;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import main.paddletask.ui.view.MainFrame;

public class TestClearCommand {

	private ByteArrayOutputStream _outContent = new ByteArrayOutputStream();
	
	@Test
	public void testClearScreenCommand() {
		MainFrame mf = new MainFrame();
		//mf.cliMode();
		System.setOut(new PrintStream(_outContent));
		System.setIn(new ByteArrayInputStream("clear\r\nexit".getBytes()));
		assertEquals("", _outContent.toString());
	}

	@Test
	public void testClearShortcutScreenCommand() {
		MainFrame mf = new MainFrame();
		//mf.cliMode();
		System.setOut(new PrintStream(_outContent));
		System.setIn(new ByteArrayInputStream("/cl".getBytes()));
		assertEquals("", _outContent.toString());
	}
}
