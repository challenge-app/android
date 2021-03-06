package br.com.challengeaccepted.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import br.com.challengeaccepted.R;
import br.com.challengeaccepted.bean.User;

@SuppressLint("NewApi")
public class FriendsAdapter extends BaseAdapter {
	
	private ArrayList<User> friends;
	private Context context;
	
	private class ViewHolder {
		TextView nameTextView;
		CheckBox checkBox;
	}
	
	public FriendsAdapter(ArrayList<User> friends, Context context) {
		this.friends = friends;
		this.context = context;
    }

	@Override
	public int getCount() {
		return friends.size();
	}

	@Override
	public Object getItem(int position) {
		return friends.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public ArrayList<User> getFriends() {
		return this.friends;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final User friend = friends.get(position); 
		
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_friends, parent, false);
			holder = new ViewHolder();
			
			// Mapear componentes
			holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
			
			convertView.setTag(holder);
			
		} else {  
		      holder = (ViewHolder)convertView.getTag();  
		}  
		
		holder.nameTextView.setText(friend.getEmail());
		
		holder.checkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cb = holder.checkBox;
				friend.setSelected(cb.isChecked());
			}
		});
		
		holder.nameTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cb = holder.checkBox;
				cb.setChecked(!cb.isChecked());
				friend.setSelected(cb.isChecked());
			}
		});
				
//		if(isSearchResult){
//			holder.btnAddFriend.setVisibility(View.VISIBLE);
//			
//			holder.btnAddFriend.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					ChallengeFriendsFragment.addFriend(challengeFriendsFragment, ((User) getItem(position)).getId());
//				}
//			});
//		} else {
//			holder.btnAddFriend.setVisibility(View.GONE);
//		}
		
		return convertView;
	}

	
}
