package com.openerp.addons.idea;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.openerp.R;

public class DatePicker extends LinearLayout {

	private int startYear = 1900;
	private int endYear = 2100;

	private View myPickerView;

	private Button month_plus;
	private EditText month_display;
	private Button month_minus;

	private Button date_plus;
	private EditText date_display;
	private Button date_minus;

	private Button year_plus;
	private EditText year_display;
	private Button year_minus;

	private Calendar cal;

	public DatePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context mContext) {
		LayoutInflater inflator = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		myPickerView = inflator.inflate(R.layout.datepicker, null);
		this.addView(myPickerView);

		initializeReference();
	}

	private void initializeReference() {

		GradientDrawable gd = new GradientDrawable(
				GradientDrawable.Orientation.RIGHT_LEFT, new int[] {
						Color.MAGENTA, Color.RED });
		gd.setCornerRadius(0f);

		month_plus = (Button) myPickerView.findViewById(R.id.month_plus);
		month_plus.setOnClickListener(month_plus_listener);
		month_display = (EditText) myPickerView
				.findViewById(R.id.month_display);
		month_minus = (Button) myPickerView.findViewById(R.id.month_minus);
		month_minus.setOnClickListener(month_minus_listener);

		date_plus = (Button) myPickerView.findViewById(R.id.date_plus);
		date_plus.setOnClickListener(date_plus_listener);
		date_display = (EditText) myPickerView.findViewById(R.id.date_display);
		date_display.addTextChangedListener(date_watcher);
		date_minus = (Button) myPickerView.findViewById(R.id.date_minus);
		date_minus.setOnClickListener(date_minus_listener);

		year_plus = (Button) myPickerView.findViewById(R.id.year_plus);
		year_plus.setOnClickListener(year_plus_listener);
		year_display = (EditText) myPickerView.findViewById(R.id.year_display);
		year_display.setOnFocusChangeListener(mLostFocusYear);
		year_display.addTextChangedListener(year_watcher);
		year_minus = (Button) myPickerView.findViewById(R.id.year_minus);
		year_minus.setOnClickListener(year_minus_listener);

//		month_plus.setBackgroundDrawable(gd);
//		month_display.setBackgroundDrawable(gd);
//		month_minus.setBackgroundDrawable(gd);
//		date_plus.setBackgroundDrawable(gd);
//		date_display.setBackgroundDrawable(gd);
//		date_minus.setBackgroundDrawable(gd);
//		year_plus.setBackgroundDrawable(gd);
//		year_display.setBackgroundDrawable(gd);
//		year_minus.setBackgroundDrawable(gd);

		initData();
		initFilterNumericDigit();

	}

	private void initData() {
		cal = Calendar.getInstance();
		month_display.setText(months[cal.get(Calendar.MONTH)]);
		date_display.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
	}

	private void initFilterNumericDigit() {

		try {
			date_display.setFilters(new InputFilter[] { new InputFilterMinMax(
					1, cal.getActualMaximum(Calendar.DAY_OF_MONTH)) });

			InputFilter[] filterArray_year = new InputFilter[1];
			filterArray_year[0] = new InputFilter.LengthFilter(4);
			year_display.setFilters(filterArray_year);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void changeFilter() {
		try {

			date_display.setFilters(new InputFilter[] { new InputFilterMinMax(
					1, 31) });
			// cal.getActualMaximum(Calendar.DAY_OF_MONTH)
		} catch (Exception e) {
			date_display.setText("" + cal.get(Calendar.DAY_OF_MONTH));
			Log.i("error occuerd" + cal.get(Calendar.DAY_OF_MONTH), "exception");
			e.printStackTrace();
		}
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) throws Exception {
		if (startYear < 2100 && startYear > 1900) {
			this.startYear = startYear;
			swapStartEndYear();
		} else {
			throw new NumberFormatException(
					"StartYear should be in the range of 1900 to 2100");
		}
	}

	public void reset() {
		initData();
	}

	public int getEndYear() {
		return endYear;
	}

	public void setDateChangedListener(DateWatcher listener) {
		this.mDateWatcher = listener;
	}

	public void removeDateChangedListener() {
		this.mDateWatcher = null;
	}

	public void setEndYear(int endYear) throws Exception {
		if (endYear < 2100 && endYear > 1900) {
			this.endYear = endYear;
			swapStartEndYear();
		} else {
			throw new NumberFormatException(
					"endYear should be in the range of 1900 to 2100");
		}
	}

	String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
			"Sep", "Oct", "Nov", "Dec" };

	View.OnClickListener month_plus_listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			try {
				cal.add(Calendar.MONTH, 1);

				month_display.setText(months[cal.get(Calendar.MONTH)]);
				year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));

				date_display.setText(String.valueOf(cal
						.get(Calendar.DAY_OF_MONTH)));

				changeFilter();
				sendToListener();
			} catch (Exception e) {
				Log.e("", e.toString());
			}
		}
	};
	View.OnClickListener month_minus_listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				cal.add(Calendar.MONTH, -1);

				month_display.setText(months[cal.get(Calendar.MONTH)]);
				year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));

				date_display.setText(String.valueOf(cal
						.get(Calendar.DAY_OF_MONTH)));
				changeFilter();
				sendToListener();
			} catch (Exception e) {
				Log.e("", e.toString());
			}
		}
	};

	View.OnClickListener date_plus_listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			try {
				date_display.requestFocus();
				cal.add(Calendar.DAY_OF_MONTH, 1);

				month_display.setText(months[cal.get(Calendar.MONTH)]);
				year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
				if ((String.valueOf(cal.get(Calendar.DAY_OF_MONTH) + "")
						.equals(""))) {
					date_display.setText("31");
				} else {
					date_display.setText(String.valueOf(cal
							.get(Calendar.DAY_OF_MONTH)));
				}
				changeFilter();
				sendToListener();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};
	View.OnClickListener date_minus_listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			try {
				date_display.requestFocus();
				cal.add(Calendar.DAY_OF_MONTH, -1);

				month_display.setText(months[cal.get(Calendar.MONTH)]);
				year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));

				Log.d("31 called minus"
						+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH)),
						"ok...!");

				date_display.setText(String.valueOf(cal
						.get(Calendar.DAY_OF_MONTH)));

				changeFilter();
				sendToListener();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	View.OnClickListener year_plus_listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			try {
				year_display.requestFocus();

				if (cal.get(Calendar.YEAR) >= endYear) {

					cal.set(Calendar.YEAR, startYear);

				} else {
					cal.add(Calendar.YEAR, +1);

				}

				month_display.setText(months[cal.get(Calendar.MONTH)]);
				year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
				date_display.setText(String.valueOf(cal
						.get(Calendar.DAY_OF_MONTH)));

				changeFilter();
				sendToListener();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};
	View.OnClickListener year_minus_listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			try {
				year_display.requestFocus();

				if (cal.get(Calendar.YEAR) <= startYear) {
					cal.set(Calendar.YEAR, endYear);

				} else {
					cal.add(Calendar.YEAR, -1);

				}

				month_display.setText(months[cal.get(Calendar.MONTH)]);
				year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));

				date_display.setText(String.valueOf(cal
						.get(Calendar.DAY_OF_MONTH)));

				changeFilter();
				sendToListener();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	View.OnFocusChangeListener mLostFocusYear = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {

				year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
			}
		}
	};

	class InputFilterMinMax implements InputFilter {

		private int min, max;

		public InputFilterMinMax(int min, int max) {
			this.min = min;
			this.max = max;
		}

		public InputFilterMinMax(String min, String max) {
			this.min = Integer.parseInt(min);
			this.max = Integer.parseInt(max);
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			try {
				int input = Integer.parseInt(dest.toString()
						+ source.toString());
				if (isInRange(min, max, input)) {
					return null;
				}
			} catch (NumberFormatException nfe) {
			}
			return "";
		}

		private boolean isInRange(int a, int b, int c) {
			return b > a ? c >= a && c <= b : c >= b && c <= a;
		}
	}

	TextWatcher date_watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

			try {
				Log.e("", "afterTextChanged date=: " + (Calendar.DAY_OF_MONTH)
						+ "");

				if (s.toString().length() > 0) {

					cal.set(Calendar.DAY_OF_MONTH,
							Integer.parseInt(s.toString()));

					// date_display.setText("9");
					month_display.setText(months[cal.get(Calendar.MONTH)]);

					sendToListener();
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	TextWatcher year_watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			try {
				if (s.toString().length() == 4) {
					int year = Integer.parseInt(s.toString());

					if (year > endYear) {
						cal.set(Calendar.YEAR, endYear);
					} else if (year < startYear) {
						cal.set(Calendar.YEAR, startYear);
					} else {
						cal.set(Calendar.YEAR, year);
					}
				}

				sendToListener();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private void sendToListener() {

		if (mDateWatcher != null) {
			mDateWatcher.onDateChanged(cal);
		}

	}

	DateWatcher mDateWatcher = null;

	public interface DateWatcher {
		void onDateChanged(Calendar c);
	}

	private void swapStartEndYear() {
		if (this.startYear > this.endYear) {
			int temp = endYear;
			endYear = startYear;
			startYear = temp;
		}

		cal.set(Calendar.YEAR, endYear);
		initDisplay();

	}

	private void initDisplay() {
		Log.d(" date init =" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)),
				"");
		month_display.setText(months[cal.get(Calendar.MONTH)]);

		date_display.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));

		year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));

	}
}
