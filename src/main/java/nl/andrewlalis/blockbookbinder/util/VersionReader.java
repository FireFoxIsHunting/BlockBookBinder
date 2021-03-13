package nl.andrewlalis.blockbookbinder.util;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class VersionReader {
	public static String getVersion() {
		MavenXpp3Reader reader = new MavenXpp3Reader();
		try {
			Model model;
			if ((new File("pom.xml")).exists()) {
				model = reader.read(new FileReader("pom.xml"));
			} else {
				model = reader.read(new InputStreamReader(
						VersionReader.class.getResourceAsStream("/META-INF/maven/nl.andrewlalis/BlockBookBinder/pom.xml")
				));
			}
			return model.getVersion();
		} catch (IOException | XmlPullParserException e) {
			e.printStackTrace();
			return "Unknown";
		}
	}
}
