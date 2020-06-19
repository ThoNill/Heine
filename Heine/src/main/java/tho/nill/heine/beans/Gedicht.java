package tho.nill.heine.beans;

import java.util.HashSet;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import static tho.nill.helper.LogHelper.*;
import tho.nill.heine.prozessoren.GedichteProzessor;

public class Gedicht {
	private static final Logger LOGGER = Logger.getLogger(Gedicht.class.getName());

	private String titel;
	private List<Strophe> strophen = new LinkedList<>();
	private Set<String> zeilen = new HashSet<>();
	private boolean mitEigenerSeite;
	private Strophe aktuelleStrophe;

	public Gedicht(String titel, boolean mitEigenerSeite) {
		super();
		this.titel = titel;
		this.mitEigenerSeite = mitEigenerSeite;
		this.neueStrophe();
	}

	public boolean isEmpty() {
		return strophen.isEmpty();
	}

	public boolean mitEigenerSeite() {
		return mitEigenerSeite;
	}


	public void add(String text) {
		if (text != null) {
			if (leererText(text)) {
				neueStrophe();
			} else {
				finer(LOGGER,"Text an Strophe weitergereicht = {0} ",text.trim());
				if (aktuelleStrophe != null) {
					aktuelleStrophe.add(text);
					zeilen.add(text);
				}
			}
		}
	}
	private boolean leererText(String text) {
		return "".equals(text.trim());
	}

	private void neueStrophe() {
		if (aktuelleStrophe == null || !aktuelleStrophe.isEmpty()) {
			LOGGER.fine("Neue Strophe erzeugt");
			aktuelleStrophe = new Strophe();
			strophen.add(aktuelleStrophe);
		}
	}


	public static int compareLevel(Gedicht a, Gedicht b) {
		int level = 0;
		HashSet<String> gemeinsam = new HashSet<>();
		gemeinsam.addAll(a.zeilen);
		gemeinsam.addAll(b.zeilen);
		for (String z : gemeinsam) {
			if (a.zeilen.contains(z) && b.zeilen.contains(z)) {
				level++;
			}
		}
		return level;
	}

	public void run(GedichteProzessor prozessor) {
		finer(LOGGER,"Gedicht ausgeben: {0} ",getTitel());
		prozessor.startGedicht(this);
		for (Strophe s : strophen) {
			s.run(prozessor);
		}
		prozessor.endGedicht(this);
	}
	
	public String getTitel() {
		return titel;
	}
}
