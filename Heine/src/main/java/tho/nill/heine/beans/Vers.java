package tho.nill.heine.beans;

import java.util.logging.Logger;

import javax.annotation.processing.Generated;

import tho.nill.heine.prozessoren.GedichteProzessor;

public class Vers {
	private static final Logger LOGGER = Logger.getLogger(Vers.class.getName());
	
	private String text;


	public String getText() {
		return text;
	}

	public Vers(String text) {
		super();
		this.text = text.trim();
	}

	@Override
	public String toString() {
		return text;
	}

	@Override
	@Generated(value = { "eclipse" })
	@SuppressWarnings("all") 
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}
	
	@Override
	@Generated(value = { "eclipse" })
	@SuppressWarnings("all") 
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vers other = (Vers) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	public void run(GedichteProzessor prozessor) {
		LOGGER.fine("Vers ausgeben: " + getText());
		prozessor.vers(this);
	}
	
}
