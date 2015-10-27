package wyye.javafilm.controllers;

import wyye.javafilm.objects.Person;

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

public class PersonController {
	private LinkedList<Person> personList = new LinkedList<>();
	
	public void read(String file) throws ParserConfigurationException, IOException, SAXException {
		File xmlFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		NodeList nList = doc.getElementsByTagName("person");
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);	
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String strId = eElement.getElementsByTagName("id").item(0).getTextContent();
				String strName = eElement.getElementsByTagName("name").item(0).getTextContent();
				String strGender = eElement.getElementsByTagName("gender").item(0).getTextContent();
				String strBirthdate = eElement.getElementsByTagName("birthdate").item(0).getTextContent();
				String strBiography = eElement.getElementsByTagName("biography").item(0).getTextContent();
				personList.add(new Person(Long.parseLong(strId), strName, strGender, Long.parseLong(strBirthdate), strBiography));
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
			if (doc.getElementsByTagName("persons").item(0) != null) {
				javafilm.removeChild(doc.getElementsByTagName("persons").item(0));
			}
		} catch (FileNotFoundException e) {
			doc = dBuilder.newDocument();
			javafilm = doc.createElement("javafilm");
			doc.appendChild(javafilm);
		}
		
		Element persons = doc.createElement("persons");
		javafilm.appendChild(persons);
		
		for (Person f: personList) {
			Element person = doc.createElement("person");
			persons.appendChild(person);
			
			Element id = doc.createElement("id");
			person.appendChild(id);
			id.appendChild(doc.createTextNode(Long.toString(f.getId())));
			
			Element name = doc.createElement("name");
			person.appendChild(name);
			name.appendChild(doc.createTextNode(f.getName()));
			
			Element gender = doc.createElement("gender");
			person.appendChild(gender);
			gender.appendChild(doc.createTextNode(f.getGender()));
			
			Element birthdate = doc.createElement("birthdate");
			person.appendChild(birthdate);
			birthdate.appendChild(doc.createTextNode(Long.toString(f.getBirthdate())));
			
			Element biography = doc.createElement("biography");
			person.appendChild(biography);
			biography.appendChild(doc.createTextNode(f.getBiography()));
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(xml));
		transformer.transform(source, result);
	}
	
	public void list() {
		for (Person f: personList) 
			System.out.println(f);
	}
	
	public void add(long id, String name, String gender, long birthdate, String biography) throws Exception {
		Iterator<Person> it = personList.iterator();
		while (it.hasNext()) {
			Person f = it.next();
			if (f.getId() == id) {
				throw new Exception("Can't add duplicate entry");
			}
		}
		personList.add(new Person(id, name, gender, birthdate, biography));
	}
	
	public boolean modify(long id, String name, String gender, long birthdate, String biography) {
		Iterator<Person> it = personList.iterator();
		while (it.hasNext()) {
			Person f = it.next();
			if (f.getId() == id) {
				f.setName(name);
				f.setGender(gender);
				f.setBirthdate(birthdate);
				f.setBiography(biography);
				return true;
			}
		}
		return false;
	}
	
	public boolean remove(long id) {
		Iterator<Person> it = personList.iterator();
		while (it.hasNext()) {
			Person f = it.next();
			if (f.getId() == id) {
				personList.remove(f);
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception {
		PersonController fc = new PersonController();
		fc.read("javafilm.xml");
		fc.modify(1, "a", "b", 11, "aaa");
		fc.add(10, "a", "b", 11, "aaa");
		fc.add(15, "a", "b", 11, "aaa");
		fc.remove(15);
		fc.list();
		fc.write("test.xml");
	}
}
