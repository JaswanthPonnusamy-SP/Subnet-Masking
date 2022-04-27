package filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebFilter("/findsubnet")
public class findsubnet implements Filter {

    public findsubnet() {
      }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req=(HttpServletRequest) request;
		HttpServletResponse res=(HttpServletResponse) response;
		
		String ipaddress=req.getParameter("ipaddress");
		String cidr=req.getParameter("cidr");
		Map<String,String> temp=new HashMap<String,String>();
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		if(ipaddress.matches("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$")){

			String[] ipadd=ipaddress.split("\\.");
			if(Integer.parseInt(ipadd[0])<256 && Integer.parseInt(ipadd[0])>=0 && Integer.parseInt(ipadd[1])>=0 && Integer.parseInt(ipadd[1])<256 && Integer.parseInt(ipadd[2])>=0 && Integer.parseInt(ipadd[2])<256 && Integer.parseInt(ipadd[3])<256 && Integer.parseInt(ipadd[3])>=0){
				
				if(String.valueOf(cidr).matches("^[0-9]{1,2}$")){
					if(Integer.parseInt(cidr)>0 && Integer.parseInt(cidr)<=32){
			chain.doFilter(req, res);
					}
					else{
						temp.put("cidr", "error");
						String json = new Gson().toJson(temp);
						response.getWriter().write(json);
					}
				}
				
				else{
					temp.put("cidr", "error");
					String json = new Gson().toJson(temp);
					response.getWriter().write(json);
				}
				}
			
			else{
				temp.put("ip", "error");
				String json = new Gson().toJson(temp);
				response.getWriter().write(json);
			}
				
				
			}
			else{
				temp.put("ip", "error");
				String json = new Gson().toJson(temp);
				response.getWriter().write(json);
			}


		
		
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}



