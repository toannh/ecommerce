package vn.chodientu.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vn.chodientu.component.Validator;
import vn.chodientu.entity.db.ParameterKey;
import vn.chodientu.entity.form.ParameterKeyForm;
import vn.chodientu.entity.input.ParameterKeySearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.ParameterKeyRepository;

@Service
public class ParameterKeyService {

    @Autowired
    private ParameterKeyRepository parameterKeyRepository;
    @Autowired
    private Validator validator;

    /**
     * danh sách toàn bộ parameter key
     *
     * @return
     */
    public List<ParameterKey> list() {
        List<ParameterKey> parameterKeys = parameterKeyRepository.getAll();
        return parameterKeys;
    }

    public DataPage<ParameterKey> search(ParameterKeySearch search) {
        Criteria cri = new Criteria();
        if (search.getKeyConvention() != null && !search.getKeyConvention().equals("")) {
            cri.and("keyConvention").is(search.getKeyConvention());
        }
        if (search.getKey()!= null && !search.getKey().equals("")) {
            cri.and("key").is(search.getKey());
        }
        if (search.getValue()!= null && !search.getValue().equals("")) {
            cri.and("value").is(search.getValue());
        }
        Sort sort;
        switch (search.getOrderBy()) {
            case 1:
                sort = new Sort(Sort.Direction.DESC, "key");
                break;
            case 2:
            default:
                sort = new Sort(Sort.Direction.DESC, "keyConvention");
                break;
        }
        DataPage<ParameterKey> dataPage = new DataPage<>();
        Query query = new Query(cri);
        query.skip(search.getPageIndex() * search.getPageSize()).limit(search.getPageSize());
        dataPage.setDataCount(parameterKeyRepository.count(new Query(cri)));
        dataPage.setPageIndex(search.getPageIndex());
        dataPage.setPageSize(search.getPageSize());
        dataPage.setPageCount(dataPage.getDataCount() / search.getPageSize());
        if (dataPage.getDataCount() % search.getPageSize() != 0) {
            dataPage.setPageCount(dataPage.getPageCount() + 1);
        }
        query.with(new PageRequest(search.getPageIndex(), search.getPageSize(), sort));
        dataPage.setData(parameterKeyRepository.find(query));
        return dataPage;
    }

    /**
     * Thêm mới 1 parameter key
     *
     * @return
     */
    public Response add(ParameterKeyForm parameterKeyForm) {
        Map<String, String> errors = validator.validate(parameterKeyForm);
        ParameterKey param = new ParameterKey();
        if (errors.isEmpty()) {
            param.setKeyConvention(parameterKeyForm.getKeyConvention());
            param.setKey(parameterKeyForm.getKey());
            param.setValue(parameterKeyForm.getValue());
            parameterKeyRepository.save(param);
            return new Response(true, "Thêm mới thông số thành công!", param);
        } else {
            return new Response(true, "Chưa nhập đủ thông tin!", errors);
        }
    }

    /**
     * Sửa 1 parameter key
     *
     * @return
     */
    public Response edit(ParameterKeyForm parameterKeyForm) {
        Map<String, String> errors = validator.validate(parameterKeyForm);
        ParameterKey param = parameterKeyRepository.find(parameterKeyForm.getKeyConvention());
        if (errors.isEmpty()) {
            param.setKey(parameterKeyForm.getKey());
            param.setValue(parameterKeyForm.getValue());
            parameterKeyRepository.save(param);
            return new Response(true, "Sửa thông số thành công!", param);
        } else {
            return new Response(true, "Chưa nhập đủ thông tin!", errors);
        }
    }

    /**
     * Xóa 1 parameter key
     *
     * @return
     */
    public Response delete(String key) {
        ParameterKey param = parameterKeyRepository.find(key);
        if (param != null) {
            parameterKeyRepository.delete(param);
            return new Response(true, "Xóa thông số thành công!", param);
        } else {
            return new Response(true, "Không thể xóa thông số này!");
        }
    }

    /**
     * lấy chi tiết 1 parameter key
     *
     * @param key
     * @return
     */
    public Response getParameterKeybyId(String key) {
        ParameterKey param = parameterKeyRepository.find(key);
        if (param != null) {
            return new Response(true, "", param);
        } else {
            return new Response(false, "Thông số không tồn tại!");
        }
    }
    
    public ParameterKey getKeyById(String key) {
        ParameterKey parameter = parameterKeyRepository.getParameterKeyById(key);
        if (parameter != null) {
            return parameter;
        } else {
            return new ParameterKey();
        }
    }
    public String getValue(String key,boolean isKey){
        ParameterKey parameterKey=parameterKeyRepository.getParameterKeyById(key);
        if(parameterKey!=null){
        return isKey == true ? parameterKey.getKey() : parameterKey.getValue();
        }
        return null;
    }
}
