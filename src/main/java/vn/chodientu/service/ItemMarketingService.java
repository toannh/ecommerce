package vn.chodientu.service;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import vn.chodientu.component.imboclient.Url.ImageUrl;
import vn.chodientu.entity.db.Category;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.ItemDetail;
import vn.chodientu.entity.db.ItemMarketing;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.repository.ItemMarketingRepository;
import vn.chodientu.util.TextUtils;
import vn.chodientu.util.UrlUtils;

/**
 *
 * @author Anhpp
 */
@Service
public class ItemMarketingService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ItemMarketingRepository itemMarketingRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ImageService imageService;

    //@Scheduled(fixedDelay = 60000)
    public void process() throws Exception {
        String categoryId = "";
        String cateId = "2924";
        if (cateId != null && !cateId.equals("")) {
            categoryId = cateId;
        } else {
            return;
        }
        Category category = categoryService.get(categoryId);
        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setCategoryIds(new ArrayList<String>());
        itemSearch.getCategoryIds().add(categoryId);
        itemSearch.setStatus(1);
        itemSearch.setPageSize(60);
        itemSearch.setPageIndex(0);
        DataPage<Item> itemPage = itemService.search(itemSearch);
        int i = 1;
        while (itemPage.getPageIndex() < itemPage.getPageCount() + 1) {
            if (itemPage.getData() == null) {
            } else {
                for (Item item : itemPage.getData()) {
                    i++;
                    //get Item Image URL 
                    List<String> images = new ArrayList<>();
                    if (item.getImages() != null && !item.getImages().isEmpty()) {
                        for (String img : item.getImages()) {
                            ImageUrl bigImg = imageService.getUrl(img);
                            ImageUrl smallImg = imageService.getUrl(img).thumbnail(350, 350, "inset");
                            if (bigImg != null && smallImg != null) {
                                images.add(bigImg.compress(100).getUrl("big " + item.getName()));
                                images.add(smallImg.compress(100).getUrl("smaill " + item.getName()));
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
                    ItemMarketing im = new ItemMarketing();

                    if (itemDetail != null) {
                        if (itemDetail.getDetail() != null) {
                            String detail = removeImageUrl(itemDetail.getDetail());
                            if (detail.length() > 30000) {
                                detail = detail.substring(0, 30000);

                                im.setDescription(detail);
                            }
                            im.setDescription(detail);
                        }
                    }
                    if (item.getImages() != null) {
                        if (item.getImages().size() > 0) {
                            im.setImage(item.getImages().get(0));
                        }
                    }
                    if (item.getImages() != null) {
                        if (item.getImages().size() > 0) {
                            im.setSmallimage(item.getImages().get(1));
                        }
                    }
                    im.setPrice(sellPrice);
                    if (cate1 != null && !cate1.equals("")) {
                        im.setCategory(cate1);
                    } else {
                        im.setCategory("");
                    }
                    if (cate2 != null && !cate2.equals("")) {
                        im.setCategory2(cate2);
                    } else {
                        im.setCategory2("");
                    }
                    if (cate3 != null && !cate3.equals("")) {
                        im.setCategory3(cate3);
                    } else {
                        im.setCategory3("");
                    }
                    im.setSprice(price);
                    im.setDiscount(discount);
                    im.setName(item.getName());
                    im.setId(item.getId());
                    im.setUrl("http://chodientu.vn" + itemUrl);
                    itemMarketingRepository.save(im);
                }

            }
            itemSearch.setPageIndex(itemPage.getPageIndex() + 1);
            itemPage = itemService.search(itemSearch);
        }
    }

    public List<ItemMarketing> getAll() {
        return itemMarketingRepository.getAll();
    }

    public List<ItemMarketing> getSearch(int pageIndex, int pageSize) {
        return itemMarketingRepository.search(pageIndex, pageSize);
    }

    public String removeImageUrl(String detailString) {
        String regex = "<img([\\w\\W]+?)/>";
        Pattern pattUrl = Pattern.compile(regex);
        Matcher mUrl = pattUrl.matcher(detailString);
        StringBuffer sb = new StringBuffer(detailString.length());
        while (mUrl.find()) {
            String text = mUrl.group();
            String image = text.substring(5, text.lastIndexOf("\""));

            mUrl.appendReplacement(sb, Matcher.quoteReplacement(""));
        }
        mUrl.appendTail(sb);
        return sb.toString();
    }

//    @Scheduled(cron = "0 30 23 * * ?")//5 giờ hàng tháng
//    @Scheduled(fixedDelay = 120000)
    public void exportXML() throws Exception {
        int pageSize = 5000000;
        int page = 0;
        List<ItemMarketing> itemMarketings = getSearch(page, pageSize);
        
        int i = 1;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("products");
        doc.appendChild(rootElement);
        for (ItemMarketing im : itemMarketings) {

            Element product = doc.createElement("product");
            rootElement.appendChild(product);
            Element id = doc.createElement("ProductID");
            id.appendChild(doc.createTextNode(im.getId()));
            product.appendChild(id);
            Element name = doc.createElement("Name");
            name.appendChild(doc.createTextNode(im.getName()));
            product.appendChild(name);
            Element url = doc.createElement("URL");
            url.appendChild(doc.createTextNode(im.getUrl()));
            product.appendChild(url);
            Element bigImage = doc.createElement("BigImage");
            bigImage.appendChild(doc.createTextNode(im.getImage()));
            product.appendChild(bigImage);
            Element smallImage = doc.createElement("SmallImage");
            smallImage.appendChild(doc.createTextNode(im.getSmallimage()));
            product.appendChild(smallImage);
            Element price = doc.createElement("Price");
            price.appendChild(doc.createTextNode(im.getPrice()));
            product.appendChild(price);
            Element cate1 = doc.createElement("Category1");
            cate1.appendChild(doc.createTextNode(im.getCategory()));
            product.appendChild(cate1);
            Element cate2 = doc.createElement("Category2");
            cate2.appendChild(doc.createTextNode(im.getCategory2()));
            product.appendChild(cate2);
            Element cate3 = doc.createElement("Category3");
            cate3.appendChild(doc.createTextNode(im.getCategory3()));
            product.appendChild(cate3);
            Element des = doc.createElement("Description");
            des.appendChild(doc.createTextNode(im.getDescription()));
            product.appendChild(des);
            Element reta = doc.createElement("RetailPrice");
            if (im.getDiscount().equals("") || im.getDiscount().equals("0")) {
                reta.appendChild(doc.createTextNode(im.getPrice()));
            } else {
                reta.appendChild(doc.createTextNode(im.getSprice()));
            }

            product.appendChild(reta);
            Element dis = doc.createElement("Discount");
            dis.appendChild(doc.createTextNode(im.getDiscount()));
            product.appendChild(dis);
        }
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        String url = "/var/tmp";//System.getProperty("java.io.tmpdir");
        
        File file = new File(url + "/products.xml");
        System.out.println("URL ******************* "+url);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
        startFTP();
        file.deleteOnExit();

    }

    public boolean startFTP() {
        String fileToFTP = "products.xml";
        //Properties props = new Properties();
        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {

            //props.load(new FileInputStream(propertiesFilename));
            String serverAddress = "222.255.28.247";
            String userId = "dev";
            String password = "GHGyb2ttRvtkGx4c";
            String remoteDirectory = "file/";
        //String server = "10.0.0.247";
            //check if the file exists
            String url = "/var/tmp";//System.getProperty("java.io.tmpdir");
            String filepath = url + "/" + fileToFTP;
            File file = new File(filepath);
            if (!file.exists()) {
                throw new RuntimeException("Error. Local file not found");
            }

            //Initializes the file manager
            manager.init();

            //Setup our SFTP configuration
            FileSystemOptions opts = new FileSystemOptions();
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
                    opts, "no");
            SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
            SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

            //Create the SFTP URI using the host name, userid, password,  remote path and file name
            String sftpUri = "sftp://" + userId + ":" + password + "@" + serverAddress + "/"
                    + remoteDirectory + fileToFTP;

            // Create local file object
            FileObject localFile = manager.resolveFile(file.getAbsolutePath());

            // Create remote file object
            FileObject remoteFile = manager.resolveFile(sftpUri, opts);

            // Copy local file to sftp server
            remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
            System.out.println("File upload successful");

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            manager.close();
        }

        return true;
    }
}
