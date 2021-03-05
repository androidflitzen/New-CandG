package com.flitzen.cng.utils;

import com.flitzen.cng.model.AddQuotationModel;
import com.flitzen.cng.model.AllProductDataModel;
import com.flitzen.cng.model.CategoryModel;
import com.flitzen.cng.model.CommonModel;
import com.flitzen.cng.model.CrediNotesListModel;
import com.flitzen.cng.model.CustomerModel;
import com.flitzen.cng.model.LoginResponseModel;
import com.flitzen.cng.model.ProductListRequestModel;
import com.flitzen.cng.model.ProductListRequestModelTest;
import com.flitzen.cng.model.ProductModel;
import com.flitzen.cng.model.QuotationDetailsModel;
import com.flitzen.cng.model.QuotationListingModel;
import com.flitzen.cng.model.SalesPersonModel;
import com.flitzen.cng.model.SubCategoryModel;
import com.flitzen.cng.model.TodayInvoiceListingModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface WebApi {

    @GET("users/login?")
    Call<LoginResponseModel> login(@Query("api_key") String api_key, @Query("email") String email, @Query("password") String password);

    //All data list
    @GET("products/all_category?")
    Call<AllProductDataModel> allProductApi(@Query("api_key") String api_key);

    //Customer list
    @GET("customer/all?")
    Call<CustomerModel> customerApi(@Query("api_key") String api_key);

    //SalesPersons list
    @GET("users/sales_person?")
    Call<SalesPersonModel> salesPersonApi(@Query("api_key") String api_key);

    //Add Quotation
    @GET("quotation/add")
    Call<AddQuotationModel> addQuotationApi(@QueryMap Map<String, String> params);

    //Add Invoice
    @GET("invoice/add")
    Call<AddQuotationModel> addInvoiceApi(@QueryMap Map<String, String> params);

    //Add credit Note
    @GET("credit_note/add")
    Call<AddQuotationModel> addCreditNotesApi(@QueryMap Map<String, String> params);

    //Today Quotation List
    @GET("quotation/today?")
    Call<QuotationListingModel> todayQuotationListApi(@Query("api_key") String api_key);

    //Week Quotation List
    @GET("quotation/week?")
    Call<QuotationListingModel> weekQuotationListApi(@Query("api_key") String api_key, @Query("pno") String pno);

    //Month Quotation List
    @GET("quotation/month?")
    Call<QuotationListingModel> monthQuotationListApi(@Query("api_key") String api_key, @Query("pno") String pno);

    //Year Quotation List
    @GET("quotation/all?")
    Call<QuotationListingModel> yearQuotationListApi(@Query("api_key") String api_key, @Query("pno") String pno);

    //Today invoice List
    @GET("invoice/today?")
    Call<TodayInvoiceListingModel> todayInvoiceList(@Query("api_key") String api_key);

    //Week invoice List
    @GET("invoice/week?")
    Call<TodayInvoiceListingModel> weekInvoiceList(@Query("api_key") String api_key, @Query("pno") String pno);

    //Month invoice List
    @GET("invoice/month?")
    Call<TodayInvoiceListingModel> monthInvoiceList(@Query("api_key") String api_key, @Query("pno") String pno);

    //Year invoice List
    @GET("invoice/all?")
    Call<TodayInvoiceListingModel> yearInvoiceList(@Query("api_key") String api_key, @Query("pno") String pno);

    //Today credit note List
    @GET("credit_note/today?")
    Call<CrediNotesListModel> todayCreditNoteList(@Query("api_key") String api_key);

    //Week credit note List
    @GET("credit_note/week?")
    Call<CrediNotesListModel> weekCreditNoteList(@Query("api_key") String api_key, @Query("pno") String pno);

    //Month credit note List
    @GET("credit_note/month?")
    Call<CrediNotesListModel> monthCreditNoteList(@Query("api_key") String api_key, @Query("pno") String pno);

    //Year credit note List
    @GET("credit_note/all?")
    Call<CrediNotesListModel> yearCreditNoteList(@Query("api_key") String api_key, @Query("pno") String pno);

    //Delete Quotation
    @GET("quotation/delete_quotation?")
    Call<CommonModel> deleteQuotation(@Query("api_key") String api_key, @Query("quotation_id") String quotation_id);

    //Get Quotation Details
    @GET("quotation/quotation_by_id?")
    Call<QuotationDetailsModel> quotationDetails(@Query("api_key") String api_key, @Query("quotation_id") String quotation_id);


     /* //category list
    @GET("products/category?")
    Call<CategoryModel> categoryApi(@Query("api_key") String api_key);

    //subcategory list
    @GET("products/sub_category?")
    Call<SubCategoryModel> subCategoryApi(@Query("api_key") String api_key, @Query("category_id") String category_id);

    //product list
    @GET("products/data?")
    Call<ProductModel> productApi(@Query("api_key") String api_key,@Query("category_id") String category_id, @Query("sub_category_id") String sub_category_id);*/

}
