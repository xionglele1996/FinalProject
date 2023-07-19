package net.zhichuan.bear;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.google.android.material.snackbar.Snackbar;
import net.R;
import net.databinding.RiverFragmentListBinding;
import net.databinding.RiverFragmentRowBinding;
import net.zhichuan.bear.utils.*;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ListFragment extends Fragment {
    static String ARG_WIDTH = "width";
    static String ARG_HEIGHT = "height";
    int mWidth;
    int mHeight;
    boolean shouldDownload = false;
    ArrayList<ImageEntity> images;
    private RiverFragmentListBinding binding;
    private ImageViewModel imageViewModel;
    private ImageDAO imageDAO;
    private Toolbar toolbar;
    private RecyclerView.Adapter myAdapter;
    public boolean deleteMode = false;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    public static ListFragment newInstance(ImageEntity entity) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WIDTH, entity.getWidth());
        args.putInt(ARG_HEIGHT, entity.getHeight());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mWidth = getArguments().getInt(ARG_WIDTH);
            mHeight = getArguments().getInt(ARG_HEIGHT);
            shouldDownload = true;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RiverFragmentListBinding.inflate(getLayoutInflater());

        imageViewModel = new ImageViewModel();

        ImageDatabase imageDatabase = Room.databaseBuilder(requireActivity().getApplicationContext(),
                                                           ImageDatabase.class,
                                                           "image-db").build();
        imageDAO = imageDatabase.imageDAO();

        images = imageViewModel.image.getValue();

        toolbar = binding.toolbar;
        toolbar.setTitle("Generated Images");
        toolbar.inflateMenu(R.menu.river_menu);

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.river_new_image) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.river_frame, GeneratorFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.river_delete_image) {
                deleteMode = true;
            }
            return false;
        });

        if (images == null) {
            imageViewModel.image.setValue(images = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
                           {
                               images.addAll(imageDAO.getAllImages()); //Once you get the data from the database

                               requireActivity().runOnUiThread(() -> binding.riverRecyclerView.setAdapter(myAdapter)); //You can then load the RecyclerView
                           });
        }


        binding.riverRecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                RiverFragmentRowBinding binding = RiverFragmentRowBinding.inflate(getLayoutInflater());

                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ImageEntity image = images.get(position);

                new DownloadImageTask(holder.image).execute(image.getWidth(), image.getHeight());


                holder.width.setText(String.valueOf(image.getWidth()));
                holder.height.setText(String.valueOf(image.getHeight()));
            }

            @Override
            public int getItemCount() {
                return images.size();
            }
        });

        binding.riverRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));

        if (shouldDownload) {
            ImageEntity image = new ImageEntity(mWidth, mHeight);

            images.add(image);

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
                                   imageDAO.insert(image));

            myAdapter.notifyItemInserted(images.size());
        }

        return binding.getRoot();
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView width;
        TextView height;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAdapterPosition();

                if (deleteMode) {
                    ImageEntity image = images.get(position);

                    images.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() ->
                                           imageDAO.delete(image));

//                    give the user a chance to undo the deletion
                    Snackbar.make(binding.getRoot(), "Image deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", v -> {
                                images.add(position, image);
                                myAdapter.notifyItemInserted(position);

                                Executor thread1 = Executors.newSingleThreadExecutor();
                                thread1.execute(() ->
                                                        imageDAO.insert(image));
                            }).show();
                }
            });

            image = itemView.findViewById(R.id.river_row_image);
            width = itemView.findViewById(R.id.river_row_width);
            height = itemView.findViewById(R.id.river_row_height);
        }
    }
}