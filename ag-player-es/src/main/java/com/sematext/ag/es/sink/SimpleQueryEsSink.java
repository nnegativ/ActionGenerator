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
package com.sematext.ag.es.sink;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.SimpleSearchEvent;
import com.sematext.ag.exception.InitializationFailedException;
import com.sematext.ag.sink.AbstractHttpSink;
import com.sematext.ag.sink.Sink;

/**
 * {@link Sink} implementation for ElasticSearch query.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class SimpleQueryEsSink extends AbstractHttpSink<SimpleSearchEvent> {
  public static final String ES_BASE_URL_KEY = "simpleEsSink.esBaseUrl";
  public static final String ES_INDEX_NAME_KEY = "simpleEsSink.indexName";
  private static final Logger LOG = Logger.getLogger(SimpleQueryEsSink.class);
  private static final String ES_QUERY_TEMPLATE = "/${INDEX_NAME}/_search?q=${QUERY_STRING}";
  private String esBaseUrl;
  private String indexName;

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.sink.Sink#init(com.sematext.ag.PlayerConfig)
   */
  @Override
  public void init(PlayerConfig config) throws InitializationFailedException {
    super.init(config);
    esBaseUrl = config.get(ES_BASE_URL_KEY);
    indexName = config.get(ES_INDEX_NAME_KEY);
    if (esBaseUrl == null || "".equals(esBaseUrl.trim())) {
      throw new IllegalArgumentException(this.getClass().getName() + " expects configuration property "
          + ES_BASE_URL_KEY);
    }
    if (indexName == null || "".equals(indexName.trim())) {
      throw new IllegalArgumentException(this.getClass().getName() + " expects configuration property "
          + ES_INDEX_NAME_KEY);
    }
  }

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.sink.Sink#write(com.sematext.ag.Event)
   */
  @Override
  public boolean write(SimpleSearchEvent event) {
    HttpGet httpGet = new HttpGet(esBaseUrl
        + ES_QUERY_TEMPLATE.replace("${INDEX_NAME}", indexName).replace("${QUERY_STRING}",
            event.getQueryString().replace(" ", "+")));
    LOG.info("Sending ES search event " + httpGet.getRequestLine());
    return execute(httpGet);
  }
}
