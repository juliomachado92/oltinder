package julio.puc.br.oltinder.Model;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import julio.puc.br.oltinder.Controler.MainActivity;
import julio.puc.br.oltinder.R;

/**
 * Created by julio on 02/06/2016.
 */
public class ProductAdapter extends BaseAdapter{
    private List<Products> productsList;
    private Context context;

    private  TextView nameProduct;
    private TextView descriptionProduct;
    private ImageView imgProduct;

    private View view;
    public ProductAdapter(List<Products> productsList, Context context) {
        this.productsList = productsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return productsList.size();
    }

    @Override
    public Object getItem(int position) {
        return productsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convert, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.list_product, null);


        nameProduct = (TextView)view.findViewById(R.id.txtListNameProduct);
        descriptionProduct = (TextView)view.findViewById(R.id.txtListDescriptionProduct);
        imgProduct = (ImageView)view.findViewById(R.id.imgListProduct);

        nameProduct.setText(productsList.get(position).getProductname());
        descriptionProduct.setText(productsList.get(position).getDescricao());

        //get photo in backendless
        try {
            URL url = new URL("https://api.backendless.com/978405B2-3D41-0989-FFCD-5110C26D2600/v1/files/"+ productsList.get(position).getImageLocation());
            DonwloadFileTask donwloadFileTask = new DonwloadFileTask();
            donwloadFileTask.execute(url);

        }catch (MalformedURLException e ){
            e.printStackTrace();
        }


        return view;
    }

    private void displayFile(Bitmap bitmap){
//        Log.i(MainActivity.TAG,"AlertDialog para mostrar foto");
//        AlertDialog.Builder imageDialog = new AlertDialog.Builder(context);
//        imageDialog.setMessage("Produto :" + nameProduct.getText().toString());
//
//        imageDialog.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//
//        ImageView imageView = new ImageView(context);
//        imageView.setImageBitmap(bitmap);
//
//        imageDialog.setView(imageView);
//        imageDialog.create();
//        imageDialog.show();

        imgProduct.setImageBitmap(bitmap);


    }


    //fazer download de arquivos de photos

    private class DonwloadFileTask extends AsyncTask<URL,Void,Bitmap>{
        @Override
        protected Bitmap doInBackground(URL... params) {
            //Log.i(MainActivity.TAG,"Background");
           for(URL url : params){
               try{
                   HttpURLConnection http = (HttpURLConnection)url.openConnection();
                   int response  = http.getResponseCode();
                   if(response == HttpURLConnection.HTTP_OK){
                       InputStream inputStream = http.getInputStream();
                       Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
                       inputStream.close();
                       return imageBitmap;

                   }
               }catch (IOException e){
                   e.printStackTrace();
               }
           }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //Log.i(MainActivity.TAG,"Chamada de metodo para mostrar foto");
          displayFile(bitmap);

        }
    }

}
