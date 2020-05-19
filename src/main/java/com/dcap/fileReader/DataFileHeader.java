package com.dcap.fileReader;

import com.dcap.helper.FileException;
import com.dcap.helper.FilterException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class from Cheetah_Web1.0
 * modification by uli
 *
 */
public class DataFileHeader implements IDataFileLine {
	private Map<String, DataFileColumn> columns;

	public DataFileHeader() {
		columns = new LinkedHashMap<String, DataFileColumn>();
	}

	public DataFileColumn appendColumn(String name) {
		if (columns.containsKey(name)) {
			throw new RuntimeException("Column " + name + " is already present");
		}

		DataFileColumn column = new DataFileColumn(name, columns.size());
		columns.put(name, column);
		return column;
	}

	@Override
	public String get(int columnNumber) {
		DataFileColumn column = getColumns().get(columnNumber);
		if (column.getColumnNumber() != columnNumber) {
			throw new IllegalStateException("Illegal column index.");
		}

		return column.getName();
	}

	@Override
	public String get(DataFileColumn column) {
		return column.getName();
	}

	public DataFileColumn getColumn(String columnName) throws FileException {
		DataFileColumn column = columns.get(columnName);
		if(column==null){
			throw new FileException("Could not find " +columnName);
		}
		return column;
	}

	public List<DataFileColumn> getColumns() {
		return new ArrayList<DataFileColumn>(columns.values());
	}

	@Override
	public String getString(String separator) {
		StringBuilder builder = new StringBuilder();
		for (DataFileColumn csvColumn : getColumns()) {
			if (builder.length() > 0) {
				builder.append(separator);
			}
			builder.append(csvColumn.getName());
		}
		return builder.toString();
	}

	public boolean hasColumn(String column) {
		return columns.containsKey(column);
	}

	@Override
	public int size() {
		return columns.size();
	}

	private void addData(String strOne, String strTwo, Integer number ){
		this.columns.put(strOne, new DataFileColumn(strTwo, number));
	}

	public DataFileHeader copyHeader(){
		DataFileHeader dataFileHeader = new DataFileHeader();
		for(Map.Entry<String, DataFileColumn> me: columns.entrySet()){
			dataFileHeader.addData(me.getKey(), me.getValue().getName(), me.getValue().getColumnNumber());
		}
		return  dataFileHeader;
	}
}
