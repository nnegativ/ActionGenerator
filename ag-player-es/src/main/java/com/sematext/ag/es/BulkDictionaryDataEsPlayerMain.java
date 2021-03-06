/**
 * Copyright Sematext International
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sematext.ag.es;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.PlayerRunner;
import com.sematext.ag.es.sink.BulkJSONDataESSink;
import com.sematext.ag.es.sink.SimpleJSONDataESSink;
import com.sematext.ag.es.sink.ThreadedBulkJSONDataESSink;
import com.sematext.ag.source.FiniteEventSource;
import com.sematext.ag.source.SimpleSourceFactory;
import com.sematext.ag.source.dictionary.DataDictionaryEventSource;

public final class BulkDictionaryDataEsPlayerMain {
  private BulkDictionaryDataEsPlayerMain() {
  }

  public static void main(String[] args) {
    if (args.length < 8) {
      System.out
          .println("Usage: esBaseUrl esIndexName esTypeName useThreading bulkSize eventsCount dictionaryFile fieldName:type [fieldName:type] ...");
      System.out.println("The following types are available:");
      System.out.println(" * text");
      System.out.println(" * numeric");
      System.out.println(" * date");
      System.out
          .println("Example: http://localhost:9200 dashboard post false 10 100 someFile.txt id:numeric title:text");
      System.exit(1);
    }

    String esBaseUrl = args[0];
    String esIndexName = args[1];
    String esTypeName = args[2];
    String useThreading = args[3];
    String bulkSize = args[4];
    String eventsCount = args[5];
    String dictionaryFile = args[6];

    StringBuilder fields = new StringBuilder();
    fields.append(args[6]);
    if (args.length > 7) {
      for (int i = 7; i < args.length; i++) {
        fields.append(" ").append(args[i]);
      }
    }

    String sinkClassName = BulkJSONDataESSink.class.getName();
    if (Boolean.parseBoolean(useThreading)) {
      sinkClassName = ThreadedBulkJSONDataESSink.class.getName();
    }

    PlayerConfig config = new PlayerConfig(SimpleSourceFactory.SOURCE_CLASS_CONFIG_KEY,
        DataDictionaryEventSource.class.getName(), FiniteEventSource.MAX_EVENTS_KEY, eventsCount,
        DataDictionaryEventSource.DICTIONARY_FILE_NAME_KEY, dictionaryFile, DataDictionaryEventSource.FIELDS_KEY,
        fields.toString(), PlayerRunner.SINK_CLASS_CONFIG_KEY, sinkClassName, SimpleJSONDataESSink.ES_BASE_URL_KEY,
        esBaseUrl, SimpleJSONDataESSink.ES_INDEX_NAME_KEY, esIndexName, SimpleJSONDataESSink.ES_TYPE_NAME_KEY,
        esTypeName, BulkJSONDataESSink.ES_BATCH_SIZE_KEY, bulkSize);
    PlayerRunner.play(config);
  }
}
