package zarko.maric.onlineshop;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import zarko.maric.onlineshop.R;


public class MainActivity extends AppCompatActivity {

    public static User activeUser = new User();

    public static boolean saleStarted = false;
    public static boolean isLoggedIn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button login = findViewById(R.id.log_in);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment lf = LoginFragment.newInstance("String", "String");
                getSupportFragmentManager().beginTransaction().add(R.id.loginscreen, lf).commit();
                findViewById(R.id.log_in).setVisibility(View.INVISIBLE);
                findViewById(R.id.register).setVisibility(View.INVISIBLE);
            }
        });

        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment rf = RegisterFragment.newInstance("String", "String");
                getSupportFragmentManager().beginTransaction().add(R.id.loginscreen, rf).commit();
                findViewById(R.id.log_in).setVisibility(View.INVISIBLE);
                findViewById(R.id.register).setVisibility(View.INVISIBLE);
            }
        });

        Intent serviceIntent = new Intent(this, SaleService.class);
        startService(serviceIntent);

        Intent from = getIntent();


    }
}
