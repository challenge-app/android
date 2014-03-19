package br.com.challengeaccepted.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.challengeaccepted.R;
import br.com.challengeaccepted.bean.Challenge;

public class ReceivedChallengesAdapter extends BaseAdapter {
	
	private ArrayList<Challenge> challenges;
	private Context context;
	
	private class ViewHolder {
		TextView senderTextView;
		TextView challengeTextView;
	}
	
	public ReceivedChallengesAdapter(ArrayList<Challenge> challenges, Context context) {
		this.challenges = challenges;
		this.context = context;
    }

	@Override
	public int getCount() {
		return challenges.size();
	}

	@Override
	public Object getItem(int position) {
		return challenges.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public ArrayList<Challenge> getFriends() {
		return this.challenges;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final Challenge challenge = challenges.get(position); 
		
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_received_challenges, parent, false);
			holder = new ViewHolder();
			
			// Mapear componentes
			holder.senderTextView =  (TextView) convertView.findViewById(R.id.senderTextView);
			holder.challengeTextView = (TextView) convertView.findViewById(R.id.challengeTextView);
			
			convertView.setTag(holder);
			
		} else {  
		      holder = (ViewHolder)convertView.getTag();  
		}  
		
		holder.senderTextView.setText(challenge.getSender().getEmail());
		
		String description = challenge.getInfo().getDescription();
		holder.challengeTextView.setText(description);
		
		return convertView;
	}	
}
