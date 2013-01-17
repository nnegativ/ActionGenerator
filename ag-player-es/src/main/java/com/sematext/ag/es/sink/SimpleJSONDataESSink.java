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

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.es.util.JSONUtils;
import com.sematext.ag.event.SimpleDataEvent;
import com.sematext.ag.exception.InitializationFailedException;
import com.sematext.ag.sink.AbstractHttpSink;
import com.sematext.ag.sink.Sink;

/**
 * {@link Sink} implementation for ElasticSearch data.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class SimpleJSONDataESSink extends AbstractHttpSink<SimpleDataEvent> {
  public static final String ES_BASE_URL_KEY = "simpleJSONDataEsSink.esBaseUrl";
  public static final String ES_INDEX_NAME_KEY = "simpleJSONDataEsSink.indexName";
  public static final String ES_TYPE_NAME_KEY = "simpleJSONDataEsSink.typeName";
  private static final Logger LOG = Logger.getLogger(SimpleJSONDataESSink.class);
  protected String esBaseUrl;
  protected String indexName;
  protected String typeName;
  
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
    typeName = config.get(ES_TYPE_NAME_KEY);
    if (esBaseUrl == null || "".equals(esBaseUrl.trim())) {
      throw new IllegalArgumentException(this.getClass().getName() + " expects configuration property "
          + ES_BASE_URL_KEY);
    }
    if (indexName == null || "".equals(indexName.trim())) {
      throw new IllegalArgumentException(this.getClass().getName() + " expects configuration property "
          + ES_INDEX_NAME_KEY);
    }
    if (typeName == null || "".equals(typeName.trim())) {
      throw new IllegalArgumentException(this.getClass().getName() + " expects configuration property "
          + ES_TYPE_NAME_KEY);
    }
  }
  
  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.sink.Sink#write(com.sematext.ag.Event)
   */
  @Override
  public boolean write(SimpleDataEvent event) {
    LOG.info("Sending ES index event");
    HttpPost postMethod = new HttpPost(esBaseUrl + "/" + indexName + "/" + typeName + "/" + event.getId());
    StringEntity postEntity;
    try {
      postEntity = new StringEntity(JSONUtils.getElasticSearchAddDocument(event.pairs()), "UTF-8");
      postMethod.setEntity(postEntity);
      postMethod.expectContinue();
      return execute(postMethod);
    } catch (UnsupportedEncodingException uee) {
      LOG.error("Error sending event: " + uee);
      return false;
    }
  }
}
