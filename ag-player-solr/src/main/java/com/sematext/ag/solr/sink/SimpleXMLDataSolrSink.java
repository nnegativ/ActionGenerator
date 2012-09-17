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

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.SimpleDataEvent;
import com.sematext.ag.exception.InitializationFailedException;
import com.sematext.ag.sink.AbstractHttpSink;
import com.sematext.ag.sink.Sink;
import com.sematext.ag.solr.util.XMLUtils;

/**
 * {@link Sink} implementation for Apache Solr data.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class SimpleXMLDataSolrSink extends AbstractHttpSink<SimpleDataEvent> {
  public static final String SOLR_URL_KEY = "simpleXMLDataSink.solrUrl";
  private static final Logger LOG = Logger.getLogger(SimpleXMLDataSolrSink.class);
  private String solrUrl;

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.sink.Sink#init(com.sematext.ag.PlayerConfig)
   */
  @Override
  public void init(PlayerConfig config) throws InitializationFailedException {
    super.init(config);
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
  public boolean write(SimpleDataEvent event) {
    LOG.info("Sending data to Apache Solr");
    HttpPost postMethod = new HttpPost(solrUrl);
    postMethod.setHeader("Content-type", "application/xml");
    StringEntity postEntity;
    try {
      postEntity = new StringEntity(XMLUtils.getSolrAddDocument(event.pairs()), "UTF-8");
      postMethod.setEntity(postEntity);
      return execute(postMethod);
    } catch (UnsupportedEncodingException uee) {
      LOG.error("Error sending event: " + uee);
      return false;
    }
  }
}
