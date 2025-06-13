package com.trading.volsurface;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
public class AppTest 
{
    @Test
    public void testImpliedVolatilityIsPositive() {
        Option opt = new Option(100.0, 1.0, 10.0, 100.0, 0.01, true);
        double iv = BSCalculator.impliedVolatility(opt);
        assertTrue(iv > 0 && iv < 1, "IV should be between 0 and 1");
    }
    @Test
    public void testIVInversionAccuracy() {
        Option opt = new Option(100, 1.0, 7.0, 100, 0.01, true);
        double iv = BSCalculator.impliedVolatility(opt);
        double modelPrice = BSCalculator.blackScholesPrice(opt, iv);
        assertEquals(opt.getPrice(), modelPrice, 1e-4, "Model price should match market price with computed IV");
    }
    @Test
    public void testVolInterpolation() {
        Map<Double, Map<Double, Double>> raw = new HashMap<>();
        raw.put(95.0, Map.of(0.5, 0.15, 1.0, 0.16));
        raw.put(105.0, Map.of(0.5, 0.20, 1.0, 0.22));
        VolatilitySurface surf = new VolatilitySurface(raw);

        double iv = surf.interpolate(100.0, 0.75);
        assertTrue(iv > 0.15 && iv < 0.22, "Interpolated vol should be between min and max");
    }
    @Test
    public void testCallATMGreeks() {
        double sigma = 0.2;
        Option opt = new Option(100, 1e-6, 0.0, 100, 0, true);
        

        double delta = BSCalculator.delta(opt, sigma);
        double vega = BSCalculator.vega(opt, sigma);
        double gamma = BSCalculator.gamma(opt, sigma);
        assertTrue(delta > 0.45 && delta < 0.55, "ATM call delta should be around 0.5");
        assertTrue(vega > 0, "Vega should be positive");
        assertTrue(gamma > 0, "Gamma should be positive");
    }
}
