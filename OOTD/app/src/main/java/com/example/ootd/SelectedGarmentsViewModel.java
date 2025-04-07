package com.example.ootd;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SelectedGarmentsViewModel extends ViewModel {
    private final MutableLiveData<List<Garment>> selectedGarments = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Garment>> getSelectedGarments() {
        return selectedGarments;
    }

    public void addGarment(Garment gar) {
        List<Garment> current = selectedGarments.getValue();
        assert current != null;
        current.add(gar);
        selectedGarments.setValue(current);
    }

    public void removeGarment(Garment gar) {
        List<Garment> current = selectedGarments.getValue();
        assert current != null;
        current.remove(gar);
        selectedGarments.setValue(current);
    }

    public Garment getGarment(Integer pos) {
        List<Garment> current = selectedGarments.getValue();
        assert current != null;
        return current.get(pos);
    }

    public void clearSelection() {
        selectedGarments.setValue(new ArrayList<>());
    }

    public int numGarments() {
        return Objects.requireNonNull(selectedGarments.getValue()).size();
    }

    public boolean contains(Garment gar) {
        return Objects.requireNonNull(selectedGarments.getValue()).contains(gar);
    }
}
