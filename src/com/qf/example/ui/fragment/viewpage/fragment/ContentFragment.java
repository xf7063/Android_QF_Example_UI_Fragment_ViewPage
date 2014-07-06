package com.qf.example.ui.fragment.viewpage.fragment;

import com.qf.example.ui.fragment.viewpage.activity.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContentFragment extends Fragment {
	public static final String ARG_SECTION_KEY = "section_key";
	
	private TextView txContent;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		
		Bundle args = getArguments();
		
		txContent = (TextView) view.findViewById(R.id.tx_content);
		txContent.setText(args.getString(ARG_SECTION_KEY));
		
		return view;
	}
}
