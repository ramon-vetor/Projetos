package com.melancia;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.melancia.Funcoes.*;
import com.melancia.Geral.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	Activity activity;
	
	static Spinner spnConexao;
	Spinner spnEmp;
	Spinner spnUsu;
	EditText edtSenha;
	Button btnEntrar;
	Button btnSair;
	
	static List<ItemSpinner> lstCns;
	static ArrayAdapter<ItemSpinner> adapCns;
	ArrayList<Configuracao> configs = new ArrayList<Geral.Configuracao>();
	
	boolean flag = false;
	
	private class CarregaConexoes extends AsyncTask<Void, Void, Void> {
		
		ProgressDialog pgrDialog;
		Boolean existeConfig = false;
		String erro = "";

		ArrayList<Configuracao> configs;
		
		@SuppressWarnings("rawtypes")
		@Override
		protected void onPreExecute() {
			pgrDialog = ProgressDialog.show(activity, "", "Preparando Sistema...");
			
			if((ArrayAdapter)spnEmp.getAdapter() != null)
				((ArrayAdapter)spnEmp.getAdapter()).clear();
			
			if((ArrayAdapter)spnUsu.getAdapter() != null)
				((ArrayAdapter)spnUsu.getAdapter()).clear();
			
			edtSenha.setText("");
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(Void... params) {
			try {
				File arqConf = getFileStreamPath("configuracoes.xml");

				if (arqConf.exists()) {
					FileInputStream xmlConfig = openFileInput("configuracoes.xml");
					XStream xmlStream = new XStream(new DomDriver());

					xmlStream.alias("configuracoes", Geral.class);
					
					configs = new ArrayList<Configuracao>();
					configs = (ArrayList<Configuracao>) xmlStream.fromXML(xmlConfig);
					
					if(configs != null){
						if(configs.size() > 0){
							existeConfig = true;
						}
					}
				}	
			} catch (Exception e) {
				erro = e.getMessage();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			try {
				if (!erro.equals(""))
					throw new Exception(erro);
					
				adapCns.clear();
				lstCns.clear();
				lstCns.add(new ItemSpinner("Selecione uma Conexão", -1));
					
				if (!existeConfig){
					if(!flag){
						Intent intConfig = new Intent(activity, ConfigActivity.class);
						activity.startActivityForResult(intConfig, 1);
						flag = true;
					}
					return;
				}else{
					for (int i = 0; i < configs.size(); i++) {
						lstCns.add(new ItemSpinner(configs.get(i).getNmConexao(), configs.get(i).getCdConexao()));
					}
				}
				
				adapCns.notifyDataSetChanged();
				spnConexao.setSelection(0);
				
			} catch (Exception e) {
				Funcoes.msgBox("Ocorreu um erro ao preparar o sistema." + e.getMessage(), activity);
			} finally{
				if (pgrDialog.isShowing())
					pgrDialog.dismiss();
			}
		};
		
	
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		activity = this;
		
		try {
			
			spnConexao = (Spinner) findViewById(R.id.spnConexao);
			spnEmp = (Spinner) findViewById(R.id.spnEmpresa);
			spnUsu = (Spinner) findViewById(R.id.spnUsuario);
			edtSenha = (EditText) findViewById(R.id.edtTermDest);
			btnEntrar = (Button) findViewById(R.id.btnEntrar);
			btnSair = (Button) findViewById(R.id.btnSair);
			
			lstCns = new ArrayList<ItemSpinner>();
			adapCns = new ArrayAdapter<ItemSpinner>(this, android.R.layout.simple_spinner_item, lstCns);
			adapCns.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnConexao.setAdapter(adapCns);
			
			spnConexao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@SuppressWarnings("rawtypes")
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					try {
						
						if((ArrayAdapter)spnEmp.getAdapter() != null)
							((ArrayAdapter)spnEmp.getAdapter()).clear();
						
						if((ArrayAdapter)spnUsu.getAdapter() != null)
							((ArrayAdapter)spnUsu.getAdapter()).clear();
						
						edtSenha.setText("");
						
						if(Geral.localDB != null){
							if(Geral.localDB.isOpen())
								Geral.localDB.close();
						}
						
						if(spnConexao.getSelectedItem() == null) return;
						if(!spnConexao.getSelectedItem().getClass().equals(ItemSpinner.class)) return;
						
						if(((ItemSpinner) spnConexao.getSelectedItem()).getCodigo() == -1) return;
						
						new AsyncTask<Void, Void, Void>() {
	
							ProgressDialog pgrDialog = null;
							String erro = "";
	
							@Override
							protected void onPreExecute() {
								pgrDialog = ProgressDialog.show(activity, "", "Conectando...");
							}
	
							@SuppressWarnings({ "unchecked", "deprecation" })
							@Override
							protected Void doInBackground(Void... params) {
								try {
									File arqConf = getFileStreamPath("configuracoes.xml");
	
									if (arqConf.exists()) {
	
										FileInputStream xmlConfig = openFileInput("configuracoes.xml");
										XStream xmlStream = new XStream(new DomDriver());
	
										xmlStream.alias("configuracoes", Geral.class);
										ArrayList<Configuracao> configs = (ArrayList<Configuracao>) xmlStream.fromXML(xmlConfig);
										
										for(int i = 0; i < configs.size(); i++){
											if (configs.get(i).getCdConexao() == ((ItemSpinner)spnConexao.getSelectedItem()).getCodigo()){
												Geral.config = configs.get(i);
											}
										}
										
										if (Geral.config == null){
											throw new Exception("Parâmetros de conexão não encontrados.");
										}
										
										File pathDB = new File(Environment.getExternalStorageDirectory() + "/Melancia Sys/" + Geral.config.getNomeDb() + "_local.db");
										if(!pathDB.exists())
											throw new Exception("Banco de dados local não encontrado.");
										
										Geral.localDB = openOrCreateDatabase(pathDB.getPath(), Context.MODE_WORLD_WRITEABLE, null);
										
										if(!Geral.localDB.isOpen())
											throw new Exception("Falha ao abrir conexção com banco local.");

										Geral.atualizaDB();
										
										configs = null;
									} else {
										erro = "Parâmetros de conexão não configurados.";
									}
									
								} catch (Exception e) {
									erro = e.getMessage();
								}
								
								return null;
							}
	
							@Override
							protected void onPostExecute(Void result) {
								try {
									if (!erro.equals("")) {
										throw new Exception(erro);
									}
								
									Funcoes.preencheSpinnerLocal(LoginActivity.this, null, R.id.spnEmpresa, "Empresa", "pub_emp", null, null, "cd_emp", "nm_emp", "cd_emp");
									Funcoes.preencheSpinnerLocal(LoginActivity.this, null, R.id.spnUsuario, "Usuário", "pub_usu", null, null, "nm_usu", "nm_usu", "cd_usu");
	
								} catch (Exception e) {
									pgrDialog.dismiss();
									Funcoes.msgBox("Erro ao abrir sistema." + e.getMessage(), activity);
									Funcoes.selItemSpinner(activity, null, R.id.spnConexao, -1, true);
								} finally {
									if (pgrDialog.isShowing())
										pgrDialog.dismiss();
								}
							}
	
						}.execute();
					
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao carregar dados da configuração.", activity);
					}
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
				
			});
			
			btnEntrar.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						
						if (spnConexao.getSelectedItem() == null){
							Funcoes.msgBox("Selecione uma conexão.", activity);
							spnConexao.requestFocus();
							return;
						}else if (spnEmp.getSelectedItem() == null){
							Funcoes.msgBox("Selecione uma empresa.",  activity);
							spnEmp.requestFocus();
							return;
						}else if (spnUsu.getSelectedItem() == null) {
							Funcoes.msgBox("Selecione um usuário.", activity);
							spnUsu.requestFocus();
							return;
						}else if (edtSenha.getText().toString().equals("")){
							Funcoes.msgBox("Informe a senha do usuário.", activity);
							edtSenha.requestFocus();
							return;
						}else{
							
							new AsyncTask<Void, Void, String>(){
								ProgressDialog prgDialog = null;
								
								@Override
								protected void onPreExecute() {
									prgDialog = ProgressDialog.show(activity, "", "Efetuando login...");
								}

								@Override
								protected String doInBackground(Void... params) {
									try {
										
										String cdUsu = String.valueOf(((ItemSpinner) spnUsu.getSelectedItem()).getCodigo());
										if (edtSenha.getText().toString().equals(Funcoes.decrypt(Funcoes.vlCampoTblLocal("senha_usu", "pub_usu", "cd_usu = " + cdUsu).toString()))) {
											WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
											String macAddress = manager.getConnectionInfo().getMacAddress();

											if (macAddress != null) {
												String criterio = "cd_emp = " + ((ItemSpinner) spnEmp.getSelectedItem()).getCodigo() + " and nm_term = '" + macAddress + "'";
												Geral.terminal = (Byte.parseByte(Funcoes.vlCampoTblLocal("coalesce(max(cd_term), 0) as cd_term", "pub_term", criterio).toString()));
												if (Geral.terminal == 0) {
													return "Dispositivo não cadastrado no sistema, efetue o cadastro do terminal com o nome: " + macAddress;
												}
											}else{
												Geral.terminal = 1;
											}
											
											Geral.cdEmpresa = (byte) ((ItemSpinner) spnEmp.getSelectedItem()).getCodigo();
											Geral.cdUsuario = (byte) ((ItemSpinner) spnUsu.getSelectedItem()).getCodigo();
											Geral.usuarioAdm = Funcoes.toInt(Funcoes.vlCampoTblLocal("usu_adm", "pub_usu", "cd_usu = " + Geral.cdUsuario)) == 1;
											
											//Intent intMov = new Intent(activity, TabMovActivity.class);
											Intent intMov = new Intent(activity, PrincipalActivity.class);
											activity.startActivity(intMov);
											
											return "";
										} else {
											return "Senha incorreta.";
										}

									} catch (Exception e) {
										return e.getMessage();
									}
								}
								
								@Override
								protected void onPostExecute(String result) {
									if (prgDialog.isShowing())
										prgDialog.dismiss();
									
									if(!result.equals("")){
										Funcoes.msgBox("Falha ao efetuar login." + result, activity);
									}else{
										edtSenha.setText("");
									}
								}
								
							}.execute();
						}
						
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao entrar no sistema." + e.getMessage(), activity);
					}
				}
			});
			
			btnSair.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Funcoes.pergunta("Deseja realmente sair do sistema?", activity, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(Geral.localDB != null)
								if(Geral.localDB.isOpen())
									Geral.localDB.close();
								
							activity.finish();
						}
					}, null);
				}
			});
			
			edtSenha.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if(actionId == EditorInfo.IME_ACTION_DONE)
						btnEntrar.performClick();
					
					return true;
				}
			});
			
			new CarregaConexoes().execute();
			
		} catch (Exception e) {
			Funcoes.msgBox("Erro ao abrir sistema." + e.getMessage(), activity);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuItem mnuConfig = menu.add("Configurações");
		mnuConfig.setIcon(android.R.drawable.ic_menu_preferences);
		mnuConfig.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intConfig = new Intent(activity, ConfigActivity.class);
				activity.startActivityForResult(intConfig, 1);
				flag = true;
				return false;
			}
		});
		
		MenuItem mnuSobre = menu.add("Informações");
		mnuSobre.setIcon(android.R.drawable.ic_menu_info_details);
		mnuSobre.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@SuppressLint("InflateParams")
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				View vSobre = LoginActivity.this.getLayoutInflater().inflate(R.layout.dialog_about, null);
				
				new AlertDialog.Builder(activity)
				.setView(vSobre)
				.setTitle(R.string.app_name)
				.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
					}
				})
				.create().show();
				
				return false;
			}
		});
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 1)
			new CarregaConexoes().execute();

	}
	
}
