package pruefung;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;


public class Pruefung implements JSONCodec{
   // Klassenattribute
    private String modulNummer;
    private String modulBezeichnung;
    private int tag;
    private int monat;
    private int jahr;
    private String ort;

    MessageFormat messageFormat = new MessageFormat(
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
        this.modulNummer = modulNummer;
        this.modulBezeichnung = modulBezeichnung;
        this.tag = tag;
        this.monat = monat;
        this.jahr = jahr;
        this.ort = ort;
    }

    public Pruefung() {
    	/*String test = "'{'\"modulNummer\": {0}, " +
                "\"modulBezeichnung\": {1}, " +
                "\"tag\": {2}, " +
                "\"monat\": {3}, " +
                "\"jahr\": {4}, " +
                "\"ort\": {5}'}'";
    	System.out.println(test);*/
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
        } catch (ParseException e) {
            throw new JSONCodecException("ParseException: Falsches StringFormat!");
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

    /*public static void main(String[] args) throws JSONCodec.JSONCodecException {
        Pruefung pruef = new Pruefung();
        pruef.toJson();
        String test = "{\"modulNummer\": 2,\"modulBezeichnung\": Htwsaar,\"tag\": 2,\"monat\": 3,\"jahr\": 5,\"ort\": htwsaar}" ;
        System.out.println(test);
        pruef.fromJson(test);
        System.out.println(pruef.jahr);
        System.out.println(pruef.modulBezeichnung);
        
    }*/
    // setters
}

