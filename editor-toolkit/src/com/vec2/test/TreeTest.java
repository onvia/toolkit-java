package com.vec2.test;

import java.awt.BorderLayout;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.vec2.swing.utils.SwingUtils;
import com.vec2.swing.utils.SwingUtils.DropInAdapter;

public class TreeTest {
	public static void main(String[] args) {
		SwingUtils.initWindowsLookAndFeel();
		JFrame jf = new JFrame("测试窗口");
		jf.setSize(300, 300);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new BorderLayout());

		// 创建根节点
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("中国");

		// 创建二级节点
		DefaultMutableTreeNode gdNode = new DefaultMutableTreeNode("广东");
		DefaultMutableTreeNode fjNode = new DefaultMutableTreeNode("福建");
		DefaultMutableTreeNode shNode = new DefaultMutableTreeNode("上海");
		DefaultMutableTreeNode twNode = new DefaultMutableTreeNode("台湾");

		// 把二级节点作为子节点添加到根节点
		rootNode.add(gdNode);
		rootNode.add(fjNode);
		rootNode.add(shNode);
		rootNode.add(twNode);

		// 创建三级节点
		DefaultMutableTreeNode gzNode = new DefaultMutableTreeNode("广州");
		DefaultMutableTreeNode szNode = new DefaultMutableTreeNode("深圳");

		DefaultMutableTreeNode fzNode = new DefaultMutableTreeNode("福州");
		DefaultMutableTreeNode xmNode = new DefaultMutableTreeNode("厦门");

		DefaultMutableTreeNode tbNode = new DefaultMutableTreeNode("台北");
		DefaultMutableTreeNode gxNode = new DefaultMutableTreeNode("高雄");
		DefaultMutableTreeNode jlNode = new DefaultMutableTreeNode("基隆");

		// 把三级节点作为子节点添加到相应的二级节点
		gdNode.add(gzNode);
		gdNode.add(szNode);

		fjNode.add(fzNode);
		fjNode.add(xmNode);

		twNode.add(tbNode);
		twNode.add(gxNode);
		twNode.add(jlNode);

		// 使用根节点创建树组件
		JTree tree = new JTree(rootNode);

		// 设置树显示根节点句柄
		tree.setShowsRootHandles(true);

		// 设置树节点可编辑
		tree.setEditable(true);

		// 设置节点选中监听器
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				System.out.println("当前被选中的节点: " + e.getPath());
			}
		});
		DragSource dragSource = DragSource.getDefaultDragSource();
		SwingUtils.addDropIn(tree, new DropInAdapter() {
			@Override
			public void onDropIn(File file) {

			}
		});
		// 创建滚动面板，包裹树（因为树节点展开后可能需要很大的空间来显示，所以需要用一个滚动面板来包裹）
		JScrollPane scrollPane = new JScrollPane(tree);

		// 添加滚动面板到那内容面板
		panel.add(scrollPane, BorderLayout.CENTER);

		// 设置窗口内容面板并显示
		jf.setContentPane(panel);
		jf.setVisible(true);
	}

}
