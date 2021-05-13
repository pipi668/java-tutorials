package cn.aposoft.tutorial.screen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenShotTool {

    /**
     * 屏幕截图
     *
     * @param imageName 存储图片名称
     * @param path      图片路径
     * @param imgType   图片类型
     * @throws AWTException
     * @throws IOException
     */
    public static void cutPic(String imageName, String path, String imgType) throws AWTException, IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        ImageIO.write(image, imgType, new File(path + imageName + "." + imgType));
    }

    public static void main(String[] args) throws IOException, AWTException {
        cutPic("a", "d:/", "png");
    }
}
