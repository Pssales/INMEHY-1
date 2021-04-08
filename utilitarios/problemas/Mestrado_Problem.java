package problemas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.GraphWalk;
import org.jgrapht.io.ComponentNameProvider;
import org.jgrapht.io.DOTExporter;
import org.jgrapht.io.ExportException;
import org.jgrapht.io.GraphExporter;


import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.alg.shortestpath.*;

import dependencias_abstract.AbstractIntegerProblem;
import dependencias_interfaces.IntegerSolution;
import problemas.graph.AllPaths;
import problemas.graph.Digraph;
import utilidades.HawickJamesSimpleCycles;
import utilidades.Impressora;
import utilidades.SwTestingUtils;

public class Mestrado_Problem extends AbstractIntegerProblem{

	  private static final long serialVersionUID = -6291831458123882892L;

	  private List<List<String>> simpleCircuits = new ArrayList<List<String>>();
	  private List<List<String>> allPaths = new ArrayList<List<String>>();
	    
	  private Set<String> allEdgesSt = new LinkedHashSet<String>();
	  private List<DefaultEdge> edgeList = new ArrayList<DefaultEdge>();
	  private int nos = 0;
	  private int arestas = 0;
      private Set<String> terminalVertices = new LinkedHashSet<String>();

	  
	  public  Mestrado_Problem(int m, String caminho, String instancia,int ec) {
		// TODO Auto-generated constructor stub
		  	setNumberOfVariables(10); // Configuration More: 10 decision variables = simple circuits = test cases
	        setNumberOfObjectives(m); 
	        setNumberOfConstraints(0);
	        setName("INMEHY");
	        
	        List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables());
	        List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables());

	        
	        /*
	         * This parts handles the EFG that represents the GUI application 
	         */
	        
	        System.out.println("#### PROBLEM INSTANCE (Inside): " + getName());
	        
	     // Initial Vertices (States) gui1.
	        String[] initStates = {"main_2"};
	        // Instantiate the Graph
	        Graph<String, DefaultEdge> directedGraph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
	        // Create the Vertices 
	        
			Digraph g= new Digraph();


	        try {
				//			stripDuplicatesFromFile(caminho_dot);
				FileReader arq = new FileReader(caminho);
				BufferedReader lerArq = new BufferedReader(arq);

				String linha = lerArq.readLine(); 

				while (linha != null) {

					linha = linha.toLowerCase();
					linha = linha.replace(";", "").replace("\t", "").replace(" ", "");


					if(!linha.contains("->") && !linha.contains("{") && !linha.contains("}")){
				        directedGraph.addVertex(linha);
				        if(nos == 0) {
				        	initStates[0] = linha;
				        	System.out.println(linha);
				        }
				        nos = nos + 1;
					} else if(linha.contains("->")){
						arestas = arestas + 1;
						directedGraph.addEdge(linha.split("->")[0], linha.split("->")[1]);
						g.addEdge(linha.split("->")[0], linha.split("->")[1]);
					}

					linha = lerArq.readLine();
				}
				arq.close();

			} catch (IOException e) {
				System.err.printf("Erro na abertura do arquivo",
						e.getMessage());
			} 
	        

