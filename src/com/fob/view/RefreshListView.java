package com.fob.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fob.balls.R;

public class RefreshListView extends ListView implements OnScrollListener {

	public interface IListViewState {
		int LVS_NORMAL = 0; // 普通状态
		int LVS_PULL_REFRESH = 1; // 下拉刷新状态
		int LVS_RELEASE_REFRESH = 2; // 松开刷新状态
		int LVS_LOADING = 3; // 加载状态
	}

	public interface IOnRefreshListener {
		void OnRefresh();
	}

	private View mHeadView;
	private TextView mRefreshTextview;
	private TextView mLastUpdateTextView;
	private ImageView mArrowImageView;
	private ProgressBar mHeadProgressBar;

	private SimpleDateFormat mSimpleDateFormat;

	private View mFootView;
	// private View mLoadMoreView;
	private TextView mLoadMoreTextView;
	private ProgressBar mFootProgressBar;
	// private View mLoadingView;
	private IOnLoadMoreListener mLoadMoreListener; // 加载更多监听器
	private int mLoadMoreState = IListViewState.LVS_NORMAL;

	// private int mHeadContentWidth;
	private int mHeadContentHeight;

	private IOnRefreshListener mOnRefreshListener; // 头部刷新监听器

	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean mIsRecord = false;
	// 标记的Y坐标值
	private int mStartY = 0;
	// 当前视图能看到的第一个项的索引
	private int mFirstItemIndex = -1;
	// MOVE时保存的Y坐标值
	private int mMoveY = 0;
	// LISTVIEW状态
	private int mViewState = IListViewState.LVS_NORMAL;

	private final static int RATIO = 2;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;
	private boolean mBack = false;

	private boolean canRefresh = false;

	public RefreshListView(Context context) {
		super(context);
		init(context);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void setOnRefreshListener(IOnRefreshListener listener) {
		mOnRefreshListener = listener;
	}

	private void onRefresh() {
		if (mOnRefreshListener != null) {
			mOnRefreshListener.OnRefresh();
		}
	}

	@SuppressLint("SimpleDateFormat")
	public void onRefreshComplete() {
		mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
		mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mLastUpdateTextView.setText("最近更新: "
				+ mSimpleDateFormat.format(new Date()));
		switchViewState(IListViewState.LVS_NORMAL);
	}

	private void init(Context context) {
		setCacheColorHint(context.getResources().getColor(R.color.transparent));
		// setSelector(context.getResources().getDrawable(R.drawable.nothing));
		// setDivider(context.getResources().getDrawable(R.drawable.list_divider));

		initHeadView(context);

		initLoadMoreView(context);

		setOnScrollListener(this);
	}

	/**
	 * 初始化headview试图
	 * 
	 * @param context
	 */
	private void initHeadView(Context context) {
		mHeadView = LayoutInflater.from(context).inflate(
				R.layout.refresh_list_header, null);

		mArrowImageView = (ImageView) mHeadView
				.findViewById(R.id.refresh_list_header_pull_down);
		mArrowImageView.setMinimumWidth(60);

		mHeadProgressBar = (ProgressBar) mHeadView
				.findViewById(R.id.refresh_list_header_progressbar);

		mRefreshTextview = (TextView) mHeadView
				.findViewById(R.id.refresh_list_header_text);

		mLastUpdateTextView = (TextView) mHeadView
				.findViewById(R.id.refresh_list_header_last_update);

		measureView(mHeadView);
		mHeadContentHeight = mHeadView.getMeasuredHeight();
		// mHeadContentWidth = mHeadView.getMeasuredWidth();

		mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
		mHeadView.invalidate();

		addHeaderView(mHeadView, null, false);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);
	}

