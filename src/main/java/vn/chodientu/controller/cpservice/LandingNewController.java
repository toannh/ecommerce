/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import vn.chodientu.entity.db.BigLandingItem;
import vn.chodientu.entity.db.LandingNewItem;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.form.ImageForm;
import vn.chodientu.entity.form.LandingNewForm;
import vn.chodientu.entity.form.LandingNewSlideForm;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.LandingNewService;

@Controller("cpServiceLandingNew")
@RequestMapping(value = "/cpservice/landingnew")
public class LandingNewController extends BaseRest {

    @Autowired
    private LandingNewService landingNewService;
    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Response add(@ModelAttribute LandingNewForm landingNewForm) {
        return landingNewService.addLandingNew(landingNewForm);

    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Response add(@RequestParam String landingNewId) throws Exception {
        return landingNewService.getLandingNewById(landingNewId);

    }

    @RequestMapping(value = "/changeactive", method = RequestMethod.GET)
    @ResponseBody
    public Response changeactive(@RequestParam String landingNewId) throws Exception {
        return landingNewService.changeActiveLandingNew(landingNewId);

    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    @ResponseBody
    public Response del(@RequestParam String landingNewId) throws Exception {
        return landingNewService.del(landingNewId);

    }

    @RequestMapping(value = "/delitem", method = RequestMethod.GET)
    @ResponseBody
    public Response delbiglandingitem(@RequestParam String id) throws Exception {
        return landingNewService.deleteItem(id);
    }

    @RequestMapping(value = "/changelandingnewitem", method = RequestMethod.POST)
    @ResponseBody
    public Response changelandingnewitem(@RequestBody LandingNewItem landingNewItem) throws Exception {
        return landingNewService.changeLandingNewItem(landingNewItem);
    }

    @RequestMapping(value = "/getitembylanding", method = RequestMethod.GET)
    @ResponseBody
    public Response getitembylanding(@RequestParam(value = "landingNewId", defaultValue = "") String landingNewId, @ModelAttribute ItemSearch itemSearch) {

        itemSearch.setPageIndex(itemSearch.getPageIndex() > 0 ? itemSearch.getPageIndex() - 1 : 0);
        PageRequest request = new PageRequest(itemSearch.getPageIndex(), itemSearch.getPageSize());
        DataPage<LandingNewItem> listItems = landingNewService.getItemByLandingNew(landingNewId, request);
        return new Response(true, "ok", listItems);
    }

    @RequestMapping(value = "/addlandingnewitem", method = RequestMethod.POST)
    @ResponseBody
    public Response addBigLandingCategory(@RequestBody LandingNewItem landingNewItem) throws Exception {
        return landingNewService.addItem(landingNewItem);
    }

    @RequestMapping(value = "/cropimage", method = RequestMethod.POST)
    @ResponseBody
    public Response addItem(@RequestBody ImageForm imageForm) throws Exception {
        int width = 0;
        int height = 0;
        width = 232;
        height = 232;
        ImageUrl crop = imageService.getUrl(imageForm.getImageId()).crop(imageForm.getX(), imageForm.getY(), imageForm.getWidth(), imageForm.getHeight());
        if (crop != null) {
            Response download = null;
            if (imageForm.getWidth() > 0 && imageForm.getHeight() > 0) {
                download = imageService.download(crop.thumbnail(width, height, "outbound").getUrl(), ImageType.LANDING_NEW_ITEM, imageForm.getTargetId());
            } else {
                download = imageService.download(crop.getUrl(), ImageType.LANDING_NEW_ITEM, imageForm.getTargetId());
            }
            if (download.isSuccess()) {
                Map<String, String> map = new HashMap<>();
                if (imageForm.getName() != null && !imageForm.getName().equals("")) {
                    LandingNewItem landingNewItem = new LandingNewItem();
                    landingNewItem.setId(imageForm.getTargetId());
                    landingNewItem.setName(imageForm.getName());
                    landingNewService.changeLandingItemName(landingNewItem);
                }
                map.put("imageId", download.getData().toString());
                map.put("imageUrl", imageService.getUrl(download.getData().toString()).getUrl());
                imageService.deleteById(ImageType.LANDING_NEW_ITEM, imageForm.getTargetId(), imageForm.getImageId());
                return new Response(true, "Thành công", map);
            }
        }
        return new Response(false, "Có lỗi xảy ra trong quá trình cắt ảnh");
    }

    @RequestMapping(value = "/cropimageselide", method = RequestMethod.POST)
    @ResponseBody
    public Response cropimageselide(@RequestBody ImageForm imageForm) throws Exception {
        int width = 0;
        int height = 0;
        width = 80;
        height = 80;
        ImageUrl crop = imageService.getUrl(imageForm.getImageId()).crop(imageForm.getX(), imageForm.getY(), imageForm.getWidth(), imageForm.getHeight());
        if (crop != null) {
            Response download = null;
            if (imageForm.getWidth() > 0 && imageForm.getHeight() > 0) {
                download = imageService.download(crop.thumbnail(width, height, "outbound").getUrl(), ImageType.LANDING_NEW_SLIDE, imageForm.getTargetId());
            } else {
                download = imageService.download(crop.getUrl(), ImageType.LANDING_NEW_SLIDE, imageForm.getTargetId());
            }
            if (download.isSuccess()) {
                Map<String, String> map = new HashMap<>();
                map.put("imageId", download.getData().toString());
                map.put("imageUrl", imageService.getUrl(download.getData().toString()).getUrl());
                imageService.deleteById(ImageType.LANDING_NEW_SLIDE, imageForm.getTargetId(), imageForm.getImageId());
                return new Response(true, "Crop thành công", map);
            }
        }
        return new Response(false, "Có lỗi xảy ra trong quá trình cắt ảnh");
    }

    @RequestMapping(value = "/uploadimage", method = RequestMethod.POST)
    @ResponseBody
    public Response uploadimageItem(@ModelAttribute ImageForm imageForm) throws Exception {
        if (imageForm.getTargetId() != null && !imageForm.getTargetId().equals("")) {
            List<String> images = imageService.get(ImageType.LANDING_NEW_ITEM, imageForm.getTargetId());
            Response upload = imageService.upload(imageForm.getImage(), ImageType.LANDING_NEW_ITEM, imageForm.getTargetId());
            if (upload == null || !upload.isSuccess()) {
                return new Response(false, "Lỗi up ảnh: " + upload.getMessage());
            }
            for (String image : images) {
                imageService.deleteById(ImageType.LANDING_NEW_ITEM, imageForm.getTargetId(), image);
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
            List<String> images = imageService.get(ImageType.LANDING_NEW_ITEM, imageForm.getTargetId());
            Response download = imageService.download(imageForm.getImageUrl(), ImageType.LANDING_NEW_ITEM, imageForm.getTargetId());
            if (download == null || !download.isSuccess()) {
                return new Response(false, "Lỗi download ảnh: " + download.getMessage());
            }
            for (String image : images) {
                imageService.deleteById(ImageType.LANDING_NEW_ITEM, imageForm.getTargetId(), image);
            }
            Map<String, String> map = new HashMap<>();
            map.put("imageId", download.getData().toString());
            map.put("imageUrl", imageService.getUrl(download.getData().toString()).getUrl());
            return new Response(true, "upload thành công", map);
        }
        return new Response(false, "Sản phẩm Landing phải được chọn");
    }

    @RequestMapping(value = "/changelandingitemname", method = RequestMethod.POST)
    @ResponseBody
    public Response changelandingitemname(@RequestBody LandingNewItem landingNewItem) throws Exception {
        return new Response(true, "Cập nhật sản phẩm thành công", landingNewService.changeLandingItemName(landingNewItem));
    }

    @RequestMapping(value = "/addslide", method = RequestMethod.POST)
    @ResponseBody
    public Response addslide(@ModelAttribute LandingNewSlideForm landingNewSlideForm) {
        return landingNewService.addLandingNewSlide(landingNewSlideForm);

    }

    @RequestMapping(value = "/getslide", method = RequestMethod.GET)
    @ResponseBody
    public Response getslide(@RequestParam String id) throws Exception {
        return landingNewService.getLandingNewSlideById(id);

    }

    @RequestMapping(value = "/delslide", method = RequestMethod.GET)
    @ResponseBody
    public Response delslide(@RequestParam String id) throws Exception {
        return landingNewService.delLandingNewSlide(id);

    }

    @RequestMapping(value = "/changeactiveslide", method = RequestMethod.GET)
    @ResponseBody
    public Response changeactiveslide(@RequestParam String id) throws Exception {
        return landingNewService.changeActiveLandingNewSlide(id);

    }

    @RequestMapping(value = "/changepositionslide", method = RequestMethod.GET)
    @ResponseBody
    public Response changepositionslide(@RequestParam String id, @RequestParam int position) throws Exception {
        return landingNewService.changePositionLandingNewSlide(id, position);

    }
}
