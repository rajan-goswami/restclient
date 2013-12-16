package com.codeperf.restclient.ui;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codeperf.restclient.R;
import com.codeperf.restclient.common.Constants;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener, AddHeaderDialog.AddHeaderDialogListener {

	// Constant for identifying the dialog
	private static final int DIALOG_ADD_HEADER = 10;

	private ViewPager viewPager = null;
	private SectionsPagerAdapter sectionsPagerAdapter;

	private Fragment requestFragment = null;
	private Fragment responseFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		sectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		viewPager = (ViewPager) findViewById(R.id.container);
		viewPager.setAdapter(sectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						getActionBar().setSelectedNavigationItem(position);
					}
				});

		/*
		 * actionBar.setDisplayShowTitleEnabled(false);
		 * actionBar.setDisplayShowHomeEnabled(false);
		 */

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section1)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section2)
				.setTabListener(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_header:
			DialogFragment headerDialog = new AddHeaderDialog();
			Bundle args = new Bundle();
			args.putBoolean(Constants.BUNDLE_HEADER_MODIFY_FLAG, false);
			headerDialog.setArguments(args);
			headerDialog.show(getFragmentManager(), "Request Header");
			return true;
		case R.id.menu_Reset:
			if (getActionBar().getSelectedTab().getPosition() == 0) {
				resetRequestFragment();
			}
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				if (requestFragment == null)
					requestFragment = new RequestFragment();

				return requestFragment;
			} else {
				if (responseFragment == null)
					responseFragment = new ResponseFragment();
				return responseFragment;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public static class RequestFragment extends Fragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		public RequestFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			Toast.makeText(getActivity(), "onCreateView called",
					Toast.LENGTH_LONG).show();
			return inflater.inflate(R.layout.layout_request, container, false);
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			// TODO Auto-generated method stub
			super.onSaveInstanceState(outState);
		}
	}

	public static class ResponseFragment extends Fragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		public ResponseFragment() {
		}

		/*
		 * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
		 * container, Bundle savedInstanceState) { // Create a new TextView and
		 * set its text to the fragment's section // number argument value.
		 * return inflater.inflate(R.layout.layout_request, container, false); }
		 */
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String headerName,
			String headerValue) {

		if (headerName == null || headerName.isEmpty()) {
			return;
		}

		if (headerValue == null)
			headerValue = "";

		Bundle args = dialog.getArguments();
		if (args != null) {
			final LinearLayout headersLayout = (LinearLayout) findViewById(R.id.linear_headers_layout);
			if (!args.getBoolean(Constants.BUNDLE_HEADER_MODIFY_FLAG)) {

				/*
				 * if (headersLayout.getVisibility() == View.GONE)
				 * headersLayout.setVisibility(View.VISIBLE);
				 */

				((ViewGroup) headersLayout).addView(createHeaderView(
						headerName, headerValue));
			} else {
				// Modifying existing header
				LinearLayout headerView = (LinearLayout) headersLayout
						.findViewById(args
								.getInt(Constants.BUNDLE_HEADER_RESOURCE_ID));
				if (headerView != null) {
					TextView tvHeader = (TextView) headerView
							.findViewById(R.id.tv_header);
					tvHeader.setText(Html.fromHtml("<b>" + headerName + ":    "
							+ "</b>" + headerValue));
				}
			}
		}
	}

	private static int i = 0;

	private void resetRequestFragment() {
		EditText etUri = (EditText) findViewById(R.id.id_edittext_uri);
		etUri.setText("");

		EditText etBody = (EditText) findViewById(R.id.id_edittext_body);
		etBody.setText("");

		LinearLayout headersLayout = (LinearLayout) findViewById(R.id.linear_headers_layout);

		if (headersLayout != null) {
			// child at 0 is headers layout title
			for (int i = headersLayout.getChildCount(); i > 1; i--) {
				View v = headersLayout.getChildAt(i - 1);
				if (v != null)
					((ViewGroup) headersLayout).removeView(v);
			}
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog, Bundle bundle) {
		// TODO Auto-generated method stub

	}

	private View createHeaderView(String headerName, String headerValue) {
		// Get the layout inflater
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout headerView = (LinearLayout) inflater.inflate(
				R.layout.headers_view, null);

		int height = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 30, getResources()
						.getDisplayMetrics());
		int width = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 160, getResources()
						.getDisplayMetrics());

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, height);
		params.rightMargin = 12 * (int) getResources().getDisplayMetrics().density;
		params.leftMargin = 12 * (int) getResources().getDisplayMetrics().density;
		params.bottomMargin = 12 * (int) getResources().getDisplayMetrics().density;
		params.topMargin = 2 * (int) getResources().getDisplayMetrics().density;

		final int headerId = i++;
		headerView.setId(headerId);
		headerView.setBackgroundResource(R.drawable.header_view_shape);
		headerView.setOrientation(LinearLayout.HORIZONTAL);
		headerView.setLayoutParams(params);

		TextView tvHeader = (TextView) headerView.findViewById(R.id.tv_header);
		tvHeader.setText(Html.fromHtml("<b>" + headerName + ":    " + "</b>"
				+ headerValue));

		tvHeader.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				DialogFragment headerDialog = new AddHeaderDialog();
				Bundle args = new Bundle();
				args.putBoolean(Constants.BUNDLE_HEADER_MODIFY_FLAG, true);
				args.putInt(Constants.BUNDLE_HEADER_RESOURCE_ID, headerId);

				TextView tv = (TextView) v;
				String text = tv.getText().toString();
				String splits[] = text.split(":", 2);
				args.putString(Constants.BUNDLE_HEADER_NAME, splits[0]);
				args.putString(Constants.BUNDLE_HEADER_VALUE, splits[1]);
				headerDialog.setArguments(args);
				headerDialog.show(getFragmentManager(), "Request Header");
			}
		});

		ImageView cancel = (ImageView) headerView
				.findViewById(R.id.bt_cancel_header);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				LinearLayout headersLayout = (LinearLayout) findViewById(R.id.linear_headers_layout);
				LinearLayout headerView = (LinearLayout) headersLayout
						.findViewById(headerId);
				((ViewGroup) headersLayout).removeView(headerView);

				/*
				 * if (headersLayout.getChildCount() == 1) {
				 * headersLayout.setVisibility(View.GONE); }
				 */
			}
		});
		return headerView;
	}
}
