package zarko.maric.onlineshop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_category, container, false);

        ArrayList<String> listItems=new ArrayList<>();

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(v.getContext(),android.R.layout.simple_list_item_1, listItems);

        ListView listView=v.findViewById(R.id.lista);

        listView.setAdapter(adapter);

        final LinearLayout linearLayout = v.findViewById(R.id.admin);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),ItemActivity.class);
                intent.putExtra("category", (String) parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });

        dbHelper helper = new dbHelper(v.getContext());
        httpHelper http = new httpHelper();
        String[] categories = helper.readCategories();

        if(MainActivity.activeUser.isAdmin()) {
            v.findViewById(R.id.admin).setVisibility(View.VISIBLE);
            EditText categoryText = v.findViewById(R.id.add_category);
            v.findViewById(R.id.add_cat).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                JSONObject category = new JSONObject();
                                category.put("name",categoryText.getText().toString());
                                boolean post = http.postJSONObjectFromURL(httpHelper.URL + "/category", category);
                                if(post){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(v.getContext(), "Category added", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
        }

        for(String category : categories) {
            adapter.add(category);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        JSONObject newCategory = new JSONObject();
                        newCategory.put("name", category);
                        http.postJSONObjectFromURL(httpHelper.URL + "/category", newCategory);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        EditText itemName = v.findViewById(R.id.add_name);
        EditText itemPrice = v.findViewById(R.id.add_price);
        EditText itemCategory = v.findViewById(R.id.add_category_item);
        v.findViewById(R.id.add_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject item = new JSONObject();
                            item.put("name", itemName.getText().toString());
                            item.put("price", itemPrice.getText().toString());
                            item.put("category", itemCategory.getText().toString());
                            item.put("image", "Krasula");
                            boolean post = http.postJSONObjectFromURL(httpHelper.URL + "/item", item);
                            if(post){
                                helper.insertItem(new Item(itemPrice.getText().length(), itemName.getText().toString(), "Krasula", itemCategory.getText().toString()));
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(v.getContext(),  "Added new item", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        return v;
    }
}