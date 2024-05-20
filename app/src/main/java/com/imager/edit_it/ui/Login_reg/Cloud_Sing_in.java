package com.imager.edit_it.ui.Login_reg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.imager.edit_it.R;
import com.imager.edit_it.databinding.SignInBinding;
import com.itextpdf.io.IOException;

import java.security.GeneralSecurityException;

public class Cloud_Sing_in extends Fragment {

    private EditText email, password;
    private Button sign_in;
    private TextView sign_up_to_sign_in;
    private FirebaseAuth mAuth;
    private SignInBinding binding;
    private DatabaseReference firebaseDatabase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SignInBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sign_up_to_sign_in = root.findViewById(R.id.Sign_up_txt);
        email = root.findViewById(R.id.email_sign_in);
        password = root.findViewById(R.id.password_sign_in);
        sign_in = root.findViewById(R.id.btn_sign_in);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://edit-it-v2-default-rtdb.europe-west1.firebasedatabase.app").getReference();

        checkSavedUser();

        sign_in.setOnClickListener(v -> {
            String txtEmail = email.getText().toString();
            String txtPassword = password.getText().toString();

            if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                Toast.makeText(getActivity(), "Please enter email and password.", Toast.LENGTH_SHORT).show();
                return;
            }

            signInWithEmailAndPassword(txtEmail, txtPassword);
        });

        sign_up_to_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.navigation_cloud_Sign_up);
            }

        });

        return root;
    }

    private final NavOptions navOptions = new NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.slide_out)
            .setPopUpTo(R.id.navigation_cloud_Sign_in, true)  // Assuming cloud_sing_in is the ID of the fragment for sign-in
            .setPopUpTo(R.id.navigation_cloud_Sign_up, true)  // Assuming navigation_sign_up is the ID of the fragment for sign-up
            .build();




    public void signInWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    saveUser(email, password);

                    Navigation.findNavController(requireView())
                            .navigate(R.id.navigation_cloud_save, null, navOptions);
                }
            } else {
                Toast.makeText(getActivity(), "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveUser(String email, String password) {
        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    public void checkSavedUser() {
        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", getActivity().MODE_PRIVATE);
        String savedEmail = preferences.getString("email", null);
        String savedPassword = preferences.getString("password", null);
        if (savedEmail != null && savedPassword != null) {
            signInWithEmailAndPassword(savedEmail, savedPassword);

        }
    }

}
