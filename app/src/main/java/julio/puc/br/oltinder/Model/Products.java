package julio.puc.br.oltinder.Model;

import android.graphics.Bitmap;

import com.backendless.BackendlessUser;

/**
 * Created by julio on 01/06/2016.
 */
public class Products {

    private String sellername;
    private String productname;
    private String descricao;
    private String imageLocation;
    private BackendlessUser seller;


    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public BackendlessUser getSeller() {
        return seller;
    }

    public void setSeller(BackendlessUser seller) {
        this.seller = seller;
    }

}
