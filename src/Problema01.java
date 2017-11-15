import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	private int distancias[][] = null;
	private int cargos[][] = null;
	private int melhorSoma = 0;
	private ArrayList<Integer> melhorGrupo = new ArrayList<Integer>();
	private ArrayList<Integer> grupo = new ArrayList<Integer>();
	
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
				//É MÉDICO
				if(contM < this.maxMedicos){
					//System.out.printf("méd("+ i +")|");
					posicoes.add(i); //identifica o índice do funcionário na matriz de cargos
				}
				contM++;
			}else if(this.cargos[i][1] == 1){
				//É ENFERMEIRO
				if(contE < this.maxEnfermeiros){
					//System.out.printf("enf("+ i +")|");
					posicoes.add(i);
				}
				contE++;
			}else if(this.cargos[i][2] == 1){
				//É SECRETÁRIO
				if(contS < this.maxSecretarios){
					//System.out.printf("sec("+ i +")|");
					posicoes.add(i);
				}
				contS++;
			}
		}
		System.out.printf("1º Grupo:    " );
		atribuiDistancias(posicoes);
		imprimiGrupo(this.grupo);
		somaGrupo();
		this.melhorGrupo = this.grupo;
	}
	
	public void trocaGrupo(){
		trocaMedico();
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
	
	public void atribuiDistancias(ArrayList<Integer> posicoes){
		imprimiGrupo(posicoes);
		//atribuindo ao arraylist grupo as distancias entre os funcionários
		for(int i = 0; i <= posicoes.size(); i++){
			for(int j = i+1; j < posicoes.size(); j++){
				this.grupo.add(this.distancias[posicoes.get(i)][posicoes.get(j)]);
				break;
			}
		}
		this.grupo.add(this.distancias[posicoes.get(0)][posicoes.get((calcTamGrupo())-1)]);
		
		System.out.printf("Grupo(Dist): ");
	}
	
	public int somaGrupo(){
		int soma = 0;
		int parcial = 0;
		for (int i = 0; i < this.grupo.size(); i++) {
			parcial = 0;
			for (int j = i+1; j < this.grupo.size(); j++) {
				parcial = parcial + this.grupo.get(i) + this.grupo.get(j);				
				System.out.printf("(" + this.grupo.get(i) + " + " + this.grupo.get(j) + ")");
			}
			soma = soma + parcial;
			if(parcial != 0){
				System.out.println(" Parcial: " + parcial);
			}
		}
		
		if(soma > this.melhorSoma){
			//chamar o método que troca o grupo
		}else{
			
		}
		System.out.println("_________________________");
		System.out.println("RESULTADO: " + soma);
		
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

	
	

}
