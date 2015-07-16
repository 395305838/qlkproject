package com.xiaocoder.android.fw.general.util;
///**
// * 
// */
//package com.xiaocoder.android.fw.general.util;
//
//import java.io.DataInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Environment;
//
//import com.xiaocoder.android.fw.general.application.XCApplication;
//import com.xiaocoder.android.fw.general.application.XCBaseConfig;
//
//public class UtilImage2 {
//
//	public static Bitmap decodeFile(File f) {
////        try {
////            // Decode image size
////            BitmapFactory.Options o = new BitmapFactory.Options();
////            o.inJustDecodeBounds = true;
////            o.inPreferredConfig = Bitmap.Config.RGB_565;   
////            o.inPurgeable = true;  
////            o.inInputShareable = true;
////            o.inTempStorage = new byte[100 * 1024];
////            o.inSampleSize = 4;
////
////            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
////
////            // The new size we want to scale to
////            final int REQUIRED_SIZE = 800;
////
////            // Find the correct scale value. It should be the power of 2.
////            int scale = 1;
////            while (o.outWidth / scale / 2 >= REQUIRED_SIZE
////                    && o.outHeight / scale / 2 >= REQUIRED_SIZE)
////                scale *= 2;
////
////            // Decode with inSampleSize
////            BitmapFactory.Options o2 = new BitmapFactory.Options();
////            o2.inSampleSize = scale;
////            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f),
////                    null, o2);
////            return bitmap;
//////            ImageView iv = new ImageView(context);
//////            iv.setImageBitmap(bitmap);
//////            return iv.getDrawable();
////        } catch (FileNotFoundException e) {
////
////        }
////        return null;
//		return getimage(f);
//    }
//	
////	public static Bitmap compressImage(Bitmap image) {  
////		  
////        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
////        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
////        int options = 100;  
////        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
////            baos.reset();//重置baos即清空baos  
////            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
////            options -= 10;//每次都减少10  
////        }  
////        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
////        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
////        if(!image.isRecycled()){
////        	image.recycle();
////        }
////        return bitmap;  
////    } 
//	
//	
//	public static Bitmap compressImage(String path){
//	    try {
//	    	File f= new File(path);
//	        //Decode image size
//	        BitmapFactory.Options o = new BitmapFactory.Options();
//	        o.inJustDecodeBounds = true;
//	        BitmapFactory.decodeStream(new FileInputStream(f),null,o);
//
//	        //The new size we want to scale to
//	        final int REQUIRED_SIZE=70;
//
//	        //Find the correct scale value. It should be the power of 2.
//	        int scale=1;
//	        while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
//	            scale*=2;
//
//	        //Decode with inSampleSize
//	        BitmapFactory.Options o2 = new BitmapFactory.Options();
//	        o2.inSampleSize=scale;
//	        return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
//	    } catch (FileNotFoundException e) {}
//	    return null;
//	}
//	
//	public static Bitmap getimage(File f){
//		return getimage(f.getAbsolutePath());
//	}
//	
//	public static Bitmap getimage(String srcPath) {  
////        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
////        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
////        newOpts.inJustDecodeBounds = true;  
////        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
////        newOpts.inJustDecodeBounds = false;  
////        int w = newOpts.outWidth;  
////        int h = newOpts.outHeight;  
////        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
////        float hh = 800f;//这里设置高度为800f  
////        float ww = 480f;//这里设置宽度为480f  
////        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
////        int be = 1;//be=1表示不缩放  
////        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
////            be = (int) (newOpts.outWidth / ww);  
////        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
////            be = (int) (newOpts.outHeight / hh);  
////        }  
////        if (be <= 0)  
////            be = 1;  
////        newOpts.inSampleSize = be;//设置缩放比例  
////        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
////        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
//        return compressImage(srcPath);//压缩好比例大小后再进行质量压缩  
//    }
//	
//	
//	public static String resizeImage(String fileName){
//		if(fileName==null || "".equals(fileName))
//			return "";
//	
//		Bitmap bitmap = getimage(fileName);
//	    int width = bitmap.getWidth();
//	    int height = bitmap.getHeight();
//	    XCApplication.base_log.i(XCBaseConfig.TAG_SYSTEM_OUT, "按比例缩小后宽度--" + width);
//	    XCApplication.base_log.i(XCBaseConfig.TAG_SYSTEM_OUT, "按比例缩小后高度--" + height);
//	    
//	    File dir = new File(XCBaseConfig.CACHE_DIRECTORY);
//	    if (!dir.exists()) {
//	        dir.mkdir();
//	    }
//	    String newFileName = XCBaseConfig.CACHE_DIRECTORY + System.currentTimeMillis() + ".jpg";
//	    File f = new File(newFileName);
//	    try {
//	        f.createNewFile();
//	        FileOutputStream fOut = null;
//	        fOut = new FileOutputStream(f);  
//	        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//	        fOut.flush();  
//	        fOut.close();  
//	    } catch (IOException e1) {
//	        f = null;
//	        e1.printStackTrace();
//	        return "";
//	    } 
//	    return newFileName;
//	}
//	
//	
//	public static Bitmap loadImageFromUrl(String url) throws IOException {
//		 XCApplication.base_log.i(XCBaseConfig.TAG_SYSTEM_OUT,url);
//		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//			File dir = new File(XCBaseConfig.CACHE_DIRECTORY);
//			if(!dir.exists()){
//				dir.mkdir();
//			}
//			File f = new File(XCBaseConfig.CACHE_DIRECTORY+UtilMd5.MD5Encode(url));
//			if(f.exists()){
////				FileInputStream fis = new FileInputStream(f);
////				Drawable d = Drawable.createFromStream(fis, "src");
//				Bitmap bitmap = getimage(f);
//				 XCApplication.base_log.i(XCBaseConfig.TAG_SYSTEM_OUT,"Load image from SD :" + url);
//				return bitmap;
//			}
//			URL m = new URL(url);
//			InputStream i = (InputStream) m.getContent();
//			DataInputStream in = new DataInputStream(i);
//			FileOutputStream out = new FileOutputStream(f);
//			byte[] buffer = new byte[1024];
//			int   byteread=0;
//			while ((byteread = in.read(buffer)) != -1) {
//				out.write(buffer, 0, byteread);
//			}
//			 XCApplication.base_log.i(XCBaseConfig.TAG_SYSTEM_OUT,"Save image to SD :" + url);
//			in.close();
//			out.close();
//			return loadImageFromUrl(url);
//		}
//		return null;
//	}
//	
//	public static int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//    // Raw height and width of image
//    final int height = options.outHeight;
//    final int width = options.outWidth;
//    int inSampleSize = 1;
//
//    if (height > reqHeight || width > reqWidth) {
//
//        final int halfHeight = height / 2;
//        final int halfWidth = width / 2;
//
//        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//        // height and width larger than the requested height and width.
//        while ((halfHeight / inSampleSize) > reqHeight
//                && (halfWidth / inSampleSize) > reqWidth) {
//            inSampleSize *= 2;
//        }
//    }
//
//    return inSampleSize;
//}
//	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
//	        int reqWidth, int reqHeight) {
//
//	    // First decode with inJustDecodeBounds=true to check dimensions
//	    final BitmapFactory.Options options = new BitmapFactory.Options();
//	    options.inJustDecodeBounds = true;
//	    BitmapFactory.decodeResource(res, resId, options);
//
//	    // Calculate inSampleSize
//	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//	    // Decode bitmap with inSampleSize set
//	    options.inJustDecodeBounds = false;
//	    return BitmapFactory.decodeResource(res, resId, options);
//	}
//}
