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

import java.util.LinkedList;
import java.util.List;

/**
 * Scans and manages tokenization.
 *
 * @author Russell Bateman
 * @since December 2014
 */
@SuppressWarnings( "unused" )
public class JsonScanner
{
  private String              content;  // content buffer
  private int                 previous; // beginning of previous token (unused?)
  private int                 scanned;  // total characters scanned
  private LinkedList< Token > pushed = new LinkedList<>();

  public JsonScanner() { }

  public JsonScanner( String input )
  {
    this.content = input;
    this.scanned = 0;
  }

  /**
   * If there is one or more pushed tokens on the stack, pop the last
   * one pushed and return.
   *
   * Walk the content beginning at current. Analyze and return the next
   * token. Reset current to point at rest of unanalyzed content and
   * return the newly scanned token.
   *
   * @return the next token discovered.
   *
   * @throws IndexOutOfBoundsException
   *          Means either that there are no more tokens or that analysis
   *          ended in mid-token because run out of characters and, therefore,
   *          that the emerging token was ill-formed (and therefore the JSON
   *          document as well).
   */
  public Token getNextToken()
      throws NoMoreTokensException, IllegalCharacterOutsideOfTokenException
  {
    Token token = popToken();

    if( token != null )
      return token;

    try
    {
      token = JsonScannerUtil.scanToken( content.substring( scanned ) );
      previous = scanned;
      scanned += token.getScanned();
    }
    catch( IndexOutOfBoundsException e )
    {
      // TODO: check the validity of this assertion:
      // TODO: reveal clues as to which token halted processing?
      if( scanned < content.length() )
        throw new NoMoreTokensException( "Stopped in middle of invalid token", e );
      else
        throw new NoMoreTokensException( "No more tokens to scan", e );
    }

    return token;
  }

  /**
   * For use by the caller.
   * @param token already got and wanted next time <tt>getNextToken()</tt> is called.
   */
  public void pushToken( Token token )
  {
    pushed.addFirst( token );
  }

  private Token popToken()
  {
    if( pushed.size() < 1 )
      return null;

    return pushed.getFirst();
  }

  public int getScanned() { return scanned; }
}
