package problemas;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
	private int tamanho_cromossomo = 800;
	private int [][] matriz_adjacencia ;


	public Camila_problema(String caminho_dot) throws IOException {

		HashMap<String, Integer> dicionario = new HashMap<>();

		int nos = 0;

		// primeira parte é fazer o dicionario
		try {

			FileReader arq = new FileReader(caminho_dot);
			BufferedReader lerArq = new BufferedReader(arq);

			String linha = lerArq.readLine(); 

			while (linha != null) {

				linha = linha.toLowerCase();
				linha = linha.replace(";", "").replace("\t", "").replace(" ", "");


				if(!linha.contains("->") && !linha.contains("{") && !linha.contains("}") && !dicionario.containsKey(linha)){
					dicionario.put(linha, nos);

					nos = nos + 1;
				}

				linha = lerArq.readLine();
			}
			arq.close();

		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n Classe: Window_qt metodo: ler_interface() ",
					e.getMessage());
		} 


		nos = dicionario.size();
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
					for(String palavra : linha.split("->")) {

						String aux = palavra.replace("\t", "").replace(" ", "").replace(";", "");

						if(conta_componentes==0) {
							id_componente_1 = dicionario.get(aux);

							conta_componentes = conta_componentes + 1;
							continue;
						}



						if(conta_componentes==1) {
							id_componente_2 = dicionario.get(aux);

							if(aux.contains("final")){ //substitui pelo temrinal

								matriz_adjacencia[id_componente_1][id_componente_2] = -1;
							} else {
								if(id_componente_1 == dicionario.get("if")) {
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
		for(i=0; i<nos; i++){
			for(j=0; j<nos; j++){
				System.out.print(matriz_adjacencia[i][j]+" ");
			}
			System.out.println("");
		}

		numero_nos = nos;
		setNumberOfVariables(tamanho_cromossomo); // seta o tamanho do cromossomo
		setNumberOfObjectives(2);
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



		//		for (int i = 0; i < matriz_adjacencia.length; i++) {
		//			System.out.println("sdsd");
		//		}

		int quantidade_repetidas = 0;
		int quantidade_coerencia = 0;
		double fitness1, fitness2;
		fitness1 = 1.0;
		fitness2 = 1.0;

		////// verificação se existe ou valores repetidos no cromossomo




		System.out.println(solution);
		System.out.println(numero_nos);



		
		
		// cobertura
		int faz_sequencia = 1;
		
		int j = 0;
		for(int i=0; i<tamanho_cromossomo-1; i++) {
			j = j + 1;
			int elemento = solution.getVariableValue(i);
			int elemento_prox = solution.getVariableValue(j);
			if(matriz_adjacencia[elemento][elemento_prox]!=0) {
				faz_sequencia = faz_sequencia + 1;
			}

		}
	

	fitness1 = 1/(Math.pow(faz_sequencia, faz_sequencia));

	
	//custo
	j = 0;
	faz_sequencia = 1;
	for(int i=0; i<tamanho_cromossomo-1; i++) {
		j = j + 1;
		int elemento = solution.getVariableValue(i);
		int elemento_prox = solution.getVariableValue(j);
		if(matriz_adjacencia[elemento][elemento_prox]!=0) {
			faz_sequencia = faz_sequencia + matriz_adjacencia[elemento][elemento_prox];
		}

	}
	
	
	fitness2 = 1/(Math.pow(faz_sequencia, faz_sequencia));

	
	solution.setObjective(0, fitness1);
	solution.setObjective(1, fitness2);
	
	



}


private int getQuantidade_repetidas() {
	return 13;
}

private int getQuantidade_coerencia() {

	return 200;
}

private int getQuantidade_atributos() {

	/// fazer um metodo que le o código fonte de cada estudo de caso e descobre a quantidade de atributos
	// retorno quantidade de atributos

	Random gerador = new Random();
	return gerador.nextInt(26);


}

private int getQuantidade_metodos() {

	/// fazer um metodo que le o código fonte de cada estudo de caso e descobre a quantidade de metodos
	// retorno quantidade de metodos
	Random gerador = new Random();
	return gerador.nextInt(26);
}





}