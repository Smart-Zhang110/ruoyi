package com.ruoyi.kdd.service.impl;

import com.ruoyi.kdd.entity.PrivateInfo;
import com.ruoyi.kdd.service.ChromDriverService;
import com.ruoyi.kdd.util.EncrypDES;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ChromDriverServiceImpl implements ChromDriverService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    static {
        System.setProperty("webdriver.chrome.driver", "C:\\soft\\chromedriver_win32\\chromedriver.exe");
    }
    public ChromDriverServiceImpl() throws Exception {
    }

    //执行Javascript脚本
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
    public synchronized int order(Map paramters) throws Exception {
        System.out.println("//首次下单时间  : "+paramters.get("date"));
        System.out.println("//备选下单时间  : "+paramters.get("bacdate"));
        System.out.println("//结束下单时间  : "+paramters.get("enddate"));
        boolean order = true;
        EncrypDES des = new EncrypDES();
        ChromeOptions option = new ChromeOptions();
        option.setExperimentalOption("useAutomationExtension", false);
        option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        WebDriver driver = new ChromeDriver(option);
//        driver.manage().window().maximize();
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
        telphone.sendKeys(des.decrypt(PrivateInfo.yes));
        WebElement password = driver.findElement(By.name("fm-login-password"));
        password.sendKeys(des.decrypt(PrivateInfo.no));
//        WebElement login = driver.findElement(By.linkText("登录"));

//        WebElement login = driver.findElement(By.linkText("登录"));
        //登录
        WebElement login = driver.findElement(By.cssSelector("div>button"));
//        WebElement login = driver.findElement(By.className("fm-button fm-submit password-login"));
//        driver.findElement(By.xpath("//*[@name='wd']"))
        try {
            WebElement block = driver.findElement(By.className("nc-lang-cnt"));
            Actions action = new Actions(driver);
            action.dragAndDropBy(block,0,300);
            action.release();
        }catch (Exception e){
            //滑块异常不处理
        }
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
        WebElement j_go = driver.findElement(By.id("J_Go"));
        int orderCount = 1,currentTimeCount = 0;
        while (order){
            if(sdf.format(new Date()).contains(paramters.get("date").toString())
            || sdf.format(new Date()).contains(paramters.get("bacdate").toString())){
                Thread.sleep(8);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JsClick(driver,j_go);
                        WebElement commit = driver.findElement(By.linkText("提交订单"));
                        commit.click();
                    }
                }).start();
                System.out.println("提交"+orderCount+++"次");
            }else{
                if(currentTimeCount % 100 == 0){
                    System.out.println("当前时间:"+sdf.format(new Date()));
                }
                currentTimeCount++;
            }
            if(sdf.format(new Date()).contains(paramters.get("enddate").toString())){
                order = false;
                System.out.println("结束");
            }
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {
        String[] tmparamters = args;
        Map paramters = new HashMap();
        if(tmparamters.length > 2){
            //首次下单时间  计算网络延时时间
            paramters.put("date",tmparamters[0]);
            //备选下单时间
            paramters.put("bacdate",tmparamters[1]);
            //结束下单时间
            paramters.put("enddate",tmparamters[2]);
        }else{
            //首次下单时间  计算网络延时时间
            paramters.put("date","19:59:59.999");
            //备选下单时间
            paramters.put("bacdate","20:00:00");
            //结束下单时间
            paramters.put("enddate","20:01:00");
        }

        if (args.length > 3){
            int index = Integer.parseInt(args[3]);
            while (index > 0){
                execute(paramters);
                index--;
            }
        }else {
            execute(paramters);
        }
    }

    public static void execute(Map paramters) throws InterruptedException {
        chromExecute chromExecute = new chromExecute();
        chromExecute.setParamters(paramters);
        Thread thread = new Thread(chromExecute);
        thread.start();
        Thread.sleep(2500);
    }
}

