package com.dcap.filters;

import java.io.IOException;
import java.util.*;

import com.dcap.fileReader.*;
import com.dcap.helper.DoubleColumnException;
import com.dcap.service.threads.FilterData;
import com.dcap.fileReader.*;
import com.dcap.helper.FileException;
import com.dcap.helper.FilterException;
import com.dcap.service.threads.FilterData;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 * Class from Cheetah_Web1.0
 *
 * @author uli (modified file)
 *
 */

public class BlinkDetectionFilter extends AbstractChangingFilter {
	public static final String BLINK_COLUMN = "BlinkPedrotti";
	public static final String BLINK_LEFT = "left";
	public static final String BLINK_RIGHT = "right";
	public static final String BLINK_BOTH = "both";

	private static final String BLINK_LEFT_MARKING = "BLINK_LEFT";
	private static final String BLINK_RIGHT_MARKING = "BLINK_RIGHT";
	private static final String DELETED_LEFT_MARKING = "DELETED_LEFT";
	private static final String ARTIFACT_LEFT_MARKING = "ARTIFACT_LEFT";
	private static final String DELETED_RIGHT_MARKING = "DELETED_RIGHT";
	private static final String ARTIFACT_RIGHT_MARKING = "ARTIFACT_RIGHT";

	private String leftPupilGazeXColumnName;
	private String leftPupilGazeYColumnName;
	private String rightPupilGazeXColumnName;
	private String rightPupilGazeYColumnName;
	private String blinkDetectionTimeThreashold;
	private  String left_pupil="";
	private  String right_pupil="";


	private static final Map<String, ENUMERATED_TYPES> PARAMETERS;


	static {
		PARAMETERS = new HashMap<>();
		PARAMETERS.put("leftPupilGazeXColumnName", ENUMERATED_TYPES.String);
		PARAMETERS.put("leftPupilGazeYColumnName", ENUMERATED_TYPES.String);
		PARAMETERS.put("rightPupilGazeXColumnName", ENUMERATED_TYPES.String);
		PARAMETERS.put("rightPupilGazeYColumnName", ENUMERATED_TYPES.String);
		PARAMETERS.put("blinkDetectionTimeThreashold", ENUMERATED_TYPES.String);
		PARAMETERS.put("left_pupil", ENUMERATED_TYPES.String);
		PARAMETERS.put("right_pupil", ENUMERATED_TYPES.String);
		PARAMETERS.put("timestampcolumn", ENUMERATED_TYPES.String);
		PARAMETERS.put("columns", ENUMERATED_TYPES.InParameter);

	}



	public BlinkDetectionFilter(String leftPupilGazeXColumnName, String leftPupilGazeYColumnName, String rightPupilGazeXColumnName, String rightPupilGazeYColumnName, String blinkDetectionTimeThreashold, Map<String, String> columns, String left_pupil, String right_pupil, Map<String, String> parameters) {
		super("Blink Detection, Pedrotti (2011)", columns, parameters);
		this.leftPupilGazeXColumnName = leftPupilGazeXColumnName;
		this.leftPupilGazeYColumnName = leftPupilGazeYColumnName;
		this.rightPupilGazeXColumnName = rightPupilGazeXColumnName;
		this.rightPupilGazeYColumnName = rightPupilGazeYColumnName;
		this.blinkDetectionTimeThreashold = blinkDetectionTimeThreashold;
		this.left_pupil=left_pupil;
		this.right_pupil=right_pupil;
		this.timeStampColumn=getTimeStampColumn();
	}


	public BlinkDetectionFilter(Map<String, String> columns, Map<String, String> parameter) {
		this(parameter.get("leftPupilGazeXColumnName"), parameter.get("leftPupilGazeYColumnName"), parameter.get("rightPupilGazeXColumnName"), parameter.get("rightPupilGazeYColumnName"), parameter.get("blinkDetectionTimeThreashold"), columns ,parameter.get("left_pupil"), parameter.get("right_pupil"), parameter);
	}

	public BlinkDetectionFilter() {
		super(null, null, null);
	}

	private boolean alignData(DataFile file, DataFileColumn pupilColumn, DataFileColumn gazeXColumn,
                              DataFileColumn gazeYColumn) throws IOException {
		LinkedList<DataFileLine> content = file.getContent();
		ListIterator<DataFileLine> iterator = content.listIterator();

		boolean changed = false;
		while (iterator.hasNext()) {
			DataFileLine current = iterator.next();
			if (current.isEmpty(pupilColumn)) {
				continue;
			}

			if (iterator.hasPrevious()) {
				DataFileLine previous = iterator.previous();
				if (previous.isEmpty(pupilColumn)) {
					if (isInvalidGazePoint(current, gazeXColumn, gazeYColumn)) {
						current.deleteValue(pupilColumn);
						changed = true;
					}
				}

				iterator.next();
			}

			if (iterator.hasNext()) {
				DataFileLine next = iterator.next();
				if (next.isEmpty(gazeYColumn)) {
					if (isInvalidGazePoint(current, gazeXColumn, gazeYColumn)) {
						current.deleteValue(pupilColumn);
						changed = true;
					}
				}
				iterator.previous();
			}
		}

		return changed;
	}

