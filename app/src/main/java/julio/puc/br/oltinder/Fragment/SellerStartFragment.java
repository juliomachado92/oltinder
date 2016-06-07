package julio.puc.br.oltinder.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.persistence.BackendlessDataQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import julio.puc.br.oltinder.Controler.MainActivity;
import julio.puc.br.oltinder.Model.Constants;
import julio.puc.br.oltinder.Model.ProductAdapter;
import julio.puc.br.oltinder.Model.Products;
import julio.puc.br.oltinder.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SellerStartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SellerStartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerStartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences preferences;
    private BackendlessUser currentUser;
    //elementos da tela
    private TextView  txtNameSeller;
    private Button btnAdd;

    private EditText extNameProduct;
    private EditText extDescription;
    private Button btnAddPhoto;
    private ImageView imgPhoto;
    private  View dialogView;

    private ListView listViewProducts;
    private ArrayList<String> productList;
   // private ArrayAdapter<String>productsAdapter;
    private Bitmap imageBitmap;
    private String  imageDirectory = "productsPics";
    private ProductAdapter productsAdapter;
    private boolean takePhoto = false;

    private List<Products> productsListExist;

    private OnFragmentInteractionListener mListener;

    static final int REQUEST_IMAGE_CAPTURE = 1;


    public SellerStartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerStartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerStartFragment newInstance(String param1, String param2) {
        SellerStartFragment fragment = new SellerStartFragment();
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
        preferences = getActivity().getSharedPreferences(getString(R.string.prev_title), Context.MODE_PRIVATE);
        currentUser = Backendless.UserService.CurrentUser();
        productsListExist = new ArrayList<>();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.i(MainActivity.TAG, "SellerStartFragmente Create View ");
        View view = inflater.inflate(R.layout.fragment_seller_start, container, false);

        txtNameSeller = (TextView)view.findViewById(R.id.txtNameSeller);
        txtNameSeller.setText(preferences.getString(getString(R.string.prev_user_name),"NULL"));

        listViewProducts = (ListView) view.findViewById(R.id.listViewProducts);
        productList = new ArrayList<>();
        //productsAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,productList);



        btnAdd = (Button)view.findViewById(R.id.add_product);
        //adicionar novo produto
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Backendless.initApp(getContext(), Constants.APP_ID,Constants.SECRET_KEY, Constants.APP_VERSION);
                //create dialog add product
                LayoutInflater inflater = getActivity().getLayoutInflater();
                dialogView  = inflater.inflate(R.layout.dialog_add_product,null);

                extNameProduct = (EditText) dialogView.findViewById(R.id.etxNameProduct);
                extDescription = (EditText) dialogView.findViewById(R.id.etxtDescriProduct);
                imgPhoto = (ImageView) dialogView.findViewById(R.id.imgPhoto);
                btnAddPhoto = (Button)dialogView.findViewById(R.id.btnAddPhoto);
                btnAddPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //capture photo
                        Log.i(MainActivity.TAG,"TIRAR FOTO ");
                        dispatchTakePictureIntent();

                    }
                });

                final AlertDialog add = new AlertDialog.Builder(getContext())
                        .setTitle("Title")
                        .setView(dialogView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveProduct();
                                productsAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create();

                add.show();
            }
        });


        //PEgar todos elementos ddo tipo Products
//        Backendless.Persistence.of(Products.class).find(new AsyncCallback<BackendlessCollection<Products>>() {
//            @Override
//            public void handleResponse(BackendlessCollection<Products> response) {
//                Log.i(MainActivity.TAG,"SIZE " + response.getTotalObjects());
//                Toast.makeText(getContext(),"SIZE"+ response.getTotalObjects(),Toast.LENGTH_SHORT).show();
//
//                productsAdapter = new ProductAdapter(response.getData(),getContext());
//                listViewProducts.setAdapter(productsAdapter);
//
//            }
//
//            @Override
//            public void handleFault(BackendlessFault backendlessFault) {
//                Log.e(MainActivity.TAG,"ERRO RETURN: " + backendlessFault.toString());
//                Toast.makeText(getContext(),"RETURN errodd",Toast.LENGTH_SHORT).show();
//            }
//        });

        String whereClause = "sellername =  '" + currentUser.getProperty("name")+"'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause );
       // BackendlessCollection<Products> result =
        Backendless.Persistence.of(Products.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Products>>() {
            @Override
            public void handleResponse(BackendlessCollection<Products> response) {
                Log.i(MainActivity.TAG,"SIZE " + response.getTotalObjects());
                Toast.makeText(getContext(),"SIZE"+ response.getTotalObjects(),Toast.LENGTH_SHORT).show();

                productsAdapter = new ProductAdapter(response.getData(),getContext());
                listViewProducts.setAdapter(productsAdapter);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            takePhoto = true;
            imgPhoto = (ImageView) dialogView.findViewById(R.id.imgPhoto);
            imgPhoto.setImageBitmap(imageBitmap);
        }
    }

    public void saveProduct(){
        try{
            String timestamp= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_"+timestamp+"_.jpg";

            final Products produto = new Products();

            produto.setProductname(extNameProduct.getText().toString());
            produto.setDescricao(extDescription.getText().toString());
            produto.setSellername(currentUser.getProperty("name").toString());
            produto.setSeller(currentUser);
            produto.setImageLocation(imageDirectory+"/" + imageFileName);

            //upload image
            Backendless.Files.Android.upload(
                    imageBitmap,
                    Bitmap.CompressFormat.JPEG,
                    100,
                    imageFileName,
                    imageDirectory,
                    new AsyncCallback<BackendlessFile>() {
                        @Override
                        public void handleResponse(BackendlessFile backendlessFile) {
                            Log.i(MainActivity.TAG,"Photo product ADD");
                            Backendless.Persistence.save(produto, new AsyncCallback<Products>() {
                                @Override
                                public void handleResponse(Products products) {
                                    Log.i(MainActivity.TAG,"Product HAS BEEN SAVE");
                                    Toast.makeText(getContext(),"Produto salvo com Sucesso " , Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void handleFault(BackendlessFault backendlessFault) {
                                    Log.e(MainActivity.TAG,"Product DONT SAVE" + backendlessFault.toString());
                                    Toast.makeText(getContext(),"Erro ao salvar o produto \n Tente novamente " , Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            Log.e(MainActivity.TAG,"ERRO Photo product NOT ADD : "+ backendlessFault.toString());
                        }
                    }
            );
        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
