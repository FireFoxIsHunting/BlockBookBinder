package nl.andrewlalis.blockbookbinder.util;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides the ability to determine the length of a string, in
 * pixels, according to Minecraft's internal font.
 */
public class CharWidthMapper {
	@Getter
	private static final CharWidthMapper instance = new CharWidthMapper();

	private final Map<Character, Integer> charWidthMap;

	public CharWidthMapper() {
		this.charWidthMap = new HashMap<>();
		this.initCharWidthMap();
	}

	public int getWidth(char c) {
		return this.charWidthMap.getOrDefault(c, 6);
	}

	public int getWidth(String s) {
		if (s.length() == 0) return 0;
		int width = getWidth(s.charAt(0));
		for (int i = 1; i < s.length(); i++) {
			final char c = s.charAt(i);
			width += this.getWidth(c) + 1;
		}
		return width;
	}

	private void initCharWidthMap() {
		this.charWidthMap.put(' ', 3);
		this.charWidthMap.put('!', 1);
		this.charWidthMap.put('"', 3);
		this.charWidthMap.put('\'', 1);
		this.charWidthMap.put('(', 3);
		this.charWidthMap.put(')', 3);
		this.charWidthMap.put('*', 3);
		this.charWidthMap.put(',', 1);
		this.charWidthMap.put('.', 1);
		this.charWidthMap.put(':', 1);
		this.charWidthMap.put(';', 1);
		this.charWidthMap.put('<', 4);
		this.charWidthMap.put('>', 4);
		this.charWidthMap.put('@', 6);
		this.charWidthMap.put('I', 3);
		this.charWidthMap.put('[', 3);
		this.charWidthMap.put(']', 3);
		this.charWidthMap.put('`', 2);
		this.charWidthMap.put('f', 4);
		this.charWidthMap.put('i', 1);
		this.charWidthMap.put('k', 4);
		this.charWidthMap.put('l', 2);
		this.charWidthMap.put('t', 3);
		this.charWidthMap.put('{', 3);
		this.charWidthMap.put('|', 1);
		this.charWidthMap.put('}', 3);
		this.charWidthMap.put('~', 6);
		this.charWidthMap.put('\n', 0);

		final int defaultWidth = ApplicationProperties.getIntProp("book.default_char_width");
		for (char c = 32; c < 127; c++) {
			if (!this.charWidthMap.containsKey(c)) {
				this.charWidthMap.put(c, defaultWidth);
			}
		}
	}
}
