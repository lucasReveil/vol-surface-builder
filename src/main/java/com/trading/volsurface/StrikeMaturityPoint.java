package com.trading.volsurface;

import java.util.Objects;

public class StrikeMaturityPoint {
    public final double strike;
    public final double maturity;

    public StrikeMaturityPoint(double strike, double maturity) {
        this.strike = strike;
        this.maturity = maturity;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StrikeMaturityPoint)) return false;
        StrikeMaturityPoint p = (StrikeMaturityPoint) o;
        return Double.compare(strike, p.strike) == 0 &&
               Double.compare(maturity, p.maturity) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(strike, maturity);
    }
}
