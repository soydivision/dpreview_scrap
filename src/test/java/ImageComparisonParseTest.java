import com.codeborne.selenide.*;
import org.testng.annotations.Test;

import java.io.*;
import java.net.URL;

import static com.codeborne.selenide.Selenide.open;
import static org.apache.commons.io.FileUtils.copyURLToFile;

public class ImageComparisonParseTest {

    String HOME_URL = "https://www.dpreview.com/reviews/image-comparison";
    String MAIN_URL = "https://www.dpreview.com";
    String PROJECT_DIR = System.getProperty("user.dir");
    String LIST_OF_CAMERAS_FILE_NAME = "CAMERA LIST";

    int TIMEOUT_DOWNLOAD_FILE = 30000; // in ms
    int TIMEOUT_INITIAL_PAGE_LOAD = 10000;
    int TIMEOUT_MEDIUM = 5000; //

    ElementsCollection cameraList = Selenide.elements(Selectors.byXpath("//option"));
    SelenideElement dropDownButton = Selenide.element(Selectors.byXpath("//*[(@class=\"imageAttrInput\")]"));
    SelenideElement downloadLink = Selenide.element(Selectors.byXpath("//*[@class=\"downloadLinks\"]/a"));

    @Test
    public void test() throws IOException {
        Configuration.browser = "firefox";
        Configuration.browserSize = "1920x1080";
        open(HOME_URL);
        Selenide.sleep(TIMEOUT_INITIAL_PAGE_LOAD);
        dropDownButton.click();
        Selenide.sleep(TIMEOUT_MEDIUM);

        for (int i = 0; i < 1; i++) {
            //for (int i = 0; i < cameraList.size(); i++) {
            String name = cameraList.get(i).innerText();
            clickByCameraName(cameraList.get(i).innerText());
            var src = downloadLink.getAttribute("href");
            var imgFile = new File("test-result/images/" + name);
            copyURLToFile(new URL(src), imgFile);
        }
    }

    public void clickByCameraName(String text) {
        Selenide.element(Selectors.byText(text)).click();
    }

    public void appendToTxtFile(String txt) throws FileNotFoundException, UnsupportedEncodingException {
        try {
            String filename = "test-result/" + LIST_OF_CAMERAS_FILE_NAME + ".txt";
            FileWriter fw = new FileWriter(filename, true); //the true will append the new data
            fw.write(txt);
            fw.write("\n");
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
}
