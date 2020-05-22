package jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import java.util.List;

import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.controladors.ControladorPartida;
import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.activities.MainActivity;
import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.R;
import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.domini.Carta;

public class AdapterCartes extends RecyclerView.Adapter<AdapterCartes.myViewHolder> {

    private List<Carta> listParelles;
    private ControladorPartida controlador;
    private Carta currentCarta;
    private MainActivity mainActivity;
    private int counterRevertides = 0;
    private boolean cartaRevertida = false;

    public AdapterCartes(MainActivity mainActivity, ControladorPartida controlador, int counterRevertides, boolean cartaRevertida) {
        this.mainActivity = mainActivity;
        this.controlador = controlador;
        this.listParelles = controlador.getCartes();
        this.counterRevertides = counterRevertides;
        this.cartaRevertida = cartaRevertida;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mainActivity).inflate(R.layout.cardview_carta, parent, false);
        myViewHolder vh = new myViewHolder(v);
        return vh;
    }

    @Override
    public synchronized void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {
        currentCarta = listParelles.get(position);
        holder.imageCarta.setImageResource(listParelles.get(position).getImageBack());
        if(currentCarta.getFound()) holder.imageCarta.setEnabled(false); // ja s'ha trobat
        else {
            holder.imageCarta.setEnabled(true);

            if(currentCarta.getImageBack()!=R.drawable.cardback && cartaRevertida && counterRevertides != 1) {
                currentCarta.flipImagesCarta();
                holder.imageCarta.setImageResource(listParelles.get(position).getImageBack());
                cartaRevertida = false;
            }
        }

        holder.imageCarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counterRevertides<2) {
                    counterRevertides++;
                    view.setEnabled(false);
                    ObjectAnimator oa1 = ObjectAnimator.ofFloat(holder.imageCarta, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(holder.imageCarta, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            holder.imageCarta.setImageResource(listParelles.get(position).getImage());
                            oa2.start();

                            //llógica al tumbar una carta...
                            holder.imageCarta.setEnabled(false);
                            if (controlador.revertirCarta(listParelles.get(position))) { //retorna true si es la segona carta que es gira
                                if (controlador.checkCartesIguals()) {
                                    if (controlador.checkJocCompletat()) {
                                        mainActivity.jocFinalitzat();
                                    }
                                }
                            }
                        }
                    });
                    oa1.start();
                    if(counterRevertides==2) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.changeResultat();
                                counterRevertides = 0;
                            }
                        }, 1000); // espera a que s'acabi l'animació
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return listParelles.size();
    }


    public static class myViewHolder extends RecyclerView.ViewHolder{
        ImageView imageCarta;

        public myViewHolder(View itemView){
            super(itemView);

            imageCarta = itemView.findViewById(R.id.image_carta);
        }

        public ImageView getImageTopSecret(){
            return imageCarta;
        }
    }
}

