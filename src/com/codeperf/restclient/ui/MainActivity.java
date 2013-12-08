package com.codeperf.restclient.ui;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codeperf.restclient.R;
import com.codeperf.restclient.common.Constants;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener, AddHeaderDialog.AddHeaderDialogListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	// Constant for identifying the dialog
	private static final int DIALOG_ADD_HEADER = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
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
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, show the tab contents in the
		// container view.
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER,
				tab.getPosition() + 1);
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		public DummySectionFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return textView;
		}
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
			if (!args.getBoolean(Constants.BUNDLE_HEADER_MODIFY_FLAG)) {

				// Prepeare a new view and attach to linear_headers_layout
				final LinearLayout headersLayout = (LinearLayout) findViewById(R.id.linear_headers_layout);

				if (headersLayout.getVisibility() == View.GONE)
					headersLayout.setVisibility(View.VISIBLE);

				// Get the layout inflater
				LayoutInflater inflater = getLayoutInflater();
				LinearLayout headerView = (LinearLayout) inflater.inflate(
						R.layout.headers_view, null);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						200, 40);
				params.leftMargin = 16;
				params.bottomMargin = 12;
				params.topMargin = 2;

				final int headerId = i++;
				headerView.setId(headerId);
				headerView.setBackgroundResource(R.drawable.header_view_shape);
				headerView.setOrientation(LinearLayout.HORIZONTAL);
				headerView.setLayoutParams(params);

				TextView tvHeader = (TextView) headerView
						.findViewById(R.id.tv_header);
				tvHeader.setText(headerName + ":" + headerValue);
				tvHeader.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						DialogFragment headerDialog = new AddHeaderDialog();
						Bundle args = new Bundle();
						args.putBoolean(Constants.BUNDLE_HEADER_MODIFY_FLAG,
								true);
						args.putInt(Constants.BUNDLE_HEADER_RESOURCE_ID,
								headerId);

						TextView tv = (TextView) v;
						String text = tv.getText().toString();
						String splits[] = text.split(":", 2);
						args.putString(Constants.BUNDLE_HEADER_NAME, splits[0]);
						args.putString(Constants.BUNDLE_HEADER_VALUE, splits[1]);
						headerDialog.setArguments(args);
						headerDialog.show(getFragmentManager(),
								"Request Header");
					}
				});

				ImageView cancel = (ImageView) headerView
						.findViewById(R.id.bt_cancel_header);
				cancel.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						LinearLayout headerView = (LinearLayout) headersLayout
								.findViewById(headerId);
						((ViewGroup) headersLayout).removeView(headerView);

						if (headersLayout.getChildCount() == 1) {
							headersLayout.setVisibility(View.GONE);
						}
					}
				});
				((ViewGroup) headersLayout).addView(headerView);
			} else {
				// Modifying existing header
				LinearLayout headersLayout = (LinearLayout) findViewById(R.id.linear_headers_layout);

				LinearLayout headerView = (LinearLayout) headersLayout
						.findViewById(args
								.getInt(Constants.BUNDLE_HEADER_RESOURCE_ID));
				if (headerView != null) {
					TextView tvHeader = (TextView) headerView
							.findViewById(R.id.tv_header);
					tvHeader.setText(headerName + ":" + " " + headerValue);
				}
			}
		}
	}

	private static int i = 0;

	@Override
	public void onDialogNegativeClick(DialogFragment dialog, Bundle bundle) {
		// TODO Auto-generated method stub

	}
}
