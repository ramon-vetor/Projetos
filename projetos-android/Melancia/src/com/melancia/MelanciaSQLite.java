package com.melancia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MelanciaSQLite extends SQLiteOpenHelper { 
	public MelanciaSQLite(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = 
			"CREATE TABLE [pub_emp] (" +
			" [cd_emp] INTEGER NOT NULL ON CONFLICT ABORT PRIMARY KEY ON CONFLICT ABORT," + 
			" [nm_emp] CHAR(20) NOT NULL);";
		
		db.execSQL(sql);
			
		sql = " CREATE TABLE [pub_pes] (" +
			" [cd_emp] INTEGER NOT NULL CONSTRAINT [pub_pes_pub_emp_fk] REFERENCES [pub_emp]([cd_emp]) ON DELETE NO ACTION ON UPDATE CASCADE," + 
			" [cd_pes] SMALLINT NOT NULL, " +
			" [nm_razao_pes] CHAR(50), " +
			" [nm_fants_pes] CHAR(50), " +
			" [email_pes] CHAR(50), " +
			" [endereco_pes] CHAR(100), " +
			" [numero_end] SMALLINT, " +
			" [compl_end] CHAR(50), " +
			" [cep_end] CHAR(10), " +
			" [bairro_end] CHAR(30), " +
			" [cd_cid_end] INTEGER, " +
			" [cpf_cgc_pes] CHAR(20), " +
			" [fone_pes] CHAR(15), " +
			" [placa_veic] CHAR(10), " +
			" [uf_placa] CHAR(2), " +
			" [cd_cid] INTEGER, " +
			" [tp_pes_prdt] SMALLIN," + 
			" [tp_pes_dest] SMALLINT, " +
			" [tp_pes_carg] SMALLINT, " +
			" [tp_pes_motr] SMALLINT, " +
			" [tp_pes_emit] SMALLIN," + 
			" [cd_term] SMALLINT NOT NULL, " +
			" [st_reg] SMALLINT, " +
			" [st_pes] SMALLINT, " +
			" [cd_ver] SMALLINT, " +
			" CONSTRAINT [] PRIMARY KEY ([cd_emp], [cd_pes]) ON CONFLICT ABORT);";
			
		db.execSQL(sql);
				
		sql = "CREATE TABLE [pub_prod] (" +
			" [cd_emp] SMALLINT NOT NULL," + 
			" [cd_prod] SMALLINT NOT NULL," + 
			" [ds_prod] CHAR(50) NOT NULL," + 
			" CONSTRAINT [] PRIMARY KEY ([cd_emp], [cd_prod]));";
			
		db.execSQL(sql);
				
		sql = "CREATE TABLE [ctrl_ent] (" +
			" [cd_emp] SMALLINT NOT NULL," + 
			" [cd_ent] INTEGER NOT NULL," + 
			" [cd_pes_prdt] INTEGER," + 
			" [dt_ent] DATE," + 
			" [cd_prod] SMALLINT NOT NULL, " + 
			" [peso_bruto_prod] NUMERIC(10, 2) NOT NULL DEFAULT 0, " +
			" [perc_palha] NUMERIC(8, 2) NOT NULL DEFAULT 0," + 
			" [peso_descarte] NUMERIC(10, 2) NOT NULL DEFAULT 0," + 
			" [preco_comp] NUMERIC(10, 2) NOT NULL DEFAULT 0," + 
			" [preco_vend] NUMERIC(10, 2) NOT NULL DEFAULT 0," + 
			" [placa_frete] CHAR(10) NOT NULL DEFAULT '   -     '," + 
			" [uf_placa_frete] SMALLINT," + 
			" [cd_cid_placa] INTEGER," + 
			" [vl_frete_ton] NUMERIC(10, 2) NOT NULL DEFAULT 0," + 
			" [vl_icms_frete] NUMERIC(10, 2) NOT NULL DEFAULT 0," + 
			" [vl_mao_obra] NUMERIC(10, 2) NOT NULL DEFAULT 0," + 
			" [vl_out_desp] NUMERIC(10, 2) NOT NULL DEFAULT 0," + 
			" [cd_pes_dest] INTEGER NOT NULL," + 
			" [obs_ent] CHAR(150) NOT NULL DEFAULT ''," + 
			" [qtde_pecas] INTEGER NOT NULL DEFAULT 0," + 
			" [st_ent] SMALLINT NOT NULL," + 
			" [st_reg] SMALLINT NOT NULL DEFAULT 0," + 
			" [vl_tot_prdt] NUMERIC(10, 2) NOT NULL DEFAULT 0," + 
			" [vl_carg] NUMERIC(10, 2) NOT NULL DEFAULT 0," + 
			" [ver_reg] SMALLINT NOT NULL DEFAULT 0," + 
			" [peso_liq_prod] NUMERIC(10, 2) NOT NULL DEFAULT 0," + 
			" [vl_pago] NUMERIC(10, 2) DEFAULT 0," + 
			" [vl_recebido] NUMERIC(10, 2) NOT NULL DEFAULT 0," + 
			" [nm_motr] CHAR(50) NOT NULL DEFAULT ''," + 
			" [fone_motr] CHAR(20) NOT NULL DEFAULT ''," + 
			" [cpf_cgc_motr] CHAR(20) NOT NULL DEFAULT '', " +
			" [vl_to_frete] NUMERIC(10, 2) NOT NULL DEFAULT 0," + 
			" [vl_to_sai] NUMERIC(10, 2) NOT NULL DEFAULT 0," + 
			" [dt_sai] DATE," + 
			" [st_sai] SMALLINT NOT NULL DEFAULT 0," + 
			" [cd_pes_carg] INTEGER, " +
			" [obs_sai] CHAR(150) NOT NULL DEFAULT ''," + 
			" [cd_usu] SMALLINT, " +
			" [cd_term] SMALLINT NOT NULL, " +
			" [cd_ver] SMALLINT NOT NULL DEFAULT 0," +
			" [status_ent] SMALLINT NOT NULL DEFAULT 0," +
			" [dt_emissao] DATE," +
			" [tp_frete_sai] SMALLINT, " +
			" [tp_icms_frete] SMALLINT, " +
			" [emitir_nf] SMALLINT, " +
			" CONSTRAINT [ctrl_ent_pub_pes_carg_fk] FOREIGN KEY([cd_emp], [cd_pes_carg]) REFERENCES [pub_pes]([cd_emp], [cd_pes]) ON DELETE NO ACTION ON UPDATE CASCADE," + 
			" CONSTRAINT [ctrl_ent_pub_pes_dest_fk] FOREIGN KEY([cd_emp], [cd_pes_dest]) REFERENCES [pub_pes]([cd_emp], [cd_pes]) ON DELETE NO ACTION ON UPDATE CASCADE, " +
			" CONSTRAINT [ctrl_ent_pub_pes_prdt_fk] FOREIGN KEY([cd_emp], [cd_pes_prdt]) REFERENCES [pub_pes]([cd_emp], [cd_pes]) ON DELETE NO ACTION ON UPDATE CASCADE, " +
			" CONSTRAINT [ctrl_ent_pub_prod_fk] FOREIGN KEY([cd_emp], [cd_prod]) REFERENCES [pub_prod]([cd_emp], [cd_prod]) ON DELETE NO ACTION ON UPDATE CASCADE, " +
			" CONSTRAINT [sqlite_autoindex_ctrl_ent_1] PRIMARY KEY ([cd_emp], [cd_ent]) ON CONFLICT ABORT);";
			
		db.execSQL(sql);
		
		sql = "CREATE TABLE [ctrl_ban] (" +
			"[cd_emp] SMALLINT NOT NULL CONSTRAINT [ctrl_ban_pub_emp_fk] REFERENCES [pub_emp]([cd_emp]) ON DELETE NO ACTION ON UPDATE CASCADE," +
			"[cd_lanc] BIGINT NOT NULL," +
			"[dt_lanc] DATE NOT NULL," +
			"[tp_lanc] SMALLINT," +
			"[st_lanc] SMALLINT," +
			"[cd_pes] BIGINT," +
			"[nm_pes] VARCHAR(100)," +
			"[vl_lanc] NUMERIC(10, 2) NOT NULL DEFAULT 0," +
			"[obs_lanc] VARCHAR(200)," +
			"[km_ini] NUMERIC(10, 2) NOT NULL DEFAULT 0," +
			"[km_fin] NUMERIC(10, 2) NOT NULL DEFAULT 0," +
			"[qt_litros] NUMERIC(10, 2) NOT NULL DEFAULT 0," +
			"[cd_term] SMALLINT," +
			"[cd_usu] SMALLINT CONSTRAINT [ctrl_ban_pub_usu_fk] REFERENCES [pub_usu]([cd_usu]) ON DELETE NO ACTION ON UPDATE CASCADE," +
			"[cd_ver] SMALLINT NOT NULL DEFAULT 1," +
			"[st_reg] SMALLINT NOT NULL DEFAULT 0," +
			"CONSTRAINT [ctrl_ban_pub_pes_fk] FOREIGN KEY([cd_emp], [cd_pes]) REFERENCES [pub_pes]([cd_emp], [cd_pes]) ON DELETE NO ACTION ON UPDATE CASCADE," +
			"CONSTRAINT [ctrl_ban_pub_term_fk] FOREIGN KEY([cd_emp], [cd_term]) REFERENCES [pub_term]([cd_emp], [cd_term]) ON DELETE NO ACTION ON UPDATE CASCADE," +
			"CONSTRAINT [] PRIMARY KEY ([cd_emp], [cd_lanc]) ON CONFLICT ABORT);";
		
		db.execSQL(sql);
				
		sql = "CREATE TABLE [pub_cid] (" +
			" [cd_cid] INTEGER NOT NULL PRIMARY KEY," + 
			" [ds_cid] CHAR(100)," + 
			" [uf_cid] CHAR(2));";
			
		db.execSQL(sql);
				
		sql = "CREATE TABLE [pub_term] (" +
			" [cd_emp] SMALLINT NOT NULL," +
			" [cd_term] SMALLINT NOT NULL," + 
			" [nm_term] CHAR(50), " +
			" [mac_id] CHAR(20), " +
			" CONSTRAINT [] PRIMARY KEY ([cd_emp], [cd_term]));";
			
		db.execSQL(sql);
				
		sql = "CREATE TABLE [pub_usu] (" +
			" [cd_usu] SMALLINT, " +
			" [nm_usu] CHAR(20), " +
			" [senha_usu] CHAR(15), " +
			" [usu_adm] SMALLINT, " +
			" CONSTRAINT [] PRIMARY KEY ([cd_usu]) ON CONFLICT ABORT);";
		
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}
	
}
