package pe.com.lycsoftware.cibertecproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import pe.com.lycsoftware.cibertecproject.model.Task;
import pe.com.lycsoftware.cibertecproject.util.Networking;

public class TaskListFragment extends Fragment {
    private static final String TAG = "TaskListFragment";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        Log.d(TAG, "onCreateView: start");
        View ret = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = ret.findViewById(R.id.task_list_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout = ret.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listTasks();
            }
        });

        Log.d(TAG, "onCreateView: finish");
        return ret;
    }

    private void listTasks() {
        Log.d(TAG, "listTasks: ------ Init task request ------");

        Networking.getTasks(new Networking.NetworkingCallback<List<Task>>() {
            @Override
            public void onResponse(List<Task> response) {
                TaskAdapter lst = new TaskAdapter(response);
                recyclerView.setAdapter(lst);
                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "onResponse: tasks correctly loaded size = " + response.size());
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "onError: Error in user loading", throwable);
            }
        });

        Log.d(TAG, "listTasks: ------ Finish task request ------");
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: start");
        super.onAttach(context);
        if (context instanceof OnTaskListFragmentInteractionListener) {
            mListener = (OnTaskListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTaskListFragmentInteractionListener");
        }
        Log.d(TAG, "onAttach: finish");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: start");
        listTasks();
        Log.d(TAG, "onResume: finish");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    class TaskAdapter
            extends RecyclerView.Adapter<TaskListFragment.ViewHolder> {

        private final List<Task> lstTasks;

        TaskAdapter(List<Task> lstTasks) {
            this.lstTasks = lstTasks;
        }

        @Override
        public TaskListFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_list_content, parent, false);
            return new TaskListFragment.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder,
                                     final int position) {
            Log.d(TAG, "onBindViewHolder: position = " + lstTasks.get(position).getName());
            holder.taskName.setText(lstTasks.get(position).getName());
            holder.taskDate.setText(new SimpleDateFormat("dd/MM/yyyy")
                    .format(lstTasks.get(position).getTaskDate().toDate()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int selectedTask = holder.getAdapterPosition();
                    mListener.onTaskListFragmentInteraction(lstTasks.get(position));
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
            return lstTasks.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView taskName;
        final TextView taskDate;

        ViewHolder(View view) {
            super(view);
            taskName = view.findViewById(R.id.taskName);
            taskDate = view.findViewById(R.id.taskDate);
        }
    }

    public interface OnTaskListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onTaskListFragmentInteraction(final Task selectedTask);
    }
}
