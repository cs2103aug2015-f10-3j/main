//@@author A0126332R
package test.cases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FullSystemFlowTest.class, TestClearCommand.class,
        TestCommandParser.class, TestEditTaskCommand.class,
        TestHelpCommand.class, TestStorageController.class,
        TestTaskController.class, TestViewCommand.class,
        ViewTaskCommandTest.class })
public class AllTests {

}
