package com.example.yingscrollablepanellibrary;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

//适配器的基类-抽象类
public abstract class PanelAdapter {

    //抽象方法-绑定视图
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    //抽象方法-绑定数据
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column);

    //该方法的默认实现返回0，该整数值标识表示项目所需的视图类型
    public int getItemViewType(int row, int column) {
        return 0;
    }

    //返回适配器持有的数据集中行的项目总数
    public abstract int getRowCount();

    //返回适配器持有的数据集中列的项目总数
    public abstract int getColumnCount();
}
