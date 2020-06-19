package tho.nill.heine.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static tho.nill.helper.LogHelper.*;
import tho.nill.heine.prozessoren.GedichteProzessor;

public class Strophe {
	private static final Logger LOGGER = Logger.getLogger(Strophe.class.getName());

	private List<Vers> verse = new ArrayList<>(10);

	public void add(String zeile) {
		finer(LOGGER,"Vers angelegt: {0} ",zeile);
		verse.add(new Vers(zeile));
	}

	public boolean isEmpty() {
		return verse.isEmpty();
	}

	public void run(GedichteProzessor prozessor) {
		finer(LOGGER,"Strophen ausgeben: ");
		prozessor.startStrophe(this);
		for (Vers v : verse) {
			v.run(prozessor);
		}
		prozessor.endStrophe(this);
	}

}
