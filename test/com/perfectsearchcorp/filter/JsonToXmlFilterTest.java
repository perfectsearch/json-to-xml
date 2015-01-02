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
package com.perfectsearchcorp.filter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.perfectsearchcorp.testfodder.JsonToXmlTestFodder;

/**
 * Much of this is under-evaluated because I don't wish to make the tests
 * too brittle. This is more a way to know how to call the filter, see
 * what it produces, etc.
 *
 * @author Russell Bateman
 * @since December 2014
 */
public class JsonToXmlFilterTest
{
  private static final Logger log = LogManager.getLogger( JsonToXmlFilterTest.class );

  private static final String jsonFilename = "file-to-parse.json";

  @BeforeClass
  public static void setup() throws IOException
  {
    createJsonFileForTesting( jsonFilename, JsonToXmlTestFodder.SIMPLE_CONTENT );
  }

  @AfterClass
  public static void teardown() throws IOException
  {
    deleteJsonFileCreatedForTesting( jsonFilename );
  }

  @Test
  public void testNull() throws EarlyExitException
  {
    String[] args = null;

    // when invoked with no arguments and expected to operate from stdin...
    JsonToXmlFilter.main( args );
  }

  @Test
  public void testNaked() throws EarlyExitException
  {
    String[] args = { "" };

    JsonToXmlFilter.main( args );
  }

  @Test
  public void testHelp()
  {
    log.info( "Test: testHelp ----------------------------------------" );

    String[] args = { "--help" };

    JsonToXmlFilter.main( args );
  }

  @Test
  public void testPassive()
  {
    log.info( "Test: testPassive ----------------------------------------" );

    String[] args =
      {
        "--passive",
        "--rootname",
        "Ahoy!",
        "--dtd",
        "<!DOCTYPE xml generator test PUBLIC \"none: what did you think?\">",
        "--xmldecl",
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>",
        "--doctype",
        "<!DOCTYPE xml generator test PUBLIC \"none: what did you think?\">",
        "--tabwidth",
        "5",
        "--logging",
        "--pretty",
        jsonFilename
      };

    JsonToXmlFilter.main( args );
  }

  @Test
  public void testShortRun() throws IOException
  {
    log.info( "Test: testShortRun ----------------------------------------" );

    String[] args =
      {
        "--rootname", "Ahoy",
        "--logging",
        "--pretty",
        jsonFilename
      };

    JsonToXmlFilter.main( args );
  }

  private static void createJsonFileForTesting( String filename, String content ) throws IOException
  {
    deleteJsonFileCreatedForTesting( filename );

    File f = new File( filename );

    f.createNewFile();

    FileWriter fw = new FileWriter( f.getAbsoluteFile() );
    BufferedWriter writer = new BufferedWriter( fw );
    writer.write( content );
    writer.close();
  }

  private static void deleteJsonFileCreatedForTesting( String filename )
  {
    File f = new File( filename );

    if( f.exists() )
      f.delete();
  }
}
