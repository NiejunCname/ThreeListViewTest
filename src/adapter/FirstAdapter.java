package adapter;

import java.util.List;

import model.Province;
import android.content.Context;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.threework.MainActivity;
import com.example.threework.R;

public class FirstAdapter extends BaseAdapter{
	private List<Province> listprovince;
	private Context context;
	private String provinceName;
	
	public int selectPostion=-1;
	public FirstAdapter(Context context,String provinceName) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.provinceName=provinceName;
	}

	/*private int selectPosition=-1;*/
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listprovince.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listprovince.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View covertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Province province =listprovince.get(position);
		if(covertView==null)
		{
			covertView=LayoutInflater.from(context).inflate(R.layout.listitem_layout, parent, false);
		}
		TextView textView=(TextView)covertView;
		String name=(String)getItem(position);
		textView.setText(name);
	 
		
		return covertView;
	}
}
