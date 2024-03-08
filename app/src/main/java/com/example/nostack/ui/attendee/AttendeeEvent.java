package com.example.nostack.ui.attendee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nostack.R;
import com.example.nostack.model.Events.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Creates the AttendeeEvent fragment which is used to display the events that the user is potentially attending
 */
public class AttendeeEvent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Event event;


    public AttendeeEvent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendeeEvent.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendeeEvent newInstance(String param1, String param2) {
        AttendeeEvent fragment = new AttendeeEvent();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putSerializable(ARG_PARAM2, param1);

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method is called when the fragment is being created and then sets up the variables for the view
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            event = (Event) getArguments().getSerializable("event");
            Log.d("AttendeeEvent", "Event: " + event.getName());
        }
    }

    /**
     * This method is called when the fragment is being created and then sets up the view for the fragment
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_event, container, false);
        updateScreenInformation(view);

        // Inflate the layout for this fragment only once
        return view;
    }

    /**
     * This method is called when the fragment has been created and then allows for navigation
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(AttendeeEvent.this)
                        .navigate(R.id.action_attendeeEvent_to_attendeeHome);
            }
        });
    }

    public void updateScreenInformation(@NonNull View view) {
        TextView eventTitle = view.findViewById(R.id.AttendeeEventTitleText);
        TextView eventDescription = view.findViewById(R.id.AttendeeEventDescriptionText);
        TextView eventLocation = view.findViewById(R.id.AttendeeEventLocationText);
        TextView eventStartDate = view.findViewById(R.id.AttendeeEventDateText);
        TextView eventStartTime = view.findViewById(R.id.AttendeeEventTimeText);

        DateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.CANADA);
        DateFormat tf = new SimpleDateFormat("h:mm a", Locale.CANADA);

        String startDate = df.format(event.getStartDate());
        String endDate = df.format(event.getEndDate());
        String startTime = tf.format(event.getStartDate());
        String endTime = tf.format(event.getEndDate());

        if (!startDate.equals(endDate)) {
            eventStartDate.setText(startDate + " to");
            eventStartTime.setText(endDate);
        } else {
            eventStartDate.setText(startDate);
            eventStartTime.setText(startTime + " - " + endTime);
        }

        eventTitle.setText(event.getName());
        Log.d("AttendeeEvent", "EventMSG" + event.getName());
        eventDescription.setText(event.getDescription());
        eventLocation.setText(event.getLocation());
    }
}