package tlab.sbu_foodie.UIComponent;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tlab.sbu_foodie.Activities.MenuActivity;
import tlab.sbu_foodie.DataHandler.ExtendedDataHolder;
import tlab.sbu_foodie.R;

import static tlab.sbu_foodie.VenueNames.EAST_COCINA;
import static tlab.sbu_foodie.VenueNames.EAST_DELI;
import static tlab.sbu_foodie.VenueNames.EAST_DINE;
import static tlab.sbu_foodie.VenueNames.EAST_GRIL;
import static tlab.sbu_foodie.VenueNames.EAST_ITALIAN;

/**
 * Created by ilsung on 5/30/2018.
 */

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private Point screenPosition;
    private StringBuilder menuRead;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.bottomsheet, container, false);
        View eastDine = root.findViewById(R.id.east_dine);
        View eastCocina = root.findViewById(R.id.east_cocina);
        View eastDeli = root.findViewById(R.id.east_deli);
        View eastGril = root.findViewById(R.id.east_grill);
        View eastItalian = root.findViewById(R.id.east_italian);
        eastDine.setOnClickListener(view -> {
            presentActivity(screenPosition, EAST_DINE);
            dismiss();
        });
        eastCocina.setOnClickListener(view -> {
            presentActivity(screenPosition, EAST_COCINA);
            dismiss();
        });

        eastDeli.setOnClickListener(view -> {
            presentActivity(screenPosition, EAST_DELI);
            dismiss();
        });
        eastGril.setOnClickListener(view -> {
            presentActivity(screenPosition, EAST_GRIL);
            dismiss();
        });
        eastItalian.setOnClickListener(view -> {
            presentActivity(screenPosition, EAST_ITALIAN);
            dismiss();
        });

        return root;

    }

    public void setScreenPosition(Point screenPosition) {
        this.screenPosition = screenPosition;
    }

    public void setMenuRead(StringBuilder menuRead) {
        this.menuRead = menuRead;
    }
}
