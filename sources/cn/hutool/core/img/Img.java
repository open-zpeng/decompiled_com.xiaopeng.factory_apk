package cn.hutool.core.img;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.ImageFilter;
import java.awt.image.ImageObserver;
import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Path;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
/* loaded from: classes.dex */
public class Img implements Serializable {
    private static final long serialVersionUID = 1;
    private boolean positionBaseCentre;
    private float quality;
    private final BufferedImage srcImage;
    private Image targetImage;
    private String targetImageType;

    public static Img from(Path imagePath) {
        return from(imagePath.toFile());
    }

    public static Img from(File imageFile) {
        return new Img(ImgUtil.read(imageFile));
    }

    public static Img from(Resource resource) {
        return from(resource.getStream());
    }

    public static Img from(InputStream in) {
        return new Img(ImgUtil.read(in));
    }

    public static Img from(ImageInputStream imageStream) {
        return new Img(ImgUtil.read(imageStream));
    }

    public static Img from(URL imageUrl) {
        return new Img(ImgUtil.read(imageUrl));
    }

    public static Img from(Image image) {
        return new Img(ImgUtil.toBufferedImage(image));
    }

    public Img(BufferedImage srcImage) {
        this(srcImage, null);
    }

    public Img(BufferedImage srcImage, String targetImageType) {
        this.positionBaseCentre = true;
        this.quality = -1.0f;
        this.srcImage = srcImage;
        if (targetImageType == null) {
            if (srcImage.getType() == 2 || srcImage.getType() == 3 || srcImage.getType() == 6 || srcImage.getType() == 7) {
                targetImageType = ImgUtil.IMAGE_TYPE_PNG;
            } else {
                targetImageType = ImgUtil.IMAGE_TYPE_JPG;
            }
        }
        this.targetImageType = targetImageType;
    }

    public Img setTargetImageType(String imgType) {
        this.targetImageType = imgType;
        return this;
    }

    public Img setPositionBaseCentre(boolean positionBaseCentre) {
        this.positionBaseCentre = positionBaseCentre;
        return this;
    }

    public Img setQuality(double quality) {
        return setQuality((float) quality);
    }

    public Img setQuality(float quality) {
        if (quality > 0.0f && quality < 1.0f) {
            this.quality = quality;
        } else {
            this.quality = 1.0f;
        }
        return this;
    }

    public Img scale(float scale) {
        if (scale < 0.0f) {
            scale = -scale;
        }
        Image srcImg = getValidSrcImg();
        if (ImgUtil.IMAGE_TYPE_PNG.equals(this.targetImageType)) {
            double scaleDouble = NumberUtil.toDouble(Float.valueOf(scale));
            this.targetImage = ImgUtil.transform(AffineTransform.getScaleInstance(scaleDouble, scaleDouble), ImgUtil.toBufferedImage(srcImg, this.targetImageType));
        } else {
            int width = NumberUtil.mul(Integer.valueOf(srcImg.getWidth((ImageObserver) null)), Float.valueOf(scale)).intValue();
            int height = NumberUtil.mul(Integer.valueOf(srcImg.getHeight((ImageObserver) null)), Float.valueOf(scale)).intValue();
            scale(width, height);
        }
        return this;
    }

    public Img scale(int width, int height) {
        int scaleType;
        Image srcImg = getValidSrcImg();
        int srcHeight = srcImg.getHeight((ImageObserver) null);
        int srcWidth = srcImg.getWidth((ImageObserver) null);
        if (srcHeight == height && srcWidth == width) {
            this.targetImage = srcImg;
            return this;
        }
        if (srcHeight < height || srcWidth < width) {
            scaleType = 4;
        } else {
            scaleType = 1;
        }
        if (ImgUtil.IMAGE_TYPE_PNG.equals(this.targetImageType)) {
            double sx = NumberUtil.div(width, srcWidth);
            double sy = NumberUtil.div(height, srcHeight);
            this.targetImage = ImgUtil.transform(AffineTransform.getScaleInstance(sx, sy), ImgUtil.toBufferedImage(srcImg, this.targetImageType));
        } else {
            this.targetImage = srcImg.getScaledInstance(width, height, scaleType);
        }
        return this;
    }

    public Img scale(int width, int height, Color fixedColor) {
        Image srcImage = getValidSrcImg();
        int srcHeight = srcImage.getHeight((ImageObserver) null);
        int srcWidth = srcImage.getWidth((ImageObserver) null);
        double heightRatio = NumberUtil.div(height, srcHeight);
        double widthRatio = NumberUtil.div(width, srcWidth);
        if (NumberUtil.equals(heightRatio, widthRatio)) {
            scale(width, height);
        } else if (widthRatio < heightRatio) {
            scale(width, (int) (srcHeight * widthRatio));
        } else {
            scale((int) (srcWidth * heightRatio), height);
        }
        Image srcImage2 = getValidSrcImg();
        int srcHeight2 = srcImage2.getHeight((ImageObserver) null);
        int srcWidth2 = srcImage2.getWidth((ImageObserver) null);
        BufferedImage image = new BufferedImage(width, height, getTypeInt());
        Graphics2D g = image.createGraphics();
        if (fixedColor != null) {
            g.setBackground(fixedColor);
            g.clearRect(0, 0, width, height);
        }
        g.drawImage(srcImage2, (width - srcWidth2) / 2, (height - srcHeight2) / 2, srcWidth2, srcHeight2, fixedColor, (ImageObserver) null);
        g.dispose();
        this.targetImage = image;
        return this;
    }

