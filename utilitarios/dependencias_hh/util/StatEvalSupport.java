package dependencias_hh.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dependencias_interfaces.Problem;
import utilidades.Impressora;

//import org.uma.jmetal.problem.Problem;

public class StatEvalSupport implements Serializable {
	
	private List<Double> hypervolume;
	private List<Double> epsilon;
	private List<Double> igdplus;
	
	
	 private static StatEvalSupport instance = null;
	 protected StatEvalSupport() { // here is what first initialization should be done
	      //System.out.println("Sempre?");
		 this.hypervolume = new ArrayList<Double>();
		 this.epsilon = new ArrayList<Double>();
	
		 this.igdplus = new ArrayList<Double>();
	   }
	   public static StatEvalSupport getInstance() {
	      if(instance == null) {
	    	  //System.out.println("Dentro de inst!");
	         instance = new StatEvalSupport();
	        
	      }
	      //System.out.println("Fora de inst!");
	      
	      return instance;
	   }
	   
	   public void recordDataStatEval(String indicator, Double value) {
		 switch(indicator) {
           case "HYPERVOLUME" :
             hypervolume.add(value); 
             break;
           case "EPSILON" :
         	 epsilon.add(value); 
             break;
           case "IGDPLUS" :
             igdplus.add(value); 
             break;    
           default :
             System.out.println("%%%%% Invalid Indicator!");
		   
	     }
	 }
	   
	 public List<Double> getAllHypervolume() {
		 return this.hypervolume;
	 }
	 
	 
	 public List<Double> getAllEpsilon() {
		 return this.epsilon;
	 }
	 
	
	public List<Double> getAllIGDPlus() {
	    return this.igdplus;
	}
	
	 
	 
	 public void clearAllIndicatorsStatEval() {
		 this.hypervolume.clear();
		 this.epsilon.clear();
		 this.igdplus.clear();
	 }
	 
	 public void saveFileStatEval(String optimizationSol, String indicatorType, List<Double> indicatorValue, Problem problem, String name, int ec, int passo) throws IOException {
		 // PS: colocar problem como outro par. String dir = "result/" + optimizationSol + "/" + problem.getName() + "_" + problem.getNumberOfObjectives();
		 Impressora.getInstance()
			.verifyDiretorio("files\\" + name + "" + ec + "_" + passo + "\\" + optimizationSol);

		 
		 
		 String indicatorFile = "files\\" + name + "" + ec + "_" + passo+ "\\" + optimizationSol + "\\" + indicatorType+".tsv";
			
//		 String indicatorFile = dir + "/" + indicatorType + ".tsv";
	     
	     FileOutputStream f = new FileOutputStream(indicatorFile);
	     //ObjectOutputStream out = new ObjectOutputStream(f);
	     for (double iV: indicatorValue) {
	    	 String valueTab = String.valueOf(iV) + "\t";
	    	 f.write(valueTab.getBytes(), 0, valueTab.length());
	     }
	     //out.writeObject(indicatorValue);
	     f.close();
	        //String varFile = dir + "/VAR" + i + ".tsv";
	 }

	 public void saveFileStatEvalAppend(String optimizationSol, String indicatorType, List<Double> indicatorValue, Problem problem, int first) throws IOException {
		 // PS: colocar problem como outro par. String dir = "result/" + optimizationSol + "/" + problem.getName() + "_" + problem.getNumberOfObjectives();
		 String dir = "result/" + optimizationSol + "/" +  problem.getName() + "_" + problem.getNumberOfObjectives();
		 String indicatorFile = dir + "/" + indicatorType + ".tsv";
		 
		 if (first == 0) { // first. Do not append
	       new File(dir).mkdirs();
	     //String indicatorFile = dir + "/" + indicatorType + ".tsv";
	     
	       FileOutputStream f = new FileOutputStream(indicatorFile);
	     //ObjectOutputStream out = new ObjectOutputStream(f);
	       for (double iV: indicatorValue) {
	    	 String valueTab = String.valueOf(iV) + "\t";
	    	 f.write(valueTab.getBytes(), 0, valueTab.length());
	       }
	     //out.writeObject(indicatorValue);
	       f.close();
		 } else { // append
			   FileOutputStream ff = new FileOutputStream(indicatorFile, true);
		     //ObjectOutputStream out = new ObjectOutputStream(f);
		       for (double iV: indicatorValue) {
		    	 String valueTab = String.valueOf(iV) + "\t";
		    	 ff.write(valueTab.getBytes(), 0, valueTab.length());
		       }
		     //out.writeObject(indicatorValue);
		       ff.close();
		 }
			 
	        //String varFile = dir + "/VAR" + i + ".tsv";
	 }
	 
}
