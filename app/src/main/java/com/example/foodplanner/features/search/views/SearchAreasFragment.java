package com.example.foodplanner.features.search.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.core.helpers.MarginItemDecoration;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.common.entities.AreaEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.models.Area;
import com.example.foodplanner.features.common.remote.MealRemoteService;
import com.example.foodplanner.features.common.repositories.AreaRepository;
import com.example.foodplanner.features.common.services.AppDatabase;
import com.example.foodplanner.features.search.adapters.AreasListAdapter;
import com.example.foodplanner.features.search.helpers.SearchCriteria;
import com.example.foodplanner.features.search.models.SearchAreasModelImpl;
import com.example.foodplanner.features.search.presenters.SearchAreasPresenter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class SearchAreasFragment extends Fragment implements SearchAreasView {
    private static final String TAG = "SearchAreasFragment";

    private RecyclerView list;
    private ProgressBar loader;
    private TextView errorTv;
    private AreasListAdapter listAdapter;

    private SearchAreasPresenter presenter;

    public SearchAreasFragment() {
        super(R.layout.items_list);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchAreasPresenter(
                this,
                new SearchAreasModelImpl(savedInstanceState,
                        new AreaRepository(
                                MealRemoteService.create(),
                                AppDatabase.getInstance(requireContext()).areaDAO(),
                                new BaseMapper<>(Area.class, AreaEntity.class)
                        )
                )
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        list = view.findViewById(R.id.items_list);
        loader = view.findViewById(R.id.items_loader);
        errorTv = view.findViewById(R.id.items_error_tv);
        list.addItemDecoration(
                new MarginItemDecoration(ViewUtils.dpToPx(requireContext(), 8), 1, LinearLayoutManager.HORIZONTAL)
        );
        listAdapter = new AreasListAdapter(area -> {
            Navigation.findNavController(view).navigate(
                    SearchFragmentDirections.actionSearchToSearchResults(new SearchCriteria(SearchCriteria.Type.AREA, area))
            );
        });
        list.setAdapter(listAdapter);
        LinearLayoutManager areasLayout = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
        list.setLayoutManager(areasLayout);

        presenter.init(getViewLifecycleOwner());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.saveInstance(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void updateAreas(List<String> products) {
        list.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
        errorTv.setVisibility(View.GONE);
        listAdapter.updateAreas(products);
    }

    @Override
    public void onLoadFailure(Throwable error) {
        list.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
        errorTv.setText(error.getLocalizedMessage());
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}