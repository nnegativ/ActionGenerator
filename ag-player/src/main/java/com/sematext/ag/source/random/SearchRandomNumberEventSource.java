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
package com.sematext.ag.source.random;

import java.util.Random;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.SimpleSearchEvent;
import com.sematext.ag.source.FiniteEventSource;

/**
 * Implementation of {@link FiniteEventSource} for random number.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class SearchRandomNumberEventSource extends FiniteEventSource<SimpleSearchEvent> {
  public static final String SEARCH_FIELD_NAME_KEY = "searchRandomNumberEventSource.searchFieldName";
  private String searchFieldName;

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.source.FiniteEventSource#init(com.sematext.ag.PlayerConfig)
   */
  @Override
  public void init(PlayerConfig config) {
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
    // TODO for now, query is a number, but we should create a query from some dictionary
    SimpleSearchEvent e = new SimpleSearchEvent();
    e.setQueryString(searchFieldName + ":" + String.valueOf(new Random().nextInt(100000)));
    return e;
  }
}
