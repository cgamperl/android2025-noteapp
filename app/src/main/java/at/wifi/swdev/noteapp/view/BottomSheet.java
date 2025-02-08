package at.wifi.swdev.noteapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import at.wifi.swdev.noteapp.databinding.BottomsheetBinding;

public class BottomSheet extends BottomSheetDialogFragment {

    private BottomsheetBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Später...
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = BottomsheetBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}
