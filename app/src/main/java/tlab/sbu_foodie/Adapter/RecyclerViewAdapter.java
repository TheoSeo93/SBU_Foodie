package tlab.sbu_foodie.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tlab.sbu_foodie.MenuDisplayHandler.IconSelector;
import tlab.sbu_foodie.R;

import static tlab.sbu_foodie.VenueNames.EAST_DINE;
import static tlab.sbu_foodie.VenueNames.WEST_SIDE;

/**
 * @author Theo Seo
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private IconSelector iconSelector = new IconSelector();
    private ArrayList<RowElement> rowElementArrayList = new ArrayList<>();

    private String name;


    private Context mContext;

    /**
     * View holder class in a recycler view adapter.
     * This describes an item view and metadata about its place within the recycler view.
     * View holder items are going to be determined according to the customer types.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText;
        private TextView priceText;
        private TextView comboTitleText;
        private TextView combo;
        private ImageView iconView;

        /**
         * Constructor of view holder.
         *
         * @param itemView The view which will hold each student's information with text views.
         */
        public ViewHolder(View itemView) {

            super(itemView);
            iconView = itemView.findViewById(R.id.icon);
            switch (name) {
                case WEST_SIDE:
                    nameText = itemView.findViewById(R.id.menu);
                    break;
                case EAST_DINE:
                    nameText = itemView.findViewById(R.id.menu);
                    break;
                default:
                    nameText = itemView.findViewById(R.id.menu);
                    priceText = itemView.findViewById(R.id.price);
                    combo = itemView.findViewById(R.id.combo);
                    comboTitleText = itemView.findViewById(R.id.comboTitle);
                    break;

            }


        }
    }

    public RecyclerViewAdapter(Context context, ArrayList<RowElement> rowElementArrayList, String name) {
        this.rowElementArrayList = rowElementArrayList;
        this.name = name;
        mContext = context;

    }


    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v;
        switch (name) {
            case WEST_SIDE:
                v = inflater.inflate(R.layout.row_without_price, parent, false);
                break;
            case EAST_DINE:
                v = inflater.inflate(R.layout.row_without_price, parent, false);
                break;
            default:
                v = inflater.inflate(R.layout.row, parent, false);
                break;

        }

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder viewHolder, int position) {

        TextView menuName = viewHolder.nameText;
        TextView comboTitle = new TextView(mContext);
        TextView price = new TextView(mContext);
        TextView combo = new TextView(mContext);
        ImageView icon = viewHolder.iconView;
        switch (name) {
            case WEST_SIDE:

                break;
            default:
                price = viewHolder.priceText;
                combo = viewHolder.combo;
                comboTitle = viewHolder.comboTitleText;
                break;
        }


        switch (name) {
            case WEST_SIDE:
                if (rowElementArrayList.get(position) != null) {
                    menuName.setText(rowElementArrayList.get(position).getName());
                    menuName.setTextColor(Color.BLACK);
                }
                break;

            case EAST_DINE:
                if (rowElementArrayList.get(position) != null) {
                    menuName.setText(rowElementArrayList.get(position).getName());
                    menuName.setTextColor(Color.BLACK);
                }
                break;

            default:
                if (rowElementArrayList.get(position) != null) {
                    if (rowElementArrayList.get(position).isCombo()) {
                        String title = "Together";
                        combo.setText(rowElementArrayList.get(position).getComboMenu());
                        comboTitle.setText(title);
                        comboTitle.setTextColor(Color.parseColor("#FF4081"));
                        comboTitle.setTypeface(combo.getTypeface(), Typeface.BOLD);
                        menuName.setText(rowElementArrayList.get(position).getName());
                        combo.setTextColor(Color.BLACK);

                    } else {

                        menuName.setText(rowElementArrayList.get(position).getName());
                        combo.setHeight(0);
                        comboTitle.setHeight(0);
                    }
                    price.setText(rowElementArrayList.get(position).getPrice()+"$");

                    menuName.setTextColor(Color.BLACK);
                    price.setTextColor(Color.BLACK);
                }
                break;
        }
        int iconId = iconSelector.getIcon(rowElementArrayList.get(position).getName());
        if (iconId != -100000)
            icon.setImageResource(iconId);

    }


    @Override
    public int getItemCount() {

        return rowElementArrayList.size();
    }


    /**
     * Returns the view type of the item at position for the purposes of view recycling.
     *
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
