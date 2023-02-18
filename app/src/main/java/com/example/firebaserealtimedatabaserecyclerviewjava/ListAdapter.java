package com.example.firebaserealtimedatabaserecyclerviewjava;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.jetbrains.annotations.NotNull;


import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListAdapter extends FirebaseRecyclerAdapter<Data, ListAdapter.MyHolder> {

    private Context context;


    public ListAdapter(@NonNull @NotNull FirebaseRecyclerOptions<Data> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull MyHolder holder, final int position, @NonNull @NotNull Data model) {
        holder.firstName.setText(model.getFirstName());
        holder.lastName.setText(model.getLastName());
        holder.username.setText(model.getUsername());

        Glide.with(holder.img.getContext())
                .load(model.getUrl())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.popup))
                        .setExpanded(true, 700)
                        .create();

                // dialogPlus.show();
                View view = dialogPlus.getHolderView();

                EditText firstName = view.findViewById(R.id.etName);
                EditText lastName = view.findViewById(R.id.etLastName);
                EditText username = view.findViewById(R.id.etUsername);

                Button buttonUpdate = view.findViewById(R.id.btnUpdateData);

                firstName.setText(model.getFirstName());
                lastName.setText(model.getLastName());
                username.setText(model.getUsername());
                dialogPlus.show();

                buttonUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("firstName", firstName.getText().toString());
                        map.put("lastName", lastName.getText().toString());
                        map.put("username", username.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Users").child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.firstName.getContext(), "Success!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(holder.firstName.getContext(), "Failure!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });


            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.firstName.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleted data can`t be Undo.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(getRef(position).getKey()).removeValue();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.firstName.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });


    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyHolder(view);

    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView firstName, lastName, username;

        ImageButton edit, delete;

        public MyHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgC);
            firstName = itemView.findViewById(R.id.tvName);
            lastName = itemView.findViewById(R.id.tvL);
            username = itemView.findViewById(R.id.tvUs);
            edit = itemView.findViewById(R.id.btnEdit);
            delete = itemView.findViewById(R.id.btnDelete);

        }
    }
}
