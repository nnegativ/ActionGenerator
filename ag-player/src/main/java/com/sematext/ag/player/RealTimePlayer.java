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
package com.sematext.ag.player;

import com.sematext.ag.Player;
import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.Event;
import com.sematext.ag.exception.InitializationFailedException;
import com.sematext.ag.sink.Sink;
import com.sematext.ag.source.Source;
import com.sematext.ag.source.SourceFactory;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulates constant real-time user load: users using service with reasonable delays between actions.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class RealTimePlayer extends Player {
  private static final Logger LOG = Logger.getLogger(RealTimePlayer.class);
  public static final String TIME_TO_WORK_KEY = "player.realtime.timeToWorkInSec";
  public static final String MIN_ACTION_DELAY_KEY = "player.realtime.minActionDelayInMs";
  public static final String MAX_ACTION_DELAY_KEY = "player.realtime.maxActionDelayInMs";
  public static final String SOURCES_THREADS_COUNT_KEY = "player.realtime.sources.threadsCount";
  public static final String SOURCES_PER_THREAD_COUNT_KEY = "player.realtime.sources.perThreadCount";
  private long timeToWork; // in ms
  private int threadsNum;
  private int sourcesPerThread;
  private long minActionDelay;
  private long maxActionDelay;

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.Player#init(com.sematext.ag.PlayerConfig)
   */
  @Override
  public void init(PlayerConfig config) throws InitializationFailedException {
    super.init(config);
    validate(config);
    timeToWork = Long.valueOf(config.get(TIME_TO_WORK_KEY)) * 1000;
    minActionDelay = Long.valueOf(config.get(MIN_ACTION_DELAY_KEY));
    maxActionDelay = Long.valueOf(config.get(MAX_ACTION_DELAY_KEY));
    threadsNum = Integer.valueOf(config.get(SOURCES_THREADS_COUNT_KEY));
    sourcesPerThread = Integer.valueOf(config.get(SOURCES_PER_THREAD_COUNT_KEY));
  }

  private void validate(PlayerConfig config) throws InitializationFailedException {
    config.checkRequired(TIME_TO_WORK_KEY);
    config.checkRequired(MIN_ACTION_DELAY_KEY);
    config.checkRequired(MAX_ACTION_DELAY_KEY);
    config.checkRequired(SOURCES_THREADS_COUNT_KEY);
    config.checkRequired(SOURCES_PER_THREAD_COUNT_KEY);
  }

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.Player#play(com.sematext.ag.source.SourceFactory, com.sematext.ag.sink.Sink)
   */
  @Override
  public void play(SourceFactory sourceFactory, Sink<Event> sink) {
    List<List<Source>> sourceGroups = initSources(sourceFactory);
    Thread[] threads = new Thread[threadsNum];
    int i = 0;
    for (List<Source> sourceGroup : sourceGroups) {
      threads[i] = new UserGroupThread(sourceGroup, sink, timeToWork, minActionDelay, maxActionDelay);
      threads[i].start();
      i++;
    }

    for (Thread thread : threads) {
      try {
        thread.join();
      } catch (InterruptedException e) {
        LOG.error(e); // TODO: handle properly
      }
    }
  }

  private static class UserGroupThread extends Thread {
    private List<Source> sourceGroup;
    private Sink<Event> sink;
    private long timeToWork;
    private long minActionDelay;
    private long maxActionDelay;

    public UserGroupThread(List<Source> sourceGroup, Sink<Event> sink, long timeToWork, long minActionDelay,
        long maxActionDelay) {
      this.sourceGroup = sourceGroup;
      this.sink = sink;
      this.timeToWork = timeToWork;
      this.minActionDelay = minActionDelay;
      this.maxActionDelay = maxActionDelay;
    }

    @Override
    public void run() {
      long startTime = System.currentTimeMillis();
      while (System.currentTimeMillis() < startTime + timeToWork && sourceGroup.size() > 0) {
        int sourceIndex = (int) (Math.random() * sourceGroup.size());
        Source source = sourceGroup.get(sourceIndex);
        Event event = source.nextEvent();
        if (null == event) {
          LOG.info("Source finished producing events, left sources in group: " + sourceGroup.size()
              + ", completed source: " + source);
          close(source);
          sourceGroup.remove(source);
        } else {
          try {
            sink.startCounters();
            sink.write(event);
            sink.endCounters();
          } catch (Exception ex) {
            LOG.error("Error writing to sink, skipping event. Cause: " + ex.getCause());
          }
        }
        try {
          if (sourceGroup.size() > 0) {
            long sleepTime = (long) ((minActionDelay + Math.random() * (maxActionDelay - minActionDelay)) / sourceGroup
                .size());
            Thread.sleep(sleepTime);
          }
        } catch (InterruptedException e) {
          LOG.error(e); // TODO: handle properly
        }
      }
      for (Source source : sourceGroup) {
        close(source);
      }
    }

    private void close(Source source) {
      LOG.info("Closing source: " + source + "...");
      source.close();
      LOG.info("Closing source: " + source + "... DONE.");
    }
  }

  private List<List<Source>> initSources(SourceFactory sourceFactory) {
    List<List<Source>> result = new ArrayList<List<Source>>();
    int sourceCount = 0;
    for (int i = 0; i < threadsNum; i++) {
      List<Source> sourceList = new ArrayList<Source>();
      for (int k = 0; k < sourcesPerThread; k++) {
        Source source = null;
        try {
          source = sourceFactory.create();
        } catch (InitializationFailedException e) {
          LOG.error("Creating source failed.", e);
          continue;
        }
        sourceList.add(source);
      }
      LOG.info("Initialized source group, sources count: " + sourceList.size());
      if (sourceList.size() == 0) {
        LOG.warn("Source group contains no sources, group will be ignored");
        continue;
      }
      result.add(sourceList);
      sourceCount += sourceList.size();
    }
    LOG.info("Created " + result.size() + " source groups with total " + sourceCount + " sources.");
    return result;
  }

}
