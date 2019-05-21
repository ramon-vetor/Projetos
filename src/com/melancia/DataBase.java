package com.melancia;

import android.annotation.SuppressLint;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
	private String nomeServidorBD = "";
	private String nomeBD = "";
	private String usuarioBD = "";
	private String senhaDB = "";
	private int portaDB = 5432;
	
	private int cdUsu;
	
	private Connection conexaoDB;
	private Statement statement;
	
	public DataBase(){
		
	}
	
	public boolean abreConexao() {
		String errMsg = "Falha ao abrir conexão com o banco de dados.";

		try {
			String url = "jdbc:postgresql://" + nomeServidorBD + ":";
			url += String.valueOf(portaDB) + "/";
			url += nomeBD;
			url += "?charSet=SQL_ASCII";

			Class.forName("org.postgresql.Driver");
			DriverManager.setLoginTimeout(10);
			
			conexaoDB = DriverManager.getConnection(url, usuarioBD, senhaDB);
			
			if(conexaoDB != null){
				conexaoDB.setAutoCommit(false);
				return true;
			}else{
				return false;
			}
			
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(errMsg + e.getMessage());
		} catch (SQLException e) {
			throw new RuntimeException(errMsg + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(errMsg + e.getMessage());
		}
	}

	public ResultSet retReg(final String sql){

		try {
			
			ResultSet resultSet = null;
			
			if(this.getConnection().isClosed()){
				throw new Exception("A conexão com o banco de dados foi encerrada.");
			}

			statement = conexaoDB.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			/*
			resultSet = new AsyncTask<Void, Void, ResultSet>() {
				String erro = "";
				
				@Override
				protected ResultSet doInBackground(Void... params) {
					try {
						return statement.executeQuery(sql);
					} catch (Exception e) {
						erro = e.getMessage();
						return null;
					}
				}
				
				protected void onPostExecute(ResultSet result) {
					if(!erro.equals("")){
						throw new RuntimeException(erro);
					}
				};
				
			}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
			*/
			
			resultSet = statement.executeQuery(sql);
			if (resultSet != null){
				if(resultSet.next()){
					resultSet.previous();
					return resultSet;
				}else{
					return null;
				}
			}else{
				return null;
			}
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao buscar registro." + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Erro ao buscar registro." + e.getMessage());
		}
	}
	
	public void commit() {
		try {
			this.getConnection().commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void rollback(){
		try {
			this.getConnection().rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public String getNomeServidorBD() {
		return nomeServidorBD;
	}
	public void setNomeServidorBD(String nomeServidorBD) {
		this.nomeServidorBD = nomeServidorBD;
	}
	public String getNomeBD() {
		return nomeBD;
	}
	public void setNomeBD(String nomeBD) {
		this.nomeBD = nomeBD;
	}
	public String getUsuarioBD() {
		return usuarioBD;
	}
	public void setUsuarioBD(String usuarioBD) {
		this.usuarioBD = usuarioBD;
	}
	public String getSenhaDB() {
		return senhaDB;
	}
	public void setSenhaDB(String senhaDB) {
		this.senhaDB = senhaDB;
	}
	public int getPortaDB() {
		return portaDB;
	}
	public void setPortaDB(int portaDB) {
		this.portaDB = portaDB;
	}
	public Connection getConnection(){
		return conexaoDB;
	}
	public void setConnection(Connection conexao){
		conexaoDB = conexao;
	}
	public int getCdUsu() {
		return cdUsu;
	}
	public void setCdUsu(int cdUsu) {
		this.cdUsu = cdUsu;
	}

	public void fechaConexao() {
		if(this.getConnection() != null){
			if(this.conexaoAberta()){
				try {
					if(this.statement != null) statement.close();
					if(!this.getConnection().isClosed()){
						this.getConnection().close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean conexaoAberta() {
		if(this.getConnection() != null){
			try {
				if(!this.getConnection().isClosed()){
					return true;
				}
				
				return false;
			} catch (SQLException e) {
				return false;
			}
		}else{
			return false;
		}
	}

	public boolean executa(final String sql) {
		try {
			if(!conexaoAberta()) throw new Exception("Conexão com o banco de dados encerrada.");
			statement = conexaoDB.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			statement.execute(sql);
			statement.close();
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@SuppressLint("DefaultLocale")
	public boolean gravaLog(String tabelas, String comando) {
		try{
			String sql = "insert into logdados(chave, dados, usuario, tabela)"
					+ " values (nextval('dadosseq')," 
					+ "'" + comando.replace("'", "''") + "',"
					+ cdUsu + ","
					+ "'" + tabelas.toUpperCase() + "')";
			
			return executa(sql);
		}catch(Exception e){
			throw new RuntimeException("Erro ao gravar log da operação." + e.getMessage());
		}
	}

	public boolean addReg(String tabela, String[] vetCampos, Object[] vetValores, boolean log) {
		
		try{
			
			if(getConnection().isClosed()){
				throw new RuntimeException("A conexão com o banco de dados foi encerrada.");
			}
			
			String sql = "insert into " + tabela + "(";
			
			for (int i = 0; i < vetCampos.length; i++) {
				sql += vetCampos[i];
				if(i != (vetCampos.length - 1)){
					sql += ", ";
				}else{
					sql += ") values (";
					for (int j = 0; j < vetValores.length; j++) {
						if(vetValores[j] == null){
							sql += "null";	
						}else{
							if(vetValores[j].getClass() == String.class){
								if(!vetValores[j].toString().equals("null")){
									sql += "'" + vetValores[j].toString().replaceAll("'", "") + "'";
								}else{
									sql += "null";
								}
							}else{
								sql += vetValores[j];
							}
						}
						
						if(j != (vetValores.length - 1)){
							sql += ", ";
						}else{
							sql += ")";
						}
					}
				}
			}
			
			if(executa(sql)){
				if(log){
					return gravaLog(tabela, sql);
				}else{
					return true;
				}
				
			}else{
				return false;
			}
			
		}catch(Exception e){
			throw new RuntimeException("Erro ao adicionar registro." + e.getMessage());
		}	
	}

	public boolean altReg(String tabela, String[] vetCampos, Object[] vetValores, String criterio, boolean log){
		
		try{
			
			if(getConnection().isClosed()){
				throw new RuntimeException("A conexão com o banco de dados foi encerrada.");
			}
			
			String sql = "update " + tabela + " set ";
			
			for (int i = 0; i < vetCampos.length; i++) {
				sql += vetCampos[i] + " = ";
				
				if(vetValores[i].getClass() == String.class){
					if(!vetValores[i].toString().equals("null")){
						sql += "'" + vetValores[i].toString().replaceAll("'", "") + "'";
					}else{
						sql += vetValores[i];
					}
				}else{
					sql += vetValores[i];
				}
				
				if(i != vetCampos.length - 1) sql += ", ";
			}
			
			sql += " where " + criterio;
			
			if(executa(sql)){
				if(log){
					return gravaLog(tabela, sql);
				}else{
					return true;
				}
			}else{
				return false;	
			}
			
		}catch(Exception e){
			throw new RuntimeException("Erro ao atualizar registro." + e.getMessage());
		}
	}
	
	public boolean exclReg(String tabela, String criterio, boolean log) {
		try{
			
			if(getConnection().isClosed()){
				throw new RuntimeException("A conexão com o banco de dados foi encerrada.");
			}
			
			String sql = "delete from " + tabela + " where " + criterio;
			
			if(executa(sql)){
				if(log){
					return gravaLog(tabela, sql);
				}else{
					return true;
				}
				
			}else{
				return false;
			}
			
		}catch(Exception e){
			throw new RuntimeException("Erro ao excluir registro." + e.getMessage());
		}

	}
}
