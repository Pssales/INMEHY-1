package utilidades;

import java.io.IOException;

public class geradorFronteiraPareto {
	
	
	public static void main(String[] args) throws IOException {
		auxFronteiraPareto pf = new auxFronteiraPareto();
		
		for(int i=2; i<=2; i++) {
			pf.gerarFPFinal(i, 10);
		}
	}
}
