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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.perfectsearchcorp.testfodder.JsonToXmlTestFodder;

public class JsonParserTest
{
  private static final Logger log = LogManager.getLogger( JsonParserTest.class );

  @Test
  public void testSimple()
  {
    tryParse( "SIMPLE_CONTENT  ", new JsonParser( JsonToXmlTestFodder.SIMPLE_CONTENT ) );
  }

  @Test
  public void testArray()
  {
    tryParse( "ARRAY_CONTENT   ", new JsonParser( JsonToXmlTestFodder.ARRAY_CONTENT ) );
  }

  @Test
  public void testComplex()
  {
    tryParse( "COMPLEX_CONTENT ", new JsonParser( JsonToXmlTestFodder.COMPLEX_CONTENT ) );
  }

  @Test
  public void testSerious()
  {
    tryParse( "SERIOUS_CONTENT ", new JsonParser( JsonToXmlTestFodder.SERIOUS_CONTENT ) );
  }

  @Test
  public void testArray2()
  {
    tryParse( "ARRAY_CONTENT2  ", new JsonParser( JsonToXmlTestFodder.ARRAY_CONTENT2 ) );
  }

  @Test
  public void testPerfectSearchExample()
  {
    tryParse( "PERFECTSEARCH_CONTENT  ", new JsonParser( JsonToXmlTestFodder.PERFECTSEARCH_CONTENT ) );
  }

  private Element tryParse( String testName, JsonParser parser )
  {
    log.info( "Test: " + testName + "----------------------------------------" );

    try
    {
      Element element = parser.parse();
      element.hashCode();
      return element;
    }
    catch( ParserBrokenException e )
    {
      e.printStackTrace();
    }

    return null;
  }
}
