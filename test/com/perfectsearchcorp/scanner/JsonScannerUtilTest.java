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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Semantically, these tests pretty well demonstrate what's in mind
 * when the methods in the class under test is called.
 *
 * @author Russell Bateman
 * @since December 2014
 */
public class JsonScannerUtilTest
{
  private static final String SOME_IDENTIFIER = "this is some identifier";

  @Test
  public void testFindNoProperEnd()
  {
    /* Considering what's this is used for, we DO expect
     * that after the keyword is not the end of the buffer.
     */
    int end = JsonScannerUtil.findEnd( "null" );
    assertEquals( end, -1 );
  }

  @Test
  public void testFindEndSpace()
  {
    int end = JsonScannerUtil.findEnd( "null " );
    assertEquals( end, 4 );
  }

  @Test
  public void testFindEndComma()
  {
    int end = JsonScannerUtil.findEnd( "true," );
    assertEquals( end, 4 );
  }

  @Test
  public void testFindEndNewline()
  {
    int end = JsonScannerUtil.findEnd( "false\n" );
    assertEquals( end, 5 );
  }

  @Test
  public void testFindEndTab()
  {
    int end = JsonScannerUtil.findEnd( "false\t" );
    assertEquals( end, 5 );
  }

  @Test
  public void testFindRightBrace()
  {
    int end = JsonScannerUtil.findEnd( "true}" );
    assertEquals( end, 4 );
  }

  @Test
  public void testFindRightBracket()
  {
    int end = JsonScannerUtil.findEnd( "null]" );
    assertEquals( end, 4 );
  }

  @Test( expected = IllegalCharacterOutsideOfTokenException.class )
  public void testIsNullTrueOrFalsePrematureEndOfBuffer() throws IllegalCharacterOutsideOfTokenException
  {
    String _null = JsonScannerUtil.isNullTrueOrFalse( "null" );
    assertNull( _null );
  }

  @Test
  public void testIsNullTrueOrFalse() throws IllegalCharacterOutsideOfTokenException
  {
    String _true = JsonScannerUtil.isNullTrueOrFalse( "true " );
    assertNotNull( _true );
    assertTrue( _true.equals( "true" ) );
  }

  @Test
  public void testToDoubleQuote()
  {
    String token = JsonScannerUtil.eatToDoubleQuote( "\"" + SOME_IDENTIFIER + "\"", 1 );
    assertTrue( token.equals( SOME_IDENTIFIER ) );
  }

  private static final String _42     = "42 ";
  private static final String _n_42   = "-42, ";
  private static final String ___42   = "+42\n ";
  private static final String _p_42   = ".42\t ";
  private static final String _42_0   = "42.0] ";
  private static final String _n_42_0 = "-42.0} ";
  private static final String _42E1   = "4.2E+1 ";
  private static final String HEX     = "abcdefABCDEF0123456789 ";
  private static final String BAD1    = "this is bad stuff ";
  private static final String BAD2    = "@#$%^&*() ";

  @Test
  public void testToSpaceAfter42() throws IllegalCharacterOutsideOfTokenException
  {
    String token = JsonScannerUtil.eatToSpaceAfterNumber( _42 );
    assertNotNull( token );
  }

  @Test
  public void testToSpaceAfterNegative42() throws IllegalCharacterOutsideOfTokenException
  {
    String token = JsonScannerUtil.eatToSpaceAfterNumber( _n_42 );
    assertNotNull( token );
  }

  @Test
  public void testToSpaceAfterDot42() throws IllegalCharacterOutsideOfTokenException
  {
    String token = JsonScannerUtil.eatToSpaceAfterNumber( ___42 );
    assertNotNull( token );
  }

  @Test
  public void testToSpaceAfterPlus42() throws IllegalCharacterOutsideOfTokenException
  {
    String token = JsonScannerUtil.eatToSpaceAfterNumber( _p_42 );
    assertNotNull( token );
  }

  @Test
  public void testToSpaceAfterFloatingPoint42() throws IllegalCharacterOutsideOfTokenException
  {
    String token = JsonScannerUtil.eatToSpaceAfterNumber( _42_0 );
    assertNotNull( token );
  }

  @Test
  public void testToSpaceAfterNegativeFloatingPoint42() throws IllegalCharacterOutsideOfTokenException
  {
    String token = JsonScannerUtil.eatToSpaceAfterNumber( _n_42_0 );
    assertNotNull( token );
  }

  @Test
  public void testToSpaceAfter42E1() throws IllegalCharacterOutsideOfTokenException
  {
    String token = JsonScannerUtil.eatToSpaceAfterNumber( _42E1 );
    assertNotNull( token );
  }

  @Test
  public void testToSpaceAfterHexNumber() throws IllegalCharacterOutsideOfTokenException
  {
    String token = JsonScannerUtil.eatToSpaceAfterNumber( HEX );
    assertNotNull( token );
  }

  @Test( expected = IllegalCharacterOutsideOfTokenException.class )
  public void testToSpaceAfterBadString1() throws IllegalCharacterOutsideOfTokenException
  {
    String token = JsonScannerUtil.eatToSpaceAfterNumber( BAD1 );
    assertNull( token );
  }

  @Test( expected = IllegalCharacterOutsideOfTokenException.class )
  public void testToSpaceAfterBadString2() throws IllegalCharacterOutsideOfTokenException
  {
    String token = JsonScannerUtil.eatToSpaceAfterNumber( BAD2 );
    assertNull( token );
  }
}
