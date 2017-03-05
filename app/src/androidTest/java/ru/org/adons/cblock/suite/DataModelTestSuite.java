package ru.org.adons.cblock.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ru.org.adons.cblock.data.BlockListModelTest;
import ru.org.adons.cblock.data.CallLogModelTest;

/**
 * Suite to run all tests together
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CallLogModelTest.class,
        BlockListModelTest.class
})
public class DataModelTestSuite {
}
