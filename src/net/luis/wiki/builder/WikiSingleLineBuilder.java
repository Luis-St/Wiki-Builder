package net.luis.wiki.builder;

import java.util.ArrayList;
import java.util.List;

import net.luis.wiki.file.WikiFileBuilder;

public class WikiSingleLineBuilder extends AbstractWikiLineBuilder<WikiSingleLineBuilder> {
	
	public WikiSingleLineBuilder(WikiFileBuilder fileBuilder) {
		super(fileBuilder);
	}
	
	@Override
	public void endLine() {
		super.endLine();
		this.end();
	}
	
	@Override
	protected List<String> modifyLines(List<String> lines) {
		List<String> newLines = new ArrayList<>();
		newLines.add(lines.get(0));
		return newLines;
	}
	
}
