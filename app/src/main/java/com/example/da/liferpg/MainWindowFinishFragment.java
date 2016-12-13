package com.example.da.liferpg;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.da.liferpg.database.FinishDB;
import com.example.da.liferpg.database.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyx on 2016/12/8.
 */

public class MainWindowFinishFragment extends Fragment {
    private FinishDB finishDB;
    private List<Task> finish = new ArrayList<Task>();
    private List<String>listFinish = new ArrayList<>();
    private noScrollListView listView;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mw_habit_fragment,container,false);
        listView = (noScrollListView) view.findViewById(R.id.habit_list);
        finishDB = new FinishDB(view.getContext());
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        updateFinishView();
    }

    public void onResume() {  //更新列表信息
        super.onResume();
        updateFinishView();
        //Toast.makeText(getContext(),"sdsdddd",Toast.LENGTH_LONG).show();
    }
    private void setFinishData(List<Task> finishData){ //取得数据
        Cursor cursor = finishDB.Query();
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("taskname"));
            String detail = cursor.getString(cursor.getColumnIndex("taskdetail"));
            String taskDate = cursor.getString(cursor.getColumnIndex("taskdate"));
            String taskTime = cursor.getString(cursor.getColumnIndex("tasktime"));
            int tags[] = {cursor.getInt(cursor.getColumnIndex("work")),cursor.getInt(cursor.getColumnIndex("study")),cursor.getInt(cursor.getColumnIndex("sport")),
                    cursor.getInt(cursor.getColumnIndex("music")),cursor.getInt(cursor.getColumnIndex("internet")),cursor.getInt(cursor.getColumnIndex("reading"))};
            int completed = cursor.getInt(cursor.getColumnIndex("completed"));
            finish.add(0,new Task(name,detail,tags,taskDate,taskTime,completed));
            listFinish.add(0,name);
        }
        finishDB.close();
    }
    private void updateFinishView(){
        finish.clear();
        listFinish.clear();
        setFinishData(finish);
        ArrayAdapter finishAdapter = new ArrayAdapter(view.getContext(),android.R.layout.simple_expandable_list_item_1,listFinish);
        listView.setAdapter(finishAdapter);
    }
    class  ItemButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int position = (Integer)v.getTag();
            Task task = finish.get(position);
            task.setCompleted(task.getCompleted()+1);
            //v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            finishDB.update(task);
            finishDB.insert(task);  //已完成,插入相应的数据库
            updateFinishView();   //当点击之后更新界面
            //Log.i(position+"", "onClick: ");
        }
    }
}