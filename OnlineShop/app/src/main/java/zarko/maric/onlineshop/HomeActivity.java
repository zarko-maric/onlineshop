package zarko.maric.onlineshop;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import zarko.maric.onlineshop.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private AccountFragment accountFragment;
    private CategoryFragment categoryFragment;
    private WelcomeFragment welcomeFragment;
    private Button homeButton;
    private Button menuButton;
    private Button accountButton;
    private Button bagButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        homeButton = findViewById(R.id.home);
        menuButton = findViewById(R.id.menu);
        accountButton = findViewById(R.id.account);
        bagButton = findViewById(R.id.bag);

        homeButton.setOnClickListener(this);
        accountButton.setOnClickListener(this);
        bagButton.setOnClickListener(this);
        menuButton.setOnClickListener(this);

        String username = MainActivity.activeUser.getUsername();
        String email = MainActivity.activeUser.getEmail();

        welcomeFragment = WelcomeFragment.newInstance(username);
        accountFragment = AccountFragment.newInstance(username, email);
        categoryFragment = CategoryFragment.newInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.content, welcomeFragment).commit();

        dbHelper db = new dbHelper(this);
        Item[] item = db.readItems("all");
        httpHelper http = new httpHelper();
        if(item == null){
            for (int i = 1; i <= 5; i++) {
                db.insertItem(new Item(300, "Aloja Bakeri", "alojabakeri", "Sukulenti"));
                db.insertItem(new Item(550, "Krasula", "krasula", "Sukulenti"));
                db.insertItem(new Item(450, "Eheveria", "eheveria", "Sukulenti"));
                break;
            }
            for (int i = 1; i <= 5; i++) {
                db.insertItem(new Item(1200 , "Veberbauerocereus", "veberbauerocereus", "Kaktusi"));
                db.insertItem(new Item(1100 , "Uebelmania", "uebelmania", "Kaktusi"));
                db.insertItem(new Item(900 , "Pigmaeocereus", "pigmaeocereus", "Kaktusi"));
                break;
            }
            for (int i = 1; i <= 5; i++) {
                db.insertItem(new Item(7800 , "Norfolk", "norfolk", "Bonsai"));
                db.insertItem(new Item(8000 , "Juniper S", "juniper", "Bonsai"));
                db.insertItem(new Item(9000 , "Baby Jade", "babyjade", "Bonsai"));
                break;
            }
            for (int i = 1; i <= 5; i++) {
                db.insertItem(new Item(3200 , "Floribunda", "floribunda", "Ruže"));
                db.insertItem(new Item(2900 , "Grandiflora", "grandiflora", "Ruže"));
                db.insertItem(new Item(3100 , "Ebb Tide", "ebbtide", "Ruže"));
                break;
            }
            for (int i = 1; i <= 5; i++) {
                db.insertItem(new Item(2500 , "Gaura", "gaura", "Cvetnice"));
                db.insertItem(new Item(1100 , "Iberis Sempervirens", "iberissempervirens", "Cvetnice"));
                db.insertItem(new Item(1800 , "Hemerocallis", "hemerocallis", "Cvetnice"));
                break;
            }
        }
    }

    public void onClick(View v){
        int id = v.getId();
        if (id == R.id.account) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, accountFragment).commit();
            menuButton.setBackgroundColor(getResources().getColor(R.color.light_green));
            accountButton.setBackgroundColor(Color.RED);
            homeButton.setBackgroundColor(getResources().getColor(R.color.light_green));

        } else if (id == R.id.home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, welcomeFragment).commit();
            menuButton.setBackgroundColor(getResources().getColor(R.color.light_green));
            homeButton.setBackgroundColor(Color.RED);
            accountButton.setBackgroundColor(getResources().getColor(R.color.light_green));

        } else if (id == R.id.menu) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, categoryFragment).commit();
            menuButton.setBackgroundColor(Color.RED);
            accountButton.setBackgroundColor(getResources().getColor(R.color.light_green));
            homeButton.setBackgroundColor(getResources().getColor(R.color.light_green));
        }


    }
}