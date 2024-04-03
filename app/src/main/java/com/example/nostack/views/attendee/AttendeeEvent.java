package com.example.nostack.views.attendee;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.nostack.R;
import com.example.nostack.handlers.CurrentUserHandler;
import com.example.nostack.models.Event;
import com.example.nostack.models.Image;
import com.example.nostack.viewmodels.EventViewModel;
import com.example.nostack.viewmodels.UserViewModel;
import com.example.nostack.handlers.ImageViewHandler;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Creates the AttendeeEvent fragment which is used to display the events that the user is potentially attending
 */
public class AttendeeEvent extends Fragment {
    private Event event;
    private ImageViewHandler imageViewHandler;
    private EventViewModel eventViewModel;
    private CurrentUserHandler currentUserHandler;


    public AttendeeEvent() {}

    public static AttendeeEvent newInstance() {
        return new AttendeeEvent();
    }

    /**
     * This method is called when the fragment is being created and then sets up the variables for the view
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = (Event) getArguments().getSerializable("event");
            Log.d("AttendeeEvent", "Event: " + event.getName());
        }
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        imageViewHandler = ImageViewHandler.getSingleton();
        currentUserHandler = CurrentUserHandler.getSingleton();
    }

    /**
     * This method is called when the fragment is being created and then sets up the view for the fragment
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_event, container, false);
        return view;
    }

    /**
     * This method is called when the fragment has been created and then allows for navigation
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Watch for errors
        eventViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                eventViewModel.clearErrorLiveData();
            }
        });

        // Fetch event (in our case update it) and Get event
        eventViewModel.fetchEvent(event.getId());
        eventViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            this.event = event;
        });

        updateScreenInformation(view);
        Button register = view.findViewById(R.id.AttendeeEventRegisterButton);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (event.getAttendees() != null && event.getAttendees().contains(currentUserHandler.getCurrentUserId())) {
                    eventViewModel.unregisterToEvent(currentUserHandler.getCurrentUserId(), event.getId());
                    Snackbar.make(getView(), "Unregistered from event", Snackbar.LENGTH_LONG).show();
                    register.setText("Register");
                } else {
                    eventViewModel.registerToEvent(currentUserHandler.getCurrentUserId(), event.getId());
                    Snackbar.make(getView(), "Registered for event", Snackbar.LENGTH_LONG).show();
                    register.setText("Unregister");
                }
            }
        });

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(AttendeeEvent.this).popBackStack();
            }
        });

    }

    private void updateScreenInformation(@NonNull View view) {
        TextView eventTitle = view.findViewById(R.id.AttendeeEventTitleText);
        TextView eventDescription = view.findViewById(R.id.AttendeeEventDescriptionText);
        TextView eventLocation = view.findViewById(R.id.AttendeeEventLocationText);
        TextView eventStartDate = view.findViewById(R.id.AttendeeEventDateText);
        TextView eventStartTime = view.findViewById(R.id.AttendeeEventTimeText);
        TextView eventAttendees = view.findViewById(R.id.UsersGoing);
        ImageView eventImage = view.findViewById(R.id.AttendeeEventImage);
        ImageView eventProfileImage = view.findViewById(R.id.AttendeeEventUserImage);
        Button register = view.findViewById(R.id.AttendeeEventRegisterButton);

        if (event.getAttendees() != null && event.getAttendees().contains(currentUserHandler.getCurrentUserId())) {
            register.setText("Unregister");
        }

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
        eventAttendees.setText("Attendees: " + event.getAttendees().size());
        imageViewHandler.setUserProfileImage(currentUserHandler.getCurrentUser(), eventProfileImage, getResources(), null);
        imageViewHandler.setEventImage(event, eventImage);
    }
}