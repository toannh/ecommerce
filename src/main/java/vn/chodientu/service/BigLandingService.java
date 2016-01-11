package vn.chodientu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vn.chodientu.component.Validator;
import vn.chodientu.component.imboclient.Url.ImageUrl;
import vn.chodientu.entity.db.BigLanding;
import vn.chodientu.entity.db.BigLandingCategory;
import vn.chodientu.entity.db.BigLandingItem;
import vn.chodientu.entity.db.BigLandingSeller;
import vn.chodientu.entity.db.Image;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.LandingItem;
import vn.chodientu.entity.db.Promotion;
import vn.chodientu.entity.db.Seller;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.enu.PromotionType;
import vn.chodientu.entity.form.BigLandingForm;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.BigLandingCategoryRepository;
import vn.chodientu.repository.BigLandingItemRepository;
import vn.chodientu.repository.BigLandingRepository;
import vn.chodientu.repository.BigLandingSellerRepository;
import vn.chodientu.repository.ItemRepository;
import vn.chodientu.repository.PromotionRepository;
import vn.chodientu.repository.SellerRepository;

/**
 *
 * @author Anhpp
 */
@Service
public class BigLandingService {

    @Autowired
    private BigLandingCategoryRepository biglandingCategoryRepository;
    @Autowired
    private BigLandingItemRepository bigLandingItemRepository;
    @Autowired
    private BigLandingSellerRepository bigLandingSellerRepository;
    @Autowired
    private BigLandingRepository biglandingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private Validator validator;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ShopService shopService;

    /**
     * Lấy danh sách danh mục
     *
     * @param biglandingId
     * @param type
     * @return
     */
    public List<BigLandingCategory> getCategories(String biglandingId, boolean type) {
        List<BigLandingCategory> biglandingCategories;
        if (type) {
            biglandingCategories = biglandingCategoryRepository.getActiveByBigLanding(biglandingId);
        } else {
            biglandingCategories = biglandingCategoryRepository.getByBigLanding(biglandingId);
        }
        for (BigLandingCategory bigLandingCategory : biglandingCategories) {
            Image lastImage = imageService.getLastImage(bigLandingCategory.getId(), ImageType.BIG_LANDING);
            if (lastImage != null) {
                ImageUrl url = imageService.getUrl(lastImage.getImageId());
                bigLandingCategory.setImage(url.compress(100).getUrl(bigLandingCategory.getName()));
            }
        }
        return biglandingCategories;
    }

    /**
     * Lấy bản ghi BigLanding theo ID
     *
     * @param id
     * @return
     */
    public BigLanding getBigLanding(String id) throws Exception {
        BigLanding bigLanding = biglandingRepository.find(id);
        if (bigLanding == null) {
            throw new Exception("Landing này không tồn tại !");
        }
        Image lastImageHeartBN = imageService.getLastImage(id, ImageType.LANDING_HEART_BANNER);
        if (lastImageHeartBN != null) {
            ImageUrl url = imageService.getUrl(lastImageHeartBN.getImageId());
            bigLanding.setHeartBanner(url.compress(100).getUrl(bigLanding.getName()));
        }
        Image lastImageLOGOBN = imageService.getLastImage(id, ImageType.LANDING_LOGO_BANNER);
        if (lastImageLOGOBN != null) {
            ImageUrl url = imageService.getUrl(lastImageLOGOBN.getImageId());
            bigLanding.setLogoBanner(url.compress(100).getUrl(bigLanding.getName()));
        }
        Image lastImageCenterBN = imageService.getLastImage(id, ImageType.LANDING_CENTER_BANNER);
        if (lastImageCenterBN != null) {
            ImageUrl url = imageService.getUrl(lastImageCenterBN.getImageId());
            bigLanding.setCenterBanner(url.compress(100).getUrl(bigLanding.getName()));
        }
        return bigLanding;
    }

    /**
     *
     * Lấy danh sách danh mục theo mã danh mục cha
     *
     * @param parentId
     * @return
     */
    public List<BigLandingCategory> getCategoriesByParent(String parentId) {
        Criteria criteria = new Criteria("parentId").is(parentId);
        List<BigLandingCategory> bigLandingCategorys = biglandingCategoryRepository.find(new Query(criteria).with(new Sort(Sort.Direction.ASC, "position")));
        return bigLandingCategorys;
    }

    /**
     * ======= Lấy danh sách danh mục con
     *
     * @param cateId
     * @return
     */
    public List<BigLandingCategory> getCategoriesChild(String cateId) {
        List<BigLandingCategory> biglandingCategories = biglandingCategoryRepository.getByBigParentCate(cateId);
        for (BigLandingCategory bigLandingCategory : biglandingCategories) {
            Image lastImage = imageService.getLastImage(bigLandingCategory.getId(), ImageType.BIG_LANDING);
            if (lastImage != null) {
                ImageUrl url = imageService.getUrl(lastImage.getImageId());
                bigLandingCategory.setImage(url.compress(100).getUrl(bigLandingCategory.getName()));
            }
        }
        return biglandingCategories;
    }

