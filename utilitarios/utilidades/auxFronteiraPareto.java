package utilidades;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import algoritmos_evolucionarios.MOMBI_LLH_IntegerProblem;
import algoritmos_evolucionarios.NSGAIII_LLH_IntegerProblem;
import algoritmos_evolucionarios.NSGAII_LLH_IntegerProblem;
import dependencias_class.Saida;
import dependencias_class.SolutionListUtils;
import dependencias_interfaces.IntegerSolution;
import dependencias_interfaces.Problem;
import problemas.Camila_problema;
import utilidades.Impressora;

public class auxFronteiraPareto {

	//private List<List<IntegerSolution>> allpopNSGAII;
	private List<List<IntegerSolution>> allpopNSGAIII;
	//private List<List<IntegerSolution>> allpopMOMBI;
	private ArrayList<IntegerSolution> pfTrueKnown;
	//private List<IntegerSolution> popMOMBI;

	private int estudo_caso;
	private int trials;
	private String caminho_saida = "";
	private int maxTrials = 30;
	private int populationSize = 100;
	private int operador_crossover = 1;
	private int operador_mutacao = 1;
	private double crossoverProbability = 0.9;
	private double mutationProbability = 0.00125;
	private int numberValidations = 300;  //////// esse e o unico que muda;
	//private String weight_path = "C:\\Users\\camil\\eclipse-workspace\\jMetal-master\\resources\\weightVectorFiles\\mombi2\\weight_02D_100_mombi.sld";
	private static String caminho_projeto = "C:\\Users\\camil\\eclipse-workspace\\metaheuristic\\";
	

	public auxFronteiraPareto() {

		//allpopNSGAII = new ArrayList<List<IntegerSolution>>();
		allpopNSGAIII = new ArrayList<List<IntegerSolution>>();
		//allpopMOMBI = new ArrayList<List<IntegerSolution>>();
		pfTrueKnown = new ArrayList<IntegerSolution>();		
//		popMOMBI = new LinkedList<IntegerSolution>();

	}



	public void gerarFPFinal(int e, int t) throws IOException {

		System.out.println("#HUGS - gerando a Fronteira de Pareto - Integer Problem - so nativos...");

		this.estudo_caso = e;
		this.trials = t;
		this.caminho_saida = "pareto_fronts\\fronteira_pareto_"+estudo_caso+".pf";



		for(int i=0; i<trials; i++) {
			this.gerarFronteiraParetoUnitaria();
			this.limparTudo();
		}
		System.out.println("Fim !");

	}


	public void gerarFronteiraParetoUnitaria () throws IOException {

		//obrigatoriamente tem que estar aqui
		Problem<IntegerSolution> problem = new Camila_problema(caminho_projeto+"dots/"+estudo_caso+".dot");

		System.out.println("Executando...");

/**
	
		for(int trial = 0; trial < maxTrials; trial++){
			NSGAII_LLH_IntegerProblem nsga_nativo = new NSGAII_LLH_IntegerProblem(problem, populationSize, crossoverProbability, mutationProbability, operador_crossover, operador_mutacao, numberValidations);
			try {
				Saida popNSGA_nativo = nsga_nativo.execute();					   
				List<IntegerSolution> popnd = SolutionListUtils.getNondominatedSolutions(popNSGA_nativo.getPopulacao_final());
				allpopNSGAII.add(popnd); 
				pfTrueKnown.addAll(popnd); 
				System.out.println(trial + " NSGA-II " ); 
			} catch (Exception eee) {
				eee.printStackTrace();
			}

		} 
**/
		for(int trial = 0; trial < maxTrials; trial++){
			System.out.println("NSGA III" + trial);
			NSGAIII_LLH_IntegerProblem nsgaiii_nativo = new NSGAIII_LLH_IntegerProblem(problem, populationSize, crossoverProbability, mutationProbability, operador_crossover, operador_mutacao, numberValidations);
			try {
				Saida popNSGAIII_nativo = nsgaiii_nativo.execute();						   
				List<IntegerSolution> popnd = SolutionListUtils.getNondominatedSolutions(popNSGAIII_nativo.getPopulacao_final());
				allpopNSGAIII.add(popnd); 
				pfTrueKnown.addAll(popnd); 
				System.out.println(trial + " NSGAIII" );   
			} catch (Exception eee) {
				eee.printStackTrace();
			}

		} 

	/**	
		for(int trial = 0; trial < maxTrials; trial++){
			MOMBI_LLH_IntegerProblem mombi_nativo = new MOMBI_LLH_IntegerProblem(problem, crossoverProbability, mutationProbability, operador_crossover, operador_mutacao, (numberValidations/populationSize), weight_path);
			try {
				Saida popMOMBI_nativo = mombi_nativo.execute();						   
				popMOMBI = SolutionListUtils.getNondominatedSolutions(popMOMBI_nativo.getPopulacao_final());
				allpopMOMBI.add(popMOMBI); 
				pfTrueKnown.addAll(popMOMBI);
				System.out.println(trial + " MOMBI");       
			} catch (Exception eee) {
				eee.printStackTrace();
			}

		}
**/
		pfTrueKnown = (ArrayList<IntegerSolution>) SolutionListUtils.getNondominatedSolutions(pfTrueKnown);
		String fronteira = "";
		for(IntegerSolution elemento : pfTrueKnown){
			if(!fronteira.contains(elemento.getObjective(0) + " " + elemento.getObjective(1))) {
				fronteira = fronteira + elemento.getObjective(0) + " " + elemento.getObjective(1)+"\n";	
			}
		}
		Impressora.getInstance().imprimirArquivo((caminho_saida), fronteira);

	}

	private void limparTudo() {

	//	allpopNSGAII.clear();
		allpopNSGAIII.clear();
		//allpopMOMBI.clear(); 
		//popMOMBI.clear();
		pfTrueKnown.clear();



	}
}
