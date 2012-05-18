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
package com.sematext.ag.source;

import com.sematext.ag.PlayerConfig;
import com.sematext.ag.exception.InitializationFailedException;

/**
 * Abstract base class for {@link Source} factory.
 * 
 * @author sematext, http://www.sematext.com/
 */
public abstract class SourceFactory {
  /**
   * Initialize source factory.
   * 
   * @param config
   *          configuration
   * @throws InitializationFailedException
   *           thrown when initialization error occurs
   */
  public void init(PlayerConfig config) throws InitializationFailedException {
    // DO NOTHING BY DEFAULT
  }

  /**
   * Creates {@link Source} instance.
   * 
   * @return Source instance
   * @throws InitializationFailedException
   *           thrown when initialization error occurs
   */
  public abstract Source create() throws InitializationFailedException;
}
