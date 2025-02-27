package utilidades;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import utilidades.Impressora;



public class Impressora {
	
	private static Impressora uniqueInstance;
		
		public static Impressora getInstance(){
			if(uniqueInstance==null){
				uniqueInstance = new Impressora();
			}
			return uniqueInstance;
		}
		
		private Impressora(){}
	
	public void imprimirArquivo(String nome_arquivo, String frase) throws IOException{
			FileWriter saida = new FileWriter(nome_arquivo, true);
			PrintWriter gravarArquivo = new PrintWriter(saida);
			gravarArquivo.print(frase);
			saida.close();
			gravarArquivo.close();
		}
	public void salveTestCase(String dir, String nome_arquivo, List<String> frase) throws IOException{
		
		File diretorio = new File("casos_teste\\"+dir);
		if (!diretorio.exists()) {
			diretorio.mkdirs();
		}
		FileWriter saida = new FileWriter("casos_teste\\"+dir+"\\"+nome_arquivo, true);
		PrintWriter gravarArquivo = new PrintWriter(saida);
		gravarArquivo.print(frase);
		saida.close();
		gravarArquivo.close();
	}
	
	public void verifyDiretorio(String diretorio_) {
		File diretorio = new File(diretorio_);
		if (!diretorio.exists()) {
			diretorio.mkdirs();
		}
	}
 
}