    public Img cut(Rectangle rectangle) {
        Image srcImage = getValidSrcImg();
        fixRectangle(rectangle, srcImage.getWidth((ImageObserver) null), srcImage.getHeight((ImageObserver) null));
        this.targetImage = ImgUtil.filter((ImageFilter) new CropImageFilter(rectangle.x, rectangle.y, rectangle.width, rectangle.height), srcImage);
        return this;
    }

    public Img cut(int x, int y) {
        return cut(x, y, -1);
    }

    public Img cut(int x, int y, int radius) {
        int x2;
        int y2;
        Image srcImage = getValidSrcImg();
        int width = srcImage.getWidth((ImageObserver) null);
        int height = srcImage.getHeight((ImageObserver) null);
        int diameter = radius > 0 ? radius * 2 : Math.min(width, height);
        BufferedImage targetImage = new BufferedImage(diameter, diameter, 2);
        Graphics2D g = targetImage.createGraphics();
        g.setClip(new Ellipse2D.Double(0.0d, 0.0d, diameter, diameter));
        if (!this.positionBaseCentre) {
            x2 = x;
            y2 = y;
        } else {
            x2 = (x - (width / 2)) + (diameter / 2);
            y2 = (y - (height / 2)) + (diameter / 2);
        }
        g.drawImage(srcImage, x2, y2, (ImageObserver) null);
        g.dispose();
        this.targetImage = targetImage;
        return this;
    }

    public Img round(double arc) {
        Image srcImage = getValidSrcImg();
        int width = srcImage.getWidth((ImageObserver) null);
        int height = srcImage.getHeight((ImageObserver) null);
        double arc2 = NumberUtil.mul(arc, Math.min(width, height));
        BufferedImage targetImage = new BufferedImage(width, height, 2);
        Graphics2D g2 = targetImage.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fill(new RoundRectangle2D.Double(0.0d, 0.0d, width, height, arc2, arc2));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(srcImage, 0, 0, (ImageObserver) null);
        g2.dispose();
        this.targetImage = targetImage;
        return this;
    }

    public Img gray() {
        this.targetImage = ImgUtil.colorConvert(ColorSpace.getInstance(1003), getValidSrcBufferedImg());
        return this;
    }

    public Img binary() {
        this.targetImage = ImgUtil.copyImage(getValidSrcImg(), 12);
        return this;
    }

    public Img pressText(String pressText, Color color, Font font, int x, int y, float alpha) {
        BufferedImage targetImage = ImgUtil.toBufferedImage(getValidSrcImg(), this.targetImageType);
        Graphics2D g = targetImage.createGraphics();
        if (font == null) {
            font = FontUtil.createSansSerifFont((int) (targetImage.getHeight() * 0.75d));
        }
        g.setComposite(AlphaComposite.getInstance(10, alpha));
        if (this.positionBaseCentre) {
            GraphicsUtil.drawString((Graphics) g, pressText, font, color, new Rectangle(x, y, targetImage.getWidth(), targetImage.getHeight()));
        } else {
            GraphicsUtil.drawString((Graphics) g, pressText, font, color, new Point(x, y));
        }
        g.dispose();
        this.targetImage = targetImage;
        return this;
    }

    public Img pressImage(Image pressImg, int x, int y, float alpha) {
        int pressImgWidth = pressImg.getWidth((ImageObserver) null);
        int pressImgHeight = pressImg.getHeight((ImageObserver) null);
        return pressImage(pressImg, new Rectangle(x, y, pressImgWidth, pressImgHeight), alpha);
    }

    public Img pressImage(Image pressImg, Rectangle rectangle, float alpha) {
        Image targetImg = getValidSrcImg();
        this.targetImage = draw(ImgUtil.toBufferedImage(targetImg, this.targetImageType), pressImg, rectangle, alpha);
        return this;
    }

    public Img rotate(int degree) {
        Image image = getValidSrcImg();
        int width = image.getWidth((ImageObserver) null);
        int height = image.getHeight((ImageObserver) null);
        Rectangle rectangle = calcRotatedSize(width, height, degree);
        BufferedImage targetImg = new BufferedImage(rectangle.width, rectangle.height, getTypeInt());
        Graphics2D graphics2d = targetImg.createGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.translate((rectangle.width - width) / 2.0d, (rectangle.height - height) / 2.0d);
        graphics2d.rotate(Math.toRadians(degree), width / 2.0d, height / 2.0d);
        graphics2d.drawImage(image, 0, 0, (ImageObserver) null);
        graphics2d.dispose();
        this.targetImage = targetImg;
        return this;
    }

