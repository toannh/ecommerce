package vn.chodientu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vn.chodientu.component.Validator;
import vn.chodientu.entity.db.Landing;
import vn.chodientu.entity.db.PopoutHome;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.form.PopoutHomeForm;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.entity.web.Viewer;
import vn.chodientu.repository.PopoutHomeRepository;

/**
 * @since Jun 6, 2014
 * @author PhuongDT
 */
@Service
public class PopoutHomeService {

    @Autowired
    private PopoutHomeRepository popoutHomeRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private Validator validator;
    @Autowired
    private Viewer viewer;

    /**
     * *
     * Thêm mới popout trang chủ
     *
     * @param popoutHomeForm
     * @return
     */
    public Response add(PopoutHomeForm popoutHomeForm) {
        Map<String, String> errors = validator.validate(popoutHomeForm);
        if (popoutHomeForm.getType() <= 0) {
            errors.put("type", "Bạn chưa chọn kiểu hiển thị");
        }
        if (popoutHomeForm.getImagefile() == null || popoutHomeForm.getImagefile().getSize() <= 0) {
            errors.put("imagefile", "Bạn chưa chọn ảnh");
        }
        if (!errors.isEmpty()) {
            return new Response(false, null, errors);
        }
        PopoutHome popoutHome = new PopoutHome();
        popoutHome.setId(popoutHomeRepository.genId());
        popoutHome.setTitle(popoutHomeForm.getTitle());
        popoutHome.setActive(popoutHomeForm.isActive());
        popoutHome.setType(popoutHomeForm.getType());
        popoutHome.setTime(System.currentTimeMillis());
        popoutHome.setUrl(popoutHomeForm.getUrl());
        Response upload = null;
        try {
            upload = imageService.upload(popoutHomeForm.getImagefile(), ImageType.POPOUT_HOME, popoutHome.getId());
        } catch (Exception e) {
            return new Response(false, "Thêm ảnh thất bại", e.getMessage());
        }
        popoutHomeRepository.save(popoutHome);
        return new Response(true, "Đã thêm thành công");

    }

    /**
     * *
     * Sửa popout trang chủ
     *
     * @param popoutHomeForm
     * @return
     */
    public Response edit(PopoutHomeForm popoutHomeForm) {

        Map<String, String> errors = validator.validate(popoutHomeForm);
        if (popoutHomeForm.getType() <= 0) {
            errors.put("type", "Bạn chưa chọn kiểu hiển thị");
        }
        if (!errors.isEmpty()) {
            return new Response(false, null, errors);
        }
        PopoutHome popoutHome = popoutHomeRepository.find(popoutHomeForm.getId());
        if (popoutHome == null) {
            return new Response(false, null, "Không tồn bản ghi");
        }
        popoutHome.setTitle(popoutHomeForm.getTitle());
        popoutHome.setActive(popoutHomeForm.isActive() ? true : false);
        popoutHome.setType(popoutHomeForm.getType());
        popoutHome.setTime(System.currentTimeMillis());
        popoutHome.setUrl(popoutHomeForm.getUrl());
        Response upload = null;
        if (popoutHomeForm.getImagefile() != null && popoutHomeForm.getImagefile().getSize() > 0) {
            imageService.delete(ImageType.POPOUT_HOME, popoutHome.getId());
            upload = imageService.upload(popoutHomeForm.getImagefile(), ImageType.POPOUT_HOME, popoutHome.getId());
        }
        popoutHomeRepository.save(popoutHome);
        return new Response(true, "Đã sửa thành công");

    }

    /**
     * *
     * Thay đổi trạng thái popout trang chủ
     *
     * @param id
     * @return
     */
    public Response changeActive(String id) {
        PopoutHome popoutHome = popoutHomeRepository.find(id);
        if (popoutHome == null) {
            return new Response(false, "Không tồn tại bản ghi này");
        }
        popoutHome.setActive(!popoutHome.isActive());
        popoutHomeRepository.save(popoutHome);
        return new Response(true, "Thanh đổi trạng thái thành công", popoutHome);
    }

    /**
     * *
     * Xóa popout trang chủ
     *
     * @param id
     * @return
     */
    public Response del(String id) {
        PopoutHome popoutHome = popoutHomeRepository.find(id);
        if (popoutHome == null) {
            return new Response(false, "Không tồn tại bản ghi này");
        }
        imageService.delete(ImageType.POPOUT_HOME, id);
        popoutHomeRepository.delete(id);
        return new Response(true, "Xóa POPOUT thành công");
    }

    /**
     * *
     * Lấy ra 1 popout trang chủ
     *
     * @param id
     * @return
     */
    public Response get(String id) {
        PopoutHome popoutHome = popoutHomeRepository.find(id);
        if (popoutHome == null) {
            return new Response(false, "Không tồn tại bản ghi này");
        }
        return new Response(true, "Thông tin Popout trang chủ", popoutHome);
    }

    /**
     * *
     * List popout ra trang chủ
     *
     * @return
     */
    public List<PopoutHome> list() {
        List<PopoutHome> popoutHome = new ArrayList<>();
        List<PopoutHome> popoutHome1 = popoutHomeRepository.find(new Query(new Criteria("type").is(1).and("active").is(true)).limit(1));
        List<PopoutHome> popoutHome2 = popoutHomeRepository.find(new Query(new Criteria("type").is(2).and("active").is(true)).limit(1));
        if (!popoutHome1.isEmpty()) {
            popoutHome.addAll(popoutHome1);
        }
        if (!popoutHome2.isEmpty()) {
            popoutHome.addAll(popoutHome2);
        }
        return popoutHome;
    }

    /**
     * *
     * List All popout ra trang chủ
     *
     * @param page
     * @return
     */
    public DataPage<PopoutHome> getAll(Pageable page) {
        DataPage<PopoutHome> dataPage = new DataPage<>();
        List<PopoutHome> popoutHomes = popoutHomeRepository.getAll(page);
        for (PopoutHome popoutHome : popoutHomes) {
            List<String> logoImages = new ArrayList<>();
            logoImages = imageService.get(ImageType.POPOUT_HOME, popoutHome.getId());
            if (logoImages != null && !logoImages.isEmpty()) {
                popoutHome.setImage(imageService.getUrl(logoImages.get(0)).compress(100).getUrl("popup home"));
            }
        }
        dataPage.setData(popoutHomes);
        dataPage.setPageSize(page.getPageSize());
        dataPage.setPageIndex(page.getPageNumber());
        dataPage.setDataCount(popoutHomeRepository.count());
        dataPage.setPageCount(dataPage.getDataCount() / dataPage.getPageSize());
        if (dataPage.getDataCount() % dataPage.getPageSize() != 0) {
            dataPage.setPageCount(dataPage.getPageCount() + 1);
        }
        return dataPage;
    }

}
