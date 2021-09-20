package net.lelyak.services;

import net.lelyak.utils.TimeClock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static net.lelyak.utils.TestDataRepository.TEST_DATA_URL;
import static net.lelyak.utils.TestDataRepository.TEST_STATS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Nazar Lelyak.
 */
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class CoronaVirusDataServiceTest {

    private CoronaVirusDataService virusService;


    @Before
    public void init() {
        virusService = new CoronaVirusDataService(TEST_DATA_URL);
        virusService.fetchVirusData();
    }

    @Test
    public void locationStatsAreReturnedAfterExecutionAsExpected() {
        assertNotNull("virus service must not be null", virusService.getAllStats());

        assertTrue(virusService.getAllStats().size() > 0);

        assertEquals(TEST_STATS.size(), 2);
        assertEquals("local stats should be equal", TEST_STATS, virusService.getAllStats());
    }

    @Test
    public void dateTimeForLastUpdateIsReturnedAsExpected() {
        assertEquals("date time should be equal to mocked", TimeClock.getCurrentDateTime(), virusService.getFetchDateTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ifDataUrlIsInvalidExceptionIsThrown() {
        virusService = new CoronaVirusDataService("/incorrect-url.com");
        virusService.fetchVirusData();
    }
}