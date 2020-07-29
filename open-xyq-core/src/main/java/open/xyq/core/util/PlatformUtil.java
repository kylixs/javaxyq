package open.xyq.core.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.net.URI;

@Slf4j
public class PlatformUtil {

    @SneakyThrows
    public static void openUrl(String url) {
        if (!Desktop.isDesktopSupported()) {
            log.error("platform doesn't support launcher browser");
            return;
        }
        Desktop.getDesktop().browse(new URI(url));
    }
}
