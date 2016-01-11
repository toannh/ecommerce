/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.cpservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.component.imboclient.Url.ImageUrl;
import vn.chodientu.controller.BaseRest;
import vn.chodientu.entity.db.BigLandingCategory;
import vn.chodientu.entity.db.BigLandingItem;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.form.BigLandingForm;
import vn.chodientu.entity.form.ImageForm;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.BigLandingService;
import vn.chodientu.service.ImageService;

/**
 *
 * @author Admin
 */
@Controller("cpBigLandingService")
@RequestMapping(value = "/cpservice/biglanding")
public class BigLandingController extends BaseRest {

    @Autowired
    private BigLandingService bigLandingService;
    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/addlanding", method = RequestMethod.POST)
    @ResponseBody
    public Response add(@ModelAttribute BigLandingForm bigLandingForm) throws Exception {
        return bigLandingService.addBigLanding(bigLandingForm);
    }

    @RequestMapping(value = "/editlanding", method = RequestMethod.POST)
    @ResponseBody
    public Response editlanding(@ModelAttribute BigLandingForm bigLandingForm) throws Exception {
        return bigLandingService.addBigLanding(bigLandingForm);
    }

    @RequestMapping(value = "/getbiglanding", method = RequestMethod.GET)
    @ResponseBody
    public Response getbiglanding(@RequestParam String biglandingId) throws Exception {
        return new Response(true, "Thông tin Landing", bigLandingService.getBigLanding(biglandingId));
    }

    @RequestMapping(value = "/delbiglanding", method = RequestMethod.GET)
    @ResponseBody
    public Response delbiglanding(@RequestParam String id) {
        return bigLandingService.deleteLanding(id);
    }

    @RequestMapping(value = "/changeactivebiglanding", method = RequestMethod.GET)
    @ResponseBody
    public Response changeActiveBigLanding(@RequestParam String id) throws Exception {
        return bigLandingService.changeActiveBigLanding(id);
    }

    @RequestMapping(value = "/changebannercenteractive", method = RequestMethod.GET)
    @ResponseBody
    public Response changebannercenteractive(@RequestParam String id) throws Exception {
        return bigLandingService.changeBannerCenterActiveBigLanding(id);
    }
    @RequestMapping(value = "/changebackground", method = RequestMethod.GET)
    @ResponseBody
    public Response changebackground(@RequestParam String id,@RequestParam String background) throws Exception {
        return bigLandingService.changeBackground(id, background);
    }

    @RequestMapping(value = "/addbiglandingcategory", method = RequestMethod.POST)
    @ResponseBody
    public Response addBigLandingCategory(@RequestBody BigLandingCategory bigLandingCategory) throws Exception {
        return bigLandingService.addCategory(bigLandingCategory);
    }

    @RequestMapping(value = "/editbiglandingcategory", method = RequestMethod.POST)
    @ResponseBody
    public Response editBigLandingCategory(@RequestBody BigLandingCategory bigLandingCategory) throws Exception {
        return bigLandingService.addCategory(bigLandingCategory);
    }

    @RequestMapping(value = "/changeactivebiglandingcategory", method = RequestMethod.GET)
    @ResponseBody
    public Response changeActiveBigLandingCategory(@RequestParam String id) throws Exception {
        return bigLandingService.changeActiveBigLandingCate(id);
    }

    @RequestMapping(value = "/changepositionbiglandingcategory", method = RequestMethod.GET)
    @ResponseBody
    public Response changePositionBigLandingCategory(@RequestParam String id, @RequestParam int position) throws Exception {
        return bigLandingService.changePositionBigLandingCate(id, position);
    }

    @RequestMapping(value = "/getbiglandingcatebyid", method = RequestMethod.GET)
    @ResponseBody
    public Response getbiglandingCate(@RequestParam String bigLandingCateId) throws Exception {
        return new Response(true, "Thông tin BigLandingCate", bigLandingService.getCategory(bigLandingCateId));
    }

    @RequestMapping(value = "/addbiglandingitem", method = RequestMethod.POST)
    @ResponseBody
    public Response addBigLandingCategory(@RequestBody BigLandingItem bigLandingItem) throws Exception {
        return bigLandingService.addItem(bigLandingItem);
    }

