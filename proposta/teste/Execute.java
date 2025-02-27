package teste;

import static utilidades.SaveFiles.saveFunVar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.management.JMException;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;

import algoritmos_evolucionarios.IBEA_LLH_IntegerProblem;
import algoritmos_evolucionarios.NSGAIII_LLH_IntegerProblem;
import algoritmos_evolucionarios.SPEA2_LLH_IntegerProblem;
import algoritmos_evolucionarios.mombi.MOMBI2_LLH_IntegerProblem;
//import br.inpe.cocte.labac.hrise.main.HHLARILAMainReal;
//import br.inpe.cocte.labac.hrise.util.ProblemsWrapperLARILA;
import br.inpe.cocte.labac.hrise.util.SaveFiles;
//import br.inpe.cocte.labac.hrise.main.HRISEMainReal;
//import br.inpe.cocte.labac.hrise.qualityreal.QualityIndicatorsRealProblems;//mudar para o meu projeto
import dependencias_class.Saida;
import dependencias_class.SolutionListUtils;
import dependencias_hh.HHCFMainReal;
import dependencias_hh.HRISEMainReal;
import dependencias_hh.QualityIndicatorsRealProblems;
import dependencias_hh.util.ProblemsWrapperLARILA;
import dependencias_interfaces.IntegerSolution;
import dependencias_interfaces.Problem;
import problemas.Mestrado_Problem;
import utilidades.Impressora;
import utilidades.PFTrueKnown;
import utilidades.Populacoes;
import utilidades.SolucaoIndividual;
import utilidades.algoritmo;
import utilidades.auxiliar;

public class Execute {

	private static String base = "C:\\Users\\camil\\eclipse-workspace\\metaheuristic";
//	private static String cfgProp = base+"\\config.properties"; // graphviz
//	private static String TEMP_DIR = base+"\\temp"; //  graphviz
	// arquivos
//	private static String referenceParetoFront = "";
	private static String caminho_projeto = base + "\\";
	private static String weight_path = caminho_projeto + "geracao_weight_MOMBI\\weights\\output\\weight_03D_12.sld";

	private static int estudos_caso = 1;
	private static int maxTrials = 3;

//	private static int dimensao_problema = 3;
	private static int populationSize = 100;
//	private static int adjustMombi = 12;
	private static int operador_crossover = 1;
	private static int operador_mutacao = 1;
	private static double crossoverProbability = 0.9;
	private static double mutationProbability = 0.0125;
	private static int numberValidations = 20; //////// esse e o unico que muda;
	private static int numberArchievment = populationSize * numberValidations; //////// esse e o unico que muda;
	
	private static String name = "point";
	private static int passo = 4;
	
	public static String dot;

