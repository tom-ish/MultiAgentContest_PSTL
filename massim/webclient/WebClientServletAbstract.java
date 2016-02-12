package massim.webclient;
/**
 * This class defines methods (abtract and non-abstract) which are needed
 * for each servlet that is supposed to display data received from the tournament
 * server.
 * 
 * @author Dominik Steinborn
 *
 */

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;

import massim.framework.rmi.XMLDocumentServer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class WebClientServletAbstract extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8298333298950509445L;
	protected Document xmlDocument; //the document received from RMI-service in method getXMLDocViaRMI
	
	/**
	 * This method answers all client requests.
	 * 
	 * @param request the request from the client-side
	 * @param response the response from the servlet to the client
	 * @throws ServletException
	 * @throws IOException
	 */
	public abstract void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException;
	
	/**
	 * This method redirects all requests to the doGet-method.
	 * 
	 * @param request the request from the client-side
	 * @param response the response from the servlet to the client
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		doGet(request, response);
	}
	
	/**
	 * This method connects to rmi-registry on specified, host, port and service
	 * and tries to receive a xml-document which is saved to global 
	 * variable xmlDocument if the attempt was succesful otherwise
	 * an exception is thrown.
	 * 
	 * @param host hostname of computer where the rmi-registry is running
	 * @param port portnumber of rmi-registry on host
	 * @param service servicename of rmi-service
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public void getXMLDocViaRMI(String host, String port, String service)
	throws MalformedURLException, RemoteException, NotBoundException
	{
		String url = "rmi://" + host + ":" + port + "/" + service;
			
		XMLDocumentServer server_state = (XMLDocumentServer)Naming.lookup(url);
		
		try 
		{
			xmlDocument = (Document)server_state.getXMLDocument();
		}
		catch(NullPointerException e)
		{
			throw new RemoteException("NullPointerException while trying to get XMLDocument", e);
		}
	}
	
	/**
	 * This method will be responsible for the transformation of the xmlDocument
	 * with the StreamSource (generated from xslt-stylesheet) in a html-page and
	 * output on HttpServletResponse.
	 * 
	 * @param xmlDocument
	 * @param stylesource
	 * @param response
	 */
	public abstract void printXMLDoc(Document xmlDocument, StreamSource stylesource, HttpServletResponse response)
	throws ServletException;
	
	/**
	 * This method receives the root-element of a config-file and a servicename.
	 * If there is an element named like servicename in the configfile then
	 * the method tries to open the stylesheet specified by the attributes of
	 * servicename-element and returns a StreamSource if it is succesful 
	 * otherwise a ServletException is thrown.
	 * 
	 * @param configroot the root-element of the config-file
	 * @param service the servicename for which the stylesheet should be transformed in a StreamSource
	 * @return StreamSource generated from xslt-stylesheet
	 * @throws ServletException
	 */
	public StreamSource readXMLConfig(Element configroot, String service)
	throws ServletException
	{
		File stylesheet = null;
		
		//get nodes from config-file with same name as RMI-servicename
		NodeList configNode = configroot.getElementsByTagName(service);
		
		//get first element from nodelist
		Element configElem = (Element)configNode.item(0);

		if( configElem == null )
		{
			throw new ServletException("error: no element for " + service + " defined in the config-file");
		}

		//get attributes path and filename for the stylesheet-file from element
		String path = configElem.getAttribute("path");
		String filename = configElem.getAttribute("filename");
		if( path.equals("") || filename.equals("") )
		{
			throw new ServletException("error: attribute path or filename doesn't exist or doesn't have a value!");
		}

		stylesheet = new File(path, filename);

		if(!stylesheet.exists() || !stylesheet.canRead())
		{
			throw new ServletException("stylesheet does not exists or can't be read!");
		}
		StreamSource stylesource = new StreamSource(stylesheet); //generate StreamSource from stylesheet
		return stylesource;
	}
	
}
