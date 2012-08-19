package com.sematext.ag.es;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.PlayerRunner;
import com.sematext.ag.es.sink.ComplexJSONDataESSink;
import com.sematext.ag.es.sink.SimpleJSONDataESSink;
import com.sematext.ag.source.FiniteEventSource;
import com.sematext.ag.source.SimpleSourceFactory;
import com.sematext.ag.source.dictionary.ComplexEventSource;
import com.sematext.ag.source.dictionary.DataDictionaryEventSource;

public final class ComplexDataEsPlayerMain {
  private ComplexDataEsPlayerMain() {
  }

  public static void main(String[] args) {
    if (args.length < 5) {
      System.out
          .println("Usage: esBaseUrl esIndexName esTypeName eventsCount schemaFile");
      System.out.println("Example: http://localhost:9200 dashboard post 100 schema.json");
      System.exit(1);
    }

    String esBaseUrl = args[0];
    String esIndexName = args[1];
    String esTypeName = args[2];
    String eventsCount = args[3];
    String schemaFile = args[4];

    PlayerConfig config = new PlayerConfig(SimpleSourceFactory.SOURCE_CLASS_CONFIG_KEY,
        ComplexEventSource.class.getName(), FiniteEventSource.MAX_EVENTS_KEY, eventsCount,
        ComplexEventSource.SCHEMA_FILE_NAME_KEY, schemaFile, 
        PlayerRunner.SINK_CLASS_CONFIG_KEY, ComplexJSONDataESSink.class.getName(),
        ComplexJSONDataESSink.ES_BASE_URL_KEY, esBaseUrl, ComplexJSONDataESSink.ES_INDEX_NAME_KEY, esIndexName,
        ComplexJSONDataESSink.ES_TYPE_NAME_KEY, esTypeName);
    PlayerRunner.play(config);
  }
}
