/*
 *    Copyright (c) Sematext International
 *    All Rights Reserved
 *
 *    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
 *    The copyright notice above does not evidence any
 *    actual or intended publication of such source code.
 */
package com.sematext.ag.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.SimpleSearchEvent;

public class SearchDictionaryPhraseEventSource extends FiniteEventSource<SimpleSearchEvent> {
  public static final String SEARCH_FIELD_NAME_KEY = "searchDictionaryPhraseEventSource.searchFieldName";
  public static final String DICTIONARY_FILE_NAME_KEY = "searchDictionaryPhraseEventSource.dictionaryFileName";
  
  private static final Logger LOG = Logger.getLogger(SearchDictionaryPhraseEventSource.class);
  private static final List<String> DICTIONARY = new ArrayList<String>();
  
  private String searchFieldName;
  private Random random = new Random();
  
  @Override
  public synchronized void init(PlayerConfig config) {
    super.init(config);
    
    searchFieldName = config.get(SEARCH_FIELD_NAME_KEY);
    
    if (searchFieldName == null || "".equals(searchFieldName.trim())) {
      throw new IllegalArgumentException(this.getClass().getName() + 
          " expects configuration property " + SEARCH_FIELD_NAME_KEY);
    }

    String dictFileName = config.get(DICTIONARY_FILE_NAME_KEY);
    
    if (dictFileName == null || "".equals(dictFileName.trim())) {
      throw new IllegalArgumentException(this.getClass().getName() + 
          " expects configuration property " + DICTIONARY_FILE_NAME_KEY);
    }
    
    File f = new File(dictFileName);
    
    if (!f.exists() || f.isDirectory()) {
      throw new IllegalArgumentException("Property " + DICTIONARY_FILE_NAME_KEY + " should designate existing dictionary file!");
    }
    
    try {
      BufferedReader br = new BufferedReader(new FileReader(f));
      
      while (true) {
        String phrase = br.readLine();
        
        if (phrase != null) {
          DICTIONARY.add(phrase.trim());
        } else {
          break;
        }
      }
      
    } catch (IOException e) {
      throw new IllegalArgumentException("File " + f.getName() + " under key " + DICTIONARY_FILE_NAME_KEY +
          " not readable!");
    }
  }

  @Override
  protected SimpleSearchEvent createNextEvent() {
    SimpleSearchEvent e = new SimpleSearchEvent();
    e.setQueryString(searchFieldName + ":" + DICTIONARY.get(
        random.nextInt(DICTIONARY.size())));
    
    LOG.info("Created event with query string : " + e.getQueryString());
    
    return e;
  }
}
