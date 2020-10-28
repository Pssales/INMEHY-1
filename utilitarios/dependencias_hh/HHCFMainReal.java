package dependencias_hh;


import dependencias_hh.CreateAlgorithms;
import dependencias_hh.jhelper.util.ProblemFactory;
import dependencias_hh.jhelper.util.metrics.AlgorithmEffortCalculator;
import dependencias_hh.jhelper.util.metrics.EpsilonCalculator;
import dependencias_hh.jhelper.util.metrics.HypervolumeCalculator;
import dependencias_hh.jhelper.util.metrics.IgdCalculator;
import dependencias_hh.jhelper.util.metrics.IgdPlusCalculator;
import dependencias_hh.jhelper.util.metrics.RniCalculator;
import dependencias_hh.jhelper.util.metrics.SpreadCalculator;
import dependencias_hh.jhelper.util.metrics.UDMetricHandler;
import dependencias_hh.jhelper.util.metrics.UDMetricHandler;

import  dependencias_hh.otherhhs.choiceFunction;
import  dependencias_hh.qualityreal.QualityIndicatorsRealProblems;

import  dependencias_hh.util.PopulationHandler;
import  dependencias_hh.util.ProblemsWrapper;
import  dependencias_hh.util.QualityIndicatorsHandler;
import  dependencias_hh.util.SaveFiles;
import  dependencias_hh.util.StatEvalSupport;
import dependencias_interfaces.Algorithm;
import dependencias_interfaces.CrossoverOperator;
import dependencias_interfaces.IntegerSolution;
import dependencias_interfaces.MutationOperator;
import dependencias_interfaces.Problem;
import dependencias_interfaces.SelectionOperator;
import dependencias_interfaces.Solution;
import problemas.Mestrado_Problem;
import utilidades.PFTrueKnown;
import utilidades.SolucaoIndividual;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.JMException;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
//import org.uma.jmetal.algorithm.Algorithm;
//import org.uma.jmetal.problem.Problem;
//import org.uma.jmetal.solution.Solution;

import algoritmos_evolucionarios.IBEA;
import dependencias_class.Saida;
import dependencias_class.SolutionListUtils;


/**
 * This is the class which implements the HH-CF (Choice Function) algorithm as described in [1] and adapted by the authors in [2]. The 
 * heuristic selection method is based on a two-stage ranking scheme and four quality indicators: Algorithm Effort, Ratio of Nondominated
 * Individuals (RNI), hypervolume, and Uniform Distribution. This implementation is basically the same as accomplished by authors in [2] 
 * with a few modifications to embedded it within the HRISE framework.
 *
 *[1] M. Maashi, E. Ozcan, G. Kendall, A multi-objective hyper-heuristic based on choice function, Expert Systems with Applications 41 (9)
 * (2014) 4475 - 4493. doi:https://doi.org/10.1016/j.eswa.2013.12.050. URL http://www.sciencedirect.com/science/article/pii/S095741741400013X
 *
 *[2] W. Li, E. Ozcan, R. John, A learning automata-based multiobjective hyper-heuristic, IEEE Transactions on Evolutionary Computation 23 (1)
 * (2019) 59 - 73. doi:10.1109/TEVC.2017.2785346.
 *
 * @author Valdivino Alexandre de Santiago J�nior
 */
public class HHCFMainReal<S extends Solution<?>> {


	private Problem<IntegerSolution> problem;
	private HHCFMainReal<IntegerSolution> algorithm;
	private CrossoverOperator<IntegerSolution> crossover;
	private MutationOperator<IntegerSolution> mutation;
	private SelectionOperator<List<IntegerSolution>, IntegerSolution> selection;



	private int populationSize = 0;
	private int operador_crossover = 0;
	private int operador_mutacao = 0;
	private Double crossoverProbability = 0.0;
	private Double mutationProbability = 0.0;
	private int maxEvaluations = 0; 
	private int maxTrails = 0;

	
	private String problemString, versionHH, opt;
	private int m, k, l;
	private int trials; // Number of trials
	private int decisionPoints; // Number of decision points
	private int maxIterations; // Total number of iterations
	private String[] algName = {"NSGAII", "IBEA", "SPEA2"}; // LLHs
	private List<S> pfKnown = new ArrayList<>(); // Known PF
	private List<S> popAllH = new ArrayList<>(); // Auxiliary Population
	private List<S> popFinalHH = new ArrayList<>(); // Final Population
	private boolean first; // Initialisation?
	private String srcDir = ""; // Base Source Dir
	private String destDir = ""; // Base Dest Dir
	private SolucaoIndividual novo_HHCF;




