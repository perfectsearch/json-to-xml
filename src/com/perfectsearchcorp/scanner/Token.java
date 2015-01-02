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
 * Details a scanned token.
 *
 * @author Russell Bateman
 * @since December 2014
 */
public class Token
{
  String    token;
  TokenType type;
  int       scanned = 1;

  public Token() { };

  public Token( String token, TokenType type )
  {
    this.token = token;
    this.type = type;
  }

  public Token( String token, TokenType type, int scanned )
  {
    this(token, type);
    this.scanned = scanned;
  }

  public TokenType getType() { return type; }
  public String getToken() { return token; }
  public int getScanned() { return scanned; }

  public String toString()
  {
    return "{"
        + "\ntoken   = " + this.token
        + "\ntype    = " + this.type.toString()
        + "\nscanned = " + this.scanned
        + "\n}";
  }
}
