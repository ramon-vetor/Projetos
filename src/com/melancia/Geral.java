package com.melancia;

import android.database.sqlite.SQLiteDatabase;

public abstract class Geral {
	
	public static class Configuracao {
		private int cdConexao = 0;
		private String nmConexao = "";
		private String nmDBLocal = "";
		private String serverDb = "";
		private String nomeDb = "";
		private String usuarioDb = "";
		private String senhaDb = "";
		private int portaDb = 5432;
		private String frmDtDb = "";
		private boolean utlAreaEntrg = false;
		private boolean ocultarPrcSug = false;
		private int tema;

		private int empPadrao = 0;
		private int unEmpPadrao = 0;
		private int usuPadrao = 0;
		private String senhaUsu = "";
		
		private String obsPadrao = "";

		public Configuracao() {

		}

		public String getServerDb() {
			return serverDb;
		}

		public void setServerDb(String serverDb) {
			this.serverDb = serverDb;
		}

		public String getNomeDb() {
			return nomeDb;
		}

		public void setNomeDb(String nomeDb) {
			this.nomeDb = nomeDb;
		}

		public String getUsuarioDb() {
			return usuarioDb;
		}

		public void setUsuarioDb(String usuarioDb) {
			this.usuarioDb = usuarioDb;
		}

		public String getSenhaDb() {
			return senhaDb;
		}

		public void setSenhaDb(String senhaDb) {
			this.senhaDb = senhaDb;
		}

		public int getPortaDb() {
			return portaDb;
		}

		public void setPortaDb(int portaDb) {
			this.portaDb = portaDb;
		}

		public String getFrmDtDb() {
			return frmDtDb;
		}

		public void setFrmDtDb(String frmDtDb) {
			this.frmDtDb = frmDtDb;
		}

		public boolean getUtlAreaEntrg() {
			return utlAreaEntrg;
		}

		public void setUtlAreaEntrg(boolean utlAreaEntrg) {
			this.utlAreaEntrg = utlAreaEntrg;
		}

		public boolean getOcultarPrcSug() {
			return ocultarPrcSug;
		}

		public void setOcultarPrcSug(boolean ocultarPrcSug) {
			this.ocultarPrcSug = ocultarPrcSug;
		}

		public int getTema() {
			return tema;
		}

		public void setTema(int tema) {
			this.tema = tema;
		}

		public int getEmpPadrao() {
			return empPadrao;
		}

		public void setEmpPadrao(int empPadrao) {
			this.empPadrao = empPadrao;
		}

		public int getUnEmpPadrao() {
			return unEmpPadrao;
		}

		public void setUnEmpPadrao(int unEmpPadrao) {
			this.unEmpPadrao = unEmpPadrao;
		}

		public int getUsuPadrao() {
			return usuPadrao;
		}

		public void setUsuPadrao(int usuPadrao) {
			this.usuPadrao = usuPadrao;
		}

		public String getSenhaUsu() {
			return senhaUsu;
		}

		public void setSenhaUsu(String senhaUsu) {
			this.senhaUsu = senhaUsu;
		}

		public void validaParametros() {
			if (serverDb.equals("")) {
				throw new RuntimeException("Nome do servidor do banco de dados não configurado.");
			} else {
				if (nomeDb.equals("")) {
					throw new RuntimeException("Nome do banco de dados não configurado.");
				} else {
					if (usuarioDb.equals("")) {
						throw new RuntimeException("Nome do usuário do banco de dados não configurado.");
					} else {
						if (senhaDb.equals("Senha do banco de dados não configurada.")) {
							throw new RuntimeException("");
						} else {
							if (portaDb <= 0) {
								throw new RuntimeException("Porta de conexão do banco de dados não configurada.");
							}
						}
					}
				}
			}
		}

		public int getCdConexao() {
			return cdConexao;
		}

		public void setCdConexao(int cdConexao) {
			this.cdConexao = cdConexao;
		}

		public String getNmConexao() {
			return nmConexao;
		}

		public void setNmConexao(String nmConexao) {
			this.nmConexao = nmConexao;
		}

		public String getNmDBLocal() {
			return nmDBLocal;
		}

		public void setNmDBLocal(String nmDBLocal) {
			this.nmDBLocal = nmDBLocal;
		}

		public String getObsPadrao() {
			return obsPadrao;
		}

		public void setObsPadrao(String obsPadrao) {
			this.obsPadrao = obsPadrao;
		}

	}
	
	public static byte ANTERIOR;
	public static final byte INCLUINDO = 0;
	public static final byte EDITANDO = 1;
	public static final byte VISUALIZANDO = 3;
	
	public static byte cdEmpresa;
	public static byte terminal;
	public static String nmTerminal;
	public static byte cdUsuario;
	public static boolean usuarioAdm = false;
	
	public static SQLiteDatabase localDB;
	public static DataBase dbCn;
	public static Configuracao config;
	
	public static void atualizaDB() {
		
		String sql = "CREATE TABLE [ctrl_ban] (" +
				"[cd_emp] SMALLINT NOT NULL CONSTRAINT [ctrl_ban_pub_emp_fk] REFERENCES [pub_emp]([cd_emp]) ON DELETE NO ACTION ON UPDATE CASCADE," +
				"[cd_lanc] BIGINT NOT NULL," +
				"[dt_lanc] DATE NOT NULL," +
				"[tp_lanc] SMALLINT," +
				"[st_lanc] SMALLINT," +
				"[cd_pes] BIGINT," +
				"[nm_pes] VARCHAR(100)," +
				"[vl_lanc] NUMERIC(10, 2) NOT NULL DEFAULT 0," +
				"[obs_lanc] VARCHAR(200)," +
				"[cd_term] SMALLINT," +
				"[cd_usu] SMALLINT CONSTRAINT [ctrl_ban_pub_usu_fk] REFERENCES [pub_usu]([cd_usu]) ON DELETE NO ACTION ON UPDATE CASCADE," +
				"[cd_ver] SMALLINT NOT NULL DEFAULT 1," +
				"[st_reg] SMALLINT NOT NULL DEFAULT 0," +
				"CONSTRAINT [ctrl_ban_pub_pes_fk] FOREIGN KEY([cd_emp], [cd_pes]) REFERENCES [pub_pes]([cd_emp], [cd_pes]) ON DELETE NO ACTION ON UPDATE CASCADE," +
				"CONSTRAINT [ctrl_ban_pub_term_fk] FOREIGN KEY([cd_emp], [cd_term]) REFERENCES [pub_term]([cd_emp], [cd_term]) ON DELETE NO ACTION ON UPDATE CASCADE," +
				"CONSTRAINT [] PRIMARY KEY ([cd_emp], [cd_lanc]) ON CONFLICT ABORT);";	
		
		rodaScript(sql);
		
		sql = "ALTER TABLE [ctrl_ban] ADD COLUMN [km_ini] NUMERIC(10, 2) NOT NULL DEFAULT 0;";
		rodaScript(sql);
		
		sql = "ALTER TABLE [ctrl_ban] ADD COLUMN [km_fin] NUMERIC(10, 2) NOT NULL DEFAULT 0;";
		rodaScript(sql);
		
		sql = "ALTER TABLE [ctrl_ban] ADD COLUMN [qt_litros] NUMERIC(10, 2) NOT NULL DEFAULT 0;";  
		rodaScript(sql);
		
		sql = "ALTER TABLE [pub_usu] ADD COLUMN [usu_adm] SMALLINT NOT NULL DEFAULT 1;";  
		rodaScript(sql);

	}
	
	public static boolean rodaScript(String sql) {
		try {
			localDB.execSQL(sql);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
}
