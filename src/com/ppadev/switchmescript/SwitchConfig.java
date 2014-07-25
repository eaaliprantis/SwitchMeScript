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

public class SwitchConfig extends Activity
{
	Button clearAllPortVlanBtn, clearSelectedPortVlanBtn, backPortVlanBtn, nextPortVlanBtn;
	CheckBox portNumCheckBox, vlanNumCheckBox;
	EditText portNumEditTextBox, vlanNumEditTextBox;
	int portNum = 0, vlanNum = 0;
	String[] firstIntentArray, vlanAssignArray, vlanDescArray;
	boolean thirdKey = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switch_config_layout);
		
		Intent intent = getIntent();
		Bundle extras = getIntent().getExtras();

		initializeButtons();
		initializeCheckBox();
		initializeEditTextBox();
		
		boolean keyValue = intent.getBooleanExtra("key", true);
		if(keyValue == true && extras != null)
		{
			firstIntentArray = intent.getStringArrayExtra("firstIntentInfo");
			boolean secondKey = intent.getBooleanExtra("secondTransferKey", false);
			if(secondKey == true)
			{
				int portNumInt = intent.getIntExtra("portNumInt", portNum);
				int vlanNumInt = intent.getIntExtra("vlanNumInt", vlanNum);
				thirdKey = intent.getBooleanExtra("thirdTransferKey", false);
				if(thirdKey == true)
				{
					vlanAssignArray = intent.getStringArrayExtra("vlanAssignArray");
					vlanDescArray = intent.getStringArrayExtra("vlanDescArray");
				}
				displayAllData(portNumInt, vlanNumInt);
			}
		}
	}
	
	public void displayAllData(int _portInt, int _vlanInt)
	{
		portNumEditTextBox.setText(Integer.toString(_portInt));
		vlanNumEditTextBox.setText(Integer.toString(_vlanInt));
		portNum = _portInt;
		vlanNum = _vlanInt;
	}
	
	public void initializeButtons()
	{
		clearAllPortVlanBtn = (Button) findViewById(R.id.clearAllPortVlanButton);
		clearAllPortVlanBtn.setVisibility(View.VISIBLE);
		clearAllPortVlanBtn.setOnClickListener(PortVlanButtonClickListener);
		
		clearSelectedPortVlanBtn = (Button) findViewById(R.id.clearSelectedPortVlanButton);
		clearSelectedPortVlanBtn.setVisibility(View.VISIBLE);
		clearSelectedPortVlanBtn.setOnClickListener(PortVlanButtonClickListener);
		
		backPortVlanBtn = (Button) findViewById(R.id.backPortVlanButton);
		backPortVlanBtn.setVisibility(View.VISIBLE);
		backPortVlanBtn.setOnClickListener(PortVlanButtonClickListener);
		
		nextPortVlanBtn = (Button) findViewById(R.id.nextPortVlanButton);
		nextPortVlanBtn.setVisibility(View.VISIBLE);
		nextPortVlanBtn.setOnClickListener(PortVlanButtonClickListener);
		
	}
	
	public void initializeCheckBox()
	{
		portNumCheckBox = (CheckBox) findViewById(R.id.portNumBox);
		vlanNumCheckBox = (CheckBox) findViewById(R.id.vlanNumBox);
	}
	
	public void initializeEditTextBox()
	{
		portNumEditTextBox = (EditText) findViewById(R.id.portNumTextBox);
		vlanNumEditTextBox = (EditText) findViewById(R.id.vlanNumTextBox);
	}
	
	public void clearAllVlanPortContent()
	{
		portNumEditTextBox.setText("");
		vlanNumEditTextBox.setText("");
		resetVlanPortCheckBoxes();
	}
	public void clearSelectedVlanPortCheckbox()
	{
		if(portNumCheckBox.isChecked() == true)
		{
			portNumEditTextBox.setText("");
		}
		if(vlanNumCheckBox.isChecked() == true)
		{
			vlanNumEditTextBox.setText("");
		}
		resetVlanPortCheckBoxes();
	}
	
	public void resetVlanPortCheckBoxes()
	{
		portNumCheckBox.setChecked(false);
		vlanNumCheckBox.setChecked(false);
	}
	
	private OnClickListener PortVlanButtonClickListener = new OnClickListener()
	{
		@Override
		public void onClick(final View v)
		{
			switch(v.getId())
			{
				case R.id.clearAllPortVlanButton:
					clearAllVlanPortContent();
					break;
				case R.id.clearSelectedPortVlanButton:
					clearSelectedVlanPortCheckbox();
					break;
				case R.id.backPortVlanButton:
					changeActivity(1);
					break;
				case R.id.nextPortVlanButton:
					changeActivity(2);
					break;
			}
		}
	};
	
	public boolean validatePortNums()
	{
		String portString = portNumEditTextBox.getText().toString();
		if(portString.equals(""))
		{
			return false;
		}
		else
		{
			int portInt = Integer.parseInt(portNumEditTextBox.getText().toString());
			if(portInt > 0 && portInt <= 64)
			{
				portNum = portInt;
				return true;
			}
			return false;
		}
	}
	
	public boolean validateVlanNums()
	{
		String vlanString = vlanNumEditTextBox.getText().toString();
		if(vlanString.equals(""))
		{
			return false;
		}
		else
		{
			int vlanInt = Integer.parseInt(vlanNumEditTextBox.getText().toString());
			if(vlanInt > 0 && vlanInt <= 128)
			{
				vlanNum = vlanInt;
				return true;
			}
			return false;
		}
	}
	
	public void displayPortVlanError(int option)
	{
		switch(option)
		{
			case 1:
				portDisplayError();
				break;
			case 2:
				vlanDisplayError();
				break;
		}
	}
	
	public void portDisplayError()
	{
		int ecolor = Color.RED;
		String eString = "Port # must be > 0 and <= 64";
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(eString);
		ssbuilder.setSpan(fgcspan, 0, eString.length(), 0);
		portNumEditTextBox.setError(ssbuilder);
	}
	
	public void vlanDisplayError()
	{
		int ecolor = Color.RED;
		String eString = "Vlan # must be > 0 and <= 128";
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(eString);
		ssbuilder.setSpan(fgcspan, 0, eString.length(), 0);
		vlanNumEditTextBox.setError(ssbuilder);
	}
	
	public void validateAllVlanPort(int option)
	{
		int num2Validate = 2;
		int numValidated = 0;
		
		final Context context = this;
		
		boolean valModelNum = validatePortNums();
		if(valModelNum == true)
		{
			numValidated++;
			portNumEditTextBox.setError(null);
		}
		else
		{
			displayPortVlanError(1);
		}
		boolean valSwitchName = validateVlanNums();
		if(valSwitchName == true)
		{
			numValidated++;
			vlanNumEditTextBox.setError(null);
		}
		else
		{
			displayPortVlanError(2);
		}
		
		if(numValidated == num2Validate && option == 2)
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			
			// set title
			alertDialogBuilder.setTitle("Next Step");
			// set dialog message
			alertDialogBuilder
			.setMessage("Would you like to move to the next step with default settings?")
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog,int id)
				{
					// if this button is clicked, close
					// current activity
					gotoActivity(1);
				}
			})
			.setNegativeButton("No",new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog,int id)
				{
					//if this button is clicked, close
					//current activity
					gotoActivity(5);
				}
			});
			
			
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			
			// show it
			alertDialog.show();
		}
		else if(numValidated != num2Validate && option == 2)
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	 
			// set title
			alertDialogBuilder.setTitle("ERROR");
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
		else if(numValidated == num2Validate && option == 1)
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			
			// set title
			alertDialogBuilder.setTitle("Back to Main Page");
			// set dialog message
			alertDialogBuilder
			.setMessage("Would you like to save your data and go back a page?")
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog,int id)
				{
					// if this button is clicked, go back to the previous page with sending data
					gotoActivity(2);
				}
			})
			.setNegativeButton("No",new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog,int id)
				{
					// if this button is clicked, just close
					// the dialog box and do nothing
					dialog.cancel();
					gotoActivity(3);
				}
			});
 
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			
			// show it
			alertDialog.show();
		}
		else if(numValidated != num2Validate && option == 1)
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			 
			// set title
			alertDialogBuilder.setTitle("ERROR");
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
	
	public void gotoActivity(int option)
	{
		switch(option)
		{
			//go to next activity with save transfer
			case 1:
				//Intent intentNextActivity = new Intent(this, vlanConfigSetup.class);
				Intent intentNextActivity = new Intent(this, VlanConfig.class);
				intentNextActivity.putExtra("key", true);
				intentNextActivity.putExtra("firstIntentInfo", firstIntentArray);
				intentNextActivity.putExtra("secondTransferKey", true);
				intentNextActivity.putExtra("portNumInt", portNum);
				intentNextActivity.putExtra("vlanNumInt", vlanNum);
				intentNextActivity.putExtra("defaultSettings", true);
				if(thirdKey == true)
				{
					intentNextActivity.putExtra("vlanAssignArray",vlanAssignArray);
					intentNextActivity.putExtra("vlanDescArray", vlanDescArray);
				}
				this.startActivity(intentNextActivity);
				break;
			//go back one activity with saving current activity info
			case 2:
				Intent intentWithSave = new Intent(this, MainActivity.class);
				intentWithSave.putExtra("key", true);
				intentWithSave.putExtra("firstIntentInfo", firstIntentArray);
				intentWithSave.putExtra("secondKey", true);
				intentWithSave.putExtra("portNumInt", portNum);
				intentWithSave.putExtra("vlanNumInt", vlanNum);
				if(thirdKey == true)
				{
					intentWithSave.putExtra("vlanAssignArray",vlanAssignArray);
					intentWithSave.putExtra("vlanDescArray", vlanDescArray);
				}
				this.startActivity(intentWithSave);
				break;
			//go back one activity without saving current activity info
			case 3:
				Intent intentWithoutSave = new Intent(this, MainActivity.class);
				intentWithoutSave.putExtra("key", true);
				intentWithoutSave.putExtra("firstIntentInfo", firstIntentArray);
				intentWithoutSave.putExtra("secondKey", false);
				if(thirdKey == true)
				{
					intentWithoutSave.putExtra("vlanAssignArray",vlanAssignArray);
					intentWithoutSave.putExtra("vlanDescArray", vlanDescArray);
				}
				this.startActivity(intentWithoutSave);
				break;
			case 4:
				Intent intentNextActivityWithoutDefault = new Intent(this, VlanConfig.class);
				intentNextActivityWithoutDefault.putExtra("key", true);
				intentNextActivityWithoutDefault.putExtra("firstIntentInfo", firstIntentArray);
				intentNextActivityWithoutDefault.putExtra("secondTransferKey", true);
				intentNextActivityWithoutDefault.putExtra("portNumInt", portNum);
				intentNextActivityWithoutDefault.putExtra("vlanNumInt", vlanNum);
				intentNextActivityWithoutDefault.putExtra("defaultSettings", false);
				if(thirdKey == true)
				{
					intentNextActivityWithoutDefault.putExtra("vlanAssignArray",vlanAssignArray);
					intentNextActivityWithoutDefault.putExtra("vlanDescArray", vlanDescArray);
				}
				this.startActivity(intentNextActivityWithoutDefault);
				break;
		}
	}	
	
	public void changeActivity(int option)
	{
		switch(option)
		{
			case 1:
				validateAllVlanPort(1);
				break;
			case 2:
				validateAllVlanPort(2);
				break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
