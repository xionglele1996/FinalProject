package net.matthew.converter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.R;

public class ConversionDetailFragment extends Fragment {

    private ConversionQuery conversionQuery;

    public ConversionDetailFragment() {
        // Required empty public constructor
    }

    public static ConversionDetailFragment newInstance(ConversionQuery conversionQuery) {
        ConversionDetailFragment fragment = new ConversionDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("conversionQuery", conversionQuery);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            conversionQuery = (ConversionQuery) getArguments().getSerializable("conversionQuery");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.matthew_fragment_conversion_detail, container, false);

        TextView textViewSourceCurrency = view.findViewById(R.id.textView_sourceCurrency);
        TextView textViewDestinationCurrency = view.findViewById(R.id.textView_destinationCurrency);
        TextView textViewAmount = view.findViewById(R.id.textView_amount);
        TextView textViewConvertedAmount = view.findViewById(R.id.textView_convertedAmount);

        textViewSourceCurrency.setText(String.format("%s %s", getString(R.string.matthew_source_currency), conversionQuery.getSourceCurrency()));
        textViewDestinationCurrency.setText(String.format("%s %s", getString(R.string.matthew_destination_currency), conversionQuery.getDestinationCurrency()));
        textViewAmount.setText(String.format("%s %s", getString(R.string.matthew_amount), conversionQuery.getAmount()));
        textViewConvertedAmount.setText(String.format("%s %s", getString(R.string.matthew_converted_amount), conversionQuery.getConvertedAmount()));

        return view;
    }
}
