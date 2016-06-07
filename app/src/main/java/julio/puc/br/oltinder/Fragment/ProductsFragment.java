package julio.puc.br.oltinder.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import julio.puc.br.oltinder.Controler.MainActivity;
import julio.puc.br.oltinder.Model.Products;
import julio.puc.br.oltinder.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment implements MyProduct.OnFragmentInteractionListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView imageViewProduct;
    private TextView txtName;
    private Button btnLike;
    private Button btnDont;

    private  View view;

    public static List<Products> productsListLike;

    private List<Products> productsList;

    private OnFragmentInteractionListener mListener;

    public ProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(MainActivity.TAG, "ProductsFragmente Create View ");
        productsListLike = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_products, container, false);

        Backendless.Persistence.of(Products.class).find(new AsyncCallback<BackendlessCollection<Products>>() {
            @Override
            public void handleResponse(BackendlessCollection<Products> response) {
                productsList = response.getData();
                //setElements(productsList.get(0),0);

                try {
                    URL url = new URL("https://api.backendless.com/978405B2-3D41-0989-FFCD-5110C26D2600/v1/files/"
                            + productsList.get(0).getImageLocation());
                    DonwloadFileTask donwloadFileTask = new DonwloadFileTask(0);
                    donwloadFileTask.execute(url);

                }catch (MalformedURLException e ){
                    e.printStackTrace();
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);


    }

    public void starDown(int position){

        if(position == productsList.size()) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Atenção !")
                    .setMessage("Não  existe mais produtos para você \n deseja rever os produtos ? ")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.myProducts = productsListLike;

                            MyProduct fragment  = new MyProduct();
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                            fragmentTransaction.commit();
                        }
                    })
                    .show();

        }else{
            try {
                URL url = new URL("https://api.backendless.com/978405B2-3D41-0989-FFCD-5110C26D2600/v1/files/"
                        + productsList.get(position).getImageLocation());
                DonwloadFileTask donwloadFileTask = new DonwloadFileTask(position);
                donwloadFileTask.execute(url);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setElements(final Products products, final int position, Bitmap bitmap){

        imageViewProduct = (ImageView) view.findViewById(R.id.img_list_products);
        txtName = (TextView)view.findViewById(R.id.txtNameProduct);
        btnLike = (Button) view.findViewById(R.id.btnLike);
        btnDont = (Button) view.findViewById(R.id.btnDont);

        txtName.setText(products.getProductname().toString());
        imageViewProduct.setImageBitmap(bitmap);


        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Gostou !")
                        .setMessage("Voce se interesou pelo produto '" + products.getProductname()+"', \n" +
                                "Deseja enviar email para o vendedor ' "+ products.getSellername()+"' ? ")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                productsListLike.add(products);
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Gostou")
                                        .setMessage("Email enviado de '"+Backendless.UserService.CurrentUser().getEmail().toString()+"'\n" +
                                                "Para : "+ products.getSellername())
                                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                starDown(position+1);
                                            }
                                        })
                                        .show();
                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                starDown(position+1);
                            }
                        })
                        .show();
            }

        });

        btnDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starDown(position+1);
            }
        });

    }

    private class DonwloadFileTask extends AsyncTask<URL,Void,Bitmap>{
        int  position;

        public DonwloadFileTask(int position) {
            this.position = position;
        }

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
            setElements(productsList.get(position),position,bitmap);
        }
    }

}