	/**
	 * 初始化footview试图
	 * 
	 * @param context
	 */
	private void initLoadMoreView(Context context) {
		mFootView = LayoutInflater.from(context).inflate(
				R.layout.refresh_list_footer, null);
		mLoadMoreTextView = (TextView) mFootView
				.findViewById(R.id.refresh_list_footer_text);
		mFootProgressBar = (ProgressBar) mFootView
				.findViewById(R.id.refresh_list_footer_progressbar);

		mFootView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mLoadMoreListener != null
						&& mLoadMoreState == IListViewState.LVS_NORMAL) {
					updateLoadMoreViewState(ILoadMoreViewState.LMVS_LOADING);
					mLoadMoreListener.OnLoadMore();
				}
			}
		});

		// mLoadMoreView = mFootView.findViewById(R.id.load_more_view);
		// mLoadingView = mFootView.findViewById(R.id.loading_layout);
		// mLoadMoreView.setOnClickListener(this);

		addFooterView(mFootView);

	}

	/**
	 * 此方法直接照搬自网络上的一个下拉刷新的demo，计算headView的width以及height
	 * 
	 * @param child
	 */
	@SuppressWarnings("deprecation")
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem,
			int visibleItemCount, int totalItemCount) {
		mFirstItemIndex = firstVisiableItem;

	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mOnRefreshListener != null && canRefresh) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				doActionDown(ev);
				break;
			case MotionEvent.ACTION_MOVE:
				doActionMove(ev);
				break;
			case MotionEvent.ACTION_UP:
				doActionUp(ev);
				break;
			default:
				break;
			}
		}

		return super.onTouchEvent(ev);
	}

	private void doActionDown(MotionEvent ev) {
		if (mIsRecord == false && mFirstItemIndex == 0) {
			mStartY = (int) ev.getY();
			mIsRecord = true;
		}
	}

	private void doActionMove(MotionEvent ev) {
		mMoveY = (int) ev.getY();

		if (mIsRecord == false && mFirstItemIndex == 0) {
			mStartY = (int) ev.getY();
			mIsRecord = true;
		}

		if (mIsRecord == false || mViewState == IListViewState.LVS_LOADING) {
			return;
		}

		int offset = (mMoveY - mStartY) / RATIO;

		switch (mViewState) {
		case IListViewState.LVS_NORMAL: {
			if (offset > 0) {
				mHeadView.setPadding(0, offset - mHeadContentHeight, 0, 0);
				switchViewState(IListViewState.LVS_PULL_REFRESH);
			}
		}
			break;
		case IListViewState.LVS_PULL_REFRESH: {
			setSelection(0);
			mHeadView.setPadding(0, offset - mHeadContentHeight, 0, 0);
			if (offset < 0) {
				switchViewState(IListViewState.LVS_NORMAL);
			} else if (offset > mHeadContentHeight) {
				switchViewState(IListViewState.LVS_RELEASE_REFRESH);
			}
		}
			break;
		case IListViewState.LVS_RELEASE_REFRESH: {
			setSelection(0);
			mHeadView.setPadding(0, offset - mHeadContentHeight, 0, 0);
			if (offset >= 0 && offset <= mHeadContentHeight) {
				mBack = true;
				switchViewState(IListViewState.LVS_PULL_REFRESH);
			} else if (offset < 0) {
				switchViewState(IListViewState.LVS_NORMAL);
			} else {

			}

		}
			break;
		default:
			return;
		}
		;

	}

	private void doActionUp(MotionEvent ev) {
		mIsRecord = false;
		mBack = false;

		if (mViewState == IListViewState.LVS_LOADING) {
			return;
		}

		switch (mViewState) {
		case IListViewState.LVS_NORMAL:

			break;
		case IListViewState.LVS_PULL_REFRESH:
			mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
			switchViewState(IListViewState.LVS_NORMAL);
			break;
		case IListViewState.LVS_RELEASE_REFRESH:
			mHeadView.setPadding(0, 0, 0, 0);
			switchViewState(IListViewState.LVS_LOADING);
			onRefresh();
			break;
		}

	}

	/**
	 * 切换headview视图
	 * 
	 * @param state
	 */
	private void switchViewState(int state) {
		switch (state) {
		case IListViewState.LVS_NORMAL:
			mArrowImageView.clearAnimation();
			mArrowImageView.setImageResource(R.drawable.refresh_list_pull_down);
			break;
		case IListViewState.LVS_PULL_REFRESH:
			mHeadProgressBar.setVisibility(View.GONE);
			mArrowImageView.setVisibility(View.VISIBLE);
			mRefreshTextview.setText(R.string.app_list_header_refresh_down);
			mArrowImageView.clearAnimation();

			// 是由RELEASE_To_REFRESH状态转变来的
			if (mBack) {
				mBack = false;
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(reverseAnimation);
			}
			break;
		case IListViewState.LVS_RELEASE_REFRESH:
			mHeadProgressBar.setVisibility(View.GONE);
			mArrowImageView.setVisibility(View.VISIBLE);
			mRefreshTextview.setText(R.string.app_list_header_refresh_up);
			mArrowImageView.clearAnimation();
			mArrowImageView.startAnimation(animation);
			break;
		case IListViewState.LVS_LOADING:
			mHeadProgressBar.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);
			mRefreshTextview.setText(R.string.app_list_header_refreshing);
			break;
		default:
			return;
		}
		mViewState = state;

	}

	public interface ILoadMoreViewState {
		int LMVS_NORMAL = 0; // 普通状态
		int LMVS_LOADING = 1; // 加载状态
		int LMVS_OVER = 2; // 结束状态
		int LMVS_END = 3; // 没有数据
	}

	public interface IOnLoadMoreListener {
		void OnLoadMore();
	}

	public void setOnLoadMoreListener(IOnLoadMoreListener listener) {
		mLoadMoreListener = listener;
	}

	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.load_more_view: {
	// if (mLoadMoreListener != null
	// && mLoadMoreState == IListViewState.LVS_NORMAL) {
	// updateLoadMoreViewState(ILoadMoreViewState.LMVS_LOADING);
	// mLoadMoreListener.OnLoadMore();
	// }
	// }
	// break;
	// }
	// }

	/**
	 * flag 当前状态 0.普通状态,显示：查看更多 1.加载状态,显示：正在加载 2.结束状态,数据全部加载，显示：数据加载完毕
	 * 3.没有数据,当没有数据时，隐藏该项
	 */
	public void onLoadMoreComplete(int flag) {
		updateLoadMoreViewState(flag);
	}

	/**
	 * 更新footview视图
	 * 
	 * @param state
	 */
	private void updateLoadMoreViewState(int state) {
		switch (state) {
		case ILoadMoreViewState.LMVS_NORMAL:// 0.普通状态,显示：查看更多
			if (getFooterViewsCount() == 0)
				addFooterView(mFootView);
			mFootView.setEnabled(true);
			// mLoadingView.setVisibility(View.GONE);
			mFootProgressBar.setVisibility(View.GONE);
			mLoadMoreTextView.setVisibility(View.VISIBLE);
			mLoadMoreTextView.setText("查看更多");
			canRefresh = true;
			break;
		case ILoadMoreViewState.LMVS_LOADING:// 1.加载状态,显示：正在加载
			mFootView.setEnabled(false);
			// mLoadingView.setVisibility(View.VISIBLE);
			mFootProgressBar.setVisibility(View.VISIBLE);
			mLoadMoreTextView.setText("加载中...");
			// mLoadMoreTextView.setVisibility(View.GONE);
			canRefresh = false;
			break;
		case ILoadMoreViewState.LMVS_OVER:// 2.结束状态,数据全部加载，显示：数据加载完毕
			if (getFooterViewsCount() == 0)
				addFooterView(mFootView);
			removeFooterView(mFootView);
			// mFootView.setEnabled(false);
			// mLoadingView.setVisibility(View.GONE);
			// mFootProgressBar.setVisibility(View.GONE);
			// mLoadMoreTextView.setVisibility(View.GONE);
			// mLoadMoreTextView.setText("没有更多数据");
			canRefresh = true;
			break;
		case ILoadMoreViewState.LMVS_END:// 3.没有数据,当没有数据时，隐藏该项
			if (getFooterViewsCount() == 0)
				addFooterView(mFootView);
			mFootView.setEnabled(false);
			// mLoadingView.setVisibility(View.GONE);
			mFootProgressBar.setVisibility(View.GONE);
			// mLoadMoreTextView.setVisibility(View.VISIBLE);
			mLoadMoreTextView.setText("没有数据");
			canRefresh = true;
			break;
		default:
			break;
		}
		mLoadMoreState = state;
	}

	public void removeFootView() {
		removeFooterView(mFootView);
	}

}