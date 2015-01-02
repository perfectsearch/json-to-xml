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
package com.perfectsearchcorp.testfodder;

/**
 * A selection of JSON snippets to use in testing. Each of these was
 * developed to test or verify regression for some feature or bug
 * encountered.
 *
 * @author Russell Bateman
 * @since December 2014
 */
public class JsonToXmlTestFodder
{
  public static final String SIMPLE_CONTENT  = "{ \"key\" : \"value\" }";
  public static final String ARRAY_CONTENT   = "{ \"rgb-color\" : [ \"red\", \"blue\", \"green\" ] }";
  public static final String ARRAY_CONTENT2  = "{ \"rgb-color\" : [ \"red\", \"blue\", { \"color\" : \"purple\" } ] }";
  public static final String COMPLEX_CONTENT = "{ \"monitor\" : { \"size\" : \"2560x1440\", \"brand\" : \"Acer\" } }";
  public static final String SERIOUS_CONTENT = /* from http://json.org/example */
        "{"
      + "  \"glossary\" :"
      + "  {"
      + "    \"title\" : \"example glossary\","
      + "    \"GlossDiv\" :"
      + "    {"
      + "      \"subtitle\" : \"S\","
      + "      \"GlossList\" :"
      + "      {"
      + "        \"GlossEntry\" :"
      + "        {"
      + "          \"ID\" : \"SGML\","
      + "          \"SortAs\" : \"SGML\","
      + "          \"GlossTerm\" : \"Standard Generalized Markup Language\","
      + "          \"Acronym\" : \"SGML\","
      + "          \"Abbrev\" : \"ISO 8879:1986\","
      + "          \"GlossDef\" :"
      + "          {"
      + "            \"para\" : \"A meta-markup language.\","
      + "            \"GlossSeeAlso\" : [ \"GML\", \"XML\" ]"
      + "          },"
      + "          \"GlossSee\" : \"markup\""
      + "        }"
      + "      }"
      + "    }"
      + "  }"
      + "}";
  public static final String SERIOUS_CONTENT2 = /* from SERIOUS_CONTENT */
        "{"
      + "  \"GlossDef\" :"
      + "  {"
      + "    \"para\" : \"A meta-markup language.\","
      + "    \"GlossSeeAlso\" : [ \"GML\", \"XML\" ]"
      + "  },"
      + "  \"GlossSee\" : \"markup\""
      + "}";
  public static final String SERIOUS_CONTENT3 = /* from SERIOUS_CONTENT2 */
        "{"
      + "  \"xxxxxxxx\" :"
      + "  {"
      + "    \"zzzz\" : \"Fun with JSON.\","
      + "    \"yyyy\" : [ \"123\", \"456\" ]"
      + "  },"
      + "}";
  public static final String PERFECTSEARCH_CONTENT = /* from an e-mail memorandum */
        "{"
      + "  \"ip\" : \"127.0.0.1\","
      + "  \"nodeID\" : \"UNKNOWN\","
      + "  \"verb\" : \"GET\","
      + "  \"user\" : \"ANONYMOUS\","
      + "  \"parameters\" : \"UNKNOWN\","
      + "  \"level\" : \"INFO\","
      + "  \"url\" : \"/search?q=test\","
      + "  \"extraInfo\" : { \"username\" : null},"
      + "  \"client\" : \"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36\","
      + "  \"responseCode\" : \"200 OK\","
      + "  \"timestamp\" : \"02/Jul/2014:13:53:07 -0600\","
      + "  \"repozeAllow\" : \"true\","
      + "  \"requestID\" : \"1404330787.211\""
      + "}";
  public static final String PERFECTSEARCH_CONTENT2 = /* from PERFECTSEARCH_CONTENT */
        "{"
      + "  \"extraInfo\" : { \"username\" : null},"
      + "  \"client\" : \"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36\","
      + "}";
  public static final String SIMPLE_JSON  = "{ \"name\" : \"I'm a wide-mouthed frog!\" }";
  public static final String SIMPLE_ARRAY = "{ \"name\" : [ \"Princess Buttercup\", \"Westley\" ] }";
  public static final String COMPLEX_JSON = "{ \"monitor\" : { \"size\" : \"2560x1440\", \"brand\" : \"Acer\" } }";
}
