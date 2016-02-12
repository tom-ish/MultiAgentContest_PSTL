package massim.webclient;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * This interface defines all methods which the WebClientServlet at least must provide 
 * to be able to display the simulation statistics and there must be implemented to each Servlet...
 * 
 * @author Dominik Steinborn
 *
 */
public interface WebClientServletInterface {

	/**
	 * This method should answer all client requests...
	 * 
	 * @param request the request from the client-side
	 * @param response the response from the servlet to the client
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException;
	
	/**
	 * This method should redirects all request to the doGet-method
	 * 
	 * @param request the request from the client-side
	 * @param response the response from the servlet to the client
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException;
	
	
	/**
	 * This method 
	 * 
	 * @param host
	 * @param port
	 * @param service
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public void getXMLDocViaRMI(String host, String port, String service)
	throws MalformedURLException, RemoteException, NotBoundException;
	
	public void printXMLDoc(Document xmlDocument, StreamSource stylesource, HttpServletResponse response);
	
	public StreamSource readXMLConfig(Element configroot, String service)
	throws ServletException;
}
