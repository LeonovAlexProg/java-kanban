package ru.yandex.practicum.servers.client;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private URI url;
    private  String API_TOKEN;
    HttpClient httpClient;
    HttpResponse.BodyHandler<String> handler;

    public KVTaskClient(String url) throws IOException, InterruptedException{
        this.url = URI.create(url);
        httpClient = HttpClient.newHttpClient();
        handler = HttpResponse.BodyHandlers.ofString();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://" + url + "register"))
                .build();

        HttpResponse<String> response = httpClient.send(request, handler);

        API_TOKEN = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI toSaveUrl = URI.create("http://" + url.toString() + "save/" + key + "?API_TOKEN=" + API_TOKEN);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(toSaveUrl)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = httpClient.send(request, handler);

        if (response.statusCode() == 200) {
            System.out.println("Данные успешно сохранены!");
        } else {
            System.out.println("Ошибка во время сохранения данных!");
        }
    }

    public String load(String key) throws IOException, InterruptedException {
        URI toLoadUrl = URI.create("http://" + url.toString() + "load/" + key + "?API_TOKEN=" + API_TOKEN);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(toLoadUrl)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = httpClient.send(request, handler);

        if (response.statusCode() == 404) {
            System.out.println("Данные по ключу не найдены");
            return "";
        } else {
            return response.body();
        }
    }
}
