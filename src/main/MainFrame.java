package main;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;

import utility.DbFunc;

import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPasswordField;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtServer;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JTextField txtUser;
	private JLabel lblPass;
	private JButton btnConDisconnect;
	private JComboBox cmbSchemas;
	private JButton btnTables;
	private JButton btnRun;
	private JPanel panel;
	private JTextArea txtSqlEditor;
	private JTextField txtFilter;
	private JButton btnFilter;
	private DbFunc dbc;
	private Connection conn;
	private JScrollPane scrollPane;
	private JTable table;
	private JScrollPane scrollPane_1;
	private String[] tableColumns;
	private int tableColumnsSize = 0;
	private JPasswordField txtPassword;
	private TableRowSorter<TableModel> sorter;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setResizable(false);
		setTitle("MySQL Lookup Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1042, 527);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtServer = new JTextField();
		txtServer.setBounds(65, 12, 119, 26);
		contentPane.add(txtServer);
		txtServer.setColumns(10);
		
		lblNewLabel = new JLabel("Server");
		lblNewLabel.setBounds(22, 17, 61, 16);
		contentPane.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("User");
		lblNewLabel_1.setBounds(196, 17, 61, 16);
		contentPane.add(lblNewLabel_1);
		
		txtUser = new JTextField();
		txtUser.setBounds(230, 12, 119, 26);
		contentPane.add(txtUser);
		txtUser.setColumns(10);
		
		lblPass = new JLabel("Password");
		lblPass.setBounds(361, 17, 61, 16);
		contentPane.add(lblPass);
		
		btnConDisconnect = new JButton("Connect");
		btnConDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btnDisconnect_actionPerformed(e);
			}
		});
		btnConDisconnect.setBounds(553, 12, 101, 29);
		contentPane.add(btnConDisconnect);
		
		cmbSchemas = new JComboBox();
		cmbSchemas.setBounds(658, 13, 156, 27);
		
		cmbSchemas.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e){
                //System.out.println(e.getItem() + " " + e.getStateChange() );
            }
        });
		contentPane.add(cmbSchemas);
		
		btnTables = new JButton("Tables");
		btnTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btnTables_actionPerformed(e);
			}
		});
		btnTables.setBounds(826, 12, 93, 29);
		contentPane.add(btnTables);
		
		btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btnRun_actionPerformed(e);
			}
		});
		btnRun.setBounds(931, 12, 85, 29);
		contentPane.add(btnRun);
		
		panel = new JPanel();
		panel.setBounds(1, 50, 1040, 180);
		contentPane.add(panel);
		panel.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(2, 6, 1035, 169);
		panel.add(scrollPane);
		
		txtSqlEditor = new JTextArea();
		scrollPane.setViewportView(txtSqlEditor);
		
		txtFilter = new JTextField();
		txtFilter.setBounds(374, 473, 231, 26);
		contentPane.add(txtFilter);
		txtFilter.setColumns(10);
		
		btnFilter = new JButton("Filter");
		btnFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btnFilter_actionPerformed(e);
			}
		});
		btnFilter.setBounds(617, 473, 93, 29);
		contentPane.add(btnFilter);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(3, 227, 1035, 244);
		contentPane.add(scrollPane_1);
		
		table = new JTable();
		table.setEnabled(false);
		scrollPane_1.setViewportView(table);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(425, 12, 119, 26);
		contentPane.add(txtPassword);
		
		startup_main();

	}
	
	
	public void startup_main() {
		txtServer.setText("localhost");
		txtUser.setText("root");
		txtPassword.setText("");
		btnRun.setEnabled(false);
		btnTables.setEnabled(false);
		cmbSchemas.setEnabled(false);
	}
	
	public void cleanup_disconnect() {
		txtServer.setText("");
		txtUser.setText("");
		txtPassword.setText("");
		txtFilter.setText("");
		txtSqlEditor.setText(null);
		cmbSchemas.removeAllItems();
		btnRun.setEnabled(false);
		btnTables.setEnabled(false);
		cmbSchemas.setEnabled(false);
		txtServer.setEnabled(true);
		txtUser.setEnabled(true);
		txtPassword.setEnabled(true);
		((DefaultTableModel)table.getModel()).setRowCount(0);
		((DefaultTableModel)table.getModel()).setColumnCount(0);
	}
	
	public void startup_connect() {
		btnConDisconnect.setText("Disconnect");
		btnRun.setEnabled(true);
		btnTables.setEnabled(true);
		cmbSchemas.setEnabled(true);
		txtServer.setEnabled(false);
		txtUser.setEnabled(false);
		txtPassword.setEnabled(false);
	}
	
	public void getDBSchemas(Connection conn) {
		try {
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet rs = dbmd.getCatalogs();
			
			while (rs.next()) {
				cmbSchemas.addItem(rs.getString(1));
			}
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,e.getMessage());
		}
	}
	protected void do_btnDisconnect_actionPerformed(ActionEvent e) {
		
		if(btnConDisconnect.getText().equals("Disconnect")) {
			// Disconnect DB connection
			disconnectDBConn();
			JOptionPane.showMessageDialog(this,"Database connection has been terminated!");
			cleanup_disconnect();
		}else {
			// Start DB connection
			startDBConnWithoutSchema();
			
			if(conn!= null) {
				JOptionPane.showMessageDialog(this,"Database connection has been established!");

				// Get DB schemas and set them to combobox
				getDBSchemas(conn);
				
				// Change GUI components accordingly
				startup_connect();
			}else {
				JOptionPane.showMessageDialog(this,"Your connection attempt failed for user '"+txtUser.getText()+"' to the MySQL server at "+txtServer.getText()+":3306:\n" + 
						"  Access denied for user 'root'@'"+txtServer.getText()+"'");
			}
		}
	}
	
	
	public void startDBConnWithoutSchema() {
		dbc = new DbFunc();
		dbc.setUserName(txtUser.getText());
		dbc.setServer(txtServer.getText());
		dbc.setPassword(String.valueOf(txtPassword.getPassword()));
		conn = dbc.getConnection();
	}
	
	public void startDBConnWithinSchema() {
		dbc = new DbFunc();
		dbc.setUserName(txtUser.getText());
		dbc.setServer(txtServer.getText());
		dbc.setPassword(String.valueOf(txtPassword.getPassword()));
		dbc.setDatabase(cmbSchemas.getSelectedItem().toString());
		conn = dbc.getConnectionBySchema();
	}
	
	protected void do_btnTables_actionPerformed(ActionEvent e) {
		disconnectDBConn();
		startDBConnWithinSchema();
		
		btnConDisconnect.setText("Disconnect");
		btnRun.setEnabled(true);
		btnTables.setEnabled(true);
		cmbSchemas.setEnabled(true);

		getAllTableNames(cmbSchemas.getSelectedItem().toString());
		
	}
	
	public void disconnectDBConn() {
		if(conn != null) {
			try {
				conn.close();
				btnConDisconnect.setText("Connect");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this,e1.getMessage());
			}
		}
	}
	
	public void getAllTableNames(String schema_name) {
		
		txtSqlEditor.setText(null);
		String query = "select table_name from information_schema.tables where TABLE_SCHEMA=?"; 
		
		try (PreparedStatement		psmt = conn.prepareStatement(query);
				){
			psmt.setString(1, schema_name);
			ResultSet rs = psmt.executeQuery();
			
			if(!rs.isBeforeFirst()) {
				JOptionPane.showMessageDialog(this,"There is no table on this schema!");

			}else {
				while(rs.next() != false) {
					txtSqlEditor.append("select * from " + rs.getString("table_name") + ";\n");
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,e.getMessage());
		}

	}
	
	public void run(String query) {
		if(query.toLowerCase().contains("select")){	
			String tableName = getTableNameFromSQL(query);
			getTableColumns(tableName,cmbSchemas.getSelectedItem().toString());
			fillTable(table,query);			
		}else if (query.toLowerCase().contains("insert")){
			executeQuery(query,"insert");
		} else if(query.toLowerCase().contains("delete")) {
			executeQuery(query,"delet");
		} else if(query.toLowerCase().contains("update")) {
			executeQuery(query,"updat");
		}
	}
	
	public String getTableNameFromSQL(String query) {
		String word = "from";
		int index_1 = query.toLowerCase().indexOf("from") + word.length() + 1;
		int index_2 = query.length()-2;
		
		return query.substring(index_1, index_2);
	}
	
	public void executeQuery(String query, String operation) {
		try {
			PreparedStatement psmt;
			psmt = conn.prepareStatement(query);
			psmt.executeUpdate();
			
			JOptionPane.showMessageDialog(this,"Record "+operation+"ed successfully");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,e.getMessage());
		}
	}
	
	public void getTableColumns(String tableName, String schemaName) {
		
    	String query = "SELECT column_name FROM information_schema.columns WHERE table_name=? and table_schema=?"; 

        try (PreparedStatement psmt = conn.prepareStatement(query);
        		){
        	        	
        	List<String> colNames = new ArrayList<>();        	
    		
			psmt.setString(1, tableName);
			psmt.setString(2, schemaName);
			
			ResultSet rs = psmt.executeQuery();
			
			if(!rs.isBeforeFirst()) {
				// No table column
			}else {
				while(rs.next() != false) {
					colNames.add(rs.getString("column_name"));
				}
			}
			
			tableColumnsSize = colNames.size();
			tableColumns = new String[colNames.size()];
			
			for (int i = 0; i < colNames.size(); i++) {
				tableColumns[i] = colNames.get(i);
			}
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,e.getMessage());
		}
	}
	

	public void fillTable(JTable table, String Query)
	{
	    try (Statement stat = conn.createStatement();
	        ResultSet rs = stat.executeQuery(Query);
	    	)
	    {		      
	        //To remove previously added rows
	        while(table.getRowCount() > 0){
	            ((DefaultTableModel) table.getModel()).removeRow(0);
	        }
	        int columns = rs.getMetaData().getColumnCount();
	        	        
			DefaultTableModel model = new DefaultTableModel(tableColumns, tableColumnsSize); 
			table.setModel(model);
			table.setRowSorter(null);

	        while(rs.next()){  
	            Object[] row = new Object[columns];
	            for (int i = 1; i <= columns; i++){  
	                row[i - 1] = rs.getObject(i);
	            }
	            ((DefaultTableModel) table.getModel()).insertRow(rs.getRow()-1,row);
	        }
	    }
	    catch(SQLException e){
	    	e.printStackTrace();
			JOptionPane.showMessageDialog(this,e.getMessage());
	    }
	}
	
	protected void do_btnRun_actionPerformed(ActionEvent e) {		
		run(getSelectedQueryFromEditor());
	}
	
	public String getSelectedQueryFromEditor() {
		
		String sql = "";
		try {	
			int line = txtSqlEditor.getLineOfOffset( txtSqlEditor.getCaretPosition() );
			int start = txtSqlEditor.getLineStartOffset( line );
			int end = txtSqlEditor.getLineEndOffset( line );
			sql = txtSqlEditor.getDocument().getText(start, end - start);
			
			return sql;
			
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this,e1.getMessage());
		}
		
		return sql;
	}
	protected void do_btnFilter_actionPerformed(ActionEvent e) {
		
		sorter =  new TableRowSorter<TableModel>(((DefaultTableModel)table.getModel()));
	    table.setRowSorter(sorter);
	    
	    String text = txtFilter.getText();
        if (text.length() == 0) {
          sorter.setRowFilter(null);
        } else {
          sorter.setRowFilter(RowFilter.regexFilter(text));
        }
	}
}
