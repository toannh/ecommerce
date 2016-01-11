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
import vn.chodientu.entity.db.BigLandingItem;
import vn.chodientu.entity.db.Image;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.LandingNew;
import vn.chodientu.entity.db.LandingNewItem;
import vn.chodientu.entity.db.LandingNewSlide;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.form.LandingNewForm;
import vn.chodientu.entity.form.LandingNewSlideForm;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.ItemRepository;
import vn.chodientu.repository.LandingNewItemRepository;
import vn.chodientu.repository.LandingNewRepository;
import vn.chodientu.repository.LandingNewSlideRepository;

/**
 * @since Jul 14, 2014
 * @author Account
 */
@Service
public class LandingNewService {

    @Autowired
    private LandingNewSlideRepository landingNewSlideRepository;
    @Autowired
    private LandingNewItemRepository landingNewItemRepository;
    @Autowired
    private LandingNewRepository landingNewRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private Validator validator;
    @Autowired
    private ImageService imageService;

    /**
     * Thêm mới danh mục landing
     *
     * @param landingForm
     * @return
     */
    public Response addLandingNew(LandingNewForm landingForm) {
        Map<String, String> error = validator.validate(landingForm);
        List<String> logoImages = imageService.get(ImageType.LANDING_NEW_BANNER, landingForm.getId());

        if (landingForm.getId() == null || landingForm.getId().equals("")) {
            landingForm.setId(landingNewRepository.genId());
            landingForm.setActive(true);
        }
        if (logoImages == null && landingForm.getBannerCenter().getSize() <= 0) {
            error.put("bannerCenter", "Bạn chưa chọn banner landing new");
        }

        if (error.isEmpty()) {
            LandingNew landing = new LandingNew();
            landing.setId(landingForm.getId());
            landing.setName(landingForm.getName());
            landing.setColor(landingForm.getColor());
            landing.setDescription(landingForm.getDescription());
            landing.setActive(landingForm.isActive());
            landing.setTime(System.currentTimeMillis());
            landingNewRepository.save(landing);
            if (landingForm.getBannerCenter() != null && landingForm.getBannerCenter().getSize() > 0) {
                if (logoImages != null && !logoImages.isEmpty()) {
                    imageService.deleteById(ImageType.LANDING_NEW_BANNER, landing.getId(), logoImages.get(0));
                }
                imageService.upload(landingForm.getBannerCenter(), ImageType.LANDING_NEW_BANNER, landing.getId());

            }

            return new Response(true, "Thêm/Sửa landing new thành công");
        }
        return new Response(false, "Thêm/Sửa landing new thất bại", error);
    }

    public Response del(String landingNewId) throws Exception {
        LandingNew landingNew = landingNewRepository.find(landingNewId);
        if (landingNew == null) {
            throw new Exception("Không tồn tại bản ghi nào");
        }
        List<LandingNewItem> landingNewItems = landingNewItemRepository.find(new Query(new Criteria("landingNewId").is(landingNewId)));
        if (landingNewItems != null && !landingNewItems.isEmpty()) {
            for (LandingNewItem landingNewItem : landingNewItems) {
                imageService.delete(ImageType.LANDING_NEW_ITEM, landingNewItem.getId());
                landingNewItemRepository.delete(landingNewItem.getId());
            }
        }

        imageService.delete(ImageType.LANDING_NEW_BANNER, landingNewId);
        landingNewRepository.delete(landingNewId);
        return new Response(true, "Xóa landing new và toàn bộ sản phẩm thành công");
    }

    /**
     * *
     * Thay đổi trạng thái landing
     *
     * @param landingNewId
     * @return
     * @throws Exception
     */
    public Response changeActiveLandingNew(String landingNewId) throws Exception {
        LandingNew landingNew = landingNewRepository.find(landingNewId);
        if (landingNew == null) {
            throw new Exception("Không tồn tại landing này!");
        }
        landingNew.setActive(!landingNew.isActive());
        landingNew.setTime(System.currentTimeMillis());
        landingNewRepository.save(landingNew);
        return new Response(true, "Thay đổi trạng thái thành công", landingNew);

    }

    /**
     * Lấy thông tin của 1 landing new
     *
     * @return
     */
    public Response getLandingNewById(String landingNewId) throws Exception {
        LandingNew landingNew = landingNewRepository.find(landingNewId);
        if (landingNew == null) {
            throw new Exception("Không tồn tại landing này!");
        }
        List<String> logoImages = imageService.get(ImageType.LANDING_NEW_BANNER, landingNewId);
        if (logoImages != null && !logoImages.isEmpty()) {
            landingNew.setBannerCenter(imageService.getUrl(logoImages.get(0)).thumbnail(200, 200, "inset").getUrl(landingNew.getName()));
        }
        return new Response(true, "thông tin landing new", landingNew);
    }

