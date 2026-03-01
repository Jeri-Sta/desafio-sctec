package br.com.starosky.entrepreneur;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;

class EntrepreneurApplicationUnitTest {

    @Test
    void class_ShouldBeAnnotatedWithSpringBootApplication() {
        assertNotNull(EntrepreneurApplication.class.getAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    void class_ShouldBePublic() {
        assertTrue(Modifier.isPublic(EntrepreneurApplication.class.getModifiers()));
    }

    @Test
    void class_ShouldHaveMainMethod() throws NoSuchMethodException {
        var mainMethod = EntrepreneurApplication.class.getMethod("main", String[].class);
        assertNotNull(mainMethod);
        assertTrue(Modifier.isStatic(mainMethod.getModifiers()));
        assertEquals(void.class, mainMethod.getReturnType());
    }

    @Test
    void mainMethod_ShouldExist() {
        assertDoesNotThrow(() -> {
            EntrepreneurApplication.class.getMethod("main", String[].class);
        });
    }

    @Test
    void class_ShouldBeInCorrectPackage() {
        assertEquals("br.com.starosky.entrepreneur", EntrepreneurApplication.class.getPackageName());
    }
}
