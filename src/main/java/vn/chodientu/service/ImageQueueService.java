/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.chodientu.entity.db.ImageQueue;
import vn.chodientu.entity.db.ItemCrawlLog;
import vn.chodientu.entity.enu.CrawlImageStatus;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.ImageQueueRepository;
import vn.chodientu.repository.ItemCrawlLogRepository;
import vn.chodientu.util.UrlUtils;

/**
 *
 * @author CANH
 */
@Service
public class ImageQueueService {

    @Autowired
    ImageQueueRepository imageQueueRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ItemCrawlLogRepository crawlLogRepository;

    @Scheduled(fixedDelay = 60 * 1000)
    public void downloadImage() {
        while (true) {
            ImageQueue imageQueue = imageQueueRepository.getForDownload();
            if (imageQueue == null) {
                break;
            }
            imageQueue.setLock(1);
            imageQueueRepository.save(imageQueue);
            imageQueue.setRun(1);
            String image = imageQueue.getUrl();
            ItemCrawlLog crawlLog;
            if (imageQueue.getTime() > 0) {
                crawlLog = crawlLogRepository.findByItemIdAndTime(imageQueue.getTargetId(), imageQueue.getTime());
            } else {
                crawlLog = crawlLogRepository.findByItemId(imageQueue.getTargetId());
            }
            if (image != null && !image.equals("")) {
                if (imageService.checkImage(UrlUtils.convertURLToASCII(image.trim()), null, 300, 300, false)) {
                    Response<String> resp = imageService.downloadImageCrawl(UrlUtils.convertURLToASCII(image.trim()), ImageType.ITEM, imageQueue.getTargetId());
                    if (resp == null || !resp.isSuccess()) {
                        imageQueue.setDone(0);
                        if (crawlLog != null) {
                            crawlLog.setImageStatus(CrawlImageStatus.DOWNLOAD_FAIL);
                        }
                    } else {
                        imageQueue.setDone(1);
                        if (crawlLog != null) {
                            crawlLog.setImageStatus(CrawlImageStatus.DOWNLOAD_SUCCESS);
                        }
                    }
                } else {
                    imageQueue.setDone(2);
                    if (crawlLog != null) {
                        crawlLog.setImageStatus(CrawlImageStatus.SMALL_IMAGE);
                    }
                }
            }
            if (crawlLog != null) {
                crawlLogRepository.save(crawlLog);
            }
            imageQueue.setLock(0);
            imageQueueRepository.save(imageQueue);
        }
    }
    
    public ImageQueue findByItemAndUrl(String itemId, String url){
        return imageQueueRepository.findByItemAndUrl(itemId, url);
    }

    public void save(ImageQueue imageQueue) {
        imageQueueRepository.save(imageQueue);
    }
}
