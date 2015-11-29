/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wyye.javafilm.controllers;

import wyye.javafilm.entities.Film;
import wyye.javafilm.entities.Production;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author ad
 */
@ManagedBean
@ViewScoped
public class XMLRWController {
    public static String ROOT = "javafilm";
    public static String FILM  = "films";
    public static String PROD  = "productions";

    // TODO implement next 3

    //public static String GENR  = "genres";
    //public static String SER  = "series";
    //public static String PERS   = "persons";       

    public void myRead(String xfb, FilmController em){	
        FacesContext ctx = FacesContext.getCurrentInstance();                         

        Document doc =  docInit(xfb); 
        if (!doc.getDocumentElement().getNodeName().equals(ROOT)){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "root must be <javafilm>", null));			
        }
        NodeList fLevelNodes = doc.getChildNodes();
        NodeList nList = fLevelNodes.item(0).getChildNodes();
        for (int i = 0; i < nList.getLength(); i++) {
                if (nList.item(i).getNodeName().equals(FILM)){				
                        nList = nList.item(i).getChildNodes();				
                }
        }

        fillTables(nList, em);
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Done", null));    

    }	

    public void myWrite(String fileName){
        Document doc;       
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();        
        try {
            DocumentBuilder db = factory.newDocumentBuilder();
            DOMImplementation domImplementation = db.getDOMImplementation();
            doc = domImplementation.createDocument(null, ROOT, null);

            buildTree(doc, doc.getDocumentElement());

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            Properties transfProps = new Properties();
            transfProps.put("method", "xml");
            transfProps.put("indent", "yes");
            transformer.setOutputProperties(transfProps);

            DOMSource source = new DOMSource(doc);
            OutputStream out = new FileOutputStream(new File(fileName));
            StreamResult result =  new StreamResult(out);
            transformer.transform(source, result);
            try {
                out.close();
            } catch (Exception ex) {}            
        } catch (ParserConfigurationException | DOMException | FileNotFoundException | TransformerException ex) {
            System.err.println("XML error\n"+ex.toString());
        }
    }	

    private Element createXmlObj(Document doc, String root, String[] args, String ... values){
        Element e = createElement(doc, root, null);
        for(int i = 0; i < args.length; i++){
                e.appendChild(createElement(doc, args[i], values[i]));
        }
        return e;
    }	

    private Element createElement(Document doc, String name, String textContent) {
        Element elem = doc.createElement(name);        
        if(textContent!=null) {
                elem.setTextContent(textContent);
            }
        return elem;
    }
	
    private void buildTree(Document doc, Element root) {
        // setting up document
        doc.setXmlStandalone(true);
        doc.setStrictErrorChecking(true);
        doc.setXmlVersion("1.0");
        
        Element subRoot = createElement(doc, FILM, null);       
        Element e;
        FilmController filmController = new FilmController();
        for (Film i: filmController.getFilms()) {
            String[] args = {"id", "name", "duration",
                            "description", "release", "status", "isSerial"};
            e = createXmlObj(doc,"film", args, 
                            (i.getId()).toString(),
                            i.getName(),
                            (i.getDuration()).toString(),
                            i.getDescription(),
                            (i.getRelease()).toString(),
                            i.getStatus(),
                            (i.getIsSerial()).toString());
            subRoot.appendChild(e);
        }
        root.appendChild(subRoot);
        
        ProductionController productionController = new ProductionController();
        subRoot = createElement(doc, PROD, null);   
        for(Production i: productionController.getProductions()){
        	String[] args = {"id", "name", "country"};
        	e = createXmlObj(doc,"production", args,
        			(i.getId()).toString(),
        			i.getName(),
        			i.getCountry());
        	subRoot.appendChild(e);
        } 
        root.appendChild(subRoot);
                
        // TODO other 3 controllers 
        
    }	
	
    private Document docInit(String fileName){
            try{
                    File fXmlFile = new File(fileName);
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(fXmlFile);
                    doc.getDocumentElement().normalize();
                    return doc;
            } catch(ParserConfigurationException | SAXException | IOException ex){
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getLocalizedMessage()));
                    return null;
            }

    }

    private void fillTables(NodeList nList, FilmController em){
            FilmEditController e;		

            for (int i = 0; i < nList.getLength(); i++) {
                    if (nList.item(i).getNodeName().equals("film")){				
                            Node nNode = nList.item(i);
                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                    e = new FilmEditController();
                                    Element eElement = (Element) nNode;		     			      		
                                    e.setId(Long.parseLong(getTagValue("id", eElement)));		      
                                    e.setName(getTagValue("name", eElement));
                                    e.setDuration(Long.parseLong(getTagValue("duration", eElement)));	
                                    e.setDescription(getTagValue("description", eElement));	
                                    e.setRelease(Long.parseLong(getTagValue("release", eElement)));		
                                    e.setStatus(getTagValue("status", eElement));		
                                    e.setIsSerial(Boolean.parseBoolean(getTagValue("isSerial", eElement)));
                                    em.createFilm(e);
                            }				
                    } 
        }		
    }

    private static String getTagValue(String sTag, Element eElement) {
            try{
                    NodeList nList = eElement.getChildNodes(); 
                    for (int i = 0; i < nList.getLength(); i++){
                            if (nList.item(i).getNodeName().equals(sTag)){
                                    return nList.item(i).getChildNodes().item(0).getNodeValue();
                            }	
                    }	
                    return null; 
            }catch (Exception ex){
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getLocalizedMessage()));
                    return null;
            }       
    }
}
