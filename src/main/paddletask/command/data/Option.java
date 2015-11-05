//@@A0125473H
package main.paddletask.command.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Option {
	
	private static final String INVALID_STRING = "";
	private static final int INVALID_INT_VALUE = -1;
	private List<Object> values = new ArrayList<Object>();
	
	/*** Methods ***/
	public LocalDateTime getDateValue() {
		return getDateValue(0);
	}
	
	public String getStringValue() {
		return getStringValue(0);
	}
	
	public int getIntegerValue() {
		return getIntegerValue(0);
	}
	
	public LocalDateTime getDateValue(int position) {
		try {
			return (LocalDateTime)getValue(position);
		} catch (ClassCastException e) {
			return null;
		}
	}
	
	public String getStringValue(int position) {
		try {
			return (String)getValue(position);
		} catch (ClassCastException e) {
			return INVALID_STRING;
		}
	}
	
	public int getIntegerValue(int position) {
		try {
			return (Integer)getValue(position);
		} catch (ClassCastException e) {
			return INVALID_INT_VALUE;
		}
	}
	
	public Object getValue() {
		return getValue(0);
	}
	
	public Object getValue(int position) {
		if (!hasValues()) {
			return null;
		}
		try {
			return values.get(position);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	// this function throws exception because it is called during
	// the creation of a command. The exception will tell the 
	// parser that whatever it is currently doing is invalid
	public boolean addValues(Object[] values) throws Exception {
		boolean success = false;
		for (Object value : values) {
			success |= this.values.add(value);
		}
		return success;
	}
	
	public boolean addValue(Object value) throws Exception {
		return values.add(value);
	}
	
	public boolean hasValues() {
		return !values.isEmpty();
	}
	
	public int getValuesCount() {
		return values.size();
	}
}
