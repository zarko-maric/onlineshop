package zarko.maric.onlineshop;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

public class PasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String username = getIntent().getExtras().getString("username");
        dbHelper db = new dbHelper(this);

        EditText currentPassword = findViewById(R.id.current_password);
        EditText newPassword = findViewById(R.id.new_password);

        User user = db.readUser(username);

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            httpHelper http = new httpHelper();
                            JSONObject request = new JSONObject();
                            request.put("username", username);
                            request.put("oldPassword", currentPassword.getText().toString());
                            request.put("newPassword", newPassword.getText().toString());
                            boolean post = http.putJSONObjectFromURL(httpHelper.URL + "/password",request);
                            if(!post){
                                Toast.makeText(v.getContext(), "Invalid password", Toast.LENGTH_SHORT).show();
                            }else{
                                db.updatePassword(username, newPassword.getText().toString());
                                finish();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}