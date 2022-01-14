package app.logistikappbeta.com.logistikapp.Interfaces;

import app.logistikappbeta.com.logistikapp.POJOs.AddCurrentPosition;
import app.logistikappbeta.com.logistikapp.Params.ActivesAndVisitsParam;
import app.logistikappbeta.com.logistikapp.Params.AddActiveParam;
import app.logistikappbeta.com.logistikapp.Params.AddActiveVisitParam;
import app.logistikappbeta.com.logistikapp.Params.AddCommentParam;
import app.logistikappbeta.com.logistikapp.Params.AddNewCustomerParam;
import app.logistikappbeta.com.logistikapp.Params.AddOrderParam;
import app.logistikappbeta.com.logistikapp.Params.AddStoreTravelParam;
import app.logistikappbeta.com.logistikapp.Params.AddUserParam;
import app.logistikappbeta.com.logistikapp.Params.CheckinParam;
import app.logistikappbeta.com.logistikapp.Params.GetAddStoreInitialnfoParam;
import app.logistikappbeta.com.logistikapp.Params.GetAlmacenInfoParam;
import app.logistikappbeta.com.logistikapp.Params.GetCRMParams;
import app.logistikappbeta.com.logistikapp.Params.GetCitiesByIdStateParam;
import app.logistikappbeta.com.logistikapp.Params.GetClientTravelParam;
import app.logistikappbeta.com.logistikapp.Params.GetComment;
import app.logistikappbeta.com.logistikapp.Params.GetCustomersParam;
import app.logistikappbeta.com.logistikapp.Params.GetHistoricListParam;
import app.logistikappbeta.com.logistikapp.Params.GetHistoricParam;
import app.logistikappbeta.com.logistikapp.Params.GetImageProductParam;
import app.logistikappbeta.com.logistikapp.Params.GetMessageListParam;
import app.logistikappbeta.com.logistikapp.Params.GetMessageParam;
import app.logistikappbeta.com.logistikapp.Params.GetMetricsParam;
import app.logistikappbeta.com.logistikapp.Params.GetNotificationsParam;
import app.logistikappbeta.com.logistikapp.Params.GetProductsParam;
import app.logistikappbeta.com.logistikapp.Params.GetQtyNotifications;
import app.logistikappbeta.com.logistikapp.Params.GetReportByDriParam;
import app.logistikappbeta.com.logistikapp.Params.GetRetailsParam;
import app.logistikappbeta.com.logistikapp.Params.GetSaleByDriParam;
import app.logistikappbeta.com.logistikapp.Params.GetSubAlmacenInfoParam;
import app.logistikappbeta.com.logistikapp.Params.GetSupplierParam;
import app.logistikappbeta.com.logistikapp.Params.GetTransactionParam;
import app.logistikappbeta.com.logistikapp.Params.GetTravelParam;
import app.logistikappbeta.com.logistikapp.Params.GetTravelStoresParam;
import app.logistikappbeta.com.logistikapp.Params.GetUserListParam;
import app.logistikappbeta.com.logistikapp.Params.GetUserParam;
import app.logistikappbeta.com.logistikapp.Params.GetUserPositionParam;
import app.logistikappbeta.com.logistikapp.Params.GetUsersParam;
import app.logistikappbeta.com.logistikapp.Params.LogInParam;
import app.logistikappbeta.com.logistikapp.Params.RegisterParam;
import app.logistikappbeta.com.logistikapp.Params.RemoveStoreTravelParam;
import app.logistikappbeta.com.logistikapp.Params.RollbackParam;
import app.logistikappbeta.com.logistikapp.Params.DeliveryNotificationParam;
import app.logistikappbeta.com.logistikapp.Params.SendMessageParam;
import app.logistikappbeta.com.logistikapp.Params.SendPrintTicketParam;
import app.logistikappbeta.com.logistikapp.Params.SendTicketParam;
import app.logistikappbeta.com.logistikapp.Params.TravelEndParam;
import app.logistikappbeta.com.logistikapp.Params.TravelStartParam;
import app.logistikappbeta.com.logistikapp.Params.UpdOrdenStatusParam;
import app.logistikappbeta.com.logistikapp.Params.UpdUserParam;
import app.logistikappbeta.com.logistikapp.Params.UpdateCustomerParam;
import app.logistikappbeta.com.logistikapp.Results.ActivesAndVisitsResult;
import app.logistikappbeta.com.logistikapp.Results.AddOrderResult;
import app.logistikappbeta.com.logistikapp.Results.CRMResult;
import app.logistikappbeta.com.logistikapp.Results.ClientTravelResult;
import app.logistikappbeta.com.logistikapp.Results.CommentResult;
import app.logistikappbeta.com.logistikapp.Results.DeliveryNotificationResult;
import app.logistikappbeta.com.logistikapp.Results.GetAddStoreInitialInfoResult;
import app.logistikappbeta.com.logistikapp.Results.GetAlmacenInfoResult;
import app.logistikappbeta.com.logistikapp.Results.GetCitiesByIdStateResult;
import app.logistikappbeta.com.logistikapp.Results.GetCustomersResult;
import app.logistikappbeta.com.logistikapp.Results.GetNotificationsResult;
import app.logistikappbeta.com.logistikapp.Results.GetReportByDriResult;
import app.logistikappbeta.com.logistikapp.Results.GetRetailsResult;
import app.logistikappbeta.com.logistikapp.Results.GetSubAlmacenInfoResult;
import app.logistikappbeta.com.logistikapp.Results.GetSupplierResult;
import app.logistikappbeta.com.logistikapp.Results.GetUsersResult;
import app.logistikappbeta.com.logistikapp.Results.ImageProductResult;
import app.logistikappbeta.com.logistikapp.Results.LoginRetult;
import app.logistikappbeta.com.logistikapp.Results.MessageListResult;
import app.logistikappbeta.com.logistikapp.Results.MessageResult;
import app.logistikappbeta.com.logistikapp.Results.MetricResult;
import app.logistikappbeta.com.logistikapp.Results.NotificationsResult;
import app.logistikappbeta.com.logistikapp.Results.ProductsResult;
import app.logistikappbeta.com.logistikapp.Results.Result;
import app.logistikappbeta.com.logistikapp.Results.SendMessageResult;
import app.logistikappbeta.com.logistikapp.Results.SendPrintTicketResult;
import app.logistikappbeta.com.logistikapp.Results.SendTicketResult;
import app.logistikappbeta.com.logistikapp.Results.TOWResult;
import app.logistikappbeta.com.logistikapp.Results.TransactionListResult;
import app.logistikappbeta.com.logistikapp.Results.TransactionResult;
import app.logistikappbeta.com.logistikapp.Results.TravelResult;
import app.logistikappbeta.com.logistikapp.Results.TravelStoresResult;
import app.logistikappbeta.com.logistikapp.Results.UserListResult;
import app.logistikappbeta.com.logistikapp.Results.UserPositionResult;
import app.logistikappbeta.com.logistikapp.Results.UserResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by danie on 17/01/2017.
 */

