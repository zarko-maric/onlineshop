package zarko.maric.onlineshop;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HistoryModel> nizPodataka;

    public HistoryAdapter(Context context) {
        this.context = context;
        nizPodataka = new ArrayList<>();
    }

    public void add(HistoryModel podatak){
        nizPodataka.add(podatak);
        notifyDataSetChanged();
    }

    public void clearItems(){
        nizPodataka.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return nizPodataka.size();
    }

    @Override
    public Object getItem(int i) {
        HistoryModel podatak = null;
        try {
            podatak =  nizPodataka.get(i);
        }catch(Exception e){
            e.printStackTrace();
        }
        return podatak;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_history, null);

            viewHolder = new ViewHolder();
            viewHolder.status = view.findViewById(R.id.status);
            viewHolder.price = view.findViewById(R.id.price);
            viewHolder.date = view.findViewById(R.id.date);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        HistoryModel podatak = (HistoryModel) getItem(i);
        viewHolder.status.setText(podatak.getStatus());
        viewHolder.price.setText(podatak.getPrice());
        viewHolder.date.setText(podatak.getDate());

        return view;
    }


    private class ViewHolder{
        TextView status;
        TextView price;
        TextView date;

    }
}