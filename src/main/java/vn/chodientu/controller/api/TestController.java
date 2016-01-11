package vn.chodientu.controller.api;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.elasticsearch.index.query.TermsFilterBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.component.ScClientV2;
import vn.chodientu.component.SmsClient;
import vn.chodientu.component.imboclient.Url.ImageUrl;
import vn.chodientu.entity.api.Request;
import vn.chodientu.entity.db.Cash;
import vn.chodientu.entity.db.Category;
import vn.chodientu.entity.db.HotdealCategory;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.ItemDetail;
import vn.chodientu.entity.db.ItemMarketing;
import vn.chodientu.entity.db.ItemProperty;
import vn.chodientu.entity.db.Lading;
import vn.chodientu.entity.db.LandingItem;
import vn.chodientu.entity.db.Message;
import vn.chodientu.entity.db.Order;
import vn.chodientu.entity.db.OrderItem;
import vn.chodientu.entity.db.Seller;
import vn.chodientu.entity.db.SellerEmailMarketing;
import vn.chodientu.entity.db.SellerShop;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.db.TopUp;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.form.UserLoginForm;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.input.OrderSearch;
import vn.chodientu.entity.input.SellerShopSearch;
import vn.chodientu.entity.input.ShopSearch;
import vn.chodientu.entity.input.UserSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.EmailOutboxRepository;
import vn.chodientu.repository.HotdealCategoryRepository;
import vn.chodientu.repository.ItemPropertyRepository;
import vn.chodientu.repository.ItemRepository;
import vn.chodientu.repository.ItemSearchRepository;
import vn.chodientu.repository.LandingItemRepository;
import vn.chodientu.repository.MessageRepository;
import vn.chodientu.repository.OrderRepository;
import vn.chodientu.repository.SellerRepository;
import vn.chodientu.repository.ShopRepository;
import vn.chodientu.repository.TopUpRepository;
import vn.chodientu.repository.UserRepository;
import vn.chodientu.service.CashService;
import vn.chodientu.service.CategoryAliasService;
import vn.chodientu.service.CategoryMappingService;
import vn.chodientu.service.CategoryService;
import vn.chodientu.service.DistrictService;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.ItemFixService;
import vn.chodientu.service.ItemMarketingService;
import vn.chodientu.service.ItemService;
import vn.chodientu.service.LadingService;
import vn.chodientu.service.ModelService;
import vn.chodientu.service.OrderService;
import vn.chodientu.service.RealTimeService;
import vn.chodientu.service.ReportService;
import vn.chodientu.service.ReportTestService;
import vn.chodientu.service.SearchIndexService;
import vn.chodientu.service.SellerMarketingService;
import vn.chodientu.service.SellerShopService;
import vn.chodientu.service.ShopService;
import vn.chodientu.service.TopUpService;
import vn.chodientu.service.UserService;
import vn.chodientu.util.TextUtils;
import vn.chodientu.util.UrlUtils;

