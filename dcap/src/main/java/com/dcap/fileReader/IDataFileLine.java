package com.dcap.fileReader;


/**
 * Class from Cheetah_Web1.0
 * modification by uli
 *
 */
public interface IDataFileLine {

	String get(int columnNumber);

	String get(DataFileColumn column);

	String getString(String separator);

	int size();
}