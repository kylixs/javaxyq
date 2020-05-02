package com.javaxyq.tools;

import javax.swing.*;

import com.javaxyq.util.HashUtil;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourcePathMatchTool extends JFrame {
    private JPanel contentPane;
    private JButton buttonStart;
    private JButton buttonClose;
    private JTextArea pathRuleTextArea;
    private JTextField filenameTextField;
    private JButton browserButton;
    private JTextArea resultTextArea;
    private JButton buttonStop;
    private String lastDirectoryPath;
    private WdfFile wdfFile;
    private JFileChooser chooser;

    public ResourcePathMatchTool() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonStart);

        buttonStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onStart();
            }
        });

        buttonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onClose();
            }
        });

// call onClose() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

// call onClose() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                onClose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        browserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBrowserFile();
            }
        });
        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStop();
            }
        });
    }

    private void onBrowserFile() {
        if (chooser == null) {
            chooser = new JFileChooser(lastDirectoryPath);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }
        int rtv = chooser.showOpenDialog(top());
        if (rtv == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            filenameTextField.setText(file.getPath());
        }
    }

    private ResourcePathMatchTool top() {
        return ResourcePathMatchTool.this;
    }

    private void onStart() {
        //计算路径规则
        String pathRule = pathRuleTextArea.getText().trim();
        if (pathRule.isEmpty()) {
            JOptionPane.showMessageDialog(top(), "请输入正确的路径规则！");
            return;
        }
        System.out.println("path rule: " + pathRule);

        List<String> templateFragments = new ArrayList<String>();
        List<RuleUnit> ruleUnits = new ArrayList<RuleUnit>();
        String ruleEx = "\\{[^\\{\\}]*\\}";
        Pattern pattern = Pattern.compile(ruleEx);
        Matcher matcher = pattern.matcher(pathRule);
        int lastPos = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            templateFragments.add(pathRule.substring(lastPos, start));
            lastPos = end;

            String express = matcher.group();
            RuleUnit ruleUnit = parseUnit(express);
            if (ruleUnit != null) {
                ruleUnits.add(ruleUnit);
                System.out.println("parse: " + express + " => " + ruleUnit);
            } else {
                System.err.println("解析表达式失败：" + express);
                break;
            }
        }
        templateFragments.add(pathRule.substring(lastPos));

        //打开wdf文件
        String filename = filenameTextField.getText().trim();
        if (filename.isEmpty()) {
            JOptionPane.showMessageDialog(top(), "请选择资源文件！");
            return;
        }
        File file = new File(filename);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(top(), "资源文件不存在！");
            return;
        }
        System.out.println("wdf: "+filename);
        try {
            wdfFile = new WdfFile(filename, false);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(top(), e.getMessage());
            return;
        }


        //展开路径规则匹配
        Map<String, Long> nodeMap = new TreeMap<String, Long>();
        int[] ruleExpands = new int[ruleUnits.size()];
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < templateFragments.size(); i++) {
                sb.append(templateFragments.get(i));
                if (i < ruleUnits.size()) {
                    sb.append(ruleUnits.get(i).values().get(ruleExpands[i]));
                }
            }
            String path = sb.toString();
            long nodeId = HashUtil.stringToId(path);
            WdfFileNode node = wdfFile.findNode(nodeId);
            if (node != null) {
            	nodeMap.put(path, node.getId());
                System.out.println(Long.toHexString(nodeId)+"=" + path);
            } else {
                //System.out.println("not found: " + path);
            }
        } while (expandNext(ruleUnits, ruleExpands));

        System.out.println("match finished, total found: "+nodeMap.size());
        
        try {
			wdfFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private boolean expandNext(List<RuleUnit> ruleUnits, int[] ruleExpands) {
        for (int i = ruleUnits.size() - 1; i >= 0; i--) {
            if (ruleExpands[i] == ruleUnits.get(i).size() - 1) {
                ruleExpands[i] = 0;
            } else {
                ruleExpands[i]++;
                //System.out.println("expand: " + Arrays.toString(ruleExpands));
                return true;
            }
        }
        //System.out.println("finish expand: " + Arrays.toString(ruleExpands));
        return false;
    }

    private RuleUnit parseUnit(String express) {
        RuleUnit ruleUnit = null;
        ruleUnit = NumberRuleUnit.parse(express);
        if (ruleUnit == null) {
            ruleUnit = EnumRuleUnit.parse(express);
        }
        return ruleUnit;
    }

    private void onStop() {
    }

    private void onClose() {
// add your code here if necessary
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        ResourcePathMatchTool dialog = new ResourcePathMatchTool();
        dialog.setPreferredSize(new Dimension(800, 600));
        dialog.pack();
        dialog.setTitle("资源路径匹配工具");
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonStart = new JButton();
        buttonStart.setText("开始");
        panel2.add(buttonStart, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonClose = new JButton();
        buttonClose.setText("关闭");
        panel2.add(buttonClose, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonStop = new JButton();
        buttonStop.setText("停止");
        panel2.add(buttonStop, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(20, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("路径规则：");
        panel3.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pathRuleTextArea = new JTextArea();
        pathRuleTextArea.setText("char\\0010\\{56-60}\\{stand,walk,attack,defend,die,guard,hit,magic,rusha,rushb}.tcp");
        panel3.add(pathRuleTextArea, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("资源文件：");
        panel3.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        filenameTextField = new JTextField();
        filenameTextField.setText("G:\\Games\\梦幻西游2012\\shape.wd3");
        panel3.add(filenameTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        browserButton = new JButton();
        browserButton.setText("浏览");
        panel3.add(browserButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("匹配结果：");
        panel3.add(label3, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resultTextArea = new JTextArea();
        resultTextArea.setText("");
        panel3.add(resultTextArea, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 2, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}


interface RuleUnit {
	
	void compile();
	int size();
	List<String> values();
}

/**
 * 数字表达式：{00-80}
 * @param exp
 */
class NumberRuleUnit implements RuleUnit {
	private int min;
	private int max;
	private int width;
	private List<String> values;
	private static Pattern pattern = Pattern.compile("\\{(\\d+)\\-(\\d+)\\}");
	
	public NumberRuleUnit(int min, int max, int width) {
		super();
		this.min = min;
		this.max = max;
		this.width = width;
		values = new ArrayList<String>();
	}
	
	/**
	 * 数字表达式：{00-80}
	 * @param exp
	 */
	public static NumberRuleUnit parse(String exp) {
		Matcher matcher = pattern.matcher(exp);
		if(matcher.matches()) {
			String smin = matcher.group(1);
			String smax = matcher.group(2);
			
			int width = smin.length();
			int min = Integer.parseInt(smin);
			int max = Integer.parseInt(smax);
			NumberRuleUnit ruleUnit = new NumberRuleUnit(min, max, width);
			ruleUnit.compile();
			return ruleUnit;
		}
		return null;
	}

	@Override
	public void compile() {
		String fmt = "%0"+width+"d";
		for(int i=min;i<=max;i++) {
			values.add(String.format(fmt, i));
		}
	}
	
	@Override
	public int size() {
		return values.size();
	}
	
	@Override
	public List<String> values() {
		return values;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NumberRuleUnit [min=");
		builder.append(min);
		builder.append(", max=");
		builder.append(max);
		builder.append(", width=");
		builder.append(width);
		builder.append("]");
		return builder.toString();
	}
	
}

class EnumRuleUnit implements RuleUnit {
	private List<String> values;
	private static Pattern pattern = Pattern.compile("\\{(.+\\,.+)\\}");
	
	public EnumRuleUnit(List<String> values) {
		this.values = values;
	}
	
	public static EnumRuleUnit parse(String rule) {
		Matcher matcher = pattern.matcher(rule);
		if(matcher.matches()) {
			String all = matcher.group(1);
			String[] strs = all.split(",");
//			List<String> values = new ArrayList<String>(strs.length);
//			for (int i = 0; i < strs.length; i++) {
//				values.add(strs[i]);
//			}
			List<String> values = Arrays.asList(strs);
			return new EnumRuleUnit(values);
		}
		return null;
	}

	@Override
	public void compile() {
		//nothing to do
	}
	
	@Override
	public int size() {
		return values.size();
	}
	@Override
	public List<String> values() {
		return values;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EnumRuleUnit [values=");
		builder.append(values);
		builder.append("]");
		return builder.toString();
	}
}

