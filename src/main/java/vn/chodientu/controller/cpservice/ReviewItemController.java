/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.cpservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.BaseRest;
import vn.chodientu.entity.db.Category;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.ItemDetail;
import vn.chodientu.entity.db.ItemProperty;
import vn.chodientu.entity.db.ModelProperty;
import vn.chodientu.entity.db.Seller;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.form.ItemForm;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.CategoryService;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.ItemService;
import vn.chodientu.service.ModelService;
import vn.chodientu.util.TextUtils;

/**
 *
 * @author thunt
 */
@Controller("cpReviewItemService")
@RequestMapping(value = "/cpservice/reviewitem")
public class ReviewItemController extends BaseRest {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private Gson gson;
    @Autowired
    private CategoryService categoryService;

    /**
     * Sửa thông tin cơ bản
     *
     * @param itemForm
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Response edit(@RequestBody ItemForm itemForm) throws Exception {
        String adminId = "test";
        if (viewer != null && viewer.getAdministrator() != null) {
            adminId = viewer.getAdministrator().getId();
        }
        Item item = itemService.get(itemForm.getId());
        if (item == null) {
            item = new Item();
        }
        List<ModelProperty> properties = modelService.getProperties(itemForm.getModelId());
        if (properties != null && !properties.isEmpty()) {
            List<ItemProperty> ips = new ArrayList<>();
            ItemProperty itemProperty = null;
            for (ModelProperty modelProperty : properties) {
                itemProperty = new ItemProperty();
                itemProperty.setCategoryPropertyId(modelProperty.getCategoryPropertyId());
                itemProperty.setCategoryPropertyValueIds(modelProperty.getCategoryPropertyValueIds());
                itemProperty.setInputValue(modelProperty.getInputValue());
                itemProperty.setItemId(item.getId());
                ips.add(itemProperty);
            }
            itemService.updateProperties(item.getId(), ips);
        }
        convertToItem(item, itemForm);
        return itemService.edit(item, adminId);
    }

    /**
     * *
     * Duyệt sản phẩm lên sàn
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/submitedit", method = RequestMethod.GET)
    @ResponseBody
    public Response submitEdit(@RequestParam(value = "id", defaultValue = "") String id) throws Exception {
        String adminId = "test";
        if (viewer != null && viewer.getAdministrator() != null) {
            adminId = viewer.getAdministrator().getId();
        }
        Item item = itemService.get(id);
        return itemService.submitEdit(item, adminId);
    }

    /**
     * Sửa thuộc tính của sản phẩm
     *
     * @param properties
     * @return
     */
    @RequestMapping(value = "/editproperties", method = RequestMethod.POST)
    @ResponseBody
    public Response editProperties(@RequestBody Map<String, String> properties) {
        String itemId = properties.get("id");
        List<ItemProperty> listProperties = gson.fromJson(properties.get("properties"), new TypeToken<List<ItemProperty>>() {
        }.getType());

        try {
            itemService.updateProperties(itemId, listProperties);
        } catch (Exception ex) {
            return new Response(false, ex.getMessage());
        }
        return new Response(true, "Sửa thuộc tính thành công");
    }

    /**
     * Sửa phí vận chuyển
     *
     * @param itemForm
     * @return
     */
    @RequestMapping(value = "/editfeeship", method = RequestMethod.POST)
    @ResponseBody
    public Response editFeeShip(@RequestBody ItemForm itemForm) {
        String adminId = "test";
        if (viewer != null && viewer.getAdministrator() != null) {
            adminId = viewer.getAdministrator().getId();
        }
        try {
            Item item = itemService.get(itemForm.getId());
            item.setShipmentType(itemForm.getShipmentType());
            item.setShipmentPrice(itemForm.getShipmentPrice());
            Response submitEdit = itemService.edit(item, adminId);
            if (!submitEdit.isSuccess()) {
                return submitEdit;
            }
        } catch (Exception ex) {
            return new Response(false, ex.getMessage());
        }
        return new Response(true, "Sửa phí vận chuyển thành công");
    }

