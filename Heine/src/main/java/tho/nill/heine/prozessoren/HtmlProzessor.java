package tho.nill.heine.prozessoren;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import tho.nill.heine.beans.Dokument;
import tho.nill.heine.beans.Gedicht;
import tho.nill.heine.beans.Strophe;
import tho.nill.heine.beans.Vers;

public class HtmlProzessor extends AbstractProzessor {
	private static final Logger LOGGER = Logger.getLogger(HtmlProzessor.class.getName());

	public HtmlProzessor(Charset charset, String outDir) {
		super(charset, ".html", outDir);

	}

	private void cssKopieren(String pathname) {
		try (InputStream css = Thread.currentThread().getContextClassLoader().getResourceAsStream("gedichte.css")) {
			if (css == null) {
				LOGGER.log(Level.SEVERE, "can not find resource gedicht.css ");
			}
			cssChannelAusgeben(pathname, css);
		} catch (IOException e1) {
			LOGGER.log(Level.SEVERE, "problem with resource gedicht.css ");
		}
	}

	private void cssChannelAusgeben(String pathname, InputStream css) {
		Path path = Paths.get(getOutDir() + File.separator + pathname).getParent();
		try (ReadableByteChannel rbc = Channels.newChannel(css)) {
			inZieldateiSchreiben(path, rbc);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "problems with channel");
		}
	}

	private void inZieldateiSchreiben(Path path, ReadableByteChannel rbc)  {
		try (FileOutputStream fos = new FileOutputStream(getVerzeichnisName(path) + "gedichte.css")) {
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "can not create File in {0}", new Object[] { getVerzeichnisName(path) });
		}
	}

	private String getVerzeichnisName(Path path) {
		return (path == null) ? "" : path.toString() +  File.separator;
	}

	@Override
	public void startDocument(Dokument d) {
		cssKopieren(d.getDateiName());
		super.startDocument(d);
		a("<!doctype html>\n");
		a("<html lang=\"de\">\n");
		a("<meta charset=\"utf-8\"><head>\n <link rel=\"stylesheet\" href=\"gedichte.css\">");
		a("<Title>");
		a(aufbereiten(d.getTitel()));
		a("</Title></Head>\n<Body class=\"gedichte\" >\n");
	}

	@Override
	public void endDocument(Dokument d) {
		a("</Body>\n</Html>\n");
	}

	@Override
	public void startGedicht(Gedicht g) {
		a("<div class=\"gedicht\" ><H2 class=\"gedichttitel\">");
		a(aufbereiten(g.getTitel()));
		a("</H2>\n");
	}

	@Override
	public void endGedicht(Gedicht g) {
		a("</div>\n");
	}

	@Override
	public void startStrophe(Strophe s) {
		a("<div class=\"strophe\">");
	}

	@Override
	public void endStrophe(Strophe s) {
		a("</div>");
	}

	@Override
	public void vers(Vers v) {
		a("<div class=\"vers\" >");
		a(aufbereiten(v.getText()));
		a("</div><br>\n");
	}

	private Object aufbereiten(String text) {
		return text.replaceAll("ä", "&auml;").replaceAll("ö", "&ouml;").replaceAll("ü", "&uuml;")
				.replaceAll("Ä", "&Auml;").replaceAll("Ö", "&Ouml;").replaceAll("Ü", "&Uuml;")
				.replaceAll("ß", "&szlig;");
	}

	@Override
	protected HtmlProzessor retThis() {
		return this;
	}

	@Override
	public HtmlProzessor start() {
		return this;
	}

}
