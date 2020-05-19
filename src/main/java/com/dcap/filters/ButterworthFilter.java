package com.dcap.filters;


import biz.source_code.dsp.filter.*;
import com.dcap.fileReader.*;
import com.dcap.service.threads.FilterData;
import com.dcap.fileReader.*;
import com.dcap.service.threads.FilterData;

import java.io.IOException;
import java.util.*;

/**
 * Butterworth filter cleaning; with correction for phase shift (formula see resources/butterworth.pdf).
 *
 *  Class from Cheetah_Web1.0
 *
 * @author Stefan
 *
 * @author uli (modified file)
 *
 */
public class ButterworthFilter extends AbstractChangingFilter {

	private Double hertz;
	private Double sampleRate;

	private static final Map<String, ENUMERATED_TYPES> PARAMETERS;

	static {
		PARAMETERS = new HashMap<>();
		PARAMETERS.put("hertz", ENUMERATED_TYPES.Double);
		PARAMETERS.put("sampleRate", ENUMERATED_TYPES.Double);
		PARAMETERS.put("timestampcolumn", ENUMERATED_TYPES.String);
		PARAMETERS.put("columns", ENUMERATED_TYPES.List);
		PARAMETERS.put("Applies filter to all columns that are named", ENUMERATED_TYPES.AddidtionalInfo);


	}

	public ButterworthFilter( Map<String, String> columns,Map<String, String> parameter) {
		super("Butterworth", columns,parameter);
		this.hertz = Double.valueOf(parameter.get("hertz"));
		this.sampleRate = Double.valueOf(parameter.get("sampleRate"));
		this.timeStampColumn=getTimeStampColumn();
	}

    public ButterworthFilter() {
		super(null, null, null);

	}

    private void applyFilter(DataFile file, IirFilter filter, DataFileColumn pupilColumn,
                             DataFileColumn timestampColumn, double phaseShiftInMs) throws IOException {
		double[] values = DataFileUtils.getDoubleValues(file, pupilColumn, true);
		LinkedList<DataFileLine> content = file.getContent();
		Iterator<DataFileLine> iterator = content.iterator();
		int index = 0;

		// shift the values by the phase response
		long startTimestamp = content.get(0).getLong(timestampColumn);
		long currentTimestamp = content.get(0).getLong(timestampColumn);
		while (iterator.hasNext()) {
			DataFileLine current = iterator.next();
			currentTimestamp = current.getLong(timestampColumn);
			current.deleteValue(pupilColumn);
			filter.step(values[index]);
			index++;

			if (currentTimestamp - startTimestamp > -phaseShiftInMs * 1000) {
				break;
			}
		}

		// filter the remaining values
		iterator = content.iterator();
		while (iterator.hasNext() && index < values.length) {
			double filtered = filter.step(values[index]);
			DataFileLine element = iterator.next();
			element.setValue(pupilColumn, filtered);
			index++;
		}

		// fill remaining values with empty values
		while (iterator.hasNext()) {
			iterator.next().deleteValue(pupilColumn);
		}
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
			IirFilterCoefficients butterworth = IirFilterDesignFisher.design(FilterPassType.lowpass, FilterCharacteristicsType.butterworth, 3,
					0, hertz / sampleRate, 0);
			IirFilter filter = new IirFilter(butterworth);

			double omega = 2 * hertz * Math.PI;
			double tmp1 = 2 * omega - Math.pow(omega, 3);
			double tmp2 = 1 - 2 * Math.pow(omega, 2);
			double phaseShift = -Math.atan(tmp1 / tmp2);

			double relativePhaseShift = phaseShift / (2 * Math.PI);
			double timeShiftInMs = relativePhaseShift * (1 / hertz) * 1000;

			DataFileHeader header = file.getHeader();
			DataFileColumn timestampColumn = header.getColumn(timeStampColumn);

			for (String columnName : getColumns().values()) {
				DataFileColumn column = header.getColumn(columnName);
				applyFilter(file, filter, column, timestampColumn, timeShiftInMs);

			}
			String columnsChanged = "";
			if (isColumnCountChanged()) {
				columnsChanged = "; Column Count has changed";
			}

			String returnMessage= getName() + "( lowpass, " + hertz + " Hz, " + sampleRate + " Hz Samplerate" + columnsChanged + ")";	FilterData filterData1 = new FilterData(file, filterData.getName(), filterData.getUserData(), filterData.getMessage() + "; " + returnMessage);
			returnList.add(filterData1);
		}
		return  returnList;

	}

}
