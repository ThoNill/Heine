package tho.nill.heine.prozessoren;

import java.io.IOException;

import tho.nill.heine.beans.Dokument;
import tho.nill.heine.beans.Gedicht;
import tho.nill.heine.beans.Strophe;
import tho.nill.heine.beans.Vers;

public interface GedichteProzessor {
	void startDocument(Dokument d);
	void endDocument(Dokument d);
	void startGedicht(Gedicht g);
	void endGedicht(Gedicht g);
	void startStrophe(Strophe s);
	void endStrophe(Strophe s);
	void vers(Vers v);
	void run(Dokument d) throws IOException;

}
