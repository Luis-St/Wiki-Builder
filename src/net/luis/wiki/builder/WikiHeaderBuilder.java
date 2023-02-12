package net.luis.wiki.builder;

import java.util.ArrayList;
import java.util.List;

import net.luis.wiki.file.WikiFileBuilder;

public class WikiHeaderBuilder extends AbstractWikiBuilder<WikiHeaderBuilder> {
	
	protected final int header;
	
	public WikiHeaderBuilder(WikiFileBuilder fileBuilder, int header) {
		super(fileBuilder);
		this.header = header;
		if (0 >= this.header) {
			throw new IllegalArgumentException("Header must be larger than 0");
		}
	}
	
	@Override
	protected String getLinePrefix() {
		String prefix = "";
		for (int i = 0; i < this.header; i++) {
			prefix += "#";
		}
		return prefix + " ";
	}
	
	@Override
	protected List<String> modifyLines(List<String> lines) {
		List<String> newLines = new ArrayList<>();
		for (String line : lines) {
			if (line.endsWith("\\")) {
				line = line.substring(0, line.length() - 1);
			}
			newLines.add(line);
		}
		return newLines;
	}
	
	@Override
	public void endLine() {
		super.endLine();
		this.end();
	}
	
	@Override
	protected boolean shouldRemoveLineEnd() {
		return true;
	}
	
}
