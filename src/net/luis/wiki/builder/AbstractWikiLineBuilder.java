package net.luis.wiki.builder;

import net.luis.wiki.file.WikiFileBuilder;

public abstract class AbstractWikiLineBuilder<T extends AbstractWikiLineBuilder<T>> extends AbstractWikiBuilder<T> {

	protected AbstractWikiLineBuilder(WikiFileBuilder fileBuilder) {
		super(fileBuilder);
	}

}
