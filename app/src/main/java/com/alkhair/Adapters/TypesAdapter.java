package com.alkhair.Adapters;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.alkhair.Models.ProjecttypesResponseModel;
import com.alkhair.R;
import com.alkhair.databinding.TypeNewItemBinding;
import com.alkhair.helper.PreferenceHelper;
import com.alkhair.ui.projects.ProjectDetailsFragment;
import com.codesroots.tourismgroup.presentation.screens.details.offers.InsideDonationsFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TypesAdapter extends RecyclerView.Adapter<TypesAdapter.ProjectiewHolder> {
    FragmentActivity activity;
    InsideDonationsFragment projectsFragment;
    List<ProjecttypesResponseModel.ResultBean> result;
    PreferenceHelper helper;
    private long mLastClickTime = 0;

    public TypesAdapter(FragmentActivity activity, InsideDonationsFragment projectsFragment, List<ProjecttypesResponseModel.ResultBean> result) {
        this.activity = activity;
        this.projectsFragment = projectsFragment;
        this.result = result;
    }

    @NonNull
    @Override
    public TypesAdapter.ProjectiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TypeNewItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.type_new_item, parent, false);
        return new TypesAdapter.ProjectiewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TypesAdapter.ProjectiewHolder holder, int position) {
        helper = new PreferenceHelper(activity);
        if (result.get(position).getLogo() != null) {
            holder.binding.progressBar.setVisibility(View.GONE);
            Picasso.with(activity).load(result.get(position).getLogo()).into(holder.binding.proImage);
        }
        if (helper.getLang().equals("ar")) {
            if (result.get(position).getName_ar() != null) {
                holder.binding.name.setText(result.get(position).getName_ar().replaceAll("\\n",""));
            }
        } else {
            if (result.get(position).getName_en() != null) {
                holder.binding.name.setText(result.get(position).getName_en().replaceAll("\\n",""));
            } else {
                holder.binding.name.setText(result.get(position).getName_ar().replaceAll("\\n",""));

            }
        }
        holder.binding.proImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }

                mLastClickTime = SystemClock.elapsedRealtime();
                helper.addData("projectID", String.valueOf(result.get(position).getID()));
                helper.addData("campaignDetailsEN", result.get(position).getName_en());
                helper.addData("campaignDetailsAR", result.get(position).getName_ar());
                Bundle bundle = new Bundle();
                Fragment fragment = new ProjectDetailsFragment();
                bundle.putSerializable("project_id", result.get(position).getID());
                fragment.setArguments(bundle);
                FragmentTransaction mFragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.fragment, fragment);
                mFragmentTransaction.addToBackStack(null);
                mFragmentTransaction.commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return result == null ? 0 : result.size();
    }

    public class ProjectiewHolder extends RecyclerView.ViewHolder {

        final TypeNewItemBinding binding;

        ProjectiewHolder(TypeNewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
