package caixaeletronico;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CaixaEletronico implements ICaixaEletronico {

	// Constantes para facilitar o acesso às colunas da matriz
	private static final int COLUNA_VALOR = 0;
	private static final int COLUNA_QUANTIDADE = 1;

	// Lista que armazena os saques e as reposições de cédulas feitas no caixa
	private ArrayList<String> historicoSaques = new ArrayList<>();
	private ArrayList<String> historicoReposicoes = new ArrayList<>();

	// Matriz 6x1 que guarda o valor da cédula e a quantidade disponível
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
		// Primeiro valida se o valor é nulo ou igual a 0
		if (valor == null || valor <= 0) {
			return "Valor inválido!";
		}
		// Se o caixa estiver bloqueado ou abaixo da cota mínima, não permite saque
		if (!caixaAtivo || estaAbaixoDaCotaMinima()) {
			caixaAtivo = false;
			return "Caixa Vazio: Chame o Operador";
		}
		// Verifica se o valor solicitado é maior que o total disponível
		if (valor > calcularTotalCaixa()) {
			return "Saldo insuficiente!";
		}
		// Conta quantas cédulas serão usadas no saque
		int totalCedulas = 0;
		// Guarda o valor que ainda falta completar no saque
		int restante = valor;
		// Vetor auxiliar para guardar a quantidade usada de cada cédula
		int[] cedulasUsadas = new int[cedulas.length];
		// Percorre a matriz de cédulas, começando pelas maiores notas
		for (int i = 0; i < cedulas.length; i++) {

			int valorCedula = cedulas[i][COLUNA_VALOR];
			int quantidadeDisponivel = cedulas[i][COLUNA_QUANTIDADE];
			int qtdNecessaria = restante / valorCedula;

			if (qtdNecessaria > quantidadeDisponivel) {
				qtdNecessaria = quantidadeDisponivel;
			}
			
			// Evita deixar resto 1 ou 3, pois pode impedir a conclusão do saque
			while (qtdNecessaria > 0) {
			    int restoDepoisDaCedula = restante - (qtdNecessaria * valorCedula);

			    if (restoDepoisDaCedula == 1 || restoDepoisDaCedula == 3) {
			        qtdNecessaria--;
			    } else {
			        break;
			    }
			}
			// Ajusta o uso da nota de R$ 5 para não deixar resto impossível
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
		// Se ainda sobrou valor, não foi possível formar o saque
		if (restante != 0) {
			return "Saque não realizado por falta de cédulas";
		}
		// Impede saques com mais de 30 cédulas
		if (totalCedulas > 30) {
			return "Saque não realizado. Limite máximo de 30 cédulas excedido.";
		}
		// Se após o saque o caixa ficar abaixo da cota mínima, ele será bloqueado
		if (calcularTotalCaixa() - valor < cotaMinima) {
			caixaAtivo = false;
		}
		// Atualiza a matriz removendo as cédulas usadas
		for (int i = 0; i < cedulas.length; i++) {
			cedulas[i][COLUNA_QUANTIDADE] -= cedulasUsadas[i];
		}
		// Monta a mensagem de retorno informando as cédulas emitidas
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
		// Verifica se a cota definida é nulla ou menor que 0 
		if (minimo == null || minimo < 0) {
			return "Valor de cota mínima inválido.";
		}
		// Verifica se a cota minima que está sendo definida é maior que o valor total disponivel no caixa 
		if (minimo > calcularTotalCaixa()) {
			return "A cota mínima do caixa não pode ser definida, o valor é maior que o total disponível.";
		}
		cotaMinima = minimo;
		caixaAtivo = true;
		
		DecimalFormat formatacao = new DecimalFormat("#,###");
		return "Cota mínima armazenada com sucesso: R$ " + formatacao.format(cotaMinima);
	}
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