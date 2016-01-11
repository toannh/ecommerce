package vn.chodientu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import vn.chodientu.component.Validator;
import vn.chodientu.entity.db.ActiveKey;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.ActiveKeyRepository;

@Service
public class ActiveKeyService {
    @Autowired
    private ActiveKeyRepository activeKeyRepository;
    

    public List<ActiveKey> list() {
        List<ActiveKey> activeKeys = activeKeyRepository.getAll();
        return activeKeys;
    }

    public Response delete(String id,String type) {
        ActiveKey activeKey = activeKeyRepository.findByObject(id,type);
        if (activeKey != null) {
            activeKeyRepository.delete(activeKey);
            return new Response(true, "Xóa thành công!", activeKey);
        }
        return new Response(false, "Lỗi!");
    }

    public Response add(ActiveKey activeKey) throws Exception {
            activeKey.setCode(activeKeyRepository.genId6());
            activeKey.setTime(System.currentTimeMillis());
            activeKeyRepository.save(activeKey);
            return new Response(true, "Thêm mới thành công!", activeKey);
    }

    public ActiveKey getActiveKey(String id,String type) {
        return activeKeyRepository.findByObject(id,type);
    }
    public List<ActiveKey> getAll() {
        return activeKeyRepository.getAll();
    }
    public boolean getExistActiveKey(String id,String type) {
        return activeKeyRepository.getExistActiveKey(id,type);
    }
}
