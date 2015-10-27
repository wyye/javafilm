package wyye.javafilm.controllers;

import wyye.javafilm.objects.Production;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ProductionController {
	private LinkedList<Production> productionList = new LinkedList<>();
	
	public void read(String file) throws ParserConfigurationException, IOException, SAXException {
		File xmlFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		NodeList nList = doc.getElementsByTagName("production");
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);	
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String strId = eElement.getElementsByTagName("id").item(0).getTextContent();
				String strFilm_Id = eElement.getElementsByTagName("film_id").item(0).getTextContent();
				String strProductionName = eElement.getElementsByTagName("production_name").item(0).getTextContent();
				String strCountry = eElement.getElementsByTagName("country").item(0).getTextContent();
				productionList.add(new Production(Long.parseLong(strId), Long.parseLong(strFilm_Id), strProductionName, strCountry));
			}
		}
	}	
		
	public void write(String xml) throws TransformerException, ParserConfigurationException, IOException, SAXException{
		File xmlFile = new File(xml);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc;
		Element javafilm;
		try {
			doc = dBuilder.parse(xmlFile);
			javafilm = (Element) doc.getElementsByTagName("javafilm").item(0);
			if (doc.getElementsByTagName("productions").item(0) != null) {
				javafilm.removeChild(doc.getElementsByTagName("productions").item(0));
			}
		} catch (FileNotFoundException e) {
			doc = dBuilder.newDocument();
			javafilm = doc.createElement("javafilm");
			doc.appendChild(javafilm);
		}
		
		Element productions = doc.createElement("productions");
		javafilm.appendChild(productions);
		
		for (Production f: productionList) {
			Element production = doc.createElement("production");
			productions.appendChild(production);
			
			Element id = doc.createElement("id");
			production.appendChild(id);
			id.appendChild(doc.createTextNode(Long.toString(f.getId())));
			
			Element film_id = doc.createElement("film_id");
			production.appendChild(film_id);
			film_id.appendChild(doc.createTextNode(Long.toString(f.getFilm_id())));
			
			Element production_name = doc.createElement("production_name");
			production.appendChild(production_name);
			production_name.appendChild(doc.createTextNode(f.getProduction_name()));
			
			Element country = doc.createElement("country");
			production.appendChild(country);
			country.appendChild(doc.createTextNode(f.getCountry()));
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(xml));
		transformer.transform(source, result);
	}
	
	public void list() {
		for (Production f: productionList) 
			System.out.println(f);
	}
	
	public void add(long id, long film_id, String productionName, String country) throws Exception {
		Iterator<Production> it = productionList.iterator();
		while (it.hasNext()) {
			Production f = it.next();
			if (f.getFilm_id() == id) {
				throw new Exception("Can't add duplicate entry");
			}
		}
		productionList.add(new Production(id, film_id, productionName, country));
	}
	
	public boolean modify(long id, long film_id, String productionName, String country) {
		Iterator<Production> it = productionList.iterator();
		while (it.hasNext()) {
			Production f = it.next();
			if (f.getFilm_id() == id) {
				f.setFilm_id(film_id);
				f.setProduction_name(productionName);
				f.setCountry(country);
				return true;
			}
		}
		return false;
	}
	
	public boolean remove(long id) {
		Iterator<Production> it = productionList.iterator();
		while (it.hasNext()) {
			Production f = it.next();
			if (f.getFilm_id() == id) {
				productionList.remove(f);
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception {
		ProductionController fc = new ProductionController();
		fc.read("javafilm.xml");
		fc.modify(1, 6, "t", "y");
		fc.list();
		fc.write("test.xml");
	}
}
