package com.sematext.ag.source.dictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import pl.solr.dm.DataModel;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.ComplexEvent;
import com.sematext.ag.source.FiniteEventSource;

/**
 * Event source based on <code>ComplexEvent</code> and external record definition in JSON file.
 *
 * @author negativ
 *
 */
public class ComplexEventSource extends FiniteEventSource<ComplexEvent> {

	public static final String SCHEMA_FILE_NAME_KEY = "complexEventSource.schemaFileName";

	private DataModel model;

	@Override
	public synchronized void init(PlayerConfig config) {
		super.init(config);
		String fileName = config.get(SCHEMA_FILE_NAME_KEY);
		if (fileName == null || "".equals(fileName.trim())) {
			throw new IllegalArgumentException(this.getClass().getName()
					+ " expects configuration property " + SCHEMA_FILE_NAME_KEY);
		}
		File f = new File(fileName);
		if (!f.exists() || f.isDirectory()) {
			throw new IllegalArgumentException("Property "
					+ SCHEMA_FILE_NAME_KEY
					+ " should designate existing schema file!");
		}
		FileInputStream is = null;
		try {
			is = new FileInputStream(f);
			model = DataModel.builder().fromJson(is);
		} catch (FileNotFoundException e) {
		      throw new IllegalArgumentException("File " + f.getName() + " under key " + SCHEMA_FILE_NAME_KEY
		              + " not readable!");
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	@Override
	protected ComplexEvent createNextEvent() {
		return new ComplexEvent(model.getValue());
	}

}
