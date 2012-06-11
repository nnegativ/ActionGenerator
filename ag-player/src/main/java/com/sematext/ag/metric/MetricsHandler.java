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
package com.sematext.ag.metric;

import com.yammer.metrics.reporting.ConsoleReporter;
import com.yammer.metrics.reporting.CsvReporter;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Class for handling metrics initialization.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class MetricsHandler {
  private static final Logger LOG = Logger.getLogger(MetricsHandler.class);
  public static final String METRICS_ENABLED_KEY = "enableMetrics";
  public static final String METRICS_TYPE_KEY = "metricsType";
  public static final String METRICS_DIR_KEY = "metricsDir";

  private MetricsHandler() {
  }
  
  public static void initMetrics() {
    String enabled = System.getProperty(METRICS_ENABLED_KEY);
    String type = System.getProperty(METRICS_TYPE_KEY);
    String dir = System.getProperty(METRICS_DIR_KEY);

    LOG.info("ENABLED:" + enabled + ", TYPE:" + type + ", DIR:" + dir);

    File outputDir = null;
    MetricOutputType metricOutputType = null;
    if (Boolean.parseBoolean(enabled)) {
      if (type != null && !type.isEmpty()) {
        metricOutputType = MetricOutputType.valueOf(type.toUpperCase(Locale.ENGLISH));
      }
      if (MetricOutputType.FILE == metricOutputType && dir != null && !dir.isEmpty()) {
        outputDir = new File(dir);
        if (!outputDir.isDirectory()) {
          LOG.error(dir + " is not directory. Using console metrics");
          metricOutputType = MetricOutputType.CONSOLE;
        }
      } else {
        metricOutputType = MetricOutputType.CONSOLE;
      }
      
      switch (metricOutputType) {
        case CONSOLE:
          ConsoleReporter.enable(1, TimeUnit.SECONDS);
          break;
        case FILE:
          CsvReporter.enable(outputDir, 1, TimeUnit.SECONDS);
          break;
      }
    }
  }
}
