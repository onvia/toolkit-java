package com.vec2.editor.tool.fontthin;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.vec2.editor.data.EditorData;
import com.vec2.editor.utils.FileUtils;
import com.vec2.editor.utils.Language;
import com.vec2.editor.utils.MessageKey;
import com.vec2.editor.utils.Messager;
import com.vec2.editor.widgets.DefaultTreeNode;
import com.vec2.swing.utils.SwingUtils;
import com.vec2.swing.utils.SwingUtils.DropInAdapter;


@SuppressWarnings("serial")
public class FontThinTxtFileTree  extends JPanel {

	JTree mTree;
	public FontThinTxtFileTree() {
		mTree = new JTree();
		setLayout(new BorderLayout(0, 0));
		
		add(mTree,BorderLayout.CENTER);
		updateTree();
		initListener();
		initPopupMenu();
		initDropIn();
	}
	
	private void initListener() {
		// 设置节点选中监听器
		mTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				System.out.println("当前被选中的节点: " + e.getPath());
				TreePath treePath = e.getPath();
				DefaultTreeNode treeModel = (DefaultTreeNode) treePath
						.getLastPathComponent();
				String filepath = (String) treeModel.getUserObject();
				File file = new File(filepath);
				if(verifyTXT(file)) {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							String fileContent = FileUtils.read(new File(filepath));
							if(fileContent != null) {
								System.out.println("内容：\n"+fileContent);	
								if(fileContent == "" || fileContent == null || fileContent.equals("")) {
									fileContent = "[nothing]";
								}
								Messager.send(fileContent, MessageKey.PREVIEW);
							}							
						}
					}).start();
				}else {
					Messager.send(Language.get("text.only_preview_txt"), MessageKey.PREVIEW);
				}
			
			}
		});
	}
	
	private void initDropIn() {
		SwingUtils.addDropIn(this, new DropInAdapter() {
			@Override
			public void onDropIn(List<File> files) {
				FontThinTxtFileTree.this.onDropIn(files);				
			}
		});
	}
	public void onDropIn(List<File> files) {
		for (File file : files) {
			onDropIn(file);
		}
		updateTree();
		EditorData.save();
	}
	public void onDropIn(File file) {
		if(file.isDirectory()) {
			File [] files = file.listFiles();
			for (File file2 : files) {
				this.onDropIn(file2);
			}
		}else {
			if(FileUtils.is(file,"ttf")) {
				EditorData.fontThinData.addTtfFile(file);	
			}else if(verifyTXT(file)) {
				EditorData.fontThinData.addTxtFile(file);	
			}	
		}
	}
	
	private boolean ignoreFile(File file) {
		if(file == null)return false;

		String [] files = {"o","libs"};
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
	private boolean verifyTXT(File file) {
		if(file == null)return false;
		String [] files = {"txt","java","ini","py","md","ts","js","cs","json","plist","fnt","prefab","fire","h","cpp"};
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
	
	
	private void initPopupMenu() {
		SwingUtils.addPopup(mTree, new SwingUtils.PopmenuListener() {
			@Override
			public void onInitPopmenu(JPopupMenu popupMenu) {
				if(mTree.getSelectionPath() != null) {
					System.out.println("当前被选中的节点: " + mTree.getSelectionPath());
					SwingUtils.addPopup(popupMenu, "menu.open_file", new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							TreePath treePath = mTree.getSelectionPath();
							DefaultTreeNode treeModel = (DefaultTreeNode) treePath
									.getLastPathComponent();
							String path = (String) treeModel.getUserObject();
							File file = new File(path);
							if(verifyTXT(file)) {
								try {
									FileUtils.openFile(file);
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							}
						}
					});
					SwingUtils.addPopup(popupMenu, "menu.open_folder", new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

							TreePath treePath = mTree.getSelectionPath();
							DefaultTreeNode treeModel = (DefaultTreeNode) treePath
									.getLastPathComponent();
							String path = (String) treeModel.getUserObject();
							File file = new File(path);
							File parent = file.getParentFile();
							if(parent.isDirectory()) {
								try {
									FileUtils.openFile(parent);
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							}
						
						}
					});
					// 删除
					SwingUtils.addPopup(popupMenu, "menu.del_element", new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							TreePath[] treePaths = mTree.getSelectionPaths();
							for (TreePath treePath : treePaths) {
								DefaultTreeNode treeModel = (DefaultTreeNode) treePath
										.getLastPathComponent();
								System.out.println("delete-> "+treeModel.getUserObject());
								EditorData.fontThinData.getTxtPaths().remove(treeModel.getUserObject());
								EditorData.fontThinData.getTtfPaths().remove(treeModel.getUserObject());
							}
							EditorData.save();
							updateTree();
						}
					});
				
				}
			}
		});
			
	}
	
	private void updateTree() {
		DefaultTreeNode ttfTreeModel = new DefaultTreeNode(Language.get("label.file_ttf"));
		DefaultTreeNode txtTreeModel = new DefaultTreeNode(Language.get("label.file_txt"));
		
		for (String path : EditorData.fontThinData.getTtfPaths()) {
			ttfTreeModel.add(new DefaultTreeNode(path));
		}
		for (String path : EditorData.fontThinData.getTxtPaths()) {
			txtTreeModel.add(new DefaultTreeNode(path));
		}
		DefaultTreeNode resTreeModel = new DefaultTreeNode(Language.get("label.element_collections"));
		resTreeModel.add(ttfTreeModel);
		resTreeModel.add(txtTreeModel);
		
		mTree.setModel(new DefaultTreeModel(resTreeModel));
		// 展开树结构
		TreePath treePath = new TreePath(resTreeModel);
		this.mTree.expandPath(treePath);
		for (int i = 0; i < resTreeModel.getChildCount(); i++) {
			TreePath cTreePath = treePath.pathByAddingChild(resTreeModel.getChildAt(i));
			this.mTree.expandPath(cTreePath);
		}
	}
	
	
	
	
	
	
}
