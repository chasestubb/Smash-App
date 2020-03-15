package com.example.android.sqliteweather;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.sqliteweather.data.City;
import com.example.android.sqliteweather.utils.OpenWeatherMapUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class SavedItemsAdapter extends RecyclerView.Adapter<SavedItemsAdapter.SavedItemsViewHolder> {

    private List<City> mCities;
    private OnCityClickListener mCityClickListener;

    public interface OnCityClickListener {
        void onCityClick(City city);
    }

    public SavedItemsAdapter(OnCityClickListener clickListener) {
        mCityClickListener = clickListener;
        mCities = new ArrayList<City>();
        mCities.add(new City("London")); // dummy data
        mCities.add(new City("Corvallis")); // dummy data
        mCities.add(new City("Portland")); // dummy data
    }

    public void updateCities(List<City> cities) {
        mCities = cities;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCities != null) {
            return mCities.size();
        } else {
            return 0;
        }
    }

    @Override
    public SavedItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.city_item, parent, false); // TODO idk if this is right
        return new SavedItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SavedItemsViewHolder holder, int position) {
        holder.bind(mCities.get(position));
    }

    class SavedItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mCityNameTV;

        public SavedItemsViewHolder(View itemView) {
            super(itemView);
            mCityNameTV = itemView.findViewById(R.id.tv_city_name);
            itemView.setOnClickListener(this);
        }

        public void bind(City city) {
            mCityNameTV.setText(city.city);
        }

        @Override
        public void onClick(View v) {
            City city = mCities.get(getAdapterPosition());
            mCityClickListener.onCityClick(city);
        }
    }
}
