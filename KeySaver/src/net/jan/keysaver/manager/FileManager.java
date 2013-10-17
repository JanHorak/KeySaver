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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import net.jan.aes.decryption.Decryption;
import net.jan.aes.encryption.Encryption;
import net.jan.aes.keygenerationmanager.KeyGenerationManager;
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

    private String structureFilePath = "AppData/structure.xml";
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder = null;
    private Document doc = null;
    OutputStream outputStream = null;
    BufferedOutputStream bufferedOutputStream = null;
    private SettingManager sm_icon;
    private Properties iconProps;
    
    public FileManager(){
        sm_icon = new SettingManager("AppData/icons.properties");
        try {
            iconProps = sm_icon.initAndReturnProperties();
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile(null, ex);
        }
    }

    
    public Category returnSingleCategory(String name){
        prepareSAX();
        Category category = new Category();
     
        
        NodeList listOfNodes = doc.getElementsByTagName("category");

        for (int i = 0; i < listOfNodes.getLength(); i++) {
            Element e = (Element) listOfNodes.item(i);
            if ( e.getAttribute("name").equals(name) ){
                category.setName(name);
                category.setIconPath(e.getAttribute("icon"));
                break;
            }
        }
        encryptStructureFile();
        return category;
        

    }
    
    public void saveStructure(CategoryList categoryList) {
        prepareSAX();
        List<Category> catList = categoryList.getCategoryList();
        categoryList.printStructure();
        try {
            try {
                outputStream = new FileOutputStream(new File(structureFilePath));
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
        encryptStructureFile();
    }

    public void checkAvailibility() {
        if (!new File(structureFilePath).exists()) {
            new KeyGenerationManager().generateAndStoreKey("AppData/private.key");
            try {
                try {
                    outputStream = new FileOutputStream(new File(structureFilePath));
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
                out.writeAttribute("icon", iconProps.getProperty("FOLDER_DEFAULT"));
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
                encryptStructureFile();
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
        encryptStructureFile();
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
                encryptStructureFile();
                return result;
            }
        }
        encryptStructureFile();
        return result;
    }
    
    public List<String> returnIconCategoryPathes(){
        List<String> results = returnAttributeListFromElements("category", "icon");
        return results;
    }
    
    public List<String> returnIconKeyPathes(){
        List<String> results = returnAttributeListFromElements("key", "icon");
        return results;
    }
    
    private List<String> returnAttributeListFromElements(String elements, String attribute){
        List<String> results = new ArrayList<>();
        File structure = new File (structureFilePath);
        prepareSAX();
        NodeList listOfNodes = doc.getElementsByTagName(elements);
        for (int i = 0; i < listOfNodes.getLength(); i++ ){
            Element e = (Element) listOfNodes.item(i);
            results.add(e.getAttribute(attribute));
        }
        encryptStructureFile();
        return results;
    }
    
    
    

    private void prepareSAX() {
        File strucure = new File(structureFilePath);
        dbFactory = DocumentBuilderFactory.newInstance();
        decryptStructureFile();
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            LoggingManager.writeToErrorFile("SAX: Cant load db-Factory!", ex);
        }
        try {
            doc = dBuilder.parse(strucure);
        } catch (SAXException ex) {
            LoggingManager.writeToErrorFile("SAX-Parse-Exception!", ex);
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile("SAX: Cant load XML-File", ex);
        }
    }
    
    
    private void encryptStructureFile(){
        File strucure = new File(structureFilePath);
        strucure = new Encryption().returnEncryptedFile(strucure, structureFilePath, "AppData/private.key");
    }
    
    private void decryptStructureFile(){
        File strucure = new File(structureFilePath);
        strucure = new Decryption().returnDecryptedFile(strucure, structureFilePath, "AppData/private.key");
    }
}
