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
package com.sematext.ag.solr;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.PlayerRunner;
import com.sematext.ag.player.RealTimePlayer;
import com.sematext.ag.solr.sink.SimpleXMLDataSolrSink;
import com.sematext.ag.source.FiniteEventSource;
import com.sematext.ag.source.SimpleSourceFactory;
import com.sematext.ag.source.dictionary.DataDictionaryEventSource;

/**
 * Class used for starting {@link SimpleXMLDataSolrSink} using dictionary.
 * 
 * @author sematext, http://www.sematext.com/
 */
public final class DictionaryDataSolrPlayerMain {
  private DictionaryDataSolrPlayerMain() {
  }

  public static void main(String[] args) {
    if (args.length < 4) {
      System.out.println("Usage: solrUrl eventsCount dictionaryFile fieldName:type [fieldName:type] ...");
      System.out.println("The following types are available:");
      System.out.println(" * text");
      System.out.println(" * numeric");
      System.out.println(" * date");
      System.out
          .println("Example: http://localhost:8983/solr/core/update 10000 dictionary.txt id:numeric title:text published:date");
      System.exit(1);
    }

    String solrUrl = args[0];
    String eventsCount = args[1];
    String dictionaryFile = args[2];

    StringBuilder fields = new StringBuilder();
    fields.append(args[3]);
    if (args.length > 4) {
      for (int i = 4; i < args.length; i++) {
        fields.append(" ").append(args[i]);
      }
    }

    PlayerConfig config = new PlayerConfig(PlayerRunner.PLAYER_CLASS_CONFIG_KEY, RealTimePlayer.class.getName(),
        RealTimePlayer.MIN_ACTION_DELAY_KEY, "20", RealTimePlayer.MAX_ACTION_DELAY_KEY, "1000",
        RealTimePlayer.TIME_TO_WORK_KEY, "5400", RealTimePlayer.SOURCES_THREADS_COUNT_KEY, "3",
        RealTimePlayer.SOURCES_PER_THREAD_COUNT_KEY, "4", PlayerRunner.SOURCE_FACTORY_CLASS_CONFIG_KEY,
        SimpleSourceFactory.class.getName(), SimpleSourceFactory.SOURCE_CLASS_CONFIG_KEY,
        DataDictionaryEventSource.class.getName(), FiniteEventSource.MAX_EVENTS_KEY, eventsCount,
        DataDictionaryEventSource.DICTIONARY_FILE_NAME_KEY, dictionaryFile, DataDictionaryEventSource.FIELDS_KEY,
        fields.toString(), PlayerRunner.SINK_CLASS_CONFIG_KEY, SimpleXMLDataSolrSink.class.getName(),
        SimpleXMLDataSolrSink.SOLR_URL_KEY, solrUrl);
    PlayerRunner.play(config);
  }
}
