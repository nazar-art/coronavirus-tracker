package net.lelyak.utils;

import lombok.experimental.UtilityClass;
import net.lelyak.model.LocationStats;

import java.util.List;

/**
 * @author Nazar Lelyak.
 */
@UtilityClass
public class TestDataRepository {

    public String TEST_DATA_URL = "https://raw.githubusercontent.com/nazar-art/coronavirus-tracker/master/src/test/resources/data/test_data.csv";

    public List<LocationStats> TEST_STATS = List.of(LocationStats.builder()
                    .state("Taiwan")
                    .country("Taiwan")
                    .latestTotalCases(45)
                    .diffFromPrevDay(5)
                    .build(),
            LocationStats.builder()
                    .state("Toronto")
                    .country("Canada")
                    .latestTotalCases(15)
                    .diffFromPrevDay(5)
                    .build()
    );
}
