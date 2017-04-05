package com.designfreed.toowatch.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.designfreed.toowatch.R;
import com.designfreed.toowatch.model.Serie;

import java.util.List;

public class SerieAdapter extends ArrayAdapter<Serie> {

    public SerieAdapter(@NonNull Context context, @NonNull List<Serie> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View cardView = convertView;

        if (cardView == null) {

            cardView = LayoutInflater.from(getContext()).inflate(R.layout.serie_card_view, parent, false);

        }

        Serie serie = getItem(position);

        TextView name = (TextView) cardView.findViewById(R.id.serie_name_field);
        name.setText(serie.getName());

        TextView type = (TextView) cardView.findViewById(R.id.serie_type_field);
        type.setText(serie.getType());

        TextView year = (TextView) cardView.findViewById(R.id.serie_year_field);
        year.setText(serie.getYear().toString());

        TextView rating = (TextView) cardView.findViewById(R.id.serie_rating_field);
        rating.setText(serie.getRating().toString());

        RatingBar stars = (RatingBar) cardView.findViewById(R.id.serie_rating_stars);
        stars.setRating(serie.getRating());

        return cardView;
    }
}
