package problemas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.io.ComponentNameProvider;

import dependencias_abstract.AbstractIntegerProblem;
import dependencias_interfaces.IntegerSolution;
import utilidades.HawickJamesSimpleCycles;
import utilidades.SwTestingUtils;

public class Teste extends AbstractIntegerProblem {

	  private static final long serialVersionUID = -6291831458123882892L;

	  private List<List<String>> simpleCircuits = new ArrayList<List<String>>();
	    
	  private Set<String> allEdgesSt = new LinkedHashSet<String>();
	  private int nos = 0;
	  private int arestas = 0;
	
	  public  Teste(int m, String caminho) {
		  setNumberOfVariables(10); // Configuration More: 10 decision variables = simple circuits = test cases
	        setNumberOfObjectives(m); 
	        setNumberOfConstraints(0);
	        setName("GUI1");
	        List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables());
	        List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables());

	        
	        /*
	         * This parts handles the EFG that represents the GUI application 
	         */
	        
	        System.out.println("#### PROBLEM INSTANCE (Inside): " + getName());
	        
	     // Initial Vertices (States) gui1.
	        String[] initStates = {"main_35"};
	        // Instantiate the Graph
	        Graph<String, DefaultEdge> directedGraph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
	        // Create the Vertices 
	        directedGraph.addVertex("main_35");
	        directedGraph.addVertex("Usuario_novoU_22_36");
	        directedGraph.addVertex("Conta_novaConta_37");
	        directedGraph.addVertex("bool_uValido_novoU_verificaIdade_38");
	        directedGraph.addVertex("if_39");
	        directedGraph.addVertex("novaConta_inicializa_novoU_100_40");
	        directedGraph.addVertex("novaConta_deposita_10_41");
	        directedGraph.addVertex("return_43");
	        directedGraph.addVertex("Usuario_71");
	        directedGraph.addVertex("idade_idade_72");
	        directedGraph.addVertex("verificaIdade_74");
	        directedGraph.addVertex("if_75");
	        directedGraph.addVertex("return_76");
	        directedGraph.addVertex("else_78");
	        directedGraph.addVertex("return_79");
	        directedGraph.addVertex("inicializa_54");
	        directedGraph.addVertex("usuario_usuario_55");
	        directedGraph.addVertex("saldo_s_56");
	        directedGraph.addVertex("if_57");
	        directedGraph.addVertex("Erro_na_Cria_o_da_Conta_58");
	        directedGraph.addVertex("deposita_61");
	        directedGraph.addVertex("saldo_saldo_valor_62");
	        directedGraph.addVertex("final");
	         directedGraph.addEdge("main_35","Usuario_novoU_22_36");
	         directedGraph.addEdge("Usuario_novoU_22_36","Conta_novaConta_37");
	         directedGraph.addEdge("Conta_novaConta_37","bool_uValido_novoU_verificaIdade_38");
	         directedGraph.addEdge("bool_uValido_novoU_verificaIdade_38","if_39");
	         directedGraph.addEdge("if_39","novaConta_inicializa_novoU_100_40");
	         directedGraph.addEdge("novaConta_inicializa_novoU_100_40","novaConta_deposita_10_41");
	         directedGraph.addEdge("novaConta_deposita_10_41","return_43");
	         directedGraph.addEdge("if_39","return_43");
	         directedGraph.addEdge("return_43","final");
	         directedGraph.addEdge("usuario_usuario_55","saldo_s_56");
	         directedGraph.addEdge("saldo_s_56","if_57");
	         directedGraph.addEdge("if_57","Erro_na_Cria_o_da_Conta_58");
	         directedGraph.addEdge("if_75","return_76");
	         directedGraph.addEdge("if_75","else_78");
	         directedGraph.addEdge("else_78","return_79");
	         directedGraph.addEdge("Usuario_71","idade_idade_72");
	         directedGraph.addEdge("Usuario_novoU_22_36","Usuario_71");
	         directedGraph.addEdge("idade_idade_72","Usuario_novoU_22_36");
	         directedGraph.addEdge("verificaIdade_74","if_75");
	         directedGraph.addEdge("bool_uValido_novoU_verificaIdade_38","verificaIdade_74");
	         directedGraph.addEdge("return_76","bool_uValido_novoU_verificaIdade_38");
	         directedGraph.addEdge("return_79","bool_uValido_novoU_verificaIdade_38");
	         directedGraph.addEdge("inicializa_54","usuario_usuario_55");
	         directedGraph.addEdge("novaConta_inicializa_novoU_100_40","inicializa_54");
	         directedGraph.addEdge("Erro_na_Cria_o_da_Conta_58","novaConta_inicializa_novoU_100_40");
	         directedGraph.addEdge("deposita_61","saldo_saldo_valor_62");
	         directedGraph.addEdge("novaConta_deposita_10_41","deposita_61");
	         directedGraph.addEdge("saldo_saldo_valor_62","novaConta_deposita_10_41");

	           
	        final int FACTOR_SC = 1000;
	        int maximumSimpleCircuits = FACTOR_SC * getNumberOfVariables(); // Configuration More: 10000 possible simple circuits (test cases)
	      
	        Set<String> allVertices = directedGraph.vertexSet();
	        Set<String> terminalVertices = new LinkedHashSet<String>();
	        
	        //System.out.println("\nInit Vertices"); 
	        //for(int ii = 0; ii < initStates.length; ii++) {
	     	  // System.out.println(initStates[ii]);
	     	   
	        //}   
	        
	        //System.out.println("\nAll Vertices Before"); 
	        for(String dv: allVertices) {
	     	   //System.out.println(dv);
//	     	   if (!(Arrays.stream(initStates).anyMatch(dv::equals))) {
//	     		   terminalVertices.add(dv);
//	     	   }
	           if(dv.contains("final")) {
		     		  terminalVertices.add(dv);
	     	   }
	        }
	        
	        if (terminalVertices.isEmpty()) {
	     	   directedGraph.addVertex("null_event");
	     	   //allVertices.clear();
	     	   allVertices = directedGraph.vertexSet();
	     	   for (int i = 0; i < initStates.length; i++) {
	     		  
	     		   directedGraph.addEdge(initStates[i],"null_event");
	     		   terminalVertices.add("null_event");
	     		   //System.out.println("------- NULL NULL -------");
	     	   
	     	  }
	        }
	        
	        //System.out.println("\nTerminal Vertices"); 
	        //for(String dv: terminalVertices) {
	     	  // System.out.println(dv);
	     	  
	        //}	
	        
	        
	        //System.out.println("\nAll Vertices After"); 
	       // for(String dv: allVertices) {
	     	 //  System.out.println(dv);
	     	   //if (!(Arrays.stream(initStates).anyMatch(dv::equals))) {
	     		 //  terminalVertices.add(dv);
	     	   //}
	        //}
	        
	        //System.out.println("");
	        // making cycles with the terminal vertices
	        for (String tver: terminalVertices) {
	     	   
	     	   
	     	   for (int i = 0; i < initStates.length; i++) {
	     		   directedGraph.addEdge(tver, initStates[i]);
	     	   
	     	  }
	     	  // directedGraph.addEdge(tver, initStates[genIndexSolution(initStates.length)]);
	     	   
	        }
	                        
	        
	        Set<DefaultEdge> allEdges = directedGraph.edgeSet();
	        //Set<String> allEdgesSt = new LinkedHashSet<String>();
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
	        

	    
	        /*
	         * Get the simple circuits.
	         * Configuration More, Constraint WIDE                
	         */
	        HawickJamesSimpleCycles<String, DefaultEdge> HawJam = new HawickJamesSimpleCycles<String, DefaultEdge>(directedGraph, terminalVertices, maximumSimpleCircuits,
	        		"WIDE");
	        this.simpleCircuits = HawJam.findSimpleCycles();
	        
	        // Choose simple circuits only with initial states
	        for (Iterator<List<String>> iter = this.simpleCircuits.listIterator(); iter.hasNext();) {
	        	String ini = SwTestingUtils.getLastElement(iter.next()); // last element of a simple circuit = initial vertex of a cycle
	            if (!Arrays.asList(initStates).contains(ini))
	                 iter.remove();
	        }
	               
	          System.out.println(this.simpleCircuits.size());
	       
	              
	        //System.out.println("\n\n----- REVERSED VERTICES -----  \n\n");
	        for (List<String> scr: this.simpleCircuits){
	        	scr.add(0, scr.get(scr.size() - 1)); // add last vertex
	        	Collections.reverse(scr);
	        	System.out.println(scr);
	        }
	        
	        AllDirectedPaths a = new AllDirectedPaths(directedGraph);