    /**
     * Thêm 1 ảnh Item
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/addimage", method = RequestMethod.POST)
    @ResponseBody
    public Response addImage(@ModelAttribute ItemForm form) {
        Item item;
        try {
            item = itemService.get(form.getId());
        } catch (Exception ex) {
            return new Response(false, "Sản phẩm không tồn tại");
        }
        List<String> images = item.getImages();
        List<String> itemImages = new ArrayList<String>();

        if (images != null && !images.isEmpty()) {
            for (String img : images) {
                itemImages.add(imageService.getUrl(img).thumbnail(100, 100, "inset").getUrl());
            }
        }
        if (images == null) {
            images = new ArrayList<>();
        }
        if (form.getImageUrl() != null && !form.getImageUrl().equals("")) {
            Response<String> resp = imageService.download(form.getImageUrl().trim(), ImageType.ITEM, item.getId());
            if (resp == null || !resp.isSuccess()) {
                return new Response(false, resp.getMessage());
            }
            itemImages.add(imageService.getUrl(resp.getData()).thumbnail(100, 100, "inset").getUrl());
        } else if (form.getImage() != null && form.getImage().getSize() > 0) {
            Response resp = imageService.upload(form.getImage(), ImageType.ITEM, item.getId());
            if (resp == null || !resp.isSuccess()) {
                return new Response(false, resp.getMessage());
            }
            itemImages.add(imageService.getUrl((String) resp.getData()).thumbnail(100, 100, "inset").getUrl());
        }
        item.setImages(itemImages);
        return new Response(true, "Thêm ảnh thành công", item);
    }

    /**
     * Xóa 1 ảnh Item
     *
     * @param id
     * @param imageName
     * @return
     */
    @RequestMapping(value = "/delimage", method = RequestMethod.GET)
    @ResponseBody
    public Response delImage(@RequestParam(value = "id", defaultValue = "") String id,
            @RequestParam(value = "name", defaultValue = "") String imageName) throws Exception {
        Item item = itemService.get(id);
        if (imageName == null || imageName.trim().equals("")) {
            return new Response(false, "Phải chọn ảnh để xóa");
        }
        imageService.deleteByUrl(ImageType.ITEM, item.getId(), imageName);
        try {
            return new Response(true, "Xóa ảnh thành công", item);
        } catch (Exception ex) {
            return new Response(false, ex.getMessage());
        }
    }

    private void convertToItem(Item item, ItemForm itemForm) {
        item.setId(itemForm.getId());
        item.setName(itemForm.getName());
        item.setCategoryId(itemForm.getCategoryId());
        item.setManufacturerId(itemForm.getManufacturerId());
        item.setModelId(itemForm.getModelId());
        item.setListingType(itemForm.getListingType());
        item.setStartPrice(itemForm.getStartPrice());
        item.setSellPrice(itemForm.getSellPrice());
        item.setQuantity(itemForm.getQuantity());
        item.setCondition(itemForm.getCondition());
        if (itemForm.getCityId() != null && !"".equals(itemForm.getCityId())) {
            item.setCityId(itemForm.getCityId());
        }
        item.setActive(itemForm.isActive());
        item.setWeight(itemForm.getWeight());
        item.setStartTime(itemForm.getStartTime());
        item.setEndTime(itemForm.getStartTime() + itemForm.getEndTime() * 24 * 60 * 60 * 1000l);
        item.setSellerName(itemForm.getSellerName());

    }

    /**
     * lấy detail của sản phẩm
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getdetail", method = RequestMethod.GET)
    @ResponseBody
    public Response getDetail(@RequestParam(value = "id", defaultValue = "") String id) {
        try {
            ItemDetail detail = itemService.getDetail(id);
            return new Response(true, null, detail);
        } catch (Exception e) {
            return new Response(false, e.getMessage());
        }

    }

    /**
     * Sửa detail của sản phẩm
     *
     * @param itemDetail
     * @return
     */
    @RequestMapping(value = "/editdetail", method = RequestMethod.POST)
    @ResponseBody
    public Response getdetail(@RequestBody ItemDetail itemDetail) {
        try {
            itemService.updateDetail(itemDetail);
            return new Response(true, "Đã sửa thành công");
        } catch (Exception e) {
            return new Response(false, e.getMessage());
        }

    }

