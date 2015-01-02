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

import java.util.Map;

import com.perfectsearchcorp.parser.Element;
import com.perfectsearchcorp.parser.Key;
import com.perfectsearchcorp.parser.Value;

/**
 * From an existing <tt>Element</tt> representing a parsed JSON document,
 * generates XML.
 * <p />
 * It's assumed that the output will be stand-alone XML without a DTD,
 * but if this is not going to be the case, then the XML Declaration can
 * be rewritten. If made null or zero-length, it will be left out of the
 * generated output.
 * <p />
 * If a DTD is desired, there is provision for setting it.
 * <p />
 * If a <tt>DOCTYPE ... PUBLIC ... etc.</tt> element is desired, there is
 * provision for it.
 *
 * <h3> Root name </h3>
 *
 * If the JSON structure has an obvious, dominating key (i.e.: a top-level
 * element whose value is the rest of the JSON content in braces), it will
 * be used as the natural root name.
 * <p />
 * However, if the JSON is not so hierarchically sophisticated, method
 * <tt>setRootName()</tt> can be used to ensure a single XML element with
 * an arbitrary name. Of course, even if there is already an obvious root
 * name in the JSON document, another can be added around that.
 *
 * <h3> Pretty printing </h3>
 *
 * It's possible to specify to embed newlines in the XML output as well as
 * initial level of indentation (default is 0) and width of indentation
 * (default is 2).
 * <p />
 * Our rule for doing this are particular and meant to avoid imprecision
 * The opening tag is responsible for issuing a newline and indenting
 * itself since there's no way the previous closing tag can know with
 * complete accuracy how to preinsert a newline or indentation.
 *
 * @author Russell Bateman
 * @since December 2014
 */
public class XmlGenerator
{
  private String  rootName;
  private String  xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
  private String  dtd;
  private String  docType;
  private StringBuilder xml;

  private PrettyPrinter prettyPrinter = new PrettyPrinter();

  public XmlGenerator() { }

  public void configurePrettyPrinter( boolean enabled )
  {
    prettyPrinter.enabled = enabled;
  }

  public void configurePrettyPrinter( boolean enabled, int indentWidth )
  {
    prettyPrinter.enabled = enabled;
    prettyPrinter.indentWidth = indentWidth;
  }

  public void configurePrettyPrinter( boolean enabled, int tabLevel, int indentWidth )
  {
    prettyPrinter.enabled = enabled;
    prettyPrinter.currentTabLevel = tabLevel;
    prettyPrinter.indentWidth = indentWidth;
  }

  /**
   * Generates the XML based on the parsed JSON document. We replace
   * whatever string buffer may have been used before as this is a
   * brand new generation.
   *
   * @param root as returned from <tt>JsonParser.parse()</tt>.
   * @return XML output.
   */
  public String generate( Element root )
  {
    xml = new StringBuilder();

    prettyPrinter.injectXmlStringBuilder( xml );
    prettyPrinter.reallocateTab();

    handleHeaders();

    if( !isEmpty( rootName ) )
    {
      newline();
      xml.append( "<" + rootName + ">" );
      prettyPrinter.currentTabLevel++;
    }

    if( root != null && root.size() > 0 )
    {
      generateElement( root );
    }

    if( !isEmpty( rootName ) )
    {
      newline();
      xml.append( "</" + rootName + ">" );
      prettyPrinter.currentTabLevel--;
    }

    return xml.toString();
  }

  /**
   * Generate XML from the intermediate <tt>Element</tt>s, <tt>Key</tt>s and
   * <tt>Value</tt>s.
   *
   * @param root dominating a JSON element representation that is to be rendered in XML.
   */
  public void generateElement( Element root )
  {
    for( Map.Entry< Key, Value > element : root.getKeyValuePairs().entrySet() )
    {
      String key = element.getKey().getKey();

      newlineAndIndent();
      issueOpeningTag( key );

      Value value = element.getValue();

      /* If the value is a simple one, just put out the closing tag.
       */
      if( value.getValue() != null )
      {
        issueElementContent( value.getValue() );
      }
      else // if( value.getElement() != null && value.getElement().size() > 0 )
      {
        /* If the value is a complex (hierarchical) entity, call ourselves
         * recursively, bumping the indentation level.
         */
        Element descend = value.getElement();

        prettyPrinter.currentTabLevel++;
        generateElement( descend );
        prettyPrinter.currentTabLevel--;
        newlineAndIndent();
      }

      issueClosingTag( key );
    }
  }

