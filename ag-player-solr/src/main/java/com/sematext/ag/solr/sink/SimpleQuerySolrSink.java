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
package com.sematext.ag.solr.sink;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.SimpleSearchEvent;
import com.sematext.ag.sink.AbstractHttpSink;
import com.sematext.ag.sink.Sink;

/**
 * {@link Sink} implementation for Apache Solr query.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class SimpleQuerySolrSink extends AbstractHttpSink<SimpleSearchEvent> {
  public static final String SOLR_URL_KEY = "simpleSolrSink.solrUrl";
  private static final Logger LOG = Logger.getLogger(SimpleQuerySolrSink.class);
  private static final String SOLR_QUERY_TEMPLATE = "?q=${QUERY_STRING}";
  private String solrUrl;

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.sink.Sink#init(com.sematext.ag.PlayerConfig)
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
   * @see com.sematext.ag.sink.Sink#write(com.sematext.ag.Event)
   */
  @Override
  public boolean write(SimpleSearchEvent event) {
    HttpGet httpGet = new HttpGet(solrUrl + SOLR_QUERY_TEMPLATE.replace("${QUERY_STRING}", event.getQueryString()));
    LOG.info("Sending Apache Solr search event " + httpGet.getRequestLine());
    return execute(httpGet);
  }
}
