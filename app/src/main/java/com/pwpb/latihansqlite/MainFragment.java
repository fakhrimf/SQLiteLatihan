package com.pwpb.latihansqlite;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainFragment extends Fragment implements
        View.OnClickListener, RecyclerViewAdapter.OnUserClickListener {
    RecyclerView recyclerView;
    EditText edtName, edtAge;
    Button btnSubmit;
    RecyclerView.LayoutManager layoutManager;
    Context context;
    List<PersonBean> listPersonInfo;

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        recyclerView = view.findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        edtName = view.findViewById(R.id.edtName);
        edtAge = view.findViewById(R.id.edtAge);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        DatabaseHelper db = new DatabaseHelper(context);
        listPersonInfo = db.selectUserData();
        RecyclerViewAdapter adapter = new
                RecyclerViewAdapter(context, listPersonInfo, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        String nama = edtName.getText().toString();
        String age = edtAge.getText().toString();
        if (nama.isEmpty() || age.isEmpty()){
            Toast.makeText(getContext(),"Isi Data Terlebih Dahulu", Toast.LENGTH_SHORT).show();
        } else {
            if (v.getId() == R.id.btnSubmit) {
                DatabaseHelper db = new DatabaseHelper(context);
                PersonBean currentPerson = new PersonBean();
                String btnStatus = btnSubmit.getText().toString();
                if (btnStatus.equals("Submit")) {
                    currentPerson.setName(edtName.getText().toString());
                    currentPerson.setAge(Integer.parseInt(edtAge.getText().toString()));
                    db.insert(currentPerson);
                }
                if (btnStatus.equals("Update")) {
                    currentPerson.setName(edtName.getText().toString());
                    currentPerson.setAge(Integer.parseInt(edtAge.getText().toString()));
                    db.update(currentPerson);
                }
                setupRecyclerView();
                edtName.setText("");
                edtAge.setText("");
                edtName.setEnabled(true);
                edtName.setInputType(InputType.TYPE_CLASS_TEXT);
                btnSubmit.setText("Submit");
            }
        }
    }

    @Override
    public void onUserClick(PersonBean currentPerson, String action) {
        if (action.equals("Edit")) {
            edtName.setText(currentPerson.getName());
            edtName.setInputType(InputType.TYPE_NULL);
            edtName.setEnabled(false);
            edtAge.setText(currentPerson.getAge() + "");
            btnSubmit.setText("Update");
        }
        if (action.equals("Delete")) {
            DatabaseHelper db = new DatabaseHelper(context);
            db.delete(currentPerson.getName());
            setupRecyclerView();
        }
    }
}