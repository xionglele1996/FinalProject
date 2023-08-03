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

    /**
     * Factory method to create a new instance of this fragment using the provided ConversionQuery.
     *
     * @param conversionQuery The conversion query to display.
     * @return A new instance of fragment ConversionDetailFragment.
     */
    public static ConversionDetailFragment newInstance(ConversionQuery conversionQuery) {
        ConversionDetailFragment fragment = new ConversionDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("conversionQuery", conversionQuery);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment is first created.
     * Initializes the conversion query if arguments are provided.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            conversionQuery = (ConversionQuery) getArguments().getSerializable("conversionQuery");
        }
    }

    /**
     * Called to create and return the view hierarchy associated with the fragment.
     * Populates various TextView elements with the details of the conversion.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
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
