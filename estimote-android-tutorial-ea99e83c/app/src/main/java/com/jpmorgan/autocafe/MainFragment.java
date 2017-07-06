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

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.jpmorgan.autocafe.bean.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainFragment extends Fragment {
    private MyCustomAdapter dataAdapter = null;
    public  final Map<String, List<MenuItem>> PLACES_BY_BEACONS;
    private TextView vendor;
    private ListView listView;
    private int totalPrice;
    private TextView totalBill;
    private BeaconManager beaconManager;
    private Region region;


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

    private List<MenuItem> placesNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
            return PLACES_BY_BEACONS.get(beaconKey);
        }
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_main, container, false);

        vendor = (TextView) view.findViewById(R.id.vendor);
        totalBill = (TextView) view.findViewById(R.id.totalBill);
//        listView = (ListView) view.findViewById(R.id.listView);
        beaconManager = new BeaconManager(getActivity());
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    List<MenuItem> places = placesNearBeacon(nearestBeacon);
                    // TODO: update the UI here
                    Log.d("tapas", "Nearest places: " + places);
                    if (places !=null && places.size()>0)
                        vendor.setText("" + places.get(0).getVenderName());

//                    places = showOnlyItems(places);
//                    places.remove(0);
//                    ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, places);
//                    listView.setAdapter(arrayAdapter);

                    //Generate list View from ArrayList
                    displayListView(view, places);

                    checkButtonClick(view);
                }
            }
        });
////        region = new Region("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
        region = new Region("ranged region", UUID.fromString("8492E75F-4FD6-469D-B132-043FE94921D8"), null, null);


        return view;
    }

    private List<String> showOnlyItems(List<String> places) {
        if (places != null && !places.isEmpty()) {
            if (!TextUtils.isEmpty(places.get(0))) {
                places.remove(0);
            }
        }
        return places;
    }

    @Override
    public void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(getActivity());
        if (beaconManager != null) {
            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    if (region != null)
                        beaconManager.startRanging(region);
                }
            });

        }

    }

    @Override
    public void onPause() {
        beaconManager.stopRanging(region);

        super.onPause();
    }


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.country_info, null);

                holder = new ViewHolder();
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
                holder = (ViewHolder) convertView.getTag();
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

                ArrayList<MenuItem> menuItemList = dataAdapter.menuItemList;
                String vendorName = "";
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
