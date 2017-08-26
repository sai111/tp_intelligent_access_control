package tpson.utils;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import tpson.app.WebConfig;

/**
 * Created by liuandong on 2017/7/4.
 */
public class ImageUtils {

    /**
     * 限定缩略图的长边最多为<LongEdge>，短边最多为<ShortEdge>，进行等比缩放，不裁剪。
     * 如果只指定 w 参数则表示限定长边（短边自适应），
     * 只指定 h 参数则表示限定短边（长边自适应）。
     */
    public static String resize0(String uri,Integer longEdge,Integer shortEdge){
        try{
            ImageInfo info = new ImageInfo(uri);
            Integer width = info.getWidth();
            Integer height = info.getHeight();
            IMOperation op = new IMOperation();
            op.addImage(uri);
            if (width >= height){
                op.resize(longEdge, shortEdge);//压缩图片
            } else{
                op.resize(shortEdge, longEdge);//压缩图片
            }
            op.addImage(getName(0,uri,longEdge,shortEdge));
            ConvertCmd convert = new ConvertCmd();
            convert.setSearchPath(WebConfig.imageMagick_path);
            convert.run(op);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 限定缩略图的宽最少为<Width>，高最少为<Height>，进行等比缩放，居中裁剪。
     * 转后的缩略图通常恰好是 <Width>x<Height> 的大小（有一个边缩放的时候会因为超出矩形框而被裁剪掉多余部分）。
     * 如果只指定 w 参数或只指定 h 参数，代表限定为长宽相等的正方图。
     */
    public static String resize1(String uri,Integer width,Integer height){
        try{
            IMOperation op = new IMOperation();
            op.addImage(uri);
            op.resize(width, height, '^').gravity("center").extent(width, height);
            op.addImage(getName(1,uri,width,height));
            ConvertCmd convert = new ConvertCmd();
            convert.setSearchPath(WebConfig.imageMagick_path);
            convert.run(op);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 限定缩略图的宽最多为<Width>，高最多为<Height>，进行等比缩放，不裁剪。
     * 如果只指定 w 参数则表示限定宽（长自适应），只指定 h 参数则表示限定长（宽自适应）。
     * 它和模式0类似，区别只是限定宽和高，不是限定长边和短边。
     * 从应用场景来说，模式0适合移动设备上做缩略图，模式2适合PC上做缩略图。
     */
    public static String resize2(String uri,Integer width,Integer height){
        try{
            IMOperation op = new IMOperation();
            op.addImage(uri);
            op.resize(width, height);//压缩图片
            op.addImage(getName(2,uri,width,height));
            ConvertCmd convert = new ConvertCmd();
            convert.setSearchPath(WebConfig.imageMagick_path);
            convert.run(op);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 限定缩略图的宽最少为<Width>，高最少为<Height>，进行等比缩放，不裁剪。
     * 如果只指定 w 参数或只指定 h 参数，代表长宽限定为同样的值。
     * 你可以理解为模式1是模式3的结果再做居中裁剪得到的。
     */
    public static String resize3(String uri,Integer width,Integer height){
        try{
            ImageInfo info = new ImageInfo(uri);
            Integer imgWidth = info.getWidth();
            Integer imgHeight = info.getHeight();
            IMOperation op = new IMOperation();
            op.addImage(uri);
            if (imgWidth*height >= imgHeight*width){
                op.resize(null, height);//压缩图片
            } else{
                op.resize(width, null);//压缩图片
            }
            op.addImage(getName(3,uri,width,height));
            ConvertCmd convert = new ConvertCmd();
            convert.setSearchPath(WebConfig.imageMagick_path);
            convert.run(op);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 	限定缩略图的长边最少为<LongEdge>，短边最少为<ShortEdge>，进行等比缩放，不裁剪。
     * 	如果只指定 w 参数或只指定 h 参数，表示长边短边限定为同样的值。
     * 	这个模式很适合在手持设备做图片的全屏查看（把这里的长边短边分别设为手机屏幕的分辨率即可），
     * 	生成的图片尺寸刚好充满整个屏幕（某一个边可能会超出屏幕）。
     */
    public static String resize4(String uri,Integer longEdge,Integer shortEdge){
        try{
            ImageInfo info = new ImageInfo(uri);
            Integer imgWidth = info.getWidth();
            Integer imgHeight = info.getHeight();
            Integer width = 0;
            Integer height = 0;
            if (imgWidth >= imgHeight){
                width = longEdge;
                height = shortEdge;
            } else {
                width = shortEdge;
                height = longEdge;
            }
            IMOperation op = new IMOperation();
            op.addImage(uri);
            if (imgWidth*height >= imgHeight*width){
                op.resize(null, height);//压缩图片
            } else{
                op.resize(width, null);//压缩图片
            }
            op.addImage(getName(4,uri,longEdge,shortEdge));
            ConvertCmd convert = new ConvertCmd();
            convert.setSearchPath(WebConfig.imageMagick_path);
            convert.run(op);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 限定缩略图的长边最少为<LongEdge>，短边最少为<ShortEdge>，进行等比缩放，居中裁剪。
     * 如果只指定 w 参数或只指定 h 参数，表示长边短边限定为同样的值。
     * 同上模式4，但超出限定的矩形部分会被裁剪。
     */
    public static String resize5(String uri,Integer longEdge,Integer shortEdge){
        try{
            ImageInfo info = new ImageInfo(uri);
            Integer width = info.getWidth();
            Integer height = info.getHeight();
            IMOperation op = new IMOperation();
            op.addImage(uri);
            if (width >= height){
                op.resize(longEdge, shortEdge, '^').gravity("center").extent(longEdge, shortEdge);
            } else{
                op.resize(shortEdge, longEdge, '^').gravity("center").extent(shortEdge, longEdge);
            }
            op.addImage(getName(5,uri,longEdge,shortEdge));
            ConvertCmd convert = new ConvertCmd();
            convert.setSearchPath(WebConfig.imageMagick_path);
            convert.run(op);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 缩略图存放路径
     */
    public static String getName(int model,String uri,Integer width,Integer height){
        String[] uriName = uri.split("\\.");
//        return "thumb/"+ uriName[0] + "_" + model + "_" + width + "_" + height + uriName[1];
        return uriName[0] + "_" + model + "_" + width + "_" + height +"." + uriName[1];
    }

    public static void main(String[] args) {
        String uri = "C:\\Users\\tpson_java_2\\Desktop\\Koala.jpg";
        Integer width = 500;
        Integer height = 200;
        resize0( uri, 200, 500);
        resize1( uri, 500, 200);
        resize2( uri, 500, 200);
        resize3( uri, 500, 200);
        resize4( uri, 200, 500);
        resize5( uri, 200, 500);

//        Map<String, Object> info = new HashMap<String, Object>();
//        try{
//            info = getImageInfo(uri);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }
}
