package com.trading.volsurface;

import java.util.HashSet;
import java.util.Set;

public class GridGenerator {
    public static Set<StrikeMaturityPoint> generateGrid(
        double strikeMin, double strikeMax, double strikeStep,
        double maturityMin, double maturityMax, double maturityStep
    ) {
        Set<StrikeMaturityPoint> grid = new HashSet<>();

        for (double K = strikeMin; K <= strikeMax + 1e-6; K += strikeStep) {
            for (double T = maturityMin; T <= maturityMax + 1e-6; T += maturityStep) {
                grid.add(new StrikeMaturityPoint(roundTo2(K), roundTo2(T)));
            }
        }

        return grid;
    }

    private static double roundTo2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
