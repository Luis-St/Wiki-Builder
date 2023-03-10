package net.luis.wiki.builder.line;

import net.luis.wiki.file.WikiFileBuilder;

import java.util.ArrayList;
import java.util.List;

public class WikiMultiLineBuilder extends AbstractWikiLineBuilder<WikiMultiLineBuilder> {
	
	public WikiMultiLineBuilder(WikiFileBuilder fileBuilder) {
		super(fileBuilder);
	}
	
	@Override
	protected List<String> modifyLines(List<String> lines) {
		List<String> newLines = new ArrayList<>();
		for (int i = 0; i < lines.size() - 1; i++) {
			newLines.add(lines.get(i));
		}
		return newLines;
	}
	
}
