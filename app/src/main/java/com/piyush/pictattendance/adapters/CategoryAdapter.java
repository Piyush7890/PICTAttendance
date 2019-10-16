package com.piyush.pictattendance.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.piyush.pictattendance.R;
import com.piyush.pictattendance.databinding.ItemCategoryBinding;
import com.piyush.pictattendance.model.Category;
import com.piyush.pictattendance.ui.activities.views.Chip;

import java.util.ArrayList;
import java.util.List;



public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
private List<String> checkedItems;
    private Listener listener;
    private List<Category> categories;
    boolean forceUncheck = false;

    public CategoryAdapter(Context context) {
        categories = new ArrayList<>();
        checkedItems= new ArrayList<>();
         String[] cactegories = context.getResources().getStringArray(R.array.categories);
         int[] colors = context.getResources().getIntArray(R.array.categoryColor);
         for(int i=0;i<cactegories.length;i++)
         {
             categories.add(new Category(i,cactegories[i],colors[i]));
         }

    }

    public
    void unSelectAll()
    {
        if(checkedItems.isEmpty())
            return;
            checkedItems.clear();
      //  forceUncheck=true;
        //    notifyDataSetChanged();
        //forceUncheck=false;
            listener.onSelected(checkedItems);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new CategoryViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),R.layout.item_category,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
        categoryViewHolder.binding.setCategory(categories.get(i));
        if(forceUncheck)
            categoryViewHolder.binding.chip.toggle();

    }



    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        Chip checkableChipView;
        private ItemCategoryBinding binding;

        CategoryViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
           // setIsRecyclable(false);
            this.binding = binding;
            checkableChipView = binding.chip;
            checkableChipView.setOnClickListener(v -> {
                boolean check = checkableChipView.isChecked();
                if(check)
                    checkedItems.remove(checkableChipView.getText().toString());
                else
                    checkedItems.add(checkableChipView.getText().toString());

                checkableChipView.animateCheckAndInvoke(!check, (chip, checked) -> listener.onSelected(checkedItems));

            });
//            checkableChipView.setOnCheckedChangeListener((checkableChipView, aBoolean) -> {
//                    if(aBoolean)
//                        checkedItems.add(checkableChipView.getText().toString());
//                    else
//                        checkedItems.remove(checkableChipView.getText().toString());
//                    listener.onSelected(checkedItems);
//                    return null;
//
//            });
        }
    }

    @Override
    public long getItemId(int position) {
        return categories.get(position).getId();
    }

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    public interface Listener
    {
        void onSelected(List<String> checkedItems);
    }

}
