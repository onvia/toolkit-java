package com.vec2.editor.tool.fontthin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.typography.font.tools.sfnttool.SfntTool;
import com.vec2.editor.tool.fontthin.fontextract.ExtractionOperationUtil;
import com.vec2.editor.tool.fontthin.fontextract.FontExtract;
import com.vec2.editor.utils.FileUtils;
public class FontThinCommand {

	enum ListFileType{
		TEXT,
		TTF,
	}
	static String help = "Usage:\n java -jar FontPruner.jar inputPaths=[paths...] inputFonts=[ttfs...] outputPath=<output>";
	
	private static final String InputTxtFilelist = "input_txt_filelist.txt";
	private static final String InputTtfFilelist = "input_ttf_filelist.txt";
	private static final String IntermediateFolder = "intermediate";
	private static final String OutputFolder = "outputFont";
	private static final String ChineseOutPut = "ChineseOutPut.txt";
	private static final String UnChineseOutPut = "unChineseOutPut.txt";

	public static String result="" ;

	public static void main(String[] args) {
		HashMap<String, String> cmds = new HashMap<String, String>();
		String[] arrayOfString1 = args;
		int j = args.length;
		if(j < 3) {
			System.out.println(help);
			return;
		}
		for (int i = 0; i < j; i++) {
			String cmd = arrayOfString1[i];
			try {
				String[] sp = cmd.split("=");
				String key = sp[0].trim();
				String val = sp[1].trim();
				cmds.put(key, val);
			} catch (Exception localException) {
			}
		}
			
			System.out.println("命令行");
			String input = cmds.get("inputPaths");
			String font = cmds.get("inputFonts");
			String output = cmds.get("outputPath");
			
			String [] inputPaths = input.split(",");
			String [] inputFonts = font.split(",");
			
			exec(inputPaths,inputFonts,output);			 		
	}
	private static boolean ignoreFile(File file) {
		if(file == null)return false;

		String [] files = {"o","libs","ttf"};
		String name = file.getName();
		String [] fullname = name.split("\\.");
		if(fullname.length > 1) {
			String ext = fullname[fullname.length - 1];
			for (String string : files) {
				if(ext.equalsIgnoreCase(string)) {
					return true;
				}
			}
		}
		return false;
	}

	public static String listFile (ArrayList<String> txtPaths,ListFileType listFileType){
		for (String string : txtPaths) {
			listFile(new File(string),listFileType);
		}
		return result;
	}
	public static String listFile (File f,ListFileType listFileType){
		if(f.isFile()){
			boolean ignore = false;
			if(listFileType == ListFileType.TEXT) {
				ignore = ignoreFile(f);
			}else {
				
			}
			if(!ignore)
			{
				result+=f.getPath()+"\r\n";
				System.out.println(f.getPath());
			}
		}else{
			if(f.isDirectory()){
				for(File file :f.listFiles()){
					listFile(file,listFileType);
				}
			}
		}
		return result;
	}
	private static void genFileList(ArrayList<String> txtPaths,String outputPath,ListFileType listFileType) {
		System.out.println("outputPath:"+outputPath);
		File file = new File(outputPath);
		result = "";
		try {
			FileUtils.save(file, listFile(txtPaths,listFileType));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void exec(String[] inputPaths,String[] inputFonts,String outPath) {
		
		if(outPath == null || outPath.equals("") || outPath == "") {
			System.out.println("输出路径不能为空");
			return;
		}
		
		String input_txt_file = outPath+"/"+InputTxtFilelist;
		String input_ttf_file = outPath+"/"+InputTtfFilelist;
		
		genFileList(new ArrayList<String>(Arrays.asList(inputPaths)),input_txt_file,ListFileType.TEXT);
		genFileList(new ArrayList<String>(Arrays.asList(inputFonts)),input_ttf_file,ListFileType.TTF);
		
		String extractFileStringOutput = outPath+"/"+IntermediateFolder;
		FontExtract.exec(input_txt_file, extractFileStringOutput);
		
		String chineseoutput = extractFileStringOutput+"/"+ChineseOutPut;
		String unchineseoutput = extractFileStringOutput+"/"+UnChineseOutPut;
		String outputfontfolder = outPath+"/"+OutputFolder;
		
		FileUtils.mkdirs(outputfontfolder);
		
		//ttf 文件
		File file = new File(input_ttf_file);
		List<String> filePathList =ExtractionOperationUtil.ExtractStringListFromFile(file);
		for(String filePath :filePathList)
		{
			System.out.println(filePath);
			File f = new File(filePath);
			if(!f.exists() ||!f.isFile()){
				System.out.println("TTF不存在或者无效"+f.toString());
				continue;
			}
			
			try {
				String outputfont = outputfontfolder+"/"+f.getName();
				
				SfntTool.main(new String[]{"-c",chineseoutput,unchineseoutput,filePath,outputfont});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
