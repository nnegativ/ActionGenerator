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
package com.sematext.ag.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple implementation of data event which stores key - value pairs.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class SimpleDataEvent extends Event {
  private String id;
  private Map<String, String> pairs;

  public SimpleDataEvent(String id) {
    pairs = new HashMap<String, String>();
    this.id = id;
  }

  /**
   * Adds key - value pair to event.
   * 
   * @param key
   *          key
   * @param value
   *          value
   */
  public void addPair(String key, String value) {
    pairs.put(key, value);
  }

  /**
   * Return pairs.
   * 
   * @return pair map
   */
  public Map<String, String> pairs() {
    return pairs;
  }

  /**
   * Returns ID of the event.
   * 
   * @return ID of the event
   */
  public String getId() {
    return id;
  }
}
