package util;

import config.SupabaseConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientHelper {

    private static final HttpClient CLIENT =
            HttpClient.newHttpClient();

    private HttpClientHelper() {
    }

    public static String get(String endpoint)
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        SupabaseConfig.SUPABASE_URL
                                                + endpoint
                                )
                        )
                        .header(
                                "apikey",
                                SupabaseConfig.SUPABASE_API_KEY
                        )
                        .header(
                                "Authorization",
                                "Bearer "
                                        + SupabaseConfig.SUPABASE_API_KEY
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .GET()
                        .build();

        HttpResponse<String> response =
                CLIENT.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        return response.body();
    }

    public static String post(
            String endpoint,
            String jsonBody
    ) throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        SupabaseConfig.SUPABASE_URL
                                                + endpoint
                                )
                        )
                        .header(
                                "apikey",
                                SupabaseConfig.SUPABASE_API_KEY
                        )
                        .header(
                                "Authorization",
                                "Bearer "
                                        + SupabaseConfig.SUPABASE_API_KEY
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .header(
                                "Prefer",
                                "return=representation"
                        )
                        .POST(
                                HttpRequest.BodyPublishers.ofString(
                                        jsonBody
                                )
                        )
                        .build();

        HttpResponse<String> response =
                CLIENT.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        return response.body();
    }

    public static String patch(
            String endpoint,
            String jsonBody
    ) throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        SupabaseConfig.SUPABASE_URL
                                                + endpoint
                                )
                        )
                        .header(
                                "apikey",
                                SupabaseConfig.SUPABASE_API_KEY
                        )
                        .header(
                                "Authorization",
                                "Bearer "
                                        + SupabaseConfig.SUPABASE_API_KEY
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .header(
                                "Prefer",
                                "return=representation"
                        )
                        .method(
                                "PATCH",
                                HttpRequest.BodyPublishers.ofString(
                                        jsonBody
                                )
                        )
                        .build();

        HttpResponse<String> response =
                CLIENT.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        return response.body();
    }

    public static String delete(
            String endpoint
    ) throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        SupabaseConfig.SUPABASE_URL
                                                + endpoint
                                )
                        )
                        .header(
                                "apikey",
                                SupabaseConfig.SUPABASE_API_KEY
                        )
                        .header(
                                "Authorization",
                                "Bearer "
                                        + SupabaseConfig.SUPABASE_API_KEY
                        )
                        .DELETE()
                        .build();

        HttpResponse<String> response =
                CLIENT.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        return response.body();
    }
}