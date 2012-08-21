package com.sematext.ag.es.sink;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;

import pl.solr.dm.producers.JsonDataModelProducer;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.ComplexEvent;
import com.sematext.ag.exception.InitializationFailedException;
import com.sematext.ag.sink.AbstractHttpSink;

public class ComplexJSONDataESSink extends AbstractHttpSink<ComplexEvent> {
  public static final String ES_BASE_URL_KEY = "complexJSONDataEsSink.esBaseUrl";
  public static final String ES_INDEX_NAME_KEY = "complexJSONDataEsSink.indexName";
  public static final String ES_TYPE_NAME_KEY = "complexJSONDataEsSink.typeName";
  private static final Logger LOG = Logger.getLogger(ComplexJSONDataESSink.class);
  private String esBaseUrl;
  private String indexName;
  private String typeName;
  
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
  
  @Override
  public boolean write(ComplexEvent event) {
    LOG.info("Sending ES index event");
    HttpPost postMethod = new HttpPost(esBaseUrl + "/" + indexName + "/" + typeName + "/" + event.getIdentifier());
    StringEntity postEntity;
    try {
      postEntity = new StringEntity(new JsonDataModelProducer().convert(event.getObject()), "UTF-8");
      postMethod.setEntity(postEntity);
      postMethod.expectContinue();
      return execute(postMethod);
    } catch (UnsupportedEncodingException uee) {
      LOG.error("Error sending event: " + uee);
      return false;
    }
  }
}
