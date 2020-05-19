package com.dcap.fileReader;

import java.util.Objects;

/**
 * Class from Cheetah_Web1.0
 * some modification by uli
 *
 */
public class DataFileColumn {
	private String name;
	private int columnNumber;

	/**
	 * Instantiates a new Data file column.
	 *
	 * @param name         the name of the column
	 * @param columnNumber the column number
	 */
	public DataFileColumn(String name, int columnNumber) {
		this.name = name;
		this.columnNumber = columnNumber;
	}

	/**
	 * Gets column number.
	 *
	 * @return the column number
	 */
	public int getColumnNumber() {
		return columnNumber;
	}

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DataFileColumn)) return false;
		DataFileColumn that = (DataFileColumn) o;
		return columnNumber == that.columnNumber &&
				Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {

		return Objects.hash(name, columnNumber);
	}
}
