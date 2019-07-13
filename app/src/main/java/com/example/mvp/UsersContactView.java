package com.example.mvp;

import java.util.List;

public interface UsersContactView {
    User getCurrentUser();
    UserData getUserData();
    void showUsers(List<User> users);
    void showToast(int resId);
    void showProgress();
    void hideProgress();
}
