package dependencias_hh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

//import br.inpe.cocte.labac.hrise.jhelper.util.ProblemFactory;

//import br.inpe.cocte.labac.hrise.util.SaveFiles;
//import dependencias_hh.jhelper.util.ProblemFactory;
import dependencias_hh.jhelper.util.metrics.EpsilonCalculator;
import dependencias_hh.jhelper.util.metrics.EpsilonPlusCalculator;
import dependencias_hh.jhelper.util.metrics.HypervolumeCalculator;
import dependencias_hh.jhelper.util.metrics.IgdCalculator;
import dependencias_hh.jhelper.util.metrics.IgdPlusCalculator;
import dependencias_hh.jhelper.util.metrics.SpreadCalculator;
import dependencias_hh.jhelper.util.metrics.UDMetricHandler;
import dependencias_hh.util.PopulationHandler;
import dependencias_hh.util.ProblemsWrapper;
import dependencias_hh.util.SaveFiles;
import dependencias_hh.util.StatEvalSupport;
import dependencias_interfaces.IntegerSolution;
import dependencias_interfaces.Problem;
import dependencias_interfaces.Solution;
//import org.uma.jmetal.problem.Problem;
//import org.uma.jmetal.solution.Solution;
//import br.inpe.cocte.labac.hrise.jhelper.util.ProblemFactory;
//import br.inpe.cocte.labac.hrise.jhelper.util.metrics.EpsilonCalculator;
//import br.inpe.cocte.labac.hrise.jhelper.util.metrics.EpsilonPlusCalculator;
//import br.inpe.cocte.labac.hrise.jhelper.util.metrics.HypervolumeCalculator;
//import br.inpe.cocte.labac.hrise.jhelper.util.metrics.IgdCalculator;
//import br.inpe.cocte.labac.hrise.jhelper.util.metrics.IgdPlusCalculator;
//import br.inpe.cocte.labac.hrise.jhelper.util.metrics.SpreadCalculator;
//import br.inpe.cocte.labac.hrise.jhelper.util.metrics.UDMetricHandler;
//import br.inpe.cocte.labac.hrise.util.PopulationHandler;
//import br.inpe.cocte.labac.hrise.util.ProblemsWrapper;
//import br.inpe.cocte.labac.hrise.util.SaveFiles;
//import br.inpe.cocte.labac.hrise.util.StatEvalSupport;



public class QualityIndicatorsRealProblems<S extends Solution<?>> {
	
