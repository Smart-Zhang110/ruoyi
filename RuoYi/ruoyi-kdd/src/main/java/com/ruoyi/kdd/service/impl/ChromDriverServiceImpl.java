package com.ruoyi.kdd.service.impl;

import com.ruoyi.kdd.entity.PrivateInfo;
import com.ruoyi.kdd.service.ChromDriverService;
import com.ruoyi.kdd.util.EncrypDES;
import com.ruoyi.kdd.util.RsaDemo;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;


@Service
public class ChromDriverServiceImpl implements ChromDriverService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    static {
        System.setProperty("webdriver.chrome.driver", "C:\\soft\\chromedriver_win32\\chromedriver.exe");
    }
    public ChromDriverServiceImpl() throws Exception {
    }

    public static JavascriptExecutor JsClick(WebDriver driver,WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int order(Map paramters) throws Exception {
        boolean order = false;
        EncrypDES des = new EncrypDES();
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.tmall.com/?ali_trackid=2:mm_26632258_3504122_55934697:1605497050_127_1752153210&union_lens=recoveryid:1605497050_127_1752153210&clk1=c0ea72f3d29cb374cd04824906810f2d&bxsign=tbkzMGAMJEEY/xIW+3Nxq1/l2J1Pt/c2HWHHXJbYLEn9rENPenX0cu6TG6YnaW6WbQaz1uM/4X4L5/SdRp3arB1cYLSNRF8GkMYdx7IvSF3CBM=");
        logger.info(driver.getTitle());
        WebElement shop = driver.findElement(By.className("sn-login"));
        logger.info(driver.getCurrentUrl());
        shop.click();
        //获得所有窗口句柄
        //获得当前窗口句柄
//        String search_handle = driver.getWindowHandle();
        //页面中包含的frame
        driver.switchTo().frame("J_loginIframe");
//        Set<String> handles = driver.getWindowHandles();
//        WebElement telphone = driver.findElement(By.xpath("//*[@placeholder='会员名/邮箱/手机号']"));
//        telphone.sendKeys("17314999951");
//        driver.switchTo().window(handles.iterator().next());
        logger.info(driver.getCurrentUrl());
//        Thread.sleep(1000);
        WebElement telphone = driver.findElement(By.id("fm-login-id"));
        telphone.sendKeys(des.decrypt(PrivateInfo.yes));
        WebElement password = driver.findElement(By.name("fm-login-password"));
        password.sendKeys(des.decrypt(PrivateInfo.no));
//        WebElement login = driver.findElement(By.linkText("登录"));
        //登录
        WebElement login = driver.findElement(By.cssSelector("div>button"));
//        WebElement login = driver.findElement(By.className("fm-button fm-submit password-login"));
//        driver.findElement(By.xpath("//*[@name='wd']"))
        login.click();
        driver.switchTo();
        Thread.sleep(5000);
        logger.info(driver.getCurrentUrl());
        WebElement cart = driver.findElement(By.className("sn-cart-link"));
//        sn-cart-link
        cart.click();
        Thread.sleep(300);
        //全选
        WebElement allCbx1 = driver.findElement(By.id("J_SelectAllCbx1"));
        JsClick(driver,allCbx1);
        //结算
        WebElement j_go = driver.findElement(By.id("J_Go"));
        while (!order){
            if(sdf.format(new Date()).contains(paramters.get("date").toString())){
                JsClick(driver,j_go);
                order = true;
            }
            logger.info(sdf.format(new Date()));
        }
        WebElement commit = driver.findElement(By.linkText("提交订单"));
        commit.click();
        return 0;
    }
}
