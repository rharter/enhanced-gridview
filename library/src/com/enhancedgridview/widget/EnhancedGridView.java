package com.enhancedgridview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import com.enhancedgridview.R;

/**
 * A GridView that allows a grid of items to be scrolled in any
 * direction. Since a list is really just a grid of one column,
 * this enabled horizontally scrolling list views as well.
 */
public class EnhancedGridView extends ScrollView {
	private static final String TAG = "EnhancedGridView";

	/**
	 * Specifies that the grid can't be scrolled
	 */
	public static final int SCROLL_NONE = 0;

	/**
	 * Specifies that the grid can be scrolled horizontally
	 */
	public static final int SCROLL_HORIZONTAL = 1;

	/**
	 * Specifies that the grid can be scrolled vertically
	 */
	public static final int SCROLL_VERTICAL = 2;

	/**
	 * Specifies that the grid can be scrolled both horizontally
	 * and vertically
	 */
	public static final int SCROLL_BOTH = 3;
    
    protected int mNumColumns;

    protected int mScrollDirection;
	
    protected ListAdapter mAdapter;

    private int mItemWidth;
    private int mItemHeight;

	protected FrameLayout mContainer;

    private static final int UPDATE_GRID = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case UPDATE_GRID:
                    fillGrid();
                    break;
            }
        }
    };

	public EnhancedGridView(Context context, AttributeSet attrs) {
		super(context, attrs);

        readAttributes(attrs);

		init();
	}

	public EnhancedGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

        readAttributes(attrs);
        
		init();
	}

    private void readAttributes(AttributeSet attrs) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, 
            R.styleable.EnhancedGridView);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
        	int attr = a.getIndex(i);
        	switch (attr) {
        		case R.styleable.EnhancedGridView_numColumns:
        			mNumColumns = a.getInt(attr, -1);
        			break;
        		case R.styleable.EnhancedGridView_scrollDirection:
        			mScrollDirection = a.getInt(attr, EnhancedGridView.SCROLL_BOTH);
        			break;
        		case R.styleable.EnhancedGridView_itemHeight:
        			mItemHeight = a.getDimensionPixelSize(attr, -1);
        			break;
    			case R.styleable.EnhancedGridView_itemWidth:
    				mItemWidth = a.getDimensionPixelSize(attr, -1);
    				break;
        	}
        }

        a.recycle();
    }

	private void init() {
		mContainer = new FrameLayout(getContext());

		final LayoutParams lp = new LayoutParams(Integer.MAX_VALUE, Integer.MAX_VALUE);
		
        mContainer.setMinimumHeight(Integer.MAX_VALUE);
        mContainer.setMinimumWidth(Integer.MAX_VALUE);
        mContainer.setLayoutParams(lp);

		addView(mContainer, lp);
	}

	public void setAdapter(ListAdapter adapter) {
		mAdapter = adapter;
		fillGrid();
	}

	public ListAdapter getAdapter() {
		return mAdapter;
	}

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);fillGrid();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        
        Message msg = Message.obtain();
        msg.what = UPDATE_GRID;

        if (mHandler.hasMessages(UPDATE_GRID)) {
            mHandler.removeMessages(UPDATE_GRID);
        }

        mHandler.sendMessage(msg);
    }

	private void fillGrid() {

		if (mAdapter.getCount() == 0)
			return;

		Rect visible = new Rect();
		mContainer.getDrawingRect(visible);

		mContainer.removeAllViews();

		final int left = visible.left + getScrollX();
		final int top = visible.top + getScrollY();

		if (mItemWidth == -1 || mItemHeight == -1) {
			View reference = mAdapter.getView(0, null, this);
			mItemWidth = reference.getWidth();
			mItemHeight = reference.getHeight();
		}

		final int width = (int) (getMeasuredWidth()) + getScrollX() + mItemWidth;
		final int height = (int) (getMeasuredHeight()) + getScrollY() + mItemHeight;

		Log.d(TAG, "measuredWidth=" + getMeasuredWidth() + ", measuredHeight=" + getMeasuredHeight());
		Log.d(TAG, "left=" + left + ", top=" + top + ", width=" + width + ", height=" + height);

		// Get the tiles
		for (int y = top; y < height; y += mItemHeight) {
			final int yPos = new Double(Math.ceil(y / mItemHeight)).intValue();
			for (int x = left; x < width; x += mItemWidth) {
				final int xPos = new Double(Math.ceil(x / mItemWidth)).intValue();
				
				Log.d(TAG, "item(" + xPos + ", " + yPos + ")");

				final int viewPos = (mNumColumns * yPos) + xPos;
				if (viewPos < mAdapter.getCount()) {
	                View v = mAdapter.getView(viewPos, null, this);

					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

					lp.leftMargin = xPos * mItemWidth;
					lp.topMargin = yPos * mItemHeight;
					Log.d(TAG, "layoutParams(leftMargin=" + lp.leftMargin + ", topMargin=" + lp.topMargin + ")");
					lp.gravity = Gravity.TOP | Gravity.LEFT;
					v.setLayoutParams(lp);

					mContainer.addView(v, lp);
				}
			}
		}
	}
}