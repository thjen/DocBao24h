package io.qthjen_dev.docbao24h.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLDOMParse {

    public Document getDocument(String xml) {
        Document doc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(xml));
            inputSource.setEncoding("UTF-8");
            doc = builder.parse(inputSource);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    private final String getTextNodeValue(Node element) {
        Node child;
        if ( element != null)
            if (element.hasChildNodes())
                for (child = element.getFirstChild(); child != null; child = child.getNextSibling())
                    if (child.getNodeType() == Node.TEXT_NODE)
                        return child.getNodeValue();
        return "";
    }

    public String getValue(Element item, String key) {
        NodeList nodeList = item.getElementsByTagName(key);
        return this.getTextNodeValue(nodeList.item(0));
    }

}
