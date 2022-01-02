package pruefung;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;


public class Pruefung implements JSONCodec{
   // Klassenattribute
    private String modulNummer;
    private String modulBezeichnung;
    private int tag;
    private int monat;
    private int jahr;
    private String ort;

    // Error messages
    private static final String MODUL_NUMMER_ERROR = "Die Modulnummer darf nicht leer oder null sein." ;
    private static final String MODUL_BEZEICHNUNG_ERROR = "Die Modulbezeichnung darf nicht leer oder null sein." ;
    private static final String TAG_ERROR_MESSAGE = "Der Tag muss zwischen 1 und 31 sein.";
    private static final String MONATE_ERROR_MESSAGE = "Der gegebene Monatsnummer muss zwischen 1 und 12 liegen!";
    private static final String MODUL_ORT_MESSAGE = "Der gegebene Monatsnummer muss zwischen 1 und 12 liegen!";
    private static final String JAHR_ERROR_MESSAGE = "Das Jahr muss zwischen 2022 und 2024 liegen!";


    private MessageFormat messageFormat = new MessageFormat(
            "'{'\"modulNummer\": {0}," +
                    "\"modulBezeichnung\": {1}," +
                    "\"tag\": {2}," +
                    "\"monat\": {3}," +
                    "\"jahr\": {4}," +
                    "\"ort\": {5}" +
                    "'}'"
    );

    
    public Pruefung(String modulNummer, String modulBezeichnung,
                    int tag, int monat, int jahr, String ort) {
        pruefen (!modulNummer.trim().isEmpty() && !modulNummer.trim().isBlank() && modulNummer.trim() != "" && modulNummer.trim() != null, MODUL_NUMMER_ERROR);
        pruefen (!modulBezeichnung.trim().isEmpty() && !modulBezeichnung.trim().isBlank() && modulBezeichnung.trim() != "" && modulBezeichnung.trim() != null, MODUL_BEZEICHNUNG_ERROR);             
        pruefen(tag >= 0 && tag <= 31, TAG_ERROR_MESSAGE);
        pruefen(monat >= 1 && monat <= 12, MONATE_ERROR_MESSAGE);
        pruefen(jahr > 2021 && jahr < 2025 , JAHR_ERROR_MESSAGE);
        pruefen(!ort.trim().isEmpty() && !ort.trim().isBlank() && ort.trim() != "" && ort.trim() != null, MODUL_ORT_MESSAGE);
        
        this.modulNummer = modulNummer;
        this.modulBezeichnung = modulBezeichnung;
        this.tag = tag;
        this.monat = monat;
        this.jahr = jahr;
        this.ort = ort;
    }

    

    public Pruefung() {
    }

    @Override
    public String toJson(){

        try {
            Object[] testArgs = {modulNummer, modulBezeichnung, Integer.toString(tag), Integer.toString(monat), Integer.toString(jahr), ort };
            String jsonString = messageFormat.format(testArgs);
            return jsonString;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void fromJson(String json) throws  JSONCodecException{
        try {
            if(json == null || json.length() == 0){
                throw  new JSONCodecException("Keine Pruefung wurde gegeben!");
            }
            // TODO Alle eventuelle Fehler fangen und JSONCodecException werfen
            Object[] object = messageFormat.parse(json);
            this.modulNummer = (String)object[0];
            this.modulBezeichnung = (String)object[1];
            this.tag = Integer.parseInt((String)object[2]);
            this.monat = Integer.parseInt((String)object[3]);
            this.jahr = Integer.parseInt((String)object[4]);
            this.ort = (String)object[5];

            // Attributte ueberpruefen
            pruefen (!modulNummer.trim().isEmpty() && !modulNummer.trim().isBlank() && modulNummer.trim() != "" && modulNummer.trim() != null, MODUL_NUMMER_ERROR);
            pruefen (!modulBezeichnung.trim().isEmpty() && !modulBezeichnung.trim().isBlank() && modulBezeichnung.trim() != "" && modulBezeichnung.trim() != null, MODUL_BEZEICHNUNG_ERROR);             
            pruefen(tag >= 0 && tag <= 31, TAG_ERROR_MESSAGE);
            pruefen(monat >= 1 && monat <= 12, MONATE_ERROR_MESSAGE);
            pruefen(jahr > 2021 && jahr < 2025 , JAHR_ERROR_MESSAGE);
            pruefen(!ort.trim().isEmpty() && !ort.trim().isBlank() && ort.trim() != "" && ort.trim() != null, MODUL_ORT_MESSAGE);
        
        } catch (ParseException e ) {
            throw new JSONCodecException("ParseException: Falsches StringFormat!");
        } catch (IllegalArgumentException e) {
            throw new JSONCodecException("IllegalArgumentException: " + e.getMessage());
        }
    }

    private void pruefen(Boolean bedingung, String errorMessage) {
        if (!bedingung) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    // getters
    public String getModulBezeichnung() {
        return modulBezeichnung;
    }

    public String getOrt() {
        return ort;
    }

    public String getModulNummer() {
        return modulNummer;
    }

    public void setModulBezeichnung(String modulBezeichnung) {
        this.modulBezeichnung = modulBezeichnung;
    }
}

