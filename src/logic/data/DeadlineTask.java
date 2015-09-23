package logic.data;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DeadlineTask extends Task {
	private ArrayList<DeadlineTask> deadlineTaskList;
	private LocalDateTime start;
	private LocalDateTime end;
	
	public ArrayList<DeadlineTask> getDeadlineTaskList() {
		return deadlineTaskList;
	}
	public void setDeadlineTaskList(ArrayList<DeadlineTask> deadlineTaskList) {
		this.deadlineTaskList = deadlineTaskList;
	}
	public LocalDateTime getStart() {
		return start;
	}
	public void setStart(LocalDateTime start) {
		this.start = start;
	}
	public LocalDateTime getEnd() {
		return end;
	}
	public void setEnd(LocalDateTime end) {
		this.end = end;
	}
	
	
}
