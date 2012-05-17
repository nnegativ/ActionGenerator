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
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.Sink;
import com.sematext.ag.event.SimpleDataEvent;
import com.sematext.ag.solr.util.XMLUtils;

/**
 * {@link Sink} implementation for Apache Solr data.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class SimpleXMLDataSolrSink extends Sink<SimpleDataEvent> {
  public static final String SOLR_URL_KEY = "simpleXMLDataSink.solrUrl";
  private static final Logger LOG = Logger.getLogger(SimpleXMLDataSolrSink.class);
  private static final HttpClient HTTP_CLIENT_INSTANCE;
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
  public boolean write(SimpleDataEvent event) {
    HttpPost postMethod = new HttpPost(solrUrl);
    LOG.info("Sending data to Apache Solr");
    try {
      StringEntity postEntity = new StringEntity(XMLUtils.getSolrAddDocument(event.pairs()));
      postMethod.setEntity(postEntity);
      HttpResponse response = HTTP_CLIENT_INSTANCE.execute(postMethod);
      LOG.info("Event sent");
      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        return false;
      }
      HttpEntity entity = response.getEntity();
      EntityUtils.consume(entity);
    } catch (IOException e) {
      LOG.error("Event send failed", e);
      return false;
    }
    return true;
  }
}
