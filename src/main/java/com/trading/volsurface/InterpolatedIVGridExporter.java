package com.trading.volsurface;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class InterpolatedIVGridExporter {
    public static void exportInterpolatedSurface(VolatilitySurface surface, double kMin,double kMax,double kStep, double tMin,double tMax, double tStep, String filename){
        try(FileWriter writer = new FileWriter(filename)){
            int count=0;
            for(double k= kMin; k<=kMax;k+=kStep){
                for (double t=tMin;t<=tMax;t+=tStep){
                    Double iv= surface.interpolate(k, t);
                    if(iv!=null && !Double.isNaN(iv)){
                        writer.write(String.format(Locale.US, "%.2f,%.2f,%.4f\n",k,t,iv));
                        count++;
                    }
                }
            }
            System.out.println("Interpolated IV surface exported to " + filename);
            System.out.println("Exported " + count + " IV points.");
        } catch(IOException e){
            System.err.println("Export failed: "+e.getMessage());
        }
    }
}
