package com.octo.vanillapull.httpcoreserver.nio.dao;

import com.octo.vanillapull.model.Instrument;

import java.util.HashMap;

/**
 * Created by Bastien on 09/04/2014.
 */
public class InstrumentDAO {

    private final HashMap<String, Instrument> _instrumentMap = new HashMap<String, Instrument>();

    public InstrumentDAO () {
        pushInstrument("AC","Accor", 25.41, 1, 0.15);
        pushInstrument("AI", "Air Liquide", 97.92, 1, 0.10);
        pushInstrument("ALO","Alstom", 28.72, 1, 0.73);
        pushInstrument("MT","ArcelorMittal", 9.79, 1, -0.17);
        pushInstrument("CS","AXA", 14.78, 1, 0.19);
        pushInstrument("BNP","BNP Paribas", 45.04, 1, 0.89);
        pushInstrument("EN","Bouygues", 21.30, 1, -0.31);
        pushInstrument("CAP","Capgemini", 39.15, 1, 0.03);
        pushInstrument("CA","Carrefour", 23.33, 1, 0.14);
        pushInstrument("ACA","Credit Agricole", 6.92, 1, 0.13);
        pushInstrument("EAD","EADS", 42.44, 1, 1.24);
        pushInstrument("EDF","EDF", 18.16, 1, 0.04);
        pushInstrument("EI","Essilor", 88.73, 1, 0.57);
        pushInstrument("FTE","France Telecom", 8.32, 1, -0.01);
        pushInstrument("GTO","Gemalto", 60, 1, 1.25);
        pushInstrument("GSZ","GDF Suez", 16.58, 1, -0.16);
        pushInstrument("BN","Groupe Danone", 58.95, 1, 0.13);
        pushInstrument("OR","L'Oreal", 136, 1, 1.85);
        pushInstrument("LG","Lafarge", 53.33, 1, 0.74);
        pushInstrument("LR","Legrand", 37.68, 1, 0.17);
        pushInstrument("MC","LVMH", 135.75, 1, 2.6);
        pushInstrument("ML","Michelin", 67.46, 1, 0.36);
        pushInstrument("RI","Pernod Ricard", 94.46, 1, 0.18);
        pushInstrument("PP","PPR", 173.35, 1, -0.35);
        pushInstrument("PUB","Publicis", 55.22, 1, -0.52);
        pushInstrument("RNO","Renault", 56, 1, 2.21);
        pushInstrument("SAF","Safran", 39.5, 1, 0.29);
        pushInstrument("SGO","Saint-Gobain", 32.6, 1, 0.41);
        pushInstrument("SAN","Sanofi", 84.75, 1, 0.05);
        pushInstrument("SU","Schneider Electric", 58.9, 1, 0.22);
        pushInstrument("GLE","Societe Generale", 30, 1, 0.15);
        pushInstrument("SOLB","Solvay", 115, 1, -2.15);
        pushInstrument("STM","STMicroelectronics", 7.05, 1, -0.01);
        pushInstrument("TEC","Technip", 85.46, 1, 0.76);
        pushInstrument("FP","Total", 38.97, 1, 0.17);
        pushInstrument("UL","Unibail-Rodamco", 201.8, 1, 4.15);
        pushInstrument("VK","Vallourec", 41.24, 1, -0.22);
        pushInstrument("VIE","Veolia Environnement", 10.4, 1, -0.06);
        pushInstrument("DG","Vinci", 37.71, 1, 0.16);
        pushInstrument("VIV","Vivendi", 15.98, 1, 0.23);
    }

    public Instrument getInstrument(String symbol) {
        return _instrumentMap.get(symbol);
    }

    private void pushInstrument (String symbol, String label, double spot, double volatility, double variation) {
        Instrument instrument = new Instrument();
        instrument.setSymbol(symbol);
        instrument.setLabel(label);
        instrument.setSpot(spot);
        instrument.setVolatility(volatility);
        instrument.setVariation(variation);
        _instrumentMap.put(symbol, instrument);
    }
}
