/*
 *    Copyright (c) Sematext International
 *    All Rights Reserved
 *
 *    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
 *    The copyright notice above does not evidence any
 *    actual or intended publication of such source code.
 */
package com.sematext.ag.es.sink;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.es.util.JSONUtils;
import com.sematext.ag.event.SimpleDataEvent;
import com.sematext.ag.exception.InitializationFailedException;
import com.sematext.ag.sink.Sink;

/**
 * {@link Sink} implementation for ElasticSearch data using bulk API.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class BulkJSONDataESSink extends SimpleJSONDataESSink {
  public static final String ES_BATCH_SIZE_KEY = "bulkJSONDataEsSink.batchSize";
  public static final String ES_BULK_END_POINT = "_bulk";
  private static final Logger LOG = Logger.getLogger(BulkJSONDataESSink.class);
  private int bulkSize = 1000;
  private List<SimpleDataEvent> events;

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.es.sink.SimpleJSONDataESSink#init(com.sematext.ag.PlayerConfig)
   */
  @Override
  public void init(PlayerConfig config) throws InitializationFailedException {
    super.init(config);
    String bulkSize = config.get(ES_BATCH_SIZE_KEY);
    if (bulkSize != null && !"".equals(bulkSize.trim())) {
      this.bulkSize = Integer.parseInt(bulkSize);
    }
    events = new ArrayList<SimpleDataEvent>();
  }

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.es.sink.SimpleJSONDataESSink#write(com.sematext.ag.event.SimpleDataEvent)
   */
  @Override
  public boolean write(SimpleDataEvent event) {
    events.add(event);
    if (events.size() >= bulkSize) {
      List<SimpleDataEvent> eventsCopy = new ArrayList<SimpleDataEvent>(this.events);
      events.clear();
      LOG.info("Sending ES bulk index event with " + eventsCopy.size() + " events");
      HttpPost postMethod = new HttpPost(esBaseUrl + "/" + ES_BULK_END_POINT);
      StringEntity postEntity;
      try {
        postEntity = new StringEntity(getBulkData(eventsCopy), "UTF-8");
        postMethod.setEntity(postEntity);
        postMethod.expectContinue();
        return execute(postMethod);
      } catch (UnsupportedEncodingException uee) {
        LOG.error("Error sending event: " + uee);
        return false;
      }
    }
    return true;
  }

  /**
   * Returns ES bulk data.
   * 
   * @return ES bulk data
   */
  protected String getBulkData(List<SimpleDataEvent> events) {
    StringBuilder builder = new StringBuilder();
    for (SimpleDataEvent event : events) {
      builder.append(JSONUtils.getElasticSearchBulkHeader(event, this.indexName, this.typeName));
      builder.append("\n");
      builder.append(JSONUtils.getElasticSearchAddDocument(event.pairs()));
      builder.append("\n");
    }
    return builder.toString();
  }
}
