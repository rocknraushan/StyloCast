package com.example.stylocast;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyleAdapter  extends RecyclerView.Adapter<RecyleAdapter.viewholder> {

    ArrayList<Contact_Model> arrcont;


    Context context;
    private  int lastposition = -1;
    RecyleAdapter(Context context, ArrayList<Contact_Model> arrcont){
        this.context = context;
        this.arrcont = arrcont;
    }
    @NonNull
    @Override
    public RecyleAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.contact_card,parent,false);
        return new viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyleAdapter.viewholder holder, int position) {
        int k =position;
//            holder.contImg.setImageResource(arrcont.get(position).imgVal);
            holder.Nametxt.setText(arrcont.get(position).name);
            holder.Numbertxt.setText(arrcont.get(position).number);
            holder.contImg.setImageBitmap(arrcont.get(position).imgVal);
            holder.cont_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    holder.intentToIndiv.putExtra("name",arrcont.get(k).name);
//                    holder.intentToIndiv.putExtra("number",arrcont.get(k).number);
//                    holder.intentToIndiv.putExtra("imgval",arrcont.get(k).imgVal);
                    holder.intentToIndiv.putExtra("position", k);
//                    holder.intentToIndiv.putExtra("Contact List", arrcont);
//                    holder.intentToIndiv.putExtra("contact_img",arrcont.get(k).imgVal);


                   context.startActivity(holder.intentToIndiv);
                }
            });
            setAnimation(holder.itemView,position);


    }

    @Override
    public int getItemCount() {
        return arrcont.size();
    }




    public static class viewholder extends RecyclerView.ViewHolder {
        TextView Nametxt,Numbertxt;
        CardView cont_card;
        ImageView contImg;
//        OnItemClickListener listener;
        Intent intentToIndiv;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            this.Nametxt = (TextView) itemView.findViewById(R.id.Cont_Name);
            this.Numbertxt = (TextView) itemView.findViewById(R.id.Cont_Number);
            this.contImg = (ImageView) itemView.findViewById(R.id.cont_img);
            this.cont_card = (CardView) itemView.findViewById(R.id.cont_card);
//            this.listener = listner;
            this.intentToIndiv = new Intent(itemView.getContext(),IndividualList.class);




        }
    }


    private void setAnimation(View viewtoanimation,int position) {
        if (position > lastposition) {
            Animation slideIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewtoanimation.startAnimation(slideIn);
            lastposition=position;
        }
    }
}
