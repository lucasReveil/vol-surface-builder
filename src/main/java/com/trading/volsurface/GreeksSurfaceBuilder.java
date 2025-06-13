package com.trading.volsurface;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GreeksSurfaceBuilder {
    private final VolatilitySurface volSurface;
    private double underlying;
    private double rate;
    public GreeksSurfaceBuilder(List<Option> options,double underlying,double rate){
        volSurface=new VolatilitySurface(VolatilitySurfaceBuilder.buildSurface(options));
        this.underlying=underlying;
        this.rate=rate;
    }
    public List<GreekPoint> buildSurface(Set<StrikeMaturityPoint> grid){
        List<GreekPoint> surface = new ArrayList<>();
        for( StrikeMaturityPoint point:grid){
            Double IV = volSurface.interpolate(point.strike, point.maturity);
            if (IV == null) {
                System.out.printf("Interpolation failed at K=%.2f, T=%.2f%n", point.strike, point.maturity);
                continue;
            }
            
            Option fake = new Option(point.strike,point.maturity,0.0,underlying,rate,true);
            double delta = BSCalculator.delta(fake, IV);
            double gamma = BSCalculator.gamma(fake,IV);
            double theta= BSCalculator.theta(fake, IV);
            double vega= BSCalculator.vega(fake, IV);
            surface.add(new GreekPoint(point.strike,point.maturity,delta,vega,gamma,theta));
        }
        return surface;
    }
    public void setUnderlying(double underlying){
        this.underlying=underlying;
    }
}
