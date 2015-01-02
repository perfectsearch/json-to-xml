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

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class JsonScannerTest
{
  private static final String IDENT         = "\"This is an identifier\"";
  private static final String OPEN_BRACE    = "{";
  private static final String CLOSE_BRACE   = "}";
  private static final String OPEN_BRACKET  = "[";
  private static final String CLOSE_BRACKET = "]";
  private static final String COLON         = ":";
  private static final String COMMA         = ",";

  private static final String TWO_IDENTS    = IDENT + " : " + IDENT;


  private JsonScanner scanner;
  private Token       token;

  private void setup( String input ) throws NoMoreTokensException, IllegalCharacterOutsideOfTokenException
  {
    scanner = new JsonScanner( input );
    token   = scanner.getNextToken();
  }

  @Test
  public void testIdentifier() throws NoMoreTokensException, IllegalCharacterOutsideOfTokenException
  {
    setup( IDENT );
    assertEquals( token.getType(), TokenType.IDENT );
  }

  @Test
  public void testPointersMaintained() throws NoMoreTokensException, IllegalCharacterOutsideOfTokenException
  {
    setup( TWO_IDENTS );
    assertEquals( token.getType(), TokenType.IDENT );
    String expected = IDENT.substring( 1, IDENT.length()-1 );
    assertTrue( token.getToken().equals( expected ) );
    token = scanner.getNextToken();
    assertEquals( token.getType(), TokenType.COLON );
    assertEquals( token.getScanned(), 2 );
  }

  @Test
  public void testOpenBrace() throws NoMoreTokensException, IllegalCharacterOutsideOfTokenException
  {
    setup( OPEN_BRACE );
    assertEquals( token.getType(), TokenType.OPEN_BRACE );
  }

  @Test
  public void testCloseBrace() throws NoMoreTokensException, IllegalCharacterOutsideOfTokenException
  {
    setup( CLOSE_BRACE );
    assertEquals( token.getType(), TokenType.CLOSE_BRACE );
  }

  @Test
  public void testOpenBracket() throws NoMoreTokensException, IllegalCharacterOutsideOfTokenException
  {
    setup( OPEN_BRACKET );
    assertEquals( token.getType(), TokenType.OPEN_BRACKET );
  }

  @Test
  public void testCloseBracket() throws NoMoreTokensException, IllegalCharacterOutsideOfTokenException
  {
    setup( CLOSE_BRACKET );
    assertEquals( token.getType(), TokenType.CLOSE_BRACKET );
  }

  @Test
  public void testColon() throws NoMoreTokensException, IllegalCharacterOutsideOfTokenException
  {
    setup( COLON );
    assertEquals( token.getType(), TokenType.COLON );
  }

  @Test
  public void testComma() throws NoMoreTokensException, IllegalCharacterOutsideOfTokenException
  {
    setup( COMMA );
    assertEquals( token.getType(), TokenType.COMMA );
  }
}