    @RequestMapping(value = "/submitApprove", method = RequestMethod.POST)
    @ResponseBody
    public Response submitApprove(@RequestBody Map<String, String> idsList) throws Exception {
        List<String> listId = gson.fromJson(idsList.get("ids"), new TypeToken<List<String>>() {
            }.getType());
        if (listId == null || listId.isEmpty()) {
            return new Response(false, "Không có sản phẩm nào được truyền vào.");
        } else {
            String adminId = "test";
            if (viewer != null && viewer.getAdministrator() != null) {
                adminId = viewer.getAdministrator().getId();
            }
//            List<String> listId = gson.fromJson(ids, new TypeToken<List<String>>() {
//            }.getType());
            Map<String, Map<String, String>> listResult = new HashMap<String, Map<String, String>>();
            String strErr = "";
            String strSucc = "";
            for (String itemId : listId) {
                Item item = itemService.get(itemId);
                Response submitEdit = itemService.submitEdit(item, adminId);
                if (submitEdit.isSuccess()) {
                    strSucc += itemId + ",";
                    item.setCompleted(true);
                    itemService.save(item);
                } else {
                    listResult.put(item.getId(), (Map<String, String>) submitEdit.getData());
                    strErr += itemId + ",";
                }
            }
            if (listResult.isEmpty()) {
                return new Response(true, "Duyệt thành công tất cả các sản phẩm");
            } else if (listResult.size() != listId.size()) {
                return new Response(false,
                        "<p class=\"text-muted\">Duyệt thành công :" + (listId.size() - listResult.size()) + "</p>\n"
                        + "    <p class=\"text-danger\">Duyệt thất bại :" + listResult.size() + "</p>", listResult);
            } else {
                return new Response(false, "Không duyệt được sản phẩm nào.", listResult);
            }
        }
    }

    @RequestMapping(value = "/mapCateItems", method = RequestMethod.POST)
    @ResponseBody
    public Response submitMapCateItems(@RequestBody Map<String, String> mapIdsCate) throws Exception {
        List<String> listId = gson.fromJson(mapIdsCate.get("ids"), new TypeToken<List<String>>() {
            }.getType());
        String cateId = mapIdsCate.get("cateId");
        if (listId == null || listId.isEmpty()) {
            return new Response(false, "Không có sản phẩm nào được truyền vào.");
        } else {
//            List<String> listId = gson.fromJson(ids, new TypeToken<List<String>>() {
//            }.getType());
            Category cate = categoryService.get(cateId);
            if (cate == null) {
                return new Response(false, "Không tìm thấy danh mục tương ứng");
            }
            // sai ở đây vì chưa set category path
            for (String itemId : listId) {
                Item item = itemService.get(itemId);
                item.setCategoryId(cate.getId());
                item.setCategoryName(cate.getName());
                item.setCategoryPath(cate.getPath());
                itemService.save(item);
            }
            return new Response(true, "Map danh mục thành công");
        }
    }
    
    @RequestMapping(value = "/mapShopCateItems", method = RequestMethod.GET)
    @ResponseBody
    public Response submitMapShopCateItems(@RequestParam(value = "ids", defaultValue = "") String ids, @RequestParam(value = "shopCateId", defaultValue = "") String cateId) throws Exception {
        if (ids == null || ids.equals("")) {
            return new Response(false, "Không có sản phẩm nào được truyền vào.");
        } else {
            List<String> listId = gson.fromJson(ids, new TypeToken<List<String>>() {
            }.getType());
            Category cate = categoryService.get(cateId);
            if (cate == null) {
                return new Response(false, "Không tìm thấy danh mục tương ứng");
            }
            // sai ở đây vì chưa set category path
            for (String itemId : listId) {
                Item item = itemService.get(itemId);
                item.setCategoryId(cate.getId());
                item.setCategoryName(cate.getName());
                item.setCategoryPath(cate.getPath());
                itemService.save(item);
            }
            return new Response(true, "Map danh mục thành công");
        }
    }

    @RequestMapping(value = "/editNameItems", method = RequestMethod.POST)
    @ResponseBody
    public Response submitEditNameItems(@RequestBody Map<String, String> mapItemsName) throws Exception {
        List<String> listId = gson.fromJson(mapItemsName.get("ids"), new TypeToken<List<String>>() {
            }.getType());
        String prename = mapItemsName.get("prename");
        String sufname = mapItemsName.get("sufname");
        if (listId == null || listId.isEmpty()) {
            return new Response(false, "Không có sản phẩm nào được truyền vào.");
        } else {
            long countSuccess = 0;
            List<String> listError = new ArrayList<String>();
            String strErr = "";
            
            for (String itemId : listId) {
                Item item = itemService.get(itemId);
                if (prename != null && !prename.equals("")) {
                    item.setName(prename + item.getName());
                }
                if (sufname != null && !sufname.equals("")) {
                    item.setName(item.getName() + sufname);
                }
                if (item.getName().length() >= 180) {
                    listError.add(item.getId());
                    strErr += itemId + ",";
                } else {
                    countSuccess++;
                    itemService.save(item);
                }
            }
            if (listError.isEmpty()) {
                return new Response(true, "Sửa tên sản phẩm thành công");
            } else {
                return new Response(false, "Đã sửa tên thành công " + (listId.size() - listError.size()) + " sản phẩm  (trong tổng số " + listId.size() + " sản phẩm đã chọn). Các sản phẩm sau không sửa tên được (do quá 180 ký tự):[" + strErr.substring(0, strErr.length() - 1) + "]");
            }
        }
    }

