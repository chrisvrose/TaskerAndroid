package in.aravindweb.util;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class misc {
    /**
     * Return two if one is non-null
     * @param one Anything
     * @param two Anything
     * @param <T> -
     * @return two if one is null
     */
    public static <T> T coalesce(T one,T two){
        return one!=null?one:two;
    }

    /**
     * Return the first non-null argument
     * @param params list of nullable objects
     * @param <T> -
     * @return the first non-null argument or null
     */
    @SafeVarargs
    public static <T> T coalesceList(T...params){
        for(T param:params){
            if(param !=null) return param;

        }
        return null;
    }

    /**
     * Shortcut to using Glide
     * @param c context
     * @param uri uri string
     * @param v target
     */
    public static void Glider(Context c, String uri, ImageView v){
        Glide.with(c).load(uri).into(v);
    }


    public static int DPtoPX(int dps, Activity activity){
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, activity.getResources().getDisplayMetrics()));
    }
}
