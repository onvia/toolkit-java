package com.vec2.editor.tool.fontthin.fontextract;
import java.io.File;
import java.util.List;

public class FontExtract {
	public static void exec(String fileListString,String outputPath) {

		File file = new File(fileListString);
		if(!file.exists()){
			System.out.println("文本路径列表不存在"+file.toString());
			System.exit(1);
		}
		if(!file.isFile()){
			System.out.println("文本路径列表不是文件"+file.toString());
			System.exit(1);
		}
		
		
		StringBuffer strBuf = new StringBuffer();
		List<String> filePathList =ExtractionOperationUtil.ExtractStringListFromFile(file);
		for(String filePath :filePathList)
		{
			File f = new File(filePath);	
			if(!f.exists() ||!f.isFile()){
				System.out.println("文本不存在或者无效"+f.toString());
				continue;
			}
			strBuf.append(ExtractionOperationUtil.ExtractStringFromFile(f));
		}
		
		File outputFile = new File(outputPath);
		if(!outputFile.exists()){
			outputFile.mkdirs();
		}
		if(outputFile.isFile()){
			System.out.println("路径是文件"+outputFile.toString());
			System.exit(1);
		}
		System.out.println("输出路径"+outputPath);
		ExtractResult result =ExtractionOperationUtil.ExtractUnrepeatedWordsFromString(strBuf.toString());
		ExtractionOperationUtil.WriteStr2File(result.chineseCharString,new File(outputPath+"\\ChineseOutPut.txt"),"UTF-8");
		ExtractionOperationUtil.WriteStr2File(result.unChineseCharString,new File(outputPath+"\\unChineseOutPut.txt"),"UTF-8");
		System.out.println("提取完成");
	}
}



