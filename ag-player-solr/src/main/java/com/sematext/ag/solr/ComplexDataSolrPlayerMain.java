package com.sematext.ag.solr;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.PlayerRunner;
import com.sematext.ag.solr.sink.ComplexDataSolrSink;
import com.sematext.ag.source.FiniteEventSource;
import com.sematext.ag.source.SimpleSourceFactory;
import com.sematext.ag.source.dictionary.ComplexEventSource;

/**
 * Solr search data generator using complex definition of record from JSON file.
 *
 * @author negativ
 *
 */
public class ComplexDataSolrPlayerMain {

	private ComplexDataSolrPlayerMain() {

	}

	public static void main(String[] args) {
		if (args.length < 3) {
			System.out
					.println("Usage: solrUrl eventsCount schemaFile");
			System.out.println("The following types are available:");

			System.out
					.println("Example: http://localhost:8983/solr/core/update 10000 schema.json");
			System.exit(1);
		}

		String solrUrl = args[0];
		String eventsCount = args[1];
		String schemaFile = args[2];

		PlayerConfig config = new PlayerConfig(
				SimpleSourceFactory.SOURCE_CLASS_CONFIG_KEY,
				ComplexEventSource.class.getName(),
				FiniteEventSource.MAX_EVENTS_KEY, eventsCount,
				ComplexEventSource.SCHEMA_FILE_NAME_KEY,
				schemaFile, PlayerRunner.SINK_CLASS_CONFIG_KEY,
				ComplexDataSolrSink.class.getName(),
				ComplexDataSolrSink.SOLR_URL_KEY, solrUrl);
		PlayerRunner.play(config);
	}

}
