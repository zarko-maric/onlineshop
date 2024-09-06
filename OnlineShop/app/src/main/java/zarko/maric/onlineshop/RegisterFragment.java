package zarko.maric.onlineshop;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import zarko.maric.onlineshop.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_register, container, false);

        httpHelper hp = new httpHelper();
        JSONObject userJson = new JSONObject();

        Button register1=v.findViewById(R.id.register1);

        EditText editText= v.findViewById(R.id.password);

        dbHelper db = new dbHelper(v.getContext());

        register1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox isAdmin = v.findViewById(R.id.admin);

                EditText usernameInput = v.findViewById(R.id.username);
                EditText emailInput = v.findViewById(R.id.email);
                EditText passwordInput = v.findViewById(R.id.password);

                String username = usernameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                String input = editText.getText().toString();
                if(input.isEmpty()){
                    editText.setError("This field is required");
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject userJson = new JSONObject();

                            userJson.put("username", username);
                            userJson.put("email", email);
                            userJson.put("password", password);
                            userJson.put("isAdmin", isAdmin.isChecked());
                            boolean response = hp.postJSONObjectFromURL(httpHelper.URL + "/users",userJson);
                            if(!response){
                                Toast.makeText(view.getContext(), "User already exists!", Toast.LENGTH_SHORT).show();
                            }else {
                                User user = new User(username, email, password, isAdmin.isChecked());
                                db.insertUser(user);
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                MainActivity.activeUser = user;

                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        return v;
    }
}
