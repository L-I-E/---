package kr.ac.skhu.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.skhu.project.R;
import kr.ac.skhu.project.item.Route;

public class RouteAdapter extends BaseAdapter {
    private ArrayList<Route> items = new ArrayList<>();

    public ArrayList<Route> getItems() {
        return items;
    }

    public void setItems(ArrayList<Route> items) {
        this.items = items;
    }

    public void addItem(Route item) {
//        RouteItemView item = new RouteItemView();
//
//        item.setIcon(icon);
//        item.setTitle(title);
//        item.setDesc(desc);

        items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Route getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_route_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView tvStart = (TextView) convertView.findViewById(R.id.tvStart);
        TextView tvEnd = (TextView) convertView.findViewById(R.id.tvEnd);
        TextView tvTrafficType = (TextView) convertView.findViewById(R.id.tvTrafficType);
        TextView tvSectionTime = (TextView) convertView.findViewById(R.id.tvSectionTime);
        TextView tvNo = (TextView) convertView.findViewById(R.id.tvNo);
        ImageView ivType = (ImageView) convertView.findViewById(R.id.ivType);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Route route = items.get(position);
        // 아이템 내 각 위젯에 데이터 반영

        String type = "도보";
        if (route.getTrafficType() == 1) {
            type = "지하철";
            ivType.setImageResource(R.drawable.subway);
            tvStart.setText(route.getStart() + "역 승차");
            tvEnd.setText(route.getEnd() + "역 하차");
            tvNo.setText(route.getNo());
        } else if (route.getTrafficType() == 2) {
            type = "버스";
            ivType.setImageResource(R.drawable.bus);
            tvStart.setText(route.getStart() + " 승차");
            tvEnd.setText(route.getEnd() + " 하차");
            tvNo.setText(route.getNo()+"번");
        }else if(route.getTrafficType()==3){
            type = "도보";
            ivType.setVisibility(View.INVISIBLE);
            tvStart.setVisibility(View.GONE);
            tvEnd.setVisibility(View.GONE);
            tvNo.setVisibility(View.GONE);
        }
        tvTrafficType.setText(type);
        tvSectionTime.setText(" "+route.getSectionTime() + "분 소요 ");

        return convertView;
    }
}
