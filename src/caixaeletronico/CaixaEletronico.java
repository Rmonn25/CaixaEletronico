package caixaeletronico;

import java.util.ArrayList;
import java.util.List;

public class CaixaEletronico implements ICaixaEletronico {
	
	private static final int COLUNA_VALOR = 0;
	private static final int COLUNA_QUANTIDADE = 1;
	
	// matriz 6x1
	// coluna 0 = valor da cédula
	// coluna 1 = quantitade disponivel
	
	private int[][] cedulas;
	
	// armazena a cota mínima do caixa
	private int cotaMinima;

	// controla se o caixa ainda pode atender
	private boolean caixaAtivo;

	// histórico de operações / saques
	private List<String> historicoOperacoes;

	// construtor
	public CaixaEletronico() {
		cedulas = new int[][] {
			{100, 100},
			{50, 200},
			{20, 300},
			{10, 350},
			{5, 450},
			{2, 500}
		};

		cotaMinima = 0;
		caixaAtivo = true;
		historicoOperacoes = new ArrayList<String>();
	}
		
	public String pegaRelatorioCedulas() {
		String resposta = "";
//logica de fazer o relatorio de cedulas
		return resposta;
	}

	public String pegaValorTotalDisponivel() {
		String resposta = "";
//logica de pega o valor total disponivel no caixa eletronio
		return resposta;
	}

	public String reposicaoCedulas(Integer cedula, Integer quantidade) {
		String resposta = "";
//logica de fazer a reposicao de cedulas e criar uma mensagem //(resposta)ao usuario
		return resposta;
	}

	public String sacar(Integer valor) {
		String resposta = "";
//logica de sacar do caixa eletronico e criar um mensagem(resposta) ao // usuario
		return resposta;
	}

	public String armazenaCotaMinima(Integer minimo) {
		String resposta = "";
//logica de armazenar a cota minima para saque e criar um //mensagem(resposta)ao usuario
		return resposta;
	}

	// MÉTODOS AUXILIARES

	// procura em qual linha da matriz está a cédula informada
	private int buscarLinhaCedula(int valorCedula) {
		for (int i = 0; i < cedulas.length; i++) {
			if (cedulas[i][COLUNA_VALOR] == valorCedula) {
				return i;
			}
		}
		return -1;
	}

	// calcula o valor total disponível no caixa
	private int calcularTotalCaixa() {
		int total = 0;

		for (int i = 0; i < cedulas.length; i++) {
			total += cedulas[i][COLUNA_VALOR] * cedulas[i][COLUNA_QUANTIDADE];
		}

		return total;
	}

	// verifica se o caixa ficou abaixo da cota mínima
	private boolean estaAbaixoDaCotaMinima() {
		return calcularTotalCaixa() < cotaMinima;
	}

	// registra operação no histórico
	private void registrarOperacao(String operacao) {
		historicoOperacoes.add(operacao);
	}

	// monta o histórico em texto
	private String montarHistorico() {
		StringBuilder sb = new StringBuilder();

		for (String operacao : historicoOperacoes) {
			sb.append(operacao).append("\n");
		}

		return sb.toString();
	}

	public static void main(String arg[]) {
		GUI janela = new GUI(CaixaEletronico.class);
		janela.show();
	}
}
