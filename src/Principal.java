import java.io.IOException;
import java.util.ArrayList;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class Principal {

	public static void main(String[] args) throws IOException {
		Problema01 p = new Problema01(3,45); //(quantidade de cargos, quantidade de funcionários)
		
		//p.rand(1, 70, 44);
		
		//System.out.println("\n----------- DISTANCIAS -----------");
		p.carregaDistancias();
		//p.imprimiDistancias();
		//System.out.println("\n----------- CARGOS -----------");
		p.carregaCargos();
		//p.imprimiCargos();
		
		//total de cada cargo na organização
		p.setTotalEnfermeiros(21);
		p.setTotalMedicos(10);
		p.setTotalSecretarios(13);
		//divisão da quantidade de cada cargo por grupo
		p.setMaxEnfermeiros(8);
		p.setMaxMedicos(2);
		p.setMaxSecretarios(5);
		
		p.primeiroGrupo();
		
		
	}

}
