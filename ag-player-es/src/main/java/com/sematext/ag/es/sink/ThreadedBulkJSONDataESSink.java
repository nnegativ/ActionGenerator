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

import org.apache.http.client.methods.HttpRequestBase;

import com.sematext.ag.es.sink.thread.DataSenderThread;

/**
 * Sink that send data in a new thread and doesn't wait for completion of the call.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class ThreadedBulkJSONDataESSink extends BulkJSONDataESSink {
  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.sink.AbstractHttpSink#execute(org.apache.http.client.methods.HttpRequestBase)
   */
  @Override
  public boolean execute(HttpRequestBase request) {
    DataSenderThread senderThread = new DataSenderThread(HTTP_CLIENT_INSTANCE, request);
    senderThread.run();
    return true;
  }
}
