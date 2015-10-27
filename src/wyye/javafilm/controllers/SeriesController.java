package wyye.javafilm.controllers;

import wyye.javafilm.objects.Series;

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

public class SeriesController {
	private LinkedList<Series> seriesList = new LinkedList<>();
	
	public void read(String file) throws ParserConfigurationException, IOException, SAXException {
		File xmlFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		NodeList nList = doc.getElementsByTagName("series");
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);	
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String strId = eElement.getElementsByTagName("id").item(0).getTextContent();
				String strFilmId = eElement.getElementsByTagName("film_id").item(0).getTextContent();
				String strName = eElement.getElementsByTagName("name").item(0).getTextContent();
				String strDescription = eElement.getElementsByTagName("description").item(0).getTextContent();
				String strYear = eElement.getElementsByTagName("year").item(0).getTextContent();
				String strSeason = eElement.getElementsByTagName("season").item(0).getTextContent();
				String strEpisode = eElement.getElementsByTagName("episode").item(0).getTextContent();
				seriesList.add(new Series(Long.parseLong(strId), Long.parseLong(strFilmId), strName, strDescription,
						Long.parseLong(strYear), Long.parseLong(strSeason), Long.parseLong(strEpisode)));
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
			if (doc.getElementsByTagName("series_list").item(0) != null) {
				javafilm.removeChild(doc.getElementsByTagName("series_list").item(0));
			}
		} catch (FileNotFoundException e) {
			doc = dBuilder.newDocument();
			javafilm = doc.createElement("javafilm");
			doc.appendChild(javafilm);
		}
		
		Element series_list = doc.createElement("series_list");
		javafilm.appendChild(series_list);
		
		for (Series f: seriesList) {
			Element series = doc.createElement("series");
			series_list.appendChild(series);
			
			Element id = doc.createElement("id");
			series.appendChild(id);
			id.appendChild(doc.createTextNode(Long.toString(f.getId())));
			
			Element film_id = doc.createElement("film_id");
			series.appendChild(film_id);
			film_id.appendChild(doc.createTextNode(Long.toString(f.getFilm_id())));
			
			Element name = doc.createElement("name");
			series.appendChild(name);
			name.appendChild(doc.createTextNode(f.getName()));
			
			Element description = doc.createElement("description");
			series.appendChild(description);
			description.appendChild(doc.createTextNode(f.getDescription()));
			
			Element year = doc.createElement("year");
			series.appendChild(year);
			year.appendChild(doc.createTextNode(Long.toString(f.getYear())));
			
			Element season = doc.createElement("season");
			series.appendChild(season);
			season.appendChild(doc.createTextNode(Long.toString(f.getSeason())));
			
			Element episode = doc.createElement("episode");
			series.appendChild(episode);
			episode.appendChild(doc.createTextNode(Long.toString(f.getEpisode())));
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(xml));
		transformer.transform(source, result);
	}
	
	public void list() {
		for (Series f: seriesList) 
			System.out.println(f);
	}
	
	public void add(long id, long film_id, String name, String description, long year, long season, long episode) throws Exception {
		Iterator<Series> it = seriesList.iterator();
		while (it.hasNext()) {
			Series f = it.next();
			if (f.getId() == id) {
				throw new Exception("Can't add duplicate entry");
			}
		}
		seriesList.add(new Series(id, film_id, name, description, year, season, episode));
	}
	
	public boolean modify(long id, long film_id, String name, String description, long year, long season, long episode) {
		Iterator<Series> it = seriesList.iterator();
		while (it.hasNext()) {
			Series f = it.next();
			if (f.getId() == id) {
				f.setFilm_id(film_id);
				f.setName(name);
				f.setDescription(description);
				f.setYear(year);
				f.setSeason(season);
				f.setEpisode(episode);
				return true;
			}
		}
		return false;
	}
	
	public boolean remove(long id) {
		Iterator<Series> it = seriesList.iterator();
		while (it.hasNext()) {
			Series f = it.next();
			if (f.getId() == id) {
				seriesList.remove(f);
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception {
		SeriesController fc = new SeriesController();
		fc.read("javafilm.xml");
		fc.modify(1, 6, "a", "b", 7, 8, 9);
		fc.add(4, 6, "a", "b", 7, 8, 9);
		fc.add(5, 6, "a", "b", 7, 8, 9);
		fc.remove(4);
		fc.list();
		fc.write("test.xml");
	}
}
