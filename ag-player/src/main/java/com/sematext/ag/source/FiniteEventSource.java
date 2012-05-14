/*
 *    Copyright (c) Sematext International
 *    All Rights Reserved
 *
 *    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
 *    The copyright notice above does not evidence any
 *    actual or intended publication of such source code.
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
