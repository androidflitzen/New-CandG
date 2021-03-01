package com.flitzen.cng.utils;

import com.flitzen.cng.model.AddQuotationModel;
import com.flitzen.cng.model.AllProductDataModel;
import com.flitzen.cng.model.CategoryModel;
import com.flitzen.cng.model.CustomerModel;
import com.flitzen.cng.model.LoginResponseModel;
import com.flitzen.cng.model.ProductListRequestModel;
import com.flitzen.cng.model.ProductModel;
import com.flitzen.cng.model.QuotationListingModel;
import com.flitzen.cng.model.SalesPersonModel;
import com.flitzen.cng.model.SubCategoryModel;
import com.flitzen.cng.model.TodayInvoiceListingModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface WebApi {

    @GET("users/login?")
    Call<LoginResponseModel> login(@Query("api_key") String api_key,@Query("email") String email, @Query("password") String password);

    //All data list
    @GET("products/all_category?")
    Call<AllProductDataModel> allProductApi(@Query("api_key") String api_key);

    //Customer list
    @GET("customer/all?")
    Call<CustomerModel> customerApi(@Query("api_key") String api_key);

    //SalesPersons list
    @GET("users/sales_person?")
    Call<SalesPersonModel> salesPersonApi(@Query("api_key") String api_key);

    /*//Add Quotation
    @FormUrlEncoded
    @POST("quotation/add")
    Call<AddQuotationModel> addQuotationApi(@Field("api_key") String api_key, @Field("user_id") String user_id, @Field("customer_id") String customer_id, @Field("customer_name") String customer_name ,
                                            @Field("sales_person_id") String sales_person_id, @Field("total_amount") String total_amount , @Field("vat_amount") String vat_amount
                                            , @Field("final_total") String final_total, @Field("sales_type") String sales_type,@FieldMap Map<String, String> quotation_data);*/


    //Add Quotation
    @FormUrlEncoded
    @POST("quotation/add")
    Call<AddQuotationModel> addQuotationApi(@Field("api_key") String api_key, @Field("user_id") String user_id, @Field("customer_id") String customer_id, @Field("customer_name") String customer_name ,
                                            @Field("sales_person_id") String sales_person_id, @Field("total_amount") String total_amount , @Field("vat_amount") String vat_amount
                                            , @Field("final_total") String final_total, @Field("sales_type") String sales_type,@Part("quotation_data[]") List<ProductListRequestModel> quotation_data);


    //Today Quotation List
    @GET("quotation/today?")
    Call<QuotationListingModel> todayQuotationListApi(@Query("api_key") String api_key);

    //Week Quotation List
    @GET("quotation/week?")
    Call<QuotationListingModel> weekQuotationListApi(@Query("api_key") String api_key,@Query("pno") String pno);

    //Month Quotation List
    @GET("quotation/month?")
    Call<QuotationListingModel> monthQuotationListApi(@Query("api_key") String api_key,@Query("pno") String pno);

    //Year Quotation List
    @GET("quotation/all?")
    Call<QuotationListingModel> yearQuotationListApi(@Query("api_key") String api_key,@Query("pno") String pno);

   /* //category list
    @GET("products/category?")
    Call<CategoryModel> categoryApi(@Query("api_key") String api_key);

    //subcategory list
    @GET("products/sub_category?")
    Call<SubCategoryModel> subCategoryApi(@Query("api_key") String api_key, @Query("category_id") String category_id);

    //product list
    @GET("products/data?")
    Call<ProductModel> productApi(@Query("api_key") String api_key,@Query("category_id") String category_id, @Query("sub_category_id") String sub_category_id);*/

    @GET("invoice/today?")
    Call<TodayInvoiceListingModel> todayInvoiceList(@Query("api_key") String api_key);

    @GET("invoice/week?")
    Call<TodayInvoiceListingModel> weekInvoiceList(@Query("api_key") String api_key,@Query("pno") String pno);

    @GET("invoice/month?")
    Call<TodayInvoiceListingModel> monthInvoiceList(@Query("api_key") String api_key,@Query("pno") String pno);

    @GET("invoice/all?")
    Call<TodayInvoiceListingModel> yearInvoiceList(@Query("api_key") String api_key,@Query("pno") String pno);




}
