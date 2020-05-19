package com.dcap.analyzer;

import java.util.*;

/**
 * Class from Cheetah_Web1.0
 *
 * modified by uli
 *
 */
public class ReportableResult {

	private String identifier;

	private Map<String, List<ReportableResultEntry>> results;

	public ReportableResult(String identifier) {
		this.identifier = identifier;
		results = new HashMap<>();
	}



	public void addResult(String key, ReportableResultEntry entry) {
		if (!results.containsKey(key)) {
			results.put(key, new ArrayList<ReportableResultEntry>());
		}

		results.get(key).add(entry);
	}

	public void addResults(String key, List<ReportableResultEntry> entries) {
		if (!results.containsKey(key)) {
			results.put(key, new ArrayList<ReportableResultEntry>());
		}

		results.get(key).addAll(entries);
	}



	public Set<String> 	getDefinedKeys() {
		return results.keySet();
	}

	public String getIdentifier() {
		return identifier;
	}

	public List<ReportableResultEntry> getResult(String key) {
		List<ReportableResultEntry> list = results.get(key);
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}

	public Map<String, List<ReportableResultEntry>> getResults() {
		return Collections.unmodifiableMap(results);
	}

	public boolean isResultDefined(String key) {
		return results.containsKey(key);
	}

	public void putAllResults(Map<String, List<ReportableResultEntry>> toInsert) {
		results.putAll(toInsert);
	}
}
