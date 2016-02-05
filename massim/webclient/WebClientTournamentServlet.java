package massim.webclient;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * This class is used to display tournament-statistics received as 
 * xml-document via rmi-service and transformed with xslt-stylesheet.
 *
 */
public class WebClientTournamentServlet extends WebClientServletAbstract
{

	private static final long serialVersionUID = -1712648645410100103L;
	private String host; //hostname of computer where RMI-service runs
	private String port; //RMI-portnumber
	private String tournamentRMI; //RMI servicename for tournament statistics
	private String errorStyle; //name for "error-stylesheet"
	private Document errorXMLDoc; 
	private DocumentBuilder documentbuilder;
	private Element configroot = null;  //root element of configurationfile
	private static StreamSource tournamentStyleSource;  //streamsource generated from stylesheet for tournament statistics 
	private static StreamSource errorStyleSource; //streamsource generated from stylesheet for "error-page"	
	private String configfile ="/home/massim/www/webapps/massim/configfile.xml"; //absolute path to config-file

	public void init() throws ServletException
	{
		/*
		 * The parameters that the init-methods reads
		 * have to be defined in tomcats web.xml located
		 * in the webapps project directory in folder WEB-INF.
		 * 
		 * Example for portnumber:
		 *	<context-param>
		 *		<param-name>PortNumber</param-name>
		 *		<param-value>1099</param-value>
		 *	<context-param>
		 * 
		 */

		ServletContext context = getServletContext();

		//get servername from "web.xml"
		host = context.getInitParameter("ServerName");

		//get RMI-portnumber from "web.xml"
		port = context.getInitParameter("RMIPortNumber");

		//get RMI servicename for tournament statistics
		tournamentRMI = context.getInitParameter("TournamentRMI");

		//get Elementname for "error-stylesheet"
		errorStyle = context.getInitParameter("statisticsError");


		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		Document configfileDoc = null;

		try 
		{
			documentbuilder = dbfactory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e) 
		{
			throw new ServletException("Error while creating document builder.", e);
		}

		try 
		{
			configfileDoc = documentbuilder.parse(configfile); //parse the config-file
		}
		catch (SAXException e) 
		{
			throw new ServletException("Error parsing configuration.", e);
		}
		catch (IOException e) 
		{
			throw new ServletException("Error reading configuration.", e);
		}

		configroot = configfileDoc.getDocumentElement();
		if (configroot == null) 
		{
			throw new ServletException("Error parsing configuration: Missing root element.");
		}


		tournamentStyleSource = readXMLConfig(configroot, tournamentRMI);
		errorStyleSource = readXMLConfig(configroot, errorStyle);

	}

	/**
	 * This method answers all client requests.
	 * The tournament-statistics will be sent to the client via 
	 * HttpServletResponse or in case of an exception an error-message is sent.
	 * 
	 * @param request the request from the client-side
	 * @param response the response from the servlet to the client
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		try
		{
			getXMLDocViaRMI(host, port, tournamentRMI);
			printXMLDoc(xmlDocument, tournamentStyleSource, response);
		}
		catch (MalformedURLException e) 
		{
			System.err.println("A malformed URL has occurred or no legal protocol could be found.\n"+e);

			try
			{
				DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
				errorXMLDoc = dbfactory.newDocumentBuilder().newDocument();
			}
			catch (ParserConfigurationException exc) 
			{
				throw new ServletException("Error while creating document builder.", exc);
			}

			Element root = errorXMLDoc.createElement("error");
			root.setAttribute("header", "Tournament");
			root.setAttribute("message","A malformed URL has occurred or no legal protocol could be found...");
			errorXMLDoc.appendChild(root);

			printXMLDoc(errorXMLDoc, errorStyleSource, response);

		} 
		catch (RemoteException e) 
		{
			System.err.println("Currently no tournament statistics available...\n" +e);

			try
			{
				DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
				errorXMLDoc = dbfactory.newDocumentBuilder().newDocument();
			}
			catch (ParserConfigurationException exc) 
			{
				throw new ServletException("Error while creating document builder.", exc);
			}

			Element root = errorXMLDoc.createElement("error");
			root.setAttribute("header", "Tournament");
			root.setAttribute("message","Currently no tournament statistics available...");
			errorXMLDoc.appendChild(root);

			printXMLDoc(errorXMLDoc, errorStyleSource, response);
		} 
		catch (NotBoundException e) 
		{
			System.err.println("Currently no tournament statistics available...\n"+e);

			try
			{
				DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
				errorXMLDoc = dbfactory.newDocumentBuilder().newDocument();
			}
			catch (ParserConfigurationException exc) 
			{
				throw new ServletException("Error while creating document builder.", exc);
			}

			Element root = errorXMLDoc.createElement("error");
			root.setAttribute("header", "Tournament");
			root.setAttribute("message","Currently no tournament statistics available...");
			errorXMLDoc.appendChild(root);

			printXMLDoc(errorXMLDoc, errorStyleSource, response);
		}

	}



	/**
	 * This method transforms the xmlDocument with the StreamSource 
	 * in a html-page sends the result to the client via HttpServletResponse.
	 * 
	 * @param xmlDocument
	 * @param stylesource
	 * @param response
	 */
	public void printXMLDoc(Document xmlDocument, StreamSource stylesource, HttpServletResponse response)
	throws ServletException
	{
		try
		{
			//create the table and the ranking
			ResultsPage.calculateResults(xmlDocument);
			
			//write the xml document to the stream
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer(stylesource);

			DOMSource sourceXML = new DOMSource(xmlDocument);
			StreamResult resultStream = new StreamResult(out);
			transformer.transform(sourceXML, resultStream); //sourceXML transformed and printed on resultStream            

		}
		catch (TransformerConfigurationException e)
		{
			throw new ServletException("error: TransformerConfigurationException" +e );
		} 
		catch (TransformerException e)
		{
			throw new ServletException("error: TransformerException" +e );
		}
		catch (IOException e) 
		{
			throw new ServletException("error: IOException" +e );
		}
	}
}
