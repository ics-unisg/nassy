package com.dcap.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileHeader;
import com.dcap.fileReader.DataFileLine;
import com.dcap.service.threads.FilterData;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileHeader;
import com.dcap.fileReader.DataFileLine;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;


/**
 * Class from Cheetah_Web1.0
 *
 * @author uli (modified file)
 *
 */
public class StandardDeviatonFilter extends AbstractChangingFilter {

	public StandardDeviatonFilter(Map<String, String> columns, Map<String, String> parameters) {
		super("Standard Deviation", columns, parameters);
	}

	private static final Map<String, ENUMERATED_TYPES> PARAMETERS;

	static {
		PARAMETERS = new HashMap<>();
		PARAMETERS.put("columns", ENUMERATED_TYPES.List);

	}

    public StandardDeviatonFilter() {
		super(null, null, null);
    }


    private String cleanPupil(DataFile file, DataFileColumn column) throws IOException {
		double[] pupils = getColumnlValues(file, column);
		double standardDeviation = new StandardDeviation().evaluate(pupils);
		double mean = new Mean().evaluate(pupils);
		double upperBound = mean + 3 * standardDeviation;
		double lowerBound = mean - 3 * standardDeviation;
		int numberRemovedValues = 0;
		List<DataFileLine> content = file.getContent();
		for (DataFileLine line : content) {
			if (line.isEmpty(column)) {
				continue;
			}
			double value = line.getDouble(column);
			if (value < lowerBound || value > upperBound) {
				line.setValue(column, 0);
				numberRemovedValues++;
			}
		}

		StringBuilder resultBuilder = new StringBuilder();
		resultBuilder.append(column.getName());
		resultBuilder.append(": ");
		resultBuilder.append("M=");
		resultBuilder.append(mean);
		resultBuilder.append(", ");
		resultBuilder.append("SD=");
		resultBuilder.append(standardDeviation);
		resultBuilder.append(", ");
		resultBuilder.append("Lo=");
		resultBuilder.append(lowerBound);
		resultBuilder.append(", ");
		resultBuilder.append("Hi=");
		resultBuilder.append(upperBound);
		resultBuilder.append(", ");
		resultBuilder.append("Removed=");
		resultBuilder.append(numberRemovedValues);
		resultBuilder.append(";");

		return resultBuilder.toString();
	}


	@Override
	public Map<String, ENUMERATED_TYPES> getRequiredParameters() {
		return PARAMETERS;
	}

	@Override
	public List<FilterData> run(List<FilterData> data) throws Exception {
		data=copyList(data);
		ArrayList<FilterData> returnList = new ArrayList<>();
		for(FilterData filterData: data) {
			DataFile file = filterData.getDataFile();
			String result = "";
			DataFileHeader header = file.getHeader();
			for (String column : getColumns().values()) {
				DataFileColumn columnToProcess = getColumn(column, header);
				result += cleanPupil(file, columnToProcess);

			}
			String columnsChanged = "";
			if (isColumnCountChanged()) {
				columnsChanged = "; Column Count has changed";
			}

			String returnMessage= getName() + " (" + result + columnsChanged + ")";
			FilterData filterData1 = new FilterData(file, filterData.getName(), filterData.getUserData(), filterData.getMessage() + "; " + returnMessage);
			returnList.add(filterData1);
		}
		return  returnList;
	}
}
