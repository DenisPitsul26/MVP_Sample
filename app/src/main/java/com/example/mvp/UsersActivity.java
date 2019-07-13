package com.example.mvp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class UsersActivity extends AppCompatActivity implements UsersContactView{

    private EditText inputName, inputEmail;
    private ProgressDialog progressDialog;
    private RecyclerView userList;
    private UserAdapter userAdapter;
    private UsersPresenter presenter;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        init();
    }

    @SuppressLint("WrongConstant")
    private void init() {
        inputName = findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.add();
            }
        });
        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.clear();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        userAdapter = new UserAdapter(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User item) {
                currentUser = new User(item.getId(), item.getName(), item.getEmail());
                UserDialog dialog = new UserDialog(UsersActivity.this);
                dialog.showDialog();
            }
        });

        userList = findViewById(R.id.recycler_view);
        userList.setLayoutManager(layoutManager);
        userList.setAdapter(userAdapter);

        DBHelper dbHelper = new DBHelper(this);
        UsersModel usersModel = new UsersModel(dbHelper);
        presenter = new UsersPresenter(usersModel);
        presenter.attachView(this);
        presenter.viewIsReady();

    }

    public void update(User user) {
        presenter.update(user);
    }

    public void delete() {
        presenter.delete();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public UserData getUserData() {
        UserData userData = new UserData();
        userData.setName(inputName.getText().toString());
        userData.setEmail(inputEmail.getText().toString());

        return userData;
    }

    public void showUsers(List<User> users) {
        userAdapter.setData(users);
    }

    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.please_wait));
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
