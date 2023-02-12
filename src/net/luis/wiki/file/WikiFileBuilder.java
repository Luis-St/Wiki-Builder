package net.luis.wiki.file;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.luis.wiki.WikiFormat;
import net.luis.wiki.WikiList;
import net.luis.wiki.builder.WikiHeaderBuilder;
import net.luis.wiki.builder.WikiListBuilder;
import net.luis.wiki.builder.WikiMultiLineBuilder;
import net.luis.wiki.builder.WikiQuoteBuilder;
import net.luis.wiki.builder.WikiSingleLineBuilder;

public class WikiFileBuilder {
	
	private final String name;
	private final List<String> lines;
	
	public WikiFileBuilder(String name) {
		this.name = name;
		this.lines = new ArrayList<>();
	}
	
	private int getCurrentLine() {
		return Math.max(this.lines.size() - 1, 0);
	}
	
	public void header1(String string) {
		this.header(1).append(string).end();
	}
	
	public void header2(String string) {
		this.header(2).append(string).end();
	}
	
	public void header3(String string) {
		this.header(3).append(string).end();
	}
	
	public WikiHeaderBuilder header(int header) {
		return new WikiHeaderBuilder(this, header);
	}
	
	public void line(String string) {
		new WikiSingleLineBuilder(this).append(string).end();
	}
	
	public void line(Object object) {
		new WikiSingleLineBuilder(this).append(object).end();
	}
	
	public void line(Consumer<WikiSingleLineBuilder> consumer) {
		WikiSingleLineBuilder builder = new WikiSingleLineBuilder(this);
		consumer.accept(builder);
		builder.end();
	}
	
	public void formattedLine(String string, WikiFormat format) {
		new WikiSingleLineBuilder(this).appendFormatted(string, format).end();
	}
	
	public void formattedLine(Object object, WikiFormat format) {
		new WikiSingleLineBuilder(this).appendFormatted(object, format).end();
	}
	
	public WikiMultiLineBuilder lines() {
		return new WikiMultiLineBuilder(this);
	}
	
	public void lines(Consumer<WikiMultiLineBuilder> consumer) {
		WikiMultiLineBuilder builder = this.lines();
		consumer.accept(builder);
		builder.end();
	}
	
	public void emptyLine() {
		this.line("");
	}
	
	public WikiListBuilder numberList() {
		return this.list(WikiList.NUMBER);
	}
	
	public WikiListBuilder pointList() {
		return this.list(WikiList.POINT);
	}
	
	public WikiListBuilder list(WikiList list) {
		return new WikiListBuilder(this, list);
	}
	
	public void list(WikiList list, Consumer<WikiListBuilder> consumer) {
		WikiListBuilder builder = this.list(list);
		consumer.accept(builder);
		builder.end();
	}
	
	public WikiQuoteBuilder quote() {
		return new WikiQuoteBuilder(this);
	}
	
	public void quote(Consumer<WikiQuoteBuilder> consumer) {
		WikiQuoteBuilder builder = this.quote();
		consumer.accept(builder);
		builder.end();
	}
	
	public void appendLines(List<String> lines, boolean removeLineEnd) {
		if (removeLineEnd && !this.lines.isEmpty()) {
			String line = this.lines.get(this.getCurrentLine());
			if (line.endsWith("\\")) {
				this.lines.set(this.getCurrentLine(), line.replace("\\", ""));
			}
		}
		this.lines.addAll(lines);
	}
	
	public void print() {
		this.lines.forEach(System.out::println);
	}
	
	public void write(Path path) throws IOException {
		path = path.resolve(this.name + ".md");
		if (!Files.exists(path)) {
			Files.createDirectories(path.getParent());
			Files.createFile(path);
		}
		FileWriter writer = new FileWriter(path.toFile());
		for (int i = 0; i < this.lines.size(); i++) {
			String line = this.lines.get(i);
			if (i == this.lines.size() - 1) {
				writer.write(line.substring(0, line.length() - 1) + "\n");
			} else {
				writer.write(line + "\n");
			}
		}
		writer.flush();
		writer.close();
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
