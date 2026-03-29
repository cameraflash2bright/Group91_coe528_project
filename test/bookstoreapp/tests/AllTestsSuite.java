package bookstoreapp.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    BookStoreValidationTest.class,
    CustomerStateTransitionTest.class,
    FileLoadRobustnessTest.class
})
public class AllTestsSuite {
    // No code needed here
}