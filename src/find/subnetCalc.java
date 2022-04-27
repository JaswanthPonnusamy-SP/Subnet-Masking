package find;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class subnetCalc {

	public subnetCalc(String ip){
        this.ip = ip;
        isValidAddress(ip);
    }

    public subnetCalc(String ip,int cidr){
        this.ip=ip;
        this.cidr=cidr;
        compileIp(ip,cidr);
    }


    private String ip = null;
    private int cidr = 0;
    private int hosts = 0;
    private boolean validip=false;
    private String whichClass=null;
    private String network_Id = "";
    private String broadcastId = "";
    private String hostmin="";
   

    // Validation Varaibles



    public String getHostmin() {
        return hostmin;
    }

    public JSONObject getResourceAsJSON() throws JSONException {
        JSONObject temp = new JSONObject();
        temp.put("network_Id",network_Id);                      //storing in map.
        temp.put("broadcast_Id",broadcastId);
        temp.put("class",whichClass);
        temp.put("hostmin",hostmin);
        temp.put("hostmax",hostmax);
        temp.put("hosts",String.valueOf(hosts));
        return temp;
    }

    public String getHostmax() {
        return hostmax;
    }

    private String hostmax="";


    public String getWhichClass() {
        return whichClass;
    }

    public int getCidr() {
        return cidr;
    }

    public int getHosts() {
        return hosts;
    }

    public String getNetworkId() {
        return network_Id;
    }

    public String getBroadcastId() {
        return broadcastId;
    }

    public boolean isValidip() {
        return validip;
    }

    public String getIP() {
        return ip;
    }


    public void isValidAddress(String ip) {

        if (ip.matches("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$")) {

            String[] ipadd = ip.split("\\.");

            if (Integer.parseInt(ipadd[0]) < 256 && Integer.parseInt(ipadd[0]) >= 0 && Integer.parseInt(ipadd[1]) >= 0 && Integer.parseInt(ipadd[1]) < 256 && Integer.parseInt(ipadd[2]) >= 0 && Integer.parseInt(ipadd[2]) < 256 && Integer.parseInt(ipadd[3]) < 256 && Integer.parseInt(ipadd[3]) >= 0) {

                validip=true;

            }
        }

    }

    public void compileIp(String ip,int cidr){

        String[] binary=binaryformatforipaddress(ip);
        String[] subnetBinary=binaryformatforcidrnotation(cidr);

        ArrayList<int[]> array1=networkandbroadcastidfromtwoip(binary,subnetBinary);
        int[] networkIdarray= array1.get(0);
        int[] broadcastIdarray=array1.get(1);

        hosts=(int)Math.pow(2,hosts)-2;

        if(cidr==32){
            hostmin=broadcastId;
            hostmax=broadcastId;
            hosts=1;
        }

        else {
            hostmin=findhostminfromnetworkid(networkIdarray);
            hostmax=findhostmaxfrombroadcastid(broadcastIdarray);
        }

        whichClass=findClass(ip);
        network_Id=createIds(networkIdarray);
        broadcastId=createIds(broadcastIdarray);

    }


    public String findClass(String ip){                      //finding subnet class when ip address is given.

        String subclass="";

        String[] ipaddress=ip.split("\\.");
        int answer=Integer.parseInt(ipaddress[0]);


        if(answer>=0 && answer<=127) {
            subclass="class A";
        }

        else if(answer>=128 && answer<=191) {
            subclass="class B";
        }

        else {
            subclass="class C";
        }

        return subclass;
    }


    public String createIds(int[] ipaddressintform){                    //creating ip address.

        String Id="";

        for(int i=0;i<ipaddressintform.length;i++){
            if(i==(ipaddressintform.length-1)){
                Id = Id + Integer.toString(ipaddressintform[i]);
            }

            else{
                Id = Id + Integer.toString(ipaddressintform[i])+".";
            }
        }

        return Id;
    }


    public String[] binaryformatforipaddress(String ipaddress){                     //ip address to binary form[string array].

        String[] data=ip.split("\\.");
        String[] binary=new String[4];

        for(int i=0;i<4;i++){

            String binaryformat=Integer.toBinaryString(Integer.parseInt(data[i]));
            while(binaryformat.length()!=8){
                binaryformat="0"+binaryformat;
            }
            binary[i]=binaryformat;
        }

        return binary;
    }


    public String[] binaryformatforcidrnotation(int cidr){                          //cidr notation to binary form[string array].

        String[] subnetBinary=new String[4];

        int a=cidr/8;
        int b=cidr%8;

        for(int i=1;i<=4;i++){

            if(i<=a){
                subnetBinary[i-1]="11111111";
            }
            else if(b!=0)
            {
                String str1="";
                for(int j=1;j<=8;j++)
                {
                    if(j<=b) {
                        str1 += "1";
                    }
                    else{
                        str1 += "0";
                    }
                }
                subnetBinary[i-1]=str1;
                b=0;
            }
            else
            {
                subnetBinary[i-1]="00000000";
            }

        }

        return subnetBinary;
    }


    public ArrayList<int[]> networkandbroadcastidfromtwoip(String[] binary,String[] subnetBinary){                              //finding networkid and boradcastid form two give ip's[arraylist<string>].

        int[] netId=new int[4];
        int[] broadId=new int[4];
        ArrayList<int[]> array=new ArrayList<>();

        for(int i=0;i<4;i++){

            String answeryou="";
            String answeryou2="";
            char[] ques = binary[i].toCharArray();
            char[] ques2 = subnetBinary[i].toCharArray();

            for(int j=0;j<8;j++){

                if(String.valueOf(ques2[j]).equals("1")){
                    answeryou+=ques[j];
                    answeryou2+=ques[j];
                }

                else{
                    hosts+=1;
                    answeryou+="0";
                    answeryou2+="1";

                }
            }
            netId[i]=Integer.parseInt(answeryou,2);
            broadId[i]=Integer.parseInt(answeryou2,2);
        }

        array.add(netId);
        array.add(broadId);

        return array;
    }


    public String findhostminfromnetworkid(int[] answer){                                                               //finding minimun host id from given network id.

        String hostminimum="";

        if (answer[3] + 1 == 256) {
            if (answer[2] + 1 == 256) {
                hostminimum = answer[0] + "." + (answer[1] + 1) + ".0.0";
            }
            else {
                hostminimum = answer[0] + "." + answer[1] + "." + (answer[2] + 1) + ".0";
            }
        }
        else {
            hostminimum = answer[0] + "." + answer[1] + "." + answer[2] + "." + (answer[3] + 1);
        }

        return hostminimum;
    }


    public String findhostmaxfrombroadcastid(int[] answer2){                                                            //finding maximum host id form given broadcast id.

        String hostmaximum="";
        
        if (answer2[3] == 0) {
            if (answer2[2] == 0) {
            	hostmaximum = answer2[0] + "." + (answer2[1] - 1) + ".255.255";
            }
            else {
            	hostmaximum = answer2[0] + "." + answer2[1] + "." + (answer2[2] - 1) + ".255";
            }
        }
        else {
        	hostmaximum = answer2[0] + "." + answer2[1] + "." + answer2[2] + "." + (answer2[3] - 1);
        }
        
        return hostmaximum;
    }

	
}