    /**
     * *
     * Thay đổi thông tin Item trong landing new
     *
     * @param landingNewItem
     * @return
     * @throws Exception
     */
    public Response changeLandingNewItem(LandingNewItem landingNewItem) throws Exception {
        LandingNewItem landingNew = landingNewItemRepository.find(landingNewItem.getId());
        if (landingNew == null) {
            throw new Exception("Không tồn tại bản ghi này!");
        }
        landingNew.setName(landingNewItem.getName());
        landingNew.setActive(landingNewItem.isActive());
        landingNew.setPosition(landingNewItem.getPosition());
        landingNewItemRepository.save(landingNew);
        return new Response(true, "Lưu thành công", landingNew);

    }

    /**
     * Thêm mới sản phẩm
     *
     * @param landingItem
     * @return
     */
    public Response addItem(LandingNewItem landingItem) throws Exception {
        Map<String, String> error = validator.validate(landingItem);

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
        LandingNewItem landingNewItem = landingNewItemRepository.getByItem(item.getId(), landingItem.getLandingNewId());

        if (landingNewItem != null) {
            if (landingNewItem.getLandingNewId().equals(landingItem.getLandingNewId())) {
                error.put("itemId", "Sản phẩm này đã được thêm vào danh sách");
            }
        }
        if (error.isEmpty()) {

            if (landingItem.getId() == null || landingItem.getId().equals("")) {
                if (landingNewItem != null && landingNewItem.getLandingNewId().equals(landingItem.getId())) {
                    landingItem.setId(landingNewItem.getId());
                } else {
                    landingItem.setId(landingNewItemRepository.genId());
                }
            }
            landingItem.setName(item.getName());
            landingNewItemRepository.save(landingItem);
            Image firstImage = imageService.getFirstImage(item.getId(), ImageType.ITEM);
            if (firstImage != null) {
                ImageUrl url = imageService.getUrl(firstImage.getImageId());
                if (url != null) {
                    item.setImages(new ArrayList<String>());
                    item.getImages().add(firstImage.getImageId());
                    landingItem.setImage(url.compress(100).getUrl(landingItem.getName()));
                    imageService.delete(ImageType.LANDING_NEW_ITEM, landingItem.getId());
                    imageService.download(url.getUrl(), ImageType.LANDING_NEW_ITEM, landingItem.getId());
                }
            }
            landingItem.setItem(item);
            return new Response(true, "Thêm sản phẩm Landing New thành công", landingItem);
        }
        return new Response(false, "Thêm sản phẩm Landing New thất bại", error);
    }

    /**
     * Xóa sản phẩm
     *
     * @param id
     * @return
     */
    public Response deleteItem(String id) throws Exception {
        LandingNewItem landingNewItem = landingNewItemRepository.find(id);
        if (landingNewItem == null) {
            throw new Exception("Không tồn tại bản ghi nào");
        }
        landingNewItemRepository.delete(id);
        imageService.delete(ImageType.LANDING_NEW_ITEM, id);
        return new Response(true, "Xóa sản phẩm thành công");
    }

    /**
     * Lấy danh sách landing new
     *
     * @param page
     * @return
     */
    public DataPage<LandingNew> getAll(Pageable page) {
        DataPage<LandingNew> dataPage = new DataPage<>();
        List<LandingNew> landingNews = landingNewRepository.getAll(page);
        for (LandingNew landingNew : landingNews) {
            List<String> strings = imageService.get(ImageType.LANDING_NEW_BANNER, landingNew.getId());
            if (strings != null && !strings.isEmpty()) {
                landingNew.setBannerCenter(imageService.getUrl(strings.get(0)).compress(100).getUrl(landingNew.getName()));
            }
        }
        dataPage.setData(landingNews);
        dataPage.setPageSize(page.getPageSize());
        dataPage.setPageIndex(page.getPageNumber());
        dataPage.setDataCount(landingNewRepository.count());
        dataPage.setPageCount(dataPage.getDataCount() / dataPage.getPageSize());
        if (dataPage.getDataCount() % dataPage.getPageSize() != 0) {
            dataPage.setPageCount(dataPage.getPageCount() + 1);
        }

        return dataPage;
    }