    @RequestMapping(value = "/uploadimage", method = RequestMethod.POST)
    @ResponseBody
    public Response uploadimageItem(@ModelAttribute ImageForm imageForm) throws Exception {
        if (imageForm.getTargetId() != null && !imageForm.getTargetId().equals("")) {
            List<String> images = imageService.get(ImageType.BIG_LANDING, imageForm.getTargetId());
            Response upload = imageService.upload(imageForm.getImage(), ImageType.BIG_LANDING, imageForm.getTargetId());
            if (upload == null || !upload.isSuccess()) {
                return new Response(false, "Lỗi up ảnh: " + upload.getMessage());
            }
            for (String image : images) {
                imageService.deleteById(ImageType.BIG_LANDING, imageForm.getTargetId(), image);
            }
            Map<String, String> map = new HashMap<>();
            map.put("imageId", upload.getData().toString());
            map.put("imageUrl", imageService.getUrl(upload.getData().toString()).getUrl());
            return new Response(true, "upload thành công", map);
        }
        return new Response(false, "Sản phẩm BigLanding phải được chọn");
    }

    @RequestMapping(value = "/downloadimage", method = RequestMethod.POST)
    @ResponseBody
    public Response downloadImage(@RequestBody ImageForm imageForm) throws Exception {
        if (imageForm.getTargetId() != null && !imageForm.getTargetId().equals("")) {
            List<String> images = imageService.get(ImageType.BIG_LANDING, imageForm.getTargetId());
            Response download = imageService.download(imageForm.getImageUrl(), ImageType.BIG_LANDING, imageForm.getTargetId());
            if (download == null || !download.isSuccess()) {
                return new Response(false, "Lỗi download ảnh: " + download.getMessage());
            }
            for (String image : images) {
                imageService.deleteById(ImageType.BIG_LANDING, imageForm.getTargetId(), image);
            }
            Map<String, String> map = new HashMap<>();
            map.put("imageId", download.getData().toString());
            map.put("imageUrl", imageService.getUrl(download.getData().toString()).getUrl());
            return new Response(true, "upload thành công", map);
        }
        return new Response(false, "Sản phẩm BigLanding phải được chọn");
    }

    @RequestMapping(value = "/cropimage", method = RequestMethod.POST)
    @ResponseBody
    public Response addItem(@RequestBody ImageForm imageForm) throws Exception {
        int width = 0;
        int height = 0;
        int position = imageForm.getPosition();
        if (imageForm.getTemplate() != null && !imageForm.getTemplate().equals("")) {
            if (imageForm.getTemplate().equals("template1")) {
                width = 220;
                height = 220;
            }
            if (imageForm.getTemplate().equals("template2")) {
                width = 220;
                height = 220;
            }
            if (imageForm.getTemplate().equals("template3")) {
                width = 220;
                height = 220;
            }
            if (imageForm.getTemplate().equals("template4")) {
                width = 220;
                height = 220;
            }
        }
        ImageUrl crop = imageService.getUrl(imageForm.getImageId()).crop(imageForm.getX(), imageForm.getY(), imageForm.getWidth(), imageForm.getHeight());
        if (crop != null) {
            Response download = null;
            if (imageForm.getTemplate() != null && !imageForm.getTemplate().equals("")) {
                download = imageService.download(crop.thumbnail(width, height, "outbound").getUrl(), ImageType.BIG_LANDING, imageForm.getTargetId());
            } else {
                download = imageService.download(crop.getUrl(), ImageType.BIG_LANDING, imageForm.getTargetId());
            }
            if (download.isSuccess()) {
                Map<String, String> map = new HashMap<>();
                if (imageForm.getName() != null && !imageForm.getName().equals("")) {
                    BigLandingItem bigLandingItem = new BigLandingItem();
                    bigLandingItem.setId(imageForm.getTargetId());
                    bigLandingItem.setName(imageForm.getName());
                    bigLandingService.changeBigLandingItemName(bigLandingItem);
                }
                map.put("imageId", download.getData().toString());
                map.put("imageUrl", imageService.getUrl(download.getData().toString()).getUrl());
                imageService.deleteById(ImageType.BIG_LANDING, imageForm.getTargetId(), imageForm.getImageId());
                return new Response(true, "Thành công", map);
            }
        }
        return new Response(false, "Có lỗi xảy ra trong quá trình cắt ảnh");
    }

    @RequestMapping(value = "/getitembycate", method = RequestMethod.GET)
    @ResponseBody
    public Response getItemByCategory(@RequestParam(value = "bigLandingCateId", defaultValue = "") String bigLandingCateId, @ModelAttribute ItemSearch itemSearch) {

        List<String> cIds = new ArrayList<>();
        cIds.add(bigLandingCateId);
        itemSearch.setPageIndex(itemSearch.getPageIndex() > 0 ? itemSearch.getPageIndex() - 1 : 0);
        PageRequest request = new PageRequest(itemSearch.getPageIndex(), itemSearch.getPageSize());
        DataPage<BigLandingItem> listItems = bigLandingService.getItemByCategory(cIds, request);
        return new Response(true, "ok", listItems);
    }

