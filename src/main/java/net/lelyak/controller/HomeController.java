package net.lelyak.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lelyak.model.LocationStats;
import net.lelyak.services.CoronaVirusDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Nazar Lelyak.
 */
@Slf4j
@Controller
@AllArgsConstructor
public class HomeController {
    private CoronaVirusDataService virusDataService;

    @GetMapping(value = {"/", "/home", "/home.html"})
    public String homePage(Model model) {
        // sort all cases by new cases
        List<LocationStats> allStats = virusDataService.getAllStats().stream()
                .sorted(Comparator.comparing(LocationStats::getDiffFromPrevDay).reversed())
                .collect(Collectors.toList());

        int totalReportedCases = allStats.stream()
                .mapToInt(LocationStats::getLatestTotalCases)
                .sum();
        int totalNewCases = allStats.stream()
                .mapToInt(LocationStats::getDiffFromPrevDay)
                .sum();

        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        model.addAttribute("locationStats", allStats);
        model.addAttribute("dateTime", virusDataService.getUpdatedDateTime());

        return "home";
    }

    /**
     * Handling errors.
     */
    @GetMapping("/error")
    public String error() {
        log.error("Error occurred");
        return "error";
    }
}
