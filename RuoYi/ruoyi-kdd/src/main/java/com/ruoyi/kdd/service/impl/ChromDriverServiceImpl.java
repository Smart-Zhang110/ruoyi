package com.ruoyi.kdd.service.impl;

import com.ruoyi.kdd.entity.PrivateInfo;
import com.ruoyi.kdd.service.ChromDriverService;
import com.ruoyi.kdd.util.RsaDemo;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import static com.ruoyi.kdd.util.RsaDemo.*;


@Service
public class ChromDriverServiceImpl implements ChromDriverService {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    private static String algorithm = "RSA";
    static {
        System.setProperty("webdriver.chrome.driver", "C:\\soft\\chromedriver_win32\\chromedriver.exe");

    }

    public static void main(String[] args) throws Exception {
        test("2020-11-16 14:59:59");
    }

    public static void test(String date) throws Exception {
        boolean order = false;
        generateKeyToFile(algorithm, "a.pub", "a.pri");
        PublicKey publicKey = loadPublicKeyFromFile(algorithm, "a.pub");
        PrivateKey privateKey = loadPrivateKeyFromFile(algorithm, "a.pri");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.tmall.com/?ali_trackid=2:mm_26632258_3504122_55934697:1605497050_127_1752153210&union_lens=recoveryid:1605497050_127_1752153210&clk1=c0ea72f3d29cb374cd04824906810f2d&bxsign=tbkzMGAMJEEY/xIW+3Nxq1/l2J1Pt/c2HWHHXJbYLEn9rENPenX0cu6TG6YnaW6WbQaz1uM/4X4L5/SdRp3arB1cYLSNRF8GkMYdx7IvSF3CBM=");
        System.out.println(driver.getTitle());
        WebElement shop = driver.findElement(By.className("sn-login"));
        System.out.println(driver.getCurrentUrl());
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
        System.out.println(driver.getCurrentUrl());
//        Thread.sleep(1000);
        WebElement telphone = driver.findElement(By.id("fm-login-id"));
        telphone.sendKeys(PrivateInfo.yes);
        WebElement password = driver.findElement(By.name("fm-login-password"));
        password.sendKeys(encrypt(algorithm, PrivateInfo.no, privateKey, 20));
//        WebElement login = driver.findElement(By.linkText("登录"));
        //登录
        WebElement login = driver.findElement(By.cssSelector("div>button"));
//        WebElement login = driver.findElement(By.className("fm-button fm-submit password-login"));
//        driver.findElement(By.xpath("//*[@name='wd']"))
        login.click();
        driver.switchTo();
        Thread.sleep(5000);
        System.out.println(driver.getCurrentUrl());
        WebElement cart = driver.findElement(By.className("sn-cart-link"));
//        sn-cart-link
        cart.click();
        Thread.sleep(300);
        //全选
        WebElement allCbx1 = driver.findElement(By.id("J_SelectAllCbx1"));
        JsClick(driver,allCbx1);
        //结算

        while (!order){
            if(sdf.format(new Date()).contains(date)){
                WebElement j_go = driver.findElement(By.id("J_Go"));
                JsClick(driver,j_go);
                order = true;
            }
            System.out.println(sdf.format(new Date()));
        }
        WebElement commit = driver.findElement(By.linkText("提交订单"));
        commit.click();
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

    public static byte[] base64Encrypt(final String content) {
        return Base64.getEncoder().encode(content.getBytes());
    }

    public static byte[] base64Decrypt(final byte[] encoderContent) {
        return Base64.getDecoder().decode(encoderContent);
    }
}
