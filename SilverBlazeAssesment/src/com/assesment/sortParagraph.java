package com.assesment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class sortParagraph extends HttpServlet {
	private String para = "";
	public List<Column> wordAndOccurrence() throws IOException {
		String buffer = null;
		// get the file.
		String FileName = "/WEB-INF/paragraph.txt";
		Map<String, Integer> map = new HashMap<>();
		List<String> wordList = new ArrayList<>();
		List<Column> sortedList = new ArrayList<>();
		ServletContext cntxt = getServletContext();
		// convert text file into a string
		InputStream ins = cntxt.getResourceAsStream(FileName);
		if(ins == null){
			System.out.println("Not Found");
		}
		else {
			BufferedReader br = new BufferedReader((new InputStreamReader(ins)));
			// Read the file by lines
			buffer = br.readLine();
			while(buffer!=null) {
				para += buffer;
				buffer = br.readLine();	
			}	
		}
		// split the contents of the files into words and save it in a string array
		String[] str = para.split(" ");
		// save the word in a hash map to get unique words and their occurrences.
		for(String s:str) {
			if(map.containsKey(s)) {
				map.put(s,map.get(s)+1);
			}
			else map.put(s,1);
		}
		// save the sorted word in array list
		wordList.addAll(sort(map));
		// save the unique words and their occurrences in object array list
		for(String sortStr: wordList) {
			if(map.containsKey(sortStr)) sortedList.add(new Column(sortStr,map.get(sortStr)));
		}
		return sortedList;
	}
	// sort the words according to occurrences in descending order
	public List<String> sort(Map<String,Integer> map){	
		List<String> wordList = new ArrayList<>();
		// Initialize priority queue and write method to compare the occurrences.
		   PriorityQueue<String> pq = new PriorityQueue<>((count1, count2) -> {
			      if (map.get(count1) != map.get(count2)) { 	  
			        return map.get(count2) - map.get(count1);
			      }
			      return count1.compareTo(count2);
			    });
		   //add words in map to priority queue
		   for (String word : map.keySet()) {
			   
		          pq.add(word);
			} 
		   // Using FIFO technique poll the elements in priority queue to array list and return it.
		   while(!pq.isEmpty()) {
			   wordList.add(pq.poll());
		   }
		return wordList;
	}


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	// Assign the sorted words and occurrences list to a List.
    	PrintWriter out = response.getWriter();
    	List<Column> col =	wordAndOccurrence();
    	request.setAttribute("column", col);
    	request.setAttribute("para", para);
        RequestDispatcher view = request.getRequestDispatcher("paragraph.jsp");
        view.forward(request, response);
    	out.println("<H4><B><center>File Name: Paragraph</center></B></H4><BR>");
    	out.println("<Table border=1 align=center >");
    	out.println("<TR><TD><b>FileContents:</b></TD></TR>");
    	out.println("<TR><TD><b>"+para+"</b></TD></TR>"); 
    	out.println("</Table><br>");
    	out.println("<Table border=1 align=center >");
        out.println("<TR><TD><b>Word</b></TD><TD><b>Occurance</b></TD></TR>");
     // displaying records
        request.setAttribute("colList",col);
		
		 // request.getRequestDispatcher("HTML/File.html").forward(request, response); 
        	for(int i=0;i<col.size();i++) {
              String word = col.get(i).getWord();
              int count = col.get(i).getCount();
               out.println("<TR><TD>"+word+"</TD><TD>"+count+"</TD></TR>");
        	}
         out.println("</Table>");
    }
}