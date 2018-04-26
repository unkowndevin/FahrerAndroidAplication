package mx.com.ipn.finalproject

import android.content.Context
import android.media.tv.TvContract
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class CustomViewPagerAdapter
    constructor(context : Context, layouts : Array<Int>) : PagerAdapter() {

    private val context : Context = context;
    private val layouts : Array<Int> = layouts;

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layoutInflater : LayoutInflater = LayoutInflater.from(this.context);

        val viewPagerAdapter : View =
                layoutInflater.inflate(
                        this.layouts[position],
                        container,
                        false
                );
        container.addView(viewPagerAdapter)

        return viewPagerAdapter;


    }

    override fun getCount(): Int {
        return this.layouts.size;
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`;
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view : View = `object` as View;
        container.removeView(view);
    }

}