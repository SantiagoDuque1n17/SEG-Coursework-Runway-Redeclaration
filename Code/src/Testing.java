import Exceptions.DontNeedRedeclarationException;
import Exceptions.NegativeParameterException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class Testing extends TestCase
{
    PhysicalRunway runway;
    Runway runway1;
    Runway runway2;
    int testLDA1;
    int testLDA2;
    int testTORA1;
    int testTORA2;
    int testTODA1;
    int testTODA2;
    int testASDA1;
    int testASDA2;

    @Before
    public void setUp()
    {
        runway1 = new Runway("09L",3595, 3902, 3902, 3902, 306);
        runway2 = new Runway("27R",3884, 3884, 3962, 3884, 0);
        runway = new PhysicalRunway(runway1, runway2);
    }

    @Test
    public void testLandingOverObstacle()
    {
        testLDA1 = 2335;
        try {
            runway1.landingOverObstacle(-50, 25);
        }
        catch(NegativeParameterException e)
        {
            e.printStackTrace();
        }
        assertEquals("Calculations not correct", testLDA1, runway1.getLDA());

        testLDA2 = 2074;
        try {
            runway2.landingOverObstacle(500, 25);
        }
        catch(NegativeParameterException e)
        {
            e.printStackTrace();
        }
        assertEquals("Calculations are not correct", testLDA2, runway2.getLDA());
    }

    @Test
    public void testTakeOffAwayObstacle() throws NegativeParameterException
    {
        runway1.takeOffAwayObstacle(-50);
        testTORA1 = 3346;
        assertEquals("Calculations not correct", testTORA1, runway1.getTORA());
        testTODA1 = testTORA1 + runway1.getClearway();
        assertEquals("Calculations not correct", testTODA1, runway1.getTODA());
        testASDA1 = testTORA1 + runway1.getStopway();
        assertEquals("Calculations not correct", testASDA1, runway1.getASDA());

        runway2.takeOffAwayObstacle(50);
        testTORA2 = 3534;
        assertEquals("Calculations not correct", testTORA2, runway2.getTORA());
        testTODA2 = testTORA2 + runway2.getClearway();
        assertEquals("Calculations not correct", testTODA2, runway2.getTODA());
        testASDA2 = testTORA2 + runway2.getStopway();
        assertEquals("Calculations not correct", testASDA2, runway2.getASDA());
    }

    @Test
    public void testLandingTowardsObstacle() throws DontNeedRedeclarationException
    {
        testLDA1 = 3246;
        runway1.landingTowardsObstacle(3546);
        assertEquals("Calculations not correct", testLDA1, runway1.getLDA());

        testLDA2 = 3346;
        runway2.landingTowardsObstacle(3646);
        assertEquals("Calculations not correct", testLDA2, runway2.getLDA());
    }

    @Test
    public void testTakeOffTowardsObstacle() throws NegativeParameterException
    {
        runway1.takeOffTowardsObstacle(2500, 25);
        testTORA1 = 1496;
        assertEquals("Calculations not correct", testTORA1, runway1.getTORA());
        testTODA1 = testTORA1;
        assertEquals("Calculations not correct", testTODA1, runway1.getTODA());
        testASDA1 = testTORA1;
        assertEquals("Calculations not correct", testASDA1, runway1.getASDA());

        runway2.takeOffTowardsObstacle(2500, 25);
        testTORA2 = 1190;
        assertEquals("Calculations not correct", testTORA2, runway2.getTORA());
        testTODA2 = testTORA2;
        assertEquals("Calculations not correct", testTODA2, runway2.getTODA());
        testASDA2 = testTORA2;
        assertEquals("Calculations not correct", testASDA2, runway2.getASDA());
    }
}
