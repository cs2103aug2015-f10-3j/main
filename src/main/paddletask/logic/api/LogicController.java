//@@author A0133662J
package main.paddletask.logic.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.paddletask.command.api.Command;
import main.paddletask.command.api.SearchTaskCommand;

import main.paddletask.common.exception.NoTaskStateException;

import main.paddletask.parser.api.CommandParser;

import main.paddletask.task.entity.Task;
import main.paddletask.task.entity.TaskComparator;

public class LogicController extends Observable {

    /*** Variable ***/
    private static final Logger LOGGER = Logger.getLogger(LogicController.class.getName());
    
    private static final String LOG_MSG_INFO_INIT_LOGIC_CONTROLLER = "Intialising LogicController";
    private static final String LOG_MSG_SEVERE_NO_TASK_STATE = "LogicController:parseCommand(): Attempt to read state when there are no tasks";
    
    private static final String ERROR_MSG_NO_TASK_STATE = "Please perform a view to retrieve a list of task so that you can perform this command!";
    
    private static LogicController _thisInstance;
    private static CommandParser _parserInstance;
    private static TaskComparator _taskComparatorInstance;
    private static ArrayList<Task> _deliveredTaskState;
    private static Observer _observer;

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
                LOGGER.log(Level.SEVERE, LOG_MSG_SEVERE_NO_TASK_STATE);
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
        
        try {
            executionResult = executeCommand(command, userInput);
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
}