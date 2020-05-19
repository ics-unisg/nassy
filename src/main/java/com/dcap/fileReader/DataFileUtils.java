package com.dcap.fileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class from Cheetah_Web1.0
 * modification by uli
 *
 */
public class DataFileUtils {

	/**
	 * Get the pupil values
	 *
	 * @param lines
	 *            the lines to be processed
	 * @param column
	 *            the columns to be processed
	 * @param subsituteMissings
	 *            if <code>true</code> missing values are substituted with 0, if <code>false</code> missing values are ignored and not added
	 *            to the result
	 * @return the pupil values
	 * @throws IOException
	 */
	public static double[] getDoubleValues(List<DataFileLine> lines, DataFileColumn column, boolean subsituteMissings) {
		List<Double> pupils = new ArrayList<Double>();
		for (DataFileLine line : lines) {
			if (line.isEmpty(column)) {
				if (subsituteMissings) {
					pupils.add(0.0);
				}

				continue;
			}

			pupils.add(line.getDouble(column));
		}

		double[] result = new double[pupils.size()];
		for (int i = 0; i < pupils.size(); i++) {
			result[i] = pupils.get(i);
		}

		return result;
	}


	public static List<String> getRawValues(List<DataFileLine> lines, DataFileColumn column, boolean subsituteMissings) {
		List<String> values = new ArrayList<String>();
		for (DataFileLine line : lines) {
			if (line.isEmpty(column)) {
				if (subsituteMissings) {
					values.add("");
				}

				continue;
			}

			values.add(line.getStringOfColumn(column));
		}

		return values;
	}

	/**
	 * Get the pupil values
	 *
	 * @param file
	 *            the file to be processed
	 * @param column
	 *            the columns to be processed
	 * @param subsituteMissings
	 *            if <code>true</code> missing values are substituted with 0, if <code>false</code> missing values are ignored and not added
	 *            to the result
	 * @return
	 * @throws IOException
	 */
	public static double[] getDoubleValues(DataFile file, DataFileColumn column, boolean subsituteMissings)
			throws IOException {
		List<DataFileLine> content = file.getContent();
		return getDoubleValues(content, column, subsituteMissings);
	}


	public static List<String> getRawValues(DataFile file, DataFileColumn column, boolean subsituteMissings)
			throws IOException {
		List<DataFileLine> content = file.getContent();
		return getRawValues(content, column, subsituteMissings);
	}

	public static Long[] getTimeStamps(DataFile file, DataFileColumn column, boolean subsituteMissings) throws IOException {
		List<DataFileLine> content = file.getContent();
		return getLongValues(content, column, subsituteMissings);
	}

	/**
	 *
	 * @param lines
	 * @param column
	 * @param subsituteMissings
	 * @return
	 */
	private static Long[] getLongValues(List<DataFileLine> lines, DataFileColumn column, boolean subsituteMissings) {
		List<Long> pupils = new ArrayList<Long>();
		for (DataFileLine line : lines) {
			if (line.isEmpty(column)) {
				if (subsituteMissings) {
					pupils.add(0L);
				}

				continue;
			}


			pupils.add(line.getLong(column));
		}

		Long[] result = new Long[pupils.size()];
		for (int i = 0; i < pupils.size(); i++) {
			result[i] = pupils.get(i);
		}

		return result;
	}


	public static boolean dataFileEquality(DataFile one, DataFile two){
		List<IDataFileLine> linesOne = one.getLines();
		List<IDataFileLine> linesTwo = two.getLines();
		if(linesOne.size()!=linesTwo.size()){
			return false;
		}
		for(int i=0; i<linesOne.size(); i++) {
            IDataFileLine iDataFileLineOne = linesOne.get(i);
            IDataFileLine iDataFileLineTwo = linesTwo.get(i);
            if (iDataFileLineOne instanceof DataFileHeader) {
				if(!((DataFileHeader) iDataFileLineOne).getString(",").equals(((DataFileHeader) iDataFileLineTwo).getString(","))){
				    return false;
                }
                continue;
			}
			DataFileLine dataFileLineOne = (DataFileLine) iDataFileLineOne;
            DataFileLine dataFileLineTwo = (DataFileLine) iDataFileLineTwo;

			if (!dataFileLineOne.equals(dataFileLineTwo)) {
				return false;

			}
		}
		return true;
	}
}
