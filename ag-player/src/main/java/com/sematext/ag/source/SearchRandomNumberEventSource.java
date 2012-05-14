/*
 *    Copyright (c) Sematext International
 *    All Rights Reserved
 *
 *    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
 *    The copyright notice above does not evidence any
 *    actual or intended publication of such source code.
 */
package com.sematext.ag.source;

import java.util.Random;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.SimpleSearchEvent;

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