    @RequestMapping(value = "/changebiglandingitem", method = RequestMethod.POST)
    @ResponseBody
    public Response changebiglandingitem(@RequestBody BigLandingItem bigLandingItem) throws Exception {
        return bigLandingService.changeBigLandingItem(bigLandingItem);
    }

    @RequestMapping(value = "/changebiglandingitemname", method = RequestMethod.POST)
    @ResponseBody
    public Response changebiglandingitemname(@RequestBody BigLandingItem bigLandingItem) throws Exception {
        return new Response(true, "Cập nhật sản phẩm thành công", bigLandingService.changeBigLandingItemName(bigLandingItem));
    }

    @RequestMapping(value = "/delbiglandingitem", method = RequestMethod.GET)
    @ResponseBody
    public Response delbiglandingitem(@RequestParam String id) throws Exception {
        return bigLandingService.deleteItem(id);
    }

    @RequestMapping(value = "/delbiglandingcategory", method = RequestMethod.GET)
    @ResponseBody
    public Response delbiglandingcategory(@RequestParam String id) throws Exception {
        return bigLandingService.deleteCategory(id);
    }

    @RequestMapping(value = "/uploadbanner", method = RequestMethod.POST)
    @ResponseBody
    public Response uploadbanner(@ModelAttribute ImageForm imageForm, @RequestParam String targetId) throws Exception {
        if (targetId != null && !targetId.equals("")) {
            imageForm.setTargetId(targetId);
        }
        if (imageForm.getTargetId() != null && !imageForm.getTargetId().equals("")) {
            Response upload = null;
            if (imageForm.getImage() != null && imageForm.getImage().getSize() > 0) {
                imageService.delete(ImageType.BIG_LANDING, imageForm.getTargetId());
                upload = imageService.upload(imageForm.getImage(), ImageType.BIG_LANDING, imageForm.getTargetId());
            }
            if (upload == null || !upload.isSuccess()) {
                return new Response(false, "Lỗi up ảnh: " + upload.getMessage());
            }
            Map<String, String> map = new HashMap<>();
            map.put("imageId", upload.getData().toString());
            map.put("imageUrl", imageService.getUrl(upload.getData().toString()).getUrl());
            return new Response(true, "upload thành công", map);
        }
        return new Response(false, "Ảnh banner phải được chọn");
    }

    @RequestMapping(value = "/getbiglandingitembycustom", method = RequestMethod.GET)
    @ResponseBody
    public Response getbiglandingitembycustom(@RequestParam(value = "bigLandingCateId", defaultValue = "") String bigLandingCateId, @RequestParam(value = "position", defaultValue = "") int position) throws Exception {
        return new Response(true, "Thông tin item", bigLandingService.getItemByCustomer(bigLandingCateId, position));
    }

    @RequestMapping(value = "/uploadlandingbanner", method = RequestMethod.POST)
    @ResponseBody
    public Response uploadlandingbanner(@ModelAttribute ImageForm imageForm, @RequestParam String targetId, int type) throws Exception {
        if (targetId != null && !targetId.equals("")) {
            imageForm.setTargetId(targetId);
        }
        if (imageForm.getTargetId() != null && !imageForm.getTargetId().equals("")) {
            Response upload = null;
            if (imageForm.getImage() != null && imageForm.getImage().getSize() > 0) {
                if (type == 1) {
                    imageService.delete(ImageType.LANDING_LOGO_BANNER, imageForm.getTargetId());
                    upload = imageService.upload(imageForm.getImage(), ImageType.LANDING_LOGO_BANNER, imageForm.getTargetId());
                }
                if (type == 2) {
                    imageService.delete(ImageType.LANDING_HEART_BANNER, imageForm.getTargetId());
                    upload = imageService.upload(imageForm.getImage(), ImageType.LANDING_HEART_BANNER, imageForm.getTargetId());
                }
                if (type == 3) {
                    imageService.delete(ImageType.LANDING_CENTER_BANNER, imageForm.getTargetId());
                    upload = imageService.upload(imageForm.getImage(), ImageType.LANDING_CENTER_BANNER, imageForm.getTargetId());
                }
            }
            if (upload == null || !upload.isSuccess()) {
                return new Response(false, "Lỗi up ảnh: " + upload.getMessage());
            }
            Map<String, String> map = new HashMap<>();
            map.put("imageId", upload.getData().toString());
            map.put("imageUrl", imageService.getUrl(upload.getData().toString()).getUrl());
            map.put("type", imageService.getUrl(upload.getData().toString()).getUrl());
            return new Response(true, "upload thành công", map);
        }
        return new Response(false, "Ảnh banner phải được chọn");
    }

}
