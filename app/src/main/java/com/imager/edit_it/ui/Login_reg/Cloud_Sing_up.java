package com.imager.edit_it.ui.Login_reg;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.imager.edit_it.R;

import java.util.HashMap;
import java.util.Map;

public class Cloud_Sing_up extends Fragment {

    private com.imager.edit_it.databinding.SignUpBinding binding;
    private Button sign_up;
    EditText editemail, editpassword, editconfim_password;

    TextView signin_to_Signup;

    String email, password, confim_password;

    FirebaseAuth mAuth;
    FirebaseDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = com.imager.edit_it.databinding.SignUpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        NavOptions navOptions = new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in)
                .setExitAnim(R.anim.fade_out)
                .setPopEnterAnim(R.anim.fade_in)
                .setPopExitAnim(R.anim.slide_out)
                .build();

        sign_up = root.findViewById(R.id.btn_signin);
        editemail = root.findViewById(R.id.sign_up);
        editpassword = root.findViewById(R.id.password_log);
        editconfim_password = root.findViewById(R.id.confim_password_log);
        signin_to_Signup = root.findViewById(R.id.Sign_in_txt);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://edit-it-v2-default-rtdb.europe-west1.firebasedatabase.app");

        signin_to_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Navigation.findNavController(v).navigate(R.id.navigation_cloud_Sign_in);
                    }

        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editemail.getText().toString();
                password = editpassword.getText().toString();
                confim_password = editconfim_password.getText().toString();


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Please enter email.", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(password) || password.length() < 6) {
                    Toast.makeText(getActivity(), "Password should be at least 6 characters long.", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!password.equals(confim_password)) {
                    Toast.makeText(getActivity(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    DatabaseReference usersRef = firebaseDatabase.getReference().child("users");

                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("email", email);
                                    usersRef.child(currentUser.getUid()).setValue(userData)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Navigation.findNavController(v)
                                                                .navigate(R.id.navigation_cloud_Sign_in, null, navOptions);
                                                        Toast.makeText(getActivity(), "User email added to database", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getActivity(), "Failed to add user email to database", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(getActivity(), "User with this email already exists.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });

        return root;
    }
}
