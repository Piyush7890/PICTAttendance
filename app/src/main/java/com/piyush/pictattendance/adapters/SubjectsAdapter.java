package com.piyush.pictattendance.adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import com.piyush.pictattendance.DiffUtils.DiffUtilCallback;
import com.piyush.pictattendance.R;
import com.piyush.pictattendance.databinding.ItemSubjectBinding;
import com.piyush.pictattendance.model.Subject;
import com.piyush.pictattendance.utils.ExpandCollapseHelper;
import com.piyush.pictattendance.utils.Utils;
import java.util.ArrayList;
import java.util.List;


public class SubjectsAdapter extends ListAdapter<Subject, RecyclerView.ViewHolder> {


    private final int endColor;
    private final int startColor;
    private List<Subject> subjectList;
    private Listener listener;
    private List<Subject> tempList;

    public SubjectsAdapter(Listener listener, Context context) {

        super(new DiffUtilCallback());
       // startColor = Color.WHITE;
      //  endColor = Color.parseColor("#f5f5f5");
        startColor   = context.getResources().getColor(R.color.windowBackgroundDark);
        endColor   = Color.parseColor("#313235") ;
        this.listener = listener;

        subjectList = new ArrayList<>();
        tempList = new ArrayList<>() ;
    }

    public void submitList( List<Subject> list) {

        tempList = list;
        this.subjectList.clear();
        for(Subject subject:list)
            this.subjectList.add(new Subject(subject));
        super.submitList(list);

    }

