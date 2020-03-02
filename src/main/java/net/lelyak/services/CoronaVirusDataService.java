package net.lelyak.services;

import com.google.common.collect.Lists;
import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.lelyak.model.LocationStats;
import net.lelyak.utils.Clock;
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
import java.time.LocalDateTime;
import java.util.List;

import static java.net.http.HttpResponse.BodyHandlers;

/**
 * @author Nazar Lelyak.
 */
@Slf4j
@Service
public class CoronaVirusDataService {

    private HttpClient client;
    private HttpRequest request;

    @Getter
    private List<LocationStats> allStats;
    @Getter
    private LocalDateTime updatedDateTime;


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
    @Scheduled(cron = "* */30 * * * *")
    public void fetchVirusData() {
        log.debug("Fetch is called");
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        @Cleanup
        BufferedReader csvBodyReader = new BufferedReader(new StringReader(response.body()));

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        List<LocationStats> newStats = Lists.newArrayList();
        for (CSVRecord record : records) {
            // get the value from last column
            final int localCases = Integer.parseInt(record.get(record.size() - 1));
            final int prevDayCases = Integer.parseInt(record.get(record.size() - 2));

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
        this.updatedDateTime = Clock.getCurrentDateTime();

        log.info("PARSED_TIME: {}", Clock.getCurrentDateTime());
//        log.debug("PARSED_STAT: {}", newStats);
    }
}
