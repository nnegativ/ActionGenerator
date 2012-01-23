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

import com.sematext.ag.Event;
import com.sematext.ag.InitializationFailedException;
import com.sematext.ag.Player;
import com.sematext.ag.Sink;
import com.sematext.ag.Source;
import com.sematext.ag.SourceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * FIXME: TODO: add description
 */
public class SimplePlayer extends Player {
  private static final Log LOG = LogFactory.getLog(SimplePlayer.class);

  @Override
  public void play(SourceFactory sourceFactory, Sink sink) {
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

  private void play(Sink sink, Source source) {
    Event e = source.nextEvent();
    while (null != e) {
      sink.write(e);
      e = source.nextEvent();
    }
  }
}
