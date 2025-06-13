package com.trading.volsurface;
public class IVPoint {
    public final double strike;
    public final double maturity;
    public final double vol;

    public IVPoint(double strike, double maturity, double vol) {
        this.strike = strike;
        this.maturity = maturity;
        this.vol = vol;
    }

    @Override
    public String toString() {
        return String.format("Strike=%.2f | T=%.2f | IV=%.4f", strike, maturity, vol);
    }
}