	private void cleanPupil(DataFile file, DataFileColumn leftPupilColumn, DataFileColumn leftPupilGazeXColumn,
			DataFileColumn leftPupilGazeYColumn, String deletedMarking, String artifactMarking) throws IOException {

		while (true) {
			boolean changed = false;
			changed = markArtifacts(file, leftPupilColumn, artifactMarking) || changed;
			changed = removeArtifacts(file, leftPupilColumn, artifactMarking, deletedMarking) || changed;
			changed = alignData(file, leftPupilColumn, leftPupilGazeXColumn, leftPupilGazeYColumn) || changed;
			changed = validateData(file, leftPupilColumn, leftPupilGazeXColumn, leftPupilGazeYColumn) || changed;

			if (!changed) {
				break;
			}
		}
	}

	/**
	 * Copy the markings to a new column for persisting them.
	 *
	 * @param file
	 * @throws IOException
	 */
	private void copyMarks(DataFile file) throws IOException {
		DataFileColumn blinkColumn = null;
		try {
			blinkColumn = file.appendColumn(BLINK_COLUMN);
		} catch (DoubleColumnException e) {
			e.printStackTrace();
		}
		setColumnsCountChanged(true);

		for (DataFileLine line : file.getContent()) {
			boolean isLeftBlink = line.isMarked(BLINK_LEFT_MARKING);
			boolean isRightBlink = line.isMarked(BLINK_RIGHT_MARKING);

			if (isLeftBlink && !isRightBlink) {
				line.setValue(blinkColumn, BLINK_LEFT);
			} else if (!isLeftBlink && isRightBlink) {
				line.setValue(blinkColumn, BLINK_RIGHT);
			} else if (isLeftBlink && isRightBlink) {
				line.setValue(blinkColumn, BLINK_BOTH);
			}
		}
	}


	private boolean isInvalidGazePoint(DataFileLine line, DataFileColumn gazeXColumn, DataFileColumn gazeYColumn) {
		if (line.isEmpty(gazeYColumn) || line.isEmpty(gazeXColumn)) {
			return true;
		}

		double relativeGazePointX = 0;
		double relativeGazePointY = 0;

		// gaze points may be relative (0 to 1) or absolute - handle both cases
		try {
			double gazePointX = line.getInteger(gazeXColumn);
			double gazePointY = line.getInteger(gazeYColumn);
			relativeGazePointX = gazePointX / WebConstants.DISPLAY_SIZE_X;
			relativeGazePointY = gazePointY / WebConstants.DISPLAY_SIZE_Y;
		} catch (NumberFormatException e) {
			relativeGazePointX = line.getDouble(gazeXColumn);
			relativeGazePointY = line.getDouble(gazeYColumn);
		}

		return relativeGazePointX < 0 || relativeGazePointX > 1 || relativeGazePointY < 0 || relativeGazePointY > 1;
	}

	private boolean markArtifacts(DataFile file, DataFileColumn column, String artifactMarking) throws IOException {
		double[] pupils = getColumnlValues(file, column);
		double standardDeviation = new StandardDeviation().evaluate(pupils);
		double mean = new Mean().evaluate(pupils);
		double upperBound = mean + 3 * standardDeviation;
		double lowerBound = mean - 3 * standardDeviation;

		boolean changes = false;
		List<DataFileLine> lines = file.getContent();
		for (DataFileLine line : lines) {
			if (line.isEmpty(column) || line.isMarked(artifactMarking)) {
				continue;
			}

			Double value = line.getDouble(column);
			if (value < lowerBound || value > upperBound) {
				line.mark(artifactMarking);
				changes = true;
			}
		}

		return changes;
	}

	private boolean removeArtifacts(DataFile file, DataFileColumn leftPupilColumn, String artifactMarking,
			String deletedMarking) throws IOException {
		boolean changed = false;
		LinkedList<DataFileLine> content = file.getContent();
		ListIterator<DataFileLine> iterator = content.listIterator();
		while (iterator.hasNext()) {
			DataFileLine current = iterator.next();
			if (!current.isEmpty(leftPupilColumn)) {
				continue;
			}
			if (iterator.hasPrevious()) {
				DataFileLine previous = iterator.previous();
				if (previous.isEmpty(leftPupilColumn)) {
					if (previous.isMarked(artifactMarking) && (!previous.isMarked(deletedMarking))) {
						previous.deleteValue(leftPupilColumn);
						previous.mark(deletedMarking);
						changed = true;
					}
				}
				iterator.next();
			}

			if (iterator.hasNext()) {
				DataFileLine next = iterator.next();
				if (next.isMarked(artifactMarking) && (!next.isMarked(deletedMarking))) {
					next.deleteValue(leftPupilColumn);
					next.mark(deletedMarking);
					changed = true;
				}
				iterator.previous();
			}
		}
		return changed;
	}

