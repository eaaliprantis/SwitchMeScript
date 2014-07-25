package com.ppadev.switchmescript;

//Import Android OS features
import android.os.*;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

//Import Android Application features
import android.app.Activity;
import android.app.AlertDialog;

//Import Android content features
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

//Import Android Database
import android.database.Cursor;

//Import Bitmap Info
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;

//Import Android IO features
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

//Import Language features
import java.lang.Math;
import java.lang.String;

//Import Java Math
import java.math.BigDecimal;

//Import Android URI
import android.net.Uri;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
//Import Android Util feature
import android.util.Log;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Import Android View Features
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

//Import Android Widget Features
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	Button timeZoneMinusBtn, timeZonePlusBtn, clearAllBtn, clearSelectionBtn, nextBtn;
	CheckBox modelNumCheckBox, switchNameCheckBox, timeZoneCheckBox, 
			gatewayAddressCheckBox, ipAddressCheckBox, subnetMaskCheckBox, locationCheckBox;
	EditText modelNumEditTextBox, switchNameEditTextBox, timeZoneEditTextBox, 
				gatewayAddressEditTextBox, ipAddressEditTextBox, subnetMaskEditTextBox, locationEditTextBox;
	
	private Pattern AnyIpAddressPattern;
	private Matcher AnyIpAddressMatcher;
	private int timeZoneCounter = 0, portNumInt = 0, vlanNumInt = 0;
	private String[] intentStoreArray, vlanAssignArray, vlanDescArray;
	private boolean transferFileBool = false, thirdTransferBool = false;;
	private static final String ANY_IP_ADDRESS_PATTERN =
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//initialize Buttons
		initializeAllButtons();
		
		//initialize EditText
		initializeAllEditTexts();
		
		//initialize CheckBox
		initializeAllCheckBox();
	
		boolean storedInfoFromActivity = checkInfoFromActivity();
		if(storedInfoFromActivity == true)
		{
			Intent intent = getIntent();
			Bundle extras = getIntent().getExtras();
			
			boolean keyValue = intent.getBooleanExtra("key", true);
			if(keyValue == true && extras != null)
			{
				intentStoreArray = intent.getStringArrayExtra("firstIntentInfo");
				boolean secondKeyValue = intent.getBooleanExtra("secondKey", false);
				if(secondKeyValue == true)
				{
					portNumInt = intent.getIntExtra("portNumInt", portNumInt);
					vlanNumInt = intent.getIntExtra("vlanNumInt", vlanNumInt);
					transferFileBool = true;
				}
				else
				{
					transferFileBool = false;
					boolean thirdKeyValue = intent.getBooleanExtra("thirdTransferKey",false);
					if(thirdKeyValue == true)
					{
						vlanAssignArray = intent.getStringArrayExtra("vlanAssignArray");
						vlanDescArray = intent.getStringArrayExtra("vlanDescArray");
						thirdTransferBool = true;
					}
				}
				displayAllData();
			}
		}
	}
	
	public void displayAllData()
	{
		int intentArrayLength = intentStoreArray.length;
		if(intentArrayLength != 0)
		{
			for(int i = 0; i < intentArrayLength; i++)
			{
				switch(i)
				{
					case 0:
						modelNumEditTextBox.setText(intentStoreArray[i]);
						break;
					case 1:
						switchNameEditTextBox.setText(intentStoreArray[i]);
						break;
					case 2:
						timeZoneEditTextBox.setText(intentStoreArray[i]);
						timeZoneCounter = Integer.parseInt(intentStoreArray[i]);
						break;
					case 3:
						gatewayAddressEditTextBox.setText(intentStoreArray[i]);
						break;
					case 4:
						ipAddressEditTextBox.setText(intentStoreArray[i]);
						break;
					case 5:
						subnetMaskEditTextBox.setText(intentStoreArray[i]);
						break;
					case 6:
						locationEditTextBox.setText(intentStoreArray[i]);
						break;
				}
			}
		}
	}
	
	public boolean checkInfoFromActivity()
	{
		Intent tempIntent = getIntent();
		Bundle tempExtras = getIntent().getExtras();
		
		if(tempExtras != null)
		{
			return true;
		}
		return false;
	}
	
	//Function that initializes All Buttons
	public void initializeAllButtons()
	{
		timeZoneMinusBtn = (Button)findViewById(R.id.timeZoneMinusButton);
		timeZoneMinusBtn.setVisibility(View.VISIBLE);
		timeZoneMinusBtn.setOnClickListener(GlobalButtonClickListener);
		
		timeZonePlusBtn = (Button)findViewById(R.id.timeZonePlusButton);
		timeZonePlusBtn.setVisibility(View.VISIBLE);
		timeZonePlusBtn.setOnClickListener(GlobalButtonClickListener);
		
		clearAllBtn = (Button)findViewById(R.id.clearAllButton);
		clearAllBtn.setVisibility(View.VISIBLE);
		clearAllBtn.setOnClickListener(GlobalButtonClickListener);
		
		clearSelectionBtn = (Button)findViewById(R.id.clearSelectedButton);
		clearSelectionBtn.setVisibility(View.VISIBLE);
		clearSelectionBtn.setOnClickListener(GlobalButtonClickListener);
		
		nextBtn = (Button)findViewById(R.id.nextButton);
		nextBtn.setVisibility(View.VISIBLE);
		nextBtn.setOnClickListener(GlobalButtonClickListener);
	}

	public void initializeAllEditTexts()
	{
		modelNumEditTextBox = (EditText) findViewById(R.id.modelNumTextBox);
		switchNameEditTextBox = (EditText) findViewById(R.id.switchNameTextBox);
		timeZoneEditTextBox = (EditText) findViewById(R.id.timeZoneTextBox);
		gatewayAddressEditTextBox = (EditText) findViewById(R.id.gatewayAddressTextBox);
		ipAddressEditTextBox = (EditText) findViewById(R.id.ipAddressTextBox);
		subnetMaskEditTextBox = (EditText) findViewById(R.id.subnetMaskTextBox);
		locationEditTextBox = (EditText) findViewById(R.id.locationTextBox);
	}
	
	public void initializeAllCheckBox()
	{
		modelNumCheckBox = (CheckBox) findViewById(R.id.modelNumBox);
		switchNameCheckBox = (CheckBox) findViewById(R.id.switchNameBox);
		timeZoneCheckBox = (CheckBox) findViewById(R.id.timeZoneBox);
		gatewayAddressCheckBox = (CheckBox) findViewById(R.id.gatewayAddressBox);
		ipAddressCheckBox = (CheckBox) findViewById(R.id.ipAddressBox);
		subnetMaskCheckBox = (CheckBox) findViewById(R.id.subnetMaskBox);
		locationCheckBox = (CheckBox) findViewById(R.id.locationBox);
	}
	
	public void validateAll()
	{
		int num2Validate = 7;
		int numValidated = 0;
		
		final Context context = this;
		
		boolean valModelNum = validateModelNum();
		if(valModelNum == true)
		{
			numValidated++;
		}
		boolean valSwitchName = validateSwitchName();
		if(valSwitchName == true)
		{
			numValidated++;
		}
		boolean valLocation = validateLocation();
		if(valLocation == true)
		{
			numValidated++;
		}
		boolean valTimeZone = validateTimeZone();
		if(valTimeZone == true)
		{
			numValidated++;
		}
		boolean valAllIPAddress = checkingAllIPAddress();
		if(valAllIPAddress == true)
		{
			numValidated += 3;
		}
		if(numValidated == num2Validate)
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			
			// set title
			alertDialogBuilder.setTitle("Next Step");
			// set dialog message
			alertDialogBuilder
			.setMessage("Would you like to move to the next step?")
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog,int id)
				{
					// if this button is clicked, close
					// current activity
					setInfoToActivity();
				}
			})
			.setNegativeButton("No",new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog,int id)
				{
					// if this button is clicked, just close
					// the dialog box and do nothing
					dialog.cancel();
				}
			});
			
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			
			// show it
			alertDialog.show();
		}
		else
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			
			// set title
			alertDialogBuilder.setTitle("ERROR(S)");
			// set dialog message
			alertDialogBuilder
			.setMessage("Please fix your errors before you proceed")
			.setCancelable(false)
			.setPositiveButton("OK",new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog,int id)
				{
					// if this button is clicked, close
					// current activity
					dialog.cancel();
				}
			});
			
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			
			// show it
			alertDialog.show();				
		}
	}
	
	public void setInfoToActivity()
	{
		String[] intentStuff = new String[7];
		String modelNumString = modelNumEditTextBox.getText().toString();
		String switchNameString = switchNameEditTextBox.getText().toString();
		String timeZoneString = Integer.toString(Integer.parseInt(timeZoneEditTextBox.getText().toString()));
		String gatewayAddressString = gatewayAddressEditTextBox.getText().toString();
		String ipAddressString = ipAddressEditTextBox.getText().toString();
		String subnetMaskString = subnetMaskEditTextBox.getText().toString();
		String locationString = locationEditTextBox.getText().toString();
		intentStuff[0] = modelNumString;
		intentStuff[1] = switchNameString;
		intentStuff[2] = timeZoneString;
		intentStuff[3] = gatewayAddressString;
		intentStuff[4] = ipAddressString;
		intentStuff[5] = subnetMaskString;
		intentStuff[6] = locationString;
		
		Intent intent = new Intent(this, SwitchConfig.class);
		intent.putExtra("key", true);
		intent.putExtra("firstIntentInfo", intentStuff);
		if(transferFileBool == true)
		{
			intent.putExtra("secondTransferKey", true);
			intent.putExtra("portNumInt", portNumInt);
			intent.putExtra("vlanNumInt", vlanNumInt);
			intent.putExtra("thirdTransferKey", false);
			if(thirdTransferBool == true)
			{
				intent.putExtra("thirdTransferKey", thirdTransferBool);
				intent.putExtra("vlanAssignArray", vlanAssignArray);
			}
		}
		else
		{
			intent.putExtra("secondTransferKey", false);
			if(thirdTransferBool == true)
			{
				intent.putExtra("thirdTransferKey", thirdTransferBool);
				intent.putExtra("vlanAssignArray", vlanAssignArray);
			}
		}
		this.startActivity(intent);
	}
	
	public boolean validateTimeZone()
	{
		if(timeZoneCounter >= -12 && timeZoneCounter <= 14)
		{
			return true;
		}
		return false;
	}
	
	public boolean validateModelNum()
	{
		int ecolor = Color.RED;
		String eString = "Model number required";
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(eString);
		ssbuilder.setSpan(fgcspan, 0, eString.length(), 0);
		if(modelNumEditTextBox.getText().toString().length() == 0)
		{
			modelNumEditTextBox.setError(ssbuilder);
			return false;
		}
		else
		{
			modelNumEditTextBox.setError(null);
			return true;
		}
	}
	
	public boolean validateSwitchName()
	{
		int ecolor = Color.RED;
		String eString = "Switch name required";
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(eString);
		ssbuilder.setSpan(fgcspan, 0, eString.length(), 0);
		if(switchNameEditTextBox.getText().toString().length() == 0)
		{
			switchNameEditTextBox.setError(ssbuilder);
			return false;
		}
		else
		{
			switchNameEditTextBox.setError(null);
			return true;
		}
	}
	
	public boolean validateLocation()
	{
		int ecolor = Color.RED;
		String eString = "Location required";
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(eString);
		ssbuilder.setSpan(fgcspan, 0, eString.length(), 0);
		if(locationEditTextBox.getText().toString().length() == 0)
		{
			locationEditTextBox.setError(ssbuilder);
			return false;
		}
		else
		{
			locationEditTextBox.setError(null);
			return true;
		}
	}

	public void timeZoneController()
	{
		if(timeZoneCounter == -12 || timeZoneCounter == 14)
		{
			if(timeZoneCounter == -12)
			{
				timeZoneMinusBtn.setEnabled(false);
				timeZonePlusBtn.setEnabled(true);
			}
			else if(timeZoneCounter == 14)
			{
				timeZoneMinusBtn.setEnabled(true);
				timeZonePlusBtn.setEnabled(false);
			}
		}
		else
		{
			timeZoneMinusBtn.setEnabled(true);
			timeZonePlusBtn.setEnabled(true);			
		}
	}
	
	public void timeZoneChange(int option)
	{
		timeZoneController();
		//Add button clicked
		if(option == 1)
		{
			timeZoneCounter++;
		}
		//Subtract button clicked
		else if(option == 2)
		{
			timeZoneCounter--;
		}
		setTimeZoneTextBox();
		timeZoneController();
	}
	
	public void setTimeZoneTextBox()
	{
		String timeZoneCounterString = Integer.toString(timeZoneCounter);
		timeZoneEditTextBox.setText(timeZoneCounterString);
	}
	
	public void displayAnyIPAddressValidationError(int option)
	{
		int ecolor = Color.RED;
		String eString = "IP Address required";
		//Option 1 selected: Gateway
		if(option == 1)
		{
			eString = "Gateway should be: (1-255).(1-255).(1-255).(1-255)";
			ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
			SpannableStringBuilder ssbuilder = new SpannableStringBuilder(eString);
			ssbuilder.setSpan(fgcspan, 0, eString.length(), 0);
			gatewayAddressEditTextBox.setError(ssbuilder);
		}
		//Option 2 selected: IP Address
		else if(option == 2)
		{
			eString = "IP Address should be: (1-255).(1-255).(1-255).(1-255)";
			ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
			SpannableStringBuilder ssbuilder = new SpannableStringBuilder(eString);
			ssbuilder.setSpan(fgcspan, 0, eString.length(), 0);
			ipAddressEditTextBox.setError(ssbuilder);
		}
		//Option 3 selected: SubnetMask
		else if(option == 3)
		{
			eString = "SubnetMask should be: (1-255).(1-255).(1-255).(1-255)";
			ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
			SpannableStringBuilder ssbuilder = new SpannableStringBuilder(eString);
			ssbuilder.setSpan(fgcspan, 0, eString.length(), 0);
			subnetMaskEditTextBox.setError(ssbuilder);
		}
	}
		
	public boolean checkingAllIPAddress()
	{
		int validCounter = 0;
		boolean allIPValidated = false;
		//Option 1 selected: Gateway
		if(validateAnyIpAddress(1) == false)
		{
			displayAnyIPAddressValidationError(1);
			return allIPValidated;
		}
		else
		{
			gatewayAddressEditTextBox.setError(null);
			validCounter++;
		}
		//Option 2 selected: IP Address
		if(validateAnyIpAddress(2) == false)
		{
			displayAnyIPAddressValidationError(2);
			return allIPValidated;
		}
		else
		{
			ipAddressEditTextBox.setError(null);
			validCounter++;
		}
		//Option 3 selected: SubnetMask
		if(validateAnyIpAddress(3) == false)
		{
			displayAnyIPAddressValidationError(3);
			return allIPValidated;
		}
		else
		{
			subnetMaskEditTextBox.setError(null);
			validCounter++;
		}
		if(validCounter == 3)
		{
			allIPValidated = true;
		}
		return allIPValidated;
	}
	
	public boolean validateAnyIpAddress(int option)
	{
		//AnyIpAddressPattern - pattern
		//AnyIpAddressMatcher - matcher
		//ANY_IP_ADDRESS_PATTERN - String
		AnyIpAddressPattern = Pattern.compile(ANY_IP_ADDRESS_PATTERN);
		
		String globalIPAddressString = "ERROR";
		boolean boolIPAddress = false;
		//Option 1 selected: Gateway
		if(option == 1)
		{
			globalIPAddressString = gatewayAddressEditTextBox.getText().toString();
			boolIPAddress = validateMatchString(globalIPAddressString);
		}
		//Option 2 selected: IP Address
		else if(option == 2)
		{
			globalIPAddressString = ipAddressEditTextBox.getText().toString();
			boolIPAddress = validateMatchString(globalIPAddressString);
		}
		//Option 3 selected: SubnetMask
		else if(option == 3)
		{
			globalIPAddressString = subnetMaskEditTextBox.getText().toString();
			boolIPAddress = validateMatchString(globalIPAddressString);
		}
		//Option null selected: ERROR
		else
		{
			globalIPAddressString = "ERROR";
			boolIPAddress = false;
		}
		return boolIPAddress;
	}
	
	private boolean validateMatchString(final String ipAddressString)
	{
		AnyIpAddressMatcher = AnyIpAddressPattern.matcher(ipAddressString);
		return AnyIpAddressMatcher.matches();
	}
	
	private OnClickListener GlobalButtonClickListener = new OnClickListener()
	{
		@Override
		public void onClick(final View v)
		{
			switch(v.getId())
			{
				case R.id.timeZoneMinusButton:
					timeZoneChange(2);
					break;
				case R.id.timeZonePlusButton:
					timeZoneChange(1);
					break;
				case R.id.clearAllButton:
					clearAllContent();
					break;
				case R.id.clearSelectedButton:
					clearContentCheckbox();
					break;
				case R.id.nextButton:
					validateAll();
					break;
			}
		}
	};
	
	public void clearContentCheckbox()
	{
		if(modelNumCheckBox.isChecked() == true)
		{
			modelNumEditTextBox.setText("");
		}
		if(switchNameCheckBox.isChecked() == true)
		{
			switchNameEditTextBox.setText("");
		}
		if(timeZoneCheckBox.isChecked() == true)
		{
			timeZoneEditTextBox.setText("0"); 
		}
		if(gatewayAddressCheckBox.isChecked() == true)
		{
			gatewayAddressEditTextBox.setText("");
		}
		if(ipAddressCheckBox.isChecked() == true)
		{
			ipAddressEditTextBox.setText("");
		}
		if(subnetMaskCheckBox.isChecked() == true)
		{
			subnetMaskEditTextBox.setText("");
		}
		if(locationCheckBox.isChecked() == true)
		{
			locationEditTextBox.setText("");
		}
		resetCheckBoxes();
	}
	
	public void resetCheckBoxes()
	{
		modelNumCheckBox.setChecked(false);
		switchNameCheckBox.setChecked(false);
		timeZoneCheckBox.setChecked(false);
		gatewayAddressCheckBox.setChecked(false);
		ipAddressCheckBox.setChecked(false);
		subnetMaskCheckBox.setChecked(false);
		locationCheckBox.setChecked(false);
	}
	
	public void clearAllContent()
	{
		modelNumEditTextBox.setText("");
		switchNameEditTextBox.setText("");
		timeZoneEditTextBox.setText("0");
		timeZoneCounter = 0;
		gatewayAddressEditTextBox.setText("");
		ipAddressEditTextBox.setText("");
		subnetMaskEditTextBox.setText("");
		locationEditTextBox.setText("");
		resetCheckBoxes();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
