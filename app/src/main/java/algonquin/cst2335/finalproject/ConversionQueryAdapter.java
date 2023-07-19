package algonquin.cst2335.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConversionQueryAdapter extends RecyclerView.Adapter<ConversionQueryAdapter.ViewHolder> {

    private List<ConversionQuery> conversionQueries;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(ConversionQuery conversionQuery);
    }

    public ConversionQueryAdapter(List<ConversionQuery> conversionQueries, OnItemClickListener onItemClickListener) {
        this.conversionQueries = conversionQueries;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_conversion_query, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ConversionQuery conversionQuery = conversionQueries.get(position);
        String query = conversionQuery.getSourceCurrency() + " " + conversionQuery.getAmount() + " -> " + conversionQuery.getDestinationCurrency();
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

        private TextView textViewQuery;
        private Button buttonDelete;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            textViewQuery = itemView.findViewById(R.id.textView_query);
            buttonDelete = itemView.findViewById(R.id.button_delete);

            buttonDelete.setOnClickListener(v -> onItemClickListener.onItemClick(conversionQueries.get(getAdapterPosition())));
        }
    }
}

