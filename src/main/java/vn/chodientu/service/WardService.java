package vn.chodientu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import vn.chodientu.component.ScClientV2;
import vn.chodientu.component.Validator;
import vn.chodientu.entity.data.ScWard;
import vn.chodientu.entity.db.Ward;
import vn.chodientu.entity.form.WardForm;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.CityRepository;
import vn.chodientu.repository.DistrictRepository;
import vn.chodientu.repository.WardRepository;

@Service
public class WardService {

    @Autowired
    private Validator Validator;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private ScClientV2 scClient;

    public List<Ward> list() {
        List<Ward> ward = wardRepository.getAll();
        return ward;
    }

    public Response delete(String id) {
        Ward ward = wardRepository.find(id);
        if (ward != null) {
            wardRepository.delete(ward);
            return new Response(true, "Xóa thành công!", ward);
        }
        return new Response(false, "Không tồn tại phường / xã này!");
    }

    public Response add(WardForm wardForm) throws Exception {
        Map<String, String> errors = Validator.validate(wardForm);
        Ward ward = new Ward();

        if (!districtRepository.exists(wardForm.getDistrictId())) {
            errors.put("wardId", "Quận / huyện không tồn tại");
        } else {
            ward.setDistrictId(wardForm.getDistrictId());
        }
        if (errors.isEmpty()) {
            ward.setId(wardRepository.genId());
            ward.setName(wardForm.getName());
            ward.setPosition(wardForm.getPosition());
            ward.setScId(wardForm.getScId());
            wardRepository.save(ward);
            return new Response(true, "Thêm mới phường / xã thành công!", ward);
        } else {
            return new Response(false, "Chưa nhập đủ thông tin", errors);
        }
    }

    public Response editWard(WardForm wardForm) throws Exception {
        Map<String, String> errors = Validator.validate(wardForm);
        Ward ward = wardRepository.find(wardForm.getId());
        if (ward == null) {
            return new Response(false, "Phường xã không tồn tại!");
        }
        if (errors.isEmpty()) {
            ward.setName(wardForm.getName());
            ward.setPosition(wardForm.getPosition());
            ward.setScId(wardForm.getScId());
            wardRepository.save(ward);
            return new Response(true, "Sửa thông tin phường/xã thành công!", ward);
        } else {
            return new Response(false, "Chưa nhập đủ thông tin", errors);
        }
    }

    public Response getWardById(String id) {
        Ward ward = wardRepository.find(id);
        if (ward != null) {
            return new Response(true, "", ward);
        }
        return new Response(false, "Không tồn tại phường / xã này!");
    }

    public Response edit(String id, String name, int position, String scId) {
        Ward ward = wardRepository.find(id);
        if (ward != null) {
            ward.setName(name);
            ward.setPosition(position);
            ward.setScId(scId);
            wardRepository.save(ward);
            return new Response(true, "Thay đổi thông tin của phường / xã thành công!", ward);
        }
        return new Response(false, "Phường / xã không tồn tại!");
    }

    public Ward get(String id) {
        return wardRepository.find(id);
    }

    public List<Ward> getAll() {
        return wardRepository.getAll();
    }

    public List<Ward> getAllWardByDistrict(String id) throws Exception {
        List<Ward> allWardByDistrict = wardRepository.getAllWardByDistrict(id);
        if (allWardByDistrict == null) {
            return new ArrayList<>();
        }
        return allWardByDistrict;
    }
    @Cacheable(value = "buffercache", key = "scWard")
    public List<ScWard> getWard(String districtId) throws Exception {
        return scClient.getWard(districtId);
    }
    public boolean getExistWard(String districtId) {
        return wardRepository.getExistWard(districtId);
    }
}
