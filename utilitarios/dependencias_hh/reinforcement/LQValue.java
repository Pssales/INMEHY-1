package dependencias_hh.reinforcement;

public class LQValue {

	private String llh;
	private double quality;
	
	public LQValue(){
		
	}
	
	public void setLLH(String value) {
		this.llh = value;
	}
	
	public String getLLH() {
		return this.llh;
	}
	
	public void setQuality(double value) {
		this.quality = value;
	}
	
	public double getQuality() {
		return this.quality;
	}
	
	
}
