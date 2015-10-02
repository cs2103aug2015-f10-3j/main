package input;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Option {
	
	private HashMap<String, Object> a;
	
	public boolean getBooleanOption(){
		return false;
	}
	
	public LocalDateTime getDateOption() {
		return null;
	}
	
	
	public String getStringOption() {
		return "";
	}
	
	public int getIntegerOption() {
		return -1;
	}
	
	public void addOption() {
		
	}
}
