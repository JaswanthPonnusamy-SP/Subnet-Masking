package find;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

@WebServlet("/findsubnet")
public class findsubnet extends HttpServlet {
 
    public findsubnet() {
        super();      
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String ip=request.getParameter("ipaddress");
		int cidr=Integer.parseInt(request.getParameter("cidr"));
		
		subnetCalc sum=new subnetCalc(ip);
	    System.out.println(sum.isValidip());
	    subnetCalc sum2=new subnetCalc(ip,cidr);
		JSONObject obj = null;
		try {
			obj = sum2.getResourceAsJSON();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		System.out.print(obj.toString());
		response.getWriter().write(obj.toString());
        
		}
		
	}




