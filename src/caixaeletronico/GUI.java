package caixaeletronico;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {

	private JFrame janela;
	private ICaixaEletronico caixa; // 1. Recebe a interface

	// 2. Construtor alterado para receber a lógica pronta
	public GUI(ICaixaEletronico caixa) {

		this.caixa = caixa;
		janela = new JFrame();

		janela.setTitle("Caixa eletronico");
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setSize(350, 400);
		janela.setLocationRelativeTo(null);
		janela.setLayout(null);

		// Módulo do cliente
		JLabel labelCliente = new JLabel("Modulo do Cliente:");
		labelCliente.setBounds(75, 0, 120, 30);
		janela.add(labelCliente);

		// Botão Efetuar Saque
		JButton sacar = new JButton("Efetuar Saque");
		sacar.setBounds(75, 30, 200, 30);
		janela.add(sacar);
		sacar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = JOptionPane.showInputDialog("Valor do saque:");
				if (input != null && !input.isEmpty()) {
					try {
						String resultado = caixa.sacar(Integer.parseInt(input));
						JOptionPane.showMessageDialog(null, resultado);
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, "Digite apenas números!");
					}
				}
			}
		});

		// Módulo do administrador
		JLabel labelModuloAdm = new JLabel("Modulo do Administrador:");
		labelModuloAdm.setBounds(75, 75, 180, 30);
		janela.add(labelModuloAdm);

		// Botão Relatório
		JButton pegaRelatorioCedulas = new JButton("Relátorio de Cedulas");
		pegaRelatorioCedulas.setBounds(75, 110, 200, 30);
		janela.add(pegaRelatorioCedulas);
		pegaRelatorioCedulas.addActionListener(e ->
				JOptionPane.showMessageDialog(null, caixa.pegaRelatorioCedulas())
		);

		// Botão Valor Total
		JButton pegaValorTotalDisponivel = new JButton("Valor total disponivel");
		pegaValorTotalDisponivel.setBounds(75, 150, 200, 30);
		janela.add(pegaValorTotalDisponivel);
		pegaValorTotalDisponivel.addActionListener(e ->
				JOptionPane.showMessageDialog(null, caixa.pegaValorTotalDisponivel())
		);

		// Botão Reposição
		JButton reposicaoCedulas = new JButton("Reposição de Cedulas");
		reposicaoCedulas.setBounds(75, 190, 200, 30);
		janela.add(reposicaoCedulas);
		reposicaoCedulas.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String cedulaStr = JOptionPane.showInputDialog("Valor da nota (Ex: 50):");
				String qtdStr = JOptionPane.showInputDialog("Quantidade de notas:");
				if (cedulaStr != null && qtdStr != null) {
					try {
						String msg = caixa.reposicaoCedulas(Integer.parseInt(cedulaStr), Integer.parseInt(qtdStr));
						JOptionPane.showMessageDialog(null, msg);
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, "Dados inválidos!");
					}
				}
			}
		});

		// Botão Cota Mínima
		JButton armazenaCotaMinima = new JButton("Cota Minima");
		armazenaCotaMinima.setBounds(75, 230, 200, 30);
		janela.add(armazenaCotaMinima);
		armazenaCotaMinima.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = JOptionPane.showInputDialog("Defina o valor da cota mínima:");
				if (input != null) {
					try {
						String msg = caixa.armazenaCotaMinima(Integer.parseInt(input));
						JOptionPane.showMessageDialog(null, msg);
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, "Valor inválido!");
					}
				}
			}
		});

		// Módulo de ambos
		JLabel labelModuloAmbos = new JLabel("Modulo de Ambos:");
		labelModuloAmbos.setBounds(75, 280, 150, 30);
		janela.add(labelModuloAmbos);

		// Botão Sair
		JButton btnSair = new JButton("Sair");
		btnSair.setBounds(75, 315, 200, 30);
		janela.add(btnSair);
		btnSair.addActionListener(e -> System.exit(0));
	}

	public void show() {
		janela.setVisible(true);
	}
}
