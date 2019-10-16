package com.piyush.pictattendance.DiffUtils;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.piyush.pictattendance.model.TotalAttendance;

public class GraphDiffUtilCallback extends DiffUtil.ItemCallback<TotalAttendance> {

    @Override
    public boolean areItemsTheSame(@NonNull TotalAttendance old, @NonNull TotalAttendance neww) {
        return old.getId()==neww.getId()
                && old.getMin()==neww.getMin()
                && old.getMax() == neww.getMax();
    }

    @Override
    public boolean areContentsTheSame(@NonNull TotalAttendance old, @NonNull TotalAttendance neww) {
        return old==neww;
    }
}
