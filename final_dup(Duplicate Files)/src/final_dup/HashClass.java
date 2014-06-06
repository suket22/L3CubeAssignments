//this version uses hash map for storing original files as well as duplicates
//however it doesn't contain array of files as hash map parameter


package final_dup;
import java.io.File;
import java.util.*;

public class HashClass{
	
public static void main(String args[])
{
	 File[] root;
	 root=File.listRoots();
	 for(File rootname:root)
	 {
		 System.out.println(rootname);
	 }
	 
	 HashClass dup = new HashClass();
	 
	 for(File rname:root)				//search for each drive 
	 {
		 
		dup.searchDup(rname.toString());
	 }	
	 
	 dup.dispDup();
	 dup.deleteDup();
	 
}//end main

  HashMap<Long,File> Filenames= new HashMap<Long ,File> ();      //to store original and single copy files
	HashMap<Long,File> DupFiles = new HashMap<Long,File> ();     //to store duplicate copies
	
	public void searchDup(String rname)
	{
		
		File directory=new File(rname);
		File[] fpaths=directory.listFiles(); 
		int flag=0;
		long keycode;
		
		if(fpaths== null)
		{	
			//System.out.println("fpaths null");
			return;
		}
		
		for(File fileitr : fpaths)
		{
			flag=0;
			
			if(! (Filenames.isEmpty()) && fileitr.isFile())
			{
		 	    keycode=fileitr.getName().hashCode();     //generate hash code
		 	  
		 	    File file_succ;
		 	    file_succ= Filenames.get(keycode);        //retrieve the file with the same hash code
		 	       
		 	    if(file_succ!= null)
		 	    {
		 	       DupFiles.put(keycode, fileitr);
				   flag=1;
				   break;
		 	    }
			}
			
			if(fileitr.isFile() && flag==0)
			{
				keycode=fileitr.getName().hashCode();
				Filenames.put(keycode, fileitr);
			}
			
		}//end for
		
		for(File fileitr : fpaths)
		{
			String pathname=fileitr.getPath();
			
			if(fileitr.isDirectory())
			{
				searchDup(pathname + '\\');
			}
		
		}
	}//searchDup closed
	
	public void dispDup()
	{
	      Set<Map.Entry<Long,File>> set = DupFiles.entrySet();
	      Iterator<Map.Entry<Long,File>> i = set.iterator();
	      while(i.hasNext()) {
	         Map.Entry<Long,File> me = (Map.Entry<Long,File>)i.next();
	         System.out.print(me.getKey() + ": ");
	         System.out.println(me.getValue());
	      }
	}
	
	public void deleteDup()
	{
		Scanner sc=new Scanner(System.in); 
		int wish;
		long op;
		do
		{
			System.out.println("Enter the key for the file to be deleted : ");
			op=sc.nextLong();
		 			
			File fdelete;
			fdelete=DupFiles.get(op);
			fdelete.delete();
			System.out.println("File deleted!");
			System.out.println("Do you wish to continue? : ");
			wish=sc.nextInt();
		}while(wish==1);
		
		sc.close();
	}
}


