package com.piyush.pictattendance;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.piyush.pictattendance.adapters.UpdateAdapter;
import com.piyush.pictattendance.model.Update;

import java.util.List;

public class UpdateDialog extends BottomSheetDialogFragment {


    UpdateAdapter adapter;
    int theme=0;

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public void setList(List<Update> list) {
        this.list = list;
    }



    List<Update> list;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.update_sheet_layout, container,false);
        if(list!=null) {
            adapter = new UpdateAdapter(list);
            RecyclerView updateList = v.findViewById(R.id.update_list);
            updateList.setAdapter(adapter);
            updateList.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        v.findViewById(R.id.parent).setBackgroundColor(theme==1? Color.parseColor("#141414"):theme==0?Color.WHITE:Color.parseColor("#121212"));
        return v;
    }
}
