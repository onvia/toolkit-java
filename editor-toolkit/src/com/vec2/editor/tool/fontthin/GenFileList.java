package com.vec2.editor.tool.fontthin;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.vec2.editor.utils.FileUtils;

public class GenFileList {
	
	public static void  WriteStr2File(String str,File file,String encode){
		try {
			FileOutputStream writerStream = new FileOutputStream(file);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream,encode));
			writer.write(str);
			writer.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String listFile (ArrayList<String> txtPaths){
		String result="" ;
		for (String string : txtPaths) {
			result+=string+"\r\n";
			System.out.println(string);
		}
		System.out.println("----------------------");
		System.out.println(result);
		return result;
	}
	
	public static void gen(ArrayList<String> txtPaths,String outputPath) {
		System.out.println("outputPath:"+outputPath);
		File file = new File(outputPath);
		
		try {
			FileUtils.save(file, listFile(txtPaths));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		WriteStr2File(listFile(txtPaths),new File(outputPath),"UTF-8");
	}
	
}
