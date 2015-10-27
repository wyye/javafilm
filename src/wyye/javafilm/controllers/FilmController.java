package wyye.javafilm.controllers;

import wyye.javafilm.objects.Film;

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

public class FilmController {
	private LinkedList<Film> filmsList = new LinkedList<>();
	
	public void read(String file) throws ParserConfigurationException, IOException, SAXException {
		File xmlFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		NodeList nList = doc.getElementsByTagName("film");
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);	
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String strId = eElement.getElementsByTagName("id").item(0).getTextContent();
				String strName = eElement.getElementsByTagName("name").item(0).getTextContent();
				String strDuration = eElement.getElementsByTagName("duration").item(0).getTextContent();
				String strDescription = eElement.getElementsByTagName("description").item(0).getTextContent();
				String strYear = eElement.getElementsByTagName("year").item(0).getTextContent();
				String strStatus = eElement.getElementsByTagName("status").item(0).getTextContent();
				String strIsSerial = eElement.getElementsByTagName("is_serial").item(0).getTextContent();
				boolean isSerial = strIsSerial.equals("0") ? false : true;
				filmsList.add(new Film(Long.parseLong(strId), strName, strDuration, strDescription, Long.parseLong(strYear), strStatus,  isSerial));
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
			if (doc.getElementsByTagName("films").item(0) != null) {
				javafilm.removeChild(doc.getElementsByTagName("films").item(0));
			}
		} catch (FileNotFoundException e) {
			doc = dBuilder.newDocument();
			javafilm = doc.createElement("javafilm");
			doc.appendChild(javafilm);
		}
		
		Element films = doc.createElement("films");
		javafilm.appendChild(films);
		
		for (Film f: filmsList) {
			Element film = doc.createElement("film");
			films.appendChild(film);
			
			Element id = doc.createElement("id");
			film.appendChild(id);
			id.appendChild(doc.createTextNode(Long.toString(f.getId())));
			
			Element name = doc.createElement("name");
			film.appendChild(name);
			name.appendChild(doc.createTextNode(f.getName()));
			
			Element duration = doc.createElement("duration");
			film.appendChild(duration);
			duration.appendChild(doc.createTextNode(f.getDuration()));
			
			Element description = doc.createElement("description");
			film.appendChild(description);
			description.appendChild(doc.createTextNode(f.getDescription()));
			
			Element year = doc.createElement("year");
			film.appendChild(year);
			year.appendChild(doc.createTextNode(Long.toString(f.getYear())));
			
			Element status = doc.createElement("status");
			film.appendChild(status);
			status.appendChild(doc.createTextNode(f.getStatus()));
			
			Element isSerial = doc.createElement("is_serial");
			film.appendChild(isSerial);
			isSerial.appendChild(doc.createTextNode(f.getIsSerial() ? "1" : "0"));
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(xml));
		transformer.transform(source, result);
	}
	
	public void list() {
		for (Film f:filmsList) 
			System.out.println(f);
	}
	
	public void add(long id, String name, String duration, String description, long year, String status, boolean isSerial) throws Exception {
		Iterator<Film> it = filmsList.iterator();
		while (it.hasNext()) {
			Film f = it.next();
			if (f.getId() == id) {
				throw new Exception("Can't add duplicate entry");
			}
		}
		filmsList.add(new Film(id, name, duration, description, year, status, isSerial));
	}
	
	public boolean modify(long id, String name, String duration, String description, long year, String status, boolean isSerial) {
		Iterator<Film> it = filmsList.iterator();
		while (it.hasNext()) {
			Film f = it.next();
			if (f.getId() == id) {
				f.setName(name);
				f.setDuration(duration);
				f.setDescription(description);
				f.setYear(year);
				f.setStatus(status);
				f.setIsSerial(isSerial);
				return true;
			}
		}
		return false;
	}
	
	public boolean remove(long id) {
		Iterator<Film> it = filmsList.iterator();
		while (it.hasNext()) {
			Film f = it.next();
			if (f.getId() == id) {
				filmsList.remove(f);
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception {
		FilmController fc = new FilmController();
		fc.read("javafilm.xml");
		fc.modify(1, "a", "b", "c", 1999, "e", true);
		fc.add(3, "a", "b", "c", 1999, "e", true);
		fc.add(4, "a", "b", "c", 1999, "e", true);
		fc.remove(3);
		fc.list();
		fc.write("test.xml");
	}
}
