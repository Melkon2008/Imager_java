package com.imager.edit_it.ui.Login_reg;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.imager.edit_it.R;
import com.imager.edit_it.databinding.SignInBinding;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class Cloud_Sing_in extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText email, password;
    private Button sign_in;
    private TextView sign_in_to_sign_up;
    private FirebaseAuth mAuth;
    private SignInBinding binding;
    private DatabaseReference firebaseDatabase;
    private Uri imageUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SignInBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        sign_in_to_sign_up = root.findViewById(R.id.Sign_up_txt);
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



        return root;
    }
    NavOptions navOptions = new NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.slide_out)
            .build();

    // Метод для входа пользователя
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

    // Метод для сохранения данных пользователя
    public void saveUser(String email, String password) {
        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    // Метод для проверки сохраненных данных пользователя и автоматического входа
    public void checkSavedUser() {
        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", getActivity().MODE_PRIVATE);
        String savedEmail = preferences.getString("email", null);
        String savedPassword = preferences.getString("password", null);
        if (savedEmail != null && savedPassword != null) {
            // Выполнение входа с сохраненными данными
            signInWithEmailAndPassword(savedEmail, savedPassword);

        }
    }




}
