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
            "'{'\n\t\"modulNummer\": {0}," +
                    "\n\t\"modulBezeichnung\": {1}," +
                    "\n\t\"tag\": {2}," +
                    "\n\t\"monat\": {3}," +
                    "\n\t\"jahr\": {4}," +
                    "\n\t\"ort\": {5}," +
                    "\n'}'"
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
    }

    @Route(methode = "create")
    public void pruefungAnlegen(String nachricht){
    	System.out.println("create: " + nachricht + " aufgerufen");

    }
    
    @Route(methode = "read")
    public void pruefungAnzeigen(String nachricht) {
		System.out.println("read: " + nachricht + " aufgerufen");
	}
    
    @Route(methode = "update")
	public void pruefungAktualisieren(String nachricht) {
		System.out.println("update: " + nachricht + " aufgerufen");
	}

	@Route(methode = "delete")
	public void pruefungLoeschen(String nachricht) {
		System.out.println("delete: " + nachricht + " aufgerufen");
	}


    @Override
    public String toJson(){

        try {
            Object[] testArgs = {modulNummer, modulBezeichnung, tag, monat, jahr, ort };
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

    // setters
}

