package br.com.challengeaccepted.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import br.com.challengeaccepted.R;
import br.com.challengeaccepted.bean.User;
import br.com.challengeaccepted.fragments.ChallengeFriendsFragment;

@SuppressLint("NewApi")
public class UserSearchAdapter extends BaseAdapter {
	
	private ArrayList<User> friends;
	private ChallengeFriendsFragment challengeFriendsFragment;
	private Context context;
	
	private class ViewHolder {
		TextView nameTextView;
		Button btnFollow;
	}
	
	public UserSearchAdapter(ArrayList<User> friends, ChallengeFriendsFragment challengeFriendsFragment, Context context) {
		this.friends = friends;
		this.challengeFriendsFragment = challengeFriendsFragment;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_user_search, parent, false);
			holder = new ViewHolder();
			
			// Mapear componentes
			holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
			holder.btnFollow = (Button) convertView.findViewById(R.id.btnFollow);
			
			convertView.setTag(holder);
			
		} else {  
		      holder = (ViewHolder)convertView.getTag();  
		}  
		
		holder.nameTextView.setText(friend.getEmail());
		
		holder.btnFollow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ChallengeFriendsFragment.followUser(challengeFriendsFragment, ((User) getItem(position)).getId());
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
