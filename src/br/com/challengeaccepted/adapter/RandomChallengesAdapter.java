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
import br.com.challengeaccepted.bean.ChallengeInfo;

@SuppressLint("NewApi")
public class RandomChallengesAdapter extends BaseAdapter {
	
	private ArrayList<ChallengeInfo> challenges;
	private Context context;
	private int selectedIndex = -1;
	
	private class ViewHolder {
		TextView descriptionTextView;
	}
	
	public RandomChallengesAdapter(ArrayList<ChallengeInfo> challenges, Context context) {
		this.challenges = challenges;
		this.context = context;
    }
	
	public void setSelectedIndex(int index){
	    selectedIndex = index;
	}
	
	public int getSelectedIndex(){
	    return selectedIndex;
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
		final ChallengeInfo challenge = challenges.get(position); 
		
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_challenges, parent, false);
			holder = new ViewHolder();
			
			// Mapear componentes
			holder.descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionTextView);
			convertView.setTag(holder);
			
		} else {  
		      holder = (ViewHolder)convertView.getTag();  
		}  
		
		holder.descriptionTextView.setText(challenge.getDescription());
		
		if(selectedIndex == position){
		    // Muda o fundo para a cor selecionada
			View view = (View) holder.descriptionTextView.getParent();
			view.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_dark));
	    }
	    else{
	    	View view = (View) holder.descriptionTextView.getParent();
			view.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_bright));
	    }
		
		return convertView;
	}

	
}
