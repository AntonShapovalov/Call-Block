package mobile.addons.cblock.suite

import org.junit.runner.RunWith
import org.junit.runners.Suite

import mobile.addons.cblock.data.BlockListModelTest
import mobile.addons.cblock.data.CallLogModelTest

/**
 * Suite to run all tests together
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(CallLogModelTest::class, BlockListModelTest::class)
class DataModelTestSuite
