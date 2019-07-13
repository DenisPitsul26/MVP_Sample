package com.example.mvp;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UsersModel {

    private final DBHelper dbHelper;

    public UsersModel(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void loadUsers(LoadUserCallback callback) {
        LoadUsersTask loadUsersTask= new LoadUsersTask(callback);
        loadUsersTask.execute();
    }

    public void addUser(ContentValues contentValues, CompleteCallback callback) {
        AddUserTask addUserTask = new AddUserTask(callback);
        addUserTask.execute(contentValues);
    }

    public void updateUser(ContentValues contentValues, CompleteCallback callback) {
        UpdateUserTask updateUserTask = new UpdateUserTask(callback);
        updateUserTask.execute(contentValues);
    }

    public void deleteUser(ContentValues contentValues, CompleteCallback callback) {
        DeleteUserTask deleteUserTask = new DeleteUserTask(callback);
        deleteUserTask.execute(contentValues);
    }

    public void clearUsers(CompleteCallback callback) {
        ClearUsersTask clearUsersTask = new ClearUsersTask(callback);
        clearUsersTask.execute();
    }

    interface LoadUserCallback {
        void onLoad(List<User> users);
    }

    interface CompleteCallback {
        void onComplete();
    }

    class LoadUsersTask extends AsyncTask<Void, Void, List<User>> {

        private final LoadUserCallback callback;

        LoadUsersTask(LoadUserCallback callback) {
            this.callback = callback;
        }


        @Override
        protected List<User> doInBackground(Void... voids) {
            List<User> users = new LinkedList<>();
            Cursor cursor = dbHelper.getReadableDatabase().query(UserTable.TABLE, null,
                    null, null, null, null, null);
            while (cursor.moveToNext()) {
                User user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex(UserTable.COLUNM.ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(UserTable.COLUNM.NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(UserTable.COLUNM.EMAIL)));
                users.add(user);
            }
            cursor.close();
            return users;
        }

        @Override
        protected void onPostExecute(List<User> users) {
            if (callback != null) {
                callback.onLoad(users);
            }
        }
    }

    class AddUserTask extends AsyncTask<ContentValues, Void, Void> {

        private final CompleteCallback callback;

        AddUserTask(CompleteCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            ContentValues cvUser = contentValues[0];
            dbHelper.getWritableDatabase().insert(UserTable.TABLE, null, cvUser);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (callback != null) {
                callback.onComplete();
            }
        }
    }

    class UpdateUserTask extends AsyncTask<ContentValues, Void, Void> {

        private final CompleteCallback callback;

        UpdateUserTask(CompleteCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            ContentValues cvUser = contentValues[0];
            dbHelper.getWritableDatabase().update(UserTable.TABLE, cvUser, "_id=?",
                    new String[]{String.valueOf(cvUser.get(UserTable.COLUNM.ID))});
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (callback != null) {
                callback.onComplete();
            }
        }
    }

    class DeleteUserTask extends AsyncTask<ContentValues, Void, Void> {

        private final CompleteCallback callback;

        DeleteUserTask(CompleteCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            ContentValues cvUser = contentValues[0];
            dbHelper.getWritableDatabase().delete(UserTable.TABLE, "_id="+cvUser.get(UserTable.COLUNM.ID),null);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (callback != null) {
                callback.onComplete();
            }
        }
    }

    class ClearUsersTask extends AsyncTask<Void, Void, Void> {

        private final CompleteCallback callback;

        ClearUsersTask(CompleteCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dbHelper.getWritableDatabase().delete(UserTable.TABLE, null, null);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (callback != null) {
                callback.onComplete();
            }
        }
    }

}
