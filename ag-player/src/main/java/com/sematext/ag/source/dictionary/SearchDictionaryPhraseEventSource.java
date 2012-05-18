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
package com.sematext.ag.source.dictionary;

import org.apache.log4j.Logger;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.SimpleSearchEvent;
import com.sematext.ag.source.FiniteEventSource;

/**
 * {@link FiniteEventSource} for search phrases generated using dictionary.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class SearchDictionaryPhraseEventSource extends AbstractDictionaryEventSource<SimpleSearchEvent> {
  private static final Logger LOG = Logger.getLogger(SearchDictionaryPhraseEventSource.class);
  public static final String SEARCH_FIELD_NAME_KEY = "searchDictionaryPhraseEventSource.searchFieldName";
  private String searchFieldName;

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
  }

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.source.FiniteEventSource#createNextEvent()
   */
  @Override
  protected SimpleSearchEvent createNextEvent() {
    SimpleSearchEvent e = new SimpleSearchEvent();
    e.setQueryString(searchFieldName + ":" + getDictionaryEntry());
    LOG.info("Created event with query string : " + e.getQueryString());
    return e;
  }
}
