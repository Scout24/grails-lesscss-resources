import org.openqa.selenium.WebDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.phantomjs.PhantomJSDriverService
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.Proxy

driver = {
  createPhantomJsDriver()
}

environments {
}

WebDriver createPhantomJsDriver() {
  def proxy = new Proxy(httpProxy: System.getenv('http_proxy'), noProxy: System.getenv('no_proxy'));
  def capabilities = new DesiredCapabilities();
  capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
  capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, findPhantomJs());
  capabilities.setCapability(CapabilityType.PROXY, proxy)

  new PhantomJSDriver(capabilities)
}

String findPhantomJs() {
  [
      '/data/is24/phantomjs/bin/phantomjs',
      '/usr/bin/phantomjs',
      '/usr/local/bin/phantomjs'
  ].find { new File(it).exists() }
}