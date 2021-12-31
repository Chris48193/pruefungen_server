package pruefung;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pruefung.JSONCodec.JSONCodecException;

public class PruefungsListe {
	
    private List<Pruefung> pruefungen = new ArrayList<Pruefung>();

    public PruefungsListe() {
    	try {
    		
	        File file = new File("./src/pruefung/pruefungen.txt");
	        // file.createNewFile();
	        
	        BufferedReader r = new BufferedReader(new FileReader(file));
	        String scan;
	        while((scan=r.readLine())!=null) {
	        	Pruefung pruefung = new Pruefung();
	        	pruefung.fromJson(scan);
	        	pruefungen.add(pruefung);
	        }
    		r.close();
    	} catch(IOException|JSONCodecException e) {
    		e.printStackTrace();
    	} finally {
    	}
    }
    
    public void pruefungenAusgeben() {
    	for(Pruefung pruefung : this.pruefungen) {
    		System.out.println(pruefung.toJson());
    	}
    }

    @Route(methode = "PUT")
	public String pruefungHinzufuegen(String json) {
		try {
			Pruefung pruefung = new Pruefung();
			pruefung.fromJson(json);
			pruefungen.add(pruefung);
			pruefungenSpeichern();
			return pruefung.toJson();
		} catch(JSONCodecException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Route(methode = "DELETE")
	public String pruefungLoeschen(String modulNummer) {
		int index = getPruefungIndex(modulNummer);
		//System.out.println("Index: " + index + " ModulNr: " + modulNummer);
		if (index != -1) {
			Pruefung pruef = pruefungen.get(index);
			pruefungen.remove(index);
			pruefungenSpeichern();
			return pruef.toJson();
		} 
		return "Keine Pruefung mit Modulnummer " + modulNummer + "gefunden!";
	}
	
	public int getPruefungIndex(String modulNummer) {
		int i = 0;
		for(Pruefung pruefung : pruefungen) {
			// System.out.println("getModulNr: " + pruefung.getModulNummer() + " ModulNr: " + modulNummer + " index " + i);
			if(pruefung.getModulNummer().equals(modulNummer)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	@Route(methode = "UPDATE")
	public String pruefungAktualisieren(String json) {
		try {
			Pruefung pruefung = new Pruefung();
			pruefung.fromJson(json);
			int index = getPruefungIndex(pruefung.getModulNummer());
			if (index != -1) {
				pruefungen.set(index, pruefung);
				pruefungenSpeichern();
				return pruefung.toJson();
			}
		} catch(JSONCodecException e) {
			e.printStackTrace();
		}
		return "Die Pruefung mit der gegebenen Modulnummer existiert nicht im Datenbank!";
	}

	@Route(methode = "GET")
	public String pruefungAusgeben(String key) {
		System.out.println("The key is: " + key);
		int index = getPruefungIndex(key);
		if (index != -1) {
			return pruefungen.get(index).toJson();
		}
		return "Pruefung nicht gefunden";
	}
	
	public void pruefungenSpeichern() {
		try {
			File file = new File("./src/pruefung/pruefungen.txt");
			FileWriter fr = new FileWriter(file);
			for(Pruefung pruefung : pruefungen) {
				fr.write(pruefung.toJson() + "\n");
			}
			fr.close();
		} catch(IOException e) {
    		e.printStackTrace();
    	}
		
	}

	public void allePruefungenAusgeben() {

	}
    
    public static void main(String[] args) {
    	PruefungsListe pl = new PruefungsListe();
    	pl.pruefungenAusgeben();
    	pl.pruefungHinzufuegen("{\"modulNummer\": 9,\"modulBezeichnung\": Physik,\"tag\": 3,\"monat\": 5,\"jahr\": 2012,\"ort\": Bafoussam}");
    	pl.pruefungenAusgeben();
    	pl.pruefungLoeschen("6");
    	pl.pruefungenAusgeben();
    	pl.pruefungAktualisieren("{\"modulNummer\": 9,\"modulBezeichnung\": Mechanik,\"tag\": 3,\"monat\": 5,\"jahr\": 2012,\"ort\": Yaounde}");
    	System.out.println(pl.pruefungAusgeben("2789"));
    }  
}

