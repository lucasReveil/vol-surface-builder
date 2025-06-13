package com.trading.volsurface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.lang.Math.abs;
public class VolatilitySurfaceAnalyzer {
    public static void analyzeSmile(VolatilitySurface surface, double maturity){
        Map<Double,Double> smile = surface.getSmile(maturity);
        if(smile.size()<3){
            System.out.println("Not enough points to analyze smile");
            return;
        }
        boolean isMonotonic=isVolMonotonic(smile);
        boolean hasBump = hasLocalAnomaly(smile);
        boolean hasSkew = hasSkew(smile);
        System.out.println("=== Smile Analysis at T=" + maturity + " ===");
        System.out.println("Vols: " + smile);
        System.out.println("Monotonic: " + isMonotonic);
        System.out.println("Skew detected: " + hasSkew);
        System.out.println("Bump detected: " + hasBump);
        System.out.println();
    }
    private static boolean isVolMonotonic(Map<Double,Double> smile){
        boolean increasing=true;
        boolean decreasing=true;
        double prev=-1;
        for(double v:smile.values()){
            if(prev!=-1){
                if(v<prev) increasing=false;
                if(v>prev) decreasing=false;
            }
            prev=v;
        }
        return increasing || decreasing;
    }
    private static boolean hasLocalAnomaly(Map<Double,Double> smile){
        List<Double> vols = new ArrayList<>(smile.values());
        for(int i=1;i<vols.size()-1;i++){
            double left=vols.get(i-1);
            double mid=vols.get(i);
            double right=vols.get(i+1);
            if((mid>left&&mid>right)||(mid<left&&mid<right)){
                return true;
            }
        }
        return false;
    }
    private static boolean hasSkew(Map<Double,Double> smile){
        double left=smile.entrySet().iterator().next().getValue();
        double right =-1;
        for (double v:smile.values()) right=v;
        return abs(right-left)>0.02;
        
    }
    
}
