package com.sematext.ag.solr.sink;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;

import pl.solr.dm.producers.SolrDataModelProducer;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.ComplexEvent;
import com.sematext.ag.exception.InitializationFailedException;
import com.sematext.ag.sink.AbstractHttpSink;

public class ComplexDataSolrSink extends AbstractHttpSink<ComplexEvent> {
	public static final String SOLR_URL_KEY = "complexDataSolrSink.solrUrl";
	
	private static final Logger LOG = Logger.getLogger(ComplexDataSolrSink.class);

	private String solrUrl;

	@Override
	public void init(PlayerConfig config) throws InitializationFailedException {
		super.init(config);
		solrUrl = config.get(SOLR_URL_KEY);
		if (solrUrl == null || "".equals(solrUrl.trim())) {
			throw new IllegalArgumentException(this.getClass().getName()
					+ " expects configuration property " + SOLR_URL_KEY);
		}
	}

	@Override
	public boolean write(ComplexEvent event) {
	    LOG.info("Sending data to Apache Solr");
	    System.err.println(new SolrDataModelProducer().convert(event.getObject()));
	    HttpPost postMethod = new HttpPost(solrUrl);
	    StringEntity postEntity;
	    try {
	      postEntity = new StringEntity(new SolrDataModelProducer().convert(event.getObject()), "UTF-8");
	      postMethod.setEntity(postEntity);
	      return execute(postMethod);
	    } catch (UnsupportedEncodingException uee) {
	      LOG.error("Error sending event: " + uee);
	      return false;
	    }
	}

}
