package com.dcap.service.serviceToolKit;

import com.dcap.analyzer.ReportableResult;
import com.dcap.analyzer.ReportableResultEntry;
import com.dcap.domain.ENUMERATED_CATEGORIES;
import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.service.threads.ThreadResponse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * Class from Cheetah_Web1.0
 *
 * modified by uli
 *
 */
public class ReportableResultWriter {
	protected static final String SUBJECT_COLUMN = "subject";
	protected static final String SEPARATOR = ";";
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd_HH-mm");



	public ThreadResponse 	write(String id, User user, UserData parent, String subject, ReportableResult reportableResult, String filePrefix, String resultFileComment, String path) {
		List<String> headers = extractHeaders(reportableResult);
		StringBuilder builder = writeHeaders(headers);


		ReportableResult value = reportableResult;
		for (String key : headers) {
			if (key.equals(SUBJECT_COLUMN)) {
				builder.append(subject);
				builder.append(SEPARATOR);
				continue;
			}

			List<ReportableResultEntry> result = value.getResult(key);
			if (result.size() > 1) {
				throw new IllegalStateException("Found multiple results for the same key for the same subject. Key: " + key);
			}

			if (!result.isEmpty()) {
				builder.append(result.get(0).getResult());
			}else{
				builder.append(("no result"));
			}

			builder.append(SEPARATOR);
		}
		builder.append("\n");


		return writeResultFile(user, filePrefix, resultFileComment, builder.toString(), path, id, parent);
	}

	protected ThreadResponse writeResultFile(User user, String filePrefix, String resultFileComment, String fileContent, String path, String id, UserData parent) {
		String fileName = filePrefix + "_" + DATE_FORMAT.format(new java.util.Date()) + ".csv";
		path=path+"/"+fileName;
		File file = new File(path);
		ThreadResponse response = null;
		try {
			FileWriter writer = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			bufferedWriter.write(fileContent);
			bufferedWriter.close();
			writer.close();
			String message = "Analysis complete. Find the file '" + fileName + "' in your data section!";
			UserData userData = new UserData(parent.getSubject(),  parent, fileName, "application/octet-stream", path, false, message, null, ENUMERATED_CATEGORIES.DATA, user, id);
			response = new ThreadResponse(id,"Measure", message, path, fileName, user.getId(), userData, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	protected List<String> extractHeaders(ReportableResult reportableResult, String... additionalHeaders) {
		List<String> headers = new ArrayList<>();
		headers.add(SUBJECT_COLUMN);
		headers.addAll(Arrays.asList(additionalHeaders));

		Set<String> keys = reportableResult.getDefinedKeys();
		for (String key : keys) {
			if (!headers.contains(key)) {
				headers.add(key);
			}
		}
		Collections.sort(headers, Collator.getInstance());
		return headers;
	}

	protected StringBuilder writeHeaders(List<String> headers) {
		StringBuilder builder = new StringBuilder();
		for (String header : headers) {
			builder.append(header);
			builder.append(SEPARATOR);
		}
		builder.append("\n");
		return builder;
	}
}
