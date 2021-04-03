package moe.pingu.demo.httpbodydigest.advice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.input.TeeInputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

@ControllerAdvice
public class DigestBodyAdvice extends RequestBodyAdviceAdapter {

  private Logger logger = LoggerFactory.getLogger(DigestBodyAdvice.class);

  @Override
  public boolean supports(MethodParameter parameter, Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  private ByteArrayOutputStream cc;

  @Override
  public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

    cc = new ByteArrayOutputStream();
    InputStream wrapped = new TeeInputStream(inputMessage.getBody(), cc, true);

    return super.beforeBodyRead(new HttpInputMessage() {
      public InputStream getBody() throws IOException {
        return wrapped;
      }

      public HttpHeaders getHeaders() {
        return inputMessage.getHeaders();
      }
    }, parameter, targetType, converterType);
  }

  @Override
  public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {

    String sha256Calc = DigestUtils.sha256Hex(cc.toByteArray());
    String sha256Prov = inputMessage.getHeaders().getFirst("X-MESSAGE-DIGEST");
    logger.info("===digest result===\ncalc: {}\nprov: {}", sha256Calc, sha256Prov);

    if (!StringUtils.equalsIgnoreCase(sha256Calc, sha256Prov))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DIGEST MISMATCH");
    return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
  }

}
