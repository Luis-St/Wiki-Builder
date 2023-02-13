package net.luis.wiki.builder;

import net.luis.wiki.WikiAlignment;
import net.luis.wiki.WikiFormat;
import net.luis.wiki.file.WikiFileBuilder;

import java.util.*;

/**
 *
 * @author Luis-st
 *
 */

public class WikiTableBuilder extends AbstractWikiBuilder<WikiTableBuilder> {
	
	private final Table table = new Table();
	
	public WikiTableBuilder(WikiFileBuilder fileBuilder) {
		super(fileBuilder);
	}
	
	@Override
	protected WikiTableBuilder append(String string, boolean space) {
		if (this.isEnd()) {
			throw new IllegalStateException("Can not append \"" + string + "\", since the builder has already been end");
		}
		if (!string.isEmpty()) {
			this.table.append(string);
		
		}
		return this;
	}
	
	public WikiTableBuilder append(String string, boolean space, WikiAlignment alignment) {
		if (this.isEnd()) {
			throw new IllegalStateException("Can not append \"" + string + "\", since the builder has already been end");
		}
		if (!string.isEmpty()) {
			this.table.append(string, alignment);
		}
		return this;
	}
	
	public WikiTableBuilder append(String string, WikiAlignment alignment) {
		return this.append(string, true, alignment);
	}
	
	public WikiTableBuilder append(Object object, WikiAlignment alignment) {
		return this.append(object.toString(), alignment);
	}
	
	public WikiTableBuilder appendFormatted(String string, WikiFormat format, WikiAlignment alignment) {
		return this.append(Objects.requireNonNull(format, "WikiFormat must not be null").formate(string), true, alignment);
	}
	
	public WikiTableBuilder appendFormatted(Object object, WikiFormat format, WikiAlignment alignment) {
		return this.append(Objects.requireNonNull(format, "WikiFormat must not be null").formate(object.toString()), true, alignment);
	}
	
	@Override
	public void endLine() {
		this.table.endRow();
	}
	
	private int getColumnSize(int column) {
		int size = -1;
		for (String value : this.table.getColumn(column)) {
			size = Math.max(size, value.length());
		}
		return Math.max(size, 3);
	}
	
	private String centerString(String string, int size) {
		if (string.length() == size) {
			return string;
		}
		int remaining = size - string.length();
		if (remaining % 2 == 0) {
			return " ".repeat(remaining / 2) + string + " ".repeat(remaining / 2);
		} else {
			return " ".repeat(remaining / 2) + string + " ".repeat((remaining / 2) + 1);
		}
	}
	
	@Override
	public void end() {
		Map<Integer, StringBuilder> lines = new HashMap<>();
		for (int i = 0; i < this.table.getHeaders(); i++) {
			int columnSize = this.getColumnSize(i);
			lines.putIfAbsent(0, new StringBuilder("|"));
			lines.get(0).append(this.centerString(this.table.get(0, i), columnSize)).append("|");
			lines.putIfAbsent(1, new StringBuilder("|"));
			lines.get(1).append(this.table.getAlignment(i).create(columnSize)).append("|");
			for (int j = 1; j < this.table.getRows(); j++) {
				lines.putIfAbsent(j + 1, new StringBuilder("|"));
				lines.get(j + 1).append(this.centerString(this.table.get(j, i), columnSize)).append("|");
			}
		}
		this.fileBuilder.appendLines(lines.values().stream().map(StringBuilder::toString).toList(), true);
	}
	
	private class Table {
		
		private final Map<Integer, List<String>> table = new HashMap<>();
		private final Map<Integer, WikiAlignment> alignments = new HashMap<>();
		private int headers = 0;
		private int rows = 0;
		
		public void append(String string) {
			this.append(string, WikiAlignment.CENTER);
		}
		
		public void append(String string, WikiAlignment alignment) {
			if (this.rows == 0) {
				this.headers++;
			} else if (this.table.containsKey(this.rows) && this.table.get(this.rows).size() == this.headers) {
				this.endRow();
				this.append(string, alignment);
			}
			this.table.putIfAbsent(this.rows, new ArrayList<>());
			if (this.alignments.containsKey(this.table.get(this.rows).size())) {
				if (this.alignments.get(this.table.get(this.rows).size()) == WikiAlignment.CENTER) {
					this.alignments.putIfAbsent(this.table.get(this.rows).size(), Objects.requireNonNull(alignment, "The alignment must not be null"));
				}
			} else {
				this.alignments.putIfAbsent(this.table.get(this.rows).size(), Objects.requireNonNull(alignment, "The alignment must not be null"));
			}
			this.table.get(this.rows).add(Objects.requireNonNull(string, "The string must not be null"));
		}
		
		public int getHeaders() {
			return this.headers;
		}
		
		public int getRows() {
			return this.rows;
		}
		
		public String get(int row, int column) {
			return this.table.get(row).get(column);
		}
		
		public List<String> getRow(int row) {
			return this.table.get(row);
		}
		
		public List<String> getColumn(int column) {
			List<String> list = new ArrayList<>();
			for (List<String> row : this.table.values()) {
				list.add(row.get(column));
			}
			return list;
		}
		
		public WikiAlignment getAlignment(int column) {
			return this.alignments.get(column);
		}
		
		public void endRow() {
			if (this.rows != 0) {
				this.table.putIfAbsent(this.rows, new ArrayList<>());
				while (this.table.get(this.rows).size() < this.headers) {
					this.table.get(this.rows).add("");
				}
			}
			this.rows++;
		}
		
	}
	
}
