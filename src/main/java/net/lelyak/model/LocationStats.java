package net.lelyak.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author Nazar Lelyak.
 */
@Data
@Builder
public class LocationStats {
    private String state;
    private String country;
    private int latestTotalCases;
    private int diffFromPrevDay;
}
