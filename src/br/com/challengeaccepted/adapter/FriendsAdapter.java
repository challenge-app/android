package br.com.challengeaccepted.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.com.challengeaccepted.LoginActivity;
import br.com.challengeaccepted.R;
import br.com.challengeaccepted.bean.User;
import br.com.challengeaccepted.fragments.ChallengeFriendsFragment;

@SuppressLint("NewApi")
public class FriendsAdapter extends BaseAdapter {
	
	private ArrayList<User> friends;
	private Context context;
	private boolean isSearchResult;
	private ChallengeFriendsFragment challengeFriendsFragment;
	
	private class ViewHolder {
		TextView nameTextView;
		Button btnAddFriend;
	}
	
	public FriendsAdapter(ArrayList<User> friends, Context context, boolean isSearchResult,
			ChallengeFriendsFragment challengeFriendsFragment) {
		this.friends = friends;
		this.context = context;
		this.isSearchResult = isSearchResult;
		this.challengeFriendsFragment = challengeFriendsFragment;
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
			holder.btnAddFriend = (Button) convertView.findViewById(R.id.btnAddFriend);
			
			convertView.setTag(holder);
			
		} else {  
		      holder = (ViewHolder)convertView.getTag();  
		}  
		
		holder.nameTextView.setText(friend.getEmail());
		
		if(isSearchResult){
			holder.btnAddFriend.setVisibility(View.VISIBLE);
			
			holder.btnAddFriend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ChallengeFriendsFragment.addFriend(challengeFriendsFragment, ((User) getItem(position)).getId());
				}
			});
		} else {
			holder.btnAddFriend.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	
}
