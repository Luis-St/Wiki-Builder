package net.luis.wiki.builder;

import net.luis.wiki.file.WikiFileBuilder;

import java.util.ArrayList;
import java.util.List;

public class WikiQuoteBuilder extends AbstractWikiBuilder<WikiQuoteBuilder> {
	
	public WikiQuoteBuilder(WikiFileBuilder fileBuilder) {
		super(fileBuilder);
	}
	
	@Override
	protected String getLinePrefix() {
		return "> ";
	}
	
	@Override
	protected List<String> modifyLines(List<String> lines) {
		List<String> newLines = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.endsWith("\\")) {
				line = line.substring(0, line.length() - 1);
			}
			if (i == lines.size() - 1) {
				if (!line.replace(this.getLinePrefix(), "").trim().isEmpty()) {
					newLines.add(line);
				}
			} else {
				newLines.add(line);
			}
		}
		newLines.add("");
		return newLines;
	}
	
	@Override
	protected boolean shouldRemoveLineEnd() {
		return true;
	}
	
}