//		       
		        for (Iterator iterator = terminalVertices.iterator(); iterator.hasNext();) {
					
					List<List<String>> b =  a.getAllPaths(initStates[0], iterator.next(), false, maximumSimpleCircuits);
					
					for (int i = 0; i < b.size(); i++) {
						System.out.println(b.get(i));				
					}
					System.out.println("b size "+b.size());
				}
	        
	        
		   
	        System.out.println("#### Number of Vertices: " + directedGraph.vertexSet().size() + " and " + "Edges: " + allEdgesSt.size()) ;
	        /*
		    * End - EFG Handling
		    */
	        
	        
	        for (int i = 0; i < getNumberOfVariables(); i++) {
	            //lowerLimit.add(0.0);
	            //upperLimit.add((double) (this.simpleCircuits.size() - 1)); // It depends on the number of SC, Test Case
	            
	            lowerLimit.add(0);
	            upperLimit.add((this.simpleCircuits.size() - 1)); // It depends on the number of SC, Test Case
	            
	        }
	        setLowerLimit(lowerLimit);
	        setUpperLimit(upperLimit);
	    }

	    /**
	     * Evaluate() method
	     *
	     * @param solution
	     */
	    @Override
	    public void evaluate(IntegerSolution solution) {
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
				fx[1] = SwTestingUtils.testCaseDiversity(this.simpleCircuits, varInt, 0.5); // Test Case Diversity - GW/Dice (min)
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				fx[2] = SwTestingUtils.testCaseDiversity(this.simpleCircuits, varInt, 2.0); // Test Case Diversity - SS/Anti-Dice (min)
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
//	        fx[3] = 1.0 - (SwTestingUtils.edgeCoverage(this.allEdgesSt, this.simpleCircuits, varInt));  // Edge Coverage (max) as a min problem
	        
	        
	        solution.setObjective(0, fx[0]);
	        solution.setObjective(1, fx[1]);
	        solution.setObjective(2, fx[2]);
//	        solution.setObjective(3, fx[3]);
	    }
	}
