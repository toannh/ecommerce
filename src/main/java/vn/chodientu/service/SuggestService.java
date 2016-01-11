package vn.chodientu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vn.chodientu.entity.db.Category;
import vn.chodientu.entity.db.Suggest;
import vn.chodientu.entity.input.SuggestSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.repository.CategoryRepository;
import vn.chodientu.repository.SuggestRepository;
import vn.chodientu.util.TextUtils;

@Service
public class SuggestService {

    @Autowired
    private SuggestRepository suggestRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public void create(String keyword, String categoryId) {
        categoryId = (categoryId == null || categoryId.equals("") ? "" : categoryId);
        Category category = categoryRepository.find(categoryId);

        Suggest suggest = suggestRepository.find(TextUtils.createAlias(keyword), categoryId);
        if (suggest == null) {
            suggest = new Suggest();
            suggest.setId(TextUtils.createAlias(keyword) + "-" + categoryId);
            suggest.setKeyword(keyword);
            suggest.setCreateTime(System.currentTimeMillis());
            try {
                suggest.setCategoryId(category.getId());
                suggest.setCategoryName(category.getName());
            } catch (Exception e) {
                suggest.setCategoryId(null);
                suggest.setCategoryName("Tìm kiếm");
            }

        }
        suggest.setUpdateTime(System.currentTimeMillis());
        suggest.setQuantity(suggest.getQuantity() + 1);
        suggestRepository.save(suggest);
    }

    /**
     * Search
     *
     * @param search
     * @return
     */
    public DataPage<Suggest> suggests(SuggestSearch search) {

        Criteria cri = new Criteria();
        if (search.getKeyword() != null && !search.getKeyword().equals("")) {
            cri.and("_id").regex(TextUtils.createAlias(search.getKeyword()), "i");
        }
        if (search.getCategoryId() != null && !search.getCategoryId().equals("")) {
            cri.and("categoryId").is(search.getCategoryId());
        }

        Query query = new Query(cri);
        query.with(new Sort(Sort.Direction.DESC, "updateTime")
                .and(new Sort(Sort.Direction.DESC, "quantity")));
        query.skip(search.getPageIndex() * search.getPageSize()).limit(search.getPageSize());

        DataPage<Suggest> page = new DataPage<>();
        page.setPageSize(search.getPageSize());
        page.setPageIndex(search.getPageIndex());
        if (page.getPageSize() <= 0) {
            page.setPageSize(5);
        }
        page.setDataCount(suggestRepository.count(query));
        page.setPageCount(page.getDataCount() / page.getPageSize());
        if (page.getDataCount() % page.getPageSize() != 0) {
            page.setPageCount(page.getPageCount() + 1);
        }

        page.setData(suggestRepository.find(query));
        return page;
    }

}
