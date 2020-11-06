package nl.andrewlalis.blockbookbinder.util;

import lombok.Getter;

import java.io.IOException;
import java.util.Properties;

/**
 * Singleton class which handles the properties defined in an external
 * properties file.
 */
public class ApplicationProperties {
	private static ApplicationProperties instance;
	@Getter
	private final Properties properties;

	public static ApplicationProperties getInstance() {
		if (instance == null) {
			try {
				instance = new ApplicationProperties();
			} catch (IOException e) {
				System.err.println("Could not load properties!");
				System.exit(1);
			}
		}
		return instance;
	}

	/**
	 * Shortcut for getting a property.
	 * @param key The property's key.
	 * @return The value for that property.
	 */
	public static String getProp(String key) {
		return getInstance().getProperties().getProperty(key);
	}

	private ApplicationProperties() throws IOException {
		this.properties = new Properties();
		this.properties.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
	}
}
