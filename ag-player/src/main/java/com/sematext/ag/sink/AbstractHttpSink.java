/**
 * Copyright 2010 Sematext International
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
package com.sematext.ag.sink;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

import com.sematext.ag.event.Event;

/**
 * Abstract base {@link Sink} implementation for sinks using HTTP calls.
 * 
 * @author sematext, http://www.sematext.com/
 */
public abstract class AbstractHttpSink<T extends Event> extends Sink<T> {
  private static final Logger LOG = Logger.getLogger(AbstractHttpSink.class);
  private static final HttpClient HTTP_CLIENT_INSTANCE;

  static {
    ThreadSafeClientConnManager tsccm = new ThreadSafeClientConnManager();
    HTTP_CLIENT_INSTANCE = new DefaultHttpClient(tsccm);
  }

  /**
   * Executes HTTP request.
   * 
   * @param request
   *          request to exectue
   * @return <code>true</code> if request was run successfully, <code>false</code> otherwise
   */
  public boolean execute(HttpRequestBase request) {
    LOG.info("Sending event");
    try {
      HttpResponse response = HTTP_CLIENT_INSTANCE.execute(request);
      LOG.info("Event sent");
      if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
        return false;
      }
      HttpEntity entity = response.getEntity();
      EntityUtils.consume(entity);
      return true;
    } catch (IOException e) {
      LOG.error("Sending event failed", e);
      return false;
    }
  }
}