    /**
     * Lấy danh sách danh mục
     *
     * @param bigLandingId
     * @return
     */
    public BigLandingCategory getCategory(String bigLandingId) throws Exception {
        BigLandingCategory biglandingCategory = biglandingCategoryRepository.find(bigLandingId);
        if (biglandingCategory == null) {
            throw new Exception("biglandingCategory này không tồn tại !");
        }
        List<String> listImg = imageService.get(ImageType.BIG_LANDING, bigLandingId);
        if (listImg != null && !listImg.isEmpty()) {
            biglandingCategory.setImage(imageService.getUrl(listImg.get(0)).compress(100).getUrl(biglandingCategory.getName()));
        }
        Criteria c = new Criteria("parentId").is(bigLandingId);
        List<BigLandingCategory> landingCategorys = biglandingCategoryRepository.find(new Query(c).with(new Sort(Sort.Direction.ASC, "position")));
        List<BigLandingItem> bigLandingItems = bigLandingItemRepository.find(new Query(new Criteria("bigLandingCategoryId").is(bigLandingId).and("featured").is(true)));
        List<String> itemIds = new ArrayList<>();
        for (BigLandingItem bigLandingItem : bigLandingItems) {
            itemIds.add(bigLandingItem.getId());
        }
        Map<String, List<String>> get = imageService.get(ImageType.BIG_LANDING, itemIds);
        if (get != null && !get.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : get.entrySet()) {
                for (BigLandingItem bigLandingItem : bigLandingItems) {
                    if (bigLandingItem.getId().equals(entry.getKey()) && entry.getValue() != null && !entry.getValue().isEmpty()) {
                        String url = imageService.getUrl(entry.getValue().get(0)).compress(100).getUrl(bigLandingItem.getName());
                        bigLandingItem.setImage(url);
                    }
                }
            }
        }
        biglandingCategory.setCategorySubs(landingCategorys);
        biglandingCategory.setBigLandingItem(bigLandingItems);
        return biglandingCategory;
    }

    /**
     * Lấy danh sách landing
     *
     * @param page
     * @return
     */
    public DataPage<BigLanding> getAll(Pageable page) {
        DataPage<BigLanding> dataPage = new DataPage<>();
        List<BigLanding> biglandings = biglandingRepository.getAll(page);
        dataPage.setData(biglandings);
        dataPage.setPageSize(page.getPageSize());
        dataPage.setPageIndex(page.getPageNumber());
        dataPage.setDataCount(biglandingRepository.count());
        dataPage.setPageCount(dataPage.getDataCount() / dataPage.getPageSize());
        if (dataPage.getDataCount() % dataPage.getPageSize() != 0) {
            dataPage.setPageCount(dataPage.getPageCount() + 1);
        }

        return dataPage;
    }

    /**
     * Lấy san pham 1 danh mục
     *
     * @param bigLandingCategoryId
     * @param page
     * @return
     */
    public DataPage<BigLandingItem> getItemByCategory(List<String> bigLandingCategoryId, Pageable page) {
        List<BigLandingItem> landingItems = bigLandingItemRepository.getByLandingCategory(bigLandingCategoryId, page, 0);
        DataPage<BigLandingItem> dataPage = new DataPage<>();

        List<String> ids = new ArrayList<>();
        for (BigLandingItem hi : landingItems) {
            ids.add(hi.getItemId());
        }
        List<Item> listItems = itemRepository.get(ids, 0);
        for (BigLandingItem landingItem : landingItems) {
            Image lastImage = imageService.getLastImage(landingItem.getId(), ImageType.BIG_LANDING);
            if (lastImage != null) {
                ImageUrl url = imageService.getUrl(lastImage.getImageId()).thumbnail(30, 30, "inset");;
                landingItem.setImage(url.compress(100).getUrl(landingItem.getName()));
            }
            for (Item item : listItems) {
                if (landingItem.getItemId().equals(item.getId())) {
                    landingItem.setItem(item);
                    break;
                }
            }
        }

        dataPage.setData(landingItems);

        dataPage.setDataCount(bigLandingItemRepository.countByCategory(bigLandingCategoryId));
        if (page != null) {
            dataPage.setPageIndex(page.getPageNumber());
            dataPage.setPageSize(page.getPageSize());
            dataPage.setPageCount(page.getPageSize() > 0 ? (dataPage.getDataCount() / dataPage.getPageSize()) : 0);
            if (dataPage.getPageSize() > 0 && dataPage.getDataCount() % dataPage.getPageSize() != 0) {
                dataPage.setPageCount(dataPage.getPageCount() + 1);
            }
        }
        return dataPage;
    }

    /**
     * Lấy sản phẩm trong landing
     *
     * @param cateIds
     * @return
     */
    public List<BigLandingItem> getItemByCategories(List<String> cateIds) {
        List<BigLandingItem> landingItems = bigLandingItemRepository.getByCategories(cateIds, null, 1);

        List<String> ids = new ArrayList<>();
        for (BigLandingItem hi : landingItems) {
            ids.add(hi.getItemId());
        }
        List<Item> listItems = itemRepository.get(ids, 0);
        for (BigLandingItem landingItem : landingItems) {
            Image lastImage = imageService.getLastImage(landingItem.getId(), ImageType.BIG_LANDING);
            if (lastImage != null) {
                ImageUrl url = imageService.getUrl(lastImage.getImageId());
                landingItem.setImage(url.compress(100).getUrl(landingItem.getName()));
            }
            for (Item item : listItems) {
                if (landingItem.getItemId().equals(item.getId())) {
                    landingItem.setItem(item);
                    break;
                }
            }
        }

        return landingItems;
    }

    /**
     * Lấy sản phẩm trong landing home
     *
     * @param cateIds
     * @return
     */
    public List<BigLandingItem> getFeaturedItemByCategories(String cateIds) {
        List<BigLandingItem> landingItems = bigLandingItemRepository.getFeaturedByCategories(cateIds);
        List<String> ids = new ArrayList<>();
        for (BigLandingItem hi : landingItems) {
            ids.add(hi.getItemId());
        }
        Promotion promotion = null;
        List<BigLandingItem> landingItemsN = new ArrayList<>();
        List<Item> listItems = itemRepository.get(ids, 0);
        for (BigLandingItem landingItem : landingItems) {

            Image lastImage = imageService.getLastImage(landingItem.getId(), ImageType.BIG_LANDING);
            if (lastImage != null) {
                ImageUrl url = imageService.getUrl(lastImage.getImageId());
                landingItem.setImage(url.compress(100).getUrl(landingItem.getName()));
            }
            for (Item item : listItems) {
                if (landingItem.getItemId().equals(item.getId())) {
                    landingItem.setItem(item);
                    List<Promotion> listPromotion = promotionService.getPromotionBySellerIsRunning(item.getSellerId(), PromotionType.DISCOUND, 1);
                    if (listPromotion != null && !listPromotion.isEmpty()) {
                        promotion = listPromotion.get(0);
                        landingItem.setPromition(promotion.getName());
                    }
                    landingItemsN.add(landingItem);
                    break;
                }
            }
        }

        return landingItemsN;
    }

    /**
     * Lấy top 4 sản phẩm trong landing home
     *
     * @param cateIds
     * @return
     */
    public List<BigLandingItem> getTopItemByCategories(String cateIds) {
        List<BigLandingItem> landingItems = bigLandingItemRepository.getTopByCategories(cateIds);
        List<String> ids = new ArrayList<>();
        for (BigLandingItem hi : landingItems) {
            ids.add(hi.getItemId());
        }
        List<Item> listItems = itemRepository.get(ids, 0);
        for (BigLandingItem landingItem : landingItems) {
            Image lastImage = imageService.getLastImage(landingItem.getId(), ImageType.BIG_LANDING);
            if (lastImage != null) {
                ImageUrl url = imageService.getUrl(lastImage.getImageId()).thumbnail(250, 250, "inset");
                landingItem.setImage(url.compress(100).getUrl(landingItem.getName()));
            }
            for (Item item : listItems) {
                if (landingItem.getItemId().equals(item.getId())) {
                    if (item.getSellerId() != null && !"".equals(item.getSellerId())) {
                        List<String> images = imageService.get(ImageType.SHOP_LOGO, item.getSellerId());
                        if (!images.isEmpty()) {
                            Shop shop = shopService.getShop(item.getSellerId());
                            if (shop != null) {
                                landingItem.setSellerName(shop.getAlias());
                                landingItem.setSellerImage(imageService.getUrl(images.get(0)).maxSize(200, 100).compress(100).getUrl(landingItem.getName()));
                            }
                        }
                        landingItem.setItem(item);
                        break;
                    }
                }
            }
        }

        return landingItems;
    }

    /**
     *
     * @param id
     * @return
     */
    public BigLandingCategory getBigLandingCategory(String id) {
        BigLandingCategory bigLandingCategory = biglandingCategoryRepository.find(id);
        return bigLandingCategory;
    }

    /**
     * Thêm mới landing
     *
     * @param landingForm
     * @return
     */
    public Response addBigLanding(BigLandingForm landingForm) {
        Map<String, String> errors = validator.validate(landingForm);
        if (landingForm.getId() == null || landingForm.getId().equals("")) {
            landingForm.setId(biglandingRepository.genId());
        }
        long time = System.currentTimeMillis();
        if (landingForm.getStartTime() == 0) {
            landingForm.setStartTime(time);
        }
        if (landingForm.getStartTime() < time) {
            //errors.put("startTime", "Thời gian bắt đầu khuyến mại phải lớn hơn thời gian hiện tại");
        }
        if (landingForm.getEndTime() == 0) {
            landingForm.setEndTime(landingForm.getStartTime() + 604800000);
        }
        if (landingForm.getEndTime() <= landingForm.getStartTime()) {
            //errors.put("endTime", "Thời gian kết thúc phải lớn hơn thời gian bắt đầu");
        }
        if (!errors.isEmpty()) {
            return new Response(false, null, errors);
        }
        BigLanding landing = new BigLanding();
        landing.setId(landingForm.getId());
        landing.setName(landingForm.getName());
        landing.setActive(landingForm.isActive());
        landing.setStartTime(landingForm.getStartTime());
        landing.setEndTime(landingForm.getEndTime());
        landing.setStartTimeSeller(landingForm.getStartTimeSeller());
        landing.setEndTimeSeller(landingForm.getEndTimeSeller());
        landing.setDescription(landingForm.getDescription());
        landing.setDescriptionOrder(landingForm.getDescriptionOrder());
        landing.setLandingTemplate(landingForm.getLandingTemplate());
        biglandingRepository.save(landing);
        return new Response(true, "Thêm/Sửa Landing thành công");

    }

    public DataPage<BigLandingItem> getAllItemByCategory(List<String> landingCategoryId, Pageable page, int active) {
        List<BigLandingItem> landingItems = bigLandingItemRepository.getByLandingCategory(landingCategoryId, page, 1);
        DataPage<BigLandingItem> dataPage = new DataPage<>();
        //Hạ sản phẩm không đủ điều kiện ở biglandingItem
        List<BigLandingItem> landingItemCount = bigLandingItemRepository.getByCategories(landingCategoryId);
        List<String> idsCount = new ArrayList<>();
        for (BigLandingItem s : landingItemCount) {
            idsCount.add(s.getItemId());
        }
        List<Item> listsItem = itemRepository.get(idsCount, active);
        long count = itemRepository.getCount(idsCount, active);
        List<String> listIds = new ArrayList<>();
        List<String> itemDel = new ArrayList<>();
        for (Item item : listsItem) {
            listIds.add(item.getId());
        }
        for (BigLandingItem hi : landingItemCount) {
            if (!listIds.contains(hi.getItemId())) {
                itemDel.add(hi.getId());
            }
        }
        if (itemDel != null && !itemDel.isEmpty()) {
            for (String itemDel1 : itemDel) {
                bigLandingItemRepository.delete(itemDel1);
                imageService.delete(ImageType.BIG_LANDING, itemDel1);
            }
        }
        //Hạ sản phẩm không đủ điều kiện ở biglandingItem

        List<String> ids = new ArrayList<>();
        for (BigLandingItem hi : landingItems) {
            ids.add(hi.getItemId());
        }
        List<BigLandingItem> landingItemsN = new ArrayList<>();
        List<Item> listItems = itemRepository.get(ids, active);
        for (BigLandingItem landingItem : landingItems) {
            Image lastImage = imageService.getLastImage(landingItem.getId(), ImageType.BIG_LANDING);
            if (lastImage != null) {
                ImageUrl url = imageService.getUrl(lastImage.getImageId());
                landingItem.setImage(url.compress(100).getUrl(landingItem.getName()));
            }
            for (Item item : listItems) {
                if (landingItem.getItemId().equals(item.getId())) {
                    if (item.getSellerId() != null && !"".equals(item.getSellerId())) {
                        List<String> images = imageService.get(ImageType.SHOP_LOGO, item.getSellerId());
                        if (!images.isEmpty()) {
                            Shop shop = shopService.getShop(item.getSellerId());
                            if (shop != null) {
                                landingItem.setSellerName(shop.getAlias());
                                landingItem.setSellerImage(imageService.getUrl(images.get(0)).maxSize(200, 100).compress(100).getUrl(item.getName()));
                            }
                        }
                        landingItem.setItem(item);
                        break;
                    }
                }
            }
            landingItemsN.add(landingItem);
        }

        dataPage.setData(landingItemsN);

        dataPage.setDataCount(count);
        //dataPage.setDataCount(bigLandingItemRepository.count(landingCategoryId));
        if (page != null) {
            dataPage.setPageIndex(page.getPageNumber());
            dataPage.setPageSize(page.getPageSize());
            dataPage.setPageCount(page.getPageSize() > 0 ? (dataPage.getDataCount() / dataPage.getPageSize()) : 0);
            if (dataPage.getPageSize() > 0 && dataPage.getDataCount() % dataPage.getPageSize() != 0) {
                dataPage.setPageCount(dataPage.getPageCount() + 1);
            }
        }
        return dataPage;
    }

    /**
     * Thêm mới danh mục landing
     *
     * @param landingCategory
     * @return
     */
    public Response addCategory(BigLandingCategory landingCategory) {
        Map<String, String> error = validator.validate(landingCategory);
        if (landingCategory.getId() == null || landingCategory.getId().equals("")) {
            landingCategory.setId(biglandingCategoryRepository.genId());
        }
        if (error.isEmpty()) {
            biglandingCategoryRepository.save(landingCategory);
            return new Response(true, "Thêm/Sửa danh mục Biglanding thành công");
        }
        return new Response(false, null, error);
    }

    /**
     * Thêm mới sản phẩm
     *
     * @param landingItem
     * @return
     */
    public Response addItem(BigLandingItem landingItem) throws Exception {
        Map<String, String> error = validator.validate(landingItem);
        BigLandingCategory landingCategory = biglandingCategoryRepository.find(landingItem.getBigLandingCategoryId());
        if (landingCategory == null) {
            error.put("itemId", "Danh mục Biglanding không tồn tại");
        }
        Item item = itemRepository.find(landingItem.getItemId());
        if (item == null) {
            error.put("itemId", "Sản phẩm không tồn tại");
        } else {
            long time = System.currentTimeMillis();
            if (!item.isActive() || !item.isApproved()) {
                error.put("itemId", "Sản phẩm chưa được duyệt");
            }
            if (item.getEndTime() < time || item.getStartTime() > time) {
                error.put("itemId", "Đã hết hạn đăng bán hoặc chưa tới hạn đăng bán");
            }
            if (item.getQuantity() < 1) {
                error.put("itemId", "Sản phẩm đã hết hàng");
            }
        }
        BigLandingItem li = bigLandingItemRepository.getByItem(landingItem.getItemId(), landingItem.getBigLandingCategoryId());
        if (li != null) {
            if (li.getBigLandingCategoryId().equals(landingItem.getBigLandingCategoryId())) {
                error.put("itemId", "Sản phẩm này đã được thêm vào danh sách");
            }
        }
        if (error.isEmpty()) {
            if (landingItem.isFeatured()) {
                Criteria criteria = new Criteria("bigLandingCategoryId").is(landingItem.getBigLandingCategoryId()).and("position").is(landingItem.getPosition());
                criteria.and("featured").is(true);
                List<BigLandingItem> bigLandingItems = bigLandingItemRepository.find(new Query(criteria));
                for (BigLandingItem bigLandingItem : bigLandingItems) {
                    imageService.delete(ImageType.BIG_LANDING, bigLandingItem.getId());
                    bigLandingItemRepository.delete(bigLandingItem.getId());
                }
            }
            if (landingItem.getId() == null || landingItem.getId().equals("")) {
                if (li != null && li.getBigLandingCategoryId().equals(landingItem.getBigLandingCategoryId())) {
                    landingItem.setId(li.getId());
                } else {
                    landingItem.setId(bigLandingItemRepository.genId());
                }
            }
            landingItem.setName(item.getName());
            bigLandingItemRepository.save(landingItem);
            Image firstImage = imageService.getFirstImage(item.getId(), ImageType.ITEM);
            if (firstImage != null) {
                ImageUrl url = imageService.getUrl(firstImage.getImageId());
                if (url != null) {
                    item.setImages(new ArrayList<String>());
                    item.getImages().add(firstImage.getImageId());
                    landingItem.setImage(url.compress(100).getUrl(item.getName()));
                    imageService.delete(ImageType.BIG_LANDING, landingItem.getId());
                    imageService.download(url.getUrl(), ImageType.BIG_LANDING, landingItem.getId());
                }
            }
            landingItem.setItem(item);
            return new Response(true, "Thêm sản phẩm Biglanding thành công", landingItem);
        }
        return new Response(false, "Thêm sản phẩm Biglanding thất bại", error);
    }

    /**
     * Xóa sản phẩm
     *
     * @param id
     * @return
     */
    public Response deleteItem(String id) throws Exception {
        BigLandingItem bigLandingItem = bigLandingItemRepository.find(id);
        if (bigLandingItem == null) {
            throw new Exception("Không tồn tại bản ghi nào");
        }
        bigLandingItemRepository.delete(id);
        imageService.delete(ImageType.BIG_LANDING, id);
        return new Response(true, "Xóa sản phẩm thành công");
    }

    /**
     * Xóa danh mục landing
     *
     * @param id
     * @return
     */
    public Response deleteCategory(String id) throws Exception {
        if (biglandingCategoryRepository.exists(id)) {
            BigLandingCategory bigLandingCategory = biglandingCategoryRepository.find(id);
            if (bigLandingCategory.getParentId() != null && !bigLandingCategory.getParentId().equals("")) {
                List<BigLandingItem> bigLandingItems = bigLandingItemRepository.find(new Query(new Criteria("bigLandingCategoryId").is(id)));
                for (BigLandingItem bigLandingItem : bigLandingItems) {
                    imageService.delete(ImageType.BIG_LANDING, bigLandingItem.getId());
                    bigLandingItemRepository.delete(bigLandingItem.getId());
                }
            } else {
                List<BigLandingCategory> landingCategorys = biglandingCategoryRepository.find(new Query(new Criteria("parentId").is(id)));
                List<String> ids = new ArrayList<>();
                for (BigLandingCategory category : landingCategorys) {
                    ids.add(category.getId());
                }
                List<BigLandingItem> bigLandingItems = bigLandingItemRepository.find(new Query(new Criteria("bigLandingCategoryId").in(ids)));
                for (BigLandingItem bigLandingItem : bigLandingItems) {
                    imageService.delete(ImageType.BIG_LANDING, bigLandingItem.getId());
                    bigLandingItemRepository.delete(bigLandingItem.getId());
                }
                bigLandingItemRepository.delete(new Query(new Criteria("id").in(ids)));
            }
            biglandingCategoryRepository.delete(id);
            return new Response(true, "Xóa danh mục thành công");
        } else {
            return new Response(true, "Danh mục không tồn tại");
        }
    }

    /**
     * Xóa landing
     *
     * @param id
     * @return
     */
    public Response deleteLanding(String id) {
        if (biglandingRepository.exists(id)) {
            biglandingRepository.delete(id);
            List<BigLandingCategory> byLanding = biglandingCategoryRepository.getByBigLanding(id);
            for (BigLandingCategory landingCategory : byLanding) {
                biglandingCategoryRepository.delete(landingCategory.getId());
                bigLandingItemRepository.removeByCategory(landingCategory.getId());
            }

            return new Response(true, "Xóa danh mục thành công");
        } else {
            return new Response(true, "Danh mục không tồn tại");
        }
    }

    public BigLandingItem getItem(String id) {
        return bigLandingItemRepository.find(id);
    }

    /**
     * lay big landing hien tai
     *
     * @param id
     * @return
     */
    public BigLanding getCurent(String id) {
        return biglandingRepository.getCurrent(id);
    }

    /**
     * lay big landing dang dien ra
     *
     * @return
     */
    public BigLanding getExistCurent() {
        return biglandingRepository.getCheckExistCurrent();
    }

    public BigLanding getExistCurentSeller() {
        return biglandingRepository.getCheckExistCurrentSeller();
    }

    public BigLanding getShowMessage() {
        return biglandingRepository.getCheckShowMessage();
    }

    public Response changeActiveBigLanding(String id) {
        BigLanding bigLanding = biglandingRepository.find(id);
        if (bigLanding == null) {
            return new Response(false, "Không tồn tại bản ghi này");
        }
        bigLanding.setActive(!bigLanding.isActive());
        biglandingRepository.save(bigLanding);
        return new Response(true, "Cập nhật trạng thái thành công", bigLanding);

    }

    public Response changeBannerCenterActiveBigLanding(String id) {
        BigLanding bigLanding = biglandingRepository.find(id);
        if (bigLanding == null) {
            return new Response(false, "Không tồn tại bản ghi này");
        }
        bigLanding.setCenterBannerActive(!bigLanding.isCenterBannerActive());
        biglandingRepository.save(bigLanding);
        return new Response(true, "Cập nhật trạng thái thành công", bigLanding);

    }

    public Response changeActiveBigLandingCate(String id) {
        BigLandingCategory bigLandingCategory = biglandingCategoryRepository.find(id);
        if (bigLandingCategory == null) {
            return new Response(false, "Không tồn tại bản ghi này");
        }
        bigLandingCategory.setActive(!bigLandingCategory.isActive());
        biglandingCategoryRepository.save(bigLandingCategory);
        return new Response(true, "Cập nhật trạng thái thành công", bigLandingCategory);

    }

    public Response changePositionBigLandingCate(String id, int position) {
        BigLandingCategory bigLandingCategory = biglandingCategoryRepository.find(id);
        if (bigLandingCategory == null) {
            return new Response(false, "Không tồn tại bản ghi này");
        }
        bigLandingCategory.setPosition(position);
        biglandingCategoryRepository.save(bigLandingCategory);
        return new Response(true, "Cập nhật vị trí thành công", bigLandingCategory);

    }

    /**
     * *
     * Cập nhật 1 sản phẩm big landing
     *
     * @param bigLandingItem
     * @return
     */
    public Response changeBigLandingItem(BigLandingItem bigLandingItem) throws Exception {
        BigLandingItem landingItem = bigLandingItemRepository.find(bigLandingItem.getId());
        if (landingItem == null) {
            throw new Exception("Không tồn tại bản ghi này");
        }
        landingItem.setPosition(bigLandingItem.getPosition());
        landingItem.setName(bigLandingItem.getName());
        landingItem.setActive(bigLandingItem.isActive());
        bigLandingItemRepository.save(landingItem);
        return new Response(true, "Cập nhật sản phẩm thành công", landingItem);

    }

    /**
     * *
     * Cập nhật name sản phẩm big landing
     *
     * @param bigLandingItem
     * @return
     */
    public BigLandingItem changeBigLandingItemName(BigLandingItem bigLandingItem) throws Exception {
        BigLandingItem landingItem = bigLandingItemRepository.find(bigLandingItem.getId());
        if (landingItem == null) {
            throw new Exception("Không tồn tại bản ghi này");
        }
        landingItem.setName(bigLandingItem.getName());
        bigLandingItemRepository.save(landingItem);
        return landingItem;

    }

    /**
     * *
     *
     * @param bigLandingCateId
     * @param position
     * @return
     */
    public BigLandingItem getItemByCustomer(String bigLandingCateId, int position) {
        BigLandingItem bigLandingItem = bigLandingItemRepository.getByItemBycustom(bigLandingCateId, position);
        List<String> get = imageService.get(ImageType.BIG_LANDING, bigLandingItem.getId());
        if (get != null && !get.isEmpty()) {
            bigLandingItem.setImage(imageService.getUrl(get.get(0)).compress().getUrl(bigLandingItem.getName()));
            bigLandingItem.setSellerImage(get.get(0));
        }
        return bigLandingItem;
    }

    /**
     * Danh sách người bán trong biglanding Trạng thái 1 = true ; 0 = false
     *
     * @param active
     * @return
     */
    public List<BigLandingSeller> getListBigLandingSeller(String bigLandingId, int active) {
        return bigLandingSellerRepository.list(active, bigLandingId);
    }

    /**
     * *
     * Thêm Khuyến mãi vào BigLanding
     *
     * @param bigLandingSeller
     * @return
     * @throws Exception
     */
    public Response addBigLandingSeller(BigLandingSeller bigLandingSeller) throws Exception {
        if (bigLandingSeller.getId() != null && !bigLandingSeller.getId().equals("")) {
            BigLandingSeller find = bigLandingSellerRepository.find(bigLandingSeller.getId());
            if (find != null) {
                bigLandingSeller.setSellerId(find.getSellerId());
                bigLandingSeller.setPromotionId(find.getPromotionId());
                bigLandingSeller.setBiglandingId(find.getBiglandingId());
                bigLandingSeller.setAlias(find.getAlias());
            }
            bigLandingSeller.setId(bigLandingSeller.getId());
        } else {
            List<BigLandingSeller> landingSellers = bigLandingSellerRepository.find(new Query(new Criteria("biglandingId").is(bigLandingSeller.getBiglandingId()).and("promotionId").is(bigLandingSeller.getPromotionId())));
            if (landingSellers != null && !landingSellers.isEmpty()) {
                throw new Exception("Chương trình khuyễn mãi đã được thêm vào chiến dịch Biglanding");
            }
            Promotion promotion = promotionRepository.find(bigLandingSeller.getPromotionId());
            if (promotion == null) {
                throw new Exception("Không tồn tại chương trình khuyến mãi này");
            }
            Shop shop = shopService.getShop(promotion.getSellerId());
            if (shop == null) {
                throw new Exception("Shop không tồn tại hoặc người bán " + bigLandingSeller.getSellerId() + " chưa mở Shop");
            }
            Seller seller = sellerRepository.find(bigLandingSeller.getSellerId());
            if (seller != null) {
                if (!seller.isNlIntegrated()) {
                    throw new Exception("Người bán chưa tích hợp ngân lượng");
                }
                if (!seller.isScIntegrated()) {
                    throw new Exception("Người bán chưa tích hợp ship chung");
                }
            }
            bigLandingSeller.setId(bigLandingSellerRepository.genId());
            bigLandingSeller.setSellerId(shop.getUserId());
            bigLandingSeller.setAlias(shop.getAlias());
            bigLandingSeller.setPromotionName(promotion.getName());
            bigLandingSeller.setPromotionId(promotion.getId());
            bigLandingSeller.setSellerName(shop.getTitle());
            bigLandingSeller.setActive(true);
        }

        bigLandingSellerRepository.save(bigLandingSeller);
        return new Response(true, "Thông tin biglanding Seller", bigLandingSeller);

    }

    /**
     *
     * @param id
     * @return Xóa người bán trong biglanding
     * @throws Exception
     */
    public Response delBigLandingSeller(String id) throws Exception {
        BigLandingSeller find = bigLandingSellerRepository.find(id);
        if (find == null) {
            throw new Exception("Không tồn tại bản ghi nào");
        }
        bigLandingSellerRepository.delete(id);
        return new Response(true, "Đã xóa thành công");
    }

    public List<BigLandingSeller> getListBigLandingPromotion(String bigLandingId, int active) {
        List<BigLandingSeller> bigLandingSellers = new ArrayList<>();
        List<BigLandingSeller> bigLandingSellers1 = bigLandingSellerRepository.list(active, bigLandingId);
        for (BigLandingSeller bigLandingSeller : bigLandingSellers1) {
            Promotion find = promotionRepository.find(bigLandingSeller.getPromotionId());
            if (find.isActive()) {
                bigLandingSellers.add(bigLandingSeller);
            }
        }
        List<String> userIds = new ArrayList<>();
        if (bigLandingSellers != null && !bigLandingSellers.isEmpty()) {
            for (BigLandingSeller bigLandingSeller : bigLandingSellers) {
                if (!userIds.contains(bigLandingSeller.getSellerId())) {
                    userIds.add(bigLandingSeller.getSellerId());
                }
            }
            Map<String, List<String>> get = imageService.get(ImageType.SHOP_LOGO, userIds);
            for (Map.Entry<String, List<String>> string : get.entrySet()) {
                List<String> value = string.getValue();
                for (BigLandingSeller bigLandingSeller : bigLandingSellers) {
                    if (bigLandingSeller.getSellerId().equals(string.getKey()) && value != null && !value.isEmpty()) {
                        bigLandingSeller.setLogoShop(imageService.getUrl(value.get(0)).compress(100).getUrl(bigLandingSeller.getSellerName()));
                    }
                }
            }
            ItemSearch itemSearch = new ItemSearch();
            itemSearch.setPromotionId(bigLandingSellers.get(0).getPromotionId());
            itemSearch.setStatus(1);
            itemSearch.setPageIndex(0);
            itemSearch.setPageSize(1000);
            DataPage<Item> search = itemService.search(itemSearch);
            List<Item> items = search.getData();
            if (items != null && !items.isEmpty()) {
                for (Item item : items) {
                    Image firstImage = imageService.getFirstImage(item.getId(), ImageType.ITEM);
                    if (firstImage != null) {
                        item.setImages(new ArrayList<String>());
                        item.getImages().add(imageService.getUrl(firstImage.getImageId()).thumbnail(198, 198, "inset").getUrl(item.getName()));
                    }
                }
                bigLandingSellers.get(0).setItems(items);
            }
        }
        return bigLandingSellers;
    }

    /**
     * Lấy ra 1 khuyễn mãi theo mã promotionId
     *
     * @param promotionId
     * @return
     */
    public BigLandingSeller getBigLandingPromotion(String promotionId) {
        BigLandingSeller bigLandingSeller = bigLandingSellerRepository.getBigLandingByPromotionId(promotionId);
        if (bigLandingSeller != null) {
            List<String> get = imageService.get(ImageType.SHOP_LOGO, bigLandingSeller.getSellerId());
            if (get != null && !get.isEmpty()) {
                bigLandingSeller.setLogoShop(imageService.getUrl(get.get(0)).getUrl(bigLandingSeller.getSellerName()));
            }
            ItemSearch itemSearch = new ItemSearch();
            itemSearch.setPromotionId(bigLandingSeller.getPromotionId());
            itemSearch.setStatus(1);
            itemSearch.setPageIndex(0);
            itemSearch.setPageSize(1000);
            DataPage<Item> search = itemService.search(itemSearch);
            List<Item> items = search.getData();
            if (items != null && !items.isEmpty()) {
                for (Item item : items) {
                    Image firstImage = imageService.getFirstImage(item.getId(), ImageType.ITEM);
                    if (firstImage != null) {
                        item.setImages(new ArrayList<String>());
                        item.getImages().add(imageService.getUrl(firstImage.getImageId()).thumbnail(198, 198, "inset").getUrl(item.getName()));
                    }
                }
                bigLandingSeller.setItems(items);
            }
        }
        return bigLandingSeller;

    }

    public boolean blOrderLanding() {

        return false;
    }

    public Response changeBackground(String id, String background) {
        BigLanding bigLanding = biglandingRepository.find(id);
        if (bigLanding == null) {
            return new Response(false, "Không tồn tại bản ghi này");
        }
        bigLanding.setBackground(background);
        biglandingRepository.save(bigLanding);
        return new Response(true, "Thay đổi màu nên thành công", bigLanding);

    }
}
