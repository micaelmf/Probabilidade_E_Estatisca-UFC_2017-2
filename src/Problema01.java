import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Scanner;


public class Problema01 {
	private ArrayList combinacao = new ArrayList();
	private ArrayList resultado = new ArrayList();
	private int qntFunc = 0;
	private int qntCargos = 0;
	private int distancias[][] = null;
	private int cargos[][] = null;
	
	Problema01(int qntCargos, int qntFunc){
		this.qntCargos = qntCargos;
		this.qntFunc = qntFunc;
		
		int matrizDist[][] = new int[qntFunc][qntFunc];
		this.distancias = matrizDist;
		int matrizCargos[][] = new int[qntFunc][qntCargos];
		this.cargos = matrizCargos;
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

	

}
