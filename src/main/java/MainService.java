import cn.hutool.core.img.gif.AnimatedGifEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author: hellodk
 * @description main service class
 * - 把 telegram 导出的 jpg/jpeg/png 静态表情图片转换成微信能够导入的 `.gif` 文件
 * - 输入一个文件夹路径，转换该文件夹下的所有 sticker 图片（不会递归查询子文件夹）
 * - 输出一个文件夹路径，保存所有的 `.gif` 文件，可以全选拖动到微信聊天页，然后发送，等发送成功就可以「Add Sticker」了
 * @date: 2022/1/24 11:26
 */

public class MainService {

    public static void main(String[] args) throws Exception {
        int length = args.length;
        if (length != 2) {
            System.out.println("wrong parameters! usage: java -jar app.jar IMG_FOLDER OUTPUT_FOLDER");
            return;
        }
        MainService mainService = new MainService();
        mainService.jpg2gif(args[0], args[1]);

    }

    public void jpg2gif(String imgFolder, String outputFolder) throws Exception {
        Path outputPath = Paths.get(outputFolder);
        if (!outputPath.toFile().exists()) {
            System.out.println("output folder not exist, please make directory first!");
            return;
        }

        Path imgPath = Paths.get(imgFolder);
        File file = new File(String.valueOf(imgPath));
        if (!imgPath.toFile().exists()) {
            System.out.println("image folder not exist, please check your input.");
            return;
        }
        File[] tempList = file.listFiles();
        if (tempList != null && tempList.length > 0) {
            for (int i = 0; i < tempList.length; i++) {
                File tempFile = tempList[i];
                String tempFileName = tempFile.getName();
                BufferedImage src = ImageIO.read(tempFile);
                AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
                gifEncoder.setRepeat(0);
                int lastDot = tempFileName.lastIndexOf('.');
                String newFileName = tempFileName.substring(0, lastDot) + ".gif";
                System.out.println("current sequence is " + (i + 1) + " and output file name is " + newFileName);
                gifEncoder.start(outputPath + "/" + newFileName);
                gifEncoder.setDelay(100);
                gifEncoder.addFrame(src);
                gifEncoder.setDelay(100);
                gifEncoder.finish();
            }
        }
    }
}
