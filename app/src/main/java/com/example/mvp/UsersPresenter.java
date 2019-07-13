package com.example.mvp;

import android.content.ContentValues;
import android.text.TextUtils;

import java.util.List;

public class UsersPresenter {

    private UsersContactView view;
    private final UsersModel model;

    public UsersPresenter(UsersModel model) {
        this.model = model;
    }

    public void attachView(UsersContactView view) {
        this.view = view;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {
        loadUsers();
    }

    public void loadUsers() {
        model.loadUsers(new UsersModel.LoadUserCallback() {
            @Override
            public void onLoad(List<User> users) {
                view.showUsers(users);
            }
        });
    }

    public void add() {
        UserData userData = view.getUserData();
        if (TextUtils.isEmpty(userData.getName()) || TextUtils.isEmpty(userData.getEmail())) {
            view.showToast(R.string.empty_values);
            return;
        }

        ContentValues cv = new ContentValues(2);
        cv.put(UserTable.COLUNM.NAME, userData.getName());
        cv.put(UserTable.COLUNM.EMAIL, userData.getEmail());
        view.showProgress();

        model.addUser(cv, new UsersModel.CompleteCallback() {
            @Override
            public void onComplete() {
                view.hideProgress();
                loadUsers();
            }
        });
    }

    public void update(User user) {
        ContentValues cv = new ContentValues(3);
        cv.put(UserTable.COLUNM.ID, user.getId());
        cv.put(UserTable.COLUNM.NAME, user.getName());
        cv.put(UserTable.COLUNM.EMAIL, user.getEmail());
        view.showProgress();

        model.updateUser(cv, new UsersModel.CompleteCallback() {
            @Override
            public void onComplete() {
                view.hideProgress();
                loadUsers();
            }
        });
    }

    public void delete() {
        User user = view.getCurrentUser();

        ContentValues cv = new ContentValues(1);
        cv.put(UserTable.COLUNM.ID, user.getId());
        view.showProgress();

        model.deleteUser(cv, new UsersModel.CompleteCallback() {
            @Override
            public void onComplete() {
                view.hideProgress();
                loadUsers();
            }
        });
    }

    public void clear() {
        view.showProgress();
        model.clearUsers(new UsersModel.CompleteCallback() {
            @Override
            public void onComplete() {
                view.hideProgress();
                loadUsers();
            }
        });
    }

}
