package net.lelyak.services;

import com.google.common.collect.Lists;
import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.lelyak.model.LocationStats;
import net.lelyak.utils.TimeClock;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.List;

import static java.net.http.HttpResponse.BodyHandlers;

/**
 * @author Nazar Lelyak.
 */
@Slf4j
@Service
public class CoronaVirusDataService {

    private final HttpClient client;
    private final HttpRequest request;

    @Getter
    private List<LocationStats> allStats;
    @Getter
    private ZonedDateTime fetchDateTime;


    public CoronaVirusDataService(@Value("${virus.data.url}") String url) {
        log.info("DATA_URL: {}", url);

        allStats = Lists.newArrayList();
        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
    }

    @SneakyThrows
    @PostConstruct
    @Scheduled(cron = "0 0 * * * *") // every hour
//    @Scheduled(cron = "0 0 */2 * * *") // every 2 hour
//    @Scheduled(cron = "0/5 * * * * *") // every hour
    public void fetchVirusData() {
        log.debug("Fetch is called");
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        @Cleanup
        BufferedReader csvBodyReader = new BufferedReader(new StringReader(response.body()));

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        List<LocationStats> newStats = Lists.newArrayList();
        for (CSVRecord record : records) {
            // get the value from last column
            int localCases = Integer.parseInt(getColumnData(record, 1));
            int prevDayCases = Integer.parseInt(getColumnData(record, 2));

            var locationStat = LocationStats.builder()
                    .state(record.get("Province/State"))
                    .country(record.get("Country/Region"))
                    .latestTotalCases(localCases)
                    .diffFromPrevDay(localCases - prevDayCases)
                    .build();

            newStats.add(locationStat);
        }
        // save all results
        this.allStats = newStats;
        this.fetchDateTime = TimeClock.getCurrentDateTime();

        log.info("PARSED_TIME: {}", TimeClock.getCurrentDateTime());
//        log.debug("PARSED_STAT: {}", newStats);
    }

    /**
     * Get the data from row record starting from the end.
     * @param record row of the data.
     * @param columnIndex index of column which should be returned; starting from the end.
     * @return string value from a record row.
     */
    private String getColumnData(CSVRecord record, int columnIndex) {
        int columnsCounter = record.size();
        return record.get(columnsCounter - columnIndex).isBlank() ? "0" : record.get(columnsCounter - columnIndex);
    }
}
