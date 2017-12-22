package androidproject.pollingdevice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import androidproject.pollingdevice.R;
import androidproject.pollingdevice.model.TypeDevice;

/**
 * Created by zhukivsky_ds on 17.12.2017.
 */

public class TypeAdapter extends ArrayAdapter<TypeDevice> {
    Context ctx;
    LayoutInflater layoutInflater;
    List<TypeDevice> objects;
    ////////////
    public TypeAdapter(Context context, int textViewResourceId,
                       List<TypeDevice> typeDeviceList) {
        super(context, textViewResourceId, typeDeviceList);
        ctx = context;
        objects = typeDeviceList;
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

        View view = convertView;
        if(view == null) view = layoutInflater.inflate(R.layout.item_device, parent, false);


        TypeDevice typeDevice = getTypeDevice(position);
        TextView type = (TextView) view.findViewById(R.id.text);
        type.setText(typeDevice.getTypeName());

        return view;
    }

    public TypeDevice getTypeDevice(int position){
        return ((TypeDevice) getItem(position));
    }
}


    ///////////

  /*  public TypeAdapter(Context context, List<TypeDevice> typeDeviceList){
        ctx = context;
        objects = typeDeviceList;
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public TypeDevice getTypeDevice(int position){
        return ((TypeDevice) getItem(position));
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        View view = convertview;
        if(view == null) view = layoutInflater.inflate(R.layout.item_device, parent, false);
        TypeDevice typeDevice = getTypeDevice(position);


        TextView type = (TextView) view.findViewById(R.id.text);
        type.setText(typeDevice.getTypeName()[position]);
        //TextView type = (TextView) view.findViewById(R.id.text);
        ((TextView) view.findViewById(R.id.text)).setText(typeDevice.getTypeName());
        ((TextView) view.findViewById(R.id.id)).setText((int) typeDevice.getTypeId());

        return view;
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

        LayoutInflater inflater = getLayoutInflater();
        View row = inflater.inflate(R.layout.row, parent, false);
        TextView label = (TextView) row.findViewById(R.id.weekofday);
        label.setText(dayOfWeek[position]);

        ImageView icon = (ImageView) row.findViewById(R.id.icon);

        if (dayOfWeek[position] == "Котопятница"
                || dayOfWeek[position] == "Субкота") {
            icon.setImageResource(R.drawable.paw_on);
        } else {
            icon.setImageResource(R.drawable.ic_launcher);
        }
        return row;
    }
    public void setDropDownViewResource(int dropDownLayout) { *//* compiled code *//* }

}*/
