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

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.Event;
import com.sematext.ag.exception.InitializationFailedException;

/**
 * Abstract base class for {@link Event} source.
 * 
 * @author sematext, http://www.sematext.com/
 */
public abstract class Source {
  /**
   * Initialize source.
   * 
   * @param config
   *          source configuration
   * @throws InitializationFailedException
   *           thrown when source initializaion happens
   */
  public void init(PlayerConfig config) throws InitializationFailedException {
  }

  /**
   * Close source.
   */
  public void close() {
    // DO NOTHING BY DEFAULT
  }

  /**
   * Return next event.
   * 
   * @return event
   */
  public abstract Event nextEvent();
}
