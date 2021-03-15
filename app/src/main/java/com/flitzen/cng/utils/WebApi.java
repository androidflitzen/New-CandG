package com.flitzen.cng.utils;

import com.flitzen.cng.model.AddNewProductModel;
import com.flitzen.cng.model.AddQuotationModel;
import com.flitzen.cng.model.AllProductDataModel;
import com.flitzen.cng.model.CategoryModel;
import com.flitzen.cng.model.CommonModel;
import com.flitzen.cng.model.CrediNotesListModel;
import com.flitzen.cng.model.CustomerModel;
import com.flitzen.cng.model.LoginResponseModel;
import com.flitzen.cng.model.MonthListModel;
import com.flitzen.cng.model.ProductListRequestModel;
import com.flitzen.cng.model.ProductListRequestModelTest;
import com.flitzen.cng.model.ProductModel;
import com.flitzen.cng.model.QuotationDetailsModel;
import com.flitzen.cng.model.QuotationListingModel;
import com.flitzen.cng.model.SalesPersonModel;
import com.flitzen.cng.model.SubCategoryModel;
import com.flitzen.cng.model.TodayInvoiceListingModel;
import com.flitzen.cng.model.UnitModel;

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

    //Edit Quotation
    @GET("quotation/edit")
    Call<AddQuotationModel> editQuotationApi(@QueryMap Map<String, String> params);

    //Add Invoice
    @GET("invoice/add")
    Call<AddQuotationModel> addInvoiceApi(@QueryMap Map<String, String> params);

    //Convert Quotation to Invoice
    @GET("quotation/invoice_convert")
    Call<AddQuotationModel> convertQuotationToInvoiceApi(@QueryMap Map<String, String> params);

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

    //Search Month Quotation List
    @GET("quotation/month?")
    Call<QuotationListingModel> monthQuotationListApi(@Query("api_key") String api_key, @Query("pno") String pno, @Query("search_text") String search_text);

    //Search Week Quotation List
    @GET("quotation/week?")
    Call<QuotationListingModel> weekQuotationListApi(@Query("api_key") String api_key, @Query("pno") String pno, @Query("search_text") String search_text);

    //Search Week Quotation List
    @GET("quotation/all?")
    Call<QuotationListingModel> yearQuotationListApi(@Query("api_key") String api_key, @Query("pno") String pno, @Query("search_text") String search_text);

    //Search Year invoice List
    @GET("invoice/all?")
    Call<TodayInvoiceListingModel> yearInvoiceList(@Query("api_key") String api_key, @Query("pno") String pno, @Query("search_text") String search_text);

    //Search Month invoice List
    @GET("invoice/month?")
    Call<TodayInvoiceListingModel> monthInvoiceList(@Query("api_key") String api_key, @Query("pno") String pno, @Query("search_text") String search_text);

    //Search Week invoice List
    @GET("invoice/week?")
    Call<TodayInvoiceListingModel> weekInvoiceList(@Query("api_key") String api_key, @Query("pno") String pno, @Query("search_text") String search_text);

    //Search Month Credit Note List
    @GET("credit_note/month?")
    Call<CrediNotesListModel> monthCreditNoteList(@Query("api_key") String api_key, @Query("pno") String pno, @Query("search_text") String search_text);

    //Search Month Credit Note List
    @GET("credit_note/week?")
    Call<CrediNotesListModel> weekCreditNoteList(@Query("api_key") String api_key, @Query("pno") String pno, @Query("search_text") String search_text);

    //Search Year Credit Note List
    @GET("credit_note/all?")
    Call<CrediNotesListModel> yearCreditNoteList(@Query("api_key") String api_key, @Query("pno") String pno, @Query("search_text") String search_text);

    //get unites
    @GET("products/proudct_unit?")
    Call<UnitModel> getUnitList(@Query("api_key") String api_key);

    //Add new product
    @GET("products/add?")
    Call<AddNewProductModel> addNewProduct(@Query("api_key") String api_key, @Query("user_id") String user_id, @Query("product_name") String product_name, @Query("product_price") String product_price
            , @Query("product_unit_id") String product_unit_id);

    //Quotation month list
    @GET("quotation/abc?")
    Call<MonthListModel> getMonthList(@Query("api_key") String api_key);

    //Month Year wise Quotation List
    @GET("quotation/month_year_wise?")
    Call<QuotationListingModel> monthYearQuotationListApi(@Query("api_key") String api_key,@Query("month") String month,@Query("year") String year,@Query("pno") String pno);

    //Search month Year wise Quotation List
    @GET("quotation/month_year_wise?")
    Call<QuotationListingModel> searchMonthYearQuotationListApi(@Query("api_key") String api_key,@Query("pno") String pno,@Query("month") String month,@Query("year") String year, @Query("search_text") String search_text);

    //Month Year wise Quotation List
    @GET("invoice/month_year_wise?")
    Call<TodayInvoiceListingModel> monthYearInvoiceListApi(@Query("api_key") String api_key,@Query("month") String month,@Query("year") String year,@Query("pno") String pno);

    //Search month Year wise Invoice List
    @GET("quotation/month_year_wise?")
    Call<TodayInvoiceListingModel> searchMonthYearInvoiceListApi(@Query("api_key") String api_key,@Query("pno") String pno,@Query("month") String month,@Query("year") String year, @Query("search_text") String search_text);

    //Month Year wise Credit Notes List
    @GET("credit_note/month_year_wise?")
    Call<CrediNotesListModel> monthYearCreditListApi(@Query("api_key") String api_key,@Query("month") String month,@Query("year") String year,@Query("pno") String pno);

    //Search month Year wise Credit Notes List
    @GET("credit_note/month_year_wise?")
    Call<CrediNotesListModel> searchCreditInvoiceListApi(@Query("api_key") String api_key,@Query("pno") String pno,@Query("month") String month,@Query("year") String year, @Query("search_text") String search_text);


}
