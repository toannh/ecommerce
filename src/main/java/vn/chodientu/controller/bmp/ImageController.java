package vn.chodientu.controller.bmp;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.component.ImageClient;
import vn.chodientu.component.imboclient.Url.ImageUrl;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.ItemService;

/**
 *
 * @author thanhvv
 */
@Controller("imageController")
@RequestMapping(value = "/image")
public class ImageController {

    @Autowired
    ItemService itemService;
    @Autowired
    ImageService imageService;
    @Autowired
    ImageClient imageClient;

    @RequestMapping(value = "/{code}")
    @ResponseBody
    public void test(HttpServletResponse response, @PathVariable("code") String id) throws IOException {
        ImageUrl imageUrl = imageService.getUrl(id);
        ImageUrl thumbnail = imageUrl.thumbnail(600, 300, "inset");
        if (thumbnail != null) {
            URL url = new URL(thumbnail.getUrl());
            response.setContentType("image/bmp");
            OutputStream out = response.getOutputStream();
            BufferedImage image = null;
            image = ImageIO.read(url);
            ImageIO.write(image, "bmp", out);

        }

    }

}
