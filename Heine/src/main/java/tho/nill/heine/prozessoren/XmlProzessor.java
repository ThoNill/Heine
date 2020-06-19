package tho.nill.heine.prozessoren;

import java.nio.charset.Charset;

import tho.nill.heine.beans.Dokument;
import tho.nill.heine.beans.Gedicht;
import tho.nill.heine.beans.Strophe;
import tho.nill.heine.beans.Vers;

public class XmlProzessor extends AbstractProzessor {

	public XmlProzessor(Charset charset,String outDir) {
		super(charset, ".xml",outDir);
	}

	@Override
	public void startDocument(Dokument d) {
		super.startDocument(d);
		a("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		a("<Document>");
		a("<Dateiname>");
		a(d.getDateiName());
		a("</Dateiname>");
		a("<Titel>");
		a(d.getTitel());
		a("</Titel>");
		a("<Autor>");
		a(d.getAutor());
		a("</Autor>");

	}

	@Override
	public void endDocument(Dokument d) {
		a("</Document>");
	}

	@Override
	public void startGedicht(Gedicht g) {
		a("<Gedicht><Title>");
		a(g.getTitel());
		a("</Title>");
	}

	@Override
	public void endGedicht(Gedicht g) {
		a("</Gedicht>");
	}

	@Override
	public void startStrophe(Strophe s) {
		a("<Strophe>");
	}

	@Override
	public void endStrophe(Strophe s) {
		a("</Strophe>");
	}

	@Override
	public void vers(Vers v) {
		a("<Vers>");
		a(v.getText());
		a("</Vers>");
	}

	@Override
	protected XmlProzessor retThis() {
		return this;
	}

	@Override
	public XmlProzessor start() {
		return this;
	}

}
