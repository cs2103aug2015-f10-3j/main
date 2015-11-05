package main.paddletask.background;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import main.paddletask.task.entity.DeadlineTask;
import main.paddletask.task.entity.Task;
import main.paddletask.task.entity.TimedTask;

public class Reminder extends Observable {
	/*** Variables ***/
	private static Reminder _thisInstance;
	private static Observer _observer;
	private static Thread _thread;
	private static Runnable _runnable;

	/*** Constructor ***/
	private Reminder() {
		addObserver(_observer);
		createRunnable();
		_thread = new Thread(_runnable);
		_thread.start();
		//System.out.println("Reminder Thread started...\n");
	}

	public static Reminder getInstance(Observer reminderObserver) {
		if (_thisInstance == null) {
			_observer = reminderObserver;
			_thisInstance = new Reminder();
		}
		return _thisInstance;
	}
	
	/*** Methods ***/
	/**
     * This method creates a runnable that
     * runs in the background
     * 
     */
	private void createRunnable() {
		_runnable = new Runnable() {
			public void run() {
				while (true) {
					// Retrieve task
					ArrayList<Task> tasks = Task.getTaskList();

					// Get task with reminders in the next minute
					ArrayList<Task> dueTasks = new ArrayList<Task>();
					LocalDateTime reminder;
					LocalDateTime now = LocalDateTime.now();
					Duration duration;
					if(tasks != null){
						for (Task task : tasks) {
							// Select tasks that are not completed only
							if (!task.isComplete()) {
								switch (task.getType()) {
								case TIMED:
									reminder = ((TimedTask) task).getReminder();
									duration = Duration.between(reminder, now);
									if ((reminder.isAfter(now)) && (Math.abs(duration.getSeconds()) < 61)) {
										dueTasks.add(task);
									}
									break;
								case DEADLINE:
									reminder = ((DeadlineTask) task).getReminder();
									duration = Duration.between(reminder, now);
									if ((reminder.isAfter(now)) && (Math.abs(duration.getSeconds()) < 61)) {
										dueTasks.add(task);
									}
									break;
								default:
									break;
								}
							}
						}
					}
					
					//System.out.println("dueTaskList.size(): " + dueTaskList.size());

					// Notify observers
					if (dueTasks.size() > 0) {
						setChanged();
						notifyObservers(dueTasks);
					}

					// Sleep for 1 minute
					try {
					    //System.out.println("Go to sleep");
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}
}
