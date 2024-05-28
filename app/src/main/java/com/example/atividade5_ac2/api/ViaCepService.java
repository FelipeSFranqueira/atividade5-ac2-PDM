package com.example.atividade5_ac2.api;

import com.example.atividade5_ac2.models.Endereco;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ViaCepService {

    @GET("{cep}/json/")
    Call<Endereco> buscarEnderecoPorCep(@Path("cep") String cep);
}
