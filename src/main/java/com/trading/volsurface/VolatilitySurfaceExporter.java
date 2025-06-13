package com.trading.volsurface;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class VolatilitySurfaceExporter {
    public static void exportToCSV(VolatilitySurface surface,String filename){
        try(FileWriter writer = new FileWriter(filename)){
            List<IVPoint> points= surface.toList();
            writer.write("Strike,Maturity,IV\n");
            for (IVPoint point : points){
                writer.write(String.format(Locale.US,"%.2f,%.2f,%.4f\n",point.strike,point.maturity,point.vol));
            }
            System.out.println("Volatility surface exported to " + filename);

        } catch (IOException e){
            System.err.println("Error while exporting Volatility surface: "+e.getMessage());
        }
    }
}
