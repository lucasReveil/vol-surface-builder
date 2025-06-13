package com.trading.volsurface;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class Main 
{
    public static void main( String[] args )
    {
        String filePath ="data/options.csv";
        List<Option> options = CsvReader.readOptionsFromCsv(filePath);
        Map<Double, Map<Double, Double>> rawSurface = VolatilitySurfaceBuilder.buildSurface(options);
        VolatilitySurface surface = new VolatilitySurface(rawSurface);

        VolatilitySurfaceExporter.exportToCSV(surface, "output/vol_surface.csv");
        VolatilitySurfaceAnalyzer.analyzeSmile(surface, 0.5);
        VolatilitySurfaceAnalyzer.analyzeSmile(surface, 1.0);
        Set<StrikeMaturityPoint> grid2D = GridGenerator.generateGrid(90.0,110.0,5.0,0.3,1.2,0.1);
        
        GreeksSurfaceBuilder builder =  new GreeksSurfaceBuilder(options, 100,0.1);
        
        GreeksSurface greeksSurface = new GreeksSurface(builder.buildSurface(grid2D));
        GreeksSurfaceExporter.exportToCSV(greeksSurface, "output/greeks_surface.csv");
        List<Option> book = CsvReader.readOptionsFromCsv("data/mock_options_for_pnl.csv");
        double dS=1.0;
        double dSigma=-0.1;
        double pnl = PnLCalculator.totalPnL(book, dS, dSigma);
        System.out.printf("Explained PnL: %.4f\n", pnl);

        InterpolatedIVGridExporter.exportInterpolatedSurface(surface, 90.0,110.0,1.0,0.3,1.2,0.05,"output/iv_grid.csv");
    }
}
