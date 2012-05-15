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
package com.sematext.ag.source;

import com.sematext.ag.Event;
import com.sematext.ag.PlayerConfig;
import com.sematext.ag.Source;

/**
 * Abstract class for finite {@link Source} generator.
 * 
 * @author sematext, http://www.sematext.com/
 */
public abstract class FiniteEventSource<T extends Event> extends Source {
  public static final String MAX_EVENTS_KEY = "finiteEventSource.maxEvents";
  private int producedEvents;
  private int maxEvents;

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.Source#init(com.sematext.ag.PlayerConfig)
   */
  @Override
  public void init(PlayerConfig config) {
    maxEvents = Integer.valueOf(config.get(MAX_EVENTS_KEY));
    producedEvents = 0;
  }

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.Source#nextEvent()
   */
  @Override
  public T nextEvent() {
    if (producedEvents >= maxEvents) {
      return null;
    }
    T event = createNextEvent();
    return event;
  }

  /**
   * Creates next event.
   * 
   * @return next event
   */
  protected abstract T createNextEvent();

  public int getProducedEvents() {
    return producedEvents;
  }

  public int getMaxEvents() {
    return maxEvents;
  }
}
