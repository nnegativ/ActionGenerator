/*
 *    Copyright (c) Sematext International
 *    All Rights Reserved
 *
 *    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
 *    The copyright notice above does not evidence any
 *    actual or intended publication of such source code.
 */
package com.sematext.ag.es.sink;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.SimpleDataEvent;

public class BulkJSONDataESSinkTest extends TestCase {
  @Test
  public void testGetBulkData() throws Exception {
    BulkJSONDataESSink sink = new BulkJSONDataESSink();
    Map<String, String> properties = new HashMap<String, String>();
    properties.put(SimpleJSONDataESSink.ES_BASE_URL_KEY, "http://localhost:9200");
    properties.put(SimpleJSONDataESSink.ES_INDEX_NAME_KEY, "test");
    properties.put(SimpleJSONDataESSink.ES_TYPE_NAME_KEY, "doc");
    sink.init(new PlayerConfig(properties));

    SimpleDataEvent event = new SimpleDataEvent("1");
    event.addPair("name", "abc");
    event.addPair("category", "it");
    List<SimpleDataEvent> events = new ArrayList<SimpleDataEvent>(1);
    events.add(event);

    assertEquals(
        "{\"index\":{\"_index\":\"test\",\"_type\":\"doc\",\"_id\":\"1\"}}\n{\"category\":\"it\",\"name\":\"abc\"}\n",
        sink.getBulkData(events));
  }
}
