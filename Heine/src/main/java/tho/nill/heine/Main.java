package tho.nill.heine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import tho.nill.heine.prozessoren.GedichteProzessor;
import tho.nill.heine.prozessoren.HtmlProzessor;
import tho.nill.heine.prozessoren.TexProzessor;
import tho.nill.heine.prozessoren.TextProzessor;
import tho.nill.heine.prozessoren.XmlProzessor;

public class Main {
	private static final String TARGET = "target";
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {
		try {
			if (args.length != 3) {
				LOGGER.severe(" tho.nill.Heine <pfadname> <ausgabeart = text|tex|xml|html> <Widmung>");
				System.exit(-1);
			}
			String pfad = args[0];
			String ausgabeart = args[1];
			String widmung = args[2];

			checkPath(pfad);

			Reader reader = new Reader(pfad,widmung);
			reader.read();
			reader.run(createProzessor(ausgabeart));

		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE,"Exception occured: {0} {1} ",new Object[] {ex.getClass().getName(),ex.getLocalizedMessage()});
			System.exit(-2);
		}
	}

	private static GedichteProzessor createProzessor(String ausgabeart) {
		switch (ausgabeart) {
		case "text":
			return new TextProzessor(StandardCharsets.UTF_8,TARGET);
		case "tex":
			return new TexProzessor(StandardCharsets.UTF_8,TARGET);
		case "xml":
			return new XmlProzessor(StandardCharsets.UTF_8,TARGET);
		case "html":
			return new HtmlProzessor(StandardCharsets.UTF_8,TARGET);

		default:
			LOGGER.log(Level.SEVERE,"Prozessor unknown {0}",ausgabeart);
			System.exit(-3);
			return null;
		}
	}

	private static void checkPath(String pfad) throws IOException {
		Path p = Path.of(pfad);
		if (p.toFile().getCanonicalPath().contains("..")) {
			LOGGER.severe("Path with a .. is not allowed");
			System.exit(-2);
		}

	}

}
