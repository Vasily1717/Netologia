import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;


public class Main {
    //ссылка на запрос
    public static final String URI = "https://api.nasa.gov/planetary/apod?api_key=7TUAjchXDrGagLadwLPPQcPkXuuzgNlljzSNoTbB";

    //
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

// настройка HTTP клиента, который будет выполнять запрос
                       CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(5000)
                            .setSocketTimeout(30000)
                            .setRedirectsEnabled(false)
                            .build())
                    .build();
                       //получаем ответ на запрос
        CloseableHttpResponse response = httpClient.execute(new HttpGet(URI));

        //преобразуем ответ в java объект NasaObject
        NasaObject nasaObject = mapper.readValue(response.getEntity().getContent(), NasaObject.class);
        System.out.println(nasaObject);

// отправка запроса и получение ответа с картинкой
        CloseableHttpResponse pictureResponse = httpClient.execute(new HttpGet(nasaObject.getUrl()));

//формируем автоматическое название для файла
        String[] arr = nasaObject.getUrl().split("/");
        String file = arr[6];

//проверяем что наш ответ не null
        HttpEntity entity = pictureResponse.getEntity();
        if (entity != null) {
            //
            FileOutputStream fos = new FileOutputStream(file);
            entity.writeTo(fos);
            fos.close();
        }

        }
}
