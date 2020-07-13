package com.sherjeel.unbnd.test.Configuration;

import android.graphics.Bitmap;

public class ImageThumbnails {

    public static String fileName = "thumbnail"; // thumbnail1, thumbnail2, ...

    public static String folderName = "images";
    public static String fileExtension = ".jpg";

    public static boolean sizeReduced = true;
    public static int maximumSize = 500;

    public static int compressedQuality = 100;
    public static Bitmap.CompressFormat compressedFormat = Bitmap.CompressFormat.JPEG;

    public static int transitionRequestCode = 100;
}
