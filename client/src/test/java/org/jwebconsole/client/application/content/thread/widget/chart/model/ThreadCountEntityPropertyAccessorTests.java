package org.jwebconsole.client.application.content.thread.widget.chart.model;

import com.google.gwt.editor.client.Editor;
import org.junit.Test;
import org.jwebconsole.client.application.content.thread.widget.chart.model.ThreadCountEntityPropertyAccessor;
import org.jwebconsole.client.model.thread.ThreadCountEntity;
import org.mockito.Mockito;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

public class ThreadCountEntityPropertyAccessorTests extends Mockito {


    @Test
    public void shouldHavePropertiesNamedLikeThreadCountEntity() {
        for (Method method : ThreadCountEntityPropertyAccessor.class.getDeclaredMethods()) {
            if (!methodIsKeyProvider(method)) {
                assertTrue(method.getName(), fieldExists(method.getName()));
            }
        }
    }

    private boolean fieldExists(String methodName) {
        for (Field field : ThreadCountEntity.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }


    private boolean methodIsKeyProvider(Method method) {
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            if (annotation.annotationType().equals(Editor.Path.class)) {
                return true;
            }
        }
        return false;
    }

}
