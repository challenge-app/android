package br.com.challengeaccepted.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.challengeaccepted.R;
import br.com.challengeaccepted.bean.Challenge;

@SuppressLint("NewApi")
public class ChallengesAdapter extends BaseAdapter {
	
	private ArrayList<Challenge> challenges;
	private Context context;
	
	private class ViewHolder {
		TextView descriptionTextView;
	}
	
	public ChallengesAdapter(ArrayList<Challenge> challenges, Context context) {
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final Challenge challenge = challenges.get(position); 
		
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_challenges, parent, false);
			//	convertView = LayoutInflater.from(context).inflate(R.layout.item_messages_listview, null);
			holder = new ViewHolder();
			//	convertView.setTag("cv" + position);
			
			// Mapear componentes
			holder.descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionTextView);
			convertView.setTag(holder);
			
		} else {  
		      holder = (ViewHolder)convertView.getTag();  
		}  
		
		holder.descriptionTextView.setText(challenge.getInfo().getDescription());
		
		return convertView;
	}

	
}
