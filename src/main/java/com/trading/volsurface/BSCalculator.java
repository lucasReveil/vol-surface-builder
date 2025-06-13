package com.trading.volsurface;
import static java.lang.Math.*;
public class BSCalculator {
    private BSCalculator(){

    }
    public static double erf(double x) {
        // Approximation from Abramowitz and Stegun, formula 7.1.26
        double t = 1.0 / (1.0 + 0.5 * abs(x));
        double tau = t * exp(
            -x * x
            - 1.26551223
            + 1.00002368 * t
            + 0.37409196 * pow(t, 2)
            + 0.09678418 * pow(t, 3)
            - 0.18628806 * pow(t, 4)
            + 0.27886807 * pow(t, 5)
            - 1.13520398 * pow(t, 6)
            + 1.48851587 * pow(t, 7)
            - 0.82215223 * pow(t, 8)
            + 0.17087277 * pow(t, 9)
        );

        return x >= 0 ? 1.0 - tau : tau - 1.0;
    }
    public static double normalCdf(double x) {
        return 0.5 * (1.0 + erf(x / sqrt(2.0)));
    }
    public static double d1(Option opt, double sigma){
        double S = opt.getUnderlying();
        double K = opt.getStrike();
        double r = opt.getRate();
        double T = opt.getMaturity();

        return (log(S / K) + (r + 0.5 * pow(sigma, 2)) * T) / (sigma * sqrt(T));
    }

    public static double blackScholesPrice(Option opt, double sigma) {
        double d1 = d1(opt,sigma);
        double d2=d1-sigma*sqrt(opt.getMaturity());
        if(opt.isCall()){
            return (opt.getUnderlying()*normalCdf(d1))-(opt.getStrike()*exp(-opt.getRate()*opt.getMaturity())*normalCdf(d2));
        }
        else{
           return ((opt.getStrike()*exp(-opt.getRate()*opt.getMaturity())*normalCdf(-d2)- opt.getUnderlying()*normalCdf(-d1))); 
        }
    }

    public static double impliedVolatility(Option opt) {
        // Newton-Raphson
        double sigma =0.2; //starting guess
        double epsilon = 1e-6;
        int maxIter=100;
        for(int i=0;i<maxIter;i++){
            double price = blackScholesPrice(opt, sigma);
            double vega=vega(opt,sigma);
            double diff = price-opt.getPrice();
            if(abs(diff)<epsilon){
                return sigma;
            }
            if(vega<1e-8){
                break;
            }
            sigma-=diff/vega;
        }
        return -1;
    }

    public static double delta(Option opt, double sigma) {
        double d1 = d1(opt, sigma);
        if(opt.isCall())
            return normalCdf(d1);
        else
            return normalCdf(d1)-1;
    }
    /**
     * Vega: sensitivity of option price to 1.00 change in volatility (i.e. 100%).
     * To interpret effect of +1% vol, multiply result by 0.01.
     */   
     public static double vega(Option opt, double sigma) {
        double S= opt.getUnderlying();
        double T= opt.getMaturity();
        double d1 = d1(opt, sigma);
        return S*sqrt(T)*(1/sqrt(2*PI))* exp(-0.5*d1*d1);
    }

    /**
     * Gamma: second derivative of option price w.r.t underlying.
     */
    public static double gamma(Option opt, double sigma) {
        double S = opt.getUnderlying();
        double T = opt.getMaturity();
        double d1 = d1(opt, sigma);

        return exp(-0.5 * d1 * d1) / (S * sigma * sqrt(2 * PI * T));
    }
    /**
     * Theta: sensitivity of option price to time (per year).
     * Divide by 365.0 to get daily theta.
     */
    public static double theta(Option opt, double sigma) {
        double S = opt.getUnderlying();
        double K = opt.getStrike();
        double r = opt.getRate();
        double T = opt.getMaturity();

        double d1 = d1(opt, sigma);
        double d2 = d1 - sigma * sqrt(T);

        double firstTerm = - (S * sigma * exp(-0.5 * d1 * d1)) / (2 * sqrt(2 * PI * T));
        double secondTerm = r * K * exp(-r * T);

        if (opt.isCall()) {
            return firstTerm - secondTerm * normalCdf(d2);
        } else {
            return firstTerm + secondTerm * normalCdf(-d2);
        }
    }
    public static void printGreeks(Option opt, double sigma) {
        double priceModel = blackScholesPrice(opt, sigma);
        double delta = delta(opt, sigma);
        double vega = vega(opt, sigma);
        double gamma = gamma(opt, sigma);
        double theta = theta(opt, sigma);
        double thetaDaily = theta / 365.0;

        System.out.printf("=== Option: Strike=%.2f | Maturity=%.2f | Call=%b ===%n",
                opt.getStrike(), opt.getMaturity(), opt.isCall());
        System.out.printf("Market Price   : %.4f%n", opt.getPrice());
        System.out.printf("Model Price    : %.4f%n", priceModel);
        System.out.printf("Implied Vol    : %.4f%n", sigma);
        System.out.printf("Delta          : %.4f%n", delta);
        System.out.printf("Vega (per 1%%)  : %.4f%n", vega * 0.01);
        System.out.printf("Gamma          : %.4f%n", gamma);
        System.out.printf("Theta (daily)  : %.4f%n", thetaDaily);
        System.out.println();
    }



}