    public Img flip() {
        Image image = getValidSrcImg();
        int width = image.getWidth((ImageObserver) null);
        int height = image.getHeight((ImageObserver) null);
        BufferedImage targetImg = new BufferedImage(width, height, getTypeInt());
        Graphics2D graphics2d = targetImg.createGraphics();
        graphics2d.drawImage(image, 0, 0, width, height, width, 0, 0, height, (ImageObserver) null);
        graphics2d.dispose();
        this.targetImage = targetImg;
        return this;
    }

    public Img stroke(Color color, float width) {
        return stroke(color, (Stroke) new BasicStroke(width));
    }

    public Img stroke(Color color, Stroke stroke) {
        BufferedImage image = ImgUtil.toBufferedImage(getValidSrcImg(), this.targetImageType);
        int width = image.getWidth((ImageObserver) null);
        int height = image.getHeight((ImageObserver) null);
        Graphics2D g = image.createGraphics();
        g.setColor((Color) ObjectUtil.defaultIfNull(color, Color.BLACK));
        if (stroke != null) {
            g.setStroke(stroke);
        }
        g.drawRect(0, 0, width - 1, height - 1);
        g.dispose();
        this.targetImage = image;
        return this;
    }

    public Image getImg() {
        return getValidSrcImg();
    }

    public boolean write(OutputStream out) throws IORuntimeException {
        return write(ImgUtil.getImageOutputStream(out));
    }

    public boolean write(ImageOutputStream targetImageStream) throws IORuntimeException {
        Assert.notBlank(this.targetImageType, "Target image type is blank !", new Object[0]);
        Assert.notNull(targetImageStream, "Target output stream is null !", new Object[0]);
        BufferedImage bufferedImage = this.targetImage;
        if (bufferedImage == null) {
            bufferedImage = this.srcImage;
        }
        Assert.notNull(bufferedImage, "Target image is null !", new Object[0]);
        return ImgUtil.write((Image) bufferedImage, this.targetImageType, targetImageStream, this.quality);
    }

    public boolean write(File targetFile) throws IORuntimeException {
        String formatName = FileUtil.extName(targetFile);
        if (StrUtil.isNotBlank(formatName)) {
            this.targetImageType = formatName;
        }
        if (targetFile.exists()) {
            targetFile.delete();
        }
        ImageOutputStream out = null;
        try {
            out = ImgUtil.getImageOutputStream(targetFile);
            return write(out);
        } finally {
            IoUtil.close((Closeable) out);
        }
    }

    private BufferedImage draw(BufferedImage backgroundImg, Image img, Rectangle rectangle, float alpha) {
        Graphics2D g = backgroundImg.createGraphics();
        GraphicsUtil.setAlpha(g, alpha);
        fixRectangle(rectangle, backgroundImg.getWidth(), backgroundImg.getHeight());
        GraphicsUtil.drawImg((Graphics) g, img, rectangle);
        g.dispose();
        return backgroundImg;
    }

    private int getTypeInt() {
        String str = this.targetImageType;
        if (((str.hashCode() == 111145 && str.equals(ImgUtil.IMAGE_TYPE_PNG)) ? (char) 0 : (char) 65535) == 0) {
            return 2;
        }
        return 1;
    }

    private Image getValidSrcImg() {
        return (Image) ObjectUtil.defaultIfNull(this.targetImage, this.srcImage);
    }

    private BufferedImage getValidSrcBufferedImg() {
        return ImgUtil.toBufferedImage(getValidSrcImg(), this.targetImageType);
    }

    private Rectangle fixRectangle(Rectangle rectangle, int baseWidth, int baseHeight) {
        if (this.positionBaseCentre) {
            Point pointBaseCentre = ImgUtil.getPointBaseCentre(rectangle, baseWidth, baseHeight);
            rectangle.setLocation(pointBaseCentre.x, pointBaseCentre.y);
        }
        return rectangle;
    }

    private static Rectangle calcRotatedSize(int width, int height, int degree) {
        int width2;
        int height2;
        int degree2 = degree;
        if (degree2 < 0) {
            degree2 += 360;
        }
        if (degree2 < 90) {
            width2 = width;
            height2 = height;
        } else {
            if ((degree2 / 90) % 2 != 1) {
                width2 = width;
                height2 = height;
            } else {
                height2 = width;
                width2 = height;
            }
            degree2 %= 90;
        }
        double r = Math.sqrt((height2 * height2) + (width2 * width2)) / 2.0d;
        double len = Math.sin(Math.toRadians(degree2) / 2.0d) * 2.0d * r;
        double angel_alpha = (3.141592653589793d - Math.toRadians(degree2)) / 2.0d;
        double angel_dalta_width = Math.atan(height2 / width2);
        double angel_dalta_height = Math.atan(width2 / height2);
        int len_dalta_width = (int) (len * Math.cos((3.141592653589793d - angel_alpha) - angel_dalta_width));
        int len_dalta_height = (int) (Math.cos((3.141592653589793d - angel_alpha) - angel_dalta_height) * len);
        int des_width = (len_dalta_width * 2) + width2;
        int degree3 = height2 + (len_dalta_height * 2);
        return new Rectangle(des_width, degree3);
    }
}
