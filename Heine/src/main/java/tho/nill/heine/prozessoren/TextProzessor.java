package tho.nill.heine.prozessoren;

import java.nio.charset.Charset;

import tho.nill.heine.beans.Dokument;
import tho.nill.heine.beans.Gedicht;
import tho.nill.heine.beans.Strophe;
import tho.nill.heine.beans.Vers;

public class TextProzessor extends AbstractProzessor {

	public TextProzessor(Charset charset,String outDir) {
		super(charset, ".txt",outDir);
	}

	@Override
	public void endDocument(Dokument d) {
		// not needed
	}

	@Override
	public void startGedicht(Gedicht g) {
		a("\n");
		a(g.getTitel());
		a("\n\n");
	}

	@Override
	public void endGedicht(Gedicht g) {
		a("\n\n");
	}

	@Override
	public void startStrophe(Strophe s) {
		// not needed
	}

	@Override
	public void endStrophe(Strophe s) {
		a("\n");
	}

	@Override
	public void vers(Vers v) {
		a(v.getText());
		a("\n");
	}

	@Override
	protected TextProzessor retThis() {
		return this;
	}

	@Override
	public TextProzessor start() {
		return this;
	}

}
