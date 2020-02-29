package net.lelyak.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import net.lelyak.controller.HomeController;
import net.lelyak.model.LocationStats;
import net.lelyak.services.CoronaVirusDataService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Nazar Lelyak.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
public class HomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private WebClient webClient;

    @MockBean
    private CoronaVirusDataService virusService;

    private List<LocationStats> stats = List.of(LocationStats.builder()
                    .state("Fujiyama")
                    .country("Japan")
                    .latestTotalCases(10)
                    .diffFromPrevDay(0)
                    .build(),
            LocationStats.builder()
                    .state("Hanoi")
                    .country("Vietnam")
                    .latestTotalCases(40)
                    .diffFromPrevDay(5)
                    .build()
    );

    @Before
    public void setUp() {
        when(virusService.getAllStats()).thenReturn(stats);
        webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc)
                .useMockMvcForHosts("virus-tracker.com", "coronavirus.org")
                .build();
    }

    @Test
    public void requestForVirusDataIsSuccessfullyProcessed() throws Exception {
        this.mockMvc.perform(get("/")
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(allOf(
                        containsString("Coronavirus Tracker Application"),
                        containsString("This application lists the current number of cases reported across the globe"),
                        containsString("Total cases reported as of today"),
                        containsString("New cases reported since previous day")))
                );
    }

    @Test
    public void homePageIsRenderedAsHtmlWithListOfStates() throws IOException {
        HtmlPage page = webClient.getPage("http://virus-tracker.com/home.html");
        List<String> statesList = page.getElementsByTagName("tr").stream()
                .map(DomNode::asText)
                .collect(toList());
        // check rows with appropriate data
        assertThat(statesList, hasItems("Hanoi\tVietnam\t40\t5", "Fujiyama\tJapan\t10\t0"));
    }
}
