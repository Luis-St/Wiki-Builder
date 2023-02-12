package net.luis.wiki.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.luis.wiki.WikiFormat;
import net.luis.wiki.file.WikiFileBuilder;

public abstract class AbstractWikiBuilder<T extends AbstractWikiBuilder<T>> implements WikiBuilder<T> {
	
	private final WikiFileBuilder fileBuilder;
	private final List<String> lines;
	private boolean end;
	
	protected AbstractWikiBuilder(WikiFileBuilder fileBuilder) {
		this.fileBuilder = fileBuilder;
		this.lines = new ArrayList<>();
		this.lines.add("");
	}
	
	protected int getLineCount() {
		return this.lines.size();
	}
	
	protected boolean isInNewLine(int lineIndex) {
		if (this.lines.size() > lineIndex && lineIndex >= 0) {
			return this.lines.get(lineIndex).isEmpty();
		}
		return false;
	}
	
	protected int getCurrentLine() {
		return Math.max(this.lines.size() - 1, 0);
	}
	
	protected boolean isLineEnd() {
		return this.lines.get(this.getCurrentLine()).endsWith("\\");
	}
	
	protected int getLineToAppend() {
		int currentLine = this.getCurrentLine();
		if (!this.isLineEnd()) {
			return currentLine;
		} else if (!this.isEnd()) {
			this.endLine();
			return currentLine + 1;
		}
		return -1;
	}
	
	protected void forceNewLine() {
		if (!this.isInNewLine(this.getCurrentLine())) {
			this.endLine();
		}
	}
	
	protected String getLinePrefix() {
		return "";
	}
	
	@SuppressWarnings("unchecked")
	protected final T append(String string, boolean space) {
		if (string != null && !string.isEmpty()) {
			if (this.isEnd()) {
				throw new IllegalStateException("Can not append \"" + (string.isEmpty() ? "empty" : string) + "\", since the builder has already been end");
			}
			int lineToAppend = this.getLineToAppend();
			if (this.lines.size() > lineToAppend && lineToAppend >= 0) {
				if (this.isInNewLine(lineToAppend)) {
					this.lines.set(lineToAppend, this.getLinePrefix() + string);
				} else {
					String line = this.lines.get(lineToAppend);
					if (space && !line.isEmpty()) {
						line += " ";
					}
					line += string;
					this.lines.set(lineToAppend, line);
				}
			}
		}
		return (T) this;
	}
	
	private String excludeSpecialCharacters(String string) {
		return string.replace("#", "\\#").replace("*", "\\*").replace("_", "\\_").replace("~", "\\~").replace("`", "\\`").replace(">", "\\>").replace("-", "\\-");
	}
	
	@Override
	public T append(String string) {
		string = this.excludeSpecialCharacters(string);
		return this.append(string, true);
	}
	
	@Override
	public T append(Object object) {
		String string = this.excludeSpecialCharacters(object.toString());
		return this.append(string, true);
	}
	
	@Override
	public T appendFormatted(String string, WikiFormat format) {
		string = this.excludeSpecialCharacters(string);
		return this.append(Objects.requireNonNull(format, "WikiFormat can not be null").formate(string), true);
	}
	
	@Override
	public T appendFormatted(Object object, WikiFormat format) {
		String string = this.excludeSpecialCharacters(object.toString());
		return this.append(Objects.requireNonNull(format, "WikiFormat can not be null").formate(string), true);
	}
	
	public boolean isEnd() {
		return this.end;
	}
	
	public void endLine() {
		String line = this.lines.get(this.getCurrentLine());
		if (line.endsWith("\\")) {
			throw new IllegalStateException("The line \"" + line + "\" can not end with \\");
		}
		this.append("\\", false);
		this.lines.add("");
	}
	
	protected List<String> modifyLines(List<String> lines) {
		return lines;
	}
	
	protected boolean shouldRemoveLineEnd() {
		return false;
	}
	
	@Override
	public void end() {
		if (!this.isEnd()) {
			if (!this.isLineEnd()) {
				this.append("\\", false);
			}
			this.fileBuilder.appendLines(this.modifyLines(this.lines), this.shouldRemoveLineEnd());
			this.end = true;
		}
	}
	
}
