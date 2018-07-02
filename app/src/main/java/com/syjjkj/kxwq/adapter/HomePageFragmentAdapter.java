package com.syjjkj.kxwq.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class HomePageFragmentAdapter extends FragmentStatePagerAdapter {
	private ArrayList<Fragment> fragmentsList;

	public HomePageFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public HomePageFragmentAdapter(FragmentManager fm,
								   ArrayList<Fragment> fragments) {
		super(fm);
		this.fragmentsList = fragments;
	}

	@Override
	public int getCount() {
		return fragmentsList.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragmentsList.get(arg0);
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

}
