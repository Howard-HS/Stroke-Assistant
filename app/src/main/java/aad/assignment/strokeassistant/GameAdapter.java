package aad.assignment.strokeassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Hau Cherng on 6/6/2016.
 */

//this is previous apps, currently abandoned.
//can delete if you felt like
//
//
//         DITCHED
//
//
//          ok?

public class GameAdapter extends ArrayAdapter<Integer> {


    private ArrayList<Integer> intList;

    public GameAdapter(Context context,
                       ArrayList<Integer> items) {
        super(context, 0, items);
        this.intList = items;

        getFilter();
    }

    @Override
    public Integer getItem(int i) {
        return intList.get(i);
    }


    public Integer size() {
        return intList.size();
    }


    public void removeLastItem() {
        intList.remove(size() - 1);
        notifyDataSetChanged();
    }

    public void reset(ArrayList<Integer> newList) {
        intList = newList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {
        final ViewHolder holder;
        Integer          integer = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.game_list_adapter, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (integer == 1)
            convertView.setBackgroundResource(R.drawable.blue_ball);
        else
            convertView.setBackgroundResource(R.drawable.yellow_ball);

        return convertView;
    }

    static class ViewHolder {
        View view;
    }

}
