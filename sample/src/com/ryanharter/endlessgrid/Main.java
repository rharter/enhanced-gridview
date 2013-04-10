package com.ryanharter.endlessgrid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.enhancedgridview.widget.EnhancedGridView;

public class Main extends Activity
{
	private EnhancedGridView mGrid;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mGrid = (EnhancedGridView) findViewById(R.id.grid);
		mGrid.setAdapter(new StupidAdapter(this));
	}

	public static class StupidAdapter extends ArrayAdapter<Integer> {

		private static final Integer[] colors = new Integer[] {
			Color.rgb(255,20,23),
			Color.rgb(255,102,17),
			Color.rgb(255,136,68),
			Color.rgb(255,238,85),
			Color.rgb(254,254,56),
			Color.rgb(255,255,153),
			Color.rgb(170,204,34),
			Color.rgb(187,221,119),
			Color.rgb(200,207,130),
			Color.rgb(146,167,126),
			Color.rgb(85,153,238),
			Color.rgb(0,136,204),
			Color.rgb(34,102,136),
			Color.rgb(23,82,121),
			Color.rgb(85,119,119),
			Color.rgb(221,187,51),
			Color.rgb(211,167,109),
			Color.rgb(169,131,75),
			Color.rgb(170,102,136),
			Color.rgb(118,118,118),
			Color.rgb(246,48,10),
			Color.rgb(209,27,126),
			Color.rgb(246,239,42),
			Color.rgb(0,192,0),
			Color.rgb(10,98,218)
		};

		public StupidAdapter(Context context) {
			super(context, 0, colors);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v = new View(getContext());
			ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				400, 400);
			v.setLayoutParams(lp);
			v.setMinimumHeight(400);
			v.setMinimumWidth(400);

			v.setBackgroundColor(colors[position % colors.length]);

			return v;
		}
	}
}
