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

import android.view.Gravity;
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

public class ConfigAllVlans extends Activity
{
	int portNum = 0, vlanNum = 0;
	
	String[] vlanAssignArray, vlanDescArray, firstIntentArray;
	
	public LinearLayout mainLayout;
	
	Button clearAllButton, clearSelectedButton, backButton, nextButton;
	
	ArrayList<RadioGroup> radioGroupArrayList = new ArrayList<RadioGroup>();
	ArrayList<CheckBox> checkBoxArrayList = new ArrayList<CheckBox>();
		
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_all_vlans_layout);
		
		Intent intent = getIntent();
		Bundle extras = getIntent().getExtras();
		
		if(extras != null)
		{
			boolean keyValue = intent.getBooleanExtra("key", true);
			if(keyValue == true && extras != null)
			{
				firstIntentArray = intent.getStringArrayExtra("firstIntentInfo");
				boolean secondTKey = intent.getBooleanExtra("secondTransferKey", true);
				if(secondTKey == true)
				{
					int portNumInt = intent.getIntExtra("portNumInt", portNum);
					int vlanNumInt = intent.getIntExtra("vlanNumInt", vlanNum);
					setVlanPortNums(vlanNumInt, portNumInt);
					
					boolean thirdTKey = intent.getBooleanExtra("thirdTransferKey", true);
					if(thirdTKey == true)
					{
						vlanAssignArray = intent.getStringArrayExtra("vlanAssignArray");
						vlanDescArray = intent.getStringArrayExtra("vlanDescArray");
					}
				}
			}
		}
		establishMainLayout();
		
		/*  Description: 
		 * 		for the number of VLANS there are (i.e 5) and the number of ports (i.e 24)
		 * 		there will be 120 layouts (dynamically generated).  In each of the 120 layouts,
		 * 		there will be 4 radio buttons for un-tagged, tagged, forbidden, and none.
		 * 		Also, each of these layouts (120 of them) will have a checkbox to
		 *		clear what was selected (similar on the previous activity).
		 *		
		 * 		The 4 buttons and their purpose consist of the following:
		 * 			1.	Clear All - clears everything that is checked as well as the radio button selected
		 * 			2.  Clear Selected - - clears only the checkbox that is checked and whatever content is in that row/layout
		 * 			3.  Back - goes back to the previous activity with the option to save or not save your data
		 * 			4.  Next - goes to the next activity with saving your data
		 */
		
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
		titleParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
		
		LinearLayout titleLayout = new LinearLayout(this);
		titleLayout.setLayoutParams(titleParams);
		titleLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		
		TextView title = new TextView(this);
		title.setText("Configuring " + Integer.toString(vlanNum) + " Vlans");
		title.setTextColor(Color.BLUE);
		title.setTextSize(20);
		title.setBackgroundColor(Color.LTGRAY);
		title.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		title.setLayoutParams(titleParams);
		mainLayout.addView(titleLayout);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		
		for(int vlanNumCounter = 1; vlanNumCounter <= vlanNum; vlanNumCounter++)
		{
			LinearLayout vlanLayoutList = new LinearLayout(this);
			vlanLayoutList.setOrientation(LinearLayout.VERTICAL);
			
			LinearLayout vlanTitleBarLayout = new LinearLayout(this);
			vlanTitleBarLayout.setOrientation(LinearLayout.HORIZONTAL);
			vlanTitleBarLayout.setLayoutParams(titleParams);
			vlanTitleBarLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER);
			
			TextView vlanTitleBar = new TextView(this);
			vlanTitleBar.setText("Vlan # " + Integer.toString(vlanNumCounter));
			vlanTitleBar.setTextColor(Color.BLACK);
			vlanTitleBar.setBackgroundColor(Color.WHITE);
			vlanTitleBar.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER);
			vlanTitleBar.setLayoutParams(titleParams);
			vlanTitleBarLayout.addView(vlanTitleBar);
			vlanLayoutList.addView(vlanTitleBarLayout);
			
			for(int portNumCounter = 1; portNumCounter <= portNum; portNumCounter++)
			{
				LinearLayout radioCheckLayout = new LinearLayout(this);
				radioCheckLayout.setOrientation(LinearLayout.HORIZONTAL);
				radioCheckLayout.setLayoutParams(params);
				radioCheckLayout.setGravity(Gravity.CENTER_HORIZONTAL);
				
				RadioGroup radioGroupList = new RadioGroup(this);
				radioGroupList.setOrientation(RadioGroup.HORIZONTAL);
				
				boolean radioGroupBool = false;
				
				for(int itemCounter = 1; itemCounter <= 5; itemCounter++)
				{
					int id_num = ((vlanNumCounter-1) * portNum * 5) + ((portNumCounter-1) * 5) + itemCounter;
					if(radioGroupBool == false)
					{
						radioGroupList.setId(id_num);
						radioGroupBool = true;
					}
					switch(itemCounter)
					{
						case 1:
							//checkbox 1
							String vlan_num_string = "Port " + Integer.toString(portNumCounter) + ": ";
							CheckBox checkBox = new CheckBox(this);
							checkBox.setText(vlan_num_string);
							checkBox.setId(id_num);
							checkBoxArrayList.add(checkBox);
							radioCheckLayout.addView(checkBox);
							break;
						
						case 2:
							//radio 1
							RadioButton untaggedRadioBtn = new RadioButton(this);
							untaggedRadioBtn.setText("U");
							untaggedRadioBtn.setTag("U");
							untaggedRadioBtn.setId(id_num);
							radioGroupList.addView(untaggedRadioBtn);
							break;
						
						case 3:
							//radio 2
							RadioButton taggedRadioBtn = new RadioButton(this);
							taggedRadioBtn.setText("T");
							taggedRadioBtn.setTag("T");
							taggedRadioBtn.setId(id_num);
							radioGroupList.addView(taggedRadioBtn);
							break;
						case 4:
							//radio 3
							RadioButton forbiddenRadioBtn = new RadioButton(this);
							forbiddenRadioBtn.setText("F");
							forbiddenRadioBtn.setTag("F");
							forbiddenRadioBtn.setId(id_num);
							radioGroupList.addView(forbiddenRadioBtn);
							break;
						case 5:
							//radio 4
							RadioButton noneRadioBtn = new RadioButton(this);
							noneRadioBtn.setText("N");
							noneRadioBtn.setTag("N");
							noneRadioBtn.setId(id_num);
							radioGroupList.addView(noneRadioBtn);
							break;
					}
				}
				radioCheckLayout.addView(radioGroupList);
				vlanLayoutList.addView(radioCheckLayout);
				radioGroupArrayList.add(radioGroupList);
			}
			
			//adding vlanLayoutList to mainLayout
			mainLayout.addView(vlanLayoutList);
			
			/*
			 use this for horizontal divider
			 
			 <View
			 	android:layout_width="1dp"
			 	android:layout_height="match_parent" />
			 	
			 and this for vertical divider
			 
			 <View
			 	android:layout_width="match_parent"
			 	android:layout_height="1dp"
			 	android:background="@color/honeycombish_blue" />
			 */

			LinearLayout.LayoutParams horizontalLayoutParams = new LinearLayout.LayoutParams(
					//LayoutParams.WRAP_CONTENT,
					LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT
					);
			horizontalLayoutParams.height = (int) 2.0f;
			horizontalLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
			
			LinearLayout.LayoutParams verticalLayoutParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT
					);
			verticalLayoutParams.width = (int) 2.0f;
			verticalLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
			
			LinearLayout vlanDividerBar = new LinearLayout(this);
			vlanDividerBar.setOrientation(LinearLayout.HORIZONTAL);
			vlanDividerBar.setLayoutParams(horizontalLayoutParams);
			vlanDividerBar.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER);
			
			//sets up a TextView divider for every vlan (horizontal)
			TextView anyDivider = new TextView(this);
			anyDivider.setBackgroundColor(Color.BLUE);
			anyDivider.setLayoutParams(horizontalLayoutParams);
			//sets up a TextView divider for every vlan (vertical)
			//anyDivider.setLayoutParams(verticalLayoutParams);
			vlanDividerBar.addView(anyDivider);
			
			//add everything
			mainLayout.addView(vlanDividerBar);
		}
		
		//Add buttons
		LinearLayout buttonLinearLayout = new LinearLayout(this);
		buttonLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonLinearLayout.setWeightSum(4f);
		buttonLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER);
		LinearLayout.LayoutParams btnLinearLayoutParams = new LinearLayout.LayoutParams(
		            LinearLayout.LayoutParams.WRAP_CONTENT,
		            LinearLayout.LayoutParams.WRAP_CONTENT);
		btnLinearLayoutParams.weight = 1f;
		btnLinearLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
		
		//clear all button
		clearAllButton = new Button(this);
		clearAllButton.setText("Clear All");
		clearAllButton.setLayoutParams(btnLinearLayoutParams);
		clearAllButton.setId(1);
		clearAllButton.setTag("Clear All Method");
		clearAllButton.setOnClickListener(PortVlanButtonClickListener);
		buttonLinearLayout.addView(clearAllButton);
		
		//clear selected button
		clearSelectedButton = new Button(this);
		clearSelectedButton.setText("Clear");
		clearSelectedButton.setLayoutParams(btnLinearLayoutParams);
		clearSelectedButton.setId(2);
		clearSelectedButton.setTag("Clear Method");
		clearSelectedButton.setOnClickListener(PortVlanButtonClickListener);
		buttonLinearLayout.addView(clearSelectedButton);

		//Back button
		backButton = new Button(this);
		backButton.setText("Back");
		backButton.setLayoutParams(btnLinearLayoutParams);
		backButton.setId(3);
		backButton.setTag("Back Activity");
		backButton.setOnClickListener(PortVlanButtonClickListener);
		buttonLinearLayout.addView(backButton);
		
		//Next button
		nextButton = new Button(this);
		nextButton.setText("Next");
		nextButton.setLayoutParams(btnLinearLayoutParams);
		nextButton.setId(4);
		nextButton.setTag("Next Activity");
		nextButton.setOnClickListener(PortVlanButtonClickListener);
		buttonLinearLayout.addView(nextButton);
		
		mainLayout.addView(buttonLinearLayout);
	}
	
	public void onRadioButtonClick(View v)
	{
		RadioButton tempButton = (RadioButton) v;
		Toast buttonToast = Toast.makeText(this, tempButton.getText() + " was chosen",
				Toast.LENGTH_SHORT);
		buttonToast.show();
	}
	
	private OnClickListener PortVlanButtonClickListener = new OnClickListener()
	{
		@Override
		public void onClick(final View v)
		{
			printIdClicked(v.getId(), v.getTag());
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
	
	public void printIdClicked(int id_num, Object tag_var)
	{
		String tag_str = tag_var.toString();
		Toast toasty = Toast.makeText(this, 
				"ID called: " + id_num +
				"\nTag: " + tag_str, Toast.LENGTH_LONG);
		toasty.show();
	}
	
	public void clearAllVlanInfo()
	{
		for(int i = 0; i < checkBoxArrayList.size(); i++)
		{
			CheckBox tempCheckBox = checkBoxArrayList.get(i);
			boolean isChecked = tempCheckBox.isChecked();
			if(isChecked == true)
			{
				RadioGroup dynamicRGroup = radioGroupArrayList.get(i);
				int firstDynamicRGroupID = dynamicRGroup.getCheckedRadioButtonId();
				dynamicRGroup.clearCheck();
				int secondDynamicRGroupID = dynamicRGroup.getCheckedRadioButtonId();
			}
		}
	}
	
	public void clearSelectedVlanInfo()
	{
		Toast toasty = Toast.makeText(this, "Clear checked Toast", Toast.LENGTH_LONG);
		toasty.show();
	}
	
	public void changeActivityButton(int option)
	{
		if(option == 1)
		{
			
		}
		else if(option == 2)
		{
			checkEachRadioGroup();
			checkEachCheckBox();
		}
	}
	
	public void checkEachCheckBox()
	{
		for(int i = 0; i < checkBoxArrayList.size(); i++)
		{
			CheckBox dynamicCheckBox = checkBoxArrayList.get(i);
			boolean isChecked = dynamicCheckBox.isChecked();
			
			Toast toasty = Toast.makeText(this,
					"i: " + i + 
					"\nchecked: " + isChecked, Toast.LENGTH_LONG);
			toasty.show();
		}
	}
	
	public void checkEachRadioGroup()
	{
		for(int i = 0; i < radioGroupArrayList.size(); i++)
		{
			RadioGroup dynamicRGroup = radioGroupArrayList.get(i);
			int dynamicRGroupID = dynamicRGroup.getCheckedRadioButtonId();
			
			View radioButton = dynamicRGroup.findViewById(dynamicRGroupID);
		    int radioId = dynamicRGroup.indexOfChild(radioButton);
		    RadioButton btn = (RadioButton) dynamicRGroup.getChildAt(radioId);
		    String selectedText = (String) btn.getText();
			
			Toast toasty = Toast.makeText(this, 
					"i: " + i + 
					"\nradioID: " + dynamicRGroupID +
					"\ntext: " + selectedText, Toast.LENGTH_LONG);
			toasty.show();
		}
	}
		
	public void setVlanPortNums(int _vlanNum, int _portNum)
	{
		portNum = _portNum;
		vlanNum = _vlanNum;
	}
	
	public void establishMainLayout()
	{
		mainLayout = (LinearLayout)findViewById(R.id.config_all_linear);
		mainLayout.setOrientation(LinearLayout.VERTICAL);  
		mainLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}