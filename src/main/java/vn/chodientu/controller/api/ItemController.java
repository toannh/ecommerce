package vn.chodientu.controller.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.component.imboclient.Url.ImageUrl;
import vn.chodientu.entity.api.Request;
import vn.chodientu.entity.data.KeyVal64;
import vn.chodientu.entity.db.Category;
import vn.chodientu.entity.db.ImageQueue;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.ItemCrawlHistory;
import vn.chodientu.entity.db.ItemCrawlLog;
import vn.chodientu.entity.db.ItemDetail;
import vn.chodientu.entity.db.Model;
import vn.chodientu.entity.db.Promotion;
import vn.chodientu.entity.db.PromotionItem;
import vn.chodientu.entity.db.SellerApi;
import vn.chodientu.entity.db.ShopCategory;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.enu.Condition;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.enu.ItemSource;
import vn.chodientu.entity.enu.ListingType;
import vn.chodientu.entity.enu.ShipmentType;
import vn.chodientu.entity.form.ItemForm;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.ItemCrawlHistoryRepository;
import vn.chodientu.repository.ItemCrawlLogRepository;
import vn.chodientu.repository.PromotionItemRepository;
import vn.chodientu.service.ApiHistoryService;
import vn.chodientu.service.CategoryService;
import vn.chodientu.service.ImageQueueService;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.ItemService;
import vn.chodientu.service.ModelService;
import vn.chodientu.service.PromotionService;
import vn.chodientu.service.SearchIndexService;
import vn.chodientu.service.ShopCategoryService;
import vn.chodientu.service.UserService;
import vn.chodientu.util.JsonUtils;
import vn.chodientu.util.TextUtils;

@Controller("itemApiController")
@RequestMapping(value = "/api/item")
public class ItemController extends BaseApi {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private Gson gson;
    @Autowired
    private ApiHistoryService apiHistoryService;
    @Autowired
    private ImageQueueService imageQueueService;
    @Autowired
    private ItemCrawlHistoryRepository itemCrawlHistoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private PromotionItemRepository promItemRespo;
    @Autowired
    private PromotionService promService;
    @Autowired
    private SearchIndexService searchIndexService;
    @Autowired
    private ItemCrawlLogRepository crawlLogRepository;

