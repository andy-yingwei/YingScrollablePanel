package com.example.yingscrollablepanellibrary;

/*自定义了一个灵活的视图,但它会将第一行和第一列的itemView固定在原来的位置
垂直方向上使用了一个recycleView,故可以实现上下的联动。
这个垂直recycleView的每一个item是由一个FrameLayout和一个水平方向的recycleView组成。
每一个水平方向的recycleView都与顶部的recycleView互相设置滚动监听，由此实现左右方向的联动

执行流程
1. ScrollablePanel()构造方法
2. initView()初始化
    （1）获得firstItemView  recyclerHeaderView  recyclerContentView
    （2）panelLineAdapter填充recyclerContentView
        （a）PanelLineAdapter
             1)PanelLineAdapter()构造方法-全局变量赋值
                a)initRecyclerView()
             2)onCreateViewHolder()方法取得总布局
                a)内部类ViewHolder绑定具体控件first_column_item和recycler_line_list
             3)onBindViewHolder()给first_column_item和recycler_line_list绑定数据
*/

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.HashSet;

public class ScrollablePanel extends FrameLayout {

    protected FrameLayout firstItemView;          //表格头第一个单元格视图
    protected RecyclerView recyclerHeaderView;    //表格头视图
    protected RecyclerView recyclerContentView;   //表格数据滚动视图
    protected PanelLineAdapter panelLineAdapter;
    protected PanelAdapter panelAdapter;

    public ScrollablePanel(Context context, PanelAdapter panelAdapter) {
        super(context);
        this.panelAdapter = panelAdapter;
        initView();
    }

