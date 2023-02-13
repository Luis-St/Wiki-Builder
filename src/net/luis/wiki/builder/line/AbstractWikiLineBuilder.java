package net.luis.wiki.builder.line;

import net.luis.wiki.builder.AbstractWikiBuilder;
import net.luis.wiki.file.WikiFileBuilder;

public abstract class AbstractWikiLineBuilder<T extends AbstractWikiLineBuilder<T>> extends AbstractWikiBuilder<T> {
	
	protected AbstractWikiLineBuilder(WikiFileBuilder fileBuilder) {
		super(fileBuilder);
	}
	
}
