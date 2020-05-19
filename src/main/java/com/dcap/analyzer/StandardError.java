package com.dcap.analyzer;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.descriptive.UnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 * Class from Cheetah_Web1.0
 *
 */
public class StandardError implements UnivariateStatistic {

	@Override
	public UnivariateStatistic copy() {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public double evaluate(double[] values) throws MathIllegalArgumentException {
		StandardDeviation standardDeviation = new StandardDeviation();
		double rootOfN = Math.sqrt(values.length);

		return standardDeviation.evaluate(values) / rootOfN;
	}

	@Override
	public double evaluate(double[] arg0, int arg1, int arg2) throws MathIllegalArgumentException {
		throw new UnsupportedOperationException("not implemented");
	}

}
