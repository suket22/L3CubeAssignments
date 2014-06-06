import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class WebLogRecord
{
	String ip;
	String user_identifier;
	String user_id;
	String time;
	String timezone;
	String http_requesttype;
	String http_version;
	String page_request;
	int status_code;
	int page_size;
	String referrer;
	String browser_info;
	public static Writer writer = null;

	WebLogRecord()
	{
		ip=user_identifier=user_id=time=timezone=http_requesttype=http_version=page_request="";
		referrer=browser_info="";
		status_code=page_size=0;
	}
	
	WebLogRecord(String ip,String user_identifier,String user_id,String time,String timezone,String http_requesttype,String page_request,String http_version,int status_code,int page_size,String referrer,String browser_info)	
	{
		this.ip=ip;
		this.user_identifier=user_identifier;
		this.user_id=user_id;
		this.time=time;
		this.timezone=timezone;
		this.http_requesttype=http_requesttype;
		this.page_request=page_request;
		this.http_version=http_version;
		this.status_code=status_code;
		this.page_size=page_size;
		this.referrer=referrer;
		this.browser_info=browser_info;
	}
	
	void display(int index)
	{
		try {		    
		    writer.write("Request Number : \t\t"+"#"+index+"\n");		    
		    writer.write("Client IP Address : \t\t" + ip+"\n");		    
			writer.write("User Identifier (RFC 1413) : \t" +user_identifier+"\n");			
			writer.write("Client/User ID : \t\t" + user_id+"\n");			
			writer.write("Time of Request : \t\t" + time+"\n");			
			writer.write("Timezone(GMT) : \t\t" + timezone+"\n");			
			writer.write("HTTP Request Type : \t\t"+http_requesttype+"\n");			
			writer.write("Page requested : \t\t"+page_request+"\n");			
			writer.write("HTTP Version Used : \t\t"+http_version+"\n");			
			writer.write("Status Code : \t\t\t"+status_code+"\n");	
			writer.write("Page Size : \t\t\t"+page_size+"\n");			
			writer.write("Referrer :\t\t\t"+referrer+"\n");			
			writer.write("Client Browser Information :\t"+browser_info+"\n");			
			writer.write("\n\n----------------------------------------------\n\n");			
		} 
		catch (IOException ex) {
			System.out.println("Could not write to the File! ");
		  ex.printStackTrace();
		} 		
	}
}	//Combined Log Format



public class MainParser {
	
	static ArrayList <WebLogRecord> wlr = new ArrayList<WebLogRecord>();
	static HashMap <Integer,ArrayList<WebLogRecord>> ip_addr = new HashMap <Integer,ArrayList<WebLogRecord>>();
	static HashMap <Integer,ArrayList<WebLogRecord>> status_codes = new HashMap <Integer,ArrayList<WebLogRecord>>();
	static FileReader in = null;
	
	public static void group()	
	{
		WebLogRecord temp= new WebLogRecord();
		ArrayList <WebLogRecord> listtemp= new ArrayList<WebLogRecord>();
		ArrayList <WebLogRecord> listtemp1= new ArrayList<WebLogRecord>();
		for(int i=0;i<wlr.size();i++)
		{
			temp=wlr.get(i);
			listtemp=ip_addr.get(temp.ip.hashCode());						
			if(listtemp==null){
				listtemp= new ArrayList<WebLogRecord>();
			}
			listtemp.add(temp);	
			ip_addr.put(temp.ip.hashCode(), listtemp);	//grouped by ip under same hashkey 			
			
			listtemp1=status_codes.get(temp.status_code);			
			if(listtemp1==null){
				listtemp1= new ArrayList<WebLogRecord>();
			}
			listtemp1.add(temp);
			status_codes.put(temp.status_code, listtemp1);	//grouped by status codes under same hashkey		
		}		
	}
	
