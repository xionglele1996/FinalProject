package net.zhichuan.bear;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.ViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    static String ARG_WIDTH = "width";
    static String ARG_HEIGHT = "height";
    int mWidth;
    int mHeight;
    boolean shouldDownload = false;
    ArrayList<ImageEntity> images;
    RiverFragmentListBinding binding;
    ImageDAO imageDAO;
    Toolbar toolbar;
    Adapter<MyRowHolder> myAdapter;
    public boolean deleteMode = false;

    /**
     * The required empty public constructor.
     */
    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListFragment.
     */
    @NonNull
    public static ListFragment newInstance() {
        return new ListFragment();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param entity The image entity.
     * @return A new instance of fragment ListFragment.
     */
    @NonNull
    public static ListFragment newInstance(@NonNull ImageEntity entity) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WIDTH, entity.getWidth());
        args.putInt(ARG_HEIGHT, entity.getHeight());
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mWidth = getArguments().getInt(ARG_WIDTH);
            mHeight = getArguments().getInt(ARG_HEIGHT);
            shouldDownload = true;
        }
    }

    /**
     * This method is called when the fragment is created.
     * It sets up the toolbar and the recycler view.
     *
     * @param inflater           The layout inflater.
     * @param container          The view group container.
     * @param savedInstanceState The saved instance state.
     * @return The view of the fragment.
     */
    @NonNull
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RiverFragmentListBinding.inflate(getLayoutInflater());

        ImageViewModel imageViewModel = new ImageViewModel();

        ImageDatabase imageDatabase = Room.databaseBuilder(requireActivity().getApplicationContext(),
                                                           ImageDatabase.class,
                                                           "image-db").build();
        imageDAO = imageDatabase.imageDAO();

        images = imageViewModel.image.getValue();

        toolbar = binding.toolbar;
        toolbar.setTitle(R.string.river_generated_Images);
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
                toolbar.setTitle(R.string.river_Select_Image_to_Delete);
            } else if (item.getItemId() == R.id.river_delete_all) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .create();
                alertDialog.setTitle(R.string.river_Delete_All);
                alertDialog.setCancelable(true);
                alertDialog.setMessage(getString(R.string.river_delete_all_prompt));

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.river_Yes),
                                      (dialog, which) -> {
                                          ArrayList<ImageEntity> backupImages = new ArrayList<>(images);
                                          for (ImageEntity image : images) {
                                              Executor thread = Executors.newSingleThreadExecutor();
                                              thread.execute(() -> imageDAO.delete(image));
                                          }

                                          images.clear();
                                          myAdapter.notifyDataSetChanged();

                                          Snackbar.make(binding.getRoot(),
                                                        R.string.river_Delete_All,
                                                        Snackbar.LENGTH_LONG)
                                                  .setAction(R.string.river_Undo, (click) -> {
                                                      images.addAll(backupImages);
                                                      myAdapter.notifyDataSetChanged();
                                                      for (ImageEntity image : backupImages) {
                                                          Executor thread = Executors.newSingleThreadExecutor();
                                                          thread.execute(() -> imageDAO.insert(image));
                                                      }
                                                  }).show();
                                      });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.river_No),
                                      (dialog, which) -> dialog.dismiss());

                alertDialog.show();
                return true;
            } else if (item.getItemId() == R.id.river_help) {
                showHelpDialog();
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


        /**
         * This is the adapter for the recycler view.
         */
        binding.riverRecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                RiverFragmentRowBinding binding = RiverFragmentRowBinding.inflate(getLayoutInflater());

                return new MyRowHolder(binding.getRoot());
            }

            /**
             * This method is called when the view holder is bound to the recycler view.
             * It sets the image, width, and height of the image.
             *
             * @param holder   The view holder.
             * @param position The position of the view holder.
             */
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ImageEntity image = images.get(position);

                DownloadImage.downloadImage(image.getWidth(), image.getHeight(), holder.image);


                holder.width.setText(String.valueOf(image.getWidth()));
                holder.height.setText(String.valueOf(image.getHeight()));
            }

            /**
             * This method returns the number of items in the recycler view.
             *
             * @return The number of items in the recycler view.
             */
            @Override
            public int getItemCount() {
                return images.size();
            }
        });

        /**
         * This method sets the layout manager for the recycler view.
         */
        binding.riverRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));

        /**
         * This method is called when the user clicks on the floating action button.
         * It adds a new image to the recycler view.
         *
         * @param view The floating action button.
         */
        if (shouldDownload) {
            ImageEntity image = new ImageEntity(mWidth, mHeight, new Date().getTime());

            images.add(image);

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
                                   imageDAO.insert(image));

            myAdapter.notifyItemInserted(images.size());

            shouldDownload = false;
        }

        return binding.getRoot();
    }

    /**
     * This method shows the help dialog.
     */
    class MyRowHolder extends ViewHolder {
        ImageView image;
        TextView width;
        TextView height;

        /**
         * This method is the constructor for the view holder.
         *
         * @param itemView The view.
         */
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAdapterPosition();

                if (!deleteMode) {
                    ImageEntity image = images.get(position);
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.river_frame,
                                     DetailsFragment.newInstance(image.getWidth(),
                                                                 image.getHeight(),
                                                                 image.getTime()))
                            .addToBackStack(null)
                            .commit();
                } else {
                    ImageEntity image = images.get(position);

                    images.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    toolbar.setTitle(R.string.river_generated_Images);
                    deleteMode = false;

                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() ->
                                           imageDAO.delete(image));

//                    give the user a chance to undo the deletion
                    Snackbar.make(binding.getRoot(), R.string.river_Image_deleted, Snackbar.LENGTH_LONG)
                            .setAction(R.string.river_Undo, v -> {
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

    /**
     * This method shows the help dialog.
     */
    private void showHelpDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setTitle(R.string.river_help_title)
                .setMessage(R.string.river_help_content)
                .setPositiveButton(getString(R.string.river_Yes), null)
                .show();
    }
}