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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Base64;
import java.util.UUID;
import java.util.function.BiConsumer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaMessageType;
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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.eclipse.milo.opcua.stack.core.util.SecureXmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class OpcUaXmlEncoder implements UaEncoder {

  private final DocumentBuilder builder;

  private Document document;
  private Node currentNode;

  EncodingContext encodingContext;

  public OpcUaXmlEncoder(EncodingContext context) {
    this.encodingContext = context;

    try {
      builder = SecureXmlUtil.SHARED_DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();

      document = builder.newDocument();
      currentNode = document;
    } catch (ParserConfigurationException e) {
      throw new UaRuntimeException(StatusCodes.Bad_InternalError, e);
    }
  }

  @Override
  public EncodingContext getEncodingContext() {
    return encodingContext;
  }

  public Document getDocument() {
    return document;
  }

  public String getDocumentXml() {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    try {
      Transformer transformer = transformerFactory.newTransformer();
      StringWriter stringWriter = new StringWriter();
      transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
      return stringWriter.toString();
    } catch(TransformerException e) {
      throw new UaRuntimeException(StatusCodes.Bad_InternalError, e);
    }
  }

  public void reset(){
    document = builder.newDocument();
    currentNode = document;
  }

  @Override
  public void encodeBoolean(String field, Boolean value) throws UaSerializationException {
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);

    element.appendChild(document.createTextNode(value.toString()));

    currentNode.appendChild(element);
  }

  @Override
  public void encodeSByte(String field, Byte value) throws UaSerializationException {
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);

    element.appendChild(document.createTextNode(value.toString()));

    currentNode.appendChild(element);
  }

  @Override
  public void encodeInt16(String field, Short value) throws UaSerializationException {
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);

    element.appendChild(document.createTextNode(value.toString()));

    currentNode.appendChild(element);
  }

  @Override
  public void encodeInt32(String field, Integer value) throws UaSerializationException {
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);

    element.appendChild(document.createTextNode(value.toString()));

    currentNode.appendChild(element);
  }

  @Override
  public void encodeInt64(String field, Long value) throws UaSerializationException {
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);

    element.appendChild(document.createTextNode(value.toString()));

    currentNode.appendChild(element);
  }

  @Override
  public void encodeByte(String field, UByte value) throws UaSerializationException {
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);

    element.appendChild(document.createTextNode(value.toString()));

    currentNode.appendChild(element);
  }

  @Override
  public void encodeUInt16(String field, UShort value) throws UaSerializationException {
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);

    element.appendChild(document.createTextNode(value.toString()));

    currentNode.appendChild(element);
  }

  @Override
  public void encodeUInt32(String field, UInteger value) throws UaSerializationException {
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);

    element.appendChild(document.createTextNode(value.toString()));

    currentNode.appendChild(element);}

  @Override
  public void encodeUInt64(String field, ULong value) throws UaSerializationException {
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);

    element.appendChild(document.createTextNode(value.toString()));

    currentNode.appendChild(element);
  }

  @Override
  public void encodeFloat(String field, Float value) throws UaSerializationException {
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);

    element.appendChild(document.createTextNode(value.toString()));

    currentNode.appendChild(element);
  }

  @Override
  public void encodeDouble(String field, Double value) throws UaSerializationException {
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);

    element.appendChild(document.createTextNode(value.toString()));

    currentNode.appendChild(element);
  }

  @Override
  public void encodeString(String field, String value) throws UaSerializationException {
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);

    element.appendChild(document.createTextNode(value));

    currentNode.appendChild(element);
  }

  @Override
  public void encodeDateTime(String field, DateTime value) throws UaSerializationException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone( ZoneId.of("UTC"));
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);
    if (value.getJavaInstant().toEpochMilli() >= DateTime.MIN_ISO_8601_INSTANT.toEpochMilli()) {
      if (value.getJavaInstant().toEpochMilli() <= DateTime.MAX_ISO_8601_INSTANT.toEpochMilli()) {
        element.appendChild(document.createTextNode(formatter.format(value.getJavaInstant())));
      } else {
        element.appendChild(document.createTextNode(formatter.format(DateTime.MAX_ISO_8601_INSTANT)));
      }
    } else {
      element.appendChild(document.createTextNode(formatter.format(DateTime.MIN_ISO_8601_INSTANT)));
    }
    currentNode.appendChild(element);
  }

  @Override
  public void encodeGuid(String field, UUID value) throws UaSerializationException {
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);

    element.appendChild(document.createTextNode(value.toString())); //Java UUID is GUID in OPCUA

    currentNode.appendChild(element);
  }

  @Override
  public void encodeByteString(String field, ByteString value) throws UaSerializationException {
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);

    element.appendChild(document.createTextNode(Base64.getEncoder().encodeToString(value.bytes())));

    currentNode.appendChild(element);
  }

  @Override
  public void encodeXmlElement(String field, XmlElement value) throws UaSerializationException {
    Document newDocument;
    try {
      newDocument = builder.parse(new InputSource(new StringReader(value.getFragmentOrEmpty())));
    } catch (SAXException e) {
      throw new UaRuntimeException(e);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

   // element.setTextContent(value.getFragmentOrEmpty());
    //Node element = document.createElement(field);
    //element.appendChild(document.createTextNode(value.getFragmentOrEmpty()));
    currentNode.appendChild( document.importNode(newDocument.getDocumentElement(), true));
  }

  @Override
  public void encodeNodeId(String field, NodeId value) throws UaSerializationException {
    Node element = document.createElementNS(Namespaces.OPC_UA_XSD, field);

    element.appendChild(document.createTextNode(value.toString())); //

    currentNode.appendChild(element);
  }

  @Override
  public void encodeExpandedNodeId(String field, ExpandedNodeId value)
      throws UaSerializationException {}

  @Override
  public void encodeStatusCode(String field, StatusCode value) throws UaSerializationException {}

  @Override
  public void encodeQualifiedName(String field, QualifiedName value)
      throws UaSerializationException {}

  @Override
  public void encodeLocalizedText(String field, LocalizedText value)
      throws UaSerializationException {}

  @Override
  public void encodeExtensionObject(String field, ExtensionObject value)
      throws UaSerializationException {}

  @Override
  public void encodeDataValue(String field, DataValue value) throws UaSerializationException {}

  @Override
  public void encodeVariant(String field, Variant value) throws UaSerializationException {}

  @Override
  public void encodeDiagnosticInfo(String field, DiagnosticInfo value)
      throws UaSerializationException {}

  @Override
  public void encodeMessage(String field, UaMessageType message) throws UaSerializationException {}

  @Override
  public void encodeEnum(String field, UaEnumeratedType value) {
    encodeString(field, String.format("%s_%s", value.getName(), value.getValue()));
  }

  @Override
  public void encodeStruct(String field, Object value, NodeId dataTypeId)
      throws UaSerializationException {}

  @Override
  public void encodeStruct(String field, Object value, ExpandedNodeId dataTypeId)
      throws UaSerializationException {
    NodeId localDateTypeId =
        dataTypeId
            .toNodeId(encodingContext.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError, "no codec registered: " + dataTypeId));

    encodeStruct(field, value, localDateTypeId);
  }

  @Override
  public void encodeStruct(String field, Object value, DataTypeCodec codec)
      throws UaSerializationException {}

  @Override
  public void encodeBooleanArray(String field, Boolean[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeBoolean);
  }

  @Override
  public void encodeSByteArray(String field, Byte[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeSByte);
  }

  @Override
  public void encodeInt16Array(String field, Short[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeInt16);
  }

  @Override
  public void encodeInt32Array(String field, Integer[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeInt32);
  }

  @Override
  public void encodeInt64Array(String field, Long[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeInt64);
  }

  @Override
  public void encodeByteArray(String field, UByte[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeByte);
  }

  @Override
  public void encodeUInt16Array(String field, UShort[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeUInt16);
  }

  @Override
  public void encodeUInt32Array(String field, UInteger[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeUInt32);
  }

  @Override
  public void encodeUInt64Array(String field, ULong[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeUInt64);
  }

  @Override
  public void encodeFloatArray(String field, Float[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeFloat);
  }

  @Override
  public void encodeDoubleArray(String field, Double[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeDouble);
  }

  @Override
  public void encodeStringArray(String field, String[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeString);
  }

  @Override
  public void encodeDateTimeArray(String field, DateTime[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeDateTime);
  }

  @Override
  public void encodeGuidArray(String field, UUID[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeGuid);
  }

  @Override
  public void encodeByteStringArray(String field, ByteString[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeByteString);
  }

  @Override
  public void encodeXmlElementArray(String field, XmlElement[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeXmlElement);
  }

  @Override
  public void encodeNodeIdArray(String field, NodeId[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeNodeId);
  }

  @Override
  public void encodeExpandedNodeIdArray(String field, ExpandedNodeId[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeExpandedNodeId);
  }

  @Override
  public void encodeStatusCodeArray(String field, StatusCode[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeStatusCode);
  }

  @Override
  public void encodeQualifiedNameArray(String field, QualifiedName[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeQualifiedName);
  }

  @Override
  public void encodeLocalizedTextArray(String field, LocalizedText[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeLocalizedText);
  }

  @Override
  public void encodeExtensionObjectArray(String field, ExtensionObject[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeExtensionObject);
  }

  @Override
  public void encodeDataValueArray(String field, DataValue[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeDataValue);
  }

  @Override
  public void encodeVariantArray(String field, Variant[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeVariant);
  }

  @Override
  public void encodeDiagnosticInfoArray(String field, DiagnosticInfo[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeDiagnosticInfo);
  }

  @Override
  public void encodeEnumArray(String field, UaEnumeratedType[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeEnum);
  }

  @Override
  public void encodeStructArray(String field, Object[] values, NodeId dataTypeId)
      throws UaSerializationException {

    encodeArray(field, values, (s, o) -> encodeStruct(s, o, dataTypeId));
  }

  @Override
  public void encodeStructArray(String field, Object[] value, ExpandedNodeId dataTypeId)
      throws UaSerializationException {

    NodeId localDateTypeId =
        dataTypeId
            .toNodeId(encodingContext.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError, "no codec registered: " + dataTypeId));

    encodeStructArray(field, value, localDateTypeId);
  }

  @Override
  public <T> void encodeArray(String field, T[] values, BiConsumer<String, T> encoder)
      throws UaSerializationException {}

  @Override
  public void encodeMatrix(String field, Matrix value) throws UaSerializationException {}

  @Override
  public void encodeEnumMatrix(String field, Matrix value) throws UaSerializationException {}

  @Override
  public void encodeStructMatrix(String field, Matrix value, NodeId dataTypeId)
      throws UaSerializationException {}

  @Override
  public void encodeStructMatrix(String field, Matrix value, ExpandedNodeId dataTypeId)
      throws UaSerializationException {}
}
