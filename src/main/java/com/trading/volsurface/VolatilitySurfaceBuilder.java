package com.trading.volsurface;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class VolatilitySurfaceBuilder {
    public static Map<Double,Map<Double,Double>> buildSurface(List<Option> options){
        Map<Double,Map<Double,Double>> surface = new TreeMap<>();
        for (Option opt:options){
            double IV =BSCalculator.impliedVolatility(opt);
            surface.computeIfAbsent(opt.getStrike(),k->new TreeMap<>()).put(opt.getMaturity(), IV);
        }
        return surface;
    }

}
