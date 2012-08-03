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
package com.sematext.ag.source.dictionary;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.event.SimpleDataEvent;
import com.sematext.ag.source.FiniteEventSource;
import com.sematext.ag.source.dictionary.field.FieldType;

/**
 * {@link FiniteEventSource} for data generated using dictionary.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class DataDictionaryEventSource extends AbstractDictionaryEventSource<SimpleDataEvent> {
  public static long created = 0l;
  public static final String FIELDS_KEY = "dataDictionaryTextEventSource.fields";
  private Map<String, FieldType> fields;

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.source.dictionary.AbstractDictionaryEventSource#init(com.sematext.ag.PlayerConfig)
   */
  @Override
  public synchronized void init(PlayerConfig config) {
    super.init(config);
    this.fields = new HashMap<String, FieldType>();
    String fields = config.get(FIELDS_KEY);
    if (fields == null || "".equals(fields.trim())) {
      throw new IllegalArgumentException(this.getClass().getName() + " expects configuration property " + FIELDS_KEY);
    }
    String[] fieldsWithTypes = fields.split(" ");
    for (String fieldWithType : fieldsWithTypes) {
      String[] singleField = fieldWithType.split(":");
      if (singleField.length != 2) {
        continue;
      }
      FieldType type = getFieldType(singleField[1]);
      if (type != null) {
        this.fields.put(singleField[0], type);
      }
    }
  }

  /**
   * (non-Javadoc)
   * 
   * @see com.sematext.ag.source.FiniteEventSource#createNextEvent()
   */
  @Override
  protected SimpleDataEvent createNextEvent() {
    SimpleDataEvent event = new SimpleDataEvent("" + ++created);
    for (Map.Entry<String, FieldType> entry : fields.entrySet()) {
      addField(event, entry.getKey(), entry.getValue());
    }
    return event;
  }
  
  private FieldType getFieldType(String string) {
    return FieldType.valueOf(string.toUpperCase(Locale.ENGLISH));
  }

  private void addField(SimpleDataEvent event, String key, FieldType value) {
    String fieldValue = getValue(value);
    if (!fieldValue.isEmpty()) {
      event.addPair(key, fieldValue);
    }
  }

  private String getValue(FieldType value) {
    switch (value) {
      case TEXT:
        return getDictionaryEntry();
      case NUMERIC:
        return new String("" + RANDOM.nextInt(1000));
      case DATE:
        return "2006-02-13T15:26:37Z";
      case GEO:
        return getLatitude() + "," + getLongitude();
    }
    return "";
  }
  
  private String getLatitude() {
    return RANDOM.nextInt(180) - 90 + "." + RANDOM.nextInt(100);
  }
  
  private String getLongitude() {
    return RANDOM.nextInt(360) - 180 + "." + RANDOM.nextInt(100);
  }
}
