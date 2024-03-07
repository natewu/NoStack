package com.example.nostack.ui.organizer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nostack.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrganizerSignIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganizerSignIn extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrganizerSignIn() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrganizerSignIn.
     */
    // TODO: Rename and change types and number of parameters
    public static OrganizerSignIn newInstance(String param1, String param2) {
        OrganizerSignIn fragment = new OrganizerSignIn();
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

        View view = inflater.inflate(R.layout.fragment_organizer_sign_in, container, false);
        // Inflate the layout for this fragment

        view.findViewById(R.id.SignIn_SignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TODO: ADD SECTION THAT AUTHENTICATES USER LOGIN IF GOOD DO THE FOLLOWING:




                NavHostFragment.findNavController(OrganizerSignIn.this)
                        .navigate(R.id.action_organizerSignIn_to_organizerHome);
            }
        });

        view.findViewById(R.id.SignIn_SignUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(OrganizerSignIn.this)
                        .navigate(R.id.action_organizerSignIn_to_organizerSignUp);
            }
        });

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(OrganizerSignIn.this)
                        .popBackStack();
            }
        });


        return view;
    }
}