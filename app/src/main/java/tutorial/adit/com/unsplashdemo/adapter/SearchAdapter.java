/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.unsplashdemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import tutorial.adit.com.unsplashdemo.Activity.SingleImage;
import tutorial.adit.com.unsplashdemo.R;
import tutorial.adit.com.unsplashdemo.model.Urls;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

	private static final String TAG = SearchAdapter.class.getSimpleName();
	Context ctx;
	public ArrayList<Urls> urlsList;
	private static RecyclerViewClickListener itemListener;

    /* Shared transition support
    defining listener for position fetch*/

	public interface RecyclerViewClickListener
	{
		void recyclerViewListClicked(int position);
	}

	public class MyViewHolder extends RecyclerView.ViewHolder {
		public ImageView thumbnail;
		public TextView textView;
		public ProgressBar progressBar;

		public MyViewHolder(View itemView) {
			super(itemView);
			thumbnail = (ImageView) itemView.findViewById(R.id.image_thumbnail);
			textView = (TextView) itemView.findViewById(R.id.urlTextView);
			progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
		}

	}

	public SearchAdapter(Context context, RecyclerViewClickListener listener) {
		ctx = context;
		itemListener = listener;
		urlsList = new ArrayList<>();

	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View itemView = LayoutInflater.from(ctx).inflate(R.layout.image_list_item, parent, false);
		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		holder.thumbnail.setTransitionName(
				ctx.getString(R.string.transition_name, position));
		holder.thumbnail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				itemListener.recyclerViewListClicked(position);
				/*Sharedelement passing url list, positon*/
				ImageView mImage = holder.thumbnail;Intent i=new Intent(ctx, SingleImage.class);
				i.putExtra("urlslist",urlsList);
				i.putExtra("position",position);
				Pair<View,String> pair= Pair.create((View)holder.thumbnail,holder.thumbnail.getTransitionName());
				ActivityOptionsCompat optionsCompat= ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) ctx,pair);
				ctx.startActivity(i,optionsCompat.toBundle());


			}
		});

    /*    Glide library for image loading
                passing urls and listening to image state
        */
		Urls image_url = urlsList.get(position);
		String imageUrl = "";
		imageUrl = image_url.getRegular();
		holder.textView.setText( imageUrl);
		holder.progressBar.setVisibility(View.VISIBLE);
		Glide.with(ctx).load(imageUrl)
				.thumbnail(0.5f)
				.listener(new RequestListener<Drawable>() {
					@Override
					public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
						return false;
					}

					@Override
					public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
						holder.progressBar.setVisibility(View.GONE);
						holder.thumbnail.setVisibility(View.VISIBLE);
						return false;
					}

				})
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(holder.thumbnail);
	}


	@Override
	public int getItemCount() {
		return urlsList.size();
	}

	public View getViewAtIndex(RecyclerView recycler, int index) {
		if (index >= 0) {
			for (int i=0; i<recycler.getChildCount(); i++) {
				View child = recycler.getChildAt(i);
				int pos = recycler.getChildAdapterPosition(child);
				if (pos == index) {

					return child.findViewById(R.id.image_thumbnail);
				}
			}
		}
		return null;
	}

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	public  void clearImages()
	{
		urlsList.clear();
	}

	public void addImages(List<Urls> list){


		urlsList.addAll(list);
		notifyDataSetChanged();
	}



}
