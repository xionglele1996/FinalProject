package net.matthew.converter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import net.R;

import java.util.List;

public class ConversionQueryAdapter extends RecyclerView.Adapter<ConversionQueryAdapter.ViewHolder> {

    private List<ConversionQuery> conversionQueries;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(ConversionQuery conversionQuery);
        void onItemDetailClick(ConversionQuery conversionQuery);
    }

    public ConversionQueryAdapter(List<ConversionQuery> conversionQueries, OnItemClickListener onItemClickListener) {
        this.conversionQueries = conversionQueries;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matthew_row_conversion_query,
                                                                     parent,
                                                                     false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ConversionQuery conversionQuery = conversionQueries.get(position);
        String query = conversionQuery.getSourceCurrency() + " " + conversionQuery.getAmount() + " -> " + conversionQuery.getDestinationCurrency() + " " + conversionQuery.getConvertedAmount();
        holder.textViewQuery.setText(query);
    }


    @Override
    public int getItemCount() {
        return conversionQueries.size();
    }

    public void updateData(List<ConversionQuery> newConversionQueries) {
        this.conversionQueries = newConversionQueries;
        notifyDataSetChanged();  // Notify the adapter of the data change
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewQuery;
        private final Button buttonDelete;
        private final Button buttonDetail;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            textViewQuery = itemView.findViewById(R.id.textView_query);
            buttonDelete = itemView.findViewById(R.id.button_delete);
            buttonDetail = itemView.findViewById(R.id.button_detail);

            buttonDelete.setOnClickListener(v -> onItemClickListener.onItemClick(conversionQueries.get(
                    getAdapterPosition())));
            buttonDetail.setOnClickListener(v -> onItemClickListener.onItemDetailClick(conversionQueries.get(getAdapterPosition())));

        }
    }

}

