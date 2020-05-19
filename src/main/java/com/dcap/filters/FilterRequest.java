package com.dcap.filters;

import java.util.List;


/**
 * Used to send the filter requests to the worker
 *
 * @author uli
 */
public class FilterRequest {
	/**
	 * id of all files that should be filtered
	 */
	private List<Long> files;

	/**
	 * list of all filters that should be applied
	 */
	private List<IDataFilter> filters;


	private String decimalSeparator;


	public FilterRequest(List<Long> files, List<IDataFilter> filters, String decimalSeparator) {
		this.files = files;
		this.filters = filters;
		this.decimalSeparator = decimalSeparator;
	}

	public FilterRequest() {
		// JSON
	}

	public String getDecimalSeparator() {
		return decimalSeparator;
	}

	public List<Long> getFiles() {
		return files;
	}

	public void setDecimalSeparator(String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}

	public void setFiles(List<Long> files) {
		this.files = files;
	}

	public void setFilters(List<IDataFilter> filters) {
		this.filters = filters;
	}

	public List<IDataFilter> getFilter() {
		return filters;
	}


}
