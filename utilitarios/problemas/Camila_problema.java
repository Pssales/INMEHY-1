package problemas;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.uma.jmetal.solution.integersolution.impl.DefaultIntegerSolution;

import dependencias_abstract.AbstractIntegerProblem;
import dependencias_interfaces.IntegerSolution;

import java.awt.font.NumericShaper;
import java.io.*;

/**
 * Class representing a single-objective TSP (Traveling Salesman Problem) problem.
 * It accepts data files from TSPLIB:
 *   http://www.iwr.uni-heidelberg.de/groups/comopt/software/TSPLIB95/tsp/
 *
 */
@SuppressWarnings("serial")
public class Camila_problema extends AbstractIntegerProblem {

	private int numero_nos ;
	private int tamanho_cromossomo = 1000;
	private int [][] matriz_adjacencia ;
	private Dicionario dicionario; 
	private int numero_arestas=0;



	public Camila_problema(String caminho_dot) throws IOException {



		dicionario = new Dicionario();
		int nos = 0;

		// primeira parte é fazer o dicionario
		try {
			//			stripDuplicatesFromFile(caminho_dot);
			FileReader arq = new FileReader(caminho_dot);
			BufferedReader lerArq = new BufferedReader(arq);

			String linha = lerArq.readLine(); 

			while (linha != null) {

				linha = linha.toLowerCase();
				linha = linha.replace(";", "").replace("\t", "").replace(" ", "");


				if(!linha.contains("->") && !linha.contains("{") && !linha.contains("}")){
					Elemento_dicionario novo_elemento = new Elemento_dicionario(linha, nos);
					dicionario.addElemento(novo_elemento);
					nos = nos + 1;
				}

				linha = lerArq.readLine();
			}
			arq.close();

		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n Classe: Window_qt metodo: ler_interface() ",
					e.getMessage());
		} 


		nos = dicionario.getDicionario().size();
		matriz_adjacencia = new int[nos][nos];

		//inicializando a matriz de adjaencias
		int i = 0, j = 0;
		for(i=0; i<nos; i++){
			for(j=0; j<nos; j++){
				matriz_adjacencia[i][j] = 0;
			}
		}


		// prenchendo as conexões
		try {

			FileReader arq = new FileReader(caminho_dot);
			BufferedReader lerArq = new BufferedReader(arq);

			String linha = lerArq.readLine(); 

			while (linha != null) {

				linha = linha.toLowerCase();
				int id_componente_1 = 0;
				int id_componente_2 = 0;
				int conta_componentes = 0;
				
				if(linha.contains("->")){
					numero_arestas = numero_arestas + 1;
					for(String palavra : linha.split("->")) {

						String aux = palavra.replace("\t", "").replace(" ", "").replace(";", "");

						if(conta_componentes==0) {
							id_componente_1 = dicionario.buscar_a_partir_do_valor(aux);

							conta_componentes = conta_componentes + 1;
							//	System.out.println("componente 1 "+ id_componente_1);
							continue;

						}

						if(conta_componentes==1) {

							id_componente_2 = dicionario.buscar_a_partir_do_valor(aux);
							//	System.out.println("componente 2 "+ id_componente_2+ " " + aux);
							if(aux.contains("final")){ //substitui pelo temrinal
								matriz_adjacencia[id_componente_1][id_componente_2] = -1;
							} else {
								if(dicionario.buscar_a_partir_da_chave(id_componente_1).contains("if_") || dicionario.buscar_a_partir_da_chave(id_componente_1).contains("else_") || dicionario.buscar_a_partir_da_chave(id_componente_1).contains("switch_") || dicionario.buscar_a_partir_da_chave(id_componente_1).contains("while_") || dicionario.buscar_a_partir_da_chave(id_componente_1).contains("case_")) {
									matriz_adjacencia[id_componente_1][id_componente_2] = 2;

								}else {
									matriz_adjacencia[id_componente_1][id_componente_2] = 1;					

								}
							}
							id_componente_1 = 0;
							id_componente_2 = 0;
							break;
						}

					}

				}

				linha = lerArq.readLine();
			}
			arq.close();

		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n Classe: Window_qt metodo: ler_interface() ",
					e.getMessage());
		} 


		// lendo a matriz gerada
//				for(i=0; i<nos; i++){
//					for(j=0; j<nos; j++){
//						System.out.print(matriz_adjacencia[i][j]+" ");
//					}
//					System.out.println("");
//				}

		numero_nos = nos;
		setNumberOfVariables(tamanho_cromossomo); // seta o tamanho do cromossomo
		setNumberOfObjectives(3);
		setName("Camilanator");

		List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
		List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

		for (i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(0);
			upperLimit.add((nos-1));
		}
		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
	}


	public void evaluate(IntegerSolution solution){

		int custo = 0;
		int quantidade_coerencia = 1;
		int arestasCobertas = 0;
		double fitness1, fitness2, fitness3;
		
		fitness1 = 1.0;
		fitness2 = 1.0;
		fitness3 = 1.0;
//		------------------------------coerencia--------------------------------------------------

		int j = 0;
		for(int i=0; i<tamanho_cromossomo-1; i++) {
			j = j + 1;
			int elemento = solution.getVariableValue(i);
			int elemento_prox = solution.getVariableValue(j);
			if(matriz_adjacencia[elemento][elemento_prox]!=0) {
				quantidade_coerencia = quantidade_coerencia + 1;
			}

		}
//		System.out.print("func1: "+quantidade_coerencia);
		fitness1 = 1/(Math.sqrt(quantidade_coerencia));
//		fitness1 = fitness1 + (1/quantidade_coerencia);



//		------------------------------custo--------------------------------------------------
		j = 0;
		for(int i=0; i<tamanho_cromossomo-1; i++) {
			j = j + 1;
			int elemento = solution.getVariableValue(i);
			int elemento_prox = solution.getVariableValue(j);
			if(matriz_adjacencia[elemento][elemento_prox]!=0) {
				custo = custo + matriz_adjacencia[elemento][elemento_prox];
			}

		}
//		System.out.print(" func2: "+faz_sequencia);
		fitness2 = 1/(Math.sqrt(custo));
//		fitness2 = (1/faz_sequencia);
			
		
//		------------------------------Cobertura--------------------------------------------------
		
		for (int i = 0; i < matriz_adjacencia.length; i++) {
			for (int k = 0; k < matriz_adjacencia.length; k++) {
				for(int l=0; l<solution.getNumberOfVariables()-1; l++) {
					if(matriz_adjacencia[i][k] != 0 && i==solution.getVariableValue(l) && k==solution.getVariableValue(l+1)) {
						arestasCobertas= arestasCobertas+1;
						break;
					}
				}
			}
		}
		
			
		fitness3 = 1 -(arestasCobertas/numero_arestas);
			
		solution.setObjective(0, fitness1);
		solution.setObjective(1, fitness2);
		solution.setObjective(2, fitness3);
//		System.out.print(" func1: "+fitness1);
//		System.out.print(" func2: "+fitness2);
//		System.out.print(" func3: "+fitness3);
//		System.out.println();

	}


}