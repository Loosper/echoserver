package org.elsys.netprog.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Servlet
 */
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static HashMap<String, String> data = new HashMap<>();

    /**
     * Default constructor. 
     */
    public Servlet() {
        // TODO Auto-generated constructor stub
    }
    
    private String generateDB() {
    	String output = new String();
    	
        Iterator it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            output = output.concat(
        		"<div>" + pair.getKey() + ": " + pair.getValue() + "</div>"
            );
        }
    	return output;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		response.getOutputStream().println("<html><body>" +
				"<form method='POST'>" +
					"<input type='text' name='key' placeholder='key' />" +
					"<input type='text' name='value' placeholder='value' /> " +
					"<input type='submit' />" +
				"</form>" +	
				"</font></body></html>");
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		
		data.put(key, value);
		
		response.getOutputStream().println(
			"<html><body> <h1>Already saved</h1>" + generateDB() + "</body></html>"
		);
	}

}
