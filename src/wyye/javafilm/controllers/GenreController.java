package wyye.javafilm.controllers;

import wyye.javafilm.objects.Genre;

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

public class GenreController {
	private LinkedList<Genre> genresList = new LinkedList<>();
	
	public void read(String file) throws ParserConfigurationException, IOException, SAXException {
		File xmlFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		NodeList nList = doc.getElementsByTagName("genre");
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);	
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String strId = eElement.getElementsByTagName("id").item(0).getTextContent();
				String strName = eElement.getElementsByTagName("name").item(0).getTextContent();
				String strDescription = eElement.getElementsByTagName("description").item(0).getTextContent();
				genresList.add(new Genre(Long.parseLong(strId), strName, strDescription));
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
			if (doc.getElementsByTagName("genres").item(0) != null) {
				javafilm.removeChild(doc.getElementsByTagName("genres").item(0));
			}
		} catch (FileNotFoundException e) {
			doc = dBuilder.newDocument();
			javafilm = doc.createElement("javafilm");
			doc.appendChild(javafilm);
		}
		
		Element genres = doc.createElement("genres");
		javafilm.appendChild(genres);
		
		for (Genre f: genresList) {
			Element genre = doc.createElement("genre");
			genres.appendChild(genre);
			
			Element id = doc.createElement("id");
			genre.appendChild(id);
			id.appendChild(doc.createTextNode(Long.toString(f.getId())));
			
			Element name = doc.createElement("name");
			genre.appendChild(name);
			name.appendChild(doc.createTextNode(f.getName()));
			
			Element description = doc.createElement("description");
			genre.appendChild(description);
			description.appendChild(doc.createTextNode(f.getDescription()));
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(xml));
		transformer.transform(source, result);
	}
	
	public void list() {
		for (Genre f: genresList) 
			System.out.println(f);
	}
	
	public void add(long id, String name, String description) throws Exception {
		Iterator<Genre> it = genresList.iterator();
		while (it.hasNext()) {
			Genre f = it.next();
			if (f.getId() == id) {
				throw new Exception("Can't add duplicate entry");
			}
		}
		genresList.add(new Genre(id, name, description));
	}
	
	public boolean modify(long id, String name, String description) {
		Iterator<Genre> it = genresList.iterator();
		while (it.hasNext()) {
			Genre f = it.next();
			if (f.getId() == id) {
				f.setName(name);
				f.setDescription(description);
				return true;
			}
		}
		return false;
	}
	
	public boolean remove(long id) {
		Iterator<Genre> it = genresList.iterator();
		while (it.hasNext()) {
			Genre f = it.next();
			if (f.getId() == id) {
				genresList.remove(f);
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception {
		GenreController fc = new GenreController();
		fc.read("javafilm.xml");
		fc.modify(1, "a", "b");
		fc.add(4, "a", "b");
		fc.add(5, "a", "b");
		fc.remove(2);
		fc.list();
		fc.write("test.xml");
	}
}
