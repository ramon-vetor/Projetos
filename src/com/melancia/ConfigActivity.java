package com.melancia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.melancia.Funcoes.ItemSpinner;
import com.melancia.Geral.Configuracao;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class ConfigActivity extends Activity {
	
	Activity activity;
	
	ArrayList<Configuracao> configs = new ArrayList<Geral.Configuracao>();
	ArrayList<ItemSpinner> lstCns = new ArrayList<ItemSpinner>();
	ItemSpinner itemSpinner;

	Spinner spnConexao;
	EditText edtNmServ;
	EditText edtNmBanco;
	EditText edtNmUsu;
	EditText edtSenha;
	EditText edtNmCn;
	EditText edtObsPadrao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
		activity = this;
		
		try {

			spnConexao = (Spinner) findViewById(R.id.spnConexao);
			edtNmServ = (EditText) findViewById(R.id.edtNmServidor);
			edtNmBanco = (EditText) findViewById(R.id.edtNmBanco);
			edtNmUsu = (EditText) findViewById(R.id.edtNmUsuario);
			edtSenha = (EditText) findViewById(R.id.edtSenha);
			edtNmCn = (EditText) findViewById(R.id.edtNmConexao);
			edtObsPadrao = (EditText) findViewById(R.id.edtObsPadrao);
			
			limpaTela();
			carregaConexoes();
			
			spnConexao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					if(spnConexao.getSelectedItem() != null){
						if(((ItemSpinner) spnConexao.getSelectedItem()).codigo == -1){
							limpaTela();
						}else{
							for (int i = 0; i < configs.size(); i++) {
								if (configs.get(i).getCdConexao() == ((ItemSpinner) spnConexao.getSelectedItem()).codigo){
									Configuracao config = configs.get(i);
									edtNmServ.setText(config.getServerDb());
									edtNmBanco.setText(config.getNomeDb());
									edtNmUsu.setText(config.getUsuarioDb());
									edtSenha.setText(config.getSenhaDb());
									edtNmCn.setText(config.getNmConexao());
									edtObsPadrao.setText(config.getObsPadrao());
								}
							}
						}
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});

		} catch (Exception e) {

		}
	}

	@SuppressWarnings({ "unchecked" })
	private void carregaConexoes() {
		try {
			
			lstCns = new ArrayList<ItemSpinner>();
			itemSpinner = new ItemSpinner("Nova conexão", -1);
			lstCns.add(itemSpinner);

			File arqConf = getFileStreamPath("configuracoes.xml");
			if (arqConf.exists()) {
				FileInputStream xmlConfig = openFileInput("configuracoes.xml");
				XStream xmlStream = new XStream(new DomDriver());

				xmlStream.alias("configuracoes", Geral.class);

				configs = (ArrayList<Configuracao>) xmlStream.fromXML(xmlConfig);

				if (configs.size() > 0) {
					for (int i = 0; i < configs.size(); i++) {
						itemSpinner = new ItemSpinner(configs.get(i).getNmConexao(), configs.get(i).getCdConexao());
						lstCns.add(itemSpinner);
					}
				}

				xmlStream = null;
				xmlConfig.close();
				arqConf = null;
			}

			ArrayAdapter<ItemSpinner> adapCns = new ArrayAdapter<ItemSpinner>(this, android.R.layout.simple_spinner_item, lstCns);
			adapCns.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnConexao.setAdapter(adapCns);
			
		} catch (Exception e) {
			Funcoes.msgBox("Falha ao carregar conexões." + e.getMessage(), activity);
		}
	}
	
	private void limpaTela(){
		edtNmServ.setText("");
		edtNmBanco.setText("");
		edtNmUsu.setText("");
		edtSenha.setText("");
		edtNmCn.setText("");
		edtObsPadrao.setText("");
		
		edtNmServ.requestFocus();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem mnuSalvar = menu.add("Salvar");
		//mnuSalvar.setIcon(android.R.drawable.ic_menu_save);
		//mnuSalvar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		mnuSalvar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {

				if (edtNmServ.getText().toString().trim().equals("")) {
					Funcoes.msgBox("Informe o nome do servidor do banco de dados.", activity);
					edtNmServ.requestFocus();
				}else if (edtNmBanco.getText().toString().trim().equals("")) {
					Funcoes.msgBox("Informe o nome do banco de dados.", activity);
					edtNmBanco.requestFocus();
				}else if (edtNmUsu.getText().toString().trim().equals("")){
					Funcoes.msgBox("Informe o usuário do banco de dados.", activity);
					edtNmUsu.requestFocus();
				}else if (edtSenha.getText().toString().trim().equals("")){
					Funcoes.msgBox("Informe a senha do banco de dados.", activity);
					edtSenha.requestFocus();
				}else if (edtNmCn.getText().toString().trim().equals("")){
					Funcoes.msgBox("Informe um nome para a conexão.", activity);
					edtNmCn.requestFocus();
				}else{
					
					Funcoes.pergunta("Deseja gravar os parâmetros informados?", activity,new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							try{
								int cdCnAnt = -1;
								
								if (((ItemSpinner)spnConexao.getSelectedItem()).getCodigo() == -1){
									Configuracao config = new Configuracao();
			
									config.setServerDb(edtNmServ.getText().toString().trim());
									config.setNomeDb(edtNmBanco.getText().toString().trim());
									config.setUsuarioDb(edtNmUsu.getText().toString().trim());
									config.setSenhaDb(edtSenha.getText().toString().trim());
									config.setNmConexao(edtNmCn.getText().toString().trim());
									config.setCdConexao(configs.size() + 1);
									config.setObsPadrao(edtObsPadrao.getText().toString().trim());
									
									configs.add(config);
									cdCnAnt = configs.size() + 1;
								}else{	
									for (int i = 0; i < configs.size(); i++) {
										if(configs.get(i).getCdConexao() == ((ItemSpinner) spnConexao.getSelectedItem()).codigo){
											configs.get(i).setServerDb(edtNmServ.getText().toString().trim());
											configs.get(i).setNomeDb(edtNmBanco.getText().toString().trim());
											configs.get(i).setUsuarioDb(edtNmUsu.getText().toString().trim());
											configs.get(i).setSenhaDb(edtSenha.getText().toString().trim());
											configs.get(i).setNmConexao(edtNmCn.getText().toString().trim());
											configs.get(i).setObsPadrao(edtObsPadrao.getText().toString().trim());
											
											cdCnAnt = ((ItemSpinner) spnConexao.getSelectedItem()).codigo;
										}
									}
								}
			
								FileOutputStream xmlConfig;

								xmlConfig = openFileOutput("configuracoes.xml", Context.MODE_PRIVATE);
								XStream xmlStream = new XStream(new DomDriver());
			
								xmlStream.alias("configuracoes", configs.getClass());
								xmlStream.toXML(configs, xmlConfig);
								xmlConfig.close();
								Funcoes.msgBox("Configurações gravadas com sucesso.",
										ConfigActivity.this);
								
								limpaTela();
								carregaConexoes();
								Funcoes.selItemSpinner(activity, null, R.id.spnConexao, cdCnAnt, true);
							
							} catch (Exception e) {
								Funcoes.msgBox("Falha ao gravar parâmetros." + e.getMessage(), activity);
							}
						}
						
					}, null);
				}
				
				return false;
			}
		});
		
		MenuItem mnuTestar = menu.add("Testar Configuração");
		//mnuTestar.setIcon(android.R.drawable.ic_menu_rotate);
		//mnuTestar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		mnuTestar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				try{
					if (edtNmServ.getText().toString().trim().equals("")) {
						Funcoes.msgBox("Informe o nome do servidor do banco de dados.", activity);
						edtNmServ.requestFocus();
					}else if (edtNmBanco.getText().toString().trim().equals("")) {
						Funcoes.msgBox("Informe o nome do banco de dados.", activity);
						edtNmBanco.requestFocus();
					}else if (edtNmUsu.getText().toString().trim().equals("")){
						Funcoes.msgBox("Informe o usuário do banco de dados.", activity);
						edtNmUsu.requestFocus();
					}else if (edtSenha.getText().toString().trim().equals("")){
						Funcoes.msgBox("Informe a senha do banco de dados.", activity);
						edtSenha.requestFocus();
					}else if (edtNmCn.getText().toString().trim().equals("")){
						Funcoes.msgBox("Informe um nome para a conexão.", activity);
						edtNmCn.requestFocus();
					}else{
						
						new AsyncTask<Void, Void, Boolean>() {
							ProgressDialog pgrDialog = null;
							String erro = "";
							
							@Override
							protected void onPreExecute() {
								pgrDialog = ProgressDialog.show(activity, "","Conectando...");
							}
							
							@Override
							protected Boolean doInBackground(Void... params) {
								DataBase cn = new DataBase();
								
								try{
									cn.setNomeServidorBD(edtNmServ.getText().toString().trim());
									cn.setNomeBD(edtNmBanco.getText().toString().trim());
									cn.setUsuarioBD(edtNmUsu.getText().toString().trim());
									cn.setSenhaDB(edtSenha.getText().toString().trim());
									
									if (cn.abreConexao()){
										return true;
									}else{
										erro = "Conexão não estabelecida.";
										return false;												
									}
									
								}catch(Exception e){
									erro = new String(e.getMessage());
									return false;
								}finally{
									if(cn.conexaoAberta())
										cn.fechaConexao();									
									cn = null;
								}
							}
							
							@Override
							protected void onPostExecute(Boolean result) {
								if(!result){
									Funcoes.msgBox("Falha ao estabelecer conexão." + erro, activity);
								}else{
									Funcoes.msgBox("Conectado com sucesso!", activity);
								}
								
								if(pgrDialog.isShowing())
									pgrDialog.dismiss();
							}
							
						}.execute();

					}
				}catch(Exception e){
					Funcoes.msgBox("Falha ao validar conexão." + e.getMessage(), activity);
				}
				
				return false;
			}
		});
		
		MenuItem mnuLimpar = menu.add("Limpar");
		//mnuLimpar.setIcon(android.R.drawable.ic_menu_agenda);
		//mnuLimpar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		mnuLimpar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				limpaTela();
				spnConexao.setSelection(0);
				return false;
			}
		});	
		
		MenuItem mnuExcluir = menu.add("Excluir");
		//mnuExcluir.setIcon(android.R.drawable.ic_menu_delete);
		//mnuExcluir.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		mnuExcluir.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if(((ItemSpinner) spnConexao.getSelectedItem()).codigo > -1){
					Funcoes.pergunta("Deseja realmente excluir a configuração selecionada?", activity, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try{
								for (int i = 0; i < configs.size(); i++) {
									if(configs.get(i).getCdConexao() == ((ItemSpinner) spnConexao.getSelectedItem()).codigo){
										configs.remove(i);
											
										FileOutputStream xmlConfig = openFileOutput("configuracoes.xml", Context.MODE_PRIVATE);
										XStream xmlStream = new XStream(new DomDriver());
					
										xmlStream.alias("configuracoes", configs.getClass());
										xmlStream.toXML(configs, xmlConfig);
										xmlConfig.close();
										
										if(configs.size() > 0){
											File flConfig = new File("configuracoes.xml");
											if(flConfig.exists()){
												flConfig.delete();
											}
										}
										
										Funcoes.msgBox("Configuração excluída com sucesso.", ConfigActivity.this);
										limpaTela();
										carregaConexoes();
									}
								}
							}catch(Exception e){
								Funcoes.msgBox("Falha ao excluir a configuração selecionada." + e.getMessage(), activity);
							}
							
						}
					}, null);
				}
				return false;
			}
		});
		
		MenuItem mnuCriarBD = menu.add("Criar Banco Local");
		mnuCriarBD.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				try{
					if (edtNmServ.getText().toString().trim().equals("")) {
						Funcoes.msgBox("Informe o nome do servidor do banco de dados.", activity);
						edtNmServ.requestFocus();
					}else if (edtNmBanco.getText().toString().trim().equals("")) {
						Funcoes.msgBox("Informe o nome do banco de dados.", activity);
						edtNmBanco.requestFocus();
					}else if (edtNmUsu.getText().toString().trim().equals("")){
						Funcoes.msgBox("Informe o usuário do banco de dados.", activity);
						edtNmUsu.requestFocus();
					}else if (edtSenha.getText().toString().trim().equals("")){
						Funcoes.msgBox("Informe a senha do banco de dados.", activity);
						edtSenha.requestFocus();
					}else if (edtNmCn.getText().toString().trim().equals("")){
						Funcoes.msgBox("Informe um nome para a conexão.", activity);
						edtNmCn.requestFocus();
					}else if (((ItemSpinner) spnConexao.getSelectedItem()).getCodigo() == -1){
						Funcoes.msgBox("Salve a configuração antes de prosseguir.", activity);
					}else{
						
						DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								new AsyncTask<Void, Void, Boolean>() {
									 
									ProgressDialog prgDialog = null;
									String erro = "";
									Handler handler = new Handler();
									
									private void preparaPrgDialog(final String mensagem, final int max){
										handler.post(new Runnable() {
											@Override
											public void run() {
												prgDialog.dismiss();
												prgDialog = new ProgressDialog(activity);
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
									
									@Override
									protected void onPreExecute() {
										prgDialog = ProgressDialog.show(activity, "","Conectando no Servidor...");
										prgDialog.setCancelable(false);
										prgDialog.setCanceledOnTouchOutside(false);
									}
									
									@Override
									protected Boolean doInBackground(Void... params) {
										Geral.dbCn = new DataBase();
										
										try{
											Geral.dbCn.setNomeServidorBD(edtNmServ.getText().toString().trim());
											Geral.dbCn.setNomeBD(edtNmBanco.getText().toString().trim());
											Geral.dbCn.setUsuarioBD(edtNmUsu.getText().toString().trim());
											Geral.dbCn.setSenhaDB(edtSenha.getText().toString().trim());
											
											if (!Geral.dbCn.abreConexao()){
												erro = "Conexão com o servidor não estabelecida.";
												return false;												
											}
											
											File dbPath = new File(Environment.getExternalStorageDirectory() + "/Melancia Sys");
											if(!dbPath.exists())
												dbPath.mkdir();
											
											String nomeDB = dbPath.getPath() + "/" + edtNmBanco.getText().toString() + "_local.db";
											dbPath = new File(nomeDB);
											if(dbPath.exists())
												dbPath.delete();
											
											SQLiteDatabase dbLocal = new MelanciaSQLite(activity, nomeDB, null, 1).getWritableDatabase();

											if(!dbLocal.isOpen()){
												erro = "Não foi possível criar o banco local.";
												return false;
											}
											
											WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
											String macAddress = manager.getConnectionInfo().getMacAddress();

											ResultSet rsTmp;
											ContentValues ctvValores;
											
											rsTmp = Geral.dbCn.retReg("select cd_emp, nm_emp from pub_emp");
											
											if(rsTmp != null){
												rsTmp.last();
												preparaPrgDialog("Cadastrando empresas...", rsTmp.getRow());
												rsTmp.first();
												
												do{

													if (macAddress != null) {
														int cdTerm = (Byte.parseByte(Funcoes.vlCampoTbl("coalesce(max(cd_term), 0) as cd_term", "pub_term", "cd_emp = " + rsTmp.getInt("cd_emp") + " and nm_term = '" + macAddress + "'").toString()));
														if (cdTerm == 0) {
															cdTerm = (int) Funcoes.proximoNumero("cd_term", "pub_term", "cd_emp = " + rsTmp.getInt("cd_emp"));
															String[] vetCampos = new String[]{"cd_emp", "cd_term", "nm_term"};
															Object[] vetValores = new Object[]{rsTmp.getInt("cd_emp"), cdTerm, macAddress};
															
															if(!Geral.dbCn.addReg("pub_term", vetCampos, vetValores, true)){
																Geral.dbCn.rollback();
																erro = "Falha ao registrar dispositivo no servidor.";
																return false;
															}else{
																Geral.dbCn.commit();
															}
														}
													}
													
													ctvValores = new ContentValues();
													ctvValores.put("cd_emp", rsTmp.getInt("cd_emp"));
													ctvValores.put("nm_emp", rsTmp.getString("nm_emp"));
													
													if(dbLocal.insert("pub_emp", null, ctvValores) < 0){
														erro = "Falha ao adicionar empresa.";
														return false;
													}
													
													publishProgress();
												}while(rsTmp.next());										
											}
											
											rsTmp = Geral.dbCn.retReg("select cd_usu, nm_usu, senha_usu from pub_usu");
											if(rsTmp != null){
												rsTmp.last();
												preparaPrgDialog("Cadastrando usuarios...", rsTmp.getRow());
												rsTmp.first();
												
												do{
													ctvValores = new ContentValues();
													ctvValores.put("cd_usu", rsTmp.getInt("cd_usu"));
													ctvValores.put("nm_usu", rsTmp.getString("nm_usu"));
													ctvValores.put("senha_usu", rsTmp.getString("senha_usu"));
													
													if(dbLocal.insert("pub_usu", null, ctvValores) < 0){
														erro = "Falha ao adicionar usuário.";
														return false;
													}
													
													publishProgress();
												}while(rsTmp.next());
											}
											
											rsTmp = Geral.dbCn.retReg("select cd_emp, cd_term, nm_term from pub_term");
											if(rsTmp != null){
												rsTmp.last();
												preparaPrgDialog("Cadastrando terminais...", rsTmp.getRow());
												rsTmp.first();
												
												do{
													ctvValores = new ContentValues();
													ctvValores.put("cd_emp", rsTmp.getInt("cd_emp"));
													ctvValores.put("cd_term", rsTmp.getInt("cd_term"));
													ctvValores.put("nm_term", rsTmp.getString("nm_term"));
													
													if(dbLocal.insert("pub_term", null, ctvValores) < 0){
														erro = "Falha ao adicionar terminal.";
														return false;
													}
													
													publishProgress();
												}while(rsTmp.next());
											}
											
											rsTmp = Geral.dbCn.retReg("select cd_emp, cd_prod, ds_prod from pub_prod");
											if(rsTmp != null){
												rsTmp.last();
												preparaPrgDialog("Cadastrando produtos...", rsTmp.getRow());
												rsTmp.first();
												
												do{
													ctvValores = new ContentValues();
													ctvValores.put("cd_emp", rsTmp.getInt("cd_emp"));
													ctvValores.put("cd_prod", rsTmp.getInt("cd_prod"));
													ctvValores.put("ds_prod", rsTmp.getString("ds_prod"));
													
													if(dbLocal.insert("pub_prod", null, ctvValores) < 0){
														erro = "Falha ao adicionar produto.";
														return false;
													}
													
													publishProgress();
												}while(rsTmp.next());
											}
											
											rsTmp = Geral.dbCn.retReg("select cd_cid, ds_cid, uf_cid from pub_cid");
											if(rsTmp != null){
												rsTmp.last();
												preparaPrgDialog("Cadastrando cidades...", rsTmp.getRow());
												rsTmp.first();
												
												do{
													ctvValores = new ContentValues();
													ctvValores.put("cd_cid", rsTmp.getInt("cd_cid"));
													ctvValores.put("ds_cid", rsTmp.getString("ds_cid"));
													ctvValores.put("uf_cid", rsTmp.getString("uf_cid"));
													
													if(dbLocal.insert("pub_cid", null, ctvValores) < 0){
														erro = "Falha ao adicionar cidade.";
														return false;
													}
													
													publishProgress();
												}while(rsTmp.next());										
											}
											
											rsTmp = Geral.dbCn.retReg("select p.cd_emp, p.cd_pes, nm_razao_pes, nm_fants_pes, cpf_cgc_pes, placa_veic_pes, uf_placa_veic_pes, cd_cid_placa," +
															  " coalesce((select nr_tel_pes from pub_pes_tel t where t.cd_emp = p.cd_emp and t.cd_pes = p.cd_pes limit 1), '') as fone," +
															  " tp_pes_prdt, tp_pes_dest, tp_pes_motr, tp_pes_carg, tp_pes_emit, 1, st_reg, cd_ver, cd_term, email_pes," +
															  " coalesce(tp_lograd || ' ' || lograd, '') as endereco, coalesce(nr_cp_end, 0) as nr_cp_end, coalesce(compl_cp_end, '') as compl_cp_end," +
															  " coalesce(cep_pes_cp_end, '') as cep_pes_cp_end, coalesce(bairro_cp_end, '') as bairro_cp_end, coalesce(cd_cid, 0) as cd_cid" +
															  " from pub_pes p left join pub_pes_cp_end e" +
															  " on p.cd_emp = e.cd_emp" +
															  " and p.cd_pes = e.cd_pes" +
															  " and e.cd_seq_cp_end = 1");
											
											if(rsTmp != null){
												rsTmp.last();
												preparaPrgDialog("Cadastrando pessoas...", rsTmp.getRow());
												rsTmp.first();
												
												do{
													ctvValores = new ContentValues();
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
													
													if(dbLocal.insert("pub_pes", null, ctvValores) < 0){
														erro = "Falha ao adicionar pessoa.";
														return false;
													}
													
													publishProgress();
												}while(rsTmp.next());
											}
											
											rsTmp = Geral.dbCn.retReg("select * from ctrl_ent");
											if(rsTmp != null){
												rsTmp.last();
												preparaPrgDialog("Cadastrando entradas...", rsTmp.getRow());
												rsTmp.first();
												
												do{
													ctvValores = new ContentValues();
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
													ctvValores.put("emitir_nf", rsTmp.getInt("emitir_nf"));
													
													if(dbLocal.insert("ctrl_ent", null, ctvValores) < 0){
														erro = "Falha ao adicionar movimentação.";
														return false;
													}
													
													publishProgress();
												}while(rsTmp.next());
											}
											
											rsTmp = Geral.dbCn.retReg("select * from ctrl_ban");
											if(rsTmp != null){
												rsTmp.last();
												preparaPrgDialog("Cadastrando bancário...", rsTmp.getRow());
												rsTmp.first();
												
												do{
													ctvValores = new ContentValues();
													ctvValores.put("cd_emp", rsTmp.getInt("cd_emp"));
													ctvValores.put("cd_lanc", rsTmp.getInt("cd_lanc"));
													ctvValores.put("dt_lanc", rsTmp.getDate("dt_lanc").toString());
													ctvValores.put("tp_lanc", rsTmp.getInt("tp_lanc"));
													ctvValores.put("st_lanc", rsTmp.getInt("st_lanc"));
													ctvValores.put("cd_pes", rsTmp.getInt("cd_pes"));
													ctvValores.put("nm_pes", rsTmp.getString("nm_pes"));
													ctvValores.put("vl_lanc", rsTmp.getDouble("vl_lanc"));
													ctvValores.put("obs_lanc", rsTmp.getString("obs_lanc"));
													ctvValores.put("cd_usu", rsTmp.getInt("cd_usu"));
													ctvValores.put("cd_term", rsTmp.getInt("cd_term"));
													ctvValores.put("st_reg", 1);
													ctvValores.put("cd_ver", rsTmp.getInt("cd_ver"));
													
													if(dbLocal.insert("ctrl_ban", null, ctvValores) < 0){
														erro = "Falha ao adicionar movimentação.";
														return false;
													}
													
													publishProgress();
												}while(rsTmp.next());
											}
											
											return true;
											
										}catch(Exception e){
											if(Geral.dbCn != null){
												Geral.dbCn.rollback();
											}
											
											erro = new String(e.getMessage());
											return false;
										}finally{		
											if(Geral.dbCn != null)
												Geral.dbCn.fechaConexao();
											
											if(Geral.localDB != null)
												if(Geral.localDB.isOpen()) Geral.localDB.close();
											
											Geral.dbCn = null;
											Geral.localDB = null;
										}
									}
									
									@Override
									protected void onProgressUpdate(Void... values) {
										if(prgDialog.isShowing())
											prgDialog.incrementProgressBy(1);
									}
									
									@Override
									protected void onPostExecute(Boolean result) {
										if(!result){
											Funcoes.msgBox("Falha ao criar banco local." + erro, activity);
											File dbPath = new File(Environment.getExternalStorageDirectory() + "/Melancia Sys/" + edtNmBanco.getText().toString() + "_local.db");
											if(dbPath.exists())
												dbPath.delete();
										}else{
											Funcoes.msgBox("Banco local criado com sucesso!", activity);
										}
										
										if(prgDialog.isShowing())
											prgDialog.dismiss();
									}
									
								}.execute();
							}
							
						};
						
						File dbPath = new File(Environment.getExternalStorageDirectory() + "/Melancia Sys/" + edtNmBanco.getText().toString() + "_local.db");
						if(dbPath.exists()){
							Funcoes.pergunta("Já existe um banco local para esta conexão, deseja sobrescrevê-lo?", activity, okListener, null);
						}else{
							okListener.onClick(null, 0);
						}
							
					}
				}catch(Exception e){
					Funcoes.msgBox("Falha ao criar banco local." + e.getMessage(), activity);
				}
				
				return false;
			}
		});
		
		return true;
		
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if(menu != null){
			if(((ItemSpinner) spnConexao.getSelectedItem()).getCodigo() < 0){
				menu.getItem(3).setEnabled(false);
				menu.getItem(4).setEnabled(false);
			}else{
				menu.getItem(3).setEnabled(true);
				menu.getItem(4).setEnabled(true);
			}
		}
		
		return super.onMenuOpened(featureId, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
