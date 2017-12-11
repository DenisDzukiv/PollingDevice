package androidproject.pollingdevice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidproject.pollingdevice.R;
import androidproject.pollingdevice.model.Device;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DeviceAdapter extends BaseAdapter{
    Context ctx;
    LayoutInflater layoutInflater;
    List<Device> objects;

    public DeviceAdapter(Context context, List<Device> devices){
        ctx = context;
        objects = devices;
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    //кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    //элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    //id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }
    // товар по позиции
    public Device getDevice(int position) {
        return ((Device) getItem(position));
    }

    // пункт списка
    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        View view = convertview;
        if(view == null) view = layoutInflater.inflate(R.layout.item_device, parent, false);

        Device device = getDevice(position);

        //((TextView) view.findViewById(R.id.tvDevId)).setText((int) device.getDevId());
        ((TextView) view.findViewById(R.id.tvDevName)).setText(device.getDevName());
        ((TextView) view.findViewById(R.id.tvDevType)).setText(device.getTypeDevice().getTypeName());
        ((TextView) view.findViewById(R.id.tvDevType)).setText(device.getTypeDevice().getTypeName());


        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cbBox);
        // присваиваем чекбоксу обработчик
        checkBox.setOnCheckedChangeListener(myCheckChangeList);
        // пишем позицию
        checkBox.setTag(position);
        // заполняем данными из товаров: в корзине или нет
        checkBox.setChecked(device.getBox());
        return view;
    }

    // содержимое корзины
    public ArrayList<Device> gettBox(){
        ArrayList<Device> box = new ArrayList<>();
        for(Device d: objects){
            if(d.getBox()) box.add(d);
        }
        return box;
    }
    // обработчик для чекбоксов
    OnCheckedChangeListener myCheckChangeList = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // меняем данные товара (в корзине или нет)

            getDevice((int) buttonView.getTag()).setBox(isChecked);
        }
    };

}
