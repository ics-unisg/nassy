package com.dcap.filters;

import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileHeader;
import com.dcap.fileReader.DataFileUtils;
import com.dcap.service.threads.FilterData;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileHeader;
import com.dcap.fileReader.DataFileUtils;
import com.dcap.helper.FileException;
import com.dcap.service.threads.FilterData;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * Abstract class for all data filters
 * @author uli
 *
 */
public abstract class AbstractDataFilter implements IDataFilter {




	/**
	 * name of the filter
	 */
	private String name;

	protected String timeStampColumn;

	protected Map<String, String> parameter;

	/**
	 * map that contains the list of all strings that must be considerated, value is the name of the column in the experiment, the
	 * key is the name of the column in the csv file
	 */
	private Map<String, String> columns;


	/**
	 * Constructor for the AbstractDataFilter
	 * @param name name of the filter
	 * @param columns map that contains the list of all strings that must be considerated, value is the name of the column in the experiment, the
	 * key is the name of the column in the csv file
	 */
	public AbstractDataFilter(String name, Map<String, String> columns, Map<String, String>parameter) {
		this.name = name;
		this.columns=columns;
		this.parameter=parameter;
	}



	/**
	 * function that return the columns that are the input for the filter. String is the name of the column how it should be callec,
	 * the value is the name of the column in the csv
	 * @return map with filters <name of the column in the experiment, name of the column in the csv file>
	 */
	@Override
	public Map<String, String> getColumns(){return columns;}



	/**
	 * function to receive a column of the csv file
	 * @param columname name of the column in the csv file
	 * @param header header of the {@link DataFile}
	 * @return the {@link DataFileColumn} with the name
	 */
	public DataFileColumn getColumn(String columname, DataFileHeader header) throws FileException {
		return header.getColumn(columname);

	}

	/**
	 * function that returns the name of the Filter
	 * @return mame
	 */
	@Override
	public String getName() {
		return name;
	}


	/**
	 * function to get the values of a {@link DataFileColumn} as double[]
	 * @param file datafile that is the source
	 * @param column datafilecolumn of interest
	 * @return array of doubles with that are the content of the column
	 * @throws IOException
	 */
	public double[] getColumnlValues(DataFile file, DataFileColumn column) throws IOException {
		return DataFileUtils.getDoubleValues(file, column, false);
	}

	/**
	 * get the datafilecolumn of the timestampcolumnt
	 * @param timeStampColumn name of the timestampcolumn in the file
	 * @param header header of the datafile
	 * @return datafilecolumn of the timestampcolumn
	 */
	public DataFileColumn getTimestampColumn(String timeStampColumn, DataFileHeader header) throws FileException {
		return header.getColumn(timeStampColumn);
	}

	public List<FilterData> copyList(List<FilterData> listToCopy) throws IOException {
		/*ArrayList<FilterData> filterDataList = new ArrayList<>();
		for(FilterData fd: listToCopy){
			DataFile dataFile = fd.getDataFile().copyFile();
			FilterData filterDataNew = new FilterData(dataFile, fd.getName(), fd.getUserData(), fd.getMessage());
			filterDataList.add(filterDataNew);

		}
		return filterDataList;*/
		return listToCopy;
	}

	@Override
	public String getTimeStampColumn(){
		return parameter.get("timestampcolumn");
	}

}
