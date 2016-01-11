package vn.chodientu.controller.cp;

import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.chodientu.entity.db.TopUp;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.enu.CashTransactionType;
import vn.chodientu.entity.input.CashTransactionSearch;
import vn.chodientu.entity.input.TopUpSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.TopUpService;
import vn.chodientu.service.UserService;
import vn.chodientu.util.TextUtils;

@Controller("cpTopUp")
@RequestMapping("/cp/topup")
public class TopUpController extends BaseCp {

    @Autowired
    private TopUpService topUpService;
    @Autowired
    private UserService userService;
    @Autowired
    private Gson gson;

    @RequestMapping(method = RequestMethod.GET)
    public String list(ModelMap model, HttpSession session, @RequestParam(value = "page", defaultValue = "1") int page) {
        TopUpSearch topUpSearch = new TopUpSearch();
        if (session.getAttribute("topUpSearch") != null && page != 0) {
            topUpSearch = (TopUpSearch) session.getAttribute("topUpSearch");
        } else {
            session.setAttribute("topUpSearch", topUpSearch);
        }

        topUpSearch.setPageIndex(page - 1);
        topUpSearch.setPageSize(100);

        DataPage<TopUp> dataPage = topUpService.search(topUpSearch);

        List<String> listID = new ArrayList<>();
        for (TopUp tp : dataPage.getData()) {
            if (!listID.contains(tp.getUserId())) {
                listID.add(tp.getUserId());
            }
        }

        model.put("dataPage", dataPage);
        model.put("clientScript", "var userIds = " + gson.toJson(listID) + ";" + " topup.init();");
        model.put("topUpSearch", topUpSearch);
        model.put("totalAmount", topUpService.sumPrice(topUpSearch));
        return "cp.topup";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String search(ModelMap model, HttpSession session, @ModelAttribute TopUpSearch topUpSearch) {
        session.setAttribute("topUpSearch", topUpSearch);
        topUpSearch.setPageIndex(0);
        topUpSearch.setPageSize(100);

        DataPage<TopUp> dataPage = topUpService.search(topUpSearch);

        List<String> listID = new ArrayList<>();
        for (TopUp tp : dataPage.getData()) {
            if (!listID.contains(tp.getUserId())) {
                listID.add(tp.getUserId());
            }
        }

        model.put("dataPage", dataPage);
        model.put("clientScript", "var userIds = " + gson.toJson(listID) + ";" + " topup.init();");
        model.put("topUpSearch", topUpSearch);
        model.put("totalAmount", topUpService.sumPrice(topUpSearch));
        return "cp.topup";
    }

    @RequestMapping(value = "/exceltopup", method = RequestMethod.GET)
    public void excelbyseller(HttpServletResponse response,
            @RequestParam(value = "requestId", defaultValue = "") String requestId,
            @RequestParam(value = "email", defaultValue = "") String email,
            @RequestParam(value = "phone", defaultValue = "") String phone,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "startTime", defaultValue = "0") long startTime,
            @RequestParam(value = "success", defaultValue = "0") int success,
            @RequestParam(value = "endTime", defaultValue = "0") long endTime
    ) throws Exception {
        TopUpSearch search = new TopUpSearch();
        if (requestId != null && !requestId.equals("")) {
            search.setRequestId(requestId);
        }
        if (email != null && !email.equals("")) {
            search.setEmail(email);
        }
        if (phone != null && !phone.equals("")) {
            search.setPhone(phone);
        }
        if (type != null && !type.equals("")) {
            search.setType(type);
        }
        if (startTime > 0 && endTime > 0) {
            search.setCreateTimeFrom(startTime);
            search.setCreateTimeTo(endTime);
        }
        if (success > 0) {
            search.setSuccess(success);
        }

        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("new sheet");

        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);

        row.createCell(0).setCellValue("STT");
        row.createCell(1).setCellValue("Mã giao dịch");
        row.createCell(2).setCellValue("Email");
        row.createCell(3).setCellValue("Kiểu thanh toán");
        row.createCell(4).setCellValue("Mệnh giá");
        row.createCell(5).setCellValue("Số điện thoại");
        row.createCell(6).setCellValue("Thời gian tạo");
        row.createCell(7).setCellValue("Trạng thái");
        row.createCell(8).setCellValue("cardCode");
        row.createCell(9).setCellValue("cardSerial");
        row.createCell(10).setCellValue("cardType");
        row.createCell(11).setCellValue("cardValue");
        row.createCell(12).setCellValue("expiryDate");
        int i = 1;
        search.setPageIndex(0);
        search.setPageSize(60000);
        List<String> userIds = new ArrayList<>();
        try {
            DataPage<TopUp> dataPage = topUpService.search(search);
            for (TopUp data : dataPage.getData()) {
                userIds.add(data.getUserId());
            }
            List<User> users = userService.getUserByIds(userIds);
            long totalCash = 0;
            for (TopUp data : dataPage.getData()) {
                totalCash += data.getAmount();
                for (User user : users) {
                    if (user.getId().equals(data.getUserId())) {
                        i++;
                        String types = null;
                        if (data.getType() != null && data.getType().equals("buyCardTelco")) {
                            types = "Đổi mã thẻ";
                        }
                        if (data.getType() != null && data.getType().equals("topupTelco")) {
                            types = "Nạp thẻ";
                        }
                        row = sheet.createRow((short) i);
                        row.createCell(0).setCellValue(String.valueOf(i - 1));
                        row.createCell(1).setCellValue(String.valueOf(data.getRequestId()));
                        row.createCell(2).setCellValue(String.valueOf(user.getEmail() == null ? "" : user.getEmail()));
                        row.createCell(3).setCellValue(String.valueOf(types));
                        String amount = Long.toString(data.getAmount());
                        SimpleDateFormat ft = new SimpleDateFormat("hh:mm dd/MM/yyyy");
                        String time = ft.format(data.getCreateTime());
                        row.createCell(4).setCellValue(String.valueOf(TextUtils.numberFormat(Double.parseDouble(amount))));
                        row.createCell(5).setCellValue(String.valueOf((data.getPhone() == null || data.getPhone().equals("")) ? "" : data.getPhone()));
                        row.createCell(6).setCellValue(String.valueOf(time));
                        row.createCell(7).setCellValue(String.valueOf(data.getMessage()));
                        row.createCell(8).setCellValue(String.valueOf(data.getCardCode()));
                        row.createCell(9).setCellValue(String.valueOf(data.getCardSerial()));
                        row.createCell(10).setCellValue(String.valueOf(data.getCardType()));
                        row.createCell(11).setCellValue(String.valueOf(data.getCardValue()));
                        row.createCell(12).setCellValue(String.valueOf(data.getExpiryDate()));
                        break;
                    }
                }
            }
            String s = Long.toString(totalCash);
            row = sheet.createRow((short) i + 1);
            row.createCell(6).setCellValue("Tổng tiền: ");
            row.createCell(7).setCellValue(TextUtils.numberFormat(Double.parseDouble(s)));
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=Danh-sach-quan-ly-nap-the-dien-thoai.xls");
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }
}
