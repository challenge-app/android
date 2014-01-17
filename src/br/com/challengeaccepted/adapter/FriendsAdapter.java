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

@SuppressLint("NewApi")
public class FriendsAdapter extends BaseAdapter {
	
	private ArrayList<User> friends;
	private Context context;
	
	private class ViewHolder {
		TextView nameTextView;
		EditText edtEmail;
		Button btnSearch;
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final User friend = friends.get(position); 
		
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_friends, parent, false);
			//	convertView = LayoutInflater.from(context).inflate(R.layout.item_messages_listview, null);
			holder = new ViewHolder();
			//	convertView.setTag("cv" + position);
			
			// Mapear componentes
			holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
			convertView.setTag(holder);

			holder.btnSearch = (Button) convertView.findViewById(R.id.btnSearch);
			holder.edtEmail = (EditText) convertView.findViewById(R.id.edtEmail);
			
			holder.btnSearch.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					String email = holder.edtEmail.getText().toString();
					
					if (holder.edtEmail.getText().length() != 0)
					{
						//TODO
					}
					else
					{
						Toast.makeText(context, "Insira um email!",
								Toast.LENGTH_SHORT).show();
					}
				}
				
			});
			
		} else {  
		      holder = (ViewHolder)convertView.getTag();  
		}  
		
		holder.nameTextView.setText(friend.getEmail());
		
		return convertView;
	}

	
}
