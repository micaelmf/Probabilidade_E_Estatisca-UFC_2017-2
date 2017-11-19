import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.plaf.synth.SynthSeparatorUI;

import java.util.Scanner;


public class Problema01 {
	private int qntFunc = 0; //quantos funcionários existem na matriz
	private int qntCargos = 0; //quantos cargos diferentes existem na matriz
	private int totalMedicos = 0; //quantidade total de médicos
	private int totalEnfermeiros = 0; //quantidade total de enfermeiros
	private int totalSecretarios = 0; //quantidade total de secretários
	private int maxMedicos = 0; //quantidade mínima que um grupo deve ter de médicos
	private int maxEnfermeiros = 0; //quantidade mínima que um grupo deve ter de enfermeiros
	private int maxSecretarios = 0; //quantidade mínima que um grupo deve ter de sercretários
	private int distancias[][] = null; //matriz que armazena a distância entre os funcionários
	private int cargos[][] = null; //matriz que armazena os cargos dos funcionário
	private int melhorSoma = 100000; //armazena a melhor soma
	private ArrayList<Integer> melhorGrupo = new ArrayList<Integer>(); // armazena a melhor grupo
	private ArrayList<Integer> grupo = new ArrayList<Integer>(); //sem uso
	private ArrayList<Integer> medicos = new ArrayList<Integer>(); //array com todos os médicos
	private ArrayList<Integer> enfermeiros = new ArrayList<Integer>();
	private ArrayList<Integer> secretarios = new ArrayList<Integer>();
	private ArrayList<Integer> melhorCombMedicos = new ArrayList<Integer>(); // armazena a melhor combinação de médicos
	private ArrayList<Integer> melhorCombEnfermeiros = new ArrayList<Integer>();
	private ArrayList<Integer> melhorCombSecretarios = new ArrayList<Integer>();
	
	
	Problema01(int qntCargos, int qntFunc){
		this.qntCargos = qntCargos;
		this.qntFunc = qntFunc;
		
		int matrizDist[][] = new int[qntFunc][qntFunc];
		this.distancias = matrizDist;
		int matrizCargos[][] = new int[qntFunc][qntCargos];
		this.cargos = matrizCargos;
	}
	
	public void primeiroGrupo(){
		//System.out.println(this.maxEnfermeiros);
		int contM = 0;
		int contE = 0;
		int contS = 0;
		ArrayList<Integer> posicoes = new ArrayList<Integer>();
		ArrayList<Integer> med = new ArrayList<Integer>();
		ArrayList<Integer> enf = new ArrayList<Integer>();
		ArrayList<Integer> sec = new ArrayList<Integer>();
		
		for(int i = 0; i < qntFunc; i++){
			if(this.cargos[i][0] == 1){					
				//É MÉDICO
				this.medicos.add(i); //armazena todos os médicos
				if(contM < this.maxMedicos){
					//System.out.printf("méd("+ i +")|");
					posicoes.add(i); //guarda o índice do funcionário obtido matriz de cargos
					med.add(i);
				}
				contM++;
			}else if(this.cargos[i][1] == 1){
				//É ENFERMEIRO
				this.enfermeiros.add(i);
				if(contE < this.maxEnfermeiros){
					//System.out.printf("enf("+ i +")|");
					posicoes.add(i);
					enf.add(i);
				}
				contE++;
			}else if(this.cargos[i][2] == 1){
				//É SECRETÁRIO
				this.secretarios.add(i);
				if(contS < this.maxSecretarios){
					//System.out.printf("sec("+ i +")|");
					posicoes.add(i);
					sec.add(i);
				}
				contS++;
			}
		}
		ArrayList<Integer> distancias = atribuiDistancias(posicoes);
		somaGrupo(distancias);
		setMelhorGrupo(distancias);
		setMelhorCombMedicos(med);
		setMelhorCombEnfermeiros(enf);
		setMelhorCombSecretarios(sec);
	}
	
