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

import java.text.SimpleDateFormat;
import java.util.List;

import pe.com.lycsoftware.cibertecproject.model.Task;
import pe.com.lycsoftware.cibertecproject.model.User;
import pe.com.lycsoftware.cibertecproject.restService.TaskService;
import pe.com.lycsoftware.cibertecproject.util.Constants;
import pe.com.lycsoftware.cibertecproject.util.DateTimeTypeConverter;
import pe.com.lycsoftware.cibertecproject.util.Networking;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskListFragment extends Fragment {
    private static final String TAG = "TaskListFragment";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private OnTaskListFragmentInteractionListener mListener;

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
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Log.d(TAG, "onCreateView: ------ Init task request ------");

        Networking.getTasks(new Networking.NetworkingCallback<List<Task>>() {
            @Override
            public void onResponse(List<Task> response) {
                TaskListFragment.SimpleItemRecyclerViewAdapter lst =
                        new TaskListFragment.SimpleItemRecyclerViewAdapter(response);
                recyclerView.setAdapter(lst);
                Log.d(TAG, "onResponse: tasks correctly loaded size = " + response.size());
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "onError: Error in user loading", throwable);
            }
        });

        Log.d(TAG, "onCreateView: ------ Finish task request ------");
        return ret;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTaskListFragmentInteractionListener) {
            mListener = (OnTaskListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTaskListFragmentInteractionListener");
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
            holder.taskName.setText(mValues.get(position).getName());
            holder.taskDate.setText(new SimpleDateFormat("dd/MM/yyyy")
                    .format(mValues.get(position).getTaskDate().toDate()));
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get selected song position in song list.
                    int selectedTask = holder.getAdapterPosition();
                    mListener.onTaskListFragmentInteraction(holder);
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
        final TextView taskName;
        final TextView taskDate;
        Task mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            taskName = view.findViewById(R.id.taskName);
            taskDate = view.findViewById(R.id.taskDate);
        }

        public Task getmItem() {
            return mItem;
        }
    }

    public interface OnTaskListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onTaskListFragmentInteraction(TaskListFragment.ViewHolder holder);
    }
}
