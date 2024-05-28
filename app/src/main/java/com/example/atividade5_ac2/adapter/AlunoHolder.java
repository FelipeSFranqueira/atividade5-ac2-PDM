package com.example.atividade5_ac2.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.atividade5_ac2.R;

public class AlunoHolder extends RecyclerView.ViewHolder {

    public TextView nome, ra, cep;
    public ImageView btnexcluir, btneditar;
    public AlunoHolder(View itemView){
        super(itemView);
        nome = (TextView) itemView.findViewById(R.id.txtNome);
        ra = (TextView) itemView.findViewById(R.id.txtRa);
        cep = (TextView) itemView.findViewById(R.id.txtCep);
        btnexcluir = (ImageView) itemView.findViewById(R.id.btnExcluir);
        btneditar = (ImageView) itemView.findViewById(R.id.btnEditar);
    }
}
