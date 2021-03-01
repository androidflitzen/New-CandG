package com.flitzen.cng.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.R;
import com.flitzen.cng.model.PaymentListModel;
import com.flitzen.cng.utils.Utils;

import java.util.ArrayList;

public class PaymentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<PaymentListModel.Result> paymentList=new ArrayList<>();
    private Context mcontext;
    private OnItemClickListener listener;

    // for load more
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public interface OnItemClickListener {
        void onItemClick(PaymentListModel.Result item);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void add(int position, PaymentListModel.Result item) {
        paymentList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(PaymentListModel.Result item) {
        int position = paymentList.indexOf(item);
        paymentList.remove(position);
        notifyItemRemoved(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.adapter_payment_list, parent, false);
            return new ViewHolderRow(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.item_progressbar, parent, false);
            return new ViewHolderLoading(view);
        }
        return null;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PaymentListAdapter(Context context, ArrayList<PaymentListModel.Result> paymentList, RecyclerView recyclerView) {

        this.mcontext = context;
        this.paymentList = paymentList;

      /*  // load more
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });*/
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    /*// Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return paymentList == null ? 0 : paymentList.size();
    }*/

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 10;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public void setOnItemListener(OnItemClickListener listener) {
        this.listener = listener;
    }

   /* @Override
    public int getItemViewType(int position) {
        return paymentList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }*/

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }

    public void setLoaded() {
        isLoading = false;
    }

    private class ViewHolderLoading extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ViewHolderLoading(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.itemProgressbar);
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolderRow extends RecyclerView.ViewHolder {
        TextView txtCname, txtDate, txtAmount, txtType, txtReference, txtSalesType, txtPaymentType;
        ImageView imgSalesType;
        CardView cardviewSalesType,mainView;

        public ViewHolderRow(View v) {
            super(v);
            cardviewSalesType = (CardView) itemView.findViewById(R.id.cardview_payment_a_sales_type);
            imgSalesType = (ImageView) itemView.findViewById(R.id.img_payment_a_sales_type);
            txtSalesType = (TextView) itemView.findViewById(R.id.txt_payment_a_sales_type);
            txtCname = (TextView) itemView.findViewById(R.id.txt_payment_a_cname);
            txtDate = (TextView) itemView.findViewById(R.id.txt_payment_a_date);
            txtAmount = (TextView) itemView.findViewById(R.id.txt_payment_a_amount);
            txtReference = (TextView) itemView.findViewById(R.id.txt_payment_a_refrence);
            txtType = (TextView) itemView.findViewById(R.id.txt_payment_a_type);
            txtPaymentType = (TextView) itemView.findViewById(R.id.txt_payment_a_payment_type);
            mainView=itemView.findViewById(R.id.view_payment_a_main);
        }

        public void bind(final PaymentListModel.Result item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.playClickSound(mcontext);
                }
            });
        }
    }

}
