//this version uses array of files in hash map for taking care of collision

package final_dup;

import java.io.File;
import java.util.*;

public class Hash_filearray {
	
public static void main(String args[])
{
	 File[] root;
	 root=File.listRoots();
	 System.out.println("Searching for the following drives : ");
	 for(File rootname:root)
	 {
		 System.out.println(rootname);
	 }
	 
	 Hash_filearray dup = new Hash_filearray();
	 
	 for(File rname:root)			  //search for each drive 
	 {
		 
		dup.searchDup(rname.toString());
	 }	
	
	 dup.dispDup();
	 dup.deleteDup();
	 
}//end main

  HashMap<Long,ArrayList<File>> Filenames= new HashMap<Long ,ArrayList<File>> ();   //to store original and single copy files
  HashMap<Long,ArrayList<File>> DupFiles = new HashMap<Long,ArrayList<File>> ();    //to store duplicate copies
	
	public void searchDup(String rname)
	{
		
		File directory=new File(rname);
		File[] fpaths=directory.listFiles(); 
		
		if(fpaths== null)
		{	
			//System.out.println("fpaths null");
			return;
		}
		
		ArrayList<File> file_succ = new ArrayList<File>();
		ArrayList<File> dup_arr = new ArrayList<File>();
		long keycode;
		int flag=0;
		
		for(File fileitr : fpaths)
		{
			flag=0;
			file_succ.clear();
			dup_arr.clear();
			
			if(!(Filenames.isEmpty()) && fileitr.isFile())
			{
				
		 	    keycode=fileitr.getName().hashCode();     //generate hash code
		 	    file_succ= Filenames.get(keycode);        //retrieve all files with the same hash code
		 	       
		 	    if(file_succ!=null)
		 	    {
		 	    	for(File itr : file_succ)
		 	    	{
		 	    		if(itr.getName().equals(fileitr.getName()))
		 	    		{
		 	    			dup_arr = DupFiles.get(keycode);
		 	    			if(dup_arr == null)
		 	    			{
		 	    			  dup_arr = new ArrayList<File>();
		 	    			}
		 	    			dup_arr.add(fileitr);
		 	    			DupFiles.put(keycode, dup_arr);  			
		 				    flag=1;
		 				    break;
		 	    		}
		 	    	}//end inner for
		 	       
		 	    }//end if file_succ!=null
			}//end if(! (Filenames.isEmpty()) && fileitr.isFile())
			
			if(fileitr.isFile() && flag==0)        //not a duplicate
			{
				if(file_succ == null)
				  file_succ = new ArrayList<File>();
				
				file_succ.add(fileitr);
				Filenames.put((long)fileitr.getName().hashCode(), file_succ);
			}
		}// end outer for
		
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
		Set<Map.Entry<Long,ArrayList<File>>> set = DupFiles.entrySet();
	    Iterator<Map.Entry<Long,ArrayList<File>>> i = set.iterator();
	    while(i.hasNext()) 
	    {
	         Map.Entry<Long,ArrayList<File>> me = (Map.Entry<Long,ArrayList<File>>)i.next();
	       
	         Long valind=(Long)me.getKey();
	         ArrayList<File> fetchfiles= new ArrayList<File> ();
	         
	         fetchfiles.addAll(DupFiles.get(valind));
	         
	         for(File itr : fetchfiles)
	         {
	        	 System.out.print(me.getKey() + ": ");
	        	 System.out.println(itr);
	        	 
	         }
	      }
	}//dispDup closed
	
	public void deleteDup()
	{
		Scanner sc=new Scanner(System.in); 
		int wish;
		long op;
		ArrayList<File> fdelete = new ArrayList<File>();
		
		do
		{
			System.out.println("Enter the key for the file to be deleted : ");
			op=sc.nextLong();
		 			
			fdelete = DupFiles.get(op);
			if(fdelete.size()>1)     //more than one duplicate files with the same key
			{
				int ind=0;
				System.out.println("Multiple files present.");
				for(File itr : fdelete)
	 	         {
	 	        	 System.out.print(ind + "   " + op + ": ");
	 	        	 System.out.println(itr);
	 	        	 ind++;
	 	        	 
	 	         }
				System.out.println("Choose index to delete : ");
				ind=sc.nextInt();
				fdelete.get(ind).delete();
				
				fdelete.remove(ind);				
			}
			
			else
			{
				fdelete.get(0).delete();
				fdelete.remove(0);
			    
			}
			System.out.println("File deleted!");
			System.out.println("Do you wish to continue? (0/1) : ");
			wish=sc.nextInt();
		}while(wish==1);
		
		sc.close();
	}//end deleteDup
}