/**
 *
 * @author Phu
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private LadingService ladingService;
    @Autowired
    private CategoryAliasService categoryAliasService;
    @Autowired
    private SearchIndexService indexService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private TopUpRepository topUpRepository;
    @Autowired
    private ItemSearchRepository itemSearchRepository;
    @Autowired
    private EmailOutboxRepository emailOutboxRepository;
    @Autowired
    private ItemPropertyRepository itemPropertyRepository;
    @Autowired
    private TopUpService topUpService;
    @Autowired
    private SellerMarketingService sellerMarketingService;
    @Autowired
    private ReportTestService reportTestService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ItemFixService itemFixService;
    @Autowired
    private Gson gson;
    @Autowired
    private ItemMarketingService itemMarketingService;
    @Autowired
    private SmsClient smsClient;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private RealTimeService realTimeService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private LandingItemRepository landingItemRepository;
    @Autowired
    private CashService cashService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ShopService shopService;
    @Autowired
    private SellerShopService sellerShopService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private HotdealCategoryRepository hotdealCategoryRepository;
    @Autowired
    private ScClientV2 scClientV2;
    @Autowired
    private CategoryMappingService categoryMappingService;

    @RequestMapping(value = "/demo", method = RequestMethod.GET)
    @ResponseBody
    public void suggest(@RequestParam(value = "id") String id) throws Exception {
        SellerEmailMarketing email = sellerMarketingService.getEmail(id);
        sellerMarketingService.processEmailMarketing(email);
    }

    @RequestMapping(value = "/sendsms", method = RequestMethod.GET)
    @ResponseBody
    public Response suggest(@RequestParam(value = "phone") String phone, @RequestParam(value = "content") String content) throws Exception {
        String sendSms = smsClient.sendSms(phone, content, 2, 1);
        return new Response(true, "Gửi SMS", sendSms);
    }

    @RequestMapping(value = "/demo2", method = RequestMethod.GET)
    @ResponseBody
    public void demo2(@RequestParam(value = "id") String id) throws Exception {
        TopUp topUp = topUpRepository.find(id);
        topUp.setSendEmail(false);
        topUpService.sendAction(topUp);
    }

    @RequestMapping(value = "/date", method = RequestMethod.GET)
    @ResponseBody
    public Response date(@RequestParam(value = "time") long time, @RequestParam(value = "day") int day) throws Exception {
        return new Response(true, "", TextUtils.getTime(time, day));
    }

    @RequestMapping(value = "/getemailoutbox", method = RequestMethod.GET)
    @ResponseBody
    public Response getemailoutbox() throws Exception {
        return null;
        // List<Test> tests = testRepository.find(new Query(new Criteria()).with(new Sort(Sort.Direction.DESC, "count")));

    }

    @RequestMapping(value = "/indexitem", method = RequestMethod.GET)
    @ResponseBody
    public Response indexitem(@RequestParam(value = "id") String id) throws Exception {
        return itemService.indexItem(id);
    }

    @RequestMapping(value = "/demoitem", method = RequestMethod.GET)
    @ResponseBody
    public Response demoitem() throws Exception {
        List<ItemProperty> itemPropertys = itemPropertyRepository.find(new Query(new Criteria()));
        List<String> strings = new ArrayList<>();
        for (ItemProperty itemProperty : itemPropertys) {
            if (!strings.contains(itemProperty.getItemId())) {
                strings.add(itemProperty.getCategoryPropertyId());
            }
        }
        List<String> itemIds = new ArrayList<>();
        for (ItemProperty itemProperty : itemPropertys) {
            if (strings.contains(itemProperty.getItemId())) {
                boolean fag = true;
                List<ItemProperty> propertys = itemPropertyRepository.find(new Query(new Criteria("itemId").is(itemProperty.getItemId())));
                List<String> test = new ArrayList<>();
                for (ItemProperty property : propertys) {
                    if (test == null && !test.isEmpty()) {
                        test.add(property.getCategoryPropertyId());
                    }
                    if (!test.contains(property.getCategoryPropertyId())) {
                        test.add(property.getCategoryPropertyId());
                    } else {
                        fag = false;
                        break;
                    }
                }
                if (!fag) {
                    itemIds.add(itemProperty.getItemId());
                }
            }
        }

        return new Response(true, null, itemIds);

    }

    @RequestMapping(value = "/getitem", method = RequestMethod.GET)
    @ResponseBody
    public Response getitem(@RequestParam(value = "id") String id) throws Exception {
        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setPageIndex(0);
        itemSearch.setPageSize(20);
        itemSearch.setId(id);
        DataPage<Item> search = this.search(itemSearch);
        return new Response(true, "thông tin item", search);

    }

    @Cacheable(value = "buffercache", key = "'itemsearch-'.concat(#itemSearch.getKey())")
    public DataPage<Item> search(ItemSearch itemSearch) {
        NativeSearchQueryBuilder builder = buildSearchCondition(itemSearch, false);

        builder.withFields("id");
        builder.withPageable(new PageRequest(itemSearch.getPageIndex(), itemSearch.getPageSize()));
        DataPage<Item> dataPage = new DataPage<>();
        try {
            FacetedPage<vn.chodientu.entity.search.ItemSearch> page = itemSearchRepository.search(builder.build());

            dataPage.setDataCount(page.getTotalElements());
            dataPage.setPageCount(page.getTotalPages());
            dataPage.setPageSize(page.getSize());
            dataPage.setPageIndex(page.getNumber());

            List<String> ids = new ArrayList<>();

            for (vn.chodientu.entity.search.ItemSearch is : page.getContent()) {
                ids.add(is.getId());
            }

            dataPage.setData(itemRepository.get(ids));

        } catch (Exception ex) {
            dataPage.setData(new ArrayList<Item>());
        }
        return dataPage;

    }

    private NativeSearchQueryBuilder buildSearchCondition(ItemSearch itemSearch, boolean ignoreSearch) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        List<FilterBuilder> filters = new ArrayList<>();

        if (itemSearch.getCreateTimeFrom() > 0 && itemSearch.getCreateTimeTo() > 0) {
            filters.add(new RangeFilterBuilder("createTime").gte(itemSearch.getCreateTimeFrom()).lte(itemSearch.getCreateTimeTo()));
        } else if (itemSearch.getCreateTimeFrom() > 0) {
            filters.add(new RangeFilterBuilder("createTime").gte(itemSearch.getCreateTimeFrom()));
        } else if (itemSearch.getCreateTimeTo() > 0) {
            filters.add(new RangeFilterBuilder("createTime").lte(itemSearch.getCreateTimeTo()));
        }
        if (itemSearch.getCategoryIds() != null && !itemSearch.getCategoryIds().isEmpty() && itemSearch.getCategoryIds().size() > 0) {
            filters.add(new TermsFilterBuilder("categoryPath", itemSearch.getCategoryIds()));
        }
        if (!ignoreSearch) {
            if (itemSearch.getId() != null && !itemSearch.getId().trim().equals("")) {
                filters.add(new TermFilterBuilder("id", itemSearch.getId().trim()));
            }
            if (itemSearch.getKeyword() != null && itemSearch.getKeyword().trim().length() > 1) {
                queryBuilder.withQuery(new QueryStringQueryBuilder(TextUtils.removeDiacritical(itemSearch.getKeyword())).defaultOperator(QueryStringQueryBuilder.Operator.AND));
            } else {
                queryBuilder.withQuery(new MatchAllQueryBuilder());
            }
            if (!filters.isEmpty()) {
                queryBuilder.withFilter(new AndFilterBuilder(filters.toArray(new FilterBuilder[0])));
            }
        }
        return queryBuilder;

    }

    @RequestMapping(value = "/getsitemap", method = RequestMethod.GET)
    @ResponseBody
    public Response getsitemap() throws Exception {
//        List<CategoryAlias> aliasData = categoryAliasService.getAll(1);
//        List<String> cateIds = new ArrayList<>();
//        for (CategoryAlias categoryAlias : aliasData) {
//            if (!cateIds.contains(categoryAlias.getCategoryId())) {
//                cateIds.add(categoryAlias.getCategoryId());
//            }
//        }
//        List<Category> childCates = categoryService.getChildsByIds(cateIds);
//        List<String> listLink = new ArrayList<String>();
//        for (CategoryAlias item : aliasData) {
//            String link = UrlUtils.browse(item.getCategoryId(), item.getCategoryName());
//            listLink.add(link);
//        }
//        for (Category cate : childCates) {
//            String link = UrlUtils.browse(cate.getId(), cate.getName());
//            listLink.add(link);
//        }

        List<Category> cates = categoryService.getByLevelDisplay(1);
        List<Category> cates2 = categoryService.getByLevelDisplay(2);
        List<Category> cates3 = categoryService.getByLevelDisplay(3);
        for (Category c2 : cates2) {
            List<Category> cate = new ArrayList<>();
            for (Category c3 : cates3) {
                if (c2.getId().equals(c3.getParentId())) {
                    cate.add(c3);
                }
            }
            c2.setCategoris(cate);
        }

        for (Category c2 : cates) {
            List<Category> cate = new ArrayList<>();
            for (Category c3 : cates2) {
                if (c2.getId().equals(c3.getParentId())) {
                    cate.add(c3);
                }
            }
            c2.setCategoris(cate);
        }
        List<String> listLink = new ArrayList<String>();
        listLink.add("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">"
                + "<url>"
                + "<loc>http://chodientu.vn/</loc>"
                + "<lastmod>2014-11-28</lastmod>"
                + "<changefreq>daily</changefreq>"
                + "<priority>1.0</priority>"
                + "</url>");
        for (Category ct1 : cates) {
            String link1 = "<url>"
                    + "<loc>" + "http://chodientu.vn" + UrlUtils.browse(ct1.getId(), ct1.getName()) + "</loc>"
                    + "<lastmod>2014-11-28</lastmod>"
                    + "<changefreq>daily</changefreq>"
                    + "<priority>0.9</priority>"
                    + "</url>";
            listLink.add(link1);
            for (Category ct2 : ct1.getCategoris()) {
                String link2 = "<url>"
                        + "<loc>" + "http://chodientu.vn" + UrlUtils.browse(ct2.getId(), ct2.getName()) + "</loc>"
                        + "<lastmod>2014-11-28</lastmod>"
                        + "<changefreq>daily</changefreq>"
                        + "<priority>0.9</priority>"
                        + "</url>";
                listLink.add(link2);
                for (Category ct3 : ct2.getCategoris()) {
                    String link3 = "<url>"
                            + "<loc>" + "http://chodientu.vn" + UrlUtils.browse(ct3.getId(), ct3.getName()) + "</loc>"
                            + "<lastmod>2014-11-28</lastmod>"
                            + "<changefreq>daily</changefreq>"
                            + "<priority>0.9</priority>"
                            + "</url>";
                    listLink.add(link3);
                }
            }
        }
        String closeTag = "</urlset>";
        listLink.add(closeTag);
        return new Response(true, "get sitemap", listLink);
    }

    @RequestMapping(value = "/getorderchoxuxu", method = RequestMethod.GET)
    @ResponseBody
    public Response getorderchoxuxu() throws Exception {
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setPageIndex(0);
        orderSearch.setPageSize(6000);
        orderSearch.setCreateTimeFrom(1414774800000l);
        orderSearch.setCreateTimeTo(1417280400000l);
        orderSearch.setShipmentPrice(-1);
        DataPage<Order> search = orderService.search(orderSearch);

        List<String> ids = new ArrayList();
        List<String> orderIds = new ArrayList();
        for (Order col : search.getData()) {
            ids.add(col.getId());
        }
        List<OrderItem> orderItems = orderService.getOrderItems(ids);
        for (OrderItem orderItem : orderItems) {
            if (!orderIds.contains(orderItem.getOrderId())) {
                orderIds.add(orderItem.getOrderId());
            }
        }
        return new Response(true, "Thông tin mã Order không có phí vận chuyển trong tháng 11 cho chị Xu", orderIds);

    }

    @RequestMapping(value = "/getordersc", method = RequestMethod.GET)
    @ResponseBody
    public Response getordersc(@RequestParam(value = "type") int type) throws Exception {
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setPageIndex(0);
        orderSearch.setPageSize(6000);
        orderSearch.setCreateTimeFrom(1417626000000l);
        orderSearch.setCreateTimeTo(1417692000000l);
        orderSearch.setShipmentStatusSearch(type);
        DataPage<Order> search = orderService.search(orderSearch);

        List<String> ids = new ArrayList();
        List<String> ladingIds = new ArrayList();
        for (Order col : search.getData()) {
            ids.add(col.getId());
        }
        List<Lading> byOrderIds = ladingService.getByOrderIds(ids);
        for (Lading byOrderId : byOrderIds) {
            if (byOrderId.getShipmentPrice() <= 0) {
                ladingIds.add(byOrderId.getOrderId());
            }
        }

        return new Response(true, "Thông tin mã Order không có tính được phí truyền sang SC", ladingIds);

    }

    @RequestMapping(value = "/testSearchFast", method = RequestMethod.GET)
    @ResponseBody
    public Response testSearchFast(@ModelAttribute ItemSearch itemSearch) {
        itemSearch.setPageIndex(itemSearch.getPageIndex() > 0 ? itemSearch.getPageIndex() - 1 : 0);
        itemSearch.setSellerId("437470");
        DataPage<Item> dataPage = itemService.search100(itemSearch.getSellerId());
        List<Item> products = dataPage.getData();
        List<String> imgs = null;
        for (Item item : products) {
            imgs = new ArrayList<>();
            for (String img : item.getImages()) {
                try {
                    imgs.add(imageService.getUrl(img).thumbnail(100, 100, "inset").getUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            item.setImages(imgs);
        }
        return new Response(true, "Test chuc nang search san pham nhanh khi tao hoa don hang loat" + dataPage.getData().size(), dataPage);
    }

    @RequestMapping(value = "/runreport", method = RequestMethod.GET)
    @ResponseBody
    public Response runreport(@RequestParam(value = "from") int from, @RequestParam(value = "to") int to, @RequestParam(value = "month") int month, @RequestParam(value = "type") String type) {
        switch (type) {
            case "order":
                reportTestService.runReportOderTest(from, to, month);
                break;
            case "cash":
                reportTestService.runReportCashTest(from, to, month);
                break;
            case "lading":
                reportTestService.runReportLadingTest(from, to, month);
                break;
            case "shop":
                reportTestService.runReportShopTest(from, to, month);
                break;
            case "item":
                reportTestService.runReportItem(from, to, month);
                break;
            default:
                return new Response(false, "Á đù");
        }

        return new Response(true, "Đã chạy thống kê thành công");
    }

    @RequestMapping(value = "/runreports", method = RequestMethod.GET)
    @ResponseBody
    public Response runreports(@RequestParam(value = "from") int from, @RequestParam(value = "to") int to, @RequestParam(value = "month") int month) {
        return reportTestService.cOrderTests(from, to, month);

    }

    @RequestMapping(value = "/testsearchItem", method = RequestMethod.GET)
    @ResponseBody
    public Response testSearchItem() {
        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setPageSize(40);
        itemSearch.setPageIndex(0);
        DataPage<Item> itemPage = itemService.searchMongo(itemSearch);
        return new Response(true, "Đã chạy thống kê thành công", itemPage.getData());
    }

    @RequestMapping(value = "/testupdateprice", method = RequestMethod.GET)
    @ResponseBody
    public Response testUpdatePrice(@RequestParam(value = "id") String id) {
        itemFixService.fixItem(id);
//        itemFixService.fixItem();
        return new Response(true, "List all items updated" + id, true);
    }

//    @RequestMapping(value = "/testcountupdated", method = RequestMethod.POST)
//    @ResponseBody
//    public Response testCountUpdatedPrice() {
//        long count = 0;
//        count = itemFixService.countFixed();
//        return new Response(true, "Count number of item updated: ", count);
//    }
    @RequestMapping(value = "/testcountreport", method = RequestMethod.POST)
    @ResponseBody
    public Response testCountUpdatedPrice(@RequestBody Request data) {
        String param = gson.fromJson(data.getParams(), String.class);
        String code = gson.fromJson(data.getCode(), String.class);
        if (param.equals("user")) {
            reportService.runReportUser();
        } else if (param.equals("buyer")) {
            reportService.runReportBuyer();
        } else if (param.equals("seller")) {
            reportService.runReportSeller();
        } else if (param.equals("cash")) {
            reportService.runReportCash();
        } else if (param.equals("item")) {
            reportService.runReportItem();
        } else if (param.equals("landing")) {
            reportService.runReportLading();
        } else if (param.equals("order")) {
            reportService.runReportOder();
        } else if (param.equals("shop")) {
            reportService.runReportShop();
        } else if (param.equals("all")) {
            reportService.run();
        } else if (param.equals("seller2")) {
//            int numDay = Integer.parseInt(param.substring(param.indexOf(",")));
            reportService.runReportMonth("seller", 2);
        } else if (param.equals("buyer2")) {
//            int numDay = Integer.parseInt(param.substring(param.indexOf(",")));
            reportService.runReportMonth("buyer", 2);
        } else if (param.equals("shop2")) {
//            int numDay = Integer.parseInt(param.substring(param.indexOf(",")));
            reportService.runReportMonth("shop", 2);
        }
        long countUsernoActiveT = 0;
        long countUsernoActiveF = 0;
        long countUserSCIntegrated = 0;
        long countUserLockedT = 0;
        long countUserLockedF = 0;
        long countBuyerUnique = 0;
        long countBuyerOnce = 0;
        long countBuyerRefund = 0;
        long countBuyerSCIntegrated = 0;
        long countNewSeller = 0;
        long countSuccessSeller = 0;
        long countFirstSuccessSeller = 0;
        long countReturnedSeller = 0;
        long countSCSeller = 0;
        long countlockedShop = 0;
        if (code.equals("F")) {
            countUserSCIntegrated = reportService.countUserSCIntegrated();
            countUsernoActiveT = reportService.countUserNoActive(true);
            countUsernoActiveF = reportService.countUserNoActive(false);
            countUserLockedT = reportService.countLockUser(true);
            countUserLockedF = reportService.countLockUser(false);
            countBuyerUnique = orderService.countBuyerUnique(false);
            countBuyerOnce = orderService.countBuyerOnce(false);
            countBuyerRefund = orderService.countBuyerReturn(false);
            countBuyerSCIntegrated = orderService.countBuyerSCIntegrated(false);
            countNewSeller = itemService.countNewSeller(false);
            countSuccessSeller = orderService.countSuccessSeller(false);
            countReturnedSeller = orderService.countReturnedSeller(false);
//            countlockedShop = reportService.countLockedShop();
        } else {
            countUserSCIntegrated = reportService.countUserSCIntegrated();
            countUsernoActiveT = reportService.countUserNoActive(true);
            countUsernoActiveF = reportService.countUserNoActive(false);
            countUserLockedT = reportService.countLockUser(true);
            countUserLockedF = reportService.countLockUser(false);
            countBuyerUnique = orderService.countBuyerUnique(true);
            countBuyerOnce = orderService.countBuyerOnce(true);
            countBuyerRefund = orderService.countBuyerReturn(true);
            countBuyerSCIntegrated = orderService.countBuyerSCIntegrated(true);
            countNewSeller = itemService.countNewSeller(true);
            countSuccessSeller = orderService.countSuccessSeller(true);
            countReturnedSeller = orderService.countReturnedSeller(true);
//            countlockedShop = reportService.countLockedShop();
        }
        return new Response(true, "Count number of item updated: "
                + " --- User Locked (ALL)" + countUserLockedT
                + " --- User Locked (Curr)" + countUserLockedF
                + " --- User No Active (curr): " + countUsernoActiveF
                + " --- User No Active (All ) " + countUsernoActiveT
                + " --- User ShipChung Integrated " + countUserSCIntegrated
                + " --- Buyer Refund" + countBuyerRefund
                + " --- Buyer ShipChung Integrated" + countBuyerSCIntegrated
                + " --- Buyer Unique : " + countBuyerUnique
                + " --- Buyer Once : " + countBuyerOnce
                + " --- new Seller : " + countNewSeller
                + " --- success Seller : " + countSuccessSeller
                + " --- first success seller : " + countFirstSuccessSeller
                + " --- returned seller :" + countReturnedSeller
                + " --- ShipChung seller :" + countSCSeller
                + " --- locked shop : " + countlockedShop
                + "", true);
    }

    @RequestMapping(value = "/exportitem4marketingP", method = RequestMethod.GET)
    public void export4Marketing(HttpServletResponse response,
            @RequestParam(value = "cateId", required = true, defaultValue = "0") int cateId) throws IOException, Exception {
        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Sheet-CDT");
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);
        row.createCell(0).setCellValue("ProductID");
        row.createCell(1).setCellValue("Name");
        row.createCell(2).setCellValue("URL");
        row.createCell(3).setCellValue("Big Image");
        row.createCell(4).setCellValue("Small Image");
        row.createCell(5).setCellValue("Price");
        row.createCell(6).setCellValue("Category 1");
        row.createCell(7).setCellValue("Category 2");
        row.createCell(8).setCellValue("Category 3");
        row.createCell(9).setCellValue("Description");
        row.createCell(10).setCellValue("Retail Price*");
        row.createCell(11).setCellValue("Discount");
        int pageSize = 50000;
        List<ItemMarketing> itemMarketings = itemMarketingService.getSearch(cateId, pageSize);
        int i = 1;
        for (ItemMarketing im : itemMarketings) {

            i++;
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(im.getId());
            if (im.getName() != null) {
                row.createCell(1).setCellValue(im.getName());
            }
            row.createCell(2).setCellValue(im.getUrl());
            if (im.getImage() != null) {
                row.createCell(3).setCellValue(im.getImage());
            }
            if (im.getSmallimage() != null) {
                row.createCell(4).setCellValue(im.getSmallimage());
            }
            row.createCell(5).setCellValue(im.getPrice());
            row.createCell(6).setCellValue(im.getCategory());
            row.createCell(7).setCellValue(im.getCategory2());

            row.createCell(8).setCellValue(im.getCategory3());
            row.createCell(9).setCellValue(im.getDescription());
            if (im.getDiscount().equals("") || im.getDiscount().equals("0")) {
                row.createCell(10).setCellValue(im.getPrice());
            } else {
                row.createCell(10).setCellValue(im.getSprice());
            }
            row.createCell(11).setCellValue(im.getDiscount());

        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=DSSP_Marketing_" + cateId + ".xls");
        wb.write(response.getOutputStream());
        return;

    }

    @RequestMapping(value = "/exportitem4marketingGoogleP", method = RequestMethod.GET)
    public void export4MarketingGoogle(HttpServletResponse response,
            @RequestParam(value = "cateId", required = true, defaultValue = "0") String cateId) throws IOException, Exception {
        String categoryId = "";
        if (cateId != null && !cateId.equals("")) {
            categoryId = cateId;
        } else {
            return;
        }
        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Sheet-CDT");
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);
        row.createCell(0).setCellValue("ProductID");
        row.createCell(1).setCellValue("Name");
        row.createCell(2).setCellValue("URL");
        row.createCell(3).setCellValue("Big Image");
        row.createCell(4).setCellValue("Small Image");
        row.createCell(5).setCellValue("Price");
        row.createCell(6).setCellValue("Category 1");
        row.createCell(7).setCellValue("Category 2");
        row.createCell(8).setCellValue("Category 3");
        row.createCell(9).setCellValue("Description");
        row.createCell(10).setCellValue("Retail Price*");
        row.createCell(11).setCellValue("Discount");
        Category category = categoryService.get(categoryId);
        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setCategoryIds(new ArrayList<String>());
        itemSearch.getCategoryIds().add(categoryId);
        itemSearch.setStatus(1);
        itemSearch.setPageSize(600);
        itemSearch.setPageIndex(0);
        DataPage<Item> itemPage = itemService.search(itemSearch);
        int i = 1;
//        while (itemPage.getPageIndex() < itemPage.getPageCount()) {
        if (itemPage.getData() == null) {
        } else {
            for (Item item : itemPage.getData()) {
                i++;
                //get Item Image URL 
                List<String> images = new ArrayList<>();
                if (item.getImages() != null && !item.getImages().isEmpty()) {
                    for (String img : item.getImages()) {
                        String imgName = "image name";
                        String bigImg = imageService.getUrl(img).compress(100).getUrl(imgName);
                        if (item.getName() != null) {
                            imgName = item.getName();
                        }
                        String smallImg = imageService.getUrl(img).thumbnail(350, 350, "inset").getUrl(imgName);
                        if (bigImg != null && smallImg != null) {
                            images.add(bigImg);
                            images.add(smallImg);
                            break;
                        }
                    }
                }
                item.setImages(images);
                // get Item Destination URL
                String itemUrl = UrlUtils.item(item.getId(), item.getName());
                //Lấy detail sản phẩm
                ItemDetail itemDetail = null;
                try {
                    itemDetail = itemService.getDetail(item.getId());
                } catch (Exception e) {
                }
                // lay ten category
                String cate1 = "";
                String cate2 = "";
                String cate3 = "";
                if (item.getCategoryPath().size() == 1) {
                    cate1 = categoryService.getCategories(item.getCategoryPath()).get(0).getName();
                } else if (item.getCategoryPath().size() == 2) {
                    cate1 = categoryService.getCategories(item.getCategoryPath()).get(0).getName();
                    cate2 = categoryService.getCategories(item.getCategoryPath()).get(1).getName();
                } else if (item.getCategoryPath().size() == 3) {
                    cate1 = categoryService.getCategories(item.getCategoryPath()).get(0).getName();
                    cate2 = categoryService.getCategories(item.getCategoryPath()).get(1).getName();
                    cate3 = categoryService.getCategories(item.getCategoryPath()).get(2).getName();
                }
                //get price
                String price = TextUtils.startPrice(item.getStartPrice(), item.getSellPrice(), item.isDiscount());
                if (price.equals("0")) {
                    price = String.valueOf(item.getStartPrice());
                }
                String sellPrice = TextUtils.sellPrice(item.getSellPrice(), item.isDiscount(), item.getDiscountPrice(), item.getDiscountPercent());
                String discount = TextUtils.percentFormat(item.getStartPrice(), item.getSellPrice(), item.isDiscount(), item.getDiscountPrice(), item.getDiscountPercent());
                row = sheet.createRow((short) i);
                row.createCell(0).setCellValue(item.getId());
                if (item.getName() != null) {
                    row.createCell(1).setCellValue(item.getName());
                }
                row.createCell(2).setCellValue("http://chodientu.vn" + itemUrl);
                if (item.getImages() != null) {
                    if (item.getImages().size() > 0) {
                        row.createCell(3).setCellValue(item.getImages().get(0));
                    }
                }
                if (item.getImages() != null) {
                    if (item.getImages().size() > 0) {
                        row.createCell(4).setCellValue(item.getImages().get(1));
                    }
                }
                row.createCell(5).setCellValue(sellPrice);
                if (cate1 != null && !cate1.equals("")) {
                    row.createCell(6).setCellValue(cate1);
                } else {
                    row.createCell(6).setCellValue("");
                }
                if (cate2 != null && !cate2.equals("")) {
                    row.createCell(7).setCellValue(cate2);
                } else {
                    row.createCell(7).setCellValue("");
                }
                if (cate3 != null && !cate3.equals("")) {
                    row.createCell(8).setCellValue(cate3);
                } else {
                    row.createCell(8).setCellValue("");
                }
                if (itemDetail != null) {
                    if (itemDetail.getDetail() != null) {
                        if (itemDetail.getDetail().length() > 4000) {
                            row.createCell(9).setCellValue(itemDetail.getDetail().substring(0, 4000));
                        } else {
                            row.createCell(9).setCellValue(itemDetail.getDetail());
                        }
                    }
                }
                row.createCell(10).setCellValue(price);
                row.createCell(11).setCellValue(discount);
            }
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=DSSP_Google_" + cateId + ".xls");
        wb.write(response.getOutputStream());
        return;

    }

    @RequestMapping(value = "/exportCsv", method = RequestMethod.GET)
    @ResponseBody
    public Response exportCsv(HttpServletResponse response,
            @RequestParam(value = "cateId", required = true, defaultValue = "0") int cateId) throws IOException, Exception {
        int pageSize = 50000;
        List<ItemMarketing> itemMarketings = itemMarketingService.getSearch(cateId, pageSize);
        int i = 1;
        List<String> list = new ArrayList<>();
        for (ItemMarketing im : itemMarketings) {

            i++;
            String xml = "<item>"
                    + "<id>" + im.getId() + "</id>"
                    + "<name>" + im.getName() + "</name>"
                    + "<image>" + im.getImage() + "</image>"
                    + "<smallimage>" + im.getSmallimage() + "</smallimage>"
                    + "<price>" + im.getPrice() + "</price>"
                    + "<category1>" + im.getCategory() + "</category1>"
                    + "<category2>" + im.getCategory2() + "</category2>"
                    + "<category3>" + im.getCategory3() + "</category3>"
                    + "<description>" + im.getDescription() + "</description>"
                    + "<rprice>" + im.getSprice() + "</rprice>"
                    + "<discount>" + im.getDiscount() + "</discount>"
                    + "</item>";
            list.add(xml);
        }
        return new Response(true, "ok", list);

    }

    @RequestMapping(value = "/testsc", method = RequestMethod.POST)
    @ResponseBody
    public Response testsc() throws JSONException, IOException {
        JSONObject sexJSON = new JSONObject();
        JSONObject toJSON = new JSONObject();
        JSONObject orderJSON = new JSONObject();
        JSONObject configJSON = new JSONObject();
        JSONObject paramJSON = new JSONObject();
//        jsonFrom.put("City", fromCity);
//        jsonFrom.put("Province", fromDistrict);
        sexJSON.put("City", 18);
        sexJSON.put("Province", 163);
        paramJSON.put("From", sexJSON);
//        params.put("From", gson.toJson(jsonFrom));
        //To
//        Map<String, Object> jsonTo = new HashMap<>();
//        jsonTo.put("City", toCity);
//        jsonTo.put("Province", toDistrict);
//        jsonTo.put("Ward", "");
        toJSON.put("City", 18);
        toJSON.put("Province", 173);
        //toJSON.put("Ward", "");
        paramJSON.put("To", toJSON);
//        params.put("To", gson.toJson(jsonTo));
        //Order
//        Map<String, Object> jsonOrder = new HashMap<>();
//        jsonOrder.put("Amount", price);
//        jsonOrder.put("Weight", weight);
//        jsonOrder.put("BoxSize", "");
        orderJSON.put("Amount", 19000);
        orderJSON.put("Weight", 700);
        orderJSON.put("BoxSize", "");
//        params.put("Order", gson.toJson(jsonOrder));
        paramJSON.put("Order", orderJSON);
        //Config
//        Map<String, Object> jsonConfig = new HashMap<>();
        configJSON.put("Service", 1);
        configJSON.put("CoD", 1);
        configJSON.put("Protect", 1);
        configJSON.put("Payment", 1);

//        params.put("Config", gson.toJson(jsonConfig));
        paramJSON.put("Config", configJSON);
        String url = "http://services.shipchung.vn/api/rest/lading/calculate";
        String str = call(url, paramJSON, null);
        return new Response(true, "ok", str);
    }

    private String call(String url, JSONObject params, String token) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        if (token != null) {
            post.setHeader("Authorization", token);
        }
        StringEntity entity = new StringEntity(params.toString(), HTTP.UTF_8);
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder text = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            text.append(line);
        }

        return text.toString();
    }

    @RequestMapping(value = "/ebay", method = RequestMethod.GET)
    @ResponseBody
    public Response ebay(HttpServletResponse response) throws Exception {
        String orderId = "983331092298";
        String to = "anhpp@peacesoft.net";
        User user = userRepository.getByEmail(to);
        HashMap<String, String> error = new HashMap<>();
        String emailTo = "";
        if (user == null) {
//            error.put("toEmail", "Không tìm thấy địa chỉ email người nhận");
            // get email from order 
            Order order = orderService.get(orderId);
            if (order == null) {
                error.put("toEmail", "Không tìm thấy địa chỉ email người nhận.");
            } else {
                emailTo = order.getBuyerEmail();
            }
        } else {
            emailTo = user.getEmail();
        }
        if (!error.isEmpty()) {
            return new Response(false, null, error);
        }
        Message message = new Message();
        message.setCreateTime(System.currentTimeMillis());
        message.setContent("tessssssssss");
        message.setFromEmail("no-reply@chodientu.vn");
        message.setFromName("Chợ điện tử");
        message.setFromUserId("");
        message.setItemId(null);
        message.setLastIp("");
        message.setOrderId(null);
        message.setLastView(System.currentTimeMillis());
        message.setRead(false);
        message.setRemove(false);
        message.setSubject("paaaaaaaaa");
        message.setToEmail(emailTo);
        message.setToName(user.getName() != null ? user.getName() : (user.getUsername() != null ? user.getUsername() : user.getEmail()));
        message.setToUserId(user.getId());
        message.setUpdateTime(System.currentTimeMillis());
        messageRepository.save(message);

        realTimeService.add("Bạn vừa nhận được 1 tin nhắn từ " + message.getFromName(), message.getToUserId(), "/user/quan-ly-thu.html", "Xem tin nhắn", null);

        return new Response(true, "Gửi tin nhắn thành công", message);
    }

    @RequestMapping(value = "/createitem", method = RequestMethod.GET)
    @ResponseBody
    public Response createitem(@RequestParam String itemId) throws Exception {
        Item item = itemService.get(itemId);
        Response add = null;
        if (item != null) {
            item.setId(null);
            item.setSellerId("437470");
            add = itemService.add(item);
        }
        return add;

    }

    @RequestMapping(value = "/exportShop6Month", method = RequestMethod.GET)
    public void exportShop6Month(HttpServletResponse response) throws IOException, Exception {
        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Sheet-CDT");
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);
        row.createCell(0).setCellValue("User");
        row.createCell(1).setCellValue("Xèng hiện tại");
        String input = "jean135.jean165,hoangnhandeal,zombieshop,thao_trang_1983,giaygiare,thocambity,thoitrangkimlien.vn,thailanfashion,shoppingpnj,miracleshop,loananhsport,aocuoikhanhlinh,jerryjj,shasashop,lantui,dangkhuyenmai,KTfashion,shopquynhvy,thoitrangphucan,yumefashion,tagashop,vancute,mobilenhuy,phatminhkhangmobile,giachinhmobile,htrilam,kimcuongnguyen53,minh2604,thangnguyencomputer,nguyenthimyuyen,TanDuyLinh,sieuthilientinh,trungquan8622tb,CameraKhanhLong,trannguyenhan,Linhcamera80,bmbvietnam,amthanhbaoduong,mrhuy,hoathienthao,ngocphuong80,pasta86,modviet,chamsocsacdep,kemtrimun24hcom,trangbeautycare,shophangngoaicom,netviet2012,ngochongsam,kemnghia,altapro,shopironstyle,dogominhnhat,sieuthinem,noithatkal,lamgiakhang,mebeshopping,linhkiendienlanh,thietbidienthongminh,sontaytang,minhphungpiano,3kshop,chilan_hoanggia,ICSG,vinhtuong_water,tbcnquangminh,pianominhthanh,hanggiasoc,nhamy_shop,daquylehay,luckydeal,dientuhuyenanh,bpgemstone,hung_laser,dientuyenson,phuonglemart,hatungmobile,sonlongmobile,lanhlung213,hangbtcat,dienthoaikomart,giavulaptops,thegioidodung,revadep,hangxachtay74,sieuthithuocgiamcan,Tantungphat68,bebenguyenshop,maiglink,cosotuandung,SHOPMAICONG,xadonxep,ctynamthanglong,potavietnam, docodocu,kittyshop,xedapcacloai,ghegiuong,sonkt2011,hanggiadung,everonchinhhang,sieuthi_giadinh,xuantruongtx,halomarthanoi,Harmonyvietnam,thamthong,scom,goldspace.vn,royaltime,shopthanhdat,hungkvcu,hv_telecom,f5provn,bialaos,dienhoaquynhhoa,hieu_itsme,zinshop123,dientuduchieu.com,shop_oriflame142,jatekmobile,thegioigiaysh,familyshop2312,dangcap9x,nhaccuquythanh,sirlee1,dochoihiepluc123,sieuthi_quangchau,sunshinesy2k,toanphatinfo,shopphukienhot,sonphucnguyen,thanghv5,thanhthaoshop,hieusut,ximax,dientunguyenvinh,Shoptuancua,lotusjsc,Shopquyba,longcasio,yesbuyvn,dungtt79,truong1973,haihiencompany,diendandunghungdung,linhchikorea,shopnhat123,shopquynhhoa,eurostyle,aobong,a_designer,metieuminh,linhhaison,dongho25h,phimcachnhietngoisao,honeyQUEEN,hiepanhco,umove,thietbimayvanphong,baotrung248,Anphutrading,vandat95c,asimcotech,daychuyenmatchu,0912259090,aegroup,techlandhn,banhtrungthutungha,Chefstore,kieudung265,dienthoaisinhvien,nvmanh2000,trangquynhmobile,didongsaigon1310,phucuong011,duclinh2mobile,tienthuaniphone,123iphone,thegioiappleari,viet_singmobile,vitinhhuynhgia,ktcomputer,tuankhanhcom,lqv77,truongleaudio,vitinhthungan,ginghitaru,hothanhtin7250,AudioDongThanh,Nytshop,traxanhgiamcan,takasima,sakura_deng86,nguyenhuongbmt,lamdepdeal,nuhoangsacdep,myphamngocngan,Myphamgiasi,ngocminhshop,thailandshoppingcenter,shopthoitrangtd,CocoBoutique,Ahasport,shop4all,thanhtamtts,nhocgiftshop,congtydaiviet,anhmauhainam,heovanggiftshop,thietbidienhuunghi,hungvuong220,thanhbinhminh,champaresort,CongCuong.com,dodungchobe,mayhanchipset,SAIGONMOVERS,quatanglinhnam,tranhdonghodongviet,daotruongnguyen,thienhoaonline";
        String[] myArray = input.split(",");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_YEAR, -(6 * 30));
        long cl = calendar.getTimeInMillis();
        int i = 1;
        for (String string : myArray) {
            User user = userRepository.findByUsername(string);
            if (user != null) {
                long upTime = user.getUpdateTime();

                if (upTime < cl) {
                    i++;
                    row = sheet.createRow(i);
                    row.createCell(0).setCellValue(string);

                    if (user.getId() != null) {
                        Cash cash = cashService.getCash(user.getId());
                        if (cash != null) {
                            long c = cash.getBalance();
                            row.createCell(1).setCellValue(c);
                            if (c > 0) {
                                cashService.delCashAction(user.getId());
                            }

                        }
                    }
                }
            }
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=CheckShop.xls");
        wb.write(response.getOutputStream());
        return;

    }

    @RequestMapping(value = "/exportOrder", method = RequestMethod.GET)
    public void exportOrder(HttpServletResponse response, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "0") int size) throws IOException, Exception {
        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Sheet-CDT");
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);
        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("Email");
        row.createCell(2).setCellValue("Phone");
        row.createCell(3).setCellValue("Shop");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_YEAR, -(6 * 30));
        long cl = calendar.getTimeInMillis();
        int i = 1;
        Criteria criteria = new Criteria("active").is(true);
        criteria.and("emailVerified").is(true).and("phoneVerified").is(true);
        List<String> userIdsC = new ArrayList<>();
        List<User> users = userRepository.find(new Query(criteria).with(new PageRequest(page, size)));
        for (User user : users) {
            if (!userIdsC.contains(user.getId())) {
                userIdsC.add(user.getId());
            }
        }
        List<Seller> sellers = sellerRepository.find(new Query(new Criteria("nlIntegrated").ne(true).and("scIntegrated").ne(true).and("_id").in(userIdsC)));

        List<String> userIds = new ArrayList<>();
        List<String> userIdsOK = new ArrayList<>();
        for (Seller seller : sellers) {
            if (!userIds.contains(seller.getUserId())) {
                userIds.add(seller.getUserId());
            }
        }
        Criteria criteria1 = new Criteria("sellerId").in(userIds);
        criteria1.and("createTime").gte(1412096400000l).lte(1422259266000l);
        List<Item> items = itemRepository.find(new Query(criteria1));
        for (Item item : items) {
            if (!userIdsOK.contains(item.getSellerId())) {
                userIdsOK.add(item.getSellerId());
            }
        }
        List<Shop> shops = shopRepository.find(new Query(new Criteria("_id").in(userIdsOK)));
        for (Shop shop : shops) {
            for (User user : users) {
                if (user.getId().equals(shop.getUserId())) {
                    user.setEmailVerified(true);
                } else {
                    user.setEmailVerified(false);
                }
            }
        }

        for (User user : users) {
            if (userIdsOK.contains(user.getId())) {
                i++;
                row = sheet.createRow(i);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getEmail() == null ? "NULL" : user.getEmail());
                row.createCell(2).setCellValue(user.getPhone() == null ? "NULL" : user.getPhone());
                row.createCell(3).setCellValue(user.isEmailVerified() ? "TRUE" : "");

            }
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=danh-sach-seller.xls");
        wb.write(response.getOutputStream());
        return;

    }

    @RequestMapping(value = "/delCash", method = RequestMethod.GET)
    @ResponseBody
    public Response delCash(@RequestParam(value = "start", required = true, defaultValue = "1421773200000") String start) throws Exception {
        //String start="1421600400000";
        long startTime = Long.parseLong(start);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        calendar.add(Calendar.DAY_OF_YEAR, 30);
        long endTime = calendar.getTimeInMillis();

        List<User> users = orderService.getBuyerIdFromOrder(startTime, endTime);
        return cashService.delCash(users, startTime, endTime);
        //return cashService.infoCash(id,startTime, endTime);

    }

    @RequestMapping(value = "/revertDelCash", method = RequestMethod.GET)
    @ResponseBody
    public Response revertDelCash(@RequestParam(value = "start", required = true, defaultValue = "1421773200000") String start) throws Exception {
        //String start="1421600400000";
        long startTime = Long.parseLong(start);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        calendar.add(Calendar.DAY_OF_YEAR, 30);
        long endTime = calendar.getTimeInMillis();

        List<User> users = orderService.getBuyerIdFromOrder(startTime, endTime);
        return cashService.revertDelCash(users);

    }

    @RequestMapping(value = "/loadWard", method = RequestMethod.GET)
    @ResponseBody
    public String loadWard() throws Exception {
        districtService.loadWard();
        return "ok";

    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    @ResponseBody
    public Response signin(@RequestBody UserLoginForm loginForm) throws Exception {
        try {
            //UserLoginForm loginForm = gson.fromJson(data.getParams(),UserLoginForm.class);
            if (loginForm == null) {
                return new Response(false, "Thông tin chưa chính xác");
            }
            String username = loginForm.getUsername();
            String password = loginForm.getPassword();
            return userService.signin(username, password);
        } catch (Exception e) {
            return new Response(false, "Đăng nhập ChợĐiệnTử thất bại!", null);
        }
    }

    @RequestMapping(value = "/toLowerCaseShop", method = RequestMethod.GET)
    @ResponseBody
    public Response toLowerCaseShop() throws Exception {
        List<Shop> shops = shopRepository.find(new Query(new Criteria("alias").ne(null)));
        for (Shop shop : shops) {
            Shop find = shopRepository.find(shop.getUserId());
            find.setAlias(shop.getAlias().toLowerCase());
            shopRepository.save(find);
        }
        return new Response(true, "Chuyển hết Alias shop từ hoa sang chữ thường");

    }

    @RequestMapping(value = "/exportseller", method = RequestMethod.GET)
    public void exportSeller(HttpServletResponse response) throws IOException, Exception {
        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Sheet-CDT");
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);
        row.createCell(0).setCellValue("UserId CĐT");
        row.createCell(1).setCellValue("EmailChợ");
        row.createCell(2).setCellValue("EmailSC");
        row.createCell(3).setCellValue("Merchant key");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_YEAR, -(6 * 30));
        long cl = calendar.getTimeInMillis();
        int i = 1;
        List<Seller> sellers = sellerRepository.find(new Query(new Criteria("scIntegrated").is(true).and("scEmail").ne(null)));
        List<String> userIds = new ArrayList<>();
        for (Seller seller : sellers) {
            if (!userIds.contains(seller.getUserId())) {
                userIds.add(seller.getUserId());
            }
        }
        List<User> users = userRepository.find(new Query(new Criteria("_id").in(userIds)));
        for (Seller seller : sellers) {
            for (User user : users) {
                if (user.getId().equals(seller.getUserId())) {
                    i++;
                    row = sheet.createRow(i);
                    row.createCell(0).setCellValue(seller.getUserId());
                    row.createCell(1).setCellValue(user.getEmail());
                    row.createCell(2).setCellValue(seller.getScEmail());
                    row.createCell(3).setCellValue(seller.getMerchantKey());
                }
            }
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=danh-sach-seller.xls");
        wb.write(response.getOutputStream());
        return;

    }

    @RequestMapping(value = "/createmerchantkeyseller", method = RequestMethod.GET)
    public void createMerchantKeySeller() {
        List<Seller> sellers = sellerRepository.find(new Query(new Criteria("scIntegrated").is(true).and("scEmail").ne(null).and("merchantKey").is(null)));
        for (Seller seller : sellers) {
            Seller seller1 = sellerRepository.find(seller.getUserId());
            seller1.setMerchantKey(md5(seller1.getScEmail() + seller1.getUserId()));
            sellerRepository.save(seller1);
        }
    }

    public static String md5(String input) {
        String md5 = null;
        if (null == input) {
            return null;
        }
        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            //Converts message digest value in base 16 (hex) 
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        return md5;
    }

    @RequestMapping(value = "/indexmodel", method = RequestMethod.GET)
    public void indexmodel() {
        Response index = modelService.index();
    }

    @RequestMapping(value = "/getnametolandingitem", method = RequestMethod.GET)
    public void getnametolandingitem() {
        List<LandingItem> landingItems = landingItemRepository.find(new Query(new Criteria()));
        List<String> itemIds = new ArrayList();

        for (LandingItem landingItem : landingItems) {
            if (!itemIds.contains(landingItem.getItemId())) {
                itemIds.add(landingItem.getItemId());
            }
        }
        List<Item> items = itemRepository.get(itemIds);
        for (Item item : items) {
            for (LandingItem landingItem : landingItems) {
                if (landingItem.getItemId().equals(item.getId())) {
                    landingItem.setName(item.getName());
                    landingItemRepository.save(landingItem);
                }
            }
        }

    }

    @RequestMapping(value = "/exportGoogleRemarketing", method = RequestMethod.GET)
    public void export4Marketing(HttpServletResponse response,
            @RequestParam(value = "cate", required = true) String cate, 
            @RequestParam(value = "cod", required = true) int cod, 
            @RequestParam(value = "page", required = true, defaultValue = "0") int page) throws IOException, Exception {
        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Sheet-CDT");
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);
        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("ID2");
        row.createCell(2).setCellValue("Item title");
        row.createCell(3).setCellValue("Destination URL");
        row.createCell(4).setCellValue("Image URL");
        row.createCell(5).setCellValue("Item subtitle");
        row.createCell(6).setCellValue("Item description");
        row.createCell(7).setCellValue("Item category");
        row.createCell(8).setCellValue("Price");
        row.createCell(9).setCellValue("Sale price");
        row.createCell(10).setCellValue("Contextual keywords");
        row.createCell(11).setCellValue("Item address");
        row.createCell(12).setCellValue("Cod");
        Category category = categoryService.get(cate);
        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setCategoryIds(new ArrayList<String>());
        itemSearch.getCategoryIds().add(cate);
        itemSearch.setStatus(1);
        itemSearch.setPageSize(1000);
        itemSearch.setPageIndex(page);
        if (cod == 1) {
            itemSearch.setCod(true);
        }
        DataPage<Item> itemPage = itemService.search(itemSearch);
        int i = 1;
//        while (itemPage.getPageIndex() < itemPage.getPageCount()) {
        if (itemPage.getData() == null) {
        } else {
            for (Item item : itemPage.getData()) {
                i++;
                //get Item Image URL 
                List<String> images = new ArrayList<>();
                if (item.getImages() != null && !item.getImages().isEmpty()) {
                        for (String img : item.getImages()) {
                            String name="image product";
                            if(item.getName() !=null)name=item.getName();
                            String bigImg = imageService.getUrl(img).compress(100).getUrl(name);
                            if (bigImg != null) {
                                images.add(bigImg);
                                break;
                            }
                        }
                    }
                item.setImages(images);
                // get Item Destination URL
                String itemUrl = UrlUtils.item(item.getId(), item.getName());
                //Lấy detail sản phẩm
                ItemDetail itemDetail = null;
                try {
                    itemDetail = itemService.getDetail(item.getId());
                } catch (Exception e) {
                }
                //get price
                String price = TextUtils.startPrice(item.getStartPrice(), item.getSellPrice(), item.isDiscount());
                if (price.equals("0")) {
                    price = String.valueOf(item.getStartPrice());
                }
                price = price.replaceAll("\\.", "");
                String sellPrice = TextUtils.sellPrice(item.getSellPrice(), item.isDiscount(), item.getDiscountPrice(), item.getDiscountPercent());
                sellPrice = sellPrice.replaceAll("\\.", "");
                row = sheet.createRow((short) i);
                row.createCell(0).setCellValue(item.getId());
                row.createCell(1).setCellValue("");
                if (item.getName() != null) {
                    row.createCell(2).setCellValue(item.getName());
                }
                row.createCell(3).setCellValue("http://chodientu.vn" + itemUrl);
                if (item.getImages() != null) {
                    if (item.getImages().size() > 0) {
                        row.createCell(4).setCellValue(item.getImages().get(0));
                    }
                }
                row.createCell(5).setCellValue("");
                if (itemDetail != null) {
                    if (itemDetail.getDetail() != null) {
                        if (itemDetail.getDetail().length() > 4000) {
                            row.createCell(6).setCellValue(itemDetail.getDetail().substring(0, 4000));
                        } else {
                            row.createCell(6).setCellValue(itemDetail.getDetail());
                        }
                    }
                }
                row.createCell(7).setCellValue(category.getName());

                row.createCell(8).setCellValue(price);
                row.createCell(9).setCellValue(sellPrice);
                row.createCell(10).setCellValue("no keyword");
                row.createCell(11).setCellValue("no item address");
                row.createCell(12).setCellValue(item.isCod());
            }
        }
//            itemSearch.setPageIndex(itemPage.getPageIndex() + 1);
//            itemPage = itemService.search(itemSearch);
//        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=DSSP_Marketing_" + cate + "_" + page + ".xls");
        wb.write(response.getOutputStream());
        return;

    }

    @RequestMapping(value = "/exportsellerhasshop", method = RequestMethod.GET)
    public void exportSellerHasShop(@RequestParam(value = "st", defaultValue = "0") long startTime,
            @RequestParam(value = "et", defaultValue = "0") long endTime,
            @RequestParam(value = "page", required = true, defaultValue = "0") int page,
            @RequestParam(value = "pageSize", required = true, defaultValue = "1000") int pageSize,
            HttpServletResponse response,
            HttpServletRequest request) throws IOException, Exception {
        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Sheet-CDT");
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);
        row.createCell(0).setCellValue("UserId CĐT");
        row.createCell(1).setCellValue("Link shop");
        row.createCell(2).setCellValue("Email");
        row.createCell(3).setCellValue("SĐT");
        int i = 1;
        if (endTime == 0) {
            endTime = System.currentTimeMillis();
        }
        //
        List<User> users = new ArrayList<>();
        List<Shop> shops = new ArrayList<>();
        List<String> listUsers = new ArrayList<>();
        //
        ShopSearch shopSearch = new ShopSearch();
        shopSearch.setPageIndex(0);
        shopSearch.setPageSize(500);
        DataPage<Shop> shopPage = shopService.search(shopSearch);
        while (shopPage.getPageIndex() < shopPage.getPageCount()) {
            List<Shop> listShop = shopPage.getData();
            if (listShop != null) {
                for (Shop tempShop : listShop) {
                    if (tempShop != null) {
                        listUsers.add(tempShop.getUserId());
                        shops.add(tempShop);
                    }
                }
//                users.addAll(userRepository.find(new Query(new Criteria("_id").in(listUserID).and("active").is(true))));
            } else {
                break;
            }
            shopSearch.setPageIndex(shopSearch.getPageIndex() + 1);
            shopPage = shopService.search(shopSearch);
        }
        UserSearch userSearch = new UserSearch();
        userSearch.setPageIndex(page);
        userSearch.setPageSize(pageSize);
        userSearch.setActive(1);
        DataPage<User> userPage = userService.search(userSearch);
        users = userPage.getData();
        for (User user : users) {
            if (listUsers.contains(user.getId())) {
                Shop shop = shops.get(listUsers.indexOf(user.getId()));
                if (user.getId().equals(shop.getUserId())) {
                    String linkShop = "http://chodientu.vn/" + shop.getAlias() + "/index.html";
                    i++;
                    row = sheet.createRow(i);
                    row.createCell(0).setCellValue(user.getId());
                    row.createCell(1).setCellValue(linkShop);
                    row.createCell(2).setCellValue(shop.getEmail());
                    row.createCell(3).setCellValue(shop.getPhone());
                }
            }
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=ds_shopseller_" + pageSize + "_" + page + ".xls");
        wb.write(response.getOutputStream());
        return;
    }

    @RequestMapping(value = "/exportsellernoshop", method = RequestMethod.GET)
    public void exportSellerNoShop(HttpServletResponse response) throws IOException, Exception {
        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Sheet-CDT");
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);
        row.createCell(0).setCellValue("UserId CĐT");
        row.createCell(1).setCellValue("Tên User");
        row.createCell(2).setCellValue("Email");
        row.createCell(3).setCellValue("SĐT");
        int i = 1;
        SellerShopSearch search = new SellerShopSearch();
        search.setActive(true);
        search.setPageIndex(0);
        search.setPageSize(500);
        DataPage<SellerShop> searchPage = sellerShopService.search(search);
        while (searchPage.getPageIndex() < searchPage.getPageCount()) {
            List<SellerShop> listSearch = searchPage.getData();
            for (SellerShop sellerShop : listSearch) {
                if (sellerShop.getShopId() == null || sellerShop.getShopId().equals("") || !sellerShop.getShopId().equals(sellerShop.getUserId())) {
                    i++;
                    row = sheet.createRow(i);
                    row.createCell(0).setCellValue(sellerShop.getUserId());
                    row.createCell(1).setCellValue(sellerShop.getName());
                    row.createCell(2).setCellValue(sellerShop.getEmail());
                    row.createCell(3).setCellValue(sellerShop.getPhone());
                }
            }
            search.setPageIndex(search.getPageIndex() + 1);
            searchPage = sellerShopService.search(search);
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=danh-sach-noshopseller.xls");
        wb.write(response.getOutputStream());
        return;
    }

//    @RequestMapping(value = "/runExportSellerShoptodb", method = RequestMethod.GET)
//    public void runExportSellerShoptodb() throws Exception {
//        sellerShopService.listSellerID();
//    }
    public static Map<String, Long> getDayofMonth(int month, int year) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = month + "/01/" + year;
        Date today = dateFormat.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        Date lastDayOfMonth = calendar.getTime();
        SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Date parse = dateFormats.parse(dateFormats.format(lastDayOfMonth));
        long milliseconds = parse.getTime() + 86399000;
        DateFormat sdfFirst = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        DateFormat sdfLast = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        //System.out.println("First Day of Month: " + sdfFirst.format(today));
        //System.out.println("Last Day of Month: " + sdfLast.format(lastDayOfMonth));
        Map<String, Long> map = new HashMap<>();
        map.put("first", today.getTime());
        map.put("last", milliseconds);
        return map;

    }

    @RequestMapping(value = "/exportOrder500k", method = RequestMethod.GET)
    public void exportOrder500k(HttpServletResponse response, @RequestParam(value = "month", required = true, defaultValue = "0") int month, @RequestParam(value = "year", required = true, defaultValue = "0") int year) throws Exception {

        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Sheet-CDT");
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);
        row.createCell(0).setCellValue("ShopID");
        row.createCell(1).setCellValue("ShopAlias");
        row.createCell(2).setCellValue("Email");
        row.createCell(3).setCellValue("Phone");
        Map<String, Long> dayofMonth = getDayofMonth(month, year);

        Criteria criteria = new Criteria();
        //criteria.and("finalPrice").lte(500000);
        criteria.and("createTime").gte(dayofMonth.get("first")).lte(dayofMonth.get("last"));
        // List<Order> orders = orderRepository.find(new Query(criteria));
        Map<String, Long> sumPriceFinal = orderRepository.sumPriceFinal(criteria);

        List<String> sellerIds = new ArrayList<>();

        if (sumPriceFinal != null && !sumPriceFinal.isEmpty()) {
            for (Map.Entry<String, Long> entry : sumPriceFinal.entrySet()) {
                if (!sellerIds.contains(entry.getKey()) && entry.getValue() <= 1000000) {
                    sellerIds.add(entry.getKey());
                }
            }
        }

        List<User> users = userRepository.find(new Query(new Criteria("_id").in(sellerIds).and("emailVerified").is(true).and("phoneVerified").is(true)));

        List<String> userIds = new ArrayList<>();
        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                if (!userIds.contains(user.getId())) {
                    userIds.add(user.getId());
                }
            }
        }
        if (userIds != null && !userIds.isEmpty()) {
            List<Shop> shops = shopRepository.find(new Query(new Criteria("_id").in(userIds)));
            for (Shop shop : shops) {
                for (User user : users) {
                    if (user.getId().equals(shop.getUserId())) {
                        if (shop.getEmail() == null || shop.getEmail().equals("")) {
                            shop.setEmail(user.getEmail() == null ? "" : user.getEmail());
                        }
                        if (shop.getPhone() == null || shop.getPhone().equals("")) {
                            shop.setPhone(user.getPhone() == null ? "" : user.getPhone());
                        }
                    }
                }

            }

            int i = 1;
            if (shops == null) {
            } else {
                for (Shop shop : shops) {
                    i++;
                    row = sheet.createRow((short) i);
                    row.createCell(0).setCellValue(String.valueOf(shop.getUserId()));
                    row.createCell(1).setCellValue(String.valueOf((shop.getAlias() == null) ? "" : shop.getAlias()));
                    row.createCell(2).setCellValue(String.valueOf((shop.getEmail() == null) ? "" : shop.getEmail()));
                    row.createCell(3).setCellValue(String.valueOf((shop.getPhone() == null) ? "" : shop.getPhone()));

                }
            }
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=exportOrder500k.xls");
        wb.write(response.getOutputStream());

        return;

    }
    /*
     @RequestMapping(value = "/runhotdeal", method = RequestMethod.GET)
     public void runHotdeal() {
     List<HotdealCategory> hotdealCategorys = hotdealCategoryRepository.find(new Query(new Criteria()));
     for (HotdealCategory hotdealCategory : hotdealCategorys) {
     if (hotdealCategory.getParentId() != null && !hotdealCategory.getParentId().equals("")) {
     List<HotdealCategory> categorys = hotdealCategoryRepository.find(new Query(new Criteria("parentId").is(hotdealCategory.getParentId())));
     if (categorys != null && !categorys.isEmpty()) {
     for (HotdealCategory category : categorys) {
     category.setPath(new ArrayList<String>());
     category.getPath().add(hotdealCategory.getParentId());
     category.getPath().add(category.getId());
     hotdealCategoryRepository.save(category);
     }
     }
     } else {
     hotdealCategory.setPath(new ArrayList<String>());
     hotdealCategory.getPath().add(hotdealCategory.getId());
     hotdealCategoryRepository.save(hotdealCategory);
     }
     }
     }
     */

    @RequestMapping(value = "/tester", method = RequestMethod.GET)
    public Response tester(@RequestParam(value = "email", defaultValue = "") String email) {
        List<User> users = userRepository.find(new Query(new Criteria("email").is(email)));
        return new Response(true, "Thông tin User", users);

    }

    @RequestMapping(value = "/excelsellernosc", method = RequestMethod.GET)
    public void excelsellernosc(HttpServletResponse response, @RequestParam(value = "m", required = true, defaultValue = "0") int m) throws IOException, Exception {
        Criteria criteria = new Criteria("scIntegrated").is(true);
        criteria.and("scEmail").ne(null);
        if (m > 0) {
            criteria.and("merchantKey").ne(null);
        } else {
            criteria.and("merchantKey").is(null);
        }
        List<String> ids = new ArrayList<String>();
        List<Seller> sellers = sellerRepository.find(new Query(criteria));
        if (m > 0) {
            for (Seller seller : sellers) {
                if (seller.getMerchantKey() != null && !seller.getMerchantKey().equals("")) {
                    String checkMerchantKey = scClientV2.checkMerchantKey(seller.getMerchantKey());
                    if (checkMerchantKey == null | !checkMerchantKey.equals("")) {
                        if (!ids.contains(seller.getUserId())) {
                            ids.add(seller.getUserId());
                        }
                    }

                }

            }
        }

        List<String> userIds = new ArrayList<>();
        if (sellers != null && !sellers.isEmpty()) {
            for (Seller user : sellers) {
                if (!userIds.contains(user.getUserId())) {
                    userIds.add(user.getUserId());
                }
            }
        }

        List<User> usersN = new ArrayList<User>();
        List<User> users = userRepository.find(new Query(new Criteria("_id").in(userIds)));
        List<Shop> shops = shopRepository.find(new Query(new Criteria("_id").in(userIds)));
        for (Shop shop : shops) {
            for (User user : users) {
                if (!user.getId().equals(shop.getUserId())) {
                    usersN.add(user);
                    break;
                }
            }

        }
        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Sheet-CDT");
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);
        row.createCell(0).setCellValue("UserID");
        row.createCell(1).setCellValue("EmailSC");
        row.createCell(2).setCellValue("Phone");
        int i = 1;
        if (usersN == null) {
        } else {
            for (User user : usersN) {
                for (Seller seller : sellers) {
                    if (user.getId().equals(seller.getUserId())) {
                        i++;
                        row = sheet.createRow((short) i);
                        row.createCell(0).setCellValue(String.valueOf(user.getId()));
                        row.createCell(1).setCellValue(String.valueOf((seller.getScEmail() == null) ? "" : seller.getScEmail()));
                        row.createCell(2).setCellValue(String.valueOf((user.getPhone() == null) ? "" : user.getPhone()));
                    }
                }
            }
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=danh-sach-NB-chua-mo-shop.xls");
        wb.write(response.getOutputStream());

        return;

    }

    @RequestMapping(value = "/excelbyleaf", method = RequestMethod.GET)
    public void excel(HttpServletResponse response) throws Exception {
        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("new sheet");
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);
        row.createCell(0).setCellValue("Cấp 1");
        row.createCell(1).setCellValue("Cấp 2");
        row.createCell(2).setCellValue("Cấp 3");
        row.createCell(3).setCellValue("Cấp 4");
        row.createCell(4).setCellValue("Cấp 5");

        int i = 1;
        List<Category> listAll = categoryService.getByLevelDisplay(1);
        for (Category print : listAll) {
            List<Category> categorys = categoryService.getDescendantsCate(print.getId());
            for (Category category : categorys) {

                i++;
                row = sheet.createRow((short) i);
                String path = "";
                for (String categoryId : print.getPath()) {
                    Category get = categoryService.get(categoryId);
                    path += get.getName() + " >>";
                }
                row.createCell(0).setCellValue(String.valueOf("[" + print.getId() + "]" + path));
                if (category.getLevel() == 2) {
                    path = "";
                    for (String categoryId : category.getPath()) {
                        Category get = categoryService.get(categoryId);
                        path += get.getName() + " >>";
                    }
                    row.createCell(1).setCellValue(String.valueOf("[" + category.getId() + "]" + path));
                }
                if (category.getLevel() == 3) {
                    path = "";
                    for (String categoryId : category.getPath()) {
                        Category get = categoryService.get(categoryId);
                        path += get.getName() + " >>";
                    }
                    row.createCell(2).setCellValue(String.valueOf("[" + category.getId() + "]" + path));
                }
                if (category.getLevel() == 4) {
                    path = "";
                    for (String categoryId : category.getPath()) {
                        Category get = categoryService.get(categoryId);
                        path += get.getName() + " >>";
                    }
                    row.createCell(3).setCellValue(String.valueOf("[" + category.getId() + "]" + path));
                }
                if (category.getLevel() == 5) {
                    path = "";
                    for (String categoryId : category.getPath()) {
                        Category get = categoryService.get(categoryId);
                        path += get.getName() + " >>";
                    }
                    row.createCell(4).setCellValue(String.valueOf("[" + category.getId() + "]" + path));
                }

            }
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=Danh-sach-danh-muc-cap-la.xls");
        wb.write(response.getOutputStream());
    }

    @RequestMapping(value = "/excelbuyeroncdt", method = RequestMethod.GET)
    public void excelbuyer(HttpServletResponse response, @RequestParam(value = "page", required = true, defaultValue = "0") int page) throws IOException, Exception {
        // Map<String, Long> dayofMonth = getDayofMonth(month, year);

        Criteria criteria = new Criteria();
        //criteria.and("finalPrice").lte(500000);
        //criteria.and("createTime").gte(dayofMonth.get("first")).lte(dayofMonth.get("last"));
        PageRequest pageRequest = new PageRequest(page, 20000);
        List<Order> orders = orderRepository.find(new Query(criteria).with(pageRequest));
        List<String> phones = new ArrayList<>();
        for (Order order : orders) {
            if (!phones.contains(order.getBuyerPhone())) {
                phones.add(order.getBuyerPhone());
            }
        }
        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Sheet-CDT");
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);
        row.createCell(0).setCellValue("Phone");
        int i = 1;
        for (String phone : phones) {
            i++;
            row = sheet.createRow((short) i);
            row.createCell(0).setCellValue(String.valueOf(phone));
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=danh-sach-nguoi-mua-tren-cdt-page-" + page + ".xls");
        wb.write(response.getOutputStream());

        return;

    }

    @RequestMapping(value = "/testcatemap", method = RequestMethod.GET)
    public Response testCateMap(@RequestParam(value = "email", defaultValue = "") String email) {
        try {
            return categoryMappingService.mapCate("437470", "606434652224", "914206649206", true, true, "Thuộc tính mới");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new Response(true, "Thông tin User", "123");

    }

    @RequestMapping(value = "/exportsellerchoseploi", method = RequestMethod.GET)
    public void exportsellerchoseploi(HttpServletResponse response) throws IOException, Exception {
        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Sheet-CDT");
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);
        row.createCell(0).setCellValue("UserId CĐT");
        row.createCell(1).setCellValue("EmailCDT");
        row.createCell(2).setCellValue("EmailSC");
        int i = 1;
        List<Seller> sellers = sellerRepository.find(new Query(new Criteria("scIntegrated").is(true).and("scEmail").ne(null).and("merchantKey").is(null)));
        List<String> userIds = new ArrayList<>();
        for (Seller seller : sellers) {
            if (!userIds.contains(seller.getUserId())) {
                userIds.add(seller.getUserId());
            }
        }
        List<User> users = userRepository.find(new Query(new Criteria("_id").in(userIds)));
        for (Seller seller : sellers) {
            for (User user : users) {
                if (user.getId().equals(seller.getUserId())) {
                    i++;
                    row = sheet.createRow(i);
                    row.createCell(0).setCellValue(seller.getUserId());
                    row.createCell(1).setCellValue(user.getEmail());
                    row.createCell(2).setCellValue(seller.getScEmail());
                }
            }
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=danh-sach-seller-no-sc.xls");
        wb.write(response.getOutputStream());
        return;

    }

}
