package com.soumyajit.mavhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.soumyajit.mavhealth.model.User;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth mAuth;
    private Button signUpBtn, loginBtn, createBtn;
    private EditText emailText, passwordText, secondPass, confirmPass;
    private SignInButton signInButton;
    private FirebaseFirestore db;
    private CollectionReference usersRef;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("User");

        // Initialize views
        confirmPass = findViewById(R.id.editText3);
        //signInButton = findViewById(R.id.sign_in_button);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);
        //signInButton.setColorScheme(SignInButton.COLOR_LIGHT);

        emailText = findViewById(R.id.editText);
        passwordText = findViewById(R.id.editText2);
        secondPass = findViewById(R.id.editText3);
        secondPass.setVisibility(View.INVISIBLE);
        signUpBtn = findViewById(R.id.SignUpBtn);
        loginBtn = findViewById(R.id.LoginBtn);
        createBtn = findViewById(R.id.CreateAccount);
        signUpBtn.setVisibility(View.GONE);

        // Set click listeners
        signUpBtn.setOnClickListener(v -> signUp());
        loginBtn.setOnClickListener(v -> login());
        createBtn.setOnClickListener(v -> toggleCreateAccount());

        //signInButton.setOnClickListener(v -> signInWithGoogle());
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signUp() {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPass = secondPass.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && password.equals(confirmPass)) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(MainActivity.this, "Invalid email format.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 3) {
                Toast.makeText(MainActivity.this, "Password length must be at least 3 characters.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Account Created.", Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            // Check if the email is already in use
                            if (task.getException() != null && task.getException().getMessage() != null &&
                                    task.getException().getMessage().contains("email address is already in use")) {
                                Toast.makeText(MainActivity.this, "Email address is already in use.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                            updateUI(null);
                        }
                    });
        } else {
            Toast.makeText(MainActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
            if (!password.equals(confirmPass)) {
                Toast.makeText(MainActivity.this, "Password didn't match", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void login() {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // Hide confirm password field
        confirmPass.setVisibility(View.INVISIBLE);

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(MainActivity.this, "Invalid email format.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 3) {
                Toast.makeText(MainActivity.this, "Password length must be at least 3 characters.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Login Successfully.", Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            // Check specific error for incorrect password
                            if (Objects.requireNonNull(task.getException()).getMessage().contains("password is invalid")) {
                                Toast.makeText(MainActivity.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
                            } else if (Objects.requireNonNull(task.getException()).getMessage().contains("no user record corresponding to this identifier")) {
                                // Error for email not found
                                Toast.makeText(MainActivity.this, "No account found with this email.", Toast.LENGTH_SHORT).show();
                            } else {
                                // General authentication failed error
                                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                            updateUI(null);
                        }
                    });
        } else {
            Toast.makeText(MainActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }


    private void toggleCreateAccount() {
        emailText.setText("");
        passwordText.setText("");
        if (createBtn.getText().toString().equals("Create Account")) {
            confirmPass.setVisibility(View.VISIBLE);
            signUpBtn.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.INVISIBLE);
            createBtn.setText("Back to login");
            //signInButton.setVisibility(View.GONE);
        } else {
            confirmPass.setVisibility(View.INVISIBLE);
            signUpBtn.setVisibility(View.INVISIBLE);
            loginBtn.setVisibility(View.VISIBLE);
            createBtn.setText("Create Account");
            //signInButton.setVisibility(View.VISIBLE);
        }
    }



    private void updateUI(final FirebaseUser currentUser) {
        if (currentUser != null) {
            usersRef.document(currentUser.getEmail()).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null && (user.getType() == null || user.getType().isEmpty())) {
                        startActivity(new Intent(MainActivity.this, FirstSigninActivity.class));
                    } else {
                        Class<?> nextActivity = user != null && user.getType().equals("Patient") ? HomeActivity.class : DoctorHomeActivity.class;
                        startActivity(new Intent(MainActivity.this, nextActivity));
                    }
                } else {
                    startActivity(new Intent(MainActivity.this, FirstSigninActivity.class));
                }
            }).addOnFailureListener(e -> e.printStackTrace());
        }
    }
}