	public HHCFMainReal(Problem<IntegerSolution> problem, int maxTrials,  int _populationSize, Double _crossoverProbability, Double _mutationProbability, int _operador_crossover, int _operador_mutacao, int _numberValidations, String[] args) {		

		trials = Integer.parseInt(args[0]);
		opt = args[1];
		versionHH = args[2];
		maxIterations = _numberValidations;
		decisionPoints = Integer.parseInt(args[4]);//mudar valdvino 25
		srcDir = args[5];
		destDir = args[6];

		ProblemsWrapper prw = new ProblemsWrapper(opt);
		l = prw.getL(); // Decision/DISTANCE variables. 
		m = prw.getM(); //  Number of objectives. 
		problemString = prw.getProblemString();
		

		this.problem = problem;
		populationSize = _populationSize;
	    operador_crossover = _operador_crossover;
	    operador_mutacao = _operador_mutacao;
	    crossoverProbability = _crossoverProbability;
		mutationProbability = _mutationProbability;
		maxEvaluations = _numberValidations;
	}



	
	
	
	
	public void execute(SolucaoIndividual novo_HHCF) throws IOException, ConfigurationException, JMException { 
	/*
	 * Handling dirs
	 */
//	FileUtils.deleteDirectory(new File("result"));
//	FileUtils.deleteDirectory(new File("exec"));
//	FileUtils.deleteDirectory(new File("pareto_fronts_known"));

	Logger.getLogger(HHCFMainReal.class.getName()).log(Level.INFO, "#### HH-CF, Discrete Optimisation and Real Problems!"); 
	k = 2 * (m - 1); // POSITION VARIABLES

	List<List<Double>> qualityIndicators = new ArrayList<List<Double>>(); // All quality indicators
	List<List<S>> popsInit = new ArrayList<List<S>>();
	StatEvalSupport stindic = StatEvalSupport.getInstance(); // Record and save the indicators for the statistical evaluation
	
	String filepfKnown;
	String hhid = " ";
	
	QualityIndicatorsRealProblems qireal = QualityIndicatorsRealProblems.getInstance(); 
	
	PFTrueKnown pftrueknown = PFTrueKnown.getInstance();

	

	for (int i = 0; i < trials; i++) { // ******* BEGIN -> TRIALS *******
		hhid = versionHH + "." + problem.getName() + "." + "t_" + i;	

		filepfKnown = " ";	
		first = true;	
		PopulationHandler popHandler = new PopulationHandler();
		int popToCreate = 0;
		int currIterationsLLH;

		/*
		 *  Time
		 */
		
		long[] elapsedTime = new long[algName.length];
		long[] lastInvokedTime = new long[algName.length];
		long[] executionTimeArray = new long[algName.length];
		for(int itim = 0; itim <algName.length; itim++){
			elapsedTime[itim] = 0;					
		}

		/*
		 * Choice Function - Initialisation: BEGIN
		 */
		int cntOnlyIni = 0;
		currIterationsLLH = maxIterations/decisionPoints;
		if ((first) && versionHH.equals("HH-CF")) {
			System.out.println("#### HH-CF: Initialisation -- LLHs: " + algName[0] + " // " + algName[1] + " // " + algName[2]); 
			for (int cllh = 0; cllh < algName.length; cllh++) { // algName: [0] = NSGAII; [1] = IBEA; [2] = SPEA2
				CreateAlgorithms crin = new CreateAlgorithms();
				popToCreate = populationSize;
				lastInvokedTime[cllh] = System.currentTimeMillis();
				
				Algorithm algin =  crin.createAlg(algName[cllh], popToCreate, currIterationsLLH, problem, first, popAllH); 
				long startTimein = System.currentTimeMillis(); // Record the execution time for each LLH
				algin.run();
				long executionTimein = System.currentTimeMillis() -  startTimein;
				executionTimeArray[cllh] = executionTimein;
				List resultin = (List) algin.getResult();
				
				List<S> popHin = resultin;
				
				popsInit.add(popHin);
				pfKnown.addAll(popHin);
				int um = 0;
				if(um==0) {
					for(IntegerSolution pf: pftrueknown.getPftrueknow()) {
						pfKnown.add((S) pf);
					}				
				}
	              um = 1;
				
			    Saida saida = new Saida(resultin, 0.0, 0.0); 

			    List<IntegerSolution> popHHCFFinal= SolutionListUtils.getNondominatedSolutions(saida.getPopulacao_final());
			    novo_HHCF.addPop(popHHCFFinal);					
				pftrueknown.addPop(saida.getPopulacao_final());//add pftrueknow
			
				
				if (cntOnlyIni > 0) {
					pfKnown = popHandler.generateNonDominated(pfKnown, problem);
				}
				
				pfKnown = popHandler.removeRepeatedSolutionsInteger(pfKnown);
								
				filepfKnown = "pareto_fronts_known/"+
						versionHH+"."+problem.getName()+"."+problem.getNumberOfObjectives()+"D.t_"+i+".dp_"+cntOnlyIni+
						".pf";
				
				SaveFiles sPFK = new SaveFiles();
				sPFK.savePFKnown(filepfKnown, pfKnown);
				cntOnlyIni++;
				qualityIndicators.add(HHCFMainReal.printResultsAsList(popHin, problem, false, popToCreate, executionTimeArray[cllh], filepfKnown));
				
			}
		}

		for(int cllh = 0; cllh < algName.length; cllh++){
			elapsedTime[cllh] = System.currentTimeMillis() - lastInvokedTime[cllh];
		}

		/*
		 * Quality indicators stored in matrices
		 */
		double[][] epsilonValues = new double[algName.length][2];
		double[][] igdplusValues = new double[algName.length][2];
		double[][] hypervolumeValues = new double[algName.length][2];		 
//		double[][] uniDistValues = new double[algName.length][2];

		QualityIndicatorsHandler qualIndMatrices = new  QualityIndicatorsHandler();
		List<Double> allHypervolumes = new ArrayList<Double>();
		allHypervolumes = qualIndMatrices.getAllIndicatorsCF(qualityIndicators, "HYPERVOLUME");
		
		List<Double> allEp = new ArrayList<Double>();
		allEp = qualIndMatrices.getAllIndicatorsCF(qualityIndicators, "EPSILON");
		
		List<Double> allIGDPLUS = new ArrayList<Double>();
		allIGDPLUS = qualIndMatrices.getAllIndicatorsCF(qualityIndicators, "IGDPLUS");
		

		for (int qind = 0; qind < qualityIndicators.size(); qind++){ //algName: [0] = NSGAII; [1] = IBEA; [2] = SPEA2
			hypervolumeValues[qind][0] = qind;
			hypervolumeValues[qind][1] = allHypervolumes.get(qind);
			
			epsilonValues[qind][0] = qind;
			epsilonValues[qind][1] = allEp.get(qind);
			
			igdplusValues[qind][0] = qind;
			igdplusValues[qind][1] = allIGDPLUS.get(qind);
			
		}		 

		int outcomeLLHCF = -1;
		choiceFunction chFun = new choiceFunction();
		int numberOfMeasures = 3;
		int alpha = 100;

//		System.out.println(""+ "-------");
//		Scanner a = new Scanner(System.in);
//		int v = a.nextInt();
		
		qualIndMatrices.clearAllIndicatorsCF();
		outcomeLLHCF = chFun.getMaxChoiceFunction(epsilonValues, 0, igdplusValues, 0, hypervolumeValues, 1,  numberOfMeasures, alpha, elapsedTime);
		
		
		
		lastInvokedTime[outcomeLLHCF] = System.currentTimeMillis(); // Start time of the chosen LLH
		String selectedLLH = null;
		selectedLLH = stringSelectedLLH(outcomeLLHCF);
		/*
		 * Choice Function - Initialisation: END
		 */

		for (int j = 3; j < decisionPoints; j++) { // ******* BEGIN -> DECISION POINTS ******* -> j = 3 -> minus 3 iterations due to the initialisation 
			first = false;
			CreateAlgorithms cr = new CreateAlgorithms();
			if (j == 3) { // Get the population of the selected LLH as a result of the initialisation
				popAllH.addAll(popsInit.get(outcomeLLHCF));
			}        	         
			while (popAllH.size() < populationSize) {
				popAllH.add(popAllH.get(popHandler.getRandomSolution(popAllH.size())));
			}	
			popToCreate = popAllH.size();
			Algorithm alg = cr.createAlg(selectedLLH, popToCreate, currIterationsLLH, problem, first, popAllH); 
			long beginTime = System.currentTimeMillis();
			alg.run();
			executionTimeArray[outcomeLLHCF] = System.currentTimeMillis() - beginTime;
			/*
			 * Update elapsed time since the LLH was last called.
			 */
			for(int itimst = 0; itimst < algName.length; itimst++){
				elapsedTime[itimst] = System.currentTimeMillis() - lastInvokedTime[itimst];
			}
			List result = (List) alg.getResult();
			List<S> popH = result;
			popAllH.clear(); 
			popAllH.addAll(popH); // All Moves

			/*
			 * Calculate Known PF
			 */
			pfKnown.addAll(popH);
			
			Saida resultsaida = new Saida(result, 0.0, 0.0); 

			   
			List<IntegerSolution> popHHCFFinal= SolutionListUtils.getNondominatedSolutions(resultsaida.getPopulacao_final());
			
			novo_HHCF.addPop(popHHCFFinal);					
			
			pftrueknown.addPop(resultsaida.getPopulacao_final());

			pfKnown = popHandler.generateNonDominated(pfKnown, problem);
			pfKnown = popHandler.removeRepeatedSolutionsInteger(pfKnown);
			filepfKnown = "pareto_fronts_known/"+
					versionHH+"."+problem.getName()+"."+problem.getNumberOfObjectives()+"D.t_"+i+".dp_"+j+
					".pf";
			SaveFiles sPFKSt = new SaveFiles();
			sPFKSt.savePFKnown(filepfKnown, pfKnown);
			qualityIndicators.clear();
			qualityIndicators.add(HHCFMainReal.printResultsAsList(popAllH, problem, false, popToCreate, executionTimeArray[outcomeLLHCF], filepfKnown));

			Logger.getLogger(HHCFMainReal.class.getName()).log(Level.INFO, "#### HH-CF  -- TRIAL: " + (i+1) + "  --  DECISION POINT: " + (j+1) +
					"  --  PROBLEM INSTANCE: " + problem.getName());
			Logger.getLogger(HHCFMainReal.class.getName()).log(Level.INFO, "## Selected LLH: " + alg.getName() + " -- " + selectedLLH + 
					" -- Integer Id: " + outcomeLLHCF);

			/*
			 * Update matrices of indicators: clear lists and get a single value
			 */
			allEp.clear();
			allEp = qualIndMatrices.getAllIndicatorsCF(qualityIndicators, "EPSILON");

			allIGDPLUS.clear();
			allIGDPLUS = qualIndMatrices.getAllIndicatorsCF(qualityIndicators, "IGDPLUS");

			allHypervolumes.clear();
			allHypervolumes = qualIndMatrices.getAllIndicatorsCF(qualityIndicators, "HYPERVOLUME");

			/*
			 * Update matrices of indicators: update itself 
			 */
			
			epsilonValues[outcomeLLHCF][1] = allEp.get(0); // Only 1 value

			igdplusValues[outcomeLLHCF][1] = allIGDPLUS.get(0); // Only 1 value


			hypervolumeValues[outcomeLLHCF][1] = allHypervolumes.get(0); // Only 1 value
			qualIndMatrices.clearAllIndicatorsCF();
			outcomeLLHCF = chFun.getMaxChoiceFunction(epsilonValues, 0, igdplusValues, 0, hypervolumeValues, 1, numberOfMeasures, alpha, elapsedTime);

			/*
			 * Start time of the chosen LLH
			 */
			lastInvokedTime[outcomeLLHCF] = System.currentTimeMillis();
			selectedLLH = stringSelectedLLH(outcomeLLHCF);
		} // ******* END -> DECISION POINTS *******

		popFinalHH.clear();
		popFinalHH.addAll(popAllH);
		int popSizeFinal = popFinalHH.size();
		popFinalHH = popHandler.generateNonDominated(popFinalHH, problem);

		  SaveFiles sFV = new SaveFiles();
		  sFV.saveFunVar(i, versionHH, problem, popFinalHH);
		qireal.addPFKnown(hhid, popFinalHH);
		HHCFMainReal.printResultsAsList(popFinalHH, problem, true, popSizeFinal, 0, filepfKnown);



		first = true;

		popAllH.clear();

		popFinalHH.clear();

		qualityIndicators.clear();

		pfKnown.clear();

		filepfKnown = " ";

		popsInit.clear(); // FD

		System.out.println("@@@@@@@ END OF TRIAL: " + (i+1) + " @@@@@@@");
	} // ******* END -> TRIALS *******

	System.out.println("........... Copying Results.......................................................................");
	File diretorio = new File(srcDir + "/result/"+ destDir + "/ALLRES/" + problem.getName() + "_" + problem.getNumberOfObjectives());
	if (!diretorio.exists()) {
		System.out.println(diretorio.getAbsolutePath());
		diretorio.mkdirs();
	}
	copyResultDirectory(srcDir + "/result",
			destDir + "/ALLRES/" + problem.getName() + "_" + problem.getNumberOfObjectives());
	copyResultDirectory(srcDir + "/pareto_fronts_known",
			destDir + "/PFKNOWN/HH-CF/");

	stindic.clearAllIndicatorsStatEval();
	} // ******* END OF MAIN PROGRAM *******


