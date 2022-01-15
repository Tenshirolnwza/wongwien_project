package com.example.wongwien.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wongwien.OnSwipeTouchListener;
import com.example.wongwien.R;
import com.example.wongwien.ReviewDetailActivity;
import com.example.wongwien.model.ModelReview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdapterReview extends RecyclerView.Adapter<AdapterReview.Myholder> {
    public static final int PATTERN_REVIEW_1 = 0;
    public static final int PATTERN_REVIEW_2 = 1;
    public static final int PATTERN_REVIEW_3 = 2;
    private static final String TAG = "AdapterReview";
    Context context;
    ArrayList<ModelReview> reviews;

    FirebaseUser user;
    String myUid;

    int star, starBeforeChange;
    boolean isUseReview = false;
    int point;


    public AdapterReview(Context context, ArrayList<ModelReview> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        checkUserStatus();
        switch (viewType) {
            case PATTERN_REVIEW_1:
                View view = LayoutInflater.from(context).inflate(R.layout.row_review_pattern1, parent, false);
                return new Myholder(view);
            case PATTERN_REVIEW_2:
                view = LayoutInflater.from(context).inflate(R.layout.row_review_pattern2, parent, false);
                return new Myholder(view);
            case PATTERN_REVIEW_3:
                view = LayoutInflater.from(context).inflate(R.layout.row_review_pattern3, parent, false);
                return new Myholder(view);
            default:
                view = LayoutInflater.from(context).inflate(R.layout.row_review_pattern1, parent, false);
                return new Myholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        loadAllStarToReviews(reviews.get(position).getrId(), myUid, holder);

        String timeStamp = reviews.get(position).getR_timeStamp();
        String title = reviews.get(position).getR_title();
        String point = reviews.get(position).getR_point();

        //conver time stamp to dd/mm/yyyy hh:mm am/pm
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = (String) DateFormat.format("dd/MM/yyyy hh:mm:aa", cal);

        holder.rTitile.setText(title);
        holder.txtPoint.setText(point);

        int num = Integer.parseInt(reviews.get(position).getR_num());
//        holder.rDesc0.setText(reviews.get(position).getR_desc0());

        switch (num) {
            case 0:
                holder.rDesc0.setText(reviews.get(position).getR_desc0());
                break;
            case 1:
                try {
                    Picasso.get().load(reviews.get(position).getR_image0()).into(holder.r_image0);
                    holder.rDesc0.setText(reviews.get(position).getR_desc0());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                if(getItemViewType(position)==PATTERN_REVIEW_3){
                    try {
                        Picasso.get().load(reviews.get(position).getR_image0()).into(holder.r_image0);
                        holder.rDesc0.setText(reviews.get(position).getR_desc0());

                        Picasso.get().load(reviews.get(position).getR_image1()).into(holder.r_image1);
                        holder.rDesc1.setText(reviews.get(position).getR_desc1());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 3:
                if(getItemViewType(position)==PATTERN_REVIEW_3) {
                    holder.cover02.setVisibility(View.VISIBLE);
                    try {
                        Picasso.get().load(reviews.get(position).getR_image0()).into(holder.r_image0);
                        holder.rDesc0.setText(reviews.get(position).getR_desc0());

                        Picasso.get().load(reviews.get(position).getR_image1()).into(holder.r_image1);
                        holder.rDesc1.setText(reviews.get(position).getR_desc1());

                        Picasso.get().load(reviews.get(position).getR_image2()).into(holder.r_image2);
                        holder.rDesc2.setText(reviews.get(position).getR_desc2());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 4:
                if(getItemViewType(position)==PATTERN_REVIEW_3) {
                    holder.cover02.setVisibility(View.VISIBLE);
                    holder.cover03.setVisibility(View.VISIBLE);
                    try {
                        Picasso.get().load(reviews.get(position).getR_image0()).into(holder.r_image0);
                        holder.rDesc0.setText(reviews.get(position).getR_desc0());

                        Picasso.get().load(reviews.get(position).getR_image1()).into(holder.r_image1);
                        holder.rDesc1.setText(reviews.get(position).getR_desc1());

                        Picasso.get().load(reviews.get(position).getR_image2()).into(holder.r_image2);
                        holder.rDesc2.setText(reviews.get(position).getR_desc2());

                        Picasso.get().load(reviews.get(position).getR_image3()).into(holder.r_image3);
                        holder.rDesc3.setText(reviews.get(position).getR_desc3());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                holder.rDesc0.setText(reviews.get(position).getR_desc0());
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReviewDetail(position);
            }
        });

        if(getItemViewType(position)==PATTERN_REVIEW_3){

            holder.cover00.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToReviewDetail(position);
                }
            });
            holder.cover01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToReviewDetail(position);
                }
            });

            holder.cover02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToReviewDetail(position);
                }
            });

            holder.cover03.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToReviewDetail(position);
                }
            });
        }

        holder.score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_review_score, null, false);

                checkPaticipation(reviews.get(position).getrId(), myUid, view);
                builder.setView(view);

                AlertDialog show = builder.show();

                ImageView star1 = view.findViewById(R.id.star1);
                ImageView star2 = view.findViewById(R.id.star2);
                ImageView star3 = view.findViewById(R.id.star3);
                ImageView star4 = view.findViewById(R.id.star4);
                ImageView star5 = view.findViewById(R.id.star5);


                star1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        star = 1;
                        checkStatusStar(view);
                    }
                });
                star2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        star = 2;
                        checkStatusStar(view);
                    }
                });
                star3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        star = 3;
                        checkStatusStar(view);
                    }
                });
                star4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        star = 4;
                        checkStatusStar(view);
                    }
                });
                star5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        star = 5;
                        checkStatusStar(view);
                    }
                });

                Button btnSend = view.findViewById(R.id.btnSend);
                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("RParticipations").child(reviews.get(position).getrId());
                        ref.child(myUid).setValue(String.valueOf(star));

                        calculateScore(reviews.get(position), star, holder);

                        holder.starScore.setImageResource(R.drawable.ic_star_primary);

                        show.dismiss();
                    }
                });

            }
        });