    /**
     * Lấy san pham 1 landing new
     *
     * @param landingNewId
     * @param page
     * @return
     */
    public DataPage<LandingNewItem> getItemByLandingNew(String landingNewId, Pageable page) {
        List<LandingNewItem> landingItems = landingNewItemRepository.getByLandingNewId(landingNewId, page);
        DataPage<LandingNewItem> dataPage = new DataPage<>();

        List<String> ids = new ArrayList<>();
        for (LandingNewItem string : landingItems) {
            ids.add(string.getItemId());
        }
        List<Item> listItems = itemRepository.get(ids, 0);
        for (LandingNewItem landingItem : landingItems) {
            Image lastImage = imageService.getLastImage(landingItem.getId(), ImageType.LANDING_NEW_ITEM);
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

        dataPage.setDataCount(landingNewItemRepository.count(new Query(new Criteria("landingNewId").is(landingNewId))));
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

    public List<LandingNewItem> listLandingNewItem(String landingNewId) {
        Criteria criteria = new Criteria("landingNewId").is(landingNewId);
        criteria.and("active").is(true);
        List<LandingNewItem> landingItems = landingNewItemRepository.find(new Query(criteria).with(new Sort(Sort.Direction.ASC, "position")));
        List<String> ids = new ArrayList<>();
        for (LandingNewItem string : landingItems) {
            ids.add(string.getItemId());
        }
        List<Item> listItems = itemRepository.get(ids, 0);
        for (LandingNewItem landingItem : landingItems) {
            Image lastImage = imageService.getLastImage(landingItem.getId(), ImageType.LANDING_NEW_ITEM);
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
    public DataPage<LandingNewItem> listLandingNewItemPage(String landingNewId, Pageable page) {
        Criteria criteria = new Criteria("landingNewId").is(landingNewId);
        criteria.and("active").is(true);
        Query query = new Query(criteria);
        if (page != null && (page.getPageNumber() > 0 || page.getPageSize() > 0)) {
            query.with(page);
        }
        List<LandingNewItem> landingItems = landingNewItemRepository.find(query.with(new Sort(Sort.Direction.ASC, "position")));
        List<String> ids = new ArrayList<>();
        for (LandingNewItem string : landingItems) {
            ids.add(string.getItemId());
        }
        List<Item> listItems = itemRepository.get(ids, 0);
        for (LandingNewItem landingItem : landingItems) {
            Image lastImage = imageService.getLastImage(landingItem.getId(), ImageType.LANDING_NEW_ITEM);
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
        DataPage<LandingNewItem> dataPage=new DataPage<>();
        dataPage.setData(landingItems);

        dataPage.setDataCount(landingNewItemRepository.count(new Query(criteria)));
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
     * *
     * Cập nhật name sản phẩm big landing
     *
     * @param landingNewItem
     * @return
     */
    public LandingNewItem changeLandingItemName(LandingNewItem landingNewItem) throws Exception {
        LandingNewItem landingItem = landingNewItemRepository.find(landingNewItem.getId());
        if (landingItem == null) {
            throw new Exception("Không tồn tại bản ghi này");
        }
        landingItem.setName(landingNewItem.getName());
        landingNewItemRepository.save(landingItem);
        return landingItem;

    }

    /**
     * Lấy danh sách landing new
     *
     * @param page
     * @param landingId
     * @return
     */
    public DataPage<LandingNewSlide> getLandingNewSlide(Pageable page, String landingId) {
        DataPage<LandingNewSlide> dataPage = new DataPage<>();
        List<LandingNewSlide> landingNewSlides = landingNewSlideRepository.getAll(page, landingId);
        for (LandingNewSlide landingNew : landingNewSlides) {
            List<String> strings = imageService.get(ImageType.LANDING_NEW_SLIDE, landingNew.getId());
            if (strings != null && !strings.isEmpty()) {
                landingNew.setImage(imageService.getUrl(strings.get(0)).compress(100).getUrl(landingNew.getName()));
            }
        }
        dataPage.setData(landingNewSlides);
        dataPage.setPageSize(page.getPageSize());
        dataPage.setPageIndex(page.getPageNumber());
        dataPage.setDataCount(landingNewRepository.count());
        dataPage.setPageCount(dataPage.getDataCount() / dataPage.getPageSize());
        if (dataPage.getDataCount() % dataPage.getPageSize() != 0) {
            dataPage.setPageCount(dataPage.getPageCount() + 1);
        }

        return dataPage;
    }

    /**
     * *
     * Lấy danh sách landing new slide
     *
     * @param landingNewId
     * @return
     */
    public List<LandingNewSlide> listLandingNewSlide(String landingNewId) {
        List<LandingNewSlide> landingNewSlides = landingNewSlideRepository.find(new Query(new Criteria("landingNewId").is(landingNewId).and("active").is(true)));

        for (LandingNewSlide landingNew : landingNewSlides) {
            List<String> strings = imageService.get(ImageType.LANDING_NEW_SLIDE, landingNew.getId());
            if (strings != null && !strings.isEmpty()) {
                landingNew.setImage(imageService.getUrl(strings.get(0)).compress(100).getUrl(landingNew.getName()));
            }
        }
        return landingNewSlides;

    }

    /**
     * Thêm mới danh mục landingnew slide
     *
     * @param landingNewSlideForm
     * @return
     */
    public Response addLandingNewSlide(LandingNewSlideForm landingNewSlideForm) {
        Map<String, String> error = validator.validate(landingNewSlideForm);
        List<String> logoImages = imageService.get(ImageType.LANDING_NEW_SLIDE, landingNewSlideForm.getId());

        if (landingNewSlideForm.getId() == null || landingNewSlideForm.getId().equals("")) {
            landingNewSlideForm.setId(landingNewRepository.genId());
            landingNewSlideForm.setActive(true);
        }
        if (logoImages == null && landingNewSlideForm.getBanner().getSize() <= 0) {
            error.put("banner", "Bạn chưa chọn banner landing new");
        }

        if (error.isEmpty()) {
            LandingNewSlide landingNewSlide = new LandingNewSlide();
            landingNewSlide.setId(landingNewSlideForm.getId());
            landingNewSlide.setName(landingNewSlideForm.getName());
            landingNewSlide.setUrl(landingNewSlideForm.getUrl());
            landingNewSlide.setLandingNewId(landingNewSlideForm.getLandingNewId());
            landingNewSlide.setPosition(landingNewSlideForm.getPosition());
            landingNewSlide.setActive(landingNewSlideForm.isActive());
            landingNewSlideRepository.save(landingNewSlide);
            if (landingNewSlideForm.getBanner() != null && landingNewSlideForm.getBanner().getSize() > 0) {
                if (logoImages != null && !logoImages.isEmpty()) {
                    imageService.deleteById(ImageType.LANDING_NEW_SLIDE, landingNewSlide.getId(), logoImages.get(0));
                }
                Response upload = imageService.upload(landingNewSlideForm.getBanner(), ImageType.LANDING_NEW_SLIDE, landingNewSlide.getId());

            }

            return new Response(true, "Thêm/Sửa landing new slide thành công");
        }
        return new Response(false, "Thêm/Sửa landing new slide thất bại", error);
    }

    public Response delLandingNewSlide(String id) throws Exception {
        LandingNewSlide landingNewSlide = landingNewSlideRepository.find(id);
        if (landingNewSlide == null) {
            throw new Exception("Không tồn tại bản ghi nào");
        }

        imageService.delete(ImageType.LANDING_NEW_SLIDE, landingNewSlide.getId());
        landingNewSlideRepository.delete(landingNewSlide);
        return new Response(true, "Xóa LandingNew Slide");
    }

    /**
     * *
     * Thay đổi trạng thái landing
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Response changeActiveLandingNewSlide(String id) throws Exception {
        LandingNewSlide landingNewSlide = landingNewSlideRepository.find(id);
        if (landingNewSlide == null) {
            throw new Exception("Không tồn tại landing này!");
        }
        landingNewSlide.setActive(!landingNewSlide.isActive());
        landingNewSlideRepository.save(landingNewSlide);
        return new Response(true, "Thay đổi trạng thái thành công", landingNewSlide);

    }

    /**
     * *
     * Thay đổi vị trí landing
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Response changePositionLandingNewSlide(String id, int position) throws Exception {
        LandingNewSlide landingNewSlide = landingNewSlideRepository.find(id);
        if (landingNewSlide == null) {
            throw new Exception("Không tồn tại landing này!");
        }
        landingNewSlide.setPosition(position);
        landingNewSlideRepository.save(landingNewSlide);
        return new Response(true, "Thay đổi vị trí thành công", landingNewSlide);

    }

    /**
     * Lấy thông tin của 1 landing new
     *
     * @param id
     * @return
     */
    public Response getLandingNewSlideById(String id) throws Exception {
        LandingNewSlide landingNewSlide = landingNewSlideRepository.find(id);
        if (landingNewSlide == null) {
            throw new Exception("Không tồn tại landing này!");
        }
        List<String> logoImages = imageService.get(ImageType.LANDING_NEW_SLIDE, id);
        if (logoImages != null && !logoImages.isEmpty()) {
            landingNewSlide.setImageId(logoImages.get(0));
            landingNewSlide.setImage(imageService.getUrl(logoImages.get(0)).compress(100).getUrl(landingNewSlide.getName()));
        }
        return new Response(true, "thông tin landing new slide", landingNewSlide);
    }

    /**
     * Lấy thông tin của 1 landing new
     *
     * @return
     */
    public LandingNew getLandingNew(String id) throws Exception {
        LandingNew landingNew = landingNewRepository.find(id);

        List<String> logoImages = imageService.get(ImageType.LANDING_NEW_BANNER, id);
        if (logoImages != null && !logoImages.isEmpty()) {
            landingNew.setBannerCenter(imageService.getUrl(logoImages.get(0)).compress(100).getUrl(landingNew.getName()));
        }
        return landingNew;
    }
}
