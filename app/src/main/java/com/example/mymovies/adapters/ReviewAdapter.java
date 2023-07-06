package com.example.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.R;
import com.example.mymovies.pojo.ReviewsProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewsProperties> reviewsProperties;

    public ReviewAdapter() {
        reviewsProperties = new ArrayList<>();
    }

    public void setReviewsProperties(List<ReviewsProperties> reviewsProperties) {
        this.reviewsProperties = reviewsProperties;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewsProperties reviewP = reviewsProperties.get(position);
        holder.textViewAuthor.setText(String.format(Locale.getDefault(), "%s:", reviewP.getAuthor()));
        holder.textViewContent.setText(reviewP.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewsProperties.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewAuthor;
        private TextView textViewContent;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewContent = itemView.findViewById(R.id.textViewContent);
        }
    }
}
