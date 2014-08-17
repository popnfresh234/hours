package com.alex.hours;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alex.hours.R;
import com.alex.hours.utilities.FileHelper;
import com.alex.hours.utilities.ParseConstants;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class RestaurantFragment extends Fragment implements
		OnClickListener, OnCheckedChangeListener, OnFocusChangeListener {

	public static final String EXTRA_RESTAURANT_ID = "com.alex.hours.extra.restaurant.id";
	private static final String mFileType = ParseConstants.KEY_FILE_TYPE;
	public static final int TAKE_PHOTO_REQUEST = 0;
	public static final int PICK_PHOTO_REQUEST = 1;
	public static final int MEDIA_TYPE_IMAGE = 2;

	private ParseACL parseACL;

	private Restaurant mRestaurant;
	protected Uri mMediaUri;
	// Declare views
	private EditText mTitleField;
	private EditText mAddressField;
	private EditText mPhoneField;
	private ImageButton mPhoneButton;
	private CheckBox mSunday;
	private CheckBox mMonday;
	private CheckBox mTuesday;
	private CheckBox mWednesday;
	private CheckBox mThursday;
	private CheckBox mFriday;
	private CheckBox mSaturday;
	private Button mMasterOpenButton;
	private Button mMasterCloseButton;
	private Button mSundayOpenButton;
	private Button mSundayCloseButton;
	private Button mMondayOpenButton;
	private Button mMondayCloseButton;
	private Button mTuesdayOpenButton;
	private Button mTuesdayCloseButton;
	private Button mWednesdayOpenButton;
	private Button mWednesdayCloseButton;
	private Button mThursdayOpenButton;
	private Button mThursdayCloseButton;
	private Button mFridayOpenButton;
	private Button mFridayCloseButton;
	private Button mSaturdayOpenButton;
	private Button mSaturdayCloseButton;
	private EditText mNotesField;
	private Button mTakePictureButton;
	private ImageView mRestaurantImageView;
	private Button mSaveButton;
	private Button mCancelButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get actionbar and set home button
		final ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// setup ACL
		parseACL = new ParseACL(ParseUser.getCurrentUser());
		parseACL.setPublicReadAccess(true);
		parseACL.setPublicWriteAccess(false);
		parseACL.setWriteAccess("e7So6F4ytk", true);
		parseACL.setWriteAccess("45LT0GnGVU", true);

		// get restaurant ID from arguments

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(getActivity());
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_restaurant, container,
				false);

		// Set the home button as enabled so caret appears
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

		// Setup fields
		mTitleField = (EditText) v.findViewById(R.id.restaurant_title);
		mTitleField.setOnFocusChangeListener(this);

		mAddressField = (EditText) v.findViewById(R.id.restaurant_address);
		mAddressField.setOnFocusChangeListener(this);

		mPhoneField = (EditText) v.findViewById(R.id.restaurant_phone);
		mPhoneButton = (ImageButton) v.findViewById(R.id.phone_button);
		mPhoneButton.setOnClickListener(this);

		mSunday = (CheckBox) v.findViewById(R.id.sunday_check);
		mSunday.setOnCheckedChangeListener(this);

		mMonday = (CheckBox) v.findViewById(R.id.monday_check);
		mMonday.setOnCheckedChangeListener(this);

		mTuesday = (CheckBox) v.findViewById(R.id.tuesday_check);
		mTuesday.setOnCheckedChangeListener(this);

		mWednesday = (CheckBox) v.findViewById(R.id.wednesday_check);
		mWednesday.setOnCheckedChangeListener(this);

		mThursday = (CheckBox) v.findViewById(R.id.thursday_check);
		mThursday.setOnCheckedChangeListener(this);

		mFriday = (CheckBox) v.findViewById(R.id.friday_check);
		mFriday.setOnCheckedChangeListener(this);

		mSaturday = (CheckBox) v.findViewById(R.id.saturday_check);
		mSaturday.setOnCheckedChangeListener(this);

		mMasterOpenButton = (Button) v.findViewById(R.id.master_hours_open);
		mMasterOpenButton.setOnClickListener(this);
		mMasterCloseButton = (Button) v.findViewById(R.id.master_hours_close);
		mMasterCloseButton.setOnClickListener(this);

		mSundayOpenButton = (Button) v.findViewById(R.id.sunday_hours_open);
		mSundayOpenButton.setOnClickListener(this);
		mSundayCloseButton = (Button) v.findViewById(R.id.sunday_hours_close);
		mSundayCloseButton.setOnClickListener(this);

		mMondayOpenButton = (Button) v.findViewById(R.id.monday_hours_open);
		mMondayOpenButton.setOnClickListener(this);
		mMondayCloseButton = (Button) v.findViewById(R.id.monday_hours_close);
		mMondayCloseButton.setOnClickListener(this);

		mTuesdayOpenButton = (Button) v.findViewById(R.id.tuesday_hours_open);
		mTuesdayOpenButton.setOnClickListener(this);
		mTuesdayCloseButton = (Button) v.findViewById(R.id.tuesday_hours_close);
		mTuesdayCloseButton.setOnClickListener(this);

		mWednesdayOpenButton = (Button) v
				.findViewById(R.id.wednesday_hours_open);
		mWednesdayOpenButton.setOnClickListener(this);
		mWednesdayCloseButton = (Button) v
				.findViewById(R.id.wednesday_hours_close);
		mWednesdayCloseButton.setOnClickListener(this);

		mThursdayOpenButton = (Button) v.findViewById(R.id.thursday_hours_open);
		mThursdayOpenButton.setOnClickListener(this);
		mThursdayCloseButton = (Button) v
				.findViewById(R.id.thursday_hours_close);
		mThursdayCloseButton.setOnClickListener(this);

		mFridayOpenButton = (Button) v.findViewById(R.id.friday_hours_open);
		mFridayOpenButton.setOnClickListener(this);
		mFridayCloseButton = (Button) v.findViewById(R.id.friday_hours_close);
		mFridayCloseButton.setOnClickListener(this);

		mSaturdayOpenButton = (Button) v.findViewById(R.id.saturday_hours_open);
		mSaturdayOpenButton.setOnClickListener(this);
		mSaturdayCloseButton = (Button) v
				.findViewById(R.id.saturday_hours_close);
		mSaturdayCloseButton.setOnClickListener(this);

		mNotesField = (EditText) v.findViewById(R.id.restaurant_notes);
		mNotesField.setOnFocusChangeListener(this);

		mTakePictureButton = (Button) v.findViewById(R.id.takePictureButton);
		mTakePictureButton.setOnClickListener(this);

		mRestaurantImageView = (ImageView) v
				.findViewById(R.id.restaurantImageView);

		mSaveButton = (Button) v.findViewById(R.id.saveButton);
		mSaveButton.setOnClickListener(this);

		mCancelButton = (Button) v.findViewById(R.id.cancelButton);
		mCancelButton.setOnClickListener(this);

		loadRestaurant();
		return v;

	}

	// Handle Results from Camera
	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == getActivity().RESULT_OK) {
			if (requestCode == PICK_PHOTO_REQUEST) {
				if (data == null) {
					Log.i("DATA", "DATA ERROR");
				} else {
					mMediaUri = data.getData();
				}
			}
			if (requestCode == TAKE_PHOTO_REQUEST) {
				Intent mediaScanIntent = new Intent(
						Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				mediaScanIntent.setData(mMediaUri);
				getActivity().sendBroadcast(mediaScanIntent);
			}
			resizeImageForUpload();

		}

		else if (resultCode != getActivity().RESULT_CANCELED) {
			Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
		}
	}

	protected void resizeImageForUpload() {

		byte[] fileBytes = FileHelper.getByteArrayFromFile(getActivity(),
				mMediaUri);

		if (fileBytes == null) {
			Log.i("Doom", "doom");
		} else {

			fileBytes = FileHelper.reduceImageForUpload(fileBytes);

			String fileName = FileHelper.getFileName(getActivity(), mMediaUri,
					mFileType);
			ParseFile file = new ParseFile(fileName, fileBytes);
			if (mRestaurant != null) {
				mRestaurant.setImage(file);

				getActivity().setProgressBarIndeterminateVisibility(true);
				mSaveButton.setEnabled(false);
				mCancelButton.setEnabled(false);
				mTakePictureButton.setEnabled(false);
				updateFields();
				mRestaurant.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						if (e == null) {

							mSaveButton.setEnabled(true);
							mCancelButton.setEnabled(true);
							mTakePictureButton.setEnabled(true);
							loadFields();
						} else {
							Log.i("SaveError", "Save Error");
						}
					}
				});
			} else {
				mRestaurant = createRestaurant();
				mRestaurant.setImage(file);
				getActivity().setProgressBarIndeterminateVisibility(true);
				mRestaurant.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						if (e == null) {
							loadFields();
						} else {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									getActivity());
							builder.setMessage(e.getMessage())
									.setTitle(R.string.general_error_title)
									.setPositiveButton(android.R.string.ok,
											null);
							AlertDialog dialog = builder.create();
							dialog.show();
						}
					}
				});
			}
		}
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {

		case R.id.phone_button:
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:"
					+ mPhoneField.getText().toString()));
			startActivity(callIntent);
			break;

		case R.id.sunday_hours_open:
			Log.i("Test", "Test");
			TimePickerDialog dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mSunday.setChecked(true);
							mSundayOpenButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 7, 0, true);
			dialog.show();
			break;
		case R.id.sunday_hours_close:
			Log.i("Test", "Test");
			dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mSunday.setChecked(true);
							mSundayCloseButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 21, 0, true);
			dialog.show();
			break;

		case R.id.monday_hours_open:
			dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mMonday.setChecked(true);
							mMondayOpenButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 7, 0, true);
			dialog.show();
			break;

		case R.id.monday_hours_close:
			dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mMonday.setChecked(true);
							mMondayCloseButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 21, 0, true);
			dialog.show();
			break;

		case R.id.tuesday_hours_open:
			dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mTuesday.setChecked(true);
							mTuesdayOpenButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 7, 0, true);
			dialog.show();
			break;

		case R.id.tuesday_hours_close:
			dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mTuesday.setChecked(true);
							mTuesdayCloseButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 21, 0, true);
			dialog.show();
			break;

		case R.id.wednesday_hours_open:
			dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mWednesday.setChecked(true);
							mWednesdayOpenButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 7, 0, true);
			dialog.show();
			break;

		case R.id.wednesday_hours_close:
			dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mWednesday.setChecked(true);
							mWednesdayCloseButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 21, 0, true);
			dialog.show();
			break;
		case R.id.thursday_hours_open:
			dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mThursday.setChecked(true);
							mThursdayOpenButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 7, 0, true);
			dialog.show();
			break;

		case R.id.thursday_hours_close:
			dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mThursday.setChecked(true);
							mThursdayCloseButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 21, 0, true);
			dialog.show();
			break;
		case R.id.friday_hours_open:
			dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mThursday.setChecked(true);
							mFridayOpenButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 7, 0, true);
			dialog.show();
			break;

		case R.id.friday_hours_close:
			dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mFriday.setChecked(true);
							mFridayCloseButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 21, 0, true);
			dialog.show();
			break;
		case R.id.saturday_hours_open:
			dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mSaturday.setChecked(true);
							mSaturdayOpenButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 7, 0, true);
			dialog.show();
			break;

		case R.id.saturday_hours_close:
			dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mSaturday.setChecked(true);
							mSaturdayCloseButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 21, 0, true);
			dialog.show();
			break;
		case R.id.master_hours_open:
			dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mSunday.setChecked(true);
							mMonday.setChecked(true);
							mTuesday.setChecked(true);
							mWednesday.setChecked(true);
							mThursday.setChecked(true);
							mFriday.setChecked(true);
							mSaturday.setChecked(true);

							mSundayOpenButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));
							mMondayOpenButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));
							mTuesdayOpenButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));
							mWednesdayOpenButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));
							mThursdayOpenButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));
							mFridayOpenButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));
							mSaturdayOpenButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 7, 0, true);
			dialog.show();
			break;

		case R.id.master_hours_close:
			dialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mSundayCloseButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));
							mMondayCloseButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));
							mTuesdayCloseButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));
							mWednesdayCloseButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));
							mThursdayCloseButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));
							mFridayCloseButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));
							mSaturdayCloseButton.setText(new StringBuilder()
									.append(padding_str(hourOfDay)).append(":")
									.append(padding_str(minute)));

						}
					}, 21, 0, true);
			dialog.show();
			break;

		case R.id.saveButton:

			mSaveButton.setEnabled(false);
			mCancelButton.setEnabled(false);
			mTakePictureButton.setEnabled(false);
			if (mRestaurant == null) {
				mRestaurant = createRestaurant();
				getActivity().setProgressBarIndeterminateVisibility(true);

				mRestaurant.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						if (getActivity() != null) {
							getActivity()
									.setProgressBarIndeterminateVisibility(
											false);
						}
						if (e == null) {
							mSaveButton.setEnabled(true);
							mCancelButton.setEnabled(true);
							mTakePictureButton.setEnabled(true);
							// check to make sure activity exists still, protect
							// against backing out of app
							if (getActivity() != null) {
								getActivity().setResult(Activity.RESULT_OK);
//								getActivity().finish();
								getFragmentManager().popBackStack();
							}
						}

					}
				});
			} else {

				// When saving check if current user is author and therefore has
				// write access, or is one of the listed administrators
				if (ParseUser
						.getCurrentUser()
						.getObjectId()
						.toString()
						.equals(mRestaurant.getAuthor().getObjectId()
								.toString())
						|| ParseUser.getCurrentUser().getObjectId().toString()
								.equals("e7So6F4ytk")
						|| ParseUser.getCurrentUser().getObjectId().toString()
								.equals("45LT0GnGVU")) {
					updateFields();
					getActivity().setProgressBarIndeterminateVisibility(true);

					mRestaurant.saveInBackground(new SaveCallback() {

						@Override
						public void done(ParseException e) {
							if (getActivity() != null) {
								getActivity()
										.setProgressBarIndeterminateVisibility(
												false);
							}
							if (e == null) {
								mSaveButton.setEnabled(true);
								mCancelButton.setEnabled(true);
								mTakePictureButton.setEnabled(true);
								if (getActivity() != null) {
									getActivity().setResult(Activity.RESULT_OK);
//									getActivity().finish();
									getFragmentManager().popBackStack();
								}
							}

						}
					});

				} else {

					mCancelButton.setEnabled(true);
					// TODO display alert
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setMessage(R.string.edit_error_message)
							.setTitle(R.string.edit_error_title)
							.setPositiveButton(android.R.string.ok, null);
					AlertDialog permissionsDialog = builder.create();
					permissionsDialog.show();
				}
			}

			break;

		case R.id.cancelButton:
			getActivity().finish();
			break;

		case R.id.takePictureButton:
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setItems(R.array.camera_choices, mDialogListener);
			AlertDialog dialog_camera = builder.create();
			dialog_camera.show();

			break;

		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int id = buttonView.getId();
		switch (id) {
		case R.id.sunday_check:
			if (!mSunday.isChecked()) {
				mSundayOpenButton.setText("");
				mSundayCloseButton.setText("");
			}
			break;

		case R.id.monday_check:
			if (!mMonday.isChecked()) {
				mMondayOpenButton.setText("");
				mMondayCloseButton.setText("");
			}
			break;

		case R.id.tuesday_check:
			if (!mTuesday.isChecked()) {
				mTuesdayOpenButton.setText("");
				mTuesdayCloseButton.setText("");
			}
			break;

		case R.id.wednesday_check:
			if (!mWednesday.isChecked()) {
				mWednesdayOpenButton.setText("");
				mWednesdayCloseButton.setText("");
			}
			break;

		case R.id.thursday_check:
			if (!mThursday.isChecked()) {
				mThursdayOpenButton.setText("");
				mThursdayCloseButton.setText("");
			}
			break;

		case R.id.friday_check:
			if (!mFriday.isChecked()) {
				mFridayOpenButton.setText("");
				mFridayCloseButton.setText("");
			}
			break;

		case R.id.saturday_check:
			if (!mSaturday.isChecked()) {
				mSaturdayOpenButton.setText("");
				mSaturdayCloseButton.setText("");
			}
			break;
		}

	}

	// FocusChangeListeners
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		int id = v.getId();
		switch (id) {
		case R.id.restaurant_title:
			if (hasFocus) {
				mTitleField.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						android.R.drawable.ic_menu_edit, 0);
			} else {
				mTitleField.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}
			break;

		case R.id.restaurant_address:
			if (hasFocus) {
				mAddressField.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						android.R.drawable.ic_menu_edit, 0);
			} else {
				mAddressField.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
						0);
			}
			break;

		case R.id.restaurant_notes:
			if (hasFocus) {
				mNotesField.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						android.R.drawable.ic_menu_edit, 0);
			} else {
				mNotesField.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}
			break;
		}
	}

	// Method for creating new restaurant

	private Restaurant createRestaurant() {
		Restaurant restaurant = new Restaurant();

		restaurant.setACL(parseACL);
		restaurant.setAuthor();

		restaurant.setTitle(mTitleField.getText().toString());
		restaurant.setLowerCaseTitle(mTitleField.getText().toString()
				.toLowerCase());

		restaurant.setAddress(mAddressField.getText().toString());
		restaurant.setLowerCaseAddress(mAddressField.getText().toString()
				.toLowerCase());

		restaurant.setPhone(PhoneNumberUtils.formatNumber(mPhoneField.getText()
				.toString()));

		restaurant.setSunday(mSunday.isChecked());
		restaurant.setSundayOpenHours(mSundayOpenButton.getText().toString());
		restaurant.setSundayCloseHours(mSundayCloseButton.getText().toString());

		restaurant.setMonday(mMonday.isChecked());
		restaurant.setMondayOpenHours(mMondayOpenButton.getText().toString());
		restaurant.setMondayCloseHours(mMondayCloseButton.getText().toString());

		restaurant.setTuesday(mTuesday.isChecked());
		restaurant.setTuesdayOpenHours(mTuesdayOpenButton.getText().toString());
		restaurant.setTuesdayCloseHours(mTuesdayCloseButton.getText()
				.toString());

		restaurant.setWednesday(mWednesday.isChecked());
		restaurant.setWednesdayOpenHours(mWednesdayOpenButton.getText()
				.toString());
		restaurant.setWednesdayCloseHours(mWednesdayCloseButton.getText()
				.toString());

		restaurant.setThursday(mThursday.isChecked());
		restaurant.setThursdayOpenHours(mThursdayOpenButton.getText()
				.toString());
		restaurant.setThursdayCloseHours(mThursdayCloseButton.getText()
				.toString());

		restaurant.setFriday(mFriday.isChecked());
		restaurant.setFridayOpenHours(mFridayOpenButton.getText().toString());
		restaurant.setFridayCloseHours(mFridayCloseButton.getText().toString());

		restaurant.setSaturday(mSaturday.isChecked());
		restaurant.setSaturdayOpenHours(mSaturdayOpenButton.getText()
				.toString());
		restaurant.setSaturdayCloseHours(mSaturdayCloseButton.getText()
				.toString());

		restaurant.setNotes(mNotesField.getText().toString());
		return restaurant;
	}

	// Methods for editing restaurant

	private void loadFields() {

		mTitleField.setText(mRestaurant.getTitle());
		mTitleField.clearFocus();

		mAddressField.setText(mRestaurant.getAddress());
		mAddressField.clearFocus();

		mPhoneField.setText(mRestaurant.getPhone());
		mPhoneField.clearFocus();

		mSunday.setChecked(mRestaurant.getSunday());
		mMonday.setChecked(mRestaurant.getMonday());
		mTuesday.setChecked(mRestaurant.getTuesday());
		mWednesday.setChecked(mRestaurant.getWednesday());
		mThursday.setChecked(mRestaurant.getThursday());
		mFriday.setChecked(mRestaurant.getFriday());
		mSaturday.setChecked(mRestaurant.getSaturday());

		mSundayOpenButton.setText(mRestaurant.getSundayOpenHours());
		mSundayCloseButton.setText(mRestaurant.getSundayCloseHours());

		mMondayOpenButton.setText(mRestaurant.getMondayOpenHours());
		mMondayCloseButton.setText(mRestaurant.getMondayCloseHours());

		mTuesdayOpenButton.setText(mRestaurant.getTuesdayOpenHours());
		mTuesdayCloseButton.setText(mRestaurant.getTuesdayCloseHours());

		mWednesdayOpenButton.setText(mRestaurant.getWednesdayOpenHours());
		mWednesdayCloseButton.setText(mRestaurant.getWednesdayCloseHours());

		mThursdayOpenButton.setText(mRestaurant.getThursdayOpenHours());
		mThursdayCloseButton.setText(mRestaurant.getThursdayCloseHours());

		mFridayOpenButton.setText(mRestaurant.getFridayOpenHours());
		mFridayCloseButton.setText(mRestaurant.getFridayCloseHours());

		mSaturdayOpenButton.setText(mRestaurant.getSaturdayOpenHours());
		mSaturdayCloseButton.setText(mRestaurant.getSaturdayCloseHours());

		mNotesField.setText(mRestaurant.getNotes());
		mNotesField.clearFocus();

		ParseFile file = mRestaurant.getImage();
		if (file != null) {
			Uri fileUri = Uri.parse(file.getUrl());
			Log.i("file", "file" + fileUri);
			if (getActivity() != null) {
				Picasso.with(getActivity()).load(fileUri.toString())
						.into(mRestaurantImageView, new Callback() {

							@Override
							public void onSuccess() {
								if (getActivity() != null) {
									getActivity()
											.setProgressBarIndeterminateVisibility(
													false);
								}
							}

							@Override
							public void onError() {

							}
						});
			}
		}
	}

	private void updateFields() {
		mRestaurant.setTitle(mTitleField.getText().toString());
		mRestaurant.setLowerCaseTitle(mTitleField.getText().toString()
				.toLowerCase());

		mRestaurant.setAddress(mAddressField.getText().toString());
		mRestaurant.setLowerCaseAddress(mAddressField.getText().toString()
				.toLowerCase());

		mRestaurant.setPhone(PhoneNumberUtils.formatNumber(mPhoneField
				.getText().toString()));

		mRestaurant.setSunday(mSunday.isChecked());
		mRestaurant.setMonday(mMonday.isChecked());
		mRestaurant.setTuesday(mTuesday.isChecked());
		mRestaurant.setWednesday(mWednesday.isChecked());
		mRestaurant.setThursday(mThursday.isChecked());
		mRestaurant.setFriday(mFriday.isChecked());
		mRestaurant.setSaturday(mSaturday.isChecked());

		mRestaurant.setSundayOpenHours(mSundayOpenButton.getText().toString());
		mRestaurant
				.setSundayCloseHours(mSundayCloseButton.getText().toString());

		mRestaurant.setMondayOpenHours(mMondayOpenButton.getText().toString());
		mRestaurant
				.setMondayCloseHours(mMondayCloseButton.getText().toString());

		mRestaurant
				.setTuesdayOpenHours(mTuesdayOpenButton.getText().toString());
		mRestaurant.setTuesdayCloseHours(mTuesdayCloseButton.getText()
				.toString());

		mRestaurant.setWednesdayOpenHours(mWednesdayOpenButton.getText()
				.toString());
		mRestaurant.setWednesdayCloseHours(mWednesdayCloseButton.getText()
				.toString());

		mRestaurant.setThursdayOpenHours(mThursdayOpenButton.getText()
				.toString());
		mRestaurant.setThursdayCloseHours(mThursdayCloseButton.getText()
				.toString());

		mRestaurant.setFridayOpenHours(mFridayOpenButton.getText().toString());
		mRestaurant
				.setFridayCloseHours(mFridayCloseButton.getText().toString());

		mRestaurant.setSaturdayOpenHours(mSaturdayOpenButton.getText()
				.toString());
		mRestaurant.setSaturdayCloseHours(mSaturdayCloseButton.getText()
				.toString());

		mRestaurant.setNotes(mNotesField.getText().toString());
	}

	protected void save(Restaurant restaurant) {
		restaurant.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {

					// success!
					// Toast.makeText(getActivity(), R.string.success_message,
					// Toast.LENGTH_LONG).show();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setMessage(R.string.error_saving)
							.setTitle(R.string.error_saving_title)
							.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		});
	}

	private void loadRestaurant() {
		if (getArguments() != null) {
			if (getArguments().getString(EXTRA_RESTAURANT_ID) != null) {
				String restaurantId = (String) getArguments().getString(
						EXTRA_RESTAURANT_ID);
				// Log.i("ID", restaurantId);
				// Fetch restaurant based on ID
				mSaveButton.setEnabled(false);
				mCancelButton.setEnabled(false);
				mTakePictureButton.setEnabled(false);
				getActivity().setProgressBarIndeterminateVisibility(true);
				ParseQuery<Restaurant> query = ParseQuery
						.getQuery("Restaurant");
				query.getInBackground(restaurantId,
						new GetCallback<Restaurant>() {
							public void done(Restaurant restaurant,
									ParseException e) {
								if (e == null) {

									mRestaurant = restaurant;
									mSaveButton.setEnabled(true);
									mCancelButton.setEnabled(true);
									mTakePictureButton.setEnabled(true);
									// If restaurant contains no image, turn off
									// progress
									// bar here, otherwise turn off when image
									// is done
									// loading in loadFields
									if (mRestaurant.getImage() == null) {
										if (getActivity() != null) {
											getActivity()
													.setProgressBarIndeterminateVisibility(
															false);
										}
									}
									// Check if current user is author, display
									// edit warning
									// if not, disable save and picture button
									String currentUser = ParseUser
											.getCurrentUser().getObjectId()
											.toString();
									String author = mRestaurant.getAuthor()
											.getObjectId().toString();
									if (!currentUser.equals(author)
											&& !currentUser
													.equals("e7So6F4ytk")
											&& !currentUser
													.equals("45LT0GnGVU")) {
										// if (currentUser.equals("e7So6F4ytk")
										// == false) {
										Toast.makeText(getActivity(),
												R.string.edit_error_message,
												Toast.LENGTH_LONG).show();
										mSaveButton.setEnabled(false);
										mTakePictureButton.setEnabled(false);
										// }
									}
									loadFields();
								} else {
									AlertDialog.Builder builder = new AlertDialog.Builder(
											getActivity());
									builder.setMessage(e.getMessage())
											.setTitle(
													R.string.general_error_title)
											.setPositiveButton(
													android.R.string.ok, null);
									AlertDialog dialog = builder.create();
									dialog.show();

								}
							}
						});
			}
		}
	}

	private static String padding_str(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case 0: // Take picture
				Intent takePhotoIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				// save file path
				mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				// if mMediaUri is null, storage error
				if (mMediaUri == null) {
					Toast.makeText(getActivity(),
							R.string.external_storage_error, Toast.LENGTH_LONG)
							.show();
				} else {
					// pass photo path to camera
					takePhotoIntent
							.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
					startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
				}
				break;
			case 1: // Take video
				Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
				choosePhotoIntent.setType("image/*");
				startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);
				break;
			}
		}
	};

	private Uri getOutputMediaFileUri(int mediaType) {
		// check if external storage is mounted
		if (isExternalStorageAvailable()) {
			Log.i("test", "testing" + isExternalStorageAvailable());
			// get URI

			// 1. Get the external storage directory
			String appName = getActivity().getString(R.string.app_name);
			File mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					appName);

			// 2. Create our subdirectory
			if (!mediaStorageDir.exists()) {
				// create directory, mkdirs returns boolean false if failed,
				// return nullif failed
				if (!mediaStorageDir.mkdirs()) {
					Log.i("DIR ERROR", "Failed to create directory");
					return null;
				}
			}
			// 3. Create a file name
			// 4 Create a file

			File mediaFile;
			Date now = new Date();
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
					Locale.US).format(now);

			String path = mediaStorageDir.getPath() + File.separator;
			// Check media type and create file
			if (mediaType == MEDIA_TYPE_IMAGE) {
				mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
			} else {
				return null;
			}
			Log.i("Filename", "file " + Uri.fromFile(mediaFile));
			// 5 Return the file's URI
			return Uri.fromFile(mediaFile);
		} else {
			return null;
		}

	}

	private boolean isExternalStorageAvailable() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static RestaurantFragment newInstance(String restaurantId) {
		Bundle args = new Bundle();
		args.putString(EXTRA_RESTAURANT_ID, restaurantId);

		RestaurantFragment fragment = new RestaurantFragment();
		fragment.setArguments(args);

		return fragment;
	}
}
