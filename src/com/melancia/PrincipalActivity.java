package com.melancia;

import java.sql.ResultSet;

import com.melancia.controles.CEditText;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PrincipalActivity extends Activity {
	
	private final Context activity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_principal);
		
		Button btnMov = (Button) findViewById(R.id.btnMov);
		Button btnCadPes = (Button) findViewById(R.id.btnCadPes);
		Button btnDespRec = (Button) findViewById(R.id.btnDespRec);
		Button btnSincrBD = (Button) findViewById(R.id.btnSincrBD);
		
		btnMov.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, TabMovActivity.class);
				activity.startActivity(intent);
				
			}
		});
		
		btnCadPes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, CadPesActivity.class);
				activity.startActivity(intent);
			}
		});
		
		btnDespRec.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, MovBanActivity.class);
				activity.startActivity(intent);
			}
		});
		
		btnSincrBD.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				try {	
					final View vSincronizar = PrincipalActivity.this.getLayoutInflater().inflate(R.layout.dialog_sincronizar, null);
					
					final CEditText edtQtEntEnv = (CEditText) vSincronizar.findViewById(R.id.edtQtEntEnv);
					final CEditText edtQtPesEnv = (CEditText) vSincronizar.findViewById(R.id.edtQtPesEnv);
					final CEditText edtQtBanEnv = (CEditText) vSincronizar.findViewById(R.id.edtQtBanEnv);
					final CEditText edtQtEntBx = (CEditText) vSincronizar.findViewById(R.id.edtQtEntBx);
					final CEditText edtQtPesBx = (CEditText) vSincronizar.findViewById(R.id.edtQtPesBx);
					final CEditText edtQtBanBx = (CEditText) vSincronizar.findViewById(R.id.edtQtBanBx);
					
					edtQtEntEnv.setText("000000");
					edtQtPesEnv.setText("000000");
					edtQtBanEnv.setText("000000");
					edtQtEntBx.setText("000000");
					edtQtPesBx.setText("000000");
					edtQtBanBx.setText("000000");
					
					new AsyncTask<Void, Void, Boolean>(){
						ProgressDialog prgDialog = null;
						String erro = "";
						
						int qtEntEnv = 0;
						int qtPesEnv = 0;
						int qtBanEnv = 0;
						int qtEntBx = 0;
						int qtPesBx = 0;
						int qtBanBx = 0;
						
						@Override
						protected void onPreExecute() {
							prgDialog = ProgressDialog.show(PrincipalActivity.this, "", "Coletando informações...");
						}
						
						@Override
						protected Boolean doInBackground(Void... params) {
							try {
								
								Geral.dbCn = new DataBase();
								Geral.dbCn.setNomeServidorBD(Geral.config.getServerDb());
								Geral.dbCn.setNomeBD(Geral.config.getNomeDb());
								Geral.dbCn.setUsuarioBD(Geral.config.getUsuarioDb());
								Geral.dbCn.setSenhaDB(Geral.config.getSenhaDb());
								Geral.dbCn.setPortaDB(Geral.config.getPortaDb());
								
								if(!Geral.dbCn.abreConexao()){
									throw new RuntimeException("Conexão com o servidor não disponível.");
								}
								
								qtEntEnv = Funcoes.toInt(Funcoes.vlCampoTblLocal("coalesce(count(*), 0) as qt", "ctrl_ent", "st_reg = 0 and cd_term = " + Geral.terminal));
								qtPesEnv = Funcoes.toInt(Funcoes.vlCampoTblLocal("coalesce(count(*), 0) as qt", "pub_pes", "st_reg = 0 and cd_term = " + Geral.terminal));
								qtBanEnv = Funcoes.toInt(Funcoes.vlCampoTblLocal("coalesce(count(*), 0) as qt", "ctrl_ban", "st_reg = 0 and cd_term = " + Geral.terminal));
								
								String tabelas = "pub_pes pes inner join ctrl_ver_pub_pes ver" +
								        " on pes.cd_emp = ver.cd_emp" +
								        " and pes.cd_pes = ver.cd_pes" +
								        " and pes.cd_ver <> ver.cd_ver";
								
								qtPesBx = Funcoes.toInt(Funcoes.vlCampoTbl("coalesce(count(*), 0) as qt", tabelas, "pes.cd_emp = " + Geral.cdEmpresa + " and ver.cd_term = " + Geral.terminal));
								
								tabelas = "ctrl_ent ent inner join ctrl_ver_ctrl_ent ver" +
										" on ent.cd_emp = ver.cd_emp" +
										" and ent.cd_ent = ver.cd_ent" +
										" and ent.cd_ver <> ver.cd_ver";
								
								qtEntBx = Funcoes.toInt(Funcoes.vlCampoTbl("coalesce(count(*), 0) as qt", tabelas, "ent.cd_emp = " + Geral.cdEmpresa + " and ver.cd_term = " + Geral.terminal));
								
								tabelas = "ctrl_ban ban inner join ctrl_ver_ctrl_ban ver" +
										" on ban.cd_emp = ver.cd_emp" +
										" and ban.cd_lanc = ver.cd_lanc" +
										" and ban.cd_ver <> ver.cd_ver";
								
								qtBanBx = Funcoes.toInt(Funcoes.vlCampoTbl("coalesce(count(*), 0) as qt", tabelas, "ban.cd_emp = " + Geral.cdEmpresa + " and ver.cd_term = " + Geral.terminal));
								
								return true;
								
							} catch (Exception e) {
								erro = e.getMessage();
								return false;
							}
						}
						
						@Override
						protected void onPostExecute(Boolean result) {
							try {
								if(!result)
									throw new RuntimeException(erro);
								
								edtQtEntEnv.setText(Funcoes.formata(qtEntEnv, 6));
								edtQtPesEnv.setText(Funcoes.formata(qtPesEnv, 6));
								edtQtBanEnv.setText(Funcoes.formata(qtBanEnv, 6));
								edtQtEntBx.setText(Funcoes.formata(qtEntBx, 6));
								edtQtPesBx.setText(Funcoes.formata(qtPesBx, 6));
								edtQtBanBx.setText(Funcoes.formata(qtBanBx, 6));
								
								final AlertDialog dlgSincronizar = new AlertDialog.Builder(PrincipalActivity.this)
								.setView(vSincronizar)
								.setTitle("Sincronizar Banco de Dados")
								.setIcon(R.drawable.ic_launcher)
								.setPositiveButton("Sincronizar", null)
								.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										if(Geral.dbCn != null){
											if(Geral.dbCn.conexaoAberta())
												Geral.dbCn.fechaConexao();
											
											Geral.dbCn = null;
										}
										
										dialog.dismiss();
									}
								})
								.create();
								
								dlgSincronizar.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
								dlgSincronizar.show();
								
								Button btnOk = dlgSincronizar.getButton(DialogInterface.BUTTON_POSITIVE);
								btnOk.setOnClickListener(new View.OnClickListener() {
									
									@Override
									public void onClick(View view) {
										
										if(edtQtEntEnv.getInt() == 0)
											if(edtQtPesEnv.getInt() == 0)
												if(edtQtBanEnv.getInt() == 0)
													if(edtQtEntBx.getInt() == 0)
														if(edtQtBanBx.getInt() == 0)
															if(edtQtPesBx.getInt() == 0){
																Funcoes.msgBox("Não ha registros para serem sincronizados.", PrincipalActivity.this);
																return;
															}
										
										DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												new AsyncTask<Void, Void, Boolean>(){
													ProgressDialog prgDialog = null;
													String erro = "";
													Handler handler = new Handler();
													
													private void preparaPrgDialog(final String mensagem, final int max){
														handler.post(new Runnable() {
															@Override
															public void run() {
																prgDialog.dismiss();
																prgDialog = new ProgressDialog(PrincipalActivity.this);
																prgDialog.setMessage(mensagem);
																prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
																prgDialog.setMax(max);
																prgDialog.setProgress(0);
																prgDialog.setCancelable(false);
																prgDialog.setCanceledOnTouchOutside(false);
																prgDialog.show();
															}
														});
													}
													
													protected void onPreExecute() {
														prgDialog = ProgressDialog.show(PrincipalActivity.this, "", "Processando...");
													};
													
													protected Boolean doInBackground(Void... params) {
														try {
															
															if(edtQtPesEnv.getInt() > 0){
																Cursor crsTmp = Geral.localDB.rawQuery("select * from pub_pes where cd_emp = " + Geral.cdEmpresa + " and cd_term = " + Geral.terminal + " and st_reg = 0 ", null);
																if(crsTmp.getCount() > 0){
																	preparaPrgDialog("Transmitindo entradas...", crsTmp.getCount());
																	
																	crsTmp.moveToFirst();
																	do{
																		String vetCampos[] = new String[]{"cd_emp", "cd_pes", "nm_razao_pes", "nm_fants_pes", 
																				"nm_resp_pes", "placa_veic_pes", "uf_placa_veic_pes", "tp_pes_motr", "tp_pes_dest",
																				"tp_pes_prdt", "cd_term", "st_reg", "cpf_cgc_pes", "ver_reg", "tp_pes_carg", 
																				"cd_cid_placa", "cd_ver", "insc_est_pes", "email_pes", "tp_pes_emit"};
																		
																		Object vetValores[] = new Object[]{
																				crsTmp.getInt(crsTmp.getColumnIndex("cd_emp")),
																				crsTmp.getLong(crsTmp.getColumnIndex("cd_pes")),
																				crsTmp.getString(crsTmp.getColumnIndex("nm_razao_pes")),
																				crsTmp.getString(crsTmp.getColumnIndex("nm_fants_pes")),
																				"", crsTmp.getString(crsTmp.getColumnIndex("placa_veic")),
																				crsTmp.getString(crsTmp.getColumnIndex("uf_placa")),
																				crsTmp.getInt(crsTmp.getColumnIndex("tp_pes_motr")),
																				crsTmp.getInt(crsTmp.getColumnIndex("tp_pes_dest")),
																				crsTmp.getInt(crsTmp.getColumnIndex("tp_pes_prdt")),
																				Geral.terminal, 1, crsTmp.getString(crsTmp.getColumnIndex("cpf_cgc_pes")),
																				crsTmp.getInt(crsTmp.getColumnIndex("cd_ver")),
																				crsTmp.getInt(crsTmp.getColumnIndex("tp_pes_carg")),
																				crsTmp.getLong(crsTmp.getColumnIndex("cd_cid")),
																				crsTmp.getInt(crsTmp.getColumnIndex("cd_ver")),
																				"", crsTmp.getString(crsTmp.getColumnIndex("email_pes")),
																				crsTmp.getInt(crsTmp.getColumnIndex("tp_pes_emit"))
																		};
																		
																		
																		String criterio = "cd_emp = " + crsTmp.getInt(crsTmp.getColumnIndex("cd_emp")) + " and cd_pes = " + crsTmp.getLong(crsTmp.getColumnIndex("cd_pes"));
																		
																		if(Funcoes.toLong(Funcoes.vlCampoTbl("cd_pes", "pub_pes", criterio)) > 0){
																			if(!Geral.dbCn.altReg("pub_pes", vetCampos, vetValores, criterio, false))
																				throw new Exception("Falha ao transmitir atualização do cadastro de pessoa.");
																		}else{
																			if(!Geral.dbCn.addReg("pub_pes", vetCampos, vetValores, false))
																				throw new Exception("Falha ao transmitir pessoa.");
																		}
																		
																		if(!crsTmp.getString(crsTmp.getColumnIndex("endereco_pes")).equals("")){
																			vetCampos = new String[]{"cd_emp", "cd_pes", "cd_seq_cp_end", "cpf_cgc_cp_end", "end_cp_end",
																					"bairro_cp_end", "cd_cid", "cep_pes_cp_end", "tp_lograd", "lograd", "nr_cp_end",
																					"compl_cp_end", "nm_fants_cp_end"};
																			
																			vetValores = new Object[]{
																				crsTmp.getInt(crsTmp.getColumnIndex("cd_emp")),
																				crsTmp.getLong(crsTmp.getColumnIndex("cd_pes")),
																				1, crsTmp.getString(crsTmp.getColumnIndex("cpf_cgc_pes")),
																				crsTmp.getString(crsTmp.getColumnIndex("endereco_pes")),
																				crsTmp.getString(crsTmp.getColumnIndex("bairro_end")),
																				crsTmp.getString(crsTmp.getColumnIndex("cd_cid_end")),
																				crsTmp.getString(crsTmp.getColumnIndex("cep_end")),
																				"", crsTmp.getString(crsTmp.getColumnIndex("endereco_pes")),
																				crsTmp.getString(crsTmp.getColumnIndex("numero_end")),
																				crsTmp.getString(crsTmp.getColumnIndex("compl_end")),
																				crsTmp.getString(crsTmp.getColumnIndex("nm_fants_pes"))
																			};
																			
																			if(Funcoes.toLong(Funcoes.vlCampoTbl("cd_pes", "pub_pes_cp_end", criterio + " and cd_seq_cp_end = 1")) > 0){
																				if(!Geral.dbCn.altReg("pub_pes_cp_end", vetCampos, vetValores, criterio + " and cd_seq_cp_end = 1", false))
																					throw new Exception("Falha ao transmitir atualização do cadastro de pessoa.");
																			}else{
																				if(!Geral.dbCn.addReg("pub_pes_cp_end", vetCampos, vetValores, false))
																					throw new Exception("Falha ao transmitir pessoa.");
																			}
																		}
																		
																		if(!crsTmp.getString(crsTmp.getColumnIndex("fone_pes")).equals("")){
																			vetCampos = new String[]{"cd_emp", "cd_pes", "cd_tel", "nr_tel_pes", "tp_tel"};
																			vetValores = new Object[]{
																					crsTmp.getInt(crsTmp.getColumnIndex("cd_emp")),
																					crsTmp.getLong(crsTmp.getColumnIndex("cd_pes")),
																					1, crsTmp.getString(crsTmp.getColumnIndex("fone_pes")), 1
																			};
																			
																			if(Funcoes.toLong(Funcoes.vlCampoTbl("cd_pes", "pub_pes_tel", criterio + " and cd_tel = 1")) > 0){
																				if(!Geral.dbCn.altReg("pub_pes_tel", vetCampos, vetValores, criterio + " and cd_tel = 1", false))
																					throw new Exception("Falha ao transmitir atualização do cadastro de pessoa.");
																			}else{
																				if(!Geral.dbCn.addReg("pub_pes_tel", vetCampos, vetValores, false))
																					throw new Exception("Falha ao transmitir pessoa.");
																			}
																		}
																		
																		String sql = "update pub_pes set st_reg = 1 where cd_emp = " + Geral.cdEmpresa + " and cd_pes = " + crsTmp.getLong((crsTmp.getColumnIndex("cd_pes")));
																		Geral.localDB.execSQL(sql);
																		Geral.dbCn.commit();
																		
																		publishProgress();
																	}while(crsTmp.moveToNext());
																}
																		
															}
															
															if(edtQtEntEnv.getInt() > 0){
																
																Cursor crsTmp = Geral.localDB.rawQuery("select * from ctrl_ent where cd_emp = " + Geral.cdEmpresa + " and cd_term = " + Geral.terminal + " and st_reg= 0 ", null);
																if(crsTmp.getCount() > 0){
																	preparaPrgDialog("Transmitindo entradas...", crsTmp.getCount());
																	
																	crsTmp.moveToFirst();
																	do{
																		String[] vetCampos = new String[]{"CD_EMP", "CD_ENT", "CD_PES_PRDT", "DT_ENT", "CD_PROD", 
												                                "PESO_BRUTO_PROD", "PERC_PALHA", "PESO_DESCARTE", "PRECO_COMP", "PRECO_VEND", 
												                                "PLACA_FRETE", "UF_PLACA_FRETE", "VL_FRETE_TON", "VL_ICMS_FRETE", "VL_MAO_OBRA", 
												                                "VL_OUT_DESP", "CD_PES_DEST", "OBS_ENT", "CD_TERM", "QTDE_PECAS", 
												                                "ST_ENT", "ST_REG", "VL_TOT_PRDT", "PESO_LIQ_PROD", 
												                                "VL_PAGO", "VL_RECEBIDO", "NM_MOTR", "FONE_MOTR", "VL_TO_FRETE", "VL_TO_SAI", 
												                                "DT_SAI", "ST_SAI", "STATUS_ENT", "CD_PES_CARG", "OBS_SAI", "CD_CID_PLACA", 
												                                "CPF_CGC_MOTR", "CD_USU", "CD_VER", "TP_FRETE_SAI", "TP_ICMS_FRETE", "EMITIR_NF"};
																		
																		Object[] vetValores = new Object[]{
																				Geral.cdEmpresa, 
																				crsTmp.getInt(crsTmp.getColumnIndex("cd_ent")), 
																				crsTmp.getLong(crsTmp.getColumnIndex("cd_pes_prdt")),
																				Funcoes.retData(Funcoes.formataData(crsTmp.getString(crsTmp.getColumnIndex("dt_ent")), "dd/MM/yyyy")),
																				crsTmp.getInt(crsTmp.getColumnIndex("cd_prod")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("peso_bruto_prod")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("perc_palha")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("peso_descarte")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("preco_comp")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("preco_vend")),
																				crsTmp.getString(crsTmp.getColumnIndex("placa_frete")),
																				crsTmp.getInt(crsTmp.getColumnIndex("uf_placa_frete")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("vl_frete_ton")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("vl_icms_frete")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("vl_mao_obra")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("vl_out_desp")),
																				crsTmp.getLong(crsTmp.getColumnIndex("cd_pes_dest")),
																				crsTmp.getString(crsTmp.getColumnIndex("obs_ent")),
																				crsTmp.getInt(crsTmp.getColumnIndex("cd_term")),
																				crsTmp.getInt(crsTmp.getColumnIndex("qtde_pecas")),
																				crsTmp.getInt(crsTmp.getColumnIndex("st_ent")),
																				1,
																				crsTmp.getDouble(crsTmp.getColumnIndex("vl_tot_prdt")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("peso_liq_prod")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("vl_pago")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("vl_recebido")),
																				crsTmp.getString(crsTmp.getColumnIndex("nm_motr")),
																				crsTmp.getString(crsTmp.getColumnIndex("fone_motr")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("vl_to_frete")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("vl_to_sai")),
																				Funcoes.retData(Funcoes.formataData(crsTmp.getString(crsTmp.getColumnIndex("dt_sai")), "dd/MM/yyyy")),
																				crsTmp.getInt(crsTmp.getColumnIndex("st_sai")),
																				crsTmp.getInt(crsTmp.getColumnIndex("status_ent")),
																				crsTmp.getLong(crsTmp.getColumnIndex("cd_pes_carg")),
																				crsTmp.getString(crsTmp.getColumnIndex("obs_sai")),
																				crsTmp.getInt(crsTmp.getColumnIndex("cd_cid_placa")),
																				crsTmp.getString(crsTmp.getColumnIndex("cpf_cgc_motr")),
																				crsTmp.getInt(crsTmp.getColumnIndex("cd_usu")),
																				crsTmp.getInt(crsTmp.getColumnIndex("cd_ver")),
																				crsTmp.getInt(crsTmp.getColumnIndex("tp_frete_sai")),
																				crsTmp.getInt(crsTmp.getColumnIndex("tp_icms_frete")),
																				crsTmp.getInt(crsTmp.getColumnIndex("emitir_nf"))
																		};
																		
																		String criterio = "cd_emp = " + crsTmp.getInt(crsTmp.getColumnIndex("cd_emp")) + " and cd_ent = " + crsTmp.getLong(crsTmp.getColumnIndex("cd_ent"));
																				
																		if(Funcoes.toLong(Funcoes.vlCampoTbl("cd_ent", "ctrl_ent", criterio)) > 0){
																			if(!Geral.dbCn.altReg("ctrl_ent", vetCampos, vetValores, criterio, false))
																				throw new Exception("Falha ao transmitir atualização da entrada.");
																		}else{
																			if(!Geral.dbCn.addReg("ctrl_ent", vetCampos, vetValores, false))
																				throw new Exception("Falha ao transmitir entrada.");
																		}
																		
																		String sql = "update ctrl_ent set st_reg = 1 where cd_emp = " + Geral.cdEmpresa + " and cd_ent = " + crsTmp.getLong((crsTmp.getColumnIndex("cd_ent")));
																		Geral.localDB.execSQL(sql);
																		Geral.dbCn.commit();
																		
																		publishProgress();
																	}while(crsTmp.moveToNext());
																}
															}
															
															if(edtQtBanEnv.getInt() > 0){
																
																Cursor crsTmp = Geral.localDB.rawQuery("select * from ctrl_ban where cd_emp = " + Geral.cdEmpresa + " and cd_term = " + Geral.terminal + " and st_reg = 0 ", null);
																if(crsTmp.getCount() > 0){
																	preparaPrgDialog("Transmitindo lançamentos de despesas/receitas...", crsTmp.getCount());
																	
																	crsTmp.moveToFirst();
																	do{
																		String[] vetCampos = new String[]{"cd_emp", "cd_lanc", "dt_lanc", "tp_lanc", "st_lanc",
																                "cd_pes", "nm_pes", "vl_lanc", "obs_lanc", "km_ini", "km_fin", "qt_litros", 
																                "cd_usu", "cd_term", "st_reg", "cd_ver"};
																		
																		Object[] vetValores = new Object[]{
																				Geral.cdEmpresa, 
																				crsTmp.getInt(crsTmp.getColumnIndex("cd_lanc")), 
																				Funcoes.retData(Funcoes.formataData(crsTmp.getString(crsTmp.getColumnIndex("dt_lanc")), "dd/MM/yyyy")),
																				crsTmp.getInt(crsTmp.getColumnIndex("tp_lanc")), 
																				crsTmp.getInt(crsTmp.getColumnIndex("st_lanc")), 
																				crsTmp.getLong(crsTmp.getColumnIndex("cd_pes")),
																				crsTmp.getString(crsTmp.getColumnIndex("nm_pes")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("vl_lanc")),
																				crsTmp.getString(crsTmp.getColumnIndex("obs_lanc")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("km_ini")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("km_fin")),
																				crsTmp.getDouble(crsTmp.getColumnIndex("qt_litros")),
																				crsTmp.getInt(crsTmp.getColumnIndex("cd_usu")),
																				crsTmp.getInt(crsTmp.getColumnIndex("cd_term")),
																				1,
																				crsTmp.getInt(crsTmp.getColumnIndex("cd_ver"))
																		};
																		
																		String criterio = "cd_emp = " + crsTmp.getInt(crsTmp.getColumnIndex("cd_emp")) + " and cd_lanc = " + crsTmp.getLong(crsTmp.getColumnIndex("cd_lanc"));
																				
																		if(Funcoes.toLong(Funcoes.vlCampoTbl("cd_lanc", "ctrl_ban", criterio)) > 0){
																			if(!Geral.dbCn.altReg("ctrl_ban", vetCampos, vetValores, criterio, false))
																				throw new Exception("Falha ao transmitir atualização do lançamento.");
																		}else{
																			if(!Geral.dbCn.addReg("ctrl_ban", vetCampos, vetValores, false))
																				throw new Exception("Falha ao transmitir lançamento.");
																		}
																		
																		String sql = "update ctrl_ban set st_reg = 1 where cd_emp = " + Geral.cdEmpresa + " and cd_lanc = " + crsTmp.getLong((crsTmp.getColumnIndex("cd_lanc")));
																		Geral.localDB.execSQL(sql);
																		Geral.dbCn.commit();
																		
																		publishProgress();
																	}while(crsTmp.moveToNext());
																}
															}
															
															if(edtQtPesBx.getInt() > 0){
																ResultSet rsTmp = Geral.dbCn.retReg("select p.cd_emp, p.cd_pes, nm_razao_pes, nm_fants_pes, cpf_cgc_pes, placa_veic_pes, uf_placa_veic_pes, cd_cid_placa," +
																		  " coalesce((select nr_tel_pes from pub_pes_tel t where t.cd_emp = p.cd_emp and t.cd_pes = p.cd_pes limit 1), '') as fone," +
																		  " tp_pes_prdt, tp_pes_dest, tp_pes_motr, tp_pes_carg, tp_pes_emit, 1, st_reg, p.cd_ver, p.cd_term, email_pes," +
																		  " coalesce(tp_lograd || ' ' || lograd, '') as endereco, coalesce(nr_cp_end, 0) as nr_cp_end, coalesce(compl_cp_end, '') as compl_cp_end," +
																		  " coalesce(cep_pes_cp_end, '') as cep_pes_cp_end, coalesce(bairro_cp_end, '') as bairro_cp_end, coalesce(cd_cid, 0) as cd_cid" +
																		  " from pub_pes p left join pub_pes_cp_end e" +
																		  " on p.cd_emp = e.cd_emp" +
																		  " and p.cd_pes = e.cd_pes" +
																		  " and e.cd_seq_cp_end = 1" +
																		  " inner join ctrl_ver_pub_pes ver" +
																		  " on p.cd_emp = ver.cd_emp" +
																		  " and p.cd_pes = ver.cd_pes" +
																		  " and p.cd_ver <> ver.cd_ver" +
																		  " where ver.cd_emp = " + Geral.cdEmpresa +
																		  " and ver.cd_term = " + Geral.terminal);
														
																if(rsTmp != null){
																	rsTmp.last();
																	preparaPrgDialog("Cadastrando pessoas...", rsTmp.getRow());
																	rsTmp.first();
																	
																	do{
																		ContentValues ctvValores = new ContentValues();
																		ctvValores.put("cd_emp", rsTmp.getInt("cd_emp"));
																		ctvValores.put("cd_pes", rsTmp.getInt("cd_pes"));
																		ctvValores.put("nm_razao_pes", rsTmp.getString("nm_razao_pes"));
																		ctvValores.put("nm_fants_pes", rsTmp.getString("nm_fants_pes"));
																		ctvValores.put("cpf_cgc_pes", rsTmp.getString("cpf_cgc_pes"));
																		ctvValores.put("email_pes", rsTmp.getString("email_pes"));
																		ctvValores.put("endereco_pes", rsTmp.getString("endereco"));
																		ctvValores.put("numero_end", rsTmp.getInt("nr_cp_end"));
																		ctvValores.put("compl_end", rsTmp.getString("compl_cp_end"));
																		ctvValores.put("cep_end", rsTmp.getString("cep_pes_cp_end"));
																		ctvValores.put("bairro_end", rsTmp.getString("bairro_cp_end"));
																		ctvValores.put("cd_cid_end", rsTmp.getString("cd_cid"));
																		ctvValores.put("placa_veic", rsTmp.getString("placa_veic_pes"));
																		ctvValores.put("uf_placa", rsTmp.getInt("uf_placa_veic_pes"));
																		ctvValores.put("cd_cid", rsTmp.getInt("cd_cid_placa"));
																		ctvValores.put("fone_pes", rsTmp.getString("fone"));
																		ctvValores.put("tp_pes_prdt", rsTmp.getInt("tp_pes_prdt"));
																		ctvValores.put("tp_pes_dest", rsTmp.getInt("tp_pes_dest"));
																		ctvValores.put("tp_pes_motr", rsTmp.getInt("tp_pes_motr"));
																		ctvValores.put("tp_pes_carg", rsTmp.getInt("tp_pes_carg"));
																		ctvValores.put("tp_pes_emit", rsTmp.getInt("tp_pes_emit"));
																		ctvValores.put("st_reg", 1);
																		ctvValores.put("cd_ver", rsTmp.getInt("cd_ver"));
																		ctvValores.put("st_pes", rsTmp.getInt("st_reg"));
																		ctvValores.put("cd_term", rsTmp.getInt("cd_term"));
																		
																		if(Funcoes.toLong(Funcoes.vlCampoTblLocal("cd_pes", "pub_pes", "cd_emp = " + rsTmp.getInt("cd_emp") + " and cd_pes = " + rsTmp.getLong("cd_pes"))) > 0){
																			if(Geral.localDB.update("pub_pes", ctvValores, "cd_emp = ?  and cd_pes = ?", new String[]{String.valueOf(rsTmp.getInt("cd_emp")), String.valueOf(rsTmp.getLong("cd_pes"))}) < 0){
																				throw new Exception("Falha ao baixar nova pessoa.");
																			}
																		}else{
																			if(Geral.localDB.insert("pub_pes", null, ctvValores) < 0){
																				throw new Exception("Falha ao baixar atualização de pessoa.");
																			}
																		}
																		String criterio = "cd_emp = " + Geral.cdEmpresa +
																				  " and cd_pes = " + rsTmp.getLong("cd_pes") +
																				  " and cd_term = " + Geral.terminal;
																		
																		if(Geral.dbCn.altReg("ctrl_ver_pub_pes", new String[]{"cd_ver"}, new Object[]{rsTmp.getInt("cd_ver")}, criterio, false)){
																			Geral.dbCn.commit();
																		}else{
																			erro = "Falha ao baixar pessoa.";
																			return false;
																		}
																		
																		publishProgress();
																	}while(rsTmp.next());
																}
															}
															
															if(edtQtEntBx.getInt() > 0){
																String sql = "select ent.*" +
																		" from ctrl_ent ent inner join ctrl_ver_ctrl_ent ver" +
																		" on ent.cd_emp = ver.cd_emp" +
																		" and ent.cd_ent = ver.cd_ent" +
																		" and ent.cd_ver <> ver.cd_ver" +
																		" where ent.cd_emp = " + Geral.cdEmpresa + 
																		" and ver.cd_term = " + Geral.terminal;
																
																ResultSet rsTmp = Geral.dbCn.retReg(sql);
																if(rsTmp != null){
																	rsTmp.last();
																	preparaPrgDialog("Cadastrando entradas...", rsTmp.getRow());
																	rsTmp.first();
																	
																	do{
																		ContentValues ctvValores = new ContentValues();
																		ctvValores.put("cd_emp", rsTmp.getInt("cd_emp"));
																		ctvValores.put("cd_ent", rsTmp.getInt("cd_ent"));
																		ctvValores.put("cd_pes_prdt", rsTmp.getInt("cd_pes_prdt"));
																		ctvValores.put("dt_ent", rsTmp.getDate("dt_ent").toString());
																		ctvValores.put("cd_prod", rsTmp.getInt("cd_prod"));
																		ctvValores.put("peso_bruto_prod", rsTmp.getDouble("peso_bruto_prod"));
																		ctvValores.put("perc_palha", rsTmp.getDouble("perc_palha"));
																		ctvValores.put("peso_descarte", rsTmp.getDouble("peso_descarte"));
																		ctvValores.put("preco_comp", rsTmp.getDouble("preco_comp"));
																		ctvValores.put("preco_vend", rsTmp.getDouble("preco_vend"));
																		ctvValores.put("placa_frete", rsTmp.getString("placa_frete"));
																		ctvValores.put("uf_placa_frete", rsTmp.getInt("uf_placa_frete"));
																		ctvValores.put("cd_cid_placa", rsTmp.getInt("cd_cid_placa"));
																		ctvValores.put("vl_frete_ton", rsTmp.getDouble("vl_frete_ton")); 
																		ctvValores.put("vl_icms_frete", rsTmp.getDouble("vl_icms_frete"));
																		ctvValores.put("vl_mao_obra", rsTmp.getDouble("vl_mao_obra")); 
																		ctvValores.put("vl_out_desp", rsTmp.getDouble("vl_out_desp")); 
																		ctvValores.put("cd_pes_dest", rsTmp.getInt("cd_pes_dest")); 
																		ctvValores.put("obs_ent", rsTmp.getString("obs_ent"));
																		ctvValores.put("qtde_pecas", rsTmp.getInt("qtde_pecas")); 
																		ctvValores.put("st_ent", rsTmp.getInt("st_ent"));
																		ctvValores.put("vl_tot_prdt", rsTmp.getDouble("vl_tot_prdt")); 
																		ctvValores.put("vl_carg", rsTmp.getDouble("vl_carg"));
																		ctvValores.put("ver_reg", rsTmp.getInt("ver_reg")); 
																		ctvValores.put("peso_liq_prod", rsTmp.getDouble("peso_liq_prod"));
																		ctvValores.put("vl_pago", rsTmp.getDouble("vl_pago"));
																		ctvValores.put("vl_recebido", rsTmp.getDouble("vl_recebido"));
																		ctvValores.put("nm_motr", rsTmp.getString("nm_motr")); 
																		ctvValores.put("fone_motr", rsTmp.getString("fone_motr"));
																		ctvValores.put("cpf_cgc_motr", rsTmp.getString("cpf_cgc_motr"));
																		ctvValores.put("vl_to_frete", rsTmp.getDouble("vl_to_frete"));
																		ctvValores.put("vl_to_sai", rsTmp.getDouble("vl_to_sai")); 
																		ctvValores.put("dt_sai", rsTmp.getString("dt_sai"));
																		ctvValores.put("st_sai", rsTmp.getInt("st_sai")); 
																		ctvValores.put("cd_pes_carg", rsTmp.getInt("cd_pes_carg")); 
																		ctvValores.put("obs_sai", rsTmp.getString("obs_sai"));
																		ctvValores.put("cd_usu", rsTmp.getInt("cd_usu"));
																		ctvValores.put("st_reg", 1);
																		ctvValores.put("cd_ver", rsTmp.getInt("cd_ver"));
																		ctvValores.put("status_ent", rsTmp.getInt("status_ent"));
																		ctvValores.put("cd_term", rsTmp.getInt("cd_term"));
																		ctvValores.put("dt_emissao", rsTmp.getString("dt_ent"));
																		ctvValores.put("tp_frete_sai", rsTmp.getInt("tp_frete_sai"));
																		ctvValores.put("tp_icms_frete", rsTmp.getInt("tp_icms_frete"));
																		ctvValores.put("emitir_nf", rsTmp.getInt("emitir_nf"));
																		
																		if(Funcoes.toLong(Funcoes.vlCampoTblLocal("cd_ent", "ctrl_ent", "cd_emp = " + rsTmp.getInt("cd_emp") + " and cd_ent = " + rsTmp.getLong("cd_ent"))) > 0){
																			if(Geral.localDB.update("ctrl_ent", ctvValores, "cd_emp = ?  and cd_ent = ?", new String[]{String.valueOf(rsTmp.getInt("cd_emp")), String.valueOf(rsTmp.getLong("cd_ent"))}) < 0){
																				throw new Exception("Falha ao baixar atualização de entrada.");
																			}
																		}else{
																			if(Geral.localDB.insert("ctrl_ent", null, ctvValores) < 0){
																				throw new Exception("Falha ao baixar entrada.");
																			}
																		}
																		
																		String criterio = "cd_emp = " + Geral.cdEmpresa +
																						  " and cd_ent = " + rsTmp.getLong("cd_ent") +
																						  " and cd_term = " + Geral.terminal;
																				
																		if(Geral.dbCn.altReg("ctrl_ver_ctrl_ent", new String[]{"cd_ver"}, new Object[]{rsTmp.getInt("cd_ver")}, criterio, false)){
																			Geral.dbCn.commit();
																		}else{
																			erro = "Falha ao baixar entrada.";
																			return false;
																		}
																		
																		publishProgress();
																	}while(rsTmp.next());
																}
															}
															
															if(edtQtBanBx.getInt() > 0){
																String sql = "select ban.*" +
																		" from ctrl_ban ban inner join ctrl_ver_ctrl_ban ver" +
																		" on ban.cd_emp = ver.cd_emp" +
																		" and ban.cd_lanc = ver.cd_lanc" +
																		" and ban.cd_ver <> ver.cd_ver" +
																		" where ban.cd_emp = " + Geral.cdEmpresa + 
																		" and ver.cd_term = " + Geral.terminal;
																
																ResultSet rsTmp = Geral.dbCn.retReg(sql);
																if(rsTmp != null){
																	rsTmp.last();
																	preparaPrgDialog("Cadastrando bancário...", rsTmp.getRow());
																	rsTmp.first();
																	
																	do{
																		ContentValues ctvValores = new ContentValues();
																		ctvValores.put("cd_emp", rsTmp.getInt("cd_emp"));
																		ctvValores.put("cd_lanc", rsTmp.getInt("cd_lanc"));
																		ctvValores.put("dt_lanc", rsTmp.getDate("dt_lanc").toString());
																		ctvValores.put("tp_lanc", rsTmp.getInt("tp_lanc"));
																		ctvValores.put("st_lanc", rsTmp.getInt("st_lanc"));
																		ctvValores.put("cd_pes", rsTmp.getInt("cd_pes"));
																		ctvValores.put("nm_pes", rsTmp.getString("nm_pes"));
																		ctvValores.put("vl_lanc", rsTmp.getDouble("vl_lanc"));
																		ctvValores.put("obs_lanc", rsTmp.getString("obs_lanc"));
																		ctvValores.put("km_ini", rsTmp.getDouble("km_ini"));
																		ctvValores.put("km_fin", rsTmp.getDouble("km_fin"));
																		ctvValores.put("qt_litros", rsTmp.getDouble("qt_litros"));
																		ctvValores.put("cd_usu", rsTmp.getInt("cd_usu"));
																		ctvValores.put("cd_term", rsTmp.getInt("cd_term"));
																		ctvValores.put("st_reg", 1);
																		ctvValores.put("cd_ver", rsTmp.getInt("cd_ver"));
																		
																		if(Funcoes.toLong(Funcoes.vlCampoTblLocal("cd_lanc", "ctrl_ban", "cd_emp = " + rsTmp.getInt("cd_emp") + " and cd_lanc = " + rsTmp.getLong("cd_lanc"))) > 0){
																			if(Geral.localDB.update("ctrl_ban", ctvValores, "cd_emp = ?  and cd_lanc = ?", new String[]{String.valueOf(rsTmp.getInt("cd_emp")), String.valueOf(rsTmp.getLong("cd_lanc"))}) < 0){
																				throw new Exception("Falha ao baixar atualização do bancário.");
																			}
																		}else{
																			if(Geral.localDB.insert("ctrl_ban", null, ctvValores) < 0){
																				throw new Exception("Falha ao baixar bancário.");
																			}
																		}
																		
																		String criterio = "cd_emp = " + Geral.cdEmpresa +
																						  " and cd_lanc = " + rsTmp.getLong("cd_lanc") +
																						  " and cd_term = " + Geral.terminal;
																				
																		if(Geral.dbCn.altReg("ctrl_ver_ctrl_ban", new String[]{"cd_ver"}, new Object[]{rsTmp.getInt("cd_ver")}, criterio, false)){
																			Geral.dbCn.commit();
																		}else{
																			erro = "Falha ao baixar bancário.";
																			return false;
																		}
																		
																		publishProgress();
																	}while(rsTmp.next());
																}
															}
															
															return true;
														} catch (Exception e) {
															if(Geral.dbCn != null){
																if(Geral.dbCn.conexaoAberta())
																	Geral.dbCn.rollback();
															}
															
															erro = e.getMessage();
															return false;
														} finally {
															if(Geral.dbCn != null)
																Geral.dbCn.fechaConexao();
														}
													};
													
													@Override
													protected void onPostExecute(Boolean result) {
														try {
															if(!result)
																throw new RuntimeException(erro);
															
															Funcoes.msgBox("Banco de dados sincronizado com sucesso!", PrincipalActivity.this);
															dlgSincronizar.dismiss();
															
														} catch (Exception e) {
															Funcoes.msgBox("Falha ao sincronizar banco de dados." + e.getMessage(), PrincipalActivity.this);
														} finally {
															if(prgDialog.isShowing())
																prgDialog.dismiss();
														}
													}
													
													@Override
													protected void onProgressUpdate(Void... values) {
														if(prgDialog.isShowing())
															prgDialog.incrementProgressBy(1);
													}
													
												}.execute();
											}
										};
										
										Funcoes.pergunta("Deseja iniciar a sincronização?", PrincipalActivity.this, okListener, null);
									}
								});
								
							} catch (Exception e) {
								Funcoes.msgBox("Falha ao efetuar a operação." + e.getMessage(), PrincipalActivity.this);
							} finally {
								if(prgDialog.isShowing())
									prgDialog.dismiss();
							}
						}
						
					}.execute();

				} catch (Exception e) {
					Funcoes.msgBox("Falha ao efetuar a operação." + e.getMessage(), PrincipalActivity.this);
				}
				
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
}
