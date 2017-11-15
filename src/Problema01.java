import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.plaf.synth.SynthSeparatorUI;

import java.util.Scanner;


public class Problema01 {
	private int qntFunc = 0; //quantos funcion�rios existem na matriz
	private int qntCargos = 0; //quantos cargos diferentes existem na matriz
	private int totalMedicos = 0; //quantidade total de m�dicos
	private int totalEnfermeiros = 0; //quantidade total de enfermeiros
	private int totalSecretarios = 0; //quantidade total de secret�rios
	private int maxMedicos = 0; //quantidade m�nima que um grupo deve ter de m�dicos
	private int maxEnfermeiros = 0; //quantidade m�nima que um grupo deve ter de enfermeiros
	private int maxSecretarios = 0; //quantidade m�nima que um grupo deve ter de sercret�rios
	private int distancias[][] = null;
	private int cargos[][] = null;
	private int melhorSoma = 0;
	private ArrayList<Integer> melhorGrupo = new ArrayList<Integer>();
	private ArrayList<Integer> grupo = new ArrayList<Integer>();
	private ArrayList<Integer> medicos = new ArrayList<Integer>();
	private ArrayList<Integer> enfermeiros = new ArrayList<Integer>();
	private ArrayList<Integer> secretarios = new ArrayList<Integer>();
	
	
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
		
		for(int i = 0; i < qntFunc; i++){
			if(this.cargos[i][0] == 1){					
				//� M�DICO
				this.medicos.add(i); //armazena todos os m�dicos
				if(contM < this.maxMedicos){
					//System.out.printf("m�d("+ i +")|");
					posicoes.add(i); //identifica o �ndice do funcion�rio na matriz de cargos
				}
				contM++;
			}else if(this.cargos[i][1] == 1){
				//� ENFERMEIRO
				this.enfermeiros.add(i);
				if(contE < this.maxEnfermeiros){
					//System.out.printf("enf("+ i +")|");
					posicoes.add(i);
				}
				contE++;
			}else if(this.cargos[i][2] == 1){
				//� SECRET�RIO
				this.secretarios.add(i);
				if(contS < this.maxSecretarios){
					//System.out.printf("sec("+ i +")|");
					posicoes.add(i);
				}
				contS++;
			}
		}
		ArrayList<Integer> distancias = atribuiDistancias(posicoes);
		somaGrupo(distancias);
		setMelhorGrupo(distancias);
	}
	
	public void procuraMelhorGrupo(int tentativas){
		/*
		 * METADE 1
		 * OK 1) Criar um vetor totalmente aleatorio para os tr�s cargos
		 * OK 2) juntar os tr�s vetores criados acima
		 * OK 3) somar o vetor resultante da uni�o
		 * OK 4) se a soma for menor que "melhorsoma" substituir o "melhorgrupo" pelo grupo atual e armazenar
		 * 			os tr�s vetores dos cargos
		 * OK 5) repetir os passos acima at� "cont" alcan�ar "metade1"
		 * 
		 * METADE 2
		 *    1) pegar os tr�s vetores de cargo e substituir a metade de cada um deles aleatoriamente
		 *    2) juntar os tr�s vetores alterados acima
		 *    3) somar o vetor resultante da uni�o
		 *    4) repetir os passos acima at� "cont" alcan�ar o numero de "tentativas"
		 */
		
		int metade = tentativas/2;
		int cont = 1;
		ArrayList<Integer> grupoTemp = new ArrayList<Integer>();
		
		while(cont <= metade){
			grupoTemp.addAll(vetorAleatorioFuncionarios(getMaxMedicos(), getMedicos()));
			grupoTemp.addAll(vetorAleatorioFuncionarios(getMaxEnfermeiros(), getEnfermeiros()));
			grupoTemp.addAll(vetorAleatorioFuncionarios(getMaxSecretarios(), getSecretarios()));
			
			//imprimiGrupo(grupoTemp);
			ArrayList<Integer> distancias = atribuiDistancias(grupoTemp);
			int soma = somaGrupo(distancias); 
			if(soma < getMelhorSoma()){
				setMelhorSoma(soma);
				setMelhorGrupo(distancias);
			}
			cont++;
			grupoTemp = new ArrayList<Integer>();
		}
	}
	
	public ArrayList<Integer> vetorAleatorioFuncionarios(int maxCargo, ArrayList<Integer> cargo){
		ArrayList<Integer> resultado = new ArrayList<Integer>(); //armazenas os funcion�rios para retorno
		Random r = new Random();
		boolean troca = false;
		int funcionario = (int) cargo.get(r.nextInt(cargo.size())); //sorteia um funcion�rio dentro do arraylist passado por parametro
		//int cont = 1;
		
		resultado.add(funcionario);
		//System.out.printf(funcionario + "|");
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
				//cont++;
				//System.out.printf(funcionario + "|");
			}
		}
		//System.out.println("cont: " + cont);
		return resultado;
	}
	
	
	public void trocaMedico(){
		
	}
	
	public String encontraCargo(){
		for(int i = 0; i < qntFunc; i++){
			if(this.cargos[i][0] == 1){					
				return "medico";
			}else if(this.cargos[i][1] == 1){
				return "emfermeiro";
			}else if(this.cargos[i][2] == 1){
				return "secretario";
			}
		}
		return null;
	}
	
	public ArrayList<Integer> atribuiDistancias(ArrayList<Integer> posicoes){
		System.out.println("------------------------------------------------------------------------------------");
		System.out.printf("Grupo: \t\t");
		imprimiGrupo(posicoes);
		ArrayList<Integer> grupoTemp = new ArrayList<Integer>();
		//atribuindo as distancias entre os funcion�rios
		for(int i = 0; i <= posicoes.size(); i++){
			for(int j = i+1; j < posicoes.size(); j++){
				grupoTemp.add(this.distancias[posicoes.get(i)][posicoes.get(j)]);
				break;
			}
		}
		grupoTemp.add(this.distancias[posicoes.get(0)][posicoes.get((calcTamGrupo())-1)]);
		System.out.printf("Grupo(dist): \t");
		imprimiGrupo(grupoTemp);
		System.out.println("------------------------------------------------------------------------------------");
		
		return grupoTemp;
	}
	
	public int somaGrupo(ArrayList<Integer> grupo){
		int soma = 0;
		int parcial = 0;
		for (int i = 0; i < grupo.size(); i++) {
			parcial = 0;
			for (int j = i+1; j < grupo.size(); j++) {
				parcial = parcial + (grupo.get(i) + grupo.get(j));				
				System.out.printf("(" + grupo.get(i) + " + " + grupo.get(j) + ")");
			}
			soma = soma + parcial;
			if(parcial != 0){
				System.out.println(" Parcial: " + parcial);
			}
		}
		
		if(soma > getMelhorSoma()){
			//chamar o m�todo que troca o grupo
		}else{
			
		}
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		System.out.println("RESULTADO: " + soma);
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		
		return soma;
	}
	
	public void imprimiGrupo(ArrayList<Integer> grupo){
		for(int i = 0; i < grupo.size(); i++){
			System.out.printf(grupo.get(i) + "|");
		}
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
            	//Captura a pr�xima linha
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
            	//Captura a pr�xima linha
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
	public int aleatorio(int inicio, int fim){
		return (int) Math.ceil(Math.random() * (fim  - inicio + 1)) - 1 + inicio;
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
    
}
