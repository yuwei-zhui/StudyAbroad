package com.example.studyabroad.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.studyabroad.database.AppDatabase;
import com.example.studyabroad.database.dao.UserDao;
import com.example.studyabroad.database.entity.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class UserRepository {
    
    private UserDao userDao;
    private LiveData<List<User>> allUsers;
    private ExecutorService executorService;
    
    public UserRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
        allUsers = userDao.getAllUsers();
        executorService = Executors.newSingleThreadExecutor();
    }
    
    public interface InsertCallback {
        void onUserInserted(long userId);
    }
    
    public void insertAsync(User user, InsertCallback callback) {
        executorService.execute(() -> {
            long id = userDao.insert(user);
            callback.onUserInserted(id);
        });
    }
    
    public long insert(User user) {
        try {
            Future<Long> future = executorService.submit(() -> userDao.insert(user));
            return future.get(); // This will block until the result is available
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public void update(User user) {
        executorService.execute(() -> {
            userDao.update(user);
        });
    }
    
    public void delete(User user) {
        executorService.execute(() -> {
            userDao.delete(user);
        });
    }
    
    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
    
    public LiveData<User> getUserById(long id) {
        return userDao.getUserById(id);
    }
    
    public interface LoginCallback {
        void onLoginResult(User user);
    }
    
    public void login(String email, String password, LoginCallback callback) {
        executorService.execute(() -> {
            User user = userDao.login(email, password);
            callback.onLoginResult(user);
        });
    }
    
    public interface EmailCheckCallback {
        void onEmailChecked(boolean exists);
    }
    
    public void checkEmailExists(String email, EmailCheckCallback callback) {
        executorService.execute(() -> {
            boolean exists = userDao.checkEmailExists(email);
            callback.onEmailChecked(exists);
        });
    }
} 