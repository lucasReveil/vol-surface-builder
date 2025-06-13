package com.trading.volsurface;

public class GreekPoint {

    public final double strike;
    public final double maturity;
    public final double delta;
    public final double vega;
    public final double gamma;
    public final double theta;
    public GreekPoint(double strike,double maturity,double delta,double vega, double gamma, double theta){
        this.strike=strike;
        this.maturity=maturity;
        this.delta=delta;
        this.vega=vega;
        this.gamma=gamma;
        this.theta=theta;
    }
    @Override
    public String toString() {
        return String.format("Strike=%.2f | T=%.2f | Delta=%.4f | Vega = %.4f | Gamma = %.4f | Theta = %.4f", strike, maturity,delta,vega,gamma,theta);
    }
}
