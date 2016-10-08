package com.point2points.kdusurveysystem.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.point2points.kdusurveysystem.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerSwipeAdapter<RecyclerViewAdapter.SimpleViewHolder> {

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView textViewPos;
        TextView textViewData;
        ImageButton buttonDelete;
        ImageButton  buttonEdit;
        ImageView letterimage;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            textViewPos = (TextView) itemView.findViewById(R.id.position);
            textViewData = (TextView) itemView.findViewById(R.id.text_data);
            buttonDelete = (ImageButton ) itemView.findViewById(R.id.delete);
            buttonEdit = (ImageButton ) itemView.findViewById(R.id.edit);
            letterimage = (ImageView) itemView.findViewById(R.id.letter_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(getClass().getSimpleName(), "onItemSelected: " + textViewData.getText().toString());
                    Toast.makeText(view.getContext(), "onItemSelected: " + textViewData.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Context mContext;
    private ArrayList<String> mDataset;

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    public RecyclerViewAdapter(Context context, ArrayList<String> objects) {
        this.mContext = context;
        this.mDataset = objects;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_items, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

        String item = mDataset.get(position);

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Swing).duration(1000).delay(100).playOn(layout.findViewById(R.id.edit));
                YoYo.with(Techniques.Swing).duration(1000).delay(100).playOn(layout.findViewById(R.id.delete));
            }
        });
        viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                mDataset.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mDataset.size());
                mItemManger.closeAllItems();
                Toast.makeText(view.getContext(), "Deleted " + viewHolder.textViewData.getText().toString() + "!", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                mDataset.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mDataset.size());
                mItemManger.closeAllItems();
                Toast.makeText(view.getContext(), "Deleted " + viewHolder.textViewData.getText().toString() + "!", Toast.LENGTH_SHORT).show();
            }
        });


        String firstletter = item.substring(0,1);

        String color = "#ff0400";

        switch (firstletter){
            case "A":color = "#ff0400";break;
            case "B":color = "#ff8800";break;
            case "C":color = "#ffdd00";break;
            case "D":color = "#bfeb00";break;
            case "E":color = "#59ff00";break;
            case "F":color = "#00ff7b";break;
            case "G":color = "#00eede";break;
            case "H":color = "#0095ff";break;
            case "I":color = "#0015ff";break;
            case "J":color = "#a200ed";break;
            case "K":color = "#ff12ef";break;
            case "L":color = "#e500a1";break;
            case "M":color = "#ff655c";break;
            case "N":color = "#ffcc00";break;
            case "O":color = "#b80c0c";break;
            case "P":color = "#b8590c";break;
            case "Q":color = "#27850a";break;
            case "R":color = "#ae81f2";break;
            case "S":color = "#97a0ff";break;
            case "T":color = "#642f95";break;
            case "U":color = "#ce40c2";break;
            case "V":color = "#f584c8";break;
            case "W":color = "#01517d";break;
            case "X":color = "#d5d5d5";break;
            case "Y":color = "#ffc756";break;
            case "Z":color = "#239fa5";break;
        }

        int android_color = Color.parseColor(color);

                TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .useFont(Typeface.DEFAULT)
                .fontSize(64)
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(firstletter,android_color);

        viewHolder.letterimage.setImageDrawable(drawable);
        viewHolder.textViewPos.setText("   " + (position + 1) + ". ");
        viewHolder.textViewData.setText(item);
        mItemManger.bindView(viewHolder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
}
