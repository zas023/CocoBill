package com.copasso.cocobill.utils;

import android.graphics.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhouas666 on 2017/12/26.
 */
public class ImageUtils {

    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     * @param photoName
     * @param path
     */
    public static String savePhoto(Bitmap photoBitmap, String path,
                                   String photoName) {
        String localPath = null;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File photoFile = new File(path+"/"+photoName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                            fileOutputStream)) { // 转换完成
                        localPath = photoFile.getPath();
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                localPath = null;
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                localPath = null;
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                        fileOutputStream = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return localPath;
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }
}