    public ScrollablePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ScrollablePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_scrollable_panel, this, true);
        firstItemView = (FrameLayout) findViewById(R.id.first_item);
        recyclerHeaderView = (RecyclerView) findViewById(R.id.recycler_header_list);
        recyclerContentView = (RecyclerView) findViewById(R.id.recycler_content_list);
        recyclerContentView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerHeaderView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerHeaderView.setHasFixedSize(true);
        if (panelAdapter != null) {
            panelLineAdapter = new PanelLineAdapter(recyclerHeaderView, recyclerContentView,panelAdapter );
            recyclerContentView.setAdapter(panelLineAdapter);
            setUpFirstItemView(panelAdapter);
        }
    }

    //刷新表格
    public void notifyDataSetChanged() {
        if (panelLineAdapter != null) {
            setUpFirstItemView(panelAdapter);
            panelLineAdapter.notifyDataChanged();
        }
    }

    private void setUpFirstItemView(PanelAdapter panelAdapter) {
        RecyclerView.ViewHolder viewHolder = panelAdapter.onCreateViewHolder(firstItemView, panelAdapter.getItemViewType(0, 0));
        panelAdapter.onBindViewHolder(viewHolder, 0, 0);
        firstItemView.addView(viewHolder.itemView);
    }


    public void setPanelAdapter(PanelAdapter panelAdapter) {
        if (this.panelLineAdapter != null) {
            panelLineAdapter.setPanelAdapter(panelAdapter);
            panelLineAdapter.notifyDataSetChanged();
        } else {
            panelLineAdapter = new PanelLineAdapter(recyclerHeaderView, recyclerContentView, panelAdapter);
            recyclerContentView.setAdapter(panelLineAdapter);
        }
        this.panelAdapter = panelAdapter;
        setUpFirstItemView(panelAdapter);

    }



    //此适配器用于将数据集绑定到显示在ScrollablePanel中的视图。
    private static class PanelLineAdapter extends RecyclerView.Adapter<PanelLineAdapter.ViewHolder> {

        private PanelAdapter panelAdapter;
        private RecyclerView headerRecyclerView;
        private RecyclerView contentRecyclerView;
        private HashSet<RecyclerView> observerList = new HashSet<>();
        private int firstPos = -1;
        private int firstOffset = -1;

        //1.构造方法-全局变量赋值
        public PanelLineAdapter(RecyclerView headerRecyclerView, RecyclerView contentRecyclerView, PanelAdapter panelAdapter) {
            this.panelAdapter = panelAdapter;
            this.headerRecyclerView = headerRecyclerView;
            this.contentRecyclerView = contentRecyclerView;
            initRecyclerView(headerRecyclerView);
            setUpHeaderRecyclerView();

        }

        //2.获得布局
        @Override
        public PanelLineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PanelLineAdapter.ViewHolder viewHolder = new PanelLineAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_row, parent, false));
            initRecyclerView(viewHolder.recyclerView);
            return viewHolder;
        }

        //3.绑定控件
        static class ViewHolder extends RecyclerView.ViewHolder {
            public FrameLayout firstColumnItemView;
            public RecyclerView recyclerView;
            public RecyclerView.ViewHolder firstColumnItemViewHolder;

            public ViewHolder(View view) {
                super(view);
                this.recyclerView = (RecyclerView) view.findViewById(R.id.recycler_line_list);
                this.firstColumnItemView = (FrameLayout) view.findViewById(R.id.first_column_item);
                this.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
            }
        }

        //绑定数据
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            PanelLineItemAdapter lineItemAdapter = (PanelLineItemAdapter) holder.recyclerView.getAdapter();
            if (lineItemAdapter == null) {
                lineItemAdapter = new PanelLineItemAdapter(position + 1, panelAdapter);
                holder.recyclerView.setAdapter(lineItemAdapter);
            } else {
                lineItemAdapter.setRow(position + 1);
                lineItemAdapter.notifyDataSetChanged();
            }
            if (holder.firstColumnItemViewHolder == null) {
                RecyclerView.ViewHolder viewHolder = panelAdapter.onCreateViewHolder(holder.firstColumnItemView, panelAdapter.getItemViewType(position + 1, 0));
                holder.firstColumnItemViewHolder = viewHolder;
                panelAdapter.onBindViewHolder(holder.firstColumnItemViewHolder, position + 1, 0);
                holder.firstColumnItemView.addView(viewHolder.itemView);
            } else {
                panelAdapter.onBindViewHolder(holder.firstColumnItemViewHolder, position + 1, 0);
            }

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return panelAdapter.getRowCount() - 1;
        }

        //行数据数据设置监听
        public void initRecyclerView(RecyclerView recyclerView) {
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (layoutManager != null && firstPos > 0 && firstOffset > 0) {
                layoutManager.scrollToPositionWithOffset(PanelLineAdapter.this.firstPos + 1, PanelLineAdapter.this.firstOffset);
            }
            observerList.add(recyclerView);
            recyclerView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_POINTER_DOWN:
                            for (RecyclerView rv : observerList) {
                                rv.stopScroll();
                            }
                    }
                    return false;
                }
            });
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int firstPos = linearLayoutManager.findFirstVisibleItemPosition();
                    View firstVisibleItem = linearLayoutManager.getChildAt(0);
                    if (firstVisibleItem != null) {
                        int firstRight = linearLayoutManager.getDecoratedRight(firstVisibleItem);
                        for (RecyclerView rv : observerList) {
                            if (recyclerView != rv) {
                                LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
                                if (layoutManager != null) {
                                    PanelLineAdapter.this.firstPos = firstPos;
                                    PanelLineAdapter.this.firstOffset = firstRight;
                                    layoutManager.scrollToPositionWithOffset(firstPos + 1, firstRight);
                                }
                            }
                        }
                    }
                }
                //用于监听recyclerView滑动状态的变化
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
        }

        //此适配器用于将数据集绑定到在的每一行中显示的视图中
        private static class PanelLineItemAdapter extends RecyclerView.Adapter {

            private PanelAdapter panelAdapter;
            private int row;

            public PanelLineItemAdapter(int row, PanelAdapter panelAdapter) {
                this.row = row;
                this.panelAdapter = panelAdapter;
            }


            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return this.panelAdapter.onCreateViewHolder(parent, viewType);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                this.panelAdapter.onBindViewHolder(holder, row, position + 1);
            }

            @Override
            public int getItemViewType(int position) {
                return this.panelAdapter.getItemViewType(row, position + 1);
            }


            @Override
            public int getItemCount() {
                return panelAdapter.getColumnCount() - 1;
            }

            public void setRow(int row) {
                this.row = row;
            }
        }


        public void notifyDataChanged() {
            setUpHeaderRecyclerView();
            notifyDataSetChanged();
        }

        //表格头视图填充数据
        private void setUpHeaderRecyclerView() {
            if (panelAdapter != null) {
                if (headerRecyclerView.getAdapter() == null) {
                    PanelLineItemAdapter lineItemAdapter = new PanelLineItemAdapter(0, panelAdapter);
                    headerRecyclerView.setAdapter(lineItemAdapter);
                } else {
                    headerRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        }

        public void setPanelAdapter(PanelAdapter panelAdapter) {
            this.panelAdapter = panelAdapter;
            setUpHeaderRecyclerView();
        }

        private HashSet<RecyclerView> getRecyclerViews() {
            HashSet<RecyclerView> recyclerViewHashSet = new HashSet<>();
            recyclerViewHashSet.add(headerRecyclerView);

            for (int i = 0; i < contentRecyclerView.getChildCount(); i++) {
                recyclerViewHashSet.add((RecyclerView) contentRecyclerView.getChildAt(i).findViewById(R.id.recycler_line_list));
            }
            return recyclerViewHashSet;
        }




    }


}

