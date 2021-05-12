package com.dcap.analyzer;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import java.util.*;


/**
 * The type Time frame.
 * @author uli
 */
public class TimeFrame {
	/**
	 * The start time
	 */
	private long start;
	/**
	 * The end time
	 */
	private long end;


	private String label;
	private Map<String, LinkedList<Double>> columns;

	private HashMap<String, LinkedList<Double>> columnsBaseLineCorrected;
	private ArrayList<Double> meanValuesOfTimeframe;
	private ArrayList<Double> meanValuesOfTimeframeBaseLineCorrected;



	public TimeFrame(long start, long end, String label) {
		super();
		this.start = start;
		this.end = end;
		this.label = label;
		this.columns= new HashMap<>();
	}

	public TimeFrame(String label) {
		super();
		this.label = label;
		this.columns= new HashMap<>();
	}



	public String getLabel() {
		return label;
	}


	public boolean matchesTimeFrame(long timestamp) {
		return start <= timestamp && end >= timestamp;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void add(String columName, Double value) {
		if(!columns.containsKey(columName)){
			LinkedList<Double> list = new LinkedList<>();
			list.add(value);
			columns.put(columName,list);
		}else{
			columns.get(columName).add(value);
		}
	}

	public double[] getArray(String columName){
		LinkedList<Double> array = columns.get(columName);
		double[] arrayToReturn = array.stream().mapToDouble(x -> x).toArray();
		return arrayToReturn;

	}

	public Map<String, LinkedList<Double>> getColumns() {
		return columns;
	}

	public void setColumns(Map<String, LinkedList<Double>> columns) {
		this.columns = columns;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public void prepareSlots(BaselineValues baseline){
		meanValuesOfTimeframe = new ArrayList<>();
		meanValuesOfTimeframeBaseLineCorrected = new ArrayList<>();
		columnsBaseLineCorrected = new HashMap<>();
		Set<Map.Entry<String, LinkedList<Double>>> entriesList = this.getColumns().entrySet();
		int size = entriesList.iterator().next().getValue().size();
		HashMap<Integer, List<Double>> allEntriesTogether0 = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ArrayList<Double> doubles = new ArrayList<>();
			allEntriesTogether0.put(i, doubles);
		}

		int index = 0;
		for (Map.Entry<String, LinkedList<Double>> entry : entriesList) { //getting out every column
			LinkedList<Double> column = entry.getValue(); //getting the values
			if (baseline != null) {
				LinkedList<Double> baselineCorrectedColumn = new LinkedList<>();
				columnsBaseLineCorrected.put(entry.getKey(), baselineCorrectedColumn);
			}
			for (int i = 0; i < column.size(); i++) {
				allEntriesTogether0.get(i).add(column.get(i));

				if (baseline != null) {
					Double baseLineValue = baseline.getMean().get(entry.getKey());
					double newValue = column.get(i) - baseLineValue;
					columnsBaseLineCorrected.get(entry.getKey()).add(newValue);
				}
			}
		}
		index++;
		for(Map.Entry<Integer, List<Double>>  entry: allEntriesTogether0.entrySet()){
			double evaluate = new Mean().evaluate(entry.getValue().stream().mapToDouble(x -> x).toArray());
			meanValuesOfTimeframe.add(evaluate);
			if (baseline != null) {
				evaluate = evaluate - baseline.getMeanOverAll();
				meanValuesOfTimeframeBaseLineCorrected.add(evaluate);
			}
		}
	}

	public Set<Map.Entry<String,LinkedList<Double>>> getColumnsBaseLineCorrected(BaselineValues baseline) {
		if(columnsBaseLineCorrected == null){
			prepareSlots(baseline);
		}
		return columnsBaseLineCorrected.entrySet();
	}

	public ArrayList<Double> getMeanValuesOfTimeframe(BaselineValues baseline) {
		if(meanValuesOfTimeframe == null){
			prepareSlots(baseline);
		}
		return meanValuesOfTimeframe;
	}

	public ArrayList<Double> getMeanValuesOfTimeframeBaseLineCorrected(BaselineValues baseline) {
		if(meanValuesOfTimeframeBaseLineCorrected == null){
			prepareSlots(baseline);
		}
		return meanValuesOfTimeframeBaseLineCorrected;
	}
}
