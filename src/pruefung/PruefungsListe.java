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
				return "{'message': 'Die Pruefung wurde im datenbank hinzugefuegt', 'status': 200, 'Pruefung': " + pruefung.toJson() + "}";
			}
			return "{'message': 'Eine Pruefung mit gleicher Modulnummer existiert schon. Die ModulNummer muss eindeutig sein!', 'status': 400}";	
		} catch(JSONCodecException e) {
			return "{'message': '" + e.getMessage() + "', 'status': 400 }";
		} catch(IOException e) {
			return "{'message': '" + e.getMessage() + "', 'status': 500 }";
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
			//System.out.println("Index: " + index + " ModulNr: " + modulNummer);
			if (index != -1) {
				Pruefung pruef = pruefungen.get(index);
				pruefungen.remove(index);
				pruefungenSpeichern();
				return "{'message': 'Die Pruefung wurde vom Datenbank geloescht', 'status': 204 }";
			} 
			return "{'message': 'Keine Pruefung mit Modulnummer " + modulNummer + " gefunden!', 'status': 400}";
		} catch(IOException e) {
			return "{'message': '" + e.getMessage() + "', 'status': 500}";
		}
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
				return "{'message': 'Die Pruefung wurde aktualisiert', 'status': 200, 'Pruefung': " + pruefung.toJson() + "}";
			}
			return "{'message': 'Die Pruefung mit der gegebenen Modulnummer existiert nicht im Datenbank!', status: 400}";
		} catch(JSONCodecException e) {
			return "{'message': '" + e.getMessage() + "', 'status': 400 }";
		} catch(IOException e) {
			return "{'message': '" + e.getMessage() + "', 'status': 500 }";
		}
	}

	
	@Route(methode = "GET")
	@Path(value = "/get-pruefung")
	public String pruefungAusgeben(String key) {
		int index = getPruefungIndex(key);
		if (index != -1) {
			return "{'message': " + pruefungen.get(index).toJson() + ", 'status': 200}";
		}
		return "{'message': 'Pruefung nicht gefunden', 'status': 404}";
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
			return "{'message': {" + result + "}, 'status': 200}";
		}
		return "{'message': 'Keine Pruefung wurde angelegt', 'status': 200}";
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

