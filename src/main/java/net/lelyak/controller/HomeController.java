package net.lelyak.controller;

import lombok.AllArgsConstructor;
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
@Controller
@AllArgsConstructor
public class HomeController {
    private CoronaVirusDataService virusDataService;

    @GetMapping("/")
    public String home(Model model) {
        // sort all cases by total cases
        List<LocationStats> allStats = virusDataService.getAllStats().stream()
                .sorted(Comparator.comparing(LocationStats::getLatestTotalCases).reversed())
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

        return "home";
    }
}
