package tpson.utils;

import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;
import tpson.app.WebConfig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by liuandong on 2017/7/6.
 */
public class ImageInfo {
    private int height;
    private int width;

    /**
     * 获取文件信息 使用文件流
     * @param picture
     * @throws Exception
     */
    public ImageInfo(File picture) throws Exception {
        FileInputStream stream = new FileInputStream(picture);
        BufferedImage sourceImg = ImageIO.read(stream);
        this.width = sourceImg.getWidth();
        this.height = sourceImg.getHeight();
        stream.close();
    }

    /**
     * 获取图片信息
     * @param path 图片路径
     * @throws Exception
     */
    public ImageInfo(String path) throws Exception {
        IMOperation op = new IMOperation();
        op.format("%w,%h,%d,%f,%b");
        op.addImage(path);
        IdentifyCmd identifyCmd =  new IdentifyCmd();
        IdentifyCmd.setGlobalSearchPath(WebConfig.imageMagick_path);
        ArrayListOutputConsumer output = new ArrayListOutputConsumer();
        identifyCmd.setOutputConsumer(output);
        identifyCmd.run(op);
        ArrayList<String> cmdOutput = output.getOutput();
        if (cmdOutput.size() != 1) {
            throw new Exception("参数不对");
        }
        String line = cmdOutput.get(0);
        String[] arr = line.split(",");

        this.width = Integer.parseInt(arr[0]);
        this.height = Integer.parseInt(arr[1]);
    }

    public ImageInfo(int width,int height){
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
