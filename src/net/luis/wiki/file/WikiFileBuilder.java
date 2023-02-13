package net.luis.wiki.file;

import net.luis.wiki.WikiFormat;
import net.luis.wiki.WikiList;
import net.luis.wiki.builder.WikiHeaderBuilder;
import net.luis.wiki.builder.WikiListBuilder;
import net.luis.wiki.builder.WikiQuoteBuilder;
import net.luis.wiki.builder.WikiTableBuilder;
import net.luis.wiki.builder.line.WikiMultiLineBuilder;
import net.luis.wiki.builder.line.WikiSingleLineBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
		new WikiHeaderBuilder(this, 1).append(string).end();
	}
	
	public void header2(String string) {
		new WikiHeaderBuilder(this, 2).append(string).end();
	}
	
	public void header3(String string) {
		new WikiHeaderBuilder(this, 3).append(string).end();
	}
	
	public void header(int header, String string) {
		new WikiHeaderBuilder(this, header).append(string).end();
	}
	
	public void line(String string) {
		new WikiSingleLineBuilder(this).append(string).end();
	}
	
	public void line(Object object) {
		new WikiSingleLineBuilder(this).append(object).end();
	}
	
	public void formattedLine(String string, WikiFormat format) {
		new WikiSingleLineBuilder(this).appendFormatted(string, format).end();
	}
	
	public void formattedLine(Object object, WikiFormat format) {
		new WikiSingleLineBuilder(this).appendFormatted(object, format).end();
	}
	
	public void lines(Consumer<WikiMultiLineBuilder> consumer) {
		WikiMultiLineBuilder builder = new WikiMultiLineBuilder(this);
		consumer.accept(builder);
		builder.end();
	}
	
	public void emptyLine() {
		this.line("");
	}
	
	public void numberList(Consumer<WikiListBuilder> consumer) {
		WikiListBuilder builder = new WikiListBuilder(this, WikiList.NUMBER);
		consumer.accept(builder);
		builder.end();
	}
	
	public void pointList(Consumer<WikiListBuilder> consumer) {
		WikiListBuilder builder = new WikiListBuilder(this, WikiList.POINT);
		consumer.accept(builder);
		builder.end();
	}
	
	public void quote(Consumer<WikiQuoteBuilder> consumer) {
		WikiQuoteBuilder builder = new WikiQuoteBuilder(this);
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
	
	public void table(Consumer<WikiTableBuilder> consumer) {
		WikiTableBuilder builder = new WikiTableBuilder(this);
		consumer.accept(builder);
		builder.end();
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
