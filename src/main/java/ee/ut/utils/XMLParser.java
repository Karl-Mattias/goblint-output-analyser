package ee.ut.utils;

import ee.ut.data.Access;
import ee.ut.data.Protector;
import ee.ut.data.ProtectorType;
import ee.ut.data.BasicWarning;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Karl-Mattias on 09.04.2017
 */
public class XMLParser {

    private XMLParser(){}

    public static List<BasicWarning> extractWarnings(File inputXML) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputXML);
        document.getDocumentElement().normalize();

        List<BasicWarning> warnings = new ArrayList<>();
        NodeList childNodes = document.getDocumentElement().getElementsByTagName("mem");

        for (int i = 0; i < childNodes.getLength(); i++) {
            Element element = (Element) childNodes.item(i);

            if (element.getAttribute("status").equals("safe")) continue;

            BasicWarning warning = new BasicWarning(element.getAttribute("id"));

            List<Access> accesses = extractAccesses(element);

            warning.setAccesses(accesses);
            warnings.add(warning);
        }

        return warnings;
    }

    private static List<Access> extractAccesses(Element element) {

        List<Access> accessList = new ArrayList<>();
        NodeList accessNodes = element.getElementsByTagName("access");

        for (int i = 0; i < accessNodes.getLength(); i++) {
            Element accessElement = (Element) accessNodes.item(i);

            boolean isWrite = accessElement.getAttribute("type").equals("write");
            String loc = accessElement.getAttribute("loc");

            Element protectorsElements = (Element) accessElement.getElementsByTagName("protectors").item(0);
            Set<Protector> protectors = new HashSet<>();

            if (protectorsElements != null) {
                NodeList protectorNodes = protectorsElements.getElementsByTagName("prot");
                for (int j = 0; j < protectorNodes.getLength(); j++) {
                    Element protectorElement = (Element) protectorNodes.item(j);
                    String id = protectorElement.getAttribute("id");
                    String type = protectorElement.getAttribute("type");

                    if (type.contains("-")) type = type.split("-")[1];

                    protectors.add(new Protector(id, ProtectorType.valueOf(type.toUpperCase())));
                }
            }

            Access access = new Access(loc, isWrite, protectors);
            accessList.add(access);

            Element partitionElements = (Element) accessElement.getElementsByTagName("partitions").item(0);

            if (partitionElements == null) continue;

            NodeList partNodes = partitionElements.getElementsByTagName("part");

            for (int j = 0; j < partNodes.getLength(); j++) {
                Element partElement = (Element) partNodes.item(j);
                if (partElement.getAttribute("type").equals("region")) {
                    String region = partElement.getAttribute("id");
                    access.setRegion(region);
                }
            }
        }

        return accessList;
    }
}