	public static void main(String[] args)
			throws IOException, InterruptedException, ConfigurationException, JMException, ClassNotFoundException {

		for (int ec = 1; ec <= estudos_caso; ec++) {

			Populacoes popsFinais = Populacoes.getInstance();
			PFTrueKnown pftrueknown = PFTrueKnown.getInstance();

			System.out.println("#Mestrado - Integer Problem...");
			// modelo
			dot = caminho_projeto + "dots\\" + name + "\\" + ec + ".dot";
			Problem<IntegerSolution> problem = new Mestrado_Problem(3,dot);
//			Problem<IntegerSolution> problem = new Camila_problema(caminho_projeto+"dots\\"+name+"\\"+ec+".dot");
//			int contagem_solucao = 0;
			System.out.println("#############################");
			System.out.println("INICIANDO");

//			
			SolucaoIndividual novo_MOMBI = new SolucaoIndividual();
			novo_MOMBI.setAlg(algoritmo.mombi);
			novo_MOMBI.setLlh("");
			novo_MOMBI.setTrial(maxTrials);
			for (int trial = 0; trial < maxTrials; trial++) {
				System.out.println("Algoritmo: MOMBI-II Contagem de execu��o: " + trial);
				MOMBI2_LLH_IntegerProblem llh = new MOMBI2_LLH_IntegerProblem(problem, crossoverProbability,
						mutationProbability, operador_crossover, operador_mutacao, numberValidations, weight_path);
				Saida popMOMBI = llh.execute();
				List<IntegerSolution> popMOMBIFinal = SolutionListUtils
						.getNondominatedSolutions(popMOMBI.getPopulacao_final());
				novo_MOMBI.addPop(popMOMBIFinal);
				pftrueknown.addPop(popMOMBI.getPopulacao_final());

				saveFunVar(trial, "MOMBI-II", problem, popMOMBI.getPopulacao_final(), ec, name, base, passo);
//				saveFunVar(trial, "MOMBI-II", problem, popMOMBI.getPopulacao_final());
				popMOMBI.getPopulacao_final().clear();
				auxiliar.getInstance().limpa();
			}
			popsFinais.addAllpopMOMBITodoMundoMesmo(novo_MOMBI);

			System.out.println("#############################");

			SolucaoIndividual novo_IBEA = new SolucaoIndividual();
			novo_IBEA.setAlg(algoritmo.nsgaii);
			novo_IBEA.setLlh("");
			novo_IBEA.setTrial(maxTrials);
			for (int trial = 0; trial < maxTrials; trial++) {
//				SolucaoIndividual novo = new SolucaoIndividual();
				System.out.println("Algoritmo: IBEA Contagem de execu��o: " + trial);
				IBEA_LLH_IntegerProblem llh = new IBEA_LLH_IntegerProblem(problem, populationSize, crossoverProbability,
						mutationProbability, operador_crossover, operador_mutacao, populationSize * numberValidations,
						numberArchievment);
				Saida popIBEA = llh.execute();
				List<IntegerSolution> popIBEAFinal = SolutionListUtils
						.getNondominatedSolutions(popIBEA.getPopulacao_final());
				novo_IBEA.addPop(popIBEAFinal);
				pftrueknown.addPop(popIBEA.getPopulacao_final());
				saveFunVar(trial, "IBEA", problem, popIBEA.getPopulacao_final(), ec, name, base,passo);
//				saveFunVar(trial, "IBEA", problem, popIBEA.getPopulacao_final());

				popIBEA.getPopulacao_final().clear();
				auxiliar.getInstance().limpa();
			}
			popsFinais.addAllpopIBEATodoMundoMesmo(novo_IBEA);

			System.out.println("#############################");
			SolucaoIndividual novo_NSGAIII = new SolucaoIndividual();
			novo_NSGAIII.setAlg(algoritmo.nsgaiii);
			novo_NSGAIII.setLlh("");
			novo_NSGAIII.setTrial(maxTrials);
			for (int trial = 0; trial < maxTrials; trial++) {
				System.out.println("Algoritmo: NSGA-III Contagem de execu��o: " + trial);
				NSGAIII_LLH_IntegerProblem llh3 = new NSGAIII_LLH_IntegerProblem(problem, populationSize,
						crossoverProbability, mutationProbability, operador_crossover, operador_mutacao,
						numberValidations);
				Saida popNSGAIII = llh3.execute();
				List<IntegerSolution> popNSGAIIIFinal = SolutionListUtils
						.getNondominatedSolutions(popNSGAIII.getPopulacao_final());
				novo_NSGAIII.addPop(popNSGAIIIFinal);
				pftrueknown.addPop(popNSGAIII.getPopulacao_final());
				saveFunVar(trial, "NSGA-III", problem, popNSGAIII.getPopulacao_final(), ec, name, base, passo);
//				saveFunVar(trial, "NSGA-III", problem, popNSGAIII.getPopulacao_final());

				popNSGAIII.getPopulacao_final().clear();
				auxiliar.getInstance().limpa();
			}
			popsFinais.addAllpopNSGAIIITodoMundoMesmo(novo_NSGAIII);

			System.out.println("#############################");
			SolucaoIndividual novo_SPEA2 = new SolucaoIndividual();
			novo_SPEA2.setAlg(algoritmo.spea2);
			novo_SPEA2.setLlh("");
			novo_SPEA2.setTrial(maxTrials);
			for (int trial = 0; trial < maxTrials; trial++) {
				System.out.println("Algoritmo: SPEA2 Contagem de execu��o: " + trial);
				SPEA2_LLH_IntegerProblem llh2 = new SPEA2_LLH_IntegerProblem(problem, populationSize,
						crossoverProbability, mutationProbability, operador_crossover, operador_mutacao,
						numberValidations);
				Saida popSPEA2 = llh2.execute();
				List<IntegerSolution> popSPEA2Final = SolutionListUtils
						.getNondominatedSolutions(popSPEA2.getPopulacao_final());
				novo_SPEA2.addPop(popSPEA2Final);
				pftrueknown.addPop(popSPEA2.getPopulacao_final());
				saveFunVar(trial, "SPEA-II", problem, popSPEA2.getPopulacao_final(), ec, name, base, passo);
//				saveFunVar(trial, "SPEA-II", problem, popSPEA2.getPopulacao_final());

				popSPEA2.getPopulacao_final().clear();
				auxiliar.getInstance().limpa();
			}
			popsFinais.addAllpopSPEA2TodoMundoMesmo(novo_SPEA2);
//			

// -------------------------------HH----------------------------------
			String baseDestDir = caminho_projeto + "Des\\Rst\\HRISE\\ResultsExec"; // The main Destination Dir. Change
//																					// it according to your platform.
//			
			SolucaoIndividual novo_HHCF = new SolucaoIndividual();
			novo_HHCF.setAlg(algoritmo.HHCF);
			novo_HHCF.setLlh("");
			novo_HHCF.setTrial(maxTrials);
//
			String[] argsHHCF = { "" + maxTrials, "inmehy", "HH-CF", "1000", "5", base, baseDestDir };
			HHCFMainReal HHCF = new HHCFMainReal(problem, argsHHCF , ec, name, base, passo);
//			HHCF.execute(novo_HHCF);
			
			System.out.println("------------");			

			SolucaoIndividual novo_HRISE_M = new SolucaoIndividual();
			novo_HRISE_M.setAlg(algoritmo.HRISEM);
			novo_HRISE_M.setLlh("");
			novo_HRISE_M.setTrial(maxTrials);

			String[] newArgsHRISEMAJ = { maxTrials + "", "inmehy", "HRISE_M", "1000", "7", "1.0", "2", base,
					baseDestDir };
			HRISEMainReal HRISE_M = new HRISEMainReal(problem, maxTrials, populationSize, crossoverProbability,
					mutationProbability, operador_crossover, operador_mutacao, numberValidations, newArgsHRISEMAJ, ec, name, base, passo);
			HRISE_M.execute(novo_HRISE_M);
//			popsFinais.addAllpopHRISE_MTodoMundoMesmo(novo_HRISE_M);
//			

			SolucaoIndividual novo_HRISE_R = new SolucaoIndividual();
			novo_HRISE_R.setAlg(algoritmo.HRISER);
			novo_HRISE_R.setLlh("");
			novo_HRISE_R.setTrial(maxTrials);

			String[] newArgsHRISERES = { maxTrials + "", "inmehy", "HRISE_R", "1000", "7", "0.1", "3", base,
					baseDestDir };
			HRISEMainReal HRISE_R = new HRISEMainReal(problem, maxTrials, populationSize, crossoverProbability,
					mutationProbability, operador_crossover, operador_mutacao, numberValidations, newArgsHRISERES, ec, name, base, passo);
			HRISE_R.execute(novo_HRISE_R);
//			popsFinais.addAllpopHRISE_RTodoMundoMesmo(novo_HRISE_R);

// -------------------------------pftrueknown ----------------------------------

			String pf = new String();
			for (IntegerSolution el : pftrueknown.getPftrueknow()) {
				if (!pf.contains(el.getObjective(0) + "\t" + el.getObjective(1) + "\t" + el.getObjective(2))) {
					pf = pf + el.getObjective(0) + "\t" + el.getObjective(1) + "\t" + el.getObjective(2) + "\n";
				}
			}

			Impressora.getInstance().imprimirArquivo(
					(caminho_projeto + "files\\" + name + "" + ec + "_"+passo+"\\TrueKnownParetoFront.tsv"), pf);

			pftrueknown.gerarParetoFront();
//------------------------------Soutions------------------------------

			String[] allAlgs = {"HRISE_M", "HRISE_R", "HH-CF"};
			QualityIndicatorsRealProblems qualIndReal = QualityIndicatorsRealProblems.getInstance();

			qualIndReal.calculateQualityIndicatorsReal(allAlgs, "inmehy", base, baseDestDir, problem, ec, name,passo);

			calculaIndicadores(popsFinais.getAllpopSPEA2TodoMundoMesmo(), ec, "SPEA-II", name, passo);
			calculaIndicadores(popsFinais.getAllpopMOMBITodoMundoMesmo(), ec, "MOMBI-II", name, passo);
			calculaIndicadores(popsFinais.getAllpopNSGAIIITodoMundoMesmo(), ec, "NSGA-III", name, passo);
			calculaIndicadores(popsFinais.getAllpopIBEATodoMundoMesmo(), ec, "IBEA", name, passo);

			System.out.println("Fim execu��o do estudo de caso: " + ec);
		}
		System.out.println("Fim do algoritmo!");
	}

