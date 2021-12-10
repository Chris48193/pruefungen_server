package rest;

import pruefung.Pruefung;
//import pruefung.Route;

/*class Pruefung {

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
}*/

public class Server {

	/* F�r so ein Framework m�ssen Vereinbarungen getroffen werden. 
	 * Hier ist es so, dass der Server ein Tupel von Informationen bekommt. 
	 * 1. Die Methode: Das ist entweder der Text "create", "read", "update" oder "delete"
	 * 2. Die Nachricht: Das ist ein beliebiger Text
	 * �ber die Annotation wird die Methode bestimmt. 
	 * Die Nachricht wird dann �ber das Argument �bergeben.
	 * 
	 *  Beispiel ist so etwas wie eine ausf�hrende Einheit. 
	 *  
	 *  In dieser Anwendung ist es so, dass der Server hartkodiert 4 Requests bekommt. 
	 * */
	
	public static void main(String[] args) {
		ServerApplication.run(Pruefung.class);
	}

}
