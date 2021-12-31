package http;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.Scanner;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import pruefung.PruefungsListe;

public class CustomHttpServer {

	private static int port = 4711;

	public static void main(String[] args) {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(port), 0); // siehe Fabrikmethode
			HttpContext context = server.createContext("/");
			context.setHandler(new Handler()); // siehe strategy pattern, auch als lambda-Ausdruck moeglich.

			server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
			server.start();
			System.out.println("Web-Server auf Port " + port + " gestartet.");
			System.out.println(
					"Rufe Web-Server im Web-Browser auf mit http://localhost:" + port + "/MeineRessource?MeineFrage");

			System.out.println("Stoppe Web-Server durch irgendeine Eingabe");
			Scanner sc = new Scanner(System.in);
			sc.next();
			sc.close();
			server.stop(0);
			System.out.println("Web-Server gestoppt.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class Handler implements HttpHandler {

		// HTTP Status Codes
		static final int OK = 200;

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			String method = exchange.getRequestMethod();
			String uri = exchange.getRequestURI().getPath();
			String query = exchange.getRequestURI().getQuery();
			String body = new String(exchange.getRequestBody().readAllBytes());
			
			String response;

			// String response = MessageFormat.format(
			// 		"{0} Methode mit URI \"{1}\" und Query \"{2}\" und Body \"{3}\" erhalten.", method, uri, query,
			// 		ServerApplication.run(PruefungsListe.class, method, body));
			// System.out.println(response);

			// Addapt response messages
			if(method.equals("GET")) {
				response = MessageFormat.format(
					"Pruefung mit Modulnummer  \"{0}\" : {1}.", body,
					ServerApplication.run(PruefungsListe.class, method, body));

			} else if (method.equals("DELETE")) {
				response = MessageFormat.format(
					"Pruefung mit Modulnummer  \"{0}\" geloescht: {1}.", body,
					ServerApplication.run(PruefungsListe.class, method, body));

			} else if (method.equals("PUT")) {
				response = MessageFormat.format(
					"Eine Pruefung wurde im Datenbank hinzugefuegt: {0}.",
					ServerApplication.run(PruefungsListe.class, method, body));

			} else if (method.equals("UPADATE")) {
				response = MessageFormat.format(
					"Diese Pruefung wurde aktualisiert: {1}.", body,
					ServerApplication.run(PruefungsListe.class, method, body));

			} else  {
				response = MessageFormat.format(
							"{0} Methode mit URI \"{1}\" und Query \"{2}\" und Body \"{3}\" erhalten.", method, uri, query,
							ServerApplication.run(PruefungsListe.class, method, body));
					System.out.println(response);
			}

			exchange.getResponseHeaders().add("Content-Type", "text/html");
			exchange.sendResponseHeaders(OK, response.getBytes().length);
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

}


