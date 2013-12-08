package com.codeperf.restclient.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.codeperf.restclient.R;
import com.codeperf.restclient.common.Constants;

public class AddHeaderDialog extends DialogFragment {

	/*
	 * The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks. Each method
	 * passes the DialogFragment in case the host needs to query it.
	 */
	public interface AddHeaderDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog,
				String headerName, String headerValue);

		public void onDialogNegativeClick(DialogFragment dialog, Bundle bundle);
	}

	// Use this instance of the interface to deliver action events
	AddHeaderDialogListener mListener = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		final View v = inflater.inflate(R.layout.layout_add_header, null);

		Bundle args = getArguments();
		if (args != null) {
			if (args.getBoolean(Constants.BUNDLE_HEADER_MODIFY_FLAG)) {
				EditText etHeaderName = (EditText) v
						.findViewById(R.id.et_header_name);
				EditText etHeaderValue = (EditText) v
						.findViewById(R.id.et_header_value);
				Log.d("RESTClient", " etHeaderName - " + etHeaderName);
				etHeaderName.setText(args
						.getString(Constants.BUNDLE_HEADER_NAME));
				etHeaderValue.setText(args
						.getString(Constants.BUNDLE_HEADER_VALUE));
			}
		}

		return new AlertDialog.Builder(getActivity())
				.setView(v)
				.setTitle(R.string.title_header_dialog)
				.setNegativeButton(R.string.bt_text_cancel,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (mListener != null)
									mListener.onDialogNegativeClick(
											AddHeaderDialog.this, null);
							}
						})
				.setPositiveButton(R.string.bt_text_okay,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (mListener != null) {
									EditText etHeaderName = (EditText) getDialog()
											.findViewById(R.id.et_header_name);
									EditText etHeaderValue = (EditText) getDialog()
											.findViewById(R.id.et_header_value);
									mListener.onDialogPositiveClick(
											AddHeaderDialog.this, etHeaderName
													.getText().toString(),
											etHeaderValue.getText().toString());
								}
								dialog.dismiss();
							}
						}).create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the AddHeaderDialogListener so we can send events to
			// the host
			mListener = (AddHeaderDialogListener) activity;

		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement AddHeaderDialogListener");
		}
	}
}