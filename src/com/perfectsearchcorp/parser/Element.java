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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Russell Bateman
 * @since December 2014
 */
public class Element
{
  private Map< Key, Value > key_value_pairs = new HashMap<>();

  public Element() { }

  public Element( Key key, Value value )
  {
    this.addKeyValuePair( key, value );
  }

  public Map< Key, Value > getKeyValuePairs() { return key_value_pairs; }

  public void addKeyValuePair( Key key, Value value )
  {
    key_value_pairs.put( key, value );
  }

  public void addElementContents( Element element )
  {
    for( Map.Entry< Key, Value > map : element.key_value_pairs.entrySet() )
      this.addKeyValuePair( map.getKey(), map.getValue() );
  }

  public int size() { return key_value_pairs.size(); }

  public String toString()
  {
    if( key_value_pairs.size() > 0 )
    {
      StringBuilder sb = new StringBuilder().append( "{\n" );

      for( Map.Entry< Key, Value > map : this.key_value_pairs.entrySet() )
      {
        Key   key   = map.getKey();
        Value value = map.getValue();

        if( value.getValue() != null )
          sb.append( "  " + key.getKey() + " : " + value.toString() );
        else
          sb.append( "  " + key.getKey() + " : " + value.getElement().toString() );
      }

      return sb.append( "\n}" ).toString();
    }

    return "(nothing)";
  }
}
