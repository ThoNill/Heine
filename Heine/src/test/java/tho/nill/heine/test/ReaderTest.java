package tho.nill.heine.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import tho.nill.heine.Main;
import tho.nill.heine.Reader;
import tho.nill.heine.prozessoren.HtmlProzessor;
import tho.nill.heine.prozessoren.TexProzessor;
import tho.nill.heine.prozessoren.TextProzessor;
import tho.nill.heine.prozessoren.XmlProzessor;

public class ReaderTest {

  
    @Test
    public void test() {
        try {
        	Reader reader = new Reader("src/test/resources/test.gedichte","für Test");
            reader.read();
            reader.run(new TextProzessor(StandardCharsets.UTF_8,"target"));
            reader.run(new TexProzessor(StandardCharsets.UTF_8,"target"));
            reader.run(new XmlProzessor(StandardCharsets.UTF_8,"target"));
            reader.run(new HtmlProzessor(StandardCharsets.UTF_8,"target"));
            testeDatei("target/src/test/resources/test.txt");
            testeDatei("target/src/test/resources/test.tex");
            testeDatei("target/src/test/resources/test.xml");
            testeDatei("target/src/test/resources/test.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	private void testeDatei(String filename) {
		assertTrue(new File(filename).exists());
	}


    @Test
    public void main() {
        try {
        	Main.main(new String[]{"src/test/resources/test.gedichte","tex","für Test"});
        } catch (Exception e) {
            fail("Error occured");
        }
    }
  
}
