package zarko.maric.onlineshop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PurchaseHistoryActivity extends AppCompatActivity {

    private HistoryAdapter adapter;

    Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_purchase_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tv=findViewById(R.id.prazno);
        tv.setVisibility(View.INVISIBLE);

        HistoryAdapter historyAdapter= new HistoryAdapter(this);

        ListView list=findViewById(R.id.lista);
        list.setAdapter(historyAdapter);

        dbHelper db = new dbHelper(this);

        HistoryModel[] history = db.readHistory(MainActivity.activeUser.getUsername());

        if(history!=null) {
            for (HistoryModel r : history) {
                historyAdapter.add(r);
            }
        }


        int cnt=historyAdapter.getCount();

        if(cnt==0){
            list.setEmptyView(findViewById(R.id.prazno));
        }

    }
}