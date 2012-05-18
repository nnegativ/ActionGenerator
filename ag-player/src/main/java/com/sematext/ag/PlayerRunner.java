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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sematext.ag.event.Event;
import com.sematext.ag.exception.InitializationFailedException;
import com.sematext.ag.sink.Sink;
import com.sematext.ag.source.SourceFactory;

import java.io.IOException;

/**
 * Main class for running process.
 * 
 * @author sematext, http://www.sematext.com/
 */
public final class PlayerRunner {
  private static final Log LOG = LogFactory.getLog(PlayerRunner.class);
  public static final String PLAYER_CLASS_CONFIG_KEY = "player.class";
  public static final String SOURCE_FACTORY_CLASS_CONFIG_KEY = "player.sourceFactory.class";
  public static final String SINK_CLASS_CONFIG_KEY = "player.sink.class";

  private PlayerRunner() {
  }

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.out.println("Usage: java com.sematext.ag.PlayerRunner <config-file>");
      System.exit(1);
    }
    PlayerConfig config = new PlayerConfig(args[0]);
    play(config);
  }

  public static void play(PlayerConfig config) {
    try {
      validateConfig(config);
    } catch (InitializationFailedException e) {
      LOG.fatal("Bad configuration", e);
      System.exit(0);
    }
    Player player = instantiatePlayer(config);
    SourceFactory sourceFactory = instantiateSourceFactory(config);
    Sink<Event> sink = instantiateSink(config);

    initPlayer(config, player);
    initSourceFactory(config, sourceFactory);
    initSink(config, sink);

    try {
      play(player, sourceFactory, sink);
    } finally {
      closeSink(sink);
    }
  }

  private static void play(Player player, SourceFactory sourceFactory, Sink<Event> sink) {
    LOG.info("Start playing...");
    player.play(sourceFactory, sink);
    LOG.info("Finished playing.");
  }

  private static void initSink(PlayerConfig config, Sink<Event> sink) {
    LOG.info("Initializing sink...");
    try {
      sink.init(config);
      LOG.info("Initializing sink... DONE.");
    } catch (InitializationFailedException e) {
      LOG.fatal("Sink initialization failed", e);
      System.exit(1);
    }
  }

  private static void closeSink(Sink<Event> sink) {
    LOG.info("Closing sink...");
    sink.close();
    LOG.info("Closing sink... DONE.");
  }

  private static void initSourceFactory(PlayerConfig config, SourceFactory sourceFactory) {
    LOG.info("Initializing source factory...");
    try {
      sourceFactory.init(config);
    } catch (InitializationFailedException e) {
      LOG.fatal("Source factory initialization failed", e);
      System.exit(1);
    }
    LOG.info("Initializing source factory... DONE.");
  }

  private static void initPlayer(PlayerConfig config, Player player) {
    LOG.info("Initializing player...");
    try {
      player.init(config);
    } catch (InitializationFailedException e) {
      LOG.fatal("Player initialization failed", e);
      System.exit(1);
    }
    LOG.info("Initializing player... DONE.");
  }

  @SuppressWarnings("unchecked")
  private static Sink<Event> instantiateSink(PlayerConfig config) {
    String sinkClass = config.get(SINK_CLASS_CONFIG_KEY);
    Sink<Event> sink = null;
    try {
      sink = (Sink<Event>) Class.forName(sinkClass).newInstance();
    } catch (InstantiationException e) {
      LOG.fatal("Instantiating sink failed, " + SINK_CLASS_CONFIG_KEY + ": " + sinkClass, e);
      System.exit(1);
    } catch (IllegalAccessException e) {
      LOG.fatal("Instantiating sink failed, " + SINK_CLASS_CONFIG_KEY + ": " + sinkClass, e);
      System.exit(1);
    } catch (ClassNotFoundException e) {
      LOG.fatal("Instantiating sink failed, " + SINK_CLASS_CONFIG_KEY + ": " + sinkClass, e);
      System.exit(1);
    }
    return sink;
  }

  private static SourceFactory instantiateSourceFactory(PlayerConfig config) {
    String sourceFactoryClass = config.get(SOURCE_FACTORY_CLASS_CONFIG_KEY);
    SourceFactory sourceFactory = null;
    try {
      sourceFactory = (SourceFactory) Class.forName(sourceFactoryClass).newInstance();
    } catch (InstantiationException e) {
      LOG.fatal("Instantiating source factory failed, " + SOURCE_FACTORY_CLASS_CONFIG_KEY + ": " + sourceFactoryClass,
          e);
      System.exit(1);
    } catch (IllegalAccessException e) {
      LOG.fatal("Instantiating source factory failed, " + SOURCE_FACTORY_CLASS_CONFIG_KEY + ": " + sourceFactoryClass,
          e);
      System.exit(1);
    } catch (ClassNotFoundException e) {
      LOG.fatal("Instantiating source factory failed, " + SOURCE_FACTORY_CLASS_CONFIG_KEY + ": " + sourceFactoryClass,
          e);
      System.exit(1);
    }
    return sourceFactory;
  }

  private static Player instantiatePlayer(PlayerConfig config) {
    String playerClass = config.get(PLAYER_CLASS_CONFIG_KEY);
    Player player = null;
    try {
      player = (Player) Class.forName(playerClass).newInstance();
    } catch (InstantiationException e) {
      LOG.fatal("Instantiating player failed, " + PLAYER_CLASS_CONFIG_KEY + ": " + playerClass, e);
      System.exit(1);
    } catch (IllegalAccessException e) {
      LOG.fatal("Instantiating player failed, " + PLAYER_CLASS_CONFIG_KEY + ": " + playerClass, e);
      System.exit(1);
    } catch (ClassNotFoundException e) {
      LOG.fatal("Instantiating player failed, " + PLAYER_CLASS_CONFIG_KEY + ": " + playerClass, e);
      System.exit(1);
    }
    return player;
  }

  private static void validateConfig(PlayerConfig config) throws InitializationFailedException {
    config.checkRequired(PLAYER_CLASS_CONFIG_KEY);
    config.checkRequired(SOURCE_FACTORY_CLASS_CONFIG_KEY);
    config.checkRequired(SINK_CLASS_CONFIG_KEY);
  }
}
