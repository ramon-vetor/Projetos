package com.melancia;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.melancia.Funcoes.ItemSpinner;
import com.melancia.controles.CEditText;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressWarnings("deprecation")
@SuppressLint("DefaultLocale")
public class TabMovActivity extends FragmentActivity implements ActionBar.TabListener {

	private static int ST_REG_INCLUINDO = 1;
	private static int ST_REG_EDITANDO = 2;
	private static int ST_REG_VISUALIZANDO = 3;
	
	private int stRegistro;
	
	static ImageButton btnProxReg;
	static ImageButton btnRegAnt;
	
	static CEditText edtTermEnt;
	static CEditText edtCdEnt;
	static CEditText edtDtEnt;
	static CEditText edtTermPrdr;
	static CEditText edtCdPrdr;
	static CEditText edtNmPrdr;
	static ImageButton btnPesqPrdr;
	static CEditText edtCdProd;
	static CEditText edtDsProd;
	static ImageButton btnPesqProd;
	static CEditText edtPesoBruto;
	static CEditText edtPercPalha;
	static CEditText edtDescarte;
	static CEditText edtPesoLiq;
	static CEditText edtQtdePecas;
	static CEditText edtPesoMedio;
	static CEditText edtPrecoEnt;
	static CEditText edtTermCarg;
	static CEditText edtCdCarg;
	static CEditText edtNmCarg;
	static ImageButton btnPesqCarg;
	static CEditText edtVlMaoObra;
	static CEditText edtVlDespPrdr;
	static CEditText edtVlTotPrdr;
	static CEditText edtObsEnt;
	static Spinner spnStEnt;
	static CEditText edtVlPgEnt;
	
	static CEditText edtTermDest;
	static CEditText edtCdDest;
	static CEditText edtNmDest;
	static ImageButton btnPesqDest;
	static CEditText edtDtSai;
	static CEditText edtNmMotr;
	static ImageButton btnPesqMotr;
	static CEditText edtFoneMotr;
	static CEditText edtCPFCGCMotr;
	static CEditText edtPlacaMotr;
	static Spinner spnUfPlaca;
	static Spinner spnCidPlaca;
	static CEditText edtVlFrete;
	static CEditText edtVlICMSFrete;
	static CEditText edtVlTotFrete;
	static CEditText edtPrecoSai;
	static CEditText edtVlTotSai;
	static CEditText edtObsSai;
	static Spinner spnStSai;
	static CEditText edtVlRecSai;
	static Spinner spnTpFreteSai;
	static Spinner spnTpICMS;
	static CheckBox chkNotaFiscal;

	ViewPager mViewPager;
	
	View vMovEnt;
	View vMovSai;
	
	class Movimentacao extends HashMap<String, String>{
		private static final long serialVersionUID = 1L;
		
		public Movimentacao(
				long cdEnt,
				String dtEnt,
				String nmPrdt,
				String nmDest,
				String placa,
				String cidUf,
				String obs,
				double pBruto,
				double pLiquido,
				int qtPecas,
				double pMedio,
				double prcEnt,
				double prcSai,
				double vlMObra,
				double vlPg,
				double vlICMS,
				double vlRec,
				double vlLucro
				){
			
			this.put("cdEnt", Funcoes.formata(cdEnt, 6));
			this.put("dtEnt", dtEnt);
			this.put("nmPrdt", nmPrdt);
			this.put("nmDest", nmDest);
			this.put("placa", placa);
			this.put("cidUf", cidUf);
			this.put("obs", obs);
			this.put("pBruto", Funcoes.decimal(pBruto, 2));
			this.put("pLiquido", Funcoes.decimal(pLiquido, 2));
			this.put("qtPecas", Funcoes.formata(qtPecas, 6));
			this.put("pMedio", Funcoes.decimal(pMedio, 2));
			this.put("prcEnt", Funcoes.decimal(prcEnt, 2));
			this.put("prcSai", Funcoes.decimal(prcSai, 2));
			this.put("vlMObra", Funcoes.decimal(vlMObra, 2));
			this.put("vlPg", Funcoes.decimal(vlPg, 2));
			this.put("vlICMS", Funcoes.decimal(vlICMS, 2));
			this.put("vlRec", Funcoes.decimal(vlRec, 2));
			this.put("vlLucro", Funcoes.decimal(vlLucro, 2));
			
		}
	}
	
	public static class RelEntrada extends Activity{
		
		public ScrollView rllRelEnt;
		
		public TextView txvNmEmp;
		public TextView txvCdEnt;
		public TextView txvDtEmis;
		public TextView txvNmPrdt;
		public TextView txvNmDest;
		public TextView txvDsProd;
		public TextView txvPesoBruto;
		public TextView txvPercPalha;
		public TextView txvDescarte;
		public TextView txvPesoLiq;
		public TextView txvQtPecas;
		public TextView txvPesoMed;
		public TextView txvPrcVenda;
		public TextView txvVlTotSai;
		public TextView txvNmMotr;
		public TextView txvFoneMotr;
		public TextView txvCPFCGCMotr;
		public TextView txvPlaca;
		public TextView txvCidUf;
		public TextView txvNmCarg;
		public TextView txvVlFrete;
		public TextView txvVlIcmsFrete;
		public TextView txvVlTotFrete;
		public TextView txvObsSai;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.rel_ent);
			
