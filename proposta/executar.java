import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.opencsv.CSVWriter;

import algoritmos_evolucionarios.IBEA_LLH_IntegerProblem;
import algoritmos_evolucionarios.MOMBI_LLH_IntegerProblem;
import algoritmos_evolucionarios.NSGAIII_LLH_IntegerProblem;
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
	
	private static int estudos_caso =2; // referente a pasta arquivos para leitura. são os dots. uma pasta por dot 
	private static int maxTrials = 30;

	//arquivos
	private static String referenceParetoFront = "";
	private static String caminho_projeto = "C:\\Users\\camil\\eclipse-workspace\\metaheuristic\\";
	
	// configuração dos algoritmos genéticos
	private static int populationSize = 100;
	private static int operador_crossover = 1;
	private static int operador_mutacao = 1;
	private static double crossoverProbability = 0.9;
	private static double mutationProbability = 0.0125;
	private static int numberValidations = 1000;  //////// esse e o unico que muda;
	private static int numberArchievment = 100;
	private static String weight_path = "C:\\Users\\camil\\eclipse-workspace\\jMetal\\resources\\weightVectorFiles\\mombi2\\weight_03D_12.sld";
	////////////////////////////////
		
	private static List<List<IntegerSolution>> allpopNSGAIII = new ArrayList<List<IntegerSolution>>();
	private static List<List<IntegerSolution>> allpopMOMBI = new ArrayList<List<IntegerSolution>>();
	private static List<List<IntegerSolution>> allpopIBEA = new ArrayList<List<IntegerSolution>>();
	private static List<IntegerSolution> pfTrueKnown = new ArrayList<IntegerSolution>();
	
	private static List<IntegerSolution> popNSGAIIIFinal = new ArrayList<>();
	private static List<IntegerSolution> popMOMBIFinal = new ArrayList<>();
	private static List<IntegerSolution> popIBEAFinal = new ArrayList<>();
	
	

	public static void main(String[] args) throws IOException{

		
		for(int ec=2; ec<=estudos_caso; ec++){
			
			String referenceParetoFront = "C:\\Users\\camil\\eclipse-workspace\\metaheuristic\\pareto_fronts\\fronteira_pareto_"+ec+".pf";
			Front referenceFront = new ArrayFront(referenceParetoFront); 
			FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);
			Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront); 
			
			System.out.println("#Integer Problem - teste..." + ec);
			
			Problem<IntegerSolution> problem = new Camila_problema(caminho_projeto+"dots\\"+ec+".dot");

			System.out.println("Rodando algoritmos nativos...");


			for(int trial = 0; trial < maxTrials; trial++){

				NSGAIII_LLH_IntegerProblem nsgaiii_nativo = new NSGAIII_LLH_IntegerProblem(problem, populationSize, crossoverProbability, mutationProbability, operador_crossover, operador_mutacao, numberValidations);
				try {
					Saida popNSGAIII_nativo = nsgaiii_nativo.execute();							   
					List<IntegerSolution> popnd = SolutionListUtils.getNondominatedSolutions(popNSGAIII_nativo.getPopulacao_final());
					allpopNSGAIII.add(popnd); // terah as 30 pops do NSGA-III
					nsgaiii_nativo.limparPopulacao();
					
				} catch (Exception eee) {
					eee.printStackTrace();
				}

			} // #### END 30 TRIAL NSGA-II

			System.out.println("NSGAIII Nativo finalizado");   
			
//			----------------------------------------------------------
			for(int trial = 0; trial < maxTrials; trial++){

				IBEA_LLH_IntegerProblem ibea_nativo = new IBEA_LLH_IntegerProblem(problem, populationSize, crossoverProbability, mutationProbability, operador_crossover, operador_mutacao, numberValidations*populationSize, numberArchievment);
				try {
					Saida popIBEA_nativo = ibea_nativo.execute();							   
					List<IntegerSolution> popnd = SolutionListUtils.getNondominatedSolutions(popIBEA_nativo.getPopulacao_final());
					allpopIBEA.add(popnd); // terah as 30 pops do NSGA-II
					pfTrueKnown.addAll(popnd); // para gerar a PFTrueKnown 
				} catch (Exception eee) {
					eee.printStackTrace();
				}

			} // #### END 30 TRIAL NSGA-II

			System.out.println("IBEA Nativo finalizado");  
			
//			-----------------------------------------------------------
			
			for(int trial = 0; trial < maxTrials; trial++){
				MOMBI_LLH_IntegerProblem mombi_nativo = new MOMBI_LLH_IntegerProblem(problem, crossoverProbability, mutationProbability, operador_crossover, operador_mutacao, numberValidations, weight_path);
				try {
					Saida popMOMBI_nativo = mombi_nativo.execute();		   
					List<IntegerSolution> popnd= SolutionListUtils.getNondominatedSolutions(popMOMBI_nativo.getPopulacao_final());
					allpopMOMBI.add(popnd); // terah as 30 pops do NSGA-II
					pfTrueKnown.addAll(popnd); // para gerar a PFTrueKnown   
				} catch (Exception eee) {
					eee.printStackTrace();
				}
			} // #### END 30 TRIAL NSGA-II
			
			System.out.println("MOMBI Nativo finalizado");    
			
