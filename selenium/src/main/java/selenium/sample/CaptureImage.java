package selenium.sample;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.bytebuddy.asm.Advice.This;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class CaptureImage {
    private static final String FILE_DATE_FORMAT = "yyyyMMddHHmmss";

    private static Logger logger = LoggerFactory.getLogger(This.class);

    /**
     * 起動メソッド.
     * @param args 引数
     */
    public static void main(final String[] args) {
        String storageFolder = "c:\\temp\\";
        ChromeDriver driver = null;

        // choromeバージョンに合わせて、chromedriverを設定。※今回は74
        // drivrダウンロードサイト→http://chromedriver.chromium.org/downloads
        System.setProperty("webdriver.chrome.driver", "driver/74chromedriver.exe");

        try {
            driver = new ChromeDriver();

            // googleから"selenium java"を検索
            driver.navigate().to("https://www.google.com/");
            driver.findElement(By.name("q")).sendKeys("selenium java");
            driver.findElement(By.name("q")).submit();
            
            // 検索結果の先頭を選択し、画面キャプチャを取得
            driver.findElement(By.xpath("//a/h3[1]")).click();
            driver.manage().window().maximize();
            Thread.sleep(1000);
            saveFullCapture(driver, storageFolder, "capture", true);
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            // ブラウザ閉じる
            if (driver != null) {
                driver.close();
                driver = null;
            }
        }
    }

    /**
     * 画面全体のキャプチャ保存.
     * @param webDriver chrome操作用ドライバー
     * @param storageFolder 保存先ディレクトリ
     * @param fileName ファイル名
     * @param addTimeFlg ファイル名の末尾に時間を付けるかどうかのフラグ（true:付与、false:付与しない）
     * @throws IOException
     */
    public static void saveFullCapture(final ChromeDriver webDriver, final String storageFolder, String fileName,
            final Boolean addTimeFlg) throws IOException {
        // ページ全体を撮影。
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(webDriver);

        // 保管したイメージを任意の場所に書き込む
        if (addTimeFlg) {
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat(FILE_DATE_FORMAT);
            String strDate = dateFormat.format(date);
            fileName = fileName + strDate;
        }
        ImageIO.write(screenshot.getImage(), "PNG", new File(storageFolder + "/" + fileName + ".png"));
    }
}
