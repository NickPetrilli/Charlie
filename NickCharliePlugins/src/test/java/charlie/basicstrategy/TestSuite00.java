package charlie.basicstrategy;

import charlie.basicstrategy.invalid.BlackjackHandTest;
import charlie.basicstrategy.invalid.CharlieHandTest;
import charlie.basicstrategy.invalid.EmptyHandTest;
import charlie.basicstrategy.invalid.GreaterThan10UpCardTest;
import charlie.basicstrategy.invalid.GreaterThan21HandTest;
import charlie.basicstrategy.invalid.NegativeValueHandTest;
import charlie.basicstrategy.invalid.NegativeValueUpCardTest;
import charlie.basicstrategy.invalid.NullHandTest;
import charlie.basicstrategy.invalid.NullUpCardTest;
import charlie.basicstrategy.invalid.OneCardHandTest;
import charlie.basicstrategy.section1.Test00_12_2;
import charlie.basicstrategy.section1.Test01_14_6;
import charlie.basicstrategy.section1.Test02_15_7;
import charlie.basicstrategy.section1.Test03_16_A;
import charlie.basicstrategy.section1.Test04_20_2;
import charlie.basicstrategy.section2.Test05_5_2;
import charlie.basicstrategy.section2.Test06_9_2;
import charlie.basicstrategy.section2.Test07_9_6;
import charlie.basicstrategy.section2.Test08_10_9;
import charlie.basicstrategy.section2.Test09_11_A;
import charlie.basicstrategy.section3.Test10_A2_2;
import charlie.basicstrategy.section3.Test11_A4_6;
import charlie.basicstrategy.section3.Test12_A6_7;
import charlie.basicstrategy.section3.Test13_A7_2;
import charlie.basicstrategy.section3.Test14_A7_7;
import charlie.basicstrategy.section3.Test15_A7_A;
import charlie.basicstrategy.section3.Test16_A8_6;
import charlie.basicstrategy.section4.Test17_22_7;
import charlie.basicstrategy.section4.Test18_33_8;
import charlie.basicstrategy.section4.Test19_44_4;
import charlie.basicstrategy.section4.Test20_55_9;
import charlie.basicstrategy.section4.Test21_99_7;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for all the test cases testing the Basic Strategy implementation
 * @author Nick Petrilli
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({Test00_12_2.class, Test01_14_6.class, Test02_15_7.class,
                     Test03_16_A.class, Test04_20_2.class, Test05_5_2.class,
                     Test06_9_2.class, Test07_9_6.class, Test08_10_9.class,
                     Test09_11_A.class, Test10_A2_2.class, Test11_A4_6.class,
                     Test12_A6_7.class, Test13_A7_2.class, Test14_A7_7.class,
                     Test15_A7_A.class, Test16_A8_6.class, Test17_22_7.class,
                     Test18_33_8.class, Test19_44_4.class, Test20_55_9.class,
                     Test21_99_7.class, NullUpCardTest.class, NullHandTest.class,
                     OneCardHandTest.class, EmptyHandTest.class, CharlieHandTest.class,
                     BlackjackHandTest.class, GreaterThan21HandTest.class, 
                     GreaterThan10UpCardTest.class, NegativeValueHandTest.class,
                     NegativeValueUpCardTest.class})
public class TestSuite00 {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
