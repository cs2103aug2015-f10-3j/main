package test;

import static org.junit.Assert.*;

import org.junit.Test;

import logic.command.Command;
import parser.CommandParser;

public class TestParser {

	@Test
	public void test() {
		CommandParser cp = new CommandParser();
		Command a = cp.tryParse("add \"abc\"");
		assertNotEquals(null, a);
	}

}
