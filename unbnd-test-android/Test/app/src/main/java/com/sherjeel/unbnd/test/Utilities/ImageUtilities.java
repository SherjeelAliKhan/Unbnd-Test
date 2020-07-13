package com.sherjeel.unbnd.test.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.sherjeel.unbnd.test.Configuration.ImageThumbnails;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ImageUtilities {

    public void saveImage(Context context, String url, String fileName) {
        Picasso.get().load(url).into(picassoImageTarget(context, fileName));
    }

    public File loadImage(Context context, String fileName) {
        String folderName = ImageThumbnails.folderName;
        String extension = ImageThumbnails.fileExtension;
        final FileUtilities fileUtilities = new FileUtilities();
        return fileUtilities.loadFile(context, folderName, fileName + extension);
    }


    public Bitmap getBitmap(Context context, String imageName) {
        File file = loadImage(context, imageName);
        String filePath = file.getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        return bitmap;
    }

    public void saveBitmap(Context context, final Bitmap bitmap, String imageName) {
        try {
            String folderName = ImageThumbnails.folderName;
            String extension = ImageThumbnails.fileExtension;
            final FileUtilities fileUtilities = new FileUtilities();
            final File imageFile = fileUtilities.createFile(context, folderName, imageName + extension);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            if (ImageThumbnails.sizeReduced) {
                Bitmap reducedSize = getResizedBitmap(bitmap, ImageThumbnails.maximumSize);
                reducedSize.compress(ImageThumbnails.compressedFormat, ImageThumbnails.compressedQuality, fileOutputStream);
            } else {
                bitmap.compress(ImageThumbnails.compressedFormat, ImageThumbnails.compressedQuality, fileOutputStream);
            }
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e("ImageUtilities", "Error:\n" + e);
            e.printStackTrace();
        }
    }

    private Target picassoImageTarget(final Context context, final String imageName) {
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String folderName = ImageThumbnails.folderName;
                            String extension = ImageThumbnails.fileExtension;
                            final FileUtilities fileUtilities = new FileUtilities();
                            final File imageFile = fileUtilities.createFile(context, folderName, imageName + extension);
                            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                            if (ImageThumbnails.sizeReduced) {
                                Bitmap reducedSize = getResizedBitmap(bitmap, ImageThumbnails.maximumSize);
                                reducedSize.compress(ImageThumbnails.compressedFormat, ImageThumbnails.compressedQuality, fileOutputStream);
                            } else {
                                bitmap.compress(ImageThumbnails.compressedFormat, ImageThumbnails.compressedQuality, fileOutputStream);
                            }
                            fileOutputStream.close();
                        } catch (IOException e) {
                            Log.e("ImageUtilities", "Error:\n" + e);
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e("ImageUtilities", "Error:\n" + e);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {
                }
            }
        };
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
