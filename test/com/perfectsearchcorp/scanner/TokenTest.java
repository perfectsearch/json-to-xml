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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Russell Bateman
 * @since December 2014
 */
public class TokenTest
{
  private Token token;

  @Test
  public void testIdentifier()
  {
    token = new Token( "This is an identifier", TokenType.IDENT );
    assertEquals( token.type, TokenType.IDENT );
  }

  @Test
  public void testOpenBrace()
  {
    token = new Token( "{", TokenType.OPEN_BRACE );
    assertEquals( token.type, TokenType.OPEN_BRACE );
  }

  @Test
  public void testCloseBrace()
  {
    token = new Token( "}", TokenType.CLOSE_BRACE );
    assertEquals( token.type, TokenType.CLOSE_BRACE );
  }

  @Test
  public void testOpenBracket()
  {
    token = new Token( "[", TokenType.OPEN_BRACKET );
    assertEquals( token.type, TokenType.OPEN_BRACKET );
  }

  @Test
  public void testCloseBracket()
  {
    token = new Token( "}", TokenType.CLOSE_BRACE );
    assertEquals( token.type, TokenType.CLOSE_BRACE );
  }

  @Test
  public void testColon()
  {
    token = new Token( ":", TokenType.COLON );
    assertEquals( token.type, TokenType.COLON );
  }

  @Test
  public void testComma()
  {
    token = new Token( ",", TokenType.COMMA );
    assertEquals( token.type, TokenType.COMMA );
  }

  @Test
  public void testUnknown()
  {
    token = new Token( "#", TokenType.UNKNOWN );
    assertEquals( token.type, TokenType.UNKNOWN );
  }

  @Test
  public void testAnotherUnknown()
  {
    token = new Token( "This is simply another unknown token", TokenType.UNKNOWN );
    assertEquals( token.type, TokenType.UNKNOWN );
  }
}
