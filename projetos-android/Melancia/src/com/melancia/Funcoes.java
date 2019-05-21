package com.melancia;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public abstract class Funcoes {

	//Natureza de operaçao
	public static final String BX_ORI_NT = "BX_ORI_NT";
	public static final String ATUAL_ORI_NT = "ATUAL_ORI_NT"; 
	public static final String GERAR_DUPL_NT = "GERAR_DUPL_NT";
	public static final String GERAR_COMIS_NT = "GERAR_COMIS_NT";
	
	//Tipos de saldo
	public static final byte SALDO_R = 0;
	public static final byte SALDO_F = 1; 
	public static final byte SALDO_DISP = 2;
	
	//Activity RequestCode
	public static final int ENVIAR_EMAIL = 90;
	public static final int COMPARTILHAR_ARQUIVO = 91;

	public static class Autorizacoes{
		
		public static class Acoes{
			public static final byte ABRIR = 0;
			public static final byte INCLUIR = 1;
			public static final byte ALTERAR = 2;
			public static final byte PESQUISAR = 3;
			public static final byte EXCLUIR = 4;
			public static final byte IMPRIMIR = 5;
			public static final byte OUTROS = 6;
		}
		
		public static class Procedimentos{
			public static final byte NENHUM = 0;
			public static final byte VND_C_DEBITO = 1;
			public static final byte ACERTAR_ESTOQUE = 2;
			public static final byte ALT_PRC_VENDA = 3;
			public static final byte CONC_DESC_ACIMA_PERM = 4;
			public static final byte VND_CLI_S_CREDITO = 5;
			public static final byte CONC_DESC_VENDA = 6;
			public static final byte ALT_EXCL_FAT_ANT = 7;
			public static final byte LIMPAR_NS_NR_DUPL = 8;
			public static final byte UTL_TBL_PRC_VENC = 9;
			public static final byte VND_PROD_S_TBL_PRC = 10;
			public static final byte ANTECIP_UTL_TBL_PRC = 11;
			public static final byte ALT_EMB_TBL_PRC = 12;
			public static final byte RECL_FECH_CX_ANT = 13;
			public static final byte VIS_PREV_VND_PEND = 14;
			public static final byte ALT_COND_PAG_CLI = 15;
			public static final byte ALT_PRC_TBL_VND = 16;
			public static final byte EMITIR_REL_MOV_DIARIO_GERAL = 17;
			public static final byte FAZER_BONIF = 18;
			public static final byte VND_CLI_C_TIT_EM_ABERTO = 19;
			public static final byte ALT_STATUS_OS = 20;
			public static final byte VENDA_C_DEBITO = 21;
			public static final byte CONC_DESC_IT = 22;
			public static final byte EMITIR_SEG_VIA_PED = 23;
			public static final byte ALT_FATURAMENTO = 24;
			public static final byte EXCL_FATURAMENTO = 25;
			public static final byte INCL_ORD_SERV = 28;
			public static final byte ALT_ORD_SERV = 27;
			public static final byte EXCL_ORD_SERV = 28;
			public static final byte ALT_VL_FATURAMENTO = 29;
			public static final byte ALT_QTDE_IT_VENDA = 33;
		}
		
		public static class AutorizacaoConcedida{
			public int cdUsuPediuAutorizacao;
			public int cdAcao;
			public int cdProcedimento;
			public int cdUsuAutorizou;
			public Date dtAutorizacao;
			public Date hrAutorizacao;
			
			public AutorizacaoConcedida(){
				
			}
		}
	}

	public static class ItemSpinner {

		String label;
		String tag;
		int codigo;
		
		public ItemSpinner(String label, int codigo, String tag) {
			this.label = label;
			this.codigo = codigo;
			this.tag = tag;
		}
		
		public ItemSpinner(String label, int codigo) {
			this.label = label;
			this.codigo = codigo;
		}
		
		public ItemSpinner(){
			
		}
		
		public final String getLabel() {
			return label;
		}
		public final void setLabel(String label) {
			this.label = label;
		}
		public final int getCodigo() {
			return codigo;
		}
		public final void setCodigo(int codigo) {
			this.codigo = codigo;
		}

		public String getTag() {
			return tag;
		}
		public void setTag(String tag) {
			this.tag = tag;
		}
		
		@Override
		public String toString() {
			return label;
		}
		
	}
	
	public static void setaFoco(final EditText campo){
		((View) campo.getParent()).post(new Runnable() {
			@Override
			public void run() {
				campo.requestFocus();
				campo.requestFocusFromTouch();
				
			}
		});
	}
	
	public static void selecionaCampo(EditText txtCampo) {
		txtCampo.setSelection(0, txtCampo.length());	
	}
	
	public static void preencheListView(Activity context, View view, int listView, ResultSet rsItens, String label, String value) {
		
		ListView list = null;
		
		try {
			ArrayList<ItemSpinner> itens = new ArrayList<ItemSpinner>();

			while (rsItens.next()) {
				ItemSpinner item = new ItemSpinner();

				item.setLabel(rsItens.getString(label));
				item.setCodigo(rsItens.getInt(value));

				itens.add(item);
			}

			ArrayAdapter<ItemSpinner> itensAdapter = new ArrayAdapter<ItemSpinner>(context, R.layout.adapter_list_pesquisa, itens);
			
			if (view == null){
				list = (ListView) context.findViewById(listView);
			}else{
				list = (ListView) view.findViewById(listView);
			};
			
			list.setAdapter(itensAdapter);
			list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		} catch (Exception e) {
			throw new RuntimeException("Falha ao preparar componente." + e.getMessage());
		}

	}

	public static void preencheListViewLocal(Activity context, View view, int listView, Cursor crsItens, String label, String value) {
		
		ListView list = null;
		
		try {
			ArrayList<ItemSpinner> itens = new ArrayList<ItemSpinner>();
			crsItens.moveToFirst();
			do{
				ItemSpinner item = new ItemSpinner();

				item.setLabel(crsItens.getString(crsItens.getColumnIndex(label)).trim());
				item.setCodigo(crsItens.getInt(crsItens.getColumnIndex(value)));

				itens.add(item);
			}while (crsItens.moveToNext());

			ArrayAdapter<ItemSpinner> itensAdapter = new ArrayAdapter<ItemSpinner>(context, R.layout.adapter_list_pesquisa, itens);
			
			if (view == null){
				list = (ListView) context.findViewById(listView);
			}else{
				list = (ListView) view.findViewById(listView);
			};
			
			list.setAdapter(itensAdapter);
			list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		} catch (Exception e) {
			throw new RuntimeException("Falha ao preparar componente." + e.getMessage());
		}

	}
	
	public static void preencheSpinner(Activity context, View view, int spinner, String titulo, ResultSet rsItens, String label, String value) {
		
		Spinner spn = null;
		
		try {
			ArrayList<ItemSpinner> itens = new ArrayList<ItemSpinner>();

			while (rsItens.next()) {
				ItemSpinner item = new ItemSpinner();

				item.setLabel(rsItens.getString(label));
				item.setCodigo(rsItens.getInt(value));

				itens.add(item);
			}

			ArrayAdapter<ItemSpinner> itensAdapter = new ArrayAdapter<ItemSpinner>(context, android.R.layout.simple_spinner_item, itens);
			itensAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			if (view == null){
				spn = (Spinner) context.findViewById(spinner);
			}else{
				spn = (Spinner) view.findViewById(spinner);
			};
			
			spn.setAdapter(itensAdapter);
			spn.setPrompt(titulo);

		} catch (Exception e) {
			throw new RuntimeException("Falha ao preparar componente." + e.getMessage());
		}

	}
	
	public static <T> void preencheSpinner(Activity context, View view, int spinner, String titulo, ArrayList<T> lstItens) {
		
		Spinner spn = null;
		
		try {

			ArrayAdapter<T> itensAdapter = new ArrayAdapter<T>(context, android.R.layout.simple_spinner_item, lstItens);
			itensAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			if (view == null){
				spn = (Spinner) context.findViewById(spinner);
			}else{
				spn = (Spinner) view.findViewById(spinner);
			};
			
			spn.setAdapter(itensAdapter);
			spn.setPrompt(titulo);

		} catch (Exception e) {
			throw new RuntimeException("Falha ao preparar componente." + e.getMessage());
		}

	}

	public static void preencheSpinnerLocal(Activity context, View view, int spinner, String titulo, String tabelas, String criterio, String[] valoresCriterio, String ordem, String label, String value) {
		
		Spinner spn = null;
		
		try {
			
			if(Geral.localDB == null){
				throw new Exception("A conexão com o banco de dados está fechada.");
			}
			
			ArrayList<ItemSpinner> itens = new ArrayList<ItemSpinner>();
			
			if (view == null){
				spn = (Spinner) context.findViewById(spinner);
			}else{
				spn = (Spinner) view.findViewById(spinner);
			};
			
			String sql = "select " + label + ", " + value + " from " + tabelas;
			if(criterio != null){
				if(!criterio.equals(""))
					sql += " where " + criterio;
			}
			
			if(ordem != null){
				if(!ordem.equals(""))
					sql += " order by " + ordem;
			}
			
			Cursor rsTmp = Geral.localDB.rawQuery(sql, valoresCriterio);
			if(rsTmp.getCount() > 0){

				rsTmp.moveToFirst();
				do{
					ItemSpinner item = new ItemSpinner();
	
					item.setLabel(rsTmp.getString(0));
					item.setCodigo(rsTmp.getInt(1));
	
					itens.add(item);
				}while(rsTmp.moveToNext());

				ArrayAdapter<ItemSpinner> itensAdapter = new ArrayAdapter<ItemSpinner>(context, android.R.layout.simple_spinner_item, itens);
				itensAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				spn.setAdapter(itensAdapter);
				spn.setPrompt(titulo);
				rsTmp.close();
			}else{
				ArrayAdapter<ItemSpinner> itensAdapter = new ArrayAdapter<ItemSpinner>(context, android.R.layout.simple_spinner_item, itens);
				itensAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spn.setAdapter(itensAdapter);
				spn.setPrompt(titulo);
			}

		} catch (Exception e) {
			throw new RuntimeException("Erro ao recuperar dados" + e.getMessage());
		}

	}
	
	public static void preencheSpinner(Activity context, View view, int spinner, String titulo, String tabelas, String campos, String criterio, String ordem, String label, String value) {
		
		Spinner spn = null;
		
		try {
			
			if(!Geral.dbCn.conexaoAberta()){
				throw new Exception("A conexão com o banco de dados está fechada.");
			}
			
			String sql = "";
			ArrayList<ItemSpinner> itens = new ArrayList<ItemSpinner>();
			
			if (view == null){
				spn = (Spinner) context.findViewById(spinner);
			}else{
				spn = (Spinner) view.findViewById(spinner);
			};
			
			sql = "select distinct ";
			if(!campos.equals("")){
				sql += campos;
			}else{
				sql += "(to_ascii(" + label + ", 'LATIN1')) as " + label + ", " + value;
			}
			
			sql += " from " + tabelas;
			if(!criterio.equals(""))sql += " where " + criterio;
			if(!ordem.equals(""))sql += " order by " + ordem;

			ResultSet rsTmp = Geral.dbCn.retReg(sql);
			
			if(rsTmp != null){
				while (rsTmp.next()) {
					ItemSpinner item = new ItemSpinner();
	
					item.setLabel(rsTmp.getString(label));
					item.setCodigo(rsTmp.getInt(value));
	
					itens.add(item);
				}

				ArrayAdapter<ItemSpinner> itensAdapter = new ArrayAdapter<ItemSpinner>(context, android.R.layout.simple_spinner_item, itens);
				itensAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				spn.setAdapter(itensAdapter);
				spn.setPrompt(titulo);
				rsTmp.close();
			}else{
				ArrayAdapter<ItemSpinner> itensAdapter = new ArrayAdapter<ItemSpinner>(context, android.R.layout.simple_spinner_item, itens);
				itensAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spn.setAdapter(itensAdapter);
				spn.setPrompt(titulo);
			}

		} catch (Exception e) {
			throw new RuntimeException("Erro ao recuperar dados" + e.getMessage());
		}

	}
	
	public static void selItemSpinner(Activity context, View view, int spinner, int cdItem, boolean atualiza){
		
		try{
			Spinner spn = null;

			if(view == null){
				spn = (Spinner) context.findViewById(spinner);
			}else{
				spn = (Spinner) view.findViewById(spinner);
			}

			for (int i = 0; i < spn.getCount(); i++) {
				if(((ItemSpinner) spn.getItemAtPosition(i)).getCodigo() == cdItem){
					spn.setSelection(i, atualiza);
					break;
				}
			}
			
		}catch(Exception e){
			throw new RuntimeException("Falha ao selecionar item do controle." + e.getMessage());
		}
	}
	
	public static void selItemSpinner(Activity context, View view, int spinner, String dsItem, boolean atualiza){
		
		try{
			Spinner spn = null;

			if(view == null){
				spn = (Spinner) context.findViewById(spinner);
			}else{
				spn = (Spinner) view.findViewById(spinner);
			}

			for (int i = 0; i < spn.getCount(); i++) {
				if(spn.getItemAtPosition(i).equals(dsItem)){
					spn.setSelection(i, atualiza);
					break;
				}
			}
			
		}catch(Exception e){
			throw new RuntimeException("Falha ao selecionar item do controle." + e.getMessage());
		}
	}
	
	public static void selItemSpinner(Activity context, View view, int spinner, Object item, boolean atualiza){
		
		try{
			Spinner spn = null;

			if(view == null){
				spn = (Spinner) context.findViewById(spinner);
			}else{
				spn = (Spinner) view.findViewById(spinner);
			}

			for (int i = 0; i < spn.getCount(); i++) {
				if(spn.getItemAtPosition(i).equals(item)){
					spn.setSelection(i, atualiza);
					break;
				}
			}
			
		}catch(Exception e){
			throw new RuntimeException("Falha ao selecionar item do controle." + e.getMessage());
		}
	}

	public static void limpaSpinner(Activity context, View view, int spinner){
		Spinner spn = null;
		
		if (view == null){
			spn = (Spinner) context.findViewById(spinner);
		}else{
			spn = (Spinner) view.findViewById(spinner);
		};
		
		if(spn.getAdapter() != null){
			ArrayAdapter<String> tmpAdap = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
			spn.setAdapter(tmpAdap);
			
		}
		
	}
	
	public static void pergunta(String msg, Activity context, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {		
		AlertDialog.Builder alerta = new AlertDialog.Builder(context)
				.setTitle(R.string.app_name).setMessage(msg)
				.setPositiveButton("Sim", positiveListener)
				.setNegativeButton("Não", negativeListener)
				.setCancelable(false);
		alerta.show();
	}
	
	public static void msgBox(String msg, Activity context) {
		AlertDialog.Builder alerta = new AlertDialog.Builder(context);
		alerta.setTitle(R.string.app_name);
		alerta.setMessage(msg);
		alerta.setPositiveButton("Ok", null);
		alerta.show();
	}

	public static void msgBox(String msg, Activity context, DialogInterface.OnClickListener okListener) {
		AlertDialog.Builder alerta = new AlertDialog.Builder(context);
		alerta.setTitle(R.string.app_name);
		alerta.setMessage(msg);
		alerta.setPositiveButton("Ok", okListener);
		alerta.setCancelable(false);
		alerta.show();

	}
	
	public static String decrypt(String texto) {
		int sana = 0;
		int salasana = 0;
		int g = 0;
		int x1 = 0;

		String decrypted = "";

		for (int i = 0; i < texto.length(); i++) {
			try {
				sana = (int) texto.charAt(i);
				g++;

				if (g == 6) {
					g = 0;
					x1 = 0;
				}
				if (g == 0) {
					x1 = sana + (salasana - 2);
				}
				if (g == 1) {
					x1 = sana - (salasana - 5);
				}
				if (g == 2) {
					x1 = sana + (salasana - 4);
				}
				if (g == 3) {
					x1 = sana - (salasana - 2);
				}
				if (g == 4) {
					x1 = sana + (salasana - 3);
				}
				if (g == 5) {
					x1 = sana - (salasana - 5);
				}

				x1 = x1 - g;
				decrypted = decrypted + (char) x1;
			} catch (Exception e) {
				continue;
			}
		}

		return decrypted;
	}

	public static String decrypt2(String texto) {

		int strTempChar = 0;
		String decrypted = "";

		for (int i = 0; i < texto.length(); i++) {
			try {
				strTempChar = (int) texto.charAt(i);

				if (strTempChar == 130) {
					strTempChar = 130;
				} else if (strTempChar > 129) {
					if ((strTempChar + 2) < 255) {
						strTempChar = strTempChar - 2;
					}
				} else if (strTempChar < 129) {
					strTempChar = strTempChar - 10;
				}

				if (strTempChar < 128) {
					strTempChar = strTempChar + 128;
				} else if (strTempChar > 130) {
					strTempChar = strTempChar - 128;
				}

				decrypted = decrypted + (char) strTempChar;
			} catch (Exception e) {
				continue;
			}
		}

		return decrypted;
	}

	public static Object vlCampoTbl(String campos, String tabelas, String criterio){
		try{
			ResultSet rsTmp;
			String sql = "select " + campos;
			if(!tabelas.equals("")) sql += " from " + tabelas;			
			if(!criterio.equals("")) sql += " where " + criterio;
			
			rsTmp = Geral.dbCn.retReg(sql);

			if(rsTmp != null){
				rsTmp.next();
				Object valor = rsTmp.getObject(1);
				rsTmp.close();
				if(valor == null){
					return "";
				}
				
				return valor;
			}else{
				return "";
			}
			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static Object vlCampoTblLocal(String campos, String tabelas, String criterio) throws Exception{
		try{
			
			String sql = "select " + campos + " from " + tabelas;
			if(criterio != null){
				if(!criterio.equals(""))
					sql += " where " + criterio;
			}
			
			Cursor crsTmp = Geral.localDB.rawQuery(sql, null);
			if(crsTmp.getCount() > 0){
				crsTmp.moveToFirst();
				switch (crsTmp.getType(0)) {
				case Cursor.FIELD_TYPE_INTEGER: return crsTmp.getInt(0);
				case Cursor.FIELD_TYPE_STRING: return crsTmp.getString(0); 
				case Cursor.FIELD_TYPE_FLOAT: return crsTmp.getDouble(0);
				case Cursor.FIELD_TYPE_BLOB: return crsTmp.getBlob(0); 
				default: return "";
				}
			}else{
				return "";
			}
			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	public static String retData(String data){
		Date dt = new Date();
		SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			dt = dtFormat.parse(data);
			dtFormat = new SimpleDateFormat("MM-dd-yyyy");
			return  "'" + dtFormat.format(dt).toString() + "'";
		} catch (ParseException e) {
			throw new RuntimeException("Erro ao converter data para consulta." + e.getMessage());
		}
	
	}
	
	public static String retDataLocal(String data){
		Date dt = new Date();
		SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			dt = dtFormat.parse(data);
			dtFormat = new SimpleDateFormat("yyyy-MM-dd");
			return  "'" + dtFormat.format(dt).toString() + "'";
		} catch (ParseException e) {
			throw new RuntimeException("Erro ao converter data para consulta." + e.getMessage());
		}
	
	}
	
	public static String retData(Date data){
		SimpleDateFormat dtFormat = new SimpleDateFormat("MM-dd-yyyy");
		return "'" + dtFormat.format(data).toString() + "'";
	}
	
	public static String retDataLocal(Date data){
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
		return "'" + dtFormat.format(data).toString() + "'";
	}

	public static String retData(){
		try {
			return "'" + vlCampoTbl("LOCALTIMESTAMP as data", "", "").toString() + "'";
		} catch (Exception e) {
			throw new RuntimeException("Erro ao buscar data do banco de dados." + e.getMessage());
		}
	}
	
	public static String retHora(){
		try {
			return "'" + vlCampoTbl("CURRENT_TIMESTAMP as hora", "", "").toString() + "'";
		} catch (Exception e) {
			throw new RuntimeException("Erro ao buscar data do banco de dados." + e.getMessage());
		}
	}	
	
	public static String retHora(Date data){
		SimpleDateFormat dtFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		return "'" + dtFormat.format(data).toString() + "'";
	}
	
	public static String formataData(Date data, String formato){
		SimpleDateFormat dtFormat = new SimpleDateFormat(formato);
		return dtFormat.format(data).toString();
	}

	public static String formataData(String data, String formato){
		SimpleDateFormat dtFormat = new SimpleDateFormat(formato);
		return dtFormat.format(Funcoes.toDate(data, "yyyy-MM-dd")).toString();
	}
	
	public static String decimal(double numero, int casasDec){
		String aux = "0.";
		
		for (int i = 0; i < casasDec; i++) {
			aux += "0";
		}
		
		DecimalFormat df = new DecimalFormat(aux);  
		return df.format(numero);
	}

	public static String formata(int numero, int digitos){
		Formatter f = new Formatter();
		String formatado= "";
		formatado = f.format("%0" + digitos + "d", numero).toString();
		f.close();
		return formatado;		
	}
	
	public static String formata(long numero, int digitos) {
		Formatter f = new Formatter();
		String formatado= "";
		formatado = f.format("%0" + digitos + "d", numero).toString();
		f.close();
		return formatado;	
	}

	public static String separaNumeros(String texto){
		String numeros = "";
		
		for (int i = 0; i < texto.length(); i++) {
			if(Character.isDigit(texto.charAt(i))){
				numeros += texto.charAt(i);
			}
		}
		
		return numeros;
	}
	
	public static long proximoNumero(String campo, String tabela, String criterio){

		try{

			String sql = "select coalesce(max(" + campo + "), 0) + 1 as proximo" +
						 " from " + tabela;
			if(!criterio.equals("")){
				sql += " where " + criterio;
			}
			
			ResultSet rsTmp = Geral.dbCn.retReg(sql);
			
			if(rsTmp != null){
				rsTmp.next();
				long codigo = rsTmp.getLong("proximo");
				rsTmp.close();
				return codigo;
			}else{
				return 1;
			}
			
		}catch(Exception e){
			throw new RuntimeException("Erro ao buscar próximo código da tabela " + tabela + ". " + e.getMessage());
		}
	}
	
	public static long proximoNumeroLocal(String campo, String tabela, String criterio) throws Exception {

		try{

			String sql = "select coalesce(max(" + campo + "), 0) + 1 as proximo" +
						 " from " + tabela;
			if(!criterio.equals("")){
				sql += " where " + criterio;
			}
			
			Cursor rsTmp = Geral.localDB.rawQuery(sql, null);
			
			if(rsTmp != null){
				if(rsTmp.getCount() > 0){
					rsTmp.moveToFirst();
					long codigo = rsTmp.getInt(rsTmp.getColumnIndex("proximo"));
					rsTmp.close();
					return codigo;
				}else{
					return 1;
				}
			}else{
				return 1;
			}
			
		}catch(Exception e){
			throw new RuntimeException("Erro ao buscar próximo código da tabela " + tabela + ". " + e.getMessage());
		}
	}
	
	public static double toDouble(Object numero){
		String aux = numero.toString();
		if (aux.equals("")) aux = "0";
		aux = aux.replace(".", ",");
		DecimalFormat df = new DecimalFormat("0.0000");
		try {
			return df.parse(aux).doubleValue();
		} catch (ParseException e) {
			return 0;
		}
	}
	
	public static Integer toInt(Object numero){
		try {
			String aux = numero.toString();
			if (aux.equals("")) aux = "0";
			aux = aux.replace(".", ",");
			return Integer.parseInt(aux);
		} catch (Exception e) {
			return 0;
		}	
	}
	
	public static Long toLong(Object numero){
		try {
			if(numero == null) numero = "";
			String aux = numero.toString();
			if (aux.equals("")) aux = "0";
			aux = aux.replace(".", ",");
			return Long.parseLong(aux);
		} catch (Exception e) {
			return (long) 0;
		}	
	}

	public static Date toDate(String data){
		Date dt = new Date();
		SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			dt = dtFormat.parse(data);
			return dt;
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date toDate(String data, String formato){
		Date dt = new Date();
		SimpleDateFormat dtFormat = new SimpleDateFormat(formato);
		
		try {
			dt = dtFormat.parse(data);
			return dt;
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static boolean verificaNaturezaOperacao(String operacao, int cdNt){
		try{
			return vlCampoTbl(operacao, "PUB_NT_OP", "CD_NT=" + cdNt).toString().equals("1");
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static boolean validaInscricao(String ie, String uf, String ntJur){
		if(ie == null || ie.trim().equals("")){
			return false;
		}else{
			return true;
		}
	}
	
	public static double saldoProduto(int cdEmp, int cdUnEmp, long cdProd, byte tpSld, long cdLt, long cdRom){
		
		try{
			
			double saldo = 0;
			String campo = "";
			String tabela = "";
			String criterio = "cd_emp = " + cdEmp
					+ " and cd_pes_un_emp = " + cdUnEmp
					+ " and cd_prod = " + cdProd;
			
			if(tpSld == SALDO_R){
				tabela = "ESTQ_CP";
				campo = "QT_ESTQ_CP";
			}else{
				if(tpSld == SALDO_F){
					tabela = "ESTQ_CP";
					campo = "QT_CP";				
				}else{
					if(tpSld == SALDO_DISP){
						if(cdRom == 0){
							if(cdLt == 0){
								tabela = "ESTQ_CP";
								campo = "QT_DISP_CP";
							}else{
								tabela = "ESTQ_CP_LT";
								campo = "QT_SLD_DISP";
								criterio += " AND CD_LT = " + cdLt;
							}
						}else{
							tabela = "ESTQ_ENTG_ROM_IT";
							campo = "COALESCE(SUM(QTDE_IT - QTDE_VEND),0)";
							criterio += " AND CD_SAI = " + cdRom;
						}
					}
				}
			}
			
			saldo = Double.parseDouble(vlCampoTbl(campo, tabela, criterio).toString());
			return saldo;
			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	public static boolean atualizaEstoqueDisp(int cdEmp, int cdUnEmp, long cdProd, double qtProd, String op, long cdLt){
		
		try{
			String sinal = "";
			String tabela = "";
			String criterio = "cd_emp = " + cdEmp + " and cd_pes_un_emp = " + cdUnEmp + " and cd_prod = " + cdProd;
			
			String[] vetCampos = null;
			Object[] vetValores = null;
			
			if(op == BX_ORI_NT){
				sinal = "-";
			}else{
				if(op == ATUAL_ORI_NT){
					sinal = "+";
				}
			}
			
			if(cdLt == 0){
				vetCampos = new String[]{"qt_disp_cp"};
				vetValores = new Object[]{"qt_disp_cp " + sinal + " " + qtProd};
				tabela = "estq_cp";
				
			}else{
				vetCampos = new String[]{"qt_sld_disp"};
				vetValores = new Object[]{"qt_sld_disp " + sinal + " " + qtProd};
				tabela = "estq_cp_lt";
				criterio += " and cd_lt = " + cdLt;
				
			}
			
			return Geral.dbCn.altReg(tabela, vetCampos, vetValores, criterio, true);
			
		}catch(Exception e){
			throw new RuntimeException("Erro ao atualizar estoque." + e.getMessage());
		}

	}

	public static String between(Date dtIni, Date dtFin) {
		
		return "between " + retData(dtIni) + " and " + retData(dtFin);
	}

	public static String criaSQLUpdate(String tabela, String[] vetCampos, Object[] vetValores, String criterio) {
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
		
		return sql;
	}

	public static String criaSQLInsert(String tabela, String[] vetCampos, Object[] vetValores) {
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
		
		return sql;
	}

	private static Intent createEmailOnlyChooserIntent(Activity context, Intent source,
        CharSequence chooserTitle) {
        Stack<Intent> intents = new Stack<Intent>();
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
                "info@domain.com", null));
        List<ResolveInfo> activities = context.getPackageManager()
                .queryIntentActivities(i, 0);

        for(ResolveInfo ri : activities) {
            Intent target = new Intent(source);
            target.setPackage(ri.activityInfo.packageName);
            intents.add(target);
        }

        if(!intents.isEmpty()) {
            Intent chooserIntent = Intent.createChooser(intents.remove(0),
                    chooserTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    intents.toArray(new Parcelable[intents.size()]));

            return chooserIntent;
        } else {
            return Intent.createChooser(source, chooserTitle);
        }
    }
	
	public static void enviaPorEmail(Activity context, String assunto, String mensagem, ArrayList<Uri> arquivos) {
		Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
        i.setType("text/plain");
        i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arquivos);
        i.putExtra(Intent.EXTRA_SUBJECT, assunto);
        i.putExtra(Intent.EXTRA_TEXT, mensagem);

        context.startActivityForResult(Funcoes.createEmailOnlyChooserIntent(context, i, "Enviar por e-mail"), ENVIAR_EMAIL);
	}
	
	public static void compartilhaArquivo(Activity context, Uri arquivo) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.putExtra(Intent.EXTRA_STREAM, arquivo);
		i.setType("*/*");
		context.startActivityForResult(Intent.createChooser(i, "Compartilhar"), COMPARTILHAR_ARQUIVO);
	}
}
