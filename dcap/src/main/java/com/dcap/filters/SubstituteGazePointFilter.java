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
 * Class from Cheetah_Web1.0
 *
 * @author uli (modified file)
 *
 */

public class SubstituteGazePointFilter extends AbstractChangingFilter {

	private static final String MISSING_COLUMN = "GazePointMissing";
	private static final String MISSING_LEFT = "left";
	private static final String MISSING_RIGHT = "right";
	private static final String MISSING_BOTH = "both";
	private String leftPupilGazeXName;
	private String leftPupilGazeYName;
	private String rightPupilGazeXName;
	private String rightPupilGazeYName;

	private static final Map<String, ENUMERATED_TYPES> PARAMETERS;

	static{
			PARAMETERS=new HashMap<>();
			PARAMETERS.put("leftPupilGazeXName", ENUMERATED_TYPES.String);
			PARAMETERS.put("leftPupilGazeYName", ENUMERATED_TYPES.String);
			PARAMETERS.put("rightPupilGazeXName", ENUMERATED_TYPES.String);
			PARAMETERS.put("rightPupilGazeYName", ENUMERATED_TYPES.String);
			PARAMETERS.put("columns", ENUMERATED_TYPES.InParameter);

	}

	public SubstituteGazePointFilter(String leftPupilGazeXName, String leftPupilGazeYName, String rightPupilGazeXName, String rightPupilGazeYName, Map<String, String> columns,  Map<String, String> parameter) {
		super("Gaze Point Substitution", columns, parameter);
			this.leftPupilGazeXName = leftPupilGazeXName;
			this.leftPupilGazeYName = leftPupilGazeYName;
			this.rightPupilGazeXName = rightPupilGazeXName;
			this.rightPupilGazeYName = rightPupilGazeYName;
		}
	public SubstituteGazePointFilter(Map<String, String> columns, Map<String, String> parameter) {
		this(parameter.get("leftPupilGazeXName"), parameter.get("leftPupilGazeYName"), parameter.get("rightPupilGazeXName"), parameter.get("rightPupilGazeYName"), columns, parameter );
	}

	public SubstituteGazePointFilter() {
		super(null, null, null);
	}


	private boolean isMissing(String x, String y) {
		return x.trim().isEmpty() || y.trim().isEmpty();
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
			DataFileColumn leftPupilGazeX = file.getHeader().getColumn(leftPupilGazeXName);
			DataFileColumn leftPupilGazeY = file.getHeader().getColumn(leftPupilGazeYName);
			DataFileColumn rightPupilGazeX = file.getHeader()
					.getColumn(rightPupilGazeXName);
			DataFileColumn rightPupilGazeY = file.getHeader()
					.getColumn(rightPupilGazeYName);

			DataFileColumn missingColumn = null;
			if (file.hasColumn(MISSING_COLUMN)) {
				missingColumn = file.getHeader().getColumn(MISSING_COLUMN);
			} else {
				missingColumn = file.appendColumn(MISSING_COLUMN);
				setColumnsCountChanged(true);
			}

			long substituted = 0;

			for (DataFileLine line : file.getContent()) {
				String leftX = line.get(leftPupilGazeX);
				String leftY = line.get(leftPupilGazeY);
				String rightX = line.get(rightPupilGazeX);
				String rightY = line.get(rightPupilGazeY);
				boolean leftMissing = isMissing(leftX, leftY);
				boolean rightMissing = isMissing(rightX, rightX);

				if (leftMissing && !rightMissing) {
					line.setValue(leftPupilGazeX, rightX);
					line.setValue(leftPupilGazeY, rightY);
					substituted++;
					line.setValue(missingColumn, MISSING_LEFT);
				} else if (!leftMissing && rightMissing) {
					line.setValue(rightPupilGazeX, leftX);
					line.setValue(rightPupilGazeY, leftY);
					substituted++;
					line.setValue(missingColumn, MISSING_RIGHT);
				} else if (leftMissing && rightMissing) {
					line.setValue(missingColumn, MISSING_BOTH);
				}
			}

			String columnsChanged = "";
			if (isColumnCountChanged()) {
				columnsChanged = "; Column Count has changed";
			}

			String returnMessage = getName() + " (Substituted " + substituted + " gaze point values" + columnsChanged + ")";
			FilterData filterData1 = new FilterData(file, filterData.getName(), filterData.getUserData(), filterData.getMessage() + "; " + returnMessage);
			returnList.add(filterData1);
		}
		return  returnList;
	}
}
