package problemas.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/******************************************************************************
 *  Compilation:  javac AllPaths.java
 *  Execution:    java AllPaths
 *  Depedencies:  Graph.java
 *  
 *  Find all paths from s to t.
 *  
 *  % java AllPaths
 *  A: B C 
 *  B: A F 
 *  C: A D F 
 *  D: C E F G 
 *  E: D G 
 *  F: B C D 
 *  G: D E 
 * 
 *  [A, B, F, C, D, E, G]
 *  [A, B, F, C, D, G]
 *  [A, B, F, D, E, G]
 *  [A, B, F, D, G]
 *  [A, C, D, E, G]
 *  [A, C, D, G]
 *  [A, C, F, D, E, G]
 *  [A, C, F, D, G]
 *
 *  Remarks
 *  --------
 *   -  Currently prints in reverse order due to stack toString()
 *   
 *   Copyright © 2000–2017, Robert Sedgewick and Kevin Wayne.
 *   Last updated: Fri Oct 20 14:12:12 EDT 2017.
 *
 *	Modificações foram realizadas para se adequar ao desejado
 *	
 *
 ******************************************************************************/

public class AllPaths {
	
		private static Stack<String> path  = new Stack<String>();   // the current path
	    private static ArrayList<String> onPath  = new ArrayList<String>();
	    private static ArrayList<Stack<String>>paths = new ArrayList<Stack<String>>();
	    private static ArrayList<List<String>>ArrayListpaths = new ArrayList<List<String>>();
	    // the set of vertices on the path

	    public static ArrayList<List<String>> getAllPaths(Digraph G, String from, String to) {
	        enumerate(G, from, to);
	        return ArrayListpaths;
	        
	    }

	    // use DFS
	    private static void enumerate(Digraph G, String from, String to) {

	        // add node v to current path from s
	        path.push(from);
	        onPath.add(from);

	        if(paths.size() > 100) {
	        	return;
	        }
	        // found path from s to t - currently prints in reverse order because of stack
	        if (from.equals(to)) {
	        	paths.add((Stack<String>) path.clone());
	        	ArrayListpaths.add((ArrayList<String>) onPath.clone());
//System.out.println(paths);	  
//System.out.println(ArrayListpaths);
//System.out.println("----------------------------");
	        }

	        // consider all neighbors that would continue path with repeating a node
	        else {
//	        	System.out.println("de: " + from);
	            for (String w : G.adjacentTo(from)) {
//	            	System.out.println("para: "+ w);
	                if (!onPath.contains(w)) {
	                	enumerate(G, w, to);
	                }
	                else {
	                	boolean contain = false;
	                	for (int i = 0; i < onPath.size(); i++) {

	                		if(i+1 < onPath.size()) {
	                			if(onPath.get(i).equals(from) && onPath.get(i+1).equals(w)) {
	                				contain = true;
	                				break;
	                			}	                			
	                		}	
						}
	                
	                	 if(!contain) {
	 	                	enumerate(G, w, to);
	                	 }
	                }
	                
	            }
	        }

	        // done exploring from v, so remove from path
	        path.pop();
	        onPath.remove(from);
	    }
}
