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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.Sink;
import com.sematext.ag.event.SimpleSearchEvent;

/**
 * {@link Sink} implementation for Apache Solr query.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class SimpleQuerySolrSink extends Sink<SimpleSearchEvent> {
  public static final String SOLR_URL_KEY = "simpleSolrSink.solrUrl";
  private static final Logger LOG = Logger.getLogger(SimpleQuerySolrSink.class);
  private static final HttpClient HTTP_CLIENT_INSTANCE;
  private static final String SOLR_QUERY_TEMPLATE = "?q=${QUERY_STRING}";
  private String solrUrl;

  static {
    ThreadSafeClientConnManager tsccm = new ThreadSafeClientConnManager();
    HTTP_CLIENT_INSTANCE = new DefaultHttpClient(tsccm);
  }

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.Sink#init(com.sematext.ag.PlayerConfig)
   */
  @Override
  public void init(PlayerConfig config) {
    solrUrl = config.get(SOLR_URL_KEY);
    if (solrUrl == null || "".equals(solrUrl.trim())) {
      throw new IllegalArgumentException(this.getClass().getName() + " expects configuration property " + SOLR_URL_KEY);
    }
  }

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.Sink#write(com.sematext.ag.Event)
   */
  @Override
  public boolean write(SimpleSearchEvent event) {
    HttpGet httpget = new HttpGet(solrUrl + SOLR_QUERY_TEMPLATE.replace("${QUERY_STRING}", event.getQueryString()));
    LOG.info("Sending Apache Solr search event " + httpget.getRequestLine());
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
