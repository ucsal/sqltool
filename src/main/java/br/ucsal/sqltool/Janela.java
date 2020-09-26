package br.ucsal.sqltool;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class Janela extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection connection;
	
	private final JTextField tfQuery = new JTextField(30);
	private final JLabel lbQuery = new JLabel("CONSULTA:");
	private final JButton btQuery = new JButton("EXECUTE");
	private final JTextArea taResultado = new JTextArea();
	private JPanel panel = new JPanel();
	private final JScrollPane resultado = new JScrollPane();
	

	public Janela(Connection connection) {
		super("SQL TOOL");
		this.connection = connection;
		setSize(800, 400);
		JPanel top = new JPanel();
		top.add(lbQuery);
		top.add(tfQuery);
		top.add(btQuery);
		this.add(top, BorderLayout.NORTH);
		//this.add(resultado);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setVisible(true);
		btQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executarSQL();
			}
		});
		
		

	}
	
	private void exibirTabela(List<String> header, List<List<String>> dados) {

		Object[][] array = new Object[dados.size()][];
		
		for (int i = 0; i < array.length; i++) {
			array[i] = dados.get(i).toArray();
		}
		
		JTable tabelaJTable = new JTable(array, header.toArray());
		tabelaJTable.getTableHeader().setVisible(true);
		JScrollPane resultado = new JScrollPane(tabelaJTable);
		this.add(resultado);
		tabelaJTable.updateUI();
		resultado.updateUI();
		this.getRootPane().updateUI();

	}
	
	
	private void exibirTextField(List<String> header, List<List<String>> dados) {

		JPanel interno = new JPanel(new GridLayout(header.size(),dados.size()));
		for (String string : header) {
			JLabel field = new JLabel(string);
			interno.add(field);
		}
		
		for (List<String> list : dados) {
			for (String string : list) {
				JTextField field = new JTextField(string.length());
				field.setText(string);
				field.setEditable(false);
				interno.add(field);
			}
		}
		panel.add(interno);
		this.add(resultado);
		resultado.updateUI();
		
	}
	
	private void exibirTexto(List<String> header, List<List<String>> dados) {
		StringBuilder sb = new StringBuilder();
		for (String string : header) {
			sb.append(string);
			sb.append(" \t");
		}
		sb.append("\n");
		for (List<String> list : dados) {
			for (String string : list) {
				sb.append(string);
				sb.append(" \t");
			}
			sb.append("\n");
		}
		taResultado.setText(sb.toString());
		System.out.println(sb.toString());
//		resultado.add(taResultado);
//		resultado.updateUI();

	}
	
	
	private void executarSQL() {
		Statement statement;
		try {
			
			statement = this.connection.createStatement();
			ResultSet resultSet = statement.executeQuery(tfQuery.getText());
			
			ResultSetMetaData metaData = resultSet.getMetaData();
			List<String> header = new ArrayList<String>();
			int qtdColunas = metaData.getColumnCount();
			for(int i =1 ; i <= qtdColunas ; i++) {
				String nome = metaData.getColumnName(i);
				header.add(nome);
			}
			
			List<List<String>> dados = new ArrayList<List<String>>();
			while (resultSet.next()) {
				List<String> tupla = new ArrayList<String>();
				for(int i =1 ; i <= qtdColunas ; i++) {
					tupla.add(resultSet.getString(i));
				}
				dados.add(tupla);
			}
			exibirTabela(header,dados);
			//exibirTexto(header,dados);
			//exibirTextField(header,dados);
		} catch (SQLException e1) {
			taResultado.setText(e1.getCause().toString());
//			resultado.add(taResultado);
//			resultado.updateUI();
		}
	}
	

}
