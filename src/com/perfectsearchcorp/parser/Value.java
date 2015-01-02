/* -------------------------------------------------------------------
 * Copyright (c) 2014 PerfectSearch Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * -------------------------------------------------------------------
 */
package com.perfectsearchcorp.parser;

/**
 * A value can be either simply an identifier (a quoted string, number, etc.)
 * or an arbitrarily complex construct/element.
 *
 * @author Russell Bateman
 * @since December 2014
 */
public class Value
{
  private String  value;    // if scalar
  private Element element;  // if of hierarchical composition

  public Value() { }
  public Value( String value ) { this.value = value; }
  public Value( Element element ) { this.element = element; }

  public String getValue() { return value; }
  public void setValue( String value ) { this.value = value; }

  public Element getElement() { return element; }
  public void setElements( Element element ) { this.element = element; }

  public String toString()
  {
    if( value != null )
      return "\"" + this.value + "\"";

    if ( element != null )
      return this.element.toString();

    return "null";
  }
}