	public static void copyResultDirectory(String sourceDir, String destDir) {
		File source = new File(sourceDir);
		File dest = new File(destDir);
		try {
			FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static String printResults(List archive, Problem problem, int popSize,
			long timeEffort, String pf) throws FileNotFoundException, IOException {

		String strToReturn = "";

		// HV: Convergence-Diversity
		PopulationHandler popHndList = new PopulationHandler();
		
		HypervolumeCalculator hyp = new HypervolumeCalculator(problem.getNumberOfObjectives(), pf);
		double hypValue = hyp.execute(archive);

		// IGD: Convergence-Diversity
		IgdCalculator igd = new IgdCalculator(problem.getNumberOfObjectives(), pf);
		double igdValue = igd.execute(archive);

		// Epsilon: Convergence
		EpsilonCalculator eps=new EpsilonCalculator(problem.getNumberOfObjectives(), pf);
		double epsilonValue=eps.execute(archive);

		// GSPREAD/SPREAD: Diversity
		

		strToReturn="Hyper: " + hypValue + " ## " + "IGD: " + igdValue + " ## " + "Epsilon: " + epsilonValue + " ## ";

		return strToReturn;
	}


	public static List<Double> printResultsAsList(List archive, Problem problem, boolean recordStatEval, int popSize,
			long timeEffort, String pf) throws FileNotFoundException, IOException {

		List<Double> qual = new ArrayList<Double>();
		StatEvalSupport stindicators = StatEvalSupport.getInstance();

		// HV: Convergence-Diversity
		PopulationHandler popHnd = new PopulationHandler();
		
		HypervolumeCalculator hyp = new HypervolumeCalculator(problem.getNumberOfObjectives(), pf);
		double hypValue = hyp.execute(archive);


		// Epsilon: Convergence
		EpsilonCalculator eps=new EpsilonCalculator(problem.getNumberOfObjectives(), pf);
		double epsilonValue=eps.execute(archive);

		// Uniform Dist
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


	private static String stringSelectedLLH(int res) {
		String resSt = null;
		switch(res) {
		case 0 :
			resSt = "NSGAII";
			break;
		case 1 :
			resSt = "IBEA";
			break;  
		case 2 :
			resSt = "SPEA2";
			break;    
		default:
			System.out.println("Wrong LLH!");
		}

		return resSt;	
	}

}
