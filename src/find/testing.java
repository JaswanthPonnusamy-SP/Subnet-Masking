package find;

import org.json.JSONException;
import org.json.JSONObject;

public class testing {

	public static void main(String[] args) throws JSONException {
		
	subnetCalc sum=new subnetCalc("192.255.12.256");
    System.out.println(sum.isValidip());
    subnetCalc sum2=new subnetCalc("192.255.12.255",30);
	JSONObject obj=sum2.getResourceAsJSON();
	
    System.out.print(obj);
	
	}
}


