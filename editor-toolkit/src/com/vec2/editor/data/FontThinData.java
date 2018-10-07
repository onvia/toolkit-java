package com.vec2.editor.data;

import java.io.File;
import java.util.ArrayList;


public class FontThinData {

	ArrayList<String> txtPaths = new ArrayList<String>();
	ArrayList<String> ttfPaths = new ArrayList<String>();
	String exportPath;
	
	public FontThinData() {
		
	}

	public void addTtfFile(File file) {
		if(ttfPaths.contains(file.getAbsolutePath()) == false) {
			ttfPaths.add(file.getAbsolutePath());
		}
	}
	public void addTxtFile(File file) {
		if(txtPaths.contains(file.getAbsolutePath()) == false) {
			txtPaths.add(file.getAbsolutePath());
		}
	}
	public void setExportPath(String exportPath) {
		this.exportPath = exportPath;
	}
	
	public ArrayList<String> getTxtPaths() {
		return txtPaths;
	}
	public ArrayList<String> getTtfPaths() {
		return ttfPaths;
	}
	public String getExportPath() {
		return exportPath;
	}
}
