package com.sherjeel.unbnd.test.UserInterfaces.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.sherjeel.unbnd.test.Configuration.Debugging;
import com.sherjeel.unbnd.test.R;

public class AccountFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        Button loadHardcodedButton = (Button) root.findViewById(R.id.loadHardcoded_button);
        Button loadMediaButton = (Button) root.findViewById(R.id.loadMediaFromPhone_button);

        loadHardcodedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
                Navigation.findNavController(view).navigate(R.id.navigation_videos);
            }
        });
        loadMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Debugging.playSavedVideo = true;
                Navigation.findNavController(view).popBackStack();
                Navigation.findNavController(view).navigate(R.id.navigation_videos);
            }
        });
        return root;
    }
}
