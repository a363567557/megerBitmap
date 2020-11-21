package cn.cn.testPic.utils;

import cn.cn.testPic.ui.WindownSwing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MergeBitmap {

    //蒙层的路径
    private String markImgPath = "/Users/mrl/Desktop/abcd.jpg";

    // 需要处理图片的文件目录
    private String imgPathDir = "/Users/mrl/Desktop/12345.jpg";

    private MergeBitmapProogress mMergeBitmapProogress;


    public MergeBitmap(String imgPathDir, String markImgPath) {
        this.imgPathDir = imgPathDir;
        this.markImgPath = markImgPath;
    }

    /**
     * 开始进行合成
     */
    public void start() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                File imgPathDirectory = new File(imgPathDir);//设定为当前文件夹
                int resultCount = 0;
                if (imgPathDirectory.exists()
                        && imgPathDirectory.isDirectory()) {
                    try {
                        System.out.println(imgPathDirectory.getAbsolutePath());
                        File[] files = imgPathDirectory.listFiles();
                        BufferedImage imageMark = ImageIO.read(new FileInputStream(markImgPath));
                        int count = files.length;
                        for (int i = 0; i < count; i++) {
                            File subFile = files[i];
                            if (subFile.isDirectory()) {
                                File[] subFileList = subFile.listFiles();
                                int subCount = subFileList.length;
                                for (int j = 0; j < subCount; j++) {
                                    resultCount++;
                                    File handleFile = subFileList[j];
                                    System.out.println(handleFile.getAbsolutePath());
                                    BufferedImage image = ImageIO.read(new FileInputStream(handleFile.getAbsolutePath()));
                                    if (image == null) {
                                        continue;
                                    }
                                    BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
                                    Graphics g = resultImage.getGraphics();
                                    g.drawImage(image, 0, 0, null);
                                    g.drawImage(imageMark, 0, 0, null);
                                    String fileName = handleFile.getName();
                                    int endIndex = -1;
                                    String endName = "";
                                    if (fileName != null
                                            && (endIndex = fileName.lastIndexOf(".")) > 0) {
                                        endName = fileName.substring(endIndex + 1);
                                    }
                                    ImageIO.write(resultImage, endName, handleFile);
                                    if (mMergeBitmapProogress != null) {
                                        mMergeBitmapProogress.progress(count, resultCount, handleFile.getAbsolutePath());
                                    }
                                }
                            }


                        }
                        System.out.println("结束了！！");

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (mMergeBitmapProogress != null) {
                            mMergeBitmapProogress.complete();
                        }
                    }
                }
            }
        }.start();
    }

    public void setMergeBitmapProogress(MergeBitmapProogress mergeBitmapProogress) {
        this.mMergeBitmapProogress = mergeBitmapProogress;
    }

    public interface MergeBitmapProogress {
        void progress(int count, int progress, String currFile);

        void complete();
    }

}
