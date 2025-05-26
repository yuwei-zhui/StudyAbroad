package com.example.studyabroad.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studyabroad.database.entity.User;
import com.example.studyabroad.repositories.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    
    private UserRepository repository;
    private LiveData<List<User>> allUsers;
    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>(false);
    
    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllUsers();
    }
    
    // Registration methods
    public long insert(User user) {
        return repository.insert(user);
    }
    
    public void update(User user) {
        repository.update(user);
    }
    
    public void delete(User user) {
        repository.delete(user);
    }
    
    // Authentication methods
    public void login(String email, String password, UserRepository.LoginCallback callback) {
        repository.login(email, password, user -> {
            if (user != null) {
                currentUser.postValue(user);
                isLoggedIn.postValue(true);
            } else {
                isLoggedIn.postValue(false);
            }
            callback.onLoginResult(user);
        });
    }
    
    public void logout() {
        currentUser.postValue(null);
        isLoggedIn.postValue(false);
    }
    
    public void checkEmailExists(String email, UserRepository.EmailCheckCallback callback) {
        repository.checkEmailExists(email, callback);
    }
    
    // LiveData getters
    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
    
    public LiveData<User> getCurrentUser() {
        return currentUser;
    }
    
    public LiveData<Boolean> getIsLoggedIn() {
        return isLoggedIn;
    }
    
    public LiveData<User> getUserById(long id) {
        return repository.getUserById(id);
    }
    
    // Set current user (for example after retrieving from SharedPreferences)
    public void setCurrentUser(User user) {
        currentUser.postValue(user);
        isLoggedIn.postValue(user != null);
    }
} 