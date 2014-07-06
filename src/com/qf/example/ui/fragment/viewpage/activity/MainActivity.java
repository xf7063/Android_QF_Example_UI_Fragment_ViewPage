package com.qf.example.ui.fragment.viewpage.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qf.example.ui.fragment.viewpage.adapter.ContentFragmentPageAdapter;
import com.qf.example.ui.fragment.viewpage.fragment.ContentFragment;

public class MainActivity extends FragmentActivity {
	private ViewPager vp;
	private String[] tabs = new String[] { "头条", "世界杯", "推荐", "娱乐", "体育", "财经" };
	private List<Fragment> fragments;
	private LinearLayout layoutNavi;
	private ImageView imgCursor;

	private int bmpW; // 动画图片宽度
	private int offset = 0; // 动画图片偏移量

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViewPage();
		initTextView();
		initImageView();
	}

	private void initViewPage() {
		vp = (ViewPager) findViewById(R.id.viewpager);
		fragments = new ArrayList<Fragment>();
		for (int i = 0; i < tabs.length; i++) {
			Bundle args = new Bundle();
			args.putString(ContentFragment.ARG_SECTION_KEY, tabs[i]);

			Fragment fragment = new ContentFragment();
			fragment.setArguments(args);
			fragments.add(fragment);
		}

		ContentFragmentPageAdapter adapter = new ContentFragmentPageAdapter(getSupportFragmentManager(), fragments);
		vp.setAdapter(adapter);
		vp.setCurrentItem(0);
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			private boolean left = false;
			private boolean right = false;
			private boolean isScrolling = false;
			private int lastValue = -1;

			@Override
			public void onPageSelected(int position) {
				int one = offset * 2 + bmpW;
				Animation animation = new TranslateAnimation(one * position, one * position, 0, 0);
				animation.setFillAfter(true);// True:图片停在动画结束位置
				animation.setDuration(300);
				imgCursor.startAnimation(animation);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				if (isScrolling && lastValue != 0) {
					if (lastValue > positionOffsetPixels) {
						// 递减，向右侧滑动
						right = true;
						left = false;
					} else if (lastValue < positionOffsetPixels) {
						// 递减，向右侧滑动
						right = false;
						left = true;
					} else if (lastValue == positionOffsetPixels) {
						right = left = false;
					}
				}
				lastValue = positionOffsetPixels;
				
				Matrix matrix = new Matrix();
				if (!isScrolling) {
					matrix.postTranslate(bmpW, 0);
					imgCursor.setImageMatrix(matrix);
				} else if (left) {
					matrix.postTranslate((positionOffsetPixels / 6 + bmpW), 0);
					imgCursor.setImageMatrix(matrix);
					if ((positionOffsetPixels / 6 + bmpW) > 80) {
						vp.setCurrentItem(position + 1);
					}
				} else if (right) {
					matrix.postTranslate((positionOffsetPixels / 6 + bmpW) - 120, 0);
					imgCursor.setImageMatrix(matrix);
					if ((positionOffsetPixels / 6 + bmpW) - 120 <= 0) {
						vp.setCurrentItem(position);
					}
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == 1) {
					isScrolling = true;
				} else {
					isScrolling = false;
				}
				
				if (state == 2) {
					right = left = false;
				}
			}
		});
	}

	private void initTextView() {
		layoutNavi = (LinearLayout) findViewById(R.id.layout_navi);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1, 1);
		for (int i = 0; i < tabs.length; i++) {
			TextView textView = new TextView(this);
			textView.setGravity(Gravity.CENTER);
			textView.setTextColor(Color.BLACK);
			textView.setText(tabs[i]);
			textView.setTag(i);
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					vp.setCurrentItem((int) v.getTag());
				}
			});
			layoutNavi.addView(textView, layoutParams);
		}
	}

	private void initImageView() {
		imgCursor = (ImageView) findViewById(R.id.img_cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.pic_title_bg).getWidth(); // 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels; // 获取分辨率宽度
		offset = (screenW / 6 - bmpW) / 2; // 计算偏移量，6代表界面页签的显示个数
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imgCursor.setImageMatrix(matrix); // 设置动画初始位置
	}

}