	private Map<String, List<S>> allPFKnown; 
	private static QualityIndicatorsRealProblems instance = null;
	
	
	protected QualityIndicatorsRealProblems() { 
	  
		 this.allPFKnown = new LinkedHashMap<String, List<S>>();
		
	}
	
	
	public static QualityIndicatorsRealProblems getInstance() {
	    if(instance == null) {
	    	  
	         instance = new QualityIndicatorsRealProblems();
	        
	    }
	          
	    return instance;
	}
	
	
	public void addPFKnown(String hh, List<S> pop) {
//		System.out.println("add: "+hh);
//		System.out.println(pop.size());
//    	Scanner a = new Scanner(System.in);
//    	a.nextInt();
		
		List<S> auxPop = new ArrayList<>();
		auxPop.addAll(pop);
		
		this.allPFKnown.put(hh,auxPop);
	}
	
	
    public Map<String,List<S>> getAllPFKnown(){
	    	return this.allPFKnown;
	    }
	
    
    public int getAllPFKnownSize() {
    	return this.allPFKnown.size();
    }
    
    
    public void clearAllPFKnown() {
    	System.out.println("limpo");
//    	Scanner a = new Scanner(System.in);
//    	a.nextInt();
    	this.allPFKnown.clear();
    }
    
    
    public void calculateQualityIndicatorsReal(String[] hh, String opt, String srcDir, String destDir, Problem<IntegerSolution> problem2, int ec, String name, int passo) throws IOException {
	   
	   int l, m, k;
	   String problemString, versionHH;
	   ProblemsWrapper prw = new ProblemsWrapper(opt);
	   
	   l = prw.getL();
	   m = prw.getM();
	   problemString = prw.getProblemString();
	   
	   k = 2 * (m - 1); // POSITION VARIABLES

       
       Problem problem = problem2;
	   
//       FileUtils.deleteDirectory(new File("result"));
//       FileUtils.deleteDirectory(new File("pareto_fronts_true"));
       
	   
	   PopulationHandler popHandler = new PopulationHandler();
	   // Get all Known PFs
	   String filepfTrueKnown = " ";
	   List<S> pfTrueKnown = new ArrayList<>();
	//    System.out.println(allPFKnown.entrySet());
	   
	   for(Entry<String, List<S>> onePFKnown : allPFKnown.entrySet()){
		    System.out.println("Known PF Id: " + onePFKnown.getKey() + " -- Known PF Size: " + onePFKnown.getValue().size()); 
		    if (onePFKnown.getKey().contains(problem.getName())){ // for precaution. Per problem. 
		       pfTrueKnown.addAll(onePFKnown.getValue());
		    } 
	   }
	  
	   pfTrueKnown = popHandler.generateNonDominated(pfTrueKnown, problem); 
             
       pfTrueKnown = popHandler.removeRepeatedSolutionsInteger(pfTrueKnown);
       
       
	   filepfTrueKnown = "pareto_fronts_true/"+
      		 problem.getName()+"."+problem.getNumberOfObjectives()+"D.True"+
      		 ".pf";
       SaveFiles sPFK = new SaveFiles();
       sPFK.savePFTrueKnown(filepfTrueKnown, pfTrueKnown, "pareto_fronts_true");
       
       System.out.println("........... Copying Results: True Known PF ............................................");
       copyDirectory(srcDir + "/pareto_fronts_true",
	            destDir + "/PFTRUEKNOWN/");	
       
       for (int indHH = 0; indHH < hh.length; indHH++) { 
		  versionHH = hh[indHH];	
		  
		  for(Entry<String, List<S>> onePop : allPFKnown.entrySet()){
			    
			    
			    if (onePop.getKey().contains(versionHH + "." + problem.getName())){ // for precaution. Per problem. 
			       
			       saveIndicatorsReal(onePop.getValue(), problem, filepfTrueKnown, true);
			    }  
//			    else {
//			    	System.out.println(versionHH);
//			    	Scanner a = new Scanner(System.in);
//			    	a.nextInt();
//			    }
			    
		   }
		  
		
		  StatEvalSupport stind = StatEvalSupport.getInstance();
		  
	      
	    	
		  
		  
          stind.saveFileStatEval(versionHH, versionHH + "_hypervolume", stind.getAllHypervolume(), problem, name, ec, passo);
		  stind.saveFileStatEval(versionHH, versionHH + "_epsilon", stind.getAllEpsilon(), problem, name, ec, passo);
		  stind.saveFileStatEval(versionHH, versionHH + "_igdplus", stind.getAllIGDPlus(), problem, name, ec, passo);
		  
		  
		  System.out.println("........... Copying Results: Real Quality Indicators ................................................................");
		  copyDirectory(srcDir + "/result",
					            destDir + "/ALLRES/" + problem.getName() + "_" + problem.getNumberOfObjectives());
		  
		  		  
		  stind.clearAllIndicatorsStatEval();
		}  
       
   }
   
  
   private String findStr(String s, char c){
	   return s.substring(0, s.indexOf(c));
	} 
   
   
   private String printIndicatorsReal(List archive, Problem problem, String pf) throws FileNotFoundException, IOException {
       
   	String strToReturn = "";
     
           // HV: Convergence-Diversity
           PopulationHandler popHndList = new PopulationHandler();
           HypervolumeCalculator hyp = new HypervolumeCalculator(problem.getNumberOfObjectives(), pf);
           
           double hypValue = hyp.execute(archive);
           
           // Epsilon: Convergence
           EpsilonCalculator eps=new EpsilonCalculator(problem.getNumberOfObjectives(), pf);
           double epsilonValue=eps.execute(archive);
                      
           
           // IGD+: Convergence-Diversity
           IgdPlusCalculator igdplus = new IgdPlusCalculator(problem.getNumberOfObjectives(), pf);
           double igdPlusValue = igdplus.execute(archive);
           
           
           strToReturn="Hyper: " + hypValue + " ## " + 
        		        "Epsilon: " + epsilonValue + " ## " +
                        "IGD Plus: " + igdPlusValue + " ## " ;
           
       
       return strToReturn;
   }
   
   private List<Double> saveIndicatorsReal(List archive, Problem problem, String pf, boolean recordStatEval) throws FileNotFoundException, IOException {
       
   	List<Double> qual = new ArrayList<Double>();
       StatEvalSupport stindicators = StatEvalSupport.getInstance();
       

           // HV: Convergence-Diversity
           PopulationHandler popHnd = new PopulationHandler();
           HypervolumeCalculator hyp = new HypervolumeCalculator(problem.getNumberOfObjectives(), pf);
        
           double hypValue = hyp.execute(archive);

           
           // Epsilon: Convergence
           EpsilonCalculator eps = new EpsilonCalculator(problem.getNumberOfObjectives(), pf);
           double epsilonValue = eps.execute(archive);
           
           // IGD+: Convergence-Diversity
           IgdPlusCalculator igdplus = new IgdPlusCalculator(problem.getNumberOfObjectives(), pf);
           double igdPlusValue = igdplus.execute(archive);
           
           
        
                       
           qual.add(hypValue);
           qual.add(epsilonValue);
           qual.add(igdPlusValue);
           
       
           if (recordStatEval) {
           	stindicators.recordDataStatEval("HYPERVOLUME", hypValue);
   			stindicators.recordDataStatEval("EPSILON", epsilonValue);
   			stindicators.recordDataStatEval("IGDPLUS", igdPlusValue); 			
   			
           }
       
       return qual;
   }

   private void copyDirectory(String sourceDir, String destDir) {
		File source = new File(sourceDir);
		File dest = new File(destDir);
		try {
		    FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
	}

}
