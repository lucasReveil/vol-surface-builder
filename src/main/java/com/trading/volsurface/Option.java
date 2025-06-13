package com.trading.volsurface;

public class Option {
    private final double strike;
    private final double maturity;
    private double price;
    private double underlying; //underlying price
    private double rate; //risk free rate
    private final boolean isCall;
    
    public Option(double strike, double maturity, double price, double underlying, double rate, boolean isCall) {
        this.strike = strike;
        this.maturity = maturity;
        this.price = price;
        this.underlying = underlying;
        this.rate = rate;
        this.isCall = isCall;
    }
    public double getStrike() { return strike; }
    public double getMaturity() { return maturity; }
    public double getPrice() { return price; }
    public double getUnderlying() { return underlying; }
    public double getRate() { return rate; }
    public boolean isCall() { return isCall; }

    public double moneyness() {
        return underlying / strike;
    }
}