	public static void print_ip() throws IOException
	{
		ArrayList <WebLogRecord> w = new ArrayList<WebLogRecord>();
		WebLogRecord temp = new WebLogRecord();
		Writer writer = null;
		File theDir = new File("ClientRecords");		  
		if (!theDir.exists()) 
		{
		    System.out.println("Creating Directory: " + "ClientRecords");
		    boolean result = theDir.mkdir();  
		     if(result) 
		     {    
		       System.out.println("Successful!");  
		     }		
		}
		
		//Directory will be created to store the records for each client
		//Each Client will have a seperate text file wherein it's usage will be recorded
		
		for (Map.Entry<Integer,ArrayList<WebLogRecord>> entry : ip_addr.entrySet()) 
		{					  
		    w = entry.getValue();
		    writer=new BufferedWriter(new OutputStreamWriter(
	  		          new FileOutputStream("ClientRecords\\"+w.get(0).ip+".txt"), "utf-8"));			  
		    writer.write("USAGE RECORD for Client with IP -" + w.get(0).ip+"\n\n");		    
		    writer.write("====================================================================\n\n");		    
		    for(int i=0;i<w.size();i++)
		    {
		    	temp=w.get(i);		    		  						    
		    	try 
		    	{		    		
		    		writer.write("User Identifier (RFC 1413) : \t" +temp.user_identifier+"\n");			
					writer.write("Client/User ID : \t\t" + temp.user_id+"\n");			
					writer.write("Time of Request : \t\t" + temp.time+"\n");			
					writer.write("Timezone(GMT) : \t\t" + temp.timezone+"\n");			
					writer.write("HTTP Request Type : \t\t"+temp.http_requesttype+"\n");			
					writer.write("Page requested : \t\t"+temp.page_request+"\n");			
					writer.write("HTTP Version Used : \t\t"+temp.http_version+"\n");			
					writer.write("Status Code : \t\t\t"+temp.status_code+"\n");	
					writer.write("Page Size : \t\t\t"+temp.page_size+"\n");			
					writer.write("Referrer :\t\t\t"+temp.referrer+"\n");			
					writer.write("Client Browser Information :\t"+temp.browser_info+"\n");			
					writer.write("\n\n----------------------------------------------\n\n");			
				} 
				catch (IOException ex) 
				{
					System.out.println("Could not write to the File! ");
				  ex.printStackTrace();
				} 				    				
		    }
		    writer.close();
		}
		
	}
	
	public static void print_status() throws IOException
	{
		ArrayList <WebLogRecord> w = new ArrayList<WebLogRecord>();
		WebLogRecord temp = new WebLogRecord();
		Writer writer = null;
		File theDir = new File("StatusRecords");		  
		if (!theDir.exists()) 
		{
		    System.out.println("Creating Directory: " + "StatusRecords");
		    boolean result = theDir.mkdir();  
		     if(result) 
		     {    
		       System.out.println("Successful!");  
		     }		
		}
		
		//Directory will be created to store the records for each client
		//Each Client will have a seperate text file wherein it's usage will be recorded
		
		for (Map.Entry<Integer,ArrayList<WebLogRecord>> entry : status_codes.entrySet()) 
		{					  
		    w = entry.getValue();
		    writer=new BufferedWriter(new OutputStreamWriter(
	  		          new FileOutputStream("StatusRecords\\"+w.get(0).status_code+".txt"), "utf-8"));			  
		    writer.write("USAGE RECORD grouped by Status Codes -" + w.get(0).status_code+"\n\n");		    
		    writer.write("====================================================================\n\n");		    
		    for(int i=0;i<w.size();i++)
		    {
		    	temp=w.get(i);		    		  						    
		    	try 
		    	{
		    		writer.write("Client IP Address : \t\t" + temp.ip+"\n");
		    		writer.write("User Identifier (RFC 1413) : \t" +temp.user_identifier+"\n");			
					writer.write("Client/User ID : \t\t" + temp.user_id+"\n");			
					writer.write("Time of Request : \t\t" + temp.time+"\n");			
					writer.write("Timezone(GMT) : \t\t" + temp.timezone+"\n");			
					writer.write("HTTP Request Type : \t\t"+temp.http_requesttype+"\n");			
					writer.write("Page requested : \t\t"+temp.page_request+"\n");			
					writer.write("HTTP Version Used : \t\t"+temp.http_version+"\n");								
					writer.write("Page Size : \t\t\t"+temp.page_size+"\n");			
					writer.write("Referrer :\t\t\t"+temp.referrer+"\n");			
					writer.write("Client Browser Information :\t"+temp.browser_info+"\n");			
					writer.write("\n\n----------------------------------------------\n\n");			
				} 
				catch (IOException ex) 
				{
					System.out.println("Could not write to the File! ");
				  ex.printStackTrace();
				} 				    				
		    }
		    writer.close();
		}
		
	}
	
