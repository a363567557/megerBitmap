package cn.cn.testPic.ui;

import cn.cn.testPic.utils.MergeBitmap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * 窗口
 */
public class WindownSwing {
    /**
     * 图层合并工具
     */
    private MergeBitmap mergeBitmap;

    /**
     * 需要合并的文件目录
     */
    private String imgPathDir;

    /**
     * 蒙层图片路径
     */
    private String markImgPath;

    //ui

    private JLabel labelSelectdDir;
    private JLabel labelMaskPath;
    private JLabel labelProgressResult;

    /**
     * 选择合成图片的路径按钮
     */
    private JButton btnSelectdDir;

    /**
     * 选择合成图片的蒙层按钮
     */
    private JButton btnSelectdMaskImg;

    /**
     * 开始合成按钮
     */
    private JButton btnMaskStart;

    /**
     * 开启时间
     */
    private long startTime = 0;

    /**
     * 当前时间
     */
    private long handleTime = 0;

    public void createAndShowGUI() {
        // 确保一个漂亮的外观风格

        JFrame.setDefaultLookAndFeelDecorated(true);

        // 创建及设置窗口
        JFrame frame = new JFrame("图层合并工具");
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(null);


        btnSelectdDir = buildJButton("选择文件夹", 50, 50, 100, 50);
        btnSelectdDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fd = new JFileChooser();
                fd.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int ret = fd.showOpenDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File f = fd.getSelectedFile();
                    if (f != null) {
                        System.out.println(imgPathDir = f.getAbsolutePath());
                        labelSelectdDir.setText("文件夹路径:" + imgPathDir);
                    }
                }
            }
        });

        contentPane.add(btnSelectdDir);

        labelSelectdDir = buildJLabel("文件夹路径", 50, 100, 500, 30);

        contentPane.add(labelSelectdDir);


        btnSelectdMaskImg = buildJButton("选择蒙层图片", 50, 150, 100, 50);
        btnSelectdMaskImg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("点击了！！");
                JFileChooser fd = new JFileChooser();
                //fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int ret = fd.showOpenDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File f = fd.getSelectedFile();
                    if (f != null) {
                        System.out.println(markImgPath = f.getAbsolutePath());
                        labelMaskPath.setText("蒙层图片路径:" + markImgPath);

                    }
                }
            }
        });

        contentPane.add(btnSelectdMaskImg);


        labelMaskPath = buildJLabel("蒙层图片路径", 50, 200, 500, 30);

        contentPane.add(labelMaskPath);


        btnMaskStart = buildJButton("开始添加蒙层", 50, 250, 100, 50);
        btnMaskStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mergeBitmap = new MergeBitmap(imgPathDir, markImgPath);

                btnSelectdDir.setEnabled(false);
                btnSelectdMaskImg.setEnabled(false);
                btnMaskStart.setEnabled(false);
                mergeBitmap.setMergeBitmapProogress(new MergeBitmap.MergeBitmapProogress() {
                    @Override
                    public void progress(int count, int progress, String currFile) {
                        handleTime = System.currentTimeMillis();
                        long interval = handleTime - startTime;
                        String timeText = "";
                        if (interval > 2000) {
                            timeText = (interval / 1000) + "s   ";
                        } else {
                            timeText = interval + "ms   ";
                        }
                        labelProgressResult.setText("<html> 耗时：" + timeText + "<br/>" + progress + "/" + count + "<br/>" + currFile + "</html>");
                    }

                    @Override
                    public void complete() {
                        btnSelectdDir.setEnabled(true);
                        btnSelectdMaskImg.setEnabled(true);
                        btnMaskStart.setEnabled(true);
                        System.out.println("耗时:" + (handleTime - startTime));
                    }
                });
                startTime = System.currentTimeMillis();
                mergeBitmap.start();
            }
        });

        contentPane.add(btnMaskStart);

        labelProgressResult = buildJLabel("进度", 50, 300, 400, 100);

        contentPane.add(labelProgressResult);

        // 显示窗口
//        frame.pack();
        frame.setVisible(true);
    }

    /**
     * 创建button
     *
     * @param name
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    private static JButton buildJButton(String name, int x, int y, int width, int height) {
        JButton button = new JButton(name);
        button.setBounds(x, y, width, height);
        return button;
    }

    /**
     * 创建label
     *
     * @param name
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    private static JLabel buildJLabel(String name, int x, int y, int width, int height) {
        JLabel label = new JLabel(name);
        label.setBounds(x, y, width, height);
        return label;
    }
}
