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
package com.sematext.ag.config;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Abstract class which allows to implement functionalities that can be initialized with file based properties. Class
 * extending {@link Configurable} will look for provided properties file (specified with
 * <code>-Dconfigurable.file</code>. If such file is not found, the default class path configuration file will be taken.
 * 
 * @author sematext, http://www.sematext.com/
 */
public abstract class Configurable {
  private static final Logger LOG = Logger.getLogger(Configurable.class);
  public static final String SYSTEM_PROPERTY = "configurable.file";
  public static final String DEFAULT_FILE_NAME = "/configurable.properties";
  private Properties properties;

  /**
   * Initializes configuration.
   * 
   * @throws IOException
   *           thrown when initialization error occurs
   */
  public void init() {
    String fileName = null;
    try {
      fileName = System.getProperty(SYSTEM_PROPERTY);
      if (fileName == null) {
        LOG.info("User file configuration not provided, reading default file.");
        readConfiguration(getClass().getResourceAsStream(DEFAULT_FILE_NAME));
      } else {
        try {
          FileInputStream stream = new FileInputStream(fileName);
          LOG.info("Reading configuration file: " + fileName);
          readConfiguration(stream);
        } catch (FileNotFoundException fnfe) {
          LOG.info("Error using user provided configuration file, reading default file.");
          readConfiguration(getClass().getResourceAsStream(DEFAULT_FILE_NAME));
        }
      }
    } catch (IOException ioe) {
      LOG.error("Error reading provided " + fileName + " file");
    }
  }

  /**
   * Returns configuration value.
   * 
   * @param name
   *          name of the configuration property
   * @return property value as String or <code>null</code> if property doesn't exist.
   */
  public String getConfigurationValue(String name) {
    return properties.getProperty(name);
  }

  protected void readConfiguration(InputStream stream) throws IOException {
    Properties properties = new Properties();
    properties.load(stream);
    this.properties = properties;
  }
}
