/*
 *    Copyright (c) Sematext International
 *    All Rights Reserved
 *
 *    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
 *    The copyright notice above does not evidence any
 *    actual or intended publication of such source code.
 */
package com.sematext.ag.es;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.PlayerRunner;
import com.sematext.ag.player.RealTimePlayer;
import com.sematext.ag.source.SearchDictionaryPhraseEventSource;
import com.sematext.ag.source.SimpleSourceFactory;

/**
 * Class used for starting {@link SimpleQueryEsSink} using dictionary.
 * 
 * @author sematext, http://www.sematext.com/
 */
public final class DictionaryEsPlayerMain {
  private DictionaryEsPlayerMain() {
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    if (args.length != 5) {
      System.out.println("Usage: esBaseUrl esIndexName searchFieldName eventsCount dictionaryFile");
      System.out.println("Example: http://localhost:9200 dashboard text 10000 someFile.txt");
      System.exit(1);
    }

    String esBaseUrl = args[0];
    String esIndexName = args[1];
    String searchFieldName = args[2];
    String eventsCount = args[3];
    String dictionaryFile = args[4];

    PlayerConfig config = new PlayerConfig(PlayerRunner.PLAYER_CLASS_CONFIG_KEY, RealTimePlayer.class.getName(),
        RealTimePlayer.MIN_ACTION_DELAY_KEY, "20", RealTimePlayer.MAX_ACTION_DELAY_KEY, "1000",
        RealTimePlayer.TIME_TO_WORK_KEY, "5400", RealTimePlayer.SOURCES_THREADS_COUNT_KEY, "3",
        RealTimePlayer.SOURCES_PER_THREAD_COUNT_KEY, "4", PlayerRunner.SOURCE_FACTORY_CLASS_CONFIG_KEY,
        SimpleSourceFactory.class.getName(), SimpleSourceFactory.SOURCE_CLASS_CONFIG_KEY,
        SearchDictionaryPhraseEventSource.class.getName(), SearchDictionaryPhraseEventSource.SEARCH_FIELD_NAME_KEY,
        searchFieldName, SearchDictionaryPhraseEventSource.MAX_EVENTS_KEY, eventsCount,
        SearchDictionaryPhraseEventSource.DICTIONARY_FILE_NAME_KEY, dictionaryFile, PlayerRunner.SINK_CLASS_CONFIG_KEY,
        SimpleQueryEsSink.class.getName(), SimpleQueryEsSink.ES_BASE_URL_KEY, esBaseUrl,
        SimpleQueryEsSink.ES_INDEX_NAME_KEY, esIndexName);
    PlayerRunner.play(config);
  }
}
