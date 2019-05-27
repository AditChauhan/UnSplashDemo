/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.unsplashdemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.List;

import tutorial.adit.com.unsplashdemo.Activity.SingleImage;
import tutorial.adit.com.unsplashdemo.R;
import tutorial.adit.com.unsplashdemo.model.Urls;

/**
 * Adapter for paging detail views.
 */

public class DetailViewPagerAdapter extends PagerAdapter {

	 int timer =  0 ;
    private final List<Urls> allPhotos;
    private final LayoutInflater layoutInflater;
    private final int photoWidth;
    private final Activity host;
    Context ctx ;
    ImageView displayImage;
	int current ;

	HashMap<String,View> viewholder;
    public DetailViewPagerAdapter( Activity activity, List<Urls> photos,int initial) {

    	ctx = activity;
        layoutInflater = LayoutInflater.from(activity);
        allPhotos = photos;
        photoWidth = activity.getResources().getDisplayMetrics().widthPixels;
        host = activity;
        current = initial;

        viewholder = new HashMap<String,View>();
    }

    @Override
    public int getCount() {
        return allPhotos.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


	    View view = this.layoutInflater.inflate(R.layout.activity_single_image, container, false);
	    displayImage = (ImageView)view.findViewById(R.id.image_thumbnail);
        Urls image_url = allPhotos.get(position);
        String imageUrl = "";
        imageUrl = image_url.getRegular();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
	        String name = container.getContext().getString(R.string.transition_name, position);
	        viewholder.put(name,displayImage);
		        displayImage.setTransitionName(name);
		        if(position == current) {
			        ((SingleImage) ctx).setStartPostTransition(displayImage);
		        }
	        }
	    Glide.with(host).load(imageUrl)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(displayImage);
	    container.addView(view);
	    return view;
    }


    //vhcghgchcgjg

    public View getview(String key)
    {
    	View view = viewholder.get(key);
		return view ;
    }


    public void setTimer()
	{
		timer= 0;
	}

	public View getCurrentView(ViewPager pager) {

//    	Log.d("position",pager.getCurrentItem()+" current item");
//		View  v =pager.getChildAt(pager.getCurrentItem());
//
//		if( v instanceof  ImageView) {
//			Log.d("position", "getcurrentview: instance" + v.toString());
//		}
//		else {
//			Log.d("position", "getcurrentview: not instance " + v.toString());
//		}
		return displayImage;
		// pager.getChildAt(i);
	}



	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View)object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
