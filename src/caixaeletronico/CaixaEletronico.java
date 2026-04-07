package caixaeletronico;

public class CaixaEletronico implements ICaixaEletronico {
	
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

	public static void main(String arg[]) {
		GUI janela = new GUI(CaixaEletronico.class);
		janela.show();
	}
}
