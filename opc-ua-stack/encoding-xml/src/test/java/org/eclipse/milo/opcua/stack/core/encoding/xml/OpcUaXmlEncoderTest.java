/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;

class OpcUaXmlEncoderTest {

  private final EncodingContext context = DefaultEncodingContext.INSTANCE;

  @Test
  void writeBoolean() throws IOException, TransformerException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.encodeBoolean("Boolean", true);

    OpcUaXmlDecoder decoder =
      new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));

    assertEquals(true,decoder.decodeBoolean("Boolean"));

    encoder.reset();
    encoder.encodeBoolean("booleanValue", false);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(false,decoder.decodeBoolean("booleanValue"));
  }

  @Test
  public void writeSByte() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.encodeSByte("null", Byte.MIN_VALUE);

    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));

    assertEquals(Byte.MIN_VALUE,decoder.decodeSByte("null"));

    encoder.reset();
    encoder.encodeSByte("foo", Byte.MAX_VALUE);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(Byte.MAX_VALUE,decoder.decodeSByte("foo"));
  }

  @Test
  public void writeInt16() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.encodeInt16("Int16", Short.MIN_VALUE);

    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));

    assertEquals(Short.MIN_VALUE,decoder.decodeInt16("Int16"));

    encoder.reset();
    encoder.encodeInt16("shortValue", Short.MAX_VALUE);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(Short.MAX_VALUE,decoder.decodeInt16("shortValue"));
  }

  @Test
  public void writeInt32() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.encodeInt32("Int32", Integer.MIN_VALUE);

    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));

    assertEquals(Integer.MIN_VALUE,decoder.decodeInt32("Int32"));

    encoder.reset();
    encoder.encodeInt32("integerValue", Integer.MAX_VALUE);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(Integer.MAX_VALUE,decoder.decodeInt32("integerValue"));
  }

  @Test
  public void writeInt64() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.encodeInt64("Int64", Long.MIN_VALUE);

    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));

    assertEquals(Long.MIN_VALUE,decoder.decodeInt64("Int64"));

    encoder.reset();
    encoder.encodeInt64("longValue", Long.MAX_VALUE);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(Long.MAX_VALUE,decoder.decodeInt64("longValue"));
  }

  @Test
  public void writeByte() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.encodeByte("Byte", UByte.MIN);

    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));

    assertEquals(UByte.MIN,decoder.decodeByte("Byte"));

    encoder.reset();
    encoder.encodeByte("byteValue", UByte.MAX);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(UByte.MAX,decoder.decodeByte("byteValue"));
  }

  @Test
  public void writeUInt16() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.encodeUInt16("UInt16", ushort(0));

    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));

    assertEquals(ushort(0),decoder.decodeUInt16("UInt16"));

    encoder.reset();
    encoder.encodeUInt16("uInt16Value", UShort.MAX);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(UShort.MAX,decoder.decodeUInt16("uInt16Value"));
  }

  @Test
  public void writeUInt32() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.encodeUInt32("UInt32", uint(0));

    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));

    assertEquals(uint(0),decoder.decodeUInt32("UInt32"));

    encoder.reset();
    encoder.encodeUInt32("uInt32Value", UInteger.MAX);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(UInteger.MAX,decoder.decodeUInt32("uInt32Value"));
  }

  @Test
  public void writeUInt64() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.encodeUInt64("UInt64", ULong.MIN);

    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));

    assertEquals(ULong.MIN,decoder.decodeUInt64("UInt64"));

    encoder.reset();
    encoder.encodeUInt64("uInt64Value", ULong.MAX);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(ULong.MAX,decoder.decodeUInt64("uInt64Value"));
  }

  @Test
  public void writeFloat() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.encodeFloat("Float", Float.MIN_VALUE);

    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));

    assertEquals(Float.MIN_VALUE,decoder.decodeFloat("Float"));

    encoder.reset();
    encoder.encodeFloat("floatValue", Float.MAX_VALUE);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(Float.MAX_VALUE,decoder.decodeFloat("floatValue"));

    // Special floating-point numbers such as positive infinity (INF),
    // negative infinity (-INF) and not-a- number (NaN) shall be
    // represented by the values "Infinity", "-Infinity" and "NaN" encoded
    // as a XML string.

    encoder.reset();
    encoder.encodeFloat("floatValue", Float.POSITIVE_INFINITY);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(Float.POSITIVE_INFINITY,decoder.decodeFloat("floatValue"));

    encoder.reset();
    encoder.encodeFloat("floatValue", Float.NEGATIVE_INFINITY);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(Float.NEGATIVE_INFINITY,decoder.decodeFloat("floatValue"));

    encoder.reset();
    encoder.encodeFloat("floatValue", Float.NaN);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(Float.NaN,decoder.decodeFloat("floatValue"));
  }

  @Test
  public void writeDouble() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.encodeDouble("Double", Double.MIN_VALUE);

    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));

    assertEquals(Double.MIN_VALUE,decoder.decodeDouble("Double"));

    encoder.reset();
    encoder.encodeDouble("doubleValue", Double.MAX_VALUE);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(Double.MAX_VALUE,decoder.decodeDouble("doubleValue"));

    // Special floating-point numbers such as positive infinity (INF),
    // negative infinity (-INF) and not-a- number (NaN) shall be
    // represented by the values "Infinity", "-Infinity" and "NaN" encoded
    // as a XML string.

    encoder.reset();
    encoder.encodeDouble("doubleValue", Double.POSITIVE_INFINITY);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(Double.POSITIVE_INFINITY,decoder.decodeDouble("doubleValue"));

    encoder.reset();
    encoder.encodeDouble("doubleValue", Double.NEGATIVE_INFINITY);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(Double.NEGATIVE_INFINITY,decoder.decodeDouble("doubleValue"));

    encoder.reset();
    encoder.encodeDouble("doubleValue", Double.NaN);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(Double.NaN,decoder.decodeDouble("doubleValue"));
  }

  @Test
  public void writeString() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.encodeString("String", "");

    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));

    assertEquals("",decoder.decodeString("String"));

    encoder.reset();
    encoder.encodeString("stringValue", "foo");
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals("foo",decoder.decodeString("stringValue"));

    encoder.reset();
    encoder.encodeString("stringValue", "\"foo\"");
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals("\"foo\"",decoder.decodeString("stringValue"));
  }

  @Test
  public void writeDateTime() throws IOException, SAXException {
    // DateTime values shall be formatted as specified by ISO 8601:2004
    // and encoded as a XML string.
    var encoder = new OpcUaXmlEncoder(context);

    encoder.encodeDateTime("DateTime", new DateTime(DateTime.MIN_ISO_8601_INSTANT));

    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));

    assertEquals(new DateTime(DateTime.MIN_ISO_8601_INSTANT),decoder.decodeDateTime("DateTime"));

    encoder.reset();
    encoder.encodeDateTime("dateTimeValue", new DateTime(DateTime.MAX_ISO_8601_INSTANT));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new DateTime(DateTime.MAX_ISO_8601_INSTANT),decoder.decodeDateTime("dateTimeValue"));

    // DateTime values which exceed the minimum or maximum values supported
    // on a platform shall be encoded as "0001-01-01T00:00:00Z" or
    // "9999-12-31T23:59:59Z" respectively. During decoding, these values
    // shall be converted to the minimum or maximum values supported on the
    // platform.

    encoder.reset();
    encoder.encodeDateTime("dateTimeValue", new DateTime(DateTime.MIN_ISO_8601_INSTANT.minus(1, ChronoUnit.SECONDS)));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new DateTime(DateTime.MIN_ISO_8601_INSTANT),decoder.decodeDateTime("dateTimeValue"));

    encoder.reset();
    encoder.encodeDateTime("dateTimeValue", new DateTime(DateTime.MAX_ISO_8601_INSTANT.plus(1, ChronoUnit.SECONDS)));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new DateTime(DateTime.MAX_ISO_8601_INSTANT),decoder.decodeDateTime("dateTimeValue"));
  }

  @Test
  public void writeGuid() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.encodeGuid("GUID", UUID.fromString("00000000-0000-0000-0000-000000000000"));

    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));

    assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"),decoder.decodeGuid("GUID"));

    encoder.reset();
    UUID uuid = UUID.randomUUID();
    encoder.encodeGuid("guidValue", uuid);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(uuid,decoder.decodeGuid("guidValue"));
  }

  @Test
  public void writeByteString() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    // ByteString values shall be formatted as a Base64 text and encoded as
    // a JSON string.
    // Any characters which are not allowed in JSON strings are escaped
    // using the rules defined in RFC 7159.

    for (int i = 1; i < 100; i++) {
      ByteString bs = ByteString.of(randomBytes(16 * i));
      encoder.reset();
      encoder.encodeByteString("ByteString", bs);
      assertEquals(
          "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><ByteString xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">"
              + Base64.getEncoder().encodeToString(bs.bytes())
              + "</ByteString>",
          encoder.getDocumentXml());
    }

    ByteString bs = ByteString.of(randomBytes(16 ));
    encoder.reset();
    encoder.encodeByteString("ByteString", bs);
    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE)
            .setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(bs, decoder.decodeByteString("ByteString"));
  }

  @Test
  public void writeXmlElement() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.encodeXmlElement("XmlElement", new XmlElement("<foo>bar</foo>"));

    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new XmlElement("<foo>bar</foo>"),decoder.decodeXmlElement("XmlElement"));

    encoder.reset();
    encoder.encodeXmlElement("XmlValue", new XmlElement("<foo>bar</foo>"));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new XmlElement("<foo>bar</foo>"),decoder.decodeXmlElement("XmlValue"));
  }

  @Test
  public void writeNodeId() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    // IdType == UInt32, Namespace = 0, reversible
    encoder.encodeNodeId("UInt32", new NodeId(0, uint(0)));
    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new NodeId(0, uint(0)), decoder.decodeNodeId("UInt32"));

    // IdType == UInt32, Namespace != 0, reversible
    encoder.reset();
    encoder.encodeNodeId("UInt32", new NodeId(1, uint(0)));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new NodeId(1, uint(0)), decoder.decodeNodeId("UInt32"));

    // IdType == String, Namespace = 0, reversible
    encoder.reset();
    encoder.encodeNodeId("String", new NodeId(0, "foo"));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new NodeId(0, "foo"), decoder.decodeNodeId("String"));

    // IdType == String, Namespace != 0, reversible
    encoder.reset();
    encoder.encodeNodeId("String", new NodeId(1, "foo"));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new NodeId(1, "foo"), decoder.decodeNodeId("String"));

    // IdType == Guid, Namespace = 0, reversible
    UUID uuid = UUID.randomUUID();
    encoder.reset();
    encoder.encodeNodeId("Guid", new NodeId(0, uuid));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new NodeId(0, uuid), decoder.decodeNodeId("Guid"));

    // IdType == Guid, Namespace != 0, reversible
    encoder.reset();
    encoder.encodeNodeId("Guid", new NodeId(1, uuid));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new NodeId(1, uuid), decoder.decodeNodeId("Guid"));

    // IdType == ByteString, Namespace = 0, reversible
    ByteString bs = ByteString.of(randomBytes(16));
    encoder.reset();
    encoder.encodeNodeId("ByteString", new NodeId(0, bs));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new NodeId(0, bs), decoder.decodeNodeId("ByteString"));

    // IdType == ByteString, Namespace != 0, reversible
    encoder.reset();
    encoder.encodeNodeId("ByteString", new NodeId(1, bs));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new NodeId(1, bs), decoder.decodeNodeId("ByteString"));


    encoder.reversible = false;
    encoder.encodingContext = new DefaultEncodingContext();
    encoder.encodingContext.getNamespaceTable().add("urn:eclipse:milo:test1");
    encoder.encodingContext.getNamespaceTable().add("urn:eclipse:milo:test2");

    // IdType == UInt32, Namespace = 0, non-reversible
    encoder.reset();
    encoder.encodeNodeId("Uint32", new NodeId(0, uint(0)));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new NodeId(0, uint(0)), decoder.decodeNodeId("Uint32"));

    // IdType == UInt32, Namespace = 1, non-reversible
    // IdType == UInt32, Namespace = 0, non-reversible
    encoder.reset();
    encoder.encodeNodeId("Uint32", new NodeId(1, uint(0)));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new NodeId(1, uint(0)), decoder.decodeNodeId("Uint32"));


    // IdType == UInt32, Namespace > 1, non-reversible
    encoder.reset();
    encoder.encodeNodeId("Uint32", new NodeId(2, uint(0)));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new NodeId(2, uint(0)), decoder.decodeNodeId("Uint32"));

    // Namespace > 1 but not in table, non-reversible
    encoder.reset();
    encoder.encodeNodeId("Uint32", new NodeId(99, uint(0)));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new NodeId(99, uint(0)), decoder.decodeNodeId("Uint32"));

    // key != null
    //encoder.reset();
    //encoder.jsonWriter.beginObject();
    //encoder.encodeNodeId("foo", new NodeId(1, "foo"));
    //encoder.jsonWriter.endObject();
    //assertEquals("{\"foo\":{\"IdType\":1,\"Id\":\"foo\",\"Namespace\":1}}", writer.toString());
  }

  @Test
  public void writeExpandedNodeId() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    // Two things differentiate the encoding of ExpandedNodeId from NodeId:
    // 1. if the namespace URI is specified it is encoded in the "Namespace" field
    // 2. if the ExpandedNodeId is non-local (server index > 0) it is encoded in the "ServerUri"
    // field

    // reversible, namespace URI specified
    encoder.encodeExpandedNodeId("ExpandedNodeId", new ExpandedNodeId(ushort(0), Namespaces.OPC_UA, "foo"));
    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));

    assertEquals(new ExpandedNodeId(ushort(0), Namespaces.OPC_UA, "foo"),
            decoder.decodeExpandedNodeId("ExpandedNodeId"));

    // reversible, remote server index
    encoder.reset();
    encoder.encodeExpandedNodeId("ExpandedNodeId", new ExpandedNodeId(ushort(0), null, "foo", uint(1)));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new ExpandedNodeId(ushort(0), null, "foo", uint(1)),
            decoder.decodeExpandedNodeId("ExpandedNodeId"));

    // non-reversible, remote server index
    //encoder.reversible = false;
    encoder.encodingContext = new DefaultEncodingContext();
    encoder.encodingContext.getServerTable().add("urn:server:local");
    encoder.encodingContext.getServerTable().add("urn:server:remote");
    encoder.reset();
    encoder.encodeExpandedNodeId("ExpandedNodeId", new ExpandedNodeId(ushort(0), null, "foo", uint(1)));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new ExpandedNodeId(ushort(0), null, "foo", uint(1)),
            decoder.decodeExpandedNodeId("ExpandedNodeId"));

    // non-reversible, remote server index not in table
    encoder.reset();
    encoder.encodeExpandedNodeId("ExpandedNodeId", new ExpandedNodeId(ushort(0), null, "foo", uint(2)));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new ExpandedNodeId(ushort(0), null, "foo", uint(2)),
            decoder.decodeExpandedNodeId("ExpandedNodeId"));
  }

  @Test
  public void writeStatusCode() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    /*encoder.encodeStatusCode(null, StatusCode.GOOD);
    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(StatusCode.GOOD,decoder.decodeStatusCode(null));*/

    encoder.reset();
    encoder.encodeStatusCode(null, new StatusCode(StatusCodes.Uncertain_InitialValue));
    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(StatusCodes.Uncertain_InitialValue, decoder.decodeStatusCode(null).getValue());

    encoder.reset();
    encoder.encodeStatusCode(null, new StatusCode(StatusCodes.Bad_UnexpectedError));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(StatusCodes.Bad_UnexpectedError, decoder.decodeStatusCode(null).getValue());
  }

  @Test
  public void writeQualifiedName() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.reset();
    encoder.encodeQualifiedName(null, new QualifiedName(0, "foo"));
    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new QualifiedName(0, "foo"), decoder.decodeQualifiedName(null));

    encoder.reset();
    encoder.encodeQualifiedName(null, new QualifiedName(1, "foo"));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new QualifiedName(1, "foo"), decoder.decodeQualifiedName(null));

    //encoder.reversible = false;
    encoder.encodingContext = new DefaultEncodingContext();
    encoder.encodingContext.getNamespaceTable().add("urn:eclipse:milo:test1");
    encoder.encodingContext.getNamespaceTable().add("urn:eclipse:milo:test2");

    encoder.reset();
    encoder.encodeQualifiedName(null, new QualifiedName(0, "foo"));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new QualifiedName(0, "foo"), decoder.decodeQualifiedName(null));

    encoder.reset();
    encoder.encodeQualifiedName(null, new QualifiedName(1, "foo"));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new QualifiedName(1, "foo"), decoder.decodeQualifiedName(null));

    encoder.reset();
    encoder.encodeQualifiedName(null, new QualifiedName(2, "foo"));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new QualifiedName(2, "foo"), decoder.decodeQualifiedName(null));

    encoder.reset();
    encoder.encodeQualifiedName(null, new QualifiedName(99, "foo"));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new QualifiedName(99, "foo"), decoder.decodeQualifiedName(null));

    encoder.reset();
    encoder.encodeQualifiedName("foo", new QualifiedName(0, "foo"));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new QualifiedName(0, "foo"), decoder.decodeQualifiedName("foo"));
  }

  @Test
  public void writeLocalizedText() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.reset();
    encoder.encodeLocalizedText(null, LocalizedText.english("foo"));
    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(LocalizedText.english("foo"), decoder.decodeLocalizedText(null));

    encoder.reset();
    encoder.encodeLocalizedText(null, new LocalizedText("en", null));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new LocalizedText("en", null), decoder.decodeLocalizedText(null));

    encoder.reset();
    encoder.encodeLocalizedText(null, new LocalizedText(null, "foo"));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new LocalizedText(null, "foo"), decoder.decodeLocalizedText(null));

    encoder.reset();
    encoder.encodeLocalizedText(null, new LocalizedText(null, null));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(new LocalizedText(null, null),decoder.decodeLocalizedText(null));

    encoder.reversible = false;
    encoder.reset();
    encoder.encodeLocalizedText(null, LocalizedText.english("foo"));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(LocalizedText.english("foo"),decoder.decodeLocalizedText(null));

    encoder.reversible = true;

    encoder.reset();
    encoder.encodeLocalizedText("foo", LocalizedText.english("foo"));
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertEquals(LocalizedText.english("foo"),decoder.decodeLocalizedText("foo"));
  }

  @Test
  public void writeExtensionObject() {
    var writer = new StringWriter();
    var encoder = new OpcUaXmlEncoder(context);

    var byteStringXo =
            new ExtensionObject(ByteString.of(new byte[] {0x00, 0x01, 0x02, 0x03}), new NodeId(2, 42));

    var xmlElementXo = new ExtensionObject(new XmlElement("<foo>bar</foo>"), new NodeId(2, 42));

    var jsonStringXo = new ExtensionObject("{\"foo\":\"bar\",\"baz\":42}", new NodeId(2, 42));

    encoder.encodeExtensionObject(null, jsonStringXo);
    assertEquals(
            "{\"TypeId\":{\"Id\":42,\"Namespace\":2},\"Body\":{\"foo\":\"bar\",\"baz\":42}}",
            writer.toString());

    encoder.reset();
    encoder.encodeExtensionObject(null, xmlElementXo);
    assertEquals(
            "{\"TypeId\":{\"Id\":42,\"Namespace\":2},\"Encoding\":2,\"Body\":\"<foo>bar</foo>\"}",
            writer.toString());

    encoder.reset();
    encoder.encodeExtensionObject(null, byteStringXo);
    assertEquals(
            "{\"TypeId\":{\"Id\":42,\"Namespace\":2},\"Encoding\":1,\"Body\":\"AAECAw==\"}",
            writer.toString());

    //encoder.reversible = false;
    encoder.reset();
    encoder.encodeExtensionObject(null, jsonStringXo);
    assertEquals("{\"foo\":\"bar\",\"baz\":42}", writer.toString());

    encoder.reset();
    encoder.encodeExtensionObject(null, xmlElementXo);
    assertEquals("\"<foo>bar</foo>\"", writer.toString());

    encoder.reset();
    encoder.encodeExtensionObject(null, byteStringXo);
    assertEquals("\"AAECAw==\"", writer.toString());

    encoder.reset();
    encoder.encodeExtensionObject(null, null);
    assertEquals("null", writer.toString());
  }

  @Test
  public void writeDataValue() throws IOException {
    var writer = new StringWriter();
    var encoder = new OpcUaXmlEncoder(context);

    DateTime now = DateTime.now();
    String isoNow = now.toIso8601String();

    DataValue allFieldsValue =
            new DataValue(
                    new Variant("foo"),
                    new StatusCode(StatusCodes.Good_Overload),
                    now,
                    ushort(100),
                    now,
                    ushort(200));

    encoder.reset();
    encoder.encodeDataValue(null, allFieldsValue);
    assertEquals(
            String.format(
                    "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}",
                    isoNow, isoNow),
            writer.toString());

    // omit "Value"
    encoder.reset();
    encoder.encodeDataValue(null, allFieldsValue.copy(b -> b.setValue(Variant.NULL_VALUE)));
    assertEquals(
            String.format(
                    "{\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}",
                    isoNow, isoNow),
            writer.toString());

    // omit "Status"
    encoder.reset();
    encoder.encodeDataValue(null, allFieldsValue.copy(b -> b.setStatus(StatusCode.GOOD)));
    assertEquals(
            String.format(
                    "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}",
                    isoNow, isoNow),
            writer.toString());

    // omit "SourceTimestamp"
    encoder.reset();
    encoder.encodeDataValue(null, allFieldsValue.copy(b -> b.setSourceTime(null)));
    assertEquals(
            String.format(
                    "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}",
                    isoNow),
            writer.toString());

    // omit "SourcePicoseconds"
    encoder.reset();
    encoder.encodeDataValue(null, allFieldsValue.copy(b -> b.setSourcePicoseconds(null)));
    assertEquals(
            String.format(
                    "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"ServerTimestamp\":\"%s\",\"ServerPicoseconds\":200}",
                    isoNow, isoNow),
            writer.toString());

    // omit "ServerTimestamp"
    encoder.reset();
    encoder.encodeDataValue(null, allFieldsValue.copy(b -> b.setServerTime(null)));
    assertEquals(
            String.format(
                    "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerPicoseconds\":200}",
                    isoNow),
            writer.toString());

    // omit "ServerPicoseconds"
    encoder.reset();
    encoder.encodeDataValue(null, allFieldsValue.copy(b -> b.setServerPicoseconds(null)));
    assertEquals(
            String.format(
                    "{\"Value\":{\"Type\":12,\"Body\":\"foo\"},\"Status\":3080192,\"SourceTimestamp\":\"%s\",\"SourcePicoseconds\":100,\"ServerTimestamp\":\"%s\"}",
                    isoNow, isoNow),
            writer.toString());

    // omit all fields
    encoder.reset();
    encoder.encodeDataValue(null, new DataValue(Variant.NULL_VALUE, null, null));
    assertEquals("", writer.toString());

    // omit all fields while embedded in object
    encoder.reset();
    //encoder.jsonWriter.beginObject();
    encoder.encodeDataValue("foo", new DataValue(Variant.NULL_VALUE, null, null));
    //encoder.jsonWriter.endObject();
    assertEquals("{}", writer.toString());
  }

  @Test
  public void writeVariant() {
    var writer = new StringWriter();
    var encoder = new OpcUaXmlEncoder(context);

    // region reversible
    encoder.reset();
    encoder.encodeVariant(null, new Variant(true));
    assertEquals("{\"Type\":1,\"Body\":true}", writer.toString());

    encoder.reset();
    encoder.encodeVariant(null, new Variant(new QualifiedName(1, "foo")));
    assertEquals("{\"Type\":20,\"Body\":{\"Name\":\"foo\",\"Uri\":1}}", writer.toString());

    encoder.reset();
    encoder.encodeVariant(
            null, new Variant(new Variant[] {new Variant("foo"), new Variant("bar")}));
    assertEquals(
            "{\"Type\":24,\"Body\":[{\"Type\":12,\"Body\":\"foo\"},{\"Type\":12,\"Body\":\"bar\"}]}",
            writer.toString());

    encoder.reset();
    encoder.encodeVariant(null, new Variant(Matrix.ofInt32(new int[][] {{0, 1}, {2, 3}})));
    assertEquals("{\"Type\":6,\"Body\":[0,1,2,3],\"Dimensions\":[2,2]}", writer.toString());
    // endregion

    // region non-reversible
    //encoder.reversible = false;
    encoder.reset();
    encoder.encodeVariant(null, new Variant(true));
    assertEquals("true", writer.toString());

    encoder.reset();
    encoder.encodeVariant(null, new Variant(new QualifiedName(1, "foo")));
    assertEquals("{\"Name\":\"foo\",\"Uri\":1}", writer.toString());

    encoder.reset();
    encoder.encodeVariant(
            null, new Variant(new Variant[] {new Variant("foo"), new Variant("bar")}));
    assertEquals("[\"foo\",\"bar\"]", writer.toString());
    // endregion

    int[] value1d = {0, 1, 2, 3};
    int[][] value2d = {
            {0, 2, 3},
            {1, 3, 4}
    };
    int[][][] value3d = {
            {
                    {0, 1},
                    {2, 3}
            },
            {
                    {4, 5},
                    {6, 7},
            }
    };

    // region Arrays, reversible
    //encoder.reversible = true;
    encoder.reset();
    encoder.encodeVariant(null, new Variant(value1d));
    assertEquals("{\"Type\":6,\"Body\":[0,1,2,3]}", writer.toString());

    encoder.reset();
    encoder.encodeVariant(null, new Variant(Matrix.ofInt32(value2d)));
    assertEquals("{\"Type\":6,\"Body\":[0,2,3,1,3,4],\"Dimensions\":[2,3]}", writer.toString());

    encoder.reset();
    encoder.encodeVariant(null, new Variant(Matrix.ofInt32(value3d)));
    assertEquals(
            "{\"Type\":6,\"Body\":[0,1,2,3,4,5,6,7],\"Dimensions\":[2,2,2]}", writer.toString());
    // endregion

    // region Arrays, non-reversible
    //encoder.reversible = false;

    encoder.reset();
    encoder.encodeVariant(null, new Variant(value1d));
    assertEquals("[0,1,2,3]", writer.toString());

    encoder.reset();
    encoder.encodeVariant(null, new Variant(Matrix.ofInt32(value2d)));
    assertEquals("[[0,2,3],[1,3,4]]", writer.toString());

    encoder.reset();
    encoder.encodeVariant(null, new Variant(Matrix.ofInt32(value3d)));
    assertEquals("[[[0,1],[2,3]],[[4,5],[6,7]]]", writer.toString());
    // endregion
  }

  @Test
  public void writeDiagnosticInfo() throws IOException {
    var writer = new StringWriter();
    var encoder = new OpcUaXmlEncoder(context);

    var diagnosticInfo = new DiagnosticInfo(0, 1, 2, 3, "foo", null, null);

    var nestedDiagnosticInfo =
            new DiagnosticInfo(4, 5, 6, 7, "bar", StatusCode.GOOD, diagnosticInfo);

    encoder.encodeDiagnosticInfo(null, diagnosticInfo);
    assertEquals(
            "{\"SymbolicId\":1,\"NamespaceUri\":0,\"Locale\":2,\"LocalizedText\":3,\"AdditionalInfo\":\"foo\"}",
            writer.toString());

    encoder.reset();
    encoder.encodeDiagnosticInfo(null, nestedDiagnosticInfo);
    assertEquals(
            "{\"SymbolicId\":5,\"NamespaceUri\":4,\"Locale\":6,\"LocalizedText\":7,\"AdditionalInfo\":\"bar\",\"InnerStatusCode\":0,\"InnerDiagnosticInfo\":{\"SymbolicId\":1,\"NamespaceUri\":0,\"Locale\":2,\"LocalizedText\":3,\"AdditionalInfo\":\"foo\"}}",
            writer.toString());

    encoder.reset();
    //encoder.jsonWriter.beginObject();
    encoder.encodeDiagnosticInfo("foo", diagnosticInfo);
    //encoder.jsonWriter.endObject();
    assertEquals(
            "{\"foo\":{\"SymbolicId\":1,\"NamespaceUri\":0,\"Locale\":2,\"LocalizedText\":3,\"AdditionalInfo\":\"foo\"}}",
            writer.toString());
  }

  @Test
  public void writeMessage() {
    var writer = new StringWriter();
    var encoder = new OpcUaXmlEncoder(context);

    var message =
            new ReadRequest(
                    new RequestHeader(
                            NodeId.NULL_VALUE, DateTime.NULL_VALUE, uint(0), uint(0), "foo", uint(0), null),
                    0.0,
                    TimestampsToReturn.Both,
                    new ReadValueId[] {
                            new ReadValueId(new NodeId(0, 1), uint(13), null, QualifiedName.NULL_VALUE)
                    });

    encoder.reset();
    encoder.encodeMessage(null, message);
    assertEquals(
            "{\"TypeId\":{\"Id\":15257},\"Body\":{\"RequestHeader\":{\"Timestamp\":\"1601-01-01T00:00:00Z\",\"AuditEntryId\":\"foo\"},\"TimestampsToReturn\":2,\"NodesToRead\":[{\"NodeId\":{\"Id\":1},\"AttributeId\":13}]}}",
            writer.toString());
  }

  @Test
  public void writeEnum() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE);

    for (ApplicationType applicationType : ApplicationType.values()) {
      encoder.reset();
      encoder.encodeEnum("Enum", applicationType);
      decoder.setInput(new StringReader(encoder.getDocumentXml()));
      assertEquals(applicationType.getValue(),decoder.decodeEnum("Enum"));
    }
  }

  @Test
  public void writeStruct() {
    var writer = new StringWriter();
    var encoder = new OpcUaXmlEncoder(context);

    var struct = new Argument("foo", NodeIds.Int32, -1, null, LocalizedText.english("foo desc"));

    encoder.encodeStruct(null, struct, Argument.TYPE_ID);
    assertEquals(
            "{\"Name\":\"foo\",\"DataType\":{\"Id\":6},\"ValueRank\":-1,\"Description\":{\"Locale\":\"en\",\"Text\":\"foo"
                    + " desc\"}}",
            writer.toString());
  }

  @Test
  public void writeBooleanArray() throws IOException, SAXException {
    var encoder = new OpcUaXmlEncoder(context);

    encoder.reset();
    encoder.encodeBooleanArray(null, new Boolean[] {true, false, true});
    OpcUaXmlDecoder decoder =
            new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(encoder.getDocumentXml()));
    assertArrayEquals(new Boolean[] {true, false, true},decoder.decodeBooleanArray(null));

    encoder.reset();
    encoder.encodeBooleanArray("foo", new Boolean[] {true, false, null});
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertArrayEquals(new Boolean[] {true, false},decoder.decodeBooleanArray("foo"));

    encoder.reset();
    encoder.encodeBooleanArray("foo", new Boolean[] {true, false, true});
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertArrayEquals(new Boolean[] {true, false, true},decoder.decodeBooleanArray("foo"));

    encoder.reset();
    encoder.encodeBooleanArray("foo", null);
    decoder.setInput(new StringReader(encoder.getDocumentXml()));
    assertNull(decoder.decodeBooleanArray("foo"));
  }

  @Test
  void encodeMatrix() throws IOException {
    var writer = new StringWriter();
    var encoder = new OpcUaXmlEncoder(context);

    var matrix2d =
            new Matrix(
                    new Integer[][] {
                            new Integer[] {0, 1},
                            new Integer[] {2, 3}
                    });

    var matrix3d =
            new Matrix(
                    new Integer[][][] {
                            new Integer[][] {{0, 1}, {2, 3}},
                            new Integer[][] {{4, 5}, {6, 7}}
                    });

    encoder.reset();
    encoder.encodeMatrix(null, matrix2d);
    assertEquals("[[0,1],[2,3]]", writer.toString());

    encoder.reset();
    encoder.encodeMatrix(null, matrix3d);
    assertEquals("[[[0,1],[2,3]],[[4,5],[6,7]]]", writer.toString());

    encoder.reset();
    //encoder.jsonWriter.beginObject();
    encoder.encodeMatrix("foo", matrix2d);
    //encoder.jsonWriter.endObject();
    assertEquals("{\"foo\":[[0,1],[2,3]]}", writer.toString());

    encoder.reset();
    //encoder.jsonWriter.beginObject();
    encoder.encodeMatrix("foo", matrix3d);
    //encoder.jsonWriter.endObject();
    assertEquals("{\"foo\":[[[0,1],[2,3]],[[4,5],[6,7]]]}", writer.toString());
  }

  @Test
  void encodeEnumMatrix() {
    var writer = new StringWriter();
    var encoder = new OpcUaXmlEncoder(context);

    var applicationTypes =
            new ApplicationType[][] {
                    new ApplicationType[] {ApplicationType.Server, ApplicationType.Client},
                    new ApplicationType[] {ApplicationType.ClientAndServer, ApplicationType.DiscoveryServer}
            };

    var matrix = new Matrix(applicationTypes);

    encoder.reset();
    encoder.encodeEnumMatrix(null, matrix);
    assertEquals("[[0,1],[2,3]]", writer.toString());
  }

  @Test
  void encodeStructMatrix() {
    var writer = new StringWriter();
    var encoder = new OpcUaXmlEncoder(context);

    XVType[][] xvTypes =
            new XVType[][] {
                    new XVType[] {new XVType(0.0d, 1.0f), new XVType(2.0d, 3.0f)},
                    new XVType[] {new XVType(4.0d, 5.0f), new XVType(6.0d, 7.0f)}
            };

    var matrix = new Matrix(xvTypes);

    encoder.reset();
    encoder.encodeStructMatrix(null, matrix, XVType.TYPE_ID);
    assertEquals(
            "[[{\"Value\":1.0},{\"X\":2.0,\"Value\":3.0}],[{\"X\":4.0,\"Value\":5.0},{\"X\":6.0,\"Value\":7.0}]]",
            writer.toString());
  }

  private static byte[] randomBytes(int length) {
    var random = new Random();
    var bs = new byte[length];
    random.nextBytes(bs);
    return bs;
  }
}
