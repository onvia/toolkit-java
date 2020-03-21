package com.vec2.editor.utils;

public class ZN {
	public static class Message {
		public static final String data_save = "数据信息已保存";
		public static final String config_save = "参数信息已保存";
	}

	public static class Window {
		public static final String title = "Psd 图层信息工具";
		public static final String data_manage = "数据编辑工具";
		public static final String clean_same_char = "清除相同字符串";
		public static final String untexturepacker = "合图分解器";
		public static final String font_thin = "字体瘦身";
		
	}

	public static class Menu {
		public static final String system = "系统";
		public static final String open_param = "打开 INI 文件夹";
		public static final String open_folder = "打开文件夹";
		public static final String open_file = "打开文件";
		public static final String exit = "退出";
		
		public static final String refresh = "刷新 ";
		public static final String update_param_file = "刷新参数信息 ";
		//
		public static final String export = "导出";
		public static final String pack = "打包";
		public static final String pack_config = "打包参数设定";
		public static final String about = "关于";
		public static final String help = "帮助";
		public static final String source_code = "源代码地址";

		public static final String close_all = "关闭 全部 页面";
		public static final String close_others = "关闭 其他 页面";
		public static final String close_this = "关闭页面";
		//
		public static final String reset_element = "设置元素ID";
		public static final String del_element = "删除元素";

		public static final String game_data_edit = "游戏数据编辑";
		public static final String clean_same_char = "文本去重";
		public static final String untexturepacker = "合图切分";
		public static final String font_thin = "字体瘦身";
	}

	public static class Label {
		public static final String param_file_tree = "INI 文件列表";
		public static final String param_file_count = "文件数量";
		public static final String psd_layer_tree = "数据图层结构";
		public static final String layer_param_table = "数据参数列表";
		//
		public static final String element_tree = "数据列表面板";
		public static final String element_collections = "资源";
		public static final String element_class = "类对象";
		public static final String element_resource = "资源对象";
		public static final String field_tree = "参数面板";

		public static final String class_name = "类名";
		public static final String field_value = "参数值";
		
		public static final String file_ttf = "字体文件";
		public static final String file_txt = "文本文件";
		public static final String preview = "预览";
		public static final String file_type = "文件类型";
		
		
		//
		public static final String notice = "注意 : 删除元素不能恢复";
				
	}

	public static class Dialog {
		public static final String yes = "是";
		public static final String no = "否";
		public static final String prompt = "提示";
		public static final String warning = "警告";
		public static final String delete_psd_file = "确定要删除文件，数据文件不能恢复";
		public static final String delete_param_file = "删除选中的 参数设定 文件?";
		public static final String delete_layer_param = "确定要删除 选中的图层参数?";
		//
		public static final String frame_config = "参数设定面板";
		//
		public static final String reset_element = "设置元素ID";
		public static final String del_class_element = "删除元素";
		
		//
		public static final String must_is = "必须是 {0} 文件";
	}

	public static class Error {
		public static final String parse_psd_file_failed = "解析 PSD 文件失败";
		public static final String parse_ini_file_failed = "解析 ini 文件失败";
		public static final String init_data_failed = "初始化数据失败";
		public static final String ttf_txt_paths_error = "任务失败，文本文件或字体文件为空";
		public static final String export_paths_error = "任务失败，输出路径为空";
	}

	public static class Text {
		public static final String layer_id = "图层ID";
		public static final String param_key = "键";
		public static final String param_val = "值";
		public static final String layer_name = "图层名称";
		//
		public static final String add_param = "添加事件";
		public static final String input_param_value = "输入事件的值";
		//
		public static final String used_clean_folder = "清除目录缓存";
		public static final String pack_folder = "导出目录";
		public static final String select_folder = "选择目录";
		public static final String used_libgdx_coordinate = "使用 libgdx 的坐标系";
		public static final String used_texture_packer = "使用 TexturePacker 打包图片";
		public static final String used_android_assets_name = "使用 Android Assets 名称规范";
		public static final String copy_to_clipboard = "拷贝到剪切板";
		public static final String drag_file = "拖拽 {0} 文件到面板";
		public static final String choose_file = "选择文本文件";
		public static final String dispose = "处理";
		public static final String select = "选择";
		public static final String only_preview_txt = "只能预览文本文件";
		public static final String ignore_type = "忽略样式";
		public static final String unrealized = "未实现";
		
		//
		public static final String help = "使用帮助" + "\n导入PSD文件的方法 " + "\n拖拽PSD文件 或者 文件夹 , 到  [PSD文件列表] (左上)面板"
				+ "\n导入INI文件的方法 \n拖拽INI文件 或者 文件夹 , 到  [INI文件列表] (左下)面板" + "\n\nINI 的参数设定方式 有两种"
				+ "\nA : key=val  (表现为  文本编辑方式 , 输入字符串)"
				+ "\nB : key=[v1,v2,v3] (表现为   使用选择方式 , 选择数值 , 这个数值不能自定义或者修改)" + "\n\nINI 参数 只可以设定到 图层上 "
				+ "\n添加方式 : 在[数据图层结构] (右上) 面板  , 单选节点 , 右键->添加事件->选择属性->编辑属性数值"
				+ "\n删除方式 : 在[数据参数列表] (右下) 面板 , 单选, 或者多选 , 右键 ->删除图层参数"
				+ "\n修改方式 : 在[数据参数列表] (右下) 面板 , 双击 '值' 列 , 修改参数" + "\n\n导出数据" + "\n菜单 导出 -> 打包";

		//
		public static final String input_element_id = "输入新的元素ID";
	}

	public static void main(String[] args) {
		try {
			PropertiesUtil.save(ZN.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

