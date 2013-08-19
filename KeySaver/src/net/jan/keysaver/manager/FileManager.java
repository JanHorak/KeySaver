/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.manager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import net.jan.keysaver.sources.Category;
import net.jan.keysaver.sources.CategoryList;
import net.jan.keysaver.sources.Key;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Jan Horak
 */
public class FileManager {

    private final String path = "AppData\\structure.xml";
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder = null;
    private Document doc = null;
    OutputStream outputStream = null;
    BufferedOutputStream bufferedOutputStream = null;

    public void saveStructure(CategoryList categoryList) {
        prepareSAX();
        List<Category> catList = categoryList.getCategoryList();
        categoryList.printStructure();
        try {
            try {
                outputStream = new FileOutputStream(new File(path));
            } catch (FileNotFoundException ex) {
                LoggingManager.writeToErrorFile("File not found! XML", ex);
            }

            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(outputStream, "utf-8"));
            out.writeStartDocument();
            out.writeStartElement("structure");

            for (Category c : catList) {
                List<Key> keyList = c.getKeylist();
                out.writeStartElement("category");
                out.writeAttribute("name", c.getName());
                out.writeAttribute("icon", c.getIconPath());
                if (keyList.isEmpty()) {
                    out.writeEmptyElement("keys");
                } else {
                    out.writeStartElement("keys");
                    for (Key k : keyList) {
                        out.writeEmptyElement("key");
                        out.writeAttribute("keyname", k.getKeyname());
                        out.writeAttribute("username", k.getUsername());
                        out.writeAttribute("description", k.getDescription());
                        out.writeAttribute("password", k.getPassword());
                    }
                    out.writeEndElement();
                }
                out.writeEndElement();
            }
            out.writeEndDocument();
            out.close();
            outputStream.close();
        } catch (XMLStreamException ex) {
            LoggingManager.writeToErrorFile(null, ex);
        } catch (UnsupportedEncodingException ex) {
            LoggingManager.writeToErrorFile(null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void checkAvailibility() {
        if (!new File(path).exists()) {
            try {
                try {
                    outputStream = new FileOutputStream(new File(path));
                } catch (FileNotFoundException ex) {
                    LoggingManager.writeToErrorFile("File not found! XML", ex);
                }

                XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                        new OutputStreamWriter(outputStream, "utf-8"));
                out.writeStartDocument();
                out.writeStartElement("structure");
                out.writeAttribute("name", "categories");
                out.writeStartElement("category");
                out.writeAttribute("name", "default");
                out.writeAttribute("icon", returnDefaultPath());
                out.writeStartElement("keys");
                out.writeEmptyElement("key");
                out.writeAttribute("keyname", "defaultKey");
                out.writeAttribute("username", "defaultName");
                out.writeAttribute("description", "This is a default-Key");
                out.writeAttribute("password", "password");
                out.writeEndElement();
                out.writeEndElement();
                out.writeEndElement();

                out.writeEndDocument();
                try {
                    out.close();
                    outputStream.close();
                } catch (XMLStreamException | IOException ex) {
                    LoggingManager.writeToErrorFile("Cant close XMLOutWriter or OutputStream", ex);
                }
                System.out.println("First Start: List of Categories not existing. Created and initialized it!");
            } catch (XMLStreamException ex) {
                LoggingManager.writeToErrorFile(null, ex);
            } catch (UnsupportedEncodingException ex) {
                LoggingManager.writeToErrorFile(null, ex);
            }
        } else {
            System.out.println("Category-File founded");
        }
    }

    public CategoryList returnListofCategories() {
        CategoryList categoryList = new CategoryList();
        List<Category> list = new ArrayList<Category>();

        prepareSAX();

        NodeList listOfNodes = doc.getElementsByTagName("category");

        for (int i = 0; i < listOfNodes.getLength(); i++) {
            Category cat = new Category();
            List<Key> keyList = new ArrayList<Key>();
            Element e = (Element) listOfNodes.item(i);
            cat.setName(e.getAttribute("name"));
            cat.setIconPath(e.getAttribute("icon"));
            NodeList keys = e.getElementsByTagName("key");
            for (int j = 0; j < keys.getLength(); j++) {
                Key k = new Key();
                Element e2 = (Element) keys.item(j);
                k.setKeyname(e2.getAttribute("keyname"));
                k.setUsername(e2.getAttribute("username"));
                k.setDescription(e2.getAttribute("description"));
                k.setPassword(e2.getAttribute("password"));
                keyList.add(k);
            }
            cat.setKeylist(keyList);

            list.add(cat);
        }

        categoryList.setCategoryList(list);

        return categoryList;
    }

    public Key returnKey(String keyName) {
        Key result = new Key();
        prepareSAX();
        NodeList keys = doc.getElementsByTagName("key");

        for (int i = 0; i < keys.getLength(); i++) {
            Element e = (Element) keys.item(i);
            if (e.getAttribute("keyname").endsWith(keyName)) {
                result.setKeyname(keyName);
                result.setUsername(e.getAttribute("username"));
                result.setDescription(e.getAttribute("description"));
                result.setPassword(e.getAttribute("password"));
                return result;
            }
        }

        return result;
    }

    private String returnDefaultPath() {
        return "AppData\\" 
                + "Images\\" 
                + "intern\\"
                + "Folder_default_16x16.png";
    }

    private void prepareSAX() {
        dbFactory = DocumentBuilderFactory.newInstance();
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            LoggingManager.writeToErrorFile("SAX: Cant load db-Factory!", ex);
        }
        try {
            doc = dBuilder.parse(path);
        } catch (SAXException ex) {
            LoggingManager.writeToErrorFile("SAX-Parse-Exception!", ex);
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile("SAX: Cant load XML-File", ex);
        }
    }
}
