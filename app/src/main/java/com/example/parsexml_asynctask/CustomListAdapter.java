package com.example.parsexml_asynctask;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
public class CustomListAdapter extends ArrayAdapter<Data> {
    ArrayList<Data> ListData;
    Context context;
    int resource;
    public CustomListAdapter(Context context, int resource,ArrayList<Data> list) {
        super(context, resource, list);
        this.context = context;
        this.resource=resource;
        this.ListData = list;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_list_layout,null,true);
        }
        Data data = getItem(position);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);//image
        Picasso.with(context).load(data.getUrl_image()).into(imageView);
        TextView textName = (TextView) convertView.findViewById(R.id.txtName);//name
        textName.setText(data.getName());
        TextView textArtist = (TextView) convertView.findViewById(R.id.txtArtist);//artist(s)
        textArtist.setText(data.getArtist());
        TextView textPrice = (TextView) convertView.findViewById(R.id.txtPrice);//price
        textPrice.setText(Float.toString(data.getPrice())+" $");
        return convertView;
    }
}
