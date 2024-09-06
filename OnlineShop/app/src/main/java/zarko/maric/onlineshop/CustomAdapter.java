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

import java.text.DecimalFormat;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Locale;

public class CustomAdapter extends BaseAdapter {

    private dbHelper db;
    private Context context;
    private ArrayList<Item> nizPodataka;

    public CustomAdapter(Context context) {
        this.context = context;
        this.db =new dbHelper(context);
        nizPodataka = new ArrayList<>();
    }

    public void add(Item podatak){
        nizPodataka.add(podatak);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return nizPodataka.size();
    }

    @Override
    public Object getItem(int i) {
        Item podatak = null;
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
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_item,null);

            viewHolder = new ViewHolder();
            viewHolder.cena = view.findViewById(R.id.cena);
            viewHolder.naziv = view.findViewById(R.id.naziv);
            viewHolder.slika = view.findViewById(R.id.slika);
            viewHolder.add = view.findViewById(R.id.add);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        Item podatak = (Item) getItem(i);
        int imageID = context.getResources().getIdentifier(podatak.getSlike(), "drawable", context.getPackageName());

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double cena = Double.parseDouble(decimalFormat.format(podatak.getCena() *(SaleService.isSale ? 0.8 : 1)));

        int price = (int) cena;
        viewHolder.naziv.setText(podatak.getNaziv());
        viewHolder.cena.setText((SaleService.isSale ? "Sale ":"") + "RSD" + cena);
        viewHolder.slika.setImageResource(imageID);
        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "WAITING FOR DELIVERY";
                LocalDate localDate = LocalDate.now();
                String date = String.valueOf(localDate);
                HistoryModel history = new HistoryModel(status, date, price, MainActivity.activeUser.getUsername());
                Toast.makeText(v.getContext(), podatak.getNaziv() + " je dodat u korpu.",Toast.LENGTH_SHORT).show();
                db.insertHistory(history);
            }
        });
        return view;
    }

    private class ViewHolder{
        ImageView slika;
        TextView cena;
        TextView naziv;
        Button add;

    }
}