package com.leon.daniel.grp1;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Creado por Pollux.
 */

public class PsPagerAdapter extends PagerAdapter{
    Context mCtx;
    int [] promoGames;
    public PsPagerAdapter(Context context, int [] psGames) {
        this.mCtx = context;
        this.promoGames = psGames;
    }
    @Override
    public int getCount() {
        return promoGames.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.promo_games_row, container, false);
        container.addView(view);

        ImageView psImage = (ImageView) view.findViewById(R.id.promo_games_iv);
        Picasso.with(mCtx)
                .load(promoGames[position])
                .into(psImage);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View) object);
    }
}