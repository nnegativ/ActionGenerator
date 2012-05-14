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

/**
 * Exception thrown when initialization error occurs.
 * 
 * @author sematext, http://www.sematext.com/
 */
public class InitializationFailedException extends Exception {
  private static final long serialVersionUID = 6055746873175504471L;

  /**
   * Constructor.
   * 
   * @param message
   *          error message
   */
  public InitializationFailedException(String message) {
    super(message);
  }

  /**
   * Constructor.
   * 
   * @param message
   *          error message
   * @param cause
   *          error cause
   */
  public InitializationFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
