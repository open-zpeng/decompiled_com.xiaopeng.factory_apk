package cn.hutool.core.img;

import androidx.core.view.MotionEventCompat;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
/* loaded from: classes.dex */
public class BackgroundRemoval {
    public static String[] IMAGES_TYPE = {ImgUtil.IMAGE_TYPE_JPG, ImgUtil.IMAGE_TYPE_PNG};

    public static boolean backgroundRemoval(String inputPath, String outputPath, int tolerance) {
        return backgroundRemoval(new File(inputPath), new File(outputPath), tolerance);
    }

    public static boolean backgroundRemoval(File input, File output, int tolerance) {
        return backgroundRemoval(input, output, null, tolerance);
    }

    public static boolean backgroundRemoval(File input, File output, Color override, int tolerance) {
        if (fileTypeValidation(input, IMAGES_TYPE)) {
            return false;
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(input);
            return ImageIO.write(backgroundRemoval(bufferedImage, override, tolerance), ImgUtil.IMAGE_TYPE_PNG, output);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static BufferedImage backgroundRemoval(BufferedImage bufferedImage, Color override, int tolerance) {
        int tolerance2 = Math.min(255, Math.max(tolerance, 0));
        ImageIcon imageIcon = new ImageIcon(bufferedImage);
        BufferedImage image = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), 6);
        Graphics graphics = image.getGraphics();
        graphics.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
        String[] removeRgb = getRemoveRgb(bufferedImage);
        String mainColor = getMainColor(bufferedImage);
        for (int y = image.getMinY(); y < image.getHeight(); y++) {
            for (int x = image.getMinX(); x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                String hex = ImgUtil.toHex((16711680 & rgb) >> 16, (65280 & rgb) >> 8, rgb & 255);
                boolean z = true;
                if (!ArrayUtil.contains(removeRgb, hex) && !areColorsWithinTolerance(hexToRgb(mainColor), new Color(Integer.parseInt(hex.substring(1), 16)), tolerance2)) {
                    z = false;
                }
                boolean isTrue = z;
                if (isTrue) {
                    rgb = override == null ? ((0 + 1) << 24) | (16777215 & rgb) : override.getRGB();
                }
                image.setRGB(x, y, rgb);
            }
        }
        graphics.drawImage(image, 0, 0, imageIcon.getImageObserver());
        return image;
    }

    public static BufferedImage backgroundRemoval(ByteArrayOutputStream outputStream, Color override, int tolerance) {
        try {
            return backgroundRemoval(ImageIO.read(new ByteArrayInputStream(outputStream.toByteArray())), override, tolerance);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String[] getRemoveRgb(BufferedImage image) {
        int width = image.getWidth() - 1;
        int height = image.getHeight() - 1;
        int leftUpPixel = image.getRGB(1, 1);
        String leftUp = ImgUtil.toHex((leftUpPixel & 16711680) >> 16, (leftUpPixel & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8, leftUpPixel & 255);
        int upMiddlePixel = image.getRGB(width / 2, 1);
        String upMiddle = ImgUtil.toHex((upMiddlePixel & 16711680) >> 16, (upMiddlePixel & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8, upMiddlePixel & 255);
        int rightUpPixel = image.getRGB(width, 1);
        String rightUp = ImgUtil.toHex((rightUpPixel & 16711680) >> 16, (rightUpPixel & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8, rightUpPixel & 255);
        int rightMiddlePixel = image.getRGB(width, height / 2);
        String rightMiddle = ImgUtil.toHex((rightMiddlePixel & 16711680) >> 16, (rightMiddlePixel & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8, rightMiddlePixel & 255);
        int lowerRightPixel = image.getRGB(width, height);
        String lowerRight = ImgUtil.toHex((lowerRightPixel & 16711680) >> 16, (lowerRightPixel & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8, lowerRightPixel & 255);
        int lowerMiddlePixel = image.getRGB(width / 2, height);
        String lowerMiddle = ImgUtil.toHex((lowerMiddlePixel & 16711680) >> 16, (lowerMiddlePixel & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8, lowerMiddlePixel & 255);
        int leftLowerPixel = image.getRGB(1, height);
        String leftLower = ImgUtil.toHex((leftLowerPixel & 16711680) >> 16, (leftLowerPixel & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8, leftLowerPixel & 255);
        int leftMiddlePixel = image.getRGB(1, height / 2);
        String leftMiddle = ImgUtil.toHex((16711680 & leftMiddlePixel) >> 16, (leftMiddlePixel & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8, leftMiddlePixel & 255);
        return new String[]{leftUp, upMiddle, rightUp, rightMiddle, lowerRight, lowerMiddle, leftLower, leftMiddle};
    }

    public static Color hexToRgb(String hex) {
        return new Color(Integer.parseInt(hex.substring(1), 16));
    }

    public static boolean areColorsWithinTolerance(Color color1, Color color2, int tolerance) {
        return areColorsWithinTolerance(color1, color2, new Color(tolerance, tolerance, tolerance));
    }

    public static boolean areColorsWithinTolerance(Color color1, Color color2, Color tolerance) {
        return color1.getRed() - color2.getRed() < tolerance.getRed() && color1.getRed() - color2.getRed() > (-tolerance.getRed()) && color1.getBlue() - color2.getBlue() < tolerance.getBlue() && color1.getBlue() - color2.getBlue() > (-tolerance.getBlue()) && color1.getGreen() - color2.getGreen() < tolerance.getGreen() && color1.getGreen() - color2.getGreen() > (-tolerance.getGreen());
    }

    public static String getMainColor(String input) {
        return getMainColor(new File(input));
    }

    public static String getMainColor(File input) {
        try {
            return getMainColor(ImageIO.read(input));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getMainColor(BufferedImage bufferedImage) {
        int integer;
        if (bufferedImage == null) {
            throw new IllegalArgumentException("图片流是空的");
        }
        List<String> list = new ArrayList<>();
        for (int y = bufferedImage.getMinY(); y < bufferedImage.getHeight(); y++) {
            for (int x = bufferedImage.getMinX(); x < bufferedImage.getWidth(); x++) {
                int pixel = bufferedImage.getRGB(x, y);
                list.add(((16711680 & pixel) >> 16) + "-" + ((65280 & pixel) >> 8) + "-" + (pixel & 255));
            }
        }
        Map<String, Integer> map = new HashMap<>(list.size());
        for (String string : list) {
            Integer integer2 = map.get(string);
            if (integer2 == null) {
                integer = 1;
            } else {
                integer = Integer.valueOf(integer2.intValue() + 1);
            }
            map.put(string, integer);
        }
        String max = "";
        long num = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            Integer temp = entry.getValue();
            if (StrUtil.isBlank(max) || temp.intValue() > num) {
                max = key;
                num = temp.intValue();
            }
        }
        String[] strings = max.split("-");
        if (strings.length == 3) {
            return ImgUtil.toHex(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
        }
        return "";
    }

    private static boolean fileTypeValidation(File input, String[] imagesType) {
        if (!input.exists()) {
            throw new IllegalArgumentException("给定文件为空");
        }
        String type = FileTypeUtil.getType(input);
        if (ArrayUtil.contains(imagesType, type)) {
            return false;
        }
        throw new IllegalArgumentException(StrUtil.format("文件类型{}不支持", type));
    }
}
