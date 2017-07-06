package com.jpmorgan.autocafe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jpmorgan.autocafe.bean.MenuItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tapas on 2/14/2017.
 */
public class MenuFragment extends Fragment {
    public final Map<String, List<MenuItem>> PLACES_BY_BEACONS;
    private MyCustomAdapter dataAdapter = null;
    private TextView vendor;
    private ListView listView;
    private int totalPrice;
    private TextView totalBill;

    // TODO: replace "<major>:<minor>" strings to match your own beacons.
    {
        Map<String, List<MenuItem>> placesByBeacons = new HashMap<>();
        placesByBeacons.put("12635:6578", new ArrayList<MenuItem>() {{
//            add(new MenuItem("", "Sodexo", false));
            // read as: "Heavenly Sandwiches" is closest
            // to the beacon with major 22504 and minor 48827
            add(new MenuItem("Sodexo", "50", "Paneer Meal", false));
            // "Green & Green Salads" is the next closest
            add(new MenuItem("Sodexo", "60", "Chicken Biryani", false));
            add(new MenuItem("Sodexo", "60", "Non-Veg Meal", false));
            add(new MenuItem("Sodexo", "30", "Veg Noodles", false));
            add(new MenuItem("Sodexo", "30", "Maggie", false));
            add(new MenuItem("Sodexo", "30", "Set Dosa", false));
            // "Mini Panini" is the furthest away
        }});
        placesByBeacons.put("1:2", new ArrayList<MenuItem>() {{
//            add(new MenuItem("", "Oreo Land Juice Centre", false));
            add(new MenuItem("Oreo Land Juice Centre", "30", "Papaya Juice", false));
            add(new MenuItem("Oreo Land Juice Centre", "30", "Banana Shake", false));
            add(new MenuItem("Oreo Land Juice Centre", "30", "Butter Fruit", false));
        }});

        placesByBeacons.put("1:3", new ArrayList<MenuItem>() {{
//            add(new MenuItem("", "Vaishnow Chaat Bhandar", false));
            add(new MenuItem("Vaishnow Chaat Bhandar", "20", "Papadi Chat", false));
            add(new MenuItem("Vaishnow Chaat Bhandar", "20", "Chat Masala", false));
            add(new MenuItem("Vaishnow Chaat Bhandar", "20", "Samosa Chat", false));
        }});

        placesByBeacons.put("1:4", new ArrayList<MenuItem>() {{
//            add(new MenuItem("", "Food City", false));
            add(new MenuItem("Food City", "50", "Veg Biryani", false));
            add(new MenuItem("Food City", "60", "Non Veg Biryani", false));
            add(new MenuItem("Food City", "30", "Veg Noodles", false));
            add(new MenuItem("Food City", "30", "Maggie", false));
            add(new MenuItem("Food City", "30", "Set Dosa", false));
        }});


        PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        vendor = (TextView) view.findViewById(R.id.vendor);
        totalBill = (TextView) view.findViewById(R.id.totalBill);

        List<MenuItem> places = getAllVendorsAndMenus(PLACES_BY_BEACONS);
        // TODO: update the UI here
        Log.d("tapas", "Nearest places: " + places);
        Log.d("tapas", "Nearest places: " + places);

        vendor.setText("Overall Menu");

//                    places = showOnlyItems(places);
//                    places.remove(0);
//                    ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, places);
//                    listView.setAdapter(arrayAdapter);

        //Generate list View from ArrayList
        displayListView(view, places);

        checkButtonClick(view);


//        vendor.setText("" + places.get(0));
//
////                    places = showOnlyItems(places);
////                    places.remove(0);
//        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, places);
//        listView.setAdapter(arrayAdapter);
        return view;
    }

    private List<MenuItem> getAllVendorsAndMenus(Map<String, List<MenuItem>> placesByBeacons) {
        List<MenuItem> places = new ArrayList<>();
        Collection<List<MenuItem>> strings = placesByBeacons.values();
        for (List<MenuItem> eachMenu : strings) {
            for (MenuItem menuItem : eachMenu) {
                places.add(menuItem);
            }
        }
        return places;
    }

    private void displayListView(View view, List<MenuItem> menuItemList) {
        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(getActivity(),
                R.layout.country_info, menuItemList);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                MenuItem menuItem = (MenuItem) parent.getItemAtPosition(position);


//                Toast.makeText(getActivity(),
//                        "Clicked on Row: " + menuItem.getName(),
//                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private class MyCustomAdapter extends ArrayAdapter<MenuItem> {

        private ArrayList<MenuItem> menuItemList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               List<MenuItem> menuItemList) {
            super(context, textViewResourceId, menuItemList);
            this.menuItemList = new ArrayList<MenuItem>();
            this.menuItemList.addAll(menuItemList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MyCustomAdapter.ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.country_info, null);

                holder = new MyCustomAdapter.ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.price);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        MenuItem menuItem = (MenuItem) cb.getTag();
//                        Toast.makeText(getActivity(),
//                                "Clicked on Checkbox: " + cb.getText() +
//                                        " is " + cb.isChecked() +"Price: "+menuItem.getPrice(),
//                                Toast.LENGTH_LONG).show();
                        int currentPrice = 0;
                        try {
                            currentPrice = Integer.parseInt(menuItem.getPrice());
                        } catch (Exception e) {

                        }
                        if (cb.isChecked()) {
                            totalPrice = totalPrice + currentPrice;
                        } else {
                            totalPrice = totalPrice - currentPrice;
                        }
                        totalBill.setText("Amount to pay: " + totalPrice + "/-");
                        menuItem.setSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (MyCustomAdapter.ViewHolder) convertView.getTag();
            }

            MenuItem menuItem = menuItemList.get(position);
            holder.code.setText(" (" + menuItem.getPrice() + ")");
            holder.name.setText(menuItem.getName());
            holder.name.setChecked(menuItem.isSelected());
            holder.name.setTag(menuItem);

            return convertView;

        }

    }

    private void checkButtonClick(View view) {


        Button myButton = (Button) view.findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");
                String vendorName = "";
                ArrayList<MenuItem> menuItemList = dataAdapter.menuItemList;
                for (int i = 0; i < menuItemList.size(); i++) {
                    MenuItem menuItem = menuItemList.get(i);
                    if (menuItem.isSelected()) {
                        responseText.append("\n" + menuItem.getName());
                    }

                    if (!TextUtils.isEmpty(vendorName) && !vendorName.contains(menuItem.getVenderName())) {
                        vendorName = vendorName+ " , " + menuItem.getVenderName();
                    } else {
                        vendorName = menuItem.getVenderName();
                    }
                }

                if (totalPrice > 0) {
                    Intent intent = new Intent(getActivity(), PaymentScreen.class);
                    intent.putExtra("totalPrice", totalPrice);
                    intent.putExtra("vendorName", vendorName);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Please select items from Menu !!!", Toast.LENGTH_SHORT).show();
                }

//                Toast.makeText(getActivity(),
//                        responseText, Toast.LENGTH_LONG).show();

            }
        });

    }
}
