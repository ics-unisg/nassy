package com.dcap.filters;

import org.apache.commons.math3.stat.descriptive.UnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class used to store timelsots in TEPR experiments
 *
 * @author uli
 */
public class InnerTimeSlot {

    private String nameOfTrial;
    private LinkedHashMap<Long, Double> preMap;
    private LinkedHashMap<Long, Double> resMap;
    private Long startPre;
    private Long endPre;
    private Long startRes;
    private Long endRes;

    private Double baseline;

    public InnerTimeSlot(String nameOfTrial) {
        this.nameOfTrial=nameOfTrial;
        this.preMap= new LinkedHashMap<>();
        this.resMap= new LinkedHashMap<>();
    }

    public void addPre(Long aLong, double value) {
        this.preMap.put(aLong, value);
        this.endPre=aLong;
        if(this.startPre==null){
            this.startPre=aLong;
        }
    }

    public void addRes(Long aLong, double value) {
        this.resMap.put(aLong, value);
        this.endRes=aLong;
        if(this.startRes==null){
            this.startRes=aLong;
        }
    }

    public void deleteDiliation(Long period){

        LinkedHashMap<Long,Double> resultMap = new LinkedHashMap<>();
        this.resMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey() >= this.startRes + period)
                .forEach(entry -> resultMap.put(entry.getKey(), entry.getValue()));
        this.resMap=resultMap;



    }

    public void baselineAndApplication(Long period){
        deleteDiliation(period);
        calculateBaseline();
        applyBaseLine();
    }

    private void applyBaseLine() {
        LinkedHashMap<Long, Double> newResultMap = new LinkedHashMap<>();
        for(Map.Entry<Long, Double> entry:this.resMap.entrySet()){
            newResultMap.put(entry.getKey(),entry.getValue()-this.baseline);
        }
        this.resMap=newResultMap;
    }

    private void calculateBaseline() {
        double[] preArray = this.preMap.values().stream().mapToDouble(Double::valueOf).toArray();
        this.baseline = new Mean().evaluate(preArray);
    }

    public Double applyMeasure(UnivariateStatistic method) {
        double[] values = this.resMap.values().stream().mapToDouble(Double::valueOf).toArray();
        double resulteOfStatistics = method.evaluate(values);
        return resulteOfStatistics;
    }

    public String getName() {
        return this.nameOfTrial;
    }

    public Double getBaseline() {
        return this.baseline;
    }
}
