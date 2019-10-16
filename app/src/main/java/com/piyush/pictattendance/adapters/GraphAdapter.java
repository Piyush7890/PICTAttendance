package com.piyush.pictattendance.adapters;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.piyush.pictattendance.DiffUtils.GraphDiffUtilCallback;
import com.piyush.pictattendance.R;
import com.piyush.pictattendance.model.TotalAttendance;
import com.piyush.pictattendance.ui.activities.views.InnerGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphAdapter extends ListAdapter<TotalAttendance,GraphViewHolder> {


    private List<TotalAttendance> list;
    private double[] percentages;
    double min=Double.MAX_VALUE, max = 0;
    private RecyclerView recyclerView;

    public GraphAdapter(RecyclerView recyclerView) {

        super(new GraphDiffUtilCallback());
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public GraphViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GraphViewHolder(LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_graph,
                        viewGroup,
                        false));
    }

    public void setData(List<TotalAttendance> list)
    {

        this.list = list;
        buildPercentageArray();
        submitList(list);
       // recyclerView.smoothScrollToPosition(list.size()-1);

    }

    private void buildPercentageArray() {
        if(list.size()*2-1<=0)return;

        for(int i=0;i<list.size();i++)
        {
            TotalAttendance item = list.get(i);
            if(item.getTotalAttendance()<min)
                min = item.getTotalAttendance();
            if(item.getTotalAttendance()>max)
                max = item.getTotalAttendance();
        }
        for(TotalAttendance object : list) {
            object.setMin(min);
            object.setMax(max);
        }
        this.percentages = new double[list.size() * 2 - 1];
        int len = percentages.length;
        for (int i = 0; i < len; i += 2) {
            percentages[i] = list.get(i / 2).getTotalAttendance();
        }
        for (int i = 1; i < len; i += 2) {
            percentages[i] = (percentages[i - 1] + percentages[i + 1]) * 0.5F;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull GraphViewHolder graphViewHolder, int i) {
        graphViewHolder.bindItem(buildTempArrayForItem(i),getItem(i), min, max);
    }

    private double[] buildTempArrayForItem(int adapterPosition) {
        double[] a = new double[3];
        a[1] = percentages[2 * adapterPosition];
        if (2 * adapterPosition - 1 < 0) {
            a[0] = InnerGraph.NON_EXISTENT;
        } else {
            a[0] = percentages[2 * adapterPosition - 1];
        }
        if (2 * adapterPosition + 1 >= percentages.length) {
            a[2] = InnerGraph.NON_EXISTENT;
        } else {
            a[2] = percentages[2 * adapterPosition + 1];
        }
        return a;
    }

}

class GraphViewHolder extends RecyclerView.ViewHolder {

    private TextView date;
    private TextView month;
    private InnerGraph graph;
     GraphViewHolder(@NonNull View itemView) {
        super(itemView);
        graph = itemView.findViewById(R.id.graph);
        date = itemView.findViewById(R.id.date);
        month = itemView.findViewById(R.id.month);

    }


     void bindItem(double percent[], TotalAttendance attendance, double min, double max)
     {
         month.setText(attendance.getMonth());
         date.setText(attendance.getDate());
         graph.setPoints(percent, min, max);
     }

}
