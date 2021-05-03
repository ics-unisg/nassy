package com.dcap.analyzer;

import org.apache.commons.math3.stat.descriptive.UnivariateStatistic;
import java.util.Map;


/**
 * Class that saves the baseline for a timeframe and each column
 * returns the mean over all baslinevalues for a given method
 *
 * @author uli
 */
public class BaselineValues {
	/**
	 * baseline for each column
	 */
	private final Map<String, Double> mean;

	/**
	 * statistic for the mean
	 */
	private final UnivariateStatistic statistic;

	/**
	 * name of the timeslot
	 */
	private final String name;

	public BaselineValues(Map<String, Double> mean, UnivariateStatistic method, String name) {
		this.mean=mean;
		statistic = method;
        this.name = name;
    }

	public Map<String, Double> getMean() {
		return mean;
	}

	public double getMeanOverAll(){
		double[] allMeans= mean.entrySet().stream().map(e -> (double) e.getValue()).mapToDouble(Double::doubleValue).toArray();
		double evaluate = statistic.evaluate(allMeans);
		return evaluate;
	}
}
