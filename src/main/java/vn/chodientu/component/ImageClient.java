package vn.chodientu.component;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import vn.chodientu.component.imboclient.ImboClient;
import vn.chodientu.entity.output.Response;

/**
 * @since May 28, 2014
 * @author Phu
 */
public class ImageClient extends ImboClient {

    public ImageClient(String serverUrl, String publicKey, String privateKey) {
        super(serverUrl, publicKey, privateKey);
    }

    public ImageClient(String[] serverUrl, String publicKey, String privateKey) {
        super(serverUrl, publicKey, privateKey);
    }

    /**
     * Download ảnh từ một url
     *
     * @param imageUrl
     * @return
     */
    public Response download(String imageUrl) {
        try {
            URL url;
            HttpURLConnection conn;
            int size;
            url = new URL(imageUrl);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.getInputStream();
            size = conn.getContentLength();
            if (size > 500 * 1024) {
                conn.getInputStream().close();
                return new Response(false, "Đăng ảnh thất bại, ảnh của bạn vượt quá 500kb", size);
            }
            conn.getInputStream().close();
            vn.chodientu.component.imboclient.Http.Response resp = addImageFromUrl(new URI(imageUrl));
            if (resp.isSuccess()) {
                return new Response(true, "Download ảnh thành công", resp.getImageIdentifier());
            }
            return new Response(false, resp.getImboErrorDescription(), resp.getStatusCode());
        } catch (Exception ex) {
            if (ex.getMessage().equals("max500")) {
                return new Response(false, "Đăng ảnh thất bại, ảnh của bạn vượt quá 500kb", ex.getMessage());
            }
            return new Response(false, "Đăng ảnh thất bại, xin thử lại", ex.getMessage());
        }
    }

    /**
     * Download ảnh từ một url
     *
     * @param imageUrl
     * @return
     */
    public Response downloadForCrawl(String imageUrl) {
        return download(imageUrl, 3000);
    }
    
    public Response downloadForAPI(String imageUrl) {
        return download(imageUrl, 3000);
    }

    public Response download(String imageUrl, long maxSize) {
        String sizeInText = "";
            if (maxSize < 1000) {
                sizeInText = maxSize + "kb";
            } else if (maxSize >= 1000) {
                sizeInText = (float) (maxSize / 1000) + "M";
            }
        try {
            URL url;
            HttpURLConnection conn;
            int size;
            url = new URL(imageUrl);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.getInputStream();
            size = conn.getContentLength();
            if (size > maxSize * 1024) {
                conn.getInputStream().close();
                return new Response(false, "Đăng ảnh thất bại, ảnh của bạn vượt quá "+ sizeInText, size);
            }
            conn.getInputStream().close();
            vn.chodientu.component.imboclient.Http.Response resp = addImageFromUrl(new URI(imageUrl), maxSize);
            if (resp.isSuccess()) {
                return new Response(true, "Download ảnh thành công", resp.getImageIdentifier());
            }
            return new Response(false, resp.getImboErrorDescription(), resp.getStatusCode());
        } catch (Exception ex) {
            if (ex.getMessage().equals("max"+ maxSize)) {
                return new Response(false, "Đăng ảnh thất bại, ảnh của bạn vượt quá "+ sizeInText, ex.getMessage());
            }
            return new Response(false, "Đăng ảnh thất bại, xin thử lại", ex.getMessage());
        }
    }

    public Response upload(MultipartFile images) {
        try {
            File file = File.createTempFile(UUID.randomUUID().toString(), images.getContentType().split("/")[1]);
            images.transferTo(file);
            if (file.length() > 500 * 1024) {
                return new Response(false, "Đăng ảnh thất bại, ảnh của bạn vượt quá 500kb", file.length());
            }
            vn.chodientu.component.imboclient.Http.Response resp = addImage(file);
            if (resp.isSuccess()) {
                return new Response(true, "Upload ảnh thành công", resp.getImageIdentifier());
            }
            return new Response(false, resp.getImboErrorDescription(), resp.getStatusCode());

        } catch (Exception ex) {
            ex.printStackTrace();
            return new Response(false, "Đăng ảnh thất bại, xin thử lại<br/><font color='white'>"+ex.getMessage()+"</font>", ex.getMessage());

        }
    }
}
