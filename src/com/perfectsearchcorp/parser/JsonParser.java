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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.perfectsearchcorp.scanner.IllegalCharacterOutsideOfTokenException;
import com.perfectsearchcorp.scanner.NoMoreTokensException;
import com.perfectsearchcorp.scanner.Token;
import com.perfectsearchcorp.scanner.TokenType;
import com.perfectsearchcorp.scanner.JsonScanner;

/**
 * JSON parser. It calls the scanner since there's little sense in holding
 * the scanner too separate anyway. However, the parser's output is in an
 * <tt>Element</tt> that can be consumed as desired or passed to this
 * library's XML generator.
 *
 * @author Russell Bateman
 * @since December 2014
 */
public class JsonParser
{
  private static final Logger log = LogManager.getLogger( JsonParser.class );

  private JsonScanner scanner;
  private Level logLevel = Level.OFF;

  public JsonParser( String content )
  {
    scanner = new JsonScanner( content );
  }

  public Element parse() throws ParserBrokenException
  {
    try
    {
      log( "Begin parsing..." );
      Token token = scanner.getNextToken();

      if( token.getType() != TokenType.OPEN_BRACE )
        throwParserException( "Ill-formed JSON construct", token );

      return parseElement();
    }
    catch( Exception e )
    {
      throw new ParserBrokenException( "Ill-formed JSON construct" );
    }
  }

  /**
   * Heavy lifting in the composition of elements. This is where it happens,
   * folks.
   * <p />
   * Think of this as starting immediately after a brace. We're looking for
   * a closing brace to pinch this off.
   *
   * @return a new <tt>Element</tt> consisting of key-value pairs.
   * @throws ParserBrokenException
   * @throws NoMoreTokensException
   */
  private Element parseElement()
      throws ParserBrokenException, NoMoreTokensException, IllegalCharacterOutsideOfTokenException
  {
    Element element = new Element();
    Token   token;
    Key     key = null;

    log( "Begin parsing element..." );

    while( ( token = scanner.getNextToken() ) != null )
    {
      Value value;

      switch( token.getType() )
      {
        case IDENT :
          log( "IDENT(" + token.getToken() + ")" );
          if( key == null )
          {
            key = new Key( token.getToken() );
          }
          else
          {
            value = new Value( token.getToken() );
            element.addKeyValuePair( key, value );
            key = null; // got the value, make sure next time IDENT is a key again
          }
          break;

        case OPEN_BRACE :
          log( "OPEN_BRACE" );
          Element subordinate = parseElement();

          if( subordinate.size() > 0 )
          {
            value = new Value( subordinate );
            element.addKeyValuePair( key, value );
            key = null; // got the (complex) value, make sure next time IDENT is a key again
          }
          break;

          // start/stop array...
          case OPEN_BRACKET :
            log( "OPEN_BRACKET" );
            if( key == null )
              throwParserException( "Syntax error (no key for array)", token );

            Element maps = parseArray( key );

            if( maps.size() > 0 )
              element.addElementContents( maps );
            break;

          case CLOSE_BRACE :
            log( "CLOSE_BRACE" );
            /* Been looking for this: we're done: tie off and pop back up.
             */
            return element;

        case COMMA :  // (okay, just skip it...)
          log( "COMMA" );
          break;

        case COLON :  // (okay, just skip it...)
          log( "COLON" );
          break;

        case UNKNOWN :
          log( "UNKNOWN(" + token.getToken() + ")" );
          throwParserException( "Unknown construct", token );

        default :
          log( "OTHER(" + token.getToken() + ")" );
          throwParserException( "Unexpected " + token.getType().name(), token );
      }
    }

    return element;
  }

  /**
   * Heavy lifting in the composition of array elements. Heading for XML as
   * we are, the strategy is simple: create a new element for each member of
   * the array we scan pairing it with a copy of the key.
   * <hr />
   * <b>Array parsing makes an non-trivial implementation decision</b>.
   * <hr />
   * We create and return a new element to our caller who will grab its
   * contents for adding to whatever element he had under construction in
   * the first place.
   * <p />
   * Note that one or more values in the array could just as well be complex
   * elements themselves.
   *
   * @param key with the array we're about to parse.
   * @return a new <tt>Element</tt> consisting of each array value paired
   *          with the (same) key.
   * @throws ParserBrokenException
   * @throws NoMoreTokensException
   */
  private Element parseArray( Key key )
      throws ParserBrokenException, NoMoreTokensException, IllegalCharacterOutsideOfTokenException
  {
    Element element = new Element();
    Token token;

    log( "Begin parsing array..." );

    while( ( token = scanner.getNextToken() ) != null )
    {
      Key   newKey = new Key( key.getKey() );
      Value newValue;

      switch( token.getType() )
      {
        case IDENT :
          log( "IDENT(" + token.getToken() + ")" );
          newValue = new Value( token.getToken() );
          element.addKeyValuePair( newKey, newValue );
          break;

        case OPEN_BRACE :
          log( "OPEN_BRACE" );
          Element subordinate = parseElement();

          if( subordinate.size() > 0 )
          {
            newValue = new Value( subordinate );
            element.addKeyValuePair( newKey, newValue );
          }
          break;

        case CLOSE_BRACKET :
          log( "CLOSE_BRACKET" );
          /* Been looking for this: tie off and return.
           */
          return element;

        case COMMA :  // (okay, just skip it...)
          log( "COMMA" );
          break;

        case UNKNOWN :
          log( "UNKNOWN(" + token.getToken() + ")" );
          throwParserException( "Unknown construct", token );

        default :
          log( "OTHER(" + token.getToken() + ")" );
          throwParserException( "Unexpected " + token.getType().name(), token );
      }
    }

    throwParserException( "Ill formed JSON construct parsing array", token );
    return element;
  }

  /**
   * Helper through which all broken-parser exceptions pass.
   *
   * @param message error statement.
   * @param location the token knows how many characters it has scanned so far.
   * @throws ParserBrokenException
   */
  private void throwParserException( String message, Token location ) throws ParserBrokenException
  {
    int position = location.getScanned() + scanner.getScanned();

    throw new ParserBrokenException( message + " at offset " + position );
  }

  private void log( String message )
  {
    switch( logLevel.toString() )
    {
      case "INFO" :  log.info( message );  break;
      case "DEBUG" : log.debug( message ); break;
      case "TRACE" : log.trace( message ); break;
    }
  }

  public Level getLogLevel() { return logLevel; }
  public void setLogLevel( Level logLevel ) { this.logLevel = logLevel; }
}
