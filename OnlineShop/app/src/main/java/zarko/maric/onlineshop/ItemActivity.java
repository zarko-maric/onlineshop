package zarko.maric.onlineshop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONObject;

public class ItemActivity extends AppCompatActivity {

    private Item adapter;
    TextView textCategory;
    Button back;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(SaleService.change_sale.equals(intent.getAction())) {
                recreate();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(SaleService.change_sale));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CustomAdapter adapter = new CustomAdapter(this);

        ListView list = findViewById(R.id.listCategory);
        list.setAdapter(adapter);
        back = findViewById(R.id.back);
        textCategory = findViewById(R.id.category);

        String category = getIntent().getStringExtra("category");

        textCategory.setText(category);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dbHelper db = new dbHelper(this);
        httpHelper http = new httpHelper();

        Item[] item = db.readItems(category);

        for (Item r : item) {
            adapter.add(r);
        }

        if (MainActivity.activeUser.isAdmin()) {



            EditText addTextName = findViewById(R.id.add_name);
            EditText addTextPrice = findViewById(R.id.add_price);
            findViewById(R.id.admin).setVisibility(View.VISIBLE);
            findViewById(R.id.add_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject newItem = new JSONObject();
                                newItem.put("name", addTextName.getText().toString());
                                newItem.put("price", addTextPrice.getText().toString());
                                newItem.put("category", category);
                                newItem.put("imageName", "Plant");
                                boolean post = http.postJSONObjectFromURL(httpHelper.URL + "/item", newItem);
                                if (post) {
                                    db.insertItem(new Item(addTextPrice.getText().length(),category, addTextName.getText().toString(),  "Plant"));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(v.getContext(), "Added new item", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {

                            }
                        }
                    }).start();
                }
            });
        }
    }
}