	private int removeBlinks(DataFile file, DataFileColumn leftPupilColumn, DataFileColumn timestampColumn,
			long threshold, String blinkMarking) throws IOException {
		LinkedList<DataFileLine> content = file.getContent();
		ListIterator<DataFileLine> iterator = content.listIterator();
		int blinks = 0;

		while (iterator.hasNext()) {
			DataFileLine current = iterator.next();
			// some additional info is logged without timestamp
			if (current.isEmpty(timestampColumn)) {
				continue;
			}
			if (current.isMarked(blinkMarking)) {
				continue;
			}

			if (current.isEmpty(leftPupilColumn)) {
				blinks++;
				current.mark(blinkMarking);
				long firstEmtpy = current.getLong(timestampColumn);

				while (iterator.hasPrevious()) {
					DataFileLine previous = iterator.previous();
					if (previous.isEmpty(timestampColumn)) {
						continue;
					}

					if (previous.getLong(timestampColumn) > firstEmtpy - threshold) {
						previous.mark(blinkMarking);
						previous.deleteValue(leftPupilColumn);
					} else {
						break;
					}
				}

				long lastEmpty = firstEmtpy;
				while (iterator.hasNext()) {
					DataFileLine next = iterator.next();
					if (next.isEmpty(timestampColumn)) {
						continue;
					}
					if (next.isEmpty(leftPupilColumn)) {
						lastEmpty = next.getLong(timestampColumn);
					}

					long nextTimestamp = next.getLong(timestampColumn);
					if (nextTimestamp < lastEmpty + threshold) {
						next.mark(blinkMarking);
						next.deleteValue(leftPupilColumn);
					} else {
						break;
					}
				}
			}
		}

		return blinks;
	}

	@Override
	public Map<String, ENUMERATED_TYPES> getRequiredParameters() {
		return PARAMETERS;
	}

	@Override
	public List<FilterData> run(List<FilterData> data) throws IOException, FileException {
		ArrayList<FilterData> returnList = new ArrayList<>();
		data=copyList(data);
		for(FilterData filterData: data) {
			DataFile file = filterData.getDataFile();
			DataFileHeader header = file.getHeader();

/*			if (getColumns().size() != 2) {
//				return null;
				System.err.println("Not two collumns in BlinkDetection");
				//TODO Maybe an exception
			}*/
			DataFileColumn leftPupilColumn = file.getHeader().getColumn(left_pupil);
			DataFileColumn rightPupilColumn = file.getHeader().getColumn(right_pupil);

			DataFileColumn leftPupilGazeXColumn = header
					.getColumn(leftPupilGazeXColumnName);
			if (leftPupilGazeXColumn == null) {

			}
			DataFileColumn leftPupilGazeYColumn = header
					.getColumn(leftPupilGazeYColumnName);
			DataFileColumn rightPupilGazeXColumn = header
					.getColumn(rightPupilGazeXColumnName);
			DataFileColumn rightPupilGazeYColumn = header
					.getColumn(rightPupilGazeYColumnName);
			DataFileColumn timestampColumn = header.getColumn(timeStampColumn);

			cleanPupil(file, leftPupilColumn, leftPupilGazeXColumn, leftPupilGazeYColumn, DELETED_LEFT_MARKING, ARTIFACT_LEFT_MARKING);
			cleanPupil(file, rightPupilColumn, rightPupilGazeXColumn, rightPupilGazeYColumn, DELETED_RIGHT_MARKING, ARTIFACT_RIGHT_MARKING);

			String parameter = blinkDetectionTimeThreashold;
			long threshold = Long.parseLong(parameter) * 1000;

			int blinksLeft = removeBlinks(file, leftPupilColumn, timestampColumn, threshold, BLINK_LEFT_MARKING);
			int blinksRight = removeBlinks(file, rightPupilColumn, timestampColumn, threshold, BLINK_RIGHT_MARKING);

			copyMarks(file);
			String columnsChanged = "";
			if (isColumnCountChanged()) {
				columnsChanged = "; Column Count has changed";
			}
			String returnMessage=getName() +" (Blinks left: " + blinksLeft + "; Blinks Right: " + blinksRight+columnsChanged+")";

			FilterData filterData1 = new FilterData(file, filterData.getName(), filterData.getUserData(), filterData.getMessage() + "; " + returnMessage);
			returnList.add(filterData1);
		}
		return  returnList;

	}

	private boolean validateData(DataFile file, DataFileColumn column, DataFileColumn gazeXColumn,
			DataFileColumn gazeYColumn) throws IOException {
		boolean changed = false;
		LinkedList<DataFileLine> content = file.getContent();
		ListIterator<DataFileLine> iterator = content.listIterator();
		while (iterator.hasNext()) {
			DataFileLine current = iterator.next();
			if (current.isEmpty(column)) {
				continue;
			}

			if (current.isEmpty(gazeXColumn) || current.isEmpty(gazeYColumn)) {
				current.deleteValue(column);
				changed = true;
				continue;
			}

			if (isInvalidGazePoint(current, gazeXColumn, gazeYColumn)) {
				current.deleteValue(column);
				changed = true;
			}
		}

		return changed;
	}
}
