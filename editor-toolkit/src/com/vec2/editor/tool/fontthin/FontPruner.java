package com.vec2.editor.tool.fontthin;

import java.util.HashMap;

public class FontPruner {

static String inputDir = "D:\\tmp\\test\\json,D:\\tmp\\test\\json2";
	static String inputFont = "D:\\tmp\\test\\test.ttf";
	static String outputFont = "D:\\tmp\\test\\out";
	static String [] _args = new String[] {"inputPaths="+inputDir,"inputFonts="+inputFont,"outputPath="+outputFont};
	
	public static void main(String[] args) {	
		HashMap<String, String> cmds = new HashMap<String, String>();
		int j = args.length;
		for (int i = 0; i < j; i++) {
			String cmd = args[i];
			if("-h".equals(cmd) || "--help".equals(cmd) || "help".equals(cmd)) {
				System.out.println(FontThinCommand.help);
			}
			try {
				String[] sp = cmd.split("=");
				String key = sp[0].trim();
				String val = sp[1].trim();
				cmds.put(key, val);
			} catch (Exception localException) {
			}
		}
		
		if(args.length > 0) {
			FontThinCommand.main(args);	
		}else {
			System.out.println(FontThinCommand.help);
			FontThin.main(args);	
		}
	}
}
