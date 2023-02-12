package net.luis.wiki;

public enum WikiFormat {
	
	BOLD("**", "**"),
	ITALIC("*", "*"),
	CROSSED_OUT("~~", "~~"),
	BOLD_ITALIC("***", "***"),
	CODE("`", "`"),
	EXCLUDE("\\", "");
	
	private final String prefix;
	private final String suffix;
	
	private WikiFormat(String prefix, String suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
	}
	
	public String formate(String string) {
		return this.prefix + string + this.suffix;
	}
	
}
