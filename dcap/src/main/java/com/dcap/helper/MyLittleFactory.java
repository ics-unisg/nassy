package com.dcap.helper;

import com.dcap.analyzer.StandardError;
import com.dcap.domain.Pythoncode;
import com.dcap.filters.CustomizedChangeFilterTemplate;
import com.dcap.filters.CustomizedTrimFilterTemplate;
import com.dcap.filters.ENUMERATED_TYPES;
import com.dcap.filters.IDataFilter;
import com.dcap.analyzer.StandardError;
import com.dcap.domain.Pythoncode;
import com.dcap.filters.*;
import com.dcap.service.Exceptions.AbstractFilterException;
import com.dcap.service.Exceptions.NoSuchFilterException;
import com.dcap.service.PythonCodeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.stat.descriptive.UnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * Class to handle the management of the possible statistics and filters
 *
 * @author uli
 */
@Service
public class MyLittleFactory {


    private static Set<BeanDefinition> classes;

    final PythonCodeService pythonCodeService;
    private static PythonCodeService myPythonCodeService;

    static{
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*Filter")));
        classes = provider.findCandidateComponents("com.dcap.filters");
    }

    @Autowired
    public MyLittleFactory(PythonCodeService pythonCodeService) {
        this.pythonCodeService = pythonCodeService;
    }

    @PostConstruct
    public void init() {
        myPythonCodeService = pythonCodeService;
    }

    /**
     * Functino to select a statistic
     *
     * @param stat name of the statistic
     * @return the wanted statistic
     */
    public static UnivariateStatistic getStatistic(String stat) {
        switch (stat) {
            case "mean":
                return new Mean();
            case "median":
                return new Median();
            case "min":
                return new Min();
            case "max":
                return new Max();
            case "standDev":
                return new StandardDeviation();
            case "standErr":
                return new StandardError();
            default:
                return null;
        }

    }



    public static IDataFilter getFilter(String name, Map<String, String> columns, Map<String, String> actualParameters) throws NoSuchFilterException, AbstractFilterException {



        for (BeanDefinition bean : classes) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(bean.getBeanClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String clazzName = clazz.getName();
            String filterName = StringUtils.substringAfterLast(clazzName, ".");
            if (filterName.endsWith(name)) {
                if (filterName.startsWith("Abstract")) {
                    throw new AbstractFilterException(name);
                }
                IDataFilter iDataFilter = null;
                try {
                    if(columns==null && actualParameters==null){
                        iDataFilter = (IDataFilter) clazz.getConstructor().newInstance();
                    }else{
                        iDataFilter = (IDataFilter) clazz.getConstructor(Map.class, Map.class).newInstance(columns, actualParameters);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return iDataFilter;
            }
        }
        Pythoncode pythonCodeElement = myPythonCodeService.getPythonCodeElement(name);
        if(pythonCodeElement!=null){
            String type = pythonCodeElement.getType();
            if(type!=null && type.equals("trim")){
                return new CustomizedTrimFilterTemplate(pythonCodeElement, columns, actualParameters);
            }else{
                return new CustomizedChangeFilterTemplate(pythonCodeElement, columns, actualParameters);
            }
        }


        throw new NoSuchFilterException(name);
    }


    /**
     * Function to the required parameters for a given filter
     * @param name of the filter
     * @return parameters, their name and their type.
     * @throws NoSuchFilterException if there is no such filter
     * @throws AbstractFilterException if the filter is abstract
     */
    public static Map<String, ENUMERATED_TYPES> getFilterParameters(String name) throws
            NoSuchFilterException, AbstractFilterException {

        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*Filter")));
        final Set<BeanDefinition> classes = provider.findCandidateComponents("com.dcap.filters");

        for (BeanDefinition bean : classes) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(bean.getBeanClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String clazzName = clazz.getName();
            String filterName = StringUtils.substringAfterLast(clazzName, ".");
            if (filterName.endsWith(name)) {
                if (filterName.startsWith("Abstract")) {
                    throw new AbstractFilterException(name);
                }
                IDataFilter iDataFilter = null;
                try {
                    iDataFilter = (IDataFilter) clazz.getConstructor().newInstance();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return iDataFilter.getRequiredParameters();
            }
        }
        throw new NoSuchFilterException(name);
    }


    /**
     * Method to retrieve a list of the names of all available filters
     * @return list of filters
     */
    public static List<String> getListOfAllFilters() {
        ArrayList<String> namesList = new ArrayList<>();

        List<String> listOfAllImplementedFilters = getListOfAllImplementedFilters();
        List<String> allListedPythonFilterNames = myPythonCodeService.getAllListedPythonFilterNames();

        namesList.addAll(listOfAllImplementedFilters);
        namesList.addAll(allListedPythonFilterNames);

        return namesList;
    }

        public static List<String> getListOfAllImplementedFilters() {
        ArrayList<String> filters = new ArrayList<>();
        for (BeanDefinition bean : classes) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(bean.getBeanClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String clazzName = clazz.getName();
            String filterName = StringUtils.substringAfterLast(clazzName, ".");
            if (filterName.startsWith("Abstract")) {
                continue;
            }
            filters.add(filterName);
        }
        return filters;
    }
}
