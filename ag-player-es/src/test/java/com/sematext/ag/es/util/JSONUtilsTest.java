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
package com.sematext.ag.es.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

public class JSONUtilsTest extends TestCase {
  @Test
  public void testGetElasticSearchAddDocument() throws Exception {
    Map<String, String> pairs = new HashMap<String, String>();
    pairs.put("aaa", "val_aaa");
    pairs.put("bb", "val_bb");
    assertEquals("{\"aaa\":\"val_aaa\",\"bb\":\"val_bb\"}", JSONUtils.getElasticSearchAddDocument(pairs));
  }
}
