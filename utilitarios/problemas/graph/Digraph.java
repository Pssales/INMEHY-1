package problemas.graph;


/******************************************************************************
 *  Compilation:  javac Digraph.java
 *  Execution:    java Digraph
 *  Dependencies: ST.java SET.java StdOut.java
 *  
 *  Directed graph data type implemented using a symbol table
 *  whose keys are strings and whose values are sets of strings.
 *
 *  Copyright © 2000–2017, Robert Sedgewick and Kevin Wayne.
 *  Last updated: Fri Oct 20 14:12:12 EDT 2017.
 ******************************************************************************/

public class Digraph {

    // symbol table of linked lists
    private ST<String, SET<String>> st;
    
    private int E;


    // create an empty digraph
    public Digraph() {
        st = new ST<String, SET<String>>();
    }

    // add v to w's list of neighbors; self-loops allowed
    public void addEdge(String v, String w) {
        if (!st.contains(v)) addVertex(v);
        if (!st.contains(w)) addVertex(w);
        st.get(v).add(w);
    }

    // add a new vertex v with no neighbors if vertex does not yet exist
    public void addVertex(String v) {
        if (!st.contains(v)) st.put(v, new SET<String>());
    }

    // return the degree of vertex v
    public int degree(String v) {
        if (!st.contains(v)) return 0;
        else                 return st.get(v).size();
    }

    // return the array of vertices incident to v
    public Iterable<String> adjacentTo(String v) {
        if (!st.contains(v)) return new SET<String>();
        else                 return st.get(v);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String v : st) {
            s.append(v + ": ");
            for (String w : st.get(v)) {
                s.append(w + " ");
            }
            s.append("\n");
        }
        return s.toString();
    }

}
