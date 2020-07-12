package com.example.eartquake2;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter<Eathquakehelperclass> {

    public EarthquakeAdapter(Context context, ArrayList<Eathquakehelperclass> earthquakes)
    {
        super(context,0,earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView, @NonNull ViewGroup parent) {
       View listitemView=convertView;
       if(listitemView==null)
       {
           listitemView= LayoutInflater.from(getContext()).inflate(R.layout.row,parent,false);
       }

       Eathquakehelperclass earthquake=getItem(position);
       //hooks
        TextView mag=listitemView.findViewById(R.id.mag);
        TextView location=listitemView.findViewById(R.id.place);
        TextView date=listitemView.findViewById(R.id.date);
        TextView time=listitemView.findViewById(R.id.time);

        // Chnaging the background according to magnitude
        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();
        int magnitudeColor = getMagnitudeColor(earthquake.getmMag());
        magnitudeCircle.setColor(magnitudeColor);

        // setting the text feilds

            String Magnitude=Double.toString(earthquake.getmMag());
            mag.setText(Magnitude);


        location.setText(earthquake.getmLocation());

        Date dateobject=new Date(earthquake.getTime());
        String formattedDate=formatDate(dateobject);

        date.setText(formattedDate);
        String formattedtime=formatTime(dateobject);

        time.setText(formattedtime);
        return listitemView;
    }
    private String formatDate(Date dateobject)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateobject);
    }
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    private int getMagnitudeColor(double Magnitude)
    {
        int magnitudeFloor = (int) Math.floor(Magnitude);
        int colorid;
        switch (magnitudeFloor)
        {
            case 0:
            case 1:
                colorid=R.color.magnitude1;
                break;
            case 2: colorid=R.color.magnitude2;
            break;
            case 3: colorid=R.color.magnitude3;
                break;
            case 4: colorid=R.color.magnitude4;
                break;
            case 5: colorid=R.color.magnitude5;
                break;
            case 6: colorid=R.color.magnitude6;
                break;
            case 7: colorid=R.color.magnitude7;
                break;
            case 8: colorid=R.color.magnitude8;
                break;
            case 9: colorid=R.color.magnitude9;
                break;
            default: colorid=R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),colorid);
    }
}
