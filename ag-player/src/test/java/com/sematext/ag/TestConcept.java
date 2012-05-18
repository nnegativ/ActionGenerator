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

import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.sematext.ag.event.Event;
import com.sematext.ag.player.RealTimePlayer;
import com.sematext.ag.player.SimplePlayer;
import com.sematext.ag.sink.Sink;
import com.sematext.ag.source.SimpleSourceFactory;
import com.sematext.ag.source.Source;

public class TestConcept extends TestCase {
  private volatile static int writtenEvents;
  private volatile static int writtenEventsSum;

  public static class TestEvent extends Event {
    private int number;
  }

  public static class TestSource extends Source {
    private int producedEvents;
    private int maxEvents;

    @Override
    public void init(PlayerConfig config) {
      maxEvents = Integer.valueOf(config.get("testSource.maxEvents"));
      producedEvents = 0;
    }

    @Override
    public Event nextEvent() {
      if (producedEvents >= maxEvents) {
        return null;
      }
      TestEvent testEvent = new TestEvent();
      testEvent.number = ++producedEvents;
      return testEvent;
    }
  }

  public static class TestSink extends Sink<Event> {
    private boolean initialized = false;

    @Override
    public void init(PlayerConfig config) {
      initialized = Boolean.valueOf(config.get("testSink.initialize"));
      writtenEvents = 0;
      writtenEventsSum = 0;
    }

    @Override
    public boolean write(Event event) {
      if (!initialized) {
        Assert.assertTrue("Sink wasn't initialized properly", true);
      }
      writtenEvents++;
      writtenEventsSum += ((TestEvent) event).number;
      return true;
    }

  }

  @Test
  public void test() {
    PlayerConfig config = new PlayerConfig(PlayerRunner.PLAYER_CLASS_CONFIG_KEY, SimplePlayer.class.getName(),
        PlayerRunner.SOURCE_FACTORY_CLASS_CONFIG_KEY, SimpleSourceFactory.class.getName(),
        SimpleSourceFactory.SOURCE_CLASS_CONFIG_KEY, TestSource.class.getName(), PlayerRunner.SINK_CLASS_CONFIG_KEY,
        TestSink.class.getName(), "testSource.maxEvents", "1000", "testSink.initialize", "true");
    PlayerRunner.play(config);
    Assert.assertEquals(1000, writtenEvents);
    Assert.assertEquals((1 + 1000) * 1000 / 2, writtenEventsSum);
  }

  @Test
  public void testRealTimePlayer() {
    PlayerConfig config = new PlayerConfig(PlayerRunner.PLAYER_CLASS_CONFIG_KEY, RealTimePlayer.class.getName(),
        RealTimePlayer.MIN_ACTION_DELAY_KEY, "0", RealTimePlayer.MAX_ACTION_DELAY_KEY, "100",
        RealTimePlayer.TIME_TO_WORK_KEY, "2", RealTimePlayer.SOURCES_THREADS_COUNT_KEY, "2",
        RealTimePlayer.SOURCES_PER_THREAD_COUNT_KEY, "3", PlayerRunner.SOURCE_FACTORY_CLASS_CONFIG_KEY,
        SimpleSourceFactory.class.getName(), SimpleSourceFactory.SOURCE_CLASS_CONFIG_KEY, TestSource.class.getName(),
        PlayerRunner.SINK_CLASS_CONFIG_KEY, TestSink.class.getName(), "testSource.maxEvents", "1000",
        "testSink.initialize", "true");
    PlayerRunner.play(config);

    int eventsPerSourceEstimate = 2000 / ((100 - 0) / 2);
    int sourcesCount = 2 * 3;
    int eventsCountEstimate = eventsPerSourceEstimate * sourcesCount;
    System.out.println("eventsCountEstimate: " + eventsCountEstimate);
    System.out.println("writtenEvents: " + writtenEvents);
    Assert.assertTrue(eventsCountEstimate * 0.85 < writtenEvents && eventsCountEstimate * 1.15 > writtenEvents);
    int eventsValuesSumEstimate = sourcesCount * (1 + eventsPerSourceEstimate) * eventsPerSourceEstimate / 2;
    System.out.println("eventsValuesSumEstimate: " + eventsValuesSumEstimate);
    System.out.println("writtenEventsSum: " + writtenEventsSum);
    Assert.assertTrue(eventsValuesSumEstimate * 0.85 < writtenEventsSum
        && eventsValuesSumEstimate * 1.15 > writtenEventsSum);
  }
}
