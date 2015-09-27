package storage.data;

import java.util.ArrayList;
import java.time.LocalDateTime;

public class TimedTask extends Task {
	private ArrayList<TimedTask> timedTaskList;
	private LocalDateTime start;
	private boolean complete;
	
	public ArrayList<TimedTask> getTimedTaskList() {
		return timedTaskList;
	}
	public void setTimedTaskList(ArrayList<TimedTask> timedTaskList) {
		this.timedTaskList = timedTaskList;
	}
	public LocalDateTime getStart() {
		return start;
	}
	public void setStart(LocalDateTime start) {
		this.start = start;
	}
	public boolean isComplete() {
		return complete;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	
}
