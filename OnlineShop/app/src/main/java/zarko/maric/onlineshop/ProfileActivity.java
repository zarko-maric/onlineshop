package zarko.maric.onlineshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import zarko.maric.onlineshop.R;

public class ProfileActivity extends AppCompatActivity {

    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        username = getIntent().getExtras().getString(AccountFragment.ARG_USERNAME);
        String email = getIntent().getExtras().getString(AccountFragment.ARG_EMAIL);


        TextView usernameTextView = findViewById(R.id.username);
        TextView emailTextView = findViewById(R.id.email);

        usernameTextView.setText(username);
        emailTextView.setText(email);

        Button password=findViewById(R.id.password);
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,PasswordActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        Button endSession=findViewById(R.id.end_session);
        endSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, zarko.maric.onlineshop.MainActivity.class);
                startActivity(intent);
            }
        });

    }
}