			try {
						
				rllRelEnt = (ScrollView) findViewById(R.id.rllRelEnt);
				
				txvNmEmp = (TextView) findViewById(R.id.txvNmEmp);
				txvCdEnt = (TextView) findViewById(R.id.txvCdEnt);
				txvDtEmis = (TextView) findViewById(R.id.txvDtEnt);
				txvNmPrdt = (TextView) findViewById(R.id.txvNmPrdt);
				txvNmDest = (TextView) findViewById(R.id.txvNmDest);
				txvDsProd = (TextView) findViewById(R.id.txvDsProd);
				txvPesoBruto = (TextView) findViewById(R.id.txvPesoBruto);
				txvPercPalha = (TextView) findViewById(R.id.txvPercPalha);
				txvDescarte = (TextView) findViewById(R.id.txvDescarte);
				txvPesoLiq = (TextView) findViewById(R.id.txvPesoLiq);
				txvQtPecas = (TextView) findViewById(R.id.txvQtPecas);
				txvPesoMed = (TextView) findViewById(R.id.txvPesoMed);
				txvPrcVenda = (TextView) findViewById(R.id.txvPrcVenda);
				txvVlTotSai = (TextView) findViewById(R.id.txvVlTotSai);
				txvNmMotr = (TextView) findViewById(R.id.txvNmMotr);
				txvFoneMotr = (TextView) findViewById(R.id.txvFoneMotr);
				txvCPFCGCMotr = (TextView) findViewById(R.id.txvCpfCgcMotr);
				txvPlaca = (TextView) findViewById(R.id.txvPlaca);
				txvCidUf = (TextView) findViewById(R.id.txvCidUf);
				txvNmCarg = (TextView) findViewById(R.id.txvNmCarg);
				txvVlFrete = (TextView) findViewById(R.id.txvVlFrete);
				txvVlIcmsFrete = (TextView) findViewById(R.id.txvVlIcmsFrete);
				txvVlTotFrete = (TextView) findViewById(R.id.txvVlTotFrete);
				txvObsSai = (TextView) findViewById(R.id.txvObsSai);
				
				
				if(getIntent().getStringExtra("tipo").equals(("Exportar (Sem produtor)"))){
					((LinearLayout) findViewById(R.id.lnlDadosPrdt)).getLayoutParams().height = 0;
				}
				
				limpaCampos();
				
				new AsyncTask<Void, Void, Boolean>() {
					
					ProgressDialog prgDialog;
					String erro = "";
					Cursor crsTmp;
					
					protected void onPreExecute() {
						prgDialog = ProgressDialog.show(RelEntrada.this, "", "Processando...");
					};
					
					@Override
					protected Boolean doInBackground(Void... params) {
						try{
							String sql = "select ent.*, coalesce(ent.dt_sai, ent.dt_ent) as dt_sai_nv, prod.ds_prod, prdt.nm_fants_pes as nm_prdt, carg.nm_fants_pes as nm_carg, dest.nm_fants_pes as nm_dest, cid.ds_cid, cid.uf_cid" +
									" from ctrl_ent ent inner join pub_pes prdt" + 
									" on ent.cd_emp = prdt.cd_emp" +
									" and ent.cd_pes_prdt = prdt.cd_pes" + 
									" inner join pub_pes dest" +
									" on ent.cd_emp = dest.cd_emp" +
									" and ent.cd_pes_dest = dest.cd_pes" +
									" inner join pub_pes carg" +
									" on ent.cd_emp = carg.cd_emp" +
									" and ent.cd_pes_carg = carg.cd_pes" +
									" inner join pub_prod prod" +
									" on ent.cd_emp = prod.cd_emp" +
									" and ent.cd_prod = prod.cd_prod" +
									" left join pub_cid cid" +
									" on ent.cd_cid_placa = cid.cd_cid" +
									" where ent.cd_emp = " + Geral.cdEmpresa + 
									" and cd_ent = " + edtTermEnt.getString() + edtCdEnt.getString();
							
							crsTmp = Geral.localDB.rawQuery(sql, null);
							
							if(crsTmp.getCount() > 0){
								return true;
							}else{
								erro = "Movimentação não encontrada.";
								return false;
							}
						}catch(Exception e){
							erro = e.getMessage();
							return false;
						}
					}
					
					protected void onPostExecute(Boolean result) {
						try {
							if(!result)
								throw new RuntimeException(erro);
	
							crsTmp.moveToFirst();
							
							txvNmEmp.setText(Funcoes.vlCampoTblLocal("nm_emp", "pub_emp", "cd_emp = " + Geral.cdEmpresa).toString());
							txvCdEnt.setText(Funcoes.formata(crsTmp.getInt(crsTmp.getColumnIndex("cd_ent")), 6));
							txvDtEmis.setText(Funcoes.formataData(crsTmp.getString(crsTmp.getColumnIndex("dt_ent")), "dd/MM/yyyy"));
							txvNmPrdt.setText(crsTmp.getString(crsTmp.getColumnIndex("nm_prdt")));
							txvNmDest.setText(crsTmp.getString(crsTmp.getColumnIndex("nm_dest")));
							
							txvDsProd.setText(crsTmp.getString(crsTmp.getColumnIndex("ds_prod")));
							txvPesoBruto.setText(Funcoes.decimal(crsTmp.getDouble(crsTmp.getColumnIndex("peso_bruto_prod")), 2));
							txvPercPalha.setText(Funcoes.decimal(crsTmp.getDouble(crsTmp.getColumnIndex("perc_palha")), 2));
							txvDescarte.setText(Funcoes.decimal(crsTmp.getDouble(crsTmp.getColumnIndex("peso_descarte")), 2));
							txvPesoLiq.setText(Funcoes.decimal(crsTmp.getDouble(crsTmp.getColumnIndex("peso_liq_prod")), 2));
							txvQtPecas.setText(Funcoes.formata(crsTmp.getInt(crsTmp.getColumnIndex("qtde_pecas")), 6));
							txvPesoMed.setText(Funcoes.decimal(crsTmp.getDouble(crsTmp.getColumnIndex("peso_bruto_prod")) / crsTmp.getInt(crsTmp.getColumnIndex("qtde_pecas")), 3));
							txvPrcVenda.setText(Funcoes.decimal(crsTmp.getDouble(crsTmp.getColumnIndex("preco_vend")), 2));
							txvVlTotSai.setText(Funcoes.decimal(crsTmp.getDouble(crsTmp.getColumnIndex("vl_to_sai")), 2));
							
							txvNmMotr.setText(crsTmp.getString(crsTmp.getColumnIndex("nm_motr")));
							txvNmCarg.setText(crsTmp.getString(crsTmp.getColumnIndex("nm_carg")));
							if(!Funcoes.separaNumeros(crsTmp.getString(crsTmp.getColumnIndex("fone_motr"))).equals("")){
								txvFoneMotr.setText(crsTmp.getString(crsTmp.getColumnIndex("fone_motr")));
							}else{
								txvFoneMotr.setText("");
							}
							txvCPFCGCMotr.setText(crsTmp.getString(crsTmp.getColumnIndex("cpf_cgc_motr")));
							if(crsTmp.getString(crsTmp.getColumnIndex("placa_frete")) != null){
								txvPlaca.setText(crsTmp.getString(crsTmp.getColumnIndex("placa_frete")));
							}
							
							if(crsTmp.getString(crsTmp.getColumnIndex("ds_cid")) != null){
								txvCidUf.setText(crsTmp.getString(crsTmp.getColumnIndex("ds_cid")) + "-" + crsTmp.getString(crsTmp.getColumnIndex("uf_cid")));
							}
							
							txvVlFrete.setText(Funcoes.decimal(crsTmp.getDouble(crsTmp.getColumnIndex("vl_frete_ton")), 2));
							txvVlIcmsFrete.setText(Funcoes.decimal(crsTmp.getDouble(crsTmp.getColumnIndex("vl_icms_frete")), 2));
							txvVlTotFrete.setText(Funcoes.decimal(crsTmp.getDouble(crsTmp.getColumnIndex("vl_to_frete")), 2));
							
							txvObsSai.setText(crsTmp.getString(crsTmp.getColumnIndex("obs_sai")));

							crsTmp.close();
							
						} catch (Exception e) {
							Funcoes.msgBox("Falha ao exibir registro." + e.getMessage(), RelEntrada.this, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									finish();
								}
							});
							
						} finally {
							if(prgDialog.isShowing())
								prgDialog.dismiss();
						}
					};
					
				}.execute();
			} catch (Exception e) {
				Funcoes.msgBox("Falha ao consultar movimentação." + e.getMessage(), this, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
			}
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			
			MenuItem mnuSalvar = menu.add("Salvar");
			mnuSalvar.setIcon(android.R.drawable.ic_menu_save);
			mnuSalvar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			mnuSalvar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
				
				File pdfPath;
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					try {
						rllRelEnt.setDrawingCacheEnabled(true);
						rllRelEnt.buildDrawingCache();
						Bitmap bmpRelEnt = rllRelEnt.getDrawingCache();
						
						PdfDocument pdfRelatorio = new PdfDocument();
						
						pdfPath = new File(Environment.getExternalStorageDirectory() + "/Melancia Sys/Relatorios");
						if(!pdfPath.exists())
							pdfPath.mkdir();
						
						String caminho = Environment.getExternalStorageDirectory() + "/Melancia Sys/Relatorios/ent_" + edtTermEnt.getString() + edtCdEnt.getString() + ".pdf";
						pdfPath = new File(caminho);

						PageInfo pageInfo = new PageInfo.Builder(bmpRelEnt.getWidth() + 200, bmpRelEnt.getHeight() + 200, 1).create();
						Page pagina = pdfRelatorio.startPage(pageInfo);
						
						pagina.getCanvas().drawBitmap(bmpRelEnt, 100,  100, new Paint());
						
						pdfRelatorio.finishPage(pagina);
				        FileOutputStream fileOS = new FileOutputStream(pdfPath);
				        pdfRelatorio.writeTo(fileOS);
			            pdfRelatorio.close();
			            
			            fileOS.flush();
			            fileOS.close();
			            
			            Funcoes.msgBox("Movimentação exportada com sucesso." + "Savo em " + caminho, RelEntrada.this, new DialogInterface.OnClickListener() {							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Funcoes.pergunta("Deseja compartilhado o arquivo criado?", RelEntrada.this, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which){
										Funcoes.compartilhaArquivo(RelEntrada.this, Uri.fromFile(pdfPath));
									}
								}, null);
							}
						});
			            
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao exportar movimentação." + e.getMessage(), RelEntrada.this);
					}
					return false;
				}
			});
			
			MenuItem mnuFechar = menu.add("Fechar");
			mnuFechar.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
			mnuFechar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			mnuFechar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					finish();
					return false;
				}
			});
			
			return true;
		}

		private void limpaCampos(){
			txvNmEmp.setText("");
			txvCdEnt.setText("");
			txvDtEmis.setText("");
			txvNmPrdt.setText("");
			txvNmDest.setText("");
			txvDsProd.setText("");
			txvPesoBruto.setText("");
			txvPercPalha.setText("");
			txvDescarte.setText("");
			txvPesoLiq.setText("");
			txvQtPecas.setText("");
			txvPesoMed.setText("");
			txvPrcVenda.setText("");
			txvVlTotSai.setText("");
			txvNmMotr.setText("");
			txvFoneMotr.setText("");
			txvCPFCGCMotr.setText("");
			txvPlaca.setText("");
			txvCidUf.setText("");
			txvNmCarg.setText("");
			txvVlFrete.setText("");
			txvVlIcmsFrete.setText("");
			txvVlTotFrete.setText("");
			txvObsSai.setText("");
		}
	}

	public static ArrayList<Movimentacao> lstRelMov = null;
	
	public static class RelMovEnt extends Activity{
		public RelativeLayout rlRelMov;
		
		@Override
		protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.rel_mov);
			
			try {

				ArrayList<Movimentacao> lanctos = TabMovActivity.lstRelMov;

				if(lanctos == null || lanctos.size() == 0) {
					Funcoes.msgBox("Dados do relatório não encontrados.", RelMovEnt.this);
				}
				
				final ListView lstMov = (ListView) findViewById(R.id.lstMov);
				findViewById(R.id.lnlPageNumber).setVisibility(View.GONE);
				
				TextView txvNmEmp = (TextView) findViewById(R.id.txvNmEmp);
				txvNmEmp.setText(getIntent().getStringExtra("nmEmp"));
				
				final int prcExib = getIntent().getIntExtra("prcExib", 0);
				
				String[] campos = new String[]{"cdEnt", "dtEnt", "nmPrdt", "nmDest", "placa", "cidUf", "obs", "pBruto", "pLiquido", "qtPecas", "pMedio", "prcEnt", "prcSai", "vlMObra", "vlPg", "vlICMS", "vlRec", "vlLucro"};
				int[] ids = new int[]{R.id.txvCdEnt, R.id.txvDtEnt, R.id.txvNmPrdt, R.id.txvNmDest, R.id.txvPlaca, R.id.txvCidUf, R.id.txvObs, R.id.txvPBruto, R.id.txvPLiq, R.id.txvQtPecas, R.id.txvPMedio, R.id.txvPrcEnt, R.id.txvPrcSai, R.id.txvVlMObra, R.id.txvVlPg, R.id.txvICMSFrete, R.id.txvVlRec, R.id.txvVlLucro};
				
				SimpleAdapter listAdap = new SimpleAdapter(RelMovEnt.this, lanctos, R.layout.adapter_rel_mov, campos, ids){
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						View vItem = super.getView(position, convertView, parent);
						
						if(vItem != null){
							TextView txvPrcSai = (TextView) vItem.findViewById(R.id.txvPrcSai);
							TextView txvICMS = (TextView) vItem.findViewById(R.id.txvICMSFrete);
							TextView txvVlRec = (TextView) vItem.findViewById(R.id.txvVlRec);
							TextView txvVlLucro = (TextView) vItem.findViewById(R.id.txvVlLucro);
							TextView txvPrcEnt = (TextView) vItem.findViewById(R.id.txvPrcEnt);
							TextView txvVlMObra = (TextView) vItem.findViewById(R.id.txvVlMObra);
							TextView txvVlPg = (TextView) vItem.findViewById(R.id.txvVlPg);
							
							switch (prcExib) {
							case 1:
								txvPrcSai.setVisibility(View.INVISIBLE);
								txvICMS.setVisibility(View.INVISIBLE);
								txvVlRec.setVisibility(View.INVISIBLE);
								txvVlLucro.setVisibility(View.INVISIBLE);
								break;
							case 2:
								txvPrcEnt.setVisibility(View.INVISIBLE);
								txvVlMObra.setVisibility(View.INVISIBLE);
								txvVlPg.setVisibility(View.INVISIBLE);
								txvVlLucro.setVisibility(View.INVISIBLE);
							default:
								break;
							}
						}
						
						return vItem;
					}
				};
				
				lstMov.setAdapter(listAdap);
				
				double pBruto = 0;
				double pLiquido = 0;
				int qtPecas = 0;
				double vlPago = 0;
				double vlMObra = 0;
				double vlICMS = 0;
				double vlRec = 0;
				double vlLucro = 0;
				
				for (HashMap<String, String> mov : lanctos) {
					pBruto += Funcoes.toDouble(mov.get("pBruto"));
					pLiquido += Funcoes.toDouble(mov.get("pLiquido"));
					qtPecas += Funcoes.toInt(mov.get("qtPecas"));
					vlPago += Funcoes.toDouble(mov.get("vlPg"));
					vlMObra += Funcoes.toDouble(mov.get("vlMObra"));
					vlICMS += Funcoes.toDouble(mov.get("vlICMS"));
					vlRec += Funcoes.toDouble(mov.get("vlRec"));
					vlLucro += Funcoes.toDouble(mov.get("vlLucro"));					
				}
				
				TextView txvQtPed = (TextView) findViewById(R.id.txvQtPed);
				TextView txvTotPBruto = (TextView) findViewById(R.id.txvTotPBruto);
				TextView txvTotPLiq = (TextView) findViewById(R.id.txvTotPLiq);
				TextView txvTotQtPecas = (TextView) findViewById(R.id.txvTotQtPecas);
				TextView txvTotVlPg = (TextView) findViewById(R.id.txvTotVlPg);
				TextView txvTotVlMObra = (TextView) findViewById(R.id.txvTotVlMObra);
				TextView txvTotVlICMS = (TextView) findViewById(R.id.txvTotVlICMS);
				TextView txvTotVlRec = (TextView) findViewById(R.id.txvTotVlRec);
				TextView txvTotVlLucro = (TextView) findViewById(R.id.txvTotVlLucro);
				
				txvQtPed.setText(Funcoes.formata(lanctos.size(), 6));
				txvTotPBruto.setText(Funcoes.decimal(pBruto, 2));
				txvTotPLiq.setText(Funcoes.decimal(pLiquido, 2));
				txvTotQtPecas.setText(Funcoes.formata(qtPecas, 6));
				txvTotVlPg.setText(Funcoes.decimal(vlPago, 2));
				txvTotVlMObra.setText(Funcoes.decimal(vlMObra, 2));
				txvTotVlICMS.setText(Funcoes.decimal(vlICMS, 2));
				txvTotVlRec.setText(Funcoes.decimal(vlRec, 2));
				txvTotVlLucro.setText(Funcoes.decimal(vlLucro, 2));
				
				TextView lblPrcSai = (TextView) findViewById(R.id.lblPrcSai);
				TextView lblICMS = (TextView) findViewById(R.id.lblVlICMS);
				TextView lblVlRec = (TextView) findViewById(R.id.lblVlRec);
				TextView lblVlLucro = (TextView) findViewById(R.id.lblVlLucro);
				TextView lblPrcEnt = (TextView) findViewById(R.id.lblPrcEnt);
				TextView lblVlMObra = (TextView) findViewById(R.id.lblVlMObra);
				TextView lblVlPg = (TextView) findViewById(R.id.lblVlPg);
				
				switch (prcExib) {
				case 1:
					lblPrcSai.setVisibility(View.INVISIBLE);
					lblICMS.setVisibility(View.INVISIBLE);
					txvTotVlICMS.setVisibility(View.INVISIBLE);
					lblVlRec.setVisibility(View.INVISIBLE);
					txvTotVlRec.setVisibility(View.INVISIBLE);
					lblVlLucro.setVisibility(View.INVISIBLE);
					txvTotVlLucro.setVisibility(View.INVISIBLE);
					break;
				case 2:
					lblPrcEnt.setVisibility(View.INVISIBLE);
					lblVlMObra.setVisibility(View.INVISIBLE);
					txvTotVlMObra.setVisibility(View.INVISIBLE);
					lblVlPg.setVisibility(View.INVISIBLE);
					txvTotVlPg.setVisibility(View.INVISIBLE);
					lblVlLucro.setVisibility(View.INVISIBLE);
					txvTotVlLucro.setVisibility(View.INVISIBLE);
					
				default:
					break;
				}

				
			} catch (Exception e) {
				Funcoes.msgBox("Falha ao gerar relatório." + e.getMessage(), this, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
			}
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			
			MenuItem mnuSalvar = menu.add("Salvar");
			mnuSalvar.setIcon(android.R.drawable.ic_menu_save);
			mnuSalvar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			mnuSalvar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					try {
	
						new AsyncTask<Void, Void, String>(){
							
							ProgressDialog prgDialog;
							int allitemsheight = 0;
						    int qtItens = 0;
						    int pageNumber = 1;
						    String data = (new SimpleDateFormat("ddMMyyHHmm").format(new Date()));
						    
						    RelativeLayout rllHeader;
							RelativeLayout rllFooter;
							LinearLayout lnlPageNumber;
							TextView txvPageNumber = (TextView) findViewById(R.id.txvPageNumber);
						    
							protected void onPreExecute() {
								prgDialog = ProgressDialog.show(RelMovEnt.this, "", "Exportando dados...");
							};
				
							@Override
							protected String doInBackground(Void... params) {
								try {
									
									runOnUiThread(new Runnable() {																	
										
										File pdfPath;
										
										@Override
										public void run() {
											try {
												rllHeader = (RelativeLayout) findViewById(R.id.rllHeader);
												rllFooter = (RelativeLayout) findViewById(R.id.rllFooter);
												lnlPageNumber = (LinearLayout) findViewById(R.id.lnlPageNumber);
												txvPageNumber = (TextView) findViewById(R.id.txvPageNumber);
												
												rllHeader.setDrawingCacheEnabled(true);
												rllHeader.buildDrawingCache();
												
												rllFooter.setDrawingCacheEnabled(true);
												rllFooter.buildDrawingCache();
												
												lnlPageNumber.measure(MeasureSpec.makeMeasureSpec(rllHeader.getWidth(), MeasureSpec.EXACTLY), 
										                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			
												lnlPageNumber.layout(0, 0, lnlPageNumber.getMeasuredWidth(), lnlPageNumber.getMeasuredHeight());
												
												pdfPath = new File(Environment.getExternalStorageDirectory() + "/Melancia Sys/Relatorios");
												if(!pdfPath.exists())
													pdfPath.mkdir();
												
												ListView listview =  (ListView) findViewById(R.id.lstMov);
											    final SimpleAdapter adapter = (SimpleAdapter) listview.getAdapter(); 
										
											    List<Bitmap> bmpItems = new ArrayList<Bitmap>();
											    
											    PdfDocument pdfRelatorio = new PdfDocument();
											    
											    for (int i = 0; i < adapter.getCount(); i++) {
			
											        final View childView = adapter.getView(i, null, listview);
											        childView.measure(MeasureSpec.makeMeasureSpec(listview.getWidth(), MeasureSpec.EXACTLY), 
											                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			
													childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
			
											        childView.setDrawingCacheEnabled(true);
											        childView.buildDrawingCache();
											        bmpItems.add(childView.getDrawingCache());
											        allitemsheight += childView.getMeasuredHeight();
											        
											        qtItens += 1;
											        if(qtItens == 5 || i == (adapter.getCount() - 1)) {
			
														int totalHeight = rllHeader.getHeight() + allitemsheight + lnlPageNumber.getHeight();
														
														if(i == (adapter.getCount() - 1))
															totalHeight += rllFooter.getHeight();
														
													    //Bitmap pagina = Bitmap.createBitmap(listview.getMeasuredWidth(), totalHeight, Bitmap.Config.ARGB_8888);
													    //Canvas canvas = new Canvas(pagina);
			
														PageInfo pageInfo = new PageInfo.Builder(listview.getMeasuredWidth() + 200, totalHeight + 200, pageNumber).create();
														Page pagina = pdfRelatorio.startPage(pageInfo);
														
														Canvas canvas = pagina.getCanvas();
													    Paint paint = new Paint();
													    
													    canvas.drawBitmap(rllHeader.getDrawingCache(), 100, 100, paint);
													    int usedHeight = rllHeader.getHeight() + 100;
			
													    for (int j = 0; j < bmpItems.size(); j++) {
													        Bitmap bmp = bmpItems.get(j);
													        canvas.drawBitmap(bmp, 100, usedHeight, paint);
													        usedHeight += bmp.getHeight();
			
													        bmp.recycle();
													        bmp = null;
													    }
													    
													    bmpItems.clear();
													    
														if(i == (adapter.getCount() - 1)) {
															canvas.drawBitmap(rllFooter.getDrawingCache(), 100, usedHeight, paint);
															usedHeight += rllFooter.getHeight();
														}
			
														txvPageNumber.setText("Página: " + Funcoes.formata(pageNumber, 2) + "/" + Funcoes.formata((int) Math.ceil(adapter.getCount() / 5.0), 2));
														
														lnlPageNumber.measure(MeasureSpec.makeMeasureSpec(rllHeader.getWidth(), MeasureSpec.EXACTLY), 
												                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
														lnlPageNumber.layout(0, 0, lnlPageNumber.getMeasuredWidth(), lnlPageNumber.getMeasuredHeight());
				
														lnlPageNumber.setDrawingCacheEnabled(true);
														lnlPageNumber.buildDrawingCache();
														canvas.drawBitmap(lnlPageNumber.getDrawingCache(), 100, usedHeight, paint);
														
														/*
														String caminho = Environment.getExternalStorageDirectory() + "/Melancia Sys/Relatorios/REL_ENT_" + data + "_" + Funcoes.formata(pageNumber, 3) + ".png";
														pdfPath = new File(caminho);
														arquivos.add(Uri.fromFile(pdfPath));
														
												        FileOutputStream fOs = null;
											            fOs = new FileOutputStream(pdfPath);
											            pagina.compress(Bitmap.CompressFormat.PNG, 100, fOs);
											            fOs.flush();
											            fOs.close();
											            */
														
														pdfRelatorio.finishPage(pagina);
			
													    allitemsheight = 0;
													    qtItens = 0;
													    pageNumber ++;
											        }
			
											    }
											    
											    String caminho = Environment.getExternalStorageDirectory() + "/Melancia Sys/Relatorios/REL_ENT_" + data + ".pdf";
												pdfPath = new File(caminho);
												
												FileOutputStream fileOS = new FileOutputStream(pdfPath);
												pdfRelatorio.writeTo(fileOS);
												pdfRelatorio.close();
												
												fileOS.flush();
												fileOS.close();
												
											    prgDialog.dismiss();

												Funcoes.msgBox("Movimentação exportada com sucesso." + "Savo em " + Environment.getExternalStorageDirectory() + "/Melancia Sys/Relatorios", RelMovEnt.this, new DialogInterface.OnClickListener() {							
													@Override
													public void onClick(DialogInterface dialog, int which) {
														Funcoes.pergunta("Deseja compartilhar o arquivo gerado?", RelMovEnt.this, new DialogInterface.OnClickListener() {
															@Override
															public void onClick(DialogInterface dialog, int which) {
																Funcoes.compartilhaArquivo(RelMovEnt.this, Uri.fromFile(pdfPath));
															}
														}, null);
													}
												});
												
											} catch(Exception e) {
												prgDialog.dismiss();
												Funcoes.msgBox("Falha ao exportar movimentação." + e.getMessage(), RelMovEnt.this);
											}
										}
									});
								    
									return null;
								}catch(Exception e) {
									return e.getMessage();
								}
							}
							
						}.execute();


					} catch (Exception e) {
						Funcoes.msgBox("Falha ao exportar movimentação." + e.getMessage(), RelMovEnt.this);
					}
					return false;
				}
			});
			
			
			MenuItem mnuFechar = menu.add("Fechar");
			mnuFechar.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
			mnuFechar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			mnuFechar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					finish();
					return false;
				}
			});
			
			return true;
		}

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_mov);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		
		vMovEnt = View.inflate(this, R.layout.tab_mov_ent, null);
		
		btnProxReg = (ImageButton) vMovEnt.findViewById(R.id.btnProx);
		btnRegAnt = (ImageButton) vMovEnt.findViewById(R.id.btnAnt);
		edtTermEnt = (CEditText) vMovEnt.findViewById(R.id.edtTermLanc);
		edtCdEnt = (CEditText) vMovEnt.findViewById(R.id.edtCdEnt);
		edtDtEnt = (CEditText) vMovEnt.findViewById(R.id.edtDtEmis);
		edtTermPrdr = (CEditText) vMovEnt.findViewById(R.id.edtTermProdr);
		edtCdPrdr = (CEditText) vMovEnt.findViewById(R.id.edtCdProdr);
		edtNmPrdr = (CEditText) vMovEnt.findViewById(R.id.edtNmProdr);
		btnPesqPrdr = (ImageButton) vMovEnt.findViewById(R.id.btnPesqProdr);
		edtCdProd = (CEditText) vMovEnt.findViewById(R.id.edtCdProd);
		edtDsProd = (CEditText) vMovEnt.findViewById(R.id.edtDsProd);
		btnPesqProd = (ImageButton) vMovEnt.findViewById(R.id.btnPesqProd);
		edtPesoBruto = (CEditText) vMovEnt.findViewById(R.id.edtPBruto);
		edtPercPalha = (CEditText) vMovEnt.findViewById(R.id.edtPercPalha);
		edtDescarte = (CEditText) vMovEnt.findViewById(R.id.edtDescarte);
		edtPesoLiq = (CEditText) vMovEnt.findViewById(R.id.edtPLiquido);
		edtQtdePecas = (CEditText) vMovEnt.findViewById(R.id.edtQtPecas);
		edtPesoMedio = (CEditText) vMovEnt.findViewById(R.id.edtPMedio);
		edtPrecoEnt = (CEditText) vMovEnt.findViewById(R.id.edtPreco);
		edtTermCarg = (CEditText) vMovEnt.findViewById(R.id.edtTermCarg);
		edtCdCarg = (CEditText) vMovEnt.findViewById(R.id.edtCdCarg);
		edtNmCarg = (CEditText) vMovEnt.findViewById(R.id.edtNmCarg);
		btnPesqCarg = (ImageButton) vMovEnt.findViewById(R.id.btnPesqCarg);
		edtVlMaoObra = (CEditText) vMovEnt.findViewById(R.id.edtVlMaoObra);
		edtVlDespPrdr = (CEditText) vMovEnt.findViewById(R.id.edtVlDespProdr);
		edtVlTotPrdr = (CEditText) vMovEnt.findViewById(R.id.edtVlTotProdr);
		edtObsEnt = (CEditText) vMovEnt.findViewById(R.id.edtObsEnt);
		spnStEnt = (Spinner) vMovEnt.findViewById(R.id.spnStEnt);
		edtVlPgEnt = (CEditText) vMovEnt.findViewById(R.id.edtVlPgEnt);
		
		vMovSai = View.inflate(this, R.layout.tab_mov_sai, null);
		
		edtTermDest = (CEditText) vMovSai.findViewById(R.id.edtTermDest);
		edtCdDest = (CEditText) vMovSai.findViewById(R.id.edtCdDest);
		edtNmDest = (CEditText) vMovSai.findViewById(R.id.edtNmDest);
		btnPesqDest = (ImageButton) vMovSai.findViewById(R.id.btnPesqDest);
		edtDtSai = (CEditText) vMovSai.findViewById(R.id.edtDtEntrega);
		edtNmMotr = (CEditText) vMovSai.findViewById(R.id.edtNmMotor);
		btnPesqMotr = (ImageButton) vMovSai.findViewById(R.id.btnPesqMotor);
		edtFoneMotr = (CEditText) vMovSai.findViewById(R.id.edtFoneMotor);
		edtCPFCGCMotr = (CEditText) vMovSai.findViewById(R.id.edtCpfCgcMotor);
		edtPlacaMotr = (CEditText) vMovSai.findViewById(R.id.edtPlacaMotor);
		spnUfPlaca = (Spinner) vMovSai.findViewById(R.id.spnUfPlaca);
		spnCidPlaca = (Spinner) vMovSai.findViewById(R.id.spnCidPlaca);
		edtVlFrete = (CEditText) vMovSai.findViewById(R.id.edtVlFrete);
		edtVlICMSFrete = (CEditText) vMovSai.findViewById(R.id.edtIcmsFrete);
		edtVlTotFrete = (CEditText) vMovSai.findViewById(R.id.edtVlTotFrete);
		edtPrecoSai = (CEditText) vMovSai.findViewById(R.id.edtPrcVenda);
		edtVlTotSai = (CEditText) vMovSai.findViewById(R.id.edtVlTotVenda);
		edtObsSai = (CEditText) vMovSai.findViewById(R.id.edtObsSai);
		spnStSai = (Spinner) vMovSai.findViewById(R.id.spnStSai);
		edtVlRecSai = (CEditText) vMovSai.findViewById(R.id.edtVlRecSai);
		spnTpFreteSai = (Spinner) vMovSai.findViewById(R.id.spnTpFrete);
		spnTpICMS = (Spinner) vMovSai.findViewById(R.id.spnTPICMS);
		chkNotaFiscal = (CheckBox) vMovSai.findViewById(R.id.chkNotaFiscal);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				return 2;
			}
			
			@SuppressLint("ValidFragment")
			@Override
			public Fragment getItem(final int arg0) {
				return new Fragment(){
					@Override
					public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
						if(arg0 == 0){
							return vMovEnt;
						}else{
							return vMovSai;
						}
					}
				};
			}		
		});
				
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		
		actionBar.addTab(actionBar.newTab().setText("Compra").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Venda").setTabListener(this));

		spnUfPlaca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(spnUfPlaca.getSelectedItem() != null){
					Funcoes.preencheSpinnerLocal(TabMovActivity.this, vMovSai, R.id.spnCidPlaca, "Cidade Placa", "pub_cid", "uf_cid = ?", new String[]{spnUfPlaca.getSelectedItem().toString()}, "ds_cid", "ds_cid", "cd_cid");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
			
		});
		
		btnProxReg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					if(edtTermEnt.getInt() == 0)
						edtTermEnt.setText(Funcoes.formata(Geral.terminal, 2));
					
					if(edtCdEnt.getString().equals(""))
						edtCdEnt.setText("0000");
					
					String criterio = "cd_emp = " + Geral.cdEmpresa +
							" and cd_ent between " + edtTermEnt.getString() + "0000 and " + edtTermEnt.getString() + "9999" +
							" and cd_ent > " + edtTermEnt.getString() + edtCdEnt.getString() +
							" and status_ent <> 2";
					
					long cdEnt = Funcoes.toLong(Funcoes.vlCampoTblLocal("min(cd_ent)", "ctrl_ent", criterio));
					if(cdEnt > 0) {
						mostraDados(cdEnt);
					}
				} catch (Exception e) {
					Funcoes.msgBox("Falha ao consultar próximo registro." + e.getMessage(), TabMovActivity.this);
				}
			}
			
		});
		
		btnRegAnt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if(edtTermEnt.getInt() == 0)
						edtTermEnt.setText(Funcoes.formata(Geral.terminal, 2));
					
					if(edtCdEnt.getString().equals(""))
						edtCdEnt.setText("9999");
					
					String criterio = "cd_emp = " + Geral.cdEmpresa +
							" and cd_ent between " + edtTermEnt.getString() + "0000 and " + edtTermEnt.getString() + "9999" +
							" and cd_ent < " + edtTermEnt.getString() + edtCdEnt.getString() +
							" and status_ent <> 2";
					
					long cdEnt = Funcoes.toLong(Funcoes.vlCampoTblLocal("max(cd_ent)", "ctrl_ent", criterio));
					if(cdEnt > 0) {
						mostraDados(cdEnt);
					}
				} catch (Exception e) {
					Funcoes.msgBox("Falha ao buscar registro anterior." + e.getMessage(), TabMovActivity.this);
				}
			}
		});
		
		edtTermEnt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					if(edtTermEnt.getInt() == 0){
						edtTermEnt.setText(Funcoes.formata(Geral.terminal, 2));
						edtTermEnt.selectAll();
					}
				}
			}
		});
		
		edtTermEnt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				try{
					if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
						if(edtTermDest.getInt() == 0){
							edtTermEnt.setText(Funcoes.formata(Geral.terminal, 2));
						}else
							edtTermEnt.setText(Funcoes.formata(edtTermEnt.getInt(), 2));
						
						if(edtTermEnt.getInt() != edtTermEnt.getCdAnterior()){
							String criterio = "cd_emp = " + Geral.cdEmpresa + " and cd_ent between " + edtTermEnt.getInt() + "0000 and " + edtTermEnt.getInt() + "9999";
							edtCdEnt.setText(Funcoes.formata(Funcoes.proximoNumeroLocal("cd_ent", "ctrl_ent", criterio), 6).substring(2));
							edtCdEnt.setCdAnterior(Funcoes.toLong(edtCdEnt.getString()));
						}
					}
				} catch(Exception e){
					Funcoes.msgBox("Falha ao buscar próximo código de movimentação.", TabMovActivity.this);
				}
				
				return false;
			}
		});;
		
		edtCdEnt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				try{
					if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
						edtCdEnt.formata(4);
						
						if(edtCdEnt.getLong() != edtCdEnt.getCdAnterior()){
							if(edtTermEnt.getInt() > 0 && edtCdEnt.getInt() > 0){
								long cdEnt = Funcoes.toLong(edtTermEnt.getString() + edtCdEnt.getString());
								
								if (Funcoes.toLong(Funcoes.vlCampoTblLocal("cd_ent", "ctrl_ent", "cd_emp = " + Geral.cdEmpresa + " and cd_ent = " + cdEnt).toString()) > 0){
									mostraDados(cdEnt);
								}else{
									Funcoes.msgBox("Nenhum registro encontrado com este código.", TabMovActivity.this);
									Funcoes.setaFoco(edtCdEnt);
								}
							}else{
								novoRegistro();
							}
						}
					}
				}catch (Exception e){
					Funcoes.msgBox("Falha ao consultar movimentação." + e.getMessage(), TabMovActivity.this);
				}
				return false;
			}
		});
		
		edtDtEnt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Date data = Funcoes.toDate(edtDtEnt.getString());
				
				DatePickerDialog dtpDlg = new DatePickerDialog(TabMovActivity.this, new DatePickerDialog.OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						edtDtEnt.setText(Funcoes.formataData(new Date(year - 1900, monthOfYear, dayOfMonth), "dd/MM/yyyy"));
					}
				}, data.getYear() + 1900, data.getMonth(), data.getDate());
				
				dtpDlg.show();
			}
		});

		edtDtEnt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					edtDtEnt.performClick();
			}
		});
		
		edtTermPrdr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					if(edtTermPrdr.getInt() == 0){
						edtTermPrdr.setText(Funcoes.formata(Geral.terminal, 2));
						edtTermPrdr.selectAll();
					}
				}
			}
		});
		
		edtTermPrdr.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					if(edtTermPrdr.getInt() == 0){
						edtTermPrdr.setText(Funcoes.formata(Geral.terminal, 2));
					}else
						edtTermPrdr.setText(Funcoes.formata(edtTermPrdr.getInt(), 2));
				}
				return false;
			}
		});
		
		edtCdPrdr.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				try {
					if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
						if(edtTermPrdr.getInt() > 0 && edtCdPrdr.getInt() > 0){
							edtCdPrdr.formata(4);
							
							if(edtCdPrdr.getLong() != edtCdPrdr.getCdAnterior()){
								long cdPes = Funcoes.toLong(edtTermPrdr.getString() + edtCdPrdr.getString());
								
								edtNmPrdr.setText(Funcoes.vlCampoTblLocal("nm_fants_pes", "pub_pes", "tp_pes_prdt = 1 and cd_emp = " + Geral.cdEmpresa + " and cd_pes = " + cdPes).toString());
								if (edtNmPrdr.getString().equals("")){
									Funcoes.msgBox("Produtor não encontrado.", TabMovActivity.this);
									Funcoes.setaFoco(edtCdPrdr);
								}
							}
						}else
							edtNmPrdr.setText("");
					}
				} catch (Exception e) {
					Funcoes.msgBox("Falha ao buscar dados do produtor." + e.getMessage(), TabMovActivity.this);
				}
				return false;
			}
		});
		
		btnPesqPrdr.setOnClickListener(new View.OnClickListener() {
			
			ItemSpinner selecionado;
			
			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				try{
					final View vPesq = ((LayoutInflater) TabMovActivity.this.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pesquisa, null, false);
					
					final Spinner spnFiltro = (Spinner) vPesq.findViewById(R.id.spnFiltro);
					final EditText edtPesq = (EditText) vPesq.findViewById(R.id.edtPesq);
					final ListView lstResult = (ListView) vPesq.findViewById(R.id.lstResult);
					
					ArrayList<ItemSpinner> lstFiltro = new ArrayList<ItemSpinner>();
					lstFiltro.add(new ItemSpinner("Nome fantasia", 0, "nm_fants_pes"));
					lstFiltro.add(new ItemSpinner("Razao Social", 1, "nm_razao_pes"));
					
					Funcoes.preencheSpinner(TabMovActivity.this, vPesq, R.id.spnFiltro, "Filtrar por", lstFiltro);
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
											   + " and pes.tp_pes_prdt = 1"
											   + " and pes.st_pes <> 2"
											   + " and "+ filtro +" like '%" + nome + "%'"
											   + " order by indice, " + filtro + "  limit 20";
									
									Cursor crsResult = Geral.localDB.rawQuery(sql, null);
									if(crsResult.getCount() > 0){
										Funcoes.preencheListViewLocal(TabMovActivity.this, vPesq, R.id.lstResult, crsResult, filtro, "cd_pes");
										crsResult.close();
									}
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao realizar consulta." + e.getMessage(), TabMovActivity.this);
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
					
					new AlertDialog.Builder(TabMovActivity.this)
							.setTitle("Pesquisa de Produtores")
							.setView(vPesq)
							.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									if(selecionado != null){
										edtTermPrdr.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(0, 2));
										edtCdPrdr.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(2));
										edtCdPrdr.onEditorAction(EditorInfo.IME_ACTION_NEXT);
									}									
								}
							})
							.setNegativeButton("Cancelar", null)
							.show().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					
					Funcoes.setaFoco(edtPesq);
					
				}catch(Exception e){
					Funcoes.msgBox("Falha ao pesquisar produtores." + e.getMessage(), TabMovActivity.this);
				}
				
			}
		});
		
		edtCdProd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				try{
					if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
						edtCdProd.formata(4);
						
						if(edtCdProd.getLong() != edtCdProd.getCdAnterior()){
							if(edtCdProd.getInt() > 0){
								edtDsProd.setText(Funcoes.vlCampoTblLocal("ds_prod", "pub_prod", "cd_emp = " + Geral.cdEmpresa + " and cd_prod = " + edtCdProd.getInt()).toString());
								if(edtDsProd.getString().equals("")){
									Funcoes.msgBox("Produto não encontrado.", TabMovActivity.this);
									Funcoes.setaFoco(edtCdProd);
								}
							}else{
								edtDsProd.setText("");					
							}
						}
					}
				} catch (Exception e) {
					Funcoes.msgBox("Falha ao buscar dados do produto.", TabMovActivity.this);
				}
				
				return false;
			}
		});
		
		btnPesqProd.setOnClickListener(new View.OnClickListener() {
			
			ItemSpinner selecionado;
			
			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				try{
					final View vPesq = ((LayoutInflater) TabMovActivity.this.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pesquisa, null, false);
					
					final Spinner spnFiltro = (Spinner) vPesq.findViewById(R.id.spnFiltro);
					final EditText edtPesq = (EditText) vPesq.findViewById(R.id.edtPesq);
					final ListView lstResult = (ListView) vPesq.findViewById(R.id.lstResult);
					
					ArrayList<ItemSpinner> lstFiltro = new ArrayList<ItemSpinner>();
					lstFiltro.add(new ItemSpinner("Descrição", 0, "ds_prod"));
					
					Funcoes.preencheSpinner(TabMovActivity.this, vPesq, R.id.spnFiltro, "Filtrar por", lstFiltro);
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
									
									String sql = "select prod.cd_prod, " + filtro + ", (case when " + filtro +" like '" + nome + "%' then 0 else 1 end) as indice"
											   + " from pub_prod prod"
											   + " where prod.cd_emp = " + Geral.cdEmpresa
											   + " and "+ filtro +" like '%" + nome + "%'"
											   + " order by indice, " + filtro + "  limit 20";
									
									Cursor crsResult = Geral.localDB.rawQuery(sql, null);
									if(crsResult.getCount() > 0){
										Funcoes.preencheListViewLocal(TabMovActivity.this, vPesq, R.id.lstResult, crsResult, filtro, "cd_prod");
										crsResult.close();
									}
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao realizar consulta" + e.getMessage(), TabMovActivity.this);
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
					
					new AlertDialog.Builder(TabMovActivity.this)
							.setTitle("Pesquisa de Produtos")
							.setView(vPesq)
							.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									if(selecionado != null){
										edtCdProd.setText(Funcoes.formata(selecionado.getCodigo(), 4));
										edtCdProd.onEditorAction(EditorInfo.IME_ACTION_NEXT);
									}									
								}
							})
							.setNegativeButton("Cancelar", null)
							.show().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);;
					
					Funcoes.setaFoco(edtPesq);
					
				}catch(Exception e){
					Funcoes.msgBox("Falha ao pesquisar produtos." + e.getMessage(), TabMovActivity.this);
				}
			}
		});
		
		edtPesoBruto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtPesoBruto.decimal(2);
					totalizaEntrada();					
				}
				return false;
			}
		});
		
		edtPercPalha.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtPercPalha.decimal(2);
					if(edtPercPalha.getDouble() > 10){
						Funcoes.msgBox("O percentual de palha não pode ser superior a 10%.", TabMovActivity.this);
						Funcoes.setaFoco(edtPercPalha);
					}else
						totalizaEntrada();					
				}
				return false;
			}
		});
		
		edtDescarte.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtDescarte.decimal(2);
					if(edtDescarte.getDouble() > edtPesoBruto.getDouble()){
						Funcoes.msgBox("Peso do descarte não pode ser superior ao peso bruto.", TabMovActivity.this);
						Funcoes.setaFoco(edtPesoBruto);
					}else
						totalizaEntrada();					
				}
				return false;
			}
		});
		
		edtQtdePecas.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					totalizaEntrada();					
				}
				return false;
			}
		});
		
		edtPrecoEnt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtPrecoEnt.decimal(2);
					totalizaEntrada();					
				}
				return false;
			}
		});
		
		edtTermCarg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					if(edtTermCarg.getInt() == 0){
						edtTermCarg.setText(Funcoes.formata(Geral.terminal, 2));
						edtTermCarg.selectAll();
					}
				}
			}
		});
		
		edtTermCarg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					if(edtTermCarg.getInt() == 0){
						edtTermCarg.setText(Funcoes.formata(Geral.terminal, 2));
					}else
						edtTermCarg.setText(Funcoes.formata(edtTermCarg.getInt(), 2));
				}
				return false;
			}
		});
		
		edtCdCarg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				try{
					if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
						if(edtTermCarg.getInt() > 0 && edtCdCarg.getInt() > 0){
							edtCdCarg.formata(4);
							
							if(edtCdCarg.getLong() != edtCdCarg.getCdAnterior()){
								long cdPes = Funcoes.toLong(edtTermCarg.getString() + edtCdCarg.getString());
								
								edtNmCarg.setText(Funcoes.vlCampoTblLocal("nm_fants_pes", "pub_pes", "tp_pes_carg = 1 and cd_emp = " + Geral.cdEmpresa + " and cd_pes = " + cdPes).toString());
								if (edtNmCarg.getString().equals("")){
									Funcoes.msgBox("Cargueiro não encontrado.", TabMovActivity.this);
									Funcoes.setaFoco(edtCdCarg);
								}
							}
						}else
							edtNmCarg.setText("");
					}
				} catch (Exception e) {
					Funcoes.msgBox("Falha ao buscar dados do cargueiro.", TabMovActivity.this);
				}
				
				return false;
			}
		});
		
		btnPesqCarg.setOnClickListener(new View.OnClickListener() {
			
			ItemSpinner selecionado;
			
			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				try{
					final View vPesq = ((LayoutInflater) TabMovActivity.this.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pesquisa, null, false);
					
					final Spinner spnFiltro = (Spinner) vPesq.findViewById(R.id.spnFiltro);
					final EditText edtPesq = (EditText) vPesq.findViewById(R.id.edtPesq);
					final ListView lstResult = (ListView) vPesq.findViewById(R.id.lstResult);
					
					ArrayList<ItemSpinner> lstFiltro = new ArrayList<ItemSpinner>();
					lstFiltro.add(new ItemSpinner("Nome fantasia", 0, "nm_fants_pes"));
					lstFiltro.add(new ItemSpinner("Razao Social", 1, "nm_razao_pes"));
					
					Funcoes.preencheSpinner(TabMovActivity.this, vPesq, R.id.spnFiltro, "Filtrar por", lstFiltro);
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
									
									String sql = "select pes.cd_pes, " + filtro + ", (case when " + filtro +" like '" + nome + "%' then 0 else 1 end) as indice"
											   + " from pub_pes pes"
											   + " where pes.cd_emp = " + Geral.cdEmpresa
											   + " and pes.tp_pes_carg = 1"
											   + " and pes.st_pes <> 2"
											   + " and "+ filtro +" like '%" + nome + "%'"
											   + " order by indice, " + filtro + "  limit 20";
									
									Cursor rsResult = Geral.localDB.rawQuery(sql, null);
									if(rsResult.getCount() > 0){
										Funcoes.preencheListViewLocal(TabMovActivity.this, vPesq, R.id.lstResult, rsResult, filtro, "cd_pes");
										rsResult.close();
									}
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao realizar consulta" + e.getMessage(), TabMovActivity.this);
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
					
					new AlertDialog.Builder(TabMovActivity.this)
							.setTitle("Pesquisa de Cargueiros")
							.setView(vPesq)
							.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									if(selecionado != null){
										edtTermCarg.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(0, 2));
										edtCdCarg.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(2));
										edtCdCarg.onEditorAction(EditorInfo.IME_ACTION_NEXT);
									}									
								}
							})
							.setNegativeButton("Cancelar", null)
							.show().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);;
					
					Funcoes.setaFoco(edtPesq);
					
				}catch(Exception e){
					Funcoes.msgBox("Falha ao pesquisar cargueiros." + e.getMessage(), TabMovActivity.this);
				}
				
			}
		});
		
		edtVlMaoObra.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtVlMaoObra.decimal(2);
					totalizaEntrada();					
				}
				return false;
			}
		});
		
		edtVlDespPrdr.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtVlDespPrdr.decimal(2);
					totalizaEntrada();					
				}
				return false;
			}
		});
		
		edtVlPgEnt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtVlPgEnt.decimal(2);
				}
				return false;
			}
		});
		
		edtTermDest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					if(edtTermDest.getInt() == 0){
						edtTermDest.setText(Funcoes.formata(Geral.terminal, 2));
						edtTermDest.selectAll();
					}
				}
			}
		});
		
		edtTermDest.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					if(edtTermDest.getInt() == 0){
						edtTermDest.setText(Funcoes.formata(Geral.terminal, 2));
					}else
						edtTermDest.setText(Funcoes.formata(edtTermDest.getInt(), 2));
				}
				return false;
			}
		});
		
		edtCdDest.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				try{
					if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
						if(edtTermDest.getInt() > 0 && edtCdDest.getInt() > 0){
							edtCdDest.formata(4);
							
							if(edtCdDest.getLong() != edtCdDest.getCdAnterior()){
								long cdPes = Funcoes.toLong(edtTermDest.getString() + edtCdDest.getString());
								
								edtNmDest.setText(Funcoes.vlCampoTblLocal("nm_fants_pes", "pub_pes", "tp_pes_dest = 1 and cd_emp = " + Geral.cdEmpresa + " and cd_pes = " + cdPes).toString());
								if (edtNmDest.getString().equals("")){
									Funcoes.msgBox("Destinatário não encontrado.", TabMovActivity.this);
									Funcoes.setaFoco(edtCdDest);
								}
							}
						}else
							edtNmDest.setText("");
					}
				} catch (Exception e) {
					Funcoes.msgBox("Falha ao buscar dados do destinatário." + e.getMessage(), TabMovActivity.this);
				}
				return false;
			}
		});
		
		btnPesqDest.setOnClickListener(new View.OnClickListener() {
			
			ItemSpinner selecionado;
			
			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				try{
					final View vPesq = ((LayoutInflater) TabMovActivity.this.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pesquisa, null, false);
					
					final Spinner spnFiltro = (Spinner) vPesq.findViewById(R.id.spnFiltro);
					final EditText edtPesq = (EditText) vPesq.findViewById(R.id.edtPesq);
					final ListView lstResult = (ListView) vPesq.findViewById(R.id.lstResult);
					
					ArrayList<ItemSpinner> lstFiltro = new ArrayList<ItemSpinner>();
					lstFiltro.add(new ItemSpinner("Nome fantasia", 0, "nm_fants_pes"));
					lstFiltro.add(new ItemSpinner("Razao Social", 1, "nm_razao_pes"));
					
					Funcoes.preencheSpinner(TabMovActivity.this, vPesq, R.id.spnFiltro, "Filtrar por", lstFiltro);
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
									
									String sql = "select pes.cd_pes, " + filtro + ", (case when " + filtro +" like '" + nome + "%' then 0 else 1 end) as indice"
											   + " from pub_pes pes"
											   + " where pes.cd_emp = " + Geral.cdEmpresa
											   + " and pes.tp_pes_dest = 1"
											   + " and pes.st_pes <> 2"
											   + " and "+ filtro +" like '%" + nome + "%'"
											   + " order by indice, " + filtro + "  limit 20";
									
									Cursor rsResult = Geral.localDB.rawQuery(sql, null);
									if(rsResult.getCount() > 0){
										Funcoes.preencheListViewLocal(TabMovActivity.this, vPesq, R.id.lstResult, rsResult, filtro, "cd_pes");
										rsResult.close();
									}
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao realizar consulta" + e.getMessage(), TabMovActivity.this);
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
					
					new AlertDialog.Builder(TabMovActivity.this)
							.setTitle("Pesquisa de Destinatários")
							.setView(vPesq)
							.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									if(selecionado != null){
										edtTermDest.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(0, 2));
										edtCdDest.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(2));
										edtCdDest.onEditorAction(EditorInfo.IME_ACTION_NEXT);
									}									
								}
							})
							.setNegativeButton("Cancelar", null)
							.show().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);;
					
					Funcoes.setaFoco(edtPesq);
					
				}catch(Exception e){
					Funcoes.msgBox("Falha ao pesquisar destinatários." + e.getMessage(), TabMovActivity.this);
				}
				
			}
		});
		
		edtDtSai.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Date data = Funcoes.toDate(edtDtSai.getString());
				
				DatePickerDialog dtpDlg = new DatePickerDialog(TabMovActivity.this, new DatePickerDialog.OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						edtDtSai.setText(Funcoes.formataData(new Date(year - 1900, monthOfYear, dayOfMonth), "dd/MM/yyyy"));
					}
				}, data.getYear() + 1900, data.getMonth(), data.getDate());
				
				dtpDlg.show();
			}
		});

		edtDtSai.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					edtDtSai.performClick();
			}
		});
		
		edtNmMotr.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtNmMotr.setText(edtNmMotr.getString().trim().toUpperCase());
				}
				
				return false;
			}
		});
		
		btnPesqMotr.setOnClickListener(new View.OnClickListener() {
			
			ItemSpinner selecionado;
			
			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				try{
					final View vPesq = ((LayoutInflater) TabMovActivity.this.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pesquisa, null, false);
					
					final Spinner spnFiltro = (Spinner) vPesq.findViewById(R.id.spnFiltro);
					final EditText edtPesq = (EditText) vPesq.findViewById(R.id.edtPesq);
					final ListView lstResult = (ListView) vPesq.findViewById(R.id.lstResult);
					
					ArrayList<ItemSpinner> lstFiltro = new ArrayList<ItemSpinner>();
					lstFiltro.add(new ItemSpinner("Nome fantasia", 0, "nm_fants_pes"));
					lstFiltro.add(new ItemSpinner("Razao Social", 1, "nm_razao_pes"));
					
					Funcoes.preencheSpinner(TabMovActivity.this, vPesq, R.id.spnFiltro, "Filtrar por", lstFiltro);
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
									
									String sql = "select pes.cd_pes, " + filtro + ", (case when " + filtro +" like '" + nome + "%' then 0 else 1 end) as indice"
											   + " from pub_pes pes"
											   + " where pes.cd_emp = " + Geral.cdEmpresa
											   + " and pes.tp_pes_motr = 1"
											   + " and pes.st_pes <> 2"
											   + " and "+ filtro +" like '%" + nome + "%'"
											   + " order by indice, " + filtro + "  limit 20";
									
									Cursor rsResult = Geral.localDB.rawQuery(sql, null);
									if(rsResult.getCount() > 0){
										Funcoes.preencheListViewLocal(TabMovActivity.this, vPesq, R.id.lstResult, rsResult, filtro, "cd_pes");
										rsResult.close();
									}
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao realizar consulta" + e.getMessage(), TabMovActivity.this);
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
					
					new AlertDialog.Builder(TabMovActivity.this)
							.setTitle("Pesquisa de Motoristas")
							.setView(vPesq)
							.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									if(selecionado != null){
										try {
											new AsyncTask<Void, Void, Boolean>(){
												
												ProgressDialog prgDialog;
												String erro = "";
												Cursor rsMotr;
												int stRegAnt;
												
												@Override
												protected void onPreExecute() {
													prgDialog = ProgressDialog.show(TabMovActivity.this, "", "Pesquisando...");
												}
												
												@Override
												protected Boolean doInBackground(Void... params) {
													try {
														String sql = "select pes.*"
																+ " from pub_pes pes"
																+ " where pes.cd_emp = " + Geral.cdEmpresa
																+ " and pes.cd_pes = " + selecionado.getCodigo();
														
														rsMotr = Geral.localDB.rawQuery(sql, null);
														
														if(rsMotr.getCount() > 0){
															return true;
														}else{
															throw new RuntimeException("Dados do motorista não encontrados.");
														}
														
													} catch (Exception e) {
														erro = e.getMessage();
														return false;
													}
												}
												
												@Override
												protected void onPostExecute(Boolean result) {
													try {
														stRegAnt = stRegistro;
														
														if(result){			
															rsMotr.moveToFirst();
															stRegistro = ST_REG_VISUALIZANDO;
															
															edtNmMotr.setText(rsMotr.getString(rsMotr.getColumnIndex("nm_razao_pes")));
															
															String fone = Funcoes.separaNumeros(rsMotr.getString(rsMotr.getColumnIndex("fone_pes")));
															switch (fone.length()){
															case 10:
																edtFoneMotr.setText(fone.replaceAll("([0-9]{2})([0-9]{4})([0-9]{4})", "($1)$2-$3"));
																break;
															case 11:
																edtFoneMotr.setText(fone.replaceAll("([0-9]{2})([0-9]{5})([0-9]{4})", "($1)$2-$3"));
																break;
															default:
																edtFoneMotr.setText("");
															}
															
															String cpfCgc = Funcoes.separaNumeros(rsMotr.getString(rsMotr.getColumnIndex("cpf_cgc_pes")));
															switch (cpfCgc.length()) {
															case 11:
																edtCPFCGCMotr.setText(cpfCgc.replaceAll("([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})", "$1.$2.$3-$4"));
																break;
															case 14:
																edtCPFCGCMotr.setText(cpfCgc.replaceAll("([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})", "$1.$2.$3/$4-$5"));
																break;
															default:
																edtCPFCGCMotr.setText("");
															}
															
															edtPlacaMotr.setText(rsMotr.getString(rsMotr.getColumnIndex("placa_veic")));
															
															String uf = getResources().getStringArray(R.array.uf_cid)[rsMotr.getInt(rsMotr.getColumnIndex("uf_placa")) + 1];
															Funcoes.selItemSpinner(TabMovActivity.this, vMovSai, R.id.spnUfPlaca, uf, false);
															Funcoes.selItemSpinner(TabMovActivity.this, vMovSai, R.id.spnCidPlaca, rsMotr.getInt(rsMotr.getColumnIndex("cd_cid")), true);
															
															rsMotr.close();
															
															Funcoes.setaFoco(edtVlFrete);
															
														}else{
															throw new RuntimeException(erro);
														}
														
													} catch (Exception e) {
														Funcoes.msgBox("Falha ao pesquisar motorista." + e.getMessage(), TabMovActivity.this);
														edtNmMotr.setText("");
														edtFoneMotr.setText("");
														edtCPFCGCMotr.setText("");
														edtPlacaMotr.setText("");
														Funcoes.selItemSpinner(TabMovActivity.this, vMovSai, R.id.spnUfPlaca, "--", true);
														
													} finally{
														if(prgDialog.isShowing())
															prgDialog.dismiss();
														
														stRegistro = stRegAnt;
													}
												}
												
											}.execute();
											
										} catch (Exception e) {
											Funcoes.msgBox("Falha ao buscar dados do motorista." + e.getMessage(), TabMovActivity.this);
										}
									}									
								}
							})
							.setNegativeButton("Cancelar", null)
							.show().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);;
					
					Funcoes.setaFoco(edtPesq);
					
				}catch(Exception e){
					Funcoes.msgBox("Falha ao pesquisar motoristas." + e.getMessage(), TabMovActivity.this);
				}
				
			}
		});
		
		edtFoneMotr.setMask("(99)99999-9999");
		edtFoneMotr.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					String fone = Funcoes.separaNumeros(edtFoneMotr.getString());
					if(!fone.equals("")){
						switch (fone.length()) {
						case 10:
							//edtFoneMotr.setText(fone.replaceAll("([0-9]{2})([0-9]{4})([0-9]{4})", "($1)$2-$3"));
							break;
						case 11:
							//edtFoneMotr.setText(fone.replaceAll("([0-9]{2})([0-9]{5})([0-9]{4})", "($1)$2-$3"));
							break;
						default:
							Funcoes.msgBox("Telefone inválido.", TabMovActivity.this);
							Funcoes.setaFoco(edtFoneMotr);
							break;
						}
					}
				}
				return false;
			}
		});
		
		edtCPFCGCMotr.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					String cpfCgc = Funcoes.separaNumeros(edtCPFCGCMotr.getString());
					if(!cpfCgc.equals("")){
						switch (cpfCgc.length()) {
						case 11:
							edtCPFCGCMotr.setText(cpfCgc.replaceAll("([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})", "$1.$2.$3-$4"));
							break;
						case 14:
							edtCPFCGCMotr.setText(cpfCgc.replaceAll("([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})", "$1.$2.$3/$4-$5"));
							break;
						default:
							Funcoes.msgBox("CPF ou CGC inválido.", TabMovActivity.this);
							Funcoes.setaFoco(edtCPFCGCMotr);
							break;
						}
					}
				}
				return false;
			}
		});
		
		edtPlacaMotr.setMask("AAA - 9999");
		edtPlacaMotr.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					try{
						String placa = edtPlacaMotr.getString().replaceAll("[^a-zA-Z0-9]", "");
						if(!placa.equals("")){
							placa = placa.toUpperCase();
							if(placa.length() == 7) {
								if(placa.substring(0, 3).replaceAll("[0-9]", "").length() != 3){
									throw new RuntimeException("Prefixo da placa inválido.");
								}else if(placa.substring(3).replaceAll("[A-Z]", "").length() != 4){
									throw new RuntimeException("Sufixo da placa inválido.");
								}
								
								//edtPlacaMotr.setText(placa.replaceAll("([A-Z]{3})([0-9]{4})", "$1 - $2"));
							}else{
								throw new RuntimeException("Placa inválida.");
							}
						}
					}catch(Exception e){
						Funcoes.msgBox(e.getMessage(), TabMovActivity.this);
						Funcoes.setaFoco(edtPlacaMotr);
					}
				}
				return false;
			}
		});
		
		edtVlFrete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtVlFrete.decimal(2);
					totalizaEntrada();					
				}
				return false;
			}
		});
		
		edtVlICMSFrete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtVlICMSFrete.decimal(2);
					totalizaEntrada();	
				}
				return false;
			}
		});
		
		edtPrecoSai.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtPrecoSai.decimal(2);
					totalizaEntrada();					
				}
				return false;
			}
		});
		
		edtVlRecSai.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtVlRecSai.decimal(2);				
				}
				return false;
			}
		});
		
		spnTpFreteSai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				totalizaEntrada();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		
		spnTpICMS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				totalizaEntrada();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
			
		});
		
		novoRegistro();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuItem mnuNovo = menu.add("Novo Registro");
		mnuNovo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

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
				if(validaEntrada()){
					if(stRegistro == ST_REG_INCLUINDO){
						incluiRegistro();
					}else if(stRegistro == ST_REG_EDITANDO){
						alteraRegistro();
					}
				}
				return false;
			}
		});
		
		MenuItem mnuExcluir = menu.add("Excluir");
		mnuExcluir.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if(stRegistro != ST_REG_EDITANDO)
					return false;
				
				if(edtTermEnt.getInt() > 0 && edtCdEnt.getInt() > 0){
					excluiRegistro();
				}else{
					Funcoes.msgBox("Informe o código da entrada.", TabMovActivity.this);
					Funcoes.setaFoco(edtTermEnt);
				}
				return false;
			}
		});
		
		MenuItem mnuPesq = menu.add("Pesquisar");
		mnuPesq.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			class Pedido extends HashMap<String, String>{

				private static final long serialVersionUID = 1L;

				public Pedido(long cdPedido, String nomePessoa, Date dtEmissao, double vlTotal){
					this.put("cdEnt", Funcoes.formata(cdPedido, 6));
					this.put("nmPessoa", nomePessoa);
					this.put("dtEmissao", Funcoes.formataData(dtEmissao, "dd/MM/yyyy"));
					this.put("vlTotal", Funcoes.decimal(vlTotal, 2));
				}
			}
			
			int pedSel = -1;
			
			@SuppressLint("InflateParams")
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				try{
					View vPesq = ((LayoutInflater) TabMovActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pesq_ent,  null, false);
					
					final CEditText edtDtIni = (CEditText) vPesq.findViewById(R.id.edtDtIni);
					final CEditText edtDtFin = (CEditText) vPesq.findViewById(R.id.edtDtFin);
					final Spinner spnTpDt = (Spinner) vPesq.findViewById(R.id.spnTpDt);
					final CheckBox chkTodasPes = (CheckBox) vPesq.findViewById(R.id.chkTodasPes);
					final Spinner spnTpPes = (Spinner) vPesq.findViewById(R.id.spnTpPes);
					final CEditText edtTermPes = (CEditText) vPesq.findViewById(R.id.edtTermPes);
					final CEditText edtCdPes = (CEditText) vPesq.findViewById(R.id.edtCdPes);
					final CEditText edtNmPes = (CEditText) vPesq.findViewById(R.id.edtNmPes);
					final ImageButton btnPesqPes = (ImageButton) vPesq.findViewById(R.id.btnPesqPes);					
					final ImageButton btnPesqEnt = (ImageButton) vPesq.findViewById(R.id.btnPesqEnt);
					final TextView txvDsPes = (TextView) vPesq.findViewById(R.id.txvDsPes);
					final ListView lstPedidos = (ListView) vPesq.findViewById(R.id.lstPedidos);
					
					edtDtIni.setText(Funcoes.formataData(new Date(), "dd/MM/yyyy"));
					edtDtFin.setText(Funcoes.formataData(new Date(), "dd/MM/yyyy"));
					
					edtDtIni.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Date data = Funcoes.toDate(edtDtIni.getString());
							
							DatePickerDialog dtpDlg = new DatePickerDialog(TabMovActivity.this, new DatePickerDialog.OnDateSetListener() {
								
								@Override
								public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
									edtDtIni.setText(Funcoes.formataData(new Date(year - 1900, monthOfYear, dayOfMonth), "dd/MM/yyyy"));
								}
							}, data.getYear() + 1900, data.getMonth(), data.getDate());
							
							dtpDlg.show();
						}
					});

					edtDtIni.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if(hasFocus)
								edtDtIni.performClick();
						}
					});
					
					edtDtFin.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Date data = Funcoes.toDate(edtDtFin.getString());
							
							DatePickerDialog dtpDlg = new DatePickerDialog(TabMovActivity.this, new DatePickerDialog.OnDateSetListener() {
								
								@Override
								public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
									edtDtFin.setText(Funcoes.formataData(new Date(year - 1900, monthOfYear, dayOfMonth), "dd/MM/yyyy"));
								}
							}, data.getYear() + 1900, data.getMonth(), data.getDate());
							
							dtpDlg.show();
						}
					});

					edtDtFin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if(hasFocus)
								edtDtFin.performClick();
						}
					});
					
					spnTpPes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
							if(position == 0){
								txvDsPes.setText("Nome Produtor");
								edtTermPes.setText("");
								edtCdPes.setText("");
								
								Funcoes.setaFoco(edtTermPes);
							}else{
								txvDsPes.setText("Nome Destinatário");
							}
							
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {}
					});
					
					chkTodasPes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if(isChecked){
								edtTermPes.setText("00");
								edtTermPes.setEnabled(false);
								edtCdPes.setText("0000");
								edtCdPes.setEnabled(false);
								edtNmPes.setText("");		
								chkTodasPes.requestFocusFromTouch();
							}else{
								edtTermPes.setEnabled(true);
								edtCdPes.setEnabled(true);
								Funcoes.setaFoco(edtTermPes);
							}
						}
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
							if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
								if(edtTermPes.getInt() == 0){
									edtTermPes.setText(Funcoes.formata(Geral.terminal, 2));
								}else
									edtTermPes.setText(Funcoes.formata(edtTermPes.getInt(), 2));
							}
							return false;
						}
					});
					
					edtCdPes.setOnEditorActionListener(new TextView.OnEditorActionListener() {
						
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							try{
								if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
									if(edtTermPes.getInt() > 0 && edtCdPes.getInt() > 0){
										edtCdPes.formata(4);
										
										if(edtCdPes.getLong() != edtCdPes.getCdAnterior()){
											long cdPes = Funcoes.toLong(edtTermPes.getString() + edtCdPes.getString());
											String criterio = "cd_emp = " + Geral.cdEmpresa + " and cd_pes = " + cdPes;
											criterio += " and " + (spnTpPes.getSelectedItemPosition() == 0 ? "tp_pes_prdt = 1" : "tp_pes_dest = 1");
											
											edtNmPes.setText(Funcoes.vlCampoTblLocal("nm_fants_pes", "pub_pes", criterio).toString());
											if (edtNmPes.getString().equals("")){
												Funcoes.msgBox((spnTpPes.getSelectedItemPosition() == 0 ? "Produtor" : "Destinatário") + " não encontrado.", TabMovActivity.this);
												Funcoes.setaFoco(edtCdPes);
											}
										}
									}else
										edtNmPes.setText("");
								}
							} catch (Exception e){
								Funcoes.msgBox("Falha ao buscar dados da pessoa." + e.getMessage(), TabMovActivity.this);
							}
							return false;
						}
					});
					
					btnPesqPes.setOnClickListener(new View.OnClickListener() {
						
						ItemSpinner selecionado;
						
						@SuppressLint("InflateParams")
						@Override
						public void onClick(View v) {
							try{
								final View vPesq = ((LayoutInflater) TabMovActivity.this.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pesquisa, null, false);
								
								final Spinner spnFiltro = (Spinner) vPesq.findViewById(R.id.spnFiltro);
								final EditText edtPesq = (EditText) vPesq.findViewById(R.id.edtPesq);
								final ListView lstResult = (ListView) vPesq.findViewById(R.id.lstResult);
								
								ArrayList<ItemSpinner> lstFiltro = new ArrayList<ItemSpinner>();
								lstFiltro.add(new ItemSpinner("Nome fantasia", 0, "nm_fants_pes"));
								lstFiltro.add(new ItemSpinner("Razao Social", 1, "nm_razao_pes"));
								
								Funcoes.preencheSpinner(TabMovActivity.this, vPesq, R.id.spnFiltro, "Filtrar por", lstFiltro);
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
												
												String sql = "select pes.cd_pes, " + filtro  + ", (case when " + filtro +" like '" + nome + "%' then 0 else 1 end) as indice"
														   + " from pub_pes pes"
														   + " where pes.cd_emp = " + Geral.cdEmpresa
														   + " and " + (spnTpPes.getSelectedItemPosition() == 0 ? "pes.tp_pes_prdt = 1" : "pes.tp_pes_dest = 1")  
														   + " and pes.st_pes <> 2"
														   + " and "+ filtro +" like '%" + nome + "%'"
														   + " order by indice, " + filtro + "  limit 20";
												
												Cursor rsResult = Geral.localDB.rawQuery(sql, null);
												if(rsResult.getCount() > 0){
													Funcoes.preencheListViewLocal(TabMovActivity.this, vPesq, R.id.lstResult, rsResult, filtro, "cd_pes");
													rsResult.close();
												}
												
											} catch (Exception e) {
												Funcoes.msgBox("Falha ao realizar consulta" + e.getMessage(), TabMovActivity.this);
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
								
								new AlertDialog.Builder(TabMovActivity.this)
										.setTitle("Pesquisa de " + spnTpPes.getSelectedItem().toString())
										.setView(vPesq)
										.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface arg0, int arg1) {
												if(selecionado != null){
													chkTodasPes.setChecked(false);
													edtTermPes.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(0, 2));
													edtCdPes.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(2));
													edtCdPes.onEditorAction(EditorInfo.IME_ACTION_NEXT);
												}									
											}
										})
										.setNegativeButton("Cancelar", null)
										.show().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);;
								
							}catch(Exception e){
								Funcoes.msgBox("Falha ao realizar pesquisa." + e.getMessage(), TabMovActivity.this);
							}
							
						}
					});
					
					btnPesqEnt.setOnClickListener(new View.OnClickListener() {
						
						private boolean validaParametros(){
							try {
								
								if(edtDtFin.getDate() == null){
									Funcoes.msgBox("Data inicial inválida.", TabMovActivity.this);
									Funcoes.setaFoco(edtDtIni);
								}else if(edtDtFin.getDate() == null){
									Funcoes.msgBox("Data final inválida.", TabMovActivity.this);
									Funcoes.setaFoco(edtDtFin);
								}else if(edtDtFin.getDate().before(edtDtIni.getDate())){
									Funcoes.msgBox("Data final inferior a data inicial.", TabMovActivity.this);
									Funcoes.setaFoco(edtDtFin);
								}else if(!chkTodasPes.isChecked() && (edtTermPes.getInt() == 0 || edtCdPes.getInt() == 0 || edtNmPes.getString().equals(""))){
									Funcoes.msgBox("Informe um " + spnTpPes.getSelectedItem().toString(), TabMovActivity.this);
									Funcoes.setaFoco(edtTermPes);
								}else{
									return true;
								}
								
								return false;
								
							} catch (Exception e) {
								return false;
							}
						}
						
						@Override
						public void onClick(View v) {
							try{
								
								if(!validaParametros())
									return;
								
								pedSel = -1;
								new AsyncTask<Void, Void, Void>(){
									ProgressDialog prgDialog;
									String erro = "";
									ArrayList<Pedido> arrayPedidos = null;
									
									@Override
									protected void onPreExecute() {
										prgDialog = ProgressDialog.show(TabMovActivity.this, "", "Pesquisando...");
									}
									
									@SuppressLint("SimpleDateFormat")
									@Override
									protected Void doInBackground(Void... params) {
										try{
											String sql = "select e.cd_ent, p.nm_fants_pes, e.dt_ent, e.vl_tot_prdt"
													+ " from ctrl_ent e inner join pub_pes p"
													+ " on p.cd_emp = e.cd_emp"
													+ " and p.cd_pes = " + (spnTpPes.getSelectedItemPosition() == 0 ? "e.cd_pes_prdt" : "e.cd_pes_dest")
													+ " where e.cd_emp = " + Geral.cdEmpresa;
											
											switch (spnTpDt.getSelectedItemPosition()) {
											case 0:
												sql += " and e.dt_emissao between " + Funcoes.retDataLocal(edtDtIni.getDate()) + " and " + Funcoes.retDataLocal(edtDtFin.getDate());
												break;
											case 1:
												sql += " and e.dt_ent between " + Funcoes.retDataLocal(edtDtIni.getDate()) + " and " + Funcoes.retDataLocal(edtDtFin.getDate());
												break;
											case 2:
												sql += " and e.dt_sai between " + Funcoes.retDataLocal(edtDtIni.getDate()) + " and " + Funcoes.retDataLocal(edtDtFin.getDate());
												break;
											}

											if(!chkTodasPes.isChecked())
												sql += " and p.cd_pes = " + edtTermPes.getString() + edtCdPes.getString();
											
											sql += " and e.status_ent <> 2" +
											       " order by e.dt_ent limit 20";
											
											Cursor rsResult = Geral.localDB.rawQuery(sql, null);
											if(rsResult.getCount() > 0){
												arrayPedidos = new ArrayList<Pedido>();
												rsResult.moveToFirst();
												do{
													Pedido pedido = new Pedido(rsResult.getLong(rsResult.getColumnIndex("cd_ent")), rsResult.getString(rsResult.getColumnIndex("nm_fants_pes")), Funcoes.toDate(rsResult.getString(rsResult.getColumnIndex("dt_ent")), "yyyy-MM-dd"), rsResult.getDouble(rsResult.getColumnIndex("vl_tot_prdt")));
													arrayPedidos.add(pedido);
												}while(rsResult.moveToNext());
												
											}else{
												erro = "Nenhum pedido encontrado.";
												return null;
											}
											
										}catch(Exception e){
											erro = "Falha ao realizar consulta." + e.getMessage();
										}
										
										return null;
									}
									
									@Override
									protected void onPostExecute(Void result) {
										try {
											if(!erro.equals(""))													
												throw new RuntimeException(erro);
												
											if(arrayPedidos != null){
												SimpleAdapter listAdap = new SimpleAdapter(TabMovActivity.this, arrayPedidos, R.layout.adapter_pesq_ent, new String[]{"cdEnt", "nmPessoa", "dtEmissao", "vlTotal"}, new int[]{R.id.txvCdPedido, R.id.txvNmCliente, R.id.txvDtEmissao, R.id.txvVlTotal}){
													@Override
													public View getView(int position, View convertView, ViewGroup parent) {
														View vTemp = super.getView(position, convertView, parent);
														if(vTemp != null){
															RadioButton rdb = (RadioButton) vTemp.findViewById(R.id.rdbMarcado);
															rdb.setChecked(position == pedSel);
														}
														
														return vTemp;
													}
												};
												
												lstPedidos.setAdapter(listAdap);
											}
											
										} catch (Exception e) {
											Funcoes.msgBox(e.getMessage(), TabMovActivity.this);
										} finally {
											if(prgDialog.isShowing())
												prgDialog.dismiss();
										}
									}
									
								}.execute();
								
							}catch(Exception e){
								Funcoes.msgBox("Falha ao realizar consulta." + e.getMessage(), TabMovActivity.this);
							}
						}
					});

					lstPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							pedSel = arg2;
							((SimpleAdapter) lstPedidos.getAdapter()).notifyDataSetChanged();
						}
					});
					
					
					new AlertDialog.Builder(TabMovActivity.this)
					.setTitle("Pesquisa de Pedidos")
					.setView(vPesq)
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							if(pedSel >= 0){
								Pedido pedido = (Pedido) lstPedidos.getItemAtPosition(pedSel);
								if(pedido != null){
									mostraDados(Funcoes.toLong(pedido.get("cdEnt")));
								}
							}
						}
					})
					.setNegativeButton("Cancelar", null)
					.show()
					.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					
				}catch(Exception e){
					Funcoes.msgBox(e.getMessage(), TabMovActivity.this);
				}
				return false;
			}
		});
		
		MenuItem.OnMenuItemClickListener mnuExportarClick = new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				try {
					if(stRegistro != ST_REG_EDITANDO)
						return false;
					
					Intent intRelEnt = new Intent(TabMovActivity.this, RelEntrada.class);
					
					intRelEnt.putExtra("tipo", item.getTitle());
					TabMovActivity.this.startActivity(intRelEnt);

				} catch (Exception e) {
					Funcoes.msgBox("Falha ao exportar registro." + e.getMessage(), TabMovActivity.this);
					novoRegistro();
				}
				
				return false;
			}
		};
		
		MenuItem mnuExpPDF = menu.add("Exportar");
		mnuExpPDF.setOnMenuItemClickListener(mnuExportarClick);
		
		MenuItem mnuExpPDFSemPrdt = menu.add("Exportar (Sem produtor)");
		mnuExpPDFSemPrdt.setOnMenuItemClickListener(mnuExportarClick);
		
		MenuItem mnuRelMov = menu.add("Relatório de Movimentações");
		mnuRelMov.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			@SuppressLint("InflateParams")
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				try {
					View vRelMov = TabMovActivity.this.getLayoutInflater().inflate(R.layout.dialog_rel_mov, null);
					
					final CEditText edtDtIni = (CEditText) vRelMov.findViewById(R.id.edtDtIni);
					final CEditText edtDtFin = (CEditText) vRelMov.findViewById(R.id.edtDtFin);
					final Spinner spnStEntSai = (Spinner) vRelMov.findViewById(R.id.spnStEntSai);
					final CheckBox chkTdPlacas = (CheckBox) vRelMov.findViewById(R.id.chkTdPlacas);
					final CEditText edtPlaca = (CEditText) vRelMov.findViewById(R.id.edtPlaca);
					final Spinner spnPrcExib = (Spinner) vRelMov.findViewById(R.id.spnPrcExib);
					final CheckBox chkTdPrdt = (CheckBox) vRelMov.findViewById(R.id.chkTdPrdt);
					final CEditText edtTermPrdt = (CEditText) vRelMov.findViewById(R.id.edtTermPrdt);
					final CEditText edtCdPrdt = (CEditText) vRelMov.findViewById(R.id.edtCdPrdt);
					final CEditText edtNmPrdt = (CEditText) vRelMov.findViewById(R.id.edtNmPrdt);
					final ImageButton btnPesqPrdt = (ImageButton) vRelMov.findViewById(R.id.btnPesqPrdt);
					final CheckBox chkTdDest = (CheckBox) vRelMov.findViewById(R.id.chkTdDest);
					final CEditText edtTermDest = (CEditText) vRelMov.findViewById(R.id.edtTermDest);
					final CEditText edtCdDest = (CEditText) vRelMov.findViewById(R.id.edtCdDest);
					final CEditText edtNmDest = (CEditText) vRelMov.findViewById(R.id.edtNmDest);
					final ImageButton btnPesqDest = (ImageButton) vRelMov.findViewById(R.id.btnPesqDest);
					final CheckBox chkTdCarg = (CheckBox) vRelMov.findViewById(R.id.chkTdCarg);
					final CEditText edtTermCarg = (CEditText) vRelMov.findViewById(R.id.edtTermCarg);
					final CEditText edtCdCarg = (CEditText) vRelMov.findViewById(R.id.edtCdCarg);
					final CEditText edtNmCarg = (CEditText) vRelMov.findViewById(R.id.edtNmCarg);
					final ImageButton btnPesqCarg = (ImageButton) vRelMov.findViewById(R.id.btnPesqCarg);
					
					edtDtIni.setText(Funcoes.formataData(new Date(), "dd/MM/yyyy"));
					edtDtFin.setText(Funcoes.formataData(new Date(), "dd/MM/yyyy"));
					edtPlaca.setText("");
					
					edtDtIni.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Date data = Funcoes.toDate(edtDtIni.getString());
							
							DatePickerDialog dtpDlg = new DatePickerDialog(TabMovActivity.this, new DatePickerDialog.OnDateSetListener() {
								
								@Override
								public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
									edtDtIni.setText(Funcoes.formataData(new Date(year - 1900, monthOfYear, dayOfMonth), "dd/MM/yyyy"));
								}
							}, data.getYear() + 1900, data.getMonth(), data.getDate());
							
							dtpDlg.show();
						}
					});

					edtDtIni.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if(hasFocus)
								edtDtIni.performClick();
						}
					});
					
					edtDtFin.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Date data = Funcoes.toDate(edtDtFin.getString());
							
							DatePickerDialog dtpDlg = new DatePickerDialog(TabMovActivity.this, new DatePickerDialog.OnDateSetListener() {
								
								@Override
								public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
									edtDtFin.setText(Funcoes.formataData(new Date(year - 1900, monthOfYear, dayOfMonth), "dd/MM/yyyy"));
								}
							}, data.getYear() + 1900, data.getMonth(), data.getDate());
							
							dtpDlg.show();
						}
					});

					edtDtFin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if(hasFocus)
								edtDtFin.performClick();
						}
					});
					
					chkTdPlacas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if(isChecked){
								edtPlaca.setText("");
								edtPlaca.setEnabled(false);
							}else{
								edtPlaca.setEnabled(true);
							}
						}
					});
					
					edtPlaca.setMask("AAA - 9999");
					edtPlaca.setOnEditorActionListener(new TextView.OnEditorActionListener() {
						
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
								try{
									String placa = edtPlaca.getString().replaceAll("[^a-zA-Z0-9]", "");
									if(!placa.equals("")){
										placa = placa.toUpperCase();
										if(placa.length() == 7) {
											if(placa.substring(0, 3).replaceAll("[0-9]", "").length() != 3){
												throw new RuntimeException("Prefixo da placa inválido.");
											}else if(placa.substring(3).replaceAll("[A-Z]", "").length() != 4){
												throw new RuntimeException("Sufixo da placa inválido.");
											}
											
											//edtPlacaMotr.setText(placa.replaceAll("([A-Z]{3})([0-9]{4})", "$1 - $2"));
										}else{
											throw new RuntimeException("Placa inválida.");
										}
									}
								}catch(Exception e){
									Funcoes.msgBox(e.getMessage(), TabMovActivity.this);
									Funcoes.setaFoco(edtPlaca);
								}
							}
							return false;
						}
					});
							
					chkTdPrdt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if(isChecked){
								edtTermPrdt.setText("");
								edtTermPrdt.setEnabled(false);
								edtCdPrdt.setText("");
								edtCdPrdt.setEnabled(false);
								edtNmPrdt.setText("");
							}else{
								edtTermPrdt.setEnabled(true);
								edtCdPrdt.setEnabled(true);
								Funcoes.setaFoco(edtTermPrdt);
							}
						}
					});
					
					edtTermPrdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if(hasFocus){
								if(edtTermPrdt.getInt() == 0){
									edtTermPrdt.setText(Funcoes.formata(Geral.terminal, 2));
									edtTermPrdt.selectAll();
								}
							}
						}
					});
					
					edtTermPrdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
						
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
								if(edtTermPrdt.getInt() == 0){
									edtTermPrdt.setText(Funcoes.formata(Geral.terminal, 2));
								}else
									edtTermPrdt.setText(Funcoes.formata(edtTermPrdt.getInt(), 2));
							}
							return false;
						}
					});
					
					edtCdPrdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
						
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							try {
								if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
									if(edtTermPrdt.getInt() > 0 && edtCdPrdt.getInt() > 0){
										edtCdPrdt.formata(4);
										
										if(edtCdPrdt.getLong() != edtCdPrdt.getCdAnterior()){
											long cdPes = Funcoes.toLong(edtTermPrdt.getString() + edtCdPrdt.getString());
											
											edtNmPrdt.setText(Funcoes.vlCampoTblLocal("nm_fants_pes", "pub_pes", "tp_pes_prdt = 1 and cd_emp = " + Geral.cdEmpresa + " and cd_pes = " + cdPes).toString());
											if (edtNmPrdt.getString().equals("")){
												Funcoes.msgBox("Produtor não encontrado.", TabMovActivity.this);
												Funcoes.setaFoco(edtCdPrdt);
											}
										}
									}else
										edtNmPrdt.setText("");
								}
							} catch (Exception e) {
								Funcoes.msgBox("Falha ao buscar dados do produtor." + e.getMessage(), TabMovActivity.this);
							}
							return false;
						}
					});
					
					btnPesqPrdt.setOnClickListener(new View.OnClickListener() {
						
						ItemSpinner selecionado;
						
						@SuppressLint("InflateParams")
						@Override
						public void onClick(View v) {
							try{
								final View vPesq = ((LayoutInflater) TabMovActivity.this.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pesquisa, null, false);
								
								final Spinner spnFiltro = (Spinner) vPesq.findViewById(R.id.spnFiltro);
								final EditText edtPesq = (EditText) vPesq.findViewById(R.id.edtPesq);
								final ListView lstResult = (ListView) vPesq.findViewById(R.id.lstResult);
								
								ArrayList<ItemSpinner> lstFiltro = new ArrayList<ItemSpinner>();
								lstFiltro.add(new ItemSpinner("Nome fantasia", 0, "nm_fants_pes"));
								lstFiltro.add(new ItemSpinner("Razao Social", 1, "nm_razao_pes"));
								
								Funcoes.preencheSpinner(TabMovActivity.this, vPesq, R.id.spnFiltro, "Filtrar por", lstFiltro);
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
														   + " and pes.tp_pes_prdt = 1"
														   + " and pes.st_pes <> 2"
														   + " and "+ filtro +" like '%" + nome + "%'"
														   + " order by indice, " + filtro + "  limit 20";
												
												Cursor crsResult = Geral.localDB.rawQuery(sql, null);
												if(crsResult.getCount() > 0){
													Funcoes.preencheListViewLocal(TabMovActivity.this, vPesq, R.id.lstResult, crsResult, filtro, "cd_pes");
													crsResult.close();
												}
												
											} catch (Exception e) {
												Funcoes.msgBox("Falha ao realizar consulta." + e.getMessage(), TabMovActivity.this);
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
								
								new AlertDialog.Builder(TabMovActivity.this)
										.setTitle("Pesquisa de Produtores")
										.setView(vPesq)
										.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface arg0, int arg1) {
												if(selecionado != null){
													chkTdPrdt.setChecked(false);
													edtTermPrdt.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(0, 2));
													edtCdPrdt.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(2));
													edtCdPrdt.onEditorAction(EditorInfo.IME_ACTION_NEXT);
												}									
											}
										})
										.setNegativeButton("Cancelar", null)
										.show().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
								
								Funcoes.setaFoco(edtPesq);
								
							}catch(Exception e){
								Funcoes.msgBox("Falha ao pesquisar produtores." + e.getMessage(), TabMovActivity.this);
							}
							
						}
					});
					
					chkTdDest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if(isChecked){
								edtTermDest.setText("");
								edtTermDest.setEnabled(false);
								edtCdDest.setText("");
								edtCdDest.setEnabled(false);
								edtNmDest.setText("");
							}else{
								edtTermDest.setEnabled(true);
								edtCdDest.setEnabled(true);
								Funcoes.setaFoco(edtTermDest);
							}
						}
					});
					
					edtTermDest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if(hasFocus){
								if(edtTermDest.getInt() == 0){
									edtTermDest.setText(Funcoes.formata(Geral.terminal, 2));
									edtTermDest.selectAll();
								}
							}
						}
					});
					
					edtTermDest.setOnEditorActionListener(new TextView.OnEditorActionListener() {
						
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
								if(edtTermDest.getInt() == 0){
									edtTermDest.setText(Funcoes.formata(Geral.terminal, 2));
								}else
									edtTermDest.setText(Funcoes.formata(edtTermDest.getInt(), 2));
							}
							return false;
						}
					});
					
					edtCdDest.setOnEditorActionListener(new TextView.OnEditorActionListener() {
						
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							try {
								if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
									if(edtTermDest.getInt() > 0 && edtCdDest.getInt() > 0){
										edtCdDest.formata(4);
										
										if(edtCdDest.getLong() != edtCdDest.getCdAnterior()){
											long cdPes = Funcoes.toLong(edtTermDest.getString() + edtCdDest.getString());
											
											edtNmDest.setText(Funcoes.vlCampoTblLocal("nm_fants_pes", "pub_pes", "tp_pes_dest = 1 and cd_emp = " + Geral.cdEmpresa + " and cd_pes = " + cdPes).toString());
											if (edtNmDest.getString().equals("")){
												Funcoes.msgBox("Destinatário não encontrado.", TabMovActivity.this);
												Funcoes.setaFoco(edtCdDest);
											}
										}
									}else
										edtNmDest.setText("");
								}
							} catch (Exception e) {
								Funcoes.msgBox("Falha ao buscar dados do destinatário." + e.getMessage(), TabMovActivity.this);
							}
							return false;
						}
					});
					
					btnPesqDest.setOnClickListener(new View.OnClickListener() {
						
						ItemSpinner selecionado;
						
						@SuppressLint("InflateParams")
						@Override
						public void onClick(View v) {
							try{
								final View vPesq = ((LayoutInflater) TabMovActivity.this.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pesquisa, null, false);
								
								final Spinner spnFiltro = (Spinner) vPesq.findViewById(R.id.spnFiltro);
								final EditText edtPesq = (EditText) vPesq.findViewById(R.id.edtPesq);
								final ListView lstResult = (ListView) vPesq.findViewById(R.id.lstResult);
								
								ArrayList<ItemSpinner> lstFiltro = new ArrayList<ItemSpinner>();
								lstFiltro.add(new ItemSpinner("Nome fantasia", 0, "nm_fants_pes"));
								lstFiltro.add(new ItemSpinner("Razao Social", 1, "nm_razao_pes"));
								
								Funcoes.preencheSpinner(TabMovActivity.this, vPesq, R.id.spnFiltro, "Filtrar por", lstFiltro);
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
														   + " and pes.tp_pes_Dest = 1"
														   + " and pes.st_pes <> 2"
														   + " and "+ filtro +" like '%" + nome + "%'"
														   + " order by indice, " + filtro + "  limit 20";
												
												Cursor crsResult = Geral.localDB.rawQuery(sql, null);
												if(crsResult.getCount() > 0){
													Funcoes.preencheListViewLocal(TabMovActivity.this, vPesq, R.id.lstResult, crsResult, filtro, "cd_pes");
													crsResult.close();
												}
												
											} catch (Exception e) {
												Funcoes.msgBox("Falha ao realizar consulta." + e.getMessage(), TabMovActivity.this);
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
								
								new AlertDialog.Builder(TabMovActivity.this)
										.setTitle("Pesquisa de Destinatários")
										.setView(vPesq)
										.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface arg0, int arg1) {
												if(selecionado != null){
													chkTdDest.setChecked(false);
													edtTermDest.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(0, 2));
													edtCdDest.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(2));
													edtCdDest.onEditorAction(EditorInfo.IME_ACTION_NEXT);
												}									
											}
										})
										.setNegativeButton("Cancelar", null)
										.show().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
								
								Funcoes.setaFoco(edtPesq);
								
							}catch(Exception e){
								Funcoes.msgBox("Falha ao pesquisar destinatários." + e.getMessage(), TabMovActivity.this);
							}
							
						}
					});
					
					chkTdCarg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if(isChecked){
								edtTermCarg.setText("");
								edtTermCarg.setEnabled(false);
								edtCdCarg.setText("");
								edtCdCarg.setEnabled(false);
								edtNmCarg.setText("");
							}else{
								edtTermCarg.setEnabled(true);
								edtCdCarg.setEnabled(true);
								Funcoes.setaFoco(edtTermCarg);
							}
						}
					});
					
					edtTermCarg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if(hasFocus){
								if(edtTermCarg.getInt() == 0){
									edtTermCarg.setText(Funcoes.formata(Geral.terminal, 2));
									edtTermCarg.selectAll();
								}
							}
						}
					});
					
					edtTermCarg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
						
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
								if(edtTermCarg.getInt() == 0){
									edtTermCarg.setText(Funcoes.formata(Geral.terminal, 2));
								}else
									edtTermCarg.setText(Funcoes.formata(edtTermCarg.getInt(), 2));
							}
							return false;
						}
					});
					
					edtCdCarg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
						
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							try {
								if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
									if(edtTermCarg.getInt() > 0 && edtCdCarg.getInt() > 0){
										edtCdCarg.formata(4);
										
										if(edtCdCarg.getLong() != edtCdCarg.getCdAnterior()){
											long cdPes = Funcoes.toLong(edtTermCarg.getString() + edtCdCarg.getString());
											
											edtNmCarg.setText(Funcoes.vlCampoTblLocal("nm_fants_pes", "pub_pes", "tp_pes_carg = 1 and cd_emp = " + Geral.cdEmpresa + " and cd_pes = " + cdPes).toString());
											if (edtNmCarg.getString().equals("")){
												Funcoes.msgBox("Cargueiro não encontrado.", TabMovActivity.this);
												Funcoes.setaFoco(edtCdCarg);
											}
										}
									}else
										edtNmCarg.setText("");
								}
							} catch (Exception e) {
								Funcoes.msgBox("Falha ao buscar dados do cargueiro." + e.getMessage(), TabMovActivity.this);
							}
							return false;
						}
					});
					
					btnPesqCarg.setOnClickListener(new View.OnClickListener() {
						
						ItemSpinner selecionado;
						
						@SuppressLint("InflateParams")
						@Override
						public void onClick(View v) {
							try{
								final View vPesq = ((LayoutInflater) TabMovActivity.this.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pesquisa, null, false);
								
								final Spinner spnFiltro = (Spinner) vPesq.findViewById(R.id.spnFiltro);
								final EditText edtPesq = (EditText) vPesq.findViewById(R.id.edtPesq);
								final ListView lstResult = (ListView) vPesq.findViewById(R.id.lstResult);
								
								ArrayList<ItemSpinner> lstFiltro = new ArrayList<ItemSpinner>();
								lstFiltro.add(new ItemSpinner("Nome fantasia", 0, "nm_fants_pes"));
								lstFiltro.add(new ItemSpinner("Razao Social", 1, "nm_razao_pes"));
								
								Funcoes.preencheSpinner(TabMovActivity.this, vPesq, R.id.spnFiltro, "Filtrar por", lstFiltro);
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
														   + " and pes.tp_pes_Carg = 1"
														   + " and pes.st_pes <> 2"
														   + " and "+ filtro +" like '%" + nome + "%'"
														   + " order by indice, " + filtro + "  limit 20";
												
												Cursor crsResult = Geral.localDB.rawQuery(sql, null);
												if(crsResult.getCount() > 0){
													Funcoes.preencheListViewLocal(TabMovActivity.this, vPesq, R.id.lstResult, crsResult, filtro, "cd_pes");
													crsResult.close();
												}
												
											} catch (Exception e) {
												Funcoes.msgBox("Falha ao realizar consulta." + e.getMessage(), TabMovActivity.this);
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
								
								new AlertDialog.Builder(TabMovActivity.this)
										.setTitle("Pesquisa de Cargueiros")
										.setView(vPesq)
										.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface arg0, int arg1) {
												if(selecionado != null){
													chkTdCarg.setChecked(false);
													edtTermCarg.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(0, 2));
													edtCdCarg.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(2));
													edtCdCarg.onEditorAction(EditorInfo.IME_ACTION_NEXT);
												}									
											}
										})
										.setNegativeButton("Cancelar", null)
										.show().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
								
								Funcoes.setaFoco(edtPesq);
								
							}catch(Exception e){
								Funcoes.msgBox("Falha ao pesquisar cargueiros." + e.getMessage(), TabMovActivity.this);
							}
							
						}
					});
					
					AlertDialog dlgRelMov = new AlertDialog.Builder(TabMovActivity.this)
						.setView(vRelMov)
						.setTitle("Relatório de Movimentações")
						.setPositiveButton("Ok", null)
						.setNegativeButton("Fechar", null)
						.create();
					
					dlgRelMov.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					dlgRelMov.show();
					
					Button btnOk = dlgRelMov.getButton(AlertDialog.BUTTON_POSITIVE);
					btnOk.setOnClickListener(new View.OnClickListener() {
						
						private boolean validaDados() {
							try {
								if(Funcoes.toDate(edtDtFin.getString()).before(Funcoes.toDate(edtDtIni.getString()))){
									Funcoes.msgBox("Data final não pode ser inferior a data inicial.", TabMovActivity.this);
								}else if(!chkTdPlacas.isChecked() && !edtPlacaMotr.getString().trim().equals("") && edtPlacaMotr.getString().length() != 10){
									Funcoes.msgBox("Placa inválida.", TabMovActivity.this);
									Funcoes.setaFoco(edtPlaca);
								} else if(!chkTdPrdt.isChecked() && (edtTermPrdt.getInt() == 0 || edtCdPrdt.getInt() == 0 || edtNmPrdt.getString().equals(""))){
									Funcoes.msgBox("Informe um produtor.", TabMovActivity.this);
									Funcoes.setaFoco(edtTermPrdt);
								} else if(!chkTdDest.isChecked() && (edtTermDest.getInt() == 0 || edtCdDest.getInt() == 0 || edtNmDest.getString().equals(""))){
									Funcoes.msgBox("Informe um destinatário.", TabMovActivity.this);
									Funcoes.setaFoco(edtTermDest);
								} else if(!chkTdCarg.isChecked() && (edtTermCarg.getInt() == 0 || edtCdCarg.getInt() == 0 || edtNmCarg.getString().equals(""))){
									Funcoes.msgBox("Informe um cargueiro.", TabMovActivity.this);
									Funcoes.setaFoco(edtTermCarg);
								} else {
									return true;
								}
								
								return false;
								
							} catch (Exception e) {
								return false;
							}
						}
						
						@Override
						public void onClick(View v) {
							
							if(!validaDados())
								return;
							
							new AsyncTask<Void, Void, Boolean>(){
								ProgressDialog prgDialog = null;
								String erro = "";
								Cursor crsTmp = null;
								
								protected void onPreExecute() {
									prgDialog = ProgressDialog.show(TabMovActivity.this, "", "Buscando...");
								};
								
								@Override
								protected Boolean doInBackground(Void... params) {
									try {
										String sql = "select ent.*, coalesce(ent.dt_sai, ent.dt_ent) as dt_sai_nv, prod.ds_prod, prdt.nm_fants_pes as nm_prdt, carg.nm_fants_pes as nm_carg, dest.nm_fants_pes as nm_dest, cid.ds_cid, cid.uf_cid" +
												" from ctrl_ent ent inner join pub_pes prdt" + 
												" on ent.cd_emp = prdt.cd_emp" +
												" and ent.cd_pes_prdt = prdt.cd_pes" + 
												" inner join pub_pes dest" +
												" on ent.cd_emp = dest.cd_emp" +
												" and ent.cd_pes_dest = dest.cd_pes" +
												" inner join pub_pes carg" +
												" on ent.cd_emp = carg.cd_emp" +
												" and ent.cd_pes_carg = carg.cd_pes" +
												" inner join pub_prod prod" +
												" on ent.cd_emp = prod.cd_emp" +
												" and ent.cd_prod = prod.cd_prod" +
												" left join pub_cid cid" +
												" on ent.cd_cid_placa = cid.cd_cid" +
												" where ent.cd_emp = " + Geral.cdEmpresa + 
												" and ent.status_ent <> 2" +
												" and ent.dt_ent between " + Funcoes.retDataLocal(edtDtIni.getDate()) + " and " + Funcoes.retDataLocal(edtDtFin.getDate());
										
										switch (spnStEntSai.getSelectedItemPosition()) {
											case 1: sql += " and ent.st_ent = 0"; break;
											case 2: sql += " and ent.st_ent = 1"; break;
											case 3: sql += " and ent.st_sai = 0"; break;
											case 4: sql += " and ent.st_sai = 1"; break;
											default: break;
										}
										
										if(!chkTdPlacas.isChecked())
											sql += " and ent.placa_frete = '" + edtPlaca.getString() + "'";
										
										if(!chkTdPrdt.isChecked())
											sql += " and ent.cd_pes_prdt = " + edtTermPrdt.getString() + edtCdPrdt.getString();
										
										if(!chkTdDest.isChecked())
											sql += " and ent.cd_pes_dest = " + edtTermDest.getString() + edtCdDest.getString();
										
										if(!chkTdCarg.isChecked())
											sql += " and ent.cd_pes_carg = " + edtTermCarg.getString() + edtCdCarg.getString();
										
										if(!Geral.usuarioAdm)
											sql += " and ent.cd_term = " + Geral.terminal;
										
										sql += " order by ent.dt_ent";
												
										crsTmp = Geral.localDB.rawQuery(sql, null);
										
										if(crsTmp.getCount() > 0){
											lstRelMov = new ArrayList<Movimentacao>();
											
											crsTmp.moveToFirst();
											do{
												double vlPago = (crsTmp.getDouble(crsTmp.getColumnIndex("vl_pago")) > 0 ? crsTmp.getDouble(crsTmp.getColumnIndex("vl_pago")) : crsTmp.getDouble(crsTmp.getColumnIndex("vl_tot_prdt")));
												double vlRecebido = (crsTmp.getDouble(crsTmp.getColumnIndex("vl_recebido")) > 0 ? crsTmp.getDouble(crsTmp.getColumnIndex("vl_recebido")) : crsTmp.getDouble(crsTmp.getColumnIndex("vl_to_sai")));
												String cidUf = (crsTmp.getString(crsTmp.getColumnIndex("ds_cid")) != null ? crsTmp.getString(crsTmp.getColumnIndex("ds_cid")) + "-" + crsTmp.getString(crsTmp.getColumnIndex("uf_cid")) : "");
												
												Movimentacao mov = new Movimentacao(
														crsTmp.getLong(crsTmp.getColumnIndex("cd_ent")), 
														Funcoes.formataData(crsTmp.getString(crsTmp.getColumnIndex("dt_ent")), "dd/MM/yyyy"), 
														crsTmp.getString(crsTmp.getColumnIndex("nm_prdt")), 
														crsTmp.getString(crsTmp.getColumnIndex("nm_dest")),
														crsTmp.getString(crsTmp.getColumnIndex("placa_frete")),
														cidUf,
														crsTmp.getString(crsTmp.getColumnIndex("obs_ent")),
														crsTmp.getDouble(crsTmp.getColumnIndex("peso_bruto_prod")), 
														crsTmp.getDouble(crsTmp.getColumnIndex("peso_liq_prod")), 
														crsTmp.getInt(crsTmp.getColumnIndex("qtde_pecas")),
														crsTmp.getDouble(crsTmp.getColumnIndex("peso_bruto_prod")) / crsTmp.getInt(crsTmp.getColumnIndex("qtde_pecas")),
														crsTmp.getDouble(crsTmp.getColumnIndex("preco_comp")),
														crsTmp.getDouble(crsTmp.getColumnIndex("preco_vend")),
														crsTmp.getDouble(crsTmp.getColumnIndex("vl_mao_obra")),
														vlPago,
														crsTmp.getDouble(crsTmp.getColumnIndex("vl_icms_frete")),
														vlRecebido,
														(vlRecebido - vlPago)
													);
												
												lstRelMov.add(mov);
												
											}while(crsTmp.moveToNext());
											
											return true;
										}else{
											erro = "Nenhuma movimentação encontrada.";
											return false;
										}
										
									} catch (Exception e) {
										erro = "Falha ao gerar relatório." + e.getMessage();
										return false;
									}
								}
								
								protected void onPostExecute(Boolean result) {
									try {
										if(!result)
											throw new Exception(erro);
										
										Intent intRelMov = new Intent(TabMovActivity.this, RelMovEnt.class);
										intRelMov.putExtra("prcExib", spnPrcExib.getSelectedItemPosition());
										intRelMov.putExtra("nmEmp", Funcoes.vlCampoTblLocal("nm_emp", "pub_emp", "cd_emp = " + Geral.cdEmpresa).toString());
										
										TabMovActivity.this.startActivity(intRelMov);
										
									} catch (Exception e) {
										Funcoes.msgBox(e.getMessage(), TabMovActivity.this);
									} finally {
										if(prgDialog.isShowing())
											prgDialog.dismiss();
									}
								};
								
							}.execute();
						}
					});
				
				} catch (Exception e) {
					Funcoes.msgBox("Falha ao efetuar a operação." + e.getMessage(), TabMovActivity.this);
				}
				
				return false;
			}
		});
		
		/*
		MenuItem mnuSincronizar = menu.add("Sincronizar Banco de Dados");
		mnuSincronizar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@SuppressLint("InflateParams")
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				try {	
					final View vSincronizar = TabMovActivity.this.getLayoutInflater().inflate(R.layout.dialog_sincronizar, null);
					
					final CEditText edtQtEntEnv = (CEditText) vSincronizar.findViewById(R.id.edtQtEntEnv);
					final CEditText edtQtPesEnv = (CEditText) vSincronizar.findViewById(R.id.edtQtPesEnv);
					final CEditText edtQtEntBx = (CEditText) vSincronizar.findViewById(R.id.edtQtEntBx);
					final CEditText edtQtPesBx = (CEditText) vSincronizar.findViewById(R.id.edtQtPesBx);
					
					edtQtEntEnv.setText("000000");
					edtQtPesEnv.setText("000000");
					edtQtEntBx.setText("000000");
					edtQtPesBx.setText("000000");
					
					new AsyncTask<Void, Void, Boolean>(){
						ProgressDialog prgDialog = null;
						String erro = "";
						
						int qtEntEnv = 0;
						int qtPesEnv = 0;
						int qtEntBx = 0;
						int qtPesBx = 0;
						
						@Override
						protected void onPreExecute() {
							prgDialog = ProgressDialog.show(TabMovActivity.this, "", "Coletando informações...");
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
								edtQtEntBx.setText(Funcoes.formata(qtEntBx, 6));
								edtQtPesBx.setText(Funcoes.formata(qtPesBx, 6));
								
								final AlertDialog dlgSincronizar = new AlertDialog.Builder(TabMovActivity.this)
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
												if(edtQtEntBx.getInt() == 0)
													if(edtQtPesBx.getInt() == 0){
														Funcoes.msgBox("Não ha registros para serem sincronizados.", TabMovActivity.this);
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
																prgDialog = new ProgressDialog(TabMovActivity.this);
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
														prgDialog = ProgressDialog.show(TabMovActivity.this, "", "Processando...");
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
															
															Funcoes.msgBox("Banco de dados sincronizado com sucesso!", TabMovActivity.this);
															dlgSincronizar.dismiss();
															
														} catch (Exception e) {
															Funcoes.msgBox("Falha ao sincronizar banco de dados." + e.getMessage(), TabMovActivity.this);
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
										
										Funcoes.pergunta("Deseja iniciar a sincronização?", TabMovActivity.this, okListener, null);
									}
								});
								
							} catch (Exception e) {
								Funcoes.msgBox("Falha ao efetuar a operação." + e.getMessage(), TabMovActivity.this);
							} finally {
								if(prgDialog.isShowing())
									prgDialog.dismiss();
							}
						}
						
					}.execute();

				} catch (Exception e) {
					Funcoes.msgBox("Falha ao efetuar a operação." + e.getMessage(), TabMovActivity.this);
				}
				return false;
			}
		});
		*/
		
		MenuItem mnuSair = menu.add("Sair");
		mnuSair.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				TabMovActivity.this.onBackPressed();
				return false;
			}
		});
		
		MenuItem mnuCadPes = menu.add("Cadastro de Pessoas");
		mnuCadPes.setIcon(android.R.drawable.ic_menu_add);
		mnuCadPes.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		mnuCadPes.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intCadPes = new Intent(TabMovActivity.this, CadPesActivity.class);
				startActivity(intCadPes);
				return false;
			}
		});
		
		return true;
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if(menu != null){
			if(stRegistro == ST_REG_INCLUINDO){
				menu.getItem(2).setEnabled(false);
				menu.getItem(4).setEnabled(false);
			}else{
				menu.getItem(2).setEnabled(true);
				menu.getItem(4).setEnabled(true);
			}
		}
		return super.onMenuOpened(featureId, menu);
	}
	
	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onBackPressed() {
		Funcoes.pergunta("Deseja realmente voltar para o menu principal?", TabMovActivity.this, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		}, null);
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
	
	private void limpaTela(){
		
		mViewPager.setCurrentItem(0);
		
		edtTermEnt.setEnabled(Geral.usuarioAdm);
		edtTermEnt.setText(Funcoes.formata(Geral.terminal, 2));
		edtCdEnt.setText(Funcoes.formata(0, 4));
		edtDtEnt.setText(Funcoes.formataData(new Date(), "dd/MM/yyyy"));
		edtTermPrdr.setText(Funcoes.formata(Geral.terminal, 2));
		edtCdPrdr.setText(Funcoes.formata(0, 4));
		edtNmPrdr.setText("");
		edtCdProd.setText(Funcoes.formata(1, 4));
		edtDsProd.setText("MELANCIA");
		edtPesoBruto.setText(Funcoes.decimal(0, 2));
		edtPercPalha.setText(Funcoes.decimal(2, 2));
		edtDescarte.setText(Funcoes.decimal(0, 2));
		edtPesoLiq.setText(Funcoes.decimal(0, 2));
		edtQtdePecas.setText(Funcoes.formata(0, 6));
		edtPesoMedio.setText(Funcoes.decimal(0, 3));
		edtPrecoEnt.setText(Funcoes.decimal(0, 2));
		edtTermCarg.setText("");
		edtCdCarg.setText("");
		edtNmCarg.setText("");
		edtVlMaoObra.setText(Funcoes.decimal(0, 2));
		edtVlDespPrdr.setText(Funcoes.decimal(0, 2));
		edtVlTotPrdr.setText(Funcoes.decimal(0, 2));
		edtObsEnt.setText("");
		spnStEnt.setSelection(1);
		edtVlPgEnt.setText(Funcoes.decimal(0, 2));
		
		edtTermDest.setText(Funcoes.formata(Geral.terminal, 2));
		edtCdDest.setText(Funcoes.formata(0, 4));
		edtNmDest.setText("");
		edtDtSai.setText(Funcoes.formataData(new Date(), "dd/MM/yyyy"));
		edtNmMotr.setText("");
		edtFoneMotr.setText("");
		edtCPFCGCMotr.setText("");
		edtPlacaMotr.setText("");
		edtVlFrete.setText(Funcoes.decimal(0, 2));
		edtVlICMSFrete.setText(Funcoes.decimal(0, 2));
		edtVlTotFrete.setText(Funcoes.decimal(0, 2));
		edtPrecoSai.setText(Funcoes.decimal(0, 2));
		edtVlTotSai.setText(Funcoes.decimal(0, 2));
		edtObsSai.setText("");
		spnStSai.setSelection(1);
		edtVlRecSai.setText(Funcoes.decimal(0, 2));
		spnTpFreteSai.setSelection(0);
		spnTpICMS.setSelection(1);
		chkNotaFiscal.setChecked(true);
		
		btnRegAnt.setEnabled(true);
		btnProxReg.setEnabled(true);
		
		Funcoes.selItemSpinner(TabMovActivity.this, vMovSai, R.id.spnUfPlaca, "--", true);
	}

	private void novoRegistro(){
		limpaTela();
		mViewPager.setCurrentItem(0);
		
		try{
			String criterio = "cd_emp = " + Geral.cdEmpresa + " and cd_ent between " + Geral.terminal + "0000 and " + Geral.terminal + "9999";
			long cdEnt = Funcoes.proximoNumeroLocal("cd_ent", "ctrl_ent", criterio);
			if(cdEnt == 1)
				cdEnt = Funcoes.toInt(Geral.terminal + "0001");
			
			edtCdEnt.setText(Funcoes.formata(cdEnt, 6).substring(2));
			edtCdEnt.setCdAnterior(edtCdEnt.getLong());
			
			stRegistro = ST_REG_INCLUINDO;
			edtObsSai.setText(Geral.config.getObsPadrao());
			
			Funcoes.setaFoco(edtCdEnt);
			edtCdEnt.selectAll();
		} catch(Exception e){
			Funcoes.msgBox("Falha ao preparar tela para movimentação." + e.getMessage(), TabMovActivity.this);
			Funcoes.setaFoco(edtTermEnt);
		}
	}

	private void mostraDados(final long cdEnt){
		try {
			
			new AsyncTask<Void, Void, Boolean>() {
				
				ProgressDialog prgDialog;
				String erro = "";
				Cursor rsTmp;
				boolean firstReg = false;
				boolean lastReg = false;
				
				protected void onPreExecute() {
					prgDialog = ProgressDialog.show(TabMovActivity.this, "", "Carregando...");
				};
				
				@Override
				protected Boolean doInBackground(Void... params) {
					try{
						String sql = "select ent.*, coalesce(ent.dt_sai, ent.dt_ent) as dt_sai_nv, prod.ds_prod, prdt.nm_fants_pes as nm_prdt," +
								" coalesce(carg.nm_fants_pes, '') as nm_carg, dest.nm_fants_pes as nm_dest, cid.ds_cid, cid.uf_cid" +
								" from ctrl_ent ent inner join pub_pes prdt" + 
								" on ent.cd_emp = prdt.cd_emp" +
								" and ent.cd_pes_prdt = prdt.cd_pes" + 
								" inner join pub_pes dest" +
								" on ent.cd_emp = dest.cd_emp" +
								" and ent.cd_pes_dest = dest.cd_pes" +
								" left join pub_pes carg" +
								" on ent.cd_emp = carg.cd_emp" +
								" and ent.cd_pes_carg = carg.cd_pes" +
								" inner join pub_prod prod" +
								" on ent.cd_prod = prod.cd_prod" +
								" left join pub_cid cid" +
								" on ent.cd_cid_placa = cid.cd_cid" +
								" where ent.cd_emp = " + Geral.cdEmpresa + 
								" and cd_ent = " + cdEnt;
						
						rsTmp = Geral.localDB.rawQuery(sql, null);
						
						if(rsTmp != null){
							String criterio = "cd_emp = " + Geral.cdEmpresa +
									" and cd_ent between " + edtTermEnt.getText().toString() + "0000 and " + edtTermEnt.getText().toString() + "9999" +
									" and cd_ent > " + cdEnt +
									" and status_ent <> 2" +
									" limit 1";
							
							if(Funcoes.toLong(Funcoes.vlCampoTblLocal("cd_ent", "ctrl_ent", criterio)) == 0){
								lastReg = true;
							}
							
							criterio = "cd_emp = " + Geral.cdEmpresa +
									" and cd_ent between " + edtTermEnt.getText().toString() + "0000 and " + edtTermEnt.getText().toString() + "9999" +
									" and cd_ent < " + cdEnt +
									" and status_ent <> 2" +
									" limit 1";									
							
							if(Funcoes.toLong(Funcoes.vlCampoTblLocal("cd_ent", "ctrl_ent", criterio)) == 0){
								firstReg = true;
							}
							
							return true;
						}else{
							throw new RuntimeException("Registro não encontrado.");
						}
					}catch(Exception e){
						erro = e.getMessage();
						return false;
					}
				}
				
				protected void onPostExecute(Boolean result) {
					try {
						if(!result || rsTmp == null)
							throw new RuntimeException(erro);
						
						rsTmp.moveToFirst();
						
						if(rsTmp.getInt(rsTmp.getColumnIndex("status_ent")) == 2){
							Funcoes.msgBox("Registro cancelado.", TabMovActivity.this);
							novoRegistro();
							return;
						}
							
						limpaTela();
						edtTermEnt.setText(Funcoes.formata(cdEnt, 6).substring(0, 2));
						edtCdEnt.setText(Funcoes.formata(cdEnt, 6).substring(2));
						edtCdEnt.setCdAnterior(edtCdEnt.getLong());
						
						edtDtEnt.setText(Funcoes.formataData(rsTmp.getString(rsTmp.getColumnIndex("dt_ent")), "dd/MM/yyyy"));
						edtTermPrdr.setText(Funcoes.formata(rsTmp.getInt(rsTmp.getColumnIndex("cd_pes_prdt")), 6).substring(0, 2));
						edtCdPrdr.setText(Funcoes.formata(rsTmp.getInt(rsTmp.getColumnIndex("cd_pes_prdt")), 6).substring(2));
						edtNmPrdr.setText(rsTmp.getString(rsTmp.getColumnIndex("nm_prdt")));
						edtCdProd.setText(Funcoes.formata(rsTmp.getInt(rsTmp.getColumnIndex("cd_prod")), 4));
						edtDsProd.setText(rsTmp.getString(rsTmp.getColumnIndex("ds_prod")));
						edtPesoBruto.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("peso_bruto_prod")), 2));
						edtPercPalha.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("perc_palha")), 2));
						edtDescarte.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("peso_descarte")), 2));
						edtPesoLiq.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("peso_liq_prod")), 2));
						edtQtdePecas.setText(Funcoes.formata(rsTmp.getInt(rsTmp.getColumnIndex("qtde_pecas")), 6));
						edtPesoMedio.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("peso_bruto_prod")) / rsTmp.getInt(rsTmp.getColumnIndex("qtde_pecas")), 3));
						edtPrecoEnt.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("preco_comp")), 4));
						
						if (rsTmp.getInt(rsTmp.getColumnIndex("cd_pes_carg")) > 0){
							edtTermCarg.setText(Funcoes.formata(rsTmp.getInt(rsTmp.getColumnIndex("cd_pes_carg")), 6).substring(0, 2));
							edtCdCarg.setText(Funcoes.formata(rsTmp.getInt(rsTmp.getColumnIndex("cd_pes_carg")), 6).substring(2));
							edtNmCarg.setText(rsTmp.getString(rsTmp.getColumnIndex("nm_carg")));
						}
						
						edtVlMaoObra.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("vl_mao_obra")), 2));
						edtVlDespPrdr.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("vl_out_desp")), 2));
						edtVlTotPrdr.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("vl_tot_prdt")), 2));
						edtObsEnt.setText(rsTmp.getString(rsTmp.getColumnIndex("obs_ent")));
						spnStEnt.setSelection(rsTmp.getInt(rsTmp.getColumnIndex("st_ent")));
						edtVlPgEnt.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("vl_pago")), 2));
						
						edtTermDest.setText(Funcoes.formata(rsTmp.getInt(rsTmp.getColumnIndex("cd_pes_dest")), 6).substring(0, 2));
						edtCdDest.setText(Funcoes.formata(rsTmp.getInt(rsTmp.getColumnIndex("cd_pes_dest")), 6).substring(2));
						edtNmDest.setText(rsTmp.getString(rsTmp.getColumnIndex("nm_dest")));
						
						if(rsTmp.getString(rsTmp.getColumnIndex("dt_sai_nv")) != null){
							edtDtSai.setText(Funcoes.formataData(rsTmp.getString(rsTmp.getColumnIndex("dt_sai_nv")), "dd/MM/yyyy"));
						}
						
						edtNmMotr.setText(rsTmp.getString(rsTmp.getColumnIndex("nm_motr")));
						if(!Funcoes.separaNumeros(rsTmp.getString(rsTmp.getColumnIndex("fone_motr"))).equals("")){
							edtFoneMotr.setText(rsTmp.getString(rsTmp.getColumnIndex("fone_motr")));
						}
						
						if(rsTmp.getString(rsTmp.getColumnIndex("placa_frete")) != null){
							if(!rsTmp.getString(rsTmp.getColumnIndex("placa_frete")).equals("    -     "))
								edtPlacaMotr.setText(rsTmp.getString(rsTmp.getColumnIndex("placa_frete")));
						}
						
						String uf = getResources().getStringArray(R.array.uf_cid)[rsTmp.getInt(rsTmp.getColumnIndex("uf_placa_frete")) + 1];
						Funcoes.selItemSpinner(TabMovActivity.this, vMovSai, R.id.spnUfPlaca, uf, true);
						Funcoes.selItemSpinner(TabMovActivity.this, vMovSai, R.id.spnCidPlaca, rsTmp.getInt(rsTmp.getColumnIndex("cd_cid_placa")), true);
						
						edtCPFCGCMotr.setText(rsTmp.getString(rsTmp.getColumnIndex("cpf_cgc_motr")));
						edtVlFrete.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("vl_frete_ton")), 2));
						edtVlICMSFrete.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("vl_icms_frete")), 2));
						edtVlTotFrete.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("vl_to_frete")), 2));
						edtPrecoSai.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("preco_vend")), 2));
						edtVlTotSai.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("vl_to_sai")), 2));
						edtObsSai.setText(rsTmp.getString(rsTmp.getColumnIndex("obs_sai")));
						spnStSai.setSelection(rsTmp.getInt(rsTmp.getColumnIndex("st_sai")));
						edtVlRecSai.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("vl_recebido")), 2));
						spnTpFreteSai.setSelection(rsTmp.getInt(rsTmp.getColumnIndex("tp_frete_sai")));
						spnTpICMS.setSelection(rsTmp.getInt(rsTmp.getColumnIndex("tp_icms_frete")));
						chkNotaFiscal.setChecked((rsTmp.getInt(rsTmp.getColumnIndex("emitir_nf")) == 1? true : false));
						rsTmp.close();
						
						btnRegAnt.setEnabled(!firstReg);
						btnProxReg.setEnabled(!lastReg);

						Funcoes.setaFoco(edtCdEnt);
						stRegistro  = ST_REG_EDITANDO;
						
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao exibir registro." + e.getMessage(), TabMovActivity.this);
						novoRegistro();
					} finally {
						if(prgDialog.isShowing())
							prgDialog.dismiss();
					}
				};
				
			}.execute();
			
		} catch (Exception e) {
			Funcoes.msgBox("Falha ao exibir registro." + e.getMessage(), TabMovActivity.this);
			novoRegistro();
		}
	}
	
	private void totalizaEntrada(){
		try {
			if(edtPesoBruto.getDouble() > 0){
				double pesoLiq = edtPesoBruto.getDouble()  - edtDescarte.getDouble();
				pesoLiq = pesoLiq - (pesoLiq * edtPercPalha.getDouble() / 100);
				
				edtPesoLiq.setText(Funcoes.decimal(pesoLiq, 2));
				
				if(edtQtdePecas.getInt() > 0){
					edtPesoMedio.setText(Funcoes.decimal((edtPesoBruto.getDouble()- (edtPesoBruto.getDouble() * edtPercPalha.getDouble() / 100)) / edtQtdePecas.getDouble(), 3));
				}else{
					edtPesoMedio.setText("0,00");
				}
				
				edtVlTotFrete.setText(Funcoes.decimal(edtVlFrete.getDouble() * (edtPesoBruto.getDouble() / 1000) + edtVlICMSFrete.getDouble(), 2));
				edtVlTotPrdr.setText(Funcoes.decimal(pesoLiq * edtPrecoEnt.getDouble() - edtVlMaoObra.getDouble() - edtVlDespPrdr.getDouble(), 2));
				
				double vlSai = pesoLiq * edtPrecoSai.getDouble();
				if(spnTpFreteSai.getSelectedItemPosition() == 0){
					vlSai -= edtVlFrete.getDouble() * (edtPesoBruto.getDouble() / 1000);
				}
				
				if(spnTpICMS.getSelectedItemPosition() == 0){
					vlSai -= edtVlICMSFrete.getDouble();
				}
				
				edtVlTotSai.setText(Funcoes.decimal(vlSai, 2));
			}else{
				edtPesoLiq.setText("0,00");
				edtPesoMedio.setText("0,000");
				edtVlTotFrete.setText("0,00");
				edtVlTotPrdr.setText("0,00");
				edtVlTotSai.setText("0,00");
			}
			
		} catch (Exception e) {
			Funcoes.msgBox("Falha ao totalizar movimentação." + e.getMessage(), TabMovActivity.this);
		}
	}

	private boolean validaEntrada(){
		try {
			if(edtTermPrdr.getInt() == 0 || edtCdPrdr.getInt() == 0 || edtNmPrdr.getString().trim().length() == 0){
				Funcoes.msgBox("Informe o produtor.", TabMovActivity.this);
				Funcoes.setaFoco(edtTermPrdr);
				mViewPager.setCurrentItem(0);
			}else if(edtCdProd.getInt() == 0 || edtDsProd.getString().trim().length() == 0){
				Funcoes.msgBox("Informe o produto.", TabMovActivity.this);
				Funcoes.setaFoco(edtCdProd);
				mViewPager.setCurrentItem(0);
			}else if(edtPesoBruto.getDouble() == 0){
				Funcoes.msgBox("Informe o peso bruto da carga.", TabMovActivity.this);
				Funcoes.setaFoco(edtPesoBruto);
				mViewPager.setCurrentItem(0);
			}else if(edtQtdePecas.getInt() == 0){
				Funcoes.msgBox("Informe a quantidade de peças.", TabMovActivity.this);
				Funcoes.setaFoco(edtQtdePecas);
				mViewPager.setCurrentItem(0);
			}else if(edtPrecoEnt.getDouble() == 0){
				Funcoes.msgBox("Informe o preço de compra da carga.", TabMovActivity.this);
				Funcoes.setaFoco(edtPrecoEnt);
				mViewPager.setCurrentItem(0);
			//}else if(edtTermCarg.getInt() == 0 || edtCdCarg.getInt() == 0 || edtNmCarg.getString().trim().length() == 0){
			//	Funcoes.msgBox("Informe o cargueiro.", TabMovActivity.this);
			//	Funcoes.setaFoco(edtTermCarg);
			//	mViewPager.setCurrentItem(0);
			}else if(edtTermDest.getInt() == 0 || edtCdDest.getInt() == 0 || edtNmDest.getString().trim().length() == 0){
				Funcoes.msgBox("Informe o destinatário da carga.", TabMovActivity.this);
				Funcoes.setaFoco(edtTermDest);
				mViewPager.setCurrentItem(1);
			}else if(!edtPlacaMotr.getString().trim().equals("") && edtPlacaMotr.getString().length() != 10){
				Funcoes.msgBox("Placa inválida.", TabMovActivity.this);
				Funcoes.setaFoco(edtPlacaMotr);
				mViewPager.setCurrentItem(1);
			}else{
				return true;
			}
			
			return false;
			
		} catch (Exception e) {
			Funcoes.msgBox("Falha ao validadar entrada." + e.getMessage(), TabMovActivity.this);
			return false;
		}
	}
	
	private void incluiRegistro(){
		try {
			Funcoes.pergunta("Deseja gravar o registro atual?", TabMovActivity.this, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						new AsyncTask<Void, Void, Boolean>(){
							ProgressDialog prgDialog;
							String erro = "";
							
							@Override
							protected void onPreExecute() {
								prgDialog = ProgressDialog.show(TabMovActivity.this, "", "Processando...");
							};
							
							@Override
							protected Boolean doInBackground(Void... params) {
								try{
									
									String criterio = "cd_emp = " + Geral.cdEmpresa + " and cd_ent between " + Geral.terminal + "0000 and " + Geral.terminal + "9999";
									long cdEnt = Funcoes.proximoNumeroLocal("cd_ent", "ctrl_ent", criterio);
									if(cdEnt == 1)
										cdEnt = Funcoes.toInt(Geral.terminal + "0001");
									
									String cdCid;
									
									if(spnCidPlaca.getSelectedItem() != null){
										cdCid = String.valueOf(((ItemSpinner) spnCidPlaca.getSelectedItem()).getCodigo());
									}else{
										cdCid = "null";
									}
									
									String placaFrete;
									if(edtPlacaMotr.getString().trim().equals("")){
										placaFrete = "    -     ";
									}else{
										placaFrete = edtPlacaMotr.getString();
									}	
									
									String cdCarg = "null";
									if(Funcoes.toInt(edtTermCarg.getString()) > 0 && Funcoes.toInt(edtCdCarg.getString()) > 0 && !edtNmCarg.getText().equals(""))
										cdCarg = Funcoes.toInt(edtTermCarg.getString() + edtCdCarg.getString()).toString();
									
									String[] vetCampos = new String[]{"CD_EMP", "CD_ENT", "CD_PES_PRDT", "DT_ENT", "CD_PROD",
					                        "PESO_BRUTO_PROD", "PERC_PALHA", "PESO_DESCARTE", "PRECO_COMP", "PRECO_VEND",
					                        "PLACA_FRETE", "UF_PLACA_FRETE", "CD_CID_PLACA", "VL_FRETE_TON", "VL_ICMS_FRETE", "VL_MAO_OBRA",
					                        "VL_OUT_DESP", "CD_PES_DEST", "OBS_ENT", "CD_TERM", "QTDE_PECAS",
					                        "ST_ENT", "ST_REG", "VL_TOT_PRDT", "VL_CARG", "VER_REG", "PESO_LIQ_PROD",
					                        "VL_PAGO", "VL_RECEBIDO", "NM_MOTR", "FONE_MOTR", "CPF_CGC_MOTR", "VL_TO_FRETE", "VL_TO_SAI",
					                        "DT_SAI", "ST_SAI", "CD_PES_CARG", "OBS_SAI", "CD_USU", "CD_VER", "CD_TERM", "DT_EMISSAO", "TP_FRETE_SAI", "TP_ICMS_FRETE", "EMITIR_NF"};
									
									Object[] vetValores = new Object[]{Geral.cdEmpresa, cdEnt, Funcoes.toInt(edtTermPrdr.getString() + edtCdPrdr.getString()), Funcoes.retDataLocal(edtDtEnt.getString()), edtCdProd.getInt(),
											edtPesoBruto.getDouble(), edtPercPalha.getDouble(), edtDescarte.getDouble(), edtPrecoEnt.getDouble(), edtPrecoSai.getDouble(),
											placaFrete, spnUfPlaca.getSelectedItemPosition() - 1, cdCid, edtVlFrete.getDouble(), edtVlICMSFrete.getDouble(), edtVlMaoObra.getDouble(),
											edtVlDespPrdr.getDouble(), Funcoes.toInt(edtTermDest.getString() + edtCdDest.getString()), edtObsEnt.getString(), Geral.terminal, edtQtdePecas.getDouble(),
											spnStEnt.getSelectedItemPosition(), 0, edtVlTotPrdr.getDouble(), 0, 0, edtPesoLiq.getDouble(),
											edtVlPgEnt.getDouble(), edtVlRecSai.getDouble(), edtNmMotr.getString(), edtFoneMotr.getString(), edtCPFCGCMotr.getString(), edtVlTotFrete.getDouble(), edtVlTotSai.getDouble(),
											Funcoes.retDataLocal(edtDtSai.getString()), spnStSai.getSelectedItemPosition(), cdCarg, edtObsSai.getString(), Geral.cdUsuario, 1, Geral.terminal, Funcoes.retDataLocal(new Date()), spnTpFreteSai.getSelectedItemPosition(), spnTpICMS.getSelectedItemPosition(), (chkNotaFiscal.isChecked()? 1 : 0)};
									
									Geral.localDB.execSQL(Funcoes.criaSQLInsert("ctrl_ent", vetCampos, vetValores));
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

									Funcoes.msgBox("Registro incluído com sucesso!", TabMovActivity.this);
									novoRegistro();
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao gravar registro." + e.getMessage(), TabMovActivity.this);
								} finally{
									if(prgDialog.isShowing())
										prgDialog.dismiss();
								}
							};
							
						}.execute();
						
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao gravar registro." + e.getMessage(), TabMovActivity.this);
					}
				}
			}, null);
			
		} catch (Exception e) {
			Funcoes.msgBox("Ocorreu um erro ao efetuar a operação." + e.getMessage(), TabMovActivity.this);
		}
	}
	
	private void alteraRegistro(){
		try {
			Funcoes.pergunta("Deseja gravar as alterações do registro atual?", TabMovActivity.this, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						new AsyncTask<Void, Void, Boolean>(){
							ProgressDialog prgDialog;
							String erro = "";
							
							@Override
							protected void onPreExecute() {
								prgDialog = ProgressDialog.show(TabMovActivity.this, "", "Processando...");
							};
							
							@Override
							protected Boolean doInBackground(Void... params) {
								try{
									
									String criterio = "cd_emp = " + Geral.cdEmpresa
													+ " and cd_ent = " + edtTermEnt.getString() + edtCdEnt.getString();
									
									long verReg = Funcoes.proximoNumeroLocal("ver_reg", "ctrl_ent", criterio);
									
									String cdCid;
									if(spnCidPlaca.getSelectedItem() != null){
										cdCid = String.valueOf(((ItemSpinner) spnCidPlaca.getSelectedItem()).getCodigo());
									}else{
										cdCid = "null";
									}
									
									String placaFrete;
									if(edtPlacaMotr.getString().trim().equals("")){
										placaFrete = "    -     ";
									}else{
										placaFrete = edtPlacaMotr.getString();
									}
									
									String cdCarg = "null";
									if(Funcoes.toInt(edtTermCarg.getString()) > 0 && Funcoes.toInt(edtCdCarg.getString()) > 0 && !edtNmCarg.getText().equals(""))
										cdCarg = Funcoes.toInt(edtTermCarg.getString() + edtCdCarg.getString()).toString();
											
									String[] vetCampos = new String[]{"CD_PES_PRDT", "DT_ENT", "CD_PROD",
					                        "PESO_BRUTO_PROD", "PERC_PALHA", "PESO_DESCARTE", "PRECO_COMP", "PRECO_VEND",
					                        "PLACA_FRETE", "UF_PLACA_FRETE", "CD_CID_PLACA", "VL_FRETE_TON", "VL_ICMS_FRETE", "VL_MAO_OBRA",
					                        "VL_OUT_DESP", "CD_PES_DEST", "OBS_ENT", "CD_TERM", "QTDE_PECAS",
					                        "ST_ENT", "ST_REG", "VL_TOT_PRDT", "VL_CARG", "VER_REG", "PESO_LIQ_PROD",
					                        "VL_PAGO", "VL_RECEBIDO", "NM_MOTR", "FONE_MOTR", "CPF_CGC_MOTR", "VL_TO_FRETE", "VL_TO_SAI",
					                        "DT_SAI", "ST_SAI", "CD_PES_CARG", "OBS_SAI", "CD_USU", "CD_VER", "TP_FRETE_SAI", "TP_ICMS_FRETE", "EMITIR_NF"};
									
									Object[] vetValores = new Object[]{Funcoes.toInt(edtTermPrdr.getString() + edtCdPrdr.getString()), Funcoes.retDataLocal(edtDtEnt.getString()), edtCdProd.getInt(),
											edtPesoBruto.getDouble(), edtPercPalha.getDouble(), edtDescarte.getDouble(), edtPrecoEnt.getDouble(), edtPrecoSai.getDouble(),
											placaFrete, spnUfPlaca.getSelectedItemPosition() - 1, cdCid, edtVlFrete.getDouble(), edtVlICMSFrete.getDouble(), edtVlMaoObra.getDouble(),
											edtVlDespPrdr.getDouble(), Funcoes.toInt(edtTermDest.getString() + edtCdDest.getString()), edtObsEnt.getString(), Geral.terminal, edtQtdePecas.getDouble(),
											spnStEnt.getSelectedItemPosition(), 0, edtVlTotPrdr.getDouble(), 0, 0, edtPesoLiq.getDouble(),
											edtVlPgEnt.getDouble(), edtVlRecSai.getDouble(), edtNmMotr.getString(), edtFoneMotr.getString(), edtCPFCGCMotr.getString(), edtVlTotFrete.getDouble(), edtVlTotSai.getDouble(),
											Funcoes.retDataLocal(edtDtSai.getString()), spnStSai.getSelectedItemPosition(), cdCarg, edtObsSai.getString(), Geral.cdUsuario, verReg, spnTpFreteSai.getSelectedItemPosition(), spnTpICMS.getSelectedItemPosition(), (chkNotaFiscal.isChecked()? 1 : 0)};
									
									Geral.localDB.execSQL(Funcoes.criaSQLUpdate("ctrl_ent", vetCampos, vetValores, criterio));
									
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
									
									Funcoes.msgBox("Registro alterado com sucesso!", TabMovActivity.this);
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao alterar registro." + e.getMessage(), TabMovActivity.this);
								} finally{
									if(prgDialog.isShowing())
										prgDialog.dismiss();
								}
							};
							
						}.execute();
						
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao alterar registro." + e.getMessage(), TabMovActivity.this);
					}
				}
			}, null);
			
		} catch (Exception e) {
			Funcoes.msgBox("Ocorreu um erro ao efetuar a operação." + e.getMessage(), TabMovActivity.this);
		}	
	}
	
	private void excluiRegistro(){
		try {
			Funcoes.pergunta("Deseja gravar o registro atual?", TabMovActivity.this, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						new AsyncTask<Void, Void, Boolean>(){
							ProgressDialog prgDialog;
							String erro = "";
							
							@Override
							protected void onPreExecute() {
								prgDialog = ProgressDialog.show(TabMovActivity.this, "", "Processando...");
							};
							
							@Override
							protected Boolean doInBackground(Void... params) {
								try{
									
									String criterio = "cd_emp = " + Geral.cdEmpresa
													+ " and cd_ent = " + edtTermEnt.getString() + edtCdEnt.getString();
									
									long verReg = Funcoes.proximoNumeroLocal("ver_reg", "ctrl_ent", criterio);
									
									String[] vetCampos = new String[]{"ST_REG", "STATUS_ENT", "VER_REG"};
									Object[] vetValores = new Object[]{0, 2, verReg};
									
									Geral.localDB.execSQL(Funcoes.criaSQLUpdate("ctrl_ent", vetCampos, vetValores, criterio));
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
									
									Funcoes.msgBox("Registro excluído com sucesso!", TabMovActivity.this);
									novoRegistro();
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao excluir registro." + e.getMessage(), TabMovActivity.this);
								} finally{
									if(prgDialog.isShowing())
										prgDialog.dismiss();
								}
							};
							
						}.execute();
						
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao excluir registro." + e.getMessage(), TabMovActivity.this);
					}
				}
			}, null);
			
		} catch (Exception e) {
			Funcoes.msgBox("Ocorreu um erro ao efetuar a operação." + e.getMessage(), TabMovActivity.this);
		}
	}
	
}
