/*
DialerActivity.java
Copyright (C) 2010  Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package org.linphone;



import java.util.List;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCallLog;
import org.linphone.core.LinphoneCallLog.CallDirection;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryActivity extends ListActivity {
	LayoutInflater mInflater;   
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }
	
	  
	  @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		TextView lFirstLineView = (TextView) v.findViewById(R.id.history_cell_first_line);
		TextView lSecondLineView = (TextView) v.findViewById(R.id.history_cell_second_line);
		if (lSecondLineView.getVisibility() == View.GONE) {
			// no display name
			DialerActivity.getDialer().setContactAddress(lFirstLineView.getText().toString(), null);
		} else {
			DialerActivity.getDialer().setContactAddress(lSecondLineView.getText().toString()
														,lFirstLineView.getText().toString());
		}
		LinphoneActivity.instance().getTabHost().setCurrentTabByTag(LinphoneActivity.DIALER_TAB);	
	}


	@Override
	protected void onResume() {
		super.onResume();
		 setListAdapter(new CallHistoryAdapter(this));
	}


	class CallHistoryAdapter extends  BaseAdapter {
		private final Context mContext;
		final List<LinphoneCallLog> mLogs; 
		CallHistoryAdapter(Context aContext) {
			mContext = aContext;
			mLogs = LinphoneService.instance().getLinphoneCore().getCallLogs();
		}
		public int getCount() {
			return mLogs.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View lView=null;
			if (convertView !=null) {
				lView = convertView;
			} else {
				lView = mInflater.inflate(R.layout.history_cell, parent,false);
				
			}
			LinphoneCallLog lLog = mLogs.get(mLogs.size()-position-1);
			LinphoneAddress lAddress;
			TextView lFirstLineView = (TextView) lView.findViewById(R.id.history_cell_first_line);
			TextView lSecondLineView = (TextView) lView.findViewById(R.id.history_cell_second_line);
			ImageView lDirectionImageIn = (ImageView) lView.findViewById(R.id.history_cell_icon_in);
			ImageView lDirectionImageOut = (ImageView) lView.findViewById(R.id.history_cell_icon_out);
			
			if (lLog.getDirection() == CallDirection.Callincoming) {
				lAddress = lLog.getFrom();
				lDirectionImageIn.setVisibility(View.VISIBLE);
				lDirectionImageOut.setVisibility(View.GONE);
				
			} else {
				lAddress = lLog.getTo();
				lDirectionImageIn.setVisibility(View.GONE);
				lDirectionImageOut.setVisibility(View.VISIBLE);
			}

			if (lAddress.getDisplayName() == null) {
				lFirstLineView.setText(lAddress.getUserName());
				lSecondLineView.setVisibility(View.GONE);
			} else {
				lFirstLineView.setText(lAddress.getDisplayName());
				lSecondLineView.setText(lAddress.getUserName());
				lSecondLineView.setVisibility(View.VISIBLE);
			}
			
			return lView;
			
		}
		  
	  }
}