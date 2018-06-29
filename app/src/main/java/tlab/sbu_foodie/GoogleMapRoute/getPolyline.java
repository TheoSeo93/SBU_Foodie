package tlab.sbu_foodie.GoogleMapRoute;




import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface getPolyline {
    @GET("json")
    Call<JsonObject> getPolylineData(@Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode);
}
