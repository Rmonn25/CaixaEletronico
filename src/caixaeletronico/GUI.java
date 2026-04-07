package caixaeletronico;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
	
	private JFrame janela; // declaramos uma variável do tipo JFrame para guardar a janela
	

	public GUI(Class<?> classeCaixa) {
		
		janela = new JFrame();
		
		// define o título do nosso GUI
		janela.setTitle("Caixa eletronico"); 
		
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // faz o GUI fechar quando clicar no X da janela
		janela.setSize(350, 400); // definição de largura e altura da janela
		janela.setLocationRelativeTo(null);
		janela.setLayout(null);
		
		// subtitulo - Modulo do cliente
		JLabel labelCliente = new JLabel("Modulo do Cliente:");
		labelCliente.setBounds(75, 0, 120, 30);	
		janela.add(labelCliente);
		
		// Botão de efetuar o saque
		JButton sacar = new JButton("Efetuar Saque");
		sacar.setBounds(75, 30, 200, 30);
		janela.add(sacar);
		sacar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub	
			}	
		});
		
		// subtitulo - Modulo do adminsitrador
		JLabel labelModuloAdm = new JLabel("Modulo do Administrador:");
		labelModuloAdm.setBounds(75, 75, 150, 30);	
		janela.add(labelModuloAdm);
		
		//botao para mostrar relatorio de cedulas no caixa eletronico
		JButton pegaRelatorioCedulas = new JButton("Relátorio de Cedulas");
		pegaRelatorioCedulas.setBounds(75, 110, 200, 30);
		janela.add(pegaRelatorioCedulas);
		pegaRelatorioCedulas.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub	
			}
		});
		
		// mostra o valor total disponivel do caixa eletronico
		JButton pegaValorTotalDisponivel = new JButton("Valor total disponivel");
		pegaValorTotalDisponivel.setBounds(75, 150, 200, 30);
		janela.add(pegaValorTotalDisponivel);
		pegaValorTotalDisponivel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub	
			}
		});
		
		// Botão para repor as cedulas do caixa eletronica
		JButton reposicaoCedulas = new JButton("Reposição de Cedulas");
		reposicaoCedulas.setBounds(75, 190, 200, 30);
		janela.add(reposicaoCedulas);
		reposicaoCedulas.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		// Define uma cota minima para saque
		JButton armazenaCotaMinima = new JButton("Cota Minima");
		armazenaCotaMinima.setBounds(75, 230, 200, 30);
		janela.add(armazenaCotaMinima);
		armazenaCotaMinima.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		// subtitulo - Modulo do ambos
		JLabel labelModuloAmbos = new JLabel("Modulo de Ambos:");
		labelModuloAmbos.setBounds(75, 280, 150, 30);	
		janela.add(labelModuloAmbos);
		
		// Botão para sair do programa
		JButton btnSair = new JButton("Sair");
		btnSair.setBounds(75, 315, 200, 30);
		janela.add(btnSair);
		btnSair.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
	
	public void show() {
		janela.setVisible(true);
	}	
}
