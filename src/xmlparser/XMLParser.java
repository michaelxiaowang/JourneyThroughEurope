/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlparser;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jte.data.City;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {

    public static void parsexml() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File("./data/cities.xml"));
            Node root = doc.getElementsByTagName("routes").item(0);
            NodeList cardlist = root.getChildNodes();
            City city = new City();
            for (int i = 0; i < cardlist.getLength(); i++) {
                Node cardNode = cardlist.item(i);
                if (cardNode.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList cardAttrs = cardNode.getChildNodes();
                    // one card
                    for (int j = 0; j < cardAttrs.getLength(); j++) {
                        if (cardAttrs.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Node theNode = cardAttrs.item(j);
                            
                            switch (theNode.getNodeName()) {
                                case "name":
                                    if(!City.cities.containsKey(theNode.getTextContent())) {
                                        city = new City(theNode.getTextContent());
                                        city.cities.put(city.getName(), city);
                                    }
                                    else {
                                        city = City.cities.get(theNode.getTextContent());
                                    }
                                    break;
                                case "land":
                                    NodeList landList = theNode.getChildNodes();
                                    String neighbor;
                                    for (int k = 0; k < landList.getLength(); k++) {
                                        if (landList.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                            city.getLandNeighbors().add(landList.item(k).getTextContent());
                                        }
                                    }
                                    break;
                                case "sea":
                                    NodeList seaList = theNode.getChildNodes();
                                    for (int k = 0; k < seaList.getLength(); k++) {
                                        if (seaList.item(k).getNodeType() == Node.ELEMENT_NODE) {

                                        }
                                    }
                                    break;
                            }
                        }
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