	public void procuraMelhorGrupo(int tentativas){
		/*
		 * 1) Pegar o melhor grupo e trocar apenas um funcionário 
		 * 		1.1 > Sortear um número entre 0 e 1mil
		 * 			  Relação entre o número sorteado e o cargo: 1-130 = médico; 131-670 = enfermeiro; 671-1000 = secretários
		 * 		1.2 > Selecionar o funcionário, para troca, com base no número sorteado, ex.: Se o número sorteado foi 339
		 * 			  deve-se selecionar um secretário para ser trocado.
		 * 		1.3 > Selecionar um funcionário do mesmo tipo para substituir o funcionário selecionado antes 
		 * 2) Somar as distâncias da nova lista
		 * 3) Guardar a melhor soma e a melhor lista
		 */
		
		Random r = new Random();		
		ArrayList<Integer> grupoTemp = new ArrayList<Integer>();
		
		for (int i = 0; i < tentativas; i++) {
			int num = r.nextInt(1000);
			
			if(num >= 0 && num <= 130 ){
				//trocar médico
				Random r2 = new Random();
				ArrayList<Integer> melhorSubGrupo = getMelhorCombMedicos();
				int indice = r2.nextInt(melhorSubGrupo.size()-1); // -1 para que no prox. laço não seja removido quem foi adicionado no laço atual
				ArrayList<Integer> subGrupo = trocaFunc(getMelhorCombMedicos(), getMedicos(), indice);
				grupoTemp.addAll(subGrupo);
				grupoTemp.addAll(getMelhorCombEnfermeiros());
				grupoTemp.addAll(getMelhorCombSecretarios());
				
				ArrayList<Integer> distancias = atribuiDistancias(grupoTemp);
				int soma = somaGrupo(distancias);
				imprimiGrupo(grupoTemp);
				System.out.println("Soma: " + soma);
				if(soma < getMelhorSoma()){
					setMelhorSoma(soma);
					setMelhorGrupo(grupoTemp);
					setMelhorCombMedicos(subGrupo);
				}
				grupoTemp = new ArrayList<Integer>();
			}else if(num >= 131 && num <= 670){
				//troca enfermeiro
				Random r2 = new Random();		
				ArrayList<Integer> melhorSubGrupo = getMelhorCombEnfermeiros();
				int indice = r2.nextInt(melhorSubGrupo.size()-1); // -1 para que no prox. laço não seja removido quem foi adicionado no laço atual
				ArrayList<Integer> subGrupo = trocaFunc(getMelhorCombEnfermeiros(), getEnfermeiros(), indice);
				grupoTemp.addAll(getMelhorCombMedicos());
				grupoTemp.addAll(subGrupo);
				grupoTemp.addAll(getMelhorCombSecretarios());
				
				ArrayList<Integer> distancias = atribuiDistancias(grupoTemp);
				int soma = somaGrupo(distancias);
				imprimiGrupo(grupoTemp);
				System.out.println("Soma: " + soma);
				if(soma < getMelhorSoma()){
					setMelhorSoma(soma);
					setMelhorGrupo(grupoTemp);
					setMelhorCombEnfermeiros(subGrupo);
				}
				grupoTemp = new ArrayList<Integer>();
			}else if(num >= 671 && num <= 1000 ){
				//troca secretário
				Random r2 = new Random();		
				ArrayList<Integer> melhorSubGrupo = getMelhorCombSecretarios();
				int indice = r2.nextInt(melhorSubGrupo.size()-1); // -1 para que no prox. laço não seja removido quem foi adicionado no laço atual
				ArrayList<Integer> subGrupo = trocaFunc(getMelhorCombSecretarios(), getSecretarios(), indice);
				grupoTemp.addAll(getMelhorCombMedicos());
				grupoTemp.addAll(getMelhorCombEnfermeiros());
				grupoTemp.addAll(subGrupo);
				
				ArrayList<Integer> distancias = atribuiDistancias(grupoTemp);
				int soma = somaGrupo(distancias);
				imprimiGrupo(grupoTemp);
				System.out.println("Soma: " + soma);
				if(soma < getMelhorSoma()){
					setMelhorSoma(soma);
					setMelhorGrupo(grupoTemp);
					setMelhorCombSecretarios(subGrupo);
				}
				grupoTemp = new ArrayList<Integer>();
			}else{
				System.out.println("COMO ASSIM?!");
			}
			
		}
	}
	
	public ArrayList<Integer> trocaFunc(ArrayList<Integer> melhorSubGrupo, ArrayList<Integer> funcionarios, int indice){
		Random r = new Random();
		boolean adiciona = false;
		
		int valor = melhorSubGrupo.get(indice);
		//melhorSubGrupo.remove(indice);
		int funcionario = (int) funcionarios.get(r.nextInt(funcionarios.size()));
		for (int i = 0; i < melhorSubGrupo.size(); i++) {
			if(funcionario != melhorSubGrupo.get(i)){
				adiciona = true;
			}else{
				adiciona = false;
				break;
			}
		}
		if(adiciona == true){
			melhorSubGrupo.remove(indice);
			//melhorSubGrupo.add(funcionario);
			melhorSubGrupo.add(indice, funcionario);
		}else{
			//melhorSubGrupo.add(valor);
			trocaFunc(melhorSubGrupo, funcionarios, indice);
		}
		return melhorSubGrupo;
	}
	
