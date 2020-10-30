package com.example.test.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import javax.imageio.ImageIO;

public class ImageUtil {
    public ImageUtil() {
    }

    public static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
        int type = 5;
        BufferedImage target = null;
        double sx = (double)targetW / (double)source.getWidth();
        double sy = (double)targetH / (double)source.getHeight();
        if (sx < sy) {
            sx = sy;
            targetW = (int)(sy * (double)source.getWidth());
        } else {
            sy = sx;
            targetH = (int)(sx * (double)source.getHeight());
        }

        if (type == 0) {
            ColorModel cm = source.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, raster, alphaPremultiplied, (Hashtable)null);
        } else {
            target = new BufferedImage(targetW, targetH, type);
        }

        Graphics2D g = target.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        target.flush();
        return target;
    }

    public static void saveImageAsJpg(InputStream in, File target, int width, int height) throws IOException {
        BufferedImage srcImage = ImageIO.read(in);
        int w;
        int h;
        if (width > 0 || height > 0) {
            w = srcImage.getWidth();
            h = srcImage.getHeight();
            if (w <= width || h <= height) {
                String fileName = target.getName();
                String formatName = fileName.substring(fileName.lastIndexOf(46) + 1);
                ImageIO.write(srcImage, formatName, target);
                return;
            }

            srcImage = resize(srcImage, width, height);
        }

        w = srcImage.getWidth();
        h = srcImage.getHeight();
        if (w == width) {
            int x = 0;
            int y = h / 2 - height / 2;
            saveSubImage(srcImage, new Rectangle(x, y, width, height), target);
        } else if (h == height) {
            int x = w / 2 - width / 2;
            int y = 0;
            saveSubImage(srcImage, new Rectangle(x, y, width, height), target);
        }

        srcImage.flush();
    }

    public static void saveImageAsJpg(File src, File target, int width, int height) throws Exception {
        try {
            InputStream in = new FileInputStream(src);
            saveImageAsJpg((InputStream)in, target, width, height);
            in.close();
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }

    private static void saveSubImage(BufferedImage image, Rectangle subImageBounds, File subImageFile) throws IOException {
        if (subImageBounds.x >= 0 && subImageBounds.y >= 0 && subImageBounds.width - subImageBounds.x <= image.getWidth() && subImageBounds.height - subImageBounds.y <= image.getHeight()) {
            BufferedImage subImage = image.getSubimage(subImageBounds.x, subImageBounds.y, subImageBounds.width, subImageBounds.height);
            String fileName = subImageFile.getName();
            String formatName = fileName.substring(fileName.lastIndexOf(46) + 1);
            ImageIO.write(subImage, formatName, subImageFile);
            image.flush();
        } else {
            System.out.println("Bad   subimage   bounds");
        }
    }

    public static void crop(File src, File target, int x, int y, int width, int height) {
        try {
            BufferedImage bi = ImageIO.read(src);
            int srcWidth = bi.getWidth();
            int srcHeight = bi.getHeight();
            if (srcWidth >= width && srcHeight >= height) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight, 1);
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
                BufferedImage tag = new BufferedImage(width, height, 1);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, (ImageObserver)null);
                g.dispose();
                ImageIO.write(tag, "JPEG", target);
            }
        } catch (Exception var14) {
            var14.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            //读取目录
            String filePath = "C:\\Users\\TDH\\Desktop\\最终版";
            String outFilePath = "C:\\Users\\TDH\\Desktop\\preview";
            File file=new File(filePath);
            File[] tempList = file.listFiles();
            System.out.println("该目录下对象个数："+tempList.length);
            for (int i = 0; i < tempList.length; i++) {
                File tempFile = tempList[i];
                if (tempFile.isFile()) {
                    String fileName = tempFile.getName().replace(" ", "");
                    System.out.println(i + "    " + fileName);

                    int index = fileName.lastIndexOf('.');
                    String ext = fileName.substring(index, fileName.length());
                    String nameExceptExt = fileName.substring(0, index);
                    String name=nameExceptExt + "_preview"+ext;
                    System.out.println(name);

                    saveImageAsJpg(tempFile, new File(outFilePath+"\\" + name), 64, 48);
                }
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }
}

