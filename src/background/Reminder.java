package background;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import task.entity.Task;
import task.entity.TimedTask;

public class Reminder extends Observable {
    /*** Variables ***/
    private static Reminder thisInstance;
    private static Observer observer;
    private static Thread thread;
    private static Runnable r;
    
    /*** Constructor ***/
    private Reminder() {
        addObserver(observer);
        createRunnable();
        thread = new Thread(r);
        thread.start();
        System.out.println("Reminder Thread exited...\n");
    }
    
    public static Reminder getInstance(Observer reminderObserver) {
        if (thisInstance == null) {
            observer = reminderObserver;
            thisInstance = new Reminder();
        }
        return thisInstance;
    }
    
    private void createRunnable() {
        r = new Runnable() {
            public void run() {
                while (true) {
                    // Retrieve task
                    ArrayList<Task> taskList = Task.getTaskList();
                    
                    // Get task with reminders in the next minute
                    ArrayList<Task> dueTaskList = new ArrayList<Task>();
                    LocalDateTime reminder;
                    LocalDateTime now = LocalDateTime.now();
                    Duration duration;
                    for (Task task : taskList) {
                        // Select tasks that are not completed only
                        if (!task.isComplete()) {
                            switch (task.getType()) {
                                case TIMED:
                                    reminder = ((TimedTask) task).getReminder();
                                    duration = Duration.between(reminder, now);
                                    if ((reminder.isAfter(now)) && (duration.getSeconds() < 61)) {
                                        dueTaskList.add(task);
                                    }
                                    break;
                                case DEADLINE:
                                    reminder = ((TimedTask) task).getReminder();
                                    duration = Duration.between(reminder, now);
                                    if ((reminder.isAfter(now)) && (duration.getSeconds() < 61)) {
                                        dueTaskList.add(task);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    
                    // Notify observers
                    if (dueTaskList.size() > 0) {
                        setChanged();
                        notifyObservers(dueTaskList);
                    }
                    
                    // Sleep for 1 minute
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
            }
        };
    }
}
