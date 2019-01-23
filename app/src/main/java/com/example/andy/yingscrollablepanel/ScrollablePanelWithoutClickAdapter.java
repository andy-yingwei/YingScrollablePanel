package com.example.andy.yingscrollablepanel;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yingscrollablepanellibrary.ColumnInfo;
import com.example.yingscrollablepanellibrary.DataInfo;
import com.example.yingscrollablepanellibrary.PanelAdapter;
import com.example.yingscrollablepanellibrary.RowInfo;

import java.util.ArrayList;
import java.util.List;
/*
只要实现抽象PanelAdapter类
1. setRowInfoList() setColumnInfoList() setDataList() 传递进来表格数据
2. onCreateViewHolder()获得表格中各个模块的view
      （1） TitleViewHolder() RowViewHolder() ColumViewHolder() DataViewHolder() 内部类获得各个view具体控件绑定

3. onBindViewHolder()为控件赋值
      （1） getItemViewType() 通过行列编号获得每个单元格性质
      （2） setRowTextView() setColumnTextView() setDataTextView() 把传递进来的表格数据为每个控件赋值
*/

public class ScrollablePanelWithoutClickAdapter extends PanelAdapter {

    private List<RowInfo> rowInfoList =new ArrayList<>();
    private List<ColumnInfo> columnInfoList = new ArrayList<>();
    private List<List<DataInfo>> dataList =new ArrayList<>();

    private int position;

    private static final int TITLE_TYPE = 1;  //左上角名
    private static final int ROW_TYPE = 2;    //行名
    private static final int COLUMN_TYPE = 3; //列名
    private static final int DATA_TYPE = 4;   //数据


    //通过内部类绑定title_textview
    private static class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TitleViewHolder(View view) {
            super(view);
            this.titleTextView = (TextView) view.findViewById(R.id.title_textview);
        }
    }

    //通过内部类绑定row_textview
    private static class RowViewHolder extends RecyclerView.ViewHolder{
        public TextView rowTextView;
        public RowViewHolder(View view) {
            super(view);
            this.rowTextView = (TextView) view.findViewById(R.id.row_textview);
        }
    }

    //通过内部类绑定column_textview
    private static class ColumViewHolder extends RecyclerView.ViewHolder {
        public TextView columnTextView;

        public ColumViewHolder(View itemView) {
            super(itemView);
            this.columnTextView = (TextView) itemView.findViewById(R.id.column_textview);
        }
    }

    //通过内部类绑定data_textview
    private static class DataViewHolder extends RecyclerView.ViewHolder {
        public TextView dataTextView;
        public View view;

        public DataViewHolder(View view) {
            super(view);
            this.view = view;
            this.dataTextView = (TextView) view.findViewById(R.id.data_textview);
        }
    }

    //控件绑定
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {//按照单元格性质来填充布局
            case TITLE_TYPE:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_title, parent, false));
            case ROW_TYPE:
                return new RowViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_row, parent, false));
            case COLUMN_TYPE:
                return new ColumViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_column, parent, false));
            case DATA_TYPE://数据
                return new DataViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_data, parent, false));
            default:
                break;
        }
        return new DataViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_data, parent, false));
    }

    //绑定数据
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int row, int column) {
        int viewType = getItemViewType(row, column);
        switch (viewType) {
            case TITLE_TYPE:
                break;
            case ROW_TYPE:
                setRowTextView((RowViewHolder) holder , row);
                break;
            case COLUMN_TYPE:
                setColumnTextView((ColumViewHolder) holder , column);
                break;
            case DATA_TYPE:
                setDataTextView((DataViewHolder) holder , row , column);
                break;
            default:
                setDataTextView((DataViewHolder) holder , row , column);
        }
    }

    //通过行号和列号取得此单单元格的性质
    @Override
    public int getItemViewType(int row, int column) {
        if (column == 0 && row == 0) {
            return TITLE_TYPE;     //左上角名
        }
        if (column == 0) {
            return ROW_TYPE;       //行名
        }
        if (row == 0) {
            return COLUMN_TYPE;    //列名
        }
        return DATA_TYPE;          //数据
    }


    //重写方法获得行数,rowInfoList下标从0开始
    @Override
    public int getRowCount() {
        return rowInfoList.size() + 1;
    }

    //重写方法获得列数,columnInfoList下标从0开始
    @Override
    public int getColumnCount() {
        return columnInfoList.size()+1;
    }

    //行名称赋值
    private void setRowTextView(final RowViewHolder rowViewHolder , final int row) {
        //(0,0)位置是titel,表格中的行名称的最小编号是(1,0),但是行数据是list编号从0开始,所以行编号-1，才是和表格中行号匹配的值
        final RowInfo rowInfo = rowInfoList.get(row - 1);
        if (rowInfo != null && row > 0) {
            rowViewHolder.rowTextView.setText(rowInfo.getRowName());
        }
    }

    //列名称赋值
    private void setColumnTextView(final ColumViewHolder columViewHolder , final int column) {
        //(0,0)位置是titel,表格中的列名称的最小编号是(0,1),但是列数据是list编号从0开始,所以列编号-1，才是和表格中列号匹配的值
        ColumnInfo ColumnInfo = columnInfoList.get(column -1);
        if (ColumnInfo != null && column > 0) {
            columViewHolder.columnTextView.setText(ColumnInfo.getName());
        }
    }

    //数据赋值
    private void setDataTextView(final DataViewHolder dataViewHolder , final int row, final int column) {
        //(0,0)位置是titel,表格中的数据值的最小编号是(1,1),但是行和列数据是list编号都是从0开始,所以行列编号都-1，才是和表格中数据匹配的值
        final DataInfo dataInfo = dataList.get(row-1).get(column-1);
        dataViewHolder.dataTextView.setText(dataInfo.getData());

    }

    //行名全局变量赋值
    public void setRowInfoList(List<RowInfo> rowInfoList) {
        this.rowInfoList = rowInfoList;
    }

    //列名全局变量赋值
    public void setColumnInfoList(List<ColumnInfo> columnInfoList) {
        this.columnInfoList = columnInfoList;
    }

    //数据全局变量赋值
    public void setDataList(List<List<DataInfo>> dataList) {
        this.dataList = dataList;
    }

}