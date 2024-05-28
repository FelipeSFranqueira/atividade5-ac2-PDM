package com.example.atividade5_ac2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.atividade5_ac2.api.AlunoService;
import com.example.atividade5_ac2.api.ApiClient;
import com.example.atividade5_ac2.api.ApiClientViaCep;
import com.example.atividade5_ac2.api.ViaCepService;
import com.example.atividade5_ac2.models.Aluno;
import com.example.atividade5_ac2.models.Endereco;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroAlunoActivity extends AppCompatActivity {
    Button btnSalvar;
    AlunoService alunoService;
    ViaCepService viaCepService;
    TextView txtra, txtnome, txtcep, txtlog, txtcomp, txtbairro, txtcidade, txtuf;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_aluno);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        alunoService = ApiClient.getAlunoService();
        viaCepService = ApiClientViaCep.getViaCepService();
        txtra = findViewById(R.id.txtRaAluno);
        txtnome = findViewById(R.id.txtNomeAluno);
        txtcep = findViewById(R.id.txtCepAluno);
        txtlog = findViewById(R.id.txtLogAluno);
        txtcomp = findViewById(R.id.txtCompAluno);
        txtbairro = findViewById(R.id.txtBairroAluno);
        txtcidade = findViewById(R.id.txtCidadeAluno);
        txtuf = findViewById(R.id.txtUfAluno);
        id = getIntent().getIntExtra("id", 0);
        if (id > 0) {
            alunoService.getAlunoPorId(id).enqueue(new Callback<Aluno>() {
                @Override
                public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                    if (response.isSuccessful()) {
                        txtra.setText(String.valueOf(response.body().getRa()));
                        txtnome.setText(response.body().getNome());
                        txtcep.setText(response.body().getCep());
                        txtlog.setText(response.body().getLogradouro());
                        txtcomp.setText(response.body().getComplemento());
                        txtbairro.setText(response.body().getBairro());
                        txtcidade.setText(response.body().getCidade());
                        txtuf.setText(response.body().getUf());
                    }
                }

                @Override
                public void onFailure(Call<Aluno> call, Throwable t) {
                    Log.e("Obter aluno", "Erro ao obter aluno");
                }
            });
        }
        txtcep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String cep = txtcep.getText().toString().trim();
                    if (!cep.isEmpty()) {
                        buscarEndereco(cep);
                    }
                }
            }
        });
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Aluno aluno = new Aluno();
                aluno.setRa(Integer.parseInt(txtra.getText().toString()));
                aluno.setNome(txtnome.getText().toString());
                aluno.setCep(txtcep.getText().toString());
                aluno.setLogradouro(txtlog.getText().toString());
                aluno.setComplemento(txtcomp.getText().toString());
                aluno.setBairro(txtbairro.getText().toString());
                aluno.setCidade(txtcidade.getText().toString());
                aluno.setUf(txtuf.getText().toString());
                if (id == 0)
                    inserirAluno(aluno);
                else {
                    aluno.setId(id);
                    editarAluno(aluno);
                }
            }
        });
    }
    private void buscarEndereco(String cep) {
        Call<Endereco> call = viaCepService.buscarEnderecoPorCep(cep);
        call.enqueue(new Callback<Endereco>() {
            @Override
            public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                if (response.isSuccessful()) {
                    Endereco endereco = response.body();
                    if (endereco != null) {
                        txtlog.setText(endereco.getLogradouro());
                        txtcomp.setText(endereco.getComplemento());
                        txtbairro.setText(endereco.getBairro());
                        txtcidade.setText(endereco.getLocalidade());
                        txtuf.setText(endereco.getUf());
                    }
                } else {
                    Toast.makeText(CadastroAlunoActivity.this, "Erro ao buscar endereço", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Endereco> call, Throwable t) {
                Toast.makeText(CadastroAlunoActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void inserirAluno(Aluno aluno) {
        Call<Aluno> call = alunoService.postAluno(aluno);
        call.enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful()) {
                    // A solicitação foi bem-sucedida
                    Aluno createdPost = response.body();
                    Toast.makeText(CadastroAlunoActivity.this, "Inserido com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // A solicitação falhou
                    Log.e("Inserir", "Erro ao criar: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                // Ocorreu um erro ao fazer a solicitação
                Log.e("Inserir", "Erro ao criar: " + t.getMessage());
            }
        });
    }

    private void editarAluno(Aluno aluno) {
        Call<Aluno> call = alunoService.putAluno(id,aluno);
        call.enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful()) {
                    // A solicitação foi bem-sucedida
                    Aluno createdPost = response.body();
                    Toast.makeText(CadastroAlunoActivity.this, "Editado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // A solicitação falhou
                    Log.e("Editar", "Erro ao editar: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                // Ocorreu um erro ao fazer a solicitação
                Log.e("Editar", "Erro ao editar: " + t.getMessage());
            }
        });
    }
}