//			-----------------------------------------------------------
//			Indicadores
//			-----------------------------------------------------------

			for(List<IntegerSolution> pop : allpopIBEA){  
				popIBEAFinal.addAll(pop);
			}

			Front normalizedFrontIBEA = frontNormalizer.normalize(new ArrayFront(popIBEAFinal)) ;
			List<PointSolution> normalizedPopulationIBEA = FrontUtils
					.convertFrontToSolutionList(normalizedFrontIBEA) ;
			
//			-----------------------------------------------------------			
			
			for(List<IntegerSolution> pop : allpopMOMBI){  
				popMOMBIFinal.addAll(pop);
			}
			Front normalizedFrontMOMBI = frontNormalizer.normalize(new ArrayFront(popMOMBIFinal)) ;
			List<PointSolution> normalizedPopulationMOMBI = FrontUtils
					.convertFrontToSolutionList(normalizedFrontMOMBI) ;
//			-----------------------------------------------------------
			
			for(List<IntegerSolution> pop : allpopNSGAIII){  
				popNSGAIIIFinal.addAll(pop);
			}
			Front normalizedFrontNSGAIII = frontNormalizer.normalize(new ArrayFront(popNSGAIIIFinal)) ;
			List<PointSolution> normalizedPopulationNSGAIII = FrontUtils
					.convertFrontToSolutionList(normalizedFrontNSGAIII) ;


			String result_hyp_nsgaiii = new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluateModificado(normalizedPopulationNSGAIII) + "";
			String result_eps_nsgaiii = new Epsilon<PointSolution>(referenceFront).evaluateModificado(popNSGAIIIFinal) + " ";
			String result_igd_nsgaiii = new InvertedGenerationalDistancePlus<PointSolution>(normalizedReferenceFront).evaluateModificado(normalizedPopulationNSGAIII) + "";


			String result_hyp_ibea = new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluateModificado(normalizedPopulationIBEA) + "";
			String result_eps_ibea = new Epsilon<PointSolution>(referenceFront).evaluateModificado(popIBEAFinal) + " ";
			String result_igd_ibea = new InvertedGenerationalDistancePlus<PointSolution>(normalizedReferenceFront).evaluateModificado(normalizedPopulationIBEA) + "";


			String result_hyp_mombi = new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluateModificado(normalizedPopulationMOMBI) + "";
			String result_eps_mombi = new Epsilon<PointSolution>(referenceFront).evaluateModificado(popMOMBIFinal) + " ";
			String result_igd_mombi = new InvertedGenerationalDistancePlus<PointSolution>(normalizedReferenceFront).evaluateModificado(normalizedPopulationMOMBI) + "";

			
			List<String[]> linhas = new ArrayList<>();
			linhas.add(new String[]{(ec+""), "NSGA-III", result_hyp_nsgaiii, result_eps_nsgaiii, result_igd_nsgaiii});
			linhas.add(new String[]{(ec+""), "IBEA", result_hyp_ibea, result_eps_ibea, result_igd_ibea});
			linhas.add(new String[]{(ec+""), "MOMBI-II", result_hyp_mombi, result_eps_mombi, result_igd_mombi});

			Writer writer = Files.newBufferedWriter(Paths.get("C:\\Users\\camil\\eclipse-workspace\\metaheuristic\\files\\resultados.csv"), StandardOpenOption.APPEND);
			CSVWriter csvWriter = new CSVWriter(writer, '\t'); 

			csvWriter.writeAll(linhas);

			csvWriter.flush();
			writer.close();
			System.out.println("NSGA-III hyp: "+result_hyp_nsgaiii);
			System.out.println("NSGA-III eps: "+result_eps_nsgaiii);
			System.out.println("NSGA-III igd+: "+result_igd_nsgaiii);
			

			System.out.println("IBEA hyp: "+result_hyp_ibea);
			System.out.println("IBEA eps: "+result_eps_ibea);
			System.out.println("IBEA igd+: "+result_igd_ibea);
			
			System.out.println("MOMBI hyp: "+result_hyp_mombi);
			System.out.println("MOMBI eps: "+result_eps_mombi);
			System.out.println("MOMBI igd+: "+result_igd_mombi);
			
			System.out.println("------------------------------------------");
		
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
//			str += "\t \t IGD+ (N) : " + new InvertedGenerationalDistancePlus<S>(normalizedReferenceFront).evaluateModificado(normalizedPopulation) + " \n";

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
		allpopNSGAIII.clear();
		allpopMOMBI.clear(); 
		allpopIBEA.clear();
		
		popNSGAIIIFinal.clear();
		popMOMBIFinal.clear();
		popIBEAFinal.clear();
		
	}
}









