package br.ucsal.sqltool;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class Janela extends JFrame {

	private Connection connection;

	public Janela(Connection connection) {
		super("SQL TOOL");
		this.connection = connection;
		setSize(800, 400);
		JLabel lbQuery = new JLabel("CONSULTA:");
		JTextField tfQuery = new JTextField(30);
		JButton btQuery = new JButton("EXECUTE");

		final JTextArea taResultado = new JTextArea();

		JPanel top = new JPanel();
		top.add(lbQuery);
		top.add(tfQuery);
		top.add(btQuery);

		this.add(top, BorderLayout.NORTH);

		this.add(taResultado);

		this.setVisible(true);

		btQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Statement statement;
				try {
					statement = Janela.this.connection.createStatement();

					ResultSet resultSet = statement.executeQuery("SELECT * FROM ALUNO");
					StringBuffer valor = new StringBuffer();
					valor.append("ID \t MATRICULA \t NOME  \n");
					while (resultSet.next()) {
						valor.append(resultSet.getInt(1)+" \t");
						valor.append(resultSet.getString(2)+" \t");
						valor.append(resultSet.getString(3)+" \n");
					}
					
					taResultado.setText(valor.toString());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	}

}