	public ArrayList<Integer> vetorAleatorioFuncionarios(int maxCargo, ArrayList<Integer> cargo){
		ArrayList<Integer> resultado = new ArrayList<Integer>(); //armazenas os funcionários para retorno
		Random r = new Random();
		boolean troca = false;
		int funcionario = (int) cargo.get(r.nextInt(cargo.size())); //sorteia um funcionário dentro do arraylist passado por parametro
		
		resultado.add(funcionario);
		for (int k = 0; resultado.size() < maxCargo; k++) {
			funcionario = (int) cargo.get(r.nextInt(cargo.size()));
			for (int i = 0; i < resultado.size(); i++) {
				if(funcionario != resultado.get(i)){
					troca = true;
				}else{
					troca = false;
					break;
				}
			}
			if(troca == true){
				resultado.add(funcionario);
			}
		}
		return resultado;
	}
	
	public ArrayList<Integer> atribuiDistancias(ArrayList<Integer> posicoes){
		/*
		System.out.println("------------------------------------------------------------------------------------");
		System.out.printf("Grupo: \t\t");
		imprimiGrupo(posicoes);
		*/
		ArrayList<Integer> grupoTemp = new ArrayList<Integer>();
		//atribuindo as distancias entre os funcionários
		for(int i = 0; i <= posicoes.size(); i++){
			for(int j = i+1; j < posicoes.size(); j++){
				grupoTemp.add(this.distancias[posicoes.get(i)][posicoes.get(j)]);
				break;
			}
		}
		grupoTemp.add(this.distancias[posicoes.get(0)][posicoes.get((calcTamGrupo())-1)]);
		/*
		System.out.printf("Grupo(dist): \t");
		imprimiGrupo(grupoTemp);
		System.out.println("------------------------------------------------------------------------------------");
		*/
		return grupoTemp;
	}
	
	public int somaGrupo(ArrayList<Integer> grupo){
		int soma = 0;
		int parcial = 0;
		for (int i = 0; i < grupo.size(); i++) {
			parcial = 0;
			for (int j = i+1; j < grupo.size(); j++) {
				parcial = parcial + (grupo.get(i) + grupo.get(j));				
				//System.out.printf("(" + grupo.get(i) + " + " + grupo.get(j) + ")");
			}
			soma = soma + parcial;
			if(parcial != 0){
				//System.out.println(" Parcial: " + parcial);
			}
		}
		/*
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		System.out.println("RESULTADO: " + soma);
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		*/
		return soma;
	}
	
	public void imprimiGrupo(ArrayList<Integer> grupo){
		for(int i = 0; i < grupo.size(); i++){
			System.out.printf(grupo.get(i) + "|");
		}
		//System.out.println("");
	}
	
	public void imprimiMelhorGrupo(){
		ArrayList<Integer> melhor = new ArrayList<Integer>();
		melhor = getMelhorGrupo();
		for(int i = 0; i < melhor.size(); i++){
			System.out.printf(melhor.get(i)+1 + "|");
		}
		System.out.printf("Soma:" + getMelhorSoma());
		System.out.println("");
	}
	
	public void carregaDistancias() throws IOException{
		
        FileReader arq = new FileReader("C:\\Users\\Micael Ferreira\\workspace\\PE01\\dados.txt");
        //armazenando conteudo no arquivo no buffer
        BufferedReader lerArq = new BufferedReader(arq);
        //lendo a primeira linha
        String linha = lerArq.readLine();
        //a variavel linha recebe o valor 'null' quando chegar no final do arquivo
        while (linha != null){
        	
            for(int i = 0; i < this.distancias.length; i++){
            	//passa os "atributos" do Array para a matriz
            	String[] valores = linha.split(Pattern.quote("|"));
            	//Captura a próxima linha
                linha = lerArq.readLine();
            	for(int j = 0; j < valores.length ; j++){
            		this.distancias[i][j] = Integer.parseInt(valores[j]);
            	}
            }
        }
	
	}
	
	public void carregaCargos() throws IOException{
		
        FileReader arq = new FileReader("C:\\Users\\Micael Ferreira\\workspace\\PE01\\cargos.txt");
        //armazenando conteudo no arquivo no buffer
        BufferedReader lerArq = new BufferedReader(arq);
        //lendo a primeira linha
        String linha = lerArq.readLine();
        //a variavel linha recebe o valor 'null' quando chegar no final do arquivo
        while (linha != null){
        	
            for(int i = 0; i < this.cargos.length; i++){
            	//passa os "atributos" do Array para a matriz
            	String[] valores = linha.split(Pattern.quote("|"));
            	//Captura a próxima linha
                linha = lerArq.readLine();
            	for(int j = 0; j < valores.length; j++){
            		this.cargos[i][j] = Integer.parseInt(valores[j]);
            	}
            }
        }
	
	}
	
	public void imprimiDistancias(){
		for(int i = 0; i < this.distancias.length; i++){
        	for(int j = 0; j < this.distancias.length; j++){
        		System.out.printf(this.distancias[i][j] + "|");
        	}
        	System.out.println("");
        }
	}
	
