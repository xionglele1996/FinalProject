package net.matthew.converter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConversionDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversionDetailFragment extends Fragment {

    /**
     * The query to display.
     */
    private ConversionQuery conversionQuery;

    /**
     * Required empty public constructor.
     */
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

        textViewSourceCurrency.setText(conversionQuery.getSourceCurrency());
        textViewDestinationCurrency.setText(conversionQuery.getDestinationCurrency());
        textViewAmount.setText(conversionQuery.getAmount());
        textViewConvertedAmount.setText(conversionQuery.getConvertedAmount());

        return view;
    }
}