    public Response submitItemCrawl(List<KeyVal64> listKeyVal64, ItemCrawlLog crawlLog, Item tempItem) {
        for (KeyVal64 keyval : listKeyVal64) {
            if (keyval.getKeyword() == null || keyval.getKeyword().equals("")) {
                continue;
            }
            String value = StringUtils.newStringUtf8(Base64.decodeBase64(keyval.getValue()));
            if (value != null && !value.equals("")) {
                value = value.trim();
            }
            switch (keyval.getKeyword().toLowerCase().trim()) {
                case "sourcelink":
                    if (keyval.getValue() == null || keyval.getValue().equals("")) {
                        crawlLog.getErrorCode().add("ERR_CRAWL_001");
                    } else {
                        crawlLog.setSourceLink(value);
                    }
                    tempItem.setSellerSku(value);
                    break;
                case "title":
                    if (keyval.getValue() == null || keyval.getValue().equals("")) {
                        tempItem.setName("");
                        crawlLog.getAlertCode().add("ALERT_CRAWL_001");
                    } else {
                        if (JsonUtils.isJsonArray(value)) {
                            List<String> listNames = gson.fromJson(value, new TypeToken<List<String>>() {
                            }.getType());
                            String nameResult = "";
                            if (listNames != null) {
                                for (String name : listNames) {
                                    nameResult += name;
                                }
                                tempItem.setName(nameResult);
                            } else {
                                tempItem.setName("");
                                crawlLog.getAlertCode().add("ALERT_CRAWL_001");
                            }
                        } else {
                            tempItem.setName(value);
                        }
                    }
                    break;
                case "content":
                    tempItem.setDetail(value);
                    if (value == null || value.equals("")) {
                        crawlLog.getAlertCode().add("ALERT_CRAWL_002");
                    }
                    break;
                case "images":
                    if (keyval.getValue() == null || keyval.getValue().equals("")) {
                        tempItem.setImages(new ArrayList<String>());
                        break;
                    }
                    if (!JsonUtils.isJSONValid(value)) {
                        List<String> images = new ArrayList<>();
                        images.add(value);
                        tempItem.setImages(images);
                        break;
                    } else {
                        List<String> images = gson.fromJson(value, new TypeToken<List<String>>() {
                        }.getType());
                        tempItem.setImages(images);
                        break;
                    }
                case "sellprice":
                    if (value != null && !value.equals("")) {
                        if (!TextUtils.isLongNumber(value)) {
                            crawlLog.getErrorCode().add("ERR_CRAWL_003");
                            break;
                        }
                        tempItem.setSellPrice(Long.parseLong(value));
                    } else {
                        tempItem.setSellPrice(0);
                    }
                    break;
                case "startprice":
                    if (value != null && !value.equals("")) {
                        if (!TextUtils.isLongNumber(value)) {
                            crawlLog.getErrorCode().add("ERR_CRAWL_004");
                            break;
                        }
                        tempItem.setStartPrice(Long.parseLong(value));
                    } else {
                        tempItem.setStartPrice(0);
                    }
                    break;
                case "sellerid":
                    tempItem.setSellerId(value);
                    if (value == null || value.equals("")) {
                        crawlLog.getAlertCode().add("ALERT_CRAWL_003");
                    } else {
                        crawlLog.setSellerId(value);
                    }
                    break;
                case "categoryid":
                    tempItem.setCategoryId(value);
                    if (value == null || value.equals("")) {
                        crawlLog.getAlertCode().add("ALERT_CRAWL_005");
                    }
                    break;
                case "scategoryid":
                    tempItem.setShopCategoryId(value);
                    break;
                default:
                    crawlLog.getErrorCode().add("ERR_CRAWL_006");
                    break;
            }
        }
        if (!crawlLog.getErrorCode().isEmpty()) {
            crawlLog.setStatus(false);
            crawlLogRepository.save(crawlLog);
            return new Response(false, "error", crawlLog.getErrorCode());
        } else {
            try {
                if (tempItem.getSellerSku() != null) {
                    Item found_item = itemService.findBySellerSku(tempItem.getSellerSku());
                    if (found_item != null) {
                        Response res = itemService.editCrawlItem(tempItem, found_item, crawlLog);
                        if (res.isSuccess()) {
                            crawlLog.setStatus(true);
                            crawlLogRepository.save(crawlLog);
                            return new Response(true, "OK", crawlLog.getItemId());
                        } else {
                            crawlLog.setStatus(false);
                            crawlLogRepository.save(crawlLog);
                            return new Response(false, "error", crawlLog.getErrorCode());
                        }
                    } else {
                        Response res = itemService.addCrawlItem(tempItem, crawlLog);
                        if (res.isSuccess()) {
                            crawlLog.setStatus(true);
                            crawlLogRepository.save(crawlLog);
                            return new Response(true, "OK", crawlLog.getItemId());
                        } else {
                            crawlLog.setStatus(false);
                            crawlLogRepository.save(crawlLog);
                            return new Response(false, "error", crawlLog.getErrorCode());
                        }
                    }
                } else {
                    Response res = itemService.addCrawlItem(tempItem, crawlLog);
                    if (res.isSuccess()) {
                        crawlLog.setStatus(true);
                        crawlLogRepository.save(crawlLog);
                        return new Response(true, "OK", crawlLog.getItemId());
                    } else {
                        crawlLog.setStatus(false);
                        crawlLogRepository.save(crawlLog);
                        return new Response(false, "error", crawlLog.getErrorCode());
                    }
                }
            } catch (Exception ex) {
                return new Response(false, "error", "Error in java code : " + ex.getMessage());
            }
        }
    }

    @RequestMapping(value = "/postlistitems", method = RequestMethod.POST)
    @ResponseBody
    public Response postListItem(@RequestBody Request data, ModelMap model) {
        long startTime = System.currentTimeMillis();
        String code = data.getCode();
        // code = base64("peacesoft");
        if (!code.equals("cGVhY2Vzb2Z0")) {
            return new Response(false, "No permission");
        }
        List<List<KeyVal64>> listItemBase64 = gson.fromJson(data.getParams(), new TypeToken<List<List<KeyVal64>>>() {
        }.getType());
        List<Item> result = new ArrayList<>();
        long total = listItemBase64.size();
        long successCount = 0;
        if (listItemBase64 != null && listItemBase64.size() > 0) {
            Item tempItem = new Item();
            ItemCrawlLog crawlLog = new ItemCrawlLog();
            crawlLog.setTime(startTime);
            List<Response> listResp = new ArrayList<>();
            for (List<KeyVal64> listKeyVal64 : listItemBase64) {
                crawlLog.setRequest(listKeyVal64);
                Response resp = submitItemCrawl(listKeyVal64, crawlLog, tempItem);
                listResp.add(resp);
                if (resp.isSuccess()) {
                    successCount++;
                }
            }
            long endTime = System.currentTimeMillis();
            Response response = new Response(true, "success", listResp);
            ItemCrawlHistory itemCrawlHistory = new ItemCrawlHistory();
            itemCrawlHistory.setRequest(data);
            itemCrawlHistory.setResponse(response);
            itemCrawlHistory.setStartTime(startTime);
            itemCrawlHistory.setEndTime(endTime);
            itemCrawlHistory.setTotal(total);
            itemCrawlHistory.setSuccessCount(successCount);
            itemCrawlHistoryRepository.save(itemCrawlHistory);
            return response;
        } else {
            Response response = new Response(false, "error", "Have no item");
            return response;
        }
    }

