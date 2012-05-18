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

import com.sematext.ag.exception.InitializationFailedException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;

/**
 * Player configuration.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class PlayerConfig {
  private Map<String, String> properties = new HashMap<String, String>();

  /**
   * Constructor.
   * 
   * @param configPath
   *          path to configuration file
   * @throws IOException
   *           thrown when I/O error occurs
   */
  public PlayerConfig(String configPath) throws IOException {
    this(new FileInputStream(configPath));
  }

  /**
   * COnstructor.
   * 
   * @param configAsStream
   *          stream with configuration contents
   * @throws IOException
   *           thrown when I/O error occurs
   */
  public PlayerConfig(InputStream configAsStream) throws IOException {
    PropertyResourceBundle bundle = new PropertyResourceBundle(configAsStream);
    for (String key : bundle.keySet()) {
      properties.put(key, bundle.getString(key));
    }
  }

  /**
   * Constructor.
   * 
   * @param key
   *          property key
   * @param valueAndOtherProperties
   *          key and values
   */
  public PlayerConfig(String key, String... valueAndOtherProperties) {
    if (valueAndOtherProperties.length % 2 != 1) {
      throw new IllegalArgumentException("This ctor accepts pairs of String values, but got "
          + (valueAndOtherProperties.length + 1) + "params.");
    }
    Map<String, String> properties = new HashMap<String, String>();
    properties.put(key, valueAndOtherProperties[0]);

    for (int i = 1; i < valueAndOtherProperties.length; i += 2) {
      properties.put(valueAndOtherProperties[i], valueAndOtherProperties[i + 1]);
    }
    this.properties = properties;
  }

  /**
   * Constructor.
   * 
   * @param properties
   *          configuration properties
   */
  public PlayerConfig(Map<String, String> properties) {
    this.properties = properties;
  }

  /**
   * Return property value for a given name.
   * 
   * @param key
   *          property name
   * @return property value
   */
  public String get(String key) {
    return properties.get(key);
  }

  /**
   * Checks if a property value for a given key exists.
   * 
   * @param key
   *          property name
   * @throws InitializationFailedException
   *           throw when a given property is not defined
   */
  public void checkRequired(String key) throws InitializationFailedException {
    if (properties.get(key) == null) {
      throw new InitializationFailedException("Missing required property in config: " + key);
    }
  }
}