	public void imprimiCargos(){
		for(int i = 0; i < this.qntFunc; i++){
        	for(int j = 0; j < this.qntCargos; j++){
        		System.out.printf(this.cargos[i][j] + "|");
        	}
        	System.out.println("");
        }
	}

	public void rand(int Str, int End, int tam) {
		ArrayList<Integer> numeros = new ArrayList<Integer>();
		boolean add = false;
		int num = (int) Math.ceil(Math.random() * (End  - Str + 1)) - 1 + Str;
		numeros.add(num);
		for(int i = tam; i > 0; i--){
			while (numeros.size() <= i){
				num = (int) Math.ceil(Math.random() * (End  - Str + 1)) - 1 + Str;
				for(int j = 0; j < numeros.size(); j++){
					if (num != numeros.get(j)){
						add = true;
					}else{
						add = false;
						break;
					}
				}
				if(add == true){
					numeros.add(num);
					System.out.printf(num + "|");
				}
			}
			System.out.println("");
			numeros = new ArrayList<Integer>();
			num = (int) Math.ceil(Math.random() * (End  - Str + 1)) - 1 + Str;
			numeros.add(num);
		}
    }
	
	public int calcTamGrupo(){
		return this.getMaxEnfermeiros() + this.getMaxMedicos() + this.getMaxSecretarios();
	}
	
	public int getTotalMedicos() {
		return totalMedicos;
	}

	public void setTotalMedicos(int totalMedicos) {
		this.totalMedicos = totalMedicos;
	}

	public int getTotalEnfermeiros() {
		return totalEnfermeiros;
	}

	public void setTotalEnfermeiros(int totalEnfermeiros) {
		this.totalEnfermeiros = totalEnfermeiros;
	}

	public int getTotalSecretarios() {
		return totalSecretarios;
	}

	public void setTotalSecretarios(int totalSecretarios) {
		this.totalSecretarios = totalSecretarios;
	}

	public int getMaxMedicos() {
		return maxMedicos;
	}

	public void setMaxMedicos(int maxMedicos) {
		this.maxMedicos = maxMedicos;
	}

	public int getMaxEnfermeiros() {
		return maxEnfermeiros;
	}

	public void setMaxEnfermeiros(int maxEnfermeiros) {
		this.maxEnfermeiros = maxEnfermeiros;
	}

	public int getMaxSecretarios() {
		return maxSecretarios;
	}

	public void setMaxSecretarios(int maxSecretarios) {
		this.maxSecretarios = maxSecretarios;
	}

	public ArrayList<Integer> getMedicos() {
		return medicos;
	}

	public void setMedicos(ArrayList<Integer> medicos) {
		this.medicos = medicos;
	}

	public ArrayList<Integer> getEnfermeiros() {
		return enfermeiros;
	}

	public void setEnfermeiros(ArrayList<Integer> enfermeiros) {
		this.enfermeiros = enfermeiros;
	}

	public ArrayList<Integer> getSecretarios() {
		return secretarios;
	}

	public void setSecretarios(ArrayList<Integer> secretarios) {
		this.secretarios = secretarios;
	}

	public int getMelhorSoma() {
		return melhorSoma;
	}

	public void setMelhorSoma(int melhorSoma) {
		this.melhorSoma = melhorSoma;
	}

	public ArrayList<Integer> getGrupo() {
		return grupo;
	}

	public void setGrupo(ArrayList<Integer> grupo) {
		this.grupo = grupo;
	}

	public int getQntFunc() {
		return qntFunc;
	}

	public void setQntFunc(int qntFunc) {
		this.qntFunc = qntFunc;
	}

	public int getQntCargos() {
		return qntCargos;
	}

	public void setQntCargos(int qntCargos) {
		this.qntCargos = qntCargos;
	}

	public ArrayList<Integer> getMelhorGrupo() {
		return melhorGrupo;
	}

	public void setMelhorGrupo(ArrayList<Integer> melhorGrupo) {
		this.melhorGrupo = melhorGrupo;
	}

	public ArrayList<Integer> getMelhorCombMedicos() {
		return melhorCombMedicos;
	}

	public void setMelhorCombMedicos(ArrayList<Integer> melhorCombMedicos) {
		this.melhorCombMedicos = melhorCombMedicos;
	}

	public ArrayList<Integer> getMelhorCombEnfermeiros() {
		return melhorCombEnfermeiros;
	}

	public void setMelhorCombEnfermeiros(ArrayList<Integer> melhorCombEnfermeiros) {
		this.melhorCombEnfermeiros = melhorCombEnfermeiros;
	}

	public ArrayList<Integer> getMelhorCombSecretarios() {
		return melhorCombSecretarios;
	}

	public void setMelhorCombSecretarios(ArrayList<Integer> melhorCombSecretarios) {
		this.melhorCombSecretarios = melhorCombSecretarios;
	}
    
	
}