    @RequestMapping(value = "/listbyids", method = RequestMethod.POST)
    @ResponseBody
    public Response listByIds(@RequestBody Request data, ModelMap model
    ) {
        Response response;
        try {
            List<String> listIds = gson.fromJson(data.getParams(), new TypeToken<List<String>>() {
            }.getType());
            if (listIds == null || listIds.size() == 0) {
                return new Response(false, "Tham số chưa chính xác");
            }
            DataPage<Item> dataPage = itemService.searchByIds(listIds);
            for (Item it : dataPage.getData()) {
                List<String> img = new ArrayList<>();
                if (it != null && it.getImages() != null && !it.getImages().isEmpty()) {
                    for (String ig : it.getImages()) {
                        img.add(imageService.getUrl(ig).compress(100).getUrl(it.getName()));
                    }
                    it.setImages(img);
                }
            }
            response = new Response(true, "Danh sách sản phẩm", dataPage);
        } catch (Exception e) {
            response = new Response(false, e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Response list(@RequestBody Request data, ModelMap model
    ) {
        Response response;
        User user = null;
        try {
            validate(data, model);
            user = (User) model.get("user");
            SellerApi sellerApi = (SellerApi) model.get("sellerApi");
            ItemSearch search = gson.fromJson(data.getParams(), ItemSearch.class
            );
            if (search
                    == null) {
                return new Response(false, "Tham số chưa chính xác");
            }

            if (search.getPageSize()
                    <= 0) {
                search.setPageSize(100);
            }

            search.setSellerId(user.getId());
            DataPage<Item> dataPage = itemService.search(search);
            for (Item it : dataPage.getData()) {
                List<String> img = new ArrayList<>();
                if (it != null && it.getImages() != null && !it.getImages().isEmpty()) {
                    for (String ig : it.getImages()) {
                        img.add(imageService.getUrl(ig).compress(100).getUrl(it.getName()));
                    }
                    it.setImages(img);
                }
            }
            response = new Response(true, "Danh sách sản phẩm", dataPage);
        } catch (Exception e) {
            response = new Response(false, e.getMessage());
        }
        apiHistoryService.create(data, user, response);
        return response;
    }

    @RequestMapping(value = "/additem", method = RequestMethod.POST)
    @ResponseBody
    public Response addItem(@RequestBody Request data, ModelMap model
    ) {
        Response response;
        User user = null;
        try {
            validate(data, model);
            user = (User) model.get("user");
            SellerApi sellerApi = (SellerApi) model.get("sellerApi");
            Item item = gson.fromJson(data.getParams(), Item.class
            );
            if (item
                    == null) {
                return new Response(false, "Tham số chưa chính xác", "PARAM_EMPTY");
            }

            if (item.getSellerSku()
                    == null) {
                return new Response(false, "Link gốc sản phẩm chưa được nhập", "SELLERSKU_EMPTY");
            }
            Item oldItem = itemService.findBySellerSku(item.getSellerSku());

            item.setSellerId(user.getId());
            itemService.add(item);
            response = new Response(true, "Đã tạo sản phẩm thành công trên chợ", item);
        } catch (Exception e) {
            response = new Response(false, e.getMessage());
        }
        apiHistoryService.create(data, user, response);
        return response;
    }

    @RequestMapping(value = "/addimages", method = RequestMethod.POST)
    @ResponseBody
    public Response addImages(@RequestBody Request data, ModelMap model
    ) {
        Response response;
        User user = null;
        try {
            validate(data, model);
            user = (User) model.get("user");
            SellerApi sellerApi = (SellerApi) model.get("sellerApi");
            ItemForm itemform = gson.fromJson(data.getParams(), ItemForm.class
            );
            if (itemform
                    == null) {
                return new Response(false, "Tham số chưa chính xác", "PARAM_EMPTY");
            }

            if (itemform.getSellerSku()
                    == null || itemform.getSellerSku().equals("")) {
                return new Response(false, "Link gốc sản phẩm chưa được nhập", "SELLERSKU_EMPTY");
            }

            if (itemform.getImageUrl()
                    == null || itemform.getImageUrl().equals("")) {
                return new Response(false, "Link ảnh cần đăng cho sản phẩm chưa được nhập", "IMAGEURL_EMPTY");
            }
            Item item = itemService.findBySellerSku(itemform.getSellerSku(), user.getId());
            if (item
                    == null) {
                return new Response(false, "Không tìm thấy sản phẩm!");
            }
            List<String> images = item.getImages();
            if (images
                    == null) {
                images = new ArrayList<>();
            }

            if (itemform.getImageUrl()
                    != null && !itemform.getImageUrl().equals("")) {
                if (imageService.checkImage(itemform.getImageUrl(), null, 300, 300, false)) {
                    Response<String> resp = imageService.download(itemform.getImageUrl().trim(), ImageType.ITEM, item.getId());
                    if (resp == null || !resp.isSuccess()) {
                        return new Response(false, resp.getMessage());
                    }
                    images.add(resp.getData());
                } else {
                    return new Response(false, "Ảnh phải có kích thước tối thiểu 1 chiều là 300px");
                }
            } else if (itemform.getImage()
                    != null && itemform.getImage().getSize() > 0) {
                if (imageService.checkImage(null, itemform.getImage(), 300, 300, false)) {
                    Response resp = imageService.upload(itemform.getImage(), ImageType.ITEM, item.getId());
                    if (resp == null || !resp.isSuccess()) {
                        return new Response(false, resp.getMessage());
                    }
                    images.add((String) resp.getData());
                } else {
                    return new Response(false, "Ảnh phải có kích thước tối thiểu 1 chiều là 300px");
                }
            }

            item.setImages(images);
            List<String> img = new ArrayList<>();
            if (item
                    != null && item.getImages()
                    != null && !item.getImages().isEmpty()) {
                for (String ig : item.getImages()) {
                    img.add(imageService.getUrl(ig).compress(100).getUrl(item.getName()));
                }
                item.setImages(img);
            }
            response = new Response(true, "Đã thêm ảnh cho sản phẩm thành công trên chợ", item);
        } catch (Exception e) {
            response = new Response(false, e.getMessage());
        }
        apiHistoryService.create(data, user, response);
        return response;
    }

    @RequestMapping(value = "/delimage", method = RequestMethod.POST)
    @ResponseBody
    public Response delImage(@RequestBody Request data, ModelMap model
    ) {
        Response response;
        User user = null;
        try {
            validate(data, model);
            user = (User) model.get("user");
            SellerApi sellerApi = (SellerApi) model.get("sellerApi");
            HashMap<String, String> params = gson.fromJson(data.getParams(), new TypeToken<HashMap<String, String>>() {
            }.getType());
            if (params == null) {
                return new Response(false, "Tham số chưa chính xác", "PARAM_EMPTY");
            }
            String sellerSku = params.get("sellerSku");
            String imageUrl = params.get("imageUrl");
            if (sellerSku == null || sellerSku.equals("")) {
                return new Response(false, "Link gốc sản phẩm chưa được nhập", "SELLERSKU_EMPTY");
            }
            Item item = itemService.findBySellerSku(sellerSku, user.getId());
            if (item == null) {
                return new Response(false, "Sản phẩm không tồn trên hệ thống!");
            }
            if (imageUrl == null || imageUrl.equals("")) {
                return new Response(false, "Link ảnh cần xóa của sản phẩm chưa được nhập", "IMAGEURL_EMPTY");
            }
            imageService.deleteByUrl(ImageType.ITEM, item.getId(), imageUrl);
            response = new Response(true, "Xóa ảnh thành công");
        } catch (Exception e) {
            response = new Response(false, e.getMessage());
        }
        apiHistoryService.create(data, user, response);
        return response;
    }

    @RequestMapping(value = "/addsubmit", method = RequestMethod.POST)
    @ResponseBody
    public Response addSubmit(@RequestBody Request data, ModelMap model
    ) {
        Response response;
        User user = null;
        try {
            validate(data, model);
            user = (User) model.get("user");
            SellerApi sellerApi = (SellerApi) model.get("sellerApi");
            ItemForm params = gson.fromJson(data.getParams(), ItemForm.class
            );
            if (params
                    == null) {
                return new Response(false, "Tham số chưa chính xác");
            }

            if (params.getSellerSku()
                    == null || params.getSellerSku().equals("")) {
                return new Response(false, "Link gốc sản phẩm chưa được nhập", "SELLERSKU_EMPTY");
            }
            Item item = itemService.findBySellerSku(params.getSellerSku(), user.getId());
            if (item
                    == null) {
                return new Response(false, "Sản phẩm không tồn tại trên hệ thống");
            }

            if (params.getDetail()
                    == null || params.getDetail().equals("")) {
                return new Response(false, "Detail sản phẩm không được để trống!");
            }

            item.setName(params.getName());
            item.setCategoryId(params.getCategoryId());
            item.setQuantity(params.getQuantity());
            item.setListingType(params.getListingType());
            if (item.getCondition()
                    != null && !item.getCondition().equals("")) {
                item.setCondition(params.getCondition());
            } else {
                item.setCondition(Condition.NEW);
            }

            item.setShopCategoryId(params.getShopCategoryId());
            item.setSource(ItemSource.API);

            item.setModelId(params.getModelId());
            item.setManufacturerId(params.getManufacturerId());
            item.setStartPrice(params.getStartPrice());
            item.setSellPrice(params.getSellPrice());
            item.setStartTime(params.getStartTime());
            item.setEndTime(params.getEndTime());
            item.setWeight(params.getWeight());
            item.setBidStep(params.getBidStep());
            item.setShipmentType(params.getShipmentType());
            item.setShipmentPrice(params.getShipmentPrice());

            ItemDetail itemDetail = new ItemDetail();

            itemDetail.setItemId(item.getId());
            itemDetail.setDetail(params.getDetail());
            itemService.updateDetail(itemDetail);
            response = itemService.submitAdd(item, null);
        } catch (Exception e) {
            response = new Response(false, e.getMessage());
        }
        apiHistoryService.create(data, user, response);
        return response;
    }

    @RequestMapping(value = "/getitem", method = RequestMethod.POST)
    @ResponseBody
    public Response getItem(@RequestBody Request data, ModelMap model
    ) {
        Response response;
        User user = null;
        try {
            validate(data, model);
            user = (User) model.get("user");
            SellerApi sellerApi = (SellerApi) model.get("sellerApi");
            HashMap<String, String> params = gson.fromJson(data.getParams(), new TypeToken<HashMap<String, String>>() {
            }.getType());
            if (params == null) {
                return new Response(false, "Tham số chưa chính xác");
            }
            String sellerSku = params.get("sellerSku");
            if (sellerSku == null || sellerSku.equals("")) {
                return new Response(false, "Link gốc sản phẩm chưa được nhập", "SELLERSKU_EMPTY");
            }
            Item item = itemService.findBySellerSku(sellerSku, user.getId());
            item.setImages(imageService.get(ImageType.ITEM, item.getId()));
            List<String> img = new ArrayList<>();
            if (item != null && item.getImages() != null && !item.getImages().isEmpty()) {
                for (String ig : item.getImages()) {
                    img.add(imageService.getUrl(ig).compress(100).getUrl(item.getName()));
                }
                item.setImages(img);
            }
            response = new Response(true, "Thông tin sản phẩm", item);
        } catch (Exception e) {
            response = new Response(false, e.getMessage());
        }
        apiHistoryService.create(data, user, response);
        return response;
    }

    @RequestMapping(value = "/editsubmit", method = RequestMethod.POST)
    @ResponseBody
    public Response editSubmit(@RequestBody Request data, ModelMap model
    ) {
        Response response;
        User user = null;
        try {
            validate(data, model);
            user = (User) model.get("user");
            SellerApi sellerApi = (SellerApi) model.get("sellerApi");
            ItemForm params = gson.fromJson(data.getParams(), ItemForm.class
            );
            if (params
                    == null) {
                return new Response(false, "Tham số chưa chính xác");
            }

            if (params.getSellerSku()
                    == null || params.getSellerSku().equals("")) {
                return new Response(false, "Link gốc sản phẩm chưa được nhập", "SELLERSKU_EMPTY");
            }
            Item item = itemService.findBySellerSku(params.getSellerSku(), user.getId());
            if (item
                    == null) {
                return new Response(false, "Sản phẩm không tồn tại!");
            }

            if (item.getListingType()
                    == ListingType.AUCTION) {
                return new Response(false, "Sản phẩm đấu giá không thể sửa!");
            }

            item.setName(params.getName());
            item.setCategoryId(params.getCategoryId());
            item.setQuantity(params.getQuantity());
            item.setListingType(params.getListingType());
            item.setCondition(params.getCondition());
            item.setShopCategoryId(params.getShopCategoryId());
            item.setModelId(params.getModelId());
            item.setManufacturerId(params.getManufacturerId());
            item.setStartPrice(params.getStartPrice());
            item.setSellPrice(params.getSellPrice());
            item.setStartTime(params.getStartTime());
            item.setEndTime(params.getEndTime());
            item.setWeight(params.getWeight());
            item.setBidStep(params.getBidStep());
            item.setShipmentType(params.getShipmentType());
            item.setShipmentPrice(params.getShipmentPrice());
            if (params.getDetail()
                    != null && !params.getDetail().equals("")) {
                ItemDetail itemDetail = new ItemDetail();
                itemDetail.setItemId(item.getId());
                itemDetail.setDetail(params.getDetail());
                itemService.updateDetail(itemDetail);
            }

            response = itemService.submitEdit(item, null);
        } catch (Exception e) {
            response = new Response(false, e.getMessage());
        }
        apiHistoryService.create(data, user, response);
        return response;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestBody Request data, ModelMap model
    ) {
        Response response;
        User user = null;
        try {
            validate(data, model);
            user = (User) model.get("user");
            SellerApi sellerApi = (SellerApi) model.get("sellerApi");
            HashMap<String, String> params = gson.fromJson(data.getParams(), new TypeToken<HashMap<String, String>>() {
            }.getType());
            if (params == null) {
                return new Response(false, "Tham số chưa chính xác");
            }
            String sellerSku = params.get("sellerSku");
            if (sellerSku == null || sellerSku.equals("")) {
                return new Response(false, "Link gốc sản phẩm chưa được nhập", "SELLERSKU_EMPTY");
            }
            Item item = itemService.findBySellerSku(sellerSku, user.getId());
            if (item == null) {
                return new Response(false, "Không tìm thấy sản phẩm nào!");
            }
            List<String> itemIds = new ArrayList<>();
            itemIds.add(item.getId());
            itemService.updateActive(itemIds, user.getId());
            response = new Response(true, "Xóa sản phẩm thành công,sản phẩm đã được đưa vào thùng rác!");
        } catch (Exception e) {
            response = new Response(false, e.getMessage());
        }
        apiHistoryService.create(data, user, response);
        return response;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public Response search(@RequestBody ItemSearch itemSearch
    ) {
        itemSearch.setStatus(1);
        if (itemSearch.getPageSize() == 0) {
            itemSearch.setPageSize(60);
        }
        DataPage<Item> itemPage = itemService.search(itemSearch);
        List<Item> list = new ArrayList<>();
        for (Item item : itemPage.getData()) {
            List<String> images = new ArrayList<>();
            if (item.getImages() != null && !item.getImages().isEmpty()) {
                for (String img : item.getImages()) {
                    ImageUrl url = imageService.getUrl(img).thumbnail(350, 350, "inset").compress(100);
                    if (url != null && url.getUrl() != null) {
                        images.add(url.getUrl(item.getName()));
                    }
                }
            }
            item.setImages(images);
        }
        return new Response(true, "Danh sách sản phẩm", itemPage);
    }

    @RequestMapping(value = "/postitem", method = RequestMethod.POST)
    @ResponseBody
    public Response postItem(@RequestBody Request data, ModelMap model
    ) {
        Response response;
        User user = null;
        try {
            validate(data, model);
            user = (User) model.get("user");
            SellerApi sellerApi = (SellerApi) model.get("sellerApi");
            Item params = gson.fromJson(data.getParams(), Item.class
            );
            if (params == null) {
                return new Response(false, "Tham số chưa chính xác");
            }
            Item item = itemService.findBySellerSku(params.getSellerSku(), user.getId());
            if (item != null) {
                if (item.getListingType() == ListingType.AUCTION) {
                    return new Response(false, "Sản phẩm đấu giá không thể sửa!");
                }
                List<PromotionItem> listPromItems = promItemRespo.getByItem(item.getId());
                if (listPromItems != null && !listPromItems.isEmpty()) {
                    for (PromotionItem promItem : listPromItems) {
                        Promotion promotion = promService.getPromotion(promItem.getPromotionId());
                        if (promotion != null) {
                            long currTime = System.currentTimeMillis();
                            if (currTime > promotion.getStartTime() && currTime < promotion.getEndTime() && promotion.isPublished() && promotion.isActive()) {
                                return new Response(false, "Sản phẩm đang chạy promotion không thể sửa!");
                            }
                        }
                    }
                }
                item.setQuantity(params.getQuantity());
                item.setSoldQuantity(params.getSoldQuantity());
                item.setStartPrice(params.getStartPrice());
                item.setSellPrice(params.getSellPrice());
                item.setUpdateTime(System.currentTimeMillis());
                item.setStartTime(params.getStartTime() / 1000 < System.currentTimeMillis() / 1000 ? System.currentTimeMillis() : params.getStartTime());
                item.setEndTime(TextUtils.getTime(System.currentTimeMillis(), 60));
                item.setCategoryId(params.getCategoryId());
                item.setShopCategoryId(params.getShopCategoryId());
                item.setSource(ItemSource.API);
                item.setModelId(params.getModelId());
                item.setManufacturerId(params.getManufacturerId());
                item.setGift(params.isGift());
                item.setGiftDetail(params.getGiftDetail());
                if (item.getImages() == null || item.getImages().isEmpty()) {
                    item.setImages(params.getImages());
                }
                if (params.getDetail() != null && !params.getDetail().equals("")) {
                    ItemDetail itemOld = itemService.getDetail(item.getId());
                    if (itemOld != null) {
                        String detailOld = itemOld.getDetail();
                        if (detailOld != null && !detailOld.equals("")) {
                            delImageDetail(detailOld, item);
                        }
                    }
                    ItemDetail itemDetail = new ItemDetail();
                    itemDetail.setItemId(item.getId());
                    String detail = updateImageUrl(params.getDetail(), item);
                    detail = updateImageUrlHost(params.getDetail(), item);
                    itemDetail.setDetail(detail);
                    itemService.updateDetail(itemDetail);
                }
                // dat khoi luong theo model -> danh muc shop -> danh muc
                if (item.getWeight() == 0) {
                    if (item.getModelId() != null && !item.getModelId().equals("")) {
                        Model modelItem = modelService.getModel(item.getModelId());
                        if (modelItem != null) {
                            item.setWeight(modelItem.getWeight());
                        }
                    }
                }
                if (item.getWeight() == 0) {
                    if (item.getShopCategoryId() != null && !item.getShopCategoryId().equals("")) {
                        ShopCategory shopCategoryItem = shopCategoryService.get(item.getShopCategoryId());
                        if (shopCategoryItem != null) {
                            item.setWeight(shopCategoryItem.getWeight());
                        }
                    }
                }
                if (item.getWeight() == 0) {
                    if (item.getCategoryId() != null && !item.getCategoryId().equals("")) {
                        Category categoryItem = categoryService.get(item.getCategoryId());
                        if (categoryItem != null) {
                            item.setWeight(categoryItem.getWeight());
                        }
                    }
                }
                if (item.getWeight() > 0) {
                    item.setShipmentType(ShipmentType.BYWEIGHT);
                } else {
                    item.setShipmentType(ShipmentType.AGREEMENT);
                }
                item.setName(params.getName());
                response = itemService.submitEditAPI(item, null);
            } else {
                params.setSellerId(user.getId());
                item = (Item) itemService.add(params).getData();
                item.setSource(ItemSource.API);
                try {
                    List<String> images = item.getImages();
                    if (images != null && images.size() > 0) {
                        for (String image : images) {
                            if (image != null && !image.equals("")) {
                                if (imageService.checkImage(image, null, 300, 300, false)) {
                                    Response<String> resp = imageService.downloadImageAPI(image.trim(), ImageType.ITEM, item.getId());
                                    if (resp == null || !resp.isSuccess()) {
                                        return new Response(false, resp.getMessage());
                                    }
                                    images.add(resp.getData());
                                } else {
                                    return new Response(false, "Ảnh phải có kích thước tối thiểu 1 chiều là 300px");
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }

                item.setStartTime(params.getStartTime() / 1000 < System.currentTimeMillis() / 1000 ? System.currentTimeMillis() : params.getStartTime());
                item.setEndTime(TextUtils.getTime(System.currentTimeMillis(), 60));

                if (params.getDetail() != null && !params.getDetail().equals("")) {
                    ItemDetail itemDetail = new ItemDetail();
                    itemDetail.setItemId(item.getId());
                    //String detail = updateImageUrl(params.getDetail(), item);
                    itemDetail.setDetail(params.getDetail());
                    itemService.updateDetail(itemDetail);
                }
                // dat khoi luong theo model -> danh muc shop -> danh muc
                if (item.getWeight() == 0) {
                    if (item.getModelId() != null && !item.getModelId().equals("")) {
                        Model modelItem = modelService.getModel(item.getModelId());
                        if (modelItem != null) {
                            item.setWeight(modelItem.getWeight());
                        }
                    }
                }
                if (item.getWeight() == 0) {
                    if (item.getShopCategoryId() != null && !item.getShopCategoryId().equals("")) {
                        ShopCategory shopCategoryItem = shopCategoryService.get(item.getShopCategoryId());
                        if (shopCategoryItem != null) {
                            item.setWeight(shopCategoryItem.getWeight());
                        }
                    }
                }
                if (item.getWeight() == 0) {
                    if (item.getCategoryId() != null && !item.getCategoryId().equals("")) {
                        Category categoryItem = categoryService.get(item.getCategoryId());
                        if (categoryItem != null) {
                            item.setWeight(categoryItem.getWeight());
                        }
                    }
                }
                if (item.getWeight() > 0) {
                    item.setShipmentType(ShipmentType.BYWEIGHT);
                } else {
                    item.setShipmentType(ShipmentType.AGREEMENT);
                }
                response = itemService.submitAddAPI(params, null);
            }

        } catch (Exception e) {
            response = new Response(false, e.getMessage());
        }
        apiHistoryService.create(data, user, response);
        return response;
    }

    /**
     * Thay thế link ảnh chi tiết
     *
     * @param detailString
     * @param item
     * @return
     * @throws java.net.URISyntaxException
     */
    public String updateImageUrl(String detailString, Item item) {
        try {
            String siteUrl = request.getHeader("referer");
            if (siteUrl != null) {
                String http = siteUrl.substring(0, siteUrl.indexOf("://") + 3);
                URI uri = new URI(siteUrl);
                String domain = http + uri.getHost();
                Pattern patt = Pattern.compile("(src)=\"\\/[\\/\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])\"");
                //Pattern pattUrl = Pattern.compile("src\\=\\\"(http|ftp|https):\\/\\/+(?!ichodientuvn)[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])\\\"");
                Matcher m = patt.matcher(detailString);
                StringBuffer sb = new StringBuffer(detailString.length());
                while (m.find()) {
                    String text = m.group();
                    String image = text.substring(5, text.lastIndexOf("\""));
                    if (image != null && !image.equals("")) {
                        image = domain + image;
                    }
                    Response<String> resp = imageService.download(image, ImageType.ITEM, item.getId());
                    if (resp == null || !resp.isSuccess()) {

                    } else {
                        image = resp.getData();
                    }
                    m.appendReplacement(sb, Matcher.quoteReplacement("src=\"" + image + "\""));
                }
                m.appendTail(sb);
                return sb.toString();
            }
        } catch (URISyntaxException ex) {
            return detailString;
        }
        return detailString;
    }

    public String updateImageUrlHost(String detailString, Item item) {
        String regex = "src=\\\"(http|ftp|https):\\/\\/+(?!ichodientuvn)[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])\\\"";
        Pattern pattUrl = Pattern.compile(regex);
        Matcher mUrl = pattUrl.matcher(detailString);
        StringBuffer sb = new StringBuffer(detailString.length());
        while (mUrl.find()) {
            String text = mUrl.group();
            String image = text.substring(5, text.lastIndexOf("\""));
            Response<String> resp = imageService.download(image, ImageType.ITEM, item.getId());
            if (resp == null || !resp.isSuccess()) {

            } else {
                image = imageService.getUrl(resp.getData()).getUrl(item.getName());
            }
            mUrl.appendReplacement(sb, Matcher.quoteReplacement("src=\"" + image + "\""));
        }
        mUrl.appendTail(sb);
        return sb.toString();
    }

    public void delImageDetail(String detailString, Item item) throws Exception {
        String strPatt = "src\\=\\\"(http|ftp|https):\\/\\/+(ichodientuvn)(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])\\\"";
        Pattern patt = Pattern.compile(strPatt);
        Matcher m = patt.matcher(detailString);
        while (m.find()) {
            String text = m.group();
            String image = text.substring(5, text.lastIndexOf("\""));
            imageService.deleteByUrl(ImageType.ITEM, item.getId(), image);
        }
    }

    @RequestMapping(value = "/getid", method = RequestMethod.POST)
    @ResponseBody
    public Response getId(@RequestParam(value = "name", defaultValue = "") String name) {
        return userService.getByUsername(name);
    }

}
