/*
 * Copyright 2020 kec.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hl7.tinkar.lombok.dto.json.parser;


/**
 * Original obtained from: https://github.com/fangyidong/json-simple under Apache 2 license
 * Original project had no support for Java Platform Module System, and not updated for 8 years. 
 * Integrated here to integrate with Java Platform Module System. 
 * 
 * A simplified and stoppable SAX-like content handler for stream processing of
 * JSON text.
 *
 * @see org.xml.sax.ContentHandler
 * @see org.hl7.tinkar.parser.JSONParser#parse(java.io.Reader, ContentHandler,
 * boolean)
 *
 * @author FangYidong<fangyidong@yahoo.com.cn>
 */
public interface ContentHandler {

    /**
     * Receive notification of the beginning of JSON processing.The parser will
     * invoke this method only once.
     *
     * @throws ParseException - JSONParser will stop and throw the same
     * exception to the caller when receiving this exception.
     */
    void startJSON() throws ParseException;

    /**
     * Receive notification of the end of JSON processing.
     *
     * @throws ParseException
     */
    void endJSON() throws ParseException;

    /**
     * Receive notification of the beginning of a JSON object.
     *
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException - JSONParser will stop and throw the same
     * exception to the caller when receiving this exception.
     * @see #endJSON
     */
    boolean startObject() throws ParseException;

    /**
     * Receive notification of the end of a JSON object.
     *
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException
     *
     * @see #startObject
     */
    boolean endObject() throws ParseException;

    /**
     * Receive notification of the beginning of a JSON object entry.
     *
     * @param key - Key of a JSON object entry.
     *
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException
     *
     * @see #endObjectEntry
     */
    boolean startObjectEntry(String key) throws ParseException;

    /**
     * Receive notification of the end of the value of previous object entry.
     *
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException
     *
     * @see #startObjectEntry
     */
    boolean endObjectEntry() throws ParseException;

    /**
     * Receive notification of the beginning of a JSON array.
     *
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException
     *
     * @see #endArray
     */
    boolean startArray() throws ParseException;

    /**
     * Receive notification of the end of a JSON array.
     *
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException
     *
     * @see #startArray
     */
    boolean endArray() throws ParseException;

    /**
     * Receive notification of the JSON primitive values: java.lang.String,
     * java.lang.Number, java.lang.Boolean null
     *
     * @param value - Instance of the following: java.lang.String,
     * java.lang.Number, java.lang.Boolean null
     *
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException
     */
    boolean primitive(Object value) throws ParseException;

}
