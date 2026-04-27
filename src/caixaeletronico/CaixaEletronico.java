package caixaeletronico;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CaixaEletronico implements ICaixaEletronico {

	private static final int COLUNA_VALOR = 0;
	private static final int COLUNA_QUANTIDADE = 1;

	private ArrayList<String> historicoSaques = new ArrayList<>();
	private ArrayList<String> historicoReposicoes = new ArrayList<>();

	// matriz 6x1
	// coluna 0 = valor da cédula
	// coluna 1 = quantitade disponivel
	private int[][] cedulas;

	// armazena a cota mínima do caixa
	private int cotaMinima;

	// controla se o caixa ainda pode atender
	private boolean caixaAtivo;

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
	}

	public String pegaRelatorioCedulas() {

		StringBuilder sb = new StringBuilder("--- Relatório de Cédulas ---\n");
		for (int i = 0; i < cedulas.length; i++) {
			int valor = cedulas[i][COLUNA_VALOR];
			int quantidade = cedulas[i][COLUNA_QUANTIDADE];
			{
				sb.append("R$ ").append(valor).append(": ").append(quantidade).append(" unidades\n");
			}
		}

		return sb.toString();
	}

	public String pegaValorTotalDisponivel() {

		int total = calcularTotalCaixa();

		DecimalFormat formatacao = new DecimalFormat("#,###");
		return "Total disponível no caixa: R$ " + formatacao.format(total);

		//logica de pega o valor total disponivel no caixa eletronio
	}

	public String reposicaoCedulas(Integer cedula, Integer quantidade) {

		int linha = buscarLinhaCedula(cedula);

		if (linha == -1) {
			return "Cédula inválida!";
		}

		cedulas[linha][COLUNA_QUANTIDADE] += quantidade;

		// Depois da reposição, verifica se o caixa voltou a ficar ativo
		caixaAtivo = !estaAbaixoDaCotaMinima();

		StringBuilder sb = new StringBuilder("=== RELATÓRIO DE CÉDULAS ===\n");
		for (int i = 0; i < cedulas.length; i++) {
			sb.append("R$ ").append(cedulas[i][COLUNA_VALOR]).append(": ").append(cedulas[i][COLUNA_QUANTIDADE])
					.append(" unidades\n");
		}

		DateTimeFormatter DataHoraExtrato = DateTimeFormatter.ofPattern("dd/mm/yyyy - HH:mm:ss");
		String DataHora = LocalDateTime.now().format(DataHoraExtrato);

		historicoReposicoes.add(DataHora + "| Reposição: " + quantidade + " notas de R$ " + cedula);

		return sb.toString();
	}

	public String sacar(Integer valor) {

		// Primeiro valida se o valor é nulo ou inválido
		if (valor == null || valor <= 0) {
			return "Valor inválido!";
		}

		// Verifica se o caixa já está bloqueado ou abaixo da cota mínima
		if (!caixaAtivo && estaAbaixoDaCotaMinima()) {
			return "Caixa Vazio: Chame o Operador";
		}

		// Verifica se existe dinheiro suficiente no caixa
		if (valor > calcularTotalCaixa()) {
			return "Saldo insuficiente!";
		}

		// Guarda quanto ainda falta sacar
		int restante = valor;

		// Conta o total de cédulas que seriam emitidas
		int totalCedulas = 0;

		// Guarda quantas cédulas de cada valor serão usadas
		int[] cedulasUsadas = new int[cedulas.length];

		// Simula o saque usando as maiores notas primeiro
		for (int i = 0; i < cedulas.length; i++) {

			int valorCedula = cedulas[i][COLUNA_VALOR];
			int quantidadeDisponivel = cedulas[i][COLUNA_QUANTIDADE];

			int qtdNecessaria = restante / valorCedula;

			if (qtdNecessaria > quantidadeDisponivel) {
				qtdNecessaria = quantidadeDisponivel;
			}

			// Se for nota de 5 e o uso dela deixar resto 1,
			// reduz uma nota de 5 para permitir completar com notas de 2
			if (valorCedula == 5 && qtdNecessaria > 0) {
				int restoDepoisDaNota5 = restante - (qtdNecessaria * valorCedula);

				if (restoDepoisDaNota5 % 2 != 0) {
					qtdNecessaria--;
				}
			}

			cedulasUsadas[i] = qtdNecessaria;
			restante -= qtdNecessaria * valorCedula;
			totalCedulas += qtdNecessaria;
		}

		// Se não conseguiu formar o valor com as cédulas disponíveis
		if (restante != 0) {
			return "Saque não realizado por falta de cédulas";
		}

		// Se passou de 30 cédulas, não pode sacar
		if (totalCedulas > 30) {
			return "Saque não realizado. Limite máximo de 30 cédulas excedido.";
		}

		// Verifica se depois do saque o caixa ficaria abaixo da cota mínima
		if (calcularTotalCaixa() - valor < cotaMinima) {
			caixaAtivo = false;
			return "Caixa Vazio: Chame o Operador";
		}

		// Só agora atualiza a matriz, porque o saque foi aprovado
		for (int i = 0; i < cedulas.length; i++) {
			cedulas[i][COLUNA_QUANTIDADE] -= cedulasUsadas[i];
		}

		// Monta a mensagem do saque
		String resultado = "Saque realizado com sucesso!\n\nCédulas emitidas:\n";

		for (int i = 0; i < cedulas.length; i++) {
			if (cedulasUsadas[i] > 0) {
				resultado += "R$ " + cedulas[i][COLUNA_VALOR] + ": " + cedulasUsadas[i] + " cédula(s)\n";
			}
		}

		// Registra o saque no histórico com data e hora
		DateTimeFormatter DataHoraExtrato = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");

		String DataHora = LocalDateTime.now().format(DataHoraExtrato);

		historicoSaques.add(DataHora + " | -R$ " + valor);

		return resultado;
	}

	//logica de armazenar a cota minima para saque e criar um //mensagem(resposta)ao usuario	
	public String armazenaCotaMinima(Integer minimo) {

		if (minimo == null || minimo < 0) {
			return "Valor de cota mínima inválido.";
		}

		cotaMinima = minimo;

		if (estaAbaixoDaCotaMinima()) {
			caixaAtivo = false;
			return "Cota mínima armazenada com sucesso: R$ " + cotaMinima + "\nCaixa Vazio: Chame o Operador";
		}

		caixaAtivo = true;

		return "Cota mínima armazenada com sucesso: R$ " + cotaMinima;
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

	// monta o histórico em texto
	public String montarHistorico() {
		StringBuilder sb = new StringBuilder();

		sb.append("==== EXTRATO DO CAIXA ====\n\n");

		sb.append("========= SAQUES =========\n");
		for (String saque : historicoSaques) {
			sb.append(saque).append("\n");
		}

		sb.append("\n========= REPOSIÇÃO =========\n");
		for (String reposicao : historicoReposicoes) {
			sb.append(reposicao).append("\n");
		}

		sb.append("\nSaldo atualizado: ");
		sb.append(pegaValorTotalDisponivel());

		return sb.toString();
	}
	
	public static void main(String[] args) {
		ICaixaEletronico caixa = new CaixaEletronico();
		GUI janela = new GUI(caixa);
		janela.show();
	}
}