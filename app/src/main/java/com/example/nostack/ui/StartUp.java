package com.example.nostack;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.nostack.model.Profile.Profile;
import com.example.nostack.model.User.User;

/**
 * A simple {@link Fragment} subclass.
 * Creates the StartUp fragment which is used to display the start up page for the user to decide
 * to be an attendee or organizer or login as an admin
 */
public class StartUp extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Profile profile;
    private AlertDialog dialog;

    public StartUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartUp.
     */
    // TODO: Rename and change types and number of parameters
    public static StartUp newInstance(String param1, String param2) {
        StartUp fragment = new StartUp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method is called when the fragment is being created and then sets up the variables for the view
     * and checks if the user profile already exists
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // check if user profile exists
        profile = new Profile(getActivity());
    }

    /**
     * This method is called when the fragment is being created and then sets up the view for the fragment
     * also sets up the buttons for the user to decide to be an attendee or organizer or login as an admin
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_start_up, container, false);
        String uuid = profile.getUuid();

        if (!profile.exists()) {
            showCreateProfile(container, inflater);
            Log.d("StartUp", profile.exists() + "");
        } else {
            Log.d("StartUp", uuid);
            profile.retrieveProfile(uuid)
                    .thenApply(success -> {
                        if (!success) {
                            showCreateProfile(container, inflater);
                        }
                        return null;
                    });
        }

        view.findViewById(R.id.AttendeeSignInButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(StartUp.this)
                        .navigate(R.id.action_startUp_to_attendeeHome);
            }
        });

        view.findViewById(R.id.SignIn_SignUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(StartUp.this)
                        .navigate(R.id.action_startUp_to_organizerHome);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    /**
     * This method displays the necessary dialogue boxes to create a profile
     *
     * @param container The parent view that the fragment's UI should be attached to.
     * @param inflater  The LayoutInflater object that can be used to inflate
     */
    private void showCreateProfile(ViewGroup container, LayoutInflater inflater) {
        View dialogue = inflater.inflate(R.layout.user_info_pop_up, container, false);


        EditText firstName = dialogue.findViewById(R.id.addFirstNameField);
        EditText lastName = dialogue.findViewById(R.id.addLastNameField);
        EditText emailAddress = dialogue.findViewById(R.id.addEmailField);
        EditText phoneNumber = dialogue.findViewById(R.id.addPhoneField);
        EditText username = dialogue.findViewById(R.id.addUsernameField);


        Button saveButton = dialogue.findViewById(R.id.saveInfoFormButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("StartUp", "Save button clicked");

                if (firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || emailAddress.getText().toString().isEmpty() || phoneNumber.getText().toString().isEmpty() || username.getText().toString().isEmpty()) {
                    // Make all fields red
                    if (firstName.getText().toString().isEmpty()) {
                        firstName.setError("First name is required");
                    }
                    if (lastName.getText().toString().isEmpty()) {
                        lastName.setError("Last name is required");
                    }
                    if (emailAddress.getText().toString().isEmpty()) {
                        emailAddress.setError("Email address is required");
                    }
                    if (phoneNumber.getText().toString().isEmpty()) {
                        phoneNumber.setError("Phone number is required");
                    }
                    if (username.getText().toString().isEmpty()) {
                        username.setError("Username is required");
                    }
                    return;
                }

                User user = new User(
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        username.getText().toString(),
                        emailAddress.getText().toString(),
                        phoneNumber.getText().toString(),
                        null
                );


                // Pass the User object to createProfile
                profile.createProfile(user);

                // Close the dialogue box
                dialog.dismiss();

            }
        });
        dialog = builder.setView(dialogue).create();
        dialog.show();
    }
}

