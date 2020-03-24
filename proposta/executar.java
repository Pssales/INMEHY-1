

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.jgrapht.alg.interfaces.CliqueAlgorithm;

import com.opencsv.CSVWriter;

import algoritmos_evolucionarios.IBEA_LLH_IntegerProblem;
import algoritmos_evolucionarios.MOMBI_LLH_IntegerProblem;
import algoritmos_evolucionarios.NSGAIII_LLH_IntegerProblem;
import algoritmos_evolucionarios.NSGAII_LLH_IntegerProblem;
import dependencias_class.ArrayFront;
import dependencias_class.Epsilon;
import dependencias_class.FrontNormalizer;
import dependencias_class.FrontUtils;
import dependencias_class.InvertedGenerationalDistancePlus;
import dependencias_class.PISAHypervolume;
import dependencias_class.PointSolution;
import dependencias_class.RankingAndCrowdingSelection;
import dependencias_class.Saida;
import dependencias_class.SolutionListUtils;
import dependencias_interfaces.Front;
import dependencias_interfaces.IntegerSolution;
import dependencias_interfaces.Problem;
import problemas.Camila_problema;
import utilidades.GraphViz;
import utilidades.Impressora;

public class executar {

	private static String cfgProp = "C:\\Users\\camil\\eclipse-workspace\\metaheuristic\\config.properties"; // graphviz
	private static String TEMP_DIR = "C:\\Users\\camil\\eclipse-workspace\\metaheuristic\\temp"; //  graphviz
	
	private static int estudos_caso = 1; // referente a pasta arquivos para leitura. são os dots. uma pasta por dot 
	private static int maxTrials = 30;

	//arquivos
	private static String referenceParetoFront = "";
	private static String caminho_projeto = "C:\\Users\\camil\\eclipse-workspace\\metaheuristic\\";
	
	
	
	
	// configuração dos algoritmos genéticos
	private static int populationSize = 1000;
	private static int operador_crossover = 1;
	private static int operador_mutacao = 1;
	private static double crossoverProbability = 0.9;
	private static double mutationProbability = 0.0125;
	private static int numberValidations = 2000;  //////// esse e o unico que muda;
	private static String weight_path = "C:\\Users\\camil\\eclipse-workspace\\jMetal-master\\resources\\weightVectorFiles\\mombi2\\weight_02D_100_mombi.sld";
	////////////////////////////////
	

	private static List<List<IntegerSolution>> allpopNSGAII = new ArrayList<List<IntegerSolution>>();
	private static List<List<IntegerSolution>> allpopNSGAIII = new ArrayList<List<IntegerSolution>>();
	private static List<List<IntegerSolution>> allpopMOMBI = new ArrayList<List<IntegerSolution>>();
	private static List<List<IntegerSolution>> allpopIBEA = new ArrayList<List<IntegerSolution>>();
	
	private static List<IntegerSolution> popNSGAFinal = new ArrayList();
	private static List<IntegerSolution> popNSGAIIIFinal = new ArrayList<>();
	private static List<IntegerSolution> popMOMBIFinal = new ArrayList<>();
	private static List<IntegerSolution> popIBEAFinal = new ArrayList<>();
	
	

	public static void main(String[] args) throws IOException{

		
		for(int ec=1; ec<=estudos_caso; ec++){
			
			String referenceParetoFront = "C:\\Users\\camil\\eclipse-workspace\\metaheuristic\\pareto_fronts\\fronteira_pareto_"+ec+".pf";
			Front referenceFront = new ArrayFront(referenceParetoFront); 
			FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);
			Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront); 
			
			System.out.println("#Integer Problem - teste de interface..." + ec);
			
			Problem<IntegerSolution> problem = new Camila_problema(caminho_projeto+"dots\\"+ec+".dot");

			//System.out.println("Rodando algoritmos nativos...");


			for(int trial = 0; trial < maxTrials; trial++){

				NSGAIII_LLH_IntegerProblem nsgaiii_nativo = new NSGAIII_LLH_IntegerProblem(problem, populationSize, crossoverProbability, mutationProbability, operador_crossover, operador_mutacao, numberValidations);
				try {
					Saida popNSGAIII_nativo = nsgaiii_nativo.execute();							   
					List<IntegerSolution> popnd = SolutionListUtils.getNondominatedSolutions(popNSGAIII_nativo.getPopulacao_final());
					allpopNSGAIII.add(popnd); // terah as 30 pops do NSGA-II
					nsgaiii_nativo.limparPopulacao();
					
				} catch (Exception eee) {
					eee.printStackTrace();
				}

			} // #### END 30 TRIAL NSGA-II

