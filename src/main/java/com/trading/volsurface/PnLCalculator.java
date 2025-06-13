package com.trading.volsurface;


import java.util.List;

public class PnLCalculator {
    public static double localPnL(Option opt,double dS,double dSigma){
        double sigma = BSCalculator.impliedVolatility(opt);
        double delta = BSCalculator.delta(opt, sigma);
        double gamma = BSCalculator.gamma(opt, sigma);
        double vega = BSCalculator.vega(opt, sigma);
        return delta * dS + gamma/2 * dS * dS + vega * dSigma;
    }
    public static double totalPnL(List<Option> options,double dS, double dSigma){
        double PnL = 0;
        for (Option opt : options){
            PnL += localPnL(opt, dS, dSigma);
        }
        return PnL;
    }  
    public static double realPnL(Option opt, double dS, double dSigma){
        double S0= opt.getUnderlying();
        double sigma0 = BSCalculator.impliedVolatility(opt);
        Option shocked = new Option(opt.getStrike(),opt.getMaturity(),0.0,S0+dS,opt.getRate(),opt.isCall());
        double price0= BSCalculator.blackScholesPrice(opt, sigma0);
        double price1 = BSCalculator.blackScholesPrice(shocked,sigma0+dSigma);
        return price1-price0;
    }
    public static double approxError(Option opt, double dS,double dSigma){
        return Math.abs(realPnL(opt, dS, dSigma) - localPnL(opt, dS, dSigma));
    }
}
