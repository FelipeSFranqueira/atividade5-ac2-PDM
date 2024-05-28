package com.example.atividade5_ac2.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.atividade5_ac2.CadastroAlunoActivity;
import com.example.atividade5_ac2.R;
import com.example.atividade5_ac2.api.AlunoService;
import com.example.atividade5_ac2.api.ApiClient;
import com.example.atividade5_ac2.models.Aluno;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoAdapter extends RecyclerView.Adapter<AlunoHolder> {
    private final List<Aluno> alunos;
    Context context;

    public AlunoAdapter(List<Aluno> alunos, Context context) {
        this.alunos = alunos;
        this.context = context;
    }

    @Override
    public AlunoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlunoHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_aluno, parent, false));
    }

    @Override
    public void onBindViewHolder(AlunoHolder holder, int position) {
        holder.nome.setText(alunos.get(position).getId() + " - " + alunos.get(position).getNome());
        holder.ra.setText(String.valueOf(alunos.get(position).getRa()));
        holder.cep.setText(alunos.get(position).getCep());
        holder.btnexcluir.setOnClickListener(view -> removerItem(position));
        holder.btneditar.setOnClickListener(view -> editarItem(position));
    }

    @Override
    public int getItemCount() {
        return alunos != null ? alunos.size() : 0;
    }

    private void removerItem(int position) {
        int id = alunos.get(position).getId();
        AlunoService alunoService = ApiClient.getAlunoService();
        Call<Void> call = alunoService.deleteAluno(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
// A solicitação foi bem-sucedida
                    alunos.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, alunos.size());
                    Toast.makeText(context, "Excluído com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
// A solicitação falhou
                    Log.e("Exclusao", "Erro ao excluir");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
// Ocorreu um erro ao fazer a solicitação
                Log.e("Exclusao", "Erro ao excluir");
            }
        });
    }

    private void editarItem(int position) {
        int id = alunos.get(position).getId();
        Intent i = new Intent(context, CadastroAlunoActivity.class);
        i.putExtra("id",id);
        context.startActivity(i);
    }

}