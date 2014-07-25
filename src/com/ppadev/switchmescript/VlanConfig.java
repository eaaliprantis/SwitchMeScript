package com.ppadev.switchmescript;

import android.os.*;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

//Import Android Application features
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;

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
import java.lang.*;

//Import Java Math
import java.math.BigDecimal;

//Import Android URI
import android.net.Uri;

import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
//Import Android Util feature
import android.util.Log;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Import Android View Features
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;

//Import Android Widget Features
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class VlanConfig extends Activity
{
	String countryName[] = { "India", "Pakistan", "China", "Nepal", "Bangladesh" };
	int portNum = 0, vlanNum = 0, vlanNumListCounter = 0;
	boolean defaultSettings = false;
	String[] firstIntentArray;
	
	ArrayList<CheckBox> vlanCheckBoxList = new ArrayList<CheckBox>();
	ArrayList<EditText> vlanTextBoxList = new ArrayList<EditText>();
	ArrayList<Integer> vlanAssignCheckList = new ArrayList<Integer>();
	ArrayList<Integer> vlanAssignTextList = new ArrayList<Integer>();
	ArrayList<Integer> vlanDescCheckList = new ArrayList<Integer>();
	ArrayList<Integer> vlanDescTextList = new ArrayList<Integer>();
	
	ArrayList<Integer> dupIndex = new ArrayList<Integer>();
	ArrayList<Integer> dupIndexValue = new ArrayList<Integer>();
	ArrayList<Integer> intList = new ArrayList<Integer>();
	ArrayList<Integer> intIndexList = new ArrayList<Integer>();
	
	ArrayList<String> stringList = new ArrayList<String>();
	ArrayList<Integer> stringListIndex = new ArrayList<Integer>();
	ArrayList<Integer> stringDupIndex = new ArrayList<Integer>();
	ArrayList<String> stringDupIndexValue = new ArrayList<String>();
	
	int vlanAssignCheckBoxCounter = 100;
	int	vlanAssignTextBoxCounter = 200;
	int	vlanDescCheckBoxCounter = 300;
	int	vlanDescTextBoxCounter = 400;
	
	CheckBox vlanAssignCheckBox;
	EditText vlanAssignTextBox;
	CheckBox vlanDescCheckBox;
	EditText vlanDescTextBox;
	
	Button clearAllButton;
	Button clearSelectedButton;
	Button backButton;
	Button nextButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vlan_config_layout);
		
		Intent intent = getIntent();
		Bundle extras = getIntent().getExtras();
		
		if(extras != null)
		{
			boolean keyValue = intent.getBooleanExtra("key", true);
			if(keyValue == true && extras != null)
			{
				firstIntentArray = intent.getStringArrayExtra("firstIntentInfo");
				boolean secondTKey = intent.getBooleanExtra("secondTransferKey", false);
				if(secondTKey == true)
				{
					int portNumInt = intent.getIntExtra("portNumInt", portNum);
					int vlanNumInt = intent.getIntExtra("vlanNumInt", vlanNum);
					boolean default_settings = intent.getBooleanExtra("defaultSettings", false);
					setVlanPortNums(vlanNumInt, portNumInt, default_settings);
				}
			}			
		}
		
		LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.linear1);
		
		TextView title = new TextView(this);
		title.setText("# of Vlans: " + Integer.toString(vlanNum));
		title.setTextColor(Color.BLUE);
		mLinearLayout.addView(title);

		if(vlanNumListCounter >= 100)
		{
			vlanAssignCheckBoxCounter += vlanNumListCounter;
			vlanAssignTextBoxCounter += vlanNumListCounter;
			vlanDescCheckBoxCounter += vlanNumListCounter;
			vlanDescTextBoxCounter += vlanNumListCounter;
		}
		
		if(defaultSettings == true)
		{
			for(int i = 0; i < vlanNumListCounter; i++)
			{
				//Create vlan Assign layout
				LinearLayout vlanAssign_layout = new LinearLayout(this);
				vlanAssign_layout.setOrientation(LinearLayout.HORIZONTAL);
				vlanAssign_layout.setFocusableInTouchMode(true);
				
				vlanAssignCheckBox = new CheckBox(this);
				int number = i + 1;
				vlanAssignCheckBox.setText(Integer.toString(number) + ".) Vlan # Assign: " );
				
				//Assigning the ID to the box and to the arrayList			
				int vlanAssignIDCheckBox = i + vlanAssignCheckBoxCounter;
				vlanAssignCheckBox.setId(vlanAssignIDCheckBox);
				vlanAssignCheckList.add(vlanAssignIDCheckBox);
				
				// Defining the LinearLayout layout parameters to fill the parent.
		        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
		            LinearLayout.LayoutParams.WRAP_CONTENT,
		            LinearLayout.LayoutParams.WRAP_CONTENT);
		        vlanAssignCheckBox.setLayoutParams(llp);
		        vlanCheckBoxList.add(vlanAssignCheckBox);
				vlanAssign_layout.addView(vlanAssignCheckBox);
				
				vlanAssignTextBox = new EditText(this);
				vlanAssignTextBox.setMaxLines(1);
				vlanAssignTextBox.setFocusable(true);
				vlanAssignTextBox.setFocusableInTouchMode(true);
				vlanAssignTextBox.setInputType(InputType.TYPE_CLASS_NUMBER);
				vlanAssignTextBox.setText(i);
				vlanAssignTextBox.setMinimumWidth(125);
				
				//Adding restrictions to the character
				int maxLength = 15;
				InputFilter[] fArray = new InputFilter[1];
				fArray[0] = new InputFilter.LengthFilter(maxLength);
				vlanAssignTextBox.setFilters(fArray);
				
				//Assigning the ID to the box and to the arrayList			
				int vlanAssignIDTextBox = i + vlanAssignTextBoxCounter;
				vlanAssignTextBox.setId(vlanAssignIDTextBox);
				vlanAssignTextList.add(vlanAssignIDTextBox);
				vlanTextBoxList.add(vlanAssignTextBox);
				vlanAssign_layout.addView(vlanAssignTextBox);
				
				//Create vlan Desc layout
				LinearLayout vlanDesc_layout = new LinearLayout(this);
				vlanDesc_layout.setOrientation(LinearLayout.HORIZONTAL);
				vlanDesc_layout.setFocusableInTouchMode(true);
				
				vlanDescCheckBox = new CheckBox(this);
				vlanDescCheckBox.setText(Integer.toString(number) + ".) Vlan # Desc:   " );
				
				//Assigning the ID to the box and to the arrayList
				int vlanDescIDCheckBox = i + vlanDescCheckBoxCounter;
				vlanDescCheckBox.setId(vlanDescIDCheckBox);
				vlanDescCheckList.add(vlanDescIDCheckBox);
				
				// Defining the LinearLayout layout parameters to fill the parent.
		        LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(
		            LinearLayout.LayoutParams.WRAP_CONTENT,
		            LinearLayout.LayoutParams.WRAP_CONTENT);
		        vlanDescCheckBox.setLayoutParams(llp2);
		        vlanCheckBoxList.add(vlanDescCheckBox);
		        vlanDesc_layout.addView(vlanDescCheckBox);
				
				vlanDescTextBox = new EditText(this);
				vlanDescTextBox.setFocusable(true);
				vlanDescTextBox.setFocusableInTouchMode(true);
				vlanDescTextBox.setMaxLines(1);
				vlanDescTextBox.setMinimumWidth(125);
				vlanDescTextBox.setText("default_vlan_" + i);
				vlanDescTextBox.setInputType(InputType.TYPE_CLASS_TEXT);
				
				//Adding restrictions to the character
				int maxLength2 = 15;
				InputFilter[] fArray2 = new InputFilter[1];
				fArray2[0] = new InputFilter.LengthFilter(maxLength2);
				vlanDescTextBox.setFilters(fArray2);
				
				//Assigning the ID to the box and to the arrayList
				int vlanDescIDTextBox = i + vlanDescTextBoxCounter;
				vlanDescTextBox.setId(vlanDescIDTextBox);
				vlanDescTextList.add(vlanDescIDTextBox);
				vlanTextBoxList.add(vlanDescTextBox);
				vlanDesc_layout.addView(vlanDescTextBox);
				
				mLinearLayout.addView(vlanAssign_layout);
				mLinearLayout.addView(vlanDesc_layout);
			}			
		}
		else
		{
			for(int i = 0; i < vlanNumListCounter; i++)
			{
				//Create vlan Assign layout
				LinearLayout vlanAssign_layout = new LinearLayout(this);
				vlanAssign_layout.setOrientation(LinearLayout.HORIZONTAL);
				vlanAssign_layout.setFocusableInTouchMode(true);
				
				vlanAssignCheckBox = new CheckBox(this);
				int number = i + 1;
				vlanAssignCheckBox.setText(Integer.toString(number) + ".) Vlan # Assign: " );
				
				//Assigning the ID to the box and to the arrayList			
				int vlanAssignIDCheckBox = i + vlanAssignCheckBoxCounter;
				vlanAssignCheckBox.setId(vlanAssignIDCheckBox);
				vlanAssignCheckList.add(vlanAssignIDCheckBox);
				
				// Defining the LinearLayout layout parameters to fill the parent.
		        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
		            LinearLayout.LayoutParams.WRAP_CONTENT,
		            LinearLayout.LayoutParams.WRAP_CONTENT);
		        vlanAssignCheckBox.setLayoutParams(llp);
		        vlanCheckBoxList.add(vlanAssignCheckBox);
				vlanAssign_layout.addView(vlanAssignCheckBox);
				
				vlanAssignTextBox = new EditText(this);
				vlanAssignTextBox.setMaxLines(1);
				vlanAssignTextBox.setFocusable(true);
				vlanAssignTextBox.setFocusableInTouchMode(true);
				vlanAssignTextBox.setInputType(InputType.TYPE_CLASS_NUMBER);
				vlanAssignTextBox.setMinimumWidth(125);
				
				//Adding restrictions to the character
				int maxLength = 15;
				InputFilter[] fArray = new InputFilter[1];
				fArray[0] = new InputFilter.LengthFilter(maxLength);
				vlanAssignTextBox.setFilters(fArray);
				
				//Assigning the ID to the box and to the arrayList			
				int vlanAssignIDTextBox = i + vlanAssignTextBoxCounter;
				vlanAssignTextBox.setId(vlanAssignIDTextBox);
				vlanAssignTextList.add(vlanAssignIDTextBox);
				vlanTextBoxList.add(vlanAssignTextBox);
				vlanAssign_layout.addView(vlanAssignTextBox);
				
				//Create vlan Desc layout
				LinearLayout vlanDesc_layout = new LinearLayout(this);
				vlanDesc_layout.setOrientation(LinearLayout.HORIZONTAL);
				vlanDesc_layout.setFocusableInTouchMode(true);
				
				vlanDescCheckBox = new CheckBox(this);
				vlanDescCheckBox.setText(Integer.toString(number) + ".) Vlan # Desc:   " );
				
				//Assigning the ID to the box and to the arrayList
				int vlanDescIDCheckBox = i + vlanDescCheckBoxCounter;
				vlanDescCheckBox.setId(vlanDescIDCheckBox);
				vlanDescCheckList.add(vlanDescIDCheckBox);
				
				// Defining the LinearLayout layout parameters to fill the parent.
		        LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(
		            LinearLayout.LayoutParams.WRAP_CONTENT,
		            LinearLayout.LayoutParams.WRAP_CONTENT);
		        vlanDescCheckBox.setLayoutParams(llp2);
		        vlanCheckBoxList.add(vlanDescCheckBox);
		        vlanDesc_layout.addView(vlanDescCheckBox);
				
				vlanDescTextBox = new EditText(this);
				vlanDescTextBox.setFocusable(true);
				vlanDescTextBox.setFocusableInTouchMode(true);
				vlanDescTextBox.setMaxLines(1);
				vlanDescTextBox.setMinimumWidth(125);
				vlanDescTextBox.setInputType(InputType.TYPE_CLASS_TEXT);
				
				//Adding restrictions to the character
				int maxLength2 = 15;
				InputFilter[] fArray2 = new InputFilter[1];
				fArray2[0] = new InputFilter.LengthFilter(maxLength2);
				vlanDescTextBox.setFilters(fArray2);
				
				//Assigning the ID to the box and to the arrayList
				int vlanDescIDTextBox = i + vlanDescTextBoxCounter;
				vlanDescTextBox.setId(vlanDescIDTextBox);
				vlanDescTextList.add(vlanDescIDTextBox);
				vlanTextBoxList.add(vlanDescTextBox);
				vlanDesc_layout.addView(vlanDescTextBox);
				
				mLinearLayout.addView(vlanAssign_layout);
				mLinearLayout.addView(vlanDesc_layout);
			}
		}
		
		LinearLayout buttonLinearLayout = new LinearLayout(this);
		buttonLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonLinearLayout.setWeightSum(4f);
		LinearLayout.LayoutParams btnLinearLayoutParams = new LinearLayout.LayoutParams(
		            LinearLayout.LayoutParams.WRAP_CONTENT,
		            LinearLayout.LayoutParams.WRAP_CONTENT);
		btnLinearLayoutParams.weight = 1f;
		
		//clear all button
		clearAllButton = new Button(this);
		clearAllButton.setText("Clear All");
		clearAllButton.setLayoutParams(btnLinearLayoutParams);
		clearAllButton.setId(1);
		clearAllButton.setOnClickListener(PortVlanButtonClickListener);
		buttonLinearLayout.addView(clearAllButton);
		
		//clear selected button
		clearSelectedButton = new Button(this);
		clearSelectedButton.setText("Clear");
		clearSelectedButton.setLayoutParams(btnLinearLayoutParams);
		clearSelectedButton.setId(2);
		clearSelectedButton.setOnClickListener(PortVlanButtonClickListener);
		buttonLinearLayout.addView(clearSelectedButton);

		//Back button
		backButton = new Button(this);
		backButton.setText("Back");
		backButton.setLayoutParams(btnLinearLayoutParams);
		backButton.setId(3);
		backButton.setOnClickListener(PortVlanButtonClickListener);
		buttonLinearLayout.addView(backButton);
		
		//Next button
		nextButton = new Button(this);
		nextButton.setText("Next");
		nextButton.setLayoutParams(btnLinearLayoutParams);
		nextButton.setId(4);
		nextButton.setOnClickListener(PortVlanButtonClickListener);
		buttonLinearLayout.addView(nextButton);
		
		mLinearLayout.addView(buttonLinearLayout);
	}
	
	private OnClickListener PortVlanButtonClickListener = new OnClickListener()
	{
		@Override
		public void onClick(final View v)
		{
			switch(v.getId())
			{
				case 1:
					clearAllVlanInfo();
					break;
				case 2:
					clearSelectedVlanInfo();
					break;
				case 3:
					changeActivityButton(1);
					break;
				case 4:
					changeActivityButton(2);
					break;
			}
		}
	};
	
	//number of vlans = 5
	//number of boxes = 10
	//vlan nums (by index): 0, 2, 4, 6, 8
	public void getAllVlansAssign()
	{
		int totalNum = (int)vlanTextBoxList.size()/(int)2;
		for(int i = 0; i < vlanTextBoxList.size(); i+=2)
		{
			String tempString = vlanTextBoxList.get(i).getText().toString();
			
			if(!tempString.equals(""))
			{
				intList.add(i);
				intIndexList.add(Integer.parseInt(tempString));
			}
		}
		if(totalNum != intList.size())
		{
			for(int j = 0; j < vlanTextBoxList.size(); j+=2)
			{
				dupIndex.add(j);
			}
			displayErrors();
		}
	}
	
	public void sortArraysFirst()
	{
		int[] input = getArrayListInt(1);
		int[] inputValue = getArrayListInt(2);
		int temp = 0;
		int tempValue = 0;
		for(int i = 0; i < input.length; i++)
		{
			for(int j = 0; j < input.length-1; j++)
			{
				if(input[j+1] < input[j])
				{
					temp = input[j+1];
					tempValue = inputValue[j+1];
					input[j+1] = input[j];
					inputValue[j+1] = inputValue[j];
					input[j] = temp;
					inputValue[j] = tempValue;
				}
			}
		}
	}
	
	public boolean findAllDuplicates()
	{
		for (int i=0; i < intList.size(); i++)
		{
			boolean duplicateExist = false;
			for (int j=0; j < intList.size(); j++)
			{
				if (i != j && intIndexList.get(i)==intIndexList.get(j))
				{
					if(dupIndex.size() == 0)
					{
						dupIndex.add(intList.get(i));
						dupIndex.add(intList.get(j));
						dupIndexValue.add(intIndexList.get(i));
						dupIndexValue.add(intIndexList.get(j));
					}
					else if(dupIndex.size() >= 1)
					{
						int check4Index = checkForIndex(intList.get(i), intList.get(j));
						int firstIndexValue = intIndexList.get(i);
						int secondIndexValue = intIndexList.get(j);
						if(check4Index == -1)
						{
							//already exist in the array
						}
						else if(check4Index == -2)
						{
							//adding both to the array
							dupIndexValue.add(firstIndexValue); //or dupIndexValue.add(firstIndexValue);
							dupIndexValue.add(secondIndexValue); //or dupIndexValue.add(secondIndexValue);
						}
						else if(check4Index == i)
						{
							//adding the first index in the array
							dupIndexValue.add(firstIndexValue); //or dupIndexValue.add(firstIndexValue);
						}
						else if(check4Index == j)
						{
							//adding the second index in the array
							dupIndexValue.add(secondIndexValue); //or dupIndexValue.add(secondIndexValue);
						}
						else if(check4Index == 0)
						{
							// didn't work for any reason
						}
					}
					else
					{
						//duplicate value
					}
					duplicateExist=true;
				}
			}
			if (duplicateExist == false)
			{
				//unique value
			}
		}
		
		if(dupIndex.size() == 0)
		{
			return true;
		}
		return false;
	}
	
	public int[] getArrayListInt(int option)
	{
		int[] tempArray2 = new int[dupIndex.size()];
		if(option == 1)
		{
			int[] tempArray = new int[dupIndex.size()];
			for(int i = 0; i < dupIndex.size(); i++)
			{
				tempArray[i] = dupIndex.get(i);
			}
			return tempArray;
		}
		else if(option == 2)
		{
			int[] tempArray = new int[dupIndexValue.size()];
			for(int k = 0; k < dupIndexValue.size(); k++)
			{
				tempArray[k] = dupIndexValue.get(k);
			}
			return tempArray;
		}
		else
		{
			return tempArray2;
		}
	}
	
	public int checkForIndex(int firstIndex, int secondIndex)
	{
		boolean firstIndexFound = false;
		boolean secondIndexFound = false;
		for(int i = 0; i < dupIndex.size(); i++)
		{
			if(dupIndex.get(i) == firstIndex || dupIndex.get(i) == secondIndex)
			{
				if(dupIndex.get(i) == firstIndex)
				{
					firstIndexFound = true;
				}
				if(dupIndex.get(i) == secondIndex)
				{
					secondIndexFound = true;
				}
			}
		}
		
		if(firstIndexFound == true && secondIndexFound == true)
		{
			return -1;
		}
		else if(firstIndexFound == false && secondIndexFound == true)
		{
			dupIndex.add(firstIndex);
			return firstIndex;
		}
		else if(firstIndexFound == true && secondIndexFound == false)
		{
			dupIndex.add(secondIndex);
			return secondIndex;
		}
		else if(firstIndexFound == false && secondIndexFound == false)
		{
			dupIndex.add(firstIndex);
			dupIndex.add(secondIndex);
			return -2;
		}
		return 0;
	}
	
	
	public void displayErrors()
	{
		int ecolor = Color.RED;
		String eString = "# must not be a duplicate";
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(eString);
		ssbuilder.setSpan(fgcspan, 0, eString.length(), 0);
		
		for(int i = 0; i < intList.size(); i++)
		{
			vlanTextBoxList.get(intList.get(i)).setError(null);
		}
		
		//0, 2, 4, 6, 8 in a 5 vlan port assign
		for(int i = 0; i < dupIndex.size(); i++)
		{
			vlanTextBoxList.get(dupIndex.get(i)).setError(ssbuilder);	
		}
		
		//Clear All in indexDuplicate K and J
		intList.clear();
		intIndexList.clear();
		dupIndex.clear();
		dupIndexValue.clear();
	}
	
	public void clearAllVlanInfo()
	{
		//vlanCheckBoxList
		//vlanTextBoxList
		
		for(int i=0; i < vlanTextBoxList.size(); i++)
		{
			vlanTextBoxList.get(i).setText("");
			vlanCheckBoxList.get(i).setChecked(false);
			vlanTextBoxList.get(i).setError(null);
		}		
	}
	
	public void clearSelectedVlanInfo()
	{
		for(int k = 0; k < vlanCheckBoxList.size(); k++)
		{
			boolean isCheckBoxChecked = vlanCheckBoxList.get(k).isChecked();
			if(isCheckBoxChecked == true)
			{
				vlanTextBoxList.get(k).setText("");
				vlanCheckBoxList.get(k).setChecked(false);
				vlanTextBoxList.get(k).setError(null);
			}
		}		
	}
	
	public void changeActivityButton(int option)
	{
		switch(option)
		{
			case 1:
				gotoBackActivity();
				break;
			case 2:
				gotoNextActivity();
				break;
		}
	}
	
	public void gotoBackActivity()
	{
		promptNextLayout(3);
	}
	
	public void gotoNextActivity()
	{
		clearAllErrors();
		//assign vlans
		disableAllButtons();
		getAllVlansAssign();
		boolean isValidated = findAllDuplicates();
		boolean isDescValidated = false;
		boolean alreadyPrompted = false;
		if(isValidated == false)
		{
			validateVlanDesc();
			isDescValidated = findDuplicateInStrings();
			if(isDescValidated == false)
			{
				displayErrors();
				displayVlanDescErrors();
			}
		}
		else
		{
			validateVlanDesc();
			isDescValidated = findDuplicateInStrings();
			if(isDescValidated == false)
			{
				displayVlanDescErrors();
			}
			else
			{
				if(isValidated == true && isDescValidated == true)
				{
					alreadyPrompted = true;
					clearAllErrors();
					promptNextLayout(1);
				}
			}
		}
		if(isValidated == true && isDescValidated == true && alreadyPrompted == false)
		{
			clearAllErrors();
			promptNextLayout(1);
		}
		enableAllButtons();
	}
	
	public void promptNextLayout(int option)
	{
		final Context context = this;
		switch(option)
		{
			case 1:
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
						gotoActivity(1);
					}
				})
				.setNegativeButton("No",new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,int id)
					{
						dialog.cancel();
					}
				});
				
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				
				// show it
				alertDialog.show();
				break;
			case 2:
				AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);
				
				// set title
				alertDialogBuilder2.setTitle("Back to Previous Page");
				// set dialog message
				alertDialogBuilder2
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
				AlertDialog alertDialog2 = alertDialogBuilder2.create();
				
				// show it
				alertDialog2.show();
				break;
			case 3:
				AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(context);
				 
				// set title
				alertDialogBuilder3.setTitle("ERROR");
				// set dialog message
				alertDialogBuilder3
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
				AlertDialog alertDialog4 = alertDialogBuilder3.create();
				
				// show it
				alertDialog4.show();
				break;
		}
	}
	
	public void gotoActivity(int option)
	{
		ArrayList<String> vlanDescArrayList = new ArrayList<String>();
		ArrayList<String> vlanAssignArrayList = new ArrayList<String>();
		
		for(int i = 0; i < vlanTextBoxList.size(); i++)
		{
			if(i % 2 == 0)
			{
				vlanAssignArrayList.add(vlanTextBoxList.get(i).getText().toString());
			}
			else
			{
				vlanDescArrayList.add(vlanTextBoxList.get(i).getText().toString());
			}
		}
		
		for(int printList = 0; printList < vlanAssignArrayList.size(); printList++)
		{
			Toast toasty = Toast.makeText(this, 
					"vlanAssign: " + vlanAssignArrayList.get(printList) +
					"\nvlanDesc: " + vlanDescArrayList.get(printList), Toast.LENGTH_LONG);
			toasty.show();
		}
		
		String[] vlanAssignArray = new String[vlanAssignArrayList.size()];
		String[] vlanDescArray = new String[vlanDescArrayList.size()];
		vlanAssignArray = vlanAssignArrayList.toArray(vlanAssignArray);
		vlanDescArray = vlanDescArrayList.toArray(vlanDescArray);
		
		//Next Activity
		if(option == 1)
		{
			Intent intentNextActivity = new Intent(this, ConfigAllVlans.class);
			intentNextActivity.putExtra("key", true);
			intentNextActivity.putExtra("firstIntentInfo", firstIntentArray);
			intentNextActivity.putExtra("secondTransferKey", true);
			intentNextActivity.putExtra("portNumInt", portNum);
			intentNextActivity.putExtra("vlanNumInt", vlanNum);
			intentNextActivity.putExtra("thirdTransferKey", true);
			intentNextActivity.putExtra("vlanAssignArray", vlanAssignArray);
			intentNextActivity.putExtra("vlanDescArray", vlanDescArray);
			this.startActivity(intentNextActivity);
		}
		//Back Activity with Save
		else if(option == 2)
		{
			//Intent intentNextActivity = new Intent(this, vlanConfigSetup.class);
			Intent intentNextActivity = new Intent(this, SwitchConfig.class);
			intentNextActivity.putExtra("key", true);
			intentNextActivity.putExtra("firstIntentInfo", firstIntentArray);
			intentNextActivity.putExtra("secondTransferKey", true);
			intentNextActivity.putExtra("portNumInt", portNum);
			intentNextActivity.putExtra("vlanNumInt", vlanNum);
			intentNextActivity.putExtra("thirdTransferKey", true);
			intentNextActivity.putExtra("vlanAssignArray", vlanAssignArray);
			intentNextActivity.putExtra("vlanDescArray", vlanDescArray);
			this.startActivity(intentNextActivity);			
		}
		//Back Activity without Save
		else if(option == 3)
		{
			//Intent intentNextActivity = new Intent(this, vlanConfigSetup.class);
			Intent intentNextActivity = new Intent(this, SwitchConfig.class);
			intentNextActivity.putExtra("key", true);
			intentNextActivity.putExtra("firstIntentInfo", firstIntentArray);
			intentNextActivity.putExtra("secondTransferKey", true);
			intentNextActivity.putExtra("portNumInt", portNum);
			intentNextActivity.putExtra("vlanNumInt", vlanNum);
			intentNextActivity.putExtra("thirdTransferKey", false);
			this.startActivity(intentNextActivity);
		}
	}
	
	public void clearAllErrors()
	{
		for(int i = 0; i < vlanTextBoxList.size(); i++)
		{
			vlanTextBoxList.get(i).setError(null);
		}
	}
	
	public void disableAllButtons()
	{
		clearAllButton.setEnabled(false);
		clearSelectedButton.setEnabled(false);
		backButton.setEnabled(false);
		nextButton.setEnabled(false);
	}
	
	public void enableAllButtons()
	{
		clearAllButton.setEnabled(true);
		clearSelectedButton.setEnabled(true);
		backButton.setEnabled(true);
		nextButton.setEnabled(true);
	}
	
	public boolean findDuplicateInStrings()
	{
		for(int i = 0; i < stringList.size(); i++)
		{
			boolean duplicateExist = false;
			for(int j = 0; j < stringList.size(); j++)
			{
				String firstIndexString = stringList.get(i).toLowerCase(Locale.US);
				String secondIndexString = stringList.get(j).toLowerCase(Locale.US);
				if(i != j && firstIndexString.equals(secondIndexString))
				{
					if(stringDupIndex.size() == 0)
					{
						stringDupIndex.add(stringListIndex.get(i));
						stringDupIndex.add(stringListIndex.get(j));
						stringDupIndexValue.add(firstIndexString);
						stringDupIndexValue.add(secondIndexString);
					}
					else if(stringDupIndex.size() >= 1)
					{
						int check4IndexString = checkForIndexString(stringListIndex.get(i),stringListIndex.get(j));
						String firstIndexValue = stringList.get(i);
						String secondIndexValue = stringList.get(j);
						if(check4IndexString == -1)
						{
							//Both already exist in the array
						}
						else if(check4IndexString == -2)
						{
							//Added both first and second index value in array
							stringDupIndexValue.add(firstIndexValue);
							stringDupIndexValue.add(secondIndexValue);
						}
						else if(check4IndexString == i)
						{
							//Added firstIndexValue to array
							stringDupIndexValue.add(firstIndexValue);
						}
						else if(check4IndexString == j)
						{
							//Added secondIndexValue to array
							stringDupIndexValue.add(secondIndexValue);
						}
						else if(check4IndexString == 0)
						{
							//didn't work for no reason
						}
					}
					else
					{
						//duplicate value of these numbers
					}
					duplicateExist=true;
				}
			}
			if (duplicateExist == false)
			{
				//Unique values
			}
		}
		
		if(stringDupIndex.size() == 0)
		{
			return true;
		}
		return false;
	}
	
	public int checkForIndexString(int firstIndex, int secondIndex)
	{
		boolean firstIndexFound = false;
		boolean secondIndexFound = false;
		for(int i = 0; i < stringDupIndex.size(); i++)
		{
			if(stringDupIndex.get(i) == firstIndex || stringDupIndex.get(i) == secondIndex)
			{
				if(stringDupIndex.get(i) == firstIndex)
				{
					firstIndexFound = true;
				}
				if(stringDupIndex.get(i) == secondIndex)
				{
					secondIndexFound = true;
				}
			}
		}
		
		if(firstIndexFound == true && secondIndexFound == true)
		{
			return -1;
		}
		else if(firstIndexFound == false && secondIndexFound == true)
		{
			stringDupIndex.add(firstIndex);
			return firstIndex;
		}
		else if(firstIndexFound == true && secondIndexFound == false)
		{
			stringDupIndex.add(secondIndex);
			return secondIndex;
		}
		else if(firstIndexFound == false && secondIndexFound == false)
		{
			stringDupIndex.add(firstIndex);
			stringDupIndex.add(secondIndex);
			return -2;
		}
		return 0;
	}
	
	public void displayVlanDescErrors()
	{
		int ecolor = Color.RED;
		String eString = "Description must not be a duplicate";
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(eString);
		ssbuilder.setSpan(fgcspan, 0, eString.length(), 0);
		
		for(int i = 0; i < stringListIndex.size(); i++)
		{
			vlanTextBoxList.get(stringListIndex.get(i)).setError(null);
		}
		
		//1, 3, 5, 7, 9 in a 5 vlan port assign
		for(int i = 0; i < stringDupIndex.size(); i++)
		{
			vlanTextBoxList.get(stringDupIndex.get(i)).setError(ssbuilder);	
		}
		
		//Clear all
		stringList.clear();
		stringListIndex.clear();
		stringDupIndex.clear();
		stringDupIndexValue.clear();
	}
	
	public void validateVlanDesc()
	{
		for(int i = 1; i < vlanTextBoxList.size(); i+=2)
		{
			stringList.add(vlanTextBoxList.get(i).getText().toString());
			stringListIndex.add(i);
		}
	}
	
	public void setVlanPortNums(int _vlan, int _port, boolean _settings)
	{
		portNum = _port;
		vlanNum = _vlan;
		vlanNumListCounter = vlanNum;
		defaultSettings = _settings;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
