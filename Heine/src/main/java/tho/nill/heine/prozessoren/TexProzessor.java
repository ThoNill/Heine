package tho.nill.heine.prozessoren;

import java.nio.charset.Charset;

import tho.nill.heine.beans.Dokument;
import tho.nill.heine.beans.Gedicht;
import tho.nill.heine.beans.Strophe;
import tho.nill.heine.beans.Vers;

public class TexProzessor extends AbstractProzessor {
	private boolean startStrophe = true;

	public TexProzessor(Charset charset, String outDir) {
		super(charset, ".tex", outDir);
	}

	private String quotierungBeachten(String text) {
		boolean left = true;
		StringBuilder builder = new StringBuilder();
		for (char c : text.toCharArray()) {
			if (c == '"') {
				if (left) {
					a("\\glqq ");
				} else {
					a("\\grqq ");
				}
				left = !left;
			} else {
				a(c);
			}
		}
		return builder.toString();
	}

	private String wegenHyperlinks(String text) {
		StringBuilder builder = new StringBuilder();
		for (char c : text.toCharArray()) {
			if (c == '_' || c == '$' || c == '#') {
				a("\\");
			}
			a(c);

		}
		return builder.toString();
	}

	@Override
	public void startDocument(Dokument d) {
		super.startDocument(d);
		a("\\documentclass[a4paper]{article}\n");
		a("\\pagestyle{plain}\n");
		a("\\usepackage{german}\n");
		a("\\usepackage{hyperref}\n");
		a("\\begin{document}\n");
		a("\\begin{titlepage}\n");
		a("\\begin{center}\n");
		a("\\vfill\n");
		a("\\Huge\\textbf{" + d.getTitel() + "}\\\\\n");
		a("\\large\\textbf{}\\\\\n");
		a("\\large\\textit{" + d.getAutor() + "}\\\\\n");
		if (d.getWidmung() != null && !d.getWidmung().isBlank()) {
			a("\\large\\textit{" + d.getWidmung() + "}\\\\\n");
		}
		a("\\vfill\n");
		a("\\end{center}\n");
		a("\\end{titlepage}\n");
		a("\\maketitle\n");
		a("\\newpage\n");
	}

	@Override
	public void endDocument(Dokument d) {
		a("\\end{document}\n");

	}

	@Override
	public void startGedicht(Gedicht g) {
		if (g.isEmpty()) {
			a("\n");
			return;
		}

		if (g.mitEigenerSeite()) {
			a("\\newpage\n");
		} else {
			a("\n\n");
			a("\n\n");
		}
		a("\\begin{quote}{\\bf ");
		a(g.getTitel());
		a("}\\end{quote}\n");
		a("\\begin{verse}\n");

	}

	@Override
	public void endGedicht(Gedicht g) {
		a("\\end{verse}\n");
	}

	@Override
	public void startStrophe(Strophe s) {
		a("\n");
		startStrophe = true;
	}

	@Override
	public void endStrophe(Strophe s) {
		a("\n");
	}

	@Override
	public void vers(Vers v) {
		if (!startStrophe) {
			a("\\\\\n");
		}
		String text = v.getText();
		if (text.startsWith("http")) {
			text = wegenHyperlinks(text);
			text = "\\href{" + text + "}{Link}";
		}

		a(quotierungBeachten(text));
		startStrophe = false;
	}

	@Override
	protected TexProzessor retThis() {
		return this;
	}

	@Override
	public TexProzessor start() {
		return this;
	}

}
