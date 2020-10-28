package dependencias_hh.util;

import java.util.ArrayList;
import java.util.List;

public class QualityIndicatorsHandler {
	
	private List<Double> hypervolume;
	private List<Double> igdPlus;
	private List<Double> epsilon;
	
	public QualityIndicatorsHandler() { // here is what first initialization should be done
	      //System.out.println("Sempre?");
		 this.hypervolume = new ArrayList<Double>();
		 this.epsilon = new ArrayList<Double>();
		 this.igdPlus = new ArrayList<Double>();
	}
	
	public List<Double> getAllIndicatorsCF (List<List<Double>> qualityIndicators, String ind){
		
		//System.out.println("Before indNotNormal: " + qualityIndicators);
		// Get the values of all indicators per stage
		for (List<Double> eachQI: qualityIndicators) {
			hypervolume.add(eachQI.get(0));
			epsilon.add(eachQI.get(1));			
			igdPlus.add(eachQI.get(2));
		}
		
		List<Double> res = new ArrayList<Double>();
		
		switch(ind) {
          case "HYPERVOLUME" :
            res.addAll(this.hypervolume);
            break;
          case "EPSILON" :
        	res.addAll(this.epsilon);
            break;    
          case "IGDPLUS" :
          	res.addAll(this.igdPlus);
              break;
          default:
        	  System.out.println("Wrong Quality Ind");
            
	   }
		
	   return res;	
	}	
	
	public void clearAllIndicatorsCF() {
		this.hypervolume.clear();
		this.igdPlus.clear();
		this.epsilon.clear();
	}
}
