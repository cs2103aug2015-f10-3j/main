package task.entity;

import java.time.LocalDateTime;
import java.util.Comparator;

import task.entity.Task.TASK_TYPE;

public class TaskComparator implements Comparator<Task> {

	@Override
	public int compare(Task o1, Task o2) {
		if (o1.getPriority() != o2.getPriority()) {
			return o1.getPriority() - o2.getPriority();
		} else if (o1.getType()!= TASK_TYPE.FLOATING && o2.getType() != TASK_TYPE.FLOATING) {
			return getTaskEnd(o1).compareTo(getTaskEnd(o2));
		} else {
			return o1.getTaskId() - o2.getTaskId();
		}
	}

	private LocalDateTime getTaskEnd(Task task) {
		if (task.getType().equals(TASK_TYPE.DEADLINE)) {
			return castToDeadline(task).getEnd();
		} else if (task.getType().equals(TASK_TYPE.TIMED)) {
			return castToTimed(task).getEnd();
		} else {
			return null;
		}
	}
	
	private DeadlineTask castToDeadline(Task task) {
		return (DeadlineTask) task;
	}
	
	private TimedTask castToTimed(Task task) {
		return (TimedTask) task;
	}
}
