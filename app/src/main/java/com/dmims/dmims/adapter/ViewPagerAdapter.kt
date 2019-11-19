package com.dmims.dmims.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.dmims.dmims.R

/**
 * Created by Kamal on 18-04-2018.
 */
//Class for ViewPager
class ViewPagerAdapter(//Declaration
    private val context: Context
) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null

    //Resource Provider - Images in Array
    private val images = arrayOf<Int>(
        R.drawable.sl1,
        R.drawable.sl3,
        R.drawable.gf1,
        R.drawable.gf6,
        R.drawable.gf2
    )

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.custom_layout, null)
        val imageView = view.findViewById(R.id.imageView) as ImageView
        imageView.setImageResource(images[position])
        view.setOnClickListener(View.OnClickListener { })
        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}