	public static void calculaIndicadores(List<SolucaoIndividual> solucoes, int ec, String alg, String name,
			int passoInt) throws IOException {
		Impressora.getInstance()
				.verifyDiretorio(caminho_projeto + "files\\" + name + "" + ec + "_" + passoInt + "\\" + alg);

		for (SolucaoIndividual solucao : solucoes) {

			solucao.calcularIndicadores();
			String result_hyp = "";
			String result_igd = "";
			String result_eps = "";

			for (String elemento : solucao.getHypervolume()) {
				result_hyp = result_hyp + elemento + "\t";
			}
			for (String elemento : solucao.getIgdPlus()) {
				result_igd = result_igd + elemento + "\t";
			}
			for (String elemento : solucao.getEpsilon()) {
				result_eps = result_eps + elemento + "\t";
			}

			Impressora.getInstance().imprimirArquivo((caminho_projeto + "files\\" + name + "" + ec + "_" + passoInt
					+ "\\" + alg + "\\" + alg + "_hypervolume.tsv"), result_hyp);

			Impressora.getInstance().imprimirArquivo((caminho_projeto + "files\\" + name + "" + ec + "_" + passoInt
					+ "\\" + alg + "\\" + alg + "_idgplus.tsv"), result_igd);

			Impressora.getInstance().imprimirArquivo((caminho_projeto + "files\\" + name + "" + ec + "_" + passoInt
					+ "\\" + alg + "\\" + alg + "_epsilon.tsv"), result_eps);

		}
	}


}
