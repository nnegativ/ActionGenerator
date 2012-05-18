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
import com.sematext.ag.exception.InitializationFailedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base implementation for {@link SourceFactory}.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class SimpleSourceFactory extends SourceFactory {
  private static final Log LOG = LogFactory.getLog(SourceFactory.class);
  public static final String SOURCE_CLASS_CONFIG_KEY = "player.source.class";
  private Class<? extends Source> sourceClass;
  private PlayerConfig config;

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.source.SourceFactory#init(com.sematext.ag.PlayerConfig)
   */
  @SuppressWarnings("unchecked")
  public void init(PlayerConfig config) throws InitializationFailedException {
    this.config = config;
    String sourceClassName = config.get(SOURCE_CLASS_CONFIG_KEY);
    if (sourceClassName == null) {
      System.out.println("Missing required property in config: " + SOURCE_CLASS_CONFIG_KEY);
    }
    try {
      sourceClass = (Class<? extends Source>) Class.forName(sourceClassName);
    } catch (ClassNotFoundException e) {
      LOG.fatal("Instantiating source factory failed, " + SOURCE_CLASS_CONFIG_KEY + ": " + sourceClassName, e);
      System.exit(0);
    }
  }

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.source.SourceFactory#create()
   */
  @Override
  public Source create() throws InitializationFailedException {
    Source source = null;
    try {
      source = sourceClass.newInstance();
    } catch (InstantiationException e) {
      LOG.fatal("Creating source failed, " + SOURCE_CLASS_CONFIG_KEY + ": " + sourceClass, e);
      System.exit(0);
    } catch (IllegalAccessException e) {
      LOG.fatal("Creating source failed, " + SOURCE_CLASS_CONFIG_KEY + ": " + sourceClass, e);
      System.exit(0);
    }
    LOG.info("Initializing source...");
    source.init(config);
    LOG.info("Initializing source... DONE.");
    return source;
  }
}
