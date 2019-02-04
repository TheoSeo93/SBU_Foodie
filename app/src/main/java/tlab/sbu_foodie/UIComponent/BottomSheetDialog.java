package tlab.sbu_foodie.UIComponent;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.ButterKnife;
import tlab.sbu_foodie.Activities.MenuActivity;
import tlab.sbu_foodie.DataHandler.ExtendedDataHolder;
import tlab.sbu_foodie.R;

import static tlab.sbu_foodie.VenueNames.EAST_COCINA;
import static tlab.sbu_foodie.VenueNames.EAST_DELI;
import static tlab.sbu_foodie.VenueNames.EAST_DINE;
import static tlab.sbu_foodie.VenueNames.EAST_GRIL;
import static tlab.sbu_foodie.VenueNames.EAST_HALAL;
import static tlab.sbu_foodie.VenueNames.EAST_ISLAND;
import static tlab.sbu_foodie.VenueNames.EAST_ITALIAN;
import static tlab.sbu_foodie.VenueNames.EAST_KOSHER;

/**
 * Created by ilsung on 5/30/2018.
 */

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private Point screenPosition;
    private StringBuilder menuRead;
    private String[] names = {EAST_COCINA, EAST_DELI, EAST_DINE, EAST_GRIL, EAST_ISLAND, EAST_ITALIAN, EAST_KOSHER, EAST_HALAL};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void presentActivity(Point v, String title) {
        View view = new View(getContext());
        view.setX(v.x);
        view.setY(v.y);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);
        ExtendedDataHolder extras = ExtendedDataHolder.getInstance();
        extras.putExtra("extra", menuRead.toString());
        Intent intent = new Intent(getContext(), MenuActivity.class);
        intent.putExtra(MenuActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(MenuActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        intent.putExtra(MenuActivity.venue, title);
        ActivityCompat.startActivity(getContext(), intent, options.toBundle());
        getActivity().finish();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bottomsheet, container, false);
        root.offsetTopAndBottom(16);
        root.offsetLeftAndRight(16);
        ButterKnife.bind(this, root);
        HashMap<String, Double> rateMap = (HashMap) ExtendedDataHolder.getInstance().getExtra("RATE");
        for (int i = 0; i < names.length; i++) {
            AtomicInteger index = new AtomicInteger(i);
            final String name = names[index.get()];
            final View item = LayoutInflater.from(getContext()).inflate(R.layout.bottomsheet_item, container);
            TextView title = item.findViewById(R.id.east_title);
            TextView desc = item.findViewById(R.id.east_desc);
            String append = "\t" + "★ " + rateMap.get(name);
            Typeface font = Typeface.createFromAsset(this.getContext().getAssets(), "ubuntu.ttf");
            title.setText(name + append);
            title.setTextColor(getResources().getColor(R.color.black));
            title.setTypeface(font, Typeface.BOLD);

            if (name.equals(EAST_COCINA)) {
                desc.setText(getResources().getString(R.string.cocina_desc));
            } else if (name.equals(EAST_DELI)) {
                desc.setText(getResources().getString(R.string.deli_desc));
            } else if (name.equals(EAST_DINE)) {
                desc.setText(getResources().getString(R.string.dine_desc));
            } else if (name.equals(EAST_GRIL)) {
                desc.setText(getResources().getString(R.string.gril_desc));
            } else if (name.equals(EAST_HALAL)) {
                desc.setText(getResources().getString(R.string.hala_desc));
            } else if (name.equals(EAST_ISLAND)) {
                desc.setText(getResources().getString(R.string.island_desc));
            } else if (name.equals(EAST_ITALIAN)) {
                desc.setText(getResources().getString(R.string.italian_desc));
            } else {
                desc.setText(getResources().getString(R.string.kosher_desc));
            }
            desc.setTypeface(font);
            ((LinearLayout) root).addView(item);
        }

        for(int i=0;i< ((LinearLayout)root).getChildCount();i++){

            final int index = i;
            ((LinearLayout)root).getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presentActivity(screenPosition,names[index]);
                    dismiss();
                }
            });
        }

        return root;

}

    public void setScreenPosition(Point screenPosition) {
        this.screenPosition = screenPosition;
    }

    public void setMenuRead(StringBuilder menuRead) {
        this.menuRead = menuRead;
    }
}
