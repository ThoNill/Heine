package tho.nill.heine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Stream;

import tho.nill.heine.beans.Dokument;
import tho.nill.heine.prozessoren.GedichteProzessor;

public class Reader implements Consumer<Path> {
	private static final Logger LOGGER = Logger.getLogger(Reader.class.getName());

	
	private String path;
	private List<Dokument> dokumente = new LinkedList<>();
	
	private String widmung;

	public Reader(String path,String widmung) {
		this.path = path;
		this.widmung = widmung;
		File f = new File(path);
		if (!f.exists()) {
			LOGGER.severe("File does not exist " + f.getAbsolutePath());
		}

	}
	
	
	public void read() throws IOException {
		Path p = Path.of(this.path);
		if (p.toFile().isDirectory()) {
			readDirectory(p);
		}
		if (p.toFile().isFile()) {
			accept(p);
		}
		
	}

	private void readDirectory(Path path) throws IOException {
		try (Stream<Path> paths = Files.walk(path)) {
			paths.filter(Files::isRegularFile).filter(Reader::isGedichtFile).forEach(this);
		}
	}

	
	
	private static boolean isGedichtFile(Path f) {
		return f.getFileName().toString().endsWith("gedicht");
	}

	@Override
	public void accept(Path path) {
		try (Stream<String> strings = Files.lines(path)){
			Dokument aktuellesDokument = new Dokument(path.toString(),widmung);
			strings.forEach(text -> aktuellesDokument.add(text));
			aktuellesDokument.endOfDocument();
			dokumente.add(aktuellesDokument);
		} catch (IOException e) {
			LOGGER.severe("Error in execution " + path);
		}
	}

	public void run(GedichteProzessor prozessor) {
		dokumente.stream().forEach(d -> {
			try {
				prozessor.run(d);
			} catch (IOException e) {
				LOGGER.severe(e.getMessage());
			}
		});
	}
}
