package com.sigiep.serviciospublicos.ui.archivo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArchivoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ArchivoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}