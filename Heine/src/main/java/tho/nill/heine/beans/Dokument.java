package tho.nill.heine.beans;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import tho.nill.heine.prozessoren.GedichteProzessor;
import static tho.nill.helper.LogHelper.*;

public class Dokument {
	private static final Logger LOGGER = Logger.getLogger(Dokument.class.getName());
	private enum Zustand {
		NEUES_DOKUMENT, NEUES_GEDICHT, IM_GEDICHT
	}

	static final String KURZER_SCHNITT = "----";
	static final String LANGER_SCHNITT = "-----------";

	private Zustand zustand = Zustand.NEUES_DOKUMENT;
	private boolean neueSeite = false;
	private Gedicht aktuellesGedicht;

	private List<Gedicht> gedichte = new LinkedList<>();
	private List<Gedicht> doppelteGedichte = new LinkedList<>(); // für doppelteGedichte

	private String dateiName;
	private String titel;
	private String autor;
	private String widmung;

	public Dokument(String dateiName, String widmung) {
		super();
		this.dateiName = dateiName;
		this.widmung = widmung;

		zustand = Zustand.NEUES_DOKUMENT;
	}

	public void add(String text) {
		zustandWechseln(text);
		textBearbeiten(text);
	}

	private void zustandWechseln(String text) {
		finer(LOGGER,"Text= {0}",text);
		if (text.startsWith(KURZER_SCHNITT)) {
			zustand = Zustand.NEUES_GEDICHT;
			neueSeite = false;
			finer(LOGGER,"Neues Gedicht = {0}", text);
		}
		if (text.startsWith(LANGER_SCHNITT)) {
			zustand = Zustand.NEUES_GEDICHT;
			neueSeite = true;
			finer(LOGGER,"Neues Gedicht = {0} ",text);
		}
	}


	private void textBearbeiten(String text) {
		if (normalerText(text)) {
			switch (zustand) {
			case NEUES_DOKUMENT:
				dokumentDatenLesen(text);
				break;
			case NEUES_GEDICHT:
				gedichtAnlegen(text);
				break;
			case IM_GEDICHT:
				ansGedichtWeiterreichen(text);
				break;
			}
		}
	}

	private boolean normalerText(String text) {
		return !text.startsWith(KURZER_SCHNITT);
	}

	private void dokumentDatenLesen(String text) {
		if (titel == null) {
			titel = text.trim();
		} else {
			if (autor == null) {
				autor = text.trim();
			}
		}
	}

	private void gedichtAnlegen(String text) {
		if (!text.isBlank()) {
			dasGedichtmerken();
			finer(LOGGER,"Titel des Gedichtes = {0}",text.trim());
			aktuellesGedicht = new Gedicht(text.trim(), this.neueSeite);
			zustand = Zustand.IM_GEDICHT;
		}
	}

	private void ansGedichtWeiterreichen(String text) {
		if (aktuellesGedicht != null) {
			finer(LOGGER,"Text ans Gedicht weitergereicht = {0}",text.trim());
			aktuellesGedicht.add(text);
		}
	}

	private void dasGedichtmerken() {
		if (aktuellesGedicht != null) {
			addGedicht(aktuellesGedicht);
			aktuellesGedicht = null;
		}
	}



	private void addGedicht(Gedicht g) {
		if (g != null) {
			if (alreadyThere(g, 4)) {
				doppelteGedichte.add(g);
			} else {
				gedichte.add(g);
			}
		}
	}

	private boolean alreadyThere(Gedicht g, int level) {
		for (Gedicht gedicht : gedichte) {
			if (Gedicht.compareLevel(g, gedicht) > level) {
				return true;
			}
		}
		return false;
	}

	
	
	public void run(GedichteProzessor prozessor) {
		finer(LOGGER,"Dokument ausgeben: {0}",getDateiName());
		prozessor.startDocument(this);
		for (Gedicht g : gedichte) {
			g.run(prozessor);
		}
		prozessor.endDocument(this);
	}
	
	public void endOfDocument() {
		addGedicht(aktuellesGedicht);
	}

	public String getDateiName() {
		return dateiName;
	}

	public String getTitel() {
		return titel;
	}

	public String getAutor() {
		return autor;
	}

	public String getWidmung() {
		return widmung;
	}
}
