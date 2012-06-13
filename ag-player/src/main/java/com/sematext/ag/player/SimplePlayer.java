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
package com.sematext.ag.player;

import com.sematext.ag.Player;
import com.sematext.ag.event.Event;
import com.sematext.ag.exception.InitializationFailedException;
import com.sematext.ag.sink.Sink;
import com.sematext.ag.source.Source;
import com.sematext.ag.source.SourceFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base player implementation.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class SimplePlayer extends Player {
  private static final Log LOG = LogFactory.getLog(SimplePlayer.class);

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.Player#play(com.sematext.ag.source.SourceFactory, com.sematext.ag.sink.Sink)
   */
  @Override
  public void play(SourceFactory sourceFactory, Sink<Event> sink) {
    LOG.info("Actions will be played using one source.");
    Source source = null;
    try {
      source = sourceFactory.create();
    } catch (InitializationFailedException e) {
      LOG.error("Source initialization failed. Can not proceed with playing, stopping...", e);
      return;
    }

    try {
      play(sink, source);
    } finally {
      source.close();
    }
  }

  protected void play(Sink<Event> sink, Source source) {
    Event e = source.nextEvent();
    while (null != e) {
      try {
        sink.startCounters();
        sink.write(e);
        sink.endCounters();
      } catch (Exception ex) {
        LOG.error("Error writing to sink, skipping event. Cause: " + ex.getCause());
      }
      e = source.nextEvent();
    }
  }
}
