package com.example.excaliberani;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class UserListViewAdapter extends BaseAdapter implements Filterable {

    Context context;
    LayoutInflater inflater;
    private ArrayList<String> arrayList;
    private ArrayList<String> orig;

    public UserListViewAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        inflater= LayoutInflater.from(context);
//        this.arrayList=new ArrayList<String>();
//        this.orig=new ArrayList<String>();
        this.arrayList = arrayList;
        this.orig=arrayList;
    }


    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public String getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter(){
     @Override

            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase();
                FilterResults filterResults = new FilterResults();

//                if(constraint==null){
//                    original=arrayList;
//                }
                if(constraint!=null && constraint.toString().length()>0){
                    final ArrayList<String> results=new ArrayList<String>();
//                    if(original!=null && original.size()>0){
                        for(final String u: orig){
                            if(u.toLowerCase(Locale.getDefault()).contains(constraint)){
                                results.add(u);
                            }
                        }
  //                  }
                    filterResults.count=results.size();
                    filterResults.values=results;
                }
                else{
                    filterResults.values=orig;
                    filterResults.count=orig.size();
                }
         Toast.makeText(context,orig.size(),Toast.LENGTH_SHORT).show();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
//                if(results.count==0){
//                    notifyDataSetInvalidated();
//                }
                    arrayList.clear();
                    for (String item : (ArrayList<String>) results.values) {
                        arrayList.add(item);
                    }
//                  orig=(ArrayList<String>) results.values;
//                if (results != null && results.count > 0) {
//                    arrayList.addAll((ArrayList<String>) results.values);
//                }
//                else {
//                    arrayList.addAll(orig);
//                }
                    notifyDataSetChanged();
                }
        };
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent){
        final ViewHolder viewHolder;
        if(view==null){
            viewHolder=new ViewHolder();
            view=inflater.inflate(R.layout.activity_item_view,null);
            viewHolder.name=(TextView) view.findViewById(R.id.user_name);
            view.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder) view.getTag();
        }
        viewHolder.name.setText(arrayList.get(position));
        return view;
    }

//    public void filter(String text){
//        text=text.toLowerCase(Locale.getDefault());
//        SearchActivity.namelist.clear();
//        if(text.isEmpty()){
//            SearchActivity.namelist.addAll(arrayList);
//        }
//        else {
//            for(String u: arrayList){
//                if(u.toLowerCase(Locale.getDefault()).contains(text)){
//                    SearchActivity.namelist.add(u);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }


}
