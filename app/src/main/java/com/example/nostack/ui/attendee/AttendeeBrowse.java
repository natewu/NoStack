package com.example.nostack.ui.attendee;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.nostack.R;
import com.example.nostack.model.Events.Event;
import com.example.nostack.model.Events.EventArrayAdapter;
import com.example.nostack.model.State.UserViewModel;
import com.example.nostack.ui.organizer.OrganizerHome;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AttendeeBrowse extends Fragment{
    private EventArrayAdapter eventArrayAdapter;
    private ListView eventList;
    private ArrayList<Event> dataList;
    private UserViewModel userViewModel;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private Activity activity;


    public AttendeeBrowse(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userViewModel = new ViewModelProvider((AppCompatActivity) getActivity() ).get(UserViewModel.class);
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");
        activity = getActivity();
        dataList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment only once
        View rootView = inflater.inflate(R.layout.fragment_attendee_home_browse, container, false);

        eventList = rootView.findViewById(R.id.listView_yourEvents);
        eventArrayAdapter = new EventArrayAdapter(getContext(),dataList,this);
        eventList.setAdapter(eventArrayAdapter);

        Log.d("AttendeeHome", "UserViewModel: " + userViewModel.getUser().getValue());

        eventsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Event event = document.toObject(Event.class);
                    eventArrayAdapter.addEvent(event);
                    Log.d("EventAdd", "" + document.toObject(Event.class).getName());
                }
            }
        });

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = eventArrayAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("event", event);

                NavHostFragment.findNavController(AttendeeBrowse.this)
                        .navigate(R.id.action_attendeeHome_to_attendeeEvent, bundle);
            }
        });

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {

            if (user != null) {

            }
            else{
                Log.d("AttendeeHome", "User is null");
            }
        });

        // Return the modified layout
        return rootView;
    }

}
