package br.com.ogawalucasgmail.monitoramento_mit_app.modules.okhttp;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static br.com.ogawalucasgmail.monitoramento_mit_app.modules.comum.constants.Header.AUTHORIZATION;

public class OkHttpRequest {

    public OkHttpResponse POST(String url, String token, RequestBody requestBody) throws InterruptedException {
        Request request = new Request.Builder().url(url).post(requestBody).addHeader(AUTHORIZATION, token).build();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final OkHttpResponse okHttpResponse = new OkHttpResponse();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                okHttpResponse.setCode(response.code());
                okHttpResponse.setBody(response.body() != null ? response.body().string() : "");
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call call, final IOException ex) {
                okHttpResponse.setCode(-1);
                okHttpResponse.setBody(ex.getMessage());
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
        return okHttpResponse;
    }

    public OkHttpResponse GET(String url, String token) throws InterruptedException {
        Request request = new Request.Builder().url(url).addHeader(AUTHORIZATION, token).build();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final OkHttpResponse okHttpResponse = new OkHttpResponse();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                okHttpResponse.setCode(response.code());
                okHttpResponse.setBody(response.body() != null ? response.body().string() : "");
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call call, final IOException ex) {
                okHttpResponse.setCode(-1);
                okHttpResponse.setBody(ex.getMessage());
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
        return okHttpResponse;
    }

    public OkHttpResponse PUT(String url, String token, RequestBody requestBody) throws InterruptedException {
        Request request = new Request.Builder().url(url).put(requestBody).addHeader(AUTHORIZATION, token).build();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final OkHttpResponse okHttpResponse = new OkHttpResponse();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                okHttpResponse.setCode(response.code());
                okHttpResponse.setBody(response.body() != null ? response.body().string() : "");
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call call, final IOException ex) {
                okHttpResponse.setCode(-1);
                okHttpResponse.setBody(ex.getMessage());
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
        return okHttpResponse;
    }
}
