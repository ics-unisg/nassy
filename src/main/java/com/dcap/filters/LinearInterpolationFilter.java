package com.dcap.filters;

import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileLine;
import com.dcap.fileReader.DataFileUtils;
import com.dcap.service.threads.FilterData;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileLine;
import com.dcap.fileReader.DataFileUtils;
import com.dcap.helper.FileException;
import com.dcap.helper.FilterException;
import com.dcap.service.threads.FilterData;

import java.io.IOException;
import java.util.*;

/**
 * Linear interpolation of missing values.
 *
 * @author Stefan
 *
 * @author uli (modification file)
 */
public class LinearInterpolationFilter extends AbstractChangingFilter {

	private static final Map<String, ENUMERATED_TYPES> PARAMETERS;

	static {
		PARAMETERS = new HashMap<>();
		PARAMETERS.put("columns", ENUMERATED_TYPES.List);

	}

	public LinearInterpolationFilter(Map<String, String> columns, Map<String, String> parameters) {
		super("Linear Interpolation", columns, parameters);
	}

    public LinearInterpolationFilter() {
		super(null, null, null);
    }

    /**
	 * Linearly interpolate an array of values.
	 *
	 * @param values
	 *            the values to be interpolated
	 *
	 * @return the amount of replaced values
	 */
	private int interpolate(double[] values, DataFile file, DataFileColumn column) throws IOException {
		int replaced = 0;

		for (int i = 0; i < values.length; i++) {
			double value = values[i];
			if (value == 0.0) {
				double start = 0;
				if (i != 0) {
					start = values[i - 1];
				}

				double end = 0;
				int endIndex = 0;
				for (int j = i; j < values.length; j++) {
					if (values[j] != 0.0) {
						end = values[j];
						endIndex = j;
						break;
					}
				}

				double difference = end - start;
				double stepSize = difference / (endIndex - i);
				double current = start;
				for (int j = i; j < endIndex; j++) {
					values[j] = current;
					current += stepSize;
					replaced++;
				}
			}
		}

		Iterator<DataFileLine> iterator = file.getContent().iterator();
		int index = 0;
		while (iterator.hasNext()) {
			DataFileLine line = iterator.next();
			line.setValue(column, values[index]);
			index++;
		}

		return replaced;
	}

	@Override
	public Map<String, ENUMERATED_TYPES> getRequiredParameters() {
		return PARAMETERS;
	}

	@Override
	public List<FilterData> run(List<FilterData> data) throws IOException, FileException {
		data=copyList(data);
		ArrayList<FilterData> returnList = new ArrayList<>();
		for(FilterData filterData: data) {
			DataFile file = filterData.getDataFile();
			int replaced = 0;
			Map<String, String> columns = getColumns();
			for (String columnName : columns.values()) {
				DataFileColumn columnn = file.getHeader().getColumn(columnName);
				double[] values = DataFileUtils.getDoubleValues(file, columnn, true);
				replaced += interpolate(values, file, columnn);

			}
			;

			String columnsChanged = "";
			if (isColumnCountChanged()) {
				columnsChanged = "; Column Count has changed";
			}
			String returnMessage = getName() + " (" + replaced + " values replaced" + columnsChanged + ")";
			FilterData filterData1 = new FilterData(file, filterData.getName(), filterData.getUserData(), filterData.getMessage() + "; " + returnMessage);
			returnList.add(filterData1);
		}
		return  returnList;
	}
}
