package com.trading.volsurface;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class GreeksSurfaceExporter {
    public static void exportToCSV(GreeksSurface surface, String filename){
        try(FileWriter writer = new FileWriter(filename)){
            writer.write("Strike,Maturity,Delta,Vega,Gamma,Theta\n");
            for(GreekPoint point: surface.getRawSurface()){
                writer.write(String.format(Locale.US,"%.2f,%.2f,%.4f,%.4f,%.4f,%.4f\n",point.strike,point.maturity,point.delta,point.vega,point.gamma,point.theta));
            }
            System.out.println("Greeks surface exported to " + filename);
        } catch(IOException e){
            System.err.println("Error while exporting Greeks surface: "+e.getMessage());
        }
    }
}
