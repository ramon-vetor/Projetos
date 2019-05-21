package com.melancia;

import java.util.ArrayList;
import com.melancia.Funcoes.ItemSpinner;
import com.melancia.controles.CEditText;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class CadPesActivity extends Activity {
	
	private static int ST_REG_INCLUINDO = 1;
	private static int ST_REG_EDITANDO = 2;
	//private static int ST_REG_VISUALIZANDO = 3;
	
	private int stRegistro;
	
	CEditText edtTermPes;
	CEditText edtCdPes;
	CEditText edtNmRazao;
	CEditText edtNmFantasia;
	CEditText edtCpfCgc;
	CEditText edtEmail;
	CEditText edtTelefone;
	CEditText edtEndereco;
	CEditText edtNumero;
	CEditText edtComplemento;
	CEditText edtCEP;
	CEditText edtBairro;
	Spinner spnUf;
	Spinner spnCidade;
	CheckBox chkTpPesPrdt;
	CheckBox chkTpPesDest;
	CheckBox chkTpPesMotr;
	CheckBox chkTpPesCarg;
	CheckBox chkTpPesEmit;
	CEditText edtPlaca;
	Spinner spnUfPlaca;
	Spinner spnCidPlaca;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cad_pes);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setTitle("Cadastro de Pessoas");
	    
		try {
			edtTermPes = (CEditText) findViewById(R.id.edtTermPes);
			edtCdPes = (CEditText) findViewById(R.id.edtCdPes);
			edtNmRazao = (CEditText) findViewById(R.id.edtRazaoSocial);
			edtNmFantasia = (CEditText) findViewById(R.id.edtNmFants);
			edtCpfCgc = (CEditText) findViewById(R.id.edtCPFCGC);
			edtEmail = (CEditText) findViewById(R.id.edtEmail);
			edtTelefone = (CEditText) findViewById(R.id.edtTelefone);
			edtEndereco = (CEditText) findViewById(R.id.edtEndereco);
			edtNumero = (CEditText) findViewById(R.id.edtNumero);
			edtComplemento = (CEditText) findViewById(R.id.edtComplemento);
			edtCEP = (CEditText) findViewById(R.id.edtCEP);
			edtBairro = (CEditText) findViewById(R.id.edtBairro);
			spnUf = (Spinner) findViewById(R.id.spnUFEnd);
			spnCidade = (Spinner) findViewById(R.id.spnCidade);
			chkTpPesPrdt = (CheckBox) findViewById(R.id.chkTpPesPrdt);
			chkTpPesDest = (CheckBox) findViewById(R.id.chkTpPesDest);
			chkTpPesMotr = (CheckBox) findViewById(R.id.chkTpPesMotr);
			chkTpPesCarg = (CheckBox) findViewById(R.id.chkTpPesCarg);
			chkTpPesEmit = (CheckBox) findViewById(R.id.chkTpPesEmitChq);
			edtPlaca = (CEditText) findViewById(R.id.edtPlaca);
			spnUfPlaca = (Spinner) findViewById(R.id.spnUfPlaca);
			spnCidPlaca = (Spinner) findViewById(R.id.spnCidPlaca);
			
			edtTelefone.setMask("(99)99999-9999");
			edtCEP.setMask("99.999-999");
			edtPlaca.setMask("AAA - 9999");
			
			spnUf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					if(spnUf.getSelectedItem() != null){
						Funcoes.preencheSpinnerLocal(CadPesActivity.this, null, R.id.spnCidade, "Cidade", "pub_cid", "uf_cid = ?", new String[]{spnUf.getSelectedItem().toString()}, "ds_cid", "ds_cid", "cd_cid");
					}
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
			
			spnUfPlaca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					if(spnUfPlaca.getSelectedItem() != null){
						Funcoes.preencheSpinnerLocal(CadPesActivity.this, null, R.id.spnCidPlaca, "Cidade PLaca", "pub_cid", "uf_cid = ?", new String[]{spnUfPlaca.getSelectedItem().toString()}, "ds_cid", "ds_cid", "cd_cid");
					}
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
			
			edtTermPes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus){
						if(edtTermPes.getInt() == 0){
							edtTermPes.setText(Funcoes.formata(Geral.terminal, 2));
							edtTermPes.selectAll();
						}
					}
				}
			});
			
			edtTermPes.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					try{
						if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
							if(edtTermPes.getInt() == 0){
								edtTermPes.setText(Funcoes.formata(Geral.terminal, 2));
							}else
								edtTermPes.setText(Funcoes.formata(edtTermPes.getInt(), 2));
							
							if(edtTermPes.getInt() != edtTermPes.getCdAnterior()){
								String criterio = "cd_emp = " + Geral.cdEmpresa + " and cd_pes between " + edtTermPes.getInt() + "0000 and " + edtTermPes.getInt() + "9999";
								edtCdPes.setText(Funcoes.formata(Funcoes.proximoNumeroLocal("cd_pes", "pub_pes", criterio), 6).substring(2));
								edtCdPes.setCdAnterior(Funcoes.toLong(edtCdPes.getString()));
							}
						}
					} catch(Exception e){
						Funcoes.msgBox("Falha ao buscar próximo código de cadastro.", CadPesActivity.this);
					}
					
					return false;
				}
				
			});
			
			edtCdPes.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					try{
						if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
							edtCdPes.formata(4);
							
							if(edtCdPes.getLong() != edtCdPes.getCdAnterior()){
								if(edtTermPes.getInt() > 0 && edtCdPes.getInt() > 0){
									long cdPes = Funcoes.toLong(edtTermPes.getString() + edtCdPes.getString());
									
									if (Funcoes.toLong(Funcoes.vlCampoTblLocal("cd_pes", "pub_pes", "cd_emp = " + Geral.cdEmpresa + " and cd_pes = " + cdPes).toString()) > 0){
										mostraPessoa(cdPes);
									}else{
										Funcoes.msgBox("Nenhum registro encontrado com este código.", CadPesActivity.this);
										Funcoes.setaFoco(edtCdPes);
									}
								}else{
									novoRegistro();
								}
							}
						}
					}catch (Exception e){
						Funcoes.msgBox("Falha ao consultar pessoa." + e.getMessage(), CadPesActivity.this);
					}
					return false;
				}
			});
			
			edtNmRazao.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
						edtNmRazao.setText(edtNmRazao.getString().trim());
						if(edtNmFantasia.getString().trim().equals("")) edtNmFantasia.setText(edtNmRazao.getString());
					}
					return false;
				}
			});
			
			edtCpfCgc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
						String cpfCgc = Funcoes.separaNumeros(edtCpfCgc.getString());
						if(!cpfCgc.equals("")){
							switch (cpfCgc.length()) {
							case 11:
								edtCpfCgc.setText(cpfCgc.replaceAll("([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})", "$1.$2.$3-$4"));
								break;
							case 14:
								edtCpfCgc.setText(cpfCgc.replaceAll("([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})", "$1.$2.$3/$4-$5"));
								break;
							default:
								Funcoes.msgBox("CPF ou CGC inválido.", CadPesActivity.this);
								Funcoes.setaFoco(edtCpfCgc);
								return false;
							}
						}
					}
					
					try{
						final long cdPes = Funcoes.toLong(Funcoes.vlCampoTblLocal("cd_pes", "pub_pes", "cd_emp = " + Geral.cdEmpresa + " and cpf_cgc_pes = '" + edtCpfCgc.getString() + "'"));
						if(cdPes > 0){
							Funcoes.pergunta("Já existe uma pessoa cadastrada com este CPF/CGC.Deseja visualiza-la?", CadPesActivity.this, new DialogInterface.OnClickListener(){
								@Override
								public void onClick(DialogInterface dialog, int which) {
									mostraPessoa(cdPes);
								}
								
							}, null);
						}
					}catch(Exception e){
						Funcoes.msgBox("Falha ao consultar CPF/CGC no banco de dados." + e.getMessage(), CadPesActivity.this);
					}
					
					return false;
				}
			});
			
			edtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
						edtEmail.setText(edtEmail.getString().trim());
						if(!edtEmail.getString().equals("") && !edtEmail.getString().contains("@")){
							Funcoes.msgBox("E-mail inválido.", CadPesActivity.this);
							Funcoes.setaFoco(edtEmail);
						}
					}
					return false;
				}
			});
			
			edtTelefone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
						String fone = Funcoes.separaNumeros(edtTelefone.getString());
						if(!fone.equals("") && (fone.length() != 10 && fone.length() != 11)){
							Funcoes.msgBox("Telefone inválido.", CadPesActivity.this);
							Funcoes.setaFoco(edtTelefone);
						}
					}
					return false;
				}
			});
			
			edtNumero.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
						edtNumero.formata(4);
					}
					return false;
				}
				
			});
			
			edtCEP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
						String cep = edtCEP.getString();
						cep = Funcoes.separaNumeros(cep);
						if(!cep.equals("") && cep.length() != 8){
							Funcoes.msgBox("CEP inválido.", CadPesActivity.this);
							Funcoes.setaFoco(edtCEP);
						}
					}
					return false;
				}
			});
			
			chkTpPesMotr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					edtPlaca.setEnabled(isChecked);
					edtPlaca.setText("");
					
					Funcoes.selItemSpinner(CadPesActivity.this, null, R.id.spnUfPlaca, "--", true);
					spnUfPlaca.setEnabled(isChecked);
					spnCidPlaca.setEnabled(isChecked);
				}
			});
			
			novoRegistro();
			
		}catch(Exception e){
			Funcoes.msgBox("Falha ao preparar tela." + e.getMessage(), CadPesActivity.this, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem mnuNovo = menu.add("Novo Registro");
		mnuNovo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				novoRegistro();
				return false;
			}
		});
		
		MenuItem mnuSalvar = menu.add("Salvar");
		mnuSalvar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if(stRegistro == ST_REG_INCLUINDO){
					incluiRegistro();
				}else if(stRegistro == ST_REG_EDITANDO){
					alteraRegistro();
				}
				
				return false;
			}
		});
		
		MenuItem mnuExcluir = menu.add("Excluir");
		mnuExcluir.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				excluiRegistro();
				return false;
			}
		});
		
		MenuItem mnuPesquisar = menu.add("Pesquisar");
		mnuPesquisar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			ItemSpinner selecionado;
			
			@SuppressLint("InflateParams")
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				try{
					final View vPesq = ((LayoutInflater) CadPesActivity.this.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pesquisa, null, false);
					
					final Spinner spnFiltro = (Spinner) vPesq.findViewById(R.id.spnFiltro);
					final EditText edtPesq = (EditText) vPesq.findViewById(R.id.edtPesq);
					final ListView lstResult = (ListView) vPesq.findViewById(R.id.lstResult);
					
					ArrayList<ItemSpinner> lstFiltro = new ArrayList<ItemSpinner>();
					lstFiltro.add(new ItemSpinner("Nome fantasia", 0, "nm_fants_pes"));
					lstFiltro.add(new ItemSpinner("Razao Social", 1, "nm_razao_pes"));
					
					Funcoes.preencheSpinner(CadPesActivity.this, vPesq, R.id.spnFiltro, "Filtrar por", lstFiltro);
					spnFiltro.setClickable(true);
					
					edtPesq.addTextChangedListener(new TextWatcher() {
						
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {}
						
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
						
						@SuppressLint("DefaultLocale")
						@Override
						public void afterTextChanged(Editable s) {

							if (s.length() > 0) {
								
								try {
									String filtro = ((ItemSpinner) spnFiltro.getSelectedItem()).getTag();
									String nome = s.toString().toUpperCase().replaceAll("'", "");
									
									String sql = "select pes.cd_pes, " + filtro  +", (case when " + filtro +" like '" + nome + "%' then 0 else 1 end) as indice"
											   + " from pub_pes pes"
											   + " where pes.cd_emp = " + Geral.cdEmpresa
											   + " and pes.st_pes <> 2"
											   + " and "+ filtro +" like '%" + nome + "%'"
											   + " order by indice, " + filtro + "  limit 20";
									
									Cursor crsResult = Geral.localDB.rawQuery(sql, null);
									if(crsResult.getCount() > 0){
										Funcoes.preencheListViewLocal(CadPesActivity.this, vPesq, R.id.lstResult, crsResult, filtro, "cd_pes");
										crsResult.close();
									}
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao realizar consulta." + e.getMessage(), CadPesActivity.this);
								}
							}
						}
					});
					
					lstResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							selecionado = (ItemSpinner) arg0.getItemAtPosition(arg2);
						}
					});
					
					new AlertDialog.Builder(CadPesActivity.this)
							.setTitle("Pesquisa de Pessoas")
							.setView(vPesq)
							.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									if(selecionado != null){
										edtTermPes.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(0, 2));
										edtCdPes.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(2));
										edtCdPes.onEditorAction(EditorInfo.IME_ACTION_NEXT);
									}									
								}
							})
							.setNegativeButton("Cancelar", null)
							.show().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					
					Funcoes.setaFoco(edtPesq);
					
				}catch(Exception e){
					Funcoes.msgBox("Falha ao pesquisar pessoas." + e.getMessage(), CadPesActivity.this);
				}
				
				return false;
			}
			
		});
		
		return true;
	}

	private void limpaTela(){
		edtTermPes.setText(Funcoes.formata(Geral.terminal, 2));
		edtCdPes.setText("");
		edtNmRazao.setText("");
		edtNmFantasia.setText("");
		edtCpfCgc.setText("");
		edtEmail.setText("");
		edtTelefone.setText("");
		edtEndereco.setText("");
		edtNumero.setText("");
		edtComplemento.setText("");
		edtCEP.setText("");
		edtBairro.setText("");
		
		Funcoes.selItemSpinner(CadPesActivity.this, null, R.id.spnUFEnd, "--", true);
		
		chkTpPesCarg.setChecked(false);
		chkTpPesDest.setChecked(false);
		chkTpPesEmit.setChecked(false);
		chkTpPesMotr.setChecked(false);
		chkTpPesPrdt.setChecked(false);
		
		edtPlaca.setText("");
		edtPlaca.setEnabled(false);
		spnUfPlaca.setEnabled(false);
		spnCidPlaca.setEnabled(false);
		
		Funcoes.selItemSpinner(CadPesActivity.this, null, R.id.spnUfPlaca, "--", true);
		
	
	}
	
	private void novoRegistro(){
		limpaTela();
		
		try{
			String criterio = "cd_emp = " + Geral.cdEmpresa + " and cd_pes between " + Geral.terminal + "0000 and " + Geral.terminal + "9999";
			long cdPes = Funcoes.proximoNumeroLocal("cd_pes", "pub_pes", criterio);
			if(cdPes == 1)
				cdPes = Funcoes.toInt(Geral.terminal + "0001");
			
			edtCdPes.setText(Funcoes.formata(cdPes, 6).substring(2));
			edtCdPes.setCdAnterior(edtCdPes.getLong());
			
			stRegistro = ST_REG_INCLUINDO;
			Funcoes.setaFoco(edtCdPes);
			edtCdPes.selectAll();
			
		}catch(Exception e){
			Funcoes.msgBox("Falha ao preparar tela para inclusão." + e.getMessage(), CadPesActivity.this);
		}
	}
	
	private void mostraPessoa(final long cdPes){
		try {
			new AsyncTask<Void, Void, Boolean>() {
				ProgressDialog prgDlg = null;
				String erro = "";
				Cursor crsTmp = null;
				
				@Override
				protected void onPreExecute() {
					prgDlg = ProgressDialog.show(CadPesActivity.this, "", "Carregando...");
				}
				
				@Override
				protected Boolean doInBackground(Void... params) {
					try {
						String sql = "select pub_pes.*, pub_cid.uf_cid as uf_end" +
								" from pub_pes left join pub_cid" +
								" on pub_pes.cd_cid_end = pub_cid.cd_cid" +
								" where cd_emp = " + Geral.cdEmpresa +
								" and cd_pes = " + cdPes;
						
						crsTmp = Geral.localDB.rawQuery(sql, null);
						
						if(crsTmp == null){
							erro = "Registro não encontrado.";
							return false;
						}
						
						return true;
					} catch (Exception e) {
						erro = e.getMessage();
						return false;
					}
				}
				
				@Override
				protected void onPostExecute(Boolean result) {
					try {
						if(!result || crsTmp == null)
							throw new RuntimeException(erro);
							
						crsTmp.moveToFirst();
						
						if(crsTmp.getInt(crsTmp.getColumnIndex("st_reg")) == 2){
							Funcoes.msgBox("Cadastro cancelado.Visualização não disponível.", CadPesActivity.this);
							novoRegistro();
							return;
						}
						
						limpaTela();
						
						edtTermPes.setText(Funcoes.formata(cdPes, 6).substring(0, 2));
						edtCdPes.setText(Funcoes.formata(cdPes, 6).substring(2));
						edtNmRazao.setText(crsTmp.getString(crsTmp.getColumnIndex("nm_razao_pes")));
						edtNmFantasia.setText(crsTmp.getString(crsTmp.getColumnIndex("nm_fants_pes")));
						edtCpfCgc.setText(crsTmp.getString(crsTmp.getColumnIndex("cpf_cgc_pes")));
						edtEmail.setText(crsTmp.getString(crsTmp.getColumnIndex("email_pes")));
						edtTelefone.setText(crsTmp.getString(crsTmp.getColumnIndex("fone_pes")));
						edtEndereco.setText(crsTmp.getString(crsTmp.getColumnIndex("endereco_pes")));
						edtNumero.setText(Funcoes.formata(crsTmp.getInt(crsTmp.getColumnIndex("numero_end")), 4));
						edtComplemento.setText(crsTmp.getString(crsTmp.getColumnIndex("compl_end")));
						edtCEP.setText(crsTmp.getString(crsTmp.getColumnIndex("cep_end")));
						edtBairro.setText(crsTmp.getString(crsTmp.getColumnIndex("bairro_end")));
						
						Funcoes.selItemSpinner(CadPesActivity.this, null, R.id.spnUFEnd, crsTmp.getString(crsTmp.getColumnIndex("uf_end")), true);
						Funcoes.selItemSpinner(CadPesActivity.this, null, R.id.spnCidade, crsTmp.getInt(crsTmp.getColumnIndex("cd_cid_end")), true);
						
						chkTpPesCarg.setChecked(crsTmp.getInt(crsTmp.getColumnIndex("tp_pes_carg")) > 0);
						chkTpPesDest.setChecked(crsTmp.getInt(crsTmp.getColumnIndex("tp_pes_dest")) > 0);
						chkTpPesEmit.setChecked(crsTmp.getInt(crsTmp.getColumnIndex("tp_pes_emit")) > 0);
						chkTpPesMotr.setChecked(crsTmp.getInt(crsTmp.getColumnIndex("tp_pes_motr")) > 0);
						chkTpPesPrdt.setChecked(crsTmp.getInt(crsTmp.getColumnIndex("tp_pes_prdt")) > 0);
						
						edtPlaca.setText(crsTmp.getString(crsTmp.getColumnIndex("placa_veic")));
						edtPlaca.setEnabled(chkTpPesMotr.isChecked());
						spnUfPlaca.setEnabled(chkTpPesMotr.isChecked());
						spnCidPlaca.setEnabled(chkTpPesMotr.isChecked());
						
						String uf = getResources().getStringArray(R.array.uf_cid)[crsTmp.getInt(crsTmp.getColumnIndex("uf_placa")) + 1];
						Funcoes.selItemSpinner(CadPesActivity.this, null, R.id.spnUfPlaca, uf, true);
						Funcoes.selItemSpinner(CadPesActivity.this, null, R.id.spnCidPlaca, crsTmp.getInt(crsTmp.getColumnIndex("cd_cid")), true);
						
						Funcoes.setaFoco(edtCdPes);
						stRegistro = ST_REG_EDITANDO;
						
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao consultar registro." + e.getMessage(), CadPesActivity.this);
						novoRegistro();
					} finally{
						if(prgDlg.isShowing())
							prgDlg.dismiss();
					}
				}
				
			}.execute();
			
		} catch (Exception e) {
			Funcoes.msgBox("Falha ao visualizar cadastro.", CadPesActivity.this);
			novoRegistro();
		}
	}

	private void incluiRegistro(){
		try {
			
			if(edtNmRazao.getString().trim().equals("")){
				Funcoes.msgBox("Informe o nome da pessoa.", CadPesActivity.this);
				Funcoes.setaFoco(edtNmRazao);
				return;
			}else{
				if(!chkTpPesCarg.isChecked() && !chkTpPesDest.isChecked() && !chkTpPesEmit.isChecked() && !chkTpPesMotr.isChecked() && !chkTpPesPrdt.isChecked()){
					Funcoes.msgBox("Selecione ao menos um tipo de pessoa.", CadPesActivity.this);
					return;
				}
			}
			
			Funcoes.pergunta("Deseja gravar o registro atual?", CadPesActivity.this, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						new AsyncTask<Void, Void, Boolean>(){
							ProgressDialog prgDialog;
							String erro = "";
							
							@Override
							protected void onPreExecute() {
								prgDialog = ProgressDialog.show(CadPesActivity.this, "", "Processando...");
							};
							
							@Override
							protected Boolean doInBackground(Void... params) {
								try{
									
									String criterio = "cd_emp = " + Geral.cdEmpresa + " and cd_pes between " + Geral.terminal + "0000 and " + Geral.terminal + "9999";
									long cdPes = Funcoes.proximoNumeroLocal("cd_pes", "pub_pes", criterio);
									if(cdPes == 1)
										cdPes = Funcoes.toInt(Geral.terminal + "0001");
									
									String cdCidEnd;
									String cdCidPlaca;
									
									if(spnCidade.getSelectedItem() != null){
										cdCidEnd = String.valueOf(((ItemSpinner) spnCidade.getSelectedItem()).getCodigo());
									}else{
										cdCidEnd = "null";
									}
									
									if(spnCidPlaca.getSelectedItem() != null){
										cdCidPlaca = String.valueOf(((ItemSpinner) spnCidPlaca.getSelectedItem()).getCodigo());
									}else{
										cdCidPlaca = "null";
									}
									
									String placa;
									if(edtPlaca.getString().trim().equals("")){
										placa = "    -     ";
									}else{
										placa = edtPlaca.getString();
									}	
									
									String[] vetCampos = new String[]{"CD_EMP", "CD_PES", "NM_RAZAO_PES", "NM_FANTS_PES", "EMAIL_PES", "ENDERECO_PES", 
											"NUMERO_END", "COMPL_END", "CEP_END", "BAIRRO_END", "CD_CID_END", "CPF_CGC_PES", "FONE_PES",
											"PLACA_VEIC", "UF_PLACA", "CD_CID", "TP_PES_PRDT", "TP_PES_DEST", "TP_PES_CARG", "TP_PES_MOTR", "TP_PES_EMIT", "CD_TERM", "ST_REG", "ST_PES", "CD_VER"};
									
									Object[] vetValores = new Object[]{Geral.cdEmpresa, cdPes, edtNmRazao.getString(), edtNmFantasia.getString(), edtEmail.getString(), edtEndereco.getString(),
											edtNumero.getInt(), edtComplemento.getString(), edtCEP.getString(), edtBairro.getString(), cdCidEnd, edtCpfCgc.getString(), edtTelefone.getString(),
											placa, spnUf.getSelectedItemPosition() + 1, cdCidPlaca, chkTpPesPrdt.isChecked()?1:0, chkTpPesDest.isChecked()?1:0, chkTpPesCarg.isChecked()?1:0, chkTpPesMotr.isChecked()?1:0, chkTpPesEmit.isChecked()?1:0,
											Geral.terminal, 0, 0, 1};
									
									Geral.localDB.execSQL(Funcoes.criaSQLInsert("pub_pes", vetCampos, vetValores));
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

									Funcoes.msgBox("Registro incluído com sucesso!", CadPesActivity.this);
									novoRegistro();
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao gravar registro." + e.getMessage(), CadPesActivity.this);
								} finally{
									if(prgDialog.isShowing())
										prgDialog.dismiss();
								}
							};
							
						}.execute();
						
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao gravar registro." + e.getMessage(), CadPesActivity.this);
					}
				}
			}, null);
			
		} catch (Exception e) {
			Funcoes.msgBox("Ocorreu um erro ao efetuar a operação." + e.getMessage(), CadPesActivity.this);
		}
	}
	
	private void alteraRegistro(){
		try {
			
			if(edtNmRazao.getString().trim().equals("")){
				Funcoes.msgBox("Informe o nome da pessoa.", CadPesActivity.this);
				Funcoes.setaFoco(edtNmRazao);
				return;
			}else{
				if(!chkTpPesCarg.isChecked() && !chkTpPesDest.isChecked() && !chkTpPesEmit.isChecked() && !chkTpPesMotr.isChecked() && !chkTpPesPrdt.isChecked()){
					Funcoes.msgBox("Selecione ao menos um tipo de pessoa.", CadPesActivity.this);
					return;
				}
			}
			
			Funcoes.pergunta("Deseja gravar as alterações do registro atual?", CadPesActivity.this, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						new AsyncTask<Void, Void, Boolean>(){
							ProgressDialog prgDialog;
							String erro = "";
							
							@Override
							protected void onPreExecute() {
								prgDialog = ProgressDialog.show(CadPesActivity.this, "", "Processando...");
							};
							
							@Override
							protected Boolean doInBackground(Void... params) {
								try{
									
									String criterio = "cd_emp = " + Geral.cdEmpresa
													+ " and cd_pes = " + edtTermPes.getString() + edtCdPes.getString();
									
									long verReg = Funcoes.proximoNumeroLocal("cd_ver", "pub_pes", criterio);
									
									String cdCidEnd;
									String cdCidPlaca;
									
									if(spnCidade.getSelectedItem() != null){
										cdCidEnd = String.valueOf(((ItemSpinner) spnCidade.getSelectedItem()).getCodigo());
									}else{
										cdCidEnd = "null";
									}
									
									if(spnCidPlaca.getSelectedItem() != null){
										cdCidPlaca = String.valueOf(((ItemSpinner) spnCidPlaca.getSelectedItem()).getCodigo());
									}else{
										cdCidPlaca = "null";
									}
									
									String placa;
									if(edtPlaca.getString().trim().equals("")){
										placa = "    -     ";
									}else{
										placa = edtPlaca.getString();
									}	
									
									String[] vetCampos = new String[]{"NM_RAZAO_PES", "NM_FANTS_PES", "EMAIL_PES", "ENDERECO_PES", 
											"NUMERO_END", "COMPL_END", "CEP_END", "BAIRRO_END", "CD_CID_END", "CPF_CGC_PES", "FONE_PES",
											"PLACA_VEIC", "UF_PLACA", "CD_CID", "TP_PES_PRDT", "TP_PES_DEST", "TP_PES_CARG", "TP_PES_MOTR", "TP_PES_EMIT", "CD_TERM", "ST_REG", "ST_PES", "CD_VER"};
									
									Object[] vetValores = new Object[]{edtNmRazao.getString(), edtNmFantasia.getString(), edtEmail.getString(), edtEndereco.getString(),
											edtNumero.getInt(), edtComplemento.getString(), edtCEP.getString(), edtBairro.getString(), cdCidEnd, edtCpfCgc.getString(), edtTelefone.getString(),
											placa, spnUf.getSelectedItemPosition() + 1, cdCidPlaca, chkTpPesPrdt.isChecked()?1:0, chkTpPesDest.isChecked()?1:0, chkTpPesCarg.isChecked()?1:0, chkTpPesMotr.isChecked()?1:0, chkTpPesEmit.isChecked()?1:0,
											Geral.terminal, 0, 0, verReg};
									
									Geral.localDB.execSQL(Funcoes.criaSQLUpdate("pub_pes", vetCampos, vetValores, criterio));
									
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
									
									Funcoes.msgBox("Registro alterado com sucesso!", CadPesActivity.this);
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao alterar registro." + e.getMessage(), CadPesActivity.this);
								} finally{
									if(prgDialog.isShowing())
										prgDialog.dismiss();
								}
							};
							
						}.execute();
						
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao alterar registro." + e.getMessage(), CadPesActivity.this);
					}
				}
			}, null);
			
		} catch (Exception e) {
			Funcoes.msgBox("Ocorreu um erro ao efetuar a operação." + e.getMessage(), CadPesActivity.this);
		}	
	}
	
	private void excluiRegistro(){
		try {
			Funcoes.pergunta("Deseja gravar o registro atual?", CadPesActivity.this, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						new AsyncTask<Void, Void, Boolean>(){
							ProgressDialog prgDialog;
							String erro = "";
							
							@Override
							protected void onPreExecute() {
								prgDialog = ProgressDialog.show(CadPesActivity.this, "", "Processando...");
							};
							
							@Override
							protected Boolean doInBackground(Void... params) {
								try{
									
									String criterio = "cd_emp = " + Geral.cdEmpresa
													+ " and cd_pes = " + edtTermPes.getString() + edtCdPes.getString();
									
									long verReg = Funcoes.proximoNumeroLocal("cd_ver", "pub_pes", criterio);
									
									String[] vetCampos = new String[]{"st_reg", "st_pes", "cd_ver"};
									Object[] vetValores = new Object[]{0, 2, verReg};
									
									Geral.localDB.execSQL(Funcoes.criaSQLUpdate("pub_pes", vetCampos, vetValores, criterio));
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
									
									Funcoes.msgBox("Registro excluído com sucesso!", CadPesActivity.this);
									novoRegistro();
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao excluir registro." + e.getMessage(), CadPesActivity.this);
								} finally{
									if(prgDialog.isShowing())
										prgDialog.dismiss();
								}
							};
							
						}.execute();
						
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao excluir registro." + e.getMessage(), CadPesActivity.this);
					}
				}
			}, null);
			
		} catch (Exception e) {
			Funcoes.msgBox("Ocorreu um erro ao efetuar a operação." + e.getMessage(), CadPesActivity.this);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        onBackPressed();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		Funcoes.pergunta("Deseja relamente voltar para o menu principal?", CadPesActivity.this, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		}, null);
	}
}
