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
package com.sematext.ag.es.sink.thread;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Data sender class using a new thread.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class DataSenderThread implements Runnable {
  private static final Logger LOG = Logger.getLogger(DataSenderThread.class);
  private HttpClient httpClient;
  private HttpRequestBase request;

  /**
   * Constructor.
   * 
   * @param httpClient
   *          HTTP client instance
   * @param request
   *          request to process
   */
  public DataSenderThread(HttpClient httpClient, HttpRequestBase request) {
    this.httpClient = httpClient;
    this.request = request;
  }

  /**
   * (non-Javadoc)
   * 
   * @see java.lang.Runnable#run()
   */
  @Override
  public void run() {
    LOG.info("Sending event in another thread");
    try {
      HttpResponse response = httpClient.execute(request);
      LOG.info("Event sent");
      if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
        LOG.error("Error during event sending");
      } else {
        HttpEntity entity = response.getEntity();
        EntityUtils.consume(entity);
      }
    } catch (IOException e) {
      LOG.error("Sending event failed", e);
    } finally {
      request.releaseConnection();
    }
  }
}
