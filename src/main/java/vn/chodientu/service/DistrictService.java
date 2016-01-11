package vn.chodientu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import vn.chodientu.component.ScClientV2;
import vn.chodientu.component.Validator;
import vn.chodientu.entity.data.ScDistrict;
import vn.chodientu.entity.data.ScWard;
import vn.chodientu.entity.db.City;
import vn.chodientu.entity.db.District;
import vn.chodientu.entity.db.Ward;
import vn.chodientu.entity.form.DistrictForm;
import vn.chodientu.entity.form.WardForm;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.CityRepository;
import vn.chodientu.repository.DistrictRepository;
import vn.chodientu.repository.WardRepository;

@Service
public class DistrictService {

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

    public List<District> list() {
        List<District> district = districtRepository.getAll();
        return district;
    }
    public void loadWard() {
        List<District> district = districtRepository.getAll();
        for(District d:district){
            try {
        List<ScWard> scWards = getWard(d.getScId());
        for (ScWard scWard : scWards) {
            Ward ds = wardRepository.getBySc(scWard.getId() + "");
            if (ds == null) {
                WardForm form=new WardForm();
                form.setPosition(1);
                form.setDistrictId(d.getId());
                form.setName(scWard.getName());
                form.setScId(scWard.getId()+"");
                add(form);
            }
        }
        } catch (Exception e) {
        
        }
        }
    }
public void add(WardForm wardForm) throws Exception {
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
            
        } else {
            
        }
    }
    public Response delete(String id) {
        District district = districtRepository.find(id);
        if (district != null) {
            districtRepository.delete(district);
            return new Response(true, "Xóa thành công!", district);
        }
        return new Response(false, "Không tồn tại quận / huyện này!");
    }

    public Response add(DistrictForm districtForm) throws Exception {
        Map<String, String> errors = Validator.validate(districtForm);
        District district = new District();
        City cid = cityRepository.find(districtForm.getCityId());

        if (!cityRepository.exists(districtForm.getCityId())) {
            errors.put("cityId", "Tỉnh / thành không tồn tại");
        } else {
            district.setCityId(districtForm.getCityId());
        }
        if (errors.isEmpty()) {
            district.setId(districtRepository.genId());
            district.setName(districtForm.getName());
            district.setPosition(districtForm.getPosition());
            district.setScId(districtForm.getScId());
            districtRepository.save(district);
            return new Response(true, "Thêm mới quận / huyện thành công!", district);
        } else {
            return new Response(false, "Chưa nhập đủ thông tin", errors);
        }
    }

    public Response editDistrict(DistrictForm districtForm) throws Exception {
        Map<String, String> errors = Validator.validate(districtForm);
        District district = districtRepository.find(districtForm.getId());
        if (district == null) {
            return new Response(false, "Quận huyện không tồn tại!");
        }
        if (errors.isEmpty()) {
            district.setName(districtForm.getName());
            district.setPosition(districtForm.getPosition());
            district.setScId(districtForm.getScId());
            districtRepository.save(district);
            return new Response(true, "Sửa thông tin quận/huyện thành công!", district);
        } else {
            return new Response(false, "Chưa nhập đủ thông tin", errors);
        }
    }

    public Response getDistrictById(String id) {
        District district = districtRepository.find(id);
        if (district != null) {
            return new Response(true, "", district);
        }
        return new Response(false, "Không tồn tại quận / huyện này!");
    }

    public Response edit(String id, String name, int position, String scId) {
        District district = districtRepository.find(id);
        if (district != null) {
            district.setName(name);
            district.setPosition(position);
            district.setScId(scId);
            districtRepository.save(district);
            return new Response(true, "Thay đổi thông tin của quận / huyện thành công!", district);
        }
        return new Response(false, "Quận / huyện không tồn tại!");
    }

    public District get(String id) {
        return districtRepository.find(id);
    }

    public List<District> getAll() {
        return districtRepository.getAll();
    }

    public List<District> getAllDistrictByCity(String id) throws Exception {
        try {
         City city=cityRepository.getbyId(id);
        List<ScDistrict> scDistricts = getDistrict(city.getScId());
        for (ScDistrict district : scDistricts) {
            District d = districtRepository.getBySc(district.getId() + "");
            if (d == null) {
                DistrictForm form=new DistrictForm();
                form.setPosition(1);
                form.setCityId(id);
                form.setName(district.getName());
                form.setScId(district.getId()+"");
                add(form);
            }
        }
            
        } catch (Exception e) {
        }
        List<District> allDistrictByCity = districtRepository.getAllDistrictByCity(id);
        if (allDistrictByCity == null) {
            return new ArrayList<>();
        }
        return allDistrictByCity;
    }

    @Cacheable(value = "buffercache", key = "scDistrict")
    public List<ScDistrict> getDistrict(String cityId) throws Exception {
        return scClient.getDistrict(cityId);
    }
    @Cacheable(value = "buffercache", key = "scWard")
    public List<ScWard> getWard(String districtId) throws Exception {
        return scClient.getWard(districtId);
    }
}
