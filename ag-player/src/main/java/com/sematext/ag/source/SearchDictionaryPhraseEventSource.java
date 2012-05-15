/**
 * Copyright 2010 Sematext International
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
package com.sematext.ag.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.SimpleSearchEvent;

/**
 * {@link FiniteEventSource} for search phrases generated using dictionary.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class SearchDictionaryPhraseEventSource extends FiniteEventSource<SimpleSearchEvent> {
  private static final Logger LOG = Logger.getLogger(SearchDictionaryPhraseEventSource.class);
  public static final String SEARCH_FIELD_NAME_KEY = "searchDictionaryPhraseEventSource.searchFieldName";
  public static final String DICTIONARY_FILE_NAME_KEY = "searchDictionaryPhraseEventSource.dictionaryFileName";
  private static final List<String> DICTIONARY = new ArrayList<String>();
  private String searchFieldName;
  private Random random = new Random();

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.source.FiniteEventSource#init(com.sematext.ag.PlayerConfig)
   */
  @Override
  public synchronized void init(PlayerConfig config) {
    super.init(config);
    searchFieldName = config.get(SEARCH_FIELD_NAME_KEY);
    if (searchFieldName == null || "".equals(searchFieldName.trim())) {
      throw new IllegalArgumentException(this.getClass().getName() + " expects configuration property "
          + SEARCH_FIELD_NAME_KEY);
    }
    String dictFileName = config.get(DICTIONARY_FILE_NAME_KEY);
    if (dictFileName == null || "".equals(dictFileName.trim())) {
      throw new IllegalArgumentException(this.getClass().getName() + " expects configuration property "
          + DICTIONARY_FILE_NAME_KEY);
    }
    File f = new File(dictFileName);
    if (!f.exists() || f.isDirectory()) {
      throw new IllegalArgumentException("Property " + DICTIONARY_FILE_NAME_KEY
          + " should designate existing dictionary file!");
    }
    try {
      BufferedReader br = new BufferedReader(new FileReader(f));
      while (true) {
        String phrase = br.readLine();
        if (phrase != null) {
          DICTIONARY.add(phrase.trim());
        } else {
          break;
        }
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("File " + f.getName() + " under key " + DICTIONARY_FILE_NAME_KEY
          + " not readable!");
    }
  }

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.source.FiniteEventSource#createNextEvent()
   */
  @Override
  protected SimpleSearchEvent createNextEvent() {
    SimpleSearchEvent e = new SimpleSearchEvent();
    e.setQueryString(searchFieldName + ":" + DICTIONARY.get(random.nextInt(DICTIONARY.size())));
    LOG.info("Created event with query string : " + e.getQueryString());
    return e;
  }
}