    @RequestMapping(value = "/replaceNameItems", method = RequestMethod.POST)
    @ResponseBody
    public Response submitReplaceNameItems(@RequestBody Map<String, String> mapReplaces) throws Exception {
        List<String> listId = gson.fromJson(mapReplaces.get("ids"), new TypeToken<List<String>>() {
            }.getType());
        String pattern_name = mapReplaces.get("pattern_name");
        String replace_name = mapReplaces.get("replace_name");
        if (listId == null || listId.isEmpty()) {
            return new Response(false, "Không có sản phẩm nào được truyền vào.");
        } else {
            if (pattern_name == null || pattern_name.equals("")) {
                return new Response(false, "Cụm từ cần thay thế không được để trống");
            }
            long countSuccess = 0;
            List<String> listError = new ArrayList<String>();
            String strErr = "";
//            List<String> listId = gson.fromJson(ids, new TypeToken<List<String>>() {
//            }.getType());
            for (String itemId : listId) {
                Item item = itemService.get(itemId);
                item.setName(item.getName().replace(pattern_name, replace_name));
                if (item.getName().length() >= 180) {
                    listError.add(item.getId());
                    strErr += itemId + ",";
                } else {
                    countSuccess++;
                    itemService.save(item);
                }
            }
            if (listError.isEmpty()) {
                return new Response(true, "Sửa tên sản phẩm thành công");
            } else {
                return new Response(false, "Đã sửa tên thành công " + (listId.size() - listError.size()) + " sản phẩm  (trong tổng số " + listId.size() + " sản phẩm đã chọn). Các sản phẩm sau không sửa tên được (do quá 180 ký tự):[" + strErr.substring(0, strErr.length() - 1) + "]");
            }
        }
    }

    @RequestMapping(value = "/delItems", method = RequestMethod.POST)
    @ResponseBody
    public Response del(@RequestBody Map<String, String> mapIds) throws Exception {
        List<String> listId = gson.fromJson(mapIds.get("ids"), new TypeToken<List<String>>() {
            }.getType());
        if (listId == null || listId.isEmpty()) {
            return new Response(false, "Không có sản phẩm nào được truyền vào.");
        } else {
            List<String> listErr = new ArrayList<String>();
            String strErr = "";
            for (String itemId : listId) {
                Item item = itemService.get(itemId);
                boolean isRemoved = itemService.removeItem(itemId);
                if (!isRemoved) {
                    listErr.add(itemId);
                    strErr += itemId + ",";
                }
            }
            if (listErr.isEmpty()) {
                return new Response(true, "Xóa sản phẩm thành công");
            } else {
                return new Response(false, "Đã xóa " + (listId.size() - listErr.size()) + " sản phẩm thành công (trong tổng số " + listId.size() + " sản phẩm đã chọn). Các sản phẩm sau không xóa được :[" + strErr.substring(0, strErr.length() - 1) + "]");
            }
        }
    }

    //weight_net
    @RequestMapping(value = "/editWeightItems", method = RequestMethod.POST)
    @ResponseBody
    public Response submitEditWeightItems(@RequestBody Map<String, String> mapItemsWeight) throws Exception {
        List<String> listId = gson.fromJson(mapItemsWeight.get("ids"), new TypeToken<List<String>>() {
            }.getType());
        String weight_net = mapItemsWeight.get("weight_net");
        if (listId == null || listId.isEmpty()) {
            return new Response(false, "Không có sản phẩm nào được truyền vào.");
        } else {
            if (weight_net == null || weight_net.equals("")) {
                return new Response(false, "Trọng lượng sản phẩm không được để trống");
            } else if (!TextUtils.isLongNumber(weight_net)) {
                return new Response(false, "Trọng lượng sản phẩm phải là số nguyên");
            }
            for (String itemId : listId) {
                Item item = itemService.get(itemId);
                item.setWeight(Integer.parseInt(weight_net));
                itemService.save(item);
            }
            return new Response(true, "Sửa trọng lượng sản phẩm thành công");
        }
    }

}
