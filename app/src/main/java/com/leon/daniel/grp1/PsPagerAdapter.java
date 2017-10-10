package com.leon.daniel.grp1;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Creado por Pollux.
 */

public class PsPagerAdapter extends PagerAdapter{
    Context mCtx;
    int [] psGames;
    public PsPagerAdapter(Context context, int [] psGames) {
        this.mCtx = context;
        this.psGames = psGames;
    }
    @Override
    public int getCount() {
        return psGames.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.ps_games_row, container, false);
        container.addView(view);

        ImageView psImage = (ImageView) view.findViewById(R.id.ps_game);
        Picasso.with(mCtx)
                .load(psGames[position])
                .into(psImage);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View) object);
    }
}