//        holder.itemView.setOnTouchListener(new OnSwipeTouchListener(context) {
//            @Override
//            public void onSwipeRight() {
//                super.onSwipeRight();
//            }
//
//            @Override
//            public void onSwipeLeft() {
//                super.onSwipeLeft();
//            }
//
//            @Override
//            public void onClick() {
//                Intent intent = new Intent(context, ReviewDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("list", reviews.get(position));
//
//                intent.putExtra("bundle", bundle);
//                context.startActivity(intent);
//                super.onClick();
//            }
//        });
//
    }

    private void goToReviewDetail(int position){
        Intent intent = new Intent(context, ReviewDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("list", reviews.get(position));

        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    private void calculateScore(ModelReview review, int star, Myholder holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Reviews");
        Query q = ref.orderByChild("rId").equalTo(review.getrId());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    ModelReview model = d.getValue(ModelReview.class);
                    point = Integer.parseInt(model.getR_point());


                    Log.d(TAG, "calculateScore: star::" + star);
                    Log.d(TAG, "calculateScore: beforechange::" + starBeforeChange);
                    Log.d(TAG, "calculateScore: point::" + point);
                    Log.d(TAG, "calculateScore: isUseReview::" + isUseReview);

                    if (isUseReview) {
                        point = point + (star - starBeforeChange);
                    } else {
                        point = point + star;
                    }
                    int point2 = point;

                    Log.d(TAG, "calculateScore: point afterchange::" + point);
                    Log.d(TAG, "calculateScore:***********************");

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Reviews");
                    Query q = ref.orderByChild("rId").equalTo(review.getrId());
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot d : snapshot.getChildren()) {
                                ModelReview model = d.getValue(ModelReview.class);
                                if (model.getrId().equals(review.getrId())) {
                                    d.getRef().child("r_point").setValue(String.valueOf(point2));
                                    changeShowTextScore(holder, point2);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void changeShowTextScore(Myholder holder, int point) {
        holder.txtPoint.setText(String.valueOf(point));
    }

    private void checkStatusStar(View view) {
        ImageView star1 = view.findViewById(R.id.star1);
        ImageView star2 = view.findViewById(R.id.star2);
        ImageView star3 = view.findViewById(R.id.star3);
        ImageView star4 = view.findViewById(R.id.star4);
        ImageView star5 = view.findViewById(R.id.star5);

        switch (star) {
            case 1:
                star1.setImageResource(R.drawable.ic_star_white);
                star2.setImageResource(R.drawable.ic_star_empty);
                star3.setImageResource(R.drawable.ic_star_empty);
                star4.setImageResource(R.drawable.ic_star_empty);
                star5.setImageResource(R.drawable.ic_star_empty);
                break;
            case 2:
                star1.setImageResource(R.drawable.ic_star_white);
                star2.setImageResource(R.drawable.ic_star_white);
                star3.setImageResource(R.drawable.ic_star_empty);
                star4.setImageResource(R.drawable.ic_star_empty);
                star5.setImageResource(R.drawable.ic_star_empty);
                break;
            case 3:
                star1.setImageResource(R.drawable.ic_star_white);
                star2.setImageResource(R.drawable.ic_star_white);
                star3.setImageResource(R.drawable.ic_star_white);
                star4.setImageResource(R.drawable.ic_star_empty);
                star5.setImageResource(R.drawable.ic_star_empty);
                break;
            case 4:
                star1.setImageResource(R.drawable.ic_star_white);
                star2.setImageResource(R.drawable.ic_star_white);
                star3.setImageResource(R.drawable.ic_star_white);
                star4.setImageResource(R.drawable.ic_star_white);
                star5.setImageResource(R.drawable.ic_star_empty);
                break;
            case 5:
                star1.setImageResource(R.drawable.ic_star_white);
                star2.setImageResource(R.drawable.ic_star_white);
                star3.setImageResource(R.drawable.ic_star_white);
                star4.setImageResource(R.drawable.ic_star_white);
                star5.setImageResource(R.drawable.ic_star_white);
                break;
            default:
                star1.setImageResource(R.drawable.ic_star_empty);
                star2.setImageResource(R.drawable.ic_star_empty);
                star3.setImageResource(R.drawable.ic_star_empty);
                star4.setImageResource(R.drawable.ic_star_empty);
                star5.setImageResource(R.drawable.ic_star_empty);
                break;
        }
    }

    private void loadAllStarToReviews(String rId, String myUid, Myholder holder) {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("RParticipations").child(rId).child(myUid);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String num = snapshot.getValue(String.class);
                    if (num != null) {
                        star = Integer.parseInt(num);
                        holder.starScore.setImageResource(R.drawable.ic_star_primary);
                    } else {
                        star = 0;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: notfound");
                }
            });
        } catch (Exception e) {
            star = 0;
        }
    }

    private void checkPaticipation(String rId, String myUid, View view) {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("RParticipations").child(rId).child(myUid);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String num = snapshot.getValue(String.class);
                    if (num != null) {
                        isUseReview = true;
                        starBeforeChange = Integer.parseInt(num);
                        star = Integer.parseInt(num);
                        checkStatusStar(view);
                        Log.d(TAG, "onDataChange::" + rId + ":: star:::" + star);
                    } else {
                        star = 0;
                        isUseReview = false;
                        Log.d(TAG, "onDataChange::" + rId + ":: star:::" + star);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: notfound");
                }
            });
        } catch (Exception e) {
            star = 0;
            isUseReview = false;
            starBeforeChange = 0;
            checkStatusStar(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (reviews.get(position).getR_type()) {
            case "pattern1":
                return PATTERN_REVIEW_1;
            case "pattern2":
                return PATTERN_REVIEW_2;
            case "pattern3":
                return PATTERN_REVIEW_3;
            default:
                return PATTERN_REVIEW_1;
        }
    }

    private void checkUserStatus() {
        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            myUid = user.getUid();
        }
    }


    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class Myholder extends RecyclerView.ViewHolder {
        private TextView rTitile, rDesc0, rDesc1, rDesc2, rDesc3, txtPoint;
        private ImageView r_image0, r_image1, r_image2, r_image3, starScore;
        private LinearLayout score;
        private RelativeLayout cover00, cover01, cover02, cover03;
        private HorizontalScrollView hsv;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            rTitile = itemView.findViewById(R.id.r_title);
            txtPoint = itemView.findViewById(R.id.txtPoint);
            score = itemView.findViewById(R.id.score);
            starScore = itemView.findViewById(R.id.starScore);
            rDesc0 = itemView.findViewById(R.id.r_desc0);


            try{
                rDesc0 = itemView.findViewById(R.id.r_desc0);
                r_image0 = itemView.findViewById(R.id.r_image0);
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                hsv = itemView.findViewById(R.id.hsv);

                cover00 = itemView.findViewById(R.id.cover00);
                cover01 = itemView.findViewById(R.id.cover01);
                cover02 = itemView.findViewById(R.id.cover02);
                cover03 = itemView.findViewById(R.id.cover03);

                rDesc0 = itemView.findViewById(R.id.r_desc0);
                rDesc1 = itemView.findViewById(R.id.r_desc1);
                rDesc2 = itemView.findViewById(R.id.r_desc2);
                rDesc3 = itemView.findViewById(R.id.r_desc3);
                r_image0 = itemView.findViewById(R.id.r_image0);
                r_image1 = itemView.findViewById(R.id.r_image1);
                r_image2 = itemView.findViewById(R.id.r_image2);
                r_image3 = itemView.findViewById(R.id.r_image3);
                hsv = itemView.findViewById(R.id.hsv);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
