package androidproject.pollingdevice.View;

import android.content.Context;



public class EditTextDevice extends android.support.v7.widget.AppCompatEditText {

    public EditTextDevice(Context context) {
        super(context);
    }
    private int id;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}