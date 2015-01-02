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
package com.perfectsearchcorp.scanner;

/**
 * Heavy lifting for JsonScanner.
 *
 * @author Russell Bateman
 * @since December 2014
 */
public class JsonScannerUtil
{
  /**
   * Grind through the content buffer sorting out the next token.
   * Remember, this is only a scanner not a parser: we do not make
   * contextual decisions about things like whether the identifier
   * scanned is a key or a value and we don't care about whether
   * the identifier is in a construct/element or array. Those
   * decisions are left to the parser that calls us.
   *
   * @param buffer beginning with the next token to scan (or, white space before it).
   * @return the next analyzed token.
   * @throws IndexOutOfBoundsException, IllegalCharacterOutsideOfTokenException
   */
  public static Token scanToken( String buffer )
      throws IndexOutOfBoundsException, IllegalCharacterOutsideOfTokenException
  {
    int       pos = 0, scanned = 0;
    boolean   done = false;
    String    tokenString = null;
    Token     token = null;
    TokenType type = TokenType.UNKNOWN;

got_token :
    while( !done )
    {
      char character = buffer.charAt( pos );

      switch( character )
      {
        default :
          String chokeUp = buffer.substring( pos );

          String keyword;

          if( ( keyword = isNullTrueOrFalse( chokeUp ) ) != null )
          {
            scanned += keyword.length();
            return new Token( keyword, TokenType.IDENT, scanned );
          }
          // fall through to inspect as number...

          String number;

          if( ( number = /*Eat.*/eatToSpaceAfterNumber( chokeUp ) ) != null )
          {
            scanned += number.length();
            return new Token( number, TokenType.IDENT, scanned );
          }

          throw new IllegalCharacterOutsideOfTokenException( "Unexpected input" );

        case '"' :
          /* JSON is all about key-value pairs which are delimited
           * using double-quotes. Here we get either a key or a
           * value and return it. To tell our caller how many
           * characters were involved during the scan, regardless
           * of the resulting token length, we maintain a count
           * that includes the backslashes for escaped characters
           * plus the token delimiters (").
           */
          String eaten = /*Eat.*/eatToDoubleQuote( buffer, pos+1 );
          scanned += 1 + eaten.length() + 1;
          return new Token( eaten, TokenType.IDENT, scanned );

        case '{' : tokenString = "{"; type = TokenType.OPEN_BRACE;    done = true; break got_token;
        case '}' : tokenString = "}"; type = TokenType.CLOSE_BRACE;   done = true; break got_token;
        case '[' : tokenString = "["; type = TokenType.OPEN_BRACKET;  done = true; break got_token;
        case ']' : tokenString = "]"; type = TokenType.CLOSE_BRACKET; done = true; break got_token;
        case ':' : tokenString = ":"; type = TokenType.COLON;         done = true; break got_token;
        case ',' : tokenString = ","; type = TokenType.COMMA;         done = true; break got_token;

        case ' ' :
        case '\n' :
        case '\t' :
          // skip white space...
          pos++;
          scanned++;
      }
    }

    /* Non-identifier tokens scanned: adjust the scanned count for
     * any white space ignored.
     */
    token = new Token( tokenString, type, scanned+1 );
    return token;
  }

  /**
   * Simple delimit the identifier up to, but not including
   * the double quote at its end.
   */
  public static String eatToDoubleQuote( String buffer, int starting ) throws IndexOutOfBoundsException
  {
    int pos = starting;
    boolean done = false;
    StringBuilder accepted = new StringBuilder();

    finished_identifier :
    while( !done )
    {
      char character = buffer.charAt( pos );

      switch( character )
      {
        case '\\' : // ignore this character, accept following...
          pos++;
          character = buffer.charAt( pos );
          /* fall through on purpose... */

        default :
          accepted.append( character );
          break;

        case '"' : // ends a token in progress
          done = true;
          break finished_identifier;
      }

      pos++;
    }

    return accepted.toString();
  }

  /**
   * Here we will scan for IDENT up to the next space or newline
   * on condition that it be something like:
   *
   * <ul>
   * <li> 42 </li>
   * <li> -42 </li>
   * <li> +42 </li>
   * <li> .42 </li>
   * <li> 42.0 </li>
   * <li> -42.0 </li>
   * <li> 4.2E+1 </li>
   * <li> hexadecimal numbers </li>
   * <li> etc. </li>
   * </ul>
   */
  public static String eatToSpaceAfterNumber( String buffer )
      throws IllegalCharacterOutsideOfTokenException
  {
    int end = findEnd( buffer );

    if( end == -1 )
      throw new IllegalCharacterOutsideOfTokenException( "Cannot delimit end of suspected number" );

    /* Make sure only that every character found is a valid
     * participant--we're not actually trying to validate
     * the JSON we're filtering.
     * TODO: do we want to go as far as to validate it?
     */
    for( int pos = 0; pos < end-1; pos++ )
    {
      switch( buffer.charAt( pos ) )
      {
        case '-' : case '+' : case '.' :
        case '0' : case '1' : case '2' : case '3' : case '4' :
        case '5' : case '6' : case '7' : case '8' : case '9' :
        case 'a' : case 'b' : case 'c' : case 'd' : case 'e' : case 'f' :
        case 'A' : case 'B' : case 'C' : case 'D' : case 'E' : case 'F' :
          break;

        default :
          throw new IllegalCharacterOutsideOfTokenException( "Suspected number is grossly invalid" );
      }
    }

    return buffer.substring( 0, end-1 );
  }

  public static int findEnd( String buffer )
  {
    int end = buffer.indexOf( ' ' );

    if( end == -1 )
    {
      end = buffer.indexOf( ',' );
      if( end == -1 )
      {
        end = buffer.indexOf( '\n' );
        if( end == -1 )
        {
          end = buffer.indexOf( '\t' );
          if( end == -1 )
          {
            end = buffer.indexOf( "}" );
            if( end == -1 )
              end = buffer.indexOf( ']' );
          }
        }
      }
    }

    return end;
  }

  /**
   * Not only must we match null, true or false, but we must ensure some
   * validity in that <u>something</u> that would halt token-building
   * after the keyword is present as the next character.
   * @throws IllegalCharacterOutsideOfTokenException
   */
  public static String isNullTrueOrFalse( String buffer ) throws IllegalCharacterOutsideOfTokenException
  {
    int end;

    if( buffer.startsWith( "null" ) || buffer.startsWith( "true" ) )
      end = 4;
    else if( buffer.startsWith( "false" ) )
      end = 5;
    else
      return null;

    if( end+1 > buffer.length() )
      throw new IllegalCharacterOutsideOfTokenException( "Premature end of identifier" );

    String keyword = buffer.substring( 0, end+1 );

    if( ( end = findEnd( keyword ) ) == -1 )
      return null;

    return keyword.substring( 0, keyword.length()-1 );
  }
}
