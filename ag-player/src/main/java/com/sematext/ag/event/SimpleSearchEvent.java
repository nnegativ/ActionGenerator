/*
 *    Copyright (c) Sematext International
 *    All Rights Reserved
 *
 *    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
 *    The copyright notice above does not evidence any
 *    actual or intended publication of such source code.
 */
package com.sematext.ag.event;

import com.sematext.ag.Event;

/**
 * Simple implementation of search event which executed just a query (without things like filtering, sorting etc).
 * 
 * @author sematext, http://www.sematext.com/
 */
public class SimpleSearchEvent extends Event {
  private String queryString;

  public String getQueryString() {
    return queryString;
  }

  public void setQueryString(String queryString) {
    this.queryString = queryString;
  }
}

