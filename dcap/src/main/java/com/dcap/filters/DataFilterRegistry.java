package com.dcap.filters;

import com.dcap.helper.MyLittleFactory;
import com.dcap.service.Exceptions.AbstractFilterException;
import com.dcap.service.Exceptions.NoSuchFilterException;

import java.util.List;

/**
 * Class that holds methods to get all available Filters
 *
 * @author uli
 *
 */
public class DataFilterRegistry {

	/**
	 *
	 * @param id id of the filter
 	 * @return filter for id
	 * @throws AbstractFilterException
	 * @throws NoSuchFilterException
	 */
	public static IDataFilter getFilter(String id) throws AbstractFilterException, NoSuchFilterException {
		IDataFilter filter = MyLittleFactory.getFilter(id, null, null);
		return filter;
	}

	/**
	 *
	 * @return list of all filters
	 */
	public static List<String> getListOfFilters(){
		List<String> listOfAllFilters = MyLittleFactory.getListOfAllFilters();
		return  listOfAllFilters;
	}

}
