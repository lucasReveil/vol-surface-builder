package com.trading.volsurface;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to read options from a CSV file.
 */
public class CsvReader {

    public static List<Option> readOptionsFromCsv(String path) {
        List<Option> options = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            String[] line;
            boolean skipHeader = true;

            while ((line = reader.readNext()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                double strike = Double.parseDouble(line[0]);
                double maturity = Double.parseDouble(line[1]);
                double price = Double.parseDouble(line[2]);
                double underlying = Double.parseDouble(line[3]);
                double rate = Double.parseDouble(line[4]);
                boolean isCall = Boolean.parseBoolean(line[5]);

                Option option = new Option(strike, maturity, price, underlying, rate, isCall);
                options.add(option);
            }

        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }

        return options;
    }
}
