package massim.webclient;
/**
 * @author Dominik Steinborn
 *
 */

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
 * This class is used to display simulation-status and server-status
 * received as xml-document via rmi-service and transformed with xslt-stylesheet.
 * 
 * @author Dominik Steinborn
 *
 */ 
public class WebClientXMLServlet extends WebClientServletAbstract
{
	private static final long serialVersionUID = -1712648645410100103L;
	private String host; //hostname of computer where RMI-service runs
	private String port; //RMI-portnumber
	private String simulationRMI; //RMI servicename for simulation statistics
	private String serverRMI; //RMI servicename for server statistics
	private String errorStyleSim; //name for "error-stylesheet-simulation"
	private String errorStyleServ; //name for "error-stylesheet-server"
	private Document errorXMLDoc; //xml-document for error-handling
	
	private DocumentBuilder documentbuilder;
	private Element configroot = null;  //root element of configurationfile
	private static StreamSource simulationStyleSource;  //streamsource generated from stylesheet for simulation statistics 
	private static StreamSource serverStyleSource; //streamsource generated from stylesheet for server statistics
	private static StreamSource errorStyleSourceSim; //streamsource generated from stylesheet for "error-page-simulation"
	private static StreamSource errorStyleSourceServ; //streamsource generated from stylesheet for "error-page-server"
	private int queryStringNumber = 0; //number received from queryString which defines which RMI-services is "loaded"
	
	private String configfile ="/home/massim/www/webapps/massim/configfile.xml"; //absolute path to config-file
	//private String configfile ="/usr/share/tomcat5/webapps/massim/configfile.xml"; //absolute path to config-file
		
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
	    
	    //get RMI servicename for simulation statistics
	    simulationRMI = context.getInitParameter("SimulationRMI");

	    //get RMI servicename for server statistics
	    serverRMI = context.getInitParameter("ServerRMI");
	    
	    //get Elementname for "error-stylesheet-simulation"
	    errorStyleSim = context.getInitParameter("simulationError");
	    
	    //get Elementname for "error-stylesheet-server"
	    errorStyleServ = context.getInitParameter("serverError");
	    
	    
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
			configfileDoc = documentbuilder.parse(configfile);
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
		
		simulationStyleSource = readXMLConfig(configroot, simulationRMI);
		serverStyleSource = readXMLConfig(configroot, serverRMI);
		errorStyleSourceSim = readXMLConfig(configroot, errorStyleSim);
		errorStyleSourceServ = readXMLConfig(configroot, errorStyleServ);
		
	}
	
	/**
	 * This method answers all client requests.
	 * Depending on the query string either the simulation-status or the server-status
	 * will be sent to the client via HttpServletResponse or in case of an exception
	 * an error-message is sent.
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
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			
			String queryString = request.getQueryString();
			if (queryString == null )
			{
				throw new ServletException("Empty query string. Try to load Servlet with \"?status=0\" or \"?status=1\" appended");
			}
			int i = queryString.indexOf('=', 0);
			//querySubstring (should only contain 0 or 1 [0 for simulation-, 1 for server-statistics)
			String querySubstring = queryString.substring(i+1);
			
			try
			{
				Integer queryStringNumberTemp = new Integer(querySubstring);
				queryStringNumber = queryStringNumberTemp.intValue();
			}
			catch(NumberFormatException e)
			{
				throw new ServletException("Wrong parameter \"status\" in query string.");
			}
					
			switch(queryStringNumber)
			{
			case 0:	{
						getXMLDocViaRMI(host, port, simulationRMI);
						printXMLDoc(xmlDocument, simulationStyleSource, response);
						break;
					}
			case 1:	{
						getXMLDocViaRMI(host, port, serverRMI);
						printXMLDoc(xmlDocument, serverStyleSource, response);
						break;
					}
			default:	{
							out.write("Wrong parameter for \"status\" in query string. Only 0 and 1 should be used.");
						}
			
			}
		}
		catch (MalformedURLException e) 
		{
			System.err.println("A malformed URL has occurred or no legal protocol could be found.\n" +e);
			
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
			root.setAttribute("header", "Status");
			root.setAttribute("message","A malformed URL has occurred or no legal protocol could be found...");
			errorXMLDoc.appendChild(root);
			if(queryStringNumber == 0)
			{
				printXMLDoc(errorXMLDoc, errorStyleSourceSim, response);
			}
			else if(queryStringNumber == 1)
			{
				printXMLDoc(errorXMLDoc, errorStyleSourceServ, response);
			}
		} 
		catch (RemoteException e) 
		{
			System.err.println("Currently no simulation running...\n" +e);
			
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
			root.setAttribute("header", "Status");
			root.setAttribute("message","Currently no status available...");
			errorXMLDoc.appendChild(root);
			
			if(queryStringNumber == 0)
			{
				printXMLDoc(errorXMLDoc, errorStyleSourceSim, response);
			}
			else if(queryStringNumber == 1)
			{
				printXMLDoc(errorXMLDoc, errorStyleSourceServ, response);
			}
		} 
		catch (NotBoundException e) 
		{
			System.err.println("Currently no simulation running...\n" +e);
			
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
			root.setAttribute("header", "Status");
			root.setAttribute("message","Currently no status available...");
			errorXMLDoc.appendChild(root);
			
			if(queryStringNumber == 0)
			{
				printXMLDoc(errorXMLDoc, errorStyleSourceSim, response);
			}
			else if(queryStringNumber == 1)
			{
				printXMLDoc(errorXMLDoc, errorStyleSourceServ, response);
			}
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
			//throw new ServletException("error: TransformerException" +e );
		}
		catch (IOException e) 
		{
			throw new ServletException("error: IOException" +e );
		}
	}
}
	
