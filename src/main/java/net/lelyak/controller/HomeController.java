package net.lelyak.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lelyak.model.LocationStats;
import net.lelyak.services.CoronaVirusDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

/**
 * @author Nazar Lelyak.
 */
@Slf4j
@Controller
@AllArgsConstructor
public class HomeController {

    private final CoronaVirusDataService virusDataService;

    @GetMapping(value = {"/", "/home", "/home.html"})
    public String homePage(Model model) {
        // sort all cases by new cases
        List<LocationStats> allStats = virusDataService.getAllStats().stream()
                .sorted(comparing(LocationStats::getDiffFromPrevDay)
                        .thenComparing(LocationStats::getLatestTotalCases)
                        .reversed())
                .collect(toList());

        int totalReportedCases = allStats.stream()
                .mapToInt(LocationStats::getLatestTotalCases)
                .sum();
        int totalNewCases = allStats.stream()
                .mapToInt(LocationStats::getDiffFromPrevDay)
                .sum();

        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        model.addAttribute("locationStats", allStats);
        model.addAttribute("dateTime", virusDataService.getFetchDateTime());

        return "home";
    }

    /**
     * Handling errors.
     */
    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
