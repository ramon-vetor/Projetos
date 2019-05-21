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
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MovBanActivity extends Activity {
	

	private static int ST_REG_INCLUINDO = 1;
	private static int ST_REG_EDITANDO = 2;
	//private static int ST_REG_VISUALIZANDO = 3;
	
	private int stRegistro;
	
	static CEditText edtTermLanc;
	static CEditText edtCdLanc;
	static CEditText edtDtEmis;
	static ImageButton btnRegAnt;
	static ImageButton btnProxReg;
	static CEditText edtTermPes;
	static CEditText edtCdPes;
	static CEditText edtNmPes;
	static ImageButton btnPesqPes;
	static Spinner spnTpLanc;
	static CEditText edtVlLanc;
	static CEditText edtObsLanc;
	static CEditText edtKmInicial;
	static CEditText edtKmFinal;
	static CEditText edtQtdeLitros;
	static CEditText edtConsumo;
	static LinearLayout lnlDadosAbastec;
	
	class Movimentacao extends HashMap<String, String>{
		private static final long serialVersionUID = 1L;
		
		public Movimentacao(
				long cdLanc,
				String dtLanc,
				int tpLanc,
				int cdPes,
				String nmPes,
				double vlLanc,
				String obsLanc,
				double kmIni,
				double kmFin,
				double qtLitros
				){
			
			this.put("cdLanc", Funcoes.formata(cdLanc, 6));
			this.put("dtLanc", dtLanc);
			this.put("tpLanc", (tpLanc == 0 ? "Despesa" : "Receita"));
			this.put("cdPes", Funcoes.formata(cdLanc, cdPes));
			this.put("nmPes", nmPes);
			this.put("vlLanc", Funcoes.decimal(vlLanc, 2));
			this.put("obsLanc", obsLanc);
			this.put("kmIni", String.valueOf((int) kmIni));
			this.put("kmFin", String.valueOf((int) kmFin));
			this.put("qtLitros", Funcoes.decimal(qtLitros, 2));
			this.put("consumo", calculaConsumo(kmIni, kmFin, qtLitros));
			
		}
		
		private String calculaConsumo(double kmIni, double kmFin, double qtLitros) {
			if(kmIni > 0 && kmFin > 0 && qtLitros > 0 && kmFin > kmIni)
				return Funcoes.decimal((kmFin - kmIni) / qtLitros, 2);
			else
				return "0,00";
		}
	}
	
	public static ArrayList<Movimentacao> lstRelMov = null;
	
	public static class RelMovBan extends Activity{
		public RelativeLayout rlRelBan;
		
		@Override
		protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.rel_mov_ban);
			
			try {
				
				ArrayList<Movimentacao> lstMov = MovBanActivity.lstRelMov;

				if(lstMov == null || lstMov.size() == 0) {
					Funcoes.msgBox("Dados do relatório não encontrados.", RelMovBan.this);
				}
				
				findViewById(R.id.lnlPageNumber).setVisibility(View.GONE);
				
				TextView txvTotDesp = (TextView) findViewById(R.id.txvTotDesp);
				TextView txvTotRec = (TextView) findViewById(R.id.txvTotRec);
				
				rlRelBan = (RelativeLayout) findViewById(R.id.RelativeLayout1);
				final ListView relMov = (ListView) findViewById(R.id.lstMov);
				
				TextView txvNmEmp = (TextView) findViewById(R.id.txvNmEmp);
				txvNmEmp.setText(getIntent().getStringExtra("nmEmp"));
				
				String[] campos = new String[]{"cdLanc", "dtLanc", "tpLanc", "nmPes", "vlLanc", "obsLanc", "kmIni", "kmFin", "qtLitros", "consumo"};
				int[] ids = new int[]{R.id.txvCdLanc, R.id.txvDtLanc, R.id.txvTpLanc, R.id.txvNmPes, R.id.txvVlLanc, R.id.txvObsLanc, R.id.txvKmInicial, R.id.txvKmFinal, R.id.txvQtLitros, R.id.txvConsumo};
				
				SimpleAdapter listAdap = new SimpleAdapter(RelMovBan.this, lstMov, R.layout.adapter_rel_ban, campos, ids) {
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						View v = super.getView(position, convertView, parent);
						LinearLayout lnlDadosAbastec = ((LinearLayout) v.findViewById(R.id.lnlDadosAbastec));
						Movimentacao lancamento = (Movimentacao) super.getItem(position);
						lnlDadosAbastec.setVisibility(lancamento.get("tpLanc").equals("Despesa")? View.VISIBLE: View.GONE);
						
						return v;
					}
				};
				
				relMov.setAdapter(listAdap);
				
				double totDesp = 0;
				double totRec = 0;
				
				for(HashMap<String, String> mov: lstMov) {
					if(Funcoes.toInt(mov.get("tpLanc")) == 0)
						totDesp += Funcoes.toDouble(mov.get("vlLanc"));
					else
						totDesp += Funcoes.toDouble(mov.get("vlLanc"));
				}
				
				txvTotDesp.setText(Funcoes.decimal(totDesp, 2));
				txvTotRec.setText(Funcoes.decimal(totRec, 2));
				
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
								prgDialog = ProgressDialog.show(RelMovBan.this, "", "Exportando dados...");
							};
				
							@Override
							protected String doInBackground(Void... params) {
								try {
									
									runOnUiThread(new Runnable() {
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
												
												
												
												File imgPath = new File(Environment.getExternalStorageDirectory() + "/Melancia Sys/Relatorios");
												if(!imgPath.exists())
													imgPath.mkdir();
												
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
											        if(qtItens == 4 || i == (adapter.getCount() - 1)) {
			
														int totalHeight = rllHeader.getHeight() + allitemsheight + lnlPageNumber.getHeight();
														
														if(i == (adapter.getCount() - 1))
															totalHeight += rllFooter.getHeight();
														
														PageInfo pageInfo = new PageInfo.Builder(listview.getMeasuredWidth() + 200, totalHeight + 200, pageNumber).create();
														Page pdfPagina = pdfRelatorio.startPage(pageInfo);
														Canvas canvas = pdfPagina.getCanvas();
														
													    //Bitmap pagina = Bitmap.createBitmap(listview.getMeasuredWidth(), totalHeight, Bitmap.Config.ARGB_8888);
													    //Canvas canvas = new Canvas(pagina);
			
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
			
														txvPageNumber.setText("Página: " + Funcoes.formata(pageNumber, 2) + "/" + Funcoes.formata((int) Math.ceil(adapter.getCount() / 4.0), 2));
														
														lnlPageNumber.measure(MeasureSpec.makeMeasureSpec(rllHeader.getWidth(), MeasureSpec.EXACTLY), 
												                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
														lnlPageNumber.layout(0, 0, lnlPageNumber.getMeasuredWidth(), lnlPageNumber.getMeasuredHeight());
			
														lnlPageNumber.setDrawingCacheEnabled(true);
														lnlPageNumber.buildDrawingCache();
														canvas.drawBitmap(lnlPageNumber.getDrawingCache(), 100, usedHeight, paint);
														
														/*
														String caminho = Environment.getExternalStorageDirectory() + "/Melancia Sys/Relatorios/REL_BAN_" + data + "_" + Funcoes.formata(pageNumber, 3) + ".png";
														imgPath = new File(caminho);
								
												        FileOutputStream fOs = null;
											            fOs = new FileOutputStream(imgPath);
											            pagina.compress(Bitmap.CompressFormat.PNG, 100, fOs);
											            fOs.flush();
											            fOs.close();
											            
											            arquivos.add(Uri.fromFile(imgPath));
														*/
														
														pdfRelatorio.finishPage(pdfPagina);
														
													    allitemsheight = 0;
													    qtItens = 0;
													    pageNumber++;
													    
											        }

											    }
											    
											    String caminho = Environment.getExternalStorageDirectory() + "/Melancia Sys/Relatorios/REL_BAN_" + data + ".pdf";
												final File pdfPath = new File(caminho);
												
												FileOutputStream fileOS = new FileOutputStream(pdfPath);
												pdfRelatorio.writeTo(fileOS);
												pdfRelatorio.close();
												
											    prgDialog.dismiss();
												Funcoes.msgBox("Movimentação exportada com sucesso." + "Savo em " + Environment.getExternalStorageDirectory() + "/Melancia Sys/Relatorios", RelMovBan.this, new DialogInterface.OnClickListener() {							
													@Override
													public void onClick(DialogInterface dialog, int which) {
														Funcoes.pergunta("Deseja compartilhar o arquivo gerado?", RelMovBan.this, new DialogInterface.OnClickListener() {
															@Override
															public void onClick(DialogInterface dialog, int which) {
																Funcoes.compartilhaArquivo(RelMovBan.this, Uri.fromFile(pdfPath));
															}
														}, null);
												
													}
												});
	
											} catch(Exception e) {
												prgDialog.dismiss();
												Funcoes.msgBox("Falha ao exportar movimentação." + e.getMessage(), RelMovBan.this);
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
						Funcoes.msgBox("Falha ao exportar movimentação." + e.getMessage(), RelMovBan.this);
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
	
		 @Override
		protected void onDestroy() {
			MovBanActivity.lstRelMov = null;
			super.onDestroy();
		}
		 
	}
	
	public static class RelBan extends Activity{
		
		public ScrollView rllRelBan;
		
		public TextView txvNmEmp;
		public TextView txvCdLanc;
		public TextView txvDtLanc;
		public TextView txvNmPes;
		public TextView txvTpLanc;
		public TextView txvVlLanc;
		public TextView txvObsLanc;
		public TextView txvKmIni;
		public TextView txvKmFin;
		public TextView txvQtLitros;
		public TextView txvConsumo;
		
		public LinearLayout lnlDadosAbastec;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.rel_ban);
			
			try {
						
				rllRelBan = (ScrollView) findViewById(R.id.rllRelBan);
				
				txvNmEmp = (TextView) findViewById(R.id.txvNmEmp);
				txvCdLanc = (TextView) findViewById(R.id.txvCdLanc);
				txvDtLanc = (TextView) findViewById(R.id.txvDtLanc);
				txvNmPes = (TextView) findViewById(R.id.txvNmPes);
				txvTpLanc = (TextView) findViewById(R.id.txvTpLanc);
				txvVlLanc = (TextView) findViewById(R.id.txvVlLanc);
				txvObsLanc = (TextView) findViewById(R.id.txvObsLanc);
				txvKmIni = (TextView) findViewById(R.id.txvKmInicial);
				txvKmFin = (TextView) findViewById(R.id.txvKmFinal);
				txvQtLitros = (TextView) findViewById(R.id.txvQtLitros);
				txvConsumo = (TextView) findViewById(R.id.txvConsumo);
				
				lnlDadosAbastec = (LinearLayout) findViewById(R.id.lnlDadosAbastec);
				
				limpaCampos();
				
				new AsyncTask<Void, Void, Boolean>() {
					
					ProgressDialog prgDialog;
					String erro = "";
					Cursor crsTmp;
					
					protected void onPreExecute() {
						prgDialog = ProgressDialog.show(RelBan.this, "", "Processando...");
					};
					
					@Override
					protected Boolean doInBackground(Void... params) {
						try{
							String sql = "select ban.*, coalesce(ban.nm_pes, pes.nm_fants_pes) as nm_pes_cad" +
									" from ctrl_ban ban inner join pub_pes pes" + 
									" on ban.cd_emp = pes.cd_emp" +
									" and ban.cd_pes = pes.cd_pes" + 
									" where ban.cd_emp = " + Geral.cdEmpresa + 
									" and ban.cd_lanc = " + edtTermLanc.getString() + edtCdLanc.getString();
							
							crsTmp = Geral.localDB.rawQuery(sql, null);
							
							if(crsTmp.getCount() > 0){
								return true;
							}else{
								erro = "Lançamento não encontrado.";
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
							txvCdLanc.setText(Funcoes.formata(crsTmp.getInt(crsTmp.getColumnIndex("cd_lanc")), 6));
							txvDtLanc.setText(Funcoes.formataData(crsTmp.getString(crsTmp.getColumnIndex("dt_lanc")), "dd/MM/yyyy"));
							txvNmPes.setText(crsTmp.getString(crsTmp.getColumnIndex("nm_pes")));
							txvTpLanc.setText(crsTmp.getInt(crsTmp.getColumnIndex("tp_lanc")) == 0? "Despesa" : "Receita");
							txvVlLanc.setText(Funcoes.decimal(crsTmp.getDouble(crsTmp.getColumnIndex("vl_lanc")), 2));
							txvObsLanc.setText(crsTmp.getString(crsTmp.getColumnIndex("obs_lanc")));

							lnlDadosAbastec.setVisibility(crsTmp.getInt(crsTmp.getColumnIndex("tp_lanc")) == 0? View.VISIBLE: View.GONE);
							
							txvKmIni.setText(String.valueOf(crsTmp.getInt(crsTmp.getColumnIndex("km_ini"))));
							txvKmFin.setText(String.valueOf(crsTmp.getInt(crsTmp.getColumnIndex("km_fin"))));
							txvQtLitros.setText(Funcoes.decimal(crsTmp.getDouble(crsTmp.getColumnIndex("qt_litros")), 2));
							
							if(crsTmp.getDouble(crsTmp.getColumnIndex("km_ini")) > 0 &&
									crsTmp.getDouble(crsTmp.getColumnIndex("km_fin")) > 0 &&
									crsTmp.getDouble(crsTmp.getColumnIndex("qt_litros")) > 0 &&
									crsTmp.getDouble(crsTmp.getColumnIndex("km_fin")) > crsTmp.getDouble(crsTmp.getColumnIndex("km_ini"))) {
								
								double consumo = crsTmp.getDouble(crsTmp.getColumnIndex("km_fin")) - crsTmp.getDouble(crsTmp.getColumnIndex("km_ini"));
								consumo = consumo / crsTmp.getDouble(crsTmp.getColumnIndex("qt_litros"));
								
								txvConsumo.setText(Funcoes.decimal(consumo, 2));
							}else {
								txvConsumo.setText("0,00");
							}
								
							crsTmp.close();
							
						} catch (Exception e) {
							Funcoes.msgBox("Falha ao exibir registro." + e.getMessage(), RelBan.this, new DialogInterface.OnClickListener() {
								
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
				
				File imgPath;
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					try {
						rllRelBan.setDrawingCacheEnabled(true);
						rllRelBan.buildDrawingCache();
						Bitmap bmpRelEnt = rllRelBan.getDrawingCache();
						
						imgPath = new File(Environment.getExternalStorageDirectory() + "/Melancia Sys/Bancario");
						if(!imgPath.exists())
							imgPath.mkdir();
						
						String caminho = Environment.getExternalStorageDirectory() + "/Melancia Sys/Relatorios/ban_" + edtTermLanc.getString() + edtCdLanc.getString() + ".pdf";
						imgPath = new File(caminho);
						if(imgPath.exists())
							imgPath.delete();
						
						PdfDocument pdfRelatorio = new PdfDocument();
						PageInfo pageInfo = new PageInfo.Builder(bmpRelEnt.getWidth() + 200, bmpRelEnt.getHeight() + 200, 1).create();
						Page pagina = pdfRelatorio.startPage(pageInfo);
						
						pagina.getCanvas().drawBitmap(bmpRelEnt, 100,  100, new Paint());
						
						pdfRelatorio.finishPage(pagina);
						FileOutputStream fileOS = new FileOutputStream(imgPath);
						pdfRelatorio.writeTo(fileOS);
						pdfRelatorio.close();
						
						/*
				        FileOutputStream fOs = null;
			            fOs = new FileOutputStream(imgPath);
			            bmpRelEnt.compress(Bitmap.CompressFormat.PNG, 100, fOs);
			            fOs.flush();
			            fOs.close();
			            */
			            
			            Funcoes.msgBox("Movimentação exportada com sucesso." + "Savo em " + caminho, RelBan.this, new DialogInterface.OnClickListener() {							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Funcoes.pergunta("Deseja compartilhar o arquivo gerado?", RelBan.this, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										Funcoes.compartilhaArquivo(RelBan.this, Uri.fromFile(imgPath));
									}
								}, null);
							}
						});
			            
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao exportar movimentação." + e.getMessage(), RelBan.this);
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
			txvCdLanc.setText("");
			txvDtLanc.setText("");
			txvNmPes.setText("");
			txvTpLanc.setText("");
			txvVlLanc.setText("");
			txvObsLanc.setText("");
			txvKmIni.setText("");
			txvKmFin.setText("");
			txvQtLitros.setText("");
			txvConsumo.setText("");
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_mov_ban);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setTitle("Lançamento de Despesas/Receitas");
	    
	    edtTermLanc = (CEditText) findViewById(R.id.edtTermLanc);
	    edtCdLanc = (CEditText) findViewById(R.id.edtCdLanc);
	    edtDtEmis = (CEditText) findViewById(R.id.edtDtEmis);
	    btnRegAnt = (ImageButton) findViewById(R.id.btnAnt);
	    btnProxReg = (ImageButton) findViewById(R.id.btnProx);
	    edtTermPes = (CEditText) findViewById(R.id.edtTermPes);
	    edtCdPes = (CEditText) findViewById(R.id.edtCdPes);
	    edtNmPes = (CEditText) findViewById(R.id.edtNmPes);
	    btnPesqPes = (ImageButton) findViewById(R.id.btnPesqPes);
	    spnTpLanc = (Spinner) findViewById(R.id.spnTpLanc); 
	    edtVlLanc = (CEditText) findViewById(R.id.edtVlLanc);
	    edtObsLanc = (CEditText) findViewById(R.id.edtObsLanc);
	    lnlDadosAbastec = (LinearLayout) findViewById(R.id.lnlDadosAbastec);
	    edtKmInicial = (CEditText) findViewById(R.id.edtKmInicial);
	    edtKmFinal = (CEditText) findViewById(R.id.edtKmFinal);
	    edtQtdeLitros = (CEditText) findViewById(R.id.edtQtLitros);
	    edtConsumo = (CEditText) findViewById(R.id.edtConsumo);
	    
	    btnProxReg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					if(edtTermLanc.getInt() == 0)
						edtTermLanc.setText(Funcoes.formata(Geral.terminal, 2));
					
					if(edtCdLanc.getString().equals(""))
						edtCdLanc.setText("0000");
					
					String criterio = "cd_emp = " + Geral.cdEmpresa +
							" and cd_lanc between " + edtTermLanc.getString() + "0000 and " + edtTermLanc.getString() + "9999" +
							" and cd_lanc > " + edtTermLanc.getString() + edtCdLanc.getString() +
							" and st_lanc <> 2";
					
					long cdLanc = Funcoes.toLong(Funcoes.vlCampoTblLocal("min(cd_lanc)", "ctrl_ban", criterio));
					if(cdLanc > 0) {
						mostraDados(cdLanc);
					}
				} catch (Exception e) {
					Funcoes.msgBox("Falha ao consultar próximo registro." + e.getMessage(), MovBanActivity.this);
				}
			}
			
		});
		
		btnRegAnt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if(edtTermLanc.getInt() == 0)
						edtTermLanc.setText(Funcoes.formata(Geral.terminal, 2));
					
					if(edtCdLanc.getString().equals(""))
						edtCdLanc.setText("9999");
					
					String criterio = "cd_emp = " + Geral.cdEmpresa +
							" and cd_lanc between " + edtTermLanc.getString() + "0000 and " + edtTermLanc.getString() + "9999" +
							" and cd_lanc < " + edtTermLanc.getString() + edtCdLanc.getString() +
							" and st_lanc <> 2";
					
					long cdLanc = Funcoes.toLong(Funcoes.vlCampoTblLocal("max(cd_lanc)", "ctrl_ban", criterio));
					if(cdLanc > 0) {
						mostraDados(cdLanc);
					}
				} catch (Exception e) {
					Funcoes.msgBox("Falha ao buscar registro anterior." + e.getMessage(), MovBanActivity.this);
				}
			}
		});
		
		edtTermLanc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					if(edtTermLanc.getInt() == 0){
						edtTermLanc.setText(Funcoes.formata(Geral.terminal, 2));
						edtTermLanc.selectAll();
					}
				}
			}
		});
		
		edtTermLanc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				try{
					if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
						if(edtTermLanc.getInt() == 0){
							edtTermLanc.setText(Funcoes.formata(Geral.terminal, 2));
						}else
							edtTermLanc.setText(Funcoes.formata(edtTermLanc.getInt(), 2));
						
						if(edtTermLanc.getInt() != edtTermLanc.getCdAnterior()){
							String criterio = "cd_emp = " + Geral.cdEmpresa + " and cd_lanc between " + edtTermLanc.getInt() + "0000 and " + edtTermLanc.getInt() + "9999";
							edtCdLanc.setText(Funcoes.formata(Funcoes.proximoNumeroLocal("cd_lanc", "ctrl_ban", criterio), 6).substring(2));
							edtCdLanc.setCdAnterior(Funcoes.toLong(edtCdLanc.getString()));
						}
					}
				} catch(Exception e){
					Funcoes.msgBox("Falha ao buscar próximo código de movimentação.", MovBanActivity.this);
				}
				
				return false;
			}
		});;
		
		edtCdLanc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				try{
					if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
						edtCdLanc.formata(4);
						
						if(edtCdLanc.getLong() != edtCdLanc.getCdAnterior()){
							if(edtTermLanc.getInt() > 0 && edtCdLanc.getInt() > 0){
								long cdLanc = Funcoes.toLong(edtTermLanc.getString() + edtCdLanc.getString());
								
								if (Funcoes.toLong(Funcoes.vlCampoTblLocal("cd_lanc", "ctrl_ban", "cd_emp = " + Geral.cdEmpresa + " and cd_lanc = " + cdLanc).toString()) > 0){
									mostraDados(cdLanc);
								}else{
									Funcoes.msgBox("Nenhum registro encontrado com este código.", MovBanActivity.this);
									Funcoes.setaFoco(edtCdLanc);
								}
							}else{
								novoRegistro();
							}
						}
					}
				}catch (Exception e){
					Funcoes.msgBox("Falha ao consultar movimentação." + e.getMessage(), MovBanActivity.this);
				}
				return false;
			}
		});
		
		edtDtEmis.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Date data = Funcoes.toDate(edtDtEmis.getString());
				
				@SuppressWarnings("deprecation")
				DatePickerDialog dtpDlg = new DatePickerDialog(MovBanActivity.this, new DatePickerDialog.OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						edtDtEmis.setText(Funcoes.formataData(new Date(year - 1900, monthOfYear, dayOfMonth), "dd/MM/yyyy"));
					}
				}, data.getYear() + 1900, data.getMonth(), data.getDate());
				
				dtpDlg.show();
			}
		});

		edtDtEmis.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					edtDtEmis.performClick();
			}
		});
		
		spnTpLanc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				lnlDadosAbastec.setVisibility(position == 0? View.VISIBLE : View.GONE);
				if(position == 1) {
					edtKmInicial.setText("0");
					edtKmFinal.setText("0");
					edtQtdeLitros.setText("0");
					
					edtObsLanc.setImeOptions(EditorInfo.IME_ACTION_DONE);
				}else
					edtObsLanc.setImeOptions(EditorInfo.IME_ACTION_NEXT);
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
				try {
					if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
						if(edtTermPes.getInt() > 0 && edtCdPes.getInt() > 0){
							edtCdPes.formata(4);
							
							if(edtCdPes.getLong() != edtCdPes.getCdAnterior()){
								long cdPes = Funcoes.toLong(edtTermPes.getString() + edtCdPes.getString());
								
								edtNmPes.setText(Funcoes.vlCampoTblLocal("nm_fants_pes", "pub_pes", "cd_emp = " + Geral.cdEmpresa + " and cd_pes = " + cdPes).toString());
								if (edtNmPes.getString().equals("")){
									Funcoes.msgBox("Pessoa não encontrada.", MovBanActivity.this);
									Funcoes.setaFoco(edtCdPes);
								}
							}
						}else
							edtNmPes.setText("");
					}
				} catch (Exception e) {
					Funcoes.msgBox("Falha ao buscar dados da pessoa." + e.getMessage(), MovBanActivity.this);
				}
				return false;
			}
		});
		
		edtVlLanc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtVlLanc.decimal(2);			
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
					final View vPesq = ((LayoutInflater) MovBanActivity.this.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pesquisa, null, false);
					
					final Spinner spnFiltro = (Spinner) vPesq.findViewById(R.id.spnFiltro);
					final EditText edtPesq = (EditText) vPesq.findViewById(R.id.edtPesq);
					final ListView lstResult = (ListView) vPesq.findViewById(R.id.lstResult);
					
					ArrayList<ItemSpinner> lstFiltro = new ArrayList<ItemSpinner>();
					lstFiltro.add(new ItemSpinner("Nome fantasia", 0, "nm_fants_pes"));
					lstFiltro.add(new ItemSpinner("Razao Social", 1, "nm_razao_pes"));
					
					Funcoes.preencheSpinner(MovBanActivity.this, vPesq, R.id.spnFiltro, "Filtrar por", lstFiltro);
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
										Funcoes.preencheListViewLocal(MovBanActivity.this, vPesq, R.id.lstResult, crsResult, filtro, "cd_pes");
										crsResult.close();
									}
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao realizar consulta." + e.getMessage(), MovBanActivity.this);
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
					
					new AlertDialog.Builder(MovBanActivity.this)
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
							.show().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					
					Funcoes.setaFoco(edtPesq);
					
				}catch(Exception e){
					Funcoes.msgBox("Falha ao pesquisar pessoas." + e.getMessage(), MovBanActivity.this);
				}
				
			}
		});
		
		edtKmInicial.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtKmInicial.setText(String.valueOf(edtKmInicial.getInt()));
					calculaConsumo();
				}
				return false;
			}
		});
		
		edtKmFinal.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtKmFinal.setText(String.valueOf(edtKmFinal.getText()));
					calculaConsumo();
				}
				return false;
			}
		});
		
		edtQtdeLitros.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
					edtQtdeLitros.decimal(2);	
					calculaConsumo();
				}
				return false;
			}
		});
		
		novoRegistro();
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
		Funcoes.pergunta("Deseja relamente voltar para o menu principal?", MovBanActivity.this, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		}, null);
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
				if(validaDados()){
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
				
				if(edtTermLanc.getInt() > 0 && edtCdLanc.getInt() > 0){
					excluiRegistro();
				}else{
					Funcoes.msgBox("Informe o código do lançamento.", MovBanActivity.this);
					Funcoes.setaFoco(edtTermLanc);
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
					
					Intent intRelEnt = new Intent(MovBanActivity.this, RelBan.class);
					
					MovBanActivity.this.startActivity(intRelEnt);

				} catch (Exception e) {
					Funcoes.msgBox("Falha ao exportar registro." + e.getMessage(), MovBanActivity.this);
					novoRegistro();
				}
				
				return false;
			}
		};
		
		MenuItem mnuExpPDF = menu.add("Exportar");
		mnuExpPDF.setOnMenuItemClickListener(mnuExportarClick);
		
		MenuItem mnuRelMov = menu.add("Relatório de Movimentações");
		mnuRelMov.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			@SuppressLint("InflateParams")
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				try {
					View vRelMov = MovBanActivity.this.getLayoutInflater().inflate(R.layout.dialog_rel_ban, null);
					
					final CEditText edtDtIni = (CEditText) vRelMov.findViewById(R.id.edtDtIni);
					final CEditText edtDtFin = (CEditText) vRelMov.findViewById(R.id.edtDtFin);
					final Spinner spnTpLanc = (Spinner) vRelMov.findViewById(R.id.spnTpLanc);
					final CheckBox chkTdPes = (CheckBox) vRelMov.findViewById(R.id.chkTdPes);
					final CEditText edtTermPes = (CEditText) vRelMov.findViewById(R.id.edtTermPes);
					final CEditText edtCdPes = (CEditText) vRelMov.findViewById(R.id.edtCdPes);
					final CEditText edtNmPes = (CEditText) vRelMov.findViewById(R.id.edtNmPes);
					final ImageButton btnPesqPes = (ImageButton) vRelMov.findViewById(R.id.btnPesqPes);
					
					spnTpLanc.setSelection(2);
					edtDtIni.setText(Funcoes.formataData(new Date(), "dd/MM/yyyy"));
					edtDtFin.setText(Funcoes.formataData(new Date(), "dd/MM/yyyy"));
					
					edtDtIni.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Date data = Funcoes.toDate(edtDtIni.getString());
							
							@SuppressWarnings("deprecation")
							DatePickerDialog dtpDlg = new DatePickerDialog(MovBanActivity.this, new DatePickerDialog.OnDateSetListener() {
								
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
							
							@SuppressWarnings("deprecation")
							DatePickerDialog dtpDlg = new DatePickerDialog(MovBanActivity.this, new DatePickerDialog.OnDateSetListener() {
								
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
					
					chkTdPes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if(isChecked){
								edtTermPes.setText("");
								edtTermPes.setEnabled(false);
								edtCdPes.setText("");
								edtCdPes.setEnabled(false);
								edtNmPes.setText("");
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
							try {
								if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
									if(edtTermPes.getInt() > 0 && edtCdPes.getInt() > 0){
										edtCdPes.formata(4);
										
										if(edtCdPes.getLong() != edtCdPes.getCdAnterior()){
											long cdPes = Funcoes.toLong(edtTermPes.getString() + edtCdPes.getString());
											
											edtNmPes.setText(Funcoes.vlCampoTblLocal("coalesce(nm_fants_pes, nm_razao_pes)", "pub_pes", "cd_emp = " + Geral.cdEmpresa + " and cd_pes = " + cdPes).toString());
											if (edtNmPes.getString().equals("")){
												Funcoes.msgBox("Pessoa não encontrada.", MovBanActivity.this);
												Funcoes.setaFoco(edtCdPes);
											}
										}
									}else
										edtNmPes.setText("");
								}
							} catch (Exception e) {
								Funcoes.msgBox("Falha ao buscar dados da pessoa." + e.getMessage(), MovBanActivity.this);
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
								final View vPesq = ((LayoutInflater) MovBanActivity.this.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pesquisa, null, false);
								
								final Spinner spnFiltro = (Spinner) vPesq.findViewById(R.id.spnFiltro);
								final EditText edtPesq = (EditText) vPesq.findViewById(R.id.edtPesq);
								final ListView lstResult = (ListView) vPesq.findViewById(R.id.lstResult);
								
								ArrayList<ItemSpinner> lstFiltro = new ArrayList<ItemSpinner>();
								lstFiltro.add(new ItemSpinner("Nome fantasia", 0, "nm_fants_pes"));
								lstFiltro.add(new ItemSpinner("Razao Social", 1, "nm_razao_pes"));
								
								Funcoes.preencheSpinner(MovBanActivity.this, vPesq, R.id.spnFiltro, "Filtrar por", lstFiltro);
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
													Funcoes.preencheListViewLocal(MovBanActivity.this, vPesq, R.id.lstResult, crsResult, filtro, "cd_pes");
													crsResult.close();
												}
												
											} catch (Exception e) {
												Funcoes.msgBox("Falha ao realizar consulta." + e.getMessage(), MovBanActivity.this);
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
								
								new AlertDialog.Builder(MovBanActivity.this)
										.setTitle("Pesquisa de Pessoas")
										.setView(vPesq)
										.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface arg0, int arg1) {
												if(selecionado != null){
													chkTdPes.setChecked(false);
													edtTermPes.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(0, 2));
													edtCdPes.setText(Funcoes.formata(selecionado.getCodigo(), 6).substring(2));
													edtCdPes.onEditorAction(EditorInfo.IME_ACTION_NEXT);
												}									
											}
										})
										.setNegativeButton("Cancelar", null)
										.show().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
								
								Funcoes.setaFoco(edtPesq);
								
							}catch(Exception e){
								Funcoes.msgBox("Falha ao pesquisar pessoas." + e.getMessage(), MovBanActivity.this);
							}
							
						}
					});
									
					AlertDialog dlgRelMov = new AlertDialog.Builder(MovBanActivity.this)
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
									Funcoes.msgBox("Data final não pode ser inferior a data inicial.", MovBanActivity.this);
								} else if(!chkTdPes.isChecked() && (edtTermPes.getInt() == 0 || edtCdPes.getInt() == 0 || edtNmPes.getString().equals(""))){
									Funcoes.msgBox("Informe uma pessoa.", MovBanActivity.this);
									Funcoes.setaFoco(edtTermPes);
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
									prgDialog = ProgressDialog.show(MovBanActivity.this, "", "Buscando...");
								};
								
								@Override
								protected Boolean doInBackground(Void... params) {
									try {
										String sql = "select ban.*, pes.nm_fants_pes as nm_pes_cad" +
												" from ctrl_ban ban inner join pub_pes pes" + 
												" on ban.cd_emp = pes.cd_emp" +
												" and ban.cd_pes = pes.cd_pes" + 
												" where ban.cd_emp = " + Geral.cdEmpresa + 
												" and ban.st_lanc <> 2" +
												" and ban.dt_lanc between " + Funcoes.retDataLocal(edtDtIni.getDate()) + " and " + Funcoes.retDataLocal(edtDtFin.getDate());
										
										if(!chkTdPes.isChecked())
											sql += " and ban.cd_pes = " + edtTermPes.getString() + edtCdPes.getString();
											
										if(!Geral.usuarioAdm)
											sql += " and ban.cd_term = " + Geral.terminal;
										
										switch (spnTpLanc.getSelectedItemPosition()) {
											case 0: sql += " and ban.tp_lanc = 0"; break;
											case 1: sql += " and ban.tp_lanc = 1"; break;
											default: break;
										}
										
										sql += " order by ban.dt_lanc";
												
										crsTmp = Geral.localDB.rawQuery(sql, null);
										
										if(crsTmp.getCount() > 0){
											lstRelMov = new ArrayList<Movimentacao>();
											
											crsTmp.moveToFirst();
											do{
				
												Movimentacao mov = new Movimentacao(
														crsTmp.getLong(crsTmp.getColumnIndex("cd_lanc")), 
														Funcoes.formataData(crsTmp.getString(crsTmp.getColumnIndex("dt_lanc")), "dd/MM/yyyy"), 
														crsTmp.getInt(crsTmp.getColumnIndex("tp_lanc")), 
														crsTmp.getInt(crsTmp.getColumnIndex("cd_pes")),
														crsTmp.getString(crsTmp.getColumnIndex("nm_pes")),
														crsTmp.getDouble(crsTmp.getColumnIndex("vl_lanc")), 
														crsTmp.getString(crsTmp.getColumnIndex("obs_lanc")),
														crsTmp.getDouble(crsTmp.getColumnIndex("km_ini")), 
														crsTmp.getDouble(crsTmp.getColumnIndex("km_fin")),
														crsTmp.getDouble(crsTmp.getColumnIndex("qt_litros"))
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
										
										Intent intRelMov = new Intent(MovBanActivity.this, RelMovBan.class);
										intRelMov.putExtra("nmEmp", Funcoes.vlCampoTblLocal("nm_emp", "pub_emp", "cd_emp = " + Geral.cdEmpresa).toString());
										
										MovBanActivity.this.startActivity(intRelMov);
										
									} catch (Exception e) {
										Funcoes.msgBox(e.getMessage(), MovBanActivity.this);
									} finally {
										if(prgDialog.isShowing())
											prgDialog.dismiss();
									}
								};
								
							}.execute();
						}
					});
				
				} catch (Exception e) {
					Funcoes.msgBox("Falha ao efetuar a operação." + e.getMessage(), MovBanActivity.this);
				}
				
				return false;
			}
		});
		
		MenuItem mnuSair = menu.add("Sair");
		mnuSair.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				MovBanActivity.this.onBackPressed();
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
				menu.getItem(3).setEnabled(false);
			}else{
				menu.getItem(2).setEnabled(true);
				menu.getItem(3).setEnabled(true);
			}
		}
		return super.onMenuOpened(featureId, menu);
	}
	
	private void limpaTela() {
		edtTermLanc.setEnabled(Geral.usuarioAdm);
		edtTermLanc.setText(Funcoes.formata(Geral.terminal, 2));
		edtCdLanc.setText(Funcoes.formata(0, 4));
		edtDtEmis.setText(Funcoes.formataData(new Date(), "dd/MM/yyyy"));
		edtTermPes.setText(Funcoes.formata(Geral.terminal, 2));
		edtCdPes.setText(Funcoes.formata(0, 4));
		edtNmPes.setText("");
		edtVlLanc.setText(Funcoes.decimal(0, 2));
		edtObsLanc.setText("");
		spnTpLanc.setSelection(1);
		
		lnlDadosAbastec.setVisibility(View.GONE);
		edtKmInicial.setText("0");
		edtKmFinal.setText("0");
		edtQtdeLitros.setText("0,00");
		edtConsumo.setText("0,00");
		
		btnRegAnt.setEnabled(true);
		btnProxReg.setEnabled(true);	
	}
	
	private void novoRegistro() {
		limpaTela();
		
		try{
			String criterio = "cd_emp = " + Geral.cdEmpresa + " and cd_lanc between " + Geral.terminal + "0000 and " + Geral.terminal + "9999";
			long cdLanc = Funcoes.proximoNumeroLocal("cd_lanc", "ctrl_ban", criterio);
			if(cdLanc == 1)
				cdLanc = Funcoes.toInt(Geral.terminal + "0001");
			
			edtCdLanc.setText(Funcoes.formata(cdLanc, 6).substring(2));
			edtCdLanc.setCdAnterior(edtCdLanc.getLong());
			
			stRegistro = ST_REG_INCLUINDO;
			
			Funcoes.setaFoco(edtCdLanc);
			edtCdLanc.selectAll();
		} catch(Exception e){
			Funcoes.msgBox("Falha ao preparar tela para movimentação." + e.getMessage(), MovBanActivity.this);
			Funcoes.setaFoco(edtTermLanc);
		}
	}
	
	private void mostraDados(final long cdLanc) {
		try {
			
			new AsyncTask<Void, Void, Boolean>() {
				
				ProgressDialog prgDialog;
				String erro = "";
				Cursor rsTmp;
				boolean firstReg = false;
				boolean lastReg = false;
				
				protected void onPreExecute() {
					prgDialog = ProgressDialog.show(MovBanActivity.this, "", "Carregando...");
				};
				
				@Override
				protected Boolean doInBackground(Void... params) {
					try{
						String sql = "select ban.*, coalesce(ban.nm_pes, pes.nm_fants_pes) as nm_pes_cad" +
								" from ctrl_ban ban inner join pub_pes pes" + 
								" on ban.cd_emp = pes.cd_emp" +
								" and ban.cd_pes = pes.cd_pes" + 
								" where ban.cd_emp = " + Geral.cdEmpresa + 
								" and ban.cd_lanc = " + cdLanc;
						
						rsTmp = Geral.localDB.rawQuery(sql, null);
						
						if(rsTmp != null){
							String criterio = "cd_emp = " + Geral.cdEmpresa +
									" and cd_lanc between " + edtTermLanc.getText().toString() + "0000 and " + edtTermLanc.getText().toString() + "9999" +
									" and cd_lanc > " + cdLanc +
									" and st_lanc <> 2" +
									" limit 1";
							
							if(Funcoes.toLong(Funcoes.vlCampoTblLocal("cd_lanc", "ctrl_ban", criterio)) == 0){
								lastReg = true;
							}
							
							criterio = "cd_emp = " + Geral.cdEmpresa +
									" and cd_lanc between " + edtTermLanc.getText().toString() + "0000 and " + edtTermLanc.getText().toString() + "9999" +
									" and cd_lanc < " + cdLanc +
									" and st_lanc <> 2" +
									" limit 1";									
							
							if(Funcoes.toLong(Funcoes.vlCampoTblLocal("cd_lanc", "ctrl_ban", criterio)) == 0){
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
						
						if(rsTmp.getInt(rsTmp.getColumnIndex("st_lanc")) == 2){
							Funcoes.msgBox("Registro cancelado.", MovBanActivity.this);
							novoRegistro();
							return;
						}
							
						limpaTela();
						edtTermLanc.setText(Funcoes.formata(cdLanc, 6).substring(0, 2));
						edtCdLanc.setText(Funcoes.formata(cdLanc, 6).substring(2));
						edtCdLanc.setCdAnterior(edtCdLanc.getLong());
						
						edtDtEmis.setText(Funcoes.formataData(rsTmp.getString(rsTmp.getColumnIndex("dt_lanc")), "dd/MM/yyyy"));
						edtTermPes.setText(Funcoes.formata(rsTmp.getInt(rsTmp.getColumnIndex("cd_pes")), 6).substring(0, 2));
						edtCdPes.setText(Funcoes.formata(rsTmp.getInt(rsTmp.getColumnIndex("cd_pes")), 6).substring(2));
						edtNmPes.setText(rsTmp.getString(rsTmp.getColumnIndex("nm_pes_cad")));
						edtVlLanc.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("vl_lanc")), 2));
						spnTpLanc.setSelection(rsTmp.getInt(rsTmp.getColumnIndex("tp_lanc")), true);
						edtObsLanc.setText(rsTmp.getString(rsTmp.getColumnIndex("obs_lanc")));

						edtKmInicial.setText(String.valueOf(rsTmp.getInt(rsTmp.getColumnIndex("km_ini"))));
						edtKmFinal.setText(String.valueOf(rsTmp.getInt(rsTmp.getColumnIndex("km_fin"))));
						edtQtdeLitros.setText(Funcoes.decimal(rsTmp.getDouble(rsTmp.getColumnIndex("qt_litros")), 2));
						calculaConsumo();
						
						rsTmp.close();
						
						btnRegAnt.setEnabled(!firstReg);
						btnProxReg.setEnabled(!lastReg);

						Funcoes.setaFoco(edtCdLanc);
						stRegistro  = ST_REG_EDITANDO;
						
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao exibir registro." + e.getMessage(), MovBanActivity.this);
						novoRegistro();
					} finally {
						if(prgDialog.isShowing())
							prgDialog.dismiss();
					}
				};
				
			}.execute();
			
		} catch (Exception e) {
			Funcoes.msgBox("Falha ao exibir registro." + e.getMessage(), MovBanActivity.this);
			novoRegistro();
		}
	}
	
	private boolean validaDados(){
		try {
			if(edtTermPes.getInt() == 0 || edtCdPes.getInt() == 0 || edtNmPes.getString().trim().length() == 0){
				Funcoes.msgBox("Informe o destinatário/remetente.", MovBanActivity.this);
				Funcoes.setaFoco(edtTermPes);
			}else if(edtVlLanc.getDouble() == 0){
				Funcoes.msgBox("Informe o valor do lançamento.", MovBanActivity.this);
				Funcoes.setaFoco(edtVlLanc);
			}else if(edtKmFinal.getInt() < edtKmInicial.getInt()) {
				Funcoes.msgBox("A kilometragem final não pode ser inferior a inicial.",  MovBanActivity.this);
				Funcoes.setaFoco(edtKmFinal);
			}else{
				return true;
			}
			
			return false;
			
		} catch (Exception e) {
			Funcoes.msgBox("Falha ao validadar lançamento." + e.getMessage(), MovBanActivity.this);
			return false;
		}
	}
	
	private void incluiRegistro(){
		try {
			Funcoes.pergunta("Deseja gravar o registro atual?", MovBanActivity.this, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						new AsyncTask<Void, Void, Boolean>(){
							ProgressDialog prgDialog;
							String erro = "";
							
							@Override
							protected void onPreExecute() {
								prgDialog = ProgressDialog.show(MovBanActivity.this, "", "Processando...");
							};
							
							@Override
							protected Boolean doInBackground(Void... params) {
								try{
									
									String criterio = "cd_emp = " + Geral.cdEmpresa + " and cd_lanc between " + Geral.terminal + "0000 and " + Geral.terminal + "9999";
									long cdLanc = Funcoes.proximoNumeroLocal("cd_lanc", "ctrl_ban", criterio);
									if(cdLanc == 1)
										cdLanc = Funcoes.toInt(Geral.terminal + "0001");
									
									String[] vetCampos = new String[]{"cd_emp", "cd_lanc", "dt_lanc", "tp_lanc", "st_lanc",
							                "cd_pes", "nm_pes", "vl_lanc", "obs_lanc", "km_ini", "km_fin", "qt_litros", 
							                "cd_usu", "cd_term", "cd_ver"};
									
									Object[] vetValores = new Object[]{Geral.cdEmpresa, cdLanc, Funcoes.retDataLocal(edtDtEmis.getString()), spnTpLanc.getSelectedItemPosition(), 0,
											Funcoes.toInt(edtTermPes.getString() + edtCdPes.getString()), edtNmPes.getString(), edtVlLanc.getDouble(), edtObsLanc.getString(), 
											edtKmInicial.getDouble(), edtKmFinal.getDouble(), edtQtdeLitros.getDouble(),
											Geral.cdUsuario, Geral.terminal, 1};
									
									Geral.localDB.execSQL(Funcoes.criaSQLInsert("ctrl_ban", vetCampos, vetValores));
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

									Funcoes.msgBox("Registro incluído com sucesso!", MovBanActivity.this);
									novoRegistro();
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao gravar registro." + e.getMessage(), MovBanActivity.this);
								} finally{
									if(prgDialog.isShowing())
										prgDialog.dismiss();
								}
							};
							
						}.execute();
						
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao gravar registro." + e.getMessage(), MovBanActivity.this);
					}
				}
			}, null);
			
		} catch (Exception e) {
			Funcoes.msgBox("Ocorreu um erro ao efetuar a operação." + e.getMessage(), MovBanActivity.this);
		}
	}
	
	private void alteraRegistro(){
		try {
			Funcoes.pergunta("Deseja gravar as alterações do registro atual?", MovBanActivity.this, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						new AsyncTask<Void, Void, Boolean>(){
							ProgressDialog prgDialog;
							String erro = "";
							
							@Override
							protected void onPreExecute() {
								prgDialog = ProgressDialog.show(MovBanActivity.this, "", "Processando...");
							};
							
							@Override
							protected Boolean doInBackground(Void... params) {
								try{
									
									String criterio = "cd_emp = " + Geral.cdEmpresa
													+ " and cd_lanc = " + edtTermLanc.getString() + edtCdLanc.getString();
									
									long verReg = Funcoes.proximoNumeroLocal("cd_ver", "ctrl_ban", criterio);
											
									String[] vetCampos = new String[]{"dt_lanc", "tp_lanc", "cd_pes", 
											"nm_pes", "vl_lanc", "obs_lanc", "km_ini", "km_fin", "qt_litros", 
											"cd_usu", "cd_term", "st_reg", "cd_ver"};
									
									Object[] vetValores = new Object[]{Funcoes.retDataLocal(edtDtEmis.getString()), spnTpLanc.getSelectedItemPosition(), Funcoes.toInt(edtTermPes.getString() + edtCdPes.getString()), 
											edtNmPes.getString(), edtVlLanc.getDouble(), edtObsLanc.getString(), 
											edtKmInicial.getDouble(), edtKmFinal.getDouble(), edtQtdeLitros.getDouble(),
											Geral.cdUsuario, Geral.terminal, 0, verReg};
									
									Geral.localDB.execSQL(Funcoes.criaSQLUpdate("ctrl_ban", vetCampos, vetValores, criterio));
									
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
									
									Funcoes.msgBox("Registro alterado com sucesso!", MovBanActivity.this);
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao alterar registro." + e.getMessage(), MovBanActivity.this);
								} finally{
									if(prgDialog.isShowing())
										prgDialog.dismiss();
								}
							};
							
						}.execute();
						
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao alterar registro." + e.getMessage(), MovBanActivity.this);
					}
				}
			}, null);
			
		} catch (Exception e) {
			Funcoes.msgBox("Ocorreu um erro ao efetuar a operação." + e.getMessage(), MovBanActivity.this);
		}	
	}
	
	private void excluiRegistro(){
		try {
			Funcoes.pergunta("Deseja gravar o registro atual?", MovBanActivity.this, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						new AsyncTask<Void, Void, Boolean>(){
							ProgressDialog prgDialog;
							String erro = "";
							
							@Override
							protected void onPreExecute() {
								prgDialog = ProgressDialog.show(MovBanActivity.this, "", "Processando...");
							};
							
							@Override
							protected Boolean doInBackground(Void... params) {
								try{
									
									String criterio = "cd_emp = " + Geral.cdEmpresa
													+ " and cd_lanc = " + edtTermLanc.getString() + edtCdLanc.getString();
									
									long verReg = Funcoes.proximoNumeroLocal("cd_ver", "ctrl_ban", criterio);
									
									String[] vetCampos = new String[]{"CD_TERM", "ST_REG", "ST_LANC", "CD_VER"};
									Object[] vetValores = new Object[]{Geral.terminal, 0, 2, verReg};
									
									Geral.localDB.execSQL(Funcoes.criaSQLUpdate("ctrl_ban", vetCampos, vetValores, criterio));
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
									
									Funcoes.msgBox("Registro excluído com sucesso!", MovBanActivity.this);
									novoRegistro();
									
								} catch (Exception e) {
									Funcoes.msgBox("Falha ao excluir registro." + e.getMessage(), MovBanActivity.this);
								} finally{
									if(prgDialog.isShowing())
										prgDialog.dismiss();
								}
							};
							
						}.execute();
						
					} catch (Exception e) {
						Funcoes.msgBox("Falha ao excluir registro." + e.getMessage(), MovBanActivity.this);
					}
				}
			}, null);
			
		} catch (Exception e) {
			Funcoes.msgBox("Ocorreu um erro ao efetuar a operação." + e.getMessage(), MovBanActivity.this);
		}
	}

	private void calculaConsumo() {
		if(edtKmInicial.getDouble() > 0 && edtKmFinal.getDouble() > 0 && edtQtdeLitros.getDouble() > 0) {
			if(edtKmFinal.getDouble() > edtKmInicial.getDouble())
				edtConsumo.setText(Funcoes.decimal((edtKmFinal.getDouble() - edtKmInicial.getDouble()) / edtQtdeLitros.getDouble(), 2));
		}
	}

}
