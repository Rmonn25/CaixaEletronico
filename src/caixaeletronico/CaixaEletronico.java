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

	public CaixaEletronico() {
		cedulas = new int[][] 
			{ { 100, 100 }, 
			{ 50, 200 }, 
			{ 20, 300 }, 
			{ 10, 350 }, 
			{ 5, 450 }, 
			{ 2, 500 } };

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
		
		    int total = calcularTotalCaixa();
		    return "Total disponível no caixa: R$ " + total;
		
//logica de pega o valor total disponivel no caixa eletronio
	}

	public String reposicaoCedulas(Integer cedula, Integer quantidade) {

		int linha = buscarLinhaCedula(cedula);

		if (linha == -1) {
			return "Cédula inválida!";
		}

		cedulas[linha][COLUNA_QUANTIDADE] += quantidade;


		StringBuilder sb = new StringBuilder("--- Relatório de Cédulas ---\n");
		for (int i = 0; i < cedulas.length; i++) {
			sb.append("R$ ").append(cedulas[i][COLUNA_VALOR])
					.append(": ").append(cedulas[i][COLUNA_QUANTIDADE]).append(" unidades\n");
		}
		return sb.toString();
	}


	public String sacar(Integer valor) {


		if (valor <= 0) {
			return "Valor inválido!";
		}

		if (valor > calcularTotalCaixa()) {
			return "Saldo insuficiente!";
		}

		int restante = valor;

		for (int i = 0; i < cedulas.length; i++) {

			int valorCedula = cedulas[i][COLUNA_VALOR];
			int quantidadeDisponivel = cedulas[i][COLUNA_QUANTIDADE];

		if (valor <= 0) {
			return "Valor inválido!";
		}
		if (valor > calcularTotalCaixa()) {
			return "Saldo insuficiente!";
		}
		int restante = valor;
		for (int i = 0; i < cedulas.length; i++) {
			int valorCedula = cedulas[i][COLUNA_VALOR];
			int quantidadeDisponivel = cedulas[i][COLUNA_QUANTIDADE];
			int qtdNecessaria = restante / valorCedula;

			if (qtdNecessaria > quantidadeDisponivel) {
				qtdNecessaria = quantidadeDisponivel;
			}

			cedulas[i][COLUNA_QUANTIDADE] -= qtdNecessaria;
			restante -= qtdNecessaria * valorCedula;
		}

		if (restante != 0) {

			return "Não é possível sacar esse valor.";
		}


			return "Não é possível sacar esse valor com as notas disponíveis.";
		}

		return "Saque realizado com sucesso!";
	}

	public String armazenaCotaMinima(Integer minimo) {
		String resposta = "";

		
//logica de armazenar a cota minima para saque e criar um //mensagem(resposta)ao usuario
		
		if (minimo == null || minimo < 0) {
	        resposta = "Valor de cota mínima inválido.";
	        return resposta;
	    }

	    cotaMinima = minimo;

	    // verifica se o caixa continua ativo após definir a nova cota
	    caixaAtivo = !estaAbaixoDaCotaMinima();

	    resposta = ("Cota mínima armazenada com sucesso: R$ " + cotaMinima);

	    registrarOperacao("Cota mínima definida para R$ " + cotaMinima);
		
		

		if (minimo == null || minimo < 0) {
			resposta = "Valor de cota mínima inválido.";
			return resposta;
		}
		cotaMinima = minimo;
		caixaAtivo = !estaAbaixoDaCotaMinima();
		resposta = ("Cota mínima armazenada com sucesso: R$ " + cotaMinima);
		registrarOperacao("Cota mínima definida para R$ " + cotaMinima);


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
	}

	public static void main(String[] args) {
		ICaixaEletronico minhaLogica = new CaixaEletronico();
		GUI janela = new GUI(minhaLogica);
		janela.show();
	}
}

