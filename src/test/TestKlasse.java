package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pruefung.JSONCodec;
import pruefung.Pruefung;
import pruefung.PruefungsListe;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestKlasse {
    Pruefung pruefung = new Pruefung();
    private static final String MODUL_NUMMER_ERROR = "Die Modulnummer darf nicht leer oder null sein." ;
    private static final String MODUL_BEZEICHNUNG_ERROR = "Die Modulbezeichnung darf nicht leer oder null sein." ;
    private static final String TAG_ERROR_MESSAGE = "Der Tag muss zwischen 1 und 31 sein.";
    private static final String MONATE_ERROR_MESSAGE = "Der gegebene Monatsnummer muss zwischen 1 und 12 liegen!";
    private static final String MODUL_ORT_MESSAGE = "Der gegebene Monatsnummer muss zwischen 1 und 12 liegen!";
    private static final String JAHR_ERROR_MESSAGE = "Das Jahr muss zwischen 2022 und 2024 liegen!";

    @BeforeEach
    public void beforeEachTestCase() {

    }

    @AfterEach
    public void afterEachTestCase() {

    }

    @Test
    public void eine_exception_werfen_wenn_modulnummer_leer_ist() {
        String pruef = "{\"modulNummer\": ,\"modulBezeichnung\": Chimie,\"tag\": 5,\"monat\": 5,\"jahr\": 2022,\"ort\": HTW-SAAR}";
        Throwable exception = assertThrows(
                JSONCodec.JSONCodecException.class, () -> {
                    pruefung.fromJson(pruef);
                }
        );
        assertEquals("IllegalArgumentException: " + MODUL_NUMMER_ERROR , exception.getMessage());
    }

    @Test
    public void eine_exception_werfen_wenn_modulbezeichnung_leer_ist() {
        String pruef = "{\"modulNummer\": MOD-1,\"modulBezeichnung\": ,\"tag\": 5,\"monat\": 5,\"jahr\": 2022,\"ort\": HTW-SAAR}";
        Throwable exception = assertThrows(
                JSONCodec.JSONCodecException.class, () -> {
                    pruefung.fromJson(pruef);
                }
        );
        assertEquals("IllegalArgumentException: " + MODUL_BEZEICHNUNG_ERROR , exception.getMessage());
    }

    @Test
    public void exception_werfen_wenn_tag_nicht_zwischen_1_und_31_liegt() {
        String pruef = "{\"modulNummer\": MOD-1,\"modulBezeichnung\": CHEMIE,\"tag\": 100,\"monat\": 5,\"jahr\": 2022,\"ort\": HTW-SAAR}";
        Throwable exception = assertThrows(
                JSONCodec.JSONCodecException.class, () -> {
                    pruefung.fromJson(pruef);
                }
        );
        assertEquals("IllegalArgumentException: " + TAG_ERROR_MESSAGE , exception.getMessage());
    }

    @Test
    public void monat_muss_zwischen_1_und_12_liegen() {
        String pruef = "{\"modulNummer\": MOD-1,\"modulBezeichnung\": CHEMIE,\"tag\": 10,\"monat\": 20,\"jahr\": 2022,\"ort\": HTW-SAAR}";
        Throwable exception = assertThrows(
                JSONCodec.JSONCodecException.class, () -> {
                    pruefung.fromJson(pruef);
                }
        );
        assertEquals("IllegalArgumentException: " + MONATE_ERROR_MESSAGE , exception.getMessage());
    }

    @Test
    public void pruefungsort_darf_nicht_leer_sein() {
        String pruef = "{\"modulNummer\": MOD-1,\"modulBezeichnung\": CHEMIE,\"tag\": 10,\"monat\": 12,\"jahr\": 2022,\"ort\": }";
        Throwable exception = assertThrows(
                JSONCodec.JSONCodecException.class, () -> {
                    pruefung.fromJson(pruef);
                }
        );
        assertEquals("IllegalArgumentException: " + MODUL_ORT_MESSAGE , exception.getMessage());
    }

    @Test
    public void exception_werfen_wenn_jahr_nicht_zwischen_2022_und_2025_liegt() {
        String pruef = "{\"modulNummer\": MOD-1,\"modulBezeichnung\": CHEMIE,\"tag\": 10,\"monat\": 12,\"jahr\": 2040,\"ort\": }";
        Throwable exception = assertThrows(
                JSONCodec.JSONCodecException.class, () -> {
                    pruefung.fromJson(pruef);
                }
        );
        assertEquals("IllegalArgumentException: " + JAHR_ERROR_MESSAGE , exception.getMessage());
    }

    @Test
    public void neue_pruefung_muss_im_dateie_gespeichert_werden() throws JSONCodec.JSONCodecException, IOException {
        String pruef = "{\"modulNummer\": MOD-1,\"modulBezeichnung\": CHEMIE,\"tag\": 10,\"monat\": 12,\"jahr\": 2022,\"ort\": UNI-SAAR}";
        pruefung.fromJson(pruef);
        PruefungsListe pl= new PruefungsListe();

        // pruefung in Datei speichern
        pl.pruefungHinzufuegen(pruefung.toJson());
        pl.pruefungenSpeichern();

        // Pruefung in Datei lesen und pruefen ob sie Vorhanden ist
        File file = new File("./src/pruefung/pruefungen.txt");
        BufferedReader r = new BufferedReader(new FileReader(file));
        String scan;
        int lines = 0;
        while((scan=r.readLine())!=null) {
            lines++;
        }
        r.close();
        // Dateie loeschen damit sie nicht in anderen Test betrachtet wird.
        file.delete();
        // Wenn die Datei gespeichert ist, dann gibt es eine nicht leere Zeile im Datei
        assertEquals(1, lines);
    }

    @Test
    public void fehler_nachricht_anzeigen_wenn_Modulnummer_dupliziert_ist() throws JSONCodec.JSONCodecException, IOException {
        // Pruefung erstellen
        String pruef = "{\"modulNummer\": MOD-1,\"modulBezeichnung\": CHEMIE,\"tag\": 10,\"monat\": 12,\"jahr\": 2022,\"ort\": UNI-SAAR}";
        pruefung.fromJson(pruef);

        PruefungsListe pl= new PruefungsListe();
        // pruefung in Datei speichern
        String jsonPruefung = pruefung.toJson();
        pl.pruefungHinzufuegen(jsonPruefung);
        pl.pruefungenSpeichern();

        assertEquals("{\n\t'message': 'Eine Pruefung mit gleicher Modulnummer existiert schon. Die ModulNummer muss eindeutig sein!', \n\t'status': 400\n}", pl.pruefungHinzufuegen(jsonPruefung));
    }

    @Test
    public void keine_pruefung_aktualisierung_wenn_modulnummer_nicht_existiert() throws JSONCodec.JSONCodecException, IOException {
        // Pruefung erstellen
        String pruef = "{\"modulNummer\": MOD-1,\"modulBezeichnung\": CHEMIE,\"tag\": 10,\"monat\": 12,\"jahr\": 2022,\"ort\": UNI-SAAR}";
        pruefung.fromJson(pruef);

        PruefungsListe pl= new PruefungsListe();
        // pruefung in Datei speichern
        assertEquals("{\n\t'message': 'Die Pruefung mit der gegebenen Modulnummer existiert nicht im Datenbank!',\n\t status: 400}", pl.pruefungAktualisieren(pruefung.toJson()));
    }

    @Test
    public void pruefung_kann_aktualisiert_werden_wenn_sie_im_datenbank_existiert() throws JSONCodec.JSONCodecException, IOException {
        // Pruefung erstellen
        String pruef = "{\"modulNummer\": MOD-1,\"modulBezeichnung\": CHEMIE,\"tag\": 10,\"monat\": 12,\"jahr\": 2022,\"ort\": UNI-SAAR}";
        pruefung.fromJson(pruef);

        PruefungsListe pl= new PruefungsListe();
        pl.pruefungHinzufuegen(pruefung.toJson());
        pl.pruefungenSpeichern();

        pruef = "{\"modulNummer\": MOD-1,\"modulBezeichnung\": PHYSIK,\"tag\": 10,\"monat\": 12,\"jahr\": 2022,\"ort\": HTW-SAAR}";

        assertEquals("{\n\t'message': 'Die Pruefung wurde aktualisiert',\n\t 'status': 200,\n\t 'Pruefung': " + pruef + "\n}", pl.pruefungAktualisieren(pruef));
    }

    @Test
    public void pruefung_kann_geloescht_werden_wenn_sie_im_datenbank_existiert() throws JSONCodec.JSONCodecException, IOException {
        // Pruefung erstellen
        String pruef = "{\"modulNummer\": MOD-1,\"modulBezeichnung\": CHEMIE,\"tag\": 10,\"monat\": 12,\"jahr\": 2022,\"ort\": UNI-SAAR}";
        pruefung.fromJson(pruef);

        PruefungsListe pl= new PruefungsListe();
        pl.pruefungHinzufuegen(pruefung.toJson());
        pl.pruefungenSpeichern();
        pl.pruefungLoeschen("MOD-1");
        pl.pruefungenSpeichern();

        // Pruefung in Datei lesen und pruefen ob die Datei leer ist
        File file = new File("./src/pruefung/pruefungen.txt");
        BufferedReader r = new BufferedReader(new FileReader(file));
        String scan;
        int lines = 0;
        while((scan=r.readLine())!=null) {
            lines++;
        }
        r.close();
        // Dateie loeschen damit sie nicht in anderen Test betrachtet wird.
        file.delete();
        // Wenn die Datei geloescht ist, dann gibt es keine ausgeffuellte Zeile im Datei
        assertEquals(0, lines);
    }

    @Test
    public void pruefungsliste_array_muss_ausgeffuellt_werden_wenn_neuer_prufungsliste_object_erzeugt_ist() throws IOException, JSONCodec.JSONCodecException {
        // Erstmal die Datei mit mehrere Pruefungen ausfuellen
        String[] pruefungen = {
                "{\"modulNummer\": MOD-1,\"modulBezeichnung\": CHEMIE,\"tag\": 10,\"monat\": 12,\"jahr\": 2023,\"ort\": UNI-SAAR}" ,
                "{\"modulNummer\": MOD-2,\"modulBezeichnung\": MATHE,\"tag\": 5,\"monat\": 7,\"jahr\": 2022,\"ort\": HTW-SAAR}" ,
                "{\"modulNummer\": MOD-3,\"modulBezeichnung\": BIOLOGIE,\"tag\": 10,\"monat\": 12,\"jahr\": 2022,\"ort\": UNI-SAAR}" ,
                "{\"modulNummer\": MOD-4,\"modulBezeichnung\": PHYSIK,\"tag\": 15,\"monat\": 6,\"jahr\": 2024,\"ort\": HTW-SAAR}"
             };

        File file = new File("./src/pruefung/pruefungen.txt");
        FileWriter fr = new FileWriter(file);
        Pruefung pruef = new Pruefung();
        for(String pruefung : pruefungen) {
            pruef.fromJson(pruefung);
            fr.write( pruef.toJson() + "\n");
        }
        fr.close();

        // Datei lesen und Pruefungen Zählen
        BufferedReader r = new BufferedReader(new FileReader(file));
        String scan;
        int lines = 0;
        while((scan=r.readLine())!=null) {
            lines++;
        }
        r.close();
        // Neues PruefungslisteObjekt erzeugen und pruefen, ob die Anzahl der Pruefungen im Array gleich ist mt der Datei
        PruefungsListe pl = new PruefungsListe();
        file.delete();
        assertEquals(lines, pl.getPruefungenAnzahl());
    }
}