//@@author A0133662J
package main.paddletask.logic.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.paddletask.command.api.Command;
import main.paddletask.command.api.*;
import main.paddletask.common.exception.InvalidRedoException;
import main.paddletask.common.exception.InvalidUndoException;
import main.paddletask.common.exception.NoTaskStateException;

import main.paddletask.parser.api.CommandParser;

import main.paddletask.task.entity.Task;
import main.paddletask.task.entity.TaskComparator;

public class LogicController extends Observable {

    /*** Variable ***/
    private static final Logger LOGGER = LoggerFactory.getLogger(LogicController.class);
    
    private static final String LOG_MSG_INFO_INIT_LOGIC_CONTROLLER = "Intialising LogicController";
    private static final String LOG_MSG_SEVERE_NO_TASK_STATE = "LogicController:parseCommand(): Attempt to read state when there are no tasks";
    
    private static final String ERROR_MSG_NO_TASK_STATE = "Please perform a view to retrieve a list of task so that you can perform this command!";
    

	private static final String ADD_MSG_SUCCESS = "New task added successfully\n";
	private static final String COMPLETE_MSG_SUCCESS = "Selected task completed successfully\n";
	private static final String DELETE_MSG_SUCCESS = "Selected task deleted successfully\n";
	private static final String EDIT_MSG_SUCCESS = "Selected task edited successfully\n";
	private static final String MORE_MSG_SUCCESS = "Showing full details of selected task\n";
	private static final String REDO_MSG_SUCCESS = "Previous undo redone successfully\n";
	private static final String UNDO_MSG_SUCCESS = "Previous command undone successfully\n";
	private static final String SETDIR_MSG_SUCCESS = "Set directory successfully\n";
	private static final String TAG_MSG_SUCCESS = "Selected task tagged successfully\n";
	private static final String UNTAG_MSG_SUCCESS = "Selected task untagged successfully\n";
	private static final String EMPTY_MSG_SUCCESS = "";
    
    private static LogicController _thisInstance;
    private static CommandParser _parserInstance;
    private static TaskComparator _taskComparatorInstance;
    private static ArrayList<Task> _deliveredTaskState;
    private static Observer _observer;
    
    private String successMessage;

    /*** Constructor ***/
    public LogicController() {
        LOGGER.info(LOG_MSG_INFO_INIT_LOGIC_CONTROLLER);
        _parserInstance = new CommandParser();
        _deliveredTaskState = new ArrayList<Task>();
        _taskComparatorInstance = new TaskComparator();
    }

    /*** Methods ***/

    /**
     * Retrieves a Singleton instance of a LogicController
     * 
     * @param mainObserver
     *          The observer view passed from the caller
     * @return an instance of LogicController
     */
    public static LogicController getInstance(Observer mainObserver) {
        if (_thisInstance == null) {
            _observer = mainObserver;
            _thisInstance = new LogicController();
        }
        
        return _thisInstance;
    }
    
    // API for UI component to access LogicController
    public ArrayList<Task> processCommand(String userInput) {
        return handleCommand(userInput);
    }

    private Command parseCommand(String userInput) throws Exception {
        assert(userInput != null);
        Command command;
        // If userinput is a command that requires the state such as "edit <running index>"
        // or "delete <running index>", send both the user input and the state to the Parser
        if (_parserInstance.isStatefulCommand(userInput)) {
            if (_deliveredTaskState.isEmpty()) {
                LOGGER.error(LOG_MSG_SEVERE_NO_TASK_STATE);
                throw new NoTaskStateException(ERROR_MSG_NO_TASK_STATE);
            } else {
                command = _parserInstance.parse(userInput, getStateTaskId());
            }
        } else {
            // Otherwise, send only the user input to the Parser
            command = _parserInstance.parse(userInput);
        }

        return command;
    }

    private ArrayList<Task> executeCommand(Command command, String userInput) throws Exception {
        command.addObserver(_observer);
        ArrayList<Task> executionResult = new ArrayList<Task>();
        
        // If userinput is a Command that will update the state such as "view all"
        // or "search <sequence>", execute the Command and update the state with the result
        if (_parserInstance.isSaveStateCommand(userInput)) {
            executionResult = command.execute();
            // If the Command is a "Search", sort the results based on degree of match
            // details specified in TaskComparator implementation
            if (!isSearch(command)) {
                Collections.sort(executionResult, _taskComparatorInstance);
            }
            _deliveredTaskState = executionResult;
        } else {
            // Otherwise, execute all other Command without updating the state
            // with the result
            executionResult = command.execute();
        }
        
        return executionResult;
    }

    private ArrayList<Task> handleCommand(String userInput) {
        ArrayList<Task> executionResult;
        Command command;
        // Attempt to parse and execute the Command specified by user input
        try {
            command = parseCommand(userInput);
            
        } catch (Exception e) {
            // If an Exception is encounted during the Command parsing or
            // Command execution, update the observer with the Error messages
            setChanged();
            notifyObservers(e.getMessage());
            return null;
        }
        
        setSuccessMessage(command);
        
        try {
            executionResult = executeCommand(command, userInput);
        } catch (InvalidRedoException | InvalidUndoException e) { 
            setChanged();
            notifyObservers(e.getMessage());
            return null;
        } catch (Exception e) {
            if (!Command.getCommandList().isEmpty()) {
                Command.getCommandList().pop();
            }
            setChanged();
            notifyObservers(e.getMessage());
            return null;
        }
        return executionResult;
    }

    private void setSuccessMessage(Command command) {
    	if (command instanceof AddTaskCommand) {
    		successMessage = ADD_MSG_SUCCESS;
    	} else if (command instanceof CompleteTaskCommand) {
    		successMessage = COMPLETE_MSG_SUCCESS;
    	} else if (command instanceof DeleteTaskCommand) {
    		successMessage = DELETE_MSG_SUCCESS;
    	} else if (command instanceof EditTaskCommand) {
    		successMessage = EDIT_MSG_SUCCESS;
    	} else if (command instanceof MoreCommand) {
    		successMessage = MORE_MSG_SUCCESS;
    	} else if (command instanceof RedoCommand) {
    		successMessage = REDO_MSG_SUCCESS;
    	} else if (command instanceof UndoCommand) {
    		successMessage = UNDO_MSG_SUCCESS;
    	} else if (command instanceof SetDirectoryCommand) {
    		successMessage = SETDIR_MSG_SUCCESS;
    	} else if (command instanceof TagTaskCommand) {
    		successMessage = TAG_MSG_SUCCESS;
    	} else if (command instanceof UntagTaskCommand) {
    		successMessage = UNTAG_MSG_SUCCESS;
    	} else {
    		successMessage = EMPTY_MSG_SUCCESS;
    	}
	}

	// Pull out the task ID of all Task in the state so that it can be 
    // passed to the Parser for stateful command parsing
    private int[] getStateTaskId() {
        int numId = _deliveredTaskState.size();
        int[] stateIndexes = new int[numId];
        for (int i = 0; i < numId; i++) {
            stateIndexes[i] = _deliveredTaskState.get(i).getTaskId();
        }
        
        return stateIndexes;
    }

    private boolean isSearch(Command command) {
        if (command instanceof SearchTaskCommand) {
            return true;
        } else {
            return false;
        }
    }

	public String getSuccessfulMessage() {
		return successMessage;
	}
}