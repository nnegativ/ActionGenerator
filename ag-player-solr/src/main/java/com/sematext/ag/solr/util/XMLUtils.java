/**
 * Copyright Sematext International
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
package com.sematext.ag.solr.util;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Utility methods for working with XML.
 * 
 * @author sematext, http://www.sematext.com/
 */
public final class XMLUtils {
  private XMLUtils() {
  }

  /**
   * Returns Apache Solr add command.
   * 
   * @param values
   *          values to include
   * @return XML as String
   */
  public static String getSolrAddDocument(Map<String, String> values) {
    StringBuilder builder = new StringBuilder();
    builder.append("<add><doc>");
    for (Map.Entry<String, String> pair : values.entrySet()) {
      XMLUtils.addSolrField(builder, pair);
    }
    builder.append("</doc></add>");
    return builder.toString();
  }

  private static void addSolrField(StringBuilder builder, Entry<String, String> pair) {
    if (pair.getKey() != null && !pair.getKey().isEmpty() && pair.getValue() != null && !pair.getValue().isEmpty()) {
      builder.append("<field name=\"").append(pair.getKey()).append("\">").append(pair.getValue()).append("</field>");
    }
  }
}
