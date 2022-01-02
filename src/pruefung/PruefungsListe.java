package pruefung;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
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
			if (!file.exists()) {
				file.createNewFile();
			}
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
	@Path(value = "/add-pruefung")
	public String pruefungHinzufuegen(String json) {
		try {
			Pruefung pruefung = new Pruefung();
			pruefung.fromJson(json);
			if (!pruefungExistiert(pruefung.getModulNummer())){
				pruefungen.add(pruefung);
				pruefungenSpeichern();
				return "{\n'message': 'Die Pruefung wurde im datenbank hinzugefuegt', \n\t'status': 200, \n\t'Pruefung': " + pruefung.toJson() + "\n}";
			}
			return "{\n\t'message': 'Eine Pruefung mit gleicher Modulnummer existiert schon. Die ModulNummer muss eindeutig sein!', \n\t'status': 400\n}";	
		} catch(JSONCodecException e) {
			return "{\n\t'message': '" + e.getMessage() + "', \n\t'status': 400 \n}";
		} catch(IOException e) {
			return "{\n\t'message': '" + e.getMessage() + "', \n\t'status': 500 \n}";
		}
	}

	public Boolean pruefungExistiert(String modulNummer) {
		return getPruefungIndex(modulNummer) != -1 ? true : false; 
	}

	@Route(methode = "DELETE")
	@Path("/delete-pruefung")
	public String pruefungLoeschen(String modulNummer) {
		try {
			int index = getPruefungIndex(modulNummer);
			if (index != -1) {
				//Pruefung pruef = pruefungen.get(index);
				pruefungen.remove(index);
				pruefungenSpeichern();
				return "{\n\t'message': 'Die Pruefung wurde vom Datenbank geloescht',\n\t 'status': 204 \n}";
			} 
			return "{\n\t'message': 'Keine Pruefung mit Modulnummer " + modulNummer + " gefunden!',\n\t 'status': 400\n}";
		} catch(IOException e) {
			return "{\n\t'message': '" + e.getMessage() + "', \n\t'status': 500\n}";
		}
	}
	
	public int getPruefungIndex(String modulNummer) {
		int i = 0;
		for(Pruefung pruefung : pruefungen) {
			if(pruefung.getModulNummer().equals(modulNummer)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	@Route(methode = "PUT")
	@Path(value = "/update-pruefung")
	public String pruefungAktualisieren(String json) {
		try {
			Pruefung pruefung = new Pruefung();
			pruefung.fromJson(json);
			int index = getPruefungIndex(pruefung.getModulNummer());
			if (index != -1) {
				pruefungen.set(index, pruefung);
				pruefungenSpeichern();
				return "{\n\t'message': 'Die Pruefung wurde aktualisiert',\n\t 'status': 200,\n\t 'Pruefung': " + pruefung.toJson() + "\n}";
			}
			return "{\n\t'message': 'Die Pruefung mit der gegebenen Modulnummer existiert nicht im Datenbank!',\n\t status: 400}";
		} catch(JSONCodecException e) {
			return "{\n\t'message': '" + e.getMessage() + "', \n\t'status': 400 \n}";
		} catch(IOException e) {
			return "{\n\t'message': '" + e.getMessage() + "',\n\t 'status': 500 \n}";
		}
	}

	
	@Route(methode = "GET")
	@Path(value = "/get-pruefung")
	public String pruefungAusgeben(String key) {
		int index = getPruefungIndex(key);
		if (index != -1) {
			return "{\n\t'message': " + pruefungen.get(index).toJson() + ", \n\t 'status': 200\n}";
		}
		return "{\n\t'message': 'Pruefung nicht gefunden',\n\t 'status': 404\n}";
	}
	
	public void pruefungenSpeichern() throws IOException {
		try {
			File file = new File("./src/pruefung/pruefungen.txt");
			FileWriter fr = new FileWriter(file);
			for(Pruefung pruefung : pruefungen) {
				fr.write(pruefung.toJson() + "\n");
			}
			fr.close();
		} catch(IOException e) {
    		throw new IOException(e.getMessage());
    	}	
	}

	@Path(value = "/get-pruefungen")
	@Route(methode = "GET")
	public String allePruefungenAusgeben( String body) {
		String result = "";
		if(pruefungen.size() != 0) {
			for(Pruefung pruefung: pruefungen) {
				result += pruefung.toJson() + "\n";
			}
			return "{\n\t 'message': {\n\t " + result + "}, \n\t'status': 200\n}";
		}
		return "{\n\t'message': 'Keine Pruefung wurde angelegt',\n\t' status': 200\n}";
	}

	public int getPruefungenAnzahl(){
    	return this.pruefungen.size();
	}
    
    /*public static void main(String[] args) {
    	PruefungsListe pl = new PruefungsListe();
    	pl.pruefungenAusgeben();
    	pl.pruefungHinzufuegen("{\"modulNummer\": 9,\"modulBezeichnung\": Physik,\"tag\": 3,\"monat\": 5,\"jahr\": 2012,\"ort\": Bafoussam}");
    	pl.pruefungenAusgeben();
    	pl.pruefungLoeschen("6");
    	pl.pruefungenAusgeben();
    	pl.pruefungAktualisieren("{\"modulNummer\": 9,\"modulBezeichnung\": Mechanik,\"tag\": 3,\"monat\": 5,\"jahr\": 2012,\"ort\": Yaounde}");
    	System.out.println(pl.pruefungAusgeben("2789"));
    }  */
}