  public void issueOpeningTag( String tag )
  {
    xml.append( "<" + tag + ">" );
  }

  public void issueClosingTag( String tag )
  {
    xml.append( "</" + tag + ">" );
  }

  public void issueElementContent( String content )
  {
    xml.append( content );
  }

  private void handleHeaders()
  {
    boolean newlineNeeded = false;

    if( !isEmpty( xmlDeclaration) )
    {
      newlineNeeded = true;
      xml.append( xmlDeclaration );
    }

    if( !isEmpty( dtd ) )
    {
      if( newlineNeeded )
        newline();

      newlineNeeded = true;
      xml.append( dtd );
    }

    if( !isEmpty( docType ) )
    {
      if( newlineNeeded )
        newline();

      newlineNeeded = true;
      xml.append( docType );
    }
  }

  private void newline() { prettyPrinter.newline(); }
  private void newlineAndIndent() { prettyPrinter.newlineAndIndent(); }

  public String getRootName() { return rootName; }
  public void setRootName( String name ) { this.rootName = name; }
  public String getXmlDeclaration() { return xmlDeclaration; }
  public void setXmlDeclaration( String header ) { this.xmlDeclaration = header; }
  public String getDtd() { return dtd; }
  public void setDtd( String dtd ) { this.dtd = dtd; }
  public String getDocType() { return docType; }
  public void setDocType( String docType ) { this.docType = docType; }

  public void makeTabsVisible() { prettyPrinter.showTabs = true; }

  private boolean isEmpty( String string ) { return( string == null || string.length() < 1 ); }

  // =================================================================
  /**
   * Manage pretty printing for XML generator output. By default,
   * the service is turned off. Allows for making the leftmost margin
   * begin at any even increment to the right of the actual column 1.
   * Allows width of tab to be specified, default is 2 spaces.
   *
   * <h3> Rules </h3>
   *
   * - Issue newlines <u>before</u> line (not after).
   * <p />
   *
   * @author Russell Bateman
   * @since December 2014
   */
  static class PrettyPrinter
  {
    private static final char VISIBLE_TAB = '\u00B7';
    private static final char SPACE       = ' ';

    public boolean enabled         = false;// default: don't pretty-print
    public int     currentTabLevel = 0;    // default: start at column 1
    public int     indentWidth     = 2;    // default: indentation is two spaces
    public String  tab             = "  "; // (the two spaces)
    public boolean showTabs        = false;// (for debugging)

    public StringBuilder xml;

    protected PrettyPrinter() { }

    protected void injectXmlStringBuilder( StringBuilder sb )
    {
      this.xml = sb;
    }

    protected void reallocateTab()
    {
      char character = ( showTabs ) ? VISIBLE_TAB : SPACE;
      int  width = indentWidth;

      StringBuilder sb = new StringBuilder( width );
      while( width-- > 0 )
        sb.append( character );
      tab = sb.toString();
    }

    protected void nl()
    {
      xml.append( '\n' );
    }

    protected void tab()
    {
      for( int indent = 0; indent < currentTabLevel; indent++ )
        xml.append( tab );
    }

    protected void newline()
    {
      if( enabled )
        nl();
    }

    protected void indent()
    {
      if( enabled )
        tab();
    }

    protected void unindent()
    {
      // do nothing...
    }

    protected void newlineAndIndent()
    {
      if( enabled )
      {
        nl();
        tab();
      }
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder( "{" );

      sb.append( "\n  hashCode = " + this.hashCode() );
      sb.append( "\n  enabled = " + enabled );
      sb.append( "\n  currentTabLevel = " + currentTabLevel );
      sb.append( "\n  indentWidth = " + indentWidth );
      sb.append( "\n  tab = " + tab );
      sb.append( "\n  xml = " + xml.toString() );

      return sb.append( "\n}" ).toString();
    }
  }
}
