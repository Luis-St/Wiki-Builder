package net.luis.wiki.builder;

import net.luis.wiki.WikiList;
import net.luis.wiki.file.WikiFileBuilder;

import java.util.ArrayList;
import java.util.List;

public class WikiListBuilder extends AbstractWikiBuilder<WikiListBuilder> {
	
	private final WikiList list;
	
	public WikiListBuilder(WikiFileBuilder fileBuilder, WikiList list) {
		super(fileBuilder);
		this.list = list;
	}
	
	private String getEntryPrefix(int lineNumber) {
		return switch (this.list) {
			case NUMBER -> {
				yield lineNumber + ". ";
			}
			case POINT -> {
				yield "- ";
			}
			default -> throw new IllegalArgumentException("Unexpected value: " + this.list);
		};
	}
	
	@Override
	protected String getLinePrefix() {
		return this.getEntryPrefix(this.getLineCount());
	}
	
	private boolean isListEntryEmpty(String line, int lineNumber) {
		return line.replace(this.getEntryPrefix(lineNumber), "").trim().isEmpty();
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
				if (!this.isListEntryEmpty(line, i + 1)) {
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
