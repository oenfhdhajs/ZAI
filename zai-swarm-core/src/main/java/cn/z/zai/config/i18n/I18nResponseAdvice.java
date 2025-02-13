/*
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.z.zai.config.i18n;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
//@RestControllerAdvice
public class I18nResponseAdvice implements ResponseBodyAdvice<Object> {

	@Value("${spring.profiles.active}")
	private String env;

	private static final ReflectionUtils.FieldFilter WRITEABLE_FIELDS = (field -> !(Modifier
		.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())));

	@Autowired
	private MessageSource messageSource;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		AnnotatedElement annotatedElement = returnType.getAnnotatedElement();
		I18nIgnore i18nIgnore = AnnotationUtils.findAnnotation(annotatedElement, I18nIgnore.class);
		return i18nIgnore == null;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		try {
			switchLanguage(body);
		} catch (Exception ex) {
			log.error("i18n response process error: {}", body,ex);
		}
		return body;
	}

	public void switchLanguage(Object source) {
		if (source == null) {
			return;
		}
		Class<?> sourceClass = source.getClass();
		I18nClass i18nClass = sourceClass.getAnnotation(I18nClass.class);
		if (i18nClass == null) {
			return;
		}
		ReflectionUtils.doWithFields(sourceClass, (field) -> {
			ReflectionUtils.makeAccessible(field);
			Class<?> fieldType = field.getType();
			Object fieldValue = ReflectionUtils.getField(field, source);

			if (fieldValue instanceof String) {
				I18nField i18nField = field.getAnnotation(I18nField.class);
				if (i18nField == null) {
					return;
				}
				Locale locale = LocaleContextHolder.getLocale();
				String code = StringUtils.isNotEmpty(i18nField.i18nCode()) ? i18nField.i18nCode() : (String) fieldValue;
				String message = codeToMessage(code, locale, (String) fieldValue);
				ReflectionUtils.setField(field, source, message);
			} else if (fieldValue instanceof Collection) {
				@SuppressWarnings("unchecked")
				Collection<Object> elements = (Collection<Object>) fieldValue;
				if (CollectionUtils.isEmpty(elements)) {
					return;
				}
				for (Object element : elements) {
					switchLanguage(element);
				}
			} else if (fieldType.isArray()) {
				Object[] elements = (Object[]) fieldValue;
				if (elements == null || elements.length == 0) {
					return;
				}
				for (Object element : elements) {
					switchLanguage(element);
				}
			} else {
				switchLanguage(fieldValue);
			}
		}, WRITEABLE_FIELDS);
	}

	private String codeToMessage(String code, Locale locale, String defaultMessage) {
		String message;
		try {
			message = this.messageSource.getMessage(code, null, locale);
			return message;
		} catch (NoSuchMessageException e) {
			if (env.equals("sit")) {
				log.warn("i18n miss config, code: {}, local: {}", code, locale);
			}
		}
		return defaultMessage;
	}

}
