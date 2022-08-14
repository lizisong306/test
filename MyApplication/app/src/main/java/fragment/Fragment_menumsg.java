package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.maidiantech.BackHandledFragment;
import com.maidiantech.R;

/**
 * Created by 13520 on 2016/9/22.
 */
public class Fragment_menumsg extends BackHandledFragment {
    private boolean hadIntercept;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.my_fragment_msg,null);
        return view;
    }
    @Override
    protected boolean onBackPressed() {
        if(hadIntercept){
            return false;
        }else{
            Toast.makeText(getActivity(), "Click From MyFragment", Toast.LENGTH_SHORT).show();
            hadIntercept = true;
            return true;
        }
    }
}
