package com.trading.volsurface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class VolatilitySurface {
    private final Map<Double, Map<Double, Double>> surface;

    public VolatilitySurface(Map<Double, Map<Double, Double>> surface) {
        this.surface = surface;
    }

    public Double getVol(double strike, double maturity) {
        return surface.getOrDefault(strike, Collections.emptyMap()).get(maturity);
    }

    public Map<Double, Double> getSmile(double maturity) {
        Map<Double, Double> smile = new TreeMap<>();
        for (Map.Entry<Double, Map<Double, Double>> entry : surface.entrySet()) {
            Double strike = entry.getKey();
            Double vol = entry.getValue().get(maturity);
            if (vol != null) {
                smile.put(strike, vol);
            }
        }
        return smile;
    }

    public Double interpolate(double strike, double maturity) {
        if (surface.containsKey(strike) && surface.get(strike).containsKey(maturity)) {
            return surface.get(strike).get(maturity); 
        }
        NavigableMap<Double, Map<Double, Double>> strikeMap = new TreeMap<>(surface);
        Double k1 = strikeMap.floorKey(strike);
        Double k2 = strikeMap.ceilingKey(strike);
        if (k1 == null || k2 == null || !surface.containsKey(k1) || !surface.containsKey(k2)) {
            return null;
        }

        NavigableMap<Double, Double> matMapK1 = new TreeMap<>(surface.get(k1));
        NavigableMap<Double, Double> matMapK2 = new TreeMap<>(surface.get(k2));
        Double t1 = matMapK1.floorKey(maturity);
        Double t2 = matMapK1.ceilingKey(maturity);
        if (t1 == null || t2 == null || !matMapK1.containsKey(t1) || !matMapK2.containsKey(t1)
            || !matMapK1.containsKey(t2) || !matMapK2.containsKey(t2)) {
            return null;
        }

        double iv11 = surface.get(k1).get(t1);
        double iv21 = surface.get(k2).get(t1);
        double iv12 = surface.get(k1).get(t2);
        double iv22 = surface.get(k2).get(t2);

        //bilinear interpolation
        double denom = (k2 - k1) * (t2 - t1);
        if (denom == 0) return null;

        double interpolated = 1.0 / denom * (
                iv11 * (k2 - strike) * (t2 - maturity) +
                iv21 * (strike - k1) * (t2 - maturity) +
                iv12 * (k2 - strike) * (maturity - t1) +
                iv22 * (strike - k1) * (maturity - t1)
        );

        return interpolated;
    }
    public List<IVPoint> toList() {
        List<IVPoint> points = new ArrayList<>();
        for (Map.Entry<Double, Map<Double, Double>> strikeEntry : surface.entrySet()) {
            double strike = strikeEntry.getKey();
            for (Map.Entry<Double, Double> matEntry : strikeEntry.getValue().entrySet()) {
                double maturity = matEntry.getKey();
                double vol = matEntry.getValue();
                points.add(new IVPoint(strike, maturity, vol));
            }
        }
        return points;
    }


    public Map<Double, Map<Double, Double>> getRawSurface() {
        return surface;
    }
}
