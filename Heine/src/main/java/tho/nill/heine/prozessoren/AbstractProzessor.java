package tho.nill.heine.prozessoren;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import tho.nill.grundgestein.texter.AbstractTexter;
import tho.nill.heine.beans.Dokument;

public abstract class AbstractProzessor extends AbstractTexter<AbstractProzessor> implements GedichteProzessor{

	private Charset charset;
	private String dateiName;
	private String filetype;
	private String outDir;


	public AbstractProzessor(Charset charset, String filetype,String outDir) {
		super(0);
		this.charset = charset;
		this.dateiName = "";
		this.filetype = filetype;
		this.outDir = outDir;
	}

	public void startDocument(Dokument dokument) {
		this.dateiName = dokument.getDateiName();
	}

	public void run(Dokument d) throws IOException {
		d.run(this);
	
		Writer w = erzeugeWriter(dateiName.replaceFirst("\\.gedichte$", filetype));
		w.write(clear());
		w.close();
	
	}

	private Writer erzeugeWriter(String dateiName) throws IOException {
		Path p = Path.of(outDir +  File.separator  + dateiName);
		Files.createDirectories(p.normalize().getParent());
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outDir + "/" + dateiName), charset));
	}
	
	protected String getOutDir() {
		return outDir;
	}
}