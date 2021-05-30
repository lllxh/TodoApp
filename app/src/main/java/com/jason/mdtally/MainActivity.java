package com.jason.mdtally;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.jason.mdtally.adapter.TodoAdapter;
import com.jason.mdtally.entity.Todo;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MdTodo-MainActivity";
    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout swipeRefresh;
    private Toolbar toolbar;
    private NavigationView navView;
    private FloatingActionButton fab;
    private TodoAdapter mAdapter;
    private SimpleDateFormat df;
    private boolean flag = true;
    private SharedPreferences.Editor editor;
    private List<Todo> todoList;
    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化数据库
        initDB();


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);

        /*
            侧边栏菜单
         */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setCheckedItem(R.id.navTask);
        navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navAboutMe:
                    Intent intent = new Intent(MainActivity.this, AboutMeActivity.class);
                    intent.putExtra("Url","http://blog.catnipball.xyz/about/");
                    startActivity(intent);
                    break;
                case R.id.navGithub:
                    Intent intent1 = new Intent(MainActivity.this, AboutMeActivity.class);
                    intent1.putExtra("Url","https://github.com/lllxh");
                    startActivity(intent1);
                    break;
            }
            drawerLayout.closeDrawers();
            return true;
        });

        /*
            FAB
         */
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewTodoActivity.class);
            startActivity(intent);
        });


        /*
            recyclerView初始化，待办列表
         */
        recyclerView = findViewById(R.id.recyclerView);
        todoList = DataSupport.findAll(Todo.class);
        mAdapter = new TodoAdapter(todoList);
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        /*
            下拉刷新
         */
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setRefreshing(false);
        swipeRefresh.setOnRefreshListener(() -> {
            //下拉震动
            Vibrator vib = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(100);
            swipeRefresh.setRefreshing(true);
            Log.i(TAG,"下拉刷新");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            requestData();
                            mAdapter.notifyDataSetChanged();
                            swipeRefresh.setRefreshing(false);
                        }
                    });
                }
            }).start();
        });


    }

    /*
        初始化数据库
     */
    private void initDB() {
        LitePal.getDatabase();
        Log.i(TAG,"数据库已创建");
    }

    /*
        获取数据集
     */
    private void requestData(){
        todoList.clear();
        todoList.addAll(DataSupport.findAll(Todo.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"进入MainActivity");
        requestData();
        mAdapter.notifyDataSetChanged();
    }
}