	public static void parse(String line,WebLogRecord temp)
	{				
		int flag=0;
		char slash ='/';
		Scanner tokenize = new Scanner(line);
		while (tokenize.hasNext()) {
			if(flag==0)				
				temp.ip=tokenize.next();
			else if(flag==1)
				temp.user_identifier=tokenize.next();
			else if(flag==2)
				temp.user_id=tokenize.next();
			else if(flag==3)
			{
				temp.time=tokenize.next();
				temp.time=temp.time.substring(1,temp.time.length());	//removing first char from string
			}	
			else if(flag==4)
			{
				temp.timezone=tokenize.next();
				temp.timezone=temp.timezone.substring(0,temp.timezone.length()-1);	//remove last char
			}	
			else if(flag==5)
			{
				temp.http_requesttype=tokenize.next();
				temp.http_requesttype=temp.http_requesttype.substring(1,temp.http_requesttype.length());
			}	
			else if(flag==6)
				temp.page_request=tokenize.next();			
			else if(flag==7)
			{
				temp.http_version=tokenize.next();
				temp.http_version=temp.http_version.substring(0,temp.http_version.length()-1);	//remove last char
				temp.http_version=temp.http_version.substring(temp.http_version.indexOf((int)slash,0)+1,temp.http_version.length());
			}
			else if(flag==8){
				try{
				temp.status_code=Integer.parseInt(tokenize.next());
				}
				catch(NumberFormatException e){
					temp.status_code=0;
				}
			}
			else if(flag==9){
				try{
					temp.page_size=Integer.parseInt(tokenize.next());
				}
				catch(NumberFormatException e){
					temp.page_size=0;
				}
			}			
			else if(flag==11){
				temp.referrer=tokenize.next();
				temp.browser_info="";
			}
			else if(flag==12){
				temp.browser_info=temp.browser_info+tokenize.next();
				temp.browser_info=temp.browser_info+ " ";
			}
			if(flag!=12)
				flag++;

		}   
	}
	
	public static void main(String args[]) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("weblog.txt"));
		String line;		
		WebLogRecord temp = new WebLogRecord();
		WebLogRecord temp1 = new WebLogRecord();
		int index=0;
		WebLogRecord.writer=new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream("Analysed.txt"), "utf-8"));
			//initialise write object
		while ((line = br.readLine()) != null) 
		{
		  parse(line,temp);	
		  wlr.add(new WebLogRecord(temp.ip,temp.user_identifier,temp.user_id,temp.time,temp.timezone,temp.http_requesttype,temp.page_request,temp.http_version,temp.status_code,temp.page_size,temp.referrer,temp.browser_info));
		  index++;
		  temp1=wlr.get(index-1);
		  temp1.display(index);
		}
		group();
		print_ip();
		print_status();
		System.out.println("1. Analysed.txt has been created in the project directory! It contains a simple representation of all Records in weblog.txt.\n\n2. Inside the folder ClientRecords in the same directory you shall find a file for each Client IP\n");
		System.out.println("3. Inside the folder StatusRecords in the same directory you shall find a file for Status Code\n");
		System.out.println("---Recommendation -> Used Notepad++ to open the text files since they are large in size");
		br.close();
		WebLogRecord.writer.close();
	}		
}


