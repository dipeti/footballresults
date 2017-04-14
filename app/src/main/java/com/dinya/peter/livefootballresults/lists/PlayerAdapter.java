package com.dinya.peter.livefootballresults.lists;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.dinya.peter.livefootballresults.R;
import com.dinya.peter.livefootballresults.entity.Player;

import java.util.List;

public class PlayerAdapter extends ExpandableRecyclerAdapter<Player,Player.PlayerExtraDetail,PlayerAdapter.PlayerViewHolder,PlayerAdapter.PlayerDetailsViewHolder> {



    /**
     * Primary constructor. Sets up {@link #mParentList} and {@link #mFlatItemList}.
     * <p>
     * Any changes to {@link #mParentList} should be made on the original instance, and notified via
     * {@link #notifyParentInserted(int)}
     * {@link #notifyParentRemoved(int)}
     * {@link #notifyParentChanged(int)}
     * {@link #notifyParentRangeInserted(int, int)}
     * {@link #notifyChildInserted(int, int)}
     * {@link #notifyChildRemoved(int, int)}
     * {@link #notifyChildChanged(int, int)}
     * methods and not the notify methods of RecyclerView.Adapter.
     *
     * @param parentList List of all parents to be displayed in the RecyclerView that this
     *                   adapter is linked to
     */
    public PlayerAdapter(@NonNull List<Player> parentList) {
        super(parentList);
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = LayoutInflater.from(parentViewGroup.getContext()).inflate(R.layout.player_list_item,parentViewGroup,false);
        return new PlayerViewHolder(view);

    }

    @NonNull
    @Override
    public PlayerDetailsViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view = LayoutInflater.from(childViewGroup.getContext()).inflate(R.layout.player_details_list_item,childViewGroup,false);
        return new PlayerDetailsViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull PlayerViewHolder parentViewHolder, int parentPosition, @NonNull Player parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull PlayerDetailsViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull Player.PlayerExtraDetail child) {
        childViewHolder.bind(child);
    }

    /**
     * PlayerViewHolder
     */
    class PlayerViewHolder extends ParentViewHolder {

        TextView tvJersey;
        TextView tvName;
        TextView tvPosition;

        PlayerViewHolder(View itemView) {
            super(itemView);
            tvJersey = (TextView) itemView.findViewById(R.id.player_item_jersey);
            tvName = (TextView) itemView.findViewById(R.id.player_item_name);
            tvPosition = (TextView) itemView.findViewById(R.id.player_item_position);
        }

        void bind(Player player) {
            tvName.setText(player.getName());
            if (player.getJerseyNumber()==Integer.MAX_VALUE){
                tvJersey.setText("N/A");
            }else{
                tvJersey.setText(String.valueOf(player.getJerseyNumber()));
            }

            tvPosition.setText(player.getPosition());
        }
    }

    /**
     * PlayerDetailsViewHolder
     */
    class PlayerDetailsViewHolder extends ChildViewHolder {

        TextView tvMarketValue;
        TextView tvContractUntil;
        TextView tvDateOfBirth;
        TextView tvNationality;

        PlayerDetailsViewHolder(View itemView) {
            super(itemView);
            tvMarketValue = (TextView) itemView.findViewById(R.id.player_detail_item_market_value);
            tvContractUntil = (TextView) itemView.findViewById(R.id.player_detail_item_contract);
            tvDateOfBirth = (TextView) itemView.findViewById(R.id.player_detail_item_birth);
            tvNationality = (TextView) itemView.findViewById(R.id.player_detail_item_nationality);
        }

        void bind(Player.PlayerExtraDetail playerExtraDetail) {
            tvMarketValue.setText(playerExtraDetail.getMarketValue());
            tvContractUntil.setText(playerExtraDetail.getFormattedContractUntil());
            tvDateOfBirth.setText(playerExtraDetail.getFormattedDateOfBirth());
            tvNationality.setText(playerExtraDetail.getNationality());
        }
    }
}


