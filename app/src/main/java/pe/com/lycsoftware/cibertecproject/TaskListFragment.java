package pe.com.lycsoftware.cibertecproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.util.List;

import pe.com.lycsoftware.cibertecproject.model.Task;
import pe.com.lycsoftware.cibertecproject.service.TaskService;
import pe.com.lycsoftware.cibertecproject.util.Constants;
import pe.com.lycsoftware.cibertecproject.util.DateTimeTypeConverter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskListFragment extends Fragment {
    private static final String TAG = "TaskListFragment";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private OnFragmentInteractionListener mListener;

    public TaskListFragment() {
        // Required empty public constructor
    }

    public static TaskListFragment newInstance() {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = ret.findViewById(R.id.task_list_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Log.d(TAG, "onCreateView: ------ Init task request ------");
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(DateTime.class, new DateTimeTypeConverter());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url)
                .addConverterFactory(GsonConverterFactory.create(gson.create())) //conversor JSON --> JAVA
                .build();

        TaskService taskService = retrofit.create(TaskService.class);
        taskService.getTasks().enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                Log.d(TAG, "onResponse: List of tasks size = " + response.body().size());
                TaskListFragment.SimpleItemRecyclerViewAdapter lst = new TaskListFragment.SimpleItemRecyclerViewAdapter(response.body());
                recyclerView.setAdapter(lst);
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.d(TAG, "onFailure: ", t);
            }
        });
        Log.d(TAG, "onCreateView: ------ Finish task request ------");
        return ret;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<TaskListFragment.ViewHolder> {

        private final List<Task> mValues;

        SimpleItemRecyclerViewAdapter(List<Task> items) {
            mValues = items;
        }

        @Override
        public TaskListFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_list_content, parent, false);
            return new TaskListFragment.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final TaskListFragment.ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: position = " + mValues.get(position).getName());
            holder.mItem = mValues.get(position);
            holder.farm_name.setText(mValues.get(position).getName());
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get selected song position in song list.
                    int selectedTask = holder.getAdapterPosition();
                    //mListener.onClickItem(holder);
                }
            });
            /*holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Get selected song position in song list.
                    int selectedCattle = holder.getAdapterPosition();
                    Context context = v.getContext();
                    Intent intent = new Intent(context,
                            CattleDetailActivity.class);
                    intent.putExtra(SongUtils.SONG_ID_KEY,
                            holder.getAdapterPosition());
                    context.startActivity(intent);
                    return true;
                }
            });*/
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView farm_name;
        Task mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            farm_name = (TextView) view.findViewById(R.id.task_name);
        }

        public Task getmItem() {
            return mItem;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