public interface WSInterface {

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/login")
  Call<LoginRetult> getLogin(@Body LogInParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/getuser")
  Call<UserResult> getUser(@Body GetUserParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/gettravel")
  Call<TravelResult> getTravel(@Body GetTravelParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/gettravelstores")
  Call<TravelStoresResult> getTravelStoresHistorico(@Body GetHistoricParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/gettravelstores")
  Call<TravelStoresResult> getTravelStores(@Body GetTravelStoresParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/gettravelstores")
  Call<TOWResult> getTOWDOS(@Body GetTravelStoresParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/getmetrics")
  Call<MetricResult> getMetrics(@Body GetMetricsParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/adduserlocation")
  Call<Result> addUserLocation(@Body AddCurrentPosition param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/getqtynotifications")
  Call<NotificationsResult> getNotifications(@Body GetQtyNotifications param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/gettravelhistory")
  Call<TravelResult> getTravelHistory(@Body GetHistoricListParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/travelstart")
  Call<Result> travelStart(@Body TravelStartParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/checkin")
  Call<Result> checkin(@Body CheckinParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/rollback")
  Call<Result> rollback(@Body RollbackParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/travelend")
  Call<Result> travelEnd(@Body TravelEndParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/getcrm")
  Call<CRMResult> getCRM(@Body GetCRMParams param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/gettransactions")
  Call<TransactionListResult> getTransactions(@Body GetSaleByDriParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/gettransaction")
  Call<TransactionResult> getTransaction(@Body GetTransactionParam param);

  @Headers({
          "Accept-Charset:UTF-8",
          "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/gettransactionbyid")
  Call<TransactionResult> getTransactionById(@Body GetTransactionParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/getproducts")
  Call<ProductsResult> getProducts(@Body GetProductsParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/addorder")
  Call<AddOrderResult> addOrder(@Body AddOrderParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/getcomment")
  Call<CommentResult> getComment(@Body GetComment param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/addcomment")
  Call<Result> addComment(@Body AddCommentParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/getimageproduct")
  Call<ImageProductResult> getImageProduct(@Body GetImageProductParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/getclienttravel")
  Call<ClientTravelResult> getClientTravel(@Body GetClientTravelParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/addstoretravel")
  Call<Result> addStoreTravel(@Body AddStoreTravelParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/removestoretravel")
  Call<Result> removeStoreTravel(@Body RemoveStoreTravelParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/getmessagelist")
  Call<MessageListResult> getMessageList(@Body GetMessageListParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/getuserlist")
  Call<UserListResult> getUserList(@Body GetUserListParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/sendmessage")
  Call<SendMessageResult> sendMessage(@Body SendMessageParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/getmessage")
  Call<MessageResult> getMessage(@Body GetMessageParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/getuserposition")
  Call<UserPositionResult> getUserPosition(@Body GetUserPositionParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/adduser2")
  Call<Result> addUser2(@Body AddUserParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type:application/json;charset=UTF-8"
  })
  @POST("/upduser")
  Call<Result> updUser(@Body UpdUserParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/sendticket")
  Call<SendTicketResult> sendTicket(@Body SendTicketParam param);

  @Headers({
          "Accept-Charset:UTF-8",
          "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/sendnotification")
  Call<DeliveryNotificationResult> sendNotification(@Body DeliveryNotificationParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/sendprintticket")
  Call<SendPrintTicketResult> sendPrintTicket(@Body SendPrintTicketParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/register")
  Call<Result> register(@Body RegisterParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/updateordenstatus")
  Call<Result> updateOrdenStatus(@Body UpdOrdenStatusParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/getusers")
  Call<GetUsersResult> getUsers(@Body GetUsersParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/getnotifications")
  Call<GetNotificationsResult> getNotifications(@Body GetNotificationsParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/getretails")
  Call<GetRetailsResult> getRetails(@Body GetRetailsParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/getcustomers")
  Call<GetCustomersResult> getCustomers(@Body GetCustomersParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/updatecustomer")
  Call<Result> updateCustomer(@Body UpdateCustomerParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/addnewcustomer")
  Call<Result> addNewCustomer(@Body AddNewCustomerParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/get-cities-by-id-state")
  Call<GetCitiesByIdStateResult> getCitiesByIdState(@Body GetCitiesByIdStateParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/get-add-store-initial-info")
  Call<GetAddStoreInitialInfoResult> getAddStoreInitialInfo(@Body GetAddStoreInitialnfoParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/getreportbydri")
  Call<GetReportByDriResult> getReportByDri(@Body GetReportByDriParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/getalmaceninfo")
  Call<GetAlmacenInfoResult> getAlmacenInfo(@Body GetAlmacenInfoParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/getsubalmaceninfo")
  Call<GetSubAlmacenInfoResult> getSubAlmacenInfo(@Body GetSubAlmacenInfoParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/getactivesandvisits")
  Call<ActivesAndVisitsResult> getActivesAndVisits(@Body ActivesAndVisitsParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/addactive")
  Call<Result> addActive(@Body AddActiveParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/addactivevisit")
  Call<Result> addActiveVisit(@Body AddActiveVisitParam param);

  @Headers({
      "Accept-Charset:UTF-8",
      "Content-Type: application/json;charset=UTF-8"
  })
  @POST("/getsupplier")
  Call<GetSupplierResult> getSupplier(@Body GetSupplierParam param);

}
