package activity;

/**
 * Created by user on 04.05.2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import helper.SQLiteHandler;
import helper.SessionManager;
import timur.diplom.Camera;
import timur.diplom.R;

import static timur.diplom.R.id.email;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private Button btnQRcheck;
    private Button btnAddPoint;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnQRcheck = (Button) findViewById(R.id.buttonQRcheck);
        btnAddPoint = (Button) findViewById(R.id.buttonPoint);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        final String id = user.get("uid");
        final String fio = user.get("fio");
        final String companyID = user.get("companyID");

        // Displaying the user details on the screen
        txtName.setText(id);
        txtEmail.setText(fio);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        btnQRcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "qr-code is scanning", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Camera.class);
                startActivity(intent);
                finish();
            }
        });
        btnAddPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "point has been added", Toast.LENGTH_SHORT).show();
            }
        });
    }
            /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}