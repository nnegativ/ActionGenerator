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
package com.sematext.ag;

import com.sematext.ag.event.Event;
import com.sematext.ag.exception.InitializationFailedException;
import com.sematext.ag.sink.Sink;
import com.sematext.ag.source.SourceFactory;

/**
 * Player class.
 * 
 * @author sematext, http://www.sematext.com/
 */
public abstract class Player {
  /**
   * Initialize player.
   * 
   * @param config
   *          player configuration
   * @throws InitializationFailedException
   *           thrown when initialization error occurs
   */
  public void init(PlayerConfig config) throws InitializationFailedException {
    // by default do nothing
  }

  /**
   * Starts player.
   * 
   * @param source
   *          source factory
   * @param sink
   *          source sink
   */
  public abstract void play(SourceFactory source, Sink<Event> sink);
}
