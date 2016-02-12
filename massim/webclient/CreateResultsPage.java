package massim.webclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This class creates the "TournamentStatistics" as html-page 
 * for a given tournament-report-file and xslt-stylesheet.
 * It is NOT a component of the webinterface itself and must
 * be started manually to create the TournamentStatistics-html-page.
 * 
 *  createResultsHTML.xslt is the standard xslt-stylesheet for 
 *  the of TournamentStatistics-html-page.
 * 
 */
public class CreateResultsPage {

	/**
	 * @param args command line parameters (expects xslt-stylesheet and xml-tournament-report-file)
	 */
	public static void main(String[] args) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentbuilder;
		StreamSource stylesource;
		Document xmlDocument;
		File htmlFile; //file where the result is saved
		
		//check if xslt-stylesheet and xml-tournament-report-file are specified
		if (args.length != 2) {
			System.err.println("Usage: java CreateResultsPage stylesheet xmlfile");
			System.exit(1);
		}

		
		try {
			File xsltStylesheet = new File(args[0]);
			File xmlFile = new File(args[1]);

			documentbuilder = factory.newDocumentBuilder();
			xmlDocument = documentbuilder.parse(xmlFile);

			stylesource = new StreamSource(xsltStylesheet);

			ResultsPage.calculateResults(xmlDocument);
			
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer(stylesource);


			htmlFile = new File( "Tournament.html" ); 
			if ( htmlFile.exists() )
			{
				//not handled
			}
			else
			{
				try
				{
					htmlFile.createNewFile();
					System.out.println( "Creating new file: " + htmlFile );
				} catch ( IOException e1 ) 
				{ 
					System.err.println("Error creating file: " + e1); 
					System.exit(1);
				}
			}
			FileOutputStream fileout = new FileOutputStream(htmlFile);
			DOMSource sourceXML = new DOMSource(xmlDocument);
			StreamResult resultStream = new StreamResult(fileout);
			transformer.transform(sourceXML, resultStream); // sourceXML transformed and written to file with FileOutputStream
			fileout.close();
		} catch (ParserConfigurationException e) {
			System.err.println("error: ParserConfigurationException" + e);
			System.exit(1);
		} catch (SAXException e) {
			System.err.println("error: SAXException" + e);
			System.exit(1);
		} catch (TransformerConfigurationException e) {
			System.err.println("error: TransformerConfigurationException" + e);
			System.exit(1);
		} catch (TransformerException e) {
			System.err.println("error: TransformerException" + e);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("error: IOException" + e);
			System.exit(1);
		} catch (Exception e) {
			System.err.println("error: Exception" + e);
			System.exit(1);
		}
	}
}
