package test;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.CommandParser;

public class TestParser {

	@Test
	public void test() {
		CommandParser.tryParse("add 123 \"your-mom\" 532");
	}

}
