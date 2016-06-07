package julio.puc.br.oltinder.Model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import julio.puc.br.oltinder.R;

/**
 * Created by julio on 07/06/2016.
 */
public class AllProductsAdapter extends BaseAdapter {
    private Products produto;
    private Context context;

    private ImageView imageViewProduct;
    private TextView txtName;
    private Button btnLike;
    private Button btnDont;


    public AllProductsAdapter(Products produto, Context context) {
        this.produto = produto;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.fragment_products,  null);

        imageViewProduct = (ImageView) view.findViewById(R.id.img_list_products);
        txtName = (TextView)view.findViewById(R.id.txtNameProduct);
        btnLike = (Button) view.findViewById(R.id.btnLike);
        btnDont = (Button) view.findViewById(R.id.btnDont);
        return view;
    }
}
