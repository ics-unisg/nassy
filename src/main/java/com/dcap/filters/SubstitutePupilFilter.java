package com.dcap.filters;


import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileLine;
import com.dcap.service.threads.FilterData;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileLine;
import com.dcap.service.threads.FilterData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Filter that copies data of left to right pupil (and vice versa) in case one value is missing.
 *
 * @author Stefan
 *
 * @author uli (modified file)
 */
public class SubstitutePupilFilter extends AbstractChangingFilter {

	private static final String MISSING_COLUMN = "MissingValue";
	private static final String MISSING_LEFT = "left";
	private static final String MISSING_RIGHT = "right";
	private static final String MISSING_BOTH = "both";
	private  String left_pupil="";
	private  String right_pupil="";


	private static final Map<String, ENUMERATED_TYPES> PARAMETERS;

	static {
		PARAMETERS = new HashMap<>();
		PARAMETERS.put("left_pupil", ENUMERATED_TYPES.String);
		PARAMETERS.put("right_pupil", ENUMERATED_TYPES.String);
		PARAMETERS.put("columns", ENUMERATED_TYPES.InParameter);
	}



	/**
	 * Constructor for the SubstitutePupilFilter
	 * @param columns columns of left and right pupil. Be aware that the first entry is the left pupil, while the second is the right pupil
	 */
	public SubstitutePupilFilter(Map<String, String> columns, Map<String, String> parameters) {
		super("Pupil Substitution", columns, parameters);
		left_pupil=parameters.get("left_pupil");
		right_pupil=parameters.get("right_pupil");
	}

	public SubstitutePupilFilter() {
		super(null, null, null);
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
			DataFileColumn leftPupil = file.getHeader().getColumn(left_pupil);
			DataFileColumn rightPupil = file.getHeader().getColumn(right_pupil);

			DataFileColumn missingColumn = null;
			if (file.hasColumn(MISSING_COLUMN)) {
				missingColumn = file.getHeader().getColumn(MISSING_COLUMN);
			} else {
				missingColumn = file.appendColumn(MISSING_COLUMN);
				setColumnsCountChanged(true);
			}

			int replaced = 0;
			for (DataFileLine line : file.getContent()) {
				String left = line.get(leftPupil);
				String right = line.get(rightPupil);
				boolean leftMissing = left.trim().isEmpty();
				boolean rightMissing = right.trim().isEmpty();

				if (leftMissing && !rightMissing) {
					line.setValue(leftPupil, right);
					replaced++;
					line.setValue(missingColumn, MISSING_LEFT);
				} else if (!leftMissing && rightMissing) {
					line.setValue(rightPupil, left);
					replaced++;
					line.setValue(missingColumn, MISSING_RIGHT);
				} else if (leftMissing && rightMissing) {
					line.setValue(missingColumn, MISSING_BOTH);
				}
			}
			String columnsChanged = "";
			if (isColumnCountChanged()) {
				columnsChanged = "; Column Count has changed";
			}

			String returnMessage = getName() + " (Substituted " + replaced + " values" + columnsChanged + ")";
			FilterData filterData1 = new FilterData(file, filterData.getName(), filterData.getUserData(), filterData.getMessage() + "; " + returnMessage);
			returnList.add(filterData1);
		}
		return  returnList;
	}

}
