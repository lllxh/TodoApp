package com.jason.mdtally;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.jason.mdtally.adapter.TodoAdapter;
import com.jason.mdtally.entity.Todo;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout swipeRefresh;
    private Toolbar toolbar;
    private NavigationView navView;
    private FloatingActionButton fab;
    private TodoAdapter adapter;
    private SimpleDateFormat df;
    private boolean flag = true;
    private SharedPreferences.Editor editor;
    private List<Todo> todoList;
    private RecyclerView recyclerView;


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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setCheckedItem(R.id.navCall);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                return true;
            }
        });

        //fab
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            //TODO fab点击事件
            Todo todo = new Todo();
            todo.setDate(new Date());
            todo.setChecked(true);
            todo.setContent("软件工程");
            todo.setmId(UUID.randomUUID().toString());
            todo.save();
            Toast.makeText(this,todoList.toString(),Toast.LENGTH_SHORT).show();
        });



        // 待办清单
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        todoList = DataSupport.findAll(Todo.class);
        adapter = new TodoAdapter(todoList);
        recyclerView.setAdapter(adapter);

        // 下拉刷新
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(this::refreshTodoList);


    }

    // 刷新方法
    //TODO 崩溃问题还是没解决
    private void refreshTodoList() {
        if (recyclerView.isComputingLayout()) {
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    requestData();
                    adapter.notifyDataSetChanged();
                }
            });
        }else {
            requestData();
            adapter.notifyDataSetChanged();
        }
        swipeRefresh.setRefreshing(false);
    }

    private void initDB() {
        LitePal.getDatabase();
        Log.i(TAG,"数据库已创建");
    }

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
            case R.id.wallet:
                Intent intent = new Intent(this, WalletActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }
}