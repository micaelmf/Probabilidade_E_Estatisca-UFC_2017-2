import java.io.IOException;
import java.util.ArrayList;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class Principal {

	public static void main(String[] args) throws IOException {
		Problema01 p = new Problema01(3,7); //(quantidade de cargos, quantidade de funcionários)

		System.out.println("\n----------- DISTANCIAS -----------");
		p.carregaDistancias();
		p.imprimiDistancias();
		System.out.println("\n----------- CARGOS -----------");
		p.carregaCargos();
		p.imprimiCargos();


	}

}
