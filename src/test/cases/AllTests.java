//@@author A0126332R
package test.cases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestEditTaskCommand.class, FullSystemFlowTest.class,
        TestCommandParser.class,
        TestHelpCommand.class, TestStorageController.class,
        TestTaskController.class, TestViewCommand.class,
        TestViewCommand.class })
public class AllTests {

}