//	        final int FACTOR_SC = 1000;
	        final int FACTOR_SC = 1;

	        int maximumSimpleCircuits = FACTOR_SC * getNumberOfVariables(); // Configuration More: 10000 possible simple circuits (test cases)
	      
	        Set<String> allVertices = directedGraph.vertexSet();
	        
	        Set<String> terminal = new LinkedHashSet<String>();
	       
	        for(String dv: allVertices) {
	     	   
	     	   if(dv.contains("final") ) {
		     		  terminalVertices.add(dv);
		     		  terminal.add(dv);
	     	   }
	        }
	        
	        if (terminalVertices.isEmpty()) {
	     	   directedGraph.addVertex("null_event");
	     	   //allVertices.clear();
	     	   allVertices = directedGraph.vertexSet();
	     	   for (int i = 0; i < initStates.length; i++) {
	     		  
	     		   directedGraph.addEdge(initStates[i],"null_event");
	     		   terminalVertices.add("null_event");
	     	   
	     	  }
	        }
	      
	        for (String tver: terminalVertices) {
	     	   
	     	   for (int i = 0; i < initStates.length; i++) {
	     		   directedGraph.addEdge(tver, initStates[i]);	     	   
	     	  }
	     	   
	        }
	        
	                        
	        
	        Set<DefaultEdge> allEdges = directedGraph.edgeSet();
	        for (DefaultEdge de: allEdges){
	        	this.allEdgesSt.add(de.toString());
	        }
	        
	        
	        
	        ComponentNameProvider<String> vertexIdProvider = new ComponentNameProvider<String>()
	        {
	            public String getName(String url)
	            {
	               // return url.getHost().replace('.', '_');
	            	return url.toString();
	            }
	        };
	        
	        ComponentNameProvider<String> vertexLabelProvider = new ComponentNameProvider<String>()
	        {
	            public String getName(String url)
	            {
	                return url.toString();
	            }
	        };
	        
	        
	        	    
	        //Verificar se melhora ou não
	        ChinesePostman cp = new ChinesePostman();
	        GraphWalk stringcp  = (GraphWalk)cp.getCPPSolution(directedGraph);
			this.simpleCircuits.add(stringcp.getVertexList());
			
			ArrayList<String> cpList = new ArrayList<String>();

			
			for (String string : (List<String>)stringcp.getVertexList()) {
				cpList.add(string);
				
				if(string.equals("final") && cpList.size() >1) {
					this.simpleCircuits.add((List<String>)cpList.clone());
					cpList.clear();
				}else if (string.equals("final") && cpList.size() == 1){
					cpList.clear();
				}
				
			}
			// final verificação
		    
	        ArrayList<List<String>>ArrayListpaths = new ArrayList<List<String>>();

	        ArrayListpaths = AllPaths.getAllPaths(g, initStates[0], "final");
	  	    for (List<String> list : ArrayListpaths) {
	  	    	GraphWalk gw = new GraphWalk(directedGraph, list,10.0)	;
				this.simpleCircuits.add(gw.getVertexList());
			}
	  	    	        	        
	        
//	        for (List<String> scr: this.simpleCircuits){ //pq precisa disso?
//	        	//System.out.println(sc);
//	        	scr.add(0, scr.get(scr.size() - 1)); // add last vertex
//	        	Collections.reverse(scr);
//	        }
	        
	        for (int i = 0; i < this.simpleCircuits.size(); i++) {
	        	try {
					Impressora.getInstance().salveTestCase(instancia+"_"+ec,""+i, this.simpleCircuits.get(i));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
	
	        
	      //salvar caso de teste
	        		   
	        System.out.println("#### Number of Vertices: " + directedGraph.vertexSet().size() + " and " + "Edges: " + allEdgesSt.size()) ;
	        /*
		    * End - EFG Handling
		    */
	        
	        
	        
	        for (int i = 0; i < getNumberOfVariables(); i++) {
	            lowerLimit.add(0);
	            upperLimit.add((this.simpleCircuits.size() - 1)); // It depends on the number of SC, Test Case
	            
	        }

	        System.out.println(this.simpleCircuits.size());
	        setLowerLimit(lowerLimit);
	        setUpperLimit(upperLimit);
	 
	}
	
	@Override
	public void evaluate(IntegerSolution solution) {
		// TODO Auto-generated method stub
		double[] fx = new double[solution.getNumberOfObjectives()];
        List<Integer> varInt = new ArrayList<Integer>();

        for (int i = 0; i < getNumberOfVariables(); i++) {
            varInt.add(solution.getVariableValue(i));
        }

        try {
			fx[0] = SwTestingUtils.sizeTestSuite(this.simpleCircuits, varInt); // Test Suite Size (min)
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try {
			fx[1] = SwTestingUtils.executionEffort(this.simpleCircuits, varInt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        fx[2] = 1.0 - SwTestingUtils.edgeCoverage(this.allEdgesSt, this.simpleCircuits, varInt); // Edge Coverage (max) as a min problem
        
//        try {
//			fx[3] = SwTestingUtils.testCaseDiversity(this.simpleCircuits, varInt, 0.5); // Test Case Diversity - GW/Dice (min)
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

                
        
        solution.setObjective(0, fx[0]);
        solution.setObjective(1, fx[1]);
        solution.setObjective(2, fx[2]);
//        solution.setObjective(3, fx[3]);
	}

}
