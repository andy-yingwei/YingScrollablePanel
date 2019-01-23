package com.example.andy.yingscrollablepanel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yingscrollablepanellibrary.ColumnInfo;
import com.example.yingscrollablepanellibrary.DataInfo;
import com.example.yingscrollablepanellibrary.RowInfo;
import com.example.yingscrollablepanellibrary.ScrollablePanel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ScrollablePanel scrollablePanelWithoutClick;
    ScrollablePanel scrollablePanelWithClick;
    private ScrollablePanelWithoutClickAdapter scrollablePanelWithoutClickAdapter=new ScrollablePanelWithoutClickAdapter();
    private ScrollablePanelWithClickAdapter scrollablePanelWithClickAdapter=new ScrollablePanelWithClickAdapter();
    private List<ColumnInfo> columnInfoList = new ArrayList<>();
    private List<RowInfo> rowInfoList=new ArrayList<>();
    private List<List<DataInfo>> dataInfoList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollablePanelWithoutClick=findViewById(R.id.without_click_ScrollablePanel);
        scrollablePanelWithClick=findViewById(R.id.with_click_ScrollablePanel);

        //列名称
        ArrayList<String> listcolumn = new ArrayList<>();
        listcolumn.add("性别");
        listcolumn.add("眼砂");
        listcolumn.add("羽色");
        for (int i = 0; i < listcolumn.size(); i++) {
            ColumnInfo ColumnInfo = new ColumnInfo();
            String name = listcolumn.get(i);
            ColumnInfo.setName(name);
            columnInfoList.add(ColumnInfo);
        }

        //行名称和单元格数据
        RowInfo rowInfo = new RowInfo();
        rowInfo.setRowName("");
        rowInfoList.add(rowInfo);

        List<DataInfo> data = new ArrayList<>();
        DataInfo dataInfo_sex = new DataInfo();
        dataInfo_sex.setData("");
        data.add(dataInfo_sex);

        DataInfo dataInfo_eye = new DataInfo();
        dataInfo_eye.setData("");
        data.add(dataInfo_eye);

        DataInfo dataInfo_color = new DataInfo();
        dataInfo_color.setData("");
        data.add(dataInfo_color);
        dataInfoList.add(data);

        scrollablePanelWithoutClickAdapter.setColumnInfoList(columnInfoList);
        scrollablePanelWithoutClickAdapter.setRowInfoList(rowInfoList);
        scrollablePanelWithoutClickAdapter.setDataList(dataInfoList);
        scrollablePanelWithoutClick.setPanelAdapter(scrollablePanelWithoutClickAdapter);

        scrollablePanelWithClickAdapter.setColumnInfoList(columnInfoList);
        scrollablePanelWithClickAdapter.setRowInfoList(rowInfoList);
        scrollablePanelWithClickAdapter.setDataList(dataInfoList);
        scrollablePanelWithClick.setPanelAdapter(scrollablePanelWithClickAdapter);
    }
}
