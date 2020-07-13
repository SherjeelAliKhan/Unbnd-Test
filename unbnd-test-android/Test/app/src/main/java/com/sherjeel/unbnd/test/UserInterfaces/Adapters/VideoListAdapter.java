package com.sherjeel.unbnd.test.UserInterfaces.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sherjeel.unbnd.test.Configuration.ImageThumbnails;
import com.sherjeel.unbnd.test.ParseJson.VideoDetail;
import com.sherjeel.unbnd.test.R;
import com.sherjeel.unbnd.test.Utilities.ImageUtilities;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class VideoListAdapter extends ArrayAdapter <VideoDetail> {

    private Context _context;
    private int resource;
    private  ArrayList<VideoDetail> videosDetails;

    public VideoListAdapter(Context context, int resource, ArrayList<VideoDetail> videosDetails) {
        super(context, resource, videosDetails);
        this._context = context;
        this.resource = resource;
        this.videosDetails = videosDetails;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlaceHolder placeHolder = null;

        if (row == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(_context);
            row = layoutInflater.inflate(resource, parent, false);

            placeHolder = new PlaceHolder();
            placeHolder.imageView = row.findViewById(R.id.imageView);
            placeHolder.titleView = row.findViewById(R.id.title);
            placeHolder.rowNumber = position;

            row.setTag(placeHolder);
        } else {
            placeHolder = (PlaceHolder) row.getTag();
            System.out.println(position);
        }

        ImageUtilities imageUtilities = new ImageUtilities();
        File imageFile = imageUtilities.loadImage(_context, ImageThumbnails.fileName + position);
        if (imageFile != null && imageFile.exists()) {
            Picasso.get().load(imageUtilities.loadImage(_context, ImageThumbnails.fileName + position)).into(placeHolder.imageView);
        } else {
            Picasso.get().load(videosDetails.get(position).getThumbnail()).fit().centerCrop().into(placeHolder.imageView);
            imageUtilities.saveImage(_context, videosDetails.get(position).getThumbnail(), ImageThumbnails.fileName + position);
        }
        placeHolder.titleView.setText(videosDetails.get(position).getTitle());
        return row;
    }

    public static Drawable loadImage(String url, String fileName) {
        try {
            InputStream inputStream = (InputStream) new URL(url).getContent();
            Drawable drawable = Drawable.createFromStream(inputStream, fileName);
            return drawable;
        } catch (Exception e) {
            return null;
        }
    }

    static class PlaceHolder{
        ImageView imageView;
        TextView titleView;
        public int rowNumber;
    }
}