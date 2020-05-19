package com.dcap.fileReader;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Class from Cheetah_Web1.0
 *  modification by uli
 *
 */
public class DataFileLine implements IDataFileLine {
	private List<String> content;
	private Map<String, Object> markings;
	private String decimalSeparator;

	public DataFileLine(String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
		content = new ArrayList<String>();
	}

	public void add(String toAdd) {
		content.add(toAdd);
	}

	/**
	 * Creates a copy of this line (omitting the markings).
	 *
	 * @return
	 */
	public DataFileLine copy() {
		DataFileLine copy = new DataFileLine(decimalSeparator);
		copy.content = new ArrayList<>(content);
		return copy;
	}

	public void deleteValue(DataFileColumn column) {
		setValue(column, "");
	}

	@Override
	public String get(int columnNumber) {
		return content.get(columnNumber);
	}

	@Override
	public String get(DataFileColumn column) {
		return content.get(column.getColumnNumber());
	}

	public Double getDouble(DataFileColumn column) {
		String value = get(column).replaceAll(",", ".");
		if (value == null || value.isEmpty()) {
			return null;
		}

		return Double.parseDouble(value);
	}

	public int getInteger(DataFileColumn column) {
		String value = get(column);
		return Integer.parseInt(value);
	}

	public Long getLong(DataFileColumn column) {
		String value = get(column);
		if(value.equals("NULL") || value.equals("null")){
			return null;
		}
		return Long.parseLong(value);
	}

	public String getStringOfColumn(DataFileColumn column) {
		return get(column);
	}

	public Object getMarking(String key) {
		if (markings == null) {
			return null;
		}

		return markings.get(key);
	}

	public Map<String, Object> getMarkings(){
		return markings;
	}

	@Override
	public String getString(String separator) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;

		for (String entry : content) {
			if (!first) {
				builder.append(separator);
			}

			first = false;
			builder.append(entry);
		}
		return builder.toString();
	}

	public boolean isEmpty(DataFileColumn column) {
		return get(column).trim().isEmpty();
	}

	public boolean isMarked(String marking) {
		if (markings == null) {
			return false;
		}

		return markings.containsKey(marking);
	}

	public void mark(String marking) {
		mark(marking, new Object());
	}

	public void mark(String marking, Object value) {
		if (this.markings == null) {
			this.markings = new HashMap<>();
		}

		this.markings.put(marking, value);
	}

	public void setValue(DataFileColumn column, double value) {
		setValue(column, String.valueOf(value).replaceAll("\\.", decimalSeparator));
	}

	public void setValue(DataFileColumn column, long value) {
		setValue(column, String.valueOf(value));
	}

	public void setValue(DataFileColumn column, String value) {
		int columnNumber = column.getColumnNumber();
		content.set(columnNumber, value);
	}

	@Override
	public int size() {
		return content.size();
	}

	@Override
	public String toString() {
		return content.toString();
	}

	public void unmark(String marking) {
		markings.remove(marking);

		// make the gc's life easier
		if (markings.isEmpty()) {
			markings = null;
		}
	}

	public void setMarkings(Map<String,Object> markings) {
		this.markings=markings;
	}


	/*
	This equals function igores the markings, because they are not stored on the disk
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DataFileLine)) return false;
		DataFileLine that = (DataFileLine) o;
		List<String> contentOThat = that.content;
		List<String> contentOfThis = this.content;
		if(contentOfThis.size()!=contentOThat.size()){
			return false;
		}
		for(int i = 0; i< content.size(); i++){

			if(contentOfThis.get(i)==null && contentOThat.get(i)==null){
				continue;
			}

			if(contentOfThis.get(i)==null || contentOThat.get(i)==null){
				return false;
			}

			String one= contentOfThis.get(i);
			String two = contentOThat.get(i);
			if(!one.equals(two) && !one.startsWith(two) && !two.startsWith(one)){
				System.err.println(contentOfThis.get(i) + " -- " + contentOThat.get(i));
				return  false;
			}
		}
		return Objects.equals(decimalSeparator, that.decimalSeparator);
	}

	@Override
	public int hashCode() {

		return Objects.hash(content, decimalSeparator);
	}

	public boolean isEqualTo(DataFileLine that, List<DataFileColumn> columnsThat , List<DataFileColumn> columnsThis, List<String> neglect) {
		boolean answer=true;
		for(DataFileColumn dfcThis:columnsThis){
			if(neglect!=null && neglect.contains(dfcThis.getName())){
				continue;
			}
			DataFileColumn dfcThat = columnsThat.stream().filter(e -> e.getName().equals(dfcThis.getName())).collect(Collectors.toList()).get(0);
			String thisValue = this.get(dfcThis);
			String thatValue = that.get(dfcThat);
			try {
				Double valueOfThis= Double.valueOf(thisValue.replaceAll(",", "."));
				Double valueOfThat = Double.valueOf(thatValue.replaceAll(",", "."));
				double diff = valueOfThat - valueOfThis;
				if(Math.abs(diff)>0.0000000000001){
				System.err.println(diff + " -- " + thisValue+ " -- " +thatValue + " " + dfcThat.getName() + " " + dfcThis.getName() );
                    return false;
                }
			}catch (NumberFormatException e){
				if(!thisValue.equals(thatValue)){
					return false;
				}
			}
		}
		return true;

	}
}
