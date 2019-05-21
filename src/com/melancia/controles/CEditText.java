package com.melancia.controles;

import java.util.Date;

import com.melancia.Funcoes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

public class CEditText extends EditText {
	
	private long cdAnterior = 0;
	private boolean flagFocus = false;
	private String mask = "";
	
	public CEditText(Context context) {
		super(context);
	}

	public CEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void formata(int digitos){
		super.setText(Funcoes.formata(Funcoes.toLong(super.getText().toString()), digitos));
	}
	
	public void decimal(int casas){
		super.setText(Funcoes.decimal(Funcoes.toDouble(this.getString()), casas));
	}
	
	public int getInt(){
		return Funcoes.toInt(super.getText().toString());
	}
	
	public long getLong(){
		return Funcoes.toLong(super.getText().toString());
	}
	
	public double getDouble(){
		return Funcoes.toDouble(super.getText().toString());
	}
	
	public String getString(){
		return super.getText().toString();
	}

	public Date getDate(){
		try{
			return Funcoes.toDate(this.getString());
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public long getCdAnterior() {
		return cdAnterior;
	}

	public void setCdAnterior(long cdAnterior) {
		this.cdAnterior = cdAnterior;
	}
	
	public boolean getFlagFocus() {
		return flagFocus;
	}

	public void setFlagFocus(boolean flagFocus) {
		this.flagFocus = flagFocus;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(final String mask) {
		this.mask = mask;
		
		if (!mask.equals("")){
			super.addTextChangedListener(new TextWatcher() {
				boolean isUpdating;
				String old = "";
				
				@SuppressLint("DefaultLocale")
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					String str = s.toString().toUpperCase().replaceAll("[^A-Z0-9]", "");
					String mascara = "";
					if (isUpdating) {
						old = str;
						isUpdating = false;
						return;
					}
					int i = 0;
					for (char m : mask.toCharArray()) {
						if (m != 'A' && m != '9' && str.length() > old.length()) {
							mascara += m;
							continue;
						}
						try {
							mascara += str.charAt(i);
						} catch (Exception e) {
							break;
						}
						i++;
					}
					isUpdating = true;
					setText(mascara);
					setSelection(mascara.length());
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				
				@Override
				public void afterTextChanged(Editable s) {}
			});
		}
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		if(focused){
			this.cdAnterior = Funcoes.toLong(super.getText().toString());
			flagFocus = false;
		}else{
			if(!flagFocus)
				super.onEditorAction(EditorInfo.IME_ACTION_DONE);
		}
		
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}
	
	@SuppressLint("DefaultLocale")
	@Override
	public void onEditorAction(int actionCode) {
		flagFocus = true;
		
		if(actionCode == EditorInfo.IME_ACTION_DONE || actionCode == EditorInfo.IME_ACTION_NEXT){
			if (getMask().equals("")) {
				super.setText(getString().trim());
				if(super.getInputType() == InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS + InputType.TYPE_CLASS_TEXT){
					super.setText(getString().toUpperCase());
				}
			}
		}
		
		super.onEditorAction(actionCode);
	}

}
