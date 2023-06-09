package com.digital.electronics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.digital.electronics.utils.Utils
import java.util.Date;
import java.util.HashMap;

@Document(collection = "ShoppingCarts")
public class ShoppingCart {

    public static String ORDERED = "ordered";
    public static String PENDING = "pending";

    @Id
    private String id;

    public String status; //pending, ordered

    public String userName;

    public HashMap<String, Product> products;

    public HashMap<String, Integer> productQuantities;

    public Date lastModified;

    public Date orderDate;

    public double totalPrice = 0;

    public ShoppingCart(){}

    public ShoppingCart(String status, String userName,
                        HashMap<String, Product> products,
                        HashMap<String, Integer> productQuantities,
                        Date orderDate, Date lastModified, double totalPrice){
        this.status = status;
        this.userName = userName;
        this.products = products;
        this.productQuantities = productQuantities;
        this.orderDate = orderDate;
        this.lastModified = lastModified;
        this.totalPrice = totalPrice;
    }

    public String getId(){
        return this.id;
    }

    public Product getProductFromId(String productId){
        return this.products.get(productId);
    }

    public void addProduct(Product product){
        this.products.putIfAbsent(product.getId(), product);
    }

    public void addProductQuantity(Product product, boolean isDiscountApplied){
        String productId = product.getId();
        int quantity = 0;
        if(this.productQuantities.containsKey(productId)){
            quantity = this.productQuantities.get(productId);
            quantity++;
            this.productQuantities.put(productId, quantity);
        }
        else {
            // init the product quantities if key not found
            this.productQuantities.put(productId, 1);
        }
        if(isDiscountApplied)
            this.totalPrice = Utils.calculateBuyOneGetHalfPrice(product,quantity);
        else
            this.totalPrice += product.price;
    }

    public void removeProduct(String productId){
        this.products.remove(productId);
    }

    public void removeProductQuantity(Product product){

        String productId = product.getId();
        if(this.productQuantities.containsKey(productId)){
            int quantity = this.productQuantities.get(productId);
            quantity--;
            //remove datas from the HashMaps when quantity is too low
            if(quantity <1){
                this.productQuantities.remove(productId);
                this.removeProduct(productId);
            }
            else {
                this.productQuantities.put(productId, quantity);
            }
            this.totalPrice -= product.price;
        }
    }
}
