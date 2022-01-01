package http;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import pruefung.Path;
import pruefung.Route;



class Kontext {
	private Strategie strategie;

	public void setStrategie(Strategie s) {
		this.strategie = s;
	}

	public String mache(Anfrage n) {
		return strategie.mache(n);
	}
}

interface Strategie {

	String mache(Anfrage n);

}

class Router implements Strategie {

	private Class<?> konkreteImplementierung = null;

	public Router(Class<?> cls) {
		this.konkreteImplementierung = cls;
	}

	@Override
	public String mache(Anfrage n) {
		/*
		 * TODO Rufe die richtige Methode aus der Implementierung, hier Beispiel, auf.
		 * Das ist die Methode, die mit der Methode in der Anfrage annotiert ist. Als
		 * Argument bekommt die Methode die Nachricht aus der Anfrage.
		 */
		try {
			for (Method method : konkreteImplementierung.getMethods()) {
				Route route = method.getAnnotation(Route.class);
				Path path = method.getAnnotation(Path.class);
				if ((route != null && route.methode().equals(n.methode)) && (path != null && path.value().equals(n.path))) {
					System.out.println("Path: " + path  + "Methode: " + method.toString());
					return (String)method.invoke(konkreteImplementierung.getDeclaredConstructor().newInstance(), n.nachricht);
				}
			}
		} catch (InstantiationException | NoSuchMethodException | SecurityException e) {
			return "{'message': '" + e.getMessage() + "', 'status': 500}";
		} catch (IllegalAccessException e) {
			return "{'message': '" + e.getMessage() + "', 'status': 500}";
		} catch (IllegalArgumentException e) {
			return "{'message': '" + e.getMessage() + "', 'status': 500}";
		} catch (InvocationTargetException e) {
			return "{'message': '" + e.getMessage() + "', 'status': 500}";
		}
		System.out.println("No method found");
		return "{'message': 'Die angegebene uri ist nicht korrekt', 'status': 405}";
	}

}

class Anfrage {

	public String methode;
	public String nachricht;
	public String path;

	public Anfrage(String methode, String nachricht, String path) {
		this.methode = methode;
		this.nachricht = nachricht;
		this.path = path;
	}

}

class ServerApplication {
	/*
	 * Analogiebetrachtung
	 * 
	 * Kontext -> HttpServer 
	 * Strategie -> HttpHandler 
	 * Kontext.mache -> HttpHandler.handle 
	 * Anfrage -> HttpExchange 
	 * Anfrage.methode -> Http-RequestMethode POST, GET, PUT, DELETE 
	 * 					analog zu den CRUD-Methoden bei Datenbanken 
	 * Anfrage.nachricht -> path, body und query eines Http-Request
	 */
	/*
	 * Wie kann man durch das Hinzuf�gen einer einzigen Klasse, ein neues Verhalten
	 * des "Kontextes", des "Servers" gekommen? 
	 * L�sung: Annotationen und Reflection API
	 * Implementieren Sie die fehlende Methode mache in der 
	 * Klasse Router mit dem Reflection API. 
	 * 
	 * Ausgabe: 
	 * create: Haus aufgerufen
	 * read: Buch aufgerufen
	 * update: Notizen aufgerufen
	 * delete: Eintrag aufgerufen
	 */
	public static String run(Class<?> cls, String methode, String body, String path) {
		Kontext k = new Kontext();
		Strategie s = new Router(cls);
		k.setStrategie(s);
		
		//empfange Nachrichten / Anfragen / Requests 
		//und leite diese weiter an die Applikation. 
		//eine Anfrage besteht aus einer Methode und einer Nachricht.  
		Anfrage n = new Anfrage(methode, body, path);
		return k.mache(n);
	}
	
}
