package net.luis.wiki;

/**
 *
 * @author Luis-st
 *
 */

public enum WikiAlignment {
	
	LEFT(":", ""),
	CENTER(":", ":"),
	RIGHT("", ":");
	
	private final String prefix;
	private final String suffix;
	
	WikiAlignment(String prefix, String suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
	}
	
	public String create(int charCount) {
		return this.prefix + "-".repeat(charCount - this.prefix.length() - this.suffix.length()) + this.suffix;
	}
	
}
