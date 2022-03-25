package com.example.whackamoleappkushaall;

import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.whackamoleappkushaall.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    static AtomicInteger score;
    static AtomicInteger time;
    private ActivityMainBinding binding;
    static List<ImageView> moleList;
    static List<Boolean> clickableList;
    static List<Boolean> clickedList;
    static List<String> appearList;
    ScaleAnimation appear;
    ScaleAnimation disappear;
    int secretTime = 0;
    static final String scoreInfo = "Kushaal";
    static final String timeInfo = "Latupalli";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        time = new AtomicInteger(60);
        score = new AtomicInteger(0);
        moleList = new ArrayList<ImageView>(Arrays.asList(binding.mole1,binding.mole2,binding.mole3,binding.mole4,binding.mole5,binding.mole6,binding.mole7,binding.mole8,binding.mole9));
        clickableList = new ArrayList<Boolean>(Arrays.asList(false,false,false,false,false,false,false,false,false));
        appearList = new ArrayList<String>(Arrays.asList("","","","","","","","",""));
        clickedList = new ArrayList<Boolean>(Arrays.asList(false,false,false,false,false,false,false,false,false));
        appear = new ScaleAnimation(0,1f,0f,1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        appear.setDuration(750);
        disappear = new ScaleAnimation(1f,0,1,0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        disappear.setDuration(750);
        for(int x = 0;x<moleList.size();x++)
        {
            moleList.get(x).setVisibility(View.INVISIBLE);
            moleList.get(x).setOnClickListener(this);
            new mole(x).start();
        }
        new Timer().start();
    }
    @Override
    public void onClick(View view) {
        if(clickableList.get(moleList.indexOf(view))==true)
        {
            ImageView imageView = moleList.get(moleList.indexOf(view));
            clickableList.set(moleList.indexOf(view),false);
            clickedList.set(moleList.indexOf(view),true);
            if(appearList.get(moleList.indexOf(view)).equalsIgnoreCase("mole"))
            {
                Glide.with(MainActivity.this).asGif()
                        .load(R.drawable.molehit)
                        .listener(new RequestListener<GifDrawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                                resource.setLoopCount(0);
                                resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                    @Override
                                    public void onAnimationEnd(Drawable drawable) {
                                        super.onAnimationEnd(drawable);
                                        Visibility(moleList.indexOf(view),"Invisible" );
                                    }
                                });
                                return false;
                            }
                        })
                        .into(imageView);
                score.getAndAdd(100);
                binding.scoreTextView.setText("Score: "+score.get());
                ImageView image = new ImageView(this);
                image.setId(View.generateViewId());
                image.setImageResource(R.drawable.star);
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,1);
                image.setLayoutParams(params);
                binding.linearLayout.addView(image);
            }
            else if(appearList.get(moleList.indexOf(view)).equalsIgnoreCase("more"))
            {
                time.getAndAdd(10);
                moleList.get(moleList.indexOf(view)).startAnimation(disappear);
                disappear.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Visibility(moleList.indexOf(view),"Invisible");
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
            else if(appearList.get(moleList.indexOf(view)).equalsIgnoreCase("less"))
            {
                time.getAndAdd(-10);
                if(time.intValue()<0)
                    time.set(0);
                binding.timetextView.setText(time+" Seconds Left");
                moleList.get(moleList.indexOf(view)).startAnimation(disappear);
                disappear.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Visibility(moleList.indexOf(view),"Invisible");
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }

        }
    }
    public void Visibility(int loc, String vis)
    {
        if(vis.equalsIgnoreCase("Visible"))
            moleList.get(loc).setVisibility(View.VISIBLE);
        else if(vis.equalsIgnoreCase("Invisible"))
            moleList.get(loc).setVisibility(View.INVISIBLE);
    }

    public class Timer extends Thread{
        public void run()
        {
            while(time.intValue()>=0)
            {
                if(time.intValue()==0)
                {
                    Intent endGame = new Intent(MainActivity.this,MainActivity3.class);
                    endGame.putExtra(scoreInfo,Integer.toString(score.intValue()));
                    endGame.putExtra(timeInfo,Integer.toString(secretTime));
                    startActivity(endGame);
                }
                secretTime++;
                time.getAndDecrement();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                binding.timetextView.setText(time+" Seconds Left");
            }
        }
    }
    public class mole extends Thread
    {
        private int loc;
        public mole(int loc)
        {
            this.loc=loc;
        }
        public void run()
        {
            while(time.intValue()>=1)
            {
                int timeRandom = (int)(Math.random()+3000)+500;
                try {
                    TimeUnit.MILLISECONDS.sleep(timeRandom);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                clickedList.set(loc, false);
                int random = (int)(Math.random()*5)+1;
                //Mole Appear
                if(random==1)
                {
                    appearList.set(loc,"mole");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(MainActivity.this)
                                    .load(R.drawable.molespot)
                                    .into(moleList.get(loc));
                            Visibility(loc, "Visible");
//                            moleList.get(0).startAnimation(appear);
                            Glide.with(MainActivity.this).asGif()
                                    .load(R.drawable.moleappear)
                                    .listener(new RequestListener<GifDrawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                                            return false;
                                        }
                                        @Override
                                        public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                                            resource.setLoopCount(0);
                                            resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                                @Override
                                                public void onAnimationEnd(Drawable drawable) {
                                                    super.onAnimationEnd(drawable);
                                                    Glide.with(MainActivity.this)
                                                            .load(R.drawable.moleout)
                                                            .into(moleList.get(loc));
                                                    clickableList.set(loc, true);
                                                }
                                            });
                                            return false;
                                        }
                                    })
                                    .into(moleList.get(loc));
                        }
                    });
                    try {
                        TimeUnit.MILLISECONDS.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (clickedList.get(loc) == false)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(MainActivity.this).asGif()
                                        .load(R.drawable.moleleave)
                                        .listener(new RequestListener<GifDrawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {

                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                                                resource.setLoopCount(0);
                                                resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                                    @Override
                                                    public void onAnimationStart(Drawable drawable) {
                                                        super.onAnimationStart(drawable);
                                                        clickableList.set(loc, false);
                                                    }
                                                });
                                                resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                                    @Override
                                                    public void onAnimationEnd(Drawable drawable) {
                                                        super.onAnimationEnd(drawable);
                                                        Visibility(loc,"Invisible");
                                                    }
                                                });
                                                return false;
                                            }
                                        })
                                        .into(moleList.get(loc));
                            }
                        });
                    }
                }//if statement for random
                else if(random==3&&time.intValue()<51)
                {
                    random=(int)(Math.random()*2)+1;
                    //More Time
                    if(random==1)
                    {
                        appearList.set(loc,"more");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(MainActivity.this).asDrawable()
                                        .load(R.drawable.greenmushroom)
                                        //.fitCenter()
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                Visibility(loc,"Visible");
                                                moleList.get(loc).startAnimation(appear);
                                                clickableList.set(loc,true);
                                                appear.setAnimationListener(new Animation.AnimationListener() {
                                                    @Override
                                                    public void onAnimationStart(Animation animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animation animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animation animation) {

                                                    }
                                                });
                                                return false;
                                            }
                                        })
                                        .into(moleList.get(loc));
                            }
                        });
                        try {
                            TimeUnit.MILLISECONDS.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(clickedList.get(loc)==false)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    moleList.get(0).startAnimation(disappear);
                                    disappear.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {
                                            clickableList.set(loc,false);
                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            Visibility(loc,"Invisible");
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {
                                        }
                                    });
                                }
                            });
                        }
                    }
                    //Less Time
                    else if(random==2&&time.intValue()>51)
                    {
                        appearList.set(loc,"less");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(MainActivity.this).asDrawable()
                                        .load(R.drawable.poisonmushroom)
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                Visibility(loc,"Visible");
                                                moleList.get(loc).startAnimation(appear);
                                                appear.setAnimationListener(new Animation.AnimationListener() {
                                                    @Override
                                                    public void onAnimationStart(Animation animation) {
                                                        clickableList.set(loc,true);
                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animation animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animation animation) {

                                                    }
                                                });
                                                return false;
                                            }
                                        })
                                        .into(moleList.get(loc));
                            }
                        });
                        try {
                            TimeUnit.MILLISECONDS.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(clickedList.get(0)==false)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    moleList.get(loc).startAnimation(disappear);
                                    disappear.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {
                                            clickableList.set(loc,false);
                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            Visibility(loc,"Invisible");

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                }
                            });
                        }
                    }
                }
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}