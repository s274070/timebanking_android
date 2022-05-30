package com.group25.timebanking.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.group25.timebanking.models.User


class UserProfileViewModel : ViewModel() {
    //my info shows in UI when isMyProfile is true
    private var _userProfile = MutableLiveData<User?>()
    private var _userEmail = MutableLiveData<String>()
    //my info shows in UI when isMyProfile is false
    private var _othersUserProfile = MutableLiveData<User?>()
    private var _isMyProfile = MutableLiveData<Boolean?>()

    fun getUserProfile(): MutableLiveData<User?> {
        return _userProfile
    }

    fun setUserProfile(User: User?) {
        _userProfile.value = User
    }
    fun getUserEmail(): MutableLiveData<String> {
        return _userEmail
    }

    fun setUserEmail(profileEmail:  String) {
        _userEmail.value = profileEmail
    }

    fun getOthersUserProfile(): MutableLiveData<User?> {
        return _othersUserProfile
    }

    fun setOthersUserProfile(email: User?) {
        _othersUserProfile.value = email
    }

    fun getIsMyProfile(): MutableLiveData<Boolean?> {
        return _isMyProfile
    }
    // if I need my profile true else if other profile false
    fun setIsMyProfile(isMyProfile:Boolean){
        _isMyProfile.value=isMyProfile
    }

}