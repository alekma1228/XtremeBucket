package com.brainyapps.xtremebucketlist.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.webkit.MimeTypeMap;

import com.brainyapps.xtremebucketlist.utility.ResourceUtil;
import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class FileModel {
    public static ParseFile createParseFile(String fileName, String filePath) {
        ParseFile parseFile = null;
        Bitmap bm = ResourceUtil.decodeSampledBitmapFromFile(filePath, UserModel.THUMBNAIL_SIZE);
        parseFile = createParseFile(fileName, bm);
        return parseFile;
    }

    public static ParseFile createParseAvatarFile(String fileName, String filePath) {
        ParseFile parseFile = null;
        Bitmap bm = ResourceUtil.decodeSampledBitmapFromFile(filePath, UserModel.AVATAR_SIZE);
        parseFile = createParseFile(fileName, bm);
        return parseFile;
    }

    public static ParseFile createParseFile(String fileName, Bitmap bitmap) {
        ParseFile parseFile = null;
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            parseFile = new ParseFile(fileName, bitmapdata);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return parseFile;
    }

    public static ParseFile createParseFile(String fileName, Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable)drawable).getBitmap();

        } else if (drawable instanceof VectorDrawable){
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        ParseFile parseFile = null;
        if (bitmap != null) {
            parseFile = createParseFile(fileName, bitmap);
            try {
                parseFile.save();
                bitmap.recycle();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        return parseFile;
    }

    // create video file
    public static ParseFile createPDFParseFile(String fileName, String filePath) {
        ParseFile parseFile = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(new File(filePath));

            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);

            byte[] videoBytes = baos.toByteArray();
            parseFile = new ParseFile(fileName, videoBytes, getMimeType(filePath));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return parseFile;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
