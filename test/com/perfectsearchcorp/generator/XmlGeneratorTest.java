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
package com.perfectsearchcorp.generator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.perfectsearchcorp.parser.Element;
import com.perfectsearchcorp.parser.JsonParser;
import com.perfectsearchcorp.parser.ParserBrokenException;
import com.perfectsearchcorp.testfodder.JsonToXmlTestFodder;

public class XmlGeneratorTest
{
  private static final Logger log = LogManager.getLogger( XmlGeneratorTest.class );

  private static final String DOCTYPE = "<!DOCTYPE xml generator test PUBLIC \"none: what did you think?\">";
  private static final String BIG_ROOTNAME = "big-rootname";

  @Test
  public void testSimpleNoPrettyPrint()
  {
    Element element = doParse( JsonToXmlTestFodder.SIMPLE_JSON );
    XmlGenerator generator = configureGenerator();

    generator.configurePrettyPrinter( false );

    String xml = generator.generate( element );

    logToConsole( "XML output: \n" + xml );

    assertNotNull( xml );
    assertFalse( xml.contains( "\n" ) );
  }

  @Test
  public void testNullElement()
  {
    XmlGenerator generator = configureGenerator();

    String xml = generator.generate( null );

    logToConsole( "XML output: \n" + xml );

    assertNotNull( xml );
  }

  @Test
  public void testSimpleJson()
  {
    Element element = doParse( JsonToXmlTestFodder.SIMPLE_JSON );
    XmlGenerator generator = configureGenerator();

    String xml = generator.generate( element );

    logToConsole( "XML output: \n" + xml );

    assertNotNull( xml );
  }

  @Test
  public void testSimpleArray()
  {
    Element element = doParse( JsonToXmlTestFodder.SIMPLE_ARRAY );
    XmlGenerator generator = configureGenerator();

    String xml = generator.generate( element );

    logToConsole( "XML output: \n" + xml );

    assertNotNull( xml );
  }

  @Test
  public void testComplexJson()
  {
    Element element = doParse( JsonToXmlTestFodder.COMPLEX_JSON );
    XmlGenerator generator = configureGenerator();

    String xml = generator.generate( element );

    logToConsole( "XML output: \n" + xml );

    assertNotNull( xml );
  }

  @Test
  public void testSeriousJson()
  {
    Element element = doParse( JsonToXmlTestFodder.SERIOUS_CONTENT );
    XmlGenerator generator = configureGenerator();
    generator.setRootName( null );

    String xml = generator.generate( element );

    logToConsole( "XML output: \n" + xml );

    assertNotNull( xml );
  }

  @Test
  public void testSeriousJson2()
  {
    Element element = doParse( JsonToXmlTestFodder.SERIOUS_CONTENT2 );
    XmlGenerator generator = configureGenerator();
    generator.setRootName( null );

    String xml = generator.generate( element );

    logToConsole( "XML output: \n" + xml );

    assertNotNull( xml );
  }

  @Test
  public void testSeriousJson3()
  {
    Element element = doParse( JsonToXmlTestFodder.SERIOUS_CONTENT3 );
    XmlGenerator generator = configureGenerator();
    generator.setRootName( null );
    generator.setDocType( null );
    generator.makeTabsVisible();

    String xml = generator.generate( element );

    logToConsole( "XML output: \n" + xml );

    assertNotNull( xml );
  }

  @Test
  public void testPerfectSearchJson()
  {
    Element element = doParse( JsonToXmlTestFodder.PERFECTSEARCH_CONTENT );
    XmlGenerator generator = configureGenerator();

    String xml = generator.generate( element );

    logToConsole( "XML output: \n" + xml );

    assertNotNull( xml );
  }

  @Test
  public void testPerfectSearchJson2()
  {
    Element element = doParse( JsonToXmlTestFodder.PERFECTSEARCH_CONTENT2 );
    XmlGenerator generator = configureGenerator();

    String xml = generator.generate( element );

    logToConsole( "XML output: \n" + xml );

    assertNotNull( xml );
  }

  private Element doParse( String content )
  {
    JsonParser parser = new JsonParser( content );

    try
    {
      return parser.parse();
    }
    catch( ParserBrokenException e )
    {
      e.printStackTrace();
    }

    return null;
  }

  private XmlGenerator configureGenerator()
  {
    XmlGenerator generator = new XmlGenerator();

    generator.configurePrettyPrinter( true );
    generator.setRootName( BIG_ROOTNAME );
    generator.setDocType( DOCTYPE );

    return generator;
  }

  /**
   * This mechanism is in case we wish to quiet down automatically
   * running tests to save time, space, etc.
   */
  private void logToConsole( String message )
  {
    log.info( message );
  }
}
