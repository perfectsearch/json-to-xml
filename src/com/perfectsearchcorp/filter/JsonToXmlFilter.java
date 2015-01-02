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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.perfectsearchcorp.generator.XmlGenerator;
import com.perfectsearchcorp.parser.Element;
import com.perfectsearchcorp.parser.JsonParser;
import com.perfectsearchcorp.parser.ParserBrokenException;

public class JsonToXmlFilter
{
  private static final Logger log = LogManager.getLogger( JsonToXmlFilter.class );

  private static final int EIO = 5;  // (from errno.h)

  private static final String APPLICATION = "JSON-to-XML filter (json-to-xml)";
  private static final String COPYRIGHT = "You are free to use this utility as a binary or its sources in any way you please.";
  private static final String DATE = "December 2014";
  private static final String VERSION = "0.9";
  private static final String OPTIONS[] =
                              {
                                "Options",
                                "--rootname <rootname>",
                                "--xmldecl <custom XML Declaration>",
                                "--dtd <DTD>",
                                "--doctype <Document type>",
                                "--pretty",
                                "--tabwidth",
                                "--version",
                                "--logging",
                                "--passive",
                                "--help"
                              };
  private static String  ROOTNAME = null;
  private static String  XMLDECL = null;
  private static String  DTD = null;
  private static String  DOCTYPE = null;
  private static String  TABWIDTH = null;
  private static String  INPUTFILE = null;

  private static boolean PASSIVE = false;
  private static boolean LOGGING = false;
  private static boolean PRETTY = false;

  private static void badCommandLine() throws EarlyExitException
  {
    log.info( "Bad command line -----------------" );
    doHelp();
    throw new EarlyExitException();
  }

  private static void parseOptionsFromCommandLine( String[] args ) throws EarlyExitException
  {
    if( args == null )    // shouldn't happen, but treat as if args.length == 1...
      return;

    String lookingFor = null;

    for( String arg : args )
    {
      switch( arg )
      {
        default :
          if( lookingFor == null )
          {
            INPUTFILE = arg;

            if( isEmpty( INPUTFILE ) )
              badCommandLine();
            break;
          }

          switch( lookingFor )
          {
            case "rootname" : ROOTNAME = arg; lookingFor = null; break;
            case "xmldecl" :  XMLDECL  = arg; lookingFor = null; break;
            case "dtd" :      DTD      = arg; lookingFor = null; break;
            case "doctype" :  DOCTYPE  = arg; lookingFor = null; break;
            case "tabwidth" : TABWIDTH = arg; lookingFor = null; break;
          }
          break;

        case "--rootname" : lookingFor = "rootname"; break;
        case "--xmldecl" :  lookingFor = "xmldecl";  break;
        case "--dtd" :      lookingFor = "dtd";      break;
        case "--doctype" :  lookingFor = "doctype";  break;
        case "--tabwidth" : lookingFor = "tabwidth"; break;

        case "--passive" : PASSIVE = true; break;
        case "--logging" : LOGGING = true; break;
        case "--pretty" :  PRETTY  = true; break;

        case "--version" :
          doApplicationHeader();
          System.exit( 0 );
          break;

        case "--help" :
          doHelp();
          throw new EarlyExitException();
      }
    }
  }

  public static void main( String[] args )
  {
    String content = null;

    try
    {
      parseOptionsFromCommandLine( args );

      try
      {
        content = ( INPUTFILE != null )
                    ? readContentFromFile( INPUTFILE )
                    : readContentFromStdIn();
      }
      catch( IOException e )
      {
        System.err.println( "Failed to open input" );
        e.printStackTrace();
        System.exit( EIO );
      }

      JsonParser parser = new JsonParser( content );

      if( LOGGING )
        parser.setLogLevel( Level.INFO );

      Element element = null;

      try
      {
        if( PASSIVE )
        {
          outputOptionsAsIfToRun();
          throw new EarlyExitException();
        }

        element = parser.parse();
      }
      catch( ParserBrokenException e )
      {
        e.printStackTrace();
      }

      XmlGenerator generator = createAndConfigureGenerator();

      String xml = generator.generate( element );

      System.out.println( xml );
    }
    catch( EarlyExitException e )
    {
      ;
    }
  }

  private static final boolean isEmpty( String string ) { return( string == null || string.length() < 1 ); }

  private static String readContentFromFile( String filename ) throws IOException
  {
    log.info( "Reading from file " + filename );
    File f = new File( filename );
    FileInputStream fis = new FileInputStream( f );
    byte[] contents = new byte[ ( int ) f.length() ];

    fis.read(  contents );
    fis.close();

    return new String( contents, "UTF-8" );
  }

  private static String readContentFromStdIn() throws IOException
  {
    log.info( "Reading from stdin" );
    StringBuilder sb = new StringBuilder();

    Scanner sc = new Scanner( System.in );

    while( sc.hasNextLine() )
      sb.append( sc.nextLine() );

    sc.close();

    return sb.toString();
  }

  private static final void doApplicationHeader()
  {
    log.info( "doApplicationHeader --------------" );
    System.out.println( APPLICATION );
    System.out.println( VERSION );
    System.out.println( DATE );
    System.out.println( COPYRIGHT );
  }

  private static final void doHelp()
  {
    doApplicationHeader();
    log.info( "doHelp ----------------------------" );
    for( String option : OPTIONS )
      System.out.println( option );
  }

  private static final void outputOptionsAsIfToRun()
  {
    doApplicationHeader();
    System.out.println( "Here are the options in force were you to run the filter:" );
    System.out.println( " ROOTNAME = " + ROOTNAME );
    System.out.println( "  XMLDECL = " + XMLDECL );
    System.out.println( "      DTD = " + DTD );
    System.out.println( "  DOCTYPE = " + DOCTYPE );
    System.out.println( " TABWIDTH = " + TABWIDTH );
    System.out.println( "INPUTFILE = " + INPUTFILE );
    System.out.println( "  PASSIVE = " + PASSIVE );
    System.out.println( "  LOGGING = " + LOGGING );
    System.out.println( "   PRETTY = " + PRETTY );
  }

  private static XmlGenerator createAndConfigureGenerator()
  {
    XmlGenerator generator = new XmlGenerator();

    if( !isEmpty( ROOTNAME ) )
      generator.setRootName( ROOTNAME );
    if( !isEmpty( XMLDECL ) )
      generator.setXmlDeclaration( XMLDECL );
    if( !isEmpty( DTD ) )
      generator.setDtd( DTD );
    if( !isEmpty( DOCTYPE ) )
      generator.setDocType( DOCTYPE );

    // pretty printing stuff...
    int tabwidth = 0;

    if( !isEmpty( TABWIDTH ) )
      tabwidth = Integer.parseInt( TABWIDTH );

    if( PRETTY )
    {
      if( tabwidth > 0 )
        generator.configurePrettyPrinter( true, tabwidth );
      else
        generator.configurePrettyPrinter( true );
    }

    if( LOGGING )
    {
      // turn on logging to INFO level...
      LoggerContext ctx = ( LoggerContext ) LogManager.getContext( false );
      Configuration config = ctx.getConfiguration();
      LoggerConfig loggerConfig = config.getLoggerConfig( LogManager.ROOT_LOGGER_NAME );
      loggerConfig.setLevel( Level.INFO );
      ctx.updateLoggers();
   }

    return generator;
  }
}
