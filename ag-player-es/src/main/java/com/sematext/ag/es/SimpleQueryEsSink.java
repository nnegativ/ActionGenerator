/*
 *    Copyright (c) Sematext International
 *    All Rights Reserved
 *
 *    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
 *    The copyright notice above does not evidence any
 *    actual or intended publication of such source code.
 */
package com.sematext.ag.es;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.Sink;
import com.sematext.ag.event.SimpleSearchEvent;

public class SimpleQueryEsSink extends Sink<SimpleSearchEvent> {
  public static final String ES_BASE_URL_KEY = "simpleEsSink.esBaseUrl";
  public static final String ES_INDEX_NAME_KEY = "simpleEsSink.indexName";

  private static final Logger LOG = Logger.getLogger(SimpleQueryEsSink.class);
  private static final HttpClient HTTP_CLIENT_INSTANCE;
  private static final String ES_QUERY_TEMPLATE = "/${INDEX_NAME}/_search?q=${QUERY_STRING}";

  static {
    ThreadSafeClientConnManager tsccm = new ThreadSafeClientConnManager();

    HTTP_CLIENT_INSTANCE = new DefaultHttpClient(tsccm);
  }

  private String esBaseUrl;
  private String indexName;

  @Override
  public void init(PlayerConfig config) {
    esBaseUrl = config.get(ES_BASE_URL_KEY);
    indexName = config.get(ES_INDEX_NAME_KEY);

    if (esBaseUrl == null || "".equals(esBaseUrl.trim())) {
      throw new IllegalArgumentException(this.getClass().getName() + " expects configuration property " + ES_BASE_URL_KEY);
    }

    if (indexName == null || "".equals(indexName.trim())) {
      throw new IllegalArgumentException(this.getClass().getName() + " expects configuration property " + ES_INDEX_NAME_KEY);
    }
  }

  @Override
  public boolean write(SimpleSearchEvent event) {
    HttpGet httpget = new HttpGet(esBaseUrl + ES_QUERY_TEMPLATE.replace("${INDEX_NAME}", indexName).replace("${QUERY_STRING}", event.getQueryString()));

    LOG.info("Sending ES search event " + httpget.getRequestLine());
    try {
      HttpResponse response = HTTP_CLIENT_INSTANCE.execute(httpget);

      LOG.info("Event sent");
      if (response.getStatusLine().getStatusCode() == 404) {
        return false;
      }

      HttpEntity entity = response.getEntity();
      EntityUtils.consume(entity);
    } catch (IOException e) {
      LOG.error("Sending event failed", e);
      return false;
    }

    return true;
  }
}
