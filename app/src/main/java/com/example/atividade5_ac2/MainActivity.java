package com.example.atividade5_ac2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atividade5_ac2.adapter.AlunoAdapter;
import com.example.atividade5_ac2.api.AlunoService;
import com.example.atividade5_ac2.api.ApiClient;
import com.example.atividade5_ac2.models.Aluno;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerAluno;
    AlunoAdapter alunoAdapter;
    AlunoService alunoService;
    List<Aluno> listaAlunos;
    FloatingActionButton btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerAluno = (RecyclerView) findViewById(R.id.recyclerAluno);
        btnAdd = (FloatingActionButton)findViewById(R.id.btnAdd);
        alunoService = ApiClient.getAlunoService();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,CadastroAlunoActivity.class);
                startActivity(i);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        obterUsuarios();
    }
    private void configurarRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerAluno.setLayoutManager(layoutManager);
        alunoAdapter = new AlunoAdapter(listaAlunos, this);
        recyclerAluno.setAdapter(alunoAdapter);
        recyclerAluno.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
    private void obterUsuarios() {
        retrofit2.Call<List<Aluno>> call = alunoService.getAlunos();
        call.enqueue(new Callback<List<Aluno>>() {
            @Override
            public void onResponse(Call<List<Aluno>> call, Response<List<Aluno>>
                    response) {
                listaAlunos = response.body();
                configurarRecycler();
            }
            @Override
            public void onFailure(Call<List<Aluno>> call, Throwable t) {
                Log.e("TESTE", "Erro ao obter os contatos: " + t.getMessage());
            }
        });
    }
}