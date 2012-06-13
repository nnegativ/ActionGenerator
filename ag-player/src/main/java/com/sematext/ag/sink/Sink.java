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

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.Event;
import com.sematext.ag.exception.InitializationFailedException;
import com.sematext.ag.metric.BasicMetrics;
import com.sematext.ag.metric.Measureable;

/**
 * Abstract base class for {@link Event} sink.
 * 
 * @author sematext, http://www.sematext.com/
 */
public abstract class Sink<T extends Event> extends Measureable {
  private BasicMetrics metrics;

  /**
   * Initializes sink.
   * 
   * @param config
   *          sink configuration
   * @throws InitializationFailedException
   *           thrown when initialization error occurs
   */
  public void init(PlayerConfig config) throws InitializationFailedException {
    metrics = getMetrics(getClass());
  }

  /**
   * Close sink.
   */
  public void close() {
    // DO NOTHING BY DEFAULT
  }

  /**
   * Starts metrics calculations for a given write.
   */
  public void startCounters() {
    metrics.incrementRequests();
    metrics.startTimer();
  }

  /**
   * Ends metrics calculations for a given write.
   */
  public void endCounters() {
    metrics.stopTimer();
  }

  /**
   * Writes event.
   * 
   * @param event
   *          event to write
   * @return return <code>true</code> if event was successfully written, <code>false</code> otherwise
   */
  public abstract boolean write(T event);
}
