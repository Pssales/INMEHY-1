package utilidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dependencias_interfaces.IntegerSolution;

public class Populacoes {
	
	
	private List<SolucaoIndividual> allpopIBEATodoMundoMesmo; 	
	private List<SolucaoIndividual> allpopMOMBITodoMundoMesmo;
	private List<SolucaoIndividual> allpopNSGAIITodoMundoMesmo;
	private List<SolucaoIndividual> allpopNSGAIIITodoMundoMesmo;
	private List<SolucaoIndividual> allpopSPEA2TodoMundoMesmo;
	private List<SolucaoIndividual> allpopHRISE_RTodoMundoMesmo;
	private List<SolucaoIndividual> allpopHHCFTodoMundoMesmo;
	private List<SolucaoIndividual> allpopHRISE_MTodoMundoMesmo;
	private List<IntegerSolution> popHRISE_R;
	private String algoritmo;


	private static Populacoes uniqueInstance;
	
	private Populacoes() {
		allpopIBEATodoMundoMesmo = new ArrayList<SolucaoIndividual>();		
		allpopMOMBITodoMundoMesmo = new ArrayList<SolucaoIndividual>();
		allpopNSGAIITodoMundoMesmo = new ArrayList<SolucaoIndividual>();
		allpopNSGAIIITodoMundoMesmo = new ArrayList<SolucaoIndividual>();
		allpopSPEA2TodoMundoMesmo = new ArrayList<SolucaoIndividual>();
		allpopHRISE_RTodoMundoMesmo = new ArrayList<SolucaoIndividual>();
		allpopHHCFTodoMundoMesmo = new ArrayList<SolucaoIndividual>();
		allpopHRISE_MTodoMundoMesmo = new ArrayList<SolucaoIndividual>();
		popHRISE_R = new ArrayList<IntegerSolution>();
		algoritmo = "";
	}
	
	
	public static Populacoes getInstance() {
		if(uniqueInstance==null) {
			uniqueInstance = new Populacoes();
		}
		return uniqueInstance;
	}

	public void addAllpopIBEATodoMundoMesmo(SolucaoIndividual popIBEA) {
		SolucaoIndividual novo_1 = new SolucaoIndividual();
		novo_1 = popIBEA.clonar();
		allpopIBEATodoMundoMesmo.add(novo_1);
	}
	
	public void addAllpopMOMBITodoMundoMesmo(SolucaoIndividual popMOMBI) {
		SolucaoIndividual novo_1 = new SolucaoIndividual();
		novo_1 = popMOMBI.clonar();
		allpopMOMBITodoMundoMesmo.add(novo_1);
	}
	public void addAllpopNSGAIITodoMundoMesmo(SolucaoIndividual popNSGAII) {
		SolucaoIndividual novo_1 = new SolucaoIndividual();
		novo_1 = popNSGAII.clonar();
		allpopNSGAIITodoMundoMesmo.add(novo_1);
	}
	public void addAllpopNSGAIIITodoMundoMesmo(SolucaoIndividual popNSGAIII) {
		SolucaoIndividual novo_1 = new SolucaoIndividual();
		novo_1 = popNSGAIII.clonar();
		allpopNSGAIIITodoMundoMesmo.add(novo_1);
	}
	public void addAllpopSPEA2TodoMundoMesmo(SolucaoIndividual popSPEA2) {
		SolucaoIndividual novo_1 = new SolucaoIndividual();
		novo_1 = popSPEA2.clonar();
		allpopSPEA2TodoMundoMesmo.add(novo_1);
	}

	public void addAllpopHHCFTodoMundoMesmo(SolucaoIndividual popHHCF) {
		SolucaoIndividual novo_1 = new SolucaoIndividual();
		novo_1 = popHHCF.clonar();
		allpopHHCFTodoMundoMesmo.add(novo_1);
	}

	public void addAllpopHRISE_MTodoMundoMesmo(SolucaoIndividual popHRISE_M) {
		SolucaoIndividual novo_1 = new SolucaoIndividual();
		novo_1 = popHRISE_M.clonar();
		allpopHRISE_MTodoMundoMesmo.add(novo_1);
		
	}
	

	public void addAllpopHRISE_RTodoMundoMesmo(SolucaoIndividual pop_HRISE_R) {
		// TODO Auto-generated method stub
		SolucaoIndividual novo_1 = new SolucaoIndividual();
		novo_1 = pop_HRISE_R.clonar();
		allpopHRISE_RTodoMundoMesmo.add(novo_1);
	}



	
	public List<SolucaoIndividual> getAllpopIBEATodoMundoMesmo() {
		return allpopIBEATodoMundoMesmo;
	}


	public void setAllpopIBEATodoMundoMesmo(List<SolucaoIndividual> allpopIBEATodoMundoMesmo) {
		this.allpopIBEATodoMundoMesmo = allpopIBEATodoMundoMesmo;
	}


	public List<SolucaoIndividual> getAllpopMOMBITodoMundoMesmo() {
		return allpopMOMBITodoMundoMesmo;
	}


	public void setAllpopMOMBITodoMundoMesmo(List<SolucaoIndividual> allpopMOMBITodoMundoMesmo) {
		this.allpopMOMBITodoMundoMesmo = allpopMOMBITodoMundoMesmo;
	}


	public List<SolucaoIndividual> getAllpopNSGAIIITodoMundoMesmo() {
		return allpopNSGAIIITodoMundoMesmo;
	}


	public void setAllpopNSGAIIITodoMundoMesmo(List<SolucaoIndividual> allpopNSGAIIITodoMundoMesmo) {
		this.allpopNSGAIIITodoMundoMesmo = allpopNSGAIIITodoMundoMesmo;
	}


	public List<SolucaoIndividual> getAllpopSPEA2TodoMundoMesmo() {
		return allpopSPEA2TodoMundoMesmo;
	}

	public void setAllpopSPEA2TodoMundoMesmo(List<SolucaoIndividual> allpopSPEA2TodoMundoMesmo) {
		this.allpopSPEA2TodoMundoMesmo = allpopSPEA2TodoMundoMesmo;
	}


	public String getAlgoritmo() {
		return algoritmo;
	}


	public void setAlgoritmo(String algoritmo) {
		this.algoritmo = algoritmo;
	}
	
	public List<SolucaoIndividual> getAllpopHHCFTodoMundoMesmo() {
		return allpopHHCFTodoMundoMesmo;
	}

	public void setAllpopHHCFTodoMundoMesmo(List<SolucaoIndividual> allpopHHCFTodoMundoMesmo) {
		this.allpopHHCFTodoMundoMesmo = allpopHHCFTodoMundoMesmo;
	}

	public void setAllpopHRISE_MTodoMundoMesmo(List<SolucaoIndividual> allpopHRISE_MTodoMundoMesmo) {
		this.allpopHRISE_MTodoMundoMesmo = allpopHRISE_MTodoMundoMesmo;
	}

	public List<SolucaoIndividual> getAllpopHRISE_MTodoMundoMesmo() {
		return allpopHRISE_MTodoMundoMesmo;
	}
	
	public void setAllpopHRISE_RTodoMundoMesmo(List<SolucaoIndividual> allpopHRISE_RTodoMundoMesmo) {
		this.allpopHRISE_RTodoMundoMesmo = allpopHRISE_RTodoMundoMesmo;
	}

	public List<SolucaoIndividual> getAllpopHRISE_RTodoMundoMesmo() {
		return allpopHRISE_RTodoMundoMesmo;
	}



	



	
}