			System.out.println("NSGAIII Nativo finalizado");   
//			
			for(List<IntegerSolution> pop : allpopNSGAIII){  
				popNSGAIIIFinal.addAll(pop);
			}
			//allpopIBEA.clear();
			//allpopIBEA.add(popIBEAFinal);
			Front normalizedFrontNSGAIII = frontNormalizer.normalize(new ArrayFront(popNSGAIIIFinal)) ;
			List<PointSolution> normalizedPopulationNSGAIII = FrontUtils
					.convertFrontToSolutionList(normalizedFrontNSGAIII) ;


			String result_hyp_nsgaiii = new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluateModificado(normalizedPopulationNSGAIII) + "";
			String result_eps_nsgaiii = new Epsilon<PointSolution>(normalizedReferenceFront).evaluateModificado(popNSGAIIIFinal) + " ";
			String result_igd_nsgaiii = new InvertedGenerationalDistancePlus<PointSolution>(normalizedReferenceFront).evaluateModificado(normalizedPopulationNSGAIII) + "";

			List<String[]> linhas = new ArrayList<>();
			linhas.add(new String[]{(ec+""), "NSGA-III", result_hyp_nsgaiii, result_eps_nsgaiii, result_igd_nsgaiii});

			Writer writer = Files.newBufferedWriter(Paths.get("C:\\Users\\camil\\eclipse-workspace\\metaheuristic\\files\\resultados.csv"), StandardOpenOption.APPEND);
			CSVWriter csvWriter = new CSVWriter(writer, '\t'); 

			//csvWriter.writeNext(cabecalho);
			csvWriter.writeAll(linhas);
//
			csvWriter.flush();
			writer.close();
			System.out.println("NSGA-III: "+result_hyp_nsgaiii);

			
			System.out.println("-----------------");
		
		}
		System.out.println("Fim");

	}

	public static List<IntegerSolution> adjustPopulation(List<IntegerSolution> pop, int popSize) {
		List<IntegerSolution> jointPopulation = new ArrayList<>();
		jointPopulation.addAll(pop);
		RankingAndCrowdingSelection<IntegerSolution> rankingAndCrowdingSelection ;
		rankingAndCrowdingSelection = new RankingAndCrowdingSelection<IntegerSolution>(popSize) ;

		return rankingAndCrowdingSelection.execute(jointPopulation) ;
	}

	public static void calculateHyperVolume(List<List<IntegerSolution>> populationGeneral, String referenceParetoFront, String caminho_saida, String solution) throws IOException{

		Front referenceFront = new ArrayFront(referenceParetoFront);
		FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);
		Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront); 


		for(List<IntegerSolution> pop : populationGeneral){  


			String str = "";
			Front normalizedFront = frontNormalizer.normalize(new ArrayFront(pop)) ;
			List<PointSolution> normalizedPopulation = FrontUtils
					.convertFrontToSolutionList(normalizedFront) ;
			str += "\tSolution: " + solution;
			str += "\t \t Hypervolume (N) : " + new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluateModificado(normalizedPopulation) + " \n";
			str += "\t \t Epsilon (N) : " + new Epsilon<PointSolution>(normalizedReferenceFront).evaluateModificado(pop) + " \n";
			//str += "\t \t IGD+ (N) : " + new InvertedGenerationalDistancePlus<S>(normalizedReferenceFront).evaluateModificado(normalizedPopulation) + " \n";

			System.out.println(str);


			Impressora.getInstance().imprimirArquivo((caminho_saida), str);
		}
	}

	public static int getRandomSolution(int high){
		Random r = new Random();
		int low = 0;
		int result = r.nextInt(high-low) + low;
		return result;
	}

	public static void createDotGraph(String dotFormat,String fileName)
	{
		GraphViz gv = new GraphViz(cfgProp, TEMP_DIR);
		gv.addln(gv.start_graph());
		gv.add(dotFormat);
		gv.addln(gv.end_graph());
		String type = "pdf";
		gv.decreaseDpi();
		gv.decreaseDpi();
		File out = new File(fileName+"."+ type); 
		gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
	}


	public static void limparTudo(){


		allpopNSGAII.clear();
		allpopNSGAIII.clear();
		allpopMOMBI.clear(); 
		popNSGAFinal.clear();
		popNSGAIIIFinal.clear();
		popMOMBIFinal.clear();
		popIBEAFinal.clear();
		
	}
}









