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
package com.sematext.ag.metric;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;

import java.util.concurrent.TimeUnit;

/**
 * Basic metrics class.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class BasicMetrics {
  protected Meter requests;
  protected Timer timer;
  private TimerContext context;
 
  /**
   * Constructor.
   * 
   * @param clazz
   *          class for calculating metrics
   */
  public BasicMetrics(Class<?> clazz) {
    requests = Metrics.newMeter(clazz, "requests", "requests", TimeUnit.SECONDS);
    timer = Metrics.newTimer(clazz, "responses", TimeUnit.MILLISECONDS, TimeUnit.SECONDS);
  }

  /** 
   * Increment number of requests.
   */
  public void incrementRequests() {
    requests.mark();
  }

  /** 
   * Start timer.
   */
  public void startTimer() {
    context = timer.time();
  }

  /** 
   * Stop timer.
   */
  public void stopTimer() {
    context.stop();
  }
}
