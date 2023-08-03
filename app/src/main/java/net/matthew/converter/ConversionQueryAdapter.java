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

/**
 * This class is used to adapt the conversion queries to the RecyclerView.
 * It handles the display of conversion queries in a list format.
 */
public class ConversionQueryAdapter extends RecyclerView.Adapter<ConversionQueryAdapter.ViewHolder> {

    /**
     * The list of conversion queries to be adapted.
     */
    private List<ConversionQuery> conversionQueries;

    /**
     * The listener for handling click events on items.
     */
    private final OnItemClickListener onItemClickListener;

    /**
     * Interface for handling click events on items within the adapter.
     */
    public interface OnItemClickListener {
        void onItemClick(ConversionQuery conversionQuery);
        void onItemDetailClick(ConversionQuery conversionQuery);
    }

    /**
     * Constructor for the ConversionQueryAdapter.
     *
     * @param conversionQueries     The conversion queries to adapt.
     * @param onItemClickListener   The listener for handling click events on items.
     */
    public ConversionQueryAdapter(List<ConversionQuery> conversionQueries, OnItemClickListener onItemClickListener) {
        this.conversionQueries = conversionQueries;
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * This method is called when the RecyclerView needs a new ViewHolder.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type.
     * @return The ViewHolder.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matthew_row_conversion_query,
                                                                     parent,
                                                                     false);
        return new ViewHolder(view, onItemClickListener);
    }

    /**
     * This method is called when the RecyclerView needs to display a ViewHolder.
     * It binds the conversion query to the provided ViewHolder.
     *
     * @param holder   The ViewHolder to display.
     * @param position The position of the ViewHolder.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ConversionQuery conversionQuery = conversionQueries.get(position);
        String query = conversionQuery.getSourceCurrency() + " " + conversionQuery.getAmount() + " -> " + conversionQuery.getDestinationCurrency() + " " + conversionQuery.getConvertedAmount();
        holder.textViewQuery.setText(query);
    }

    /**
     * Returns the total number of items in the list held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return conversionQueries.size();
    }

    /**
     * Update the conversion queries with new data and notify the adapter of the change.
     *
     * @param newConversionQueries The new list of conversion queries.
     */
    public void updateData(List<ConversionQuery> newConversionQueries) {
        this.conversionQueries = newConversionQueries;
        notifyDataSetChanged();  // Notify the adapter of the data change
    }

    /**
     * A ViewHolder class used to hold the views for each conversion query.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewQuery;
        private final Button buttonDelete;
        private final Button buttonDetail;

        /**
         * Constructor for the ViewHolder.
         *
         * @param itemView           The view for this ViewHolder.
         * @param onItemClickListener The listener for handling click events on items.
         */
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