    @Override
    public long getItemId(int position) {
        return subjectList.get(position).id;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new SubjectViewHolder(
                DataBindingUtil
                        .inflate(LayoutInflater
                                        .from(viewGroup.getContext())
                                ,R.layout.item_subject,viewGroup,false));
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder subjectViewHolder, int i) {

            SubjectViewHolder holder = (SubjectViewHolder)subjectViewHolder;
            holder.binding.setSubject(getItem(i));
            ((GradientDrawable) holder
                    .binding
                    .filters
                    .getCompoundDrawablesRelative()[0])
                    .setColor(Utils
                            .getCategoryColor(getItem(i)
                                    .getCategory()));
            holder.binding.btnAddPresent.setEnabled(false);
            holder.check(getItem(i), subjectList.get(i));


    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {

        SubjectViewHolder subjectViewHolder = ((SubjectViewHolder) holder);
        View view = subjectViewHolder.binding.edit;

        if(holder.getAdapterPosition()>=0 && !getItem(holder.getAdapterPosition()).isSelected && view.getVisibility()==View.VISIBLE)
        {

            (subjectViewHolder).binding.divider.setTranslationX((subjectViewHolder).translationX);
            (subjectViewHolder).binding.container.setTranslationZ(0);
        }
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemSubjectBinding binding;
        ExpandCollapseHelper expandCollapseHelper;

        float translationX;
         SubjectViewHolder(ItemSubjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.subjectHeading.setOnClickListener(this);
            binding.btnAddPresent.setOnClickListener(this);
            binding.btnAddTotal.setOnClickListener(this);
            binding.btnDecPresent.setOnClickListener(this);
            binding.btnDecTotal.setOnClickListener(this);
            binding.reset.setOnClickListener(this);
             expandCollapseHelper = new ExpandCollapseHelper(binding.edit);
             translationX = binding.divider.getTranslationX();
        }


        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Log.d("position",String.valueOf(pos));
            Subject subject = getItem(pos);
            Subject temp = subjectList.get(pos);
            switch (v.getId())
             {
                 case R.id.subject_heading:

                    boolean isSelected = subject.isSelected;

                    subject.isSelected=!isSelected;
                     if(isSelected){
                         binding.expansionLayout.collapse(true);
                   collapse(binding.edit, binding.container, binding.divider);}
                    else{
                        binding.expansionLayout.expand(true);
                     expand(binding.edit, binding.container, binding.divider);}
                    break;
                 case R.id.btn_add_present:{
                     int present = subject.getPresent();
                     subject.setPresent(++present);
                     int total = subject.getTotal();
                     subject.setTotal(total+1);
                     subject.recalculate();
                     binding.percentage.setText(String.valueOf(subject.getAttendance()+"%"));
                     setText(binding.textTotal,total+1);
                     setText(binding.textPresent,present);
                     listener.buttonClicked(tempList);
                     check(subject,temp);
                     break;
                 }
                 case R.id.btn_add_total: {
                     int total = subject.getTotal();
                     subject.setTotal(total+1);
                     subject.recalculate();
                     binding.percentage.setText(String.valueOf(subject.getAttendance()+"%"));
                     setText(binding.textTotal,total+1);
                     listener.buttonClicked(tempList);
                     check(subject,temp);
                     break;
                 }
                 case R.id.btn_dec_present:{
                     int present = subject.getPresent();
                     subject.setPresent(--present);
                     subject.recalculate();
                     binding.percentage.setText(String.valueOf(subject.getAttendance()+"%"));
                     setText(binding.textPresent,present);
                     listener.buttonClicked(tempList);
                     check(subject,temp);
                     break;
                 }
                 case R.id.btn_dec_total:{
                     int total = subject.getTotal();
                     subject.setTotal(--total);
                     subject.recalculate();
                     binding.percentage.setText(String.valueOf(subject.getAttendance()+"%"));
                     setText(binding.textTotal,total);
                     listener.buttonClicked(tempList);
                     check(subject,temp);
                     break;
                 }
                 case R.id.reset:
                     subject.reset(temp);
                     listener.buttonClicked(tempList);
                     ((AnimatedVectorDrawable) binding.reset.getDrawable()).start();
                     setText(binding.textTotal,temp.getTotal());
                    setText(binding.textPresent,temp.getPresent());
                    binding.percentage.setText(String.valueOf(temp.getAttendance()+"%"));
                    check(subject,temp);
                    break;
        }

    }







          void expand(ViewGroup view, ViewGroup viewGroup, View divider) {

             divider.animate()
                     .translationX(0)
                     .setInterpolator(new AccelerateDecelerateInterpolator())
                     .setDuration(200L)
                     .start();

//              ValueAnimator animator = ValueAnimator.ofArgb(startColor,endColor);
//              animator.addUpdateListener(animation -> viewGroup.setBackgroundColor(((int) animation.getAnimatedValue())));
//              animator.setDuration((int) (view.getMeasuredHeight() / view.getContext().getResources().getDisplayMetrics().density));
//              animator.start();
            // view.setAlpha(0f);
//              view.animate()
//                      .alpha(1.0f)
//                      .setDuration(150)
//                      .setStartDelay(200L
//                      ).start();

              new Handler().postDelayed(() -> viewGroup
                      .animate()
                      .translationZ(Utils.convertDpToPixel(8,
                              viewGroup.getContext())).start(),
                      120);
           // expandCollapseHelper.expand();
         }

        void collapse(ViewGroup view, ViewGroup container, View divider) {
            divider.animate().translationX(translationX)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(200L)
                    .start();
           // view.setAlpha(0f);
//            ValueAnimator animator = ValueAnimator.ofArgb(endColor,startColor);
//            animator.addUpdateListener(animation -> container.setBackgroundColor(((int) animation.getAnimatedValue())));
//            animator.setDuration((int) (view.getMeasuredHeight() / view.getContext().getResources().getDisplayMetrics().density));
//            animator.start();
            new Handler().postDelayed(() -> container
                    .animate()
                    .translationZ(0).start()
                    , 200);
           // expandCollapseHelper.collapse();
        }

            private void check(Subject subject, Subject original)
    {
            setEnabled(binding.btnDecTotal, subject.getTotal() != original.getTotal());
            setEnabled(binding.btnAddPresent,true);
            setEnabled(binding.btnDecPresent,subject.getPresent()!=0);
    }

    private void setText(TextView textView, int num)
    {
        textView.setText(String.valueOf(num));
    }

    private void setEnabled(ImageView image, boolean doEnable)
    {
        image.setEnabled(doEnable);
        image.setAlpha(doEnable?1f:0.5f);
    }

    }

    public interface Listener
    {
        void buttonClicked(List<Subject> list);
